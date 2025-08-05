package org.w3c.dom.css;

import org.w3c.dom.DOMException;

/* loaded from: rt.jar:org/w3c/dom/css/CSSCharsetRule.class */
public interface CSSCharsetRule extends CSSRule {
    String getEncoding();

    void setEncoding(String str) throws DOMException;
}
