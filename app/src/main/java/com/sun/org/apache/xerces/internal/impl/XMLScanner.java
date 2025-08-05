package com.sun.org.apache.xerces.internal.impl;

import com.sun.glass.ui.Clipboard;
import com.sun.org.apache.xerces.internal.util.Status;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLResourceIdentifierImpl;
import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer;
import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import com.sun.xml.internal.stream.Entity;
import com.sun.xml.internal.stream.XMLEntityStorage;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import javax.xml.stream.events.XMLEvent;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLScanner.class */
public abstract class XMLScanner implements XMLComponent {
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    protected static final String VALIDATION = "http://xml.org/sax/features/validation";
    protected static final String NOTIFY_CHAR_REFS = "http://apache.org/xml/features/scanner/notify-char-refs";
    protected static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    protected static final boolean DEBUG_ATTR_NORMALIZATION = false;
    protected boolean fNamespaces;
    protected SymbolTable fSymbolTable;
    protected XMLErrorReporter fErrorReporter;
    protected XMLEvent fEvent;
    protected int fEntityDepth;
    protected boolean fScanningAttribute;
    protected boolean fReportEntity;
    protected static final String fVersionSymbol = "version".intern();
    protected static final String fEncodingSymbol = "encoding".intern();
    protected static final String fStandaloneSymbol = "standalone".intern();
    protected static final String fAmpSymbol = "amp".intern();
    protected static final String fLtSymbol = "lt".intern();
    protected static final String fGtSymbol = "gt".intern();
    protected static final String fQuotSymbol = "quot".intern();
    protected static final String fAposSymbol = "apos".intern();
    private boolean fNeedNonNormalizedValue = false;
    protected ArrayList<XMLString> attributeValueCache = new ArrayList<>();
    protected ArrayList<XMLStringBuffer> stringBufferCache = new ArrayList<>();
    protected int fStringBufferIndex = 0;
    protected boolean fAttributeCacheInitDone = false;
    protected int fAttributeCacheUsedCount = 0;
    protected boolean fValidation = false;
    protected boolean fNotifyCharRefs = false;
    protected boolean fParserSettings = true;
    protected PropertyManager fPropertyManager = null;
    protected XMLEntityManager fEntityManager = null;
    protected XMLEntityStorage fEntityStore = null;
    protected XMLSecurityManager fSecurityManager = null;
    protected XMLLimitAnalyzer fLimitAnalyzer = null;
    protected XMLEntityScanner fEntityScanner = null;
    protected String fCharRefLiteral = null;
    private XMLString fString = new XMLString();
    private XMLStringBuffer fStringBuffer = new XMLStringBuffer();
    private XMLStringBuffer fStringBuffer2 = new XMLStringBuffer();
    private XMLStringBuffer fStringBuffer3 = new XMLStringBuffer();
    protected XMLResourceIdentifierImpl fResourceIdentifier = new XMLResourceIdentifierImpl();
    int initialCacheCount = 6;

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLScanner$NameType.class */
    public enum NameType {
        ATTRIBUTE("attribute"),
        ATTRIBUTENAME("attribute name"),
        COMMENT("comment"),
        DOCTYPE("doctype"),
        ELEMENTSTART("startelement"),
        ELEMENTEND("endelement"),
        ENTITY("entity"),
        NOTATION("notation"),
        PI(com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_PI_OLD_STRING),
        REFERENCE(FXMLLoader.REFERENCE_TAG);

        final String literal;

        NameType(String literal) {
            this.literal = literal;
        }

        String literal() {
            return this.literal;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        this.fParserSettings = componentManager.getFeature(PARSER_SETTINGS, true);
        if (!this.fParserSettings) {
            init();
            return;
        }
        this.fSymbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fEntityManager = (XMLEntityManager) componentManager.getProperty(ENTITY_MANAGER);
        this.fSecurityManager = (XMLSecurityManager) componentManager.getProperty("http://apache.org/xml/properties/security-manager");
        this.fEntityStore = this.fEntityManager.getEntityStore();
        this.fValidation = componentManager.getFeature(VALIDATION, false);
        this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
        this.fNotifyCharRefs = componentManager.getFeature(NOTIFY_CHAR_REFS, false);
        init();
    }

