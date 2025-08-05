package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.impl.XMLScanner;
import com.sun.org.apache.xerces.internal.util.AugmentationsImpl;
import com.sun.org.apache.xerces.internal.util.XMLAttributesIteratorImpl;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.xml.internal.stream.XMLBufferListener;
import com.sun.xml.internal.stream.XMLEntityStorage;
import com.sun.xml.internal.stream.dtd.DTDGrammarUtil;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javax.xml.stream.XMLInputFactory;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentFragmentScannerImpl.class */
public class XMLDocumentFragmentScannerImpl extends XMLScanner implements XMLDocumentScanner, XMLComponent, XMLEntityHandler, XMLBufferListener {
    protected int fElementAttributeLimit;
    protected int fXMLNameLimit;
    protected ExternalSubsetResolver fExternalSubsetResolver;
    protected static final int SCANNER_STATE_START_OF_MARKUP = 21;
    protected static final int SCANNER_STATE_CONTENT = 22;
    protected static final int SCANNER_STATE_PI = 23;
    protected static final int SCANNER_STATE_DOCTYPE = 24;
    protected static final int SCANNER_STATE_XML_DECL = 25;
    protected static final int SCANNER_STATE_ROOT_ELEMENT = 26;
    protected static final int SCANNER_STATE_COMMENT = 27;
    protected static final int SCANNER_STATE_REFERENCE = 28;
    protected static final int SCANNER_STATE_ATTRIBUTE = 29;
    protected static final int SCANNER_STATE_ATTRIBUTE_VALUE = 30;
    protected static final int SCANNER_STATE_END_OF_INPUT = 33;
    protected static final int SCANNER_STATE_TERMINATED = 34;
    protected static final int SCANNER_STATE_CDATA = 35;
    protected static final int SCANNER_STATE_TEXT_DECL = 36;
    protected static final int SCANNER_STATE_CHARACTER_DATA = 37;
    protected static final int SCANNER_STATE_START_ELEMENT_TAG = 38;
    protected static final int SCANNER_STATE_END_ELEMENT_TAG = 39;
    protected static final int SCANNER_STATE_CHAR_REFERENCE = 40;
    protected static final int SCANNER_STATE_BUILT_IN_REFS = 41;
    protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected static final String STANDARD_URI_CONFORMANT = "http://apache.org/xml/features/standard-uri-conformant";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    static final String EXTERNAL_ACCESS_DEFAULT = "all";
    private static final boolean DEBUG_SCANNER_STATE = false;
    private static final boolean DEBUG_DISPATCHER = false;
    protected static final boolean DEBUG_START_END_ELEMENT = false;
    protected static final boolean DEBUG_NEXT = false;
    protected static final boolean DEBUG = false;
    protected static final boolean DEBUG_COALESCE = false;
    protected XMLDocumentHandler fDocumentHandler;
    protected int fScannerLastState;
    protected XMLEntityStorage fEntityStore;
    protected int fMarkupDepth;
    protected boolean fEmptyElement;
    protected int fScannerState;
    protected boolean fHasExternalDTD;
    protected boolean fStandaloneSet;
    protected boolean fStandalone;
    protected String fVersion;
    protected QName fCurrentElement;
    protected String fPITarget;
    protected boolean fStrictURI;
    protected Driver fDriver;
    static final short MAX_DEPTH_LIMIT = 5;
    static final short ELEMENT_ARRAY_LENGTH = 200;
    static final short MAX_POINTER_AT_A_DEPTH = 4;
    static final boolean DEBUG_SKIP_ALGORITHM = false;
    protected String fElementRawname;
    protected boolean fUsebuffer;
    protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
    private static final String[] RECOGNIZED_FEATURES = {"http://xml.org/sax/features/namespaces", "http://xml.org/sax/features/validation", NOTIFY_BUILTIN_REFS, "http://apache.org/xml/features/scanner/notify-char-refs", Constants.STAX_REPORT_CDATA_EVENT};
    private static final Boolean[] FEATURE_DEFAULTS = {Boolean.TRUE, null, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE};
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager", "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"};
    private static final Object[] PROPERTY_DEFAULTS = {null, null, null, null};
    private static final char[] cdata = {'[', 'C', 'D', 'A', 'T', 'A', '['};
    static final char[] xmlDecl = {'<', '?', 'x', 'm', 'l'};
    protected int[] fEntityStack = new int[4];
    protected boolean fReadingAttributes = false;
    protected boolean fInScanContent = false;
    protected boolean fLastSectionWasCData = false;
    protected boolean fLastSectionWasEntityReference = false;
    protected boolean fLastSectionWasCharacterData = false;
    protected ElementStack fElementStack = new ElementStack();
    protected ElementStack2 fElementStack2 = new ElementStack2();
    protected XMLString fPIData = new XMLString();
    protected boolean fNotifyBuiltInRefs = false;
    protected boolean fSupportDTD = true;
    protected boolean fReplaceEntityReferences = true;
    protected boolean fSupportExternalEntities = false;
    protected boolean fReportCdataEvent = false;
    protected boolean fIsCoalesce = false;
    protected String fDeclaredEncoding = null;
    protected boolean fDisallowDoctype = false;
    protected String fAccessExternalDTD = "all";
    protected Driver fContentDriver = createContentDriver();
    protected QName fElementQName = new QName();
    protected QName fAttributeQName = new QName();
    protected XMLAttributesIteratorImpl fAttributes = new XMLAttributesIteratorImpl();
    protected XMLString fTempString = new XMLString();
    protected XMLString fTempString2 = new XMLString();
    private String[] fStrings = new String[3];
    protected XMLStringBuffer fStringBuffer = new XMLStringBuffer();
    protected XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
    protected XMLStringBuffer fContentBuffer = new XMLStringBuffer();
    private final char[] fSingleChar = new char[1];
    private String fCurrentEntityName = null;
    protected boolean fScanToEnd = false;
    protected DTDGrammarUtil dtdGrammarUtil = null;
    protected boolean fAddDefaultAttr = false;
    protected boolean foundBuiltInRefs = false;
    String[] fElementArray = new String[200];
    short fLastPointerLocation = 0;
    short fElementPointer = 0;
    short[][] fPointerInfo = new short[5][4];
    protected boolean fShouldSkip = false;
    protected boolean fAdd = false;
    protected boolean fSkip = false;
    private Augmentations fTempAugmentations = null;

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentFragmentScannerImpl$Driver.class */
    protected interface Driver {
        int next() throws IOException, XNIException;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner
    public void setInputSource(XMLInputSource inputSource) throws IOException, XNIException {
        this.fEntityManager.setEntityHandler(this);
        this.fEntityManager.startEntity(false, "$fragment$", inputSource, false, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0148  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0154 A[RETURN] */
    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean scanDocument(boolean r6) throws java.io.IOException, com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 342
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanDocument(boolean):boolean");
    }

    public QName getElementQName() {
        if (this.fScannerLastState == 2) {
            this.fElementQName.setValues(this.fElementStack.getLastPoppedElement());
        }
        return this.fElementQName;
    }

    public int next() throws IOException, XNIException {
        return this.fDriver.next();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        super.reset(componentManager);
        this.fReportCdataEvent = componentManager.getFeature(Constants.STAX_REPORT_CDATA_EVENT, true);
        this.fSecurityManager = (XMLSecurityManager) componentManager.getProperty("http://apache.org/xml/properties/security-manager", null);
        this.fNotifyBuiltInRefs = componentManager.getFeature(NOTIFY_BUILTIN_REFS, false);
        Object resolver = componentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver", null);
        this.fExternalSubsetResolver = resolver instanceof ExternalSubsetResolver ? (ExternalSubsetResolver) resolver : null;
        this.fReadingAttributes = false;
        this.fSupportExternalEntities = true;
        this.fReplaceEntityReferences = true;
        this.fIsCoalesce = false;
        setScannerState(22);
        setDriver(this.fContentDriver);
        XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager) componentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", null);
        this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
        this.fStrictURI = componentManager.getFeature(STANDARD_URI_CONFORMANT, false);
        resetCommon();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    public void reset(PropertyManager propertyManager) {
        super.reset(propertyManager);
        this.fNamespaces = ((Boolean) propertyManager.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE)).booleanValue();
        this.fNotifyBuiltInRefs = false;
        Boolean bo2 = (Boolean) propertyManager.getProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES);
        this.fReplaceEntityReferences = bo2.booleanValue();
        Boolean bo3 = (Boolean) propertyManager.getProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES);
        this.fSupportExternalEntities = bo3.booleanValue();
        Boolean cdata2 = (Boolean) propertyManager.getProperty("http://java.sun.com/xml/stream/properties/report-cdata-event");
        if (cdata2 != null) {
            this.fReportCdataEvent = cdata2.booleanValue();
        }
        Boolean coalesce = (Boolean) propertyManager.getProperty(XMLInputFactory.IS_COALESCING);
        if (coalesce != null) {
            this.fIsCoalesce = coalesce.booleanValue();
        }
        boolean z2 = !this.fIsCoalesce && this.fReportCdataEvent;
        this.fReportCdataEvent = z2;
        this.fReplaceEntityReferences = this.fIsCoalesce ? true : this.fReplaceEntityReferences;
        XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager) propertyManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
        this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
        this.fSecurityManager = (XMLSecurityManager) propertyManager.getProperty("http://apache.org/xml/properties/security-manager");
        resetCommon();
    }

    void resetCommon() {
        this.fMarkupDepth = 0;
        this.fCurrentElement = null;
        this.fElementStack.clear();
        this.fHasExternalDTD = false;
        this.fStandaloneSet = false;
        this.fStandalone = false;
        this.fInScanContent = false;
        this.fShouldSkip = false;
        this.fAdd = false;
        this.fSkip = false;
        this.fEntityStore = this.fEntityManager.getEntityStore();
        this.dtdGrammarUtil = null;
        if (this.fSecurityManager != null) {
            this.fElementAttributeLimit = this.fSecurityManager.getLimit(XMLSecurityManager.Limit.ELEMENT_ATTRIBUTE_LIMIT);
            this.fXMLNameLimit = this.fSecurityManager.getLimit(XMLSecurityManager.Limit.MAX_NAME_LIMIT);
        } else {
            this.fElementAttributeLimit = 0;
            this.fXMLNameLimit = XMLSecurityManager.Limit.MAX_NAME_LIMIT.defaultValue();
        }
        this.fLimitAnalyzer = this.fEntityManager.fLimitAnalyzer;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) RECOGNIZED_FEATURES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        super.setFeature(featureId, state);
        if (featureId.startsWith(Constants.XERCES_FEATURE_PREFIX)) {
            String feature = featureId.substring(Constants.XERCES_FEATURE_PREFIX.length());
            if (feature.equals(Constants.NOTIFY_BUILTIN_REFS_FEATURE)) {
                this.fNotifyBuiltInRefs = state;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) RECOGNIZED_PROPERTIES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        super.setProperty(propertyId, value);
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
            if (suffixLength == Constants.ENTITY_MANAGER_PROPERTY.length() && propertyId.endsWith(Constants.ENTITY_MANAGER_PROPERTY)) {
                this.fEntityManager = (XMLEntityManager) value;
                return;
            } else if (suffixLength == Constants.ENTITY_RESOLVER_PROPERTY.length() && propertyId.endsWith(Constants.ENTITY_RESOLVER_PROPERTY)) {
                this.fExternalSubsetResolver = value instanceof ExternalSubsetResolver ? (ExternalSubsetResolver) value : null;
                return;
            }
        }
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            String property = propertyId.substring(Constants.XERCES_PROPERTY_PREFIX.length());
            if (property.equals(Constants.ENTITY_MANAGER_PROPERTY)) {
                this.fEntityManager = (XMLEntityManager) value;
                return;
            }
            return;
        }
        if (propertyId.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
            XMLSecurityPropertyManager spm = (XMLSecurityPropertyManager) value;
            this.fAccessExternalDTD = spm.getValue(XMLSecurityPropertyManager.Property.ACCESS_EXTERNAL_DTD);
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
    public void setDocumentHandler(XMLDocumentHandler documentHandler) {
        this.fDocumentHandler = documentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.impl.XMLEntityHandler
    public void startEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
        if (this.fEntityDepth == this.fEntityStack.length) {
            int[] entityarray = new int[this.fEntityStack.length * 2];
            System.arraycopy(this.fEntityStack, 0, entityarray, 0, this.fEntityStack.length);
            this.fEntityStack = entityarray;
        }
        this.fEntityStack[this.fEntityDepth] = this.fMarkupDepth;
        super.startEntity(name, identifier, encoding, augs);
        if (this.fStandalone && this.fEntityStore.isEntityDeclInExternalSubset(name)) {
            reportFatalError("MSG_REFERENCE_TO_EXTERNALLY_DECLARED_ENTITY_WHEN_STANDALONE", new Object[]{name});
        }
        if (this.fDocumentHandler != null && !this.fScanningAttribute && !name.equals("[xml]")) {
            this.fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.impl.XMLEntityHandler
    public void endEntity(String name, Augmentations augs) throws IOException, XNIException {
        super.endEntity(name, augs);
        if (this.fMarkupDepth != this.fEntityStack[this.fEntityDepth]) {
            reportFatalError("MarkupEntityMismatch", null);
        }
        if (this.fDocumentHandler != null && !this.fScanningAttribute && !name.equals("[xml]")) {
            this.fDocumentHandler.endGeneralEntity(name, augs);
        }
    }

    protected Driver createContentDriver() {
        return new FragmentContentDriver();
    }

    protected void scanXMLDeclOrTextDecl(boolean scanningTextDecl) throws IOException, XNIException {
        super.scanXMLDeclOrTextDecl(scanningTextDecl, this.fStrings);
        this.fMarkupDepth--;
        String version = this.fStrings[0];
        String encoding = this.fStrings[1];
        String standalone = this.fStrings[2];
        this.fDeclaredEncoding = encoding;
        this.fStandaloneSet = standalone != null;
        this.fStandalone = this.fStandaloneSet && standalone.equals("yes");
        this.fEntityManager.setStandalone(this.fStandalone);
        if (this.fDocumentHandler != null) {
            if (scanningTextDecl) {
                this.fDocumentHandler.textDecl(version, encoding, null);
            } else {
                this.fDocumentHandler.xmlDecl(version, encoding, standalone, null);
            }
        }
        if (version != null) {
            this.fEntityScanner.setVersion(version);
            this.fEntityScanner.setXMLVersion(version);
        }
        if (encoding != null && !this.fEntityScanner.getCurrentEntity().isEncodingExternallySpecified()) {
            this.fEntityScanner.setEncoding(encoding);
        }
    }

    public String getPITarget() {
        return this.fPITarget;
    }

    public XMLStringBuffer getPIData() {
        return this.fContentBuffer;
    }

    public XMLString getCharacterData() {
        if (this.fUsebuffer) {
            return this.fContentBuffer;
        }
        return this.fTempString;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    protected void scanPIData(String target, XMLStringBuffer data) throws IOException, XNIException {
        super.scanPIData(target, data);
        this.fPITarget = target;
        this.fMarkupDepth--;
    }

    protected void scanComment() throws IOException, XNIException {
        this.fContentBuffer.clear();
        scanComment(this.fContentBuffer);
        this.fUsebuffer = true;
        this.fMarkupDepth--;
    }

    public String getComment() {
        return this.fContentBuffer.toString();
    }

    void addElement(String rawname) {
        short column;
        if (this.fElementPointer < 200) {
            this.fElementArray[this.fElementPointer] = rawname;
            if (this.fElementStack.fDepth < 5 && (column = storePointerForADepth(this.fElementPointer)) > 0) {
                short pointer = getElementPointer((short) this.fElementStack.fDepth, (short) (column - 1));
                if (rawname == this.fElementArray[pointer]) {
                    this.fShouldSkip = true;
                    this.fLastPointerLocation = pointer;
                    resetPointer((short) this.fElementStack.fDepth, column);
                    this.fElementArray[this.fElementPointer] = null;
                    return;
                }
                this.fShouldSkip = false;
            }
            this.fElementPointer = (short) (this.fElementPointer + 1);
        }
    }

    void resetPointer(short depth, short column) {
        this.fPointerInfo[depth][column] = 0;
    }

    short storePointerForADepth(short elementPointer) {
        short depth = (short) this.fElementStack.fDepth;
        short s2 = 0;
        while (true) {
            short i2 = s2;
            if (i2 < 4) {
                if (!canStore(depth, i2)) {
                    s2 = (short) (i2 + 1);
                } else {
                    this.fPointerInfo[depth][i2] = elementPointer;
                    return i2;
                }
            } else {
                return (short) -1;
            }
        }
    }

    boolean canStore(short depth, short column) {
        return this.fPointerInfo[depth][column] == 0;
    }

    short getElementPointer(short depth, short column) {
        return this.fPointerInfo[depth][column];
    }

    boolean skipFromTheBuffer(String rawname) throws IOException {
        if (this.fEntityScanner.skipString(rawname)) {
            char c2 = (char) this.fEntityScanner.peekChar();
            if (c2 == ' ' || c2 == '/' || c2 == '>') {
                this.fElementRawname = rawname;
                return true;
            }
            return false;
        }
        return false;
    }

    boolean skipQElement(String rawname) throws IOException {
        int c2 = this.fEntityScanner.getChar(rawname.length());
        if (XMLChar.isName(c2)) {
            return false;
        }
        return this.fEntityScanner.skipString(rawname);
    }

    protected boolean skipElement() throws IOException {
        if (!this.fShouldSkip) {
            return false;
        }
        if (this.fLastPointerLocation != 0) {
            String rawname = this.fElementArray[this.fLastPointerLocation + 1];
            if (rawname != null && skipFromTheBuffer(rawname)) {
                this.fLastPointerLocation = (short) (this.fLastPointerLocation + 1);
                return true;
            }
            this.fLastPointerLocation = (short) 0;
        }
        return this.fShouldSkip && skipElement((short) 0);
    }

    boolean skipElement(short column) throws IOException {
        short depth = (short) this.fElementStack.fDepth;
        if (depth > 5) {
            this.fShouldSkip = false;
            return false;
        }
        short s2 = column;
        while (true) {
            short i2 = s2;
            if (i2 < 4) {
                short pointer = getElementPointer(depth, i2);
                if (pointer == 0) {
                    this.fShouldSkip = false;
                    return false;
                }
                if (this.fElementArray[pointer] == null || !skipFromTheBuffer(this.fElementArray[pointer])) {
                    s2 = (short) (i2 + 1);
                } else {
                    this.fLastPointerLocation = pointer;
                    this.fShouldSkip = true;
                    return true;
                }
            } else {
                this.fShouldSkip = false;
                return false;
            }
        }
    }

    protected boolean scanStartElement() throws IOException, XNIException {
        if (this.fSkip && !this.fAdd) {
            QName name = this.fElementStack.getNext();
            this.fSkip = this.fEntityScanner.skipString(name.rawname);
            if (this.fSkip) {
                this.fElementStack.push();
                this.fElementQName = name;
            } else {
                this.fElementStack.reposition();
            }
        }
        if (!this.fSkip || this.fAdd) {
            this.fElementQName = this.fElementStack.nextElement();
            if (this.fNamespaces) {
                this.fEntityScanner.scanQName(this.fElementQName, XMLScanner.NameType.ELEMENTSTART);
            } else {
                String name2 = this.fEntityScanner.scanName(XMLScanner.NameType.ELEMENTSTART);
                this.fElementQName.setValues(null, name2, name2, null);
            }
        }
        if (this.fAdd) {
            this.fElementStack.matchElement(this.fElementQName);
        }
        this.fCurrentElement = this.fElementQName;
        String rawname = this.fElementQName.rawname;
        this.fEmptyElement = false;
        this.fAttributes.removeAllAttributes();
        checkDepth(rawname);
        if (!seekCloseOfStartTag()) {
            this.fReadingAttributes = true;
            this.fAttributeCacheUsedCount = 0;
            this.fStringBufferIndex = 0;
            this.fAddDefaultAttr = true;
            do {
                scanAttribute(this.fAttributes);
                if (this.fSecurityManager != null && !this.fSecurityManager.isNoLimit(this.fElementAttributeLimit) && this.fAttributes.getLength() > this.fElementAttributeLimit) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ElementAttributeLimit", new Object[]{rawname, Integer.valueOf(this.fElementAttributeLimit)}, (short) 2);
                }
            } while (!seekCloseOfStartTag());
            this.fReadingAttributes = false;
        }
        if (this.fEmptyElement) {
            this.fMarkupDepth--;
            if (this.fMarkupDepth < this.fEntityStack[this.fEntityDepth - 1]) {
                reportFatalError("ElementEntityMismatch", new Object[]{this.fCurrentElement.rawname});
            }
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.emptyElement(this.fElementQName, this.fAttributes, null);
            }
            this.fElementStack.popElement();
        } else {
            if (this.dtdGrammarUtil != null) {
                this.dtdGrammarUtil.startElement(this.fElementQName, this.fAttributes);
            }
            if (this.fDocumentHandler != null) {
                this.fDocumentHandler.startElement(this.fElementQName, this.fAttributes, null);
            }
        }
        return this.fEmptyElement;
    }

    protected boolean seekCloseOfStartTag() throws IOException, XNIException {
        boolean sawSpace = this.fEntityScanner.skipSpaces();
        int c2 = this.fEntityScanner.peekChar();
        if (c2 == 62) {
            this.fEntityScanner.scanChar(null);
            return true;
        }
        if (c2 == 47) {
            this.fEntityScanner.scanChar(null);
            if (!this.fEntityScanner.skipChar(62, XMLScanner.NameType.ELEMENTEND)) {
                reportFatalError("ElementUnterminated", new Object[]{this.fElementQName.rawname});
            }
            this.fEmptyElement = true;
            return true;
        }
        if (!isValidNameStartChar(c2) || !sawSpace) {
            if (!isValidNameStartHighSurrogate(c2) || !sawSpace) {
                reportFatalError("ElementUnterminated", new Object[]{this.fElementQName.rawname});
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean hasAttributes() {
        return this.fAttributes.getLength() > 0;
    }

    public XMLAttributesIteratorImpl getAttributeIterator() throws XNIException {
        if (this.dtdGrammarUtil != null && this.fAddDefaultAttr) {
            this.dtdGrammarUtil.addDTDDefaultAttrs(this.fElementQName, this.fAttributes);
            this.fAddDefaultAttr = false;
        }
        return this.fAttributes;
    }

    public boolean standaloneSet() {
        return this.fStandaloneSet;
    }

    public boolean isStandAlone() {
        return this.fStandalone;
    }

    protected void scanAttribute(XMLAttributes attributes) throws IOException, XNIException {
        if (this.fNamespaces) {
            this.fEntityScanner.scanQName(this.fAttributeQName, XMLScanner.NameType.ATTRIBUTENAME);
        } else {
            String name = this.fEntityScanner.scanName(XMLScanner.NameType.ATTRIBUTENAME);
            this.fAttributeQName.setValues(null, name, name, null);
        }
        this.fEntityScanner.skipSpaces();
        if (!this.fEntityScanner.skipChar(61, XMLScanner.NameType.ATTRIBUTE)) {
            reportFatalError("EqRequiredInAttribute", new Object[]{this.fCurrentElement.rawname, this.fAttributeQName.rawname});
        }
        this.fEntityScanner.skipSpaces();
        boolean isVC = this.fHasExternalDTD && !this.fStandalone;
        XMLString tmpStr = getString();
        scanAttributeValue(tmpStr, this.fTempString2, this.fAttributeQName.rawname, attributes, 0, isVC, this.fCurrentElement.rawname, false);
        int oldLen = attributes.getLength();
        int attIndex = attributes.addAttribute(this.fAttributeQName, XMLSymbols.fCDATASymbol, null);
        if (oldLen == attributes.getLength()) {
            reportFatalError("AttributeNotUnique", new Object[]{this.fCurrentElement.rawname, this.fAttributeQName.rawname});
        }
        attributes.setValue(attIndex, null, tmpStr);
        attributes.setSpecified(attIndex, true);
    }

    protected int scanContent(XMLStringBuffer content) throws IOException, XNIException {
        this.fTempString.length = 0;
        int c2 = this.fEntityScanner.scanContent(this.fTempString);
        content.append(this.fTempString);
        this.fTempString.length = 0;
        if (c2 == 13) {
            this.fEntityScanner.scanChar(null);
            content.append((char) c2);
            c2 = -1;
        } else if (c2 == 93) {
            content.append((char) this.fEntityScanner.scanChar(null));
            this.fInScanContent = true;
            if (this.fEntityScanner.skipChar(93, null)) {
                content.append(']');
                while (this.fEntityScanner.skipChar(93, null)) {
                    content.append(']');
                }
                if (this.fEntityScanner.skipChar(62, null)) {
                    reportFatalError("CDEndInContent", null);
                }
            }
            this.fInScanContent = false;
            c2 = -1;
        }
        if (this.fDocumentHandler == null || content.length > 0) {
        }
        return c2;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0018  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean scanCDATASection(com.sun.org.apache.xerces.internal.util.XMLStringBuffer r9, boolean r10) throws java.io.IOException, com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            r8 = this;
            r0 = r8
            com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler r0 = r0.fDocumentHandler
            if (r0 == 0) goto L7
        L7:
            r0 = r8
            com.sun.org.apache.xerces.internal.impl.XMLEntityScanner r0 = r0.fEntityScanner
            java.lang.String r1 = "]]>"
            r2 = r9
            boolean r0 = r0.scanData(r1, r2)
            if (r0 != 0) goto L18
            goto L64
        L18:
            r0 = r8
            com.sun.org.apache.xerces.internal.impl.XMLEntityScanner r0 = r0.fEntityScanner
            int r0 = r0.peekChar()
            r11 = r0
            r0 = r11
            r1 = -1
            if (r0 == r1) goto L5a
            r0 = r8
            r1 = r11
            boolean r0 = r0.isInvalidLiteral(r1)
            if (r0 == 0) goto L5a
            r0 = r11
            boolean r0 = com.sun.org.apache.xerces.internal.util.XMLChar.isHighSurrogate(r0)
            if (r0 == 0) goto L3d
            r0 = r8
            r1 = r9
            boolean r0 = r0.scanSurrogates(r1)
            goto L5a
        L3d:
            r0 = r8
            java.lang.String r1 = "InvalidCharInCDSect"
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = r2
            r4 = 0
            r5 = r11
            r6 = 16
            java.lang.String r5 = java.lang.Integer.toString(r5, r6)
            r3[r4] = r5
            r0.reportFatalError(r1, r2)
            r0 = r8
            com.sun.org.apache.xerces.internal.impl.XMLEntityScanner r0 = r0.fEntityScanner
            r1 = 0
            int r0 = r0.scanChar(r1)
        L5a:
            r0 = r8
            com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler r0 = r0.fDocumentHandler
            if (r0 == 0) goto L61
        L61:
            goto L7
        L64:
            r0 = r8
            r1 = r0
            int r1 = r1.fMarkupDepth
            r2 = 1
            int r1 = r1 - r2
            r0.fMarkupDepth = r1
            r0 = r8
            com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler r0 = r0.fDocumentHandler
            if (r0 == 0) goto L7c
            r0 = r9
            int r0 = r0.length
            if (r0 <= 0) goto L7c
        L7c:
            r0 = r8
            com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler r0 = r0.fDocumentHandler
            if (r0 == 0) goto L83
        L83:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanCDATASection(com.sun.org.apache.xerces.internal.util.XMLStringBuffer, boolean):boolean");
    }

    protected int scanEndElement() throws IOException, XNIException {
        QName endElementName = this.fElementStack.popElement();
        String rawname = endElementName.rawname;
        if (!this.fEntityScanner.skipString(endElementName.rawname)) {
            reportFatalError("ETagRequired", new Object[]{rawname});
        }
        this.fEntityScanner.skipSpaces();
        if (!this.fEntityScanner.skipChar(62, XMLScanner.NameType.ELEMENTEND)) {
            reportFatalError("ETagUnterminated", new Object[]{rawname});
        }
        this.fMarkupDepth--;
        this.fMarkupDepth--;
        if (this.fMarkupDepth < this.fEntityStack[this.fEntityDepth - 1]) {
            reportFatalError("ElementEntityMismatch", new Object[]{rawname});
        }
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endElement(endElementName, null);
        }
        if (this.dtdGrammarUtil != null) {
            this.dtdGrammarUtil.endElement(endElementName);
        }
        return this.fMarkupDepth;
    }

    protected void scanCharReference() throws IOException, XNIException {
        this.fStringBuffer2.clear();
        int ch = scanCharReferenceValue(this.fStringBuffer2, null);
        this.fMarkupDepth--;
        if (ch != -1 && this.fDocumentHandler != null) {
            if (this.fNotifyCharRefs) {
                this.fDocumentHandler.startGeneralEntity(this.fCharRefLiteral, null, null, null);
            }
            if (this.fValidation && ch <= 32) {
                if (this.fTempAugmentations != null) {
                    this.fTempAugmentations.removeAllItems();
                } else {
                    this.fTempAugmentations = new AugmentationsImpl();
                }
                Augmentations augs = this.fTempAugmentations;
                augs.putItem(Constants.CHAR_REF_PROBABLE_WS, Boolean.TRUE);
            }
            if (this.fNotifyCharRefs) {
                this.fDocumentHandler.endGeneralEntity(this.fCharRefLiteral, null);
            }
        }
    }

    protected void scanEntityReference(XMLStringBuffer content) throws IOException, XNIException {
        String name = this.fEntityScanner.scanName(XMLScanner.NameType.REFERENCE);
        if (name == null) {
            reportFatalError("NameRequiredInReference", null);
            return;
        }
        if (!this.fEntityScanner.skipChar(59, XMLScanner.NameType.REFERENCE)) {
            reportFatalError("SemicolonRequiredInReference", new Object[]{name});
        }
        if (this.fEntityStore.isUnparsedEntity(name)) {
            reportFatalError("ReferenceToUnparsedEntity", new Object[]{name});
        }
        this.fMarkupDepth--;
        this.fCurrentEntityName = name;
        if (name == fAmpSymbol) {
            handleCharacter('&', fAmpSymbol, content);
            this.fScannerState = 41;
            return;
        }
        if (name == fLtSymbol) {
            handleCharacter('<', fLtSymbol, content);
            this.fScannerState = 41;
            return;
        }
        if (name == fGtSymbol) {
            handleCharacter('>', fGtSymbol, content);
            this.fScannerState = 41;
            return;
        }
        if (name == fQuotSymbol) {
            handleCharacter('\"', fQuotSymbol, content);
            this.fScannerState = 41;
            return;
        }
        if (name == fAposSymbol) {
            handleCharacter('\'', fAposSymbol, content);
            this.fScannerState = 41;
            return;
        }
        boolean isEE = this.fEntityStore.isExternalEntity(name);
        if ((isEE && !this.fSupportExternalEntities) || ((!isEE && !this.fReplaceEntityReferences) || this.foundBuiltInRefs)) {
            this.fScannerState = 28;
            return;
        }
        if (!this.fEntityStore.isDeclaredEntity(name)) {
            if (!this.fSupportDTD && this.fReplaceEntityReferences) {
                reportFatalError("EntityNotDeclared", new Object[]{name});
                return;
            } else if (this.fHasExternalDTD && !this.fStandalone) {
                if (this.fValidation) {
                    this.fErrorReporter.reportError((XMLLocator) this.fEntityScanner, "http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{name}, (short) 1);
                }
            } else {
                reportFatalError("EntityNotDeclared", new Object[]{name});
            }
        }
        this.fEntityManager.startEntity(true, name, false);
    }

    void checkDepth(String elementName) {
        this.fLimitAnalyzer.addValue(XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT, elementName, this.fElementStack.fDepth);
        if (this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT, this.fLimitAnalyzer)) {
            this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
            reportFatalError("MaxElementDepthLimit", new Object[]{elementName, Integer.valueOf(this.fLimitAnalyzer.getTotalValue(XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT)), Integer.valueOf(this.fSecurityManager.getLimit(XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT)), "maxElementDepth"});
        }
    }

    private void handleCharacter(char c2, String entity, XMLStringBuffer content) throws XNIException {
        this.foundBuiltInRefs = true;
        checkEntityLimit(false, this.fEntityScanner.fCurrentEntity.name, 1);
        content.append(c2);
        if (this.fDocumentHandler != null) {
            this.fSingleChar[0] = c2;
            if (this.fNotifyBuiltInRefs) {
                this.fDocumentHandler.startGeneralEntity(entity, null, null, null);
            }
            this.fTempString.setValues(this.fSingleChar, 0, 1);
            if (this.fNotifyBuiltInRefs) {
                this.fDocumentHandler.endGeneralEntity(entity, null);
            }
        }
    }

    protected final void setScannerState(int state) {
        this.fScannerState = state;
    }

    protected final void setDriver(Driver driver) {
        this.fDriver = driver;
    }

    protected String getScannerStateName(int state) {
        switch (state) {
            case 21:
                return "SCANNER_STATE_START_OF_MARKUP";
            case 22:
                return "SCANNER_STATE_CONTENT";
            case 23:
                return "SCANNER_STATE_PI";
            case 24:
                return "SCANNER_STATE_DOCTYPE";
            case 25:
            case 31:
            case 32:
            default:
                return "??? (" + state + ')';
            case 26:
                return "SCANNER_STATE_ROOT_ELEMENT";
            case 27:
                return "SCANNER_STATE_COMMENT";
            case 28:
                return "SCANNER_STATE_REFERENCE";
            case 29:
                return "SCANNER_STATE_ATTRIBUTE";
            case 30:
                return "SCANNER_STATE_ATTRIBUTE_VALUE";
            case 33:
                return "SCANNER_STATE_END_OF_INPUT";
            case 34:
                return "SCANNER_STATE_TERMINATED";
            case 35:
                return "SCANNER_STATE_CDATA";
            case 36:
                return "SCANNER_STATE_TEXT_DECL";
            case 37:
                return "SCANNER_STATE_CHARACTER_DATA";
            case 38:
                return "SCANNER_STATE_START_ELEMENT_TAG";
            case 39:
                return "SCANNER_STATE_END_ELEMENT_TAG";
        }
    }

    public String getEntityName() {
        return this.fCurrentEntityName;
    }

    public String getDriverName(Driver driver) {
        return FXMLLoader.NULL_KEYWORD;
    }

    String checkAccess(String systemId, String allowedProtocols) throws IOException {
        String baseSystemId = this.fEntityScanner.getBaseSystemId();
        String expandedSystemId = XMLEntityManager.expandSystemId(systemId, baseSystemId, this.fStrictURI);
        return SecuritySupport.checkAccess(expandedSystemId, allowedProtocols, "all");
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentFragmentScannerImpl$Element.class */
    protected static final class Element {
        public QName qname;
        public char[] fRawname;
        public Element next;

        public Element(QName qname, Element next) {
            this.qname.setValues(qname);
            this.fRawname = qname.rawname.toCharArray();
            this.next = next;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentFragmentScannerImpl$ElementStack2.class */
    protected class ElementStack2 {
        protected QName[] fQName = new QName[20];
        protected int fDepth;
        protected int fCount;
        protected int fPosition;
        protected int fMark;
        protected int fLastDepth;

        public ElementStack2() {
            for (int i2 = 0; i2 < this.fQName.length; i2++) {
                this.fQName[i2] = new QName();
            }
            this.fPosition = 1;
            this.fMark = 1;
        }

        public void resize() {
            int oldLength = this.fQName.length;
            QName[] tmp = new QName[oldLength * 2];
            System.arraycopy(this.fQName, 0, tmp, 0, oldLength);
            this.fQName = tmp;
            for (int i2 = oldLength; i2 < this.fQName.length; i2++) {
                this.fQName[i2] = new QName();
            }
        }

        public boolean matchElement(QName element) {
            boolean match = false;
            if (this.fLastDepth > this.fDepth && this.fDepth <= 2) {
                if (element.rawname == this.fQName[this.fDepth].rawname) {
                    XMLDocumentFragmentScannerImpl.this.fAdd = false;
                    this.fMark = this.fDepth - 1;
                    this.fPosition = this.fMark + 1;
                    match = true;
                    this.fCount--;
                } else {
                    XMLDocumentFragmentScannerImpl.this.fAdd = true;
                }
            }
            int i2 = this.fDepth;
            this.fDepth = i2 + 1;
            this.fLastDepth = i2;
            return match;
        }

        public QName nextElement() {
            if (this.fCount == this.fQName.length) {
                XMLDocumentFragmentScannerImpl.this.fShouldSkip = false;
                XMLDocumentFragmentScannerImpl.this.fAdd = false;
                QName[] qNameArr = this.fQName;
                int i2 = this.fCount - 1;
                this.fCount = i2;
                return qNameArr[i2];
            }
            QName[] qNameArr2 = this.fQName;
            int i3 = this.fCount;
            this.fCount = i3 + 1;
            return qNameArr2[i3];
        }

        public QName getNext() {
            if (this.fPosition == this.fCount) {
                this.fPosition = this.fMark;
            }
            QName[] qNameArr = this.fQName;
            int i2 = this.fPosition;
            this.fPosition = i2 + 1;
            return qNameArr[i2];
        }

        public int popElement() {
            int i2 = this.fDepth;
            this.fDepth = i2 - 1;
            return i2;
        }

        public void clear() {
            this.fLastDepth = 0;
            this.fDepth = 0;
            this.fCount = 0;
            this.fMark = 1;
            this.fPosition = 1;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentFragmentScannerImpl$ElementStack.class */
    protected class ElementStack {
        protected int fDepth;
        protected int fCount;
        protected int fPosition;
        protected int fMark;
        protected int fLastDepth;
        protected int[] fInt = new int[20];
        protected QName[] fElements = new QName[20];

        public ElementStack() {
            for (int i2 = 0; i2 < this.fElements.length; i2++) {
                this.fElements[i2] = new QName();
            }
        }

        public QName pushElement(QName element) {
            if (this.fDepth == this.fElements.length) {
                QName[] array = new QName[this.fElements.length * 2];
                System.arraycopy(this.fElements, 0, array, 0, this.fDepth);
                this.fElements = array;
                for (int i2 = this.fDepth; i2 < this.fElements.length; i2++) {
                    this.fElements[i2] = new QName();
                }
            }
            this.fElements[this.fDepth].setValues(element);
            QName[] qNameArr = this.fElements;
            int i3 = this.fDepth;
            this.fDepth = i3 + 1;
            return qNameArr[i3];
        }

        public QName getNext() {
            if (this.fPosition == this.fCount) {
                this.fPosition = this.fMark;
            }
            return this.fElements[this.fPosition];
        }

        public void push() {
            int[] iArr = this.fInt;
            int i2 = this.fDepth + 1;
            this.fDepth = i2;
            int i3 = this.fPosition;
            this.fPosition = i3 + 1;
            iArr[i2] = i3;
        }

        public boolean matchElement(QName element) {
            boolean match = false;
            if (this.fLastDepth > this.fDepth && this.fDepth <= 3) {
                if (element.rawname == this.fElements[this.fDepth - 1].rawname) {
                    XMLDocumentFragmentScannerImpl.this.fAdd = false;
                    this.fMark = this.fDepth - 1;
                    this.fPosition = this.fMark;
                    match = true;
                    this.fCount--;
                } else {
                    XMLDocumentFragmentScannerImpl.this.fAdd = true;
                }
            }
            if (match) {
                int[] iArr = this.fInt;
                int i2 = this.fDepth;
                int i3 = this.fPosition;
                this.fPosition = i3 + 1;
                iArr[i2] = i3;
            } else {
                this.fInt[this.fDepth] = this.fCount - 1;
            }
            if (this.fCount == this.fElements.length) {
                XMLDocumentFragmentScannerImpl.this.fSkip = false;
                XMLDocumentFragmentScannerImpl.this.fAdd = false;
                reposition();
                return false;
            }
            this.fLastDepth = this.fDepth;
            return match;
        }

        public QName nextElement() {
            if (XMLDocumentFragmentScannerImpl.this.fSkip) {
                this.fDepth++;
                QName[] qNameArr = this.fElements;
                int i2 = this.fCount;
                this.fCount = i2 + 1;
                return qNameArr[i2];
            }
            if (this.fDepth == this.fElements.length) {
                QName[] array = new QName[this.fElements.length * 2];
                System.arraycopy(this.fElements, 0, array, 0, this.fDepth);
                this.fElements = array;
                for (int i3 = this.fDepth; i3 < this.fElements.length; i3++) {
                    this.fElements[i3] = new QName();
                }
            }
            QName[] qNameArr2 = this.fElements;
            int i4 = this.fDepth;
            this.fDepth = i4 + 1;
            return qNameArr2[i4];
        }

        public QName popElement() {
            if (XMLDocumentFragmentScannerImpl.this.fSkip || XMLDocumentFragmentScannerImpl.this.fAdd) {
                QName[] qNameArr = this.fElements;
                int[] iArr = this.fInt;
                int i2 = this.fDepth;
                this.fDepth = i2 - 1;
                return qNameArr[iArr[i2]];
            }
            QName[] qNameArr2 = this.fElements;
            int i3 = this.fDepth - 1;
            this.fDepth = i3;
            return qNameArr2[i3];
        }

        public void reposition() {
            for (int i2 = 2; i2 <= this.fDepth; i2++) {
                this.fElements[i2 - 1] = this.fElements[this.fInt[i2]];
            }
        }

        public void clear() {
            this.fDepth = 0;
            this.fLastDepth = 0;
            this.fCount = 0;
            this.fMark = 1;
            this.fPosition = 1;
        }

        public QName getLastPoppedElement() {
            return this.fElements[this.fDepth];
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentFragmentScannerImpl$FragmentContentDriver.class */
    protected class FragmentContentDriver implements Driver {
        protected FragmentContentDriver() {
        }

        private void startOfMarkup() throws IOException {
            XMLDocumentFragmentScannerImpl.this.fMarkupDepth++;
            int ch = XMLDocumentFragmentScannerImpl.this.fEntityScanner.peekChar();
            if (XMLDocumentFragmentScannerImpl.this.isValidNameStartChar(ch) || XMLDocumentFragmentScannerImpl.this.isValidNameStartHighSurrogate(ch)) {
                XMLDocumentFragmentScannerImpl.this.setScannerState(38);
                return;
            }
            switch (ch) {
                case 33:
                    XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(ch, null);
                    if (!XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(45, null)) {
                        if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipString(XMLDocumentFragmentScannerImpl.cdata)) {
                            XMLDocumentFragmentScannerImpl.this.setScannerState(35);
                            break;
                        } else if (!scanForDoctypeHook()) {
                            XMLDocumentFragmentScannerImpl.this.reportFatalError("MarkupNotRecognizedInContent", null);
                            break;
                        }
                    } else {
                        if (!XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(45, XMLScanner.NameType.COMMENT)) {
                            XMLDocumentFragmentScannerImpl.this.reportFatalError("InvalidCommentStart", null);
                        }
                        XMLDocumentFragmentScannerImpl.this.setScannerState(27);
                        break;
                    }
                    break;
                case 47:
                    XMLDocumentFragmentScannerImpl.this.setScannerState(39);
                    XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(ch, XMLScanner.NameType.ELEMENTEND);
                    break;
                case 63:
                    XMLDocumentFragmentScannerImpl.this.setScannerState(23);
                    XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(ch, null);
                    break;
                default:
                    XMLDocumentFragmentScannerImpl.this.reportFatalError("MarkupNotRecognizedInContent", null);
                    break;
            }
        }

        private void startOfContent() throws IOException {
            if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(60, null)) {
                XMLDocumentFragmentScannerImpl.this.setScannerState(21);
            } else if (XMLDocumentFragmentScannerImpl.this.fEntityScanner.skipChar(38, XMLScanner.NameType.REFERENCE)) {
                XMLDocumentFragmentScannerImpl.this.setScannerState(28);
            } else {
                XMLDocumentFragmentScannerImpl.this.setScannerState(37);
            }
        }

        public void decideSubState() throws IOException {
            while (true) {
                if (XMLDocumentFragmentScannerImpl.this.fScannerState == 22 || XMLDocumentFragmentScannerImpl.this.fScannerState == 21) {
                    switch (XMLDocumentFragmentScannerImpl.this.fScannerState) {
                        case 21:
                            startOfMarkup();
                            break;
                        case 22:
                            startOfContent();
                            break;
                    }
                } else {
                    return;
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:282:?, code lost:
        
            return 4;
         */
        /* JADX WARN: Code restructure failed: missing block: B:76:0x0337, code lost:
        
            if (r8.this$0.fUsebuffer == false) goto L78;
         */
        /* JADX WARN: Code restructure failed: missing block: B:77:0x033a, code lost:
        
            r8.this$0.fContentBuffer.append(r8.this$0.fTempString);
            r8.this$0.fTempString.length = 0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:79:0x035d, code lost:
        
            if (r8.this$0.dtdGrammarUtil == null) goto L84;
         */
        /* JADX WARN: Code restructure failed: missing block: B:81:0x0371, code lost:
        
            if (r8.this$0.dtdGrammarUtil.isIgnorableWhiteSpace(r8.this$0.fContentBuffer) == false) goto L282;
         */
        /* JADX WARN: Code restructure failed: missing block: B:82:0x0374, code lost:
        
            return 6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:84:0x0377, code lost:
        
            return 4;
         */
        @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.Driver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int next() throws java.io.IOException, com.sun.org.apache.xerces.internal.xni.XNIException {
            /*
                Method dump skipped, instructions count: 2346
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.FragmentContentDriver.next():int");
        }

        protected boolean scanForDoctypeHook() throws IOException, XNIException {
            return false;
        }

        protected boolean elementDepthIsZeroHook() throws IOException, XNIException {
            return false;
        }

        protected boolean scanRootElementHook() throws IOException, XNIException {
            return false;
        }

        protected void endOfFileHook(EOFException e2) throws IOException, XNIException {
            if (XMLDocumentFragmentScannerImpl.this.fMarkupDepth != 0) {
                XMLDocumentFragmentScannerImpl.this.reportFatalError("PrematureEOF", null);
            }
        }
    }

    static void pr(String str) {
        System.out.println(str);
    }

    protected XMLString getString() {
        if (this.fAttributeCacheUsedCount < this.initialCacheCount || this.fAttributeCacheUsedCount < this.attributeValueCache.size()) {
            ArrayList<XMLString> arrayList = this.attributeValueCache;
            int i2 = this.fAttributeCacheUsedCount;
            this.fAttributeCacheUsedCount = i2 + 1;
            return arrayList.get(i2);
        }
        XMLString str = new XMLString();
        this.fAttributeCacheUsedCount++;
        this.attributeValueCache.add(str);
        return str;
    }

    @Override // com.sun.xml.internal.stream.XMLBufferListener
    public void refresh() {
        refresh(0);
    }

    @Override // com.sun.xml.internal.stream.XMLBufferListener
    public void refresh(int refreshPosition) {
        if (this.fReadingAttributes) {
            this.fAttributes.refresh();
        }
        if (this.fScannerState == 37) {
            this.fContentBuffer.append(this.fTempString);
            this.fTempString.length = 0;
            this.fUsebuffer = true;
        }
    }
}
