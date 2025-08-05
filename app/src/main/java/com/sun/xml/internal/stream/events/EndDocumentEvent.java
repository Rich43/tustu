package com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.events.EndDocument;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/EndDocumentEvent.class */
public class EndDocumentEvent extends DummyEvent implements EndDocument {
    public EndDocumentEvent() {
        init();
    }

    protected void init() {
        setEventType(8);
    }

    public String toString() {
        return "ENDDOCUMENT";
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
    }
}
