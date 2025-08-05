package org.icepdf.core.events;

import org.icepdf.core.pobjects.Page;

/* loaded from: icepdf-core.jar:org/icepdf/core/events/PagePaintingEvent.class */
public class PagePaintingEvent extends PageInitializingEvent {
    private int shapesCount;

    public PagePaintingEvent(Page pageSource, int shapesCount) {
        super(pageSource, false);
        this.shapesCount = shapesCount;
    }

    public PagePaintingEvent(Page pageSource, boolean interrupted) {
        super(pageSource, interrupted);
    }

    public int getShapesCount() {
        return this.shapesCount;
    }
}
