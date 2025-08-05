package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/DOMLocator.class */
public interface DOMLocator {
    int getLineNumber();

    int getColumnNumber();

    int getByteOffset();

    int getUtf16Offset();

    Node getRelatedNode();

    String getUri();
}
