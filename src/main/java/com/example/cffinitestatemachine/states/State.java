package com.example.cffinitestatemachine.states;

public interface State {
    String getName();
    int hashCode();
    @Override
    boolean equals(Object obj);
}
