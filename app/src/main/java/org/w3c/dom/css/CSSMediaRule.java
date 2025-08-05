package org.w3c.dom.css;

import org.w3c.dom.DOMException;
import org.w3c.dom.stylesheets.MediaList;

/* loaded from: rt.jar:org/w3c/dom/css/CSSMediaRule.class */
public interface CSSMediaRule extends CSSRule {
    MediaList getMedia();

    CSSRuleList getCssRules();

    int insertRule(String str, int i2) throws DOMException;

    void deleteRule(int i2) throws DOMException;
}
