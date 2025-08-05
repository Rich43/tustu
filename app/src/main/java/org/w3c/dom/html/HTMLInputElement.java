package org.w3c.dom.html;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLInputElement.class */
public interface HTMLInputElement extends HTMLElement {
    String getDefaultValue();

    void setDefaultValue(String str);

    boolean getDefaultChecked();

    void setDefaultChecked(boolean z2);

    HTMLFormElement getForm();

    String getAccept();

    void setAccept(String str);

    String getAccessKey();

    void setAccessKey(String str);

    String getAlign();

    void setAlign(String str);

    String getAlt();

    void setAlt(String str);

    boolean getChecked();

    void setChecked(boolean z2);

    boolean getDisabled();

    void setDisabled(boolean z2);

    int getMaxLength();

    void setMaxLength(int i2);

    String getName();

    void setName(String str);

    boolean getReadOnly();

    void setReadOnly(boolean z2);

    String getSize();

    void setSize(String str);

    String getSrc();

    void setSrc(String str);

    int getTabIndex();

    void setTabIndex(int i2);

    String getType();

    String getUseMap();

    void setUseMap(String str);

    String getValue();

    void setValue(String str);

    void blur();

    void focus();

    void select();

    void click();
}
