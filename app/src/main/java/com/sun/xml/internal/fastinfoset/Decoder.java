package com.sun.xml.internal.fastinfoset;

import com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets;
import com.sun.xml.internal.fastinfoset.org.apache.xerces.util.XMLChar;
import com.sun.xml.internal.fastinfoset.util.CharArray;
import com.sun.xml.internal.fastinfoset.util.CharArrayString;
import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
import com.sun.xml.internal.fastinfoset.util.DuplicateAttributeVerifier;
import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
import com.sun.xml.internal.org.jvnet.fastinfoset.ExternalVocabulary;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/Decoder.class */
public abstract class Decoder implements FastInfosetParser {
    private static final char[] XML_NAMESPACE_NAME_CHARS = "http://www.w3.org/XML/1998/namespace".toCharArray();
    private static final char[] XMLNS_NAMESPACE_PREFIX_CHARS = "xmlns".toCharArray();
    private static final char[] XMLNS_NAMESPACE_NAME_CHARS = "http://www.w3.org/2000/xmlns/".toCharArray();
    public static final String STRING_INTERNING_SYSTEM_PROPERTY = "com.sun.xml.internal.fastinfoset.parser.string-interning";
    public static final String BUFFER_SIZE_SYSTEM_PROPERTY = "com.sun.xml.internal.fastinfoset.parser.buffer-size";
    private static boolean _stringInterningSystemDefault;
    private static int _bufferSizeSystemDefault;
    private InputStream _s;
    private Map _externalVocabularies;
    protected boolean _parseFragments;
    protected boolean _needForceStreamClose;
    protected List _notations;
    protected List _unparsedEntities;
    protected int _b;
    protected boolean _terminate;
    protected boolean _doubleTerminate;
    protected boolean _addToTable;
    protected int _integer;
    protected int _identifier;
    protected int _octetBufferStart;
    protected int _octetBufferOffset;
    protected int _octetBufferEnd;
    protected int _octetBufferLength;
    protected int _charBufferLength;
    protected static final int NISTRING_STRING = 0;
    protected static final int NISTRING_INDEX = 1;
    protected static final int NISTRING_ENCODING_ALGORITHM = 2;
    protected static final int NISTRING_EMPTY_STRING = 3;
    protected int _prefixIndex;
    protected int _namespaceNameIndex;
    private int _bitsLeftInOctet;
    private char _utf8_highSurrogate;
    private char _utf8_lowSurrogate;
    private boolean _stringInterning = _stringInterningSystemDefault;
    protected Map _registeredEncodingAlgorithms = new HashMap();
    protected int _bufferSize = _bufferSizeSystemDefault;
    protected byte[] _octetBuffer = new byte[_bufferSizeSystemDefault];
    protected char[] _charBuffer = new char[512];
    protected DuplicateAttributeVerifier _duplicateAttributeVerifier = new DuplicateAttributeVerifier();
    protected ParserVocabulary _v = new ParserVocabulary();
    protected PrefixArray _prefixTable = this._v.prefix;
    protected QualifiedNameArray _elementNameTable = this._v.elementName;
    protected QualifiedNameArray _attributeNameTable = this._v.attributeName;
    protected ContiguousCharArrayArray _characterContentChunkTable = this._v.characterContentChunk;
    protected StringArray _attributeValueTable = this._v.attributeValue;
    private boolean _vIsInternal = true;

    static {
        _stringInterningSystemDefault = false;
        _bufferSizeSystemDefault = 1024;
        String p2 = System.getProperty(STRING_INTERNING_SYSTEM_PROPERTY, Boolean.toString(_stringInterningSystemDefault));
        _stringInterningSystemDefault = Boolean.valueOf(p2).booleanValue();
        String p3 = System.getProperty(BUFFER_SIZE_SYSTEM_PROPERTY, Integer.toString(_bufferSizeSystemDefault));
        try {
            int i2 = Integer.valueOf(p3).intValue();
            if (i2 > 0) {
                _bufferSizeSystemDefault = i2;
            }
        } catch (NumberFormatException e2) {
        }
    }

