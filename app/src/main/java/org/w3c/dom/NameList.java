package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/NameList.class */
public interface NameList {
    String getName(int i2);

    String getNamespaceURI(int i2);

    int getLength();

    boolean contains(String str);

    boolean containsNS(String str, String str2);
}
