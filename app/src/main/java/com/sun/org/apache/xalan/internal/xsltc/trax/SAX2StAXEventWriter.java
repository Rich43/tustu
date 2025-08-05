package com.sun.org.apache.xalan.internal.xsltc.trax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Locator2;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/SAX2StAXEventWriter.class */
public class SAX2StAXEventWriter extends SAX2StAXBaseWriter {
    private XMLEventWriter writer;
    private XMLEventFactory eventFactory;
    private List namespaceStack;
    private boolean needToCallStartDocument;

    public SAX2StAXEventWriter() {
        this.namespaceStack = new ArrayList();
        this.needToCallStartDocument = false;
        this.eventFactory = XMLEventFactory.newInstance();
    }

    public SAX2StAXEventWriter(XMLEventWriter writer) {
        this.namespaceStack = new ArrayList();
        this.needToCallStartDocument = false;
        this.writer = writer;
        this.eventFactory = XMLEventFactory.newInstance();
    }

    public SAX2StAXEventWriter(XMLEventWriter writer, XMLEventFactory factory) {
        this.namespaceStack = new ArrayList();
        this.needToCallStartDocument = false;
        this.writer = writer;
        if (factory != null) {
            this.eventFactory = factory;
        } else {
            this.eventFactory = XMLEventFactory.newInstance();
        }
    }

    public XMLEventWriter getEventWriter() {
        return this.writer;
    }

    public void setEventWriter(XMLEventWriter writer) {
        this.writer = writer;
    }

    public XMLEventFactory getEventFactory() {
        return this.eventFactory;
    }

    public void setEventFactory(XMLEventFactory factory) {
        this.eventFactory = factory;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        super.startDocument();
        this.namespaceStack.clear();
        this.eventFactory.setLocation(getCurrentLocation());
        this.needToCallStartDocument = true;
    }

    private void writeStartDocument() throws SAXException {
        try {
            if (this.docLocator == null) {
                this.writer.add(this.eventFactory.createStartDocument());
            } else {
                try {
                    this.writer.add(this.eventFactory.createStartDocument(((Locator2) this.docLocator).getEncoding(), ((Locator2) this.docLocator).getXMLVersion()));
                } catch (ClassCastException e2) {
                    this.writer.add(this.eventFactory.createStartDocument());
                }
            }
            this.needToCallStartDocument = false;
        } catch (XMLStreamException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        this.eventFactory.setLocation(getCurrentLocation());
        try {
            this.writer.add(this.eventFactory.createEndDocument());
            super.endDocument();
            this.namespaceStack.clear();
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (this.needToCallStartDocument) {
            writeStartDocument();
        }
        this.eventFactory.setLocation(getCurrentLocation());
        Collection[] events = {null, null};
        createStartEvents(attributes, events);
        this.namespaceStack.add(events[0]);
        try {
            try {
                String[] qname = {null, null};
                parseQName(qName, qname);
                this.writer.add(this.eventFactory.createStartElement(qname[0], uri, qname[1], events[1].iterator(), events[0].iterator()));
                super.startElement(uri, localName, qName, attributes);
            } catch (XMLStreamException e2) {
                throw new SAXException(e2);
            }
        } catch (Throwable th) {
            super.startElement(uri, localName, qName, attributes);
            throw th;
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        this.eventFactory.setLocation(getCurrentLocation());
        String[] qname = {null, null};
        parseQName(qName, qname);
        Collection nsList = (Collection) this.namespaceStack.remove(this.namespaceStack.size() - 1);
        Iterator nsIter = nsList.iterator();
        try {
            this.writer.add(this.eventFactory.createEndElement(qname[0], uri, qname[1], nsIter));
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.needToCallStartDocument) {
            writeStartDocument();
        }
        super.comment(ch, start, length);
        this.eventFactory.setLocation(getCurrentLocation());
        try {
            this.writer.add(this.eventFactory.createComment(new String(ch, start, length)));
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        try {
            if (!this.isCDATA) {
                this.eventFactory.setLocation(getCurrentLocation());
                this.writer.add(this.eventFactory.createCharacters(new String(ch, start, length)));
            }
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        super.ignorableWhitespace(ch, start, length);
        characters(ch, start, length);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        if (this.needToCallStartDocument) {
            writeStartDocument();
        }
        super.processingInstruction(target, data);
        try {
            this.writer.add(this.eventFactory.createProcessingInstruction(target, data));
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXBaseWriter, org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        this.eventFactory.setLocation(getCurrentLocation());
        try {
            this.writer.add(this.eventFactory.createCData(this.CDATABuffer.toString()));
            super.endCDATA();
        } catch (XMLStreamException e2) {
            throw new SAXException(e2);
        }
    }

    protected void createStartEvents(Attributes attributes, Collection[] events) {
        Attribute attribute;
        Map nsMap = null;
        List attrs = null;
        if (this.namespaces != null) {
            int nDecls = this.namespaces.size();
            int i2 = 0;
            while (i2 < nDecls) {
                int i3 = i2;
                int i4 = i2 + 1;
                String prefix = (String) this.namespaces.elementAt(i3);
                String uri = (String) this.namespaces.elementAt(i4);
                Namespace ns = createNamespace(prefix, uri);
                if (nsMap == null) {
                    nsMap = new HashMap();
                }
                nsMap.put(prefix, ns);
                i2 = i4 + 1;
            }
        }
        String[] qname = {null, null};
        int s2 = attributes.getLength();
        for (int i5 = 0; i5 < s2; i5++) {
            parseQName(attributes.getQName(i5), qname);
            String attrPrefix = qname[0];
            String attrLocal = qname[1];
            String attrQName = attributes.getQName(i5);
            String attrValue = attributes.getValue(i5);
            String attrURI = attributes.getURI(i5);
            if ("xmlns".equals(attrQName) || "xmlns".equals(attrPrefix)) {
                if (nsMap == null) {
                    nsMap = new HashMap();
                }
                if (!nsMap.containsKey(attrLocal)) {
                    Namespace ns2 = createNamespace(attrLocal, attrValue);
                    nsMap.put(attrLocal, ns2);
                }
            } else {
                if (attrPrefix.length() > 0) {
                    attribute = this.eventFactory.createAttribute(attrPrefix, attrURI, attrLocal, attrValue);
                } else {
                    attribute = this.eventFactory.createAttribute(attrLocal, attrValue);
                }
                if (attrs == null) {
                    attrs = new ArrayList();
                }
                attrs.add(attribute);
            }
        }
        events[0] = nsMap == null ? Collections.EMPTY_LIST : nsMap.values();
        events[1] = attrs == null ? Collections.EMPTY_LIST : attrs;
    }

    protected Namespace createNamespace(String prefix, String uri) {
        if (prefix == null || prefix.length() == 0) {
            return this.eventFactory.createNamespace(uri);
        }
        return this.eventFactory.createNamespace(prefix, uri);
    }
}
