package com.sun.org.apache.xerces.internal.xinclude;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;
import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
import com.sun.org.apache.xerces.internal.util.HTTPInputSource;
import com.sun.org.apache.xerces.internal.util.IntStack;
import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDFilter;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
import com.sun.org.apache.xerces.internal.xpointer.XPointerHandler;
import com.sun.org.apache.xerces.internal.xpointer.XPointerProcessor;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Stack;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/XIncludeHandler.class */
public class XIncludeHandler implements XMLComponent, XMLDocumentFilter, XMLDTDFilter {
    public static final String XINCLUDE_DEFAULT_CONFIGURATION = "com.sun.org.apache.xerces.internal.parsers.XIncludeParserConfiguration";
    public static final String HTTP_ACCEPT = "Accept";
    public static final String HTTP_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String XPOINTER = "xpointer";
    public static final String CURRENT_BASE_URI = "currentBaseURI";
    private static final int STATE_NORMAL_PROCESSING = 1;
    private static final int STATE_IGNORE = 2;
    private static final int STATE_EXPECT_FALLBACK = 3;
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";
    protected static final String DYNAMIC_VALIDATION = "http://apache.org/xml/features/validation/dynamic";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDocumentSource fDocumentSource;
    protected XMLDTDHandler fDTDHandler;
    protected XMLDTDSource fDTDSource;
    protected XIncludeHandler fParentXIncludeHandler;
    protected String fParentRelativeURI;
    protected XMLParserConfiguration fChildConfig;
    protected XMLParserConfiguration fXIncludeChildConfig;
    protected XMLParserConfiguration fXPointerChildConfig;
    protected XMLLocator fDocLocation;
    protected XIncludeNamespaceSupport fNamespaceContext;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLEntityResolver fEntityResolver;
    protected XMLSecurityManager fSecurityManager;
    protected XMLSecurityPropertyManager fSecurityPropertyMgr;
    protected XIncludeTextReader fXInclude10TextReader;
    protected XIncludeTextReader fXInclude11TextReader;
    protected XMLResourceIdentifier fCurrentBaseURI;
    protected IntStack fBaseURIScope;
    protected Stack fBaseURI;
    protected Stack fLiteralSystemID;
    protected Stack fExpandedSystemID;
    protected IntStack fLanguageScope;
    protected Stack fLanguageStack;
    protected String fCurrentLanguage;
    protected ParserConfigurationSettings fSettings;
    private int fResultDepth;
    private static final int INITIAL_SIZE = 8;
    private ArrayList fNotations;
    private ArrayList fUnparsedEntities;
    private boolean fSendUEAndNotationEvents;
    private boolean fIsXML11;
    private boolean fInDTD;
    private boolean fSeenRootElement;
    public static final String XINCLUDE_NS_URI = "http://www.w3.org/2001/XInclude".intern();
    public static final String XINCLUDE_INCLUDE = "include".intern();
    public static final String XINCLUDE_FALLBACK = Constants.ELEMNAME_FALLBACK_STRING.intern();
    public static final String XINCLUDE_PARSE_XML = "xml".intern();
    public static final String XINCLUDE_PARSE_TEXT = "text".intern();
    public static final String XINCLUDE_ATTR_HREF = Constants.ATTRNAME_HREF.intern();
    public static final String XINCLUDE_ATTR_PARSE = "parse".intern();
    public static final String XINCLUDE_ATTR_ENCODING = "encoding".intern();
    public static final String XINCLUDE_ATTR_ACCEPT = SecurityConstants.SOCKET_ACCEPT_ACTION.intern();
    public static final String XINCLUDE_ATTR_ACCEPT_LANGUAGE = "accept-language".intern();
    public static final String XINCLUDE_INCLUDED = "[included]".intern();
    public static final String XINCLUDE_BASE = "base".intern();
    public static final QName XML_BASE_QNAME = new QName(XMLSymbols.PREFIX_XML, XINCLUDE_BASE, (XMLSymbols.PREFIX_XML + CallSiteDescriptor.TOKEN_DELIMITER + XINCLUDE_BASE).intern(), NamespaceContext.XML_URI);
    public static final String XINCLUDE_LANG = "lang".intern();
    public static final QName XML_LANG_QNAME = new QName(XMLSymbols.PREFIX_XML, XINCLUDE_LANG, (XMLSymbols.PREFIX_XML + CallSiteDescriptor.TOKEN_DELIMITER + XINCLUDE_LANG).intern(), NamespaceContext.XML_URI);
    public static final QName NEW_NS_ATTR_QNAME = new QName(XMLSymbols.PREFIX_XMLNS, "", XMLSymbols.PREFIX_XMLNS + CallSiteDescriptor.TOKEN_DELIMITER, NamespaceContext.XMLNS_URI);
    protected static final String ALLOW_UE_AND_NOTATION_EVENTS = "http://xml.org/sax/features/allow-dtd-events-after-endDTD";
    protected static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
    protected static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
    private static final String[] RECOGNIZED_FEATURES = {ALLOW_UE_AND_NOTATION_EVENTS, XINCLUDE_FIXUP_BASE_URIS, XINCLUDE_FIXUP_LANGUAGE};
    private static final Boolean[] FEATURE_DEFAULTS = {Boolean.TRUE, Boolean.TRUE, Boolean.TRUE};
    public static final String BUFFER_SIZE = "http://apache.org/xml/properties/input-buffer-size";
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/security-manager", BUFFER_SIZE};
    private static final Object[] PROPERTY_DEFAULTS = {null, null, null, new Integer(8192)};
    private static final boolean[] gNeedEscaping = new boolean[128];
    private static final char[] gAfterEscaping1 = new char[128];
    private static final char[] gAfterEscaping2 = new char[128];
    private static final char[] gHexChs = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    protected int fBufferSize = 8192;
    protected XPointerProcessor fXPtrProcessor = null;
    protected XIncludeMessageFormatter fXIncludeMessageFormatter = new XIncludeMessageFormatter();
    private boolean[] fSawInclude = new boolean[8];
    private boolean[] fSawFallback = new boolean[8];
    private int[] fState = new int[8];
    private boolean fFixupBaseURIs = true;
    private boolean fFixupLanguage = true;
    private boolean fNeedCopyFeatures = true;
    private int fDepth = 0;

    static {
        char[] escChs = {' ', '<', '>', '\"', '{', '}', '|', '\\', '^', '`'};
        for (char ch : escChs) {
            gNeedEscaping[ch] = true;
            gAfterEscaping1[ch] = gHexChs[ch >> 4];
            gAfterEscaping2[ch] = gHexChs[ch & 15];
        }
    }

