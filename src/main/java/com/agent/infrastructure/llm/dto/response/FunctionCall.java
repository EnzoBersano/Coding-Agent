package com.agent.infrastructure.llm.dto.response;

public record FunctionCall(String name, String arguments) {}