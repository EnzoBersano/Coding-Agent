package com.agent.infrastructure.llm.dto.response;

import java.util.List;

public record ChatCompletionResponse(List<Choice> choices) {}