package com.sun.java.accessibility.util;

import java.awt.AWTEvent;

/* compiled from: EventQueueMonitor.java */
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/EventQueueMonitorItem.class */
class EventQueueMonitorItem {
    AWTEvent event;
    EventQueueMonitorItem next = null;

    EventQueueMonitorItem(AWTEvent aWTEvent) {
        this.event = aWTEvent;
    }
}
