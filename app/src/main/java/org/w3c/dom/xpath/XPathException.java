package org.w3c.dom.xpath;

/* loaded from: rt.jar:org/w3c/dom/xpath/XPathException.class */
public class XPathException extends RuntimeException {
    public short code;
    public static final short INVALID_EXPRESSION_ERR = 1;
    public static final short TYPE_ERR = 2;

    public XPathException(short code, String message) {
        super(message);
        this.code = code;
    }
}
