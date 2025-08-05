package org.w3c.dom.html;

import org.w3c.dom.Document;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLObjectElement.class */
public interface HTMLObjectElement extends HTMLElement {
    HTMLFormElement getForm();

    String getCode();

    void setCode(String str);

    String getAlign();

    void setAlign(String str);

    String getArchive();

    void setArchive(String str);

    String getBorder();

    void setBorder(String str);

    String getCodeBase();

    void setCodeBase(String str);

    String getCodeType();

    void setCodeType(String str);

    String getData();

    void setData(String str);

    boolean getDeclare();

    void setDeclare(boolean z2);

    String getHeight();

    void setHeight(String str);

    String getHspace();

    void setHspace(String str);

    String getName();

    void setName(String str);

    String getStandby();

    void setStandby(String str);

    int getTabIndex();

    void setTabIndex(int i2);

    String getType();

    void setType(String str);

    String getUseMap();

    void setUseMap(String str);

    String getVspace();

    void setVspace(String str);

    String getWidth();

    void setWidth(String str);

    Document getContentDocument();
}
