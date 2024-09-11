package com.example.cffinitestatemachine.statemachine;

import com.example.cffinitestatemachine.dataloaders.DataLoader;
import com.example.cffinitestatemachine.states.SimpleState;
import com.example.cffinitestatemachine.states.State;
import com.example.cffinitestatemachine.transitions.SimpleTransition;
import com.example.cffinitestatemachine.transitions.Transition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class FiniteStateMachineFactory {

    private final Map<String, Map<String, FiniteStateMachine>> stateMachineMap;

    public FiniteStateMachineFactory(DataLoader jsonDataLoader) {
        try {
            stateMachineMap = jsonDataLoader.loadData();
        } catch (Exception e) {
            log.error("Failed to Load Config for Finite State Machines", e);
            throw new RuntimeException(e);
        }
    }

    public FiniteStateMachine getFiniteStateMachine(String productName, String sourceName) {
        if (!stateMachineMap.containsKey(productName)) {
            throw new IllegalArgumentException("State Machine for Product " + productName + " not found");
        }
        if (!stateMachineMap.get(productName).containsKey(sourceName)) {
            throw new IllegalArgumentException("State Machine for Source " + sourceName + " not found");
        }
        return stateMachineMap.get(productName).get(sourceName);
    }

}
