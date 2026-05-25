package com.agent.infrastructure.tools;

import com.agent.domain.interfaces.Tool;
import com.agent.domain.model.ToolResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class WriteFileTool implements Tool {

    @Override
    public String getName() {
        return "write_file";
    }

    @Override
    public String getDescription() {
        return "Writes content into a file.";
    }

    @Override
    public Map<String, Object> getJsonSchema() {

        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "path", Map.of(
                                "type", "string",
                                "description", "File path"
                        ),
                        "content", Map.of(
                                "type", "string",
                                "description", "Content to write"
                        )
                ),
                "required", List.of("path", "content")
        );
    }

    @Override
    public boolean isSensitive() {
        return true;
    }

    @Override
    public ToolResult execute(Map<String, Object> arguments) throws IOException {

        String filePath = (String) arguments.get("path");

        String content = (String) arguments.get("content");

        Files.writeString(
                Path.of(filePath),
                content
        );

        return new ToolResult(
                getName(),
                "File written successfully.",
                true
        );
    }
}
