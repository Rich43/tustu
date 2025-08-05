package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor;
import com.sun.xml.internal.bind.v2.util.FatalAdapter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.validation.Schema;
import javax.xml.validation.ValidatorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/ValidatingUnmarshaller.class */
final class ValidatingUnmarshaller implements XmlVisitor, XmlVisitor.TextPredictor {
    private final XmlVisitor next;
    private final ValidatorHandler validator;
    private final XmlVisitor.TextPredictor predictor;
    private NamespaceContext nsContext = null;
    private char[] buf = new char[256];

    public ValidatingUnmarshaller(Schema schema, XmlVisitor next) {
        this.validator = schema.newValidatorHandler();
        this.next = next;
        this.predictor = next.getPredictor();
        this.validator.setErrorHandler(new FatalAdapter(getContext()));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startDocument(LocatorEx locator, NamespaceContext nsContext) throws SAXException {
        this.nsContext = nsContext;
        this.validator.setDocumentLocator(locator);
        this.validator.startDocument();
        this.next.startDocument(locator, nsContext);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endDocument() throws SAXException {
        this.nsContext = null;
        this.validator.endDocument();
        this.next.endDocument();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startElement(TagName tagName) throws SAXException {
        String tagNamePrefix;
        if (this.nsContext != null && (tagNamePrefix = tagName.getPrefix().intern()) != "") {
            this.validator.startPrefixMapping(tagNamePrefix, this.nsContext.getNamespaceURI(tagNamePrefix));
        }
        this.validator.startElement(tagName.uri, tagName.local, tagName.getQname(), tagName.atts);
        this.next.startElement(tagName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endElement(TagName tagName) throws SAXException {
        this.validator.endElement(tagName.uri, tagName.local, tagName.getQname());
        this.next.endElement(tagName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startPrefixMapping(String prefix, String nsUri) throws SAXException {
        this.validator.startPrefixMapping(prefix, nsUri);
        this.next.startPrefixMapping(prefix, nsUri);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endPrefixMapping(String prefix) throws SAXException {
        this.validator.endPrefixMapping(prefix);
        this.next.endPrefixMapping(prefix);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void text(CharSequence pcdata) throws SAXException {
        int len = pcdata.length();
        if (this.buf.length < len) {
            this.buf = new char[len];
        }
        for (int i2 = 0; i2 < len; i2++) {
            this.buf[i2] = pcdata.charAt(i2);
        }
        this.validator.characters(this.buf, 0, len);
        if (this.predictor.expectText()) {
            this.next.text(pcdata);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public UnmarshallingContext getContext() {
        return this.next.getContext();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public XmlVisitor.TextPredictor getPredictor() {
        return this;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor.TextPredictor
    @Deprecated
    public boolean expectText() {
        return true;
    }
}
