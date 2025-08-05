package com.ftdi;

/* loaded from: JavaFTD2XX.jar:com/ftdi/Parity.class */
public enum Parity {
    PARITY_NONE,
    PARITY_ODD,
    PARITY_EVEN,
    PARITY_MARK,
    PARITY_SPACE;

    int constant() {
        return ordinal();
    }
}
