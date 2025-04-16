package com.ke.bella.openapi.protocol.completion.callback;

import com.ke.bella.openapi.EndpointProcessData;
import com.ke.bella.openapi.apikey.ApikeyInfo;
import com.ke.bella.openapi.protocol.completion.Callbacks;
import com.ke.bella.openapi.protocol.completion.CompletionProperty;
import com.ke.bella.openapi.protocol.log.EndpointLogger;
import com.ke.bella.openapi.safety.ISafetyCheckService;
import com.ke.bella.openapi.safety.SafetyCheckRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class StreamCallbackProvider {
    public static Callbacks.StreamCompletionCallback provide(SseEmitter sse, EndpointProcessData processData, ApikeyInfo apikeyInfo,
            EndpointLogger logger, ISafetyCheckService<SafetyCheckRequest.Chat> safetyService, CompletionProperty property) {
        Callbacks.StreamCompletionCallbackNode root = new SplitReasoningCallback(property);
        root.addLast(new ToolCallSimulatorCallback(processData));
        root.addLast(new MergeReasoningCallback(property));
        root.addLast(new StreamCompletionCallback(sse, processData, apikeyInfo, logger, safetyService));
        return root;
    }
}
