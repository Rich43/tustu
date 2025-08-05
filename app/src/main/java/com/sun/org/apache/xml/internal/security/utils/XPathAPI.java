package com.sun.org.apache.xml.internal.security.utils;

import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/XPathAPI.class */
public interface XPathAPI {
    NodeList selectNodeList(Node node, Node node2, String str, Node node3) throws TransformerException;

    boolean evaluate(Node node, Node node2, String str, Node node3) throws TransformerException;

    void clear();
}
