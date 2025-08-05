package com.sun.org.apache.xpath.internal;

import javax.xml.transform.SourceLocator;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/ExpressionNode.class */
public interface ExpressionNode extends SourceLocator {
    void exprSetParent(ExpressionNode expressionNode);

    ExpressionNode exprGetParent();

    void exprAddChild(ExpressionNode expressionNode, int i2);

    ExpressionNode exprGetChild(int i2);

    int exprGetNumChildren();
}
