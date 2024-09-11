package com.example.cffinitestatemachine.services;

import com.example.cffinitestatemachine.statemachine.FiniteStateMachine;
import com.example.cffinitestatemachine.statemachine.FiniteStateMachineFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateMachineService {

    private final FiniteStateMachineFactory finiteStateMachineFactory;

    public boolean isAllowedTransition(String productName, String sourceName, String sourceState, String targetState) {
        FiniteStateMachine fsm = finiteStateMachineFactory.getFiniteStateMachine(productName, sourceName);
        if (fsm == null) {
            throw new IllegalArgumentException("Finite State Machine for Product " + productName + " and Source " + sourceName + " not found");
        }
        return fsm.isAllowedTransition(fsm.getStateBy(sourceState), fsm.getStateBy(targetState));
    }

    public void toggleFiniteStateMachine(String productName, String sourceName, boolean enabled) {
        FiniteStateMachine fsm = finiteStateMachineFactory.getFiniteStateMachine(productName, sourceName);
        if (fsm == null) {
            throw new IllegalArgumentException("Finite State Machine for Product " + productName + " and Source " + sourceName + " not found");
        }
        fsm.setEnabled(enabled);
    }

}
