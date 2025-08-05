package org.w3c.dom.html;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLLabelElement.class */
public interface HTMLLabelElement extends HTMLElement {
    HTMLFormElement getForm();

    String getAccessKey();

    void setAccessKey(String str);

    String getHtmlFor();

    void setHtmlFor(String str);
}
