package org.w3c.dom.html;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLTextAreaElement.class */
public interface HTMLTextAreaElement extends HTMLElement {
    String getDefaultValue();

    void setDefaultValue(String str);

    HTMLFormElement getForm();

    String getAccessKey();

    void setAccessKey(String str);

    int getCols();

    void setCols(int i2);

    boolean getDisabled();

    void setDisabled(boolean z2);

    String getName();

    void setName(String str);

    boolean getReadOnly();

    void setReadOnly(boolean z2);

    int getRows();

    void setRows(int i2);

    int getTabIndex();

    void setTabIndex(int i2);

    String getType();

    String getValue();

    void setValue(String str);

    void blur();

    void focus();

    void select();
}
