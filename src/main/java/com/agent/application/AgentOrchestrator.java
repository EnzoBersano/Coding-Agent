package com.agent.application;

import com.agent.domain.interfaces.LLMClient;
import com.agent.domain.interfaces.Tool;
import com.agent.domain.model.ConversationSession;
import com.agent.domain.model.Message;
import com.agent.domain.model.ToolCall;
import com.agent.domain.model.ToolResult;

import java.util.List;

public class AgentOrchestrator {

    private static final int MAX_ITERATIONS = 10;

    private final LLMClient llmClient;
    private final List<Tool> availableTools;
    private final ToolInvocationService toolInvocationService;

    public AgentOrchestrator(
            LLMClient llmClient,
            List<Tool> availableTools,
            ToolInvocationService toolInvocationService
    ) {
        this.llmClient = llmClient;
        this.availableTools = List.copyOf(availableTools);
        this.toolInvocationService = toolInvocationService;
    }

    public Message processTurn(ConversationSession session) {

        for (int iteration = 0; iteration < MAX_ITERATIONS; iteration++) {

            Message assistantResponse = generateAssistantResponse(session);

            session.add(assistantResponse);

            if (responseIsFinal(assistantResponse)) {
                return assistantResponse;
            }

            processToolCalls(assistantResponse.toolCalls(), session);
        }

        return Message.assistant(
                "Max iterations reached."
        );
    }

    private Message generateAssistantResponse(ConversationSession session) {

        return llmClient.generateResponse(session.history(), availableTools);
    }

    private boolean responseIsFinal(Message assistantResponse) {

        return !assistantResponse.hasToolCalls();
    }

    private void processToolCalls(List<ToolCall> toolCalls, ConversationSession session) {

        for (ToolCall toolCall : toolCalls) {

            ToolResult result = toolInvocationService.invoke(toolCall);

            appendToolResult(toolCall, result, session);
        }
    }

    private void appendToolResult(ToolCall toolCall, ToolResult result, ConversationSession session) {

        session.add(
                Message.tool(
                        toolCall.id(),
                        result.result()
                )
        );
    }
}