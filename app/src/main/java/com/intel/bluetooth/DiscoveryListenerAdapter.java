package com.intel.bluetooth;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/DiscoveryListenerAdapter.class */
class DiscoveryListenerAdapter implements DiscoveryListener {
    DiscoveryListenerAdapter() {
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void inquiryCompleted(int discType) {
        DebugLog.debug("inquiryCompleted", discType);
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void serviceSearchCompleted(int transID, int respCode) {
        DebugLog.debug("serviceSearchCompleted", respCode);
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
    }
}
