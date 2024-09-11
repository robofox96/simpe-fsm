package com.example.cffinitestatemachine.statemachine;

import com.example.cffinitestatemachine.states.State;

public interface FiniteStateMachine {

    String getIdentifier();
    void setEnabled(boolean enabled);
    boolean isEnabled();
    boolean isAllowedTransition(State sourceState, State targetState);
    State getStateBy(String stateName);
    String getProductName();
    String getSourceName();

}
