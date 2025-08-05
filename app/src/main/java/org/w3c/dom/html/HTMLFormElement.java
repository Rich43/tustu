package org.w3c.dom.html;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLFormElement.class */
public interface HTMLFormElement extends HTMLElement {
    HTMLCollection getElements();

    int getLength();

    String getName();

    void setName(String str);

    String getAcceptCharset();

    void setAcceptCharset(String str);

    String getAction();

    void setAction(String str);

    String getEnctype();

    void setEnctype(String str);

    String getMethod();

    void setMethod(String str);

    String getTarget();

    void setTarget(String str);

    void submit();

    void reset();
}
