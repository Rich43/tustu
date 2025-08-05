package org.w3c.dom.html;

import org.w3c.dom.DOMException;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLTableRowElement.class */
public interface HTMLTableRowElement extends HTMLElement {
    int getRowIndex();

    int getSectionRowIndex();

    HTMLCollection getCells();

    String getAlign();

    void setAlign(String str);

    String getBgColor();

    void setBgColor(String str);

    String getCh();

    void setCh(String str);

    String getChOff();

    void setChOff(String str);

    String getVAlign();

    void setVAlign(String str);

    HTMLElement insertCell(int i2) throws DOMException;

    void deleteCell(int i2) throws DOMException;
}
