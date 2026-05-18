package com.agent.application.approval;

import com.agent.domain.model.ToolCall;

public interface ApprovalPolicy {

    boolean approve(ToolCall toolCall);
}