package com.intel.bluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/SearchServicesRunnable.class */
interface SearchServicesRunnable {
    int runSearchServices(SearchServicesThread searchServicesThread, int[] iArr, UUID[] uuidArr, RemoteDevice remoteDevice, DiscoveryListener discoveryListener) throws BluetoothStateException;
}
