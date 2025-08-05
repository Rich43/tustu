package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/Attr.class */
public interface Attr extends Node {
    String getName();

    boolean getSpecified();

    String getValue();

    void setValue(String str) throws DOMException;

    Element getOwnerElement();

    TypeInfo getSchemaTypeInfo();

    boolean isId();
}
