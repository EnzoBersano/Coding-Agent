# Coding Agent

A Java implementation of an LLM-powered coding agent built from scratch without external AI agent frameworks.

## Overview

This project demonstrates how modern AI agents work internally by implementing a complete orchestration harness capable of tool calling, conversational memory, planning workflows, and human-in-the-loop supervision.

The goal of the project is educational clarity and architectural simplicity: every major component of the agent loop is explicit, inspectable, and extensible.

## Core Features

### LLM Orchestration Harness

The agent implements the complete orchestration loop required for autonomous tool usage:

1. Send conversation history and available tools to the LLM
2. Detect tool calls in the assistant response
3. Execute tools locally
4. Return tool results back to the model
5. Repeat until the assistant produces a final response

---

### Tool System
Five built-in tools demonstrate the extensible tool architecture:

| Tool | Operation | Sensitivity |
|------|-----------|--------------|
| list_files | Directory inspection | Read-only |
| read_file | File content retrieval | Read-only |
| write_file | File creation/modification | Sensitive |
| run_command | Terminal command execution | Sensitive |
| web_search | Web information retrieval | Read-only |

Each tool implements a common interface that provides name, description, JSON schema, execution logic, and sensitivity metadata.

### Planning Mode

When planning mode is enabled, the agent generates a step-by-step execution plan before invoking tools.

The user can:
- Approve the plan
- Reject the plan
- Modify the request and retry

This simulates the reasoning and deliberation workflows used by autonomous AI systems.

### Human-in-the-Loop Supervision / Approval Workflow

Sensitive operations require explicit user approval before execution.

Protected operations include:
- File modifications
- Terminal command execution

This creates a safety layer between the agent and the host system while allowing read-only operations to execute automatically.

## Architecture

The project follows clean architecture principles with clear separation of concerns across three layers.

### Domain Layer
Contains business models and contracts independent of infrastructure:
- ConversationSession: Maintains message history across turns
- Message, ToolCall, ToolResult: Core conversation models
- Role: Enumeration of message types (USER, ASSISTANT, TOOL, SYSTEM)
- LLMClient, Tool: Abstractions that infrastructure implements

The domain layer has no external dependencies and expresses all business rules through pure Java interfaces and records.

### Application Layer
Implements orchestration logic and use cases:
- AgentOrchestrator: Main harness loop with configurable iteration limits
- ToolInvocationService: Coordinates tool lookup, approval, execution, and error handling
- ToolRegistry: Maintains tool catalog with name-based lookup
- PlanningService: Generates execution plans using the LLM
- Approval Policies: Auto-approve, console-based, and plan approval strategies
- ConsoleChatApplication: CLI interface with planning mode integration

The orchestrator manages the core agent loop: send conversation to LLM, detect tool calls, execute approved tools, append results, and repeat until final response.

### Infrastructure Layer
Provides concrete implementations:
- OpenAiClient: HTTP client for OpenAI Chat Completions API with native function calling
- DTOs: Request/response mappings for API compatibility
- File System Tools: NIO-based file operations
- Process Execution: Command-line invocation with stdout/stderr capture

## Implementation Details

### OpenAI Integration
The OpenAiClient directly implements the Chat Completions API protocol:
- Converts domain Message objects to OpenAI message format
- Injects tool definitions as function declarations
- Parses assistant responses for tool calls
- Deserializes JSON arguments into Java Map structures
- Handles tool result submission as subsequent messages

All DTOs are Jackson-annotated records with explicit serialization control. The implementation avoids SDK dependencies to expose the raw HTTP protocol and JSON contract.

### Tool Call Lifecycle
1. LLM returns assistant message with tool_calls array
2. Orchestrator extracts ToolCall objects with ID, name, and arguments
3. ToolInvocationService validates tool existence
4. ApprovalPolicy checks sensitivity and requests user confirmation
5. Tool executes with parsed arguments
6. ToolResult wraps success/failure and output
7. Message.tool() creates result message with matching call ID
8. Result appended to conversation for next LLM turn

### Conversation State
ConversationSession maintains an append-only list of messages. The session provides immutable history views while accumulating context across turns. Each orchestration iteration adds assistant responses and tool results before the next LLM request.

## Technical Stack

- Java 21+ (records, pattern matching, text blocks)
- Maven for build management
- OpenAI Chat Completions API
- Jackson for JSON processing
- Java HttpClient for API communication
- dotenv-java for configuration management

## Getting Started

### Prerequisites
- Java 21 or later
- OpenAI API key
- Maven 3.6+

### Installation

Clone the repository:
```bash
git clone <repository-url>
cd coding-agent
```

### Configure API Key

#### Option 1: `.env`

```bash
echo "OPENAI_API_KEY=your_key_here" > .env
```

#### Option 2: Environment Variable

```bash
export OPENAI_API_KEY=your_key_here
```


### Build the project:
```bash
mvn clean compile
```

### Running the Application
Execute via Maven:
```bash
mvn exec:java
```
Or run the Main class directly from your IDE.

## Design Decisions

### No AI Frameworks

The project deliberately avoids LangChain, Spring AI, and similar abstractions. This exposes the underlying patterns behind modern AI agents, including:
- Tool calling protocols
- Conversation state management
- Harness loop implementation
- DTO mapping between domain and API contracts

Each component remains explicit and understandable without hidden framework behavior.

---

### Record-Based Models

Domain models use Java records for immutable data carriers. Messages, tool calls, and tool results benefit from:
- Concise syntax
- Built-in `equals()` and `hashCode()`
- Reduced boilerplate
- Clear semantic intent

This keeps the domain layer compact and expressive.

---

### Clean Architecture

Dependency direction points inward:
- The domain layer knows nothing about OpenAI or HTTP
- The application layer depends only on domain abstractions
- Infrastructure implements domain interfaces

This separation keeps the orchestration logic testable, framework-independent, and easier to evolve.

---

### Explicit Over Clever

The implementation prioritizes readability and traceability over abstraction-heavy designs.

Key workflows remain intentionally visible:
- The orchestration loop in `AgentOrchestrator`
- Approval handling in `ToolInvocationService`
- Conversation mutation through explicit append operations
- Manual OpenAI request and response mapping

The goal is to make the internal mechanics of the agent easy to understand and extend.

## What This Project Demonstrates

- How LLM function calling works at the protocol level
- Implementation patterns for agentic loops
- Tool abstraction and dynamic registration
- Conversation history management
- Human-in-the-loop approval systems
- Planning and deliberation workflows
- DTO mapping between domain and API contracts
- Clean architecture in a real application


## Usage Examples

### Basic File Operations

```text
> list all Java files in the src directory

[Plan generated and approved]
[Tool: list_files approved]
[Tool: list_files executed]

The directory contains:
Main.java
OpenAiClient.java
AgentOrchestrator.java
```


```text
> read the Main class

[Tool: read_file executed]
```


```text
> what tools are registered there?

[Agent uses conversation memory to reference previously read content]
The Main class registers ListFilesTool, ReadFileTool, WriteFileTool, RunCommandTool, and WebSearchTool.
```