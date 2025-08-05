package com.sun.org.apache.xerces.internal.xinclude;

import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar;
import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import java.util.Enumeration;
import java.util.Stack;
import java.util.StringTokenizer;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/XPointerElementHandler.class */
public class XPointerElementHandler implements XPointerSchema {
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
    protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
    protected XMLDocumentHandler fDocumentHandler;
    protected XMLDocumentSource fDocumentSource;
    protected XIncludeHandler fParentXIncludeHandler;
    protected XMLLocator fDocLocation;
    protected XIncludeNamespaceSupport fNamespaceContext;
    protected XMLErrorReporter fErrorReporter;
    protected XMLGrammarPool fGrammarPool;
    protected XMLGrammarDescription fGrammarDesc;
    protected DTDGrammar fDTDGrammar;
    protected XMLEntityResolver fEntityResolver;
    protected ParserConfigurationSettings fSettings;
    protected StringBuffer fPointer;
    private static final int INITIAL_SIZE = 8;
    String fSchemaName;
    String fSchemaPointer;
    boolean fSubResourceIdentified;
    int fCurrentToken;
    boolean includeElement;
    private static final String[] RECOGNIZED_FEATURES = new String[0];
    private static final Boolean[] FEATURE_DEFAULTS = new Boolean[0];
    protected static final String XPOINTER_SCHEMA = "http://apache.org/xml/properties/xpointer-schema";
    private static final String[] RECOGNIZED_PROPERTIES = {"http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/grammar-pool", "http://apache.org/xml/properties/internal/entity-resolver", XPOINTER_SCHEMA};
    private static final Object[] PROPERTY_DEFAULTS = {null, null, null, null};
    private int elemCount = 0;
    private boolean[] fSawInclude = new boolean[8];
    private boolean[] fSawFallback = new boolean[8];
    private int[] fState = new int[8];
    QName foundElement = null;
    boolean skip = false;
    Stack fPointerToken = new Stack();
    int fCurrentTokenint = 0;
    String fCurrentTokenString = null;
    int fCurrentTokenType = 0;
    Stack ftempCurrentElement = new Stack();
    int fElementCount = 0;
    private int fDepth = 0;
    private int fRootDepth = 0;