    protected void setPropertyManager(PropertyManager propertyManager) {
        this.fPropertyManager = propertyManager;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (propertyId.startsWith(Constants.XERCES_PROPERTY_PREFIX)) {
            String property = propertyId.substring(Constants.XERCES_PROPERTY_PREFIX.length());
            if (property.equals(Constants.SYMBOL_TABLE_PROPERTY)) {
                this.fSymbolTable = (SymbolTable) value;
            } else if (property.equals(Constants.ERROR_REPORTER_PROPERTY)) {
                this.fErrorReporter = (XMLErrorReporter) value;
            } else if (property.equals(Constants.ENTITY_MANAGER_PROPERTY)) {
                this.fEntityManager = (XMLEntityManager) value;
            }
        }
        if (propertyId.equals("http://apache.org/xml/properties/security-manager")) {
            this.fSecurityManager = (XMLSecurityManager) value;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean value) throws XMLConfigurationException {
        if (VALIDATION.equals(featureId)) {
            this.fValidation = value;
        } else if (NOTIFY_CHAR_REFS.equals(featureId)) {
            this.fNotifyCharRefs = value;
        }
    }

    public boolean getFeature(String featureId) throws XMLConfigurationException {
        if (VALIDATION.equals(featureId)) {
            return this.fValidation;
        }
        if (NOTIFY_CHAR_REFS.equals(featureId)) {
            return this.fNotifyCharRefs;
        }
        throw new XMLConfigurationException(Status.NOT_RECOGNIZED, featureId);
    }

    protected void reset() {
        init();
        this.fValidation = true;
        this.fNotifyCharRefs = false;
    }

    public void reset(PropertyManager propertyManager) {
        init();
        this.fSymbolTable = (SymbolTable) propertyManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fErrorReporter = (XMLErrorReporter) propertyManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
        this.fEntityManager = (XMLEntityManager) propertyManager.getProperty(ENTITY_MANAGER);
        this.fEntityStore = this.fEntityManager.getEntityStore();
        this.fEntityScanner = this.fEntityManager.getEntityScanner();
        this.fSecurityManager = (XMLSecurityManager) propertyManager.getProperty("http://apache.org/xml/properties/security-manager");
        this.fValidation = false;
        this.fNotifyCharRefs = false;
    }

    protected void scanXMLDeclOrTextDecl(boolean scanningTextDecl, String[] pseudoAttributeValues) throws IOException, XNIException {
        String version = null;
        String encoding = null;
        String standalone = null;
        int state = 0;
        boolean dataFoundForTarget = false;
        boolean sawSpace = this.fEntityScanner.skipSpaces();
        Entity.ScannedEntity currEnt = this.fEntityManager.getCurrentEntity();
        boolean currLiteral = currEnt.literal;
        currEnt.literal = false;
        while (this.fEntityScanner.peekChar() != 63) {
            dataFoundForTarget = true;
            String name = scanPseudoAttribute(scanningTextDecl, this.fString);
            switch (state) {
                case 0:
                    if (name.equals(fVersionSymbol)) {
                        if (!sawSpace) {
                            reportFatalError(scanningTextDecl ? "SpaceRequiredBeforeVersionInTextDecl" : "SpaceRequiredBeforeVersionInXMLDecl", null);
                        }
                        version = this.fString.toString();
                        state = 1;
                        if (!versionSupported(version)) {
                            reportFatalError("VersionNotSupported", new Object[]{version});
                        }
                        if (!version.equals(SerializerConstants.XMLVERSION11)) {
                            break;
                        } else {
                            Entity.ScannedEntity top = this.fEntityManager.getTopLevelEntity();
                            if (top != null && (top.version == null || top.version.equals("1.0"))) {
                                reportFatalError("VersionMismatch", null);
                            }
                            this.fEntityManager.setScannerVersion((short) 2);
                            break;
                        }
                    } else if (name.equals(fEncodingSymbol)) {
                        if (!scanningTextDecl) {
                            reportFatalError("VersionInfoRequired", null);
                        }
                        if (!sawSpace) {
                            reportFatalError(scanningTextDecl ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
                        }
                        encoding = this.fString.toString();
                        state = scanningTextDecl ? 3 : 2;
                        break;
                    } else if (scanningTextDecl) {
                        reportFatalError("EncodingDeclRequired", null);
                        break;
                    } else {
                        reportFatalError("VersionInfoRequired", null);
                        break;
                    }
                    break;
                case 1:
                    if (name.equals(fEncodingSymbol)) {
                        if (!sawSpace) {
                            reportFatalError(scanningTextDecl ? "SpaceRequiredBeforeEncodingInTextDecl" : "SpaceRequiredBeforeEncodingInXMLDecl", null);
                        }
                        encoding = this.fString.toString();
                        state = scanningTextDecl ? 3 : 2;
                        break;
                    } else if (!scanningTextDecl && name.equals(fStandaloneSymbol)) {
                        if (!sawSpace) {
                            reportFatalError("SpaceRequiredBeforeStandalone", null);
                        }
                        standalone = this.fString.toString();
                        state = 3;
                        if (!standalone.equals("yes") && !standalone.equals("no")) {
                            reportFatalError("SDDeclInvalid", new Object[]{standalone});
                            break;
                        } else {
                            break;
                        }
                    } else {
                        reportFatalError("EncodingDeclRequired", null);
                        break;
                    }
                    break;
                case 2:
                    if (name.equals(fStandaloneSymbol)) {
                        if (!sawSpace) {
                            reportFatalError("SpaceRequiredBeforeStandalone", null);
                        }
                        standalone = this.fString.toString();
                        state = 3;
                        if (!standalone.equals("yes") && !standalone.equals("no")) {
                            reportFatalError("SDDeclInvalid", new Object[]{standalone});
                            break;
                        } else {
                            break;
                        }
                    } else {
                        reportFatalError("SDDeclNameInvalid", null);
                        break;
                    }
                default:
                    reportFatalError("NoMorePseudoAttributes", null);
                    break;
            }
            sawSpace = this.fEntityScanner.skipSpaces();
        }
        if (currLiteral) {
            currEnt.literal = true;
        }
        if (scanningTextDecl && state != 3) {
            reportFatalError("MorePseudoAttributes", null);
        }
        if (scanningTextDecl) {
            if (!dataFoundForTarget && encoding == null) {
                reportFatalError("EncodingDeclRequired", null);
            }
        } else if (!dataFoundForTarget && version == null) {
            reportFatalError("VersionInfoRequired", null);
        }
        if (!this.fEntityScanner.skipChar(63, null)) {
            reportFatalError("XMLDeclUnterminated", null);
        }
        if (!this.fEntityScanner.skipChar(62, null)) {
            reportFatalError("XMLDeclUnterminated", null);
        }
        pseudoAttributeValues[0] = version;
        pseudoAttributeValues[1] = encoding;
        pseudoAttributeValues[2] = standalone;
    }

    protected String scanPseudoAttribute(boolean scanningTextDecl, XMLString value) throws IOException, XNIException {
        String name = scanPseudoAttributeName();
        if (name == null) {
            reportFatalError("PseudoAttrNameExpected", null);
        }
        this.fEntityScanner.skipSpaces();
        if (!this.fEntityScanner.skipChar(61, null)) {
            reportFatalError(scanningTextDecl ? "EqRequiredInTextDecl" : "EqRequiredInXMLDecl", new Object[]{name});
        }
        this.fEntityScanner.skipSpaces();
        int quote = this.fEntityScanner.peekChar();
        if (quote != 39 && quote != 34) {
            reportFatalError(scanningTextDecl ? "QuoteRequiredInTextDecl" : "QuoteRequiredInXMLDecl", new Object[]{name});
        }
        this.fEntityScanner.scanChar(NameType.ATTRIBUTE);
        int c2 = this.fEntityScanner.scanLiteral(quote, value, false);
        if (c2 != quote) {
            this.fStringBuffer2.clear();
            do {
                this.fStringBuffer2.append(value);
                if (c2 != -1) {
                    if (c2 == 38 || c2 == 37 || c2 == 60 || c2 == 93) {
                        this.fStringBuffer2.append((char) this.fEntityScanner.scanChar(NameType.ATTRIBUTE));
                    } else if (XMLChar.isHighSurrogate(c2)) {
                        scanSurrogates(this.fStringBuffer2);
                    } else if (isInvalidLiteral(c2)) {
                        String key = scanningTextDecl ? "InvalidCharInTextDecl" : "InvalidCharInXMLDecl";
                        reportFatalError(key, new Object[]{Integer.toString(c2, 16)});
                        this.fEntityScanner.scanChar(null);
                    }
                }
                c2 = this.fEntityScanner.scanLiteral(quote, value, false);
            } while (c2 != quote);
            this.fStringBuffer2.append(value);
            value.setValues(this.fStringBuffer2);
        }
        if (!this.fEntityScanner.skipChar(quote, null)) {
            reportFatalError(scanningTextDecl ? "CloseQuoteMissingInTextDecl" : "CloseQuoteMissingInXMLDecl", new Object[]{name});
        }
        return name;
    }

    private String scanPseudoAttributeName() throws IOException, XNIException {
        int ch = this.fEntityScanner.peekChar();
        switch (ch) {
            case 101:
                if (this.fEntityScanner.skipString(fEncodingSymbol)) {
                    return fEncodingSymbol;
                }
                return null;
            case 115:
                if (this.fEntityScanner.skipString(fStandaloneSymbol)) {
                    return fStandaloneSymbol;
                }
                return null;
            case 118:
                if (this.fEntityScanner.skipString(fVersionSymbol)) {
                    return fVersionSymbol;
                }
                return null;
            default:
                return null;
        }
    }

    protected void scanPI(XMLStringBuffer data) throws IOException, XNIException {
        this.fReportEntity = false;
        String target = this.fEntityScanner.scanName(NameType.PI);
        if (target == null) {
            reportFatalError("PITargetRequired", null);
        }
        scanPIData(target, data);
        this.fReportEntity = true;
    }

    protected void scanPIData(String target, XMLStringBuffer data) throws IOException, XNIException {
        if (target.length() == 3) {
            char c0 = Character.toLowerCase(target.charAt(0));
            char c1 = Character.toLowerCase(target.charAt(1));
            char c2 = Character.toLowerCase(target.charAt(2));
            if (c0 == 'x' && c1 == 'm' && c2 == 'l') {
                reportFatalError("ReservedPITarget", null);
            }
        }
        if (!this.fEntityScanner.skipSpaces()) {
            if (this.fEntityScanner.skipString("?>")) {
                return;
            } else {
                reportFatalError("SpaceRequiredInPI", null);
            }
        }
        if (this.fEntityScanner.scanData("?>", data)) {
            do {
                int c3 = this.fEntityScanner.peekChar();
                if (c3 != -1) {
                    if (XMLChar.isHighSurrogate(c3)) {
                        scanSurrogates(data);
                    } else if (isInvalidLiteral(c3)) {
                        reportFatalError("InvalidCharInPI", new Object[]{Integer.toHexString(c3)});
                        this.fEntityScanner.scanChar(null);
                    }
                }
            } while (this.fEntityScanner.scanData("?>", data));
        }
    }

    protected void scanComment(XMLStringBuffer text) throws IOException, XNIException {
        text.clear();
        while (this.fEntityScanner.scanData("--", text)) {
            int c2 = this.fEntityScanner.peekChar();
            if (c2 != -1) {
                if (XMLChar.isHighSurrogate(c2)) {
                    scanSurrogates(text);
                } else if (isInvalidLiteral(c2)) {
                    reportFatalError("InvalidCharInComment", new Object[]{Integer.toHexString(c2)});
                    this.fEntityScanner.scanChar(NameType.COMMENT);
                }
            }
        }
        if (!this.fEntityScanner.skipChar(62, NameType.COMMENT)) {
            reportFatalError("DashDashInComment", null);
        }
    }

    protected void scanAttributeValue(XMLString value, XMLString nonNormalizedValue, String atName, XMLAttributes attributes, int attrIndex, boolean checkEntities, String eleName, boolean isNSURI) throws IOException, XNIException {
        int ch;
        int quote = this.fEntityScanner.peekChar();
        if (quote != 39 && quote != 34) {
            reportFatalError("OpenQuoteExpected", new Object[]{eleName, atName});
        }
        this.fEntityScanner.scanChar(NameType.ATTRIBUTE);
        int entityDepth = this.fEntityDepth;
        int c2 = this.fEntityScanner.scanLiteral(quote, value, isNSURI);
        if (this.fNeedNonNormalizedValue) {
            this.fStringBuffer2.clear();
            this.fStringBuffer2.append(value);
        }
        if (this.fEntityScanner.whiteSpaceLen > 0) {
            normalizeWhitespace(value);
        }
        if (c2 != quote) {
            this.fScanningAttribute = true;
            XMLStringBuffer stringBuffer = getStringBuffer();
            stringBuffer.clear();
            while (true) {
                stringBuffer.append(value);
                if (c2 == 38) {
                    this.fEntityScanner.skipChar(38, NameType.REFERENCE);
                    if (entityDepth == this.fEntityDepth && this.fNeedNonNormalizedValue) {
                        this.fStringBuffer2.append('&');
                    }
                    if (this.fEntityScanner.skipChar(35, NameType.REFERENCE)) {
                        if (entityDepth == this.fEntityDepth && this.fNeedNonNormalizedValue) {
                            this.fStringBuffer2.append('#');
                        }
                        if (this.fNeedNonNormalizedValue) {
                            ch = scanCharReferenceValue(stringBuffer, this.fStringBuffer2);
                        } else {
                            ch = scanCharReferenceValue(stringBuffer, null);
                        }
                        if (ch != -1) {
                        }
                    } else {
                        String entityName = this.fEntityScanner.scanName(NameType.ENTITY);
                        if (entityName == null) {
                            reportFatalError("NameRequiredInReference", null);
                        } else if (entityDepth == this.fEntityDepth && this.fNeedNonNormalizedValue) {
                            this.fStringBuffer2.append(entityName);
                        }
                        if (!this.fEntityScanner.skipChar(59, NameType.REFERENCE)) {
                            reportFatalError("SemicolonRequiredInReference", new Object[]{entityName});
                        } else if (entityDepth == this.fEntityDepth && this.fNeedNonNormalizedValue) {
                            this.fStringBuffer2.append(';');
                        }
                        if (resolveCharacter(entityName, stringBuffer)) {
                            checkEntityLimit(false, this.fEntityScanner.fCurrentEntity.name, 1);
                        } else if (this.fEntityStore.isExternalEntity(entityName)) {
                            reportFatalError("ReferenceToExternalEntity", new Object[]{entityName});
                        } else {
                            if (!this.fEntityStore.isDeclaredEntity(entityName)) {
                                if (checkEntities) {
                                    if (this.fValidation) {
                                        this.fErrorReporter.reportError((XMLLocator) this.fEntityScanner, "http://www.w3.org/TR/1998/REC-xml-19980210", "EntityNotDeclared", new Object[]{entityName}, (short) 1);
                                    }
                                } else {
                                    reportFatalError("EntityNotDeclared", new Object[]{entityName});
                                }
                            }
                            this.fEntityManager.startEntity(true, entityName, true);
                        }
                    }
                } else if (c2 == 60) {
                    reportFatalError("LessthanInAttValue", new Object[]{eleName, atName});
                    this.fEntityScanner.scanChar(null);
                    if (entityDepth == this.fEntityDepth && this.fNeedNonNormalizedValue) {
                        this.fStringBuffer2.append((char) c2);
                    }
                } else if (c2 == 37 || c2 == 93) {
                    this.fEntityScanner.scanChar(null);
                    stringBuffer.append((char) c2);
                    if (entityDepth == this.fEntityDepth && this.fNeedNonNormalizedValue) {
                        this.fStringBuffer2.append((char) c2);
                    }
                } else if (c2 != -1 && XMLChar.isHighSurrogate(c2)) {
                    this.fStringBuffer3.clear();
                    if (scanSurrogates(this.fStringBuffer3)) {
                        stringBuffer.append(this.fStringBuffer3);
                        if (entityDepth == this.fEntityDepth && this.fNeedNonNormalizedValue) {
                            this.fStringBuffer2.append(this.fStringBuffer3);
                        }
                    }
                } else if (c2 != -1 && isInvalidLiteral(c2)) {
                    reportFatalError("InvalidCharInAttValue", new Object[]{eleName, atName, Integer.toString(c2, 16)});
                    this.fEntityScanner.scanChar(null);
                    if (entityDepth == this.fEntityDepth && this.fNeedNonNormalizedValue) {
                        this.fStringBuffer2.append((char) c2);
                    }
                }
                c2 = this.fEntityScanner.scanLiteral(quote, value, isNSURI);
                if (entityDepth == this.fEntityDepth && this.fNeedNonNormalizedValue) {
                    this.fStringBuffer2.append(value);
                }
                if (this.fEntityScanner.whiteSpaceLen > 0) {
                    normalizeWhitespace(value);
                }
                if (c2 == quote && entityDepth == this.fEntityDepth) {
                    break;
                }
            }
            stringBuffer.append(value);
            value.setValues(stringBuffer);
            this.fScanningAttribute = false;
        }
        if (this.fNeedNonNormalizedValue) {
            nonNormalizedValue.setValues(this.fStringBuffer2);
        }
        int cquote = this.fEntityScanner.scanChar(NameType.ATTRIBUTE);
        if (cquote != quote) {
            reportFatalError("CloseQuoteExpected", new Object[]{eleName, atName});
        }
    }

    protected boolean resolveCharacter(String entityName, XMLStringBuffer stringBuffer) throws XNIException {
        if (entityName == fAmpSymbol) {
            stringBuffer.append('&');
            return true;
        }
        if (entityName == fAposSymbol) {
            stringBuffer.append('\'');
            return true;
        }
        if (entityName == fLtSymbol) {
            stringBuffer.append('<');
            return true;
        }
        if (entityName == fGtSymbol) {
            checkEntityLimit(false, this.fEntityScanner.fCurrentEntity.name, 1);
            stringBuffer.append('>');
            return true;
        }
        if (entityName == fQuotSymbol) {
            checkEntityLimit(false, this.fEntityScanner.fCurrentEntity.name, 1);
            stringBuffer.append('\"');
            return true;
        }
        return false;
    }

    protected void scanExternalID(String[] identifiers, boolean optionalSystemId) throws IOException, XNIException {
        String systemId = null;
        String publicId = null;
        if (this.fEntityScanner.skipString("PUBLIC")) {
            if (!this.fEntityScanner.skipSpaces()) {
                reportFatalError("SpaceRequiredAfterPUBLIC", null);
            }
            scanPubidLiteral(this.fString);
            publicId = this.fString.toString();
            if (!this.fEntityScanner.skipSpaces() && !optionalSystemId) {
                reportFatalError("SpaceRequiredBetweenPublicAndSystem", null);
            }
        }
        if (publicId != null || this.fEntityScanner.skipString(Clipboard.SYSTEM)) {
            if (publicId == null && !this.fEntityScanner.skipSpaces()) {
                reportFatalError("SpaceRequiredAfterSYSTEM", null);
            }
            int quote = this.fEntityScanner.peekChar();
            if (quote != 39 && quote != 34) {
                if (publicId != null && optionalSystemId) {
                    identifiers[0] = null;
                    identifiers[1] = publicId;
                    return;
                }
                reportFatalError("QuoteRequiredInSystemID", null);
            }
            this.fEntityScanner.scanChar(null);
            XMLString ident = this.fString;
            if (this.fEntityScanner.scanLiteral(quote, ident, false) != quote) {
                this.fStringBuffer.clear();
                do {
                    this.fStringBuffer.append(ident);
                    int c2 = this.fEntityScanner.peekChar();
                    if (XMLChar.isMarkup(c2) || c2 == 93) {
                        this.fStringBuffer.append((char) this.fEntityScanner.scanChar(null));
                    } else if (c2 != -1 && isInvalidLiteral(c2)) {
                        reportFatalError("InvalidCharInSystemID", new Object[]{Integer.toString(c2, 16)});
                    }
                } while (this.fEntityScanner.scanLiteral(quote, ident, false) != quote);
                this.fStringBuffer.append(ident);
                ident = this.fStringBuffer;
            }
            systemId = ident.toString();
            if (!this.fEntityScanner.skipChar(quote, null)) {
                reportFatalError("SystemIDUnterminated", null);
            }
        }
        identifiers[0] = systemId;
        identifiers[1] = publicId;
    }

    protected boolean scanPubidLiteral(XMLString literal) throws IOException, XNIException {
        int quote = this.fEntityScanner.scanChar(null);
        if (quote != 39 && quote != 34) {
            reportFatalError("QuoteRequiredInPublicID", null);
            return false;
        }
        this.fStringBuffer.clear();
        boolean skipSpace = true;
        boolean dataok = true;
        while (true) {
            int c2 = this.fEntityScanner.scanChar(null);
            if (c2 == 32 || c2 == 10 || c2 == 13) {
                if (!skipSpace) {
                    this.fStringBuffer.append(' ');
                    skipSpace = true;
                }
            } else {
                if (c2 == quote) {
                    if (skipSpace) {
                        this.fStringBuffer.length--;
                    }
                    literal.setValues(this.fStringBuffer);
                    return dataok;
                }
                if (XMLChar.isPubid(c2)) {
                    this.fStringBuffer.append((char) c2);
                    skipSpace = false;
                } else {
                    if (c2 == -1) {
                        reportFatalError("PublicIDUnterminated", null);
                        return false;
                    }
                    dataok = false;
                    reportFatalError("InvalidCharInPublicID", new Object[]{Integer.toHexString(c2)});
                }
            }
        }
    }

    protected void normalizeWhitespace(XMLString value) {
        int[] buff = this.fEntityScanner.whiteSpaceLookup;
        int buffLen = this.fEntityScanner.whiteSpaceLen;
        int end = value.offset + value.length;
        for (int i2 = 0; i2 < buffLen; i2++) {
            int j2 = buff[i2];
            if (j2 < end) {
                value.ch[j2] = ' ';
            }
        }
    }

    public void startEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
        this.fEntityDepth++;
        this.fEntityScanner = this.fEntityManager.getEntityScanner();
        this.fEntityStore = this.fEntityManager.getEntityStore();
    }

    public void endEntity(String name, Augmentations augs) throws IOException, XNIException {
        if (this.fEntityDepth > 0) {
            this.fEntityDepth--;
        }
    }

    protected int scanCharReferenceValue(XMLStringBuffer buf, XMLStringBuffer buf2) throws IOException, XNIException {
        boolean digit;
        boolean digit2;
        int initLen = buf.length;
        boolean hex = false;
        if (this.fEntityScanner.skipChar(120, NameType.REFERENCE)) {
            if (buf2 != null) {
                buf2.append('x');
            }
            hex = true;
            this.fStringBuffer3.clear();
            int c2 = this.fEntityScanner.peekChar();
            if ((c2 >= 48 && c2 <= 57) || (c2 >= 97 && c2 <= 102) || (c2 >= 65 && c2 <= 70)) {
                if (buf2 != null) {
                    buf2.append((char) c2);
                }
                this.fEntityScanner.scanChar(NameType.REFERENCE);
                this.fStringBuffer3.append((char) c2);
                do {
                    int c3 = this.fEntityScanner.peekChar();
                    digit2 = (c3 >= 48 && c3 <= 57) || (c3 >= 97 && c3 <= 102) || (c3 >= 65 && c3 <= 70);
                    if (digit2) {
                        if (buf2 != null) {
                            buf2.append((char) c3);
                        }
                        this.fEntityScanner.scanChar(NameType.REFERENCE);
                        this.fStringBuffer3.append((char) c3);
                    }
                } while (digit2);
            } else {
                reportFatalError("HexdigitRequiredInCharRef", null);
            }
        } else {
            this.fStringBuffer3.clear();
            int c4 = this.fEntityScanner.peekChar();
            if (c4 >= 48 && c4 <= 57) {
                if (buf2 != null) {
                    buf2.append((char) c4);
                }
                this.fEntityScanner.scanChar(NameType.REFERENCE);
                this.fStringBuffer3.append((char) c4);
                do {
                    int c5 = this.fEntityScanner.peekChar();
                    digit = c5 >= 48 && c5 <= 57;
                    if (digit) {
                        if (buf2 != null) {
                            buf2.append((char) c5);
                        }
                        this.fEntityScanner.scanChar(NameType.REFERENCE);
                        this.fStringBuffer3.append((char) c5);
                    }
                } while (digit);
            } else {
                reportFatalError("DigitRequiredInCharRef", null);
            }
        }
        if (!this.fEntityScanner.skipChar(59, NameType.REFERENCE)) {
            reportFatalError("SemicolonRequiredInCharRef", null);
        }
        if (buf2 != null) {
            buf2.append(';');
        }
        int value = -1;
        try {
            value = Integer.parseInt(this.fStringBuffer3.toString(), hex ? 16 : 10);
            if (isInvalid(value)) {
                StringBuffer errorBuf = new StringBuffer(this.fStringBuffer3.length + 1);
                if (hex) {
                    errorBuf.append('x');
                }
                errorBuf.append(this.fStringBuffer3.ch, this.fStringBuffer3.offset, this.fStringBuffer3.length);
                reportFatalError("InvalidCharRef", new Object[]{errorBuf.toString()});
            }
        } catch (NumberFormatException e2) {
            StringBuffer errorBuf2 = new StringBuffer(this.fStringBuffer3.length + 1);
            if (hex) {
                errorBuf2.append('x');
            }
            errorBuf2.append(this.fStringBuffer3.ch, this.fStringBuffer3.offset, this.fStringBuffer3.length);
            reportFatalError("InvalidCharRef", new Object[]{errorBuf2.toString()});
        }
        if (!XMLChar.isSupplemental(value)) {
            buf.append((char) value);
        } else {
            buf.append(XMLChar.highSurrogate(value));
            buf.append(XMLChar.lowSurrogate(value));
        }
        if (this.fNotifyCharRefs && value != -1) {
            String literal = FXMLLoader.CONTROLLER_METHOD_PREFIX + (hex ? LanguageTag.PRIVATEUSE : "") + this.fStringBuffer3.toString();
            if (!this.fScanningAttribute) {
                this.fCharRefLiteral = literal;
            }
        }
        if (this.fEntityScanner.fCurrentEntity.isGE) {
            checkEntityLimit(false, this.fEntityScanner.fCurrentEntity.name, buf.length - initLen);
        }
        return value;
    }

    protected boolean isInvalid(int value) {
        return XMLChar.isInvalid(value);
    }

    protected boolean isInvalidLiteral(int value) {
        return XMLChar.isInvalid(value);
    }

    protected boolean isValidNameChar(int value) {
        return XMLChar.isName(value);
    }

    protected boolean isValidNCName(int value) {
        return XMLChar.isNCName(value);
    }

    protected boolean isValidNameStartChar(int value) {
        return XMLChar.isNameStart(value);
    }

    protected boolean isValidNameStartHighSurrogate(int value) {
        return false;
    }

    protected boolean versionSupported(String version) {
        return version.equals("1.0") || version.equals(SerializerConstants.XMLVERSION11);
    }

    protected boolean scanSurrogates(XMLStringBuffer buf) throws IOException, XNIException {
        int high = this.fEntityScanner.scanChar(null);
        int low = this.fEntityScanner.peekChar();
        if (!XMLChar.isLowSurrogate(low)) {
            reportFatalError("InvalidCharInContent", new Object[]{Integer.toString(high, 16)});
            return false;
        }
        this.fEntityScanner.scanChar(null);
        int c2 = XMLChar.supplemental((char) high, (char) low);
        if (isInvalid(c2)) {
            reportFatalError("InvalidCharInContent", new Object[]{Integer.toString(c2, 16)});
            return false;
        }
        buf.append((char) high);
        buf.append((char) low);
        return true;
    }

    protected void reportFatalError(String msgId, Object[] args) throws XNIException {
        this.fErrorReporter.reportError((XMLLocator) this.fEntityScanner, "http://www.w3.org/TR/1998/REC-xml-19980210", msgId, args, (short) 2);
    }

    private void init() {
        this.fEntityScanner = null;
        this.fEntityDepth = 0;
        this.fReportEntity = true;
        this.fResourceIdentifier.clear();
        if (!this.fAttributeCacheInitDone) {
            for (int i2 = 0; i2 < this.initialCacheCount; i2++) {
                this.attributeValueCache.add(new XMLString());
                this.stringBufferCache.add(new XMLStringBuffer());
            }
            this.fAttributeCacheInitDone = true;
        }
        this.fStringBufferIndex = 0;
        this.fAttributeCacheUsedCount = 0;
    }

    XMLStringBuffer getStringBuffer() {
        if (this.fStringBufferIndex < this.initialCacheCount || this.fStringBufferIndex < this.stringBufferCache.size()) {
            ArrayList<XMLStringBuffer> arrayList = this.stringBufferCache;
            int i2 = this.fStringBufferIndex;
            this.fStringBufferIndex = i2 + 1;
            return arrayList.get(i2);
        }
        XMLStringBuffer tmpObj = new XMLStringBuffer();
        this.fStringBufferIndex++;
        this.stringBufferCache.add(tmpObj);
        return tmpObj;
    }

    void checkEntityLimit(boolean isPEDecl, String entityName, XMLString buffer) throws XNIException {
        checkEntityLimit(isPEDecl, entityName, buffer.length);
    }

    void checkEntityLimit(boolean isPEDecl, String entityName, int len) throws XNIException {
        if (this.fLimitAnalyzer == null) {
            this.fLimitAnalyzer = this.fEntityManager.fLimitAnalyzer;
        }
        if (isPEDecl) {
            this.fLimitAnalyzer.addValue(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT, FXMLLoader.RESOURCE_KEY_PREFIX + entityName, len);
            if (this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT, this.fLimitAnalyzer)) {
                this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
                reportFatalError("MaxEntitySizeLimit", new Object[]{FXMLLoader.RESOURCE_KEY_PREFIX + entityName, Integer.valueOf(this.fLimitAnalyzer.getValue(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT)), Integer.valueOf(this.fSecurityManager.getLimit(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT)), this.fSecurityManager.getStateLiteral(XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT)});
            }
        } else {
            this.fLimitAnalyzer.addValue(XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT, entityName, len);
            if (this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT, this.fLimitAnalyzer)) {
                this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
                reportFatalError("MaxEntitySizeLimit", new Object[]{entityName, Integer.valueOf(this.fLimitAnalyzer.getValue(XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT)), Integer.valueOf(this.fSecurityManager.getLimit(XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT)), this.fSecurityManager.getStateLiteral(XMLSecurityManager.Limit.GENERAL_ENTITY_SIZE_LIMIT)});
            }
        }
        if (this.fSecurityManager.isOverLimit(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT, this.fLimitAnalyzer)) {
            this.fSecurityManager.debugPrint(this.fLimitAnalyzer);
            reportFatalError("TotalEntitySizeLimit", new Object[]{Integer.valueOf(this.fLimitAnalyzer.getTotalValue(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)), Integer.valueOf(this.fSecurityManager.getLimit(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)), this.fSecurityManager.getStateLiteral(XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT)});
        }
    }
}
