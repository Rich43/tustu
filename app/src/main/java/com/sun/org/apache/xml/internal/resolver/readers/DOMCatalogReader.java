package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import com.sun.org.apache.xml.internal.resolver.helpers.Namespaces;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.math3.geometry.VectorFormat;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/resolver/readers/DOMCatalogReader.class */
public class DOMCatalogReader implements CatalogReader {
    protected Map<String, String> namespaceMap = new HashMap();

    public void setCatalogParser(String namespaceURI, String rootElement, String parserClass) {
        if (namespaceURI == null) {
            this.namespaceMap.put(rootElement, parserClass);
        } else {
            this.namespaceMap.put(VectorFormat.DEFAULT_PREFIX + namespaceURI + "}" + rootElement, parserClass);
        }
    }

    public String getCatalogParser(String namespaceURI, String rootElement) {
        if (namespaceURI == null) {
            return this.namespaceMap.get(rootElement);
        }
        return this.namespaceMap.get(VectorFormat.DEFAULT_PREFIX + namespaceURI + "}" + rootElement);
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.CatalogReader
    public void readCatalog(Catalog catalog, InputStream is) throws CatalogException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            try {
                Document doc = builder.parse(is);
                Element root = doc.getDocumentElement();
                String namespaceURI = Namespaces.getNamespaceURI(root);
                String localName = Namespaces.getLocalName(root);
                String domParserClass = getCatalogParser(namespaceURI, localName);
                if (domParserClass == null) {
                    if (namespaceURI == null) {
                        catalog.getCatalogManager().debug.message(1, "No Catalog parser for " + localName);
                        return;
                    } else {
                        catalog.getCatalogManager().debug.message(1, "No Catalog parser for {" + namespaceURI + "}" + localName);
                        return;
                    }
                }
                try {
                    DOMCatalogParser domParser = (DOMCatalogParser) ReflectUtil.forName(domParserClass).newInstance();
                    Node firstChild = root.getFirstChild();
                    while (true) {
                        Node node = firstChild;
                        if (node != null) {
                            domParser.parseCatalogEntry(catalog, node);
                            firstChild = node.getNextSibling();
                        } else {
                            return;
                        }
                    }
                } catch (ClassCastException e2) {
                    catalog.getCatalogManager().debug.message(1, "Cannot cast XML Catalog Parser class", domParserClass);
                    throw new CatalogException(6);
                } catch (ClassNotFoundException e3) {
                    catalog.getCatalogManager().debug.message(1, "Cannot load XML Catalog Parser class", domParserClass);
                    throw new CatalogException(6);
                } catch (IllegalAccessException e4) {
                    catalog.getCatalogManager().debug.message(1, "Cannot access XML Catalog Parser class", domParserClass);
                    throw new CatalogException(6);
                } catch (InstantiationException e5) {
                    catalog.getCatalogManager().debug.message(1, "Cannot instantiate XML Catalog Parser class", domParserClass);
                    throw new CatalogException(6);
                }
            } catch (SAXException e6) {
                throw new CatalogException(5);
            }
        } catch (ParserConfigurationException e7) {
            throw new CatalogException(6);
        }
    }

    @Override // com.sun.org.apache.xml.internal.resolver.readers.CatalogReader
    public void readCatalog(Catalog catalog, String fileUrl) throws CatalogException, IOException {
        URL url = new URL(fileUrl);
        URLConnection urlCon = url.openConnection();
        readCatalog(catalog, urlCon.getInputStream());
    }
}