    public XPointerElementHandler() {
        this.fSawFallback[this.fDepth] = false;
        this.fSawInclude[this.fDepth] = false;
        this.fSchemaName = "element";
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XPointerSchema
    public void reset() {
        this.elemCount = 0;
        this.fPointerToken = null;
        this.fCurrentTokenint = 0;
        this.fCurrentTokenString = null;
        this.fCurrentTokenType = 0;
        this.fElementCount = 0;
        this.fCurrentToken = 0;
        this.includeElement = false;
        this.foundElement = null;
        this.skip = false;
        this.fSubResourceIdentified = false;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void reset(XMLComponentManager componentManager) throws XNIException {
        this.fNamespaceContext = null;
        this.elemCount = 0;
        this.fDepth = 0;
        this.fRootDepth = 0;
        this.fPointerToken = null;
        this.fCurrentTokenint = 0;
        this.fCurrentTokenString = null;
        this.fCurrentTokenType = 0;
        this.foundElement = null;
        this.includeElement = false;
        this.skip = false;
        this.fSubResourceIdentified = false;
        try {
            setErrorReporter((XMLErrorReporter) componentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter"));
        } catch (XMLConfigurationException e2) {
            this.fErrorReporter = null;
        }
        try {
            this.fGrammarPool = (XMLGrammarPool) componentManager.getProperty("http://apache.org/xml/properties/internal/grammar-pool");
        } catch (XMLConfigurationException e3) {
            this.fGrammarPool = null;
        }
        try {
            this.fEntityResolver = (XMLEntityResolver) componentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver");
        } catch (XMLConfigurationException e4) {
            this.fEntityResolver = null;
        }
        this.fSettings = new ParserConfigurationSettings();
        Enumeration xercesFeatures = Constants.getXercesFeatures();
        while (xercesFeatures.hasMoreElements()) {
            String featureId = (String) xercesFeatures.nextElement();
            this.fSettings.addRecognizedFeatures(new String[]{featureId});
            try {
                this.fSettings.setFeature(featureId, componentManager.getFeature(featureId));
            } catch (XMLConfigurationException e5) {
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedFeatures() {
        return RECOGNIZED_FEATURES;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
        if (this.fSettings != null) {
            this.fSettings.setFeature(featureId, state);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public String[] getRecognizedProperties() {
        return RECOGNIZED_PROPERTIES;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLComponent
    public void setProperty(String propertyId, Object value) throws XMLConfigurationException {
        if (propertyId.equals("http://apache.org/xml/properties/internal/error-reporter")) {
            setErrorReporter((XMLErrorReporter) value);
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/grammar-pool")) {
            this.fGrammarPool = (XMLGrammarPool) value;
        }
        if (propertyId.equals("http://apache.org/xml/properties/internal/entity-resolver")) {
            this.fEntityResolver = (XMLEntityResolver) value;
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

    private void setErrorReporter(XMLErrorReporter reporter) {
        this.fErrorReporter = reporter;
        if (this.fErrorReporter != null) {
            this.fErrorReporter.putMessageFormatter(XIncludeMessageFormatter.XINCLUDE_DOMAIN, new XIncludeMessageFormatter());
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public void setDocumentHandler(XMLDocumentHandler handler) {
        this.fDocumentHandler = handler;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource
    public XMLDocumentHandler getDocumentHandler() {
        return this.fDocumentHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XPointerSchema
    public void setXPointerSchemaName(String schemaName) {
        this.fSchemaName = schemaName;
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XPointerSchema
    public String getXpointerSchemaName() {
        return this.fSchemaName;
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XPointerSchema
    public void setParent(Object parent) {
        this.fParentXIncludeHandler = (XIncludeHandler) parent;
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XPointerSchema
    public Object getParent() {
        return this.fParentXIncludeHandler;
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XPointerSchema
    public void setXPointerSchemaPointer(String content) {
        this.fSchemaPointer = content;
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XPointerSchema
    public String getXPointerSchemaPointer() {
        return this.fSchemaPointer;
    }

    @Override // com.sun.org.apache.xerces.internal.xinclude.XPointerSchema
    public boolean isSubResourceIndentified() {
        return this.fSubResourceIdentified;
    }

    public void getTokens() {
        this.fSchemaPointer = this.fSchemaPointer.substring(this.fSchemaPointer.indexOf("(") + 1, this.fSchemaPointer.length());
        StringTokenizer st = new StringTokenizer(this.fSchemaPointer, "/");
        Stack tempPointerToken = new Stack();
        if (this.fPointerToken == null) {
            this.fPointerToken = new Stack();
        }
        while (st.hasMoreTokens()) {
            String tempToken = st.nextToken();
            try {
                Integer integerToken = Integer.valueOf(tempToken);
                tempPointerToken.push(integerToken);
            } catch (NumberFormatException e2) {
                tempPointerToken.push(tempToken);
            }
        }
        while (!tempPointerToken.empty()) {
            this.fPointerToken.push(tempPointerToken.pop());
        }
    }

    public boolean hasMoreToken() {
        if (this.fPointerToken.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean getNextToken() {
        if (!this.fPointerToken.isEmpty()) {
            Object currentToken = this.fPointerToken.pop();
            if (currentToken instanceof Integer) {
                this.fCurrentTokenint = ((Integer) currentToken).intValue();
                this.fCurrentTokenType = 1;
                return true;
            }
            this.fCurrentTokenString = ((String) currentToken).toString();
            this.fCurrentTokenType = 2;
            return true;
        }
        return false;
    }

    private boolean isIdAttribute(XMLAttributes attributes, Augmentations augs, int index) {
        Object o2 = augs.getItem(Constants.ID_ATTRIBUTE);
        if (o2 instanceof Boolean) {
            return ((Boolean) o2).booleanValue();
        }
        return "ID".equals(attributes.getType(index));
    }

    public boolean checkStringToken(QName element, XMLAttributes attributes) {
        QName attrName = new QName();
        int attrCount = attributes.getLength();
        for (int i2 = 0; i2 < attrCount; i2++) {
            Augmentations aaugs = attributes.getAugmentations(i2);
            attributes.getName(i2, attrName);
            String attrType = attributes.getType(i2);
            String attrValue = attributes.getValue(i2);
            if (attrType != null && attrValue != null && isIdAttribute(attributes, aaugs, i2) && attrValue.equals(this.fCurrentTokenString)) {
                if (hasMoreToken()) {
                    this.fCurrentTokenType = 0;
                    this.fCurrentTokenString = null;
                    return true;
                }
                this.foundElement = element;
                this.includeElement = true;
                this.fCurrentTokenType = 0;
                this.fCurrentTokenString = null;
                this.fSubResourceIdentified = true;
                return true;
            }
        }
        return false;
    }

    public boolean checkIntegerToken(QName element) {
        if (!this.skip) {
            this.fElementCount++;
            if (this.fCurrentTokenint == this.fElementCount) {
                if (hasMoreToken()) {
                    this.fElementCount = 0;
                    this.fCurrentTokenType = 0;
                    return true;
                }
                this.foundElement = element;
                this.includeElement = true;
                this.fCurrentTokenType = 0;
                this.fElementCount = 0;
                this.fSubResourceIdentified = true;
                return true;
            }
            addQName(element);
            this.skip = true;
            return false;
        }
        return false;
    }

    public void addQName(QName element) {
        QName cacheQName = new QName(element);
        this.ftempCurrentElement.push(cacheQName);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        getTokens();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && this.includeElement) {
            this.fDocumentHandler.comment(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && this.includeElement) {
            this.fDocumentHandler.processingInstruction(target, data, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        boolean requiredToken = false;
        if (this.fCurrentTokenType == 0) {
            getNextToken();
        }
        if (this.fCurrentTokenType == 1) {
            requiredToken = checkIntegerToken(element);
        } else if (this.fCurrentTokenType == 2) {
            requiredToken = checkStringToken(element, attributes);
        }
        if (requiredToken && hasMoreToken()) {
            getNextToken();
        }
        if (this.fDocumentHandler != null && this.includeElement) {
            this.elemCount++;
            this.fDocumentHandler.startElement(element, attributes, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        if (this.includeElement && this.foundElement != null) {
            if (this.elemCount > 0) {
                this.elemCount--;
            }
            this.fDocumentHandler.endElement(element, augs);
            if (this.elemCount == 0) {
                this.includeElement = false;
                return;
            }
            return;
        }
        if (!this.ftempCurrentElement.empty()) {
            QName name = (QName) this.ftempCurrentElement.peek();
            if (name.equals(element)) {
                this.ftempCurrentElement.pop();
                this.skip = false;
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && this.includeElement) {
            this.fDocumentHandler.emptyElement(element, attributes, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startGeneralEntity(String name, XMLResourceIdentifier resId, String encoding, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && this.includeElement) {
            this.fDocumentHandler.startGeneralEntity(name, resId, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && this.includeElement) {
            this.fDocumentHandler.textDecl(version, encoding, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null) {
            this.fDocumentHandler.endGeneralEntity(name, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && this.includeElement) {
            this.fDocumentHandler.characters(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && this.includeElement) {
            this.fDocumentHandler.ignorableWhitespace(text, augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && this.includeElement) {
            this.fDocumentHandler.startCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
        if (this.fDocumentHandler != null && this.includeElement) {
            this.fDocumentHandler.endCDATA(augs);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void setDocumentSource(XMLDocumentSource source) {
        this.fDocumentSource = source;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public XMLDocumentSource getDocumentSource() {
        return this.fDocumentSource;
    }

    protected void reportFatalError(String key) throws XNIException {
        reportFatalError(key, null);
    }

    protected void reportFatalError(String key, Object[] args) throws XNIException {
        if (this.fErrorReporter != null) {
            this.fErrorReporter.reportError(this.fDocLocation, XIncludeMessageFormatter.XINCLUDE_DOMAIN, key, args, (short) 2);
        }
    }

    protected boolean isRootDocument() {
        return this.fParentXIncludeHandler == null;
    }
}
