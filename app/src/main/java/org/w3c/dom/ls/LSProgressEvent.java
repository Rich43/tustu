package org.w3c.dom.ls;

import org.w3c.dom.events.Event;

/* loaded from: rt.jar:org/w3c/dom/ls/LSProgressEvent.class */
public interface LSProgressEvent extends Event {
    LSInput getInput();

    int getPosition();

    int getTotalSize();
}
