package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.xni.XMLLocator;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/SimpleLocator.class */
public class SimpleLocator implements XMLLocator {
    String lsid;
    String esid;
    int line;
    int column;
    int charOffset;

    public SimpleLocator() {
    }

    public SimpleLocator(String lsid, String esid, int line, int column) {
        this(lsid, esid, line, column, -1);
    }

    public void setValues(String lsid, String esid, int line, int column) {
        setValues(lsid, esid, line, column, -1);
    }

    public SimpleLocator(String lsid, String esid, int line, int column, int offset) {
        this.line = line;
        this.column = column;
        this.lsid = lsid;
        this.esid = esid;
        this.charOffset = offset;
    }

    public void setValues(String lsid, String esid, int line, int column, int offset) {
        this.line = line;
        this.column = column;
        this.lsid = lsid;
        this.esid = esid;
        this.charOffset = offset;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public int getLineNumber() {
        return this.line;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public int getColumnNumber() {
        return this.column;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public int getCharacterOffset() {
        return this.charOffset;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getPublicId() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getExpandedSystemId() {
        return this.esid;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getLiteralSystemId() {
        return this.lsid;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getBaseSystemId() {
        return null;
    }

    public void setColumnNumber(int col) {
        this.column = col;
    }

    public void setLineNumber(int line) {
        this.line = line;
    }

    public void setCharacterOffset(int offset) {
        this.charOffset = offset;
    }

    public void setBaseSystemId(String systemId) {
    }

    public void setExpandedSystemId(String systemId) {
        this.esid = systemId;
    }

    public void setLiteralSystemId(String systemId) {
        this.lsid = systemId;
    }

    public void setPublicId(String publicId) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getEncoding() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getXMLVersion() {
        return null;
    }
}
