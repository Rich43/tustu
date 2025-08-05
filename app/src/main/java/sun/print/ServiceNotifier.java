package sun.print;

import java.util.Vector;
import javax.print.PrintService;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.event.PrintServiceAttributeEvent;
import javax.print.event.PrintServiceAttributeListener;

/* loaded from: rt.jar:sun/print/ServiceNotifier.class */
class ServiceNotifier extends Thread {
    private PrintService service;
    private Vector listeners;
    private boolean stop;
    private PrintServiceAttributeSet lastSet;

    ServiceNotifier(PrintService printService) {
        super(printService.getName() + " notifier");
        this.stop = false;
        this.service = printService;
        this.listeners = new Vector();
        try {
            setPriority(4);
            setDaemon(true);
            start();
        } catch (SecurityException e2) {
        }
    }

    void addListener(PrintServiceAttributeListener printServiceAttributeListener) {
        synchronized (this) {
            if (printServiceAttributeListener != null) {
                if (this.listeners != null) {
                    this.listeners.add(printServiceAttributeListener);
                }
            }
        }
    }

    void removeListener(PrintServiceAttributeListener printServiceAttributeListener) {
        synchronized (this) {
            if (printServiceAttributeListener != null) {
                if (this.listeners != null) {
                    this.listeners.remove(printServiceAttributeListener);
                }
            }
        }
    }

    boolean isEmpty() {
        return this.listeners == null || this.listeners.isEmpty();
    }

    void stopNotifier() {
        this.stop = true;
    }

    void wake() {
        try {
            interrupt();
        } catch (SecurityException e2) {
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        PrintServiceAttributeSet attributes;
        long jCurrentTimeMillis = 2000;
        while (!this.stop) {
            try {
                Thread.sleep(jCurrentTimeMillis);
            } catch (InterruptedException e2) {
            }
            synchronized (this) {
                if (this.listeners != null) {
                    long jCurrentTimeMillis2 = System.currentTimeMillis();
                    if (this.listeners != null) {
                        if (this.service instanceof AttributeUpdater) {
                            attributes = ((AttributeUpdater) this.service).getUpdatedAttributes();
                        } else {
                            attributes = this.service.getAttributes();
                        }
                        if (attributes != null && !attributes.isEmpty()) {
                            for (int i2 = 0; i2 < this.listeners.size(); i2++) {
                                ((PrintServiceAttributeListener) this.listeners.elementAt(i2)).attributeUpdate(new PrintServiceAttributeEvent(this.service, new HashPrintServiceAttributeSet(attributes)));
                            }
                        }
                    }
                    jCurrentTimeMillis = (System.currentTimeMillis() - jCurrentTimeMillis2) * 10;
                    if (jCurrentTimeMillis < 15000) {
                        jCurrentTimeMillis = 15000;
                    }
                }
            }
        }
    }
}
