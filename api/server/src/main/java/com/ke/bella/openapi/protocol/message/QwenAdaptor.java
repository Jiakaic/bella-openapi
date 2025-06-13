package com.ke.bella.openapi.protocol.message;

import com.ke.bella.openapi.protocol.completion.CompletionAdaptor;
import com.ke.bella.openapi.protocol.completion.OpenAIProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("QwenMessage")
public class QwenAdaptor implements MessageAdaptor<OpenAIProperty>  {
    @Autowired
    private com.ke.bella.openapi.protocol.completion.QwenAdaptor delegator;
    @Override
    public CompletionAdaptor<OpenAIProperty> delegator() {
        return delegator;
    }

    @Override
    public boolean isNativeSupport() {
        return false;
    }

    @Override
    public String getDescription() {
        return "通义千问扩展Openai协议模型服务适配/v1/message能力点";
    }
}
