package com.ftdi;

/* loaded from: JavaFTD2XX.jar:com/ftdi/DeviceType.class */
public enum DeviceType {
    DEVICE_232BM,
    DEVICE_232AM,
    DEVICE_100AX,
    DEVICE_UNKNOWN,
    DEVICE_2232C,
    DEVICE_232R,
    DEVICE_2232H,
    DEVICE_4232H,
    DEVICE_232H;

    int constant() {
        return ordinal();
    }
}
