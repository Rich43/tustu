package com.sun.xml.internal.fastinfoset.stax;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.Decoder;
import com.sun.xml.internal.fastinfoset.DecoderStateTables;
import com.sun.xml.internal.fastinfoset.EncodingConstants;
import com.sun.xml.internal.fastinfoset.OctetBufferListener;
import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
import com.sun.xml.internal.fastinfoset.org.apache.xerces.util.XMLChar;
import com.sun.xml.internal.fastinfoset.sax.AttributesHolder;
import com.sun.xml.internal.fastinfoset.util.CharArray;
import com.sun.xml.internal.fastinfoset.util.CharArrayString;
import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/StAXDocumentParser.class */
public class StAXDocumentParser extends Decoder implements XMLStreamReader, FastInfosetStreamReader, OctetBufferListener {
    private static final Logger logger = Logger.getLogger(StAXDocumentParser.class.getName());
    protected static final int INTERNAL_STATE_START_DOCUMENT = 0;
    protected static final int INTERNAL_STATE_START_ELEMENT_TERMINATE = 1;
    protected static final int INTERNAL_STATE_SINGLE_TERMINATE_ELEMENT_WITH_NAMESPACES = 2;
    protected static final int INTERNAL_STATE_DOUBLE_TERMINATE_ELEMENT = 3;
    protected static final int INTERNAL_STATE_END_DOCUMENT = 4;
    protected static final int INTERNAL_STATE_VOID = -1;
    protected int _internalState;
    protected int _eventType;
    protected QualifiedName[] _qNameStack;
    protected int[] _namespaceAIIsStartStack;
    protected int[] _namespaceAIIsEndStack;
    protected int _stackCount;
    protected String[] _namespaceAIIsPrefix;
    protected String[] _namespaceAIIsNamespaceName;
    protected int[] _namespaceAIIsPrefixIndex;
    protected int _namespaceAIIsIndex;
    protected int _currentNamespaceAIIsStart;
    protected int _currentNamespaceAIIsEnd;
    protected QualifiedName _qualifiedName;
    protected AttributesHolder _attributes;
    protected boolean _clearAttributes;
    protected char[] _characters;
    protected int _charactersOffset;
    protected String _algorithmURI;
    protected int _algorithmId;
    protected boolean _isAlgorithmDataCloned;
    protected byte[] _algorithmData;
    protected int _algorithmDataOffset;
    protected int _algorithmDataLength;
    protected String _piTarget;
    protected String _piData;
    protected NamespaceContextImpl _nsContext;
    protected String _characterEncodingScheme;
    protected StAXManager _manager;
    private byte[] base64TaleBytes;
    private int base64TaleLength;

    public StAXDocumentParser() {
        this._qNameStack = new QualifiedName[32];
        this._namespaceAIIsStartStack = new int[32];
        this._namespaceAIIsEndStack = new int[32];
        this._stackCount = -1;
        this._namespaceAIIsPrefix = new String[32];
        this._namespaceAIIsNamespaceName = new String[32];
        this._namespaceAIIsPrefixIndex = new int[32];
        this._attributes = new AttributesHolder();
        this._clearAttributes = false;
        this._nsContext = new NamespaceContextImpl();
        this.base64TaleBytes = new byte[3];
        reset();
        this._manager = new StAXManager(1);
    }

    public StAXDocumentParser(InputStream s2) {
        this();
        setInputStream(s2);
        this._manager = new StAXManager(1);
    }

    public StAXDocumentParser(InputStream s2, StAXManager manager) {
        this(s2);
        this._manager = manager;
    }

    @Override // com.sun.xml.internal.fastinfoset.Decoder
    public void setInputStream(InputStream s2) {
        super.setInputStream(s2);
        reset();
    }

    @Override // com.sun.xml.internal.fastinfoset.Decoder
    public void reset() {
        super.reset();
        if (this._internalState != 0 && this._internalState != 4) {
            for (int i2 = this._namespaceAIIsIndex - 1; i2 >= 0; i2--) {
                this._prefixTable.popScopeWithPrefixEntry(this._namespaceAIIsPrefixIndex[i2]);
            }
            this._stackCount = -1;
            this._namespaceAIIsIndex = 0;
            this._characters = null;
            this._algorithmData = null;
        }
        this._characterEncodingScheme = "UTF-8";
        this._eventType = 7;
        this._internalState = 0;
    }

