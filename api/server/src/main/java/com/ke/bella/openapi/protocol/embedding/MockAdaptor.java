package com.ke.bella.openapi.protocol.embedding;

import com.ke.bella.openapi.exception.BizParamCheckException;
import org.springframework.stereotype.Component;

@Component("mockEmbedding")
public class MockAdaptor implements EmbeddingAdaptor<EmbeddingProperty> {
    @Override
    public Class<?> getPropertyClass() {
        return EmbeddingAdaptor.class;
    }

    @Override
    public EmbeddingResponse embedding(EmbeddingRequest request, String url, EmbeddingProperty property) {
        throw new BizParamCheckException("尚未支持embedding mock");
    }
}
