package com.sun.org.apache.xerces.internal.parsers;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.util.EntityResolver2Wrapper;
import com.sun.org.apache.xerces.internal.util.EntityResolverWrapper;
import com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper;
import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
import com.sun.org.apache.xerces.internal.xs.PSVIProvider;
import java.io.IOException;
import java.util.Locale;
import org.xml.sax.AttributeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser.class */
public abstract class AbstractSAXParser extends AbstractXMLDocumentParser implements PSVIProvider, Parser, XMLReader {
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    protected static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
    protected static final String LEXICAL_HANDLER = "http://xml.org/sax/properties/lexical-handler";
    protected static final String DECLARATION_HANDLER = "http://xml.org/sax/properties/declaration-handler";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    protected boolean fNamespaces;
    protected boolean fNamespacePrefixes;
    protected boolean fLexicalHandlerParameterEntities;
    protected boolean fStandalone;
    protected boolean fResolveDTDURIs;
    protected boolean fUseEntityResolver2;
    protected boolean fXMLNSURIs;
    protected ContentHandler fContentHandler;
    protected DocumentHandler fDocumentHandler;
    protected NamespaceContext fNamespaceContext;
    protected DTDHandler fDTDHandler;
    protected DeclHandler fDeclHandler;
    protected LexicalHandler fLexicalHandler;
    protected QName fQName;
    protected boolean fParseInProgress;
    protected String fVersion;
    private final AttributesProxy fAttributesProxy;
    private Augmentations fAugmentations;
    private static final int BUFFER_SIZE = 20;
    private char[] fCharBuffer;
    protected SymbolHash fDeclaredAttrs;
    private static final String[] RECOGNIZED_FEATURES = {"http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/namespace-prefixes", "http://xml.org/sax/features/string-interning"};
    protected static final String DOM_NODE = "http://xml.org/sax/properties/dom-node";
    private static final String[] RECOGNIZED_PROPERTIES = {"http://xml.org/sax/properties/lexical-handler", "http://xml.org/sax/properties/declaration-handler", DOM_NODE};

