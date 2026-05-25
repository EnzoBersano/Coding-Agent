package com.agent.infrastructure.tools;

import com.agent.domain.interfaces.Tool;
import com.agent.domain.model.ToolResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ReadFileTool implements Tool {

    @Override
    public String getName() {
        return "read_file";
    }

    @Override
    public String getDescription() {
        return "Reads the contents of a file.";
    }

    @Override
    public Map<String, Object> getJsonSchema() {

        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "path", Map.of(
                                "type", "string",
                                "description", "File path to read"
                        )
                ),
                "required", List.of("path")
        );
    }

    @Override
    public boolean isSensitive() {
        return false;
    }

    @Override
    public ToolResult execute(Map<String, Object> arguments) throws IOException {

        String filePath = (String) arguments.get("path");

        String content = Files.readString(
                Path.of(filePath)
        );

        return new ToolResult(
                getName(),
                content,
                true
        );
    }
}
