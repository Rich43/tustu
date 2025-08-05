package org.w3c.dom.html;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLOptionElement.class */
public interface HTMLOptionElement extends HTMLElement {
    HTMLFormElement getForm();

    boolean getDefaultSelected();

    void setDefaultSelected(boolean z2);

    String getText();

    int getIndex();

    boolean getDisabled();

    void setDisabled(boolean z2);

    String getLabel();

    void setLabel(String str);

    boolean getSelected();

    void setSelected(boolean z2);

    String getValue();

    void setValue(String str);
}
