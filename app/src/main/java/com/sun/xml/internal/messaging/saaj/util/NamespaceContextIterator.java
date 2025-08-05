package com.sun.xml.internal.messaging.saaj.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/NamespaceContextIterator.class */
public class NamespaceContextIterator implements Iterator {
    Node context;
    NamedNodeMap attributes;
    int attributesLength;
    int attributeIndex;
    Attr next;
    Attr last;
    boolean traverseStack;

    public NamespaceContextIterator(Node context) {
        this.attributes = null;
        this.next = null;
        this.last = null;
        this.traverseStack = true;
        this.context = context;
        findContextAttributes();
    }

    public NamespaceContextIterator(Node context, boolean traverseStack) {
        this(context);
        this.traverseStack = traverseStack;
    }

    protected void findContextAttributes() {
        while (this.context != null) {
            int type = this.context.getNodeType();
            if (type == 1) {
                this.attributes = this.context.getAttributes();
                this.attributesLength = this.attributes.getLength();
                this.attributeIndex = 0;
                return;
            }
            this.context = null;
        }
    }

    protected void findNext() {
        while (this.next == null && this.context != null) {
            while (this.attributeIndex < this.attributesLength) {
                Node currentAttribute = this.attributes.item(this.attributeIndex);
                String attributeName = currentAttribute.getNodeName();
                if (!attributeName.startsWith("xmlns") || (attributeName.length() != 5 && attributeName.charAt(5) != ':')) {
                    this.attributeIndex++;
                } else {
                    this.next = (Attr) currentAttribute;
                    this.attributeIndex++;
                    return;
                }
            }
            if (this.traverseStack) {
                this.context = this.context.getParentNode();
                findContextAttributes();
            } else {
                this.context = null;
            }
        }
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        findNext();
        return this.next != null;
    }

    @Override // java.util.Iterator
    public Object next() {
        return getNext();
    }

    public Attr nextNamespaceAttr() {
        return getNext();
    }

    protected Attr getNext() {
        findNext();
        if (this.next == null) {
            throw new NoSuchElementException();
        }
        this.last = this.next;
        this.next = null;
        return this.last;
    }

    @Override // java.util.Iterator
    public void remove() throws DOMException {
        if (this.last == null) {
            throw new IllegalStateException();
        }
        ((Element) this.context).removeAttributeNode(this.last);
    }
}
