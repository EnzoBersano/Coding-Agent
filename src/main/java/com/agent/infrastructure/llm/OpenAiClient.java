package com.agent.infrastructure.llm;

import com.agent.domain.interfaces.LLMClient;
import com.agent.domain.interfaces.Tool;
import com.agent.domain.model.Message;
import com.agent.domain.model.ToolCall;
import com.agent.infrastructure.llm.dto.request.ChatCompletionRequest;
import com.agent.infrastructure.llm.dto.response.ChatCompletionResponse;
import com.agent.infrastructure.llm.dto.shared.OpenAiToolCall;
import com.agent.infrastructure.llm.dto.shared.OpenAiMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class OpenAiClient implements LLMClient {

    private static final String OPENAI_URL =
            "https://api.openai.com/v1/chat/completions";

    private static final String MODEL = "gpt-4.1-mini";

    private final String apiKey;

    private final HttpClient httpClient;

    private final ObjectMapper objectMapper;

    public OpenAiClient(String apiKey) {

        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Message generateResponse(
            List<Message> history,
            List<Tool> tools
    ) {

        try {

            String requestBody = buildRequestBody(history, tools);

            HttpRequest request = buildHttpRequest(requestBody);

            String responseBody = sendRequest(request);

            return parseAssistantMessage(responseBody);

        } catch (IOException | InterruptedException exception) {

            throw new RuntimeException(
                    "Failed to communicate with OpenAI",
                    exception
            );
        }
    }

    private String buildRequestBody(List<Message> history, List<Tool> tools) throws IOException {

        ChatCompletionRequest request = ChatCompletionRequest.from(history, tools);

        return objectMapper.writeValueAsString(request);
    }

    private HttpRequest buildHttpRequest(String requestBody) {

        return HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    private String sendRequest(HttpRequest request) throws IOException, InterruptedException {

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }

    private Message parseAssistantMessage(String responseBody) throws IOException {

        ChatCompletionResponse response =
                objectMapper.readValue(
                        responseBody,
                        ChatCompletionResponse.class
                );

        OpenAiMessage assistantMessage =
                response
                        .choices()
                        .getFirst()
                        .message();

        if (containsToolCalls(assistantMessage)) {
            return buildToolCallMessage(assistantMessage);
        }

        return buildAssistantMessage(assistantMessage);
    }

    private boolean containsToolCalls(OpenAiMessage message) {

        return message.toolCalls() != null
                && !message.toolCalls().isEmpty();
    }

    private Message buildToolCallMessage(OpenAiMessage assistantMessage) {

        List<ToolCall> toolCalls =
                assistantMessage.toolCalls()
                        .stream()
                        .map(this::convertToolCall)
                        .toList();

        return Message.assistantToolCalls(toolCalls);
    }

    private ToolCall convertToolCall(OpenAiToolCall toolCall) {

        try {

            JsonNode arguments =
                    objectMapper.readTree(
                            toolCall.function().arguments()
                    );

            Map<String, Object> parsedArguments =
                    objectMapper.convertValue(
                            arguments,
                            objectMapper.getTypeFactory()
                                    .constructMapType(
                                            Map.class,
                                            String.class,
                                            Object.class
                                    )
                    );

            return new ToolCall(
                    toolCall.id(),
                    toolCall.function().name(),
                    parsedArguments
            );

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Failed to parse tool call arguments",
                    exception
            );
        }
    }

    private Message buildAssistantMessage(OpenAiMessage assistantMessage) {

        return Message.assistant(assistantMessage.content());
    }
}