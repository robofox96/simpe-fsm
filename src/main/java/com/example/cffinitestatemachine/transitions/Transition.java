package com.example.cffinitestatemachine.transitions;

import com.example.cffinitestatemachine.states.State;

public interface Transition<S1 extends State, S2 extends State> {
    S1 getSourceState();
    S2 getTargetState();
}
