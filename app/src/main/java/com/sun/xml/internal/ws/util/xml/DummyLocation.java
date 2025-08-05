package com.sun.xml.internal.ws.util.xml;

import javax.xml.stream.Location;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/DummyLocation.class */
public final class DummyLocation implements Location {
    public static final Location INSTANCE = new DummyLocation();

    private DummyLocation() {
    }

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
