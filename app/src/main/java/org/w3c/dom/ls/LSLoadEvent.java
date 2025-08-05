package org.w3c.dom.ls;

import org.w3c.dom.Document;
import org.w3c.dom.events.Event;

/* loaded from: rt.jar:org/w3c/dom/ls/LSLoadEvent.class */
public interface LSLoadEvent extends Event {
    Document getNewDocument();

    LSInput getInput();
}
