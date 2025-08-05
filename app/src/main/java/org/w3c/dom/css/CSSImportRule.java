package org.w3c.dom.css;

import org.w3c.dom.stylesheets.MediaList;

/* loaded from: rt.jar:org/w3c/dom/css/CSSImportRule.class */
public interface CSSImportRule extends CSSRule {
    String getHref();

    MediaList getMedia();

    CSSStyleSheet getStyleSheet();
}
