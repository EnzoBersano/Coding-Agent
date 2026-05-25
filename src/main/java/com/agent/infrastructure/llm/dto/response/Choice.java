package com.agent.infrastructure.llm.dto.response;

import com.agent.infrastructure.llm.dto.shared.OpenAiMessage;

public record Choice(OpenAiMessage message) {}