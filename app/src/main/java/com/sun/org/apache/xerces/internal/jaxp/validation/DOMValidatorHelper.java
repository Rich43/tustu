package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.validation.EntityState;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import java.io.IOException;
import java.util.Enumeration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/DOMValidatorHelper.class */
final class DOMValidatorHelper implements ValidatorHelper, EntityState {
    private static final int CHUNK_SIZE = 1024;
    private static final int CHUNK_MASK = 1023;
    private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    private static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
    private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
    private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private XMLErrorReporter fErrorReporter;
    private NamespaceSupport fNamespaceContext;
    private XMLSchemaValidator fSchemaValidator;
    private SymbolTable fSymbolTable;
    private ValidationManager fValidationManager;
    private XMLSchemaValidatorComponentManager fComponentManager;
    private DOMDocumentHandler fDOMValidatorHandler;
    private Node fRoot;
    private Node fCurrentElement;
    private DOMNamespaceContext fDOMNamespaceContext = new DOMNamespaceContext();
    private final SimpleLocator fXMLLocator = new SimpleLocator(null, null, -1, -1, -1);
    private final DOMResultAugmentor fDOMResultAugmentor = new DOMResultAugmentor(this);
    private final DOMResultBuilder fDOMResultBuilder = new DOMResultBuilder();
    private NamedNodeMap fEntities = null;
    private char[] fCharBuffer = new char[1024];
    final QName fElementQName = new QName();
    final QName fAttributeQName = new QName();
    final XMLAttributesImpl fAttributes = new XMLAttributesImpl();
    final XMLString fTempString = new XMLString();

