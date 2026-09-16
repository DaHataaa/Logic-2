package com.app;

import com.app.states.State;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;

public class MasterState {
    private State currentState;
    private State nextState;

    public MasterState(State initialState) {
        this.currentState = initialState;
        this.currentState.enter();
    }

    public void handleKeyPressed(KeyEvent event) {
        if (currentState != null) {
            currentState.handleKeyPressed(event);
        }
    }

    public void handleKeyReleased(KeyEvent event) {
        if (currentState != null) {
            currentState.handleKeyReleased(event);
        }
    }

    public void handleMousePressed(MouseEvent event) {
        if (currentState != null) {
            currentState.handleMousePressed(event);
        }
    }

    public void handleMouseReleased(MouseEvent event) {
        if (currentState != null) {
            currentState.handleMouseReleased(event);
        }
    }

    public void handleMouseDragged(MouseEvent event) {
        if (currentState != null) {
            currentState.handleMouseDragged(event);
        }
    }

    public void handleMouseMoved(MouseEvent event) {
        if (currentState != null) {
            currentState.handleMouseMoved(event);
        }
    }

    public void handleScroll(ScrollEvent event) {
        if (currentState != null) {
            currentState.handleScroll(event);
        }
    }

    public void gameTick() {
        if (currentState != null) {
            currentState.update();
            nextState = currentState.getNextState();

            if (nextState != null && nextState != currentState) {
                System.out.println("Switching from " + currentState.getClass().getSimpleName() +
                        " to " + nextState.getClass().getSimpleName());
                currentState.exit();
                currentState = nextState;
                currentState.enter();
            }
        }
    }

    public Parent getRoot() {
        if (currentState != null) {
            return currentState.getRoot();
        }
        return new BorderPane();
    }
}