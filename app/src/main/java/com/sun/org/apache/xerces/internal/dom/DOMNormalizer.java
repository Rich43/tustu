package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;
import com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar;
import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDDescription;
import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
import com.sun.org.apache.xerces.internal.jaxp.JAXPConstants;
import com.sun.org.apache.xerces.internal.parsers.XMLGrammarPreparser;
import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XML11Char;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.Vector;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMNormalizer.class */
public class DOMNormalizer implements XMLDocumentHandler {
    protected static final boolean DEBUG_ND = false;
    protected static final boolean DEBUG = false;
    protected static final boolean DEBUG_EVENTS = false;
    protected static final String PREFIX = "NS";
    protected RevalidationHandler fValidationHandler;
    protected SymbolTable fSymbolTable;
    protected DOMErrorHandler fErrorHandler;
    private XMLDTDValidator fDTDValidator;
    protected DOMConfigurationImpl fConfiguration = null;
    protected CoreDocumentImpl fDocument = null;
    protected final XMLAttributesProxy fAttrProxy = new XMLAttributesProxy();
    protected final QName fQName = new QName();
    private final DOMErrorImpl fError = new DOMErrorImpl();
    protected boolean fNamespaceValidation = false;
    protected boolean fPSVI = false;
    protected final NamespaceContext fNamespaceContext = new NamespaceSupport();
    protected final NamespaceContext fLocalNSBinder = new NamespaceSupport();
    protected final ArrayList fAttributeList = new ArrayList(5);
    protected final DOMLocatorImpl fLocator = new DOMLocatorImpl();
    protected Node fCurrentNode = null;
    private QName fAttrQName = new QName();
    final XMLString fNormalizedValue = new XMLString(new char[16], 0, 0);
    private boolean allWhitespace = false;

    protected void normalizeDocument(CoreDocumentImpl document, DOMConfigurationImpl config) {
        this.fDocument = document;
        this.fConfiguration = config;
        this.fSymbolTable = (SymbolTable) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fNamespaceContext.reset();
        this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
        if ((this.fConfiguration.features & 64) != 0) {
            String schemaLang = (String) this.fConfiguration.getProperty(JAXPConstants.JAXP_SCHEMA_LANGUAGE);
            if (schemaLang != null && schemaLang.equals(Constants.NS_XMLSCHEMA)) {
                this.fValidationHandler = CoreDOMImplementationImpl.singleton.getValidator("http://www.w3.org/2001/XMLSchema");
                this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema", true);
                this.fConfiguration.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
                this.fNamespaceValidation = true;
                this.fPSVI = (this.fConfiguration.features & 128) != 0;
            }
            this.fConfiguration.setFeature("http://xml.org/sax/features/validation", true);
            this.fDocument.clearIdentifiers();
            if (this.fValidationHandler != null) {
                ((XMLComponent) this.fValidationHandler).reset(this.fConfiguration);
            }
        }
        this.fErrorHandler = (DOMErrorHandler) this.fConfiguration.getParameter(Constants.DOM_ERROR_HANDLER);
        if (this.fValidationHandler != null) {
            this.fValidationHandler.setDocumentHandler(this);
            this.fValidationHandler.startDocument(new SimpleLocator(this.fDocument.fDocumentURI, this.fDocument.fDocumentURI, -1, -1), this.fDocument.encoding, this.fNamespaceContext, null);
        }
        try {
            Node kid = this.fDocument.getFirstChild();
            while (kid != null) {
                Node next = kid.getNextSibling();
                Node kid2 = normalizeNode(kid);
                if (kid2 != null) {
                    next = kid2;
                }
                kid = next;
            }
            if (this.fValidationHandler != null) {
                this.fValidationHandler.endDocument(null);
                CoreDOMImplementationImpl.singleton.releaseValidator("http://www.w3.org/2001/XMLSchema", this.fValidationHandler);
                this.fValidationHandler = null;
            }
        } catch (AbortException e2) {
        } catch (RuntimeException e3) {
            throw e3;
        }
    }

