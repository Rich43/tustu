package com.sun.xml.internal.org.jvnet.staxex;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/NamespaceContextEx.class */
public interface NamespaceContextEx extends NamespaceContext, Iterable<Binding> {

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/NamespaceContextEx$Binding.class */
    public interface Binding {
        String getPrefix();

        String getNamespaceURI();
    }

    @Override // java.lang.Iterable, java.util.List
    Iterator<Binding> iterator();
}