    public XIncludeHandler() {
        this.fSawFallback[this.fDepth] = false;
        this.fSawInclude[this.fDepth] = false;
        this.fState[this.fDepth] = 1;
        this.fNotations = new ArrayList();
        this.fUnparsedEntities = new ArrayList();
        this.fBaseURIScope = new IntStack();
        this.fBaseURI = new Stack();
        this.fLiteralSystemID = new Stack();
        this.fExpandedSystemID = new Stack();
        this.fCurrentBaseURI = new XMLResourceIdentifierImpl();
        this.fLanguageScope = new IntStack();
        this.fLanguageStack = new Stack();
        this.fCurrentLanguage = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XNIException {
        this.fNamespaceContext = null;
        this.fDepth = 0;
        this.fResultDepth = isRootDocument() ? 0 : this.fParentXIncludeHandler.getResultDepth();
        this.fNotations.clear();
        this.fUnparsedEntities.clear();
        this.fParentRelativeURI = null;
        this.fIsXML11 = false;
        this.fInDTD = false;
        this.fSeenRootElement = false;
        this.fBaseURIScope.clear();
        this.fBaseURI.clear();
        this.fLiteralSystemID.clear();
        this.fExpandedSystemID.clear();
        this.fLanguageScope.clear();
        this.fLanguageStack.clear();
        for (int i2 = 0; i2 < this.fState.length; i2++) {
            this.fState[i2] = 1;
        }
        for (int i3 = 0; i3 < this.fSawFallback.length; i3++) {
            this.fSawFallback[i3] = false;
        }
        for (int i4 = 0; i4 < this.fSawInclude.length; i4++) {
            this.fSawInclude[i4] = false;
        }
        try {
            if (!componentManager.getFeature(PARSER_SETTINGS)) {
                return;
            }
        } catch (XMLConfigurationException e2) {
        }
        this.fNeedCopyFeatures = true;
        try {
            this.fSendUEAndNotationEvents = componentManager.getFeature(ALLOW_UE_AND_NOTATION_EVENTS);
            if (this.fChildConfig != null) {
                this.fChildConfig.setFeature(ALLOW_UE_AND_NOTATION_EVENTS, this.fSendUEAndNotationEvents);
            }
        } catch (XMLConfigurationException e3) {
        }
        try {
            this.fFixupBaseURIs = componentManager.getFeature(XINCLUDE_FIXUP_BASE_URIS);
            if (this.fChildConfig != null) {
                this.fChildConfig.setFeature(XINCLUDE_FIXUP_BASE_URIS, this.fFixupBaseURIs);
            }
        } catch (XMLConfigurationException e4) {
            this.fFixupBaseURIs = true;
        }
        try {
            this.fFixupLanguage = componentManager.getFeature(XINCLUDE_FIXUP_LANGUAGE);
            if (this.fChildConfig != null) {
                this.fChildConfig.setFeature(XINCLUDE_FIXUP_LANGUAGE, this.fFixupLanguage);
            }
        } catch (XMLConfigurationException e5) {
            this.fFixupLanguage = true;
        }
        try {
            SymbolTable value = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
            if (value != null) {
                this.fSymbolTable = value;
                if (this.fChildConfig != null) {
                    this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/symbol-table", value);
                }
            }
        } catch (XMLConfigurationException e6) {
            this.fSymbolTable = null;
        }
        try {
            XMLErrorReporter value2 = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
            if (value2 != null) {
                setErrorReporter(value2);
                if (this.fChildConfig != null) {
                    this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/error-reporter", value2);
                }
            }
        } catch (XMLConfigurationException e7) {
            this.fErrorReporter = null;
        }
        try {
            XMLEntityResolver value3 = (XMLEntityResolver) componentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
            if (value3 != null) {
                this.fEntityResolver = value3;
                if (this.fChildConfig != null) {
                    this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/entity-resolver", value3);
                }
            }
        } catch (XMLConfigurationException e8) {
            this.fEntityResolver = null;
        }
        try {
            XMLSecurityManager value4 = (XMLSecurityManager) componentManager.getProperty("http://apache.org/xml/properties/security-manager");
            if (value4 != null) {
                this.fSecurityManager = value4;
                if (this.fChildConfig != null) {
                    this.fChildConfig.setProperty("http://apache.org/xml/properties/security-manager", value4);
                }
            }
        } catch (XMLConfigurationException e9) {
            this.fSecurityManager = null;
        }
        this.fSecurityPropertyMgr = (XMLSecurityPropertyManager) componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
        try {
            Integer value5 = (Integer) componentManager.getProperty(BUFFER_SIZE);
            if (value5 != null && value5.intValue() > 0) {
                this.fBufferSize = value5.intValue();
                if (this.fChildConfig != null) {
                    this.fChildConfig.setProperty(BUFFER_SIZE, value5);
                }
            } else {
                this.fBufferSize = ((Integer) getPropertyDefault(BUFFER_SIZE)).intValue();
            }
        } catch (XMLConfigurationException e10) {
            this.fBufferSize = ((Integer) getPropertyDefault(BUFFER_SIZE)).intValue();
        }
        if (this.fXInclude10TextReader != null) {
            this.fXInclude10TextReader.setBufferSize(this.fBufferSize);
        }
        if (this.fXInclude11TextReader != null) {
            this.fXInclude11TextReader.setBufferSize(this.fBufferSize);
        }
        this.fSettings = new ParserConfigurationSettings();
        copyFeatures(componentManager, this.fSettings);
        try {
            if (componentManager.getFeature(SCHEMA_VALIDATION)) {
                this.fSettings.setFeature(SCHEMA_VALIDATION, false);
                if (componentManager.getFeature(VALIDATION)) {
                    this.fSettings.setFeature(DYNAMIC_VALIDATION, true);
                }
            }
        } catch (XMLConfigurationException e11) {
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) RECOGNIZED_FEATURES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        if (featureId.equals(ALLOW_UE_AND_NOTATION_EVENTS)) {
            this.fSendUEAndNotationEvents = state;
        }
        if (this.fSettings != null) {
            this.fNeedCopyFeatures = true;
            this.fSettings.setFeature(featureId, state);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) RECOGNIZED_PROPERTIES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (propertyId.equals("http://apache.org/xml/properties/internal/symbol-table")) {
            this.fSymbolTable = (SymbolTable) value;
            if (this.fChildConfig != null) {
                this.fChildConfig.setProperty(propertyId, value);
                return;
            }
            return;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/error-reporter")) {
            setErrorReporter((XMLErrorReporter) value);
            if (this.fChildConfig != null) {
                this.fChildConfig.setProperty(propertyId, value);
                return;
            }
            return;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
            this.fEntityResolver = (XMLEntityResolver) value;
            if (this.fChildConfig != null) {
                this.fChildConfig.setProperty(propertyId, value);
                return;
            }
            return;
        }
        if (propertyId.equals("http://apache.org/xml/properties/security-manager")) {
            this.fSecurityManager = (XMLSecurityManager) value;
            if (this.fChildConfig != null) {
                this.fChildConfig.setProperty(propertyId, value);
                return;
            }
            return;
        }
        if (propertyId.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
            this.fSecurityPropertyMgr = (XMLSecurityPropertyManager) value;
            if (this.fChildConfig != null) {
                this.fChildConfig.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", value);
                return;
            }
            return;
        }
        if (propertyId.equals(BUFFER_SIZE)) {
            Integer bufferSize = (Integer) value;
            if (this.fChildConfig != null) {
                this.fChildConfig.setProperty(propertyId, value);
            }
            if (bufferSize != null && bufferSize.intValue() > 0) {
                this.fBufferSize = bufferSize.intValue();
                if (this.fXInclude10TextReader != null) {
                    this.fXInclude10TextReader.setBufferSize(this.fBufferSize);
                }
                if (this.fXInclude11TextReader != null) {
                    this.fXInclude11TextReader.setBufferSize(this.fBufferSize);
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Boolean getFeatureDefault(String featureId) {
        for (int i2 = 0; i2 < RECOGNIZED_FEATURES.length; i2++) {
            if (RECOGNIZED_FEATURES[i2].equals(featureId)) {
                return FEATURE_DEFAULTS[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Object getPropertyDefault(String propertyId) {
        for (int i2 = 0; i2 < RECOGNIZED_PROPERTIES.length; i2++) {
            if (RECOGNIZED_PROPERTIES[i2].equals(propertyId)) {
                return PROPERTY_DEFAULTS[i2];
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public void setDocumentHandler(XMLDocumentHandler handler) {
        this.fDocumentHandler = handler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        this.fErrorReporter.setDocumentLocator(locator);
        if (!isRootDocument() && this.fParentXIncludeHandler.searchForRecursiveIncludes(locator)) {
            reportFatalError("RecursiveInclude", new Object[]{locator.getExpandedSystemId()});
        }
        if (!(namespaceContext instanceof XIncludeNamespaceSupport)) {
            reportFatalError("IncompatibleNamespaceContext");
        }
        this.fNamespaceContext = (XIncludeNamespaceSupport) namespaceContext;
        this.fDocLocation = locator;
        this.fCurrentBaseURI.setBaseSystemId(locator.getBaseSystemId());
        this.fCurrentBaseURI.setExpandedSystemId(locator.getExpandedSystemId());
        this.fCurrentBaseURI.setLiteralSystemId(locator.getLiteralSystemId());
        saveBaseURI();
        if (augs == null) {
            augs = new AugmentationsImpl();
        }
        augs.putItem(CURRENT_BASE_URI, this.fCurrentBaseURI);
        this.fCurrentLanguage = XMLSymbols.EMPTY_STRING;
        saveLanguage(this.fCurrentLanguage);
        if (isRootDocument() && this.fDocumentHandler != null) {
            this.fDocumentHandler.startDocument(locator, encoding, namespaceContext, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
        this.fIsXML11 = SerializerConstants.XMLVERSION11.equals(version);
        if (isRootDocument() && this.fDocumentHandler != null) {
            this.fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
        if (isRootDocument() && this.fDocumentHandler != null) {
            this.fDocumentHandler.doctypeDecl(rootElement, publicId, systemId, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        if (!this.fInDTD) {
            if (this.fDocumentHandler != null && getState() == 1) {
                this.fDepth++;
                this.fDocumentHandler.comment(text, modifyAugmentations(augs));
                this.fDepth--;
                return;
            }
            return;
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.comment(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
        if (!this.fInDTD) {
            if (this.fDocumentHandler != null && getState() == 1) {
                this.fDepth++;
                this.fDocumentHandler.processingInstruction(target, data, modifyAugmentations(augs));
                this.fDepth--;
                return;
            }
            return;
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.processingInstruction(target, data, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws MissingResourceException, XNIException {
        this.fDepth++;
        int lastState = getState(this.fDepth - 1);
        if (lastState == 3 && getState(this.fDepth - 2) == 3) {
            setState(2);
        } else {
            setState(lastState);
        }
        processXMLBaseAttributes(attributes);
        if (this.fFixupLanguage) {
            processXMLLangAttributes(attributes);
        }
        if (isIncludeElement(element)) {
            boolean success = handleIncludeElement(attributes);
            if (success) {
                setState(2);
                return;
            } else {
                setState(3);
                return;
            }
        }
        if (isFallbackElement(element)) {
            handleFallbackElement();
            return;
        }
        if (hasXIncludeNamespace(element)) {
            if (getSawInclude(this.fDepth - 1)) {
                reportFatalError("IncludeChild", new Object[]{element.rawname});
            }
            if (getSawFallback(this.fDepth - 1)) {
                reportFatalError("FallbackChild", new Object[]{element.rawname});
            }
            if (getState() == 1) {
                int i2 = this.fResultDepth;
                this.fResultDepth = i2 + 1;
                if (i2 == 0) {
                    checkMultipleRootElements();
                }
                if (this.fDocumentHandler != null) {
                    this.fDocumentHandler.startElement(element, processAttributes(attributes), modifyAugmentations(augs));
                    return;
                }
                return;
            }
            return;
        }
        if (getState() == 1) {
            int i3 = this.fResultDepth;
            this.fResultDepth = i3 + 1;
            if (i3 == 0) {
                checkMultipleRootElements();
            }
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.startElement(element, processAttributes(attributes), modifyAugmentations(augs));
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws MissingResourceException, XNIException {
        this.fDepth++;
        int lastState = getState(this.fDepth - 1);
        if (lastState == 3 && getState(this.fDepth - 2) == 3) {
            setState(2);
        } else {
            setState(lastState);
        }
        processXMLBaseAttributes(attributes);
        if (this.fFixupLanguage) {
            processXMLLangAttributes(attributes);
        }
        if (isIncludeElement(element)) {
            boolean success = handleIncludeElement(attributes);
            if (success) {
                setState(2);
            } else {
                reportFatalError("NoFallback", new Object[]{attributes.getValue(null, Constants.ATTRNAME_HREF)});
            }
        } else if (isFallbackElement(element)) {
            handleFallbackElement();
        } else if (hasXIncludeNamespace(element)) {
            if (getSawInclude(this.fDepth - 1)) {
                reportFatalError("IncludeChild", new Object[]{element.rawname});
            }
            if (getSawFallback(this.fDepth - 1)) {
                reportFatalError("FallbackChild", new Object[]{element.rawname});
            }
            if (getState() == 1) {
                if (this.fResultDepth == 0) {
                    checkMultipleRootElements();
                }
                if (this.fDocumentHandler != null) {
                    this.fDocumentHandler.emptyElement(element, processAttributes(attributes), modifyAugmentations(augs));
                }
            }
        } else if (getState() == 1) {
            if (this.fResultDepth == 0) {
                checkMultipleRootElements();
            }
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.emptyElement(element, processAttributes(attributes), modifyAugmentations(augs));
            }
        }
        setSawFallback(this.fDepth + 1, false);
        setSawInclude(this.fDepth, false);
        if (this.fBaseURIScope.size() > 0 && this.fDepth == this.fBaseURIScope.peek()) {
            restoreBaseURI();
        }
        this.fDepth--;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        if (isIncludeElement(element) && getState() == 3 && !getSawFallback(this.fDepth + 1)) {
            reportFatalError("NoFallback", new Object[]{"unknown"});
        }
        if (isFallbackElement(element)) {
            if (getState() == 1) {
                setState(2);
            }
        } else if (getState() == 1) {
            this.fResultDepth--;
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.endElement(element, augs);
            }
        }
        setSawFallback(this.fDepth + 1, false);
        setSawInclude(this.fDepth, false);
        if (this.fBaseURIScope.size() > 0 && this.fDepth == this.fBaseURIScope.peek()) {
            restoreBaseURI();
        }
        if (this.fLanguageScope.size() > 0 && this.fDepth == this.fLanguageScope.peek()) {
            this.fCurrentLanguage = restoreLanguage();
        }
        this.fDepth--;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startGeneralEntity(String name, XMLResourceIdentifier resId, String encoding, Augmentations augs) throws XNIException {
        if (getState() == 1) {
            if (this.fResultDepth == 0) {
                if (augs != null && Boolean.TRUE.equals(augs.getItem(com.sun.org.apache.xerces.internal.impl.Constants.ENTITY_SKIPPED))) {
                    reportFatalError("UnexpandedEntityReferenceIllegal");
                    return;
                }
                return;
            }
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.startGeneralEntity(name, resId, encoding, augs);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && getState() == 1) {
            this.fDocumentHandler.textDecl(version, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && getState() == 1 && this.fResultDepth != 0) {
            this.fDocumentHandler.endGeneralEntity(name, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        if (getState() == 1) {
            if (this.fResultDepth == 0) {
                checkWhitespace(text);
            } else if (this.fDocumentHandler != null) {
                this.fDepth++;
                this.fDocumentHandler.characters(text, modifyAugmentations(augs));
                this.fDepth--;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && getState() == 1 && this.fResultDepth != 0) {
            this.fDocumentHandler.ignorableWhitespace(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && getState() == 1 && this.fResultDepth != 0) {
            this.fDocumentHandler.startCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && getState() == 1 && this.fResultDepth != 0) {
            this.fDocumentHandler.endCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws XNIException {
        if (isRootDocument()) {
            if (!this.fSeenRootElement) {
                reportFatalError("RootElementRequired");
            }
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.endDocument(augs);
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void setDocumentSource(XMLDocumentSource source) {
        this.fDocumentSource = source;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public XMLDocumentSource getDocumentSource() {
        return this.fDocumentSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.attributeDecl(elementName, attributeName, type, enumeration, defaultType, defaultValue, nonNormalizedDefaultValue, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void elementDecl(String name, String contentModel, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.elementDecl(name, contentModel, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endAttlist(Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endAttlist(augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endConditional(Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endConditional(augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endDTD(Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endDTD(augmentations);
        }
        this.fInDTD = false;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endExternalSubset(Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endExternalSubset(augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endParameterEntity(String name, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endParameterEntity(name, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.externalEntityDecl(name, identifier, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public XMLDTDSource getDTDSource() {
        return this.fDTDSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void ignoredCharacters(XMLString text, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.ignoredCharacters(text, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.internalEntityDecl(name, text, nonNormalizedText, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augmentations) throws XNIException {
        addNotation(name, identifier, augmentations);
        if (this.fDTDHandler != null) {
            this.fDTDHandler.notationDecl(name, identifier, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void setDTDSource(XMLDTDSource source) {
        this.fDTDSource = source;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startAttlist(String elementName, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startAttlist(elementName, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startConditional(short type, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startConditional(type, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startDTD(XMLLocator locator, Augmentations augmentations) throws XNIException {
        this.fInDTD = true;
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startDTD(locator, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startExternalSubset(identifier, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augmentations) throws XNIException {
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startParameterEntity(name, identifier, encoding, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augmentations) throws XNIException {
        addUnparsedEntity(name, identifier, notation, augmentations);
        if (this.fDTDHandler != null) {
            this.fDTDHandler.unparsedEntityDecl(name, identifier, notation, augmentations);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource
    public XMLDTDHandler getDTDHandler() {
        return this.fDTDHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource
    public void setDTDHandler(XMLDTDHandler handler) {
        this.fDTDHandler = handler;
    }

    private void setErrorReporter(XMLErrorReporter reporter) {
        this.fErrorReporter = reporter;
        if (this.fErrorReporter != null) {
            this.fErrorReporter.putMessageFormatter(XIncludeMessageFormatter.XINCLUDE_DOMAIN, this.fXIncludeMessageFormatter);
            if (this.fDocLocation != null) {
                this.fErrorReporter.setDocumentLocator(this.fDocLocation);
            }
        }
    }

    protected void handleFallbackElement() throws XNIException {
        if (!getSawInclude(this.fDepth - 1)) {
            if (getState() == 2) {
                return;
            } else {
                reportFatalError("FallbackParent");
            }
        }
        setSawInclude(this.fDepth, false);
        this.fNamespaceContext.setContextInvalid();
        if (getSawFallback(this.fDepth)) {
            reportFatalError("MultipleFallbacks");
        } else {
            setSawFallback(this.fDepth, true);
        }
        if (getState() == 3) {
            setState(1);
        }
    }

    protected boolean handleIncludeElement(XMLAttributes attributes) throws MissingResourceException, XNIException {
        if (getSawInclude(this.fDepth - 1)) {
            reportFatalError("IncludeChild", new Object[]{XINCLUDE_INCLUDE});
        }
        if (getState() == 2) {
            return true;
        }
        setSawInclude(this.fDepth, true);
        this.fNamespaceContext.setContextInvalid();
        String href = attributes.getValue(XINCLUDE_ATTR_HREF);
        String parse = attributes.getValue(XINCLUDE_ATTR_PARSE);
        String xpointer = attributes.getValue(XPOINTER);
        String accept = attributes.getValue(XINCLUDE_ATTR_ACCEPT);
        String acceptLanguage = attributes.getValue(XINCLUDE_ATTR_ACCEPT_LANGUAGE);
        if (parse == null) {
            parse = XINCLUDE_PARSE_XML;
        }
        if (href == null) {
            href = XMLSymbols.EMPTY_STRING;
        }
        if (href.length() == 0 && XINCLUDE_PARSE_XML.equals(parse)) {
            if (xpointer != null) {
                Locale locale = this.fErrorReporter != null ? this.fErrorReporter.getLocale() : null;
                String reason = this.fXIncludeMessageFormatter.formatMessage(locale, "XPointerStreamability", null);
                reportResourceError("XMLResourceError", new Object[]{href, reason});
                return false;
            }
            reportFatalError("XpointerMissing");
        }
        try {
            URI hrefURI = new URI(href, true);
            if (hrefURI.getFragment() != null) {
                reportFatalError("HrefFragmentIdentifierIllegal", new Object[]{href});
            }
        } catch (URI.MalformedURIException e2) {
            String newHref = escapeHref(href);
            if (href != newHref) {
                href = newHref;
                try {
                    URI hrefURI2 = new URI(href, true);
                    if (hrefURI2.getFragment() != null) {
                        reportFatalError("HrefFragmentIdentifierIllegal", new Object[]{href});
                    }
                } catch (URI.MalformedURIException e3) {
                    reportFatalError("HrefSyntacticallyInvalid", new Object[]{href});
                }
            } else {
                reportFatalError("HrefSyntacticallyInvalid", new Object[]{href});
            }
        }
        if (accept != null && !isValidInHTTPHeader(accept)) {
            reportFatalError("AcceptMalformed", null);
            accept = null;
        }
        if (acceptLanguage != null && !isValidInHTTPHeader(acceptLanguage)) {
            reportFatalError("AcceptLanguageMalformed", null);
            acceptLanguage = null;
        }
        XMLInputSource includedSource = null;
        if (this.fEntityResolver != null) {
            try {
                XMLResourceIdentifier resourceIdentifier = new XMLResourceIdentifierImpl(null, href, this.fCurrentBaseURI.getExpandedSystemId(), XMLEntityManager.expandSystemId(href, this.fCurrentBaseURI.getExpandedSystemId(), false));
                includedSource = this.fEntityResolver.resolveEntity(resourceIdentifier);
                if (includedSource != null && !(includedSource instanceof HTTPInputSource) && ((accept != null || acceptLanguage != null) && includedSource.getCharacterStream() == null && includedSource.getByteStream() == null)) {
                    includedSource = createInputSource(includedSource.getPublicId(), includedSource.getSystemId(), includedSource.getBaseSystemId(), accept, acceptLanguage);
                }
            } catch (IOException e4) {
                reportResourceError("XMLResourceError", new Object[]{href, e4.getMessage()});
                return false;
            }
        }
        if (includedSource == null) {
            includedSource = (accept == null && acceptLanguage == null) ? new XMLInputSource(null, href, this.fCurrentBaseURI.getExpandedSystemId()) : createInputSource(null, href, this.fCurrentBaseURI.getExpandedSystemId(), accept, acceptLanguage);
        }
        if (!parse.equals(XINCLUDE_PARSE_XML)) {
            if (!parse.equals(XINCLUDE_PARSE_TEXT)) {
                reportFatalError("InvalidParseValue", new Object[]{parse});
                return true;
            }
            String encoding = attributes.getValue(XINCLUDE_ATTR_ENCODING);
            includedSource.setEncoding(encoding);
            XIncludeTextReader textReader = null;
            try {
                try {
                    try {
                        if (this.fIsXML11) {
                            if (this.fXInclude11TextReader == null) {
                                this.fXInclude11TextReader = new XInclude11TextReader(includedSource, this, this.fBufferSize);
                            } else {
                                this.fXInclude11TextReader.setInputSource(includedSource);
                            }
                            textReader = this.fXInclude11TextReader;
                        } else {
                            if (this.fXInclude10TextReader == null) {
                                this.fXInclude10TextReader = new XIncludeTextReader(includedSource, this, this.fBufferSize);
                            } else {
                                this.fXInclude10TextReader.setInputSource(includedSource);
                            }
                            textReader = this.fXInclude10TextReader;
                        }
                        textReader.setErrorReporter(this.fErrorReporter);
                        textReader.parse();
                        if (textReader == null) {
                            return true;
                        }
                        try {
                            textReader.close();
                            return true;
                        } catch (IOException e5) {
                            reportResourceError("TextResourceError", new Object[]{href, e5.getMessage()});
                            return false;
                        }
                    } catch (MalformedByteSequenceException ex) {
                        this.fErrorReporter.reportError(ex.getDomain(), ex.getKey(), ex.getArguments(), (short) 2);
                        if (textReader == null) {
                            return true;
                        }
                        try {
                            textReader.close();
                            return true;
                        } catch (IOException e6) {
                            reportResourceError("TextResourceError", new Object[]{href, e6.getMessage()});
                            return false;
                        }
                    } catch (IOException e7) {
                        reportResourceError("TextResourceError", new Object[]{href, e7.getMessage()});
                        if (textReader != null) {
                            try {
                                textReader.close();
                            } catch (IOException e8) {
                                reportResourceError("TextResourceError", new Object[]{href, e8.getMessage()});
                                return false;
                            }
                        }
                        return false;
                    }
                } catch (CharConversionException e9) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "CharConversionFailure", null, (short) 2);
                    if (textReader == null) {
                        return true;
                    }
                    try {
                        textReader.close();
                        return true;
                    } catch (IOException e10) {
                        reportResourceError("TextResourceError", new Object[]{href, e10.getMessage()});
                        return false;
                    }
                }
            } catch (Throwable th) {
                if (textReader != null) {
                    try {
                        textReader.close();
                    } catch (IOException e11) {
                        reportResourceError("TextResourceError", new Object[]{href, e11.getMessage()});
                        return false;
                    }
                }
                throw th;
            }
        }
        if ((xpointer != null && this.fXPointerChildConfig == null) || (xpointer == null && this.fXIncludeChildConfig == null)) {
            String parserName = XINCLUDE_DEFAULT_CONFIGURATION;
            if (xpointer != null) {
                parserName = "com.sun.org.apache.xerces.internal.parsers.XPointerParserConfiguration";
            }
            this.fChildConfig = (XMLParserConfiguration) ObjectFactory.newInstance(parserName, true);
            if (this.fSymbolTable != null) {
                this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
            }
            if (this.fErrorReporter != null) {
                this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
            }
            if (this.fEntityResolver != null) {
                this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fEntityResolver);
            }
            this.fChildConfig.setProperty("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
            this.fChildConfig.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
            this.fChildConfig.setProperty(BUFFER_SIZE, new Integer(this.fBufferSize));
            this.fNeedCopyFeatures = true;
            this.fChildConfig.setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNamespaceContext);
            this.fChildConfig.setFeature(XINCLUDE_FIXUP_BASE_URIS, this.fFixupBaseURIs);
            this.fChildConfig.setFeature(XINCLUDE_FIXUP_LANGUAGE, this.fFixupLanguage);
            if (xpointer != null) {
                XPointerHandler newHandler = (XPointerHandler) this.fChildConfig.getProperty("http://apache.org/xml/properties/internal/xpointer-handler");
                this.fXPtrProcessor = newHandler;
                ((XPointerHandler) this.fXPtrProcessor).setProperty("http://apache.org/xml/properties/internal/namespace-context", this.fNamespaceContext);
                ((XPointerHandler) this.fXPtrProcessor).setProperty(XINCLUDE_FIXUP_BASE_URIS, Boolean.valueOf(this.fFixupBaseURIs));
                ((XPointerHandler) this.fXPtrProcessor).setProperty(XINCLUDE_FIXUP_LANGUAGE, Boolean.valueOf(this.fFixupLanguage));
                if (this.fErrorReporter != null) {
                    ((XPointerHandler) this.fXPtrProcessor).setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
                }
                newHandler.setParent(this);
                newHandler.setDocumentHandler(getDocumentHandler());
                this.fXPointerChildConfig = this.fChildConfig;
            } else {
                XIncludeHandler newHandler2 = (XIncludeHandler) this.fChildConfig.getProperty("http://apache.org/xml/properties/internal/xinclude-handler");
                newHandler2.setParent(this);
                newHandler2.setDocumentHandler(getDocumentHandler());
                this.fXIncludeChildConfig = this.fChildConfig;
            }
        }
        if (xpointer != null) {
            this.fChildConfig = this.fXPointerChildConfig;
            try {
                this.fXPtrProcessor.parseXPointer(xpointer);
            } catch (XNIException ex2) {
                reportResourceError("XMLResourceError", new Object[]{href, ex2.getMessage()});
                return false;
            }
        } else {
            this.fChildConfig = this.fXIncludeChildConfig;
        }
        if (this.fNeedCopyFeatures) {
            copyFeatures(this.fSettings, this.fChildConfig);
        }
        this.fNeedCopyFeatures = false;
        try {
            try {
                try {
                    this.fNamespaceContext.pushScope();
                    this.fChildConfig.parse(includedSource);
                    if (this.fErrorReporter != null) {
                        this.fErrorReporter.setDocumentLocator(this.fDocLocation);
                    }
                    if (xpointer == null || this.fXPtrProcessor.isXPointerResolved()) {
                        this.fNamespaceContext.popScope();
                        return true;
                    }
                    Locale locale2 = this.fErrorReporter != null ? this.fErrorReporter.getLocale() : null;
                    String reason2 = this.fXIncludeMessageFormatter.formatMessage(locale2, "XPointerResolutionUnsuccessful", null);
                    reportResourceError("XMLResourceError", new Object[]{href, reason2});
                    this.fNamespaceContext.popScope();
                    return false;
                } catch (IOException e12) {
                    if (this.fErrorReporter != null) {
                        this.fErrorReporter.setDocumentLocator(this.fDocLocation);
                    }
                    reportResourceError("XMLResourceError", new Object[]{href, e12.getMessage()});
                    this.fNamespaceContext.popScope();
                    return false;
                }
            } catch (XNIException e13) {
                if (this.fErrorReporter != null) {
                    this.fErrorReporter.setDocumentLocator(this.fDocLocation);
                }
                reportFatalError("XMLParseError", new Object[]{href, e13.getMessage()});
                this.fNamespaceContext.popScope();
                return true;
            }
        } catch (Throwable th2) {
            this.fNamespaceContext.popScope();
            throw th2;
        }
    }

    protected boolean hasXIncludeNamespace(QName element) {
        return element.uri == XINCLUDE_NS_URI || this.fNamespaceContext.getURI(element.prefix) == XINCLUDE_NS_URI;
    }

    protected boolean isIncludeElement(QName element) {
        return element.localpart.equals(XINCLUDE_INCLUDE) && hasXIncludeNamespace(element);
    }

    protected boolean isFallbackElement(QName element) {
        return element.localpart.equals(XINCLUDE_FALLBACK) && hasXIncludeNamespace(element);
    }

    protected boolean sameBaseURIAsIncludeParent() {
        String parentBaseURI = getIncludeParentBaseURI();
        String baseURI = this.fCurrentBaseURI.getExpandedSystemId();
        return parentBaseURI != null && parentBaseURI.equals(baseURI);
    }

    protected boolean sameLanguageAsIncludeParent() {
        String parentLanguage = getIncludeParentLanguage();
        return parentLanguage != null && parentLanguage.equalsIgnoreCase(this.fCurrentLanguage);
    }

    protected boolean searchForRecursiveIncludes(XMLLocator includedSource) throws XNIException {
        String includedSystemId = includedSource.getExpandedSystemId();
        if (includedSystemId == null) {
            try {
                includedSystemId = XMLEntityManager.expandSystemId(includedSource.getLiteralSystemId(), includedSource.getBaseSystemId(), false);
            } catch (URI.MalformedURIException e2) {
                reportFatalError("ExpandedSystemId");
            }
        }
        if (includedSystemId.equals(this.fCurrentBaseURI.getExpandedSystemId())) {
            return true;
        }
        if (this.fParentXIncludeHandler == null) {
            return false;
        }
        return this.fParentXIncludeHandler.searchForRecursiveIncludes(includedSource);
    }

    protected boolean isTopLevelIncludedItem() {
        return isTopLevelIncludedItemViaInclude() || isTopLevelIncludedItemViaFallback();
    }

    protected boolean isTopLevelIncludedItemViaInclude() {
        return this.fDepth == 1 && !isRootDocument();
    }

    protected boolean isTopLevelIncludedItemViaFallback() {
        return getSawFallback(this.fDepth - 1);
    }

    protected XMLAttributes processAttributes(XMLAttributes attributes) throws XNIException {
        String strIntern;
        String uri;
        if (isTopLevelIncludedItem()) {
            if (this.fFixupBaseURIs && !sameBaseURIAsIncludeParent()) {
                if (attributes == null) {
                    attributes = new XMLAttributesImpl();
                }
                try {
                    uri = getRelativeBaseURI();
                } catch (URI.MalformedURIException e2) {
                    uri = this.fCurrentBaseURI.getExpandedSystemId();
                }
                int index = attributes.addAttribute(XML_BASE_QNAME, XMLSymbols.fCDATASymbol, uri);
                attributes.setSpecified(index, true);
            }
            if (this.fFixupLanguage && !sameLanguageAsIncludeParent()) {
                if (attributes == null) {
                    attributes = new XMLAttributesImpl();
                }
                int index2 = attributes.addAttribute(XML_LANG_QNAME, XMLSymbols.fCDATASymbol, this.fCurrentLanguage);
                attributes.setSpecified(index2, true);
            }
            Enumeration inscopeNS = this.fNamespaceContext.getAllPrefixes();
            while (inscopeNS.hasMoreElements()) {
                String prefix = (String) inscopeNS.nextElement2();
                String parentURI = this.fNamespaceContext.getURIFromIncludeParent(prefix);
                String uri2 = this.fNamespaceContext.getURI(prefix);
                if (parentURI != uri2 && attributes != null) {
                    if (prefix == XMLSymbols.EMPTY_STRING) {
                        if (attributes.getValue(NamespaceContext.XMLNS_URI, XMLSymbols.PREFIX_XMLNS) == null) {
                            if (attributes == null) {
                                attributes = new XMLAttributesImpl();
                            }
                            QName ns = (QName) NEW_NS_ATTR_QNAME.clone();
                            ns.prefix = null;
                            ns.localpart = XMLSymbols.PREFIX_XMLNS;
                            ns.rawname = XMLSymbols.PREFIX_XMLNS;
                            int index3 = attributes.addAttribute(ns, XMLSymbols.fCDATASymbol, uri2 != null ? uri2 : XMLSymbols.EMPTY_STRING);
                            attributes.setSpecified(index3, true);
                            this.fNamespaceContext.declarePrefix(prefix, uri2);
                        }
                    } else if (attributes.getValue(NamespaceContext.XMLNS_URI, prefix) == null) {
                        if (attributes == null) {
                            attributes = new XMLAttributesImpl();
                        }
                        QName ns2 = (QName) NEW_NS_ATTR_QNAME.clone();
                        ns2.localpart = prefix;
                        ns2.rawname += prefix;
                        if (this.fSymbolTable != null) {
                            strIntern = this.fSymbolTable.addSymbol(ns2.rawname);
                        } else {
                            strIntern = ns2.rawname.intern();
                        }
                        ns2.rawname = strIntern;
                        int index4 = attributes.addAttribute(ns2, XMLSymbols.fCDATASymbol, uri2 != null ? uri2 : XMLSymbols.EMPTY_STRING);
                        attributes.setSpecified(index4, true);
                        this.fNamespaceContext.declarePrefix(prefix, uri2);
                    }
                }
            }
        }
        if (attributes != null) {
            int length = attributes.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                String type = attributes.getType(i2);
                String value = attributes.getValue(i2);
                if (type == XMLSymbols.fENTITYSymbol) {
                    checkUnparsedEntity(value);
                }
                if (type == XMLSymbols.fENTITIESSymbol) {
                    StringTokenizer st = new StringTokenizer(value);
                    while (st.hasMoreTokens()) {
                        String entName = st.nextToken();
                        checkUnparsedEntity(entName);
                    }
                } else if (type == XMLSymbols.fNOTATIONSymbol) {
                    checkNotation(value);
                }
            }
        }
        return attributes;
    }

    protected String getRelativeBaseURI() throws URI.MalformedURIException {
        int includeParentDepth = getIncludeParentDepth();
        String relativeURI = getRelativeURI(includeParentDepth);
        if (isRootDocument()) {
            return relativeURI;
        }
        if (relativeURI.equals("")) {
            relativeURI = this.fCurrentBaseURI.getLiteralSystemId();
        }
        if (includeParentDepth == 0) {
            if (this.fParentRelativeURI == null) {
                this.fParentRelativeURI = this.fParentXIncludeHandler.getRelativeBaseURI();
            }
            if (this.fParentRelativeURI.equals("")) {
                return relativeURI;
            }
            URI base = new URI(this.fParentRelativeURI, true);
            URI uri = new URI(base, relativeURI);
            String baseScheme = base.getScheme();
            String literalScheme = uri.getScheme();
            if (!Objects.equals(baseScheme, literalScheme)) {
                return relativeURI;
            }
            String baseAuthority = base.getAuthority();
            String literalAuthority = uri.getAuthority();
            if (!Objects.equals(baseAuthority, literalAuthority)) {
                return uri.getSchemeSpecificPart();
            }
            String literalPath = uri.getPath();
            String literalQuery = uri.getQueryString();
            String literalFragment = uri.getFragment();
            if (literalQuery != null || literalFragment != null) {
                StringBuilder buffer = new StringBuilder();
                if (literalPath != null) {
                    buffer.append(literalPath);
                }
                if (literalQuery != null) {
                    buffer.append('?');
                    buffer.append(literalQuery);
                }
                if (literalFragment != null) {
                    buffer.append('#');
                    buffer.append(literalFragment);
                }
                return buffer.toString();
            }
            return literalPath;
        }
        return relativeURI;
    }

    private String getIncludeParentBaseURI() {
        int depth = getIncludeParentDepth();
        if (!isRootDocument() && depth == 0) {
            return this.fParentXIncludeHandler.getIncludeParentBaseURI();
        }
        return getBaseURI(depth);
    }

    private String getIncludeParentLanguage() {
        int depth = getIncludeParentDepth();
        if (!isRootDocument() && depth == 0) {
            return this.fParentXIncludeHandler.getIncludeParentLanguage();
        }
        return getLanguage(depth);
    }

    private int getIncludeParentDepth() {
        for (int i2 = this.fDepth - 1; i2 >= 0; i2--) {
            if (!getSawInclude(i2) && !getSawFallback(i2)) {
                return i2;
            }
        }
        return 0;
    }

    private int getResultDepth() {
        return this.fResultDepth;
    }

    protected Augmentations modifyAugmentations(Augmentations augs) {
        return modifyAugmentations(augs, false);
    }

    protected Augmentations modifyAugmentations(Augmentations augs, boolean force) {
        if (force || isTopLevelIncludedItem()) {
            if (augs == null) {
                augs = new AugmentationsImpl();
            }
            augs.putItem(XINCLUDE_INCLUDED, Boolean.TRUE);
        }
        return augs;
    }

    protected int getState(int depth) {
        return this.fState[depth];
    }

    protected int getState() {
        return this.fState[this.fDepth];
    }

    protected void setState(int state) {
        if (this.fDepth >= this.fState.length) {
            int[] newarray = new int[this.fDepth * 2];
            System.arraycopy(this.fState, 0, newarray, 0, this.fState.length);
            this.fState = newarray;
        }
        this.fState[this.fDepth] = state;
    }

    protected void setSawFallback(int depth, boolean val) {
        if (depth >= this.fSawFallback.length) {
            boolean[] newarray = new boolean[depth * 2];
            System.arraycopy(this.fSawFallback, 0, newarray, 0, this.fSawFallback.length);
            this.fSawFallback = newarray;
        }
        this.fSawFallback[depth] = val;
    }

    protected boolean getSawFallback(int depth) {
        if (depth >= this.fSawFallback.length) {
            return false;
        }
        return this.fSawFallback[depth];
    }

    protected void setSawInclude(int depth, boolean val) {
        if (depth >= this.fSawInclude.length) {
            boolean[] newarray = new boolean[depth * 2];
            System.arraycopy(this.fSawInclude, 0, newarray, 0, this.fSawInclude.length);
            this.fSawInclude = newarray;
        }
        this.fSawInclude[depth] = val;
    }

    protected boolean getSawInclude(int depth) {
        if (depth >= this.fSawInclude.length) {
            return false;
        }
        return this.fSawInclude[depth];
    }

    protected void reportResourceError(String key) throws XNIException {
        reportFatalError(key, null);
    }

    protected void reportResourceError(String key, Object[] args) throws XNIException {
        reportError(key, args, (short) 0);
    }

    protected void reportFatalError(String key) throws XNIException {
        reportFatalError(key, null);
    }

    protected void reportFatalError(String key, Object[] args) throws XNIException {
        reportError(key, args, (short) 2);
    }

    private void reportError(String key, Object[] args, short severity) throws XNIException {
        if (this.fErrorReporter != null) {
            this.fErrorReporter.reportError(XIncludeMessageFormatter.XINCLUDE_DOMAIN, key, args, severity);
        }
    }

    protected void setParent(XIncludeHandler parent) {
        this.fParentXIncludeHandler = parent;
    }

    protected boolean isRootDocument() {
        return this.fParentXIncludeHandler == null;
    }

    protected void addUnparsedEntity(String name, XMLResourceIdentifier identifier, String notation, Augmentations augmentations) {
        UnparsedEntity ent = new UnparsedEntity();
        ent.name = name;
        ent.systemId = identifier.getLiteralSystemId();
        ent.publicId = identifier.getPublicId();
        ent.baseURI = identifier.getBaseSystemId();
        ent.expandedSystemId = identifier.getExpandedSystemId();
        ent.notation = notation;
        ent.augmentations = augmentations;
        this.fUnparsedEntities.add(ent);
    }

    protected void addNotation(String name, XMLResourceIdentifier identifier, Augmentations augmentations) {
        Notation not = new Notation();
        not.name = name;
        not.systemId = identifier.getLiteralSystemId();
        not.publicId = identifier.getPublicId();
        not.baseURI = identifier.getBaseSystemId();
        not.expandedSystemId = identifier.getExpandedSystemId();
        not.augmentations = augmentations;
        this.fNotations.add(not);
    }

    protected void checkUnparsedEntity(String entName) throws XNIException {
        UnparsedEntity ent = new UnparsedEntity();
        ent.name = entName;
        int index = this.fUnparsedEntities.indexOf(ent);
        if (index != -1) {
            UnparsedEntity ent2 = (UnparsedEntity) this.fUnparsedEntities.get(index);
            checkNotation(ent2.notation);
            checkAndSendUnparsedEntity(ent2);
        }
    }

    protected void checkNotation(String notName) throws XNIException {
        Notation not = new Notation();
        not.name = notName;
        int index = this.fNotations.indexOf(not);
        if (index != -1) {
            checkAndSendNotation((Notation) this.fNotations.get(index));
        }
    }

    protected void checkAndSendUnparsedEntity(UnparsedEntity ent) throws XNIException {
        if (isRootDocument()) {
            int index = this.fUnparsedEntities.indexOf(ent);
            if (index == -1) {
                XMLResourceIdentifier id = new XMLResourceIdentifierImpl(ent.publicId, ent.systemId, ent.baseURI, ent.expandedSystemId);
                addUnparsedEntity(ent.name, id, ent.notation, ent.augmentations);
                if (this.fSendUEAndNotationEvents && this.fDTDHandler != null) {
                    this.fDTDHandler.unparsedEntityDecl(ent.name, id, ent.notation, ent.augmentations);
                    return;
                }
                return;
            }
            UnparsedEntity localEntity = (UnparsedEntity) this.fUnparsedEntities.get(index);
            if (!ent.isDuplicate(localEntity)) {
                reportFatalError("NonDuplicateUnparsedEntity", new Object[]{ent.name});
                return;
            }
            return;
        }
        this.fParentXIncludeHandler.checkAndSendUnparsedEntity(ent);
    }

    protected void checkAndSendNotation(Notation not) throws XNIException {
        if (isRootDocument()) {
            int index = this.fNotations.indexOf(not);
            if (index == -1) {
                XMLResourceIdentifier id = new XMLResourceIdentifierImpl(not.publicId, not.systemId, not.baseURI, not.expandedSystemId);
                addNotation(not.name, id, not.augmentations);
                if (this.fSendUEAndNotationEvents && this.fDTDHandler != null) {
                    this.fDTDHandler.notationDecl(not.name, id, not.augmentations);
                    return;
                }
                return;
            }
            Notation localNotation = (Notation) this.fNotations.get(index);
            if (!not.isDuplicate(localNotation)) {
                reportFatalError("NonDuplicateNotation", new Object[]{not.name});
                return;
            }
            return;
        }
        this.fParentXIncludeHandler.checkAndSendNotation(not);
    }

    private void checkWhitespace(XMLString value) throws XNIException {
        int end = value.offset + value.length;
        for (int i2 = value.offset; i2 < end; i2++) {
            if (!XMLChar.isSpace(value.ch[i2])) {
                reportFatalError("ContentIllegalAtTopLevel");
                return;
            }
        }
    }

    private void checkMultipleRootElements() throws XNIException {
        if (getRootElementProcessed()) {
            reportFatalError("MultipleRootElements");
        }
        setRootElementProcessed(true);
    }

    private void setRootElementProcessed(boolean seenRoot) {
        if (isRootDocument()) {
            this.fSeenRootElement = seenRoot;
        } else {
            this.fParentXIncludeHandler.setRootElementProcessed(seenRoot);
        }
    }

    private boolean getRootElementProcessed() {
        return isRootDocument() ? this.fSeenRootElement : this.fParentXIncludeHandler.getRootElementProcessed();
    }

    protected void copyFeatures(XMLComponentManager from, ParserConfigurationSettings to) {
        Enumeration features = com.sun.org.apache.xerces.internal.impl.Constants.getXercesFeatures();
        copyFeatures1(features, com.sun.org.apache.xerces.internal.impl.Constants.XERCES_FEATURE_PREFIX, from, to);
        Enumeration features2 = com.sun.org.apache.xerces.internal.impl.Constants.getSAXFeatures();
        copyFeatures1(features2, com.sun.org.apache.xerces.internal.impl.Constants.SAX_FEATURE_PREFIX, from, to);
    }

    protected void copyFeatures(XMLComponentManager from, XMLParserConfiguration to) throws XMLConfigurationException {
        Enumeration features = com.sun.org.apache.xerces.internal.impl.Constants.getXercesFeatures();
        copyFeatures1(features, com.sun.org.apache.xerces.internal.impl.Constants.XERCES_FEATURE_PREFIX, from, to);
        Enumeration features2 = com.sun.org.apache.xerces.internal.impl.Constants.getSAXFeatures();
        copyFeatures1(features2, com.sun.org.apache.xerces.internal.impl.Constants.SAX_FEATURE_PREFIX, from, to);
    }

    private void copyFeatures1(Enumeration features, String featurePrefix, XMLComponentManager from, ParserConfigurationSettings to) {
        while (features.hasMoreElements()) {
            String featureId = featurePrefix + ((String) features.nextElement2());
            to.addRecognizedFeatures(new String[]{featureId});
            try {
                to.setFeature(featureId, from.getFeature(featureId));
            } catch (XMLConfigurationException e2) {
            }
        }
    }

    private void copyFeatures1(Enumeration features, String featurePrefix, XMLComponentManager from, XMLParserConfiguration to) throws XMLConfigurationException {
        while (features.hasMoreElements()) {
            String featureId = featurePrefix + ((String) features.nextElement2());
            boolean value = from.getFeature(featureId);
            try {
                to.setFeature(featureId, value);
            } catch (XMLConfigurationException e2) {
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/XIncludeHandler$Notation.class */
    protected static class Notation {
        public String name;
        public String systemId;
        public String baseURI;
        public String publicId;
        public String expandedSystemId;
        public Augmentations augmentations;

        protected Notation() {
        }

        public boolean equals(Object obj) {
            return obj == this || ((obj instanceof Notation) && Objects.equals(this.name, ((Notation) obj).name));
        }

        public int hashCode() {
            return Objects.hashCode(this.name);
        }

        public boolean isDuplicate(Object obj) {
            if (obj != null && (obj instanceof Notation)) {
                Notation other = (Notation) obj;
                return Objects.equals(this.name, other.name) && Objects.equals(this.publicId, other.publicId) && Objects.equals(this.expandedSystemId, other.expandedSystemId);
            }
            return false;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/XIncludeHandler$UnparsedEntity.class */
    protected static class UnparsedEntity {
        public String name;
        public String systemId;
        public String baseURI;
        public String publicId;
        public String expandedSystemId;
        public String notation;
        public Augmentations augmentations;

        protected UnparsedEntity() {
        }

        public boolean equals(Object obj) {
            return obj == this || ((obj instanceof UnparsedEntity) && Objects.equals(this.name, ((UnparsedEntity) obj).name));
        }

        public int hashCode() {
            return Objects.hashCode(this.name);
        }

        public boolean isDuplicate(Object obj) {
            if (obj != null && (obj instanceof UnparsedEntity)) {
                UnparsedEntity other = (UnparsedEntity) obj;
                return Objects.equals(this.name, other.name) && Objects.equals(this.publicId, other.publicId) && Objects.equals(this.expandedSystemId, other.expandedSystemId) && Objects.equals(this.notation, other.notation);
            }
            return false;
        }
    }

    protected void saveBaseURI() {
        this.fBaseURIScope.push(this.fDepth);
        this.fBaseURI.push(this.fCurrentBaseURI.getBaseSystemId());
        this.fLiteralSystemID.push(this.fCurrentBaseURI.getLiteralSystemId());
        this.fExpandedSystemID.push(this.fCurrentBaseURI.getExpandedSystemId());
    }

    protected void restoreBaseURI() {
        this.fBaseURI.pop();
        this.fLiteralSystemID.pop();
        this.fExpandedSystemID.pop();
        this.fBaseURIScope.pop();
        this.fCurrentBaseURI.setBaseSystemId((String) this.fBaseURI.peek());
        this.fCurrentBaseURI.setLiteralSystemId((String) this.fLiteralSystemID.peek());
        this.fCurrentBaseURI.setExpandedSystemId((String) this.fExpandedSystemID.peek());
    }

    protected void saveLanguage(String language) {
        this.fLanguageScope.push(this.fDepth);
        this.fLanguageStack.push(language);
    }

    public String restoreLanguage() {
        this.fLanguageStack.pop();
        this.fLanguageScope.pop();
        return (String) this.fLanguageStack.peek();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String getBaseURI(int depth) {
        int scope = scopeOfBaseURI(depth);
        return (String) this.fExpandedSystemID.elementAt(scope);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String getLanguage(int depth) {
        int scope = scopeOfLanguage(depth);
        return (String) this.fLanguageStack.elementAt(scope);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String getRelativeURI(int depth) throws URI.MalformedURIException {
        int start = scopeOfBaseURI(depth) + 1;
        if (start == this.fBaseURIScope.size()) {
            return "";
        }
        URI uri = new URI(DeploymentDescriptorParser.ATTR_FILE, (String) this.fLiteralSystemID.elementAt(start));
        for (int i2 = start + 1; i2 < this.fBaseURIScope.size(); i2++) {
            uri = new URI(uri, (String) this.fLiteralSystemID.elementAt(i2));
        }
        return uri.getPath();
    }

    private int scopeOfBaseURI(int depth) {
        for (int i2 = this.fBaseURIScope.size() - 1; i2 >= 0; i2--) {
            if (this.fBaseURIScope.elementAt(i2) <= depth) {
                return i2;
            }
        }
        return -1;
    }

    private int scopeOfLanguage(int depth) {
        for (int i2 = this.fLanguageScope.size() - 1; i2 >= 0; i2--) {
            if (this.fLanguageScope.elementAt(i2) <= depth) {
                return i2;
            }
        }
        return -1;
    }

    protected void processXMLBaseAttributes(XMLAttributes attributes) {
        String baseURIValue = attributes.getValue(NamespaceContext.XML_URI, "base");
        if (baseURIValue != null) {
            try {
                String expandedValue = XMLEntityManager.expandSystemId(baseURIValue, this.fCurrentBaseURI.getExpandedSystemId(), false);
                this.fCurrentBaseURI.setLiteralSystemId(baseURIValue);
                this.fCurrentBaseURI.setBaseSystemId(this.fCurrentBaseURI.getExpandedSystemId());
                this.fCurrentBaseURI.setExpandedSystemId(expandedValue);
                saveBaseURI();
            } catch (URI.MalformedURIException e2) {
            }
        }
    }

    protected void processXMLLangAttributes(XMLAttributes attributes) {
        String language = attributes.getValue(NamespaceContext.XML_URI, "lang");
        if (language != null) {
            this.fCurrentLanguage = language;
            saveLanguage(this.fCurrentLanguage);
        }
    }

    private boolean isValidInHTTPHeader(String value) {
        for (int i2 = value.length() - 1; i2 >= 0; i2--) {
            char ch = value.charAt(i2);
            if (ch < ' ' || ch > '~') {
                return false;
            }
        }
        return true;
    }

    private XMLInputSource createInputSource(String publicId, String systemId, String baseSystemId, String accept, String acceptLanguage) {
        HTTPInputSource httpSource = new HTTPInputSource(publicId, systemId, baseSystemId);
        if (accept != null && accept.length() > 0) {
            httpSource.setHTTPRequestProperty(HTTP_ACCEPT, accept);
        }
        if (acceptLanguage != null && acceptLanguage.length() > 0) {
            httpSource.setHTTPRequestProperty(HTTP_ACCEPT_LANGUAGE, acceptLanguage);
        }
        return httpSource;
    }

    private String escapeHref(String href) {
        int ch2;
        int ch;
        int len = href.length();
        StringBuilder buffer = new StringBuilder(len * 3);
        int i2 = 0;
        while (i2 < len && (ch = href.charAt(i2)) <= 126) {
            if (ch < 32) {
                return href;
            }
            if (gNeedEscaping[ch]) {
                buffer.append('%');
                buffer.append(gAfterEscaping1[ch]);
                buffer.append(gAfterEscaping2[ch]);
            } else {
                buffer.append((char) ch);
            }
            i2++;
        }
        if (i2 < len) {
            int j2 = i2;
            while (j2 < len) {
                int ch3 = href.charAt(j2);
                if ((ch3 < 32 || ch3 > 126) && ((ch3 < 160 || ch3 > 55295) && ((ch3 < 63744 || ch3 > 64975) && (ch3 < 65008 || ch3 > 65519)))) {
                    if (XMLChar.isHighSurrogate(ch3)) {
                        j2++;
                        if (j2 < len) {
                            int ch22 = href.charAt(j2);
                            if (!XMLChar.isLowSurrogate(ch22) || (ch2 = XMLChar.supplemental((char) ch3, (char) ch22)) >= 983040 || (ch2 & 65535) > 65533) {
                            }
                        }
                    }
                    return href;
                }
                j2++;
            }
            try {
                byte[] bytes = href.substring(i2).getBytes("UTF-8");
                for (byte b2 : bytes) {
                    if (b2 < 0) {
                        int ch4 = b2 + 256;
                        buffer.append('%');
                        buffer.append(gHexChs[ch4 >> 4]);
                        buffer.append(gHexChs[ch4 & 15]);
                    } else if (gNeedEscaping[b2]) {
                        buffer.append('%');
                        buffer.append(gAfterEscaping1[b2]);
                        buffer.append(gAfterEscaping2[b2]);
                    } else {
                        buffer.append((char) b2);
                    }
                }
            } catch (UnsupportedEncodingException e2) {
                return href;
            }
        }
        if (buffer.length() != len) {
            return buffer.toString();
        }
        return href;
    }
}
