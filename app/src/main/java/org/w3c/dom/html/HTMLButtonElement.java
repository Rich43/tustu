package org.w3c.dom.html;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLButtonElement.class */
public interface HTMLButtonElement extends HTMLElement {
    HTMLFormElement getForm();

    String getAccessKey();

    void setAccessKey(String str);

    boolean getDisabled();

    void setDisabled(boolean z2);

    String getName();

    void setName(String str);

    int getTabIndex();

    void setTabIndex(int i2);

    String getType();

    String getValue();

    void setValue(String str);
}
