package com.example.cffinitestatemachine.states;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class SimpleState implements State {

    private final String name;

    public SimpleState(String name) {
        if (!StringUtils.hasText(name) || name.trim().isEmpty()) {
            throw new IllegalArgumentException("State name cannot be empty");
        }
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return name.equals(state.getName());

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
