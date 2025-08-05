package com.sun.org.apache.xpath.internal;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
import java.io.IOException;
import java.util.Vector;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/SourceTreeManager.class */
public class SourceTreeManager {
    private Vector m_sourceTree = new Vector();
    URIResolver m_uriResolver;

    public void reset() {
        this.m_sourceTree = new Vector();
    }

    public void setURIResolver(URIResolver resolver) {
        this.m_uriResolver = resolver;
    }

    public URIResolver getURIResolver() {
        return this.m_uriResolver;
    }

    public String findURIFromDoc(int owner) {
        int n2 = this.m_sourceTree.size();
        for (int i2 = 0; i2 < n2; i2++) {
            SourceTree sTree = (SourceTree) this.m_sourceTree.elementAt(i2);
            if (owner == sTree.m_root) {
                return sTree.m_url;
            }
        }
        return null;
    }

    public Source resolveURI(String base, String urlString, SourceLocator locator) throws TransformerException, IOException {
        Source source = null;
        if (null != this.m_uriResolver) {
            source = this.m_uriResolver.resolve(urlString, base);
        }
        if (null == source) {
            String uri = SystemIDResolver.getAbsoluteURI(urlString, base);
            source = new StreamSource(uri);
        }
        return source;
    }

    public void removeDocumentFromCache(int n2) {
        if (-1 == n2) {
            return;
        }
        for (int i2 = this.m_sourceTree.size() - 1; i2 >= 0; i2--) {
            SourceTree st = (SourceTree) this.m_sourceTree.elementAt(i2);
            if (st != null && st.m_root == n2) {
                this.m_sourceTree.removeElementAt(i2);
                return;
            }
        }
    }

    public void putDocumentInCache(int n2, Source source) {
        int cachedNode = getNode(source);
        if (-1 != cachedNode) {
            if (cachedNode != n2) {
                throw new RuntimeException("Programmer's Error!  putDocumentInCache found reparse of doc: " + source.getSystemId());
            }
        } else if (null != source.getSystemId()) {
            this.m_sourceTree.addElement(new SourceTree(n2, source.getSystemId()));
        }
    }

    public int getNode(Source source) {
        String url = source.getSystemId();
        if (null == url) {
            return -1;
        }
        int n2 = this.m_sourceTree.size();
        for (int i2 = 0; i2 < n2; i2++) {
            SourceTree sTree = (SourceTree) this.m_sourceTree.elementAt(i2);
            if (url.equals(sTree.m_url)) {
                return sTree.m_root;
            }
        }
        return -1;
    }

    public int getSourceTree(String base, String urlString, SourceLocator locator, XPathContext xctxt) throws TransformerException {
        try {
            Source source = resolveURI(base, urlString, locator);
            return getSourceTree(source, locator, xctxt);
        } catch (IOException ioe) {
            throw new TransformerException(ioe.getMessage(), locator, ioe);
        }
    }

    public int getSourceTree(Source source, SourceLocator locator, XPathContext xctxt) throws TransformerException {
        int n2 = getNode(source);
        if (-1 != n2) {
            return n2;
        }
        int n3 = parseToNode(source, locator, xctxt);
        if (-1 != n3) {
            putDocumentInCache(n3, source);
        }
        return n3;
    }

    public int parseToNode(Source source, SourceLocator locator, XPathContext xctxt) throws TransformerException {
        DTM dtm;
        try {
            Object xowner = xctxt.getOwnerObject();
            if (null != xowner && (xowner instanceof DTMWSFilter)) {
                dtm = xctxt.getDTM(source, false, (DTMWSFilter) xowner, false, true);
            } else {
                dtm = xctxt.getDTM(source, false, null, false, true);
            }
            return dtm.getDocument();
        } catch (Exception e2) {
            throw new TransformerException(e2.getMessage(), locator, e2);
        }
    }

    public static XMLReader getXMLReader(Source inputSource, SourceLocator locator) throws TransformerException, SAXException {
        try {
            XMLReader reader = inputSource instanceof SAXSource ? ((SAXSource) inputSource).getXMLReader() : null;
            if (null == reader) {
                try {
                    try {
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        SAXParser jaxpParser = factory.newSAXParser();
                        reader = jaxpParser.getXMLReader();
                    } catch (AbstractMethodError e2) {
                    } catch (NoSuchMethodError e3) {
                    } catch (FactoryConfigurationError ex1) {
                        throw new SAXException(ex1.toString());
                    }
                    if (null == reader) {
                        reader = XMLReaderFactory.createXMLReader();
                    }
                } catch (ParserConfigurationException ex) {
                    throw new SAXException(ex);
                }
            }
            try {
                reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            } catch (SAXException e4) {
            }
            return reader;
        } catch (SAXException se) {
            throw new TransformerException(se.getMessage(), locator, se);
        }
    }
}
