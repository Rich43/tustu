package org.w3c.dom.css;

import org.w3c.dom.DOMException;

/* loaded from: rt.jar:org/w3c/dom/css/CSSPageRule.class */
public interface CSSPageRule extends CSSRule {
    String getSelectorText();

    void setSelectorText(String str) throws DOMException;

    CSSStyleDeclaration getStyle();
}
