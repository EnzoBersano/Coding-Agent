package com.agent.domain.model;

public record ToolResult(
        String toolName,
        String result,
        boolean success
) {
}
