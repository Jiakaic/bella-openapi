package com.ke.bella.openapi.protocol.completion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ke.bella.openapi.protocol.UserRequest;
import com.ke.bella.openapi.utils.JacksonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
public class CompletionRequest implements UserRequest, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID of the model to use. Can be a comma-separated list of models.
     */
    private String model;

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse. Learn more.
     */
    private String user;

    /**
     * A list of messages comprising the conversation so far
     */
    private List<Message> messages;

    /**
     * Deprecated in favor of tools.
     */
    @Deprecated
    private List<Message.Function> functions;

    /**
     * A list of tools the model may call. Currently, only functions are supported as a tool. <br/> Use this to provide a list of functions the model
     * may generate JSON inputs for.
     */
    private List<Message.Tool> tools;

    /**
     * Deprecated in favor of tool_choice. Controls how the model responds to function calls. "none" means the model does not call a function, and
     * responds to the end-user. "auto" means the model can pick between an end-user or calling a function. Specifying a particular function via
     * {"name":"my_function"} forces the model to call that function. "none" is the default when no functions are present. "auto" is the default if
     * functions are present.
     */
    @Nullable
    @Deprecated
    private Object function_call;

    /**
     * Controls which (if any) function is called by the model. none means the model will not call a function and instead generates a message. auto
     * means the model can pick between generating a message or calling a function. <br/><br/> Specifying a particular function via {"type:
     * "function", "function": { "name": "my_function"}} forces the model to call that function.
     */
    @Nullable
    private Object tool_choice;

    /**
     * defaults to 1; lower values will make it more focused and deterministic.
     */
    @Nullable
    private Float temperature;

    /**
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p
     * probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered. We generally recommend altering this or
     * temperature but not both.
     */
    @Nullable
    private Float top_p;

    /**
     * defaults to 1
     */
    @Nullable
    private Integer n;

    /**
     * defaults to false
     */
    private boolean stream = false;

    /**
     * Options for streaming response. Only set this when you set stream: true.
     */
    private StreamOptions stream_options;

    /**
     * String or array Up to 4 sequences where the API will stop generating further tokens.
     */
    @Nullable
    private Object stop;

    /**
     * The maximum number of tokens to generate in the chat completion.
     */
    @Nullable
    private Integer max_tokens;

    /**
     * defaults to 0 Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the
     * model's likelihood to talk about new topics.
     */
    @Nullable
    private Float presence_penalty;

    /**
     * defaults to 0 Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing
     * the model's likelihood to repeat the same line verbatim.
     */
    @Nullable
    private Float frequency_penalty;

    /**
     * defaults to null Modify the likelihood of specified tokens appearing in the completion.
     * <p>
     * Accepts a json object that maps tokens (specified by their token ID in the tokenizer) to an associated bias value from -100 to 100.
     * Mathematically, the bias is added to the logits generated by the model prior to sampling. The exact effect will vary per model, but values
     * between -1 and 1 should decrease or increase likelihood of selection; values like -100 or 100 should result in a ban or exclusive selection of
     * the relevant token.
     */
    @Nullable
    private Map<String, Integer> logit_bias;

    /**
     * An object specifying the format that the model must output.<br/><br/> Setting to { "type": "json_object" } enables JSON mode,</br> which
     * guarantees the message the model generates is valid JSON.<br/><br/> Important: when using JSON mode, you must also instruct the model to
     * produce JSON yourself via a system or user message. <br/> Without this, the model may generate an unending stream of whitespace until the
     * generation reaches the token limit, <br> resulting in a long-running and seemingly "stuck" request. Also note that the message content may be
     * partially cut off if finish_reason="length", which indicates the generation exceeded max_tokens or the conversation exceeded the max context
     * length.<br/><br/> type  properties Defaults to text Must be one of text or json_object.
     */
    @javax.annotation.Nullable
    private Object response_format;

    /**
     * This feature is in Beta. If specified, our system will make a best effort to sample deterministically, such that repeated requests with the
     * same seed and parameters should return the same result. Determinism is not guaranteed, and you should refer to the system_fingerprint response
     * parameter to monitor changes in the backend.
     */
    private Integer seed;

    private Boolean parallel_tool_calls;

    /**
     * Constrains effort on reasoning for reasoning models. Currently supported values are low, medium, and high.
     * Reducing reasoning effort can result in faster responses and fewer tokens used on reasoning in a response.
     */
    private Object reasoning_effort;

    private Object extra_body;

    private Boolean enable_thinking;

    private Object thinking;

    @Data
    public static class StreamOptions {
        /**
         * If set, an additional chunk will be streamed before the data: [DONE] message.
         * The usage field on this chunk shows the token usage statistics for the entire request,
         * and the choices field will always be an empty array.
         * All other chunks will also include a usage field, but with a null value.
         */
        private boolean include_usage = true;
    }
}
