package com.agent.infrastructure.tools;

import com.agent.domain.interfaces.Tool;
import com.agent.domain.model.ToolResult;

import java.util.List;
import java.util.Map;

public class WebSearchTool implements Tool {

    @Override
    public String getName() {
        return "web_search";
    }

    @Override
    public String getDescription() {
        return "Searches information on the web.";
    }

    @Override
    public Map<String, Object> getJsonSchema() {

        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "query", Map.of(
                                "type", "string",
                                "description", "Search query"
                        )
                ),
                "required", List.of("query")
        );
    }

    @Override
    public boolean isSensitive() {
        return false;
    }

    @Override
    public ToolResult execute(Map<String, Object> arguments) {

        String query = (String) arguments.get("query");

        return new ToolResult(
                getName(),
                "Web search not implemented yet. Query was: " + query,
                true
        );
    }
}