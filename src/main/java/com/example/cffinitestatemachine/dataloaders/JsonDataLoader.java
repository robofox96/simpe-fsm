package com.example.cffinitestatemachine.dataloaders;

import com.example.cffinitestatemachine.statemachine.FiniteStateMachine;
import com.example.cffinitestatemachine.statemachine.SimpleFiniteStateMachine;
import com.example.cffinitestatemachine.states.SimpleState;
import com.example.cffinitestatemachine.states.State;
import com.example.cffinitestatemachine.transitions.SimpleTransition;
import com.example.cffinitestatemachine.transitions.Transition;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JsonDataLoader implements DataLoader {

    private final ResourceLoader resourceLoader;

    private static final String PRODUCT_KEY = "products";
    private static final String SOURCE_KEY = "source";
    private static final String STATE_KEY = "states";
    private static final String TRANSITION_KEY = "transitions";
    private static final String SOURCE_STATE_KEY = "source";
    private static final String TARGET_STATE_KEY = "target";


    public static final String CONFIG_FILE_PATH = "classpath:statemachine.json";

    @Override
    public Map<String, Map<String, FiniteStateMachine>> loadData() throws IOException {
        Map<String, Map<String, FiniteStateMachine>> statemachineMap = new HashMap<>();
        File configFile = resourceLoader.getResource(CONFIG_FILE_PATH).getFile();
        JsonNode jsonNode = new ObjectMapper().readTree(configFile);
        JsonNode productNode = jsonNode.get(PRODUCT_KEY);
        productNode.fields().forEachRemaining(field -> {
            String productName = field.getKey();
            field.getValue().forEach(sourceNode -> {
                String sourceName = sourceNode.path(SOURCE_KEY).asText();

                Map<String, State> states = new HashMap<>();
                sourceNode.path(STATE_KEY).forEach(stateNode -> states.put(stateNode.asText(), new SimpleState(stateNode.asText())));

                List<Transition<? extends State, ? extends State>> transitions = new ArrayList<>();
                sourceNode.path(TRANSITION_KEY).forEach(transitionNode -> {
                    String sourceState = transitionNode.path(SOURCE_STATE_KEY).asText();
                    State source = Optional.of(states.get(sourceState)).orElseThrow(() -> new IllegalArgumentException("State " + sourceState + " not found"));
                    String targetState = transitionNode.path(TARGET_STATE_KEY).asText();
                    State target = Optional.of(states.get(targetState)).orElseThrow(() -> new IllegalArgumentException("State " + targetState + " not found"));
                    transitions.add(new SimpleTransition<>(source, target));
                });

                addStateMachine(statemachineMap, new SimpleFiniteStateMachine(productName, sourceName, states, transitions));
            });
        });
        return statemachineMap;
    }

    private void addStateMachine(Map<String, Map<String, FiniteStateMachine>> stateMachineMap, FiniteStateMachine fsm) {
        if (!stateMachineMap.containsKey(fsm.getProductName())) {
            stateMachineMap.put(fsm.getProductName(), new HashMap<>());
        }
        stateMachineMap.get(fsm.getProductName()).put(fsm.getSourceName(), fsm);
    }
}
