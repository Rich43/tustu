package com.sun.jndi.ldap;

import java.util.EventObject;
import java.util.Vector;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.NamingListener;
import javax.naming.ldap.UnsolicitedNotificationEvent;
import javax.naming.ldap.UnsolicitedNotificationListener;

/* loaded from: rt.jar:com/sun/jndi/ldap/EventQueue.class */
final class EventQueue implements Runnable {
    private static final boolean debug = false;
    private QueueElement head = null;
    private QueueElement tail = null;
    private Thread qThread = Obj.helper.createThread(this);

    /* loaded from: rt.jar:com/sun/jndi/ldap/EventQueue$QueueElement.class */
    private static class QueueElement {
        QueueElement next = null;
        QueueElement prev = null;
        EventObject event;
        Vector<NamingListener> vector;

        QueueElement(EventObject eventObject, Vector<NamingListener> vector) {
            this.event = null;
            this.vector = null;
            this.event = eventObject;
            this.vector = vector;
        }
    }

    EventQueue() {
        this.qThread.setDaemon(true);
        this.qThread.start();
    }

    synchronized void enqueue(EventObject eventObject, Vector<NamingListener> vector) {
        QueueElement queueElement = new QueueElement(eventObject, vector);
        if (this.head == null) {
            this.head = queueElement;
            this.tail = queueElement;
        } else {
            queueElement.next = this.head;
            this.head.prev = queueElement;
            this.head = queueElement;
        }
        notify();
    }

    private synchronized QueueElement dequeue() throws InterruptedException {
        while (this.tail == null) {
            wait();
        }
        QueueElement queueElement = this.tail;
        this.tail = queueElement.prev;
        if (this.tail == null) {
            this.head = null;
        } else {
            this.tail.next = null;
        }
        queueElement.next = null;
        queueElement.prev = null;
        return queueElement;
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                QueueElement queueElementDequeue = dequeue();
                if (queueElementDequeue != null) {
                    EventObject eventObject = queueElementDequeue.event;
                    Vector<NamingListener> vector = queueElementDequeue.vector;
                    for (int i2 = 0; i2 < vector.size(); i2++) {
                        if (eventObject instanceof NamingEvent) {
                            ((NamingEvent) eventObject).dispatch(vector.elementAt(i2));
                        } else if (eventObject instanceof NamingExceptionEvent) {
                            ((NamingExceptionEvent) eventObject).dispatch(vector.elementAt(i2));
                        } else if (eventObject instanceof UnsolicitedNotificationEvent) {
                            ((UnsolicitedNotificationEvent) eventObject).dispatch((UnsolicitedNotificationListener) vector.elementAt(i2));
                        }
                    }
                } else {
                    return;
                }
            } catch (InterruptedException e2) {
                return;
            }
        }
    }

    void stop() {
        if (this.qThread != null) {
            this.qThread.interrupt();
            this.qThread = null;
        }
    }
}
