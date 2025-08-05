package com.sun.org.apache.xalan.internal.xsltc.runtime;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import org.xml.sax.AttributeList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/runtime/Attributes.class */
public final class Attributes implements AttributeList {
    private int _element;
    private DOM _document;

    public Attributes(DOM document, int element) {
        this._element = element;
        this._document = document;
    }

    @Override // org.xml.sax.AttributeList
    public int getLength() {
        return 0;
    }

    @Override // org.xml.sax.AttributeList
    public String getName(int i2) {
        return null;
    }

    @Override // org.xml.sax.AttributeList
    public String getType(int i2) {
        return null;
    }

    @Override // org.xml.sax.AttributeList
    public String getType(String name) {
        return null;
    }

    @Override // org.xml.sax.AttributeList
    public String getValue(int i2) {
        return null;
    }

    @Override // org.xml.sax.AttributeList
    public String getValue(String name) {
        return null;
    }
}
