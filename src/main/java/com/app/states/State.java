package com.app.states;

import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public interface State {
    void enter();
    void exit();
    void update();
    void handleKeyPressed(KeyEvent event);
    void handleKeyReleased(KeyEvent event);
    void handleMousePressed(MouseEvent event);
    void handleMouseReleased(MouseEvent event);
    void handleMouseDragged(MouseEvent event);
    void handleMouseMoved(MouseEvent event);
    void handleScroll(ScrollEvent event);
    State getNextState();
    Parent getRoot();
}