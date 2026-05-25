package com.agent.infrastructure.llm.dto.shared;

import com.agent.domain.model.ToolCall;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiToolCall(String id, String type, OpenAiFunction function) {

    public static OpenAiToolCall from(ToolCall toolCall) {

        return new OpenAiToolCall(
                toolCall.id(),
                "function",
                OpenAiFunction.from(toolCall)
        );
    }
}