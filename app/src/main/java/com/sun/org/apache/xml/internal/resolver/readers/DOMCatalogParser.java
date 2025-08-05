package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/readers/DOMCatalogParser.class */
public interface DOMCatalogParser {
    void parseCatalogEntry(Catalog catalog, Node node);
}
