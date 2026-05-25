package com.agent.infrastructure.llm.dto.request;

import com.agent.domain.interfaces.Tool;

public record OpenAiToolDefinition(
        String type,
        FunctionDefinition function
) {

    public static OpenAiToolDefinition from(Tool tool) {

        return new OpenAiToolDefinition(
                "function",
                new FunctionDefinition(
                        tool.getName(),
                        tool.getDescription(),
                        tool.getJsonSchema()
                )
        );
    }
}