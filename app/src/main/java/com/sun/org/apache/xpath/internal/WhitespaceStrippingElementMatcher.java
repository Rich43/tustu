package com.sun.org.apache.xpath.internal;

import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/WhitespaceStrippingElementMatcher.class */
public interface WhitespaceStrippingElementMatcher {
    boolean shouldStripWhiteSpace(XPathContext xPathContext, Element element) throws TransformerException;

    boolean canStripWhiteSpace();
}
