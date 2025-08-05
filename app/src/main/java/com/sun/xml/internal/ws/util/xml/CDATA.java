package com.sun.xml.internal.ws.util.xml;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/CDATA.class */
public final class CDATA {
    private String _text;

    public CDATA(String text) {
        this._text = text;
    }

    public String getText() {
        return this._text;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CDATA)) {
            return false;
        }
        CDATA cdata = (CDATA) obj;
        return this._text.equals(cdata._text);
    }

    public int hashCode() {
        return this._text.hashCode();
    }
}
