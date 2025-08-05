package com.sun.xml.internal.stream.events;

import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.IOException;
import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/DummyEvent.class */
public abstract class DummyEvent implements XMLEvent {
    private static DummyLocation nowhere = new DummyLocation();
    private int fEventType;
    protected Location fLocation = nowhere;

    protected abstract void writeAsEncodedUnicodeEx(Writer writer) throws XMLStreamException, IOException;

    public DummyEvent() {
    }

    public DummyEvent(int i2) {
        this.fEventType = i2;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public int getEventType() {
        return this.fEventType;
    }

    protected void setEventType(int eventType) {
        this.fEventType = eventType;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isStartElement() {
        return this.fEventType == 1;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isEndElement() {
        return this.fEventType == 2;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isEntityReference() {
        return this.fEventType == 9;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isProcessingInstruction() {
        return this.fEventType == 3;
    }

    public boolean isCharacterData() {
        return this.fEventType == 4;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isStartDocument() {
        return this.fEventType == 7;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isEndDocument() {
        return this.fEventType == 8;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public Location getLocation() {
        return this.fLocation;
    }

    void setLocation(Location loc) {
        if (loc == null) {
            this.fLocation = nowhere;
        } else {
            this.fLocation = loc;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.xml.stream.events.XMLEvent
    public Characters asCharacters() {
        return (Characters) this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.xml.stream.events.XMLEvent
    public EndElement asEndElement() {
        return (EndElement) this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.xml.stream.events.XMLEvent
    public StartElement asStartElement() {
        return (StartElement) this;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public QName getSchemaType() {
        return null;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isAttribute() {
        return this.fEventType == 10;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isCharacters() {
        return this.fEventType == 4;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public boolean isNamespace() {
        return this.fEventType == 13;
    }

    @Override // javax.xml.stream.events.XMLEvent
    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {
        try {
            writeAsEncodedUnicodeEx(writer);
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    protected void charEncode(Writer writer, String data) throws IOException {
        if (data == null || data == "") {
            return;
        }
        int start = 0;
        int len = data.length();
        for (int i2 = 0; i2 < len; i2++) {
            switch (data.charAt(i2)) {
                case '\"':
                    writer.write(data, start, i2 - start);
                    writer.write(SerializerConstants.ENTITY_QUOT);
                    start = i2 + 1;
                    break;
                case '&':
                    writer.write(data, start, i2 - start);
                    writer.write(SerializerConstants.ENTITY_AMP);
                    start = i2 + 1;
                    break;
                case '<':
                    writer.write(data, start, i2 - start);
                    writer.write(SerializerConstants.ENTITY_LT);
                    start = i2 + 1;
                    break;
                case '>':
                    writer.write(data, start, i2 - start);
                    writer.write(SerializerConstants.ENTITY_GT);
                    start = i2 + 1;
                    break;
            }
        }
        writer.write(data, start, len - start);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/events/DummyEvent$DummyLocation.class */
    static class DummyLocation implements Location {
        @Override // javax.xml.stream.Location
        public int getCharacterOffset() {
            return -1;
        }

        @Override // javax.xml.stream.Location
        public int getColumnNumber() {
            return -1;
        }

        @Override // javax.xml.stream.Location
        public int getLineNumber() {
            return -1;
        }

        @Override // javax.xml.stream.Location
        public String getPublicId() {
            return null;
        }

        @Override // javax.xml.stream.Location
        public String getSystemId() {
            return null;
        }
    }
}
