package com.sun.org.apache.xml.internal.utils;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import java.util.HashMap;
import jdk.xml.internal.JdkXmlUtils;
import jdk.xml.internal.XMLSecurityManager;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/XMLReaderManager.class */
public class XMLReaderManager {
    private static final XMLReaderManager m_singletonManager = new XMLReaderManager();
    private static final String property = "org.xml.sax.driver";
    private static final String LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";
    private ThreadLocal<ReaderWrapper> m_readers;
    private boolean m_overrideDefaultParser;
    private HashMap m_inUse;
    private boolean _secureProcessing;
    private String _accessExternalDTD = "all";
    private XMLSecurityManager _xmlSecurityManager;

    private XMLReaderManager() {
    }

    public static XMLReaderManager getInstance(boolean overrideDefaultParser) {
        m_singletonManager.setOverrideDefaultParser(overrideDefaultParser);
        return m_singletonManager;
    }

    public synchronized XMLReader getXMLReader() throws SAXException {
        if (this.m_readers == null) {
            this.m_readers = new ThreadLocal<>();
        }
        if (this.m_inUse == null) {
            this.m_inUse = new HashMap();
        }
        ReaderWrapper rw = this.m_readers.get();
        boolean threadHasReader = rw != null;
        XMLReader reader = threadHasReader ? rw.reader : null;
        String factory = SecuritySupport.getSystemProperty(property);
        if (threadHasReader && this.m_inUse.get(reader) != Boolean.TRUE && rw.overrideDefaultParser == this.m_overrideDefaultParser && (factory == null || reader.getClass().getName().equals(factory))) {
            this.m_inUse.put(reader, Boolean.TRUE);
        } else {
            reader = JdkXmlUtils.getXMLReader(this.m_overrideDefaultParser, this._secureProcessing);
            if (!threadHasReader) {
                this.m_readers.set(new ReaderWrapper(reader, this.m_overrideDefaultParser));
                this.m_inUse.put(reader, Boolean.TRUE);
            }
        }
        try {
            reader.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", this._accessExternalDTD);
        } catch (SAXException se) {
            XMLSecurityManager.printWarning(reader.getClass().getName(), "http://javax.xml.XMLConstants/property/accessExternalDTD", se);
        }
        String lastProperty = "";
        try {
            if (this._xmlSecurityManager != null) {
                for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
                    if (limit.isSupported(XMLSecurityManager.Processor.PARSER)) {
                        String lastProperty2 = limit.apiProperty();
                        reader.setProperty(lastProperty2, this._xmlSecurityManager.getLimitValueAsString(limit));
                    }
                }
                if (this._xmlSecurityManager.printEntityCountInfo()) {
                    lastProperty = "http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo";
                    reader.setProperty("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo", "yes");
                }
            }
        } catch (SAXException se2) {
            XMLSecurityManager.printWarning(reader.getClass().getName(), lastProperty, se2);
        }
        return reader;
    }

    public synchronized void releaseXMLReader(XMLReader reader) {
        ReaderWrapper rw = this.m_readers.get();
        if (rw != null && rw.reader == reader && reader != null) {
            reader.setContentHandler(null);
            reader.setDTDHandler(null);
            reader.setEntityResolver(null);
            try {
                reader.setProperty("http://xml.org/sax/properties/lexical-handler", null);
            } catch (SAXNotRecognizedException | SAXNotSupportedException e2) {
            }
            this.m_inUse.remove(reader);
        }
    }

    public boolean overrideDefaultParser() {
        return this.m_overrideDefaultParser;
    }

    public void setOverrideDefaultParser(boolean flag) {
        this.m_overrideDefaultParser = flag;
    }

    public void setFeature(String name, boolean value) {
        if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
            this._secureProcessing = value;
        }
    }

    public Object getProperty(String name) {
        if (name.equals("http://javax.xml.XMLConstants/property/accessExternalDTD")) {
            return this._accessExternalDTD;
        }
        if (name.equals("http://apache.org/xml/properties/security-manager")) {
            return this._xmlSecurityManager;
        }
        return null;
    }

    public void setProperty(String name, Object value) {
        if (name.equals("http://javax.xml.XMLConstants/property/accessExternalDTD")) {
            this._accessExternalDTD = (String) value;
        } else if (name.equals("http://apache.org/xml/properties/security-manager")) {
            this._xmlSecurityManager = (XMLSecurityManager) value;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/XMLReaderManager$ReaderWrapper.class */
    class ReaderWrapper {
        XMLReader reader;
        boolean overrideDefaultParser;

        public ReaderWrapper(XMLReader reader, boolean overrideDefaultParser) {
            this.reader = reader;
            this.overrideDefaultParser = overrideDefaultParser;
        }
    }
}
