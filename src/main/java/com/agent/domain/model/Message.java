package com.agent.domain.model;

import java.util.List;

public record Message(
        Role role,
        String content,
        String toolCallId,
        List<ToolCall> toolCalls
) {

    public static Message user(String content) {
        return new Message(Role.USER, content, null, List.of());
    }

    public static Message assistant(String content) {
        return new Message(Role.ASSISTANT, content, null, List.of());
    }

    public static Message assistantToolCalls(List<ToolCall> toolCalls) {
        return new Message(Role.ASSISTANT, null, null,  toolCalls);
    }

    public static Message tool(String toolCallId, String content) {
        return new Message(
                Role.TOOL,
                content,
                toolCallId,
                List.of()
        );
    }

    public boolean hasToolCalls() {
        return toolCalls != null && !toolCalls.isEmpty();
    }
}
