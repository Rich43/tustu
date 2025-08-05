package com.ftdi;

/* loaded from: JavaFTD2XX.jar:com/ftdi/FlowControl.class */
public enum FlowControl {
    FLOW_NONE(0),
    FLOW_RTS_CTS(256),
    FLOW_DTR_DSR(512),
    FLOW_XON_XOFF(1024);

    private final int constant;

    FlowControl(int constant) {
        this.constant = constant;
    }

    int constant() {
        return this.constant;
    }
}
