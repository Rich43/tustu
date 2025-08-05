package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl;
import com.sun.org.apache.xerces.internal.impl.XMLScanner;
import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDDescription;
import com.sun.org.apache.xerces.internal.impl.validation.ValidationManager;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDScanner;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.xml.internal.stream.Entity;
import com.sun.xml.internal.stream.dtd.DTDGrammarUtil;
import java.io.EOFException;
import java.io.IOException;
import java.util.NoSuchElementException;
import javax.xml.stream.XMLInputFactory;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentScannerImpl.class */
public class XMLDocumentScannerImpl extends XMLDocumentFragmentScannerImpl {
    protected static final int SCANNER_STATE_XML_DECL = 42;
    protected static final int SCANNER_STATE_PROLOG = 43;
    protected static final int SCANNER_STATE_TRAILING_MISC = 44;
    protected static final int SCANNER_STATE_DTD_INTERNAL_DECLS = 45;
    protected static final int SCANNER_STATE_DTD_EXTERNAL = 46;
    protected static final int SCANNER_STATE_DTD_EXTERNAL_DECLS = 47;
    protected static final int SCANNER_STATE_NO_SUCH_ELEMENT_EXCEPTION = 48;
    protected static final String DOCUMENT_SCANNER = "http://apache.org/xml/properties/internal/document-scanner";
    protected static final String NAMESPACE_CONTEXT = "http://apache.org/xml/properties/internal/namespace-context";
    protected ValidationManager fValidationManager;
    protected String fDoctypeName;
    protected String fDoctypePublicId;
    protected String fDoctypeSystemId;
    protected boolean fSeenDoctypeDecl;
    protected boolean fScanEndElement;
    protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    protected static final String DISALLOW_DOCTYPE_DECL_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String[] RECOGNIZED_FEATURES = {LOAD_EXTERNAL_DTD, DISALLOW_DOCTYPE_DECL_FEATURE};
    private static final Boolean[] FEATURE_DEFAULTS = {Boolean.TRUE, Boolean.FALSE};
    protected static final String DTD_SCANNER = "http://apache.org/xml/properties/internal/dtd-scanner";
    protected static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
    private static final String[] RECOGNIZED_PROPERTIES = {DTD_SCANNER, VALIDATION_MANAGER};
    private static final Object[] PROPERTY_DEFAULTS = {null, null};
    private static final char[] DOCTYPE = {'D', 'O', 'C', 'T', 'Y', 'P', 'E'};
    private static final char[] COMMENTSTRING = {'-', '-'};
    protected XMLDTDScanner fDTDScanner = null;
    protected XMLStringBuffer fDTDDecl = null;
    protected boolean fReadingDTD = false;
    protected boolean fAddedListener = false;
    protected NamespaceContext fNamespaceContext = new NamespaceSupport();
    protected boolean fLoadExternalDTD = true;
    protected XMLDocumentFragmentScannerImpl.Driver fXMLDeclDriver = new XMLDeclDriver();
    protected XMLDocumentFragmentScannerImpl.Driver fPrologDriver = new PrologDriver();
    protected XMLDocumentFragmentScannerImpl.Driver fDTDDriver = null;
    protected XMLDocumentFragmentScannerImpl.Driver fTrailingMiscDriver = new TrailingMiscDriver();
    protected int fStartPos = 0;
    protected int fEndPos = 0;
    protected boolean fSeenInternalSubset = false;
    private String[] fStrings = new String[3];
    private XMLInputSource fExternalSubsetSource = null;
    private final XMLDTDDescription fDTDDescription = new XMLDTDDescription(null, null, null, null, null);

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner
    public void setInputSource(XMLInputSource inputSource) throws IOException, XNIException {
        this.fEntityManager.setEntityHandler(this);
        this.fEntityManager.startDocumentEntity(inputSource);
        setScannerState(7);
    }