    protected AbstractSAXParser(XMLParserConfiguration config) {
        super(config);
        this.fNamespacePrefixes = false;
        this.fLexicalHandlerParameterEntities = true;
        this.fResolveDTDURIs = true;
        this.fUseEntityResolver2 = true;
        this.fXMLNSURIs = false;
        this.fQName = new QName();
        this.fParseInProgress = false;
        this.fAttributesProxy = new AttributesProxy();
        this.fAugmentations = null;
        this.fCharBuffer = new char[20];
        this.fDeclaredAttrs = null;
        config.addRecognizedFeatures(RECOGNIZED_FEATURES);
        config.addRecognizedProperties(RECOGNIZED_PROPERTIES);
        try {
            config.setFeature(ALLOW_UE_AND_NOTATION_EVENTS, false);
        } catch (XMLConfigurationException e2) {
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        this.fNamespaceContext = namespaceContext;
        try {
            if (this.fDocumentHandler != null) {
                if (locator != null) {
                    this.fDocumentHandler.setDocumentLocator(new LocatorProxy(locator));
                }
                this.fDocumentHandler.startDocument();
            }
            if (this.fContentHandler != null) {
                if (locator != null) {
                    this.fContentHandler.setDocumentLocator(new LocatorProxy(locator));
                }
                this.fContentHandler.startDocument();
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
        this.fVersion = version;
        this.fStandalone = "yes".equals(standalone);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
        this.fInDTD = true;
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.startDTD(rootElement, publicId, systemId);
            }
            if (this.fDeclHandler != null) {
                this.fDeclaredAttrs = new SymbolHash();
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x002b A[Catch: SAXException -> 0x003f, TryCatch #0 {SAXException -> 0x003f, blocks: (B:4:0x0005, B:6:0x0017, B:8:0x001e, B:9:0x002b, B:11:0x0032), top: B:17:0x0005 }] */
    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void startGeneralEntity(java.lang.String r5, com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier r6, java.lang.String r7, com.sun.org.apache.xerces.internal.xni.Augmentations r8) throws com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            r4 = this;
            r0 = r8
            if (r0 == 0) goto L2b
            java.lang.Boolean r0 = java.lang.Boolean.TRUE     // Catch: org.xml.sax.SAXException -> L3f
            r1 = r8
            java.lang.String r2 = "ENTITY_SKIPPED"
            java.lang.Object r1 = r1.getItem(r2)     // Catch: org.xml.sax.SAXException -> L3f
            boolean r0 = r0.equals(r1)     // Catch: org.xml.sax.SAXException -> L3f
            if (r0 == 0) goto L2b
            r0 = r4
            org.xml.sax.ContentHandler r0 = r0.fContentHandler     // Catch: org.xml.sax.SAXException -> L3f
            if (r0 == 0) goto L3c
            r0 = r4
            org.xml.sax.ContentHandler r0 = r0.fContentHandler     // Catch: org.xml.sax.SAXException -> L3f
            r1 = r5
            r0.skippedEntity(r1)     // Catch: org.xml.sax.SAXException -> L3f
            goto L3c
        L2b:
            r0 = r4
            org.xml.sax.ext.LexicalHandler r0 = r0.fLexicalHandler     // Catch: org.xml.sax.SAXException -> L3f
            if (r0 == 0) goto L3c
            r0 = r4
            org.xml.sax.ext.LexicalHandler r0 = r0.fLexicalHandler     // Catch: org.xml.sax.SAXException -> L3f
            r1 = r5
            r0.startEntity(r1)     // Catch: org.xml.sax.SAXException -> L3f
        L3c:
            goto L4b
        L3f:
            r9 = move-exception
            com.sun.org.apache.xerces.internal.xni.XNIException r0 = new com.sun.org.apache.xerces.internal.xni.XNIException
            r1 = r0
            r2 = r9
            r1.<init>(r2)
            throw r0
        L4b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.startGeneralEntity(java.lang.String, com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier, java.lang.String, com.sun.org.apache.xerces.internal.xni.Augmentations):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0015 A[Catch: SAXException -> 0x0029, TryCatch #0 {SAXException -> 0x0029, blocks: (B:4:0x0004, B:6:0x0015, B:8:0x001c), top: B:14:0x0004 }] */
    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void endGeneralEntity(java.lang.String r5, com.sun.org.apache.xerces.internal.xni.Augmentations r6) throws com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            r4 = this;
            r0 = r6
            if (r0 == 0) goto L15
            java.lang.Boolean r0 = java.lang.Boolean.TRUE     // Catch: org.xml.sax.SAXException -> L29
            r1 = r6
            java.lang.String r2 = "ENTITY_SKIPPED"
            java.lang.Object r1 = r1.getItem(r2)     // Catch: org.xml.sax.SAXException -> L29
            boolean r0 = r0.equals(r1)     // Catch: org.xml.sax.SAXException -> L29
            if (r0 != 0) goto L26
        L15:
            r0 = r4
            org.xml.sax.ext.LexicalHandler r0 = r0.fLexicalHandler     // Catch: org.xml.sax.SAXException -> L29
            if (r0 == 0) goto L26
            r0 = r4
            org.xml.sax.ext.LexicalHandler r0 = r0.fLexicalHandler     // Catch: org.xml.sax.SAXException -> L29
            r1 = r5
            r0.endEntity(r1)     // Catch: org.xml.sax.SAXException -> L29
        L26:
            goto L33
        L29:
            r7 = move-exception
            com.sun.org.apache.xerces.internal.xni.XNIException r0 = new com.sun.org.apache.xerces.internal.xni.XNIException
            r1 = r0
            r2 = r7
            r1.<init>(r2)
            throw r0
        L33:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.endGeneralEntity(java.lang.String, com.sun.org.apache.xerces.internal.xni.Augmentations):void");
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fAttributesProxy.setAttributes(attributes);
                this.fDocumentHandler.startElement(element.rawname, this.fAttributesProxy);
            }
            if (this.fContentHandler != null) {
                if (this.fNamespaces) {
                    startNamespaceMapping();
                    int len = attributes.getLength();
                    if (!this.fNamespacePrefixes) {
                        for (int i2 = len - 1; i2 >= 0; i2--) {
                            attributes.getName(i2, this.fQName);
                            if (this.fQName.prefix == XMLSymbols.PREFIX_XMLNS || this.fQName.rawname == XMLSymbols.PREFIX_XMLNS) {
                                attributes.removeAttributeAt(i2);
                            }
                        }
                    } else if (!this.fXMLNSURIs) {
                        for (int i3 = len - 1; i3 >= 0; i3--) {
                            attributes.getName(i3, this.fQName);
                            if (this.fQName.prefix == XMLSymbols.PREFIX_XMLNS || this.fQName.rawname == XMLSymbols.PREFIX_XMLNS) {
                                this.fQName.prefix = "";
                                this.fQName.uri = "";
                                this.fQName.localpart = "";
                                attributes.setName(i3, this.fQName);
                            }
                        }
                    }
                }
                this.fAugmentations = augs;
                String uri = element.uri != null ? element.uri : "";
                String localpart = this.fNamespaces ? element.localpart : "";
                this.fAttributesProxy.setAttributes(attributes);
                this.fContentHandler.startElement(uri, localpart, element.rawname, this.fAttributesProxy);
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        if (text.length == 0) {
            return;
        }
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.characters(text.ch, text.offset, text.length);
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.characters(text.ch, text.offset, text.length);
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.ignorableWhitespace(text.ch, text.offset, text.length);
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.ignorableWhitespace(text.ch, text.offset, text.length);
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.endElement(element.rawname);
            }
            if (this.fContentHandler != null) {
                this.fAugmentations = augs;
                String uri = element.uri != null ? element.uri : "";
                String localpart = this.fNamespaces ? element.localpart : "";
                this.fContentHandler.endElement(uri, localpart, element.rawname);
                if (this.fNamespaces) {
                    endNamespaceMapping();
                }
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.startCDATA();
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.endCDATA();
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.comment(text.ch, 0, text.length);
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.processingInstruction(target, data.toString());
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.processingInstruction(target, data.toString());
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws XNIException {
        try {
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.endDocument();
            }
            if (this.fContentHandler != null) {
                this.fContentHandler.endDocument();
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        startParameterEntity("[dtd]", null, null, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endExternalSubset(Augmentations augs) throws XNIException {
        endParameterEntity("[dtd]", augs);
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x002b A[Catch: SAXException -> 0x0046, TryCatch #0 {SAXException -> 0x0046, blocks: (B:4:0x0005, B:6:0x0017, B:8:0x001e, B:9:0x002b, B:11:0x0032, B:13:0x0039), top: B:19:0x0005 }] */
    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void startParameterEntity(java.lang.String r5, com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier r6, java.lang.String r7, com.sun.org.apache.xerces.internal.xni.Augmentations r8) throws com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            r4 = this;
            r0 = r8
            if (r0 == 0) goto L2b
            java.lang.Boolean r0 = java.lang.Boolean.TRUE     // Catch: org.xml.sax.SAXException -> L46
            r1 = r8
            java.lang.String r2 = "ENTITY_SKIPPED"
            java.lang.Object r1 = r1.getItem(r2)     // Catch: org.xml.sax.SAXException -> L46
            boolean r0 = r0.equals(r1)     // Catch: org.xml.sax.SAXException -> L46
            if (r0 == 0) goto L2b
            r0 = r4
            org.xml.sax.ContentHandler r0 = r0.fContentHandler     // Catch: org.xml.sax.SAXException -> L46
            if (r0 == 0) goto L43
            r0 = r4
            org.xml.sax.ContentHandler r0 = r0.fContentHandler     // Catch: org.xml.sax.SAXException -> L46
            r1 = r5
            r0.skippedEntity(r1)     // Catch: org.xml.sax.SAXException -> L46
            goto L43
        L2b:
            r0 = r4
            org.xml.sax.ext.LexicalHandler r0 = r0.fLexicalHandler     // Catch: org.xml.sax.SAXException -> L46
            if (r0 == 0) goto L43
            r0 = r4
            boolean r0 = r0.fLexicalHandlerParameterEntities     // Catch: org.xml.sax.SAXException -> L46
            if (r0 == 0) goto L43
            r0 = r4
            org.xml.sax.ext.LexicalHandler r0 = r0.fLexicalHandler     // Catch: org.xml.sax.SAXException -> L46
            r1 = r5
            r0.startEntity(r1)     // Catch: org.xml.sax.SAXException -> L46
        L43:
            goto L52
        L46:
            r9 = move-exception
            com.sun.org.apache.xerces.internal.xni.XNIException r0 = new com.sun.org.apache.xerces.internal.xni.XNIException
            r1 = r0
            r2 = r9
            r1.<init>(r2)
            throw r0
        L52:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.startParameterEntity(java.lang.String, com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier, java.lang.String, com.sun.org.apache.xerces.internal.xni.Augmentations):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0015 A[Catch: SAXException -> 0x0030, TryCatch #0 {SAXException -> 0x0030, blocks: (B:4:0x0004, B:6:0x0015, B:8:0x001c, B:10:0x0023), top: B:16:0x0004 }] */
    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void endParameterEntity(java.lang.String r5, com.sun.org.apache.xerces.internal.xni.Augmentations r6) throws com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            r4 = this;
            r0 = r6
            if (r0 == 0) goto L15
            java.lang.Boolean r0 = java.lang.Boolean.TRUE     // Catch: org.xml.sax.SAXException -> L30
            r1 = r6
            java.lang.String r2 = "ENTITY_SKIPPED"
            java.lang.Object r1 = r1.getItem(r2)     // Catch: org.xml.sax.SAXException -> L30
            boolean r0 = r0.equals(r1)     // Catch: org.xml.sax.SAXException -> L30
            if (r0 != 0) goto L2d
        L15:
            r0 = r4
            org.xml.sax.ext.LexicalHandler r0 = r0.fLexicalHandler     // Catch: org.xml.sax.SAXException -> L30
            if (r0 == 0) goto L2d
            r0 = r4
            boolean r0 = r0.fLexicalHandlerParameterEntities     // Catch: org.xml.sax.SAXException -> L30
            if (r0 == 0) goto L2d
            r0 = r4
            org.xml.sax.ext.LexicalHandler r0 = r0.fLexicalHandler     // Catch: org.xml.sax.SAXException -> L30
            r1 = r5
            r0.endEntity(r1)     // Catch: org.xml.sax.SAXException -> L30
        L2d:
            goto L3a
        L30:
            r7 = move-exception
            com.sun.org.apache.xerces.internal.xni.XNIException r0 = new com.sun.org.apache.xerces.internal.xni.XNIException
            r1 = r0
            r2 = r7
            r1.<init>(r2)
            throw r0
        L3a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.endParameterEntity(java.lang.String, com.sun.org.apache.xerces.internal.xni.Augmentations):void");
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void elementDecl(String name, String contentModel, Augmentations augs) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                this.fDeclHandler.elementDecl(name, contentModel);
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                String elemAttr = new StringBuffer(elementName).append("<").append(attributeName).toString();
                if (this.fDeclaredAttrs.get(elemAttr) != null) {
                    return;
                }
                this.fDeclaredAttrs.put(elemAttr, Boolean.TRUE);
                if (type.equals(SchemaSymbols.ATTVAL_NOTATION) || type.equals("ENUMERATION")) {
                    StringBuffer str = new StringBuffer();
                    if (type.equals(SchemaSymbols.ATTVAL_NOTATION)) {
                        str.append(type);
                        str.append(" (");
                    } else {
                        str.append("(");
                    }
                    for (int i2 = 0; i2 < enumeration.length; i2++) {
                        str.append(enumeration[i2]);
                        if (i2 < enumeration.length - 1) {
                            str.append('|');
                        }
                    }
                    str.append(')');
                    type = str.toString();
                }
                String value = defaultValue == null ? null : defaultValue.toString();
                this.fDeclHandler.attributeDecl(elementName, attributeName, type, defaultType, value);
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                this.fDeclHandler.internalEntityDecl(name, text.toString());
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        try {
            if (this.fDeclHandler != null) {
                String publicId = identifier.getPublicId();
                String systemId = this.fResolveDTDURIs ? identifier.getExpandedSystemId() : identifier.getLiteralSystemId();
                this.fDeclHandler.externalEntityDecl(name, publicId, systemId);
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augs) throws XNIException {
        try {
            if (this.fDTDHandler != null) {
                String publicId = identifier.getPublicId();
                String systemId = this.fResolveDTDURIs ? identifier.getExpandedSystemId() : identifier.getLiteralSystemId();
                this.fDTDHandler.unparsedEntityDecl(name, publicId, systemId, notation);
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        try {
            if (this.fDTDHandler != null) {
                String publicId = identifier.getPublicId();
                String systemId = this.fResolveDTDURIs ? identifier.getExpandedSystemId() : identifier.getLiteralSystemId();
                this.fDTDHandler.notationDecl(name, publicId, systemId);
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endDTD(Augmentations augs) throws XNIException {
        this.fInDTD = false;
        try {
            if (this.fLexicalHandler != null) {
                this.fLexicalHandler.endDTD();
            }
            if (this.fDeclaredAttrs != null) {
                this.fDeclaredAttrs.clear();
            }
        } catch (SAXException e2) {
            throw new XNIException(e2);
        }
    }

    public void parse(String systemId) throws SAXException, IOException {
        XMLInputSource source = new XMLInputSource(null, systemId, null);
        try {
            parse(source);
        } catch (XMLParseException e2) {
            Exception ex = e2.getException();
            if (ex == null) {
                LocatorImpl locatorImpl = new LocatorImpl() { // from class: com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.1
                    public String getXMLVersion() {
                        return AbstractSAXParser.this.fVersion;
                    }

                    public String getEncoding() {
                        return null;
                    }
                };
                locatorImpl.setPublicId(e2.getPublicId());
                locatorImpl.setSystemId(e2.getExpandedSystemId());
                locatorImpl.setLineNumber(e2.getLineNumber());
                locatorImpl.setColumnNumber(e2.getColumnNumber());
                throw new SAXParseException(e2.getMessage(), locatorImpl);
            }
            if (ex instanceof SAXException) {
                throw ((SAXException) ex);
            }
            if (ex instanceof IOException) {
                throw ((IOException) ex);
            }
            throw new SAXException(ex);
        } catch (XNIException e3) {
            Exception ex2 = e3.getException();
            if (ex2 == null) {
                throw new SAXException(e3.getMessage());
            }
            if (ex2 instanceof SAXException) {
                throw ((SAXException) ex2);
            }
            if (ex2 instanceof IOException) {
                throw ((IOException) ex2);
            }
            throw new SAXException(ex2);
        }
    }

    public void parse(InputSource inputSource) throws SAXException, IOException {
        try {
            XMLInputSource xmlInputSource = new XMLInputSource(inputSource.getPublicId(), inputSource.getSystemId(), null);
            xmlInputSource.setByteStream(inputSource.getByteStream());
            xmlInputSource.setCharacterStream(inputSource.getCharacterStream());
            xmlInputSource.setEncoding(inputSource.getEncoding());
            parse(xmlInputSource);
        } catch (XMLParseException e2) {
            Exception ex = e2.getException();
            if (ex == null) {
                LocatorImpl locatorImpl = new LocatorImpl() { // from class: com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.2
                    public String getXMLVersion() {
                        return AbstractSAXParser.this.fVersion;
                    }

                    public String getEncoding() {
                        return null;
                    }
                };
                locatorImpl.setPublicId(e2.getPublicId());
                locatorImpl.setSystemId(e2.getExpandedSystemId());
                locatorImpl.setLineNumber(e2.getLineNumber());
                locatorImpl.setColumnNumber(e2.getColumnNumber());
                throw new SAXParseException(e2.getMessage(), locatorImpl);
            }
            if (ex instanceof SAXException) {
                throw ((SAXException) ex);
            }
            if (ex instanceof IOException) {
                throw ((IOException) ex);
            }
            throw new SAXException(ex);
        } catch (XNIException e3) {
            Exception ex2 = e3.getException();
            if (ex2 == null) {
                throw new SAXException(e3.getMessage());
            }
            if (ex2 instanceof SAXException) {
                throw ((SAXException) ex2);
            }
            if (ex2 instanceof IOException) {
                throw ((IOException) ex2);
            }
            throw new SAXException(ex2);
        }
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver resolver) {
        try {
            XMLEntityResolver xer = (XMLEntityResolver) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
            if (this.fUseEntityResolver2 && (resolver instanceof EntityResolver2)) {
                if (xer instanceof EntityResolver2Wrapper) {
                    EntityResolver2Wrapper er2w = (EntityResolver2Wrapper) xer;
                    er2w.setEntityResolver((EntityResolver2) resolver);
                } else {
                    this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolver2Wrapper((EntityResolver2) resolver));
                }
            } else if (xer instanceof EntityResolverWrapper) {
                EntityResolverWrapper erw = (EntityResolverWrapper) xer;
                erw.setEntityResolver(resolver);
            } else {
                this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new EntityResolverWrapper(resolver));
            }
        } catch (XMLConfigurationException e2) {
        }
    }

    @Override // org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        EntityResolver entityResolver = null;
        try {
            XMLEntityResolver xmlEntityResolver = (XMLEntityResolver) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
            if (xmlEntityResolver != null) {
                if (xmlEntityResolver instanceof EntityResolverWrapper) {
                    entityResolver = ((EntityResolverWrapper) xmlEntityResolver).getEntityResolver();
                } else if (xmlEntityResolver instanceof EntityResolver2Wrapper) {
                    entityResolver = ((EntityResolver2Wrapper) xmlEntityResolver).getEntityResolver();
                }
            }
        } catch (XMLConfigurationException e2) {
        }
        return entityResolver;
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler errorHandler) {
        try {
            XMLErrorHandler xeh = (XMLErrorHandler) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
            if (xeh instanceof ErrorHandlerWrapper) {
                ErrorHandlerWrapper ehw = (ErrorHandlerWrapper) xeh;
                ehw.setErrorHandler(errorHandler);
            } else {
                this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/error-handler", new ErrorHandlerWrapper(errorHandler));
            }
        } catch (XMLConfigurationException e2) {
        }
    }

    @Override // org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        ErrorHandler errorHandler = null;
        try {
            XMLErrorHandler xmlErrorHandler = (XMLErrorHandler) this.fConfiguration.getProperty("http://apache.org/xml/properties/internal/error-handler");
            if (xmlErrorHandler != null && (xmlErrorHandler instanceof ErrorHandlerWrapper)) {
                errorHandler = ((ErrorHandlerWrapper) xmlErrorHandler).getErrorHandler();
            }
        } catch (XMLConfigurationException e2) {
        }
        return errorHandler;
    }

    @Override // org.xml.sax.Parser
    public void setLocale(Locale locale) throws SAXException, XNIException {
        this.fConfiguration.setLocale(locale);
    }

    @Override // org.xml.sax.Parser, org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler dtdHandler) {
        this.fDTDHandler = dtdHandler;
    }

    @Override // org.xml.sax.Parser
    public void setDocumentHandler(DocumentHandler documentHandler) {
        this.fDocumentHandler = documentHandler;
    }

    @Override // org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler contentHandler) {
        this.fContentHandler = contentHandler;
    }

    @Override // org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        return this.fContentHandler;
    }

    @Override // org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        return this.fDTDHandler;
    }

    public void setFeature(String featureId, boolean state) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (featureId.startsWith(Constants.SAX_FEATURE_PREFIX)) {
                int suffixLength = featureId.length() - Constants.SAX_FEATURE_PREFIX.length();
                if (suffixLength == "namespaces".length() && featureId.endsWith("namespaces")) {
                    this.fConfiguration.setFeature(featureId, state);
                    this.fNamespaces = state;
                    return;
                }
                if (suffixLength == Constants.NAMESPACE_PREFIXES_FEATURE.length() && featureId.endsWith(Constants.NAMESPACE_PREFIXES_FEATURE)) {
                    this.fConfiguration.setFeature(featureId, state);
                    this.fNamespacePrefixes = state;
                    return;
                }
                if (suffixLength == Constants.STRING_INTERNING_FEATURE.length() && featureId.endsWith(Constants.STRING_INTERNING_FEATURE)) {
                    if (!state) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "false-not-supported", new Object[]{featureId}));
                    }
                    return;
                }
                if (suffixLength == Constants.LEXICAL_HANDLER_PARAMETER_ENTITIES_FEATURE.length() && featureId.endsWith(Constants.LEXICAL_HANDLER_PARAMETER_ENTITIES_FEATURE)) {
                    this.fLexicalHandlerParameterEntities = state;
                    return;
                }
                if (suffixLength == Constants.RESOLVE_DTD_URIS_FEATURE.length() && featureId.endsWith(Constants.RESOLVE_DTD_URIS_FEATURE)) {
                    this.fResolveDTDURIs = state;
                    return;
                }
                if (suffixLength == Constants.UNICODE_NORMALIZATION_CHECKING_FEATURE.length() && featureId.endsWith(Constants.UNICODE_NORMALIZATION_CHECKING_FEATURE)) {
                    if (state) {
                        throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "true-not-supported", new Object[]{featureId}));
                    }
                    return;
                }
                if (suffixLength == Constants.XMLNS_URIS_FEATURE.length() && featureId.endsWith(Constants.XMLNS_URIS_FEATURE)) {
                    this.fXMLNSURIs = state;
                    return;
                }
                if (suffixLength == Constants.USE_ENTITY_RESOLVER2_FEATURE.length() && featureId.endsWith(Constants.USE_ENTITY_RESOLVER2_FEATURE)) {
                    if (state != this.fUseEntityResolver2) {
                        this.fUseEntityResolver2 = state;
                        setEntityResolver(getEntityResolver());
                        return;
                    }
                    return;
                }
                if ((suffixLength == Constants.IS_STANDALONE_FEATURE.length() && featureId.endsWith(Constants.IS_STANDALONE_FEATURE)) || ((suffixLength == Constants.USE_ATTRIBUTES2_FEATURE.length() && featureId.endsWith(Constants.USE_ATTRIBUTES2_FEATURE)) || ((suffixLength == Constants.USE_LOCATOR2_FEATURE.length() && featureId.endsWith(Constants.USE_LOCATOR2_FEATURE)) || (suffixLength == Constants.XML_11_FEATURE.length() && featureId.endsWith(Constants.XML_11_FEATURE))))) {
                    throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-read-only", new Object[]{featureId}));
                }
            } else if (featureId.equals("http://javax.xml.XMLConstants/feature/secure-processing") && state && this.fConfiguration.getProperty("http://apache.org/xml/properties/security-manager") == null) {
                this.fConfiguration.setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager());
            }
            this.fConfiguration.setFeature(featureId, state);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            if (e2.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[]{identifier}));
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.XMLParser, org.xml.sax.XMLReader
    public boolean getFeature(String featureId) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (featureId.startsWith(Constants.SAX_FEATURE_PREFIX)) {
                int suffixLength = featureId.length() - Constants.SAX_FEATURE_PREFIX.length();
                if (suffixLength == Constants.NAMESPACE_PREFIXES_FEATURE.length() && featureId.endsWith(Constants.NAMESPACE_PREFIXES_FEATURE)) {
                    boolean state = this.fConfiguration.getFeature(featureId);
                    return state;
                }
                if (suffixLength == Constants.STRING_INTERNING_FEATURE.length() && featureId.endsWith(Constants.STRING_INTERNING_FEATURE)) {
                    return true;
                }
                if (suffixLength == Constants.IS_STANDALONE_FEATURE.length() && featureId.endsWith(Constants.IS_STANDALONE_FEATURE)) {
                    return this.fStandalone;
                }
                if (suffixLength == Constants.XML_11_FEATURE.length() && featureId.endsWith(Constants.XML_11_FEATURE)) {
                    return this.fConfiguration instanceof XML11Configurable;
                }
                if (suffixLength == Constants.LEXICAL_HANDLER_PARAMETER_ENTITIES_FEATURE.length() && featureId.endsWith(Constants.LEXICAL_HANDLER_PARAMETER_ENTITIES_FEATURE)) {
                    return this.fLexicalHandlerParameterEntities;
                }
                if (suffixLength == Constants.RESOLVE_DTD_URIS_FEATURE.length() && featureId.endsWith(Constants.RESOLVE_DTD_URIS_FEATURE)) {
                    return this.fResolveDTDURIs;
                }
                if (suffixLength == Constants.XMLNS_URIS_FEATURE.length() && featureId.endsWith(Constants.XMLNS_URIS_FEATURE)) {
                    return this.fXMLNSURIs;
                }
                if (suffixLength == Constants.UNICODE_NORMALIZATION_CHECKING_FEATURE.length() && featureId.endsWith(Constants.UNICODE_NORMALIZATION_CHECKING_FEATURE)) {
                    return false;
                }
                if (suffixLength == Constants.USE_ENTITY_RESOLVER2_FEATURE.length() && featureId.endsWith(Constants.USE_ENTITY_RESOLVER2_FEATURE)) {
                    return this.fUseEntityResolver2;
                }
                if (suffixLength != Constants.USE_ATTRIBUTES2_FEATURE.length() || !featureId.endsWith(Constants.USE_ATTRIBUTES2_FEATURE)) {
                    if (suffixLength == Constants.USE_LOCATOR2_FEATURE.length() && featureId.endsWith(Constants.USE_LOCATOR2_FEATURE)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
            return this.fConfiguration.getFeature(featureId);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            if (e2.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "feature-not-supported", new Object[]{identifier}));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x011a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setProperty(java.lang.String r10, java.lang.Object r11) throws org.xml.sax.SAXNotRecognizedException, org.xml.sax.SAXNotSupportedException {
        /*
            Method dump skipped, instructions count: 314
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.parsers.AbstractSAXParser.setProperty(java.lang.String, java.lang.Object):void");
    }

    public Object getProperty(String propertyId) throws SAXNotRecognizedException, SAXNotSupportedException {
        try {
            if (propertyId.startsWith(Constants.SAX_PROPERTY_PREFIX)) {
                int suffixLength = propertyId.length() - Constants.SAX_PROPERTY_PREFIX.length();
                if (suffixLength == Constants.DOCUMENT_XML_VERSION_PROPERTY.length() && propertyId.endsWith(Constants.DOCUMENT_XML_VERSION_PROPERTY)) {
                    return this.fVersion;
                }
                if (suffixLength == Constants.LEXICAL_HANDLER_PROPERTY.length() && propertyId.endsWith(Constants.LEXICAL_HANDLER_PROPERTY)) {
                    return getLexicalHandler();
                }
                if (suffixLength == Constants.DECLARATION_HANDLER_PROPERTY.length() && propertyId.endsWith(Constants.DECLARATION_HANDLER_PROPERTY)) {
                    return getDeclHandler();
                }
                if (suffixLength == Constants.DOM_NODE_PROPERTY.length() && propertyId.endsWith(Constants.DOM_NODE_PROPERTY)) {
                    throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "dom-node-read-not-supported", null));
                }
            }
            return this.fConfiguration.getProperty(propertyId);
        } catch (XMLConfigurationException e2) {
            String identifier = e2.getIdentifier();
            if (e2.getType() == Status.NOT_RECOGNIZED) {
                throw new SAXNotRecognizedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-recognized", new Object[]{identifier}));
            }
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-supported", new Object[]{identifier}));
        }
    }

    protected void setDeclHandler(DeclHandler handler) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (this.fParseInProgress) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[]{"http://xml.org/sax/properties/declaration-handler"}));
        }
        this.fDeclHandler = handler;
    }

    protected DeclHandler getDeclHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        return this.fDeclHandler;
    }

    protected void setLexicalHandler(LexicalHandler handler) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (this.fParseInProgress) {
            throw new SAXNotSupportedException(SAXMessageFormatter.formatMessage(this.fConfiguration.getLocale(), "property-not-parsing-supported", new Object[]{"http://xml.org/sax/properties/lexical-handler"}));
        }
        this.fLexicalHandler = handler;
    }

    protected LexicalHandler getLexicalHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
        return this.fLexicalHandler;
    }

    protected final void startNamespaceMapping() throws SAXException {
        int count = this.fNamespaceContext.getDeclaredPrefixCount();
        if (count > 0) {
            for (int i2 = 0; i2 < count; i2++) {
                String prefix = this.fNamespaceContext.getDeclaredPrefixAt(i2);
                String uri = this.fNamespaceContext.getURI(prefix);
                this.fContentHandler.startPrefixMapping(prefix, uri == null ? "" : uri);
            }
        }
    }

    protected final void endNamespaceMapping() throws SAXException {
        int count = this.fNamespaceContext.getDeclaredPrefixCount();
        if (count > 0) {
            for (int i2 = 0; i2 < count; i2++) {
                this.fContentHandler.endPrefixMapping(this.fNamespaceContext.getDeclaredPrefixAt(i2));
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.parsers.AbstractXMLDocumentParser, com.sun.org.apache.xerces.internal.parsers.XMLParser
    public void reset() throws XNIException {
        super.reset();
        this.fInDTD = false;
        this.fVersion = "1.0";
        this.fStandalone = false;
        this.fNamespaces = this.fConfiguration.getFeature("http://xml.org/sax/features/namespaces");
        this.fNamespacePrefixes = this.fConfiguration.getFeature("http://xml.org/sax/features/namespace-prefixes");
        this.fAugmentations = null;
        this.fDeclaredAttrs = null;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser$LocatorProxy.class */
    protected class LocatorProxy implements Locator2 {
        protected XMLLocator fLocator;

        public LocatorProxy(XMLLocator locator) {
            this.fLocator = locator;
        }

        @Override // org.xml.sax.Locator
        public String getPublicId() {
            return this.fLocator.getPublicId();
        }

        @Override // org.xml.sax.Locator
        public String getSystemId() {
            return this.fLocator.getExpandedSystemId();
        }

        @Override // org.xml.sax.Locator
        public int getLineNumber() {
            return this.fLocator.getLineNumber();
        }

        @Override // org.xml.sax.Locator
        public int getColumnNumber() {
            return this.fLocator.getColumnNumber();
        }

        @Override // org.xml.sax.ext.Locator2
        public String getXMLVersion() {
            return this.fLocator.getXMLVersion();
        }

        @Override // org.xml.sax.ext.Locator2
        public String getEncoding() {
            return this.fLocator.getEncoding();
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/parsers/AbstractSAXParser$AttributesProxy.class */
    protected static final class AttributesProxy implements AttributeList, Attributes2 {
        protected XMLAttributes fAttributes;

        protected AttributesProxy() {
        }

        public void setAttributes(XMLAttributes attributes) {
            this.fAttributes = attributes;
        }

        @Override // org.xml.sax.AttributeList
        public int getLength() {
            return this.fAttributes.getLength();
        }

        @Override // org.xml.sax.AttributeList
        public String getName(int i2) {
            return this.fAttributes.getQName(i2);
        }

        @Override // org.xml.sax.Attributes
        public String getQName(int index) {
            return this.fAttributes.getQName(index);
        }

        @Override // org.xml.sax.Attributes
        public String getURI(int index) {
            String uri = this.fAttributes.getURI(index);
            return uri != null ? uri : "";
        }

        @Override // org.xml.sax.Attributes
        public String getLocalName(int index) {
            return this.fAttributes.getLocalName(index);
        }

        @Override // org.xml.sax.AttributeList
        public String getType(int i2) {
            return this.fAttributes.getType(i2);
        }

        @Override // org.xml.sax.AttributeList
        public String getType(String name) {
            return this.fAttributes.getType(name);
        }

        @Override // org.xml.sax.Attributes
        public String getType(String uri, String localName) {
            return uri.equals("") ? this.fAttributes.getType(null, localName) : this.fAttributes.getType(uri, localName);
        }

        @Override // org.xml.sax.AttributeList
        public String getValue(int i2) {
            return this.fAttributes.getValue(i2);
        }

        @Override // org.xml.sax.AttributeList
        public String getValue(String name) {
            return this.fAttributes.getValue(name);
        }

        @Override // org.xml.sax.Attributes
        public String getValue(String uri, String localName) {
            return uri.equals("") ? this.fAttributes.getValue(null, localName) : this.fAttributes.getValue(uri, localName);
        }

        @Override // org.xml.sax.Attributes
        public int getIndex(String qName) {
            return this.fAttributes.getIndex(qName);
        }

        @Override // org.xml.sax.Attributes
        public int getIndex(String uri, String localPart) {
            return uri.equals("") ? this.fAttributes.getIndex(null, localPart) : this.fAttributes.getIndex(uri, localPart);
        }

        @Override // org.xml.sax.ext.Attributes2
        public boolean isDeclared(int index) {
            if (index < 0 || index >= this.fAttributes.getLength()) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return Boolean.TRUE.equals(this.fAttributes.getAugmentations(index).getItem(Constants.ATTRIBUTE_DECLARED));
        }

        @Override // org.xml.sax.ext.Attributes2
        public boolean isDeclared(String qName) {
            int index = getIndex(qName);
            if (index == -1) {
                throw new IllegalArgumentException(qName);
            }
            return Boolean.TRUE.equals(this.fAttributes.getAugmentations(index).getItem(Constants.ATTRIBUTE_DECLARED));
        }

        @Override // org.xml.sax.ext.Attributes2
        public boolean isDeclared(String uri, String localName) {
            int index = getIndex(uri, localName);
            if (index == -1) {
                throw new IllegalArgumentException(localName);
            }
            return Boolean.TRUE.equals(this.fAttributes.getAugmentations(index).getItem(Constants.ATTRIBUTE_DECLARED));
        }

        @Override // org.xml.sax.ext.Attributes2
        public boolean isSpecified(int index) {
            if (index < 0 || index >= this.fAttributes.getLength()) {
                throw new ArrayIndexOutOfBoundsException(index);
            }
            return this.fAttributes.isSpecified(index);
        }

        @Override // org.xml.sax.ext.Attributes2
        public boolean isSpecified(String qName) {
            int index = getIndex(qName);
            if (index == -1) {
                throw new IllegalArgumentException(qName);
            }
            return this.fAttributes.isSpecified(index);
        }

        @Override // org.xml.sax.ext.Attributes2
        public boolean isSpecified(String uri, String localName) {
            int index = getIndex(uri, localName);
            if (index == -1) {
                throw new IllegalArgumentException(localName);
            }
            return this.fAttributes.isSpecified(index);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public ElementPSVI getElementPSVI() {
        if (this.fAugmentations != null) {
            return (ElementPSVI) this.fAugmentations.getItem(Constants.ELEMENT_PSVI);
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVI(int index) {
        return (AttributePSVI) this.fAttributesProxy.fAttributes.getAugmentations(index).getItem(Constants.ATTRIBUTE_PSVI);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.PSVIProvider
    public AttributePSVI getAttributePSVIByName(String uri, String localname) {
        return (AttributePSVI) this.fAttributesProxy.fAttributes.getAugmentations(uri, localname).getItem(Constants.ATTRIBUTE_PSVI);
    }
}
