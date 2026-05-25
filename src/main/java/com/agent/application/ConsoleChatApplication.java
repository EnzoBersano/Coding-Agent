package com.agent.application;

import com.agent.application.planning.approval.PlanApprovalPolicy;
import com.agent.application.planning.PlanningService;
import com.agent.domain.model.ConversationSession;
import com.agent.domain.model.Message;

import java.util.Scanner;

public class ConsoleChatApplication {

    private final AgentOrchestrator agentOrchestrator;
    private final Scanner scanner;
    private final PlanningService planningService;
    private final PlanApprovalPolicy planApprovalPolicy;
    private final boolean planModeEnabled;

    public ConsoleChatApplication(
            AgentOrchestrator agentOrchestrator,
            PlanningService planningService,
            PlanApprovalPolicy planApprovalPolicy,
            boolean planModeEnabled
    ) {
        this.agentOrchestrator = agentOrchestrator;
        this.planningService = planningService;
        this.planApprovalPolicy = planApprovalPolicy;
        this.planModeEnabled = planModeEnabled;
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        ConversationSession session = new ConversationSession();

        printWelcomeMessage();

        while (true) {

            String userInput = readUserInput();

            if (shouldTerminate(userInput)) {
                break;
            }

            if (planRejected(userInput)) {
                continue;
            }

            session.add(Message.user(userInput));

            Message response = agentOrchestrator.processTurn(session);

            printAssistantResponse(response);
        }
    }

    private void printWelcomeMessage() {

        System.out.println("Coding Agent started.");
        System.out.println("Type 'exit' to quit.");
    }

    private String readUserInput() {

        System.out.println();
        System.out.print("> ");

        return scanner.nextLine();
    }

    private boolean shouldTerminate(String userInput) {

        return userInput.equalsIgnoreCase("exit");
    }

    private boolean planRejected(String userInput) {

        if (!planModeEnabled) {
            return false;
        }

        String plan =
                planningService.generatePlan(userInput);

        boolean approved =
                planApprovalPolicy.approve(plan);

        if (!approved) {

            System.out.println("Plan rejected.");

            return true;
        }

        return false;
    }

    private void printAssistantResponse(Message response) {

        System.out.println();
        System.out.println(response.content());
    }
}