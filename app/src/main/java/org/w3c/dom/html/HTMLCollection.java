package org.w3c.dom.html;

import org.w3c.dom.Node;

/* loaded from: rt.jar:org/w3c/dom/html/HTMLCollection.class */
public interface HTMLCollection {
    int getLength();

    Node item(int i2);

    Node namedItem(String str);
}
