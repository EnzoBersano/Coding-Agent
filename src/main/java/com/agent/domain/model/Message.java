package com.agent.domain.model;

import java.util.List;

public record Message(
        Role role,
        String content,
        List<ToolCall> toolCalls
) {

    public static Message user(String content) {
        return new Message(Role.USER, content, List.of());
    }

    public static Message assistant(String content) {
        return new Message(Role.ASSISTANT, content, List.of());
    }

    public static Message assistantToolCalls(List<ToolCall> toolCalls) {
        return new Message(Role.ASSISTANT, null, toolCalls);
    }

    public static Message tool(String content) {
        return new Message(Role.TOOL, content, List.of());
    }

    public boolean hasToolCalls() {
        return toolCalls != null && !toolCalls.isEmpty();
    }
}
