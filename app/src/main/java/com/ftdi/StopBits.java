package com.ftdi;

/* loaded from: JavaFTD2XX.jar:com/ftdi/StopBits.class */
public enum StopBits {
    STOP_BITS_1(0),
    STOP_BITS_2(2);

    private final int constant;

    StopBits(int constant) {
        this.constant = constant;
    }

    int constant() {
        return this.constant;
    }
}
