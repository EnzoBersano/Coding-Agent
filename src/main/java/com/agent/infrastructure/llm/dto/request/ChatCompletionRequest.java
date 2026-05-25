package com.agent.infrastructure.llm.dto.request;

import com.agent.domain.interfaces.Tool;
import com.agent.domain.model.Message;
import com.agent.infrastructure.llm.dto.shared.OpenAiMessage;

import java.util.List;

public record ChatCompletionRequest(
        String model,
        List<OpenAiMessage> messages,
        List<OpenAiToolDefinition> tools
) {

    public static ChatCompletionRequest from(
            List<Message> history,
            List<Tool> tools
    ) {

        List<OpenAiMessage> openAiMessages = history.stream()
                .map(OpenAiMessage::from)
                .toList();

        List<OpenAiToolDefinition> openAiTools = tools.stream()
                .map(OpenAiToolDefinition::from)
                .toList();

        return new ChatCompletionRequest(
                "gpt-4.1-mini",
                openAiMessages,
                openAiTools
        );
    }
}