package com.agent.domain.model;

import java.util.Map;

public record ToolCall(
        String id,
        String toolName,
        Map<String, Object> arguments
) {
}
