package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/CharacterData.class */
public interface CharacterData extends Node {
    String getData() throws DOMException;

    void setData(String str) throws DOMException;

    int getLength();

    String substringData(int i2, int i3) throws DOMException;

    void appendData(String str) throws DOMException;

    void insertData(int i2, String str) throws DOMException;

    void deleteData(int i2, int i3) throws DOMException;

    void replaceData(int i2, int i3, String str) throws DOMException;
}
