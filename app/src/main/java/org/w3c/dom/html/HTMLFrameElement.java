package org.w3c.dom.html;

import org.w3c.dom.Document;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLFrameElement.class */
public interface HTMLFrameElement extends HTMLElement {
    String getFrameBorder();

    void setFrameBorder(String str);

    String getLongDesc();

    void setLongDesc(String str);

    String getMarginHeight();

    void setMarginHeight(String str);

    String getMarginWidth();

    void setMarginWidth(String str);

    String getName();

    void setName(String str);

    boolean getNoResize();

    void setNoResize(boolean z2);

    String getScrolling();

    void setScrolling(String str);

    String getSrc();

    void setSrc(String str);

    Document getContentDocument();
}
