package org.w3c.dom.xpath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/w3c/dom/xpath/XPathExpression.class */
public interface XPathExpression {
    Object evaluate(Node node, short s2, Object obj) throws DOMException, XPathException;
}
