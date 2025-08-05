package com.sun.org.apache.xpath.internal;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/XPathVisitable.class */
public interface XPathVisitable {
    void callVisitors(ExpressionOwner expressionOwner, XPathVisitor xPathVisitor);
}
