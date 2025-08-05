package com.sun.webkit.dom;

import org.w3c.dom.css.CSSUnknownRule;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSUnknownRuleImpl.class */
public class CSSUnknownRuleImpl extends CSSRuleImpl implements CSSUnknownRule {
    CSSUnknownRuleImpl(long peer) {
        super(peer);
    }

    static CSSUnknownRule getImpl(long peer) {
        return (CSSUnknownRule) create(peer);
    }
}
