package com.agent.infrastructure.llm.dto.response;

public record OpenAiToolCall(String id, FunctionCall function) {}