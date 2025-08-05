package com.intel.bluetooth;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothConnectionParams.class */
class BluetoothConnectionParams {
    public static final int DEFAULT_CONNECT_TIMEOUT = 120000;
    long address;
    int channel;
    boolean authenticate;
    boolean encrypt;
    boolean timeouts;
    public int timeout = 120000;

    public BluetoothConnectionParams(long address, int channel, boolean authenticate, boolean encrypt) {
        this.address = address;
        this.channel = channel;
        this.authenticate = authenticate;
        this.encrypt = encrypt;
    }
}
