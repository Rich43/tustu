package com.sun.org.apache.xerces.internal.jaxp;

import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler;
import com.sun.org.apache.xerces.internal.util.AttributesProxy;
import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
import com.sun.org.apache.xerces.internal.util.ErrorHandlerProxy;
import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
import com.sun.org.apache.xerces.internal.util.LocatorProxy;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.IOException;
import javax.xml.validation.TypeInfoProvider;
import javax.xml.validation.ValidatorHandler;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/JAXPValidatorComponent.class */
final class JAXPValidatorComponent extends TeeXMLDocumentFilterImpl implements XMLComponent {
    private static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    private final ValidatorHandler validator;
    private final XNI2SAX xni2sax = new XNI2SAX();
    private final SAX2XNI sax2xni = new SAX2XNI();
    private final TypeInfoProvider typeInfoProvider;
    private Augmentations fCurrentAug;
    private XMLAttributes fCurrentAttributes;
    private SymbolTable fSymbolTable;
    private XMLErrorReporter fErrorReporter;
    private XMLEntityResolver fEntityResolver;
    private static final TypeInfoProvider noInfoProvider = new TypeInfoProvider() { // from class: com.sun.org.apache.xerces.internal.jaxp.JAXPValidatorComponent.3
        @Override // javax.xml.validation.TypeInfoProvider
        public TypeInfo getElementTypeInfo() {
            return null;
        }

        @Override // javax.xml.validation.TypeInfoProvider
        public TypeInfo getAttributeTypeInfo(int index) {
            return null;
        }

        public TypeInfo getAttributeTypeInfo(String attributeQName) {
            return null;
        }

        public TypeInfo getAttributeTypeInfo(String attributeUri, String attributeLocalName) {
            return null;
        }

        @Override // javax.xml.validation.TypeInfoProvider
        public boolean isIdAttribute(int index) {
            return false;
        }

        @Override // javax.xml.validation.TypeInfoProvider
        public boolean isSpecified(int index) {
            return false;
        }
    };

