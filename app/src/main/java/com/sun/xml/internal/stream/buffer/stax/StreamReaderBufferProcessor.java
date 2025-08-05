package com.sun.xml.internal.stream.buffer.stax;

import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx;
import com.sun.xml.internal.stream.buffer.AbstractProcessor;
import com.sun.xml.internal.stream.buffer.AttributesHolder;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferMark;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/StreamReaderBufferProcessor.class */
public class StreamReaderBufferProcessor extends AbstractProcessor implements XMLStreamReaderEx {
    private static final int CACHE_SIZE = 16;
    protected ElementStackEntry[] _stack;
    protected ElementStackEntry _stackTop;
    protected int _depth;
    protected String[] _namespaceAIIsPrefix;
    protected String[] _namespaceAIIsNamespaceName;
    protected int _namespaceAIIsEnd;
    protected InternalNamespaceContext _nsCtx;
    protected int _eventType;
    protected AttributesHolder _attributeCache;
    protected CharSequence _charSequence;
    protected char[] _characters;
    protected int _textOffset;
    protected int _textLen;
    protected String _piTarget;
    protected String _piData;
    private static final int PARSING = 1;
    private static final int PENDING_END_DOCUMENT = 2;
    private static final int COMPLETED = 3;
    private int _completionState;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StreamReaderBufferProcessor.class.desiredAssertionStatus();
    }

    public StreamReaderBufferProcessor() {
        this._stack = new ElementStackEntry[16];
        this._namespaceAIIsPrefix = new String[16];
        this._namespaceAIIsNamespaceName = new String[16];
        this._nsCtx = new InternalNamespaceContext();
        for (int i2 = 0; i2 < this._stack.length; i2++) {
            this._stack[i2] = new ElementStackEntry();
        }
        this._attributeCache = new AttributesHolder();
    }

    public StreamReaderBufferProcessor(XMLStreamBuffer buffer) throws XMLStreamException {
        this();
        setXMLStreamBuffer(buffer);
    }

    public void setXMLStreamBuffer(XMLStreamBuffer buffer) throws XMLStreamException {
        setBuffer(buffer, buffer.isFragment());
        this._completionState = 1;
        this._namespaceAIIsEnd = 0;
        this._characters = null;
        this._charSequence = null;
        this._eventType = 7;
    }

    public XMLStreamBuffer nextTagAndMark() throws XMLStreamException {
        do {
            int s2 = peekStructure();
            if ((s2 & 240) == 32) {
                Map<String, String> inscope = new HashMap<>(this._namespaceAIIsEnd);
                for (int i2 = 0; i2 < this._namespaceAIIsEnd; i2++) {
                    inscope.put(this._namespaceAIIsPrefix[i2], this._namespaceAIIsNamespaceName[i2]);
                }
                XMLStreamBufferMark mark = new XMLStreamBufferMark(inscope, this);
                next();
                return mark;
            }
            if ((s2 & 240) == 16) {
                readStructure();
                XMLStreamBufferMark mark2 = new XMLStreamBufferMark(new HashMap(this._namespaceAIIsEnd), this);
                next();
                return mark2;
            }
        } while (next() != 2);
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Object getProperty(String name) {
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int next() throws XMLStreamException {
        switch (this._completionState) {
            case 2:
                this._namespaceAIIsEnd = 0;
                this._completionState = 3;
                this._eventType = 8;
                return 8;
            case 3:
                throw new XMLStreamException("Invalid State");
            default:
                switch (this._eventType) {
                    case 2:
                        if (this._depth > 1) {
                            this._depth--;
                            popElementStack(this._depth);
                            break;
                        } else if (this._depth == 1) {
                            this._depth--;
                            break;
                        }
                        break;
                }
                this._characters = null;
                this._charSequence = null;
                while (true) {
                    int eiiState = readEiiState();
                    switch (eiiState) {
                        case 1:
                        case 2:
                        default:
                            throw new XMLStreamException("Internal XSB error: Invalid State=" + eiiState);
                        case 3:
                            String uri = readStructureString();
                            String localName = readStructureString();
                            String prefix = getPrefixFromQName(readStructureString());
                            processElement(prefix, uri, localName, isInscope(this._depth));
                            this._eventType = 1;
                            return 1;
                        case 4:
                            processElement(readStructureString(), readStructureString(), readStructureString(), isInscope(this._depth));
                            this._eventType = 1;
                            return 1;
                        case 5:
                            processElement(null, readStructureString(), readStructureString(), isInscope(this._depth));
                            this._eventType = 1;
                            return 1;
                        case 6:
                            processElement(null, null, readStructureString(), isInscope(this._depth));
                            this._eventType = 1;
                            return 1;
                        case 7:
                            this._textLen = readStructure();
                            this._textOffset = readContentCharactersBuffer(this._textLen);
                            this._characters = this._contentCharactersBuffer;
                            this._eventType = 4;
                            return 4;
                        case 8:
                            this._textLen = readStructure16();
                            this._textOffset = readContentCharactersBuffer(this._textLen);
                            this._characters = this._contentCharactersBuffer;
                            this._eventType = 4;
                            return 4;
                        case 9:
                            this._characters = readContentCharactersCopy();
                            this._textLen = this._characters.length;
                            this._textOffset = 0;
                            this._eventType = 4;
                            return 4;
                        case 10:
                            this._eventType = 4;
                            this._charSequence = readContentString();
                            this._eventType = 4;
                            return 4;
                        case 11:
                            this._eventType = 4;
                            this._charSequence = (CharSequence) readContentObject();
                            this._eventType = 4;
                            return 4;
                        case 12:
                            this._textLen = readStructure();
                            this._textOffset = readContentCharactersBuffer(this._textLen);
                            this._characters = this._contentCharactersBuffer;
                            this._eventType = 5;
                            return 5;
                        case 13:
                            this._textLen = readStructure16();
                            this._textOffset = readContentCharactersBuffer(this._textLen);
                            this._characters = this._contentCharactersBuffer;
                            this._eventType = 5;
                            return 5;
                        case 14:
                            this._characters = readContentCharactersCopy();
                            this._textLen = this._characters.length;
                            this._textOffset = 0;
                            this._eventType = 5;
                            return 5;
                        case 15:
                            this._charSequence = readContentString();
                            this._eventType = 5;
                            return 5;
                        case 16:
                            this._piTarget = readStructureString();
                            this._piData = readStructureString();
                            this._eventType = 3;
                            return 3;
                        case 17:
                            if (this._depth > 1) {
                                this._eventType = 2;
                                return 2;
                            }
                            if (this._depth == 1) {
                                if (this._fragmentMode) {
                                    int i2 = this._treeCount - 1;
                                    this._treeCount = i2;
                                    if (i2 == 0) {
                                        this._completionState = 2;
                                    }
                                }
                                this._eventType = 2;
                                return 2;
                            }
                            this._namespaceAIIsEnd = 0;
                            this._completionState = 3;
                            this._eventType = 8;
                            return 8;
                    }
                }
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        if (type != this._eventType) {
            throw new XMLStreamException("");
        }
        if (namespaceURI != null && !namespaceURI.equals(getNamespaceURI())) {
            throw new XMLStreamException("");
        }
        if (localName != null && !localName.equals(getLocalName())) {
            throw new XMLStreamException("");
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx
    public final String getElementTextTrim() throws XMLStreamException {
        return getElementText().trim();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getElementText() throws XMLStreamException {
        if (this._eventType != 1) {
            throw new XMLStreamException("");
        }
        next();
        return getElementText(true);
    }

    public final String getElementText(boolean startElementRead) throws XMLStreamException {
        if (!startElementRead) {
            throw new XMLStreamException("");
        }
        int eventType = getEventType();
        StringBuilder content = new StringBuilder();
        while (eventType != 2) {
            if (eventType == 4 || eventType == 12 || eventType == 6 || eventType == 9) {
                content.append(getText());
            } else if (eventType != 3 && eventType != 5) {
                if (eventType == 8) {
                    throw new XMLStreamException("");
                }
                if (eventType == 1) {
                    throw new XMLStreamException("");
                }
                throw new XMLStreamException("");
            }
            eventType = next();
        }
        return content.toString();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int nextTag() throws XMLStreamException {
        next();
        return nextTag(true);
    }

    public final int nextTag(boolean currentTagRead) throws XMLStreamException {
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
            throw new XMLStreamException("");
        }
        return eventType;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean hasNext() {
        return this._eventType != 8;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void close() throws XMLStreamException {
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
            for (int i2 = start; i2 < length; i2++) {
                char c2 = ch[i2];
                if (c2 != ' ' && c2 != '\t' && c2 != '\r' && c2 != '\n') {
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
            throw new IllegalStateException("");
        }
        if (namespaceURI == null) {
            namespaceURI = "";
        }
        return this._attributeCache.getValue(namespaceURI, localName);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getAttributeCount() {
        if (this._eventType != 1) {
            throw new IllegalStateException("");
        }
        return this._attributeCache.getLength();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final QName getAttributeName(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException("");
        }
        String prefix = this._attributeCache.getPrefix(index);
        String localName = this._attributeCache.getLocalName(index);
        String uri = this._attributeCache.getURI(index);
        return new QName(uri, localName, prefix);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributeNamespace(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException("");
        }
        return fixEmptyString(this._attributeCache.getURI(index));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributeLocalName(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException("");
        }
        return this._attributeCache.getLocalName(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributePrefix(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException("");
        }
        return fixEmptyString(this._attributeCache.getPrefix(index));
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributeType(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException("");
        }
        return this._attributeCache.getType(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getAttributeValue(int index) {
        if (this._eventType != 1) {
            throw new IllegalStateException("");
        }
        return this._attributeCache.getValue(index);
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean isAttributeSpecified(int index) {
        return false;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getNamespaceCount() {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._stackTop.namespaceAIIsEnd - this._stackTop.namespaceAIIsStart;
        }
        throw new IllegalStateException("");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getNamespacePrefix(int index) {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._namespaceAIIsPrefix[this._stackTop.namespaceAIIsStart + index];
        }
        throw new IllegalStateException("");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getNamespaceURI(int index) {
        if (this._eventType == 1 || this._eventType == 2) {
            return this._namespaceAIIsNamespaceName[this._stackTop.namespaceAIIsStart + index];
        }
        throw new IllegalStateException("");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getNamespaceURI(String prefix) {
        return this._nsCtx.getNamespaceURI(prefix);
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx, javax.xml.stream.XMLStreamReader
    public final NamespaceContextEx getNamespaceContext() {
        return this._nsCtx;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getEventType() {
        return this._eventType;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getText() {
        if (this._characters != null) {
            String s2 = new String(this._characters, this._textOffset, this._textLen);
            this._charSequence = s2;
            return s2;
        }
        if (this._charSequence != null) {
            return this._charSequence.toString();
        }
        throw new IllegalStateException();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final char[] getTextCharacters() {
        if (this._characters != null) {
            return this._characters;
        }
        if (this._charSequence != null) {
            this._characters = this._charSequence.toString().toCharArray();
            this._textLen = this._characters.length;
            this._textOffset = 0;
            return this._characters;
        }
        throw new IllegalStateException();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getTextStart() {
        if (this._characters != null) {
            return this._textOffset;
        }
        if (this._charSequence != null) {
            return 0;
        }
        throw new IllegalStateException();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getTextLength() {
        if (this._characters != null) {
            return this._textLen;
        }
        if (this._charSequence != null) {
            return this._charSequence.length();
        }
        throw new IllegalStateException();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        if (this._characters == null) {
            if (this._charSequence != null) {
                this._characters = this._charSequence.toString().toCharArray();
                this._textLen = this._characters.length;
                this._textOffset = 0;
            } else {
                throw new IllegalStateException("");
            }
        }
        try {
            int remaining = this._textLen - sourceStart;
            int len = remaining > length ? length : remaining;
            System.arraycopy(this._characters, sourceStart + this._textOffset, target, targetStart, len);
            return len;
        } catch (IndexOutOfBoundsException e2) {
            throw new XMLStreamException(e2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/StreamReaderBufferProcessor$CharSequenceImpl.class */
    private class CharSequenceImpl implements CharSequence {
        private final int _offset;
        private final int _length;

        CharSequenceImpl(int offset, int length) {
            this._offset = offset;
            this._length = length;
        }

        @Override // java.lang.CharSequence
        public int length() {
            return this._length;
        }

        @Override // java.lang.CharSequence
        public char charAt(int index) {
            if (index >= 0 && index < StreamReaderBufferProcessor.this._textLen) {
                return StreamReaderBufferProcessor.this._characters[StreamReaderBufferProcessor.this._textOffset + index];
            }
            throw new IndexOutOfBoundsException();
        }

        @Override // java.lang.CharSequence
        public CharSequence subSequence(int start, int end) {
            int length = end - start;
            if (end < 0 || start < 0 || end > length || start > end) {
                throw new IndexOutOfBoundsException();
            }
            return StreamReaderBufferProcessor.this.new CharSequenceImpl(this._offset + start, length);
        }

        @Override // java.lang.CharSequence
        public String toString() {
            return new String(StreamReaderBufferProcessor.this._characters, this._offset, this._length);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx
    public final CharSequence getPCDATA() {
        if (this._characters != null) {
            return new CharSequenceImpl(this._textOffset, this._textLen);
        }
        if (this._charSequence != null) {
            return this._charSequence;
        }
        throw new IllegalStateException();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getEncoding() {
        return "UTF-8";
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean hasText() {
        return (this._characters == null && this._charSequence == null) ? false : true;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final Location getLocation() {
        return new DummyLocation();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final boolean hasName() {
        return this._eventType == 1 || this._eventType == 2;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final QName getName() {
        return this._stackTop.getQName();
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getLocalName() {
        return this._stackTop.localName;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getNamespaceURI() {
        return this._stackTop.uri;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getPrefix() {
        return this._stackTop.prefix;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getVersion() {
        return "1.0";
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
        return "UTF-8";
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getPITarget() {
        if (this._eventType == 3) {
            return this._piTarget;
        }
        throw new IllegalStateException("");
    }

    @Override // javax.xml.stream.XMLStreamReader
    public final String getPIData() {
        if (this._eventType == 3) {
            return this._piData;
        }
        throw new IllegalStateException("");
    }

    protected void processElement(String prefix, String uri, String localName, boolean inscope) {
        pushElementStack();
        this._stackTop.set(prefix, uri, localName);
        this._attributeCache.clear();
        int item = peekStructure();
        if ((item & 240) == 64 || inscope) {
            item = processNamespaceAttributes(item, inscope);
        }
        if ((item & 240) == 48) {
            processAttributes(item);
        }
    }

    private boolean isInscope(int depth) {
        return this._buffer.getInscopeNamespaces().size() > 0 && depth == 0;
    }

    private void resizeNamespaceAttributes() {
        String[] namespaceAIIsPrefix = new String[this._namespaceAIIsEnd * 2];
        System.arraycopy(this._namespaceAIIsPrefix, 0, namespaceAIIsPrefix, 0, this._namespaceAIIsEnd);
        this._namespaceAIIsPrefix = namespaceAIIsPrefix;
        String[] namespaceAIIsNamespaceName = new String[this._namespaceAIIsEnd * 2];
        System.arraycopy(this._namespaceAIIsNamespaceName, 0, namespaceAIIsNamespaceName, 0, this._namespaceAIIsEnd);
        this._namespaceAIIsNamespaceName = namespaceAIIsNamespaceName;
    }

    private int processNamespaceAttributes(int item, boolean inscope) {
        this._stackTop.namespaceAIIsStart = this._namespaceAIIsEnd;
        Set<String> prefixSet = inscope ? new HashSet<>() : Collections.emptySet();
        while ((item & 240) == 64) {
            if (this._namespaceAIIsEnd == this._namespaceAIIsPrefix.length) {
                resizeNamespaceAttributes();
            }
            switch (getNIIState(item)) {
                case 1:
                    String[] strArr = this._namespaceAIIsPrefix;
                    int i2 = this._namespaceAIIsEnd;
                    String[] strArr2 = this._namespaceAIIsNamespaceName;
                    int i3 = this._namespaceAIIsEnd;
                    this._namespaceAIIsEnd = i3 + 1;
                    strArr2[i3] = "";
                    strArr[i2] = "";
                    if (inscope) {
                        prefixSet.add("");
                        break;
                    } else {
                        break;
                    }
                case 2:
                    this._namespaceAIIsPrefix[this._namespaceAIIsEnd] = readStructureString();
                    if (inscope) {
                        prefixSet.add(this._namespaceAIIsPrefix[this._namespaceAIIsEnd]);
                    }
                    String[] strArr3 = this._namespaceAIIsNamespaceName;
                    int i4 = this._namespaceAIIsEnd;
                    this._namespaceAIIsEnd = i4 + 1;
                    strArr3[i4] = "";
                    break;
                case 3:
                    this._namespaceAIIsPrefix[this._namespaceAIIsEnd] = readStructureString();
                    if (inscope) {
                        prefixSet.add(this._namespaceAIIsPrefix[this._namespaceAIIsEnd]);
                    }
                    String[] strArr4 = this._namespaceAIIsNamespaceName;
                    int i5 = this._namespaceAIIsEnd;
                    this._namespaceAIIsEnd = i5 + 1;
                    strArr4[i5] = readStructureString();
                    break;
                case 4:
                    this._namespaceAIIsPrefix[this._namespaceAIIsEnd] = "";
                    if (inscope) {
                        prefixSet.add("");
                    }
                    String[] strArr5 = this._namespaceAIIsNamespaceName;
                    int i6 = this._namespaceAIIsEnd;
                    this._namespaceAIIsEnd = i6 + 1;
                    strArr5[i6] = readStructureString();
                    break;
            }
            readStructure();
            item = peekStructure();
        }
        if (inscope) {
            for (Map.Entry<String, String> e2 : this._buffer.getInscopeNamespaces().entrySet()) {
                String key = fixNull(e2.getKey());
                if (!prefixSet.contains(key)) {
                    if (this._namespaceAIIsEnd == this._namespaceAIIsPrefix.length) {
                        resizeNamespaceAttributes();
                    }
                    this._namespaceAIIsPrefix[this._namespaceAIIsEnd] = key;
                    String[] strArr6 = this._namespaceAIIsNamespaceName;
                    int i7 = this._namespaceAIIsEnd;
                    this._namespaceAIIsEnd = i7 + 1;
                    strArr6[i7] = e2.getValue();
                }
            }
        }
        this._stackTop.namespaceAIIsEnd = this._namespaceAIIsEnd;
        return item;
    }

    private static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }

    private void processAttributes(int item) {
        do {
            switch (getAIIState(item)) {
                case 1:
                    String uri = readStructureString();
                    String localName = readStructureString();
                    String prefix = getPrefixFromQName(readStructureString());
                    this._attributeCache.addAttributeWithPrefix(prefix, uri, localName, readStructureString(), readContentString());
                    break;
                case 2:
                    this._attributeCache.addAttributeWithPrefix(readStructureString(), readStructureString(), readStructureString(), readStructureString(), readContentString());
                    break;
                case 3:
                    this._attributeCache.addAttributeWithPrefix("", readStructureString(), readStructureString(), readStructureString(), readContentString());
                    break;
                case 4:
                    this._attributeCache.addAttributeWithPrefix("", "", readStructureString(), readStructureString(), readContentString());
                    break;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError((Object) ("Internal XSB Error: wrong attribute state, Item=" + item));
                    }
                    break;
            }
            readStructure();
            item = peekStructure();
        } while ((item & 240) == 48);
    }

    private void pushElementStack() {
        if (this._depth == this._stack.length) {
            ElementStackEntry[] tmp = this._stack;
            this._stack = new ElementStackEntry[((this._stack.length * 3) / 2) + 1];
            System.arraycopy(tmp, 0, this._stack, 0, tmp.length);
            for (int i2 = tmp.length; i2 < this._stack.length; i2++) {
                this._stack[i2] = new ElementStackEntry();
            }
        }
        ElementStackEntry[] elementStackEntryArr = this._stack;
        int i3 = this._depth;
        this._depth = i3 + 1;
        this._stackTop = elementStackEntryArr[i3];
    }

    private void popElementStack(int depth) {
        this._stackTop = this._stack[depth - 1];
        this._namespaceAIIsEnd = this._stack[depth].namespaceAIIsStart;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/StreamReaderBufferProcessor$ElementStackEntry.class */
    private final class ElementStackEntry {
        String prefix;
        String uri;
        String localName;
        QName qname;
        int namespaceAIIsStart;
        int namespaceAIIsEnd;

        private ElementStackEntry() {
        }

        public void set(String prefix, String uri, String localName) {
            this.prefix = prefix;
            this.uri = uri;
            this.localName = localName;
            this.qname = null;
            int i2 = StreamReaderBufferProcessor.this._namespaceAIIsEnd;
            this.namespaceAIIsEnd = i2;
            this.namespaceAIIsStart = i2;
        }

        public QName getQName() {
            if (this.qname == null) {
                this.qname = new QName(fixNull(this.uri), this.localName, fixNull(this.prefix));
            }
            return this.qname;
        }

        private String fixNull(String s2) {
            return s2 == null ? "" : s2;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/StreamReaderBufferProcessor$InternalNamespaceContext.class */
    private final class InternalNamespaceContext implements NamespaceContextEx {
        private InternalNamespaceContext() {
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getNamespaceURI(String prefix) {
            if (prefix != null) {
                if (StreamReaderBufferProcessor.this._stringInterningFeature) {
                    prefix = prefix.intern();
                    for (int i2 = StreamReaderBufferProcessor.this._namespaceAIIsEnd - 1; i2 >= 0; i2--) {
                        if (prefix == StreamReaderBufferProcessor.this._namespaceAIIsPrefix[i2]) {
                            return StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[i2];
                        }
                    }
                } else {
                    for (int i3 = StreamReaderBufferProcessor.this._namespaceAIIsEnd - 1; i3 >= 0; i3--) {
                        if (prefix.equals(StreamReaderBufferProcessor.this._namespaceAIIsPrefix[i3])) {
                            return StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[i3];
                        }
                    }
                }
                if (prefix.equals("xml")) {
                    return "http://www.w3.org/XML/1998/namespace";
                }
                if (prefix.equals("xmlns")) {
                    return "http://www.w3.org/2000/xmlns/";
                }
                return null;
            }
            throw new IllegalArgumentException("Prefix cannot be null");
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String namespaceURI) {
            Iterator i2 = getPrefixes(namespaceURI);
            if (i2.hasNext()) {
                return (String) i2.next();
            }
            return null;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator getPrefixes(final String namespaceURI) {
            if (namespaceURI == null) {
                throw new IllegalArgumentException("NamespaceURI cannot be null");
            }
            if (namespaceURI.equals("http://www.w3.org/XML/1998/namespace")) {
                return Collections.singletonList("xml").iterator();
            }
            if (namespaceURI.equals("http://www.w3.org/2000/xmlns/")) {
                return Collections.singletonList("xmlns").iterator();
            }
            return new Iterator() { // from class: com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferProcessor.InternalNamespaceContext.1

                /* renamed from: i, reason: collision with root package name */
                private int f12081i;
                private boolean requireFindNext = true;

                /* renamed from: p, reason: collision with root package name */
                private String f12082p;

                {
                    this.f12081i = StreamReaderBufferProcessor.this._namespaceAIIsEnd - 1;
                }

                private String findNext() {
                    while (this.f12081i >= 0) {
                        if (namespaceURI.equals(StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[this.f12081i]) && InternalNamespaceContext.this.getNamespaceURI(StreamReaderBufferProcessor.this._namespaceAIIsPrefix[this.f12081i]).equals(StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[this.f12081i])) {
                            String str = StreamReaderBufferProcessor.this._namespaceAIIsPrefix[this.f12081i];
                            this.f12082p = str;
                            return str;
                        }
                        this.f12081i--;
                    }
                    this.f12082p = null;
                    return null;
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    if (this.requireFindNext) {
                        findNext();
                        this.requireFindNext = false;
                    }
                    return this.f12082p != null;
                }

                @Override // java.util.Iterator
                public Object next() {
                    if (this.requireFindNext) {
                        findNext();
                    }
                    this.requireFindNext = true;
                    if (this.f12082p == null) {
                        throw new NoSuchElementException();
                    }
                    return this.f12082p;
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        /* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/StreamReaderBufferProcessor$InternalNamespaceContext$BindingImpl.class */
        private class BindingImpl implements NamespaceContextEx.Binding {
            final String _prefix;
            final String _namespaceURI;

            BindingImpl(String prefix, String namespaceURI) {
                this._prefix = prefix;
                this._namespaceURI = namespaceURI;
            }

            @Override // com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx.Binding
            public String getPrefix() {
                return this._prefix;
            }

            @Override // com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx.Binding
            public String getNamespaceURI() {
                return this._namespaceURI;
            }
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx, java.lang.Iterable, java.util.List
        public Iterator<NamespaceContextEx.Binding> iterator() {
            return new Iterator<NamespaceContextEx.Binding>() { // from class: com.sun.xml.internal.stream.buffer.stax.StreamReaderBufferProcessor.InternalNamespaceContext.2
                private final int end;
                private int current;
                private boolean requireFindNext = true;
                private NamespaceContextEx.Binding namespace;

                {
                    this.end = StreamReaderBufferProcessor.this._namespaceAIIsEnd - 1;
                    this.current = this.end;
                }

                private NamespaceContextEx.Binding findNext() {
                    while (this.current >= 0) {
                        String prefix = StreamReaderBufferProcessor.this._namespaceAIIsPrefix[this.current];
                        int i2 = this.end;
                        while (i2 > this.current && !prefix.equals(StreamReaderBufferProcessor.this._namespaceAIIsPrefix[i2])) {
                            i2--;
                        }
                        int i3 = i2;
                        int i4 = this.current;
                        this.current = i4 - 1;
                        if (i3 == i4) {
                            BindingImpl bindingImpl = InternalNamespaceContext.this.new BindingImpl(prefix, StreamReaderBufferProcessor.this._namespaceAIIsNamespaceName[this.current]);
                            this.namespace = bindingImpl;
                            return bindingImpl;
                        }
                    }
                    this.namespace = null;
                    return null;
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    if (this.requireFindNext) {
                        findNext();
                        this.requireFindNext = false;
                    }
                    return this.namespace != null;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public NamespaceContextEx.Binding next() {
                    if (this.requireFindNext) {
                        findNext();
                    }
                    this.requireFindNext = true;
                    if (this.namespace == null) {
                        throw new NoSuchElementException();
                    }
                    return this.namespace;
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/stax/StreamReaderBufferProcessor$DummyLocation.class */
    private class DummyLocation implements Location {
        private DummyLocation() {
        }

        @Override // javax.xml.stream.Location
        public int getLineNumber() {
            return -1;
        }

        @Override // javax.xml.stream.Location
        public int getColumnNumber() {
            return -1;
        }

        @Override // javax.xml.stream.Location
        public int getCharacterOffset() {
            return -1;
        }

        @Override // javax.xml.stream.Location
        public String getPublicId() {
            return null;
        }

        @Override // javax.xml.stream.Location
        public String getSystemId() {
            return StreamReaderBufferProcessor.this._buffer.getSystemId();
        }
    }

    private static String fixEmptyString(String s2) {
        if (s2.length() == 0) {
            return null;
        }
        return s2;
    }
}
