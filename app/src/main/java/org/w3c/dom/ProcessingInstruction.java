package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/ProcessingInstruction.class */
public interface ProcessingInstruction extends Node {
    String getTarget();

    String getData();

    void setData(String str) throws DOMException;
}
