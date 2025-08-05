package com.intel.bluetooth;

import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/BluetoothConnectionNotifierServiceRecordAccess.class */
public interface BluetoothConnectionNotifierServiceRecordAccess {
    ServiceRecord getServiceRecord();

    void updateServiceRecord(boolean z2) throws ServiceRegistrationException;
}
