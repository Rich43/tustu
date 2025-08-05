package com.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.DOMLocator;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMLocatorImpl.class */
public class DOMLocatorImpl implements DOMLocator {
    public int fColumnNumber;
    public int fLineNumber;
    public Node fRelatedNode;
    public String fUri;
    public int fByteOffset;
    public int fUtf16Offset;

    public DOMLocatorImpl() {
        this.fColumnNumber = -1;
        this.fLineNumber = -1;
        this.fRelatedNode = null;
        this.fUri = null;
        this.fByteOffset = -1;
        this.fUtf16Offset = -1;
    }

    public DOMLocatorImpl(int lineNumber, int columnNumber, String uri) {
        this.fColumnNumber = -1;
        this.fLineNumber = -1;
        this.fRelatedNode = null;
        this.fUri = null;
        this.fByteOffset = -1;
        this.fUtf16Offset = -1;
        this.fLineNumber = lineNumber;
        this.fColumnNumber = columnNumber;
        this.fUri = uri;
    }

    public DOMLocatorImpl(int lineNumber, int columnNumber, int utf16Offset, String uri) {
        this.fColumnNumber = -1;
        this.fLineNumber = -1;
        this.fRelatedNode = null;
        this.fUri = null;
        this.fByteOffset = -1;
        this.fUtf16Offset = -1;
        this.fLineNumber = lineNumber;
        this.fColumnNumber = columnNumber;
        this.fUri = uri;
        this.fUtf16Offset = utf16Offset;
    }

    public DOMLocatorImpl(int lineNumber, int columnNumber, int byteoffset, Node relatedData, String uri) {
        this.fColumnNumber = -1;
        this.fLineNumber = -1;
        this.fRelatedNode = null;
        this.fUri = null;
        this.fByteOffset = -1;
        this.fUtf16Offset = -1;
        this.fLineNumber = lineNumber;
        this.fColumnNumber = columnNumber;
        this.fByteOffset = byteoffset;
        this.fRelatedNode = relatedData;
        this.fUri = uri;
    }

    public DOMLocatorImpl(int lineNumber, int columnNumber, int byteoffset, Node relatedData, String uri, int utf16Offset) {
        this.fColumnNumber = -1;
        this.fLineNumber = -1;
        this.fRelatedNode = null;
        this.fUri = null;
        this.fByteOffset = -1;
        this.fUtf16Offset = -1;
        this.fLineNumber = lineNumber;
        this.fColumnNumber = columnNumber;
        this.fByteOffset = byteoffset;
        this.fRelatedNode = relatedData;
        this.fUri = uri;
        this.fUtf16Offset = utf16Offset;
    }

    @Override // org.w3c.dom.DOMLocator
    public int getLineNumber() {
        return this.fLineNumber;
    }

    @Override // org.w3c.dom.DOMLocator
    public int getColumnNumber() {
        return this.fColumnNumber;
    }

    @Override // org.w3c.dom.DOMLocator
    public String getUri() {
        return this.fUri;
    }

    @Override // org.w3c.dom.DOMLocator
    public Node getRelatedNode() {
        return this.fRelatedNode;
    }

    @Override // org.w3c.dom.DOMLocator
    public int getByteOffset() {
        return this.fByteOffset;
    }

    @Override // org.w3c.dom.DOMLocator
    public int getUtf16Offset() {
        return this.fUtf16Offset;
    }
}
