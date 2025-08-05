package org.w3c.dom.ls;

import org.w3c.dom.DOMException;

/* loaded from: rt.jar:org/w3c/dom/ls/DOMImplementationLS.class */
public interface DOMImplementationLS {
    public static final short MODE_SYNCHRONOUS = 1;
    public static final short MODE_ASYNCHRONOUS = 2;

    LSParser createLSParser(short s2, String str) throws DOMException;

    LSSerializer createLSSerializer();

    LSInput createLSInput();

    LSOutput createLSOutput();
}
