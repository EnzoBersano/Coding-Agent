package com.agent;

import com.agent.application.AgentOrchestrator;
import com.agent.application.ConsoleChatApplication;
import com.agent.application.ToolInvocationService;
import com.agent.application.ToolRegistry;
import com.agent.application.approval.ApprovalPolicy;
import com.agent.application.approval.ConsoleApprovalPolicy;
import com.agent.application.planning.approval.ConsolePlanApprovalPolicy;
import com.agent.application.planning.approval.PlanApprovalPolicy;
import com.agent.application.planning.PlanningService;
import com.agent.domain.interfaces.Tool;
import com.agent.infrastructure.llm.OpenAiClient;
import com.agent.infrastructure.tools.ListFilesTool;
import com.agent.infrastructure.tools.ReadFileTool;
import com.agent.infrastructure.tools.RunCommandTool;
import com.agent.infrastructure.tools.WebSearchTool;
import com.agent.infrastructure.tools.WriteFileTool;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String apiKey = System.getenv("OPENAI_API_KEY");

        OpenAiClient llmClient = new OpenAiClient(apiKey);

        List<Tool> tools = List.of(
                new ListFilesTool(),
                new ReadFileTool(),
                new WriteFileTool(),
                new RunCommandTool(),
                new WebSearchTool()
        );

        ToolRegistry toolRegistry = new ToolRegistry(tools);

        ApprovalPolicy approvalPolicy = new ConsoleApprovalPolicy();

        ToolInvocationService toolInvocationService =
                new ToolInvocationService(
                        toolRegistry,
                        approvalPolicy
                );

        AgentOrchestrator orchestrator =
                new AgentOrchestrator(
                        llmClient,
                        toolRegistry.all(),
                        toolInvocationService
                );


        PlanningService planningService = new PlanningService(llmClient);

        PlanApprovalPolicy planApprovalPolicy = new ConsolePlanApprovalPolicy();

        boolean planModeEnabled = true;

        ConsoleChatApplication application =
                new ConsoleChatApplication(
                        orchestrator,
                        planningService,
                        planApprovalPolicy,
                        planModeEnabled
                );

        application.start();
    }
}