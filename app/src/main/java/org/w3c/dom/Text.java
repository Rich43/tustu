package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/Text.class */
public interface Text extends CharacterData {
    Text splitText(int i2) throws DOMException;

    boolean isElementContentWhitespace();

    String getWholeText();

    Text replaceWholeText(String str) throws DOMException;
}
