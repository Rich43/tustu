package org.icepdf.core.events;

import org.icepdf.core.pobjects.Page;

/* loaded from: icepdf-core.jar:org/icepdf/core/events/PageImageEvent.class */
public class PageImageEvent extends PageInitializingEvent {
    private int index;
    private int total;
    private long duration;

    public PageImageEvent(Page pageSource, int index, int total, long duration, boolean interrupted) {
        super(pageSource, interrupted);
        this.index = index;
        this.total = total;
        this.duration = duration;
    }

    public int getIndex() {
        return this.index;
    }

    public int getTotal() {
        return this.total;
    }

    public long getDuration() {
        return this.duration;
    }
}
