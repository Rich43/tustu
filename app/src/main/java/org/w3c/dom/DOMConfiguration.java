package org.w3c.dom;

/* loaded from: rt.jar:org/w3c/dom/DOMConfiguration.class */
public interface DOMConfiguration {
    void setParameter(String str, Object obj) throws DOMException;

    Object getParameter(String str) throws DOMException;

    boolean canSetParameter(String str, Object obj);

    DOMStringList getParameterNames();
}
