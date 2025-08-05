package org.w3c.dom.html;

import org.w3c.dom.Document;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLIFrameElement.class */
public interface HTMLIFrameElement extends HTMLElement {
    String getAlign();

    void setAlign(String str);

    String getFrameBorder();

    void setFrameBorder(String str);

    String getHeight();

    void setHeight(String str);

    String getLongDesc();

    void setLongDesc(String str);

    String getMarginHeight();

    void setMarginHeight(String str);

    String getMarginWidth();

    void setMarginWidth(String str);

    String getName();

    void setName(String str);

    String getScrolling();

    void setScrolling(String str);

    String getSrc();

    void setSrc(String str);

    String getWidth();

    void setWidth(String str);

    Document getContentDocument();
}
