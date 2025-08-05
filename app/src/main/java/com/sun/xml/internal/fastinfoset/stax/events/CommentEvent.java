package com.sun.xml.internal.fastinfoset.stax.events;

import javax.xml.stream.events.Comment;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/CommentEvent.class */
public class CommentEvent extends EventBase implements Comment {
    private String _text;

    public CommentEvent() {
        super(5);
    }

    public CommentEvent(String text) {
        this();
        this._text = text;
    }

    public String toString() {
        return "<!--" + this._text + "-->";
    }

    @Override // javax.xml.stream.events.Comment
    public String getText() {
        return this._text;
    }

    public void setText(String text) {
        this._text = text;
    }
}
