package org.w3c.dom.html;

import org.w3c.dom.DOMException;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLSelectElement.class */
public interface HTMLSelectElement extends HTMLElement {
    String getType();

    int getSelectedIndex();

    void setSelectedIndex(int i2);

    String getValue();

    void setValue(String str);

    int getLength();

    HTMLFormElement getForm();

    HTMLCollection getOptions();

    boolean getDisabled();

    void setDisabled(boolean z2);

    boolean getMultiple();

    void setMultiple(boolean z2);

    String getName();

    void setName(String str);

    int getSize();

    void setSize(int i2);

    int getTabIndex();

    void setTabIndex(int i2);

    void add(HTMLElement hTMLElement, HTMLElement hTMLElement2) throws DOMException;

    void remove(int i2);

    void blur();

    void focus();
}
