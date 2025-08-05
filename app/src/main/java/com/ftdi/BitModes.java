package com.ftdi;

/* loaded from: JavaFTD2XX.jar:com/ftdi/BitModes.class */
public enum BitModes {
    BITMODE_RESET(0),
    BITMODE_ASYNC_BITBANG(1),
    BITMODE_MPSSE(2),
    BITMODE_SYNC_BITBANG(4),
    BITMODE_MCU_HOST(8),
    BITMODE_FAST_SERIAL(16),
    BITMODE_CBUS_BITBANG(32),
    BITMODE_SYNC_FIFO(64);

    private final int constant;

    BitModes(int constant) {
        this.constant = constant;
    }

    int constant() {
        return this.constant;
    }

    static BitModes parse(int val) {
        BitModes[] arr$ = values();
        for (BitModes curr : arr$) {
            if (curr.constant() == val) {
                return curr;
            }
        }
        return null;
    }
}
