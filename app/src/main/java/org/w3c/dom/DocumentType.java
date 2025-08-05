package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/DocumentType.class */
public interface DocumentType extends Node {
    String getName();

    NamedNodeMap getEntities();

    NamedNodeMap getNotations();

    String getPublicId();

    String getSystemId();

    String getInternalSubset();
}