    public DOMValidatorHelper(XMLSchemaValidatorComponentManager componentManager) {
        this.fComponentManager = componentManager;
        this.fErrorReporter = (XMLErrorReporter) this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fNamespaceContext = (NamespaceSupport) this.fComponentManager.getProperty(NAMESPACE_CONTEXT);
        this.fSchemaValidator = (XMLSchemaValidator) this.fComponentManager.getProperty(SCHEMA_VALIDATOR);
        this.fSymbolTable = (SymbolTable) this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fValidationManager = (ValidationManager) this.fComponentManager.getProperty(VALIDATION_MANAGER);
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.ValidatorHelper
    public void validate(Source source, Result result) throws SAXException, IOException {
        if ((result instanceof DOMResult) || result == null) {
            DOMSource domSource = (DOMSource) source;
            DOMResult domResult = (DOMResult) result;
            Node node = domSource.getNode();
            this.fRoot = node;
            if (node != null) {
                this.fComponentManager.reset();
                this.fValidationManager.setEntityState(this);
                this.fDOMNamespaceContext.reset();
                String systemId = domSource.getSystemId();
                this.fXMLLocator.setLiteralSystemId(systemId);
                this.fXMLLocator.setExpandedSystemId(systemId);
                this.fErrorReporter.setDocumentLocator(this.fXMLLocator);
                try {
                    try {
                        setupEntityMap(node.getNodeType() == 9 ? (Document) node : node.getOwnerDocument());
                        setupDOMResultHandler(domSource, domResult);
                        this.fSchemaValidator.startDocument(this.fXMLLocator, null, this.fDOMNamespaceContext, null);
                        validate(node);
                        this.fSchemaValidator.endDocument(null);
                        this.fRoot = null;
                        this.fEntities = null;
                        if (this.fDOMValidatorHandler != null) {
                            this.fDOMValidatorHandler.setDOMResult(null);
                            return;
                        }
                        return;
                    } catch (XMLParseException e2) {
                        throw Util.toSAXParseException(e2);
                    } catch (XNIException e3) {
                        throw Util.toSAXException(e3);
                    }
                } catch (Throwable th) {
                    this.fRoot = null;
                    this.fEntities = null;
                    if (this.fDOMValidatorHandler != null) {
                        this.fDOMValidatorHandler.setDOMResult(null);
                    }
                    throw th;
                }
            }
            return;
        }
        throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[]{source.getClass().getName(), result.getClass().getName()}));
    }

    @Override // com.sun.org.apache.xerces.internal.impl.validation.EntityState
    public boolean isEntityDeclared(String name) {
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.validation.EntityState
    public boolean isEntityUnparsed(String name) {
        Entity entity;
        return (this.fEntities == null || (entity = (Entity) this.fEntities.getNamedItem(name)) == null || entity.getNotationName() == null) ? false : true;
    }

    private void validate(Node node) throws XNIException {
        while (node != null) {
            beginNode(node);
            Node next = node.getFirstChild();
            while (next == null) {
                finishNode(node);
                if (node == node) {
                    break;
                }
                next = node.getNextSibling();
                if (next == null) {
                    node = node.getParentNode();
                    if (node == null || node == node) {
                        if (node != null) {
                            finishNode(node);
                        }
                        next = null;
                    }
                }
                node = next;
            }
            node = next;
        }
    }

    private void beginNode(Node node) throws XNIException {
        switch (node.getNodeType()) {
            case 1:
                this.fCurrentElement = node;
                this.fNamespaceContext.pushContext();
                fillQName(this.fElementQName, node);
                processAttributes(node.getAttributes());
                this.fSchemaValidator.startElement(this.fElementQName, this.fAttributes, null);
                break;
            case 3:
                if (this.fDOMValidatorHandler != null) {
                    this.fDOMValidatorHandler.setIgnoringCharacters(true);
                    sendCharactersToValidator(node.getNodeValue());
                    this.fDOMValidatorHandler.setIgnoringCharacters(false);
                    this.fDOMValidatorHandler.characters((Text) node);
                    break;
                } else {
                    sendCharactersToValidator(node.getNodeValue());
                    break;
                }
            case 4:
                if (this.fDOMValidatorHandler != null) {
                    this.fDOMValidatorHandler.setIgnoringCharacters(true);
                    this.fSchemaValidator.startCDATA(null);
                    sendCharactersToValidator(node.getNodeValue());
                    this.fSchemaValidator.endCDATA(null);
                    this.fDOMValidatorHandler.setIgnoringCharacters(false);
                    this.fDOMValidatorHandler.cdata((CDATASection) node);
                    break;
                } else {
                    this.fSchemaValidator.startCDATA(null);
                    sendCharactersToValidator(node.getNodeValue());
                    this.fSchemaValidator.endCDATA(null);
                    break;
                }
            case 7:
                if (this.fDOMValidatorHandler != null) {
                    this.fDOMValidatorHandler.processingInstruction((ProcessingInstruction) node);
                    break;
                }
                break;
            case 8:
                if (this.fDOMValidatorHandler != null) {
                    this.fDOMValidatorHandler.comment((Comment) node);
                    break;
                }
                break;
            case 10:
                if (this.fDOMValidatorHandler != null) {
                    this.fDOMValidatorHandler.doctypeDecl((DocumentType) node);
                    break;
                }
                break;
        }
    }

    private void finishNode(Node node) throws XNIException {
        if (node.getNodeType() == 1) {
            this.fCurrentElement = node;
            fillQName(this.fElementQName, node);
            this.fSchemaValidator.endElement(this.fElementQName, null);
            this.fNamespaceContext.popContext();
        }
    }

    private void setupEntityMap(Document doc) {
        DocumentType docType;
        if (doc != null && (docType = doc.getDoctype()) != null) {
            this.fEntities = docType.getEntities();
        } else {
            this.fEntities = null;
        }
    }

    private void setupDOMResultHandler(DOMSource source, DOMResult result) throws SAXException {
        if (result == null) {
            this.fDOMValidatorHandler = null;
            this.fSchemaValidator.setDocumentHandler(null);
            return;
        }
        Node nodeResult = result.getNode();
        if (source.getNode() == nodeResult) {
            this.fDOMValidatorHandler = this.fDOMResultAugmentor;
            this.fDOMResultAugmentor.setDOMResult(result);
            this.fSchemaValidator.setDocumentHandler(this.fDOMResultAugmentor);
            return;
        }
        if (result.getNode() == null) {
            try {
                DocumentBuilderFactory factory = JdkXmlUtils.getDOMFactory(this.fComponentManager.getFeature(JdkXmlUtils.OVERRIDE_PARSER));
                DocumentBuilder builder = factory.newDocumentBuilder();
                result.setNode(builder.newDocument());
            } catch (ParserConfigurationException e2) {
                throw new SAXException(e2);
            }
        }
        this.fDOMValidatorHandler = this.fDOMResultBuilder;
        this.fDOMResultBuilder.setDOMResult(result);
        this.fSchemaValidator.setDocumentHandler(this.fDOMResultBuilder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillQName(QName toFill, Node node) {
        String prefix = node.getPrefix();
        String localName = node.getLocalName();
        String rawName = node.getNodeName();
        String namespace = node.getNamespaceURI();
        toFill.uri = (namespace == null || namespace.length() <= 0) ? null : this.fSymbolTable.addSymbol(namespace);
        toFill.rawname = rawName != null ? this.fSymbolTable.addSymbol(rawName) : XMLSymbols.EMPTY_STRING;
        if (localName == null) {
            int k2 = rawName.indexOf(58);
            if (k2 > 0) {
                toFill.prefix = this.fSymbolTable.addSymbol(rawName.substring(0, k2));
                toFill.localpart = this.fSymbolTable.addSymbol(rawName.substring(k2 + 1));
                return;
            } else {
                toFill.prefix = XMLSymbols.EMPTY_STRING;
                toFill.localpart = toFill.rawname;
                return;
            }
        }
        toFill.prefix = prefix != null ? this.fSymbolTable.addSymbol(prefix) : XMLSymbols.EMPTY_STRING;
        toFill.localpart = localName != null ? this.fSymbolTable.addSymbol(localName) : XMLSymbols.EMPTY_STRING;
    }

    private void processAttributes(NamedNodeMap attrMap) {
        int attrCount = attrMap.getLength();
        this.fAttributes.removeAllAttributes();
        for (int i2 = 0; i2 < attrCount; i2++) {
            Attr attr = (Attr) attrMap.item(i2);
            String value = attr.getValue();
            if (value == null) {
                value = XMLSymbols.EMPTY_STRING;
            }
            fillQName(this.fAttributeQName, attr);
            this.fAttributes.addAttributeNS(this.fAttributeQName, XMLSymbols.fCDATASymbol, value);
            this.fAttributes.setSpecified(i2, attr.getSpecified());
            if (this.fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
                if (this.fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                    this.fNamespaceContext.declarePrefix(this.fAttributeQName.localpart, value.length() != 0 ? this.fSymbolTable.addSymbol(value) : null);
                } else {
                    this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, value.length() != 0 ? this.fSymbolTable.addSymbol(value) : null);
                }
            }
        }
    }

    private void sendCharactersToValidator(String str) throws XNIException {
        if (str != null) {
            int length = str.length();
            int remainder = length & 1023;
            if (remainder > 0) {
                str.getChars(0, remainder, this.fCharBuffer, 0);
                this.fTempString.setValues(this.fCharBuffer, 0, remainder);
                this.fSchemaValidator.characters(this.fTempString, null);
            }
            int i2 = remainder;
            while (i2 < length) {
                int i3 = i2;
                i2 += 1024;
                str.getChars(i3, i2, this.fCharBuffer, 0);
                this.fTempString.setValues(this.fCharBuffer, 0, 1024);
                this.fSchemaValidator.characters(this.fTempString, null);
            }
        }
    }

    Node getCurrentElement() {
        return this.fCurrentElement;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/DOMValidatorHelper$DOMNamespaceContext.class */
    final class DOMNamespaceContext implements NamespaceContext {
        protected String[] fNamespace = new String[32];
        protected int fNamespaceSize = 0;
        protected boolean fDOMContextBuilt = false;

        DOMNamespaceContext() {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
        public void pushContext() {
            DOMValidatorHelper.this.fNamespaceContext.pushContext();
        }

        @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
        public void popContext() {
            DOMValidatorHelper.this.fNamespaceContext.popContext();
        }

        @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
        public boolean declarePrefix(String prefix, String uri) {
            return DOMValidatorHelper.this.fNamespaceContext.declarePrefix(prefix, uri);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
        public String getURI(String prefix) {
            String uri = DOMValidatorHelper.this.fNamespaceContext.getURI(prefix);
            if (uri == null) {
                if (!this.fDOMContextBuilt) {
                    fillNamespaceContext();
                    this.fDOMContextBuilt = true;
                }
                if (this.fNamespaceSize > 0 && !DOMValidatorHelper.this.fNamespaceContext.containsPrefix(prefix)) {
                    uri = getURI0(prefix);
                }
            }
            return uri;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
        public String getPrefix(String uri) {
            return DOMValidatorHelper.this.fNamespaceContext.getPrefix(uri);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
        public int getDeclaredPrefixCount() {
            return DOMValidatorHelper.this.fNamespaceContext.getDeclaredPrefixCount();
        }

        @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
        public String getDeclaredPrefixAt(int index) {
            return DOMValidatorHelper.this.fNamespaceContext.getDeclaredPrefixAt(index);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
        public Enumeration getAllPrefixes() {
            return DOMValidatorHelper.this.fNamespaceContext.getAllPrefixes();
        }

        @Override // com.sun.org.apache.xerces.internal.xni.NamespaceContext
        public void reset() {
            this.fDOMContextBuilt = false;
            this.fNamespaceSize = 0;
        }

        private void fillNamespaceContext() {
            if (DOMValidatorHelper.this.fRoot != null) {
                Node parentNode = DOMValidatorHelper.this.fRoot.getParentNode();
                while (true) {
                    Node currentNode = parentNode;
                    if (currentNode != null) {
                        if (1 == currentNode.getNodeType()) {
                            NamedNodeMap attributes = currentNode.getAttributes();
                            int attrCount = attributes.getLength();
                            for (int i2 = 0; i2 < attrCount; i2++) {
                                Attr attr = (Attr) attributes.item(i2);
                                String value = attr.getValue();
                                if (value == null) {
                                    value = XMLSymbols.EMPTY_STRING;
                                }
                                DOMValidatorHelper.this.fillQName(DOMValidatorHelper.this.fAttributeQName, attr);
                                if (DOMValidatorHelper.this.fAttributeQName.uri == NamespaceContext.XMLNS_URI) {
                                    if (DOMValidatorHelper.this.fAttributeQName.prefix == XMLSymbols.PREFIX_XMLNS) {
                                        declarePrefix0(DOMValidatorHelper.this.fAttributeQName.localpart, value.length() != 0 ? DOMValidatorHelper.this.fSymbolTable.addSymbol(value) : null);
                                    } else {
                                        declarePrefix0(XMLSymbols.EMPTY_STRING, value.length() != 0 ? DOMValidatorHelper.this.fSymbolTable.addSymbol(value) : null);
                                    }
                                }
                            }
                        }
                        parentNode = currentNode.getParentNode();
                    } else {
                        return;
                    }
                }
            }
        }

        private void declarePrefix0(String prefix, String uri) {
            if (this.fNamespaceSize == this.fNamespace.length) {
                String[] namespacearray = new String[this.fNamespaceSize * 2];
                System.arraycopy(this.fNamespace, 0, namespacearray, 0, this.fNamespaceSize);
                this.fNamespace = namespacearray;
            }
            String[] strArr = this.fNamespace;
            int i2 = this.fNamespaceSize;
            this.fNamespaceSize = i2 + 1;
            strArr[i2] = prefix;
            String[] strArr2 = this.fNamespace;
            int i3 = this.fNamespaceSize;
            this.fNamespaceSize = i3 + 1;
            strArr2[i3] = uri;
        }

        private String getURI0(String prefix) {
            for (int i2 = 0; i2 < this.fNamespaceSize; i2 += 2) {
                if (this.fNamespace[i2] == prefix) {
                    return this.fNamespace[i2 + 1];
                }
            }
            return null;
        }
    }
}