    public int getScannetState() {
        return this.fScannerState;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.impl.XMLScanner
    public void reset(PropertyManager propertyManager) {
        super.reset(propertyManager);
        this.fDoctypeName = null;
        this.fDoctypePublicId = null;
        this.fDoctypeSystemId = null;
        this.fSeenDoctypeDecl = false;
        this.fNamespaceContext.reset();
        this.fSupportDTD = ((Boolean) propertyManager.getProperty(XMLInputFactory.SUPPORT_DTD)).booleanValue();
        this.fLoadExternalDTD = !((Boolean) propertyManager.getProperty("http://java.sun.com/xml/stream/properties/ignore-external-dtd")).booleanValue();
        setScannerState(7);
        setDriver(this.fXMLDeclDriver);
        this.fSeenInternalSubset = false;
        if (this.fDTDScanner != null) {
            ((XMLDTDScannerImpl) this.fDTDScanner).reset(propertyManager);
        }
        this.fEndPos = 0;
        this.fStartPos = 0;
        if (this.fDTDDecl != null) {
            this.fDTDDecl.clear();
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        super.reset(componentManager);
        this.fDoctypeName = null;
        this.fDoctypePublicId = null;
        this.fDoctypeSystemId = null;
        this.fSeenDoctypeDecl = false;
        this.fExternalSubsetSource = null;
        this.fLoadExternalDTD = componentManager.getFeature(LOAD_EXTERNAL_DTD, true);
        this.fDisallowDoctype = componentManager.getFeature(DISALLOW_DOCTYPE_DECL_FEATURE, false);
        this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
        this.fSeenInternalSubset = false;
        this.fDTDScanner = (XMLDTDScanner) componentManager.getProperty(DTD_SCANNER);
        this.fValidationManager = (ValidationManager) componentManager.getProperty(VALIDATION_MANAGER, null);
        try {
            this.fNamespaceContext = (NamespaceContext) componentManager.getProperty(NAMESPACE_CONTEXT);
        } catch (XMLConfigurationException e2) {
        }
        if (this.fNamespaceContext == null) {
            this.fNamespaceContext = new NamespaceSupport();
        }
        this.fNamespaceContext.reset();
        this.fEndPos = 0;
        this.fStartPos = 0;
        if (this.fDTDDecl != null) {
            this.fDTDDecl.clear();
        }
        setScannerState(42);
        setDriver(this.fXMLDeclDriver);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        String[] featureIds = super.getRecognizedFeatures();
        int length = featureIds != null ? featureIds.length : 0;
        String[] combinedFeatureIds = new String[length + RECOGNIZED_FEATURES.length];
        if (featureIds != null) {
            System.arraycopy(featureIds, 0, combinedFeatureIds, 0, featureIds.length);
        }
        System.arraycopy(RECOGNIZED_FEATURES, 0, combinedFeatureIds, length, RECOGNIZED_FEATURES.length);
        return combinedFeatureIds;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        super.setFeature(featureId, state);
        if (featureId.startsWith(Constants.XERCES_FEATURE_PREFIX)) {
            int suffixLength = featureId.length() - Constants.XERCES_FEATURE_PREFIX.length();
            if (suffixLength == Constants.LOAD_EXTERNAL_DTD_FEATURE.length() && featureId.endsWith(Constants.LOAD_EXTERNAL_DTD_FEATURE)) {
                this.fLoadExternalDTD = state;
            } else if (suffixLength == Constants.DISALLOW_DOCTYPE_DECL_FEATURE.length() && featureId.endsWith(Constants.DISALLOW_DOCTYPE_DECL_FEATURE)) {
                this.fDisallowDoctype = state;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        String[] propertyIds = super.getRecognizedProperties();
        int length = propertyIds != null ? propertyIds.length : 0;
        String[] combinedPropertyIds = new String[length + RECOGNIZED_PROPERTIES.length];
        if (propertyIds != null) {
            System.arraycopy(propertyIds, 0, combinedPropertyIds, 0, propertyIds.length);
        }
        System.arraycopy(RECOGNIZED_PROPERTIES, 0, combinedPropertyIds, length, RECOGNIZED_PROPERTIES.length);
        return combinedPropertyIds;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        super.setProperty(propertyId, value);
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            int suffixLength = propertyId.length() - Constants.XERCES_PROPERTY_PREFIX.length();
            if (suffixLength == Constants.DTD_SCANNER_PROPERTY.length() && propertyId.endsWith(Constants.DTD_SCANNER_PROPERTY)) {
                this.fDTDScanner = (XMLDTDScanner) value;
            }
            if (suffixLength == Constants.NAMESPACE_CONTEXT_PROPERTY.length() && propertyId.endsWith(Constants.NAMESPACE_CONTEXT_PROPERTY) && value != null) {
                this.fNamespaceContext = (NamespaceContext) value;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Boolean getFeatureDefault(String featureId) {
        for (int i2 = 0; i2 < RECOGNIZED_FEATURES.length; i2++) {
            if (RECOGNIZED_FEATURES[i2].equals(featureId)) {
                return FEATURE_DEFAULTS[i2];
            }
        }
        return super.getFeatureDefault(featureId);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public Object getPropertyDefault(String propertyId) {
        for (int i2 = 0; i2 < RECOGNIZED_PROPERTIES.length; i2++) {
            if (RECOGNIZED_PROPERTIES[i2].equals(propertyId)) {
                return PROPERTY_DEFAULTS[i2];
            }
        }
        return super.getPropertyDefault(propertyId);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.impl.XMLEntityHandler
    public void startEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
        super.startEntity(name, identifier, encoding, augs);
        this.fEntityScanner.registerListener(this);
        if (!name.equals("[xml]") && this.fEntityScanner.isExternal() && (augs == null || !((Boolean) augs.getItem(Constants.ENTITY_SKIPPED)).booleanValue())) {
            setScannerState(36);
        }
        if (this.fDocumentHandler != null && name.equals("[xml]")) {
            this.fDocumentHandler.startDocument(this.fEntityScanner, encoding, this.fNamespaceContext, null);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.impl.XMLScanner, com.sun.org.apache.xerces.internal.impl.XMLEntityHandler
    public void endEntity(String name, Augmentations augs) throws IOException, XNIException {
        super.endEntity(name, augs);
        if (name.equals("[xml]")) {
            if (this.fMarkupDepth == 0 && this.fDriver == this.fTrailingMiscDriver) {
                setScannerState(34);
                return;
            }
            throw new EOFException();
        }
    }

    public XMLStringBuffer getDTDDecl() {
        Entity entity = this.fEntityScanner.getCurrentEntity();
        this.fDTDDecl.append(((Entity.ScannedEntity) entity).ch, this.fStartPos, this.fEndPos - this.fStartPos);
        if (this.fSeenInternalSubset) {
            this.fDTDDecl.append("]>");
        }
        return this.fDTDDecl;
    }

    public String getCharacterEncodingScheme() {
        return this.fDeclaredEncoding;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentScanner
    public int next() throws IOException, XNIException {
        return this.fDriver.next();
    }

    public NamespaceContext getNamespaceContext() {
        return this.fNamespaceContext;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl
    protected XMLDocumentFragmentScannerImpl.Driver createContentDriver() {
        return new ContentDriver();
    }

    protected boolean scanDoctypeDecl(boolean supportDTD) throws IOException, XNIException {
        if (!this.fEntityScanner.skipSpaces()) {
            reportFatalError("MSG_SPACE_REQUIRED_BEFORE_ROOT_ELEMENT_TYPE_IN_DOCTYPEDECL", null);
        }
        this.fDoctypeName = this.fEntityScanner.scanName(XMLScanner.NameType.DOCTYPE);
        if (this.fDoctypeName == null) {
            reportFatalError("MSG_ROOT_ELEMENT_TYPE_REQUIRED", null);
        }
        if (this.fEntityScanner.skipSpaces()) {
            scanExternalID(this.fStrings, false);
            this.fDoctypeSystemId = this.fStrings[0];
            this.fDoctypePublicId = this.fStrings[1];
            this.fEntityScanner.skipSpaces();
        }
        this.fHasExternalDTD = this.fDoctypeSystemId != null;
        if (supportDTD && !this.fHasExternalDTD && this.fExternalSubsetResolver != null) {
            this.fDTDDescription.setValues(null, null, this.fEntityManager.getCurrentResourceIdentifier().getExpandedSystemId(), null);
            this.fDTDDescription.setRootName(this.fDoctypeName);
            this.fExternalSubsetSource = this.fExternalSubsetResolver.getExternalSubset(this.fDTDDescription);
            this.fHasExternalDTD = this.fExternalSubsetSource != null;
        }
        if (supportDTD && this.fDocumentHandler != null) {
            if (this.fExternalSubsetSource == null) {
                this.fDocumentHandler.doctypeDecl(this.fDoctypeName, this.fDoctypePublicId, this.fDoctypeSystemId, null);
            } else {
                this.fDocumentHandler.doctypeDecl(this.fDoctypeName, this.fExternalSubsetSource.getPublicId(), this.fExternalSubsetSource.getSystemId(), null);
            }
        }
        boolean internalSubset = true;
        if (!this.fEntityScanner.skipChar(91, null)) {
            internalSubset = false;
            this.fEntityScanner.skipSpaces();
            if (!this.fEntityScanner.skipChar(62, null)) {
                reportFatalError("DoctypedeclUnterminated", new Object[]{this.fDoctypeName});
            }
            this.fMarkupDepth--;
        }
        return internalSubset;
    }

    protected void setEndDTDScanState() {
        setScannerState(43);
        setDriver(this.fPrologDriver);
        this.fEntityManager.setEntityHandler(this);
        this.fReadingDTD = false;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl
    protected String getScannerStateName(int state) {
        switch (state) {
            case 42:
                return "SCANNER_STATE_XML_DECL";
            case 43:
                return "SCANNER_STATE_PROLOG";
            case 44:
                return "SCANNER_STATE_TRAILING_MISC";
            case 45:
                return "SCANNER_STATE_DTD_INTERNAL_DECLS";
            case 46:
                return "SCANNER_STATE_DTD_EXTERNAL";
            case 47:
                return "SCANNER_STATE_DTD_EXTERNAL_DECLS";
            default:
                return super.getScannerStateName(state);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentScannerImpl$XMLDeclDriver.class */
    protected final class XMLDeclDriver implements XMLDocumentFragmentScannerImpl.Driver {
        protected XMLDeclDriver() {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.Driver
        public int next() throws IOException, XNIException {
            XMLDocumentScannerImpl.this.setScannerState(43);
            XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fPrologDriver);
            try {
                if (XMLDocumentScannerImpl.this.fEntityScanner.skipString(XMLDocumentFragmentScannerImpl.xmlDecl)) {
                    XMLDocumentScannerImpl.this.fMarkupDepth++;
                    if (XMLChar.isName(XMLDocumentScannerImpl.this.fEntityScanner.peekChar())) {
                        XMLDocumentScannerImpl.this.fStringBuffer.clear();
                        XMLDocumentScannerImpl.this.fStringBuffer.append("xml");
                        while (XMLChar.isName(XMLDocumentScannerImpl.this.fEntityScanner.peekChar())) {
                            XMLDocumentScannerImpl.this.fStringBuffer.append((char) XMLDocumentScannerImpl.this.fEntityScanner.scanChar(null));
                        }
                        String target = XMLDocumentScannerImpl.this.fSymbolTable.addSymbol(XMLDocumentScannerImpl.this.fStringBuffer.ch, XMLDocumentScannerImpl.this.fStringBuffer.offset, XMLDocumentScannerImpl.this.fStringBuffer.length);
                        XMLDocumentScannerImpl.this.fContentBuffer.clear();
                        XMLDocumentScannerImpl.this.scanPIData(target, XMLDocumentScannerImpl.this.fContentBuffer);
                        XMLDocumentScannerImpl.this.fEntityManager.fCurrentEntity.mayReadChunks = true;
                        return 3;
                    }
                    XMLDocumentScannerImpl.this.scanXMLDeclOrTextDecl(false);
                    XMLDocumentScannerImpl.this.fEntityManager.fCurrentEntity.mayReadChunks = true;
                    return 7;
                }
                XMLDocumentScannerImpl.this.fEntityManager.fCurrentEntity.mayReadChunks = true;
                return 7;
            } catch (EOFException e2) {
                XMLDocumentScannerImpl.this.reportFatalError("PrematureEOF", null);
                return -1;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentScannerImpl$PrologDriver.class */
    protected final class PrologDriver implements XMLDocumentFragmentScannerImpl.Driver {
        protected PrologDriver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:15:0x00a6, code lost:
        
            r5.this$0.setScannerState(26);
            r5.this$0.setDriver(r5.this$0.fContentDriver);
         */
        /* JADX WARN: Code restructure failed: missing block: B:16:0x00c9, code lost:
        
            return r5.this$0.fContentDriver.next();
         */
        @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.Driver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int next() throws java.io.IOException, com.sun.org.apache.xerces.internal.xni.XNIException {
            /*
                Method dump skipped, instructions count: 1131
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl.PrologDriver.next():int");
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentScannerImpl$DTDDriver.class */
    protected final class DTDDriver implements XMLDocumentFragmentScannerImpl.Driver {
        protected DTDDriver() {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.Driver
        public int next() throws IOException, XNIException {
            dispatch(true);
            if (XMLDocumentScannerImpl.this.fPropertyManager != null) {
                XMLDocumentScannerImpl.this.dtdGrammarUtil = new DTDGrammarUtil(((XMLDTDScannerImpl) XMLDocumentScannerImpl.this.fDTDScanner).getGrammar(), XMLDocumentScannerImpl.this.fSymbolTable, XMLDocumentScannerImpl.this.fNamespaceContext);
                return 11;
            }
            return 11;
        }

        /* JADX WARN: Code restructure failed: missing block: B:43:0x01d4, code lost:
        
            r7.this$0.setEndDTDScanState();
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x01df, code lost:
        
            r7.this$0.fEntityManager.setEntityHandler(r7.this$0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x01ee, code lost:
        
            return true;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean dispatch(boolean r8) throws java.io.IOException, com.sun.org.apache.xerces.internal.xni.XNIException {
            /*
                Method dump skipped, instructions count: 875
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl.DTDDriver.dispatch(boolean):boolean");
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentScannerImpl$ContentDriver.class */
    protected class ContentDriver extends XMLDocumentFragmentScannerImpl.FragmentContentDriver {
        protected ContentDriver() {
            super();
        }

        @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.FragmentContentDriver
        protected boolean scanForDoctypeHook() throws IOException, XNIException {
            if (XMLDocumentScannerImpl.this.fEntityScanner.skipString(XMLDocumentScannerImpl.DOCTYPE)) {
                XMLDocumentScannerImpl.this.setScannerState(24);
                return true;
            }
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.FragmentContentDriver
        protected boolean elementDepthIsZeroHook() throws IOException, XNIException {
            XMLDocumentScannerImpl.this.setScannerState(44);
            XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fTrailingMiscDriver);
            return true;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.FragmentContentDriver
        protected boolean scanRootElementHook() throws IOException, XNIException {
            if (XMLDocumentScannerImpl.this.scanStartElement()) {
                XMLDocumentScannerImpl.this.setScannerState(44);
                XMLDocumentScannerImpl.this.setDriver(XMLDocumentScannerImpl.this.fTrailingMiscDriver);
                return true;
            }
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.FragmentContentDriver
        protected void endOfFileHook(EOFException e2) throws IOException, XNIException {
            XMLDocumentScannerImpl.this.reportFatalError("PrematureEOF", null);
        }

        protected void resolveExternalSubsetAndRead() throws IOException, XNIException {
            XMLDocumentScannerImpl.this.fDTDDescription.setValues(null, null, XMLDocumentScannerImpl.this.fEntityManager.getCurrentResourceIdentifier().getExpandedSystemId(), null);
            XMLDocumentScannerImpl.this.fDTDDescription.setRootName(XMLDocumentScannerImpl.this.fElementQName.rawname);
            XMLInputSource src = XMLDocumentScannerImpl.this.fExternalSubsetResolver.getExternalSubset(XMLDocumentScannerImpl.this.fDTDDescription);
            if (src != null) {
                XMLDocumentScannerImpl.this.fDoctypeName = XMLDocumentScannerImpl.this.fElementQName.rawname;
                XMLDocumentScannerImpl.this.fDoctypePublicId = src.getPublicId();
                XMLDocumentScannerImpl.this.fDoctypeSystemId = src.getSystemId();
                if (XMLDocumentScannerImpl.this.fDocumentHandler != null) {
                    XMLDocumentScannerImpl.this.fDocumentHandler.doctypeDecl(XMLDocumentScannerImpl.this.fDoctypeName, XMLDocumentScannerImpl.this.fDoctypePublicId, XMLDocumentScannerImpl.this.fDoctypeSystemId, null);
                }
                try {
                    XMLDocumentScannerImpl.this.fDTDScanner.setInputSource(src);
                    do {
                    } while (XMLDocumentScannerImpl.this.fDTDScanner.scanDTDExternalSubset(true));
                } finally {
                    XMLDocumentScannerImpl.this.fEntityManager.setEntityHandler(XMLDocumentScannerImpl.this);
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLDocumentScannerImpl$TrailingMiscDriver.class */
    protected final class TrailingMiscDriver implements XMLDocumentFragmentScannerImpl.Driver {
        protected TrailingMiscDriver() {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.Driver
        public int next() throws IOException, XNIException {
            if (XMLDocumentScannerImpl.this.fEmptyElement) {
                XMLDocumentScannerImpl.this.fEmptyElement = false;
                return 2;
            }
            try {
                if (XMLDocumentScannerImpl.this.fScannerState == 34) {
                    return 8;
                }
                while (true) {
                    switch (XMLDocumentScannerImpl.this.fScannerState) {
                        case 21:
                            XMLDocumentScannerImpl.this.fMarkupDepth++;
                            if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(63, null)) {
                                XMLDocumentScannerImpl.this.setScannerState(23);
                                break;
                            } else if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(33, null)) {
                                XMLDocumentScannerImpl.this.setScannerState(27);
                                break;
                            } else if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(47, null)) {
                                XMLDocumentScannerImpl.this.reportFatalError("MarkupNotRecognizedInMisc", null);
                                break;
                            } else if (XMLDocumentScannerImpl.this.isValidNameStartChar(XMLDocumentScannerImpl.this.fEntityScanner.peekChar()) || XMLDocumentScannerImpl.this.isValidNameStartHighSurrogate(XMLDocumentScannerImpl.this.fEntityScanner.peekChar())) {
                                XMLDocumentScannerImpl.this.reportFatalError("MarkupNotRecognizedInMisc", null);
                                XMLDocumentScannerImpl.this.scanStartElement();
                                XMLDocumentScannerImpl.this.setScannerState(22);
                                break;
                            } else {
                                XMLDocumentScannerImpl.this.reportFatalError("MarkupNotRecognizedInMisc", null);
                                break;
                            }
                        case 44:
                            XMLDocumentScannerImpl.this.fEntityScanner.skipSpaces();
                            if (XMLDocumentScannerImpl.this.fScannerState == 34) {
                                return 8;
                            }
                            if (XMLDocumentScannerImpl.this.fEntityScanner.skipChar(60, null)) {
                                XMLDocumentScannerImpl.this.setScannerState(21);
                                break;
                            } else {
                                XMLDocumentScannerImpl.this.setScannerState(22);
                                break;
                            }
                    }
                    if (XMLDocumentScannerImpl.this.fScannerState != 21 && XMLDocumentScannerImpl.this.fScannerState != 44) {
                        switch (XMLDocumentScannerImpl.this.fScannerState) {
                            case 22:
                                int ch = XMLDocumentScannerImpl.this.fEntityScanner.peekChar();
                                if (ch == -1) {
                                    XMLDocumentScannerImpl.this.setScannerState(34);
                                    return 8;
                                }
                                XMLDocumentScannerImpl.this.reportFatalError("ContentIllegalInTrailingMisc", null);
                                XMLDocumentScannerImpl.this.fEntityScanner.scanChar(null);
                                XMLDocumentScannerImpl.this.setScannerState(44);
                                return 4;
                            case 23:
                                XMLDocumentScannerImpl.this.fContentBuffer.clear();
                                XMLDocumentScannerImpl.this.scanPI(XMLDocumentScannerImpl.this.fContentBuffer);
                                XMLDocumentScannerImpl.this.setScannerState(44);
                                return 3;
                            case 27:
                                if (!XMLDocumentScannerImpl.this.fEntityScanner.skipString(XMLDocumentScannerImpl.COMMENTSTRING)) {
                                    XMLDocumentScannerImpl.this.reportFatalError("InvalidCommentStart", null);
                                }
                                XMLDocumentScannerImpl.this.scanComment();
                                XMLDocumentScannerImpl.this.setScannerState(44);
                                return 5;
                            case 28:
                                XMLDocumentScannerImpl.this.reportFatalError("ReferenceIllegalInTrailingMisc", null);
                                XMLDocumentScannerImpl.this.setScannerState(44);
                                return 9;
                            case 34:
                                XMLDocumentScannerImpl.this.setScannerState(48);
                                return 8;
                            case 48:
                                throw new NoSuchElementException("No more events to be parsed");
                            default:
                                throw new XNIException("Scanner State " + XMLDocumentScannerImpl.this.fScannerState + " not Recognized ");
                        }
                    }
                }
            } catch (EOFException e2) {
                if (XMLDocumentScannerImpl.this.fMarkupDepth != 0) {
                    XMLDocumentScannerImpl.this.reportFatalError("PrematureEOF", null);
                    return -1;
                }
                XMLDocumentScannerImpl.this.setScannerState(34);
                return 8;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl, com.sun.xml.internal.stream.XMLBufferListener
    public void refresh(int refreshPosition) {
        super.refresh(refreshPosition);
        if (this.fReadingDTD) {
            Entity entity = this.fEntityScanner.getCurrentEntity();
            if (entity instanceof Entity.ScannedEntity) {
                this.fEndPos = ((Entity.ScannedEntity) entity).position;
            }
            this.fDTDDecl.append(((Entity.ScannedEntity) entity).ch, this.fStartPos, this.fEndPos - this.fStartPos);
            this.fStartPos = refreshPosition;
        }
    }
}
