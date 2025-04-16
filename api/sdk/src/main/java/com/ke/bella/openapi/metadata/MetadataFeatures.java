package com.ke.bella.openapi.metadata;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.ke.bella.openapi.EnumDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum MetadataFeatures {
    OVERSEAS("overseas", "国外", ImmutableSet.of("*")),
    MAINLAND("mainland", "国内", ImmutableSet.of("*")),
    INNER("inner", "内部", ImmutableSet.of("*")),
    LARGE_INPUT_CONTEXT("long_input_context", "超长上下文", ImmutableSet.of("/v1/chat/completions")),
    LARGE_OUTPUT_CONTEXT("long_output_context", "超长输出", ImmutableSet.of("/v1/chat/completions")),
    STREAM("stream", "流式", ImmutableSet.of("/v1/chat/completions")),
    FUNCTION_CALL("function_call", "工具调用", ImmutableSet.of("/v1/chat/completions")),
    STREAM_FUNCTION_CALL("stream_function_call", "流式工具调用", ImmutableSet.of("/v1/chat/completions")),
    PARALLEL_TOOL_CALLS("parallel_tool_calls", "并行工具调用", ImmutableSet.of("/v1/chat/completions")),
    VISION("vision", "视觉", ImmutableSet.of("/v1/chat/completions")),
    JSON_FORMAT("json_format", "json格式", ImmutableSet.of("/v1/chat/completions"))
    ;
    private final String code;
    private final String name;
    private final Set<String> endpoint;
    public static List<EnumDto> listFeatures(String endpoint) {
        return Arrays.stream(MetadataFeatures.values()).filter(t -> t.endpoint.contains(endpoint) || t.endpoint.contains("*"))
                .map(t -> new EnumDto(t.getCode(), t.getName())).collect(Collectors.toList());
    }

    public static boolean validate(List<String> features) {
        Set<String> set = Sets.newHashSet(OVERSEAS.code, MAINLAND.code, INNER.code);
        int i = 0;
        for(String feature : features) {
            if(set.contains(feature)) {
                i++;
            }
        }
        return i <= 1;
    }
}