    public JAXPValidatorComponent(ValidatorHandler validatorHandler) {
        this.validator = validatorHandler;
        TypeInfoProvider tip = validatorHandler.getTypeInfoProvider();
        this.typeInfoProvider = tip == null ? noInfoProvider : tip;
        this.xni2sax.setContentHandler(this.validator);
        this.validator.setContentHandler(this.sax2xni);
        setSide(this.xni2sax);
        this.validator.setErrorHandler(new ErrorHandlerProxy() { // from class: com.sun.org.apache.xerces.internal.jaxp.JAXPValidatorComponent.1
            @Override // com.sun.org.apache.xerces.internal.util.ErrorHandlerProxy
            protected XMLErrorHandler getErrorHandler() {
                XMLErrorHandler handler = JAXPValidatorComponent.this.fErrorReporter.getErrorHandler();
                return handler != null ? handler : new ErrorHandlerWrapper(DraconianErrorHandler.getInstance());
            }
        });
        this.validator.setResourceResolver(new LSResourceResolver() { // from class: com.sun.org.apache.xerces.internal.jaxp.JAXPValidatorComponent.2
            @Override // org.w3c.dom.ls.LSResourceResolver
            public LSInput resolveResource(String type, String ns, String publicId, String systemId, String baseUri) throws XNIException {
                if (JAXPValidatorComponent.this.fEntityResolver == null) {
                    return null;
                }
                try {
                    XMLInputSource is = JAXPValidatorComponent.this.fEntityResolver.resolveEntity(new XMLResourceIdentifierImpl(publicId, systemId, baseUri, null));
                    if (is == null) {
                        return null;
                    }
                    LSInput di = new DOMInputImpl();
                    di.setBaseURI(is.getBaseSystemId());
                    di.setByteStream(is.getByteStream());
                    di.setCharacterStream(is.getCharacterStream());
                    di.setEncoding(is.getEncoding());
                    di.setPublicId(is.getPublicId());
                    di.setSystemId(is.getSystemId());
                    return di;
                } catch (IOException e2) {
                    throw new XNIException(e2);
                }
            }
        });
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.TeeXMLDocumentFilterImpl, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        this.fCurrentAttributes = attributes;
        this.fCurrentAug = augs;
        this.xni2sax.startElement(element, attributes, null);
        this.fCurrentAttributes = null;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.TeeXMLDocumentFilterImpl, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        this.fCurrentAug = augs;
        this.xni2sax.endElement(element, null);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.TeeXMLDocumentFilterImpl, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        startElement(element, attributes, augs);
        endElement(element, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.TeeXMLDocumentFilterImpl, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        this.fCurrentAug = augs;
        this.xni2sax.characters(text, null);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.TeeXMLDocumentFilterImpl, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        this.fCurrentAug = augs;
        this.xni2sax.ignorableWhitespace(text, null);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        this.fSymbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        try {
            this.fEntityResolver = (XMLEntityResolver) componentManager.getProperty(ENTITY_MANAGER);
        } catch (XMLConfigurationException e2) {
            this.fEntityResolver = null;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/JAXPValidatorComponent$SAX2XNI.class */
    private final class SAX2XNI extends DefaultHandler {
        private final Augmentations fAugmentations;
        private final QName fQName;

        private SAX2XNI() {
            this.fAugmentations = new AugmentationsImpl();
            this.fQName = new QName();
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int len) throws SAXException {
            try {
                handler().characters(new XMLString(ch, start, len), aug());
            } catch (XNIException e2) {
                throw toSAXException(e2);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void ignorableWhitespace(char[] ch, int start, int len) throws SAXException {
            try {
                handler().ignorableWhitespace(new XMLString(ch, start, len), aug());
            } catch (XNIException e2) {
                throw toSAXException(e2);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qname, Attributes atts) throws SAXException {
            try {
                JAXPValidatorComponent.this.updateAttributes(atts);
                handler().startElement(toQName(uri, localName, qname), JAXPValidatorComponent.this.fCurrentAttributes, elementAug());
            } catch (XNIException e2) {
                throw toSAXException(e2);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qname) throws SAXException {
            try {
                handler().endElement(toQName(uri, localName, qname), aug());
            } catch (XNIException e2) {
                throw toSAXException(e2);
            }
        }

        private Augmentations elementAug() {
            Augmentations aug = aug();
            return aug;
        }

        private Augmentations aug() {
            if (JAXPValidatorComponent.this.fCurrentAug != null) {
                Augmentations r2 = JAXPValidatorComponent.this.fCurrentAug;
                JAXPValidatorComponent.this.fCurrentAug = null;
                return r2;
            }
            this.fAugmentations.removeAllItems();
            return this.fAugmentations;
        }

        private XMLDocumentHandler handler() {
            return JAXPValidatorComponent.this.getDocumentHandler();
        }

        private SAXException toSAXException(XNIException xe) {
            Exception e2 = xe.getException();
            if (e2 == null) {
                e2 = xe;
            }
            return e2 instanceof SAXException ? (SAXException) e2 : new SAXException(e2);
        }

        private QName toQName(String uri, String localName, String qname) {
            String prefix = null;
            int idx = qname.indexOf(58);
            if (idx > 0) {
                prefix = JAXPValidatorComponent.this.symbolize(qname.substring(0, idx));
            }
            this.fQName.setValues(prefix, JAXPValidatorComponent.this.symbolize(localName), JAXPValidatorComponent.this.symbolize(qname), JAXPValidatorComponent.this.symbolize(uri));
            return this.fQName;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/JAXPValidatorComponent$XNI2SAX.class */
    private final class XNI2SAX extends DefaultXMLDocumentHandler {
        private ContentHandler fContentHandler;
        private String fVersion;
        protected NamespaceContext fNamespaceContext;
        private final AttributesProxy fAttributesProxy;

        private XNI2SAX() {
            this.fAttributesProxy = new AttributesProxy(null);
        }

        public void setContentHandler(ContentHandler handler) {
            this.fContentHandler = handler;
        }

        public ContentHandler getContentHandler() {
            return this.fContentHandler;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
            this.fVersion = version;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
            this.fNamespaceContext = namespaceContext;
            this.fContentHandler.setDocumentLocator(new LocatorProxy(locator));
            try {
                this.fContentHandler.startDocument();
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endDocument(Augmentations augs) throws XNIException {
            try {
                this.fContentHandler.endDocument();
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
            try {
                this.fContentHandler.processingInstruction(target, data.toString());
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
            try {
                int count = this.fNamespaceContext.getDeclaredPrefixCount();
                if (count > 0) {
                    for (int i2 = 0; i2 < count; i2++) {
                        String prefix = this.fNamespaceContext.getDeclaredPrefixAt(i2);
                        String uri = this.fNamespaceContext.getURI(prefix);
                        this.fContentHandler.startPrefixMapping(prefix, uri == null ? "" : uri);
                    }
                }
                String uri2 = element.uri != null ? element.uri : "";
                String localpart = element.localpart;
                this.fAttributesProxy.setAttributes(attributes);
                this.fContentHandler.startElement(uri2, localpart, element.rawname, this.fAttributesProxy);
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void endElement(QName element, Augmentations augs) throws XNIException {
            try {
                String uri = element.uri != null ? element.uri : "";
                String localpart = element.localpart;
                this.fContentHandler.endElement(uri, localpart, element.rawname);
                int count = this.fNamespaceContext.getDeclaredPrefixCount();
                if (count > 0) {
                    for (int i2 = 0; i2 < count; i2++) {
                        this.fContentHandler.endPrefixMapping(this.fNamespaceContext.getDeclaredPrefixAt(i2));
                    }
                }
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
            startElement(element, attributes, augs);
            endElement(element, augs);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void characters(XMLString text, Augmentations augs) throws XNIException {
            try {
                this.fContentHandler.characters(text.ch, text.offset, text.length);
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
        public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
            try {
                this.fContentHandler.ignorableWhitespace(text.ch, text.offset, text.length);
            } catch (SAXException e2) {
                throw new XNIException(e2);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/JAXPValidatorComponent$DraconianErrorHandler.class */
    private static final class DraconianErrorHandler implements ErrorHandler {
        private static final DraconianErrorHandler ERROR_HANDLER_INSTANCE = new DraconianErrorHandler();

        private DraconianErrorHandler() {
        }

        public static DraconianErrorHandler getInstance() {
            return ERROR_HANDLER_INSTANCE;
        }

        @Override // org.xml.sax.ErrorHandler
        public void warning(SAXParseException e2) throws SAXException {
        }

        @Override // org.xml.sax.ErrorHandler
        public void error(SAXParseException e2) throws SAXException {
            throw e2;
        }

        @Override // org.xml.sax.ErrorHandler
        public void fatalError(SAXParseException e2) throws SAXException {
            throw e2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAttributes(Attributes atts) {
        String prefix;
        int len = atts.getLength();
        for (int i2 = 0; i2 < len; i2++) {
            String aqn = atts.getQName(i2);
            int j2 = this.fCurrentAttributes.getIndex(aqn);
            String av2 = atts.getValue(i2);
            if (j2 == -1) {
                int idx = aqn.indexOf(58);
                if (idx < 0) {
                    prefix = null;
                } else {
                    prefix = symbolize(aqn.substring(0, idx));
                }
                this.fCurrentAttributes.addAttribute(new QName(prefix, symbolize(atts.getLocalName(i2)), symbolize(aqn), symbolize(atts.getURI(i2))), atts.getType(i2), av2);
            } else if (!av2.equals(this.fCurrentAttributes.getValue(j2))) {
                this.fCurrentAttributes.setValue(j2, av2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String symbolize(String s2) {
        return this.fSymbolTable.addSymbol(s2);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return new String[]{ENTITY_MANAGER, "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/symbol-table"};
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Boolean getFeatureDefault(String featureId) {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Object getPropertyDefault(String propertyId) {
        return null;
    }
}
