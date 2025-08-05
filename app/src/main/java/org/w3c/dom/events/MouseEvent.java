package org.w3c.dom.events;

import org.w3c.dom.views.AbstractView;

/* loaded from: rt.jar:org/w3c/dom/events/MouseEvent.class */
public interface MouseEvent extends UIEvent {
    int getScreenX();

    int getScreenY();

    int getClientX();

    int getClientY();

    boolean getCtrlKey();

    boolean getShiftKey();

    boolean getAltKey();

    boolean getMetaKey();

    short getButton();

    EventTarget getRelatedTarget();

    void initMouseEvent(String str, boolean z2, boolean z3, AbstractView abstractView, int i2, int i3, int i4, int i5, int i6, boolean z4, boolean z5, boolean z6, boolean z7, short s2, EventTarget eventTarget);
}
