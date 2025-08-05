package com.intel.bluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryListener;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/DeviceInquiryRunnable.class */
interface DeviceInquiryRunnable {
    int runDeviceInquiry(DeviceInquiryThread deviceInquiryThread, int i2, DiscoveryListener discoveryListener) throws BluetoothStateException;

    void deviceDiscoveredCallback(DiscoveryListener discoveryListener, long j2, int i2, String str, boolean z2);
}
