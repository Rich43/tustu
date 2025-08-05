package com.intel.bluetooth;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/SearchServicesThread.class */
class SearchServicesThread extends Thread {
    private static int transIDGenerator = 0;
    private static Hashtable threads = new Hashtable();
    private BluetoothStack stack;
    private SearchServicesRunnable serachRunnable;
    private int transID;
    private int[] attrSet;
    private Vector servicesRecords;
    UUID[] uuidSet;
    private RemoteDevice device;
    private DiscoveryListener listener;
    private BluetoothStateException startException;
    private boolean started;
    private boolean finished;
    private boolean terminated;
    private Object serviceSearchStartedEvent;

    private static synchronized int nextThreadNum() {
        int i2 = transIDGenerator + 1;
        transIDGenerator = i2;
        return i2;
    }

    private SearchServicesThread(int transID, BluetoothStack stack, SearchServicesRunnable serachRunnable, int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) {
        super(new StringBuffer().append("SearchServicesThread-").append(transID).toString());
        this.servicesRecords = new Vector();
        this.started = false;
        this.finished = false;
        this.terminated = false;
        this.serviceSearchStartedEvent = new Object();
        this.stack = stack;
        this.serachRunnable = serachRunnable;
        this.transID = transID;
        this.attrSet = attrSet;
        this.listener = listener;
        this.uuidSet = uuidSet;
        this.device = RemoteDeviceHelper.getStackBoundDevice(stack, device);
    }

    static int startSearchServices(BluetoothStack stack, SearchServicesRunnable searchRunnable, int[] attrSet, UUID[] uuidSet, RemoteDevice device, DiscoveryListener listener) throws BluetoothStateException {
        SearchServicesThread t2;
        synchronized (threads) {
            int runningCount = countRunningSearchServicesThreads(stack);
            int concurrentAllow = Integer.valueOf(stack.getLocalDeviceProperty(BluetoothConsts.PROPERTY_BLUETOOTH_SD_TRANS_MAX)).intValue();
            if (runningCount >= concurrentAllow) {
                throw new BluetoothStateException(new StringBuffer().append("Already running ").append(runningCount).append(" service discovery transactions").toString());
            }
            t2 = new SearchServicesThread(nextThreadNum(), stack, searchRunnable, attrSet, uuidSet, device, listener);
            threads.put(new Integer(t2.getTransID()), t2);
        }
        UtilsJavaSE.threadSetDaemon(t2);
        synchronized (t2.serviceSearchStartedEvent) {
            t2.start();
            while (!t2.started && !t2.finished) {
                try {
                    t2.serviceSearchStartedEvent.wait();
                    if (t2.startException != null) {
                        throw t2.startException;
                    }
                } catch (InterruptedException e2) {
                    return 0;
                }
            }
        }
        if (t2.started) {
            return t2.getTransID();
        }
        throw new BluetoothStateException();
    }

    private static int countRunningSearchServicesThreads(BluetoothStack stack) {
        int count = 0;
        Enumeration en = threads.elements();
        while (en.hasMoreElements()) {
            SearchServicesThread t2 = (SearchServicesThread) en.nextElement();
            if (t2.stack == stack) {
                count++;
            }
        }
        return count;
    }

    static SearchServicesThread getServiceSearchThread(int transID) {
        return (SearchServicesThread) threads.get(new Integer(transID));
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int respCode = 3;
        try {
            try {
                BlueCoveImpl.setThreadBluetoothStack(this.stack);
                respCode = this.serachRunnable.runSearchServices(this, this.attrSet, this.uuidSet, this.device, this.listener);
                this.finished = true;
                unregisterThread();
                synchronized (this.serviceSearchStartedEvent) {
                    this.serviceSearchStartedEvent.notifyAll();
                }
                DebugLog.debug("runSearchServices ends", getTransID());
                if (this.started) {
                    Utils.j2meUsagePatternDellay();
                    this.listener.serviceSearchCompleted(getTransID(), respCode);
                }
            } catch (BluetoothStateException e2) {
                this.startException = e2;
                this.finished = true;
                unregisterThread();
                synchronized (this.serviceSearchStartedEvent) {
                    this.serviceSearchStartedEvent.notifyAll();
                    DebugLog.debug("runSearchServices ends", getTransID());
                    if (this.started) {
                        Utils.j2meUsagePatternDellay();
                        this.listener.serviceSearchCompleted(getTransID(), respCode);
                    }
                }
            }
        } catch (Throwable th) {
            this.finished = true;
            unregisterThread();
            synchronized (this.serviceSearchStartedEvent) {
                this.serviceSearchStartedEvent.notifyAll();
                DebugLog.debug("runSearchServices ends", getTransID());
                if (this.started) {
                    Utils.j2meUsagePatternDellay();
                    this.listener.serviceSearchCompleted(getTransID(), respCode);
                }
                throw th;
            }
        }
    }

    private void unregisterThread() {
        synchronized (threads) {
            threads.remove(new Integer(getTransID()));
        }
    }

    public void searchServicesStartedCallback() {
        DebugLog.debug("searchServicesStartedCallback", getTransID());
        this.started = true;
        synchronized (this.serviceSearchStartedEvent) {
            this.serviceSearchStartedEvent.notifyAll();
        }
    }

    int getTransID() {
        return this.transID;
    }

    boolean setTerminated() {
        if (isTerminated()) {
            return false;
        }
        this.terminated = true;
        unregisterThread();
        return true;
    }

    boolean isTerminated() {
        return this.terminated;
    }

    RemoteDevice getDevice() {
        return this.device;
    }

    DiscoveryListener getListener() {
        return this.listener;
    }

    void addServicesRecords(ServiceRecord servRecord) {
        this.servicesRecords.addElement(servRecord);
    }

    Vector getServicesRecords() {
        return this.servicesRecords;
    }

    public int[] getAttrSet() {
        int[] requiredAttrIDs = {0, 1, 2, 3, 4};
        if (this.attrSet == null) {
            return requiredAttrIDs;
        }
        int len = requiredAttrIDs.length + this.attrSet.length;
        for (int i2 = 0; i2 < this.attrSet.length; i2++) {
            int k2 = 0;
            while (true) {
                if (k2 >= requiredAttrIDs.length) {
                    break;
                }
                if (requiredAttrIDs[k2] == this.attrSet[i2]) {
                    len--;
                    break;
                }
                k2++;
            }
        }
        int[] allIDs = new int[len];
        System.arraycopy(requiredAttrIDs, 0, allIDs, 0, requiredAttrIDs.length);
        int appendPosition = requiredAttrIDs.length;
        for (int i3 = 0; i3 < this.attrSet.length; i3++) {
            int k3 = 0;
            while (true) {
                if (k3 < requiredAttrIDs.length) {
                    if (requiredAttrIDs[k3] == this.attrSet[i3]) {
                        break;
                    }
                    k3++;
                } else {
                    allIDs[appendPosition] = this.attrSet[i3];
                    appendPosition++;
                    break;
                }
            }
        }
        return allIDs;
    }
}
