package com.ke.bella.openapi.protocol;

import com.ke.bella.openapi.IDescription;
import com.ke.bella.openapi.protocol.asr.flash.FlashAsrPriceInfo;
import com.ke.bella.openapi.protocol.asr.realtime.RealTimeAsrPriceInfo;
import com.ke.bella.openapi.protocol.completion.CompletionPriceInfo;
import com.ke.bella.openapi.protocol.embedding.EmbeddingPriceInfo;
import com.ke.bella.openapi.protocol.tts.TtsPriceInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface IPriceInfo extends IDescription {
    String getUnit();

    @AllArgsConstructor
    @Getter
    enum EndpointPriceInfoType {
        COMPLETION("/v1/chat/completions", CompletionPriceInfo.class),
        EMBEDDING("/v1/embeddings", EmbeddingPriceInfo.class),
        TTS("/v1/audio/speech", TtsPriceInfo.class),
        FLASH_ASR("/v1/audio/asr/flash", FlashAsrPriceInfo.class),
        REALTIME_ASR("/v1/audio/asr/stream", RealTimeAsrPriceInfo.class)
        ;

        private final String endpoint;
        private final Class<? extends IPriceInfo> type;

        public static Class<? extends IPriceInfo> fetchType(String endpoint) {
            for (EndpointPriceInfoType t : EndpointPriceInfoType.values()) {
                if (t.endpoint.equals(endpoint)) {
                    return t.type;
                }
            }
            return null;
        }

    }
}
