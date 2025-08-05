package com.sun.xml.internal.fastinfoset.stax;

import javax.xml.stream.Location;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/EventLocation.class */
public class EventLocation implements Location {
    String _systemId = null;
    String _publicId = null;
    int _column = -1;
    int _line = -1;
    int _charOffset = -1;

    EventLocation() {
    }

    public static Location getNilLocation() {
        return new EventLocation();
    }

    @Override // javax.xml.stream.Location
    public int getLineNumber() {
        return this._line;
    }

    @Override // javax.xml.stream.Location
    public int getColumnNumber() {
        return this._column;
    }

    @Override // javax.xml.stream.Location
    public int getCharacterOffset() {
        return this._charOffset;
    }

    @Override // javax.xml.stream.Location
    public String getPublicId() {
        return this._publicId;
    }

    @Override // javax.xml.stream.Location
    public String getSystemId() {
        return this._systemId;
    }

    public void setLineNumber(int line) {
        this._line = line;
    }

    public void setColumnNumber(int col) {
        this._column = col;
    }

    public void setCharacterOffset(int offset) {
        this._charOffset = offset;
    }

    public void setPublicId(String id) {
        this._publicId = id;
    }

    public void setSystemId(String id) {
        this._systemId = id;
    }

    public String toString() {
        StringBuffer sbuffer = new StringBuffer();
        sbuffer.append("Line number = " + this._line);
        sbuffer.append("\n");
        sbuffer.append("Column number = " + this._column);
        sbuffer.append("\n");
        sbuffer.append("System Id = " + this._systemId);
        sbuffer.append("\n");
        sbuffer.append("Public Id = " + this._publicId);
        sbuffer.append("\n");
        sbuffer.append("CharacterOffset = " + this._charOffset);
        sbuffer.append("\n");
        return sbuffer.toString();
    }
}
