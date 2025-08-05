package org.w3c.dom.events;

import org.w3c.dom.views.AbstractView;

/* loaded from: rt.jar:org/w3c/dom/events/UIEvent.class */
public interface UIEvent extends Event {
    AbstractView getView();

    int getDetail();

    void initUIEvent(String str, boolean z2, boolean z3, AbstractView abstractView, int i2);
}
