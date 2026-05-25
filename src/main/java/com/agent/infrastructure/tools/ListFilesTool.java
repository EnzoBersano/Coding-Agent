package com.agent.infrastructure.tools;

import com.agent.domain.interfaces.Tool;
import com.agent.domain.model.ToolResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListFilesTool implements Tool {

    @Override
    public String getName() {
        return "list_files";
    }

    @Override
    public String getDescription() {
        return "Lists files inside a directory.";
    }

    @Override
    public Map<String, Object> getJsonSchema() {

        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "path", Map.of(
                                "type", "string",
                                "description", "Directory path to inspect"
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

        String directoryPath = (String) arguments.get("path");

        List<String> files = Files.list(Path.of(directoryPath))
                .map(path -> path.getFileName().toString())
                .sorted()
                .collect(Collectors.toList());

        String result = String.join("\n", files);

        return new ToolResult(
                getName(),
                result,
                true
        );
    }
}