package com.sun.org.apache.xerces.internal.impl;

import com.sun.org.apache.xerces.internal.util.NamespaceContextWrapper;
import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLAttributesImpl;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import com.sun.xml.internal.stream.Entity;
import com.sun.xml.internal.stream.StaxErrorReporter;
import com.sun.xml.internal.stream.XMLEntityStorage;
import com.sun.xml.internal.stream.dtd.nonvalidating.DTDGrammar;
import com.sun.xml.internal.stream.dtd.nonvalidating.XMLNotationDecl;
import com.sun.xml.internal.stream.events.EntityDeclarationImpl;
import com.sun.xml.internal.stream.events.NotationDeclarationImpl;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/XMLStreamReaderImpl.class */
public class XMLStreamReaderImpl implements XMLStreamReader {
    protected static final String ENTITY_MANAGER = "http://apache.org/xml/properties/internal/entity-manager";
    protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String READER_IN_DEFINED_STATE = "http://java.sun.com/xml/stream/properties/reader-in-defined-state";
    private int fEventType;
    static final boolean DEBUG = false;
    private SymbolTable fSymbolTable = new SymbolTable();
    protected XMLDocumentScannerImpl fScanner = new XMLNSDocumentScannerImpl();
    protected NamespaceContextWrapper fNamespaceContextWrapper = new NamespaceContextWrapper((NamespaceSupport) this.fScanner.getNamespaceContext());
    protected XMLEntityManager fEntityManager = new XMLEntityManager();
    protected StaxErrorReporter fErrorReporter = new StaxErrorReporter();
    protected XMLEntityScanner fEntityScanner = null;
    protected XMLInputSource fInputSource = null;
    protected PropertyManager fPropertyManager = null;
    private boolean fReuse = true;
    private boolean fReaderInDefinedState = true;
    private boolean fBindNamespaces = true;
    private String fDTDDecl = null;
    private String versionStr = null;

    public XMLStreamReaderImpl(InputStream inputStream, PropertyManager props) throws XMLStreamException {
        init(props);
        XMLInputSource inputSource = new XMLInputSource((String) null, (String) null, (String) null, inputStream, (String) null);
        setInputSource(inputSource);
    }

    public XMLDocumentScannerImpl getScanner() {
        System.out.println("returning scanner");
        return this.fScanner;
    }

    public XMLStreamReaderImpl(String systemid, PropertyManager props) throws XMLStreamException {
        init(props);
        XMLInputSource inputSource = new XMLInputSource(null, systemid, null);
        setInputSource(inputSource);
    }

    public XMLStreamReaderImpl(InputStream inputStream, String encoding, PropertyManager props) throws XMLStreamException {
        init(props);
        XMLInputSource inputSource = new XMLInputSource((String) null, (String) null, (String) null, new BufferedInputStream(inputStream), encoding);
        setInputSource(inputSource);
    }

    public XMLStreamReaderImpl(Reader reader, PropertyManager props) throws XMLStreamException {
        init(props);
        XMLInputSource inputSource = new XMLInputSource((String) null, (String) null, (String) null, new BufferedReader(reader), (String) null);
        setInputSource(inputSource);
    }

    public XMLStreamReaderImpl(XMLInputSource inputSource, PropertyManager props) throws XMLStreamException {
        init(props);
        setInputSource(inputSource);
    }

    public void setInputSource(XMLInputSource inputSource) throws XMLStreamException {
        this.fReuse = false;
        try {
            this.fScanner.setInputSource(inputSource);
            if (this.fReaderInDefinedState) {
                this.fEventType = this.fScanner.next();
                if (this.versionStr == null) {
                    this.versionStr = getVersion();
                }
                if (this.fEventType == 7 && this.versionStr != null && this.versionStr.equals(SerializerConstants.XMLVERSION11)) {
                    switchToXML11Scanner();
                }
            }
        } catch (XNIException ex) {
            throw new XMLStreamException(ex.getMessage(), getLocation(), ex.getException());
        } catch (IOException ex2) {
            throw new XMLStreamException(ex2);
        }
    }

