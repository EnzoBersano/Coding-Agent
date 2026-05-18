package com.agent.application;

import com.agent.application.approval.ApprovalPolicy;
import com.agent.domain.interfaces.Tool;
import com.agent.domain.model.ToolCall;
import com.agent.domain.model.ToolResult;

public class ToolInvocationService {

    private final ToolRegistry toolRegistry;
    private final ApprovalPolicy approvalPolicy;

    public ToolInvocationService(ToolRegistry toolRegistry, ApprovalPolicy approvalPolicy) {
        this.toolRegistry = toolRegistry;
        this.approvalPolicy = approvalPolicy;
    }

    public ToolResult invoke(ToolCall toolCall) {

        return toolRegistry.findByName(toolCall.toolName())
                .map(tool -> executeTool(toolCall, tool))
                .orElseGet(() -> toolNotFound(toolCall));
    }

    private ToolResult executeTool(ToolCall toolCall, Tool tool) {

        try {

            if (requiresApproval(tool, toolCall)) {
                return rejectedExecution(tool);
            }
            return tool.execute(toolCall.arguments());

        } catch (Exception exception) {

            return failedExecution(tool, exception);
        }
    }

    private boolean requiresApproval(Tool tool, ToolCall toolCall) {

        return tool.isSensitive()
                && !approvalPolicy.approve(toolCall);
    }

    private ToolResult toolNotFound(ToolCall toolCall) {

        return new ToolResult(
                toolCall.toolName(),
                "Tool not found: " + toolCall.toolName(),
                false
        );
    }

    private ToolResult rejectedExecution(Tool tool) {

        return new ToolResult(tool.getName(), "Execution rejected by user.", false);
    }

    private ToolResult failedExecution(Tool tool, Exception exception) {

        return new ToolResult(tool.getName(), exception.getMessage(), false);
    }
}