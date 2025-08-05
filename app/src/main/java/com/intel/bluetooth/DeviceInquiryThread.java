package com.intel.bluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryListener;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/DeviceInquiryThread.class */
class DeviceInquiryThread extends Thread {
    private BluetoothStack stack;
    private DeviceInquiryRunnable inquiryRunnable;
    private int accessCode;
    private DiscoveryListener listener;
    private BluetoothStateException startException;
    private boolean started;
    private boolean terminated;
    private Object inquiryStartedEvent;
    private static int threadNumber;

    private static synchronized int nextThreadNum() {
        int i2 = threadNumber;
        threadNumber = i2 + 1;
        return i2;
    }

    private DeviceInquiryThread(BluetoothStack stack, DeviceInquiryRunnable inquiryRunnable, int accessCode, DiscoveryListener listener) {
        super(new StringBuffer().append("DeviceInquiryThread-").append(nextThreadNum()).toString());
        this.started = false;
        this.terminated = false;
        this.inquiryStartedEvent = new Object();
        this.stack = stack;
        this.inquiryRunnable = inquiryRunnable;
        this.accessCode = accessCode;
        this.listener = listener;
    }

    static boolean startInquiry(BluetoothStack stack, DeviceInquiryRunnable inquiryRunnable, int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        DeviceInquiryThread t2 = new DeviceInquiryThread(stack, inquiryRunnable, accessCode, listener);
        UtilsJavaSE.threadSetDaemon(t2);
        synchronized (t2.inquiryStartedEvent) {
            t2.start();
            while (!t2.started && !t2.terminated) {
                try {
                    t2.inquiryStartedEvent.wait();
                    if (t2.startException != null) {
                        throw t2.startException;
                    }
                } catch (InterruptedException e2) {
                    return false;
                }
            }
        }
        DebugLog.debug("startInquiry return", t2.started);
        return t2.started;
    }

    public static int getConfigDeviceInquiryDuration() {
        return BlueCoveImpl.getConfigProperty(BlueCoveConfigProperties.PROPERTY_INQUIRY_DURATION, 11);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int discType = 7;
        try {
            try {
                BlueCoveImpl.setThreadBluetoothStack(this.stack);
                discType = this.inquiryRunnable.runDeviceInquiry(this, this.accessCode, this.listener);
                this.terminated = true;
                synchronized (this.inquiryStartedEvent) {
                    this.inquiryStartedEvent.notifyAll();
                }
                DebugLog.debug("runDeviceInquiry ends");
                if (this.started) {
                    Utils.j2meUsagePatternDellay();
                    this.listener.inquiryCompleted(discType);
                }
            } catch (BluetoothStateException e2) {
                DebugLog.debug("runDeviceInquiry throw", (Throwable) e2);
                this.startException = e2;
                this.terminated = true;
                synchronized (this.inquiryStartedEvent) {
                    this.inquiryStartedEvent.notifyAll();
                    DebugLog.debug("runDeviceInquiry ends");
                    if (this.started) {
                        Utils.j2meUsagePatternDellay();
                        this.listener.inquiryCompleted(discType);
                    }
                }
            } catch (Throwable e3) {
                DebugLog.error("runDeviceInquiry", e3);
                this.terminated = true;
                synchronized (this.inquiryStartedEvent) {
                    this.inquiryStartedEvent.notifyAll();
                    DebugLog.debug("runDeviceInquiry ends");
                    if (this.started) {
                        Utils.j2meUsagePatternDellay();
                        this.listener.inquiryCompleted(discType);
                    }
                }
            }
        } catch (Throwable th) {
            this.terminated = true;
            synchronized (this.inquiryStartedEvent) {
                this.inquiryStartedEvent.notifyAll();
                DebugLog.debug("runDeviceInquiry ends");
                if (this.started) {
                    Utils.j2meUsagePatternDellay();
                    this.listener.inquiryCompleted(discType);
                }
                throw th;
            }
        }
    }

    public void deviceInquiryStartedCallback() {
        DebugLog.debug("deviceInquiryStartedCallback");
        this.started = true;
        synchronized (this.inquiryStartedEvent) {
            this.inquiryStartedEvent.notifyAll();
        }
    }
}
