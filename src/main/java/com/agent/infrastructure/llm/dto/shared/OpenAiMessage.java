package com.agent.infrastructure.llm.dto.shared;

import com.agent.domain.model.Message;
import com.agent.domain.model.Role;
import com.agent.infrastructure.llm.dto.response.OpenAiToolCall;

import java.util.List;

public record OpenAiMessage(
        String role,
        String content,
        List<OpenAiToolCall> tool_calls,
        String tool_call_id
) {

    public static OpenAiMessage from(Message message) {

        return new OpenAiMessage(
                mapRole(message.role()),
                message.content(),
                null,
                message.toolCallId()
        );
    }

    private static String mapRole(Role role) {

        return switch (role) {

            case USER -> "user";
            case ASSISTANT -> "assistant";
            case TOOL -> "tool";
            case SYSTEM -> "";
        };
    }

    public List<OpenAiToolCall> toolCalls() {
        return tool_calls;
    }
}