    protected void resetOnError() {
        super.reset();
        if (this._v != null) {
            this._prefixTable.clearCompletely();
        }
        this._duplicateAttributeVerifier.clear();
        this._stackCount = -1;
        this._namespaceAIIsIndex = 0;
        this._characters = null;
        this._algorithmData = null;
        this._eventType = 7;
        this._internalState = 0;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Object getProperty(String name) throws IllegalArgumentException {
        if (this._manager != null) {
            return this._manager.getProperty(name);
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int next() throws FastInfosetException, XMLStreamException {
        try {
            if (this._internalState != -1) {
                switch (this._internalState) {
                    case 0:
                        decodeHeader();
                        processDII();
                        this._internalState = -1;
                        break;
                    case 1:
                        if (this._currentNamespaceAIIsEnd > 0) {
                            for (int i2 = this._currentNamespaceAIIsEnd - 1; i2 >= this._currentNamespaceAIIsStart; i2--) {
                                this._prefixTable.popScopeWithPrefixEntry(this._namespaceAIIsPrefixIndex[i2]);
                            }
                            this._namespaceAIIsIndex = this._currentNamespaceAIIsStart;
                        }
                        popStack();
                        this._internalState = -1;
                        this._eventType = 2;
                        return 2;
                    case 2:
                        for (int i3 = this._currentNamespaceAIIsEnd - 1; i3 >= this._currentNamespaceAIIsStart; i3--) {
                            this._prefixTable.popScopeWithPrefixEntry(this._namespaceAIIsPrefixIndex[i3]);
                        }
                        this._namespaceAIIsIndex = this._currentNamespaceAIIsStart;
                        this._internalState = -1;
                        break;
                    case 3:
                        if (this._currentNamespaceAIIsEnd > 0) {
                            for (int i4 = this._currentNamespaceAIIsEnd - 1; i4 >= this._currentNamespaceAIIsStart; i4--) {
                                this._prefixTable.popScopeWithPrefixEntry(this._namespaceAIIsPrefixIndex[i4]);
                            }
                            this._namespaceAIIsIndex = this._currentNamespaceAIIsStart;
                        }
                        if (this._stackCount == -1) {
                            this._internalState = 4;
                            this._eventType = 8;
                            return 8;
                        }
                        popStack();
                        this._internalState = this._currentNamespaceAIIsEnd > 0 ? 2 : -1;
                        this._eventType = 2;
                        return 2;
                    case 4:
                        throw new NoSuchElementException(CommonResourceBundle.getInstance().getString("message.noMoreEvents"));
                }
            }
            this._characters = null;
            this._algorithmData = null;
            this._currentNamespaceAIIsEnd = 0;
            int b2 = read();
            switch (DecoderStateTables.EII(b2)) {
                case 0:
                    processEII(this._elementNameTable._array[b2], false);
                    return this._eventType;
                case 1:
                    processEII(this._elementNameTable._array[b2 & 31], true);
                    return this._eventType;
                case 2:
                    processEII(processEIIIndexMedium(b2), (b2 & 64) > 0);
                    return this._eventType;
                case 3:
                    processEII(processEIIIndexLarge(b2), (b2 & 64) > 0);
                    return this._eventType;
                case 4:
                    processEIIWithNamespaces((b2 & 64) > 0);
                    return this._eventType;
                case 5:
                    QualifiedName qn = processLiteralQualifiedName(b2 & 3, this._elementNameTable.getNext());
                    this._elementNameTable.add(qn);
                    processEII(qn, (b2 & 64) > 0);
                    return this._eventType;
                case 6:
                    this._octetBufferLength = (b2 & 1) + 1;
                    processUtf8CharacterString(b2);
                    this._eventType = 4;
                    return 4;
                case 7:
                    this._octetBufferLength = read() + 3;
                    processUtf8CharacterString(b2);
                    this._eventType = 4;
                    return 4;
                case 8:
                    this._octetBufferLength = ((read() << 24) | (read() << 16) | (read() << 8) | read()) + 259;
                    processUtf8CharacterString(b2);
                    this._eventType = 4;
                    return 4;
                case 9:
                    this._octetBufferLength = (b2 & 1) + 1;
                    processUtf16CharacterString(b2);
                    this._eventType = 4;
                    return 4;
                case 10:
                    this._octetBufferLength = read() + 3;
                    processUtf16CharacterString(b2);
                    this._eventType = 4;
                    return 4;
                case 11:
                    this._octetBufferLength = ((read() << 24) | (read() << 16) | (read() << 8) | read()) + 259;
                    processUtf16CharacterString(b2);
                    this._eventType = 4;
                    return 4;
                case 12:
                    boolean addToTable = (b2 & 16) > 0;
                    this._identifier = (b2 & 2) << 6;
                    int b22 = read();
                    this._identifier |= (b22 & 252) >> 2;
                    decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(b22);
                    decodeRestrictedAlphabetAsCharBuffer();
                    if (addToTable) {
                        this._charactersOffset = this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                        this._characters = this._characterContentChunkTable._array;
                    } else {
                        this._characters = this._charBuffer;
                        this._charactersOffset = 0;
                    }
                    this._eventType = 4;
                    return 4;
                case 13:
                    boolean addToTable2 = (b2 & 16) > 0;
                    this._algorithmId = (b2 & 2) << 6;
                    int b23 = read();
                    this._algorithmId |= (b23 & 252) >> 2;
                    decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(b23);
                    processCIIEncodingAlgorithm(addToTable2);
                    if (this._algorithmId == 9) {
                        this._eventType = 12;
                        return 12;
                    }
                    this._eventType = 4;
                    return 4;
                case 14:
                    int index = b2 & 15;
                    this._characterContentChunkTable._cachedIndex = index;
                    this._characters = this._characterContentChunkTable._array;
                    this._charactersOffset = this._characterContentChunkTable._offset[index];
                    this._charBufferLength = this._characterContentChunkTable._length[index];
                    this._eventType = 4;
                    return 4;
                case 15:
                    int index2 = (((b2 & 3) << 8) | read()) + 16;
                    this._characterContentChunkTable._cachedIndex = index2;
                    this._characters = this._characterContentChunkTable._array;
                    this._charactersOffset = this._characterContentChunkTable._offset[index2];
                    this._charBufferLength = this._characterContentChunkTable._length[index2];
                    this._eventType = 4;
                    return 4;
                case 16:
                    int index3 = (((b2 & 3) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT;
                    this._characterContentChunkTable._cachedIndex = index3;
                    this._characters = this._characterContentChunkTable._array;
                    this._charactersOffset = this._characterContentChunkTable._offset[index3];
                    this._charBufferLength = this._characterContentChunkTable._length[index3];
                    this._eventType = 4;
                    return 4;
                case 17:
                    int index4 = ((read() << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_4TH_BIT_LARGE_LIMIT;
                    this._characterContentChunkTable._cachedIndex = index4;
                    this._characters = this._characterContentChunkTable._array;
                    this._charactersOffset = this._characterContentChunkTable._offset[index4];
                    this._charBufferLength = this._characterContentChunkTable._length[index4];
                    this._eventType = 4;
                    return 4;
                case 18:
                    processCommentII();
                    return this._eventType;
                case 19:
                    processProcessingII();
                    return this._eventType;
                case 20:
                default:
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
                case 21:
                    processUnexpandedEntityReference(b2);
                    return next();
                case 22:
                    if (this._stackCount != -1) {
                        popStack();
                        if (this._currentNamespaceAIIsEnd > 0) {
                            this._internalState = 2;
                        }
                        this._eventType = 2;
                        return 2;
                    }
                    this._internalState = 4;
                    this._eventType = 8;
                    return 8;
                case 23:
                    if (this._stackCount != -1) {
                        popStack();
                        this._internalState = 3;
                        this._eventType = 2;
                        return 2;
                    }
                    this._internalState = 4;
                    this._eventType = 8;
                    return 8;
            }
        } catch (FastInfosetException e2) {
            resetOnError();
            logger.log(Level.FINE, "next() exception", (Throwable) e2);
            throw new XMLStreamException(e2);
        } catch (IOException e3) {
            resetOnError();
            logger.log(Level.FINE, "next() exception", (Throwable) e3);
            throw new XMLStreamException(e3);
        } catch (RuntimeException e4) {
            resetOnError();
            logger.log(Level.FINE, "next() exception", (Throwable) e4);
            throw e4;
        }
    }

    private final void processUtf8CharacterString(int b2) throws IOException {
        if ((b2 & 16) > 0) {
            this._characterContentChunkTable.ensureSize(this._octetBufferLength);
            this._characters = this._characterContentChunkTable._array;
            this._charactersOffset = this._characterContentChunkTable._arrayIndex;
            decodeUtf8StringAsCharBuffer(this._characterContentChunkTable._array, this._charactersOffset);
            this._characterContentChunkTable.add(this._charBufferLength);
            return;
        }
        decodeUtf8StringAsCharBuffer();
        this._characters = this._charBuffer;
        this._charactersOffset = 0;
    }

    private final void processUtf16CharacterString(int b2) throws IOException {
        decodeUtf16StringAsCharBuffer();
        if ((b2 & 16) > 0) {
            this._charactersOffset = this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
            this._characters = this._characterContentChunkTable._array;
        } else {
            this._characters = this._charBuffer;
            this._charactersOffset = 0;
        }
    }

    private void popStack() {
        this._qualifiedName = this._qNameStack[this._stackCount];
        this._currentNamespaceAIIsStart = this._namespaceAIIsStartStack[this._stackCount];
        this._currentNamespaceAIIsEnd = this._namespaceAIIsEndStack[this._stackCount];
        QualifiedName[] qualifiedNameArr = this._qNameStack;
        int i2 = this._stackCount;
        this._stackCount = i2 - 1;
        qualifiedNameArr[i2] = null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        if (type != this._eventType) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.eventTypeNotMatch", new Object[]{getEventTypeString(type)}));
        }
        if (namespaceURI != null && !namespaceURI.equals(getNamespaceURI())) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.namespaceURINotMatch", new Object[]{namespaceURI}));
        }
        if (localName != null && !localName.equals(getLocalName())) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.localNameNotMatch", new Object[]{localName}));
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getElementText() throws FastInfosetException, XMLStreamException {
        if (getEventType() != 1) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.mustBeOnSTARTELEMENT"), getLocation());
        }
        next();
        return getElementText(true);
    }

    public final String getElementText(boolean startElementRead) throws FastInfosetException, XMLStreamException {
        if (!startElementRead) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.mustBeOnSTARTELEMENT"), getLocation());
        }
        int eventType = getEventType();
        StringBuilder content = new StringBuilder();
        while (eventType != 2) {
            if (eventType == 4 || eventType == 12 || eventType == 6 || eventType == 9) {
                content.append(getText());
            } else if (eventType != 3 && eventType != 5) {
                if (eventType == 8) {
                    throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.unexpectedEOF"));
                }
                if (eventType == 1) {
                    throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.getElementTextExpectTextOnly"), getLocation());
                }
                throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.unexpectedEventType") + getEventTypeString(eventType), getLocation());
            }
            eventType = next();
        }
        return content.toString();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int nextTag() throws FastInfosetException, XMLStreamException {
        next();
        return nextTag(true);
    }

    public final int nextTag(boolean currentTagRead) throws FastInfosetException, XMLStreamException {
        int eventType = getEventType();
        if (!currentTagRead) {
            eventType = next();
        }
        while (true) {
            if ((eventType != 4 || !isWhiteSpace()) && ((eventType != 12 || !isWhiteSpace()) && eventType != 6 && eventType != 3 && eventType != 5)) {
                break;
            }
            eventType = next();
        }
        if (eventType != 1 && eventType != 2) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.expectedStartOrEnd"), getLocation());
        }
        return eventType;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean hasNext() throws XMLStreamException {
        return this._eventType != 8;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void close() throws XMLStreamException {
        try {
            super.closeIfRequired();
        } catch (IOException e2) {
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getNamespaceURI(String prefix) {
        String namespace = getNamespaceDecl(prefix);
        if (namespace == null) {
            if (prefix == null) {
                throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.nullPrefix"));
            }
            return null;
        }
        return namespace;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean isStartElement() {
        return this._eventType == 1;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean isEndElement() {
        return this._eventType == 2;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean isCharacters() {
        return this._eventType == 4;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean isWhiteSpace() {
        if (isCharacters() || this._eventType == 12) {
            char[] ch = getTextCharacters();
            int start = getTextStart();
            int length = getTextLength();
            for (int i2 = start; i2 < start + length; i2++) {
                if (!XMLChar.isSpace(ch[i2])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributeValue(String namespaceURI, String localName) {
        if (this._eventType != 1) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
        }
        if (localName == null) {
            throw new IllegalArgumentException();
        }
        if (namespaceURI != null) {
            for (int i2 = 0; i2 < this._attributes.getLength(); i2++) {
                if (this._attributes.getLocalName(i2).equals(localName) && this._attributes.getURI(i2).equals(namespaceURI)) {
                    return this._attributes.getValue(i2);
                }
            }
            return null;
        }
        for (int i3 = 0; i3 < this._attributes.getLength(); i3++) {
            if (this._attributes.getLocalName(i3).equals(localName)) {
                return this._attributes.getValue(i3);
            }
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getAttributeCount() {
        if (this._eventType != 1) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
        }
        return this._attributes.getLength();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final QName getAttributeName(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
        }
        return this._attributes.getQualifiedName(index).getQName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributeNamespace(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
        }
        return this._attributes.getURI(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributeLocalName(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
        }
        return this._attributes.getLocalName(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributePrefix(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
        }
        return this._attributes.getPrefix(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributeType(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
        }
        return this._attributes.getType(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributeValue(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
        }
        return this._attributes.getValue(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean isAttributeSpecified(int index) {
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getNamespaceCount() {
        if (this._eventType == 1 || this._eventType == 2) {
            if (this._currentNamespaceAIIsEnd > 0) {
                return this._currentNamespaceAIIsEnd - this._currentNamespaceAIIsStart;
            }
            return 0;
        }
        throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetNamespaceCount"));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getNamespacePrefix(int index) {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._namespaceAIIsPrefix[this._currentNamespaceAIIsStart + index];
        }
        throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetNamespacePrefix"));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getNamespaceURI(int index) {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._namespaceAIIsNamespaceName[this._currentNamespaceAIIsStart + index];
        }
        throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetNamespacePrefix"));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final NamespaceContext getNamespaceContext() {
        return this._nsContext;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getEventType() {
        return this._eventType;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getText() {
        if (this._characters == null) {
            checkTextState();
        }
        if (this._characters == this._characterContentChunkTable._array) {
            return this._characterContentChunkTable.getString(this._characterContentChunkTable._cachedIndex);
        }
        return new String(this._characters, this._charactersOffset, this._charBufferLength);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final char[] getTextCharacters() {
        if (this._characters == null) {
            checkTextState();
        }
        return this._characters;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getTextStart() {
        if (this._characters == null) {
            checkTextState();
        }
        return this._charactersOffset;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getTextLength() {
        if (this._characters == null) {
            checkTextState();
        }
        return this._charBufferLength;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        if (this._characters == null) {
            checkTextState();
        }
        try {
            int bytesToCopy = Math.min(this._charBufferLength, length);
            System.arraycopy(this._characters, this._charactersOffset + sourceStart, target, targetStart, bytesToCopy);
            return bytesToCopy;
        } catch (IndexOutOfBoundsException e2) {
            throw new XMLStreamException(e2);
        }
    }

    protected final void checkTextState() {
        if (this._algorithmData == null) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.InvalidStateForText"));
        }
        try {
            convertEncodingAlgorithmDataToCharacters();
        } catch (Exception e2) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.InvalidStateForText"));
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getEncoding() {
        return this._characterEncodingScheme;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean hasText() {
        return this._characters != null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final Location getLocation() {
        return EventLocation.getNilLocation();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final QName getName() {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._qualifiedName.getQName();
        }
        throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetName"));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getLocalName() {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._qualifiedName.localName;
        }
        throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetLocalName"));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean hasName() {
        return this._eventType == 1 || this._eventType == 2;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getNamespaceURI() {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._qualifiedName.namespaceName;
        }
        throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetNamespaceURI"));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getPrefix() {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._qualifiedName.prefix;
        }
        throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetPrefix"));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getVersion() {
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean isStandalone() {
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean standaloneSet() {
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getCharacterEncodingScheme() {
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getPITarget() {
        if (this._eventType != 3) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetPITarget"));
        }
        return this._piTarget;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getPIData() {
        if (this._eventType != 3) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetPIData"));
        }
        return this._piData;
    }

    public final String getNameString() {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._qualifiedName.getQNameString();
        }
        throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetName"));
    }

    public final String getAttributeNameString(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException(CommonResourceBundle.getInstance().getString("message.invalidCallingGetAttributeValue"));
        }
        return this._attributes.getQualifiedName(index).getQNameString();
    }

    public final String getTextAlgorithmURI() {
        return this._algorithmURI;
    }

    public final int getTextAlgorithmIndex() {
        return this._algorithmId;
    }

    public final boolean hasTextAlgorithmBytes() {
        return this._algorithmData != null;
    }

    public final byte[] getTextAlgorithmBytes() {
        if (this._algorithmData == null) {
            return null;
        }
        byte[] algorithmData = new byte[this._algorithmData.length];
        System.arraycopy(this._algorithmData, 0, algorithmData, 0, this._algorithmData.length);
        return algorithmData;
    }

    public final byte[] getTextAlgorithmBytesClone() {
        if (this._algorithmData == null) {
            return null;
        }
        byte[] algorithmData = new byte[this._algorithmDataLength];
        System.arraycopy(this._algorithmData, this._algorithmDataOffset, algorithmData, 0, this._algorithmDataLength);
        return algorithmData;
    }

    public final int getTextAlgorithmStart() {
        return this._algorithmDataOffset;
    }

    public final int getTextAlgorithmLength() {
        return this._algorithmDataLength;
    }

    public final int getTextAlgorithmBytes(int sourceStart, byte[] target, int targetStart, int length) throws XMLStreamException {
        try {
            System.arraycopy(this._algorithmData, sourceStart, target, targetStart, length);
            return length;
        } catch (IndexOutOfBoundsException e2) {
            throw new XMLStreamException(e2);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader
    public final int peekNext() throws FastInfosetException, XMLStreamException {
        try {
            switch (DecoderStateTables.EII(peek(this))) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    return 1;
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                    return 4;
                case 18:
                    return 5;
                case 19:
                    return 3;
                case 20:
                default:
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
                case 21:
                    return 9;
                case 22:
                case 23:
                    return this._stackCount != -1 ? 2 : 8;
            }
        } catch (FastInfosetException e2) {
            throw new XMLStreamException(e2);
        } catch (IOException e3) {
            throw new XMLStreamException(e3);
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.OctetBufferListener
    public void onBeforeOctetBufferOverwrite() {
        if (this._algorithmData != null) {
            this._algorithmData = getTextAlgorithmBytesClone();
            this._algorithmDataOffset = 0;
            this._isAlgorithmDataCloned = true;
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader
    public final int accessNamespaceCount() {
        if (this._currentNamespaceAIIsEnd > 0) {
            return this._currentNamespaceAIIsEnd - this._currentNamespaceAIIsStart;
        }
        return 0;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader
    public final String accessLocalName() {
        return this._qualifiedName.localName;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader
    public final String accessNamespaceURI() {
        return this._qualifiedName.namespaceName;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader
    public final String accessPrefix() {
        return this._qualifiedName.prefix;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader
    public final char[] accessTextCharacters() {
        if (this._characters == null) {
            return null;
        }
        char[] clonedCharacters = new char[this._characters.length];
        System.arraycopy(this._characters, 0, clonedCharacters, 0, this._characters.length);
        return clonedCharacters;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader
    public final int accessTextStart() {
        return this._charactersOffset;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.stax.FastInfosetStreamReader
    public final int accessTextLength() {
        return this._charBufferLength;
    }

    protected final void processDII() throws FastInfosetException, IOException {
        int b2 = read();
        if (b2 > 0) {
            processDIIOptionalProperties(b2);
        }
    }

    protected final void processDIIOptionalProperties(int b2) throws FastInfosetException, IOException {
        if (b2 == 32) {
            decodeInitialVocabulary();
            return;
        }
        if ((b2 & 64) > 0) {
            decodeAdditionalData();
        }
        if ((b2 & 32) > 0) {
            decodeInitialVocabulary();
        }
        if ((b2 & 16) > 0) {
            decodeNotations();
        }
        if ((b2 & 8) > 0) {
            decodeUnparsedEntities();
        }
        if ((b2 & 4) > 0) {
            this._characterEncodingScheme = decodeCharacterEncodingScheme();
        }
        if ((b2 & 2) > 0) {
            boolean z2 = read() > 0;
        }
        if ((b2 & 1) > 0) {
            decodeVersion();
        }
    }

    protected final void resizeNamespaceAIIs() {
        String[] namespaceAIIsPrefix = new String[this._namespaceAIIsIndex * 2];
        System.arraycopy(this._namespaceAIIsPrefix, 0, namespaceAIIsPrefix, 0, this._namespaceAIIsIndex);
        this._namespaceAIIsPrefix = namespaceAIIsPrefix;
        String[] namespaceAIIsNamespaceName = new String[this._namespaceAIIsIndex * 2];
        System.arraycopy(this._namespaceAIIsNamespaceName, 0, namespaceAIIsNamespaceName, 0, this._namespaceAIIsIndex);
        this._namespaceAIIsNamespaceName = namespaceAIIsNamespaceName;
        int[] namespaceAIIsPrefixIndex = new int[this._namespaceAIIsIndex * 2];
        System.arraycopy(this._namespaceAIIsPrefixIndex, 0, namespaceAIIsPrefixIndex, 0, this._namespaceAIIsIndex);
        this._namespaceAIIsPrefixIndex = namespaceAIIsPrefixIndex;
    }

    protected final void processEIIWithNamespaces(boolean hasAttributes) throws FastInfosetException, IOException {
        PrefixArray prefixArray = this._prefixTable;
        int i2 = prefixArray._declarationId + 1;
        prefixArray._declarationId = i2;
        if (i2 == Integer.MAX_VALUE) {
            this._prefixTable.clearDeclarationIds();
        }
        this._currentNamespaceAIIsStart = this._namespaceAIIsIndex;
        String prefix = "";
        String namespaceName = "";
        int i3 = read();
        while (true) {
            int b2 = i3;
            if ((b2 & 252) == 204) {
                if (this._namespaceAIIsIndex == this._namespaceAIIsPrefix.length) {
                    resizeNamespaceAIIs();
                }
                switch (b2 & 3) {
                    case 0:
                        String[] strArr = this._namespaceAIIsPrefix;
                        int i4 = this._namespaceAIIsIndex;
                        this._namespaceAIIsNamespaceName[this._namespaceAIIsIndex] = "";
                        strArr[i4] = "";
                        namespaceName = "";
                        prefix = "";
                        int[] iArr = this._namespaceAIIsPrefixIndex;
                        int i5 = this._namespaceAIIsIndex;
                        this._namespaceAIIsIndex = i5 + 1;
                        iArr[i5] = -1;
                        this._prefixIndex = -1;
                        this._namespaceNameIndex = -1;
                        break;
                    case 1:
                        this._namespaceAIIsPrefix[this._namespaceAIIsIndex] = "";
                        prefix = "";
                        String[] strArr2 = this._namespaceAIIsNamespaceName;
                        int i6 = this._namespaceAIIsIndex;
                        String strDecodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName = decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(false);
                        strArr2[i6] = strDecodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName;
                        namespaceName = strDecodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName;
                        int[] iArr2 = this._namespaceAIIsPrefixIndex;
                        int i7 = this._namespaceAIIsIndex;
                        this._namespaceAIIsIndex = i7 + 1;
                        iArr2[i7] = -1;
                        this._prefixIndex = -1;
                        break;
                    case 2:
                        String[] strArr3 = this._namespaceAIIsPrefix;
                        int i8 = this._namespaceAIIsIndex;
                        String strDecodeIdentifyingNonEmptyStringOnFirstBitAsPrefix = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(false);
                        strArr3[i8] = strDecodeIdentifyingNonEmptyStringOnFirstBitAsPrefix;
                        prefix = strDecodeIdentifyingNonEmptyStringOnFirstBitAsPrefix;
                        this._namespaceAIIsNamespaceName[this._namespaceAIIsIndex] = "";
                        namespaceName = "";
                        this._namespaceNameIndex = -1;
                        int[] iArr3 = this._namespaceAIIsPrefixIndex;
                        int i9 = this._namespaceAIIsIndex;
                        this._namespaceAIIsIndex = i9 + 1;
                        iArr3[i9] = this._prefixIndex;
                        break;
                    case 3:
                        String[] strArr4 = this._namespaceAIIsPrefix;
                        int i10 = this._namespaceAIIsIndex;
                        String strDecodeIdentifyingNonEmptyStringOnFirstBitAsPrefix2 = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(true);
                        strArr4[i10] = strDecodeIdentifyingNonEmptyStringOnFirstBitAsPrefix2;
                        prefix = strDecodeIdentifyingNonEmptyStringOnFirstBitAsPrefix2;
                        String[] strArr5 = this._namespaceAIIsNamespaceName;
                        int i11 = this._namespaceAIIsIndex;
                        String strDecodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName2 = decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(true);
                        strArr5[i11] = strDecodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName2;
                        namespaceName = strDecodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName2;
                        int[] iArr4 = this._namespaceAIIsPrefixIndex;
                        int i12 = this._namespaceAIIsIndex;
                        this._namespaceAIIsIndex = i12 + 1;
                        iArr4[i12] = this._prefixIndex;
                        break;
                }
                this._prefixTable.pushScopeWithPrefixEntry(prefix, namespaceName, this._prefixIndex, this._namespaceNameIndex);
                i3 = read();
            } else {
                if (b2 != 240) {
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.EIInamespaceNameNotTerminatedCorrectly"));
                }
                this._currentNamespaceAIIsEnd = this._namespaceAIIsIndex;
                int b3 = read();
                switch (DecoderStateTables.EII(b3)) {
                    case 0:
                        processEII(this._elementNameTable._array[b3], hasAttributes);
                        return;
                    case 1:
                    case 4:
                    default:
                        throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEIIAfterAIIs"));
                    case 2:
                        processEII(processEIIIndexMedium(b3), hasAttributes);
                        return;
                    case 3:
                        processEII(processEIIIndexLarge(b3), hasAttributes);
                        return;
                    case 5:
                        QualifiedName qn = processLiteralQualifiedName(b3 & 3, this._elementNameTable.getNext());
                        this._elementNameTable.add(qn);
                        processEII(qn, hasAttributes);
                        return;
                }
            }
        }
    }

    protected final void processEII(QualifiedName name, boolean hasAttributes) throws FastInfosetException, IOException {
        if (this._prefixTable._currentInScope[name.prefixIndex] != name.namespaceNameIndex) {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qnameOfEIINotInScope"));
        }
        this._eventType = 1;
        this._qualifiedName = name;
        if (this._clearAttributes) {
            this._attributes.clear();
            this._clearAttributes = false;
        }
        if (hasAttributes) {
            processAIIs();
        }
        this._stackCount++;
        if (this._stackCount == this._qNameStack.length) {
            QualifiedName[] qNameStack = new QualifiedName[this._qNameStack.length * 2];
            System.arraycopy(this._qNameStack, 0, qNameStack, 0, this._qNameStack.length);
            this._qNameStack = qNameStack;
            int[] namespaceAIIsStartStack = new int[this._namespaceAIIsStartStack.length * 2];
            System.arraycopy(this._namespaceAIIsStartStack, 0, namespaceAIIsStartStack, 0, this._namespaceAIIsStartStack.length);
            this._namespaceAIIsStartStack = namespaceAIIsStartStack;
            int[] namespaceAIIsEndStack = new int[this._namespaceAIIsEndStack.length * 2];
            System.arraycopy(this._namespaceAIIsEndStack, 0, namespaceAIIsEndStack, 0, this._namespaceAIIsEndStack.length);
            this._namespaceAIIsEndStack = namespaceAIIsEndStack;
        }
        this._qNameStack[this._stackCount] = this._qualifiedName;
        this._namespaceAIIsStartStack[this._stackCount] = this._currentNamespaceAIIsStart;
        this._namespaceAIIsEndStack[this._stackCount] = this._currentNamespaceAIIsEnd;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x00e7  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0160  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01fc  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0253  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0298  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x02e9  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0325  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x033c  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0362  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0391  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x039e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected final void processAIIs() throws com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 962
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser.processAIIs():void");
    }

    protected final QualifiedName processEIIIndexMedium(int b2) throws FastInfosetException, IOException {
        int i2 = (((b2 & 7) << 8) | read()) + 32;
        return this._elementNameTable._array[i2];
    }

    protected final QualifiedName processEIIIndexLarge(int b2) throws FastInfosetException, IOException {
        int i2;
        if ((b2 & 48) == 32) {
            i2 = (((b2 & 7) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_3RD_BIT_MEDIUM_LIMIT;
        } else {
            i2 = (((read() & 15) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_3RD_BIT_LARGE_LIMIT;
        }
        return this._elementNameTable._array[i2];
    }

    protected final QualifiedName processLiteralQualifiedName(int state, QualifiedName q2) throws FastInfosetException, IOException {
        if (q2 == null) {
            q2 = new QualifiedName();
        }
        switch (state) {
            case 0:
                return q2.set("", "", decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), "", 0, -1, -1, this._identifier);
            case 1:
                return q2.set("", decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(false), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), "", 0, -1, this._namespaceNameIndex, this._identifier);
            case 2:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameMissingNamespaceName"));
            case 3:
                return q2.set(decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(true), decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(true), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), "", 0, this._prefixIndex, this._namespaceNameIndex, this._identifier);
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingEII"));
        }
    }

    protected final void processCommentII() throws FastInfosetException, IOException {
        this._eventType = 5;
        switch (decodeNonIdentifyingStringOnFirstBit()) {
            case 0:
                if (this._addToTable) {
                    this._v.otherString.add(new CharArray(this._charBuffer, 0, this._charBufferLength, true));
                }
                this._characters = this._charBuffer;
                this._charactersOffset = 0;
                return;
            case 1:
                CharArray ca = this._v.otherString.get(this._integer);
                this._characters = ca.ch;
                this._charactersOffset = ca.start;
                this._charBufferLength = ca.length;
                return;
            case 2:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.commentIIAlgorithmNotSupported"));
            case 3:
                this._characters = this._charBuffer;
                this._charactersOffset = 0;
                this._charBufferLength = 0;
                return;
            default:
                return;
        }
    }

    protected final void processProcessingII() throws FastInfosetException, IOException {
        this._eventType = 3;
        this._piTarget = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
        switch (decodeNonIdentifyingStringOnFirstBit()) {
            case 0:
                this._piData = new String(this._charBuffer, 0, this._charBufferLength);
                if (this._addToTable) {
                    this._v.otherString.add(new CharArrayString(this._piData));
                    return;
                }
                return;
            case 1:
                this._piData = this._v.otherString.get(this._integer).toString();
                return;
            case 2:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
            case 3:
                this._piData = "";
                return;
            default:
                return;
        }
    }

    protected final void processUnexpandedEntityReference(int b2) throws FastInfosetException, IOException {
        this._eventType = 9;
        String entity_reference_name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
        String system_identifier = (b2 & 2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
        String public_identifier = (b2 & 1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "processUnexpandedEntityReference: entity_reference_name={0} system_identifier={1}public_identifier={2}", new Object[]{entity_reference_name, system_identifier, public_identifier});
        }
    }

    protected final void processCIIEncodingAlgorithm(boolean addToTable) throws FastInfosetException, IOException {
        this._algorithmData = this._octetBuffer;
        this._algorithmDataOffset = this._octetBufferStart;
        this._algorithmDataLength = this._octetBufferLength;
        this._isAlgorithmDataCloned = false;
        if (this._algorithmId >= 32) {
            this._algorithmURI = this._v.encodingAlgorithm.get(this._algorithmId - 32);
            if (this._algorithmURI == null) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent", new Object[]{Integer.valueOf(this._identifier)}));
            }
        } else if (this._algorithmId > 9) {
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
        }
        if (addToTable) {
            convertEncodingAlgorithmDataToCharacters();
            this._characterContentChunkTable.add(this._characters, this._characters.length);
        }
    }

    protected final void processAIIEncodingAlgorithm(QualifiedName name, boolean addToTable) throws FastInfosetException, IOException {
        Object algorithmData;
        EncodingAlgorithm ea = null;
        String URI = null;
        if (this._identifier >= 32) {
            URI = this._v.encodingAlgorithm.get(this._identifier - 32);
            if (URI == null) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.URINotPresent", new Object[]{Integer.valueOf(this._identifier)}));
            }
            if (this._registeredEncodingAlgorithms != null) {
                ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
            }
        } else {
            if (this._identifier >= 9) {
                if (this._identifier == 9) {
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
                }
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
            }
            ea = BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier);
        }
        if (ea != null) {
            algorithmData = ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
        } else {
            byte[] data = new byte[this._octetBufferLength];
            System.arraycopy(this._octetBuffer, this._octetBufferStart, data, 0, this._octetBufferLength);
            algorithmData = data;
        }
        this._attributes.addAttributeWithAlgorithmData(name, URI, this._identifier, algorithmData);
        if (addToTable) {
            this._attributeValueTable.add(this._attributes.getValue(this._attributes.getIndex(name.qName)));
        }
    }

    protected final void convertEncodingAlgorithmDataToCharacters() throws FastInfosetException, IOException {
        StringBuffer buffer = new StringBuffer();
        if (this._algorithmId == 1) {
            convertBase64AlorithmDataToCharacters(buffer);
        } else if (this._algorithmId < 9) {
            Object array = BuiltInEncodingAlgorithmFactory.getAlgorithm(this._algorithmId).decodeFromBytes(this._algorithmData, this._algorithmDataOffset, this._algorithmDataLength);
            BuiltInEncodingAlgorithmFactory.getAlgorithm(this._algorithmId).convertToCharacters(array, buffer);
        } else {
            if (this._algorithmId == 9) {
                this._octetBufferOffset -= this._octetBufferLength;
                decodeUtf8StringIntoCharBuffer();
                this._characters = this._charBuffer;
                this._charactersOffset = 0;
                return;
            }
            if (this._algorithmId >= 32) {
                EncodingAlgorithm ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(this._algorithmURI);
                if (ea != null) {
                    Object data = ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                    ea.convertToCharacters(data, buffer);
                } else {
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmDataCannotBeReported"));
                }
            }
        }
        this._characters = new char[buffer.length()];
        buffer.getChars(0, buffer.length(), this._characters, 0);
        this._charactersOffset = 0;
        this._charBufferLength = this._characters.length;
    }

    protected void convertBase64AlorithmDataToCharacters(StringBuffer buffer) throws IOException, EncodingAlgorithmException {
        int afterTaleOffset = 0;
        if (this.base64TaleLength > 0) {
            int bytesToCopy = Math.min(3 - this.base64TaleLength, this._algorithmDataLength);
            System.arraycopy(this._algorithmData, this._algorithmDataOffset, this.base64TaleBytes, this.base64TaleLength, bytesToCopy);
            if (this.base64TaleLength + bytesToCopy == 3) {
                base64DecodeWithCloning(buffer, this.base64TaleBytes, 0, 3);
                afterTaleOffset = bytesToCopy;
                this.base64TaleLength = 0;
            } else if (!isBase64Follows()) {
                base64DecodeWithCloning(buffer, this.base64TaleBytes, 0, this.base64TaleLength + bytesToCopy);
                return;
            } else {
                this.base64TaleLength += bytesToCopy;
                return;
            }
        }
        int taleBytesRemaining = isBase64Follows() ? (this._algorithmDataLength - afterTaleOffset) % 3 : 0;
        if (this._isAlgorithmDataCloned) {
            base64DecodeWithoutCloning(buffer, this._algorithmData, this._algorithmDataOffset + afterTaleOffset, (this._algorithmDataLength - afterTaleOffset) - taleBytesRemaining);
        } else {
            base64DecodeWithCloning(buffer, this._algorithmData, this._algorithmDataOffset + afterTaleOffset, (this._algorithmDataLength - afterTaleOffset) - taleBytesRemaining);
        }
        if (taleBytesRemaining > 0) {
            System.arraycopy(this._algorithmData, (this._algorithmDataOffset + this._algorithmDataLength) - taleBytesRemaining, this.base64TaleBytes, 0, taleBytesRemaining);
            this.base64TaleLength = taleBytesRemaining;
        }
    }

    private void base64DecodeWithCloning(StringBuffer dstBuffer, byte[] data, int offset, int length) throws EncodingAlgorithmException {
        Object array = BuiltInEncodingAlgorithmFactory.base64EncodingAlgorithm.decodeFromBytes(data, offset, length);
        BuiltInEncodingAlgorithmFactory.base64EncodingAlgorithm.convertToCharacters(array, dstBuffer);
    }

    private void base64DecodeWithoutCloning(StringBuffer dstBuffer, byte[] data, int offset, int length) throws EncodingAlgorithmException {
        BuiltInEncodingAlgorithmFactory.base64EncodingAlgorithm.convertToCharacters(data, offset, length, dstBuffer);
    }

    public boolean isBase64Follows() throws IOException {
        int b2 = peek(this);
        switch (DecoderStateTables.EII(b2)) {
            case 13:
                int algorithmId = (b2 & 2) << 6;
                int b22 = peek2(this);
                if ((algorithmId | ((b22 & 252) >> 2)) == 1) {
                }
                break;
        }
        return false;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/StAXDocumentParser$NamespaceContextImpl.class */
    protected class NamespaceContextImpl implements NamespaceContext {
        protected NamespaceContextImpl() {
        }

        @Override // javax.xml.namespace.NamespaceContext
        public final String getNamespaceURI(String prefix) {
            return StAXDocumentParser.this._prefixTable.getNamespaceFromPrefix(prefix);
        }

        @Override // javax.xml.namespace.NamespaceContext
        public final String getPrefix(String namespaceURI) {
            return StAXDocumentParser.this._prefixTable.getPrefixFromNamespace(namespaceURI);
        }

        @Override // javax.xml.namespace.NamespaceContext
        public final Iterator getPrefixes(String namespaceURI) {
            return StAXDocumentParser.this._prefixTable.getPrefixesFromNamespace(namespaceURI);
        }
    }

    public final String getNamespaceDecl(String prefix) {
        return this._prefixTable.getNamespaceFromPrefix(prefix);
    }

    public final String getURI(String prefix) {
        return getNamespaceDecl(prefix);
    }

    public final Iterator getPrefixes() {
        return this._prefixTable.getPrefixes();
    }

    public final AttributesHolder getAttributesHolder() {
        return this._attributes;
    }

    public final void setManager(StAXManager manager) {
        this._manager = manager;
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
            default:
                return "UNKNOWN_EVENT_TYPE";
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
        }
    }
}
