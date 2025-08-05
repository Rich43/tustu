package com.ftdi;

/* loaded from: JavaFTD2XX.jar:com/ftdi/WordLength.class */
public enum WordLength {
    BITS_8(8),
    BITS_7(7);

    private final int constant;

    WordLength(int constant) {
        this.constant = constant;
    }

    int constant() {
        return this.constant;
    }
}
