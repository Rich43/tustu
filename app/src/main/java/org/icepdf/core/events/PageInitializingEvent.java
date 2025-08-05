package org.icepdf.core.events;

import java.util.EventObject;

/* loaded from: icepdf-core.jar:org/icepdf/core/events/PageInitializingEvent.class */
public class PageInitializingEvent extends EventObject {
    private boolean interrupted;

    public PageInitializingEvent(Object source, boolean interrupted) {
        super(source);
        this.interrupted = interrupted;
    }

    public boolean isInterrupted() {
        return this.interrupted;
    }
}
