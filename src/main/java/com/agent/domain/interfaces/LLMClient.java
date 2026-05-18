package com.agent.domain.interfaces;

import com.agent.domain.model.Message;

import java.util.List;

public interface LLMClient {

    Message generateResponse(
            List<Message> conversationHistory,
            List<Tool> availableTools
    );
}
