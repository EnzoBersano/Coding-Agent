package com.agent.application.planning;

import com.agent.domain.interfaces.LLMClient;
import com.agent.domain.model.Message;

import java.util.List;

public class PlanningService {

    private final LLMClient llmClient;

    public PlanningService(LLMClient llmClient) {
        this.llmClient = llmClient;
    }

    public String generatePlan(String userRequest) {

        Message planningPrompt = Message.user("""
                Generate a concise step-by-step execution plan for the following task.
                Do not execute tools.
                Only describe the plan.

                Task:
                """ + userRequest);

        Message response = llmClient.generateResponse(
                List.of(planningPrompt),
                List.of()
        );

        return response.content();
    }
}