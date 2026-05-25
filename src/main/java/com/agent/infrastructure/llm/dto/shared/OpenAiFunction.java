package com.agent.infrastructure.llm.dto.shared;

import com.agent.domain.model.ToolCall;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiFunction(String name, String arguments) {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static OpenAiFunction from(ToolCall toolCall) {

        try {

            return new OpenAiFunction(
                    toolCall.toolName(),
                    OBJECT_MAPPER.writeValueAsString(toolCall.arguments())
            );

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Failed to serialize tool arguments",
                    exception
            );
        }
    }
}