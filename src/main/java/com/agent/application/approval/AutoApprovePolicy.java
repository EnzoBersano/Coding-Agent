package com.agent.application.approval;

import com.agent.domain.model.ToolCall;

public class AutoApprovePolicy implements ApprovalPolicy {

    @Override
    public boolean approve(ToolCall toolCall) {
        return true;
    }
}
