package com.intel.bluetooth;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.ServiceRegistrationException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/ServiceRecordsRegistry.class */
public abstract class ServiceRecordsRegistry {
    private static Hashtable serviceRecordsMap = new Hashtable();
    static Class class$com$intel$bluetooth$ServiceRecordsRegistry;

    private ServiceRecordsRegistry() {
    }

    static synchronized void register(BluetoothConnectionNotifierServiceRecordAccess notifier, ServiceRecordImpl serviceRecord) {
        serviceRecordsMap.put(serviceRecord, notifier);
    }

    static synchronized void unregister(ServiceRecordImpl serviceRecord) {
        serviceRecordsMap.remove(serviceRecord);
    }

    static synchronized int getDeviceServiceClasses() {
        int deviceServiceClasses = 0;
        Enumeration en = serviceRecordsMap.keys();
        while (en.hasMoreElements()) {
            ServiceRecordImpl serviceRecord = (ServiceRecordImpl) en.nextElement2();
            deviceServiceClasses |= serviceRecord.deviceServiceClasses;
        }
        return deviceServiceClasses;
    }

    public static void updateServiceRecord(ServiceRecord srvRecord) throws ServiceRegistrationException {
        Class clsClass$;
        if (class$com$intel$bluetooth$ServiceRecordsRegistry == null) {
            clsClass$ = class$("com.intel.bluetooth.ServiceRecordsRegistry");
            class$com$intel$bluetooth$ServiceRecordsRegistry = clsClass$;
        } else {
            clsClass$ = class$com$intel$bluetooth$ServiceRecordsRegistry;
        }
        Class cls = clsClass$;
        synchronized (clsClass$) {
            BluetoothConnectionNotifierServiceRecordAccess owner = (BluetoothConnectionNotifierServiceRecordAccess) serviceRecordsMap.get(srvRecord);
            if (owner == null) {
                throw new IllegalArgumentException("Service record is not registered");
            }
            owner.updateServiceRecord(false);
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }
}