    protected Decoder() {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public void setStringInterning(boolean stringInterning) {
        this._stringInterning = stringInterning;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public boolean getStringInterning() {
        return this._stringInterning;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public void setBufferSize(int bufferSize) {
        if (this._bufferSize > this._octetBuffer.length) {
            this._bufferSize = bufferSize;
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public int getBufferSize() {
        return this._bufferSize;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public void setRegisteredEncodingAlgorithms(Map algorithms) {
        this._registeredEncodingAlgorithms = algorithms;
        if (this._registeredEncodingAlgorithms == null) {
            this._registeredEncodingAlgorithms = new HashMap();
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public Map getRegisteredEncodingAlgorithms() {
        return this._registeredEncodingAlgorithms;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public void setExternalVocabularies(Map referencedVocabualries) {
        if (referencedVocabualries != null) {
            this._externalVocabularies = new HashMap();
            this._externalVocabularies.putAll(referencedVocabualries);
        } else {
            this._externalVocabularies = null;
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public Map getExternalVocabularies() {
        return this._externalVocabularies;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public void setParseFragments(boolean parseFragments) {
        this._parseFragments = parseFragments;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public boolean getParseFragments() {
        return this._parseFragments;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public void setForceStreamClose(boolean needForceStreamClose) {
        this._needForceStreamClose = needForceStreamClose;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetParser
    public boolean getForceStreamClose() {
        return this._needForceStreamClose;
    }

    public void reset() {
        this._doubleTerminate = false;
        this._terminate = false;
    }

    public void setVocabulary(ParserVocabulary v2) {
        this._v = v2;
        this._prefixTable = this._v.prefix;
        this._elementNameTable = this._v.elementName;
        this._attributeNameTable = this._v.attributeName;
        this._characterContentChunkTable = this._v.characterContentChunk;
        this._attributeValueTable = this._v.attributeValue;
        this._vIsInternal = false;
    }

    public void setInputStream(InputStream s2) {
        this._s = s2;
        this._octetBufferOffset = 0;
        this._octetBufferEnd = 0;
        if (this._vIsInternal) {
            this._v.clear();
        }
    }

    protected final void decodeDII() throws FastInfosetException, IOException {
        int b2 = read();
        if (b2 == 32) {
            decodeInitialVocabulary();
        } else if (b2 != 0) {
            throw new IOException(CommonResourceBundle.getInstance().getString("message.optinalValues"));
        }
    }

    protected final void decodeAdditionalData() throws FastInfosetException, IOException {
        int noOfItems = decodeNumberOfItemsOfSequence();
        for (int i2 = 0; i2 < noOfItems; i2++) {
            decodeNonEmptyOctetStringOnSecondBitAsUtf8String();
            decodeNonEmptyOctetStringLengthOnSecondBit();
            ensureOctetBufferSize();
            this._octetBufferStart = this._octetBufferOffset;
            this._octetBufferOffset += this._octetBufferLength;
        }
    }

    protected final void decodeInitialVocabulary() throws FastInfosetException, IOException {
        int b2 = read();
        int b22 = read();
        if (b2 == 16 && b22 == 0) {
            decodeExternalVocabularyURI();
            return;
        }
        if ((b2 & 16) > 0) {
            decodeExternalVocabularyURI();
        }
        if ((b2 & 8) > 0) {
            decodeTableItems(this._v.restrictedAlphabet);
        }
        if ((b2 & 4) > 0) {
            decodeTableItems(this._v.encodingAlgorithm);
        }
        if ((b2 & 2) > 0) {
            decodeTableItems(this._v.prefix);
        }
        if ((b2 & 1) > 0) {
            decodeTableItems(this._v.namespaceName);
        }
        if ((b22 & 128) > 0) {
            decodeTableItems(this._v.localName);
        }
        if ((b22 & 64) > 0) {
            decodeTableItems(this._v.otherNCName);
        }
        if ((b22 & 32) > 0) {
            decodeTableItems(this._v.otherURI);
        }
        if ((b22 & 16) > 0) {
            decodeTableItems(this._v.attributeValue);
        }
        if ((b22 & 8) > 0) {
            decodeTableItems(this._v.characterContentChunk);
        }
        if ((b22 & 4) > 0) {
            decodeTableItems(this._v.otherString);
        }
        if ((b22 & 2) > 0) {
            decodeTableItems(this._v.elementName, false);
        }
        if ((b22 & 1) > 0) {
            decodeTableItems(this._v.attributeName, true);
        }
    }

    private void decodeExternalVocabularyURI() throws FastInfosetException, IOException {
        if (this._externalVocabularies == null) {
            throw new IOException(CommonResourceBundle.getInstance().getString("message.noExternalVocabularies"));
        }
        String externalVocabularyURI = decodeNonEmptyOctetStringOnSecondBitAsUtf8String();
        Object o2 = this._externalVocabularies.get(externalVocabularyURI);
        if (o2 instanceof ParserVocabulary) {
            this._v.setReferencedVocabulary(externalVocabularyURI, (ParserVocabulary) o2, false);
        } else {
            if (o2 instanceof ExternalVocabulary) {
                ExternalVocabulary v2 = (ExternalVocabulary) o2;
                ParserVocabulary pv = new ParserVocabulary(v2.vocabulary);
                this._externalVocabularies.put(externalVocabularyURI, pv);
                this._v.setReferencedVocabulary(externalVocabularyURI, pv, false);
                return;
            }
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.externalVocabularyNotRegistered", new Object[]{externalVocabularyURI}));
        }
    }

    private void decodeTableItems(StringArray array) throws FastInfosetException, IOException {
        int noOfItems = decodeNumberOfItemsOfSequence();
        for (int i2 = 0; i2 < noOfItems; i2++) {
            array.add(decodeNonEmptyOctetStringOnSecondBitAsUtf8String());
        }
    }

    private void decodeTableItems(PrefixArray array) throws FastInfosetException, IOException {
        int noOfItems = decodeNumberOfItemsOfSequence();
        for (int i2 = 0; i2 < noOfItems; i2++) {
            array.add(decodeNonEmptyOctetStringOnSecondBitAsUtf8String());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void decodeTableItems(com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray r6) throws com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException, java.io.IOException {
        /*
            r5 = this;
            r0 = r5
            int r0 = r0.decodeNumberOfItemsOfSequence()
            r7 = r0
            r0 = 0
            r8 = r0
        L7:
            r0 = r8
            r1 = r7
            if (r0 >= r1) goto L4a
            r0 = r5
            int r0 = r0.decodeNonIdentifyingStringOnFirstBit()
            switch(r0) {
                case 0: goto L24;
                default: goto L34;
            }
        L24:
            r0 = r6
            r1 = r5
            char[] r1 = r1._charBuffer
            r2 = r5
            int r2 = r2._charBufferLength
            int r0 = r0.add(r1, r2)
            goto L44
        L34:
            com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException r0 = new com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException
            r1 = r0
            com.sun.xml.internal.fastinfoset.CommonResourceBundle r2 = com.sun.xml.internal.fastinfoset.CommonResourceBundle.getInstance()
            java.lang.String r3 = "message.illegalState"
            java.lang.String r2 = r2.getString(r3)
            r1.<init>(r2)
            throw r0
        L44:
            int r8 = r8 + 1
            goto L7
        L4a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.fastinfoset.Decoder.decodeTableItems(com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void decodeTableItems(com.sun.xml.internal.fastinfoset.util.CharArrayArray r9) throws com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException, java.io.IOException {
        /*
            r8 = this;
            r0 = r8
            int r0 = r0.decodeNumberOfItemsOfSequence()
            r10 = r0
            r0 = 0
            r11 = r0
        L7:
            r0 = r11
            r1 = r10
            if (r0 >= r1) goto L52
            r0 = r8
            int r0 = r0.decodeNonIdentifyingStringOnFirstBit()
            switch(r0) {
                case 0: goto L24;
                default: goto L3c;
            }
        L24:
            r0 = r9
            com.sun.xml.internal.fastinfoset.util.CharArray r1 = new com.sun.xml.internal.fastinfoset.util.CharArray
            r2 = r1
            r3 = r8
            char[] r3 = r3._charBuffer
            r4 = 0
            r5 = r8
            int r5 = r5._charBufferLength
            r6 = 1
            r2.<init>(r3, r4, r5, r6)
            r0.add(r1)
            goto L4c
        L3c:
            com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException r0 = new com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException
            r1 = r0
            com.sun.xml.internal.fastinfoset.CommonResourceBundle r2 = com.sun.xml.internal.fastinfoset.CommonResourceBundle.getInstance()
            java.lang.String r3 = "message.illegalState"
            java.lang.String r2 = r2.getString(r3)
            r1.<init>(r2)
            throw r0
        L4c:
            int r11 = r11 + 1
            goto L7
        L52:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.fastinfoset.Decoder.decodeTableItems(com.sun.xml.internal.fastinfoset.util.CharArrayArray):void");
    }

    private void decodeTableItems(QualifiedNameArray array, boolean isAttribute) throws FastInfosetException, IOException {
        int noOfItems = decodeNumberOfItemsOfSequence();
        for (int i2 = 0; i2 < noOfItems; i2++) {
            int b2 = read();
            String prefix = "";
            int prefixIndex = -1;
            if ((b2 & 2) > 0) {
                prefixIndex = decodeIntegerIndexOnSecondBit();
                prefix = this._v.prefix.get(prefixIndex);
            }
            String namespaceName = "";
            int namespaceNameIndex = -1;
            if ((b2 & 1) > 0) {
                namespaceNameIndex = decodeIntegerIndexOnSecondBit();
                namespaceName = this._v.namespaceName.get(namespaceNameIndex);
            }
            if (namespaceName == "" && prefix != "") {
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.missingNamespace"));
            }
            int localNameIndex = decodeIntegerIndexOnSecondBit();
            String localName = this._v.localName.get(localNameIndex);
            QualifiedName qualifiedName = new QualifiedName(prefix, namespaceName, localName, prefixIndex, namespaceNameIndex, localNameIndex, this._charBuffer);
            if (isAttribute) {
                qualifiedName.createAttributeValues(256);
            }
            array.add(qualifiedName);
        }
    }

    private int decodeNumberOfItemsOfSequence() throws IOException {
        int b2 = read();
        if (b2 < 128) {
            return b2 + 1;
        }
        return (((b2 & 15) << 16) | (read() << 8) | read()) + 129;
    }

    protected final void decodeNotations() throws FastInfosetException, IOException {
        int b2;
        if (this._notations == null) {
            this._notations = new ArrayList();
        } else {
            this._notations.clear();
        }
        int i2 = read();
        while (true) {
            b2 = i2;
            if ((b2 & 252) != 192) {
                break;
            }
            String name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
            String system_identifier = (this._b & 2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
            String public_identifier = (this._b & 1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
            Notation notation = new Notation(name, system_identifier, public_identifier);
            this._notations.add(notation);
            i2 = read();
        }
        if (b2 != 240) {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IIsNotTerminatedCorrectly"));
        }
    }

    protected final void decodeUnparsedEntities() throws FastInfosetException, IOException {
        int b2;
        if (this._unparsedEntities == null) {
            this._unparsedEntities = new ArrayList();
        } else {
            this._unparsedEntities.clear();
        }
        int i2 = read();
        while (true) {
            b2 = i2;
            if ((b2 & 254) != 208) {
                break;
            }
            String name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
            String system_identifier = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI);
            String public_identifier = (this._b & 1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : "";
            String notation_name = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
            UnparsedEntity unparsedEntity = new UnparsedEntity(name, system_identifier, public_identifier, notation_name);
            this._unparsedEntities.add(unparsedEntity);
            i2 = read();
        }
        if (b2 != 240) {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.unparsedEntities"));
        }
    }

    protected final String decodeCharacterEncodingScheme() throws FastInfosetException, IOException {
        return decodeNonEmptyOctetStringOnSecondBitAsUtf8String();
    }

    protected final String decodeVersion() throws FastInfosetException, IOException {
        switch (decodeNonIdentifyingStringOnFirstBit()) {
            case 0:
                String data = new String(this._charBuffer, 0, this._charBufferLength);
                if (this._addToTable) {
                    this._v.otherString.add(new CharArrayString(data));
                }
                return data;
            case 1:
                return this._v.otherString.get(this._integer).toString();
            case 2:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingNotSupported"));
            case 3:
            default:
                return "";
        }
    }

    protected final QualifiedName decodeEIIIndexMedium() throws FastInfosetException, IOException {
        int i2 = (((this._b & 7) << 8) | read()) + 32;
        return this._v.elementName._array[i2];
    }

    protected final QualifiedName decodeEIIIndexLarge() throws FastInfosetException, IOException {
        int i2;
        if ((this._b & 48) == 32) {
            i2 = (((this._b & 7) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_3RD_BIT_MEDIUM_LIMIT;
        } else {
            i2 = (((read() & 15) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_3RD_BIT_LARGE_LIMIT;
        }
        return this._v.elementName._array[i2];
    }

    protected final QualifiedName decodeLiteralQualifiedName(int state, QualifiedName q2) throws FastInfosetException, IOException {
        if (q2 == null) {
            q2 = new QualifiedName();
        }
        switch (state) {
            case 0:
                return q2.set("", "", decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, -1, this._identifier, (char[]) null);
            case 1:
                return q2.set("", decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(false), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, this._namespaceNameIndex, this._identifier, (char[]) null);
            case 2:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameMissingNamespaceName"));
            case 3:
                return q2.set(decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(true), decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(true), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), this._prefixIndex, this._namespaceNameIndex, this._identifier, this._charBuffer);
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingEII"));
        }
    }

    protected final int decodeNonIdentifyingStringOnFirstBit() throws FastInfosetException, IOException {
        int b2 = read();
        switch (DecoderStateTables.NISTRING(b2)) {
            case 0:
                this._addToTable = (b2 & 64) > 0;
                this._octetBufferLength = (b2 & 7) + 1;
                decodeUtf8StringAsCharBuffer();
                return 0;
            case 1:
                this._addToTable = (b2 & 64) > 0;
                this._octetBufferLength = read() + 9;
                decodeUtf8StringAsCharBuffer();
                return 0;
            case 2:
                this._addToTable = (b2 & 64) > 0;
                int length = (read() << 24) | (read() << 16) | (read() << 8) | read();
                this._octetBufferLength = length + 265;
                decodeUtf8StringAsCharBuffer();
                return 0;
            case 3:
                this._addToTable = (b2 & 64) > 0;
                this._octetBufferLength = (b2 & 7) + 1;
                decodeUtf16StringAsCharBuffer();
                return 0;
            case 4:
                this._addToTable = (b2 & 64) > 0;
                this._octetBufferLength = read() + 9;
                decodeUtf16StringAsCharBuffer();
                return 0;
            case 5:
                this._addToTable = (b2 & 64) > 0;
                int length2 = (read() << 24) | (read() << 16) | (read() << 8) | read();
                this._octetBufferLength = length2 + 265;
                decodeUtf16StringAsCharBuffer();
                return 0;
            case 6:
                this._addToTable = (b2 & 64) > 0;
                this._identifier = (b2 & 15) << 4;
                int b22 = read();
                this._identifier |= (b22 & 240) >> 4;
                decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b22);
                decodeRestrictedAlphabetAsCharBuffer();
                return 0;
            case 7:
                this._addToTable = (b2 & 64) > 0;
                this._identifier = (b2 & 15) << 4;
                int b23 = read();
                this._identifier |= (b23 & 240) >> 4;
                decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(b23);
                return 2;
            case 8:
                this._integer = b2 & 63;
                return 1;
            case 9:
                this._integer = (((b2 & 31) << 8) | read()) + 64;
                return 1;
            case 10:
                this._integer = (((b2 & 15) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_2ND_BIT_MEDIUM_LIMIT;
                return 1;
            case 11:
                return 3;
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingNonIdentifyingString"));
        }
    }

    protected final void decodeOctetsOnFifthBitOfNonIdentifyingStringOnFirstBit(int b2) throws FastInfosetException, IOException {
        int b3 = b2 & 15;
        switch (DecoderStateTables.NISTRING(b3)) {
            case 0:
                this._octetBufferLength = b3 + 1;
                break;
            case 1:
                this._octetBufferLength = read() + 9;
                break;
            case 2:
                int length = (read() << 24) | (read() << 16) | (read() << 8) | read();
                this._octetBufferLength = length + 265;
                break;
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingOctets"));
        }
        ensureOctetBufferSize();
        this._octetBufferStart = this._octetBufferOffset;
        this._octetBufferOffset += this._octetBufferLength;
    }

    protected final void decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(int b2) throws FastInfosetException, IOException {
        switch (b2 & 3) {
            case 0:
                this._octetBufferLength = 1;
                break;
            case 1:
                this._octetBufferLength = 2;
                break;
            case 2:
                this._octetBufferLength = read() + 3;
                break;
            case 3:
                this._octetBufferLength = (read() << 24) | (read() << 16) | (read() << 8) | read();
                this._octetBufferLength += 259;
                break;
        }
        ensureOctetBufferSize();
        this._octetBufferStart = this._octetBufferOffset;
        this._octetBufferOffset += this._octetBufferLength;
    }

    protected final String decodeIdentifyingNonEmptyStringOnFirstBit(StringArray table) throws FastInfosetException, IOException {
        int b2 = read();
        switch (DecoderStateTables.ISTRING(b2)) {
            case 0:
                this._octetBufferLength = b2 + 1;
                String s2 = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
                this._identifier = table.add(s2) - 1;
                return s2;
            case 1:
                this._octetBufferLength = read() + 65;
                String s3 = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
                this._identifier = table.add(s3) - 1;
                return s3;
            case 2:
                int length = (read() << 24) | (read() << 16) | (read() << 8) | read();
                this._octetBufferLength = length + EncodingConstants.OCTET_STRING_LENGTH_2ND_BIT_MEDIUM_LIMIT;
                String s4 = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
                this._identifier = table.add(s4) - 1;
                return s4;
            case 3:
                this._identifier = b2 & 63;
                return table._array[this._identifier];
            case 4:
                this._identifier = (((b2 & 31) << 8) | read()) + 64;
                return table._array[this._identifier];
            case 5:
                this._identifier = (((b2 & 15) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_2ND_BIT_MEDIUM_LIMIT;
                return table._array[this._identifier];
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingIdentifyingString"));
        }
    }

    protected final String decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(boolean namespaceNamePresent) throws FastInfosetException, IOException {
        int b2 = read();
        switch (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(b2)) {
            case 0:
            case 8:
            case 9:
                this._octetBufferLength = b2 + 1;
                String s2 = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
                this._prefixIndex = this._v.prefix.add(s2);
                return s2;
            case 1:
                this._octetBufferLength = read() + 65;
                String s3 = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
                this._prefixIndex = this._v.prefix.add(s3);
                return s3;
            case 2:
                int length = (read() << 24) | (read() << 16) | (read() << 8) | read();
                this._octetBufferLength = length + EncodingConstants.OCTET_STRING_LENGTH_2ND_BIT_MEDIUM_LIMIT;
                String s4 = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
                this._prefixIndex = this._v.prefix.add(s4);
                return s4;
            case 3:
                this._prefixIndex = b2 & 63;
                return this._v.prefix._array[this._prefixIndex - 1];
            case 4:
                this._prefixIndex = (((b2 & 31) << 8) | read()) + 64;
                return this._v.prefix._array[this._prefixIndex - 1];
            case 5:
                this._prefixIndex = (((b2 & 15) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_2ND_BIT_MEDIUM_LIMIT;
                return this._v.prefix._array[this._prefixIndex - 1];
            case 6:
                this._octetBufferLength = EncodingConstants.XML_NAMESPACE_PREFIX_LENGTH;
                decodeUtf8StringAsCharBuffer();
                if (this._charBuffer[0] == 'x' && this._charBuffer[1] == 'm' && this._charBuffer[2] == 'l') {
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.prefixIllegal"));
                }
                String s5 = this._stringInterning ? new String(this._charBuffer, 0, this._charBufferLength).intern() : new String(this._charBuffer, 0, this._charBufferLength);
                this._prefixIndex = this._v.prefix.add(s5);
                return s5;
            case 7:
                this._octetBufferLength = EncodingConstants.XMLNS_NAMESPACE_PREFIX_LENGTH;
                decodeUtf8StringAsCharBuffer();
                if (this._charBuffer[0] == 'x' && this._charBuffer[1] == 'm' && this._charBuffer[2] == 'l' && this._charBuffer[3] == 'n' && this._charBuffer[4] == 's') {
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.xmlns"));
                }
                String s6 = this._stringInterning ? new String(this._charBuffer, 0, this._charBufferLength).intern() : new String(this._charBuffer, 0, this._charBufferLength);
                this._prefixIndex = this._v.prefix.add(s6);
                return s6;
            case 10:
                if (namespaceNamePresent) {
                    this._prefixIndex = 0;
                    if (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(peek()) != 10) {
                        throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.wrongNamespaceName"));
                    }
                    return "xml";
                }
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.missingNamespaceName"));
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingIdentifyingStringForPrefix"));
        }
    }

    protected final String decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(boolean namespaceNamePresent) throws FastInfosetException, IOException {
        int b2 = read();
        switch (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(b2)) {
            case 3:
                this._prefixIndex = b2 & 63;
                return this._v.prefix._array[this._prefixIndex - 1];
            case 4:
                this._prefixIndex = (((b2 & 31) << 8) | read()) + 64;
                return this._v.prefix._array[this._prefixIndex - 1];
            case 5:
                this._prefixIndex = (((b2 & 15) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_2ND_BIT_MEDIUM_LIMIT;
                return this._v.prefix._array[this._prefixIndex - 1];
            case 6:
            case 7:
            case 8:
            case 9:
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingIdentifyingStringForPrefix"));
            case 10:
                if (namespaceNamePresent) {
                    this._prefixIndex = 0;
                    if (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(peek()) != 10) {
                        throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.wrongNamespaceName"));
                    }
                    return "xml";
                }
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.missingNamespaceName"));
        }
    }

    protected final String decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(boolean prefixPresent) throws FastInfosetException, IOException {
        int b2 = read();
        switch (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(b2)) {
            case 0:
            case 6:
            case 7:
                this._octetBufferLength = b2 + 1;
                String s2 = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
                this._namespaceNameIndex = this._v.namespaceName.add(s2);
                return s2;
            case 1:
                this._octetBufferLength = read() + 65;
                String s3 = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
                this._namespaceNameIndex = this._v.namespaceName.add(s3);
                return s3;
            case 2:
                int length = (read() << 24) | (read() << 16) | (read() << 8) | read();
                this._octetBufferLength = length + EncodingConstants.OCTET_STRING_LENGTH_2ND_BIT_MEDIUM_LIMIT;
                String s4 = this._stringInterning ? decodeUtf8StringAsString().intern() : decodeUtf8StringAsString();
                this._namespaceNameIndex = this._v.namespaceName.add(s4);
                return s4;
            case 3:
                this._namespaceNameIndex = b2 & 63;
                return this._v.namespaceName._array[this._namespaceNameIndex - 1];
            case 4:
                this._namespaceNameIndex = (((b2 & 31) << 8) | read()) + 64;
                return this._v.namespaceName._array[this._namespaceNameIndex - 1];
            case 5:
                this._namespaceNameIndex = (((b2 & 15) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_2ND_BIT_MEDIUM_LIMIT;
                return this._v.namespaceName._array[this._namespaceNameIndex - 1];
            case 8:
                this._octetBufferLength = EncodingConstants.XMLNS_NAMESPACE_NAME_LENGTH;
                decodeUtf8StringAsCharBuffer();
                if (compareCharsWithCharBufferFromEndToStart(XMLNS_NAMESPACE_NAME_CHARS)) {
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.xmlnsConnotBeBoundToPrefix"));
                }
                String s5 = this._stringInterning ? new String(this._charBuffer, 0, this._charBufferLength).intern() : new String(this._charBuffer, 0, this._charBufferLength);
                this._namespaceNameIndex = this._v.namespaceName.add(s5);
                return s5;
            case 9:
                this._octetBufferLength = EncodingConstants.XML_NAMESPACE_NAME_LENGTH;
                decodeUtf8StringAsCharBuffer();
                if (compareCharsWithCharBufferFromEndToStart(XML_NAMESPACE_NAME_CHARS)) {
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.illegalNamespaceName"));
                }
                String s6 = this._stringInterning ? new String(this._charBuffer, 0, this._charBufferLength).intern() : new String(this._charBuffer, 0, this._charBufferLength);
                this._namespaceNameIndex = this._v.namespaceName.add(s6);
                return s6;
            case 10:
                if (prefixPresent) {
                    this._namespaceNameIndex = 0;
                    return "http://www.w3.org/XML/1998/namespace";
                }
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.namespaceWithoutPrefix"));
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingForNamespaceName"));
        }
    }

    protected final String decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(boolean prefixPresent) throws FastInfosetException, IOException {
        int b2 = read();
        switch (DecoderStateTables.ISTRING_PREFIX_NAMESPACE(b2)) {
            case 3:
                this._namespaceNameIndex = b2 & 63;
                return this._v.namespaceName._array[this._namespaceNameIndex - 1];
            case 4:
                this._namespaceNameIndex = (((b2 & 31) << 8) | read()) + 64;
                return this._v.namespaceName._array[this._namespaceNameIndex - 1];
            case 5:
                this._namespaceNameIndex = (((b2 & 15) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_2ND_BIT_MEDIUM_LIMIT;
                return this._v.namespaceName._array[this._namespaceNameIndex - 1];
            case 6:
            case 7:
            case 8:
            case 9:
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingForNamespaceName"));
            case 10:
                if (prefixPresent) {
                    this._namespaceNameIndex = 0;
                    return "http://www.w3.org/XML/1998/namespace";
                }
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.namespaceWithoutPrefix"));
        }
    }

    private boolean compareCharsWithCharBufferFromEndToStart(char[] c2) {
        int i2 = this._charBufferLength;
        do {
            i2--;
            if (i2 < 0) {
                return true;
            }
        } while (c2[i2] == this._charBuffer[i2]);
        return false;
    }

    protected final String decodeNonEmptyOctetStringOnSecondBitAsUtf8String() throws FastInfosetException, IOException {
        decodeNonEmptyOctetStringOnSecondBitAsUtf8CharArray();
        return new String(this._charBuffer, 0, this._charBufferLength);
    }

    protected final void decodeNonEmptyOctetStringOnSecondBitAsUtf8CharArray() throws FastInfosetException, IOException {
        decodeNonEmptyOctetStringLengthOnSecondBit();
        decodeUtf8StringAsCharBuffer();
    }

    protected final void decodeNonEmptyOctetStringLengthOnSecondBit() throws FastInfosetException, IOException {
        int b2 = read();
        switch (DecoderStateTables.ISTRING(b2)) {
            case 0:
                this._octetBufferLength = b2 + 1;
                return;
            case 1:
                this._octetBufferLength = read() + 65;
                return;
            case 2:
                int length = (read() << 24) | (read() << 16) | (read() << 8) | read();
                this._octetBufferLength = length + EncodingConstants.OCTET_STRING_LENGTH_2ND_BIT_MEDIUM_LIMIT;
                return;
            case 3:
            case 4:
            case 5:
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingNonEmptyOctet"));
        }
    }

    protected final int decodeIntegerIndexOnSecondBit() throws FastInfosetException, IOException {
        int b2 = read() | 128;
        switch (DecoderStateTables.ISTRING(b2)) {
            case 0:
            case 1:
            case 2:
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingIndexOnSecondBit"));
            case 3:
                return b2 & 63;
            case 4:
                return (((b2 & 31) << 8) | read()) + 64;
            case 5:
                return (((b2 & 15) << 16) | (read() << 8) | read()) + EncodingConstants.INTEGER_2ND_BIT_MEDIUM_LIMIT;
        }
    }

    protected final void decodeHeader() throws FastInfosetException, IOException {
        if (!_isFastInfosetDocument()) {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.notFIDocument"));
        }
    }

    protected final void decodeRestrictedAlphabetAsCharBuffer() throws FastInfosetException, IOException {
        if (this._identifier <= 1) {
            decodeFourBitAlphabetOctetsAsCharBuffer(BuiltInRestrictedAlphabets.table[this._identifier]);
        } else {
            if (this._identifier >= 32) {
                CharArray ca = this._v.restrictedAlphabet.get(this._identifier - 32);
                if (ca == null) {
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.alphabetNotPresent", new Object[]{Integer.valueOf(this._identifier)}));
                }
                decodeAlphabetOctetsAsCharBuffer(ca.ch);
                return;
            }
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.alphabetIdentifiersReserved"));
        }
    }

    protected final String decodeRestrictedAlphabetAsString() throws FastInfosetException, IOException {
        decodeRestrictedAlphabetAsCharBuffer();
        return new String(this._charBuffer, 0, this._charBufferLength);
    }

    protected final String decodeRAOctetsAsString(char[] restrictedAlphabet) throws FastInfosetException, IOException {
        decodeAlphabetOctetsAsCharBuffer(restrictedAlphabet);
        return new String(this._charBuffer, 0, this._charBufferLength);
    }

    protected final void decodeFourBitAlphabetOctetsAsCharBuffer(char[] restrictedAlphabet) throws FastInfosetException, IOException {
        this._charBufferLength = 0;
        int characters = this._octetBufferLength * 2;
        if (this._charBuffer.length < characters) {
            this._charBuffer = new char[characters];
        }
        for (int i2 = 0; i2 < this._octetBufferLength - 1; i2++) {
            byte[] bArr = this._octetBuffer;
            int i3 = this._octetBufferStart;
            this._octetBufferStart = i3 + 1;
            int v2 = bArr[i3] & 255;
            char[] cArr = this._charBuffer;
            int i4 = this._charBufferLength;
            this._charBufferLength = i4 + 1;
            cArr[i4] = restrictedAlphabet[v2 >> 4];
            char[] cArr2 = this._charBuffer;
            int i5 = this._charBufferLength;
            this._charBufferLength = i5 + 1;
            cArr2[i5] = restrictedAlphabet[v2 & 15];
        }
        byte[] bArr2 = this._octetBuffer;
        int i6 = this._octetBufferStart;
        this._octetBufferStart = i6 + 1;
        int v3 = bArr2[i6] & 255;
        char[] cArr3 = this._charBuffer;
        int i7 = this._charBufferLength;
        this._charBufferLength = i7 + 1;
        cArr3[i7] = restrictedAlphabet[v3 >> 4];
        int v4 = v3 & 15;
        if (v4 != 15) {
            char[] cArr4 = this._charBuffer;
            int i8 = this._charBufferLength;
            this._charBufferLength = i8 + 1;
            cArr4[i8] = restrictedAlphabet[v4 & 15];
        }
    }

    protected final void decodeAlphabetOctetsAsCharBuffer(char[] restrictedAlphabet) throws FastInfosetException, IOException {
        if (restrictedAlphabet.length < 2) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.alphabetMustContain2orMoreChars"));
        }
        int bitsPerCharacter = 1;
        while ((1 << bitsPerCharacter) <= restrictedAlphabet.length) {
            bitsPerCharacter++;
        }
        int terminatingValue = (1 << bitsPerCharacter) - 1;
        int characters = (this._octetBufferLength << 3) / bitsPerCharacter;
        if (characters == 0) {
            throw new IOException("");
        }
        this._charBufferLength = 0;
        if (this._charBuffer.length < characters) {
            this._charBuffer = new char[characters];
        }
        resetBits();
        for (int i2 = 0; i2 < characters; i2++) {
            int value = readBits(bitsPerCharacter);
            if (bitsPerCharacter < 8 && value == terminatingValue) {
                int octetPosition = (i2 * bitsPerCharacter) >>> 3;
                if (octetPosition != this._octetBufferLength - 1) {
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.alphabetIncorrectlyTerminated"));
                }
                return;
            } else {
                char[] cArr = this._charBuffer;
                int i3 = this._charBufferLength;
                this._charBufferLength = i3 + 1;
                cArr[i3] = restrictedAlphabet[value];
            }
        }
    }

    private void resetBits() {
        this._bitsLeftInOctet = 0;
    }

    private int readBits(int bits) throws IOException {
        int i2 = 0;
        while (true) {
            int value = i2;
            if (bits > 0) {
                if (this._bitsLeftInOctet == 0) {
                    byte[] bArr = this._octetBuffer;
                    int i3 = this._octetBufferStart;
                    this._octetBufferStart = i3 + 1;
                    this._b = bArr[i3] & 255;
                    this._bitsLeftInOctet = 8;
                }
                int i4 = this._b;
                int i5 = this._bitsLeftInOctet - 1;
                this._bitsLeftInOctet = i5;
                int bit = (i4 & (1 << i5)) > 0 ? 1 : 0;
                bits--;
                i2 = value | (bit << bits);
            } else {
                return value;
            }
        }
    }

    protected final void decodeUtf8StringAsCharBuffer() throws IOException {
        ensureOctetBufferSize();
        decodeUtf8StringIntoCharBuffer();
    }

    protected final void decodeUtf8StringAsCharBuffer(char[] ch, int offset) throws IOException {
        ensureOctetBufferSize();
        decodeUtf8StringIntoCharBuffer(ch, offset);
    }

    protected final String decodeUtf8StringAsString() throws IOException {
        decodeUtf8StringAsCharBuffer();
        return new String(this._charBuffer, 0, this._charBufferLength);
    }

    protected final void decodeUtf16StringAsCharBuffer() throws IOException {
        ensureOctetBufferSize();
        decodeUtf16StringIntoCharBuffer();
    }

    protected final String decodeUtf16StringAsString() throws IOException {
        decodeUtf16StringAsCharBuffer();
        return new String(this._charBuffer, 0, this._charBufferLength);
    }

    private void ensureOctetBufferSize() throws IOException {
        if (this._octetBufferEnd < this._octetBufferOffset + this._octetBufferLength) {
            int octetsInBuffer = this._octetBufferEnd - this._octetBufferOffset;
            if (this._octetBuffer.length < this._octetBufferLength) {
                byte[] newOctetBuffer = new byte[this._octetBufferLength];
                System.arraycopy(this._octetBuffer, this._octetBufferOffset, newOctetBuffer, 0, octetsInBuffer);
                this._octetBuffer = newOctetBuffer;
            } else {
                System.arraycopy(this._octetBuffer, this._octetBufferOffset, this._octetBuffer, 0, octetsInBuffer);
            }
            this._octetBufferOffset = 0;
            int octetsRead = this._s.read(this._octetBuffer, octetsInBuffer, this._octetBuffer.length - octetsInBuffer);
            if (octetsRead < 0) {
                throw new EOFException("Unexpeceted EOF");
            }
            this._octetBufferEnd = octetsInBuffer + octetsRead;
            if (this._octetBufferEnd < this._octetBufferLength) {
                repeatedRead();
            }
        }
    }

    private void repeatedRead() throws IOException {
        while (this._octetBufferEnd < this._octetBufferLength) {
            int octetsRead = this._s.read(this._octetBuffer, this._octetBufferEnd, this._octetBuffer.length - this._octetBufferEnd);
            if (octetsRead < 0) {
                throw new EOFException("Unexpeceted EOF");
            }
            this._octetBufferEnd += octetsRead;
        }
    }

    protected final void decodeUtf8StringIntoCharBuffer() throws IOException {
        if (this._charBuffer.length < this._octetBufferLength) {
            this._charBuffer = new char[this._octetBufferLength];
        }
        this._charBufferLength = 0;
        int end = this._octetBufferLength + this._octetBufferOffset;
        while (end != this._octetBufferOffset) {
            byte[] bArr = this._octetBuffer;
            int i2 = this._octetBufferOffset;
            this._octetBufferOffset = i2 + 1;
            int b1 = bArr[i2] & 255;
            if (DecoderStateTables.UTF8(b1) == 1) {
                char[] cArr = this._charBuffer;
                int i3 = this._charBufferLength;
                this._charBufferLength = i3 + 1;
                cArr[i3] = (char) b1;
            } else {
                decodeTwoToFourByteUtf8Character(b1, end);
            }
        }
    }

    protected final void decodeUtf8StringIntoCharBuffer(char[] ch, int offset) throws IOException {
        this._charBufferLength = offset;
        int end = this._octetBufferLength + this._octetBufferOffset;
        while (end != this._octetBufferOffset) {
            byte[] bArr = this._octetBuffer;
            int i2 = this._octetBufferOffset;
            this._octetBufferOffset = i2 + 1;
            int b1 = bArr[i2] & 255;
            if (DecoderStateTables.UTF8(b1) == 1) {
                int i3 = this._charBufferLength;
                this._charBufferLength = i3 + 1;
                ch[i3] = (char) b1;
            } else {
                decodeTwoToFourByteUtf8Character(ch, b1, end);
            }
        }
        this._charBufferLength -= offset;
    }

    private void decodeTwoToFourByteUtf8Character(int b1, int end) throws IOException {
        switch (DecoderStateTables.UTF8(b1)) {
            case 2:
                if (end == this._octetBufferOffset) {
                    decodeUtf8StringLengthTooSmall();
                }
                byte[] bArr = this._octetBuffer;
                int i2 = this._octetBufferOffset;
                this._octetBufferOffset = i2 + 1;
                int b2 = bArr[i2] & 255;
                if ((b2 & 192) != 128) {
                    decodeUtf8StringIllegalState();
                }
                char[] cArr = this._charBuffer;
                int i3 = this._charBufferLength;
                this._charBufferLength = i3 + 1;
                cArr[i3] = (char) (((b1 & 31) << 6) | (b2 & 63));
                break;
            case 3:
                char c2 = decodeUtf8ThreeByteChar(end, b1);
                if (XMLChar.isContent(c2)) {
                    char[] cArr2 = this._charBuffer;
                    int i4 = this._charBufferLength;
                    this._charBufferLength = i4 + 1;
                    cArr2[i4] = c2;
                    break;
                } else {
                    decodeUtf8StringIllegalState();
                    break;
                }
            case 4:
                int supplemental = decodeUtf8FourByteChar(end, b1);
                if (XMLChar.isContent(supplemental)) {
                    char[] cArr3 = this._charBuffer;
                    int i5 = this._charBufferLength;
                    this._charBufferLength = i5 + 1;
                    cArr3[i5] = this._utf8_highSurrogate;
                    char[] cArr4 = this._charBuffer;
                    int i6 = this._charBufferLength;
                    this._charBufferLength = i6 + 1;
                    cArr4[i6] = this._utf8_lowSurrogate;
                    break;
                } else {
                    decodeUtf8StringIllegalState();
                    break;
                }
            default:
                decodeUtf8StringIllegalState();
                break;
        }
    }

    private void decodeTwoToFourByteUtf8Character(char[] ch, int b1, int end) throws IOException {
        switch (DecoderStateTables.UTF8(b1)) {
            case 2:
                if (end == this._octetBufferOffset) {
                    decodeUtf8StringLengthTooSmall();
                }
                byte[] bArr = this._octetBuffer;
                int i2 = this._octetBufferOffset;
                this._octetBufferOffset = i2 + 1;
                int b2 = bArr[i2] & 255;
                if ((b2 & 192) != 128) {
                    decodeUtf8StringIllegalState();
                }
                int i3 = this._charBufferLength;
                this._charBufferLength = i3 + 1;
                ch[i3] = (char) (((b1 & 31) << 6) | (b2 & 63));
                break;
            case 3:
                char c2 = decodeUtf8ThreeByteChar(end, b1);
                if (XMLChar.isContent(c2)) {
                    int i4 = this._charBufferLength;
                    this._charBufferLength = i4 + 1;
                    ch[i4] = c2;
                    break;
                } else {
                    decodeUtf8StringIllegalState();
                    break;
                }
            case 4:
                int supplemental = decodeUtf8FourByteChar(end, b1);
                if (XMLChar.isContent(supplemental)) {
                    int i5 = this._charBufferLength;
                    this._charBufferLength = i5 + 1;
                    ch[i5] = this._utf8_highSurrogate;
                    int i6 = this._charBufferLength;
                    this._charBufferLength = i6 + 1;
                    ch[i6] = this._utf8_lowSurrogate;
                    break;
                } else {
                    decodeUtf8StringIllegalState();
                    break;
                }
            default:
                decodeUtf8StringIllegalState();
                break;
        }
    }

    protected final void decodeUtf8NCNameIntoCharBuffer() throws IOException {
        this._charBufferLength = 0;
        if (this._charBuffer.length < this._octetBufferLength) {
            this._charBuffer = new char[this._octetBufferLength];
        }
        int end = this._octetBufferLength + this._octetBufferOffset;
        byte[] bArr = this._octetBuffer;
        int i2 = this._octetBufferOffset;
        this._octetBufferOffset = i2 + 1;
        int b1 = bArr[i2] & 255;
        if (DecoderStateTables.UTF8_NCNAME(b1) == 0) {
            char[] cArr = this._charBuffer;
            int i3 = this._charBufferLength;
            this._charBufferLength = i3 + 1;
            cArr[i3] = (char) b1;
        } else {
            decodeUtf8NCNameStartTwoToFourByteCharacters(b1, end);
        }
        while (end != this._octetBufferOffset) {
            byte[] bArr2 = this._octetBuffer;
            int i4 = this._octetBufferOffset;
            this._octetBufferOffset = i4 + 1;
            int b12 = bArr2[i4] & 255;
            if (DecoderStateTables.UTF8_NCNAME(b12) < 2) {
                char[] cArr2 = this._charBuffer;
                int i5 = this._charBufferLength;
                this._charBufferLength = i5 + 1;
                cArr2[i5] = (char) b12;
            } else {
                decodeUtf8NCNameTwoToFourByteCharacters(b12, end);
            }
        }
    }

    private void decodeUtf8NCNameStartTwoToFourByteCharacters(int b1, int end) throws IOException {
        switch (DecoderStateTables.UTF8_NCNAME(b1)) {
            case 1:
            default:
                decodeUtf8NCNameIllegalState();
                break;
            case 2:
                if (end == this._octetBufferOffset) {
                    decodeUtf8StringLengthTooSmall();
                }
                byte[] bArr = this._octetBuffer;
                int i2 = this._octetBufferOffset;
                this._octetBufferOffset = i2 + 1;
                int b2 = bArr[i2] & 255;
                if ((b2 & 192) != 128) {
                    decodeUtf8StringIllegalState();
                }
                char c2 = (char) (((b1 & 31) << 6) | (b2 & 63));
                if (XMLChar.isNCNameStart(c2)) {
                    char[] cArr = this._charBuffer;
                    int i3 = this._charBufferLength;
                    this._charBufferLength = i3 + 1;
                    cArr[i3] = c2;
                    break;
                } else {
                    decodeUtf8NCNameIllegalState();
                    break;
                }
            case 3:
                char c3 = decodeUtf8ThreeByteChar(end, b1);
                if (XMLChar.isNCNameStart(c3)) {
                    char[] cArr2 = this._charBuffer;
                    int i4 = this._charBufferLength;
                    this._charBufferLength = i4 + 1;
                    cArr2[i4] = c3;
                    break;
                } else {
                    decodeUtf8NCNameIllegalState();
                    break;
                }
            case 4:
                int supplemental = decodeUtf8FourByteChar(end, b1);
                if (XMLChar.isNCNameStart(supplemental)) {
                    char[] cArr3 = this._charBuffer;
                    int i5 = this._charBufferLength;
                    this._charBufferLength = i5 + 1;
                    cArr3[i5] = this._utf8_highSurrogate;
                    char[] cArr4 = this._charBuffer;
                    int i6 = this._charBufferLength;
                    this._charBufferLength = i6 + 1;
                    cArr4[i6] = this._utf8_lowSurrogate;
                    break;
                } else {
                    decodeUtf8NCNameIllegalState();
                    break;
                }
        }
    }

    private void decodeUtf8NCNameTwoToFourByteCharacters(int b1, int end) throws IOException {
        switch (DecoderStateTables.UTF8_NCNAME(b1)) {
            case 2:
                if (end == this._octetBufferOffset) {
                    decodeUtf8StringLengthTooSmall();
                }
                byte[] bArr = this._octetBuffer;
                int i2 = this._octetBufferOffset;
                this._octetBufferOffset = i2 + 1;
                int b2 = bArr[i2] & 255;
                if ((b2 & 192) != 128) {
                    decodeUtf8StringIllegalState();
                }
                char c2 = (char) (((b1 & 31) << 6) | (b2 & 63));
                if (XMLChar.isNCName(c2)) {
                    char[] cArr = this._charBuffer;
                    int i3 = this._charBufferLength;
                    this._charBufferLength = i3 + 1;
                    cArr[i3] = c2;
                    break;
                } else {
                    decodeUtf8NCNameIllegalState();
                    break;
                }
            case 3:
                char c3 = decodeUtf8ThreeByteChar(end, b1);
                if (XMLChar.isNCName(c3)) {
                    char[] cArr2 = this._charBuffer;
                    int i4 = this._charBufferLength;
                    this._charBufferLength = i4 + 1;
                    cArr2[i4] = c3;
                    break;
                } else {
                    decodeUtf8NCNameIllegalState();
                    break;
                }
            case 4:
                int supplemental = decodeUtf8FourByteChar(end, b1);
                if (XMLChar.isNCName(supplemental)) {
                    char[] cArr3 = this._charBuffer;
                    int i5 = this._charBufferLength;
                    this._charBufferLength = i5 + 1;
                    cArr3[i5] = this._utf8_highSurrogate;
                    char[] cArr4 = this._charBuffer;
                    int i6 = this._charBufferLength;
                    this._charBufferLength = i6 + 1;
                    cArr4[i6] = this._utf8_lowSurrogate;
                    break;
                } else {
                    decodeUtf8NCNameIllegalState();
                    break;
                }
            default:
                decodeUtf8NCNameIllegalState();
                break;
        }
    }

    private char decodeUtf8ThreeByteChar(int end, int b1) throws IOException {
        if (end == this._octetBufferOffset) {
            decodeUtf8StringLengthTooSmall();
        }
        byte[] bArr = this._octetBuffer;
        int i2 = this._octetBufferOffset;
        this._octetBufferOffset = i2 + 1;
        int b2 = bArr[i2] & 255;
        if ((b2 & 192) != 128 || ((b1 == 237 && b2 >= 160) || ((b1 & 15) == 0 && (b2 & 32) == 0))) {
            decodeUtf8StringIllegalState();
        }
        if (end == this._octetBufferOffset) {
            decodeUtf8StringLengthTooSmall();
        }
        byte[] bArr2 = this._octetBuffer;
        int i3 = this._octetBufferOffset;
        this._octetBufferOffset = i3 + 1;
        int b3 = bArr2[i3] & 255;
        if ((b3 & 192) != 128) {
            decodeUtf8StringIllegalState();
        }
        return (char) (((b1 & 15) << 12) | ((b2 & 63) << 6) | (b3 & 63));
    }

    private int decodeUtf8FourByteChar(int end, int b1) throws IOException {
        if (end == this._octetBufferOffset) {
            decodeUtf8StringLengthTooSmall();
        }
        byte[] bArr = this._octetBuffer;
        int i2 = this._octetBufferOffset;
        this._octetBufferOffset = i2 + 1;
        int b2 = bArr[i2] & 255;
        if ((b2 & 192) != 128 || ((b2 & 48) == 0 && (b1 & 7) == 0)) {
            decodeUtf8StringIllegalState();
        }
        if (end == this._octetBufferOffset) {
            decodeUtf8StringLengthTooSmall();
        }
        byte[] bArr2 = this._octetBuffer;
        int i3 = this._octetBufferOffset;
        this._octetBufferOffset = i3 + 1;
        int b3 = bArr2[i3] & 255;
        if ((b3 & 192) != 128) {
            decodeUtf8StringIllegalState();
        }
        if (end == this._octetBufferOffset) {
            decodeUtf8StringLengthTooSmall();
        }
        byte[] bArr3 = this._octetBuffer;
        int i4 = this._octetBufferOffset;
        this._octetBufferOffset = i4 + 1;
        int b4 = bArr3[i4] & 255;
        if ((b4 & 192) != 128) {
            decodeUtf8StringIllegalState();
        }
        int uuuuu = ((b1 << 2) & 28) | ((b2 >> 4) & 3);
        if (uuuuu > 16) {
            decodeUtf8StringIllegalState();
        }
        int wwww = uuuuu - 1;
        this._utf8_highSurrogate = (char) (55296 | ((wwww << 6) & 960) | ((b2 << 2) & 60) | ((b3 >> 4) & 3));
        this._utf8_lowSurrogate = (char) (56320 | ((b3 << 6) & 960) | (b4 & 63));
        return XMLChar.supplemental(this._utf8_highSurrogate, this._utf8_lowSurrogate);
    }

    private void decodeUtf8StringLengthTooSmall() throws IOException {
        throw new IOException(CommonResourceBundle.getInstance().getString("message.deliminatorTooSmall"));
    }

    private void decodeUtf8StringIllegalState() throws IOException {
        throw new IOException(CommonResourceBundle.getInstance().getString("message.UTF8Encoded"));
    }

    private void decodeUtf8NCNameIllegalState() throws IOException {
        throw new IOException(CommonResourceBundle.getInstance().getString("message.UTF8EncodedNCName"));
    }

    private void decodeUtf16StringIntoCharBuffer() throws IOException {
        this._charBufferLength = this._octetBufferLength / 2;
        if (this._charBuffer.length < this._charBufferLength) {
            this._charBuffer = new char[this._charBufferLength];
        }
        for (int i2 = 0; i2 < this._charBufferLength; i2++) {
            char c2 = (char) ((read() << 8) | read());
            this._charBuffer[i2] = c2;
        }
    }

    protected String createQualifiedNameString(String second) {
        return createQualifiedNameString(XMLNS_NAMESPACE_PREFIX_CHARS, second);
    }

    protected String createQualifiedNameString(char[] first, String second) {
        int l1 = first.length;
        int l2 = second.length();
        int total = l1 + l2 + 1;
        if (total < this._charBuffer.length) {
            System.arraycopy(first, 0, this._charBuffer, 0, l1);
            this._charBuffer[l1] = ':';
            second.getChars(0, l2, this._charBuffer, l1 + 1);
            return new String(this._charBuffer, 0, total);
        }
        return new String(first) + ':' + second;
    }

    protected final int read() throws IOException {
        if (this._octetBufferOffset < this._octetBufferEnd) {
            byte[] bArr = this._octetBuffer;
            int i2 = this._octetBufferOffset;
            this._octetBufferOffset = i2 + 1;
            return bArr[i2] & 255;
        }
        this._octetBufferEnd = this._s.read(this._octetBuffer);
        if (this._octetBufferEnd < 0) {
            throw new EOFException(CommonResourceBundle.getInstance().getString("message.EOF"));
        }
        this._octetBufferOffset = 1;
        return this._octetBuffer[0] & 255;
    }

    protected final void closeIfRequired() throws IOException {
        if (this._s != null && this._needForceStreamClose) {
            this._s.close();
        }
    }

    protected final int peek() throws IOException {
        return peek(null);
    }

    protected final int peek(OctetBufferListener octetBufferListener) throws IOException {
        if (this._octetBufferOffset < this._octetBufferEnd) {
            return this._octetBuffer[this._octetBufferOffset] & 255;
        }
        if (octetBufferListener != null) {
            octetBufferListener.onBeforeOctetBufferOverwrite();
        }
        this._octetBufferEnd = this._s.read(this._octetBuffer);
        if (this._octetBufferEnd < 0) {
            throw new EOFException(CommonResourceBundle.getInstance().getString("message.EOF"));
        }
        this._octetBufferOffset = 0;
        return this._octetBuffer[0] & 255;
    }

    protected final int peek2(OctetBufferListener octetBufferListener) throws IOException {
        if (this._octetBufferOffset + 1 < this._octetBufferEnd) {
            return this._octetBuffer[this._octetBufferOffset + 1] & 255;
        }
        if (octetBufferListener != null) {
            octetBufferListener.onBeforeOctetBufferOverwrite();
        }
        int offset = 0;
        if (this._octetBufferOffset < this._octetBufferEnd) {
            this._octetBuffer[0] = this._octetBuffer[this._octetBufferOffset];
            offset = 1;
        }
        this._octetBufferEnd = this._s.read(this._octetBuffer, offset, this._octetBuffer.length - offset);
        if (this._octetBufferEnd < 0) {
            throw new EOFException(CommonResourceBundle.getInstance().getString("message.EOF"));
        }
        this._octetBufferOffset = 0;
        return this._octetBuffer[1] & 255;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/Decoder$EncodingAlgorithmInputStream.class */
    protected class EncodingAlgorithmInputStream extends InputStream {
        protected EncodingAlgorithmInputStream() {
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (Decoder.this._octetBufferStart < Decoder.this._octetBufferOffset) {
                byte[] bArr = Decoder.this._octetBuffer;
                Decoder decoder = Decoder.this;
                int i2 = decoder._octetBufferStart;
                decoder._octetBufferStart = i2 + 1;
                return bArr[i2] & 255;
            }
            return -1;
        }

        @Override // java.io.InputStream
        public int read(byte[] b2) throws IOException {
            return read(b2, 0, b2.length);
        }

        @Override // java.io.InputStream
        public int read(byte[] b2, int off, int len) throws IOException {
            if (b2 == null) {
                throw new NullPointerException();
            }
            if (off < 0 || off > b2.length || len < 0 || off + len > b2.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (len == 0) {
                return 0;
            }
            int newOctetBufferStart = Decoder.this._octetBufferStart + len;
            if (newOctetBufferStart < Decoder.this._octetBufferOffset) {
                System.arraycopy(Decoder.this._octetBuffer, Decoder.this._octetBufferStart, b2, off, len);
                Decoder.this._octetBufferStart = newOctetBufferStart;
                return len;
            }
            if (Decoder.this._octetBufferStart < Decoder.this._octetBufferOffset) {
                int bytesToRead = Decoder.this._octetBufferOffset - Decoder.this._octetBufferStart;
                System.arraycopy(Decoder.this._octetBuffer, Decoder.this._octetBufferStart, b2, off, bytesToRead);
                Decoder.this._octetBufferStart += bytesToRead;
                return bytesToRead;
            }
            return -1;
        }
    }

    protected final boolean _isFastInfosetDocument() throws IOException {
        peek();
        this._octetBufferLength = EncodingConstants.BINARY_HEADER.length;
        ensureOctetBufferSize();
        this._octetBufferOffset += this._octetBufferLength;
        if (this._octetBuffer[0] != EncodingConstants.BINARY_HEADER[0] || this._octetBuffer[1] != EncodingConstants.BINARY_HEADER[1] || this._octetBuffer[2] != EncodingConstants.BINARY_HEADER[2] || this._octetBuffer[3] != EncodingConstants.BINARY_HEADER[3]) {
            for (int i2 = 0; i2 < EncodingConstants.XML_DECLARATION_VALUES.length; i2++) {
                this._octetBufferLength = EncodingConstants.XML_DECLARATION_VALUES[i2].length - this._octetBufferOffset;
                ensureOctetBufferSize();
                this._octetBufferOffset += this._octetBufferLength;
                if (arrayEquals(this._octetBuffer, 0, EncodingConstants.XML_DECLARATION_VALUES[i2], EncodingConstants.XML_DECLARATION_VALUES[i2].length)) {
                    this._octetBufferLength = EncodingConstants.BINARY_HEADER.length;
                    ensureOctetBufferSize();
                    byte[] bArr = this._octetBuffer;
                    int i3 = this._octetBufferOffset;
                    this._octetBufferOffset = i3 + 1;
                    if (bArr[i3] != EncodingConstants.BINARY_HEADER[0]) {
                        return false;
                    }
                    byte[] bArr2 = this._octetBuffer;
                    int i4 = this._octetBufferOffset;
                    this._octetBufferOffset = i4 + 1;
                    if (bArr2[i4] != EncodingConstants.BINARY_HEADER[1]) {
                        return false;
                    }
                    byte[] bArr3 = this._octetBuffer;
                    int i5 = this._octetBufferOffset;
                    this._octetBufferOffset = i5 + 1;
                    if (bArr3[i5] != EncodingConstants.BINARY_HEADER[2]) {
                        return false;
                    }
                    byte[] bArr4 = this._octetBuffer;
                    int i6 = this._octetBufferOffset;
                    this._octetBufferOffset = i6 + 1;
                    if (bArr4[i6] != EncodingConstants.BINARY_HEADER[3]) {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean arrayEquals(byte[] b1, int offset, byte[] b2, int length) {
        for (int i2 = 0; i2 < length; i2++) {
            if (b1[offset + i2] != b2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFastInfosetDocument(InputStream s2) throws IOException {
        byte[] header = new byte[4];
        int readBytesCount = s2.read(header);
        if (readBytesCount < 4 || header[0] != EncodingConstants.BINARY_HEADER[0] || header[1] != EncodingConstants.BINARY_HEADER[1] || header[2] != EncodingConstants.BINARY_HEADER[2] || header[3] != EncodingConstants.BINARY_HEADER[3]) {
            return false;
        }
        return true;
    }
}
