package com.agent.domain.model;

import java.util.ArrayList;
import java.util.List;

public class ConversationSession {

    private final List<Message> messages = new ArrayList<>();

    public void add(Message message) {
        messages.add(message);
    }

    public List<Message> history() {
        return List.copyOf(messages);
    }
}