package com.ftdi;

/* loaded from: JavaFTD2XX.jar:com/ftdi/Purge.class */
public enum Purge {
    PURGE_RX(1),
    PURGE_TX(2);

    private final int constant;

    Purge(int constant) {
        this.constant = constant;
    }

    int constant() {
        return this.constant;
    }
}
