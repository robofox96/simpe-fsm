package com.example.cffinitestatemachine.statemachine;

import com.example.cffinitestatemachine.states.State;
import com.example.cffinitestatemachine.transitions.Transition;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleFiniteStateMachine implements FiniteStateMachine {

    private final Map<String,State> stateMap;
    private final Map<State, Set<State>> graph;
    @Getter
    private final String productName;
    @Getter
    private final String sourceName;
    @Getter
    private final String identifier = UUID.randomUUID().toString();
    @Getter
    private boolean enabled = true;

    public SimpleFiniteStateMachine(String productName, String sourceName,
                                    Map<String,State> states, List<Transition<? extends State, ? extends State>> transitions) {
        this.productName = productName;
        this.sourceName = sourceName;

        this.stateMap = states;

        this.graph = states.values().stream().collect(Collectors.toMap(s -> s, s -> {
            HashSet<State> set = new HashSet<>();
            set.add(s);
            return set;
        }));

        for (Transition<? extends State, ? extends State> t : transitions) {
            State sourceState = t.getSourceState();
            State targetState = t.getTargetState();
            if (!graph.containsKey(sourceState)) {
                graph.put(sourceState, new HashSet<>());
            }
            graph.get(sourceState).add(targetState);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isAllowedTransition(State sourceState, State targetState) {
        //If Not Enabled then all transitions are allowed
        if (!enabled) {
            return true;
        }
        if (!graph.containsKey(sourceState)) {
            return false;
        }
        return isReachable(sourceState, targetState);
    }

    private boolean isReachable(State source, State target) {
        if (source.equals(target)) {
            return true;
        }
        Set<State> visited = new HashSet<>();
        return dfs(source, target, graph, visited);
    }

    private boolean dfs(State current, State target, Map<State, Set<State>> graph, Set<State> visited) {
        if (current.equals(target)) {
            return true;
        }
        visited.add(current);
        Set<State> neighbors = graph.getOrDefault(current, new HashSet<>());
        for (State neighbor : neighbors) {
            if (!visited.contains(neighbor) && dfs(neighbor, target, graph, visited)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public State getStateBy(String stateName) {
        return Optional.ofNullable(stateMap.get(stateName)).orElseThrow(() -> new IllegalArgumentException("State " + stateName + " not found"));
    }

}
