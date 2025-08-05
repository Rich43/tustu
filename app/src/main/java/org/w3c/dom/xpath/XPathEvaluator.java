package org.w3c.dom.xpath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/w3c/dom/xpath/XPathEvaluator.class */
public interface XPathEvaluator {
    XPathExpression createExpression(String str, XPathNSResolver xPathNSResolver) throws DOMException, XPathException;

    XPathNSResolver createNSResolver(Node node);

    Object evaluate(String str, Node node, XPathNSResolver xPathNSResolver, short s2, Object obj) throws DOMException, XPathException;
}
