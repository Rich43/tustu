package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor;
import javax.xml.namespace.NamespaceContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/InterningXmlVisitor.class */
public final class InterningXmlVisitor implements XmlVisitor {
    private final XmlVisitor next;
    private final AttributesImpl attributes = new AttributesImpl();

    public InterningXmlVisitor(XmlVisitor next) {
        this.next = next;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startDocument(LocatorEx locator, NamespaceContext nsContext) throws SAXException {
        this.next.startDocument(locator, nsContext);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endDocument() throws SAXException {
        this.next.endDocument();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startElement(TagName tagName) throws SAXException {
        this.attributes.setAttributes(tagName.atts);
        tagName.atts = this.attributes;
        tagName.uri = intern(tagName.uri);
        tagName.local = intern(tagName.local);
        this.next.startElement(tagName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endElement(TagName tagName) throws SAXException {
        tagName.uri = intern(tagName.uri);
        tagName.local = intern(tagName.local);
        this.next.endElement(tagName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startPrefixMapping(String prefix, String nsUri) throws SAXException {
        this.next.startPrefixMapping(intern(prefix), intern(nsUri));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endPrefixMapping(String prefix) throws SAXException {
        this.next.endPrefixMapping(intern(prefix));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void text(CharSequence pcdata) throws SAXException {
        this.next.text(pcdata);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public UnmarshallingContext getContext() {
        return this.next.getContext();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public XmlVisitor.TextPredictor getPredictor() {
        return this.next.getPredictor();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/InterningXmlVisitor$AttributesImpl.class */
    private static class AttributesImpl implements Attributes {
        private Attributes core;

        private AttributesImpl() {
        }

        void setAttributes(Attributes att) {
            this.core = att;
        }

        @Override // org.xml.sax.Attributes
        public int getIndex(String qName) {
            return this.core.getIndex(qName);
        }

        @Override // org.xml.sax.Attributes
        public int getIndex(String uri, String localName) {
            return this.core.getIndex(uri, localName);
        }

        @Override // org.xml.sax.Attributes
        public int getLength() {
            return this.core.getLength();
        }

        @Override // org.xml.sax.Attributes
        public String getLocalName(int index) {
            return InterningXmlVisitor.intern(this.core.getLocalName(index));
        }

        @Override // org.xml.sax.Attributes
        public String getQName(int index) {
            return InterningXmlVisitor.intern(this.core.getQName(index));
        }

        @Override // org.xml.sax.Attributes
        public String getType(int index) {
            return InterningXmlVisitor.intern(this.core.getType(index));
        }

        @Override // org.xml.sax.Attributes
        public String getType(String qName) {
            return InterningXmlVisitor.intern(this.core.getType(qName));
        }

        @Override // org.xml.sax.Attributes
        public String getType(String uri, String localName) {
            return InterningXmlVisitor.intern(this.core.getType(uri, localName));
        }

        @Override // org.xml.sax.Attributes
        public String getURI(int index) {
            return InterningXmlVisitor.intern(this.core.getURI(index));
        }

        @Override // org.xml.sax.Attributes
        public String getValue(int index) {
            return this.core.getValue(index);
        }

        @Override // org.xml.sax.Attributes
        public String getValue(String qName) {
            return this.core.getValue(qName);
        }

        @Override // org.xml.sax.Attributes
        public String getValue(String uri, String localName) {
            return this.core.getValue(uri, localName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String intern(String s2) {
        if (s2 == null) {
            return null;
        }
        return s2.intern();
    }
}
