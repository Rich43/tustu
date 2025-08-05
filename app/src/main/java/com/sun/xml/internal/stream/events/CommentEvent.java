package com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.stream.events.Comment;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/CommentEvent.class */
public class CommentEvent extends DummyEvent implements Comment {
    private String fText;

    public CommentEvent() {
        init();
    }

    public CommentEvent(String text) {
        init();
        this.fText = text;
    }

    protected void init() {
        setEventType(5);
    }

    public String toString() {
        return "<!--" + getText() + "-->";
    }

    @Override // javax.xml.stream.events.Comment
    public String getText() {
        return this.fText;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write("<!--" + getText() + "-->");
    }
}
