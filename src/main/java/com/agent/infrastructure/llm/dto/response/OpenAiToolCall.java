package com.agent.infrastructure.llm.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenAiToolCall(String id, FunctionCall function) {}