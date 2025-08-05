package com.intel.bluetooth;

import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothConnectionNotifierParams.class */
class BluetoothConnectionNotifierParams {
    UUID uuid;
    boolean authenticate;
    boolean encrypt;
    boolean authorize;
    String name;
    boolean master;
    boolean timeouts;
    int bluecove_ext_psm = 0;
    boolean obex = false;

    public BluetoothConnectionNotifierParams(UUID uuid, boolean authenticate, boolean encrypt, boolean authorize, String name, boolean master) {
        this.uuid = uuid;
        this.authenticate = authenticate;
        this.encrypt = encrypt;
        this.authorize = authorize;
        this.name = name;
        this.master = master;
    }
}
