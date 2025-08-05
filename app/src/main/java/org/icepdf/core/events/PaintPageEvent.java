package org.icepdf.core.events;

import java.util.EventObject;
import org.icepdf.core.pobjects.Page;

/* loaded from: icepdf-core.jar:org/icepdf/core/events/PaintPageEvent.class */
public class PaintPageEvent extends EventObject {
    public PaintPageEvent(Page pageSource) {
        super(pageSource);
    }
}
