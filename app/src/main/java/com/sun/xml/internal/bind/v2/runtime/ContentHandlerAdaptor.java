package com.sun.xml.internal.bind.v2.runtime;

import com.sun.istack.internal.FinalArrayList;
import com.sun.istack.internal.SAXException2;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/ContentHandlerAdaptor.class */
final class ContentHandlerAdaptor extends DefaultHandler {
    private final XMLSerializer serializer;
    private final FinalArrayList<String> prefixMap = new FinalArrayList<>();
    private final StringBuffer text = new StringBuffer();

    ContentHandlerAdaptor(XMLSerializer _serializer) {
        this.serializer = _serializer;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() {
        this.prefixMap.clear();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
        this.prefixMap.add(prefix);
        this.prefixMap.add(uri);
    }

    private boolean containsPrefixMapping(String prefix, String uri) {
        for (int i2 = 0; i2 < this.prefixMap.size(); i2 += 2) {
            if (this.prefixMap.get(i2).equals(prefix) && this.prefixMap.get(i2 + 1).equals(uri)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        try {
            flushText();
            int len = atts.getLength();
            String p2 = getPrefix(qName);
            if (containsPrefixMapping(p2, namespaceURI)) {
                this.serializer.startElementForce(namespaceURI, localName, p2, null);
            } else {
                this.serializer.startElement(namespaceURI, localName, p2, null);
            }
            for (int i2 = 0; i2 < this.prefixMap.size(); i2 += 2) {
                this.serializer.getNamespaceContext().force(this.prefixMap.get(i2 + 1), this.prefixMap.get(i2));
            }
            for (int i3 = 0; i3 < len; i3++) {
                String qname = atts.getQName(i3);
                if (!qname.startsWith("xmlns") && atts.getURI(i3).length() != 0) {
                    String prefix = getPrefix(qname);
                    this.serializer.getNamespaceContext().declareNamespace(atts.getURI(i3), prefix, true);
                }
            }
            this.serializer.endNamespaceDecls(null);
            for (int i4 = 0; i4 < len; i4++) {
                if (!atts.getQName(i4).startsWith("xmlns")) {
                    this.serializer.attribute(atts.getURI(i4), atts.getLocalName(i4), atts.getValue(i4));
                }
            }
            this.prefixMap.clear();
            this.serializer.endAttributes();
        } catch (IOException e2) {
            throw new SAXException2(e2);
        } catch (XMLStreamException e3) {
            throw new SAXException2(e3);
        }
    }

    private String getPrefix(String qname) {
        int idx = qname.indexOf(58);
        String prefix = idx == -1 ? "" : qname.substring(0, idx);
        return prefix;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            flushText();
            this.serializer.endElement();
        } catch (IOException e2) {
            throw new SAXException2(e2);
        } catch (XMLStreamException e3) {
            throw new SAXException2(e3);
        }
    }

    private void flushText() throws SAXException, XMLStreamException, IOException {
        if (this.text.length() != 0) {
            this.serializer.text(this.text.toString(), (String) null);
            this.text.setLength(0);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) {
        this.text.append(ch, start, length);
    }
}
