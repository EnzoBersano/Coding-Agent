package com.agent.infrastructure.llm.dto.request;

import java.util.Map;

public record FunctionDefinition(String name, String description, Map<String, Object> parameters) {}