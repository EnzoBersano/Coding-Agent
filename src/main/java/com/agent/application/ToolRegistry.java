package com.agent.application;

import com.agent.domain.interfaces.Tool;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToolRegistry {

    private final Map<String, Tool> toolsByName;

    public ToolRegistry(List<Tool> tools) {
        this.toolsByName = tools.stream()

                .collect(Collectors.toUnmodifiableMap(
                        Tool::getName,
                        Function.identity()
                ));
    }

    public Optional<Tool> findByName(String toolName) {

        return Optional.ofNullable(toolsByName.get(toolName));
    }

    public List<Tool> all() {

        return List.copyOf(toolsByName.values());
    }
}