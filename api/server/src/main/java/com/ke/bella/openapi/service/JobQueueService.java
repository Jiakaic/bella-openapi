package com.ke.bella.openapi.service;

import java.util.ArrayList;
import java.util.List;

import com.ke.bella.openapi.protocol.BellaStreamCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ke.bella.openapi.common.exception.BizParamCheckException;
import com.ke.bella.openapi.protocol.BellaEventSourceListener;
import com.ke.bella.openapi.protocol.Callbacks;
import com.ke.bella.openapi.protocol.queue.QueueRequest.QueueTaskGetResultReq;
import com.ke.bella.openapi.protocol.queue.QueueRequest.QueueTaskPutReq;
import com.ke.bella.openapi.protocol.queue.QueueResponse.DetailData;
import com.ke.bella.openapi.protocol.queue.QueueResponse.QueueTaskGetResultResp;
import com.ke.bella.openapi.protocol.queue.QueueResponse.QueueTaskPutResp;
import com.ke.bella.openapi.protocol.queue.QueueResponse.TaskGetDetailResp;
import com.ke.bella.openapi.utils.HttpUtils;
import com.ke.bella.openapi.utils.JacksonUtils;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component
public class JobQueueService implements Callbacks.RealTimeTaskCallback, Callbacks.StreamTaskCallback {
    @Value("${queue.put.task.url:#{null}}")
    String putTaskUrl;
    @Value("${queue.get.task.result.url:#{null}}")
    String getTaskResultUrl;
    @Value("${bella.file.url:#{null}}")
    String fileUrl;
    @Value("${queue.default.timeout:300}")
    Integer defaultTimeout;

    @Override
    public <T> T putRealTimeTask(Object task, String endpoint, String queueName, String apikey, int timeout, Class<T> clazz, Callbacks.ChannelErrorCallback<T> errorCallback) {
        Request request = getRequest(task, endpoint, queueName, apikey, timeout);
        return HttpUtils.httpRequest(request, clazz, errorCallback, timeout, timeout);
    }

    @Override
    public int defaultTimeout() {
        return defaultTimeout;
    }

    @Override
    public void putStreamTask(Object task, String endpoint, String queueName, String apikey, int timeout, BellaEventSourceListener listener) {
        Request request = getRequest(task, endpoint, queueName, apikey, timeout);
        HttpUtils.streamRequest(request, listener, timeout, timeout);
    }

    @Override
    public void putStreamTask(Object task, String endpoint, String queueName, String apikey, int timeout, BellaStreamCallback listener) {
        Request request = getRequest(task, endpoint, queueName, apikey, timeout);
        HttpUtils.streamRequest(request, listener, timeout, timeout);
    }

    public QueueTaskPutResp putTask(Object task, String endpoint, String queueName, String apikey) {
        Request request = getRequest(task, endpoint, queueName, apikey, null);
        return HttpUtils.httpRequest(request, QueueTaskPutResp.class);
    }

    private Request getRequest(Object task, String endpoint, String queueName, String apikey, Integer timeout) {
        String url = putTaskUrl;
        if (url == null) {
            throw new IllegalStateException("Queue service URL 'queue.put.task.url' is not configured.");
        }
        if(endpoint == null || queueName == null) {
            throw new BizParamCheckException("invalid params");
        }
        QueueTaskPutReq req = QueueTaskPutReq.builder()
                .data(task)
                .endpoint(endpoint)
                .model(queueName)
                .build();
        if(timeout != null) {
            req.setTimeout(timeout);
        }
        return new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + apikey)
                .post(RequestBody.create(MediaType.parse("application/json"), JacksonUtils.serialize(req)))
                .build();
    }

    public QueueTaskGetResultResp getTaskResult(List<String> taskIds, String apikey) {
        String url = getTaskResultUrl;
        List<Object> result = new ArrayList<>();
        if (url == null) {
            throw new IllegalStateException("Queue service URL 'queue.get.task.result.url' is not configured.");
        }

        for (String taskId : taskIds) {
            QueueTaskGetResultReq req = QueueTaskGetResultReq.builder()
                    .taskId(taskId)
                    .build();
            Request request = new Request.Builder().url(url)
                    .addHeader("Authorization", "Bearer " + apikey)
                    .post(RequestBody.create(MediaType.parse("application/json"), JacksonUtils.serialize(req)))
                    .build();
            // todo queue batch get
            TaskGetDetailResp taskGetDetailResp = HttpUtils.httpRequest(request, TaskGetDetailResp.class);
            DetailData data = taskGetDetailResp.getData();
            if (data == null) {
                continue;
            }
            Object outputData = data.getOutputData();
            String outputFileId = data.getOutputFileId();
            if (outputData != null) {
                result.add(outputData);
            } else if (outputFileId != null && !outputFileId.isEmpty()) {
                Request requestFile = new Request.Builder()
                        .url(fileUrl + "/" + outputFileId + "/content")
                        .addHeader("Authorization", "Bearer " + apikey)
                        .get()
                        .build();
                outputData = HttpUtils.httpRequest(requestFile, Object.class);
                result.add(outputData);
            }
        }

        return QueueTaskGetResultResp.builder()
                .data(result)
                .build();
    }
}
