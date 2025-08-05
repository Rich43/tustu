package org.w3c.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.StyleSheet;

/* loaded from: rt.jar:org/w3c/dom/css/CSSStyleSheet.class */
public interface CSSStyleSheet extends StyleSheet {
    CSSRule getOwnerRule();

    CSSRuleList getCssRules();

    int insertRule(String str, int i2) throws DOMException;

    void deleteRule(int i2) throws DOMException;
}
