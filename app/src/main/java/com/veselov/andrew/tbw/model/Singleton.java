package com.veselov.andrew.tbw.model;

// Only for testing !

public final class Singleton {
    private static final Singleton ourInstance = new Singleton();
    private int attempts;

    private Singleton() {}

    public static synchronized Singleton getInstance() {
        return ourInstance;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}