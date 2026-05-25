package com.agent.application.planning.approval;

import java.util.Scanner;

public class ConsolePlanApprovalPolicy implements PlanApprovalPolicy {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean approve(String plan) {

        System.out.println();
        System.out.println("Generated execution plan:");
        System.out.println();
        System.out.println(plan);
        System.out.println();

        System.out.print("Approve plan? (y/n): ");

        String input = scanner.nextLine();

        return input.equalsIgnoreCase("y");
    }
}