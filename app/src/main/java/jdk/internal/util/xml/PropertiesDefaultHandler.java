package jdk.internal.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import jdk.internal.org.xml.sax.Attributes;
import jdk.internal.org.xml.sax.InputSource;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.org.xml.sax.SAXParseException;
import jdk.internal.org.xml.sax.helpers.DefaultHandler;
import jdk.internal.util.xml.impl.SAXParserImpl;
import jdk.internal.util.xml.impl.XMLStreamWriterImpl;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:jdk/internal/util/xml/PropertiesDefaultHandler.class */
public class PropertiesDefaultHandler extends DefaultHandler {
    private static final String ELEMENT_ROOT = "properties";
    private static final String ELEMENT_COMMENT = "comment";
    private static final String ELEMENT_ENTRY = "entry";
    private static final String ATTR_KEY = "key";
    private static final String PROPS_DTD_DECL = "<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">";
    private static final String PROPS_DTD_URI = "http://java.sun.com/dtd/properties.dtd";
    private static final String PROPS_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- DTD for properties --><!ELEMENT properties ( comment?, entry* ) ><!ATTLIST properties version CDATA #FIXED \"1.0\"><!ELEMENT comment (#PCDATA) ><!ELEMENT entry (#PCDATA) ><!ATTLIST entry  key CDATA #REQUIRED>";
    private static final String EXTERNAL_XML_VERSION = "1.0";
    private Properties properties;
    static final String ALLOWED_ELEMENTS = "properties, comment, entry";
    static final String ALLOWED_COMMENT = "comment";
    StringBuffer buf = new StringBuffer();
    boolean sawComment = false;
    boolean validEntry = false;
    int rootElem = 0;
    String key;
    String rootElm;

    public void load(Properties properties, InputStream inputStream) throws IOException {
        this.properties = properties;
        try {
            new SAXParserImpl().parse(inputStream, this);
        } catch (SAXException e2) {
            throw new InvalidPropertiesFormatException(e2);
        }
    }

    public void store(Properties properties, OutputStream outputStream, String str, String str2) throws IOException {
        try {
            XMLStreamWriterImpl xMLStreamWriterImpl = new XMLStreamWriterImpl(outputStream, str2);
            xMLStreamWriterImpl.writeStartDocument();
            xMLStreamWriterImpl.writeDTD(PROPS_DTD_DECL);
            xMLStreamWriterImpl.writeStartElement(ELEMENT_ROOT);
            if (str != null && str.length() > 0) {
                xMLStreamWriterImpl.writeStartElement("comment");
                xMLStreamWriterImpl.writeCharacters(str);
                xMLStreamWriterImpl.writeEndElement();
            }
            synchronized (properties) {
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    Object key = entry.getKey();
                    Object value = entry.getValue();
                    if ((key instanceof String) && (value instanceof String)) {
                        xMLStreamWriterImpl.writeStartElement(ELEMENT_ENTRY);
                        xMLStreamWriterImpl.writeAttribute("key", (String) key);
                        xMLStreamWriterImpl.writeCharacters((String) value);
                        xMLStreamWriterImpl.writeEndElement();
                    }
                }
            }
            xMLStreamWriterImpl.writeEndElement();
            xMLStreamWriterImpl.writeEndDocument();
            xMLStreamWriterImpl.close();
        } catch (XMLStreamException e2) {
            if (e2.getCause() instanceof UnsupportedEncodingException) {
                throw ((UnsupportedEncodingException) e2.getCause());
            }
            throw new IOException(e2);
        }
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        if (this.rootElem < 2) {
            this.rootElem++;
        }
        if (this.rootElm == null) {
            fatalError(new SAXParseException("An XML properties document must contain the DOCTYPE declaration as defined by java.util.Properties.", null));
        }
        if (this.rootElem == 1 && !this.rootElm.equals(str3)) {
            fatalError(new SAXParseException("Document root element \"" + str3 + "\", must match DOCTYPE root \"" + this.rootElm + PdfOps.DOUBLE_QUOTE__TOKEN, null));
        }
        if (!ALLOWED_ELEMENTS.contains(str3)) {
            fatalError(new SAXParseException("Element type \"" + str3 + "\" must be declared.", null));
        }
        if (str3.equals(ELEMENT_ENTRY)) {
            this.validEntry = true;
            this.key = attributes.getValue("key");
            if (this.key == null) {
                fatalError(new SAXParseException("Attribute \"key\" is required and must be specified for element type \"entry\"", null));
                return;
            }
            return;
        }
        if (str3.equals("comment")) {
            if (this.sawComment) {
                fatalError(new SAXParseException("Only one comment element may be allowed. The content of element type \"properties\" must match \"(comment?,entry*)\"", null));
            }
            this.sawComment = true;
        }
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i2, int i3) throws SAXException {
        if (this.validEntry) {
            this.buf.append(cArr, i2, i3);
        }
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        if (!ALLOWED_ELEMENTS.contains(str3)) {
            fatalError(new SAXParseException("Element: " + str3 + " is invalid, must match  \"(comment?,entry*)\".", null));
        }
        if (this.validEntry) {
            this.properties.setProperty(this.key, this.buf.toString());
            this.buf.delete(0, this.buf.length());
            this.validEntry = false;
        }
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.DTDHandler
    public void notationDecl(String str, String str2, String str3) throws SAXException {
        this.rootElm = str;
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.EntityResolver
    public InputSource resolveEntity(String str, String str2) throws IOException, SAXException {
        if (str2.equals(PROPS_DTD_URI)) {
            InputSource inputSource = new InputSource(new StringReader(PROPS_DTD));
            inputSource.setSystemId(PROPS_DTD_URI);
            return inputSource;
        }
        throw new SAXException("Invalid system identifier: " + str2);
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ErrorHandler
    public void error(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }

    @Override // jdk.internal.org.xml.sax.helpers.DefaultHandler, jdk.internal.org.xml.sax.ErrorHandler
    public void warning(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }
}
