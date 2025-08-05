package com.intel.bluetooth;

import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/SelectServiceHandler.class */
public class SelectServiceHandler implements DiscoveryListener {
    private DiscoveryAgent agent;
    private boolean inquiryCompleted;
    private boolean serviceSearchCompleted;
    private ServiceRecord servRecordDiscovered;
    private static int threadNumber;
    private Object inquiryCompletedEvent = new Object();
    private Object serviceSearchCompletedEvent = new Object();
    private Hashtable devicesProcessed = new Hashtable();
    private Vector serviceSearchDeviceQueue = new Vector();

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized int nextThreadNum() {
        int i2 = threadNumber;
        threadNumber = i2 + 1;
        return i2;
    }

    public SelectServiceHandler(DiscoveryAgent agent) {
        this.agent = agent;
    }

    public String selectService(UUID uuid, int security, boolean master) throws BluetoothStateException {
        if (uuid == null) {
            throw new NullPointerException("uuid is null");
        }
        switch (security) {
            case 0:
            case 1:
            case 2:
                RemoteDevice[] devs = this.agent.retrieveDevices(1);
                for (int i2 = 0; devs != null && i2 < devs.length; i2++) {
                    ServiceRecord sr = findServiceOnDevice(uuid, devs[i2]);
                    if (sr != null) {
                        return sr.getConnectionURL(security, master);
                    }
                }
                RemoteDevice[] devs2 = this.agent.retrieveDevices(0);
                for (int i3 = 0; devs2 != null && i3 < devs2.length; i3++) {
                    ServiceRecord sr2 = findServiceOnDevice(uuid, devs2[i3]);
                    if (sr2 != null) {
                        return sr2.getConnectionURL(security, master);
                    }
                }
                ParallelSearchServicesThread t2 = new ParallelSearchServicesThread(this, uuid);
                t2.start();
                synchronized (this.inquiryCompletedEvent) {
                    if (!this.agent.startInquiry(10390323, this)) {
                        return null;
                    }
                    while (!this.inquiryCompleted) {
                        try {
                            this.inquiryCompletedEvent.wait();
                        } catch (InterruptedException e2) {
                            return null;
                        }
                    }
                    this.agent.cancelInquiry(this);
                    if (this.servRecordDiscovered == null && !t2.processedAll()) {
                        synchronized (this.serviceSearchDeviceQueue) {
                            this.serviceSearchDeviceQueue.notifyAll();
                        }
                        try {
                            t2.join();
                        } catch (InterruptedException e3) {
                            return null;
                        }
                    }
                    t2.interrupt();
                    if (this.servRecordDiscovered != null) {
                        return this.servRecordDiscovered.getConnectionURL(security, master);
                    }
                    return null;
                }
            default:
                throw new IllegalArgumentException();
        }
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/SelectServiceHandler$ParallelSearchServicesThread.class */
    private class ParallelSearchServicesThread extends Thread {
        private boolean stoped;
        private int processedNext;
        private int processedSize;
        private UUID uuid;
        private final SelectServiceHandler this$0;

        ParallelSearchServicesThread(SelectServiceHandler selectServiceHandler, UUID uuid) {
            super(new StringBuffer().append("SelectServiceThread-").append(SelectServiceHandler.nextThreadNum()).toString());
            this.this$0 = selectServiceHandler;
            this.stoped = false;
            this.processedNext = 0;
            this.processedSize = 0;
            this.uuid = uuid;
        }

        boolean processedAll() {
            return this.processedNext == this.this$0.serviceSearchDeviceQueue.size();
        }

        @Override // java.lang.Thread
        public void interrupt() {
            this.stoped = true;
            synchronized (this.this$0.serviceSearchDeviceQueue) {
                this.this$0.serviceSearchDeviceQueue.notifyAll();
            }
            super.interrupt();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.stoped && this.this$0.servRecordDiscovered == null) {
                synchronized (this.this$0.serviceSearchDeviceQueue) {
                    if (this.this$0.inquiryCompleted && this.processedSize == this.this$0.serviceSearchDeviceQueue.size()) {
                        return;
                    }
                    if (this.processedSize == this.this$0.serviceSearchDeviceQueue.size()) {
                        try {
                            this.this$0.serviceSearchDeviceQueue.wait();
                        } catch (InterruptedException e2) {
                            return;
                        }
                    }
                    this.processedSize = this.this$0.serviceSearchDeviceQueue.size();
                    for (int i2 = this.processedNext; i2 < this.processedSize; i2++) {
                        RemoteDevice btDevice = (RemoteDevice) this.this$0.serviceSearchDeviceQueue.elementAt(i2);
                        if (this.this$0.findServiceOnDevice(this.uuid, btDevice) != null) {
                            return;
                        }
                    }
                    this.processedNext = this.processedSize + 1;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ServiceRecord findServiceOnDevice(UUID uuid, RemoteDevice device) {
        if (this.devicesProcessed.containsKey(device)) {
            return null;
        }
        this.devicesProcessed.put(device, device);
        DebugLog.debug("searchServices on ", device);
        synchronized (this.serviceSearchCompletedEvent) {
            try {
                this.serviceSearchCompleted = false;
                this.agent.searchServices(null, new UUID[]{uuid}, device, this);
                while (!this.serviceSearchCompleted) {
                    try {
                        this.serviceSearchCompletedEvent.wait();
                    } catch (InterruptedException e2) {
                        return null;
                    }
                }
            } catch (BluetoothStateException e3) {
                DebugLog.error("searchServices", e3);
                return null;
            }
        }
        return this.servRecordDiscovered;
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        if (this.devicesProcessed.containsKey(btDevice)) {
            return;
        }
        synchronized (this.serviceSearchDeviceQueue) {
            this.serviceSearchDeviceQueue.addElement(btDevice);
            this.serviceSearchDeviceQueue.notifyAll();
        }
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void inquiryCompleted(int discType) {
        synchronized (this.inquiryCompletedEvent) {
            this.inquiryCompleted = true;
            this.inquiryCompletedEvent.notifyAll();
        }
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized (this.serviceSearchCompletedEvent) {
            this.serviceSearchCompleted = true;
            this.serviceSearchCompletedEvent.notifyAll();
        }
    }

    @Override // javax.bluetooth.DiscoveryListener
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if (servRecord.length > 0 && this.servRecordDiscovered == null) {
            this.servRecordDiscovered = servRecord[0];
            synchronized (this.serviceSearchCompletedEvent) {
                this.serviceSearchCompleted = true;
                this.serviceSearchCompletedEvent.notifyAll();
            }
            synchronized (this.inquiryCompletedEvent) {
                this.inquiryCompleted = true;
                this.inquiryCompletedEvent.notifyAll();
            }
        }
    }
}
