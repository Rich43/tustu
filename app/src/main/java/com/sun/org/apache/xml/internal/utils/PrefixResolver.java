package com.sun.org.apache.xml.internal.utils;

import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/PrefixResolver.class */
public interface PrefixResolver {
    String getNamespaceForPrefix(String str);

    String getNamespaceForPrefix(String str, Node node);

    String getBaseIdentifier();

    boolean handlesNullPrefixes();
}
