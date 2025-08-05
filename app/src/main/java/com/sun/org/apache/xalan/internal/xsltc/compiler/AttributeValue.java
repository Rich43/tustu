package com.sun.org.apache.xalan.internal.xsltc.compiler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/AttributeValue.class */
abstract class AttributeValue extends Expression {
    AttributeValue() {
    }

    public static final AttributeValue create(SyntaxTreeNode parent, String text, Parser parser) {
        AttributeValue result;
        if (text.indexOf(123) != -1 || text.indexOf(125) != -1) {
            result = new AttributeValueTemplate(text, parser, parent);
        } else {
            result = new SimpleAttributeValue(text);
            result.setParser(parser);
            result.setParent(parent);
        }
        return result;
    }
}
