package com.sun.javafx.event;

import java.util.ArrayDeque;
import java.util.Queue;
import javafx.event.Event;

/* loaded from: jfxrt.jar:com/sun/javafx/event/EventQueue.class */
public final class EventQueue {
    private Queue<Event> queue = new ArrayDeque();
    private boolean inLoop;

    public void postEvent(Event event) {
        this.queue.add(event);
    }

    public void fire() {
        if (this.inLoop) {
            return;
        }
        this.inLoop = true;
        while (!this.queue.isEmpty()) {
            try {
                Event top = this.queue.remove();
                Event.fireEvent(top.getTarget(), top);
            } finally {
                this.inLoop = false;
            }
        }
    }
}
