package com.example.cffinitestatemachine.dataloaders;

import com.example.cffinitestatemachine.statemachine.FiniteStateMachine;

import java.io.IOException;
import java.util.Map;

public interface DataLoader {
    Map<String, Map<String, FiniteStateMachine>> loadData() throws IOException;
}
