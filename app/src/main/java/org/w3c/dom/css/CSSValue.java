package org.w3c.dom.css;

import org.w3c.dom.DOMException;

/* loaded from: rt.jar:org/w3c/dom/css/CSSValue.class */
public interface CSSValue {
    public static final short CSS_INHERIT = 0;
    public static final short CSS_PRIMITIVE_VALUE = 1;
    public static final short CSS_VALUE_LIST = 2;
    public static final short CSS_CUSTOM = 3;

    String getCssText();

    void setCssText(String str) throws DOMException;

    short getCssValueType();
}
