package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/readers/CatalogReader.class */
public interface CatalogReader {
    void readCatalog(Catalog catalog, String str) throws CatalogException, IOException;

    void readCatalog(Catalog catalog, InputStream inputStream) throws CatalogException, IOException;
}