    void init(PropertyManager propertyManager) throws XMLStreamException {
        this.fPropertyManager = propertyManager;
        propertyManager.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
        propertyManager.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
        propertyManager.setProperty(ENTITY_MANAGER, this.fEntityManager);
        reset();
    }

    public boolean canReuse() {
        return this.fReuse;
    }

    public void reset() {
        this.fReuse = true;
        this.fEventType = 0;
        this.fEntityManager.reset(this.fPropertyManager);
        this.fScanner.reset(this.fPropertyManager);
        this.fDTDDecl = null;
        this.fEntityScanner = this.fEntityManager.getEntityScanner();
        this.fReaderInDefinedState = ((Boolean) this.fPropertyManager.getProperty("http://java.sun.com/xml/stream/properties/reader-in-defined-state")).booleanValue();
        this.fBindNamespaces = ((Boolean) this.fPropertyManager.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE)).booleanValue();
        this.versionStr = null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void close() throws XMLStreamException {
        this.fReuse = true;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getCharacterEncodingScheme() {
        return this.fScanner.getCharacterEncodingScheme();
    }

    public int getColumnNumber() {
        return this.fEntityScanner.getColumnNumber();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getEncoding() {
        return this.fEntityScanner.getEncoding();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getEventType() {
        return this.fEventType;
    }

    public int getLineNumber() {
        return this.fEntityScanner.getLineNumber();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getLocalName() {
        if (this.fEventType == 1 || this.fEventType == 2) {
            return this.fScanner.getElementQName().localpart;
        }
        if (this.fEventType == 9) {
            return this.fScanner.getEntityName();
        }
        throw new IllegalStateException("Method getLocalName() cannot be called for " + getEventTypeString(this.fEventType) + " event.");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI() {
        if (this.fEventType == 1 || this.fEventType == 2) {
            return this.fScanner.getElementQName().uri;
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPIData() {
        if (this.fEventType == 3) {
            return this.fScanner.getPIData().toString();
        }
        throw new IllegalStateException("Current state of the parser is " + getEventTypeString(this.fEventType) + " But Expected state is 3");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPITarget() {
        if (this.fEventType == 3) {
            return this.fScanner.getPITarget();
        }
        throw new IllegalStateException("Current state of the parser is " + getEventTypeString(this.fEventType) + " But Expected state is 3");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPrefix() {
        if (this.fEventType == 1 || this.fEventType == 2) {
            String prefix = this.fScanner.getElementQName().prefix;
            return prefix == null ? "" : prefix;
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public char[] getTextCharacters() {
        if (this.fEventType == 4 || this.fEventType == 5 || this.fEventType == 12 || this.fEventType == 6) {
            return this.fScanner.getCharacterData().ch;
        }
        throw new IllegalStateException("Current state = " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(4) + " , " + getEventTypeString(5) + " , " + getEventTypeString(12) + " , " + getEventTypeString(6) + " valid for getTextCharacters() ");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextLength() {
        if (this.fEventType == 4 || this.fEventType == 5 || this.fEventType == 12 || this.fEventType == 6) {
            return this.fScanner.getCharacterData().length;
        }
        throw new IllegalStateException("Current state = " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(4) + " , " + getEventTypeString(5) + " , " + getEventTypeString(12) + " , " + getEventTypeString(6) + " valid for getTextLength() ");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextStart() {
        if (this.fEventType == 4 || this.fEventType == 5 || this.fEventType == 12 || this.fEventType == 6) {
            return this.fScanner.getCharacterData().offset;
        }
        throw new IllegalStateException("Current state = " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(4) + " , " + getEventTypeString(5) + " , " + getEventTypeString(12) + " , " + getEventTypeString(6) + " valid for getTextStart() ");
    }

    public String getValue() {
        if (this.fEventType == 3) {
            return this.fScanner.getPIData().toString();
        }
        if (this.fEventType == 5) {
            return this.fScanner.getComment();
        }
        if (this.fEventType == 1 || this.fEventType == 2) {
            return this.fScanner.getElementQName().localpart;
        }
        if (this.fEventType == 4) {
            return this.fScanner.getCharacterData().toString();
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getVersion() {
        String version = this.fEntityScanner.getXMLVersion();
        if (!"1.0".equals(version) || this.fEntityScanner.xmlVersionSetExplicitly) {
            return version;
        }
        return null;
    }

    public boolean hasAttributes() {
        return this.fScanner.getAttributeIterator().getLength() > 0;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasName() {
        if (this.fEventType == 1 || this.fEventType == 2) {
            return true;
        }
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasNext() throws XMLStreamException {
        return (this.fEventType == -1 || this.fEventType == 8) ? false : true;
    }

    public boolean hasValue() {
        if (this.fEventType == 1 || this.fEventType == 2 || this.fEventType == 9 || this.fEventType == 3 || this.fEventType == 5 || this.fEventType == 4) {
            return true;
        }
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isEndElement() {
        return this.fEventType == 2;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStandalone() {
        return this.fScanner.isStandAlone();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStartElement() {
        return this.fEventType == 1;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isWhiteSpace() {
        if (isCharacters() || this.fEventType == 12) {
            char[] ch = getTextCharacters();
            int start = getTextStart();
            int end = start + getTextLength();
            for (int i2 = start; i2 < end; i2++) {
                if (!XMLChar.isSpace(ch[i2])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int next() throws XMLStreamException {
        Boolean isValidating;
        if (!hasNext()) {
            if (this.fEventType != -1) {
                throw new NoSuchElementException("END_DOCUMENT reached: no more elements on the stream.");
            }
            throw new XMLStreamException("Error processing input source. The input stream is not complete.");
        }
        try {
            this.fEventType = this.fScanner.next();
            if (this.versionStr == null) {
                this.versionStr = getVersion();
            }
            if (this.fEventType == 7 && this.versionStr != null && this.versionStr.equals(SerializerConstants.XMLVERSION11)) {
                switchToXML11Scanner();
            }
            if (this.fEventType == 4 || this.fEventType == 9 || this.fEventType == 3 || this.fEventType == 5 || this.fEventType == 12) {
                this.fEntityScanner.checkNodeCount(this.fEntityScanner.fCurrentEntity);
            }
            return this.fEventType;
        } catch (XNIException ex) {
            throw new XMLStreamException(ex.getMessage(), getLocation(), ex.getException());
        } catch (IOException ex2) {
            int i2 = this.fScanner.fScannerState;
            XMLDocumentScannerImpl xMLDocumentScannerImpl = this.fScanner;
            if (i2 == 46 && (isValidating = (Boolean) this.fPropertyManager.getProperty(XMLInputFactory.IS_VALIDATING)) != null && !isValidating.booleanValue()) {
                this.fEventType = 11;
                XMLDocumentScannerImpl xMLDocumentScannerImpl2 = this.fScanner;
                XMLDocumentScannerImpl xMLDocumentScannerImpl3 = this.fScanner;
                xMLDocumentScannerImpl2.setScannerState(43);
                this.fScanner.setDriver(this.fScanner.fPrologDriver);
                if (this.fDTDDecl == null || this.fDTDDecl.length() == 0) {
                    this.fDTDDecl = "<!-- Exception scanning External DTD Subset.  True contents of DTD cannot be determined.  Processing will continue as XMLInputFactory.IS_VALIDATING == false. -->";
                    return 11;
                }
                return 11;
            }
            throw new XMLStreamException(ex2.getMessage(), getLocation(), ex2);
        }
    }

    private void switchToXML11Scanner() throws IOException {
        int oldEntityDepth = this.fScanner.fEntityDepth;
        NamespaceContext oldNamespaceContext = this.fScanner.fNamespaceContext;
        this.fScanner = new XML11NSDocumentScannerImpl();
        this.fScanner.reset(this.fPropertyManager);
        this.fScanner.setPropertyManager(this.fPropertyManager);
        this.fEntityScanner = this.fEntityManager.getEntityScanner();
        this.fEntityManager.fCurrentEntity.mayReadChunks = true;
        this.fScanner.setScannerState(7);
        this.fScanner.fEntityDepth = oldEntityDepth;
        this.fScanner.fNamespaceContext = oldNamespaceContext;
        this.fEventType = this.fScanner.next();
    }

    static final String getEventTypeString(int eventType) {
        switch (eventType) {
            case 1:
                return "START_ELEMENT";
            case 2:
                return "END_ELEMENT";
            case 3:
                return "PROCESSING_INSTRUCTION";
            case 4:
                return "CHARACTERS";
            case 5:
                return "COMMENT";
            case 6:
                return "SPACE";
            case 7:
                return "START_DOCUMENT";
            case 8:
                return "END_DOCUMENT";
            case 9:
                return "ENTITY_REFERENCE";
            case 10:
                return "ATTRIBUTE";
            case 11:
                return "DTD";
            case 12:
                return "CDATA";
            default:
                return "UNKNOWN_EVENT_TYPE, " + String.valueOf(eventType);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getAttributeCount() {
        if (this.fEventType == 1 || this.fEventType == 10) {
            return this.fScanner.getAttributeIterator().getLength();
        }
        throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeCount()");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getAttributeName(int index) {
        if (this.fEventType == 1 || this.fEventType == 10) {
            return convertXNIQNametoJavaxQName(this.fScanner.getAttributeIterator().getQualifiedName(index));
        }
        throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeName()");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeLocalName(int index) {
        if (this.fEventType == 1 || this.fEventType == 10) {
            return this.fScanner.getAttributeIterator().getLocalName(index);
        }
        throw new IllegalStateException();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeNamespace(int index) {
        if (this.fEventType == 1 || this.fEventType == 10) {
            return this.fScanner.getAttributeIterator().getURI(index);
        }
        throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeNamespace()");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributePrefix(int index) {
        if (this.fEventType == 1 || this.fEventType == 10) {
            return this.fScanner.getAttributeIterator().getPrefix(index);
        }
        throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributePrefix()");
    }

    public QName getAttributeQName(int index) {
        if (this.fEventType == 1 || this.fEventType == 10) {
            String localName = this.fScanner.getAttributeIterator().getLocalName(index);
            String uri = this.fScanner.getAttributeIterator().getURI(index);
            return new QName(uri, localName);
        }
        throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeQName()");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeType(int index) {
        if (this.fEventType == 1 || this.fEventType == 10) {
            return this.fScanner.getAttributeIterator().getType(index);
        }
        throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeType()");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(int index) {
        if (this.fEventType == 1 || this.fEventType == 10) {
            return this.fScanner.getAttributeIterator().getValue(index);
        }
        throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeValue()");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(String namespaceURI, String localName) {
        if (this.fEventType == 1 || this.fEventType == 10) {
            XMLAttributesImpl attributes = this.fScanner.getAttributeIterator();
            if (namespaceURI == null) {
                return attributes.getValue(attributes.getIndexByLocalName(localName));
            }
            return this.fScanner.getAttributeIterator().getValue(namespaceURI.length() == 0 ? null : namespaceURI, localName);
        }
        throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for getAttributeValue()");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getElementText() throws XMLStreamException {
        if (getEventType() != 1) {
            throw new XMLStreamException("parser must be on START_ELEMENT to read next text", getLocation());
        }
        int eventType = next();
        StringBuffer content = new StringBuffer();
        while (eventType != 2) {
            if (eventType == 4 || eventType == 12 || eventType == 6 || eventType == 9) {
                content.append(getText());
            } else if (eventType != 3 && eventType != 5) {
                if (eventType == 8) {
                    throw new XMLStreamException("unexpected end of document when reading element text content");
                }
                if (eventType == 1) {
                    throw new XMLStreamException("elementGetText() function expects text only elment but START_ELEMENT was encountered.", getLocation());
                }
                throw new XMLStreamException("Unexpected event type " + eventType, getLocation());
            }
            eventType = next();
        }
        return content.toString();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Location getLocation() {
        return new Location() { // from class: com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl.1
            String _systemId;
            String _publicId;
            int _offset;
            int _columnNumber;
            int _lineNumber;

            {
                this._systemId = XMLStreamReaderImpl.this.fEntityScanner.getExpandedSystemId();
                this._publicId = XMLStreamReaderImpl.this.fEntityScanner.getPublicId();
                this._offset = XMLStreamReaderImpl.this.fEntityScanner.getCharacterOffset();
                this._columnNumber = XMLStreamReaderImpl.this.fEntityScanner.getColumnNumber();
                this._lineNumber = XMLStreamReaderImpl.this.fEntityScanner.getLineNumber();
            }

            public String getLocationURI() {
                return this._systemId;
            }

            @Override // javax.xml.stream.Location
            public int getCharacterOffset() {
                return this._offset;
            }

            @Override // javax.xml.stream.Location
            public int getColumnNumber() {
                return this._columnNumber;
            }

            @Override // javax.xml.stream.Location
            public int getLineNumber() {
                return this._lineNumber;
            }

            @Override // javax.xml.stream.Location
            public String getPublicId() {
                return this._publicId;
            }

            @Override // javax.xml.stream.Location
            public String getSystemId() {
                return this._systemId;
            }

            public String toString() {
                StringBuffer sbuffer = new StringBuffer();
                sbuffer.append("Line number = " + getLineNumber());
                sbuffer.append("\n");
                sbuffer.append("Column number = " + getColumnNumber());
                sbuffer.append("\n");
                sbuffer.append("System Id = " + getSystemId());
                sbuffer.append("\n");
                sbuffer.append("Public Id = " + getPublicId());
                sbuffer.append("\n");
                sbuffer.append("Location Uri= " + getLocationURI());
                sbuffer.append("\n");
                sbuffer.append("CharacterOffset = " + getCharacterOffset());
                sbuffer.append("\n");
                return sbuffer.toString();
            }
        };
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getName() {
        if (this.fEventType == 1 || this.fEventType == 2) {
            return convertXNIQNametoJavaxQName(this.fScanner.getElementQName());
        }
        throw new IllegalStateException("Illegal to call getName() when event type is " + getEventTypeString(this.fEventType) + ". Valid states are " + getEventTypeString(1) + ", " + getEventTypeString(2));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public javax.xml.namespace.NamespaceContext getNamespaceContext() {
        return this.fNamespaceContextWrapper;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getNamespaceCount() {
        if (this.fEventType == 1 || this.fEventType == 2 || this.fEventType == 13) {
            return this.fScanner.getNamespaceContext().getDeclaredPrefixCount();
        }
        throw new IllegalStateException("Current event state is " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(1) + ", " + getEventTypeString(2) + ", " + getEventTypeString(13) + " valid for getNamespaceCount().");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespacePrefix(int index) {
        if (this.fEventType == 1 || this.fEventType == 2 || this.fEventType == 13) {
            String prefix = this.fScanner.getNamespaceContext().getDeclaredPrefixAt(index);
            if (prefix.equals("")) {
                return null;
            }
            return prefix;
        }
        throw new IllegalStateException("Current state " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(1) + ", " + getEventTypeString(2) + ", " + getEventTypeString(13) + " valid for getNamespacePrefix().");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(int index) {
        if (this.fEventType == 1 || this.fEventType == 2 || this.fEventType == 13) {
            return this.fScanner.getNamespaceContext().getURI(this.fScanner.getNamespaceContext().getDeclaredPrefixAt(index));
        }
        throw new IllegalStateException("Current state " + getEventTypeString(this.fEventType) + " is not among the states " + getEventTypeString(1) + ", " + getEventTypeString(2) + ", " + getEventTypeString(13) + " valid for getNamespaceURI().");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Object getProperty(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        if (this.fPropertyManager != null) {
            PropertyManager propertyManager = this.fPropertyManager;
            if (name.equals(PropertyManager.STAX_NOTATIONS)) {
                return getNotationDecls();
            }
            PropertyManager propertyManager2 = this.fPropertyManager;
            if (name.equals(PropertyManager.STAX_ENTITIES)) {
                return getEntityDecls();
            }
            return this.fPropertyManager.getProperty(name);
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getText() {
        if (this.fEventType == 4 || this.fEventType == 5 || this.fEventType == 12 || this.fEventType == 6) {
            return this.fScanner.getCharacterData().toString();
        }
        if (this.fEventType == 9) {
            String name = this.fScanner.getEntityName();
            if (name != null) {
                if (this.fScanner.foundBuiltInRefs) {
                    return this.fScanner.getCharacterData().toString();
                }
                XMLEntityStorage entityStore = this.fEntityManager.getEntityStore();
                Entity en = entityStore.getEntity(name);
                if (en == null) {
                    return null;
                }
                if (en.isExternal()) {
                    return ((Entity.ExternalEntity) en).entityLocation.getExpandedSystemId();
                }
                return ((Entity.InternalEntity) en).text;
            }
            return null;
        }
        if (this.fEventType == 11) {
            if (this.fDTDDecl != null) {
                return this.fDTDDecl;
            }
            XMLStringBuffer tmpBuffer = this.fScanner.getDTDDecl();
            this.fDTDDecl = tmpBuffer.toString();
            return this.fDTDDecl;
        }
        throw new IllegalStateException("Current state " + getEventTypeString(this.fEventType) + " is not among the states" + getEventTypeString(4) + ", " + getEventTypeString(5) + ", " + getEventTypeString(12) + ", " + getEventTypeString(6) + ", " + getEventTypeString(9) + ", " + getEventTypeString(11) + " valid for getText() ");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        if (type != this.fEventType) {
            throw new XMLStreamException("Event type " + getEventTypeString(type) + " specified did not match with current parser event " + getEventTypeString(this.fEventType));
        }
        if (namespaceURI != null && !namespaceURI.equals(getNamespaceURI())) {
            throw new XMLStreamException("Namespace URI " + namespaceURI + " specified did not match with current namespace URI");
        }
        if (localName != null && !localName.equals(getLocalName())) {
            throw new XMLStreamException("LocalName " + localName + " specified did not match with current local name");
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        int copiedLength;
        if (target == null) {
            throw new NullPointerException("target char array can't be null");
        }
        if (targetStart < 0 || length < 0 || sourceStart < 0 || targetStart >= target.length || targetStart + length > target.length) {
            throw new IndexOutOfBoundsException();
        }
        int available = getTextLength() - sourceStart;
        if (available < 0) {
            throw new IndexOutOfBoundsException("sourceStart is greater thannumber of characters associated with this event");
        }
        if (available < length) {
            copiedLength = available;
        } else {
            copiedLength = length;
        }
        System.arraycopy(getTextCharacters(), getTextStart() + sourceStart, target, targetStart, copiedLength);
        return copiedLength;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasText() {
        if (this.fEventType == 4 || this.fEventType == 5 || this.fEventType == 12) {
            return this.fScanner.getCharacterData().length > 0;
        }
        if (this.fEventType == 9) {
            String name = this.fScanner.getEntityName();
            if (name != null) {
                if (this.fScanner.foundBuiltInRefs) {
                    return true;
                }
                XMLEntityStorage entityStore = this.fEntityManager.getEntityStore();
                Entity en = entityStore.getEntity(name);
                if (en == null) {
                    return false;
                }
                return en.isExternal() ? ((Entity.ExternalEntity) en).entityLocation.getExpandedSystemId() != null : ((Entity.InternalEntity) en).text != null;
            }
            return false;
        }
        if (this.fEventType == 11) {
            return this.fScanner.fSeenDoctypeDecl;
        }
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isAttributeSpecified(int index) {
        if (this.fEventType == 1 || this.fEventType == 10) {
            return this.fScanner.getAttributeIterator().isSpecified(index);
        }
        throw new IllegalStateException("Current state is not among the states " + getEventTypeString(1) + " , " + getEventTypeString(10) + "valid for isAttributeSpecified()");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isCharacters() {
        return this.fEventType == 4;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int nextTag() throws XMLStreamException {
        int eventType;
        int next = next();
        while (true) {
            eventType = next;
            if ((eventType != 4 || !isWhiteSpace()) && ((eventType != 12 || !isWhiteSpace()) && eventType != 6 && eventType != 3 && eventType != 5)) {
                break;
            }
            next = next();
        }
        if (eventType != 1 && eventType != 2) {
            throw new XMLStreamException("found: " + getEventTypeString(eventType) + ", expected " + getEventTypeString(1) + " or " + getEventTypeString(2), getLocation());
        }
        return eventType;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean standaloneSet() {
        return this.fScanner.standaloneSet();
    }

    public QName convertXNIQNametoJavaxQName(com.sun.org.apache.xerces.internal.xni.QName qname) {
        if (qname == null) {
            return null;
        }
        if (qname.prefix == null) {
            return new QName(qname.uri, qname.localpart);
        }
        return new QName(qname.uri, qname.localpart, qname.prefix);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("prefix cannot be null.");
        }
        return this.fScanner.getNamespaceContext().getURI(this.fSymbolTable.addSymbol(prefix));
    }

    protected void setPropertyManager(PropertyManager propertyManager) throws XMLConfigurationException {
        this.fPropertyManager = propertyManager;
        this.fScanner.setProperty(Constants.STAX_PROPERTIES, propertyManager);
        this.fScanner.setPropertyManager(propertyManager);
    }

    protected PropertyManager getPropertyManager() {
        return this.fPropertyManager;
    }

    static void pr(String str) {
        System.out.println(str);
    }

    protected List getEntityDecls() {
        if (this.fEventType == 11) {
            XMLEntityStorage entityStore = this.fEntityManager.getEntityStore();
            ArrayList list = null;
            if (entityStore.hasEntities()) {
                list = new ArrayList(entityStore.getEntitySize());
                Enumeration enu = entityStore.getEntityKeys();
                while (enu.hasMoreElements()) {
                    String key = (String) enu.nextElement2();
                    Entity en = entityStore.getEntity(key);
                    EntityDeclarationImpl decl = new EntityDeclarationImpl();
                    decl.setEntityName(key);
                    if (en.isExternal()) {
                        decl.setXMLResourceIdentifier(((Entity.ExternalEntity) en).entityLocation);
                        decl.setNotationName(((Entity.ExternalEntity) en).notation);
                    } else {
                        decl.setEntityReplacementText(((Entity.InternalEntity) en).text);
                    }
                    list.add(decl);
                }
            }
            return list;
        }
        return null;
    }

    protected List getNotationDecls() {
        DTDGrammar grammar;
        if (this.fEventType != 11 || this.fScanner.fDTDScanner == null || (grammar = ((XMLDTDScannerImpl) this.fScanner.fDTDScanner).getGrammar()) == null) {
            return null;
        }
        List<XMLNotationDecl> notations = grammar.getNotationDecls();
        ArrayList list = new ArrayList();
        for (XMLNotationDecl ni : notations) {
            if (ni != null) {
                list.add(new NotationDeclarationImpl(ni));
            }
        }
        return list;
    }
}
