package com.sun.xml.internal.org.jvnet.mimepull;

import java.nio.ByteBuffer;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEEvent.class */
abstract class MIMEEvent {
    static final StartMessage START_MESSAGE = new StartMessage();
    static final StartPart START_PART = new StartPart();
    static final EndPart END_PART = new EndPart();
    static final EndMessage END_MESSAGE = new EndMessage();

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEEvent$EVENT_TYPE.class */
    enum EVENT_TYPE {
        START_MESSAGE,
        START_PART,
        HEADERS,
        CONTENT,
        END_PART,
        END_MESSAGE
    }

    abstract EVENT_TYPE getEventType();

    MIMEEvent() {
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEEvent$StartMessage.class */
    static final class StartMessage extends MIMEEvent {
        StartMessage() {
        }

        @Override // com.sun.xml.internal.org.jvnet.mimepull.MIMEEvent
        EVENT_TYPE getEventType() {
            return EVENT_TYPE.START_MESSAGE;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEEvent$StartPart.class */
    static final class StartPart extends MIMEEvent {
        StartPart() {
        }

        @Override // com.sun.xml.internal.org.jvnet.mimepull.MIMEEvent
        EVENT_TYPE getEventType() {
            return EVENT_TYPE.START_PART;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEEvent$EndPart.class */
    static final class EndPart extends MIMEEvent {
        EndPart() {
        }

        @Override // com.sun.xml.internal.org.jvnet.mimepull.MIMEEvent
        EVENT_TYPE getEventType() {
            return EVENT_TYPE.END_PART;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEEvent$Headers.class */
    static final class Headers extends MIMEEvent {
        InternetHeaders ih;

        Headers(InternetHeaders ih) {
            this.ih = ih;
        }

        @Override // com.sun.xml.internal.org.jvnet.mimepull.MIMEEvent
        EVENT_TYPE getEventType() {
            return EVENT_TYPE.HEADERS;
        }

        InternetHeaders getHeaders() {
            return this.ih;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEEvent$Content.class */
    static final class Content extends MIMEEvent {
        private final ByteBuffer buf;

        Content(ByteBuffer buf) {
            this.buf = buf;
        }

        @Override // com.sun.xml.internal.org.jvnet.mimepull.MIMEEvent
        EVENT_TYPE getEventType() {
            return EVENT_TYPE.CONTENT;
        }

        ByteBuffer getData() {
            return this.buf;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEEvent$EndMessage.class */
    static final class EndMessage extends MIMEEvent {
        EndMessage() {
        }

        @Override // com.sun.xml.internal.org.jvnet.mimepull.MIMEEvent
        EVENT_TYPE getEventType() {
            return EVENT_TYPE.END_MESSAGE;
        }
    }
}
