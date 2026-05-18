package com.agent.application.approval;

import com.agent.domain.model.ToolCall;

import java.util.Scanner;

public class ConsoleApprovalPolicy implements ApprovalPolicy {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean approve(ToolCall toolCall) {

        System.out.println();
        System.out.println("Approval required");
        System.out.println("Tool: " + toolCall.toolName());
        System.out.println("Arguments: " + toolCall.arguments());
        System.out.print("Approve? (y/n): ");

        String input = scanner.nextLine();

        return input.equalsIgnoreCase("y");
    }
}