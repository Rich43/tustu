package org.w3c.dom.ls;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/w3c/dom/ls/LSSerializer.class */
public interface LSSerializer {
    DOMConfiguration getDomConfig();

    String getNewLine();

    void setNewLine(String str);

    LSSerializerFilter getFilter();

    void setFilter(LSSerializerFilter lSSerializerFilter);

    boolean write(Node node, LSOutput lSOutput) throws LSException;

    boolean writeToURI(Node node, String str) throws LSException;

    String writeToString(Node node) throws DOMException, LSException;
}
