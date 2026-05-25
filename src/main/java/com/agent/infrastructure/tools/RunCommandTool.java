package com.agent.infrastructure.tools;

import com.agent.domain.interfaces.Tool;
import com.agent.domain.model.ToolResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class RunCommandTool implements Tool {

    @Override
    public String getName() {
        return "run_command";
    }

    @Override
    public String getDescription() {
        return "Executes terminal commands.";
    }

    @Override
    public Map<String, Object> getJsonSchema() {

        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "command", Map.of(
                                "type", "string",
                                "description", "Shell command to execute"
                        )
                ),
                "required", List.of("command")
        );
    }

    @Override
    public boolean isSensitive() {
        return true;
    }

    @Override
    public ToolResult execute(Map<String, Object> arguments)
            throws IOException, InterruptedException {

        String command = (String) arguments.get("command");

        Process process = Runtime.getRuntime().exec(command);

        BufferedReader stdout = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        BufferedReader stderr = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );

        String output = stdout.lines()
                .reduce("", (left, right) -> left + "\n" + right);

        String errors = stderr.lines()
                .reduce("", (left, right) -> left + "\n" + right);

        process.waitFor();

        String result = """
                STDOUT:
                %s
                
                STDERR:
                %s
                """.formatted(output, errors);

        return new ToolResult(
                getName(),
                result,
                true
        );
    }
}