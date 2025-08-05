package com.sun.xml.internal.fastinfoset.stax.events;

import javax.xml.stream.events.EndDocument;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/EndDocumentEvent.class */
public class EndDocumentEvent extends EventBase implements EndDocument {
    public EndDocumentEvent() {
        super(8);
    }

    public String toString() {
        return "<? EndDocument ?>";
    }
}
