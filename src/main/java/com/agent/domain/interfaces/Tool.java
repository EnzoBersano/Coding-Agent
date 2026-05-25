package com.agent.domain.interfaces;

import com.agent.domain.model.ToolResult;

import java.util.Map;

public interface Tool {

    String getName();

    String getDescription();

    Map<String, Object> getJsonSchema();

    boolean isSensitive();

    ToolResult execute(Map<String, Object> arguments) throws Exception;
}
