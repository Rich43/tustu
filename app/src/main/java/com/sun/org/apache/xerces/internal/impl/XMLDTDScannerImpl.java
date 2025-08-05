package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.impl.XMLScanner;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.xml.internal.stream.dtd.nonvalidating.DTDGrammar;
import java.io.EOFException;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDTDScannerImpl.class */
public class XMLDTDScannerImpl extends XMLScanner implements XMLDTDScanner, XMLComponent, XMLEntityHandler {
    protected static final int SCANNER_STATE_END_OF_INPUT = 0;
    protected static final int SCANNER_STATE_TEXT_DECL = 1;
    protected static final int SCANNER_STATE_MARKUP_DECL = 2;
    private static final String[] RECOGNIZED_FEATURES = {"http://xml.org/sax/features/validation", "http://apache.org/xml/features/scanner/notify-char-refs"};
    private static final Boolean[] FEATURE_DEFAULTS = {null, Boolean.FALSE};
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/entity-manager"};
    private static final Object[] PROPERTY_DEFAULTS = {null, null, null};
    private static final boolean DEBUG_SCANNER_STATE = false;
    public XMLDTDHandler fDTDHandler;
    protected XMLDTDContentModelHandler fDTDContentModelHandler;
    protected int fScannerState;
    protected boolean fStandalone;
    protected boolean fSeenExternalDTD;
    protected boolean fSeenExternalPE;
    private boolean fStartDTDCalled;
    private XMLAttributesImpl fAttributes;
    private int[] fContentStack;
    private int fContentDepth;
    private int[] fPEStack;
    private boolean[] fPEReport;
    private int fPEDepth;
    private int fMarkUpDepth;
    private int fExtEntityDepth;
    private int fIncludeSectDepth;
    private String[] fStrings;
    private XMLString fString;
    private XMLStringBuffer fStringBuffer;
    private XMLStringBuffer fStringBuffer2;
    private XMLString fLiteral;
    private XMLString fLiteral2;
    private String[] fEnumeration;
    private int fEnumerationCount;
    private XMLStringBuffer fIgnoreConditionalBuffer;
    DTDGrammar nvGrammarInfo;
    boolean nonValidatingMode;

    public XMLDTDScannerImpl() {
        this.fDTDHandler = null;
        this.fAttributes = new XMLAttributesImpl();
        this.fContentStack = new int[5];
        this.fPEStack = new int[5];
        this.fPEReport = new boolean[5];
        this.fStrings = new String[3];
        this.fString = new XMLString();
        this.fStringBuffer = new XMLStringBuffer();
        this.fStringBuffer2 = new XMLStringBuffer();
        this.fLiteral = new XMLString();
        this.fLiteral2 = new XMLString();
        this.fEnumeration = new String[5];
        this.fIgnoreConditionalBuffer = new XMLStringBuffer(128);
        this.nvGrammarInfo = null;
        this.nonValidatingMode = false;
    }

