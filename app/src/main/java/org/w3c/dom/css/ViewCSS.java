package org.w3c.dom.css;

import org.w3c.dom.Element;
import org.w3c.dom.views.AbstractView;

/* loaded from: rt.jar:org/w3c/dom/css/ViewCSS.class */
public interface ViewCSS extends AbstractView {
    CSSStyleDeclaration getComputedStyle(Element element, String str);
}
