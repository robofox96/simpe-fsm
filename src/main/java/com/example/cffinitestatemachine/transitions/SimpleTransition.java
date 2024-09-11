package com.example.cffinitestatemachine.transitions;

import com.example.cffinitestatemachine.states.State;
import lombok.Data;

@Data
public class SimpleTransition<S1 extends State, S2 extends State> implements Transition<S1, S2> {

    private final S1 sourceState;
    private final S2 targetState;

    public SimpleTransition(S1 sourceState, S2 targetState) {
        this.sourceState = sourceState;
        this.targetState = targetState;
    }

}