    public XMLDTDScannerImpl(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityManager) {
        this.fDTDHandler = null;
        this.fAttributes = new XMLAttributesImpl();
        this.fContentStack = new int[5];
        this.fPEStack = new int[5];
        this.fPEReport = new boolean[5];
        this.fStrings = new String[3];
        this.fString = new XMLString();
        this.fStringBuffer = new XMLStringBuffer();
        this.fStringBuffer2 = new XMLStringBuffer();
        this.fLiteral = new XMLString();
        this.fLiteral2 = new XMLString();
        this.fEnumeration = new String[5];
        this.fIgnoreConditionalBuffer = new XMLStringBuffer(128);
        this.nvGrammarInfo = null;
        this.nonValidatingMode = false;
        this.fSymbolTable = symbolTable;
        this.fErrorReporter = errorReporter;
        this.fEntityManager = entityManager;
        entityManager.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner
    public void setInputSource(XMLInputSource inputSource) throws IOException, XNIException {
        if (inputSource == null) {
            if (this.fDTDHandler != null) {
                this.fDTDHandler.startDTD(null, null);
                this.fDTDHandler.endDTD(null);
            }
            if (this.nonValidatingMode) {
                this.nvGrammarInfo.startDTD(null, null);
                this.nvGrammarInfo.endDTD(null);
                return;
            }
            return;
        }
        this.fEntityManager.setEntityHandler(this);
        this.fEntityManager.startDTDEntity(inputSource);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner
    public void setLimitAnalyzer(XMLLimitAnalyzer limitAnalyzer) {
        this.fLimitAnalyzer = limitAnalyzer;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner
    public boolean scanDTDExternalSubset(boolean complete) throws IOException, XNIException {
        this.fEntityManager.setEntityHandler(this);
        if (this.fScannerState == 1) {
            this.fSeenExternalDTD = true;
            boolean textDecl = scanTextDecl();
            if (this.fScannerState == 0) {
                return false;
            }
            setScannerState(2);
            if (textDecl && !complete) {
                return true;
            }
        }
        while (scanDecls(complete)) {
            if (!complete) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner
    public boolean scanDTDInternalSubset(boolean complete, boolean standalone, boolean hasExternalSubset) throws IOException, XNIException {
        this.fEntityScanner = this.fEntityManager.getEntityScanner();
        this.fEntityManager.setEntityHandler(this);
        this.fStandalone = standalone;
        if (this.fScannerState == 1) {
            if (this.fDTDHandler != null) {
                this.fDTDHandler.startDTD(this.fEntityScanner, null);
                this.fStartDTDCalled = true;
            }
            if (this.nonValidatingMode) {
                this.fStartDTDCalled = true;
                this.nvGrammarInfo.startDTD(this.fEntityScanner, null);
            }
            setScannerState(2);
        }
        while (scanDecls(complete)) {
            if (!complete) {
                return true;
            }
        }
        if (this.fDTDHandler != null && !hasExternalSubset) {
            this.fDTDHandler.endDTD(null);
        }
        if (this.nonValidatingMode && !hasExternalSubset) {
            this.nvGrammarInfo.endDTD(null);
        }
        setScannerState(1);
        this.fLimitAnalyzer.reset(XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT);
        this.fLimitAnalyzer.reset(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT);
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner
    public boolean skipDTD(boolean supportDTD) throws IOException {
        if (supportDTD) {
            return false;
        }
        this.fStringBuffer.clear();
        while (this.fEntityScanner.scanData("]", this.fStringBuffer)) {
            int c2 = this.fEntityScanner.peekChar();
            if (c2 != -1) {
                if (XMLChar.isHighSurrogate(c2)) {
                    scanSurrogates(this.fStringBuffer);
                }
                if (isInvalidLiteral(c2)) {
                    reportFatalError("InvalidCharInDTD", new Object[]{Integer.toHexString(c2)});
                    this.fEntityScanner.scanChar(null);
                }
            }
        }
        this.fEntityScanner.fCurrentEntity.position--;
        return true;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        super.reset(componentManager);
        init();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    public void reset() {
        super.reset();
        init();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner
    public void reset(PropertyManager props) {
        setPropertyManager(props);
        super.reset(props);
        init();
        this.nonValidatingMode = true;
        this.nvGrammarInfo = new DTDGrammar(this.fSymbolTable);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return (String[]) RECOGNIZED_FEATURES.clone();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return (String[]) RECOGNIZED_PROPERTIES.clone();
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

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource
    public void setDTDHandler(XMLDTDHandler dtdHandler) {
        this.fDTDHandler = dtdHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource
    public XMLDTDHandler getDTDHandler() {
        return this.fDTDHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource
    public void setDTDContentModelHandler(XMLDTDContentModelHandler dtdContentModelHandler) {
        this.fDTDContentModelHandler = dtdContentModelHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource
    public XMLDTDContentModelHandler getDTDContentModelHandler() {
        return this.fDTDContentModelHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.impl.XMLEntityHandler
    public void startEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
        super.startEntity(name, identifier, encoding, augs);
        boolean dtdEntity = name.equals("[dtd]");
        if (dtdEntity) {
            if (this.fDTDHandler != null && !this.fStartDTDCalled) {
                this.fDTDHandler.startDTD(this.fEntityScanner, null);
            }
            if (this.fDTDHandler != null) {
                this.fDTDHandler.startExternalSubset(identifier, null);
            }
            this.fEntityManager.startExternalSubset();
            this.fEntityStore.startExternalSubset();
            this.fExtEntityDepth++;
        } else if (name.charAt(0) == '%') {
            pushPEStack(this.fMarkUpDepth, this.fReportEntity);
            if (this.fEntityScanner.isExternal()) {
                this.fExtEntityDepth++;
            }
        }
        if (this.fDTDHandler != null && !dtdEntity && this.fReportEntity) {
            this.fDTDHandler.startParameterEntity(name, identifier, encoding, null);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.impl.XMLEntityHandler
    public void endEntity(String name, Augmentations augs) throws IOException, XNIException {
        super.endEntity(name, augs);
        if (this.fScannerState == 0) {
            return;
        }
        boolean dtdEntity = name.equals("[dtd]");
        boolean z2 = this.fReportEntity;
        if (name.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX)) {
            boolean reportEntity = peekReportEntity();
            int startMarkUpDepth = popPEStack();
            if (startMarkUpDepth == 0 && startMarkUpDepth < this.fMarkUpDepth) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ILL_FORMED_PARAMETER_ENTITY_WHEN_USED_IN_DECL", new Object[]{this.fEntityManager.fCurrentEntity.name}, (short) 2);
            }
            if (startMarkUpDepth != this.fMarkUpDepth) {
                reportEntity = false;
                if (this.fValidation) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "ImproperDeclarationNesting", new Object[]{name}, (short) 1);
                }
            }
            if (this.fEntityScanner.isExternal()) {
                this.fExtEntityDepth--;
            }
            if (this.fDTDHandler != null && reportEntity) {
                this.fDTDHandler.endParameterEntity(name, null);
            }
        }
        if (dtdEntity) {
            if (this.fIncludeSectDepth != 0) {
                reportFatalError("IncludeSectUnterminated", null);
            }
            this.fScannerState = 0;
            this.fEntityManager.endExternalSubset();
            this.fEntityStore.endExternalSubset();
            if (this.fDTDHandler != null) {
                this.fDTDHandler.endExternalSubset(null);
                this.fDTDHandler.endDTD(null);
            }
            this.fExtEntityDepth--;
        }
        if (augs == null || !Boolean.TRUE.equals(augs.getItem(Constants.LAST_ENTITY))) {
            return;
        }
        if (this.fMarkUpDepth != 0 || this.fExtEntityDepth != 0 || this.fIncludeSectDepth != 0) {
            throw new EOFException();
        }
    }

    protected final void setScannerState(int state) {
        this.fScannerState = state;
    }

    private static String getScannerStateName(int state) {
        return "??? (" + state + ')';
    }

    protected final boolean scanningInternalSubset() {
        return this.fExtEntityDepth == 0;
    }

    protected void startPE(String name, boolean literal) throws IOException, XNIException {
        int depth = this.fPEDepth;
        String pName = FXMLLoader.RESOURCE_KEY_PREFIX + name;
        if (this.fValidation && !this.fEntityStore.isDeclaredEntity(pName)) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{name}, (short) 1);
        }
        this.fEntityManager.startEntity(false, this.fSymbolTable.addSymbol(pName), literal);
        if (depth != this.fPEDepth && this.fEntityScanner.isExternal()) {
            scanTextDecl();
        }
    }

    protected final boolean scanTextDecl() throws IOException, XNIException {
        boolean textDecl = false;
        if (this.fEntityScanner.skipString("<?xml")) {
            this.fMarkUpDepth++;
            if (isValidNameChar(this.fEntityScanner.peekChar())) {
                this.fStringBuffer.clear();
                this.fStringBuffer.append("xml");
                while (isValidNameChar(this.fEntityScanner.peekChar())) {
                    this.fStringBuffer.append((char) this.fEntityScanner.scanChar(null));
                }
                String target = this.fSymbolTable.addSymbol(this.fStringBuffer.ch, this.fStringBuffer.offset, this.fStringBuffer.length);
                scanPIData(target, this.fString);
            } else {
                scanXMLDeclOrTextDecl(true, this.fStrings);
                textDecl = true;
                this.fMarkUpDepth--;
                String version = this.fStrings[0];
                String encoding = this.fStrings[1];
                this.fEntityScanner.setEncoding(encoding);
                if (this.fDTDHandler != null) {
                    this.fDTDHandler.textDecl(version, encoding, null);
                }
            }
        }
        this.fEntityManager.fCurrentEntity.mayReadChunks = true;
        return textDecl;
    }

    protected final void scanPIData(String target, XMLString data) throws IOException, XNIException {
        this.fMarkUpDepth--;
        if (this.fDTDHandler != null) {
            this.fDTDHandler.processingInstruction(target, data, null);
        }
    }

    protected final void scanComment() throws IOException, XNIException {
        this.fReportEntity = false;
        scanComment(this.fStringBuffer);
        this.fMarkUpDepth--;
        if (this.fDTDHandler != null) {
            this.fDTDHandler.comment(this.fStringBuffer, null);
        }
        this.fReportEntity = true;
    }

    protected final void scanElementDecl() throws IOException, XNIException {
        String contentModel;
        this.fReportEntity = false;
        if (!skipSeparator(true, !scanningInternalSubset())) {
            reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ELEMENTDECL", null);
        }
        String name = this.fEntityScanner.scanName(XMLScanner.NameType.ELEMENTSTART);
        if (name == null) {
            reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_ELEMENTDECL", null);
        }
        if (!skipSeparator(true, !scanningInternalSubset())) {
            reportFatalError("MSG_SPACE_REQUIRED_BEFORE_CONTENTSPEC_IN_ELEMENTDECL", new Object[]{name});
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.startContentModel(name, null);
        }
        this.fReportEntity = true;
        if (this.fEntityScanner.skipString("EMPTY")) {
            contentModel = "EMPTY";
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.empty(null);
            }
        } else if (this.fEntityScanner.skipString("ANY")) {
            contentModel = "ANY";
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.any(null);
            }
        } else {
            if (!this.fEntityScanner.skipChar(40, null)) {
                reportFatalError("MSG_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED_IN_CHILDREN", new Object[]{name});
            }
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.startGroup(null);
            }
            this.fStringBuffer.clear();
            this.fStringBuffer.append('(');
            this.fMarkUpDepth++;
            skipSeparator(false, !scanningInternalSubset());
            if (this.fEntityScanner.skipString("#PCDATA")) {
                scanMixed(name);
            } else {
                scanChildren(name);
            }
            contentModel = this.fStringBuffer.toString();
        }
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.endContentModel(null);
        }
        this.fReportEntity = false;
        skipSeparator(false, !scanningInternalSubset());
        if (!this.fEntityScanner.skipChar(62, null)) {
            reportFatalError("ElementDeclUnterminated", new Object[]{name});
        }
        this.fReportEntity = true;
        this.fMarkUpDepth--;
        if (this.fDTDHandler != null) {
            this.fDTDHandler.elementDecl(name, contentModel, null);
        }
        if (this.nonValidatingMode) {
            this.nvGrammarInfo.elementDecl(name, contentModel, null);
        }
    }

    private final void scanMixed(String elName) throws IOException, XNIException {
        String childName = null;
        this.fStringBuffer.append("#PCDATA");
        if (this.fDTDContentModelHandler != null) {
            this.fDTDContentModelHandler.pcdata(null);
        }
        skipSeparator(false, !scanningInternalSubset());
        while (this.fEntityScanner.skipChar(124, null)) {
            this.fStringBuffer.append('|');
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.separator((short) 0, null);
            }
            skipSeparator(false, !scanningInternalSubset());
            childName = this.fEntityScanner.scanName(XMLScanner.NameType.ENTITY);
            if (childName == null) {
                reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_MIXED_CONTENT", new Object[]{elName});
            }
            this.fStringBuffer.append(childName);
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.element(childName, null);
            }
            skipSeparator(false, !scanningInternalSubset());
        }
        if (this.fEntityScanner.skipString(")*")) {
            this.fStringBuffer.append(")*");
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.endGroup(null);
                this.fDTDContentModelHandler.occurrence((short) 3, null);
            }
        } else if (childName != null) {
            reportFatalError("MixedContentUnterminated", new Object[]{elName});
        } else if (this.fEntityScanner.skipChar(41, null)) {
            this.fStringBuffer.append(')');
            if (this.fDTDContentModelHandler != null) {
                this.fDTDContentModelHandler.endGroup(null);
            }
        } else {
            reportFatalError("MSG_CLOSE_PAREN_REQUIRED_IN_CHILDREN", new Object[]{elName});
        }
        this.fMarkUpDepth--;
    }

    private final void scanChildren(String elName) throws IOException, XNIException {
        short oc;
        this.fContentDepth = 0;
        pushContentStack(0);
        int currentOp = 0;
        while (true) {
            if (this.fEntityScanner.skipChar(40, null)) {
                this.fMarkUpDepth++;
                this.fStringBuffer.append('(');
                if (this.fDTDContentModelHandler != null) {
                    this.fDTDContentModelHandler.startGroup(null);
                }
                pushContentStack(currentOp);
                currentOp = 0;
                skipSeparator(false, !scanningInternalSubset());
            } else {
                skipSeparator(false, !scanningInternalSubset());
                String childName = this.fEntityScanner.scanName(XMLScanner.NameType.ELEMENTSTART);
                if (childName == null) {
                    reportFatalError("MSG_OPEN_PAREN_OR_ELEMENT_TYPE_REQUIRED_IN_CHILDREN", new Object[]{elName});
                    return;
                }
                if (this.fDTDContentModelHandler != null) {
                    this.fDTDContentModelHandler.element(childName, null);
                }
                this.fStringBuffer.append(childName);
                int c2 = this.fEntityScanner.peekChar();
                if (c2 == 63 || c2 == 42 || c2 == 43) {
                    if (this.fDTDContentModelHandler != null) {
                        if (c2 == 63) {
                            oc = 2;
                        } else if (c2 == 42) {
                            oc = 3;
                        } else {
                            oc = 4;
                        }
                        this.fDTDContentModelHandler.occurrence(oc, null);
                    }
                    this.fEntityScanner.scanChar(null);
                    this.fStringBuffer.append((char) c2);
                }
                do {
                    skipSeparator(false, !scanningInternalSubset());
                    int c3 = this.fEntityScanner.peekChar();
                    if (c3 == 44 && currentOp != 124) {
                        currentOp = c3;
                        if (this.fDTDContentModelHandler != null) {
                            this.fDTDContentModelHandler.separator((short) 1, null);
                        }
                        this.fEntityScanner.scanChar(null);
                        this.fStringBuffer.append(',');
                    } else if (c3 == 124 && currentOp != 44) {
                        currentOp = c3;
                        if (this.fDTDContentModelHandler != null) {
                            this.fDTDContentModelHandler.separator((short) 0, null);
                        }
                        this.fEntityScanner.scanChar(null);
                        this.fStringBuffer.append('|');
                    } else {
                        if (c3 != 41) {
                            reportFatalError("MSG_CLOSE_PAREN_REQUIRED_IN_CHILDREN", new Object[]{elName});
                        }
                        if (this.fDTDContentModelHandler != null) {
                            this.fDTDContentModelHandler.endGroup(null);
                        }
                        currentOp = popContentStack();
                        if (this.fEntityScanner.skipString(")?")) {
                            this.fStringBuffer.append(")?");
                            if (this.fDTDContentModelHandler != null) {
                                this.fDTDContentModelHandler.occurrence((short) 2, null);
                            }
                        } else if (this.fEntityScanner.skipString(")+")) {
                            this.fStringBuffer.append(")+");
                            if (this.fDTDContentModelHandler != null) {
                                this.fDTDContentModelHandler.occurrence((short) 4, null);
                            }
                        } else if (this.fEntityScanner.skipString(")*")) {
                            this.fStringBuffer.append(")*");
                            if (this.fDTDContentModelHandler != null) {
                                this.fDTDContentModelHandler.occurrence((short) 3, null);
                            }
                        } else {
                            this.fEntityScanner.scanChar(null);
                            this.fStringBuffer.append(')');
                        }
                        this.fMarkUpDepth--;
                    }
                    skipSeparator(false, !scanningInternalSubset());
                } while (this.fContentDepth != 0);
                return;
            }
        }
    }

    protected final void scanAttlistDecl() throws IOException, XNIException {
        this.fReportEntity = false;
        if (!skipSeparator(true, !scanningInternalSubset())) {
            reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ELEMENT_TYPE_IN_ATTLISTDECL", null);
        }
        String elName = this.fEntityScanner.scanName(XMLScanner.NameType.ELEMENTSTART);
        if (elName == null) {
            reportFatalError("MSG_ELEMENT_TYPE_REQUIRED_IN_ATTLISTDECL", null);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.startAttlist(elName, null);
        }
        if (!skipSeparator(true, !scanningInternalSubset())) {
            if (this.fEntityScanner.skipChar(62, null)) {
                if (this.fDTDHandler != null) {
                    this.fDTDHandler.endAttlist(null);
                }
                this.fMarkUpDepth--;
                return;
            }
            reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ATTRIBUTE_NAME_IN_ATTDEF", new Object[]{elName});
        }
        while (!this.fEntityScanner.skipChar(62, null)) {
            String name = this.fEntityScanner.scanName(XMLScanner.NameType.ATTRIBUTENAME);
            if (name == null) {
                reportFatalError("AttNameRequiredInAttDef", new Object[]{elName});
            }
            if (!skipSeparator(true, !scanningInternalSubset())) {
                reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ATTTYPE_IN_ATTDEF", new Object[]{elName, name});
            }
            String type = scanAttType(elName, name);
            if (!skipSeparator(true, !scanningInternalSubset())) {
                reportFatalError("MSG_SPACE_REQUIRED_BEFORE_DEFAULTDECL_IN_ATTDEF", new Object[]{elName, name});
            }
            String defaultType = scanAttDefaultDecl(elName, name, type, this.fLiteral, this.fLiteral2);
            String[] enumr = null;
            if ((this.fDTDHandler != null || this.nonValidatingMode) && this.fEnumerationCount != 0) {
                enumr = new String[this.fEnumerationCount];
                System.arraycopy(this.fEnumeration, 0, enumr, 0, this.fEnumerationCount);
            }
            if (defaultType != null && (defaultType.equals("#REQUIRED") || defaultType.equals("#IMPLIED"))) {
                if (this.fDTDHandler != null) {
                    this.fDTDHandler.attributeDecl(elName, name, type, enumr, defaultType, null, null, null);
                }
                if (this.nonValidatingMode) {
                    this.nvGrammarInfo.attributeDecl(elName, name, type, enumr, defaultType, null, null, null);
                }
            } else {
                if (this.fDTDHandler != null) {
                    this.fDTDHandler.attributeDecl(elName, name, type, enumr, defaultType, this.fLiteral, this.fLiteral2, null);
                }
                if (this.nonValidatingMode) {
                    this.nvGrammarInfo.attributeDecl(elName, name, type, enumr, defaultType, this.fLiteral, this.fLiteral2, null);
                }
            }
            skipSeparator(false, !scanningInternalSubset());
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.endAttlist(null);
        }
        this.fMarkUpDepth--;
        this.fReportEntity = true;
    }

    private final String scanAttType(String elName, String atName) throws IOException, XNIException {
        String type;
        int c2;
        int c3;
        this.fEnumerationCount = 0;
        if (this.fEntityScanner.skipString("CDATA")) {
            type = "CDATA";
        } else if (this.fEntityScanner.skipString(SchemaSymbols.ATTVAL_IDREFS)) {
            type = SchemaSymbols.ATTVAL_IDREFS;
        } else if (this.fEntityScanner.skipString(SchemaSymbols.ATTVAL_IDREF)) {
            type = SchemaSymbols.ATTVAL_IDREF;
        } else if (this.fEntityScanner.skipString("ID")) {
            type = "ID";
        } else if (this.fEntityScanner.skipString(SchemaSymbols.ATTVAL_ENTITY)) {
            type = SchemaSymbols.ATTVAL_ENTITY;
        } else if (this.fEntityScanner.skipString(SchemaSymbols.ATTVAL_ENTITIES)) {
            type = SchemaSymbols.ATTVAL_ENTITIES;
        } else if (this.fEntityScanner.skipString(SchemaSymbols.ATTVAL_NMTOKENS)) {
            type = SchemaSymbols.ATTVAL_NMTOKENS;
        } else if (this.fEntityScanner.skipString(SchemaSymbols.ATTVAL_NMTOKEN)) {
            type = SchemaSymbols.ATTVAL_NMTOKEN;
        } else if (this.fEntityScanner.skipString(SchemaSymbols.ATTVAL_NOTATION)) {
            type = SchemaSymbols.ATTVAL_NOTATION;
            if (!skipSeparator(true, !scanningInternalSubset())) {
                reportFatalError("MSG_SPACE_REQUIRED_AFTER_NOTATION_IN_NOTATIONTYPE", new Object[]{elName, atName});
            }
            if (this.fEntityScanner.scanChar(null) != 40) {
                reportFatalError("MSG_OPEN_PAREN_REQUIRED_IN_NOTATIONTYPE", new Object[]{elName, atName});
            }
            this.fMarkUpDepth++;
            do {
                skipSeparator(false, !scanningInternalSubset());
                String aName = this.fEntityScanner.scanName(XMLScanner.NameType.ATTRIBUTENAME);
                if (aName == null) {
                    reportFatalError("MSG_NAME_REQUIRED_IN_NOTATIONTYPE", new Object[]{elName, atName});
                }
                ensureEnumerationSize(this.fEnumerationCount + 1);
                String[] strArr = this.fEnumeration;
                int i2 = this.fEnumerationCount;
                this.fEnumerationCount = i2 + 1;
                strArr[i2] = aName;
                skipSeparator(false, !scanningInternalSubset());
                c3 = this.fEntityScanner.scanChar(null);
            } while (c3 == 124);
            if (c3 != 41) {
                reportFatalError("NotationTypeUnterminated", new Object[]{elName, atName});
            }
            this.fMarkUpDepth--;
        } else {
            type = "ENUMERATION";
            if (this.fEntityScanner.scanChar(null) != 40) {
                reportFatalError("AttTypeRequiredInAttDef", new Object[]{elName, atName});
            }
            this.fMarkUpDepth++;
            do {
                skipSeparator(false, !scanningInternalSubset());
                String token = this.fEntityScanner.scanNmtoken();
                if (token == null) {
                    reportFatalError("MSG_NMTOKEN_REQUIRED_IN_ENUMERATION", new Object[]{elName, atName});
                }
                ensureEnumerationSize(this.fEnumerationCount + 1);
                String[] strArr2 = this.fEnumeration;
                int i3 = this.fEnumerationCount;
                this.fEnumerationCount = i3 + 1;
                strArr2[i3] = token;
                skipSeparator(false, !scanningInternalSubset());
                c2 = this.fEntityScanner.scanChar(null);
            } while (c2 == 124);
            if (c2 != 41) {
                reportFatalError("EnumerationUnterminated", new Object[]{elName, atName});
            }
            this.fMarkUpDepth--;
        }
        return type;
    }

    protected final String scanAttDefaultDecl(String elName, String atName, String type, XMLString defaultVal, XMLString nonNormalizedDefaultVal) throws IOException, XNIException {
        String defaultType = null;
        this.fString.clear();
        defaultVal.clear();
        if (this.fEntityScanner.skipString("#REQUIRED")) {
            defaultType = "#REQUIRED";
        } else if (this.fEntityScanner.skipString("#IMPLIED")) {
            defaultType = "#IMPLIED";
        } else {
            if (this.fEntityScanner.skipString("#FIXED")) {
                defaultType = "#FIXED";
                if (!skipSeparator(true, !scanningInternalSubset())) {
                    reportFatalError("MSG_SPACE_REQUIRED_AFTER_FIXED_IN_DEFAULTDECL", new Object[]{elName, atName});
                }
            }
            boolean isVC = !this.fStandalone && (this.fSeenExternalDTD || this.fSeenExternalPE);
            scanAttributeValue(defaultVal, nonNormalizedDefaultVal, atName, this.fAttributes, 0, isVC, elName, false);
        }
        return defaultType;
    }

    private final void scanEntityDecl() throws IOException, XNIException {
        boolean isPEDecl = false;
        boolean sawPERef = false;
        this.fReportEntity = false;
        if (this.fEntityScanner.skipSpaces()) {
            if (!this.fEntityScanner.skipChar(37, XMLScanner.NameType.REFERENCE)) {
                isPEDecl = false;
            } else {
                if (skipSeparator(true, !scanningInternalSubset())) {
                    isPEDecl = true;
                } else if (scanningInternalSubset()) {
                    reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_ENTITYDECL", null);
                    isPEDecl = true;
                } else if (this.fEntityScanner.peekChar() == 37) {
                    skipSeparator(false, !scanningInternalSubset());
                    isPEDecl = true;
                } else {
                    sawPERef = true;
                }
            }
        } else if (scanningInternalSubset() || !this.fEntityScanner.skipChar(37, XMLScanner.NameType.REFERENCE)) {
            reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ENTITY_NAME_IN_ENTITYDECL", null);
            isPEDecl = false;
        } else if (this.fEntityScanner.skipSpaces()) {
            reportFatalError("MSG_SPACE_REQUIRED_BEFORE_PERCENT_IN_PEDECL", null);
            isPEDecl = false;
        } else {
            sawPERef = true;
        }
        if (sawPERef) {
            while (true) {
                String peName = this.fEntityScanner.scanName(XMLScanner.NameType.REFERENCE);
                if (peName == null) {
                    reportFatalError("NameRequiredInPEReference", null);
                } else if (!this.fEntityScanner.skipChar(59, XMLScanner.NameType.REFERENCE)) {
                    reportFatalError("SemicolonRequiredInPEReference", new Object[]{peName});
                } else {
                    startPE(peName, false);
                }
                this.fEntityScanner.skipSpaces();
                if (!this.fEntityScanner.skipChar(37, XMLScanner.NameType.REFERENCE)) {
                    break;
                }
                if (!isPEDecl) {
                    if (skipSeparator(true, !scanningInternalSubset())) {
                        isPEDecl = true;
                        break;
                    }
                    isPEDecl = this.fEntityScanner.skipChar(37, XMLScanner.NameType.REFERENCE);
                }
            }
        }
        String name = this.fEntityScanner.scanName(XMLScanner.NameType.ENTITY);
        if (name == null) {
            reportFatalError("MSG_ENTITY_NAME_REQUIRED_IN_ENTITYDECL", null);
        }
        if (!skipSeparator(true, !scanningInternalSubset())) {
            reportFatalError("MSG_SPACE_REQUIRED_AFTER_ENTITY_NAME_IN_ENTITYDECL", new Object[]{name});
        }
        scanExternalID(this.fStrings, false);
        String systemId = this.fStrings[0];
        String publicId = this.fStrings[1];
        if (isPEDecl && systemId != null) {
            this.fSeenExternalPE = true;
        }
        String notation = null;
        boolean sawSpace = skipSeparator(true, !scanningInternalSubset());
        if (!isPEDecl && this.fEntityScanner.skipString("NDATA")) {
            if (!sawSpace) {
                reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NDATA_IN_UNPARSED_ENTITYDECL", new Object[]{name});
            }
            if (!skipSeparator(true, !scanningInternalSubset())) {
                reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_UNPARSED_ENTITYDECL", new Object[]{name});
            }
            notation = this.fEntityScanner.scanName(XMLScanner.NameType.NOTATION);
            if (notation == null) {
                reportFatalError("MSG_NOTATION_NAME_REQUIRED_FOR_UNPARSED_ENTITYDECL", new Object[]{name});
            }
        }
        if (systemId == null) {
            scanEntityValue(name, isPEDecl, this.fLiteral, this.fLiteral2);
            this.fStringBuffer.clear();
            this.fStringBuffer2.clear();
            this.fStringBuffer.append(this.fLiteral.ch, this.fLiteral.offset, this.fLiteral.length);
            this.fStringBuffer2.append(this.fLiteral2.ch, this.fLiteral2.offset, this.fLiteral2.length);
        }
        skipSeparator(false, !scanningInternalSubset());
        if (!this.fEntityScanner.skipChar(62, null)) {
            reportFatalError("EntityDeclUnterminated", new Object[]{name});
        }
        this.fMarkUpDepth--;
        if (isPEDecl) {
            name = FXMLLoader.RESOURCE_KEY_PREFIX + name;
        }
        if (systemId != null) {
            String baseSystemId = this.fEntityScanner.getBaseSystemId();
            if (notation != null) {
                this.fEntityStore.addUnparsedEntity(name, publicId, systemId, baseSystemId, notation);
            } else {
                this.fEntityStore.addExternalEntity(name, publicId, systemId, baseSystemId);
            }
            if (this.fDTDHandler != null) {
                this.fResourceIdentifier.setValues(publicId, systemId, baseSystemId, XMLEntityManager.expandSystemId(systemId, baseSystemId));
                if (notation != null) {
                    this.fDTDHandler.unparsedEntityDecl(name, this.fResourceIdentifier, notation, null);
                } else {
                    this.fDTDHandler.externalEntityDecl(name, this.fResourceIdentifier, null);
                }
            }
        } else {
            this.fEntityStore.addInternalEntity(name, this.fStringBuffer.toString());
            if (this.fDTDHandler != null) {
                this.fDTDHandler.internalEntityDecl(name, this.fStringBuffer, this.fStringBuffer2, null);
            }
        }
        this.fReportEntity = true;
    }

    protected final void scanEntityValue(String entityName, boolean isPEDecl, XMLString value, XMLString nonNormalizedValue) throws IOException, XNIException {
        int quote = this.fEntityScanner.scanChar(null);
        if (quote != 39 && quote != 34) {
            reportFatalError("OpenQuoteMissingInDecl", null);
        }
        int entityDepth = this.fEntityDepth;
        XMLString literal = this.fString;
        XMLString literal2 = this.fString;
        if (this.fLimitAnalyzer == null) {
            this.fLimitAnalyzer = this.fEntityManager.fLimitAnalyzer;
        }
        this.fLimitAnalyzer.startEntity(entityName);
        if (this.fEntityScanner.scanLiteral(quote, this.fString, false) != quote) {
            this.fStringBuffer.clear();
            this.fStringBuffer2.clear();
            do {
                int countChar = 0;
                int offset = this.fStringBuffer.length;
                this.fStringBuffer.append(this.fString);
                this.fStringBuffer2.append(this.fString);
                if (this.fEntityScanner.skipChar(38, XMLScanner.NameType.REFERENCE)) {
                    if (this.fEntityScanner.skipChar(35, XMLScanner.NameType.REFERENCE)) {
                        this.fStringBuffer2.append("&#");
                        scanCharReferenceValue(this.fStringBuffer, this.fStringBuffer2);
                    } else {
                        this.fStringBuffer.append('&');
                        this.fStringBuffer2.append('&');
                        String eName = this.fEntityScanner.scanName(XMLScanner.NameType.REFERENCE);
                        if (eName == null) {
                            reportFatalError("NameRequiredInReference", null);
                        } else {
                            this.fStringBuffer.append(eName);
                            this.fStringBuffer2.append(eName);
                        }
                        if (!this.fEntityScanner.skipChar(59, XMLScanner.NameType.REFERENCE)) {
                            reportFatalError("SemicolonRequiredInReference", new Object[]{eName});
                        } else {
                            this.fStringBuffer.append(';');
                            this.fStringBuffer2.append(';');
                        }
                    }
                } else if (this.fEntityScanner.skipChar(37, XMLScanner.NameType.REFERENCE)) {
                    do {
                        this.fStringBuffer2.append('%');
                        String peName = this.fEntityScanner.scanName(XMLScanner.NameType.REFERENCE);
                        if (peName == null) {
                            reportFatalError("NameRequiredInPEReference", null);
                        } else if (!this.fEntityScanner.skipChar(59, XMLScanner.NameType.REFERENCE)) {
                            reportFatalError("SemicolonRequiredInPEReference", new Object[]{peName});
                        } else {
                            if (scanningInternalSubset()) {
                                reportFatalError("PEReferenceWithinMarkup", new Object[]{peName});
                            }
                            this.fStringBuffer2.append(peName);
                            this.fStringBuffer2.append(';');
                        }
                        startPE(peName, true);
                        this.fEntityScanner.skipSpaces();
                    } while (this.fEntityScanner.skipChar(37, XMLScanner.NameType.REFERENCE));
                } else {
                    int c2 = this.fEntityScanner.peekChar();
                    if (XMLChar.isHighSurrogate(c2)) {
                        countChar = 0 + 1;
                        scanSurrogates(this.fStringBuffer2);
                    } else if (isInvalidLiteral(c2)) {
                        reportFatalError("InvalidCharInLiteral", new Object[]{Integer.toHexString(c2)});
                        this.fEntityScanner.scanChar(null);
                    } else if (c2 != quote || entityDepth != this.fEntityDepth) {
                        this.fStringBuffer.append((char) c2);
                        this.fStringBuffer2.append((char) c2);
                        this.fEntityScanner.scanChar(null);
                    }
                }
                checkEntityLimit(isPEDecl, entityName, (this.fStringBuffer.length - offset) + countChar);
            } while (this.fEntityScanner.scanLiteral(quote, this.fString, false) != quote);
            checkEntityLimit(isPEDecl, entityName, this.fString.length);
            this.fStringBuffer.append(this.fString);
            this.fStringBuffer2.append(this.fString);
            literal = this.fStringBuffer;
            literal2 = this.fStringBuffer2;
        } else {
            checkEntityLimit(isPEDecl, entityName, literal);
        }
        value.setValues(literal);
        nonNormalizedValue.setValues(literal2);
        if (this.fLimitAnalyzer != null) {
            if (isPEDecl) {
                this.fLimitAnalyzer.endEntity(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT, entityName);
            } else {
                this.fLimitAnalyzer.endEntity(XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT, entityName);
            }
        }
        if (!this.fEntityScanner.skipChar(quote, null)) {
            reportFatalError("CloseQuoteMissingInDecl", null);
        }
    }

    private final void scanNotationDecl() throws IOException, XNIException {
        this.fReportEntity = false;
        if (!skipSeparator(true, !scanningInternalSubset())) {
            reportFatalError("MSG_SPACE_REQUIRED_BEFORE_NOTATION_NAME_IN_NOTATIONDECL", null);
        }
        String name = this.fEntityScanner.scanName(XMLScanner.NameType.NOTATION);
        if (name == null) {
            reportFatalError("MSG_NOTATION_NAME_REQUIRED_IN_NOTATIONDECL", null);
        }
        if (!skipSeparator(true, !scanningInternalSubset())) {
            reportFatalError("MSG_SPACE_REQUIRED_AFTER_NOTATION_NAME_IN_NOTATIONDECL", new Object[]{name});
        }
        scanExternalID(this.fStrings, true);
        String systemId = this.fStrings[0];
        String publicId = this.fStrings[1];
        String baseSystemId = this.fEntityScanner.getBaseSystemId();
        if (systemId == null && publicId == null) {
            reportFatalError("ExternalIDorPublicIDRequired", new Object[]{name});
        }
        skipSeparator(false, !scanningInternalSubset());
        if (!this.fEntityScanner.skipChar(62, null)) {
            reportFatalError("NotationDeclUnterminated", new Object[]{name});
        }
        this.fMarkUpDepth--;
        this.fResourceIdentifier.setValues(publicId, systemId, baseSystemId, XMLEntityManager.expandSystemId(systemId, baseSystemId));
        if (this.nonValidatingMode) {
            this.nvGrammarInfo.notationDecl(name, this.fResourceIdentifier, null);
        }
        if (this.fDTDHandler != null) {
            this.fDTDHandler.notationDecl(name, this.fResourceIdentifier, null);
        }
        this.fReportEntity = true;
    }

    private final void scanConditionalSect(int currPEDepth) throws IOException, XNIException {
        this.fReportEntity = false;
        skipSeparator(false, !scanningInternalSubset());
        if (this.fEntityScanner.skipString("INCLUDE")) {
            skipSeparator(false, !scanningInternalSubset());
            if (currPEDepth != this.fPEDepth && this.fValidation) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "INVALID_PE_IN_CONDITIONAL", new Object[]{this.fEntityManager.fCurrentEntity.name}, (short) 1);
            }
            if (!this.fEntityScanner.skipChar(91, null)) {
                reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
            }
            if (this.fDTDHandler != null) {
                this.fDTDHandler.startConditional((short) 0, null);
            }
            this.fIncludeSectDepth++;
            this.fReportEntity = true;
            return;
        }
        if (this.fEntityScanner.skipString("IGNORE")) {
            skipSeparator(false, !scanningInternalSubset());
            if (currPEDepth != this.fPEDepth && this.fValidation) {
                this.fErrorReporter.reportError("http://www.w3.org/TR/1998/REC-xml-19980210", "INVALID_PE_IN_CONDITIONAL", new Object[]{this.fEntityManager.fCurrentEntity.name}, (short) 1);
            }
            if (this.fDTDHandler != null) {
                this.fDTDHandler.startConditional((short) 1, null);
            }
            if (!this.fEntityScanner.skipChar(91, null)) {
                reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
            }
            this.fReportEntity = true;
            int initialDepth = this.fIncludeSectDepth + 1;
            this.fIncludeSectDepth = initialDepth;
            if (this.fDTDHandler != null) {
                this.fIgnoreConditionalBuffer.clear();
            }
            while (true) {
                if (this.fEntityScanner.skipChar(60, null)) {
                    if (this.fDTDHandler != null) {
                        this.fIgnoreConditionalBuffer.append('<');
                    }
                    if (this.fEntityScanner.skipChar(33, null)) {
                        if (this.fEntityScanner.skipChar(91, null)) {
                            if (this.fDTDHandler != null) {
                                this.fIgnoreConditionalBuffer.append("![");
                            }
                            this.fIncludeSectDepth++;
                        } else if (this.fDTDHandler != null) {
                            this.fIgnoreConditionalBuffer.append("!");
                        }
                    }
                } else if (this.fEntityScanner.skipChar(93, null)) {
                    if (this.fDTDHandler != null) {
                        this.fIgnoreConditionalBuffer.append(']');
                    }
                    if (this.fEntityScanner.skipChar(93, null)) {
                        if (this.fDTDHandler != null) {
                            this.fIgnoreConditionalBuffer.append(']');
                        }
                        while (this.fEntityScanner.skipChar(93, null)) {
                            if (this.fDTDHandler != null) {
                                this.fIgnoreConditionalBuffer.append(']');
                            }
                        }
                        if (this.fEntityScanner.skipChar(62, null)) {
                            int i2 = this.fIncludeSectDepth;
                            this.fIncludeSectDepth = i2 - 1;
                            if (i2 == initialDepth) {
                                this.fMarkUpDepth--;
                                if (this.fDTDHandler != null) {
                                    this.fLiteral.setValues(this.fIgnoreConditionalBuffer.ch, 0, this.fIgnoreConditionalBuffer.length - 2);
                                    this.fDTDHandler.ignoredCharacters(this.fLiteral, null);
                                    this.fDTDHandler.endConditional(null);
                                    return;
                                }
                                return;
                            }
                            if (this.fDTDHandler != null) {
                                this.fIgnoreConditionalBuffer.append('>');
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                } else {
                    int c2 = this.fEntityScanner.scanChar(null);
                    if (this.fScannerState == 0) {
                        reportFatalError("IgnoreSectUnterminated", null);
                        return;
                    } else if (this.fDTDHandler != null) {
                        this.fIgnoreConditionalBuffer.append((char) c2);
                    }
                }
            }
        } else {
            reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
        }
    }

    protected final boolean scanDecls(boolean complete) throws IOException, XNIException {
        skipSeparator(false, true);
        boolean again = true;
        while (again && this.fScannerState == 2) {
            again = complete;
            if (this.fEntityScanner.skipChar(60, null)) {
                this.fMarkUpDepth++;
                if (this.fEntityScanner.skipChar(63, null)) {
                    this.fStringBuffer.clear();
                    scanPI(this.fStringBuffer);
                    this.fMarkUpDepth--;
                } else if (this.fEntityScanner.skipChar(33, null)) {
                    if (this.fEntityScanner.skipChar(45, null)) {
                        if (!this.fEntityScanner.skipChar(45, null)) {
                            reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
                        } else {
                            scanComment();
                        }
                    } else if (this.fEntityScanner.skipString("ELEMENT")) {
                        scanElementDecl();
                    } else if (this.fEntityScanner.skipString("ATTLIST")) {
                        scanAttlistDecl();
                    } else if (this.fEntityScanner.skipString(SchemaSymbols.ATTVAL_ENTITY)) {
                        scanEntityDecl();
                    } else if (this.fEntityScanner.skipString(SchemaSymbols.ATTVAL_NOTATION)) {
                        scanNotationDecl();
                    } else if (this.fEntityScanner.skipChar(91, null) && !scanningInternalSubset()) {
                        scanConditionalSect(this.fPEDepth);
                    } else {
                        this.fMarkUpDepth--;
                        reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
                    }
                } else {
                    this.fMarkUpDepth--;
                    reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
                }
            } else if (this.fIncludeSectDepth > 0 && this.fEntityScanner.skipChar(93, null)) {
                if (!this.fEntityScanner.skipChar(93, null) || !this.fEntityScanner.skipChar(62, null)) {
                    reportFatalError("IncludeSectUnterminated", null);
                }
                if (this.fDTDHandler != null) {
                    this.fDTDHandler.endConditional(null);
                }
                this.fIncludeSectDepth--;
                this.fMarkUpDepth--;
            } else {
                if (scanningInternalSubset() && this.fEntityScanner.peekChar() == 93) {
                    return false;
                }
                if (!this.fEntityScanner.skipSpaces()) {
                    reportFatalError("MSG_MARKUP_NOT_RECOGNIZED_IN_DTD", null);
                }
            }
            skipSeparator(false, true);
        }
        return this.fScannerState != 0;
    }

    private boolean skipSeparator(boolean spaceRequired, boolean lookForPERefs) throws IOException, XNIException {
        int depth = this.fPEDepth;
        boolean sawSpace = this.fEntityScanner.skipSpaces();
        if (!lookForPERefs || !this.fEntityScanner.skipChar(37, XMLScanner.NameType.REFERENCE)) {
            return (spaceRequired && !sawSpace && depth == this.fPEDepth) ? false : true;
        }
        do {
            String name = this.fEntityScanner.scanName(XMLScanner.NameType.ENTITY);
            if (name == null) {
                reportFatalError("NameRequiredInPEReference", null);
            } else if (!this.fEntityScanner.skipChar(59, XMLScanner.NameType.REFERENCE)) {
                reportFatalError("SemicolonRequiredInPEReference", new Object[]{name});
            }
            startPE(name, false);
            this.fEntityScanner.skipSpaces();
        } while (this.fEntityScanner.skipChar(37, XMLScanner.NameType.REFERENCE));
        return true;
    }

    private final void pushContentStack(int c2) {
        if (this.fContentStack.length == this.fContentDepth) {
            int[] newStack = new int[this.fContentDepth * 2];
            System.arraycopy(this.fContentStack, 0, newStack, 0, this.fContentDepth);
            this.fContentStack = newStack;
        }
        int[] iArr = this.fContentStack;
        int i2 = this.fContentDepth;
        this.fContentDepth = i2 + 1;
        iArr[i2] = c2;
    }

    private final int popContentStack() {
        int[] iArr = this.fContentStack;
        int i2 = this.fContentDepth - 1;
        this.fContentDepth = i2;
        return iArr[i2];
    }

    private final void pushPEStack(int depth, boolean report) {
        if (this.fPEStack.length == this.fPEDepth) {
            int[] newIntStack = new int[this.fPEDepth * 2];
            System.arraycopy(this.fPEStack, 0, newIntStack, 0, this.fPEDepth);
            this.fPEStack = newIntStack;
            boolean[] newBooleanStack = new boolean[this.fPEDepth * 2];
            System.arraycopy(this.fPEReport, 0, newBooleanStack, 0, this.fPEDepth);
            this.fPEReport = newBooleanStack;
        }
        this.fPEReport[this.fPEDepth] = report;
        int[] iArr = this.fPEStack;
        int i2 = this.fPEDepth;
        this.fPEDepth = i2 + 1;
        iArr[i2] = depth;
    }

    private final int popPEStack() {
        int[] iArr = this.fPEStack;
        int i2 = this.fPEDepth - 1;
        this.fPEDepth = i2;
        return iArr[i2];
    }

    private final boolean peekReportEntity() {
        return this.fPEReport[this.fPEDepth - 1];
    }

    private final void ensureEnumerationSize(int size) {
        if (this.fEnumeration.length == size) {
            String[] newEnum = new String[size * 2];
            System.arraycopy(this.fEnumeration, 0, newEnum, 0, size);
            this.fEnumeration = newEnum;
        }
    }

    private void init() {
        this.fStartDTDCalled = false;
        this.fExtEntityDepth = 0;
        this.fIncludeSectDepth = 0;
        this.fMarkUpDepth = 0;
        this.fPEDepth = 0;
        this.fStandalone = false;
        this.fSeenExternalDTD = false;
        this.fSeenExternalPE = false;
        setScannerState(1);
        this.fLimitAnalyzer = this.fEntityManager.fLimitAnalyzer;
        this.fSecurityManager = this.fEntityManager.fSecurityManager;
    }

    public DTDGrammar getGrammar() {
        return this.nvGrammarInfo;
    }
}
