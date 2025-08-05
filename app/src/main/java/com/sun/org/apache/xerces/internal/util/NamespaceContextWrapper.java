package com.sun.org.apache.xerces.internal.util;

import java.util.Iterator;
import java.util.Vector;
import javax.xml.namespace.NamespaceContext;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/NamespaceContextWrapper.class */
public class NamespaceContextWrapper implements NamespaceContext {
    private com.sun.org.apache.xerces.internal.xni.NamespaceContext fNamespaceContext;

    public NamespaceContextWrapper(NamespaceSupport namespaceContext) {
        this.fNamespaceContext = namespaceContext;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix can't be null");
        }
        return this.fNamespaceContext.getURI(prefix.intern());
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("URI can't be null.");
        }
        return this.fNamespaceContext.getPrefix(namespaceURI.intern());
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator getPrefixes(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("URI can't be null.");
        }
        Vector vector = ((NamespaceSupport) this.fNamespaceContext).getPrefixes(namespaceURI.intern());
        return vector.iterator();
    }

    public com.sun.org.apache.xerces.internal.xni.NamespaceContext getNamespaceContext() {
        return this.fNamespaceContext;
    }
}
