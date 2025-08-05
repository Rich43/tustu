package org.icepdf.core.events;

import org.icepdf.core.pobjects.Page;

/* loaded from: icepdf-core.jar:org/icepdf/core/events/PageLoadingEvent.class */
public class PageLoadingEvent extends PageInitializingEvent {
    private int contentStreamCount;
    private int imageResourceCount;

    public PageLoadingEvent(Page pageSource, int contentStreamCount, int imageResourceCount) {
        super(pageSource, false);
        this.contentStreamCount = contentStreamCount;
        this.imageResourceCount = imageResourceCount;
    }

    public PageLoadingEvent(Page pageSource, boolean interrupted) {
        super(pageSource, interrupted);
    }

    public int getContentStreamCount() {
        return this.contentStreamCount;
    }

    public int getImageResourceCount() {
        return this.imageResourceCount;
    }
}