    protected Node normalizeNode(Node node) throws DOMException, MissingResourceException, XNIException {
        boolean wellformed;
        Node nextSibling;
        boolean wellformed2;
        int type = node.getNodeType();
        this.fLocator.fRelatedNode = node;
        switch (type) {
            case 1:
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0 && this.fDocument.isXMLVersionChanged()) {
                    if (this.fNamespaceValidation) {
                        wellformed2 = CoreDocumentImpl.isValidQName(node.getPrefix(), node.getLocalName(), this.fDocument.isXML11Version());
                    } else {
                        wellformed2 = CoreDocumentImpl.isXMLName(node.getNodeName(), this.fDocument.isXML11Version());
                    }
                    if (!wellformed2) {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[]{"Element", node.getNodeName()});
                        reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short) 2, "wf-invalid-character-in-node-name");
                    }
                }
                this.fNamespaceContext.pushContext();
                this.fLocalNSBinder.reset();
                ElementImpl elem = (ElementImpl) node;
                if (elem.needsSyncChildren()) {
                    elem.synchronizeChildren();
                }
                AttributeMap attributes = elem.hasAttributes() ? (AttributeMap) elem.getAttributes() : null;
                if ((this.fConfiguration.features & 1) != 0) {
                    namespaceFixUp(elem, attributes);
                    if ((this.fConfiguration.features & 512) == 0 && attributes != null) {
                        int i2 = 0;
                        while (i2 < attributes.getLength()) {
                            Attr att = (Attr) attributes.getItem(i2);
                            if (XMLSymbols.PREFIX_XMLNS.equals(att.getPrefix()) || XMLSymbols.PREFIX_XMLNS.equals(att.getName())) {
                                elem.removeAttributeNode(att);
                                i2--;
                            }
                            i2++;
                        }
                    }
                } else if (attributes != null) {
                    for (int i3 = 0; i3 < attributes.getLength(); i3++) {
                        Attr attr = (Attr) attributes.item(i3);
                        attr.normalize();
                        if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                            isAttrValueWF(this.fErrorHandler, this.fError, this.fLocator, attributes, (AttrImpl) attr, attr.getValue(), this.fDocument.isXML11Version());
                            if (this.fDocument.isXMLVersionChanged()) {
                                boolean wellformed3 = CoreDocumentImpl.isXMLName(node.getNodeName(), this.fDocument.isXML11Version());
                                if (!wellformed3) {
                                    String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[]{"Attr", node.getNodeName()});
                                    reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg2, (short) 2, "wf-invalid-character-in-node-name");
                                }
                            }
                        }
                    }
                }
                if (this.fValidationHandler != null) {
                    this.fAttrProxy.setAttributes(attributes, this.fDocument, elem);
                    updateQName(elem, this.fQName);
                    this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    this.fCurrentNode = node;
                    this.fValidationHandler.startElement(this.fQName, this.fAttrProxy, null);
                }
                if (this.fDTDValidator != null) {
                    this.fAttrProxy.setAttributes(attributes, this.fDocument, elem);
                    updateQName(elem, this.fQName);
                    this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    this.fCurrentNode = node;
                    this.fDTDValidator.startElement(this.fQName, this.fAttrProxy, null);
                }
                Node firstChild = elem.getFirstChild();
                while (true) {
                    Node kid = firstChild;
                    if (kid != null) {
                        Node next = kid.getNextSibling();
                        Node kid2 = normalizeNode(kid);
                        if (kid2 != null) {
                            next = kid2;
                        }
                        firstChild = next;
                    } else {
                        if (this.fValidationHandler != null) {
                            updateQName(elem, this.fQName);
                            this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                            this.fCurrentNode = node;
                            this.fValidationHandler.endElement(this.fQName, null);
                        }
                        if (this.fDTDValidator != null) {
                            updateQName(elem, this.fQName);
                            this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                            this.fCurrentNode = node;
                            this.fDTDValidator.endElement(this.fQName, null);
                        }
                        this.fNamespaceContext.popContext();
                        return null;
                    }
                }
                break;
            case 2:
            case 6:
            case 9:
            default:
                return null;
            case 3:
                Node next2 = node.getNextSibling();
                if (next2 != null && next2.getNodeType() == 3) {
                    ((Text) node).appendData(next2.getNodeValue());
                    node.getParentNode().removeChild(next2);
                    return node;
                }
                if (node.getNodeValue().length() == 0) {
                    node.getParentNode().removeChild(node);
                    return null;
                }
                short nextType = next2 != null ? next2.getNodeType() : (short) -1;
                if (nextType != -1) {
                    if ((this.fConfiguration.features & 4) == 0 && nextType == 6) {
                        return null;
                    }
                    if ((this.fConfiguration.features & 32) == 0 && nextType == 8) {
                        return null;
                    }
                    if ((this.fConfiguration.features & 8) == 0 && nextType == 4) {
                        return null;
                    }
                }
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                    isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), this.fDocument.isXML11Version());
                }
                if (this.fValidationHandler != null) {
                    this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    this.fCurrentNode = node;
                    this.fValidationHandler.characterData(node.getNodeValue(), null);
                }
                if (this.fDTDValidator != null) {
                    this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    this.fCurrentNode = node;
                    this.fDTDValidator.characterData(node.getNodeValue(), null);
                    if (this.allWhitespace) {
                        this.allWhitespace = false;
                        ((TextImpl) node).setIgnorableWhitespace(true);
                        return null;
                    }
                    return null;
                }
                return null;
            case 4:
                if ((this.fConfiguration.features & 8) == 0) {
                    Node prevSibling = node.getPreviousSibling();
                    if (prevSibling != null && prevSibling.getNodeType() == 3) {
                        ((Text) prevSibling).appendData(node.getNodeValue());
                        node.getParentNode().removeChild(node);
                        return prevSibling;
                    }
                    Text text = this.fDocument.createTextNode(node.getNodeValue());
                    Node parent = node.getParentNode();
                    parent.replaceChild(text, node);
                    return text;
                }
                if (this.fValidationHandler != null) {
                    this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    this.fCurrentNode = node;
                    this.fValidationHandler.startCDATA(null);
                    this.fValidationHandler.characterData(node.getNodeValue(), null);
                    this.fValidationHandler.endCDATA(null);
                }
                if (this.fDTDValidator != null) {
                    this.fConfiguration.fErrorHandlerWrapper.fCurrentNode = node;
                    this.fCurrentNode = node;
                    this.fDTDValidator.startCDATA(null);
                    this.fDTDValidator.characterData(node.getNodeValue(), null);
                    this.fDTDValidator.endCDATA(null);
                }
                String value = node.getNodeValue();
                if ((this.fConfiguration.features & 16) != 0) {
                    Node parent2 = node.getParentNode();
                    if (this.fDocument.errorChecking) {
                        isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, node.getNodeValue(), this.fDocument.isXML11Version());
                    }
                    while (true) {
                        int index = value.indexOf("]]>");
                        if (index >= 0) {
                            node.setNodeValue(value.substring(0, index + 2));
                            value = value.substring(index + 2);
                            Node firstSplitNode = node;
                            Node newChild = this.fDocument.createCDATASection(value);
                            parent2.insertBefore(newChild, node.getNextSibling());
                            node = newChild;
                            this.fLocator.fRelatedNode = firstSplitNode;
                            String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "cdata-sections-splitted", null);
                            reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg3, (short) 1, "cdata-sections-splitted");
                        } else {
                            return null;
                        }
                    }
                } else {
                    if (this.fDocument.errorChecking) {
                        isCDataWF(this.fErrorHandler, this.fError, this.fLocator, value, this.fDocument.isXML11Version());
                        return null;
                    }
                    return null;
                }
                break;
            case 5:
                if ((this.fConfiguration.features & 4) == 0) {
                    Node prevSibling2 = node.getPreviousSibling();
                    Node parent3 = node.getParentNode();
                    ((EntityReferenceImpl) node).setReadOnly(false, true);
                    expandEntityRef(parent3, node);
                    parent3.removeChild(node);
                    Node next3 = prevSibling2 != null ? prevSibling2.getNextSibling() : parent3.getFirstChild();
                    if (prevSibling2 != null && next3 != null && prevSibling2.getNodeType() == 3 && next3.getNodeType() == 3) {
                        return prevSibling2;
                    }
                    return next3;
                }
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0 && this.fDocument.isXMLVersionChanged()) {
                    CoreDocumentImpl.isXMLName(node.getNodeName(), this.fDocument.isXML11Version());
                    return null;
                }
                return null;
            case 7:
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                    ProcessingInstruction pinode = (ProcessingInstruction) node;
                    String target = pinode.getTarget();
                    if (this.fDocument.isXML11Version()) {
                        wellformed = XML11Char.isXML11ValidName(target);
                    } else {
                        wellformed = XMLChar.isValidName(target);
                    }
                    if (!wellformed) {
                        String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[]{"Element", node.getNodeName()});
                        reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg4, (short) 2, "wf-invalid-character-in-node-name");
                    }
                    isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, pinode.getData(), this.fDocument.isXML11Version());
                    return null;
                }
                return null;
            case 8:
                if ((this.fConfiguration.features & 32) == 0) {
                    Node prevSibling3 = node.getPreviousSibling();
                    Node parent4 = node.getParentNode();
                    parent4.removeChild(node);
                    if (prevSibling3 != null && prevSibling3.getNodeType() == 3 && (nextSibling = prevSibling3.getNextSibling()) != null && nextSibling.getNodeType() == 3) {
                        ((TextImpl) nextSibling).insertData(0, prevSibling3.getNodeValue());
                        parent4.removeChild(prevSibling3);
                        return nextSibling;
                    }
                    return null;
                }
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                    String commentdata = ((Comment) node).getData();
                    isCommentWF(this.fErrorHandler, this.fError, this.fLocator, commentdata, this.fDocument.isXML11Version());
                    return null;
                }
                return null;
            case 10:
                DocumentTypeImpl docType = (DocumentTypeImpl) node;
                this.fDTDValidator = (XMLDTDValidator) CoreDOMImplementationImpl.singleton.getValidator("http://www.w3.org/TR/REC-xml");
                this.fDTDValidator.setDocumentHandler(this);
                this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", createGrammarPool(docType));
                this.fDTDValidator.reset(this.fConfiguration);
                this.fDTDValidator.startDocument(new SimpleLocator(this.fDocument.fDocumentURI, this.fDocument.fDocumentURI, -1, -1), this.fDocument.encoding, this.fNamespaceContext, null);
                this.fDTDValidator.doctypeDecl(docType.getName(), docType.getPublicId(), docType.getSystemId(), null);
                return null;
        }
    }

    private XMLGrammarPool createGrammarPool(DocumentTypeImpl docType) {
        XMLGrammarPoolImpl pool = new XMLGrammarPoolImpl();
        XMLGrammarPreparser preParser = new XMLGrammarPreparser(this.fSymbolTable);
        preParser.registerPreparser("http://www.w3.org/TR/REC-xml", null);
        preParser.setFeature("http://apache.org/xml/features/namespaces", true);
        preParser.setFeature("http://apache.org/xml/features/validation", true);
        preParser.setProperty("http://apache.org/xml/properties/internal/grammar-pool", pool);
        String internalSubset = docType.getInternalSubset();
        XMLInputSource is = new XMLInputSource(docType.getPublicId(), docType.getSystemId(), null);
        if (internalSubset != null) {
            is.setCharacterStream(new StringReader(internalSubset));
        }
        try {
            DTDGrammar g2 = (DTDGrammar) preParser.preparseGrammar("http://www.w3.org/TR/REC-xml", is);
            ((XMLDTDDescription) g2.getGrammarDescription()).setRootName(docType.getName());
            is.setCharacterStream(null);
            DTDGrammar g3 = (DTDGrammar) preParser.preparseGrammar("http://www.w3.org/TR/REC-xml", is);
            ((XMLDTDDescription) g3.getGrammarDescription()).setRootName(docType.getName());
        } catch (XNIException e2) {
        } catch (IOException e3) {
        }
        return pool;
    }

    protected final void expandEntityRef(Node parent, Node reference) throws DOMException {
        Node firstChild = reference.getFirstChild();
        while (true) {
            Node kid = firstChild;
            if (kid != null) {
                Node next = kid.getNextSibling();
                parent.insertBefore(kid, reference);
                firstChild = next;
            } else {
                return;
            }
        }
    }

    protected final void namespaceFixUp(ElementImpl element, AttributeMap attributes) throws DOMException, MissingResourceException {
        if (attributes != null) {
            for (int k2 = 0; k2 < attributes.getLength(); k2++) {
                Attr attr = (Attr) attributes.getItem(k2);
                if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0 && this.fDocument.isXMLVersionChanged()) {
                    this.fDocument.checkQName(attr.getPrefix(), attr.getLocalName());
                }
                String uri = attr.getNamespaceURI();
                if (uri != null && uri.equals(NamespaceContext.XMLNS_URI) && (this.fConfiguration.features & 512) != 0) {
                    String value = attr.getNodeValue();
                    if (value == null) {
                        value = XMLSymbols.EMPTY_STRING;
                    }
                    if (this.fDocument.errorChecking && value.equals(NamespaceContext.XMLNS_URI)) {
                        this.fLocator.fRelatedNode = attr;
                        String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CantBindXMLNS", null);
                        reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg, (short) 2, "CantBindXMLNS");
                    } else {
                        String prefix = attr.getPrefix();
                        String prefix2 = (prefix == null || prefix.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix);
                        String localpart = this.fSymbolTable.addSymbol(attr.getLocalName());
                        if (prefix2 == XMLSymbols.PREFIX_XMLNS) {
                            String value2 = this.fSymbolTable.addSymbol(value);
                            if (value2.length() != 0) {
                                this.fNamespaceContext.declarePrefix(localpart, value2);
                            }
                        } else {
                            this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, this.fSymbolTable.addSymbol(value));
                        }
                    }
                }
            }
        }
        String uri2 = element.getNamespaceURI();
        String prefix3 = element.getPrefix();
        if ((this.fConfiguration.features & 512) != 0) {
            if (uri2 != null) {
                String uri3 = this.fSymbolTable.addSymbol(uri2);
                String prefix4 = (prefix3 == null || prefix3.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix3);
                if (this.fNamespaceContext.getURI(prefix4) != uri3) {
                    addNamespaceDecl(prefix4, uri3, element);
                    this.fLocalNSBinder.declarePrefix(prefix4, uri3);
                    this.fNamespaceContext.declarePrefix(prefix4, uri3);
                }
            } else if (element.getLocalName() == null) {
                if (this.fNamespaceValidation) {
                    String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NullLocalElementName", new Object[]{element.getNodeName()});
                    reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg2, (short) 3, "NullLocalElementName");
                } else {
                    String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NullLocalElementName", new Object[]{element.getNodeName()});
                    reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg3, (short) 2, "NullLocalElementName");
                }
            } else {
                String uri4 = this.fNamespaceContext.getURI(XMLSymbols.EMPTY_STRING);
                if (uri4 != null && uri4.length() > 0) {
                    addNamespaceDecl(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING, element);
                    this.fLocalNSBinder.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                    this.fNamespaceContext.declarePrefix(XMLSymbols.EMPTY_STRING, XMLSymbols.EMPTY_STRING);
                }
            }
        }
        if (attributes != null) {
            attributes.cloneMap(this.fAttributeList);
            for (int i2 = 0; i2 < this.fAttributeList.size(); i2++) {
                Attr attr2 = (Attr) this.fAttributeList.get(i2);
                this.fLocator.fRelatedNode = attr2;
                attr2.normalize();
                String value3 = attr2.getValue();
                attr2.getNodeName();
                String uri5 = attr2.getNamespaceURI();
                if (value3 == null) {
                    value3 = XMLSymbols.EMPTY_STRING;
                }
                if (uri5 != null) {
                    String prefix5 = attr2.getPrefix();
                    String prefix6 = (prefix5 == null || prefix5.length() == 0) ? XMLSymbols.EMPTY_STRING : this.fSymbolTable.addSymbol(prefix5);
                    this.fSymbolTable.addSymbol(attr2.getLocalName());
                    if (uri5 == null || !uri5.equals(NamespaceContext.XMLNS_URI)) {
                        if (this.fDocument.errorChecking && (this.fConfiguration.features & 256) != 0) {
                            isAttrValueWF(this.fErrorHandler, this.fError, this.fLocator, attributes, (AttrImpl) attr2, attr2.getValue(), this.fDocument.isXML11Version());
                            if (this.fDocument.isXMLVersionChanged()) {
                                boolean wellformed = CoreDocumentImpl.isXMLName(attr2.getNodeName(), this.fDocument.isXML11Version());
                                if (!wellformed) {
                                    String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "wf-invalid-character-in-node-name", new Object[]{"Attribute", attr2.getNodeName()});
                                    reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg4, (short) 2, "wf-invalid-character-in-node-name");
                                }
                            }
                        }
                        ((AttrImpl) attr2).setIdAttribute(false);
                        String uri6 = this.fSymbolTable.addSymbol(uri5);
                        String declaredURI = this.fNamespaceContext.getURI(prefix6);
                        if (prefix6 == XMLSymbols.EMPTY_STRING || declaredURI != uri6) {
                            attr2.getNodeName();
                            String declaredPrefix = this.fNamespaceContext.getPrefix(uri6);
                            if (declaredPrefix != null && declaredPrefix != XMLSymbols.EMPTY_STRING) {
                                prefix6 = declaredPrefix;
                            } else {
                                if (prefix6 == XMLSymbols.EMPTY_STRING || this.fLocalNSBinder.getURI(prefix6) != null) {
                                    int counter = 1 + 1;
                                    String strAddSymbol = this.fSymbolTable.addSymbol(PREFIX + 1);
                                    while (true) {
                                        prefix6 = strAddSymbol;
                                        if (this.fLocalNSBinder.getURI(prefix6) == null) {
                                            break;
                                        }
                                        int i3 = counter;
                                        counter++;
                                        strAddSymbol = this.fSymbolTable.addSymbol(PREFIX + i3);
                                    }
                                }
                                addNamespaceDecl(prefix6, uri6, element);
                                this.fLocalNSBinder.declarePrefix(prefix6, this.fSymbolTable.addSymbol(value3));
                                this.fNamespaceContext.declarePrefix(prefix6, uri6);
                            }
                            attr2.setPrefix(prefix6);
                        }
                    }
                } else {
                    ((AttrImpl) attr2).setIdAttribute(false);
                    if (attr2.getLocalName() == null) {
                        if (this.fNamespaceValidation) {
                            String msg5 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NullLocalAttrName", new Object[]{attr2.getNodeName()});
                            reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg5, (short) 3, "NullLocalAttrName");
                        } else {
                            String msg6 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NullLocalAttrName", new Object[]{attr2.getNodeName()});
                            reportDOMError(this.fErrorHandler, this.fError, this.fLocator, msg6, (short) 2, "NullLocalAttrName");
                        }
                    }
                }
            }
        }
    }

    protected final void addNamespaceDecl(String prefix, String uri, ElementImpl element) throws DOMException, MissingResourceException {
        if (prefix == XMLSymbols.EMPTY_STRING) {
            element.setAttributeNS(NamespaceContext.XMLNS_URI, XMLSymbols.PREFIX_XMLNS, uri);
        } else {
            element.setAttributeNS(NamespaceContext.XMLNS_URI, "xmlns:" + prefix, uri);
        }
    }

    public static final void isCDataWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, String datavalue, boolean isXML11Version) throws MissingResourceException {
        if (datavalue == null || datavalue.length() == 0) {
            return;
        }
        char[] dataarray = datavalue.toCharArray();
        int datalength = dataarray.length;
        if (isXML11Version) {
            int i2 = 0;
            while (i2 < datalength) {
                int i3 = i2;
                i2++;
                char c2 = dataarray[i3];
                if (XML11Char.isXML11Invalid(c2)) {
                    if (XMLChar.isHighSurrogate(c2) && i2 < datalength) {
                        i2++;
                        char c22 = dataarray[i2];
                        if (!XMLChar.isLowSurrogate(c22) || !XMLChar.isSupplemental(XMLChar.supplemental(c2, c22))) {
                        }
                    }
                    String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInCDSect", new Object[]{Integer.toString(c2, 16)});
                    reportDOMError(errorHandler, error, locator, msg, (short) 2, "wf-invalid-character");
                } else if (c2 == ']') {
                    int count = i2;
                    if (count < datalength && dataarray[count] == ']') {
                        do {
                            count++;
                            if (count >= datalength) {
                                break;
                            }
                        } while (dataarray[count] == ']');
                        if (count < datalength && dataarray[count] == '>') {
                            String msg2 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CDEndInContent", null);
                            reportDOMError(errorHandler, error, locator, msg2, (short) 2, "wf-invalid-character");
                        }
                    }
                }
            }
            return;
        }
        int i4 = 0;
        while (i4 < datalength) {
            int i5 = i4;
            i4++;
            char c3 = dataarray[i5];
            if (XMLChar.isInvalid(c3)) {
                if (XMLChar.isHighSurrogate(c3) && i4 < datalength) {
                    i4++;
                    char c23 = dataarray[i4];
                    if (!XMLChar.isLowSurrogate(c23) || !XMLChar.isSupplemental(XMLChar.supplemental(c3, c23))) {
                    }
                }
                String msg3 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInCDSect", new Object[]{Integer.toString(c3, 16)});
                reportDOMError(errorHandler, error, locator, msg3, (short) 2, "wf-invalid-character");
            } else if (c3 == ']') {
                int count2 = i4;
                if (count2 < datalength && dataarray[count2] == ']') {
                    do {
                        count2++;
                        if (count2 >= datalength) {
                            break;
                        }
                    } while (dataarray[count2] == ']');
                    if (count2 < datalength && dataarray[count2] == '>') {
                        String msg4 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "CDEndInContent", null);
                        reportDOMError(errorHandler, error, locator, msg4, (short) 2, "wf-invalid-character");
                    }
                }
            }
        }
    }

    public static final void isXMLCharWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, String datavalue, boolean isXML11Version) throws MissingResourceException {
        if (datavalue == null || datavalue.length() == 0) {
            return;
        }
        char[] dataarray = datavalue.toCharArray();
        int datalength = dataarray.length;
        if (isXML11Version) {
            int i2 = 0;
            while (i2 < datalength) {
                int i3 = i2;
                i2++;
                if (XML11Char.isXML11Invalid(dataarray[i3])) {
                    char ch = dataarray[i2 - 1];
                    if (XMLChar.isHighSurrogate(ch) && i2 < datalength) {
                        i2++;
                        char ch2 = dataarray[i2];
                        if (!XMLChar.isLowSurrogate(ch2) || !XMLChar.isSupplemental(XMLChar.supplemental(ch, ch2))) {
                        }
                    }
                    String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "InvalidXMLCharInDOM", new Object[]{Integer.toString(dataarray[i2 - 1], 16)});
                    reportDOMError(errorHandler, error, locator, msg, (short) 2, "wf-invalid-character");
                }
            }
            return;
        }
        int i4 = 0;
        while (i4 < datalength) {
            int i5 = i4;
            i4++;
            if (XMLChar.isInvalid(dataarray[i5])) {
                char ch3 = dataarray[i4 - 1];
                if (XMLChar.isHighSurrogate(ch3) && i4 < datalength) {
                    i4++;
                    char ch22 = dataarray[i4];
                    if (!XMLChar.isLowSurrogate(ch22) || !XMLChar.isSupplemental(XMLChar.supplemental(ch3, ch22))) {
                    }
                }
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "InvalidXMLCharInDOM", new Object[]{Integer.toString(dataarray[i4 - 1], 16)});
                reportDOMError(errorHandler, error, locator, msg2, (short) 2, "wf-invalid-character");
            }
        }
    }

    public static final void isCommentWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, String datavalue, boolean isXML11Version) throws MissingResourceException {
        if (datavalue == null || datavalue.length() == 0) {
            return;
        }
        char[] dataarray = datavalue.toCharArray();
        int datalength = dataarray.length;
        if (isXML11Version) {
            int i2 = 0;
            while (i2 < datalength) {
                int i3 = i2;
                i2++;
                char c2 = dataarray[i3];
                if (XML11Char.isXML11Invalid(c2)) {
                    if (XMLChar.isHighSurrogate(c2) && i2 < datalength) {
                        i2++;
                        char c22 = dataarray[i2];
                        if (!XMLChar.isLowSurrogate(c22) || !XMLChar.isSupplemental(XMLChar.supplemental(c2, c22))) {
                        }
                    }
                    String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInComment", new Object[]{Integer.toString(dataarray[i2 - 1], 16)});
                    reportDOMError(errorHandler, error, locator, msg, (short) 2, "wf-invalid-character");
                } else if (c2 == '-' && i2 < datalength && dataarray[i2] == '-') {
                    String msg2 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "DashDashInComment", null);
                    reportDOMError(errorHandler, error, locator, msg2, (short) 2, "wf-invalid-character");
                }
            }
            return;
        }
        int i4 = 0;
        while (i4 < datalength) {
            int i5 = i4;
            i4++;
            char c3 = dataarray[i5];
            if (XMLChar.isInvalid(c3)) {
                if (XMLChar.isHighSurrogate(c3) && i4 < datalength) {
                    i4++;
                    char c23 = dataarray[i4];
                    if (!XMLChar.isLowSurrogate(c23) || !XMLChar.isSupplemental(XMLChar.supplemental(c3, c23))) {
                    }
                }
                String msg3 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidCharInComment", new Object[]{Integer.toString(dataarray[i4 - 1], 16)});
                reportDOMError(errorHandler, error, locator, msg3, (short) 2, "wf-invalid-character");
            } else if (c3 == '-' && i4 < datalength && dataarray[i4] == '-') {
                String msg4 = DOMMessageFormatter.formatMessage("http://www.w3.org/TR/1998/REC-xml-19980210", "DashDashInComment", null);
                reportDOMError(errorHandler, error, locator, msg4, (short) 2, "wf-invalid-character");
            }
        }
    }

    public static final void isAttrValueWF(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, NamedNodeMap attributes, Attr a2, String value, boolean xml11Version) throws MissingResourceException {
        DocumentType docType;
        if ((a2 instanceof AttrImpl) && ((AttrImpl) a2).hasStringValue()) {
            isXMLCharWF(errorHandler, error, locator, value, xml11Version);
            return;
        }
        NodeList children = a2.getChildNodes();
        for (int j2 = 0; j2 < children.getLength(); j2++) {
            Node child = children.item(j2);
            if (child.getNodeType() == 5) {
                Document owner = a2.getOwnerDocument();
                Entity ent = null;
                if (owner != null && (docType = owner.getDoctype()) != null) {
                    NamedNodeMap entities = docType.getEntities();
                    ent = (Entity) entities.getNamedItemNS("*", child.getNodeName());
                }
                if (ent == null) {
                    String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "UndeclaredEntRefInAttrValue", new Object[]{a2.getNodeName()});
                    reportDOMError(errorHandler, error, locator, msg, (short) 2, "UndeclaredEntRefInAttrValue");
                }
            } else {
                isXMLCharWF(errorHandler, error, locator, child.getNodeValue(), xml11Version);
            }
        }
    }

    public static final void reportDOMError(DOMErrorHandler errorHandler, DOMErrorImpl error, DOMLocatorImpl locator, String message, short severity, String type) {
        if (errorHandler != null) {
            error.reset();
            error.fMessage = message;
            error.fSeverity = severity;
            error.fLocator = locator;
            error.fType = type;
            error.fRelatedData = locator.fRelatedNode;
            if (!errorHandler.handleError(error)) {
                throw new AbortException();
            }
        }
        if (severity == 3) {
            throw new AbortException();
        }
    }

    protected final void updateQName(Node node, QName qname) {
        String prefix = node.getPrefix();
        String namespace = node.getNamespaceURI();
        String localName = node.getLocalName();
        qname.prefix = (prefix == null || prefix.length() == 0) ? null : this.fSymbolTable.addSymbol(prefix);
        qname.localpart = localName != null ? this.fSymbolTable.addSymbol(localName) : null;
        qname.rawname = this.fSymbolTable.addSymbol(node.getNodeName());
        qname.uri = namespace != null ? this.fSymbolTable.addSymbol(namespace) : null;
    }

    final String normalizeAttributeValue(String value, Attr attr) throws DOMException {
        if (!attr.getSpecified()) {
            return value;
        }
        int end = value.length();
        if (this.fNormalizedValue.ch.length < end) {
            this.fNormalizedValue.ch = new char[end];
        }
        this.fNormalizedValue.length = 0;
        boolean normalized = false;
        int i2 = 0;
        while (i2 < end) {
            char c2 = value.charAt(i2);
            if (c2 == '\t' || c2 == '\n') {
                char[] cArr = this.fNormalizedValue.ch;
                XMLString xMLString = this.fNormalizedValue;
                int i3 = xMLString.length;
                xMLString.length = i3 + 1;
                cArr[i3] = ' ';
                normalized = true;
            } else if (c2 == '\r') {
                normalized = true;
                char[] cArr2 = this.fNormalizedValue.ch;
                XMLString xMLString2 = this.fNormalizedValue;
                int i4 = xMLString2.length;
                xMLString2.length = i4 + 1;
                cArr2[i4] = ' ';
                int next = i2 + 1;
                if (next < end && value.charAt(next) == '\n') {
                    i2 = next;
                }
            } else {
                char[] cArr3 = this.fNormalizedValue.ch;
                XMLString xMLString3 = this.fNormalizedValue;
                int i5 = xMLString3.length;
                xMLString3.length = i5 + 1;
                cArr3[i5] = c2;
            }
            i2++;
        }
        if (normalized) {
            value = this.fNormalizedValue.toString();
            attr.setValue(value);
        }
        return value;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMNormalizer$XMLAttributesProxy.class */
    protected final class XMLAttributesProxy implements XMLAttributes {
        protected AttributeMap fAttributes;
        protected CoreDocumentImpl fDocument;
        protected ElementImpl fElement;
        protected final Vector fAugmentations = new Vector(5);

        protected XMLAttributesProxy() {
        }

        public void setAttributes(AttributeMap attributes, CoreDocumentImpl doc, ElementImpl elem) {
            this.fDocument = doc;
            this.fAttributes = attributes;
            this.fElement = elem;
            if (attributes != null) {
                int length = attributes.getLength();
                this.fAugmentations.setSize(length);
                for (int i2 = 0; i2 < length; i2++) {
                    this.fAugmentations.setElementAt(new AugmentationsImpl(), i2);
                }
                return;
            }
            this.fAugmentations.setSize(0);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public int addAttribute(QName qname, String attrType, String attrValue) throws DOMException {
            int index = this.fElement.getXercesAttribute(qname.uri, qname.localpart);
            if (index < 0) {
                AttrImpl attr = (AttrImpl) ((CoreDocumentImpl) this.fElement.getOwnerDocument()).createAttributeNS(qname.uri, qname.rawname, qname.localpart);
                attr.setNodeValue(attrValue);
                index = this.fElement.setXercesAttributeNode(attr);
                this.fAugmentations.insertElementAt(new AugmentationsImpl(), index);
                attr.setSpecified(false);
            }
            return index;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void removeAllAttributes() {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void removeAttributeAt(int attrIndex) {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public int getLength() {
            if (this.fAttributes != null) {
                return this.fAttributes.getLength();
            }
            return 0;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public int getIndex(String qName) {
            return -1;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public int getIndex(String uri, String localPart) {
            return -1;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void setName(int attrIndex, QName attrName) {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void getName(int attrIndex, QName attrName) {
            if (this.fAttributes != null) {
                DOMNormalizer.this.updateQName((Node) this.fAttributes.getItem(attrIndex), attrName);
            }
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getPrefix(int index) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getURI(int index) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getLocalName(int index) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getQName(int index) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public QName getQualifiedName(int index) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void setType(int attrIndex, String attrType) {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getType(int index) {
            return "CDATA";
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getType(String qName) {
            return "CDATA";
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getType(String uri, String localName) {
            return "CDATA";
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void setValue(int attrIndex, String attrValue) {
            if (this.fAttributes != null) {
                AttrImpl attr = (AttrImpl) this.fAttributes.getItem(attrIndex);
                boolean specified = attr.getSpecified();
                attr.setValue(attrValue);
                attr.setSpecified(specified);
            }
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void setValue(int attrIndex, String attrValue, XMLString value) {
            setValue(attrIndex, value.toString());
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getValue(int index) {
            return this.fAttributes != null ? this.fAttributes.item(index).getNodeValue() : "";
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getValue(String qName) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getValue(String uri, String localName) {
            Node node;
            if (this.fAttributes == null || (node = this.fAttributes.getNamedItemNS(uri, localName)) == null) {
                return null;
            }
            return node.getNodeValue();
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void setNonNormalizedValue(int attrIndex, String attrValue) {
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public String getNonNormalizedValue(int attrIndex) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void setSpecified(int attrIndex, boolean specified) {
            AttrImpl attr = (AttrImpl) this.fAttributes.getItem(attrIndex);
            attr.setSpecified(specified);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public boolean isSpecified(int attrIndex) {
            return ((Attr) this.fAttributes.getItem(attrIndex)).getSpecified();
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public Augmentations getAugmentations(int attributeIndex) {
            return (Augmentations) this.fAugmentations.elementAt(attributeIndex);
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public Augmentations getAugmentations(String uri, String localPart) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public Augmentations getAugmentations(String qName) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xni.XMLAttributes
        public void setAugmentations(int attrIndex, Augmentations augs) {
            this.fAugmentations.setElementAt(augs, attrIndex);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws DOMException, MissingResourceException, XNIException {
        Element currentElement = (Element) this.fCurrentNode;
        int attrCount = attributes.getLength();
        for (int i2 = 0; i2 < attrCount; i2++) {
            attributes.getName(i2, this.fAttrQName);
            Attr attr = currentElement.getAttributeNodeNS(this.fAttrQName.uri, this.fAttrQName.localpart);
            AttributePSVI attrPSVI = (AttributePSVI) attributes.getAugmentations(i2).getItem(Constants.ATTRIBUTE_PSVI);
            if (attrPSVI != null) {
                XSTypeDefinition decl = attrPSVI.getMemberTypeDefinition();
                boolean id = false;
                if (decl != null) {
                    id = ((XSSimpleType) decl).isIDType();
                } else {
                    XSTypeDefinition decl2 = attrPSVI.getTypeDefinition();
                    if (decl2 != null) {
                        id = ((XSSimpleType) decl2).isIDType();
                    }
                }
                if (id) {
                    ((ElementImpl) currentElement).setIdAttributeNode(attr, true);
                }
                if (this.fPSVI) {
                    ((PSVIAttrNSImpl) attr).setPSVI(attrPSVI);
                }
                if ((this.fConfiguration.features & 2) != 0) {
                    boolean specified = attr.getSpecified();
                    attr.setValue(attrPSVI.getSchemaNormalizedValue());
                    if (!specified) {
                        ((AttrImpl) attr).setSpecified(specified);
                    }
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws DOMException, MissingResourceException, XNIException {
        startElement(element, attributes, augs);
        endElement(element, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        this.allWhitespace = true;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        ElementPSVI elementPSVI;
        if (augs != null && (elementPSVI = (ElementPSVI) augs.getItem(Constants.ELEMENT_PSVI)) != null) {
            ElementImpl elementNode = (ElementImpl) this.fCurrentNode;
            if (this.fPSVI) {
                ((PSVIElementNSImpl) this.fCurrentNode).setPSVI(elementPSVI);
            }
            String normalizedValue = elementPSVI.getSchemaNormalizedValue();
            if ((this.fConfiguration.features & 2) != 0) {
                if (normalizedValue != null) {
                    elementNode.setTextContent(normalizedValue);
                }
            } else {
                String text = elementNode.getTextContent();
                if (text.length() == 0 && normalizedValue != null) {
                    elementNode.setTextContent(normalizedValue);
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void setDocumentSource(XMLDocumentSource source) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public XMLDocumentSource getDocumentSource() {
        return null;
    }
}
