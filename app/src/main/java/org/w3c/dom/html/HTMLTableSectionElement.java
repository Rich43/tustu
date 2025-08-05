package org.w3c.dom.html;

import org.w3c.dom.DOMException;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLTableSectionElement.class */
public interface HTMLTableSectionElement extends HTMLElement {
    String getAlign();

    void setAlign(String str);

    String getCh();

    void setCh(String str);

    String getChOff();

    void setChOff(String str);

    String getVAlign();

    void setVAlign(String str);

    HTMLCollection getRows();

    HTMLElement insertRow(int i2) throws DOMException;

    void deleteRow(int i2) throws DOMException;
}
