package com.agent.infrastructure.llm.dto.response;

import com.agent.infrastructure.llm.dto.shared.OpenAiMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Choice(OpenAiMessage message) {}