package com.sun.java.accessibility.util;

import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/* compiled from: EventQueueMonitor.java */
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/ComponentEvtDispatchThread.class */
class ComponentEvtDispatchThread extends Thread {
    public ComponentEvtDispatchThread(String str) {
        super(str);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        ComponentEvent componentEvent;
        while (true) {
            synchronized (EventQueueMonitor.componentEventQueueLock) {
                while (EventQueueMonitor.componentEventQueue == null) {
                    try {
                        EventQueueMonitor.componentEventQueueLock.wait();
                    } catch (InterruptedException e2) {
                    }
                }
                componentEvent = (ComponentEvent) EventQueueMonitor.componentEventQueue.event;
                EventQueueMonitor.componentEventQueue = EventQueueMonitor.componentEventQueue.next;
            }
            switch (componentEvent.getID()) {
                case 205:
                    EventQueueMonitor.maybeNotifyAssistiveTechnologies();
                    EventQueueMonitor.topLevelWindowWithFocus = ((WindowEvent) componentEvent).getWindow();
                    break;
                case 503:
                case 506:
                    EventQueueMonitor.updateCurrentMousePosition((MouseEvent) componentEvent);
                    break;
            }
        }
    }
}
