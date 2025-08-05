package com.sun.xml.internal.stream.events;

import javax.xml.stream.Location;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/LocationImpl.class */
public class LocationImpl implements Location {
    String systemId;
    String publicId;
    int colNo;
    int lineNo;
    int charOffset;

    LocationImpl(Location loc) {
        this.systemId = loc.getSystemId();
        this.publicId = loc.getPublicId();
        this.lineNo = loc.getLineNumber();
        this.colNo = loc.getColumnNumber();
        this.charOffset = loc.getCharacterOffset();
    }

    @Override // javax.xml.stream.Location
    public int getCharacterOffset() {
        return this.charOffset;
    }

    @Override // javax.xml.stream.Location
    public int getColumnNumber() {
        return this.colNo;
    }

    @Override // javax.xml.stream.Location
    public int getLineNumber() {
        return this.lineNo;
    }

    @Override // javax.xml.stream.Location
    public String getPublicId() {
        return this.publicId;
    }

    @Override // javax.xml.stream.Location
    public String getSystemId() {
        return this.systemId;
    }

    public String toString() {
        StringBuffer sbuffer = new StringBuffer();
        sbuffer.append("Line number = " + getLineNumber());
        sbuffer.append("\n");
        sbuffer.append("Column number = " + getColumnNumber());
        sbuffer.append("\n");
        sbuffer.append("System Id = " + getSystemId());
        sbuffer.append("\n");
        sbuffer.append("Public Id = " + getPublicId());
        sbuffer.append("\n");
        sbuffer.append("CharacterOffset = " + getCharacterOffset());
        sbuffer.append("\n");
        return sbuffer.toString();
    }
}
