package com.agent.application;

import com.agent.domain.model.ConversationSession;
import com.agent.domain.model.Message;

import java.util.Scanner;

public class ConsoleChatApplication {

    private final AgentOrchestrator agentOrchestrator;

    private final Scanner scanner;

    public ConsoleChatApplication(
            AgentOrchestrator agentOrchestrator
    ) {
        this.agentOrchestrator = agentOrchestrator;
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        ConversationSession session = new ConversationSession();

        System.out.println("Coding Agent started.");
        System.out.println("Type 'exit' to quit.");

        while (true) {

            System.out.println();
            System.out.print("> ");

            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("exit")) {
                break;
            }

            session.add(
                    Message.user(userInput)
            );

            Message response =
                    agentOrchestrator.processTurn(session);

            System.out.println();
            System.out.println(response.content());
        }
    }
}