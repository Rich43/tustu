package com.sun.corba.se.impl.activation;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ProcessMonitorThread.class */
public class ProcessMonitorThread extends Thread {
    private HashMap serverTable;
    private int sleepTime;
    private static ProcessMonitorThread instance = null;

    private ProcessMonitorThread(HashMap map, int i2) {
        this.serverTable = map;
        this.sleepTime = i2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Iterator it;
        while (true) {
            try {
                Thread.sleep(this.sleepTime);
                synchronized (this.serverTable) {
                    it = this.serverTable.values().iterator();
                }
                try {
                    checkServerHealth(it);
                } catch (ConcurrentModificationException e2) {
                    return;
                }
            } catch (InterruptedException e3) {
                return;
            }
        }
    }

    private void checkServerHealth(Iterator it) {
        if (it == null) {
            return;
        }
        while (it.hasNext()) {
            ((ServerTableEntry) it.next()).checkProcessHealth();
        }
    }

    static void start(HashMap map) {
        int i2 = 1000;
        String property = System.getProperties().getProperty(ORBConstants.SERVER_POLLING_TIME);
        if (property != null) {
            try {
                i2 = Integer.parseInt(property);
            } catch (Exception e2) {
            }
        }
        instance = new ProcessMonitorThread(map, i2);
        instance.setDaemon(true);
        instance.start();
    }

    static void interruptThread() {
        instance.interrupt();
    }
}
