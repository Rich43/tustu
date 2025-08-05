package com.sun.xml.internal.ws.util.xml;

import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/NamespaceContextExAdaper.class */
public class NamespaceContextExAdaper implements NamespaceContextEx {
    private final NamespaceContext nsContext;

    public NamespaceContextExAdaper(NamespaceContext nsContext) {
        this.nsContext = nsContext;
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx, java.lang.Iterable, java.util.List
    public Iterator<NamespaceContextEx.Binding> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String prefix) {
        return this.nsContext.getNamespaceURI(prefix);
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String namespaceURI) {
        return this.nsContext.getPrefix(namespaceURI);
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator getPrefixes(String namespaceURI) {
        return this.nsContext.getPrefixes(namespaceURI);
    }
}
