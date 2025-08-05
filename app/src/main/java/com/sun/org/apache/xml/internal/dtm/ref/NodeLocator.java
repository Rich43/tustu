package com.sun.org.apache.xml.internal.dtm.ref;

import javax.xml.transform.SourceLocator;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/NodeLocator.class */
public class NodeLocator implements SourceLocator {
    protected String m_publicId;
    protected String m_systemId;
    protected int m_lineNumber;
    protected int m_columnNumber;

    public NodeLocator(String publicId, String systemId, int lineNumber, int columnNumber) {
        this.m_publicId = publicId;
        this.m_systemId = systemId;
        this.m_lineNumber = lineNumber;
        this.m_columnNumber = columnNumber;
    }

    @Override // javax.xml.transform.SourceLocator
    public String getPublicId() {
        return this.m_publicId;
    }

    @Override // javax.xml.transform.SourceLocator
    public String getSystemId() {
        return this.m_systemId;
    }

    @Override // javax.xml.transform.SourceLocator
    public int getLineNumber() {
        return this.m_lineNumber;
    }

    @Override // javax.xml.transform.SourceLocator
    public int getColumnNumber() {
        return this.m_columnNumber;
    }

    public String toString() {
        return "file '" + this.m_systemId + "', line #" + this.m_lineNumber + ", column #" + this.m_columnNumber;
    }
}
