package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/Entity.class */
public interface Entity extends Node {
    String getPublicId();

    String getSystemId();

    String getNotationName();

    String getInputEncoding();

    String getXmlEncoding();

    String getXmlVersion();
}
