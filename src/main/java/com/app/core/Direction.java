package com.app.core;

public enum Direction {
    UP('u'), RIGHT('r'), DOWN('d'), LEFT('l');

    private final char code;

    Direction(char code) {
        this.code = code;
    }

    public char getCode() { return code; }

    public static Direction fromChar(char c) {
        for (Direction d : values()) {
            if (d.code == c) return d;
        }
        return UP;
    }

    public Direction rotateLeft() {
        switch (this) {
            case UP: return LEFT;
            case LEFT: return DOWN;
            case DOWN: return RIGHT;
            case RIGHT: return UP;
        }
        return this;
    }

    public Direction rotateRight() {
        switch (this) {
            case UP: return RIGHT;
            case RIGHT: return DOWN;
            case DOWN: return LEFT;
            case LEFT: return UP;
        }
        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}