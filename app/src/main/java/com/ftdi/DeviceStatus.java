package com.ftdi;

import java.util.ArrayList;
import java.util.EnumSet;

/* loaded from: JavaFTD2XX.jar:com/ftdi/DeviceStatus.class */
public enum DeviceStatus {
    CTS(16),
    DSR(32),
    RI(64),
    DCD(128),
    OE(2),
    PE(4),
    FE(8),
    BI(16);

    private final int constant;

    DeviceStatus(int constant) {
        this.constant = constant;
    }

    int constant() {
        return this.constant;
    }

    static EnumSet<DeviceStatus> parseToEnumset(int val) {
        ArrayList<DeviceStatus> enu = new ArrayList<>();
        DeviceStatus[] arr$ = values();
        for (DeviceStatus curr : arr$) {
            if ((curr.constant() & val) != 0) {
                enu.add(curr);
            }
        }
        return EnumSet.copyOf(enu);
    }
}
