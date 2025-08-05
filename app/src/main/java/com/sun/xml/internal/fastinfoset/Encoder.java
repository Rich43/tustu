package com.sun.xml.internal.fastinfoset;

import com.sun.xml.internal.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
import com.sun.xml.internal.fastinfoset.org.apache.xerces.util.XMLChar;
import com.sun.xml.internal.fastinfoset.util.CharArrayIntMap;
import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.internal.fastinfoset.util.StringIntMap;
import com.sun.xml.internal.fastinfoset.vocab.SerializerVocabulary;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithm;
import com.sun.xml.internal.org.jvnet.fastinfoset.EncodingAlgorithmException;
import com.sun.xml.internal.org.jvnet.fastinfoset.ExternalVocabulary;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import com.sun.xml.internal.org.jvnet.fastinfoset.RestrictedAlphabet;
import com.sun.xml.internal.org.jvnet.fastinfoset.VocabularyApplicationData;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/Encoder.class */
public abstract class Encoder extends DefaultHandler implements FastInfosetSerializer {
    public static final String CHARACTER_ENCODING_SCHEME_SYSTEM_PROPERTY = "com.sun.xml.internal.fastinfoset.serializer.character-encoding-scheme";
    protected static final String _characterEncodingSchemeSystemDefault = getDefaultEncodingScheme();
    private static int[] NUMERIC_CHARACTERS_TABLE = new int[maxCharacter(RestrictedAlphabet.NUMERIC_CHARACTERS) + 1];
    private static int[] DATE_TIME_CHARACTERS_TABLE = new int[maxCharacter(RestrictedAlphabet.DATE_TIME_CHARACTERS) + 1];
    private boolean _ignoreDTD;
    private boolean _ignoreComments;
    private boolean _ignoreProcessingInstructions;
    private boolean _ignoreWhiteSpaceTextContent;
    private boolean _useLocalNameAsKeyForQualifiedNameLookup;
    private boolean _encodingStringsAsUtf8;
    private int _nonIdentifyingStringOnThirdBitCES;
    private int _nonIdentifyingStringOnFirstBitCES;
    private Map _registeredEncodingAlgorithms;
    protected SerializerVocabulary _v;
    protected VocabularyApplicationData _vData;
    private boolean _vIsInternal;
    protected boolean _terminate;
    protected int _b;
    protected OutputStream _s;
    protected char[] _charBuffer;
    protected byte[] _octetBuffer;
    protected int _octetBufferIndex;
    protected int _markIndex;
    protected int minAttributeValueSize;
    protected int maxAttributeValueSize;
    protected int attributeValueMapTotalCharactersConstraint;
    protected int minCharacterContentChunkSize;
    protected int maxCharacterContentChunkSize;
    protected int characterContentChunkMapTotalCharactersConstraint;
    private int _bitsLeftInOctet;
    private EncodingBufferOutputStream _encodingBufferOutputStream;
    private byte[] _encodingBuffer;
    private int _encodingBufferIndex;

    static /* synthetic */ int access$108(Encoder x0) {
        int i2 = x0._encodingBufferIndex;
        x0._encodingBufferIndex = i2 + 1;
        return i2;
    }

    static {
        for (int i2 = 0; i2 < NUMERIC_CHARACTERS_TABLE.length; i2++) {
            NUMERIC_CHARACTERS_TABLE[i2] = -1;
        }
        for (int i3 = 0; i3 < DATE_TIME_CHARACTERS_TABLE.length; i3++) {
            DATE_TIME_CHARACTERS_TABLE[i3] = -1;
        }
        for (int i4 = 0; i4 < RestrictedAlphabet.NUMERIC_CHARACTERS.length(); i4++) {
            NUMERIC_CHARACTERS_TABLE[RestrictedAlphabet.NUMERIC_CHARACTERS.charAt(i4)] = i4;
        }
        for (int i5 = 0; i5 < RestrictedAlphabet.DATE_TIME_CHARACTERS.length(); i5++) {
            DATE_TIME_CHARACTERS_TABLE[RestrictedAlphabet.DATE_TIME_CHARACTERS.charAt(i5)] = i5;
        }
    }

    private static String getDefaultEncodingScheme() {
        String p2 = System.getProperty(CHARACTER_ENCODING_SCHEME_SYSTEM_PROPERTY, "UTF-8");
        if (p2.equals(FastInfosetSerializer.UTF_16BE)) {
            return FastInfosetSerializer.UTF_16BE;
        }
        return "UTF-8";
    }

    private static int maxCharacter(String alphabet) {
        int c2 = 0;
        for (int i2 = 0; i2 < alphabet.length(); i2++) {
            if (c2 < alphabet.charAt(i2)) {
                c2 = alphabet.charAt(i2);
            }
        }
        return c2;
    }

    protected Encoder() {
        this._encodingStringsAsUtf8 = true;
        this._registeredEncodingAlgorithms = new HashMap();
        this._terminate = false;
        this._charBuffer = new char[512];
        this._octetBuffer = new byte[1024];
        this._markIndex = -1;
        this.minAttributeValueSize = 0;
        this.maxAttributeValueSize = 32;
        this.attributeValueMapTotalCharactersConstraint = 1073741823;
        this.minCharacterContentChunkSize = 0;
        this.maxCharacterContentChunkSize = 32;
        this.characterContentChunkMapTotalCharactersConstraint = 1073741823;
        this._encodingBufferOutputStream = new EncodingBufferOutputStream();
        this._encodingBuffer = new byte[512];
        setCharacterEncodingScheme(_characterEncodingSchemeSystemDefault);
    }

    protected Encoder(boolean useLocalNameAsKeyForQualifiedNameLookup) {
        this._encodingStringsAsUtf8 = true;
        this._registeredEncodingAlgorithms = new HashMap();
        this._terminate = false;
        this._charBuffer = new char[512];
        this._octetBuffer = new byte[1024];
        this._markIndex = -1;
        this.minAttributeValueSize = 0;
        this.maxAttributeValueSize = 32;
        this.attributeValueMapTotalCharactersConstraint = 1073741823;
        this.minCharacterContentChunkSize = 0;
        this.maxCharacterContentChunkSize = 32;
        this.characterContentChunkMapTotalCharactersConstraint = 1073741823;
        this._encodingBufferOutputStream = new EncodingBufferOutputStream();
        this._encodingBuffer = new byte[512];
        setCharacterEncodingScheme(_characterEncodingSchemeSystemDefault);
        this._useLocalNameAsKeyForQualifiedNameLookup = useLocalNameAsKeyForQualifiedNameLookup;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public final void setIgnoreDTD(boolean ignoreDTD) {
        this._ignoreDTD = ignoreDTD;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public final boolean getIgnoreDTD() {
        return this._ignoreDTD;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public final void setIgnoreComments(boolean ignoreComments) {
        this._ignoreComments = ignoreComments;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public final boolean getIgnoreComments() {
        return this._ignoreComments;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public final void setIgnoreProcesingInstructions(boolean ignoreProcesingInstructions) {
        this._ignoreProcessingInstructions = ignoreProcesingInstructions;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public final boolean getIgnoreProcesingInstructions() {
        return this._ignoreProcessingInstructions;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public final void setIgnoreWhiteSpaceTextContent(boolean ignoreWhiteSpaceTextContent) {
        this._ignoreWhiteSpaceTextContent = ignoreWhiteSpaceTextContent;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public final boolean getIgnoreWhiteSpaceTextContent() {
        return this._ignoreWhiteSpaceTextContent;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setCharacterEncodingScheme(String characterEncodingScheme) {
        if (characterEncodingScheme.equals(FastInfosetSerializer.UTF_16BE)) {
            this._encodingStringsAsUtf8 = false;
            this._nonIdentifyingStringOnThirdBitCES = 132;
            this._nonIdentifyingStringOnFirstBitCES = 16;
        } else {
            this._encodingStringsAsUtf8 = true;
            this._nonIdentifyingStringOnThirdBitCES = 128;
            this._nonIdentifyingStringOnFirstBitCES = 0;
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public String getCharacterEncodingScheme() {
        return this._encodingStringsAsUtf8 ? "UTF-8" : FastInfosetSerializer.UTF_16BE;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setRegisteredEncodingAlgorithms(Map algorithms) {
        this._registeredEncodingAlgorithms = algorithms;
        if (this._registeredEncodingAlgorithms == null) {
            this._registeredEncodingAlgorithms = new HashMap();
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public Map getRegisteredEncodingAlgorithms() {
        return this._registeredEncodingAlgorithms;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public int getMinCharacterContentChunkSize() {
        return this.minCharacterContentChunkSize;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setMinCharacterContentChunkSize(int size) {
        if (size < 0) {
            size = 0;
        }
        this.minCharacterContentChunkSize = size;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public int getMaxCharacterContentChunkSize() {
        return this.maxCharacterContentChunkSize;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setMaxCharacterContentChunkSize(int size) {
        if (size < 0) {
            size = 0;
        }
        this.maxCharacterContentChunkSize = size;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public int getCharacterContentChunkMapMemoryLimit() {
        return this.characterContentChunkMapTotalCharactersConstraint * 2;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setCharacterContentChunkMapMemoryLimit(int size) {
        if (size < 0) {
            size = 0;
        }
        this.characterContentChunkMapTotalCharactersConstraint = size / 2;
    }

    public boolean isCharacterContentChunkLengthMatchesLimit(int length) {
        return length >= this.minCharacterContentChunkSize && length < this.maxCharacterContentChunkSize;
    }

    public boolean canAddCharacterContentToTable(int length, CharArrayIntMap map) {
        return map.getTotalCharacterCount() + length < this.characterContentChunkMapTotalCharactersConstraint;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public int getMinAttributeValueSize() {
        return this.minAttributeValueSize;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setMinAttributeValueSize(int size) {
        if (size < 0) {
            size = 0;
        }
        this.minAttributeValueSize = size;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public int getMaxAttributeValueSize() {
        return this.maxAttributeValueSize;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setMaxAttributeValueSize(int size) {
        if (size < 0) {
            size = 0;
        }
        this.maxAttributeValueSize = size;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setAttributeValueMapMemoryLimit(int size) {
        if (size < 0) {
            size = 0;
        }
        this.attributeValueMapTotalCharactersConstraint = size / 2;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public int getAttributeValueMapMemoryLimit() {
        return this.attributeValueMapTotalCharactersConstraint * 2;
    }

    public boolean isAttributeValueLengthMatchesLimit(int length) {
        return length >= this.minAttributeValueSize && length < this.maxAttributeValueSize;
    }

    public boolean canAddAttributeToTable(int length) {
        return this._v.attributeValue.getTotalCharacterCount() + length < this.attributeValueMapTotalCharactersConstraint;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setExternalVocabulary(ExternalVocabulary v2) {
        this._v = new SerializerVocabulary();
        SerializerVocabulary ev = new SerializerVocabulary(v2.vocabulary, this._useLocalNameAsKeyForQualifiedNameLookup);
        this._v.setExternalVocabulary(v2.URI, ev, false);
        this._vIsInternal = true;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setVocabularyApplicationData(VocabularyApplicationData data) {
        this._vData = data;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public VocabularyApplicationData getVocabularyApplicationData() {
        return this._vData;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void reset() {
        this._terminate = false;
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void setOutputStream(OutputStream s2) {
        this._octetBufferIndex = 0;
        this._markIndex = -1;
        this._s = s2;
    }

    public void setVocabulary(SerializerVocabulary vocabulary) {
        this._v = vocabulary;
        this._vIsInternal = false;
    }

    protected final void encodeHeader(boolean encodeXmlDecl) throws IOException {
        if (encodeXmlDecl) {
            this._s.write(EncodingConstants.XML_DECLARATION_VALUES[0]);
        }
        this._s.write(EncodingConstants.BINARY_HEADER);
    }

    protected final void encodeInitialVocabulary() throws IOException {
        if (this._v == null) {
            this._v = new SerializerVocabulary();
            this._vIsInternal = true;
        } else if (this._vIsInternal) {
            this._v.clear();
            if (this._vData != null) {
                this._vData.clear();
            }
        }
        if (!this._v.hasInitialVocabulary() && !this._v.hasExternalVocabulary()) {
            write(0);
            return;
        }
        if (!this._v.hasInitialVocabulary()) {
            if (this._v.hasExternalVocabulary()) {
                this._b = 32;
                write(this._b);
                this._b = 16;
                write(this._b);
                write(0);
                encodeNonEmptyOctetStringOnSecondBit(this._v.getExternalVocabularyURI());
                return;
            }
            return;
        }
        this._b = 32;
        write(this._b);
        SerializerVocabulary initialVocabulary = this._v.getReadOnlyVocabulary();
        if (initialVocabulary.hasExternalVocabulary()) {
            this._b = 16;
            write(this._b);
            write(0);
        }
        if (initialVocabulary.hasExternalVocabulary()) {
            encodeNonEmptyOctetStringOnSecondBit(this._v.getExternalVocabularyURI());
        }
    }

    protected final void encodeDocumentTermination() throws IOException {
        encodeElementTermination();
        encodeTermination();
        _flush();
        this._s.flush();
    }

    protected final void encodeElementTermination() throws IOException {
        this._terminate = true;
        switch (this._b) {
            case 240:
                this._b = 255;
                return;
            case 255:
                write(255);
                break;
        }
        this._b = 240;
    }

    protected final void encodeTermination() throws IOException {
        if (this._terminate) {
            write(this._b);
            this._b = 0;
            this._terminate = false;
        }
    }

    protected final void encodeNamespaceAttribute(String prefix, String uri) throws IOException {
        this._b = 204;
        if (prefix.length() > 0) {
            this._b |= 2;
        }
        if (uri.length() > 0) {
            this._b |= 1;
        }
        write(this._b);
        if (prefix.length() > 0) {
            encodeIdentifyingNonEmptyStringOnFirstBit(prefix, this._v.prefix);
        }
        if (uri.length() > 0) {
            encodeIdentifyingNonEmptyStringOnFirstBit(uri, this._v.namespaceName);
        }
    }

    protected final void encodeCharacters(char[] ch, int offset, int length) throws IOException {
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
        encodeNonIdentifyingStringOnThirdBit(ch, offset, length, this._v.characterContentChunk, addToTable, true);
    }

    protected final void encodeCharactersNoClone(char[] ch, int offset, int length) throws IOException {
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
        encodeNonIdentifyingStringOnThirdBit(ch, offset, length, this._v.characterContentChunk, addToTable, false);
    }

    protected final void encodeNumericFourBitCharacters(char[] ch, int offset, int length, boolean addToTable) throws FastInfosetException, IOException {
        encodeFourBitCharacters(0, NUMERIC_CHARACTERS_TABLE, ch, offset, length, addToTable);
    }

    protected final void encodeDateTimeFourBitCharacters(char[] ch, int offset, int length, boolean addToTable) throws FastInfosetException, IOException {
        encodeFourBitCharacters(1, DATE_TIME_CHARACTERS_TABLE, ch, offset, length, addToTable);
    }

    protected final void encodeFourBitCharacters(int id, int[] table, char[] ch, int offset, int length, boolean addToTable) throws FastInfosetException, IOException {
        int iObtainIndex;
        if (addToTable) {
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, this._v.characterContentChunk);
            if (canAddCharacterContentToTable) {
                iObtainIndex = this._v.characterContentChunk.obtainIndex(ch, offset, length, true);
            } else {
                iObtainIndex = this._v.characterContentChunk.get(ch, offset, length);
            }
            int index = iObtainIndex;
            if (index != -1) {
                this._b = 160;
                encodeNonZeroIntegerOnFourthBit(index);
                return;
            } else if (canAddCharacterContentToTable) {
                this._b = 152;
            } else {
                this._b = 136;
            }
        } else {
            this._b = 136;
        }
        write(this._b);
        this._b = id << 2;
        encodeNonEmptyFourBitCharacterStringOnSeventhBit(table, ch, offset, length);
    }

    protected final void encodeAlphabetCharacters(String alphabet, char[] ch, int offset, int length, boolean addToTable) throws FastInfosetException, IOException {
        int iObtainIndex;
        if (addToTable) {
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, this._v.characterContentChunk);
            if (canAddCharacterContentToTable) {
                iObtainIndex = this._v.characterContentChunk.obtainIndex(ch, offset, length, true);
            } else {
                iObtainIndex = this._v.characterContentChunk.get(ch, offset, length);
            }
            int index = iObtainIndex;
            if (index != -1) {
                this._b = 160;
                encodeNonZeroIntegerOnFourthBit(index);
                return;
            } else if (canAddCharacterContentToTable) {
                this._b = 152;
            } else {
                this._b = 136;
            }
        } else {
            this._b = 136;
        }
        int id = this._v.restrictedAlphabet.get(alphabet);
        if (id == -1) {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.restrictedAlphabetNotPresent"));
        }
        int id2 = id + 32;
        this._b |= (id2 & 192) >> 6;
        write(this._b);
        this._b = (id2 & 63) << 2;
        encodeNonEmptyNBitCharacterStringOnSeventhBit(alphabet, ch, offset, length);
    }

    protected final void encodeProcessingInstruction(String target, String data) throws IOException {
        write(225);
        encodeIdentifyingNonEmptyStringOnFirstBit(target, this._v.otherNCName);
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(data.length());
        encodeNonIdentifyingStringOnFirstBit(data, this._v.otherString, addToTable);
    }

    protected final void encodeDocumentTypeDeclaration(String systemId, String publicId) throws IOException {
        this._b = 196;
        if (systemId != null && systemId.length() > 0) {
            this._b |= 2;
        }
        if (publicId != null && publicId.length() > 0) {
            this._b |= 1;
        }
        write(this._b);
        if (systemId != null && systemId.length() > 0) {
            encodeIdentifyingNonEmptyStringOnFirstBit(systemId, this._v.otherURI);
        }
        if (publicId != null && publicId.length() > 0) {
            encodeIdentifyingNonEmptyStringOnFirstBit(publicId, this._v.otherURI);
        }
    }

    protected final void encodeComment(char[] ch, int offset, int length) throws IOException {
        write(226);
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
        encodeNonIdentifyingStringOnFirstBit(ch, offset, length, this._v.otherString, addToTable, true);
    }

    protected final void encodeCommentNoClone(char[] ch, int offset, int length) throws IOException {
        write(226);
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
        encodeNonIdentifyingStringOnFirstBit(ch, offset, length, this._v.otherString, addToTable, false);
    }

    protected final void encodeElementQualifiedNameOnThirdBit(String namespaceURI, String prefix, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(localName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i2 = 0; i2 < entry._valueIndex; i2++) {
                if ((prefix == names[i2].prefix || prefix.equals(names[i2].prefix)) && (namespaceURI == names[i2].namespaceName || namespaceURI.equals(names[i2].namespaceName))) {
                    encodeNonZeroIntegerOnThirdBit(names[i2].index);
                    return;
                }
            }
        }
        encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, prefix, localName, entry);
    }

    protected final void encodeLiteralElementQualifiedNameOnThirdBit(String namespaceURI, String prefix, String localName, LocalNameQualifiedNamesMap.Entry entry) throws IOException {
        QualifiedName name = new QualifiedName(prefix, namespaceURI, localName, "", this._v.elementName.getNextIndex());
        entry.addQualifiedName(name);
        int namespaceURIIndex = -1;
        int prefixIndex = -1;
        if (namespaceURI.length() > 0) {
            namespaceURIIndex = this._v.namespaceName.get(namespaceURI);
            if (namespaceURIIndex == -1) {
                throw new IOException(CommonResourceBundle.getInstance().getString("message.namespaceURINotIndexed", new Object[]{namespaceURI}));
            }
            if (prefix.length() > 0) {
                prefixIndex = this._v.prefix.get(prefix);
                if (prefixIndex == -1) {
                    throw new IOException(CommonResourceBundle.getInstance().getString("message.prefixNotIndexed", new Object[]{prefix}));
                }
            }
        }
        int localNameIndex = this._v.localName.obtainIndex(localName);
        this._b |= 60;
        if (namespaceURIIndex >= 0) {
            this._b |= 1;
            if (prefixIndex >= 0) {
                this._b |= 2;
            }
        }
        write(this._b);
        if (namespaceURIIndex >= 0) {
            if (prefixIndex >= 0) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(prefixIndex);
            }
            encodeNonZeroIntegerOnSecondBitFirstBitOne(namespaceURIIndex);
        }
        if (localNameIndex >= 0) {
            encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
        } else {
            encodeNonEmptyOctetStringOnSecondBit(localName);
        }
    }

    protected final void encodeAttributeQualifiedNameOnSecondBit(String namespaceURI, String prefix, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(localName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i2 = 0; i2 < entry._valueIndex; i2++) {
                if ((prefix == names[i2].prefix || prefix.equals(names[i2].prefix)) && (namespaceURI == names[i2].namespaceName || namespaceURI.equals(names[i2].namespaceName))) {
                    encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i2].index);
                    return;
                }
            }
        }
        encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, prefix, localName, entry);
    }

    protected final boolean encodeLiteralAttributeQualifiedNameOnSecondBit(String namespaceURI, String prefix, String localName, LocalNameQualifiedNamesMap.Entry entry) throws IOException {
        int namespaceURIIndex = -1;
        int prefixIndex = -1;
        if (namespaceURI.length() > 0) {
            namespaceURIIndex = this._v.namespaceName.get(namespaceURI);
            if (namespaceURIIndex == -1) {
                if (namespaceURI == "http://www.w3.org/2000/xmlns/" || namespaceURI.equals("http://www.w3.org/2000/xmlns/")) {
                    return false;
                }
                throw new IOException(CommonResourceBundle.getInstance().getString("message.namespaceURINotIndexed", new Object[]{namespaceURI}));
            }
            if (prefix.length() > 0) {
                prefixIndex = this._v.prefix.get(prefix);
                if (prefixIndex == -1) {
                    throw new IOException(CommonResourceBundle.getInstance().getString("message.prefixNotIndexed", new Object[]{prefix}));
                }
            }
        }
        int localNameIndex = this._v.localName.obtainIndex(localName);
        QualifiedName name = new QualifiedName(prefix, namespaceURI, localName, "", this._v.attributeName.getNextIndex());
        entry.addQualifiedName(name);
        this._b = 120;
        if (namespaceURI.length() > 0) {
            this._b |= 1;
            if (prefix.length() > 0) {
                this._b |= 2;
            }
        }
        write(this._b);
        if (namespaceURIIndex >= 0) {
            if (prefixIndex >= 0) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(prefixIndex);
            }
            encodeNonZeroIntegerOnSecondBitFirstBitOne(namespaceURIIndex);
        } else if (namespaceURI != "") {
            encodeNonEmptyOctetStringOnSecondBit("xml");
            encodeNonEmptyOctetStringOnSecondBit("http://www.w3.org/XML/1998/namespace");
        }
        if (localNameIndex >= 0) {
            encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
            return true;
        }
        encodeNonEmptyOctetStringOnSecondBit(localName);
        return true;
    }

    protected final void encodeNonIdentifyingStringOnFirstBit(String s2, StringIntMap map, boolean addToTable, boolean mustBeAddedToTable) throws IOException {
        int iObtainIndex;
        if (s2 == null || s2.length() == 0) {
            write(255);
            return;
        }
        if (addToTable || mustBeAddedToTable) {
            boolean canAddAttributeToTable = mustBeAddedToTable || canAddAttributeToTable(s2.length());
            if (canAddAttributeToTable) {
                iObtainIndex = map.obtainIndex(s2);
            } else {
                iObtainIndex = map.get(s2);
            }
            int index = iObtainIndex;
            if (index != -1) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
                return;
            } else if (canAddAttributeToTable) {
                this._b = 64 | this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(s2);
                return;
            } else {
                this._b = this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(s2);
                return;
            }
        }
        this._b = this._nonIdentifyingStringOnFirstBitCES;
        encodeNonEmptyCharacterStringOnFifthBit(s2);
    }

    protected final void encodeNonIdentifyingStringOnFirstBit(String s2, CharArrayIntMap map, boolean addToTable) throws IOException {
        int iObtainIndex;
        if (s2 == null || s2.length() == 0) {
            write(255);
            return;
        }
        if (addToTable) {
            char[] ch = s2.toCharArray();
            int length = s2.length();
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, map);
            if (canAddCharacterContentToTable) {
                iObtainIndex = map.obtainIndex(ch, 0, length, false);
            } else {
                iObtainIndex = map.get(ch, 0, length);
            }
            int index = iObtainIndex;
            if (index != -1) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
                return;
            } else if (canAddCharacterContentToTable) {
                this._b = 64 | this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(ch, 0, length);
                return;
            } else {
                this._b = this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(s2);
                return;
            }
        }
        this._b = this._nonIdentifyingStringOnFirstBitCES;
        encodeNonEmptyCharacterStringOnFifthBit(s2);
    }

    protected final void encodeNonIdentifyingStringOnFirstBit(char[] ch, int offset, int length, CharArrayIntMap map, boolean addToTable, boolean clone) throws IOException {
        int iObtainIndex;
        if (length == 0) {
            write(255);
            return;
        }
        if (addToTable) {
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, map);
            if (canAddCharacterContentToTable) {
                iObtainIndex = map.obtainIndex(ch, offset, length, clone);
            } else {
                iObtainIndex = map.get(ch, offset, length);
            }
            int index = iObtainIndex;
            if (index != -1) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
                return;
            } else if (canAddCharacterContentToTable) {
                this._b = 64 | this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(ch, offset, length);
                return;
            } else {
                this._b = this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(ch, offset, length);
                return;
            }
        }
        this._b = this._nonIdentifyingStringOnFirstBitCES;
        encodeNonEmptyCharacterStringOnFifthBit(ch, offset, length);
    }

    protected final void encodeNumericNonIdentifyingStringOnFirstBit(String s2, boolean addToTable, boolean mustBeAddedToTable) throws FastInfosetException, IOException {
        encodeNonIdentifyingStringOnFirstBit(0, NUMERIC_CHARACTERS_TABLE, s2, addToTable, mustBeAddedToTable);
    }

    protected final void encodeDateTimeNonIdentifyingStringOnFirstBit(String s2, boolean addToTable, boolean mustBeAddedToTable) throws FastInfosetException, IOException {
        encodeNonIdentifyingStringOnFirstBit(1, DATE_TIME_CHARACTERS_TABLE, s2, addToTable, mustBeAddedToTable);
    }

    protected final void encodeNonIdentifyingStringOnFirstBit(int id, int[] table, String s2, boolean addToTable, boolean mustBeAddedToTable) throws FastInfosetException, IOException {
        int iObtainIndex;
        if (s2 == null || s2.length() == 0) {
            write(255);
            return;
        }
        if (addToTable || mustBeAddedToTable) {
            boolean canAddAttributeToTable = mustBeAddedToTable || canAddAttributeToTable(s2.length());
            if (canAddAttributeToTable) {
                iObtainIndex = this._v.attributeValue.obtainIndex(s2);
            } else {
                iObtainIndex = this._v.attributeValue.get(s2);
            }
            int index = iObtainIndex;
            if (index != -1) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
                return;
            } else if (canAddAttributeToTable) {
                this._b = 96;
            } else {
                this._b = 32;
            }
        } else {
            this._b = 32;
        }
        write(this._b | ((id & 240) >> 4));
        this._b = (id & 15) << 4;
        int length = s2.length();
        int octetPairLength = length / 2;
        int octetSingleLength = length % 2;
        encodeNonZeroOctetStringLengthOnFifthBit(octetPairLength + octetSingleLength);
        encodeNonEmptyFourBitCharacterString(table, s2.toCharArray(), 0, octetPairLength, octetSingleLength);
    }

    protected final void encodeNonIdentifyingStringOnFirstBit(String URI, int id, Object data) throws FastInfosetException, IOException {
        int length;
        if (URI != null) {
            int id2 = this._v.encodingAlgorithm.get(URI);
            if (id2 == -1) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.EncodingAlgorithmURI", new Object[]{URI}));
            }
            int id3 = id2 + 32;
            EncodingAlgorithm ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
            if (ea != null) {
                encodeAIIObjectAlgorithmData(id3, data, ea);
                return;
            } else {
                if (data instanceof byte[]) {
                    byte[] d2 = (byte[]) data;
                    encodeAIIOctetAlgorithmData(id3, d2, 0, d2.length);
                    return;
                }
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
            }
        }
        if (id > 9) {
            if (id >= 32) {
                if (data instanceof byte[]) {
                    byte[] d3 = (byte[]) data;
                    encodeAIIOctetAlgorithmData(id, d3, 0, d3.length);
                    return;
                }
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
            }
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
        }
        switch (id) {
            case 0:
            case 1:
                length = ((byte[]) data).length;
                break;
            case 2:
                length = ((short[]) data).length;
                break;
            case 3:
                length = ((int[]) data).length;
                break;
            case 4:
            case 8:
                length = ((long[]) data).length;
                break;
            case 5:
                length = ((boolean[]) data).length;
                break;
            case 6:
                length = ((float[]) data).length;
                break;
            case 7:
                length = ((double[]) data).length;
                break;
            case 9:
                throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.CDATA"));
            default:
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.UnsupportedBuiltInAlgorithm", new Object[]{Integer.valueOf(id)}));
        }
        encodeAIIBuiltInAlgorithmData(id, data, 0, length);
    }

    protected final void encodeAIIOctetAlgorithmData(int id, byte[] d2, int offset, int length) throws IOException {
        write(48 | ((id & 240) >> 4));
        this._b = (id & 15) << 4;
        encodeNonZeroOctetStringLengthOnFifthBit(length);
        write(d2, offset, length);
    }

    protected final void encodeAIIObjectAlgorithmData(int id, Object data, EncodingAlgorithm ea) throws FastInfosetException, IOException {
        write(48 | ((id & 240) >> 4));
        this._b = (id & 15) << 4;
        this._encodingBufferOutputStream.reset();
        ea.encodeToOutputStream(data, this._encodingBufferOutputStream);
        encodeNonZeroOctetStringLengthOnFifthBit(this._encodingBufferIndex);
        write(this._encodingBuffer, this._encodingBufferIndex);
    }

    protected final void encodeAIIBuiltInAlgorithmData(int id, Object data, int offset, int length) throws IOException {
        write(48 | ((id & 240) >> 4));
        this._b = (id & 15) << 4;
        int octetLength = BuiltInEncodingAlgorithmFactory.getAlgorithm(id).getOctetLengthFromPrimitiveLength(length);
        encodeNonZeroOctetStringLengthOnFifthBit(octetLength);
        ensureSize(octetLength);
        BuiltInEncodingAlgorithmFactory.getAlgorithm(id).encodeToBytes(data, offset, length, this._octetBuffer, this._octetBufferIndex);
        this._octetBufferIndex += octetLength;
    }

    protected final void encodeNonIdentifyingStringOnThirdBit(char[] ch, int offset, int length, CharArrayIntMap map, boolean addToTable, boolean clone) throws IOException {
        int iObtainIndex;
        if (addToTable) {
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, map);
            if (canAddCharacterContentToTable) {
                iObtainIndex = map.obtainIndex(ch, offset, length, clone);
            } else {
                iObtainIndex = map.get(ch, offset, length);
            }
            int index = iObtainIndex;
            if (index != -1) {
                this._b = 160;
                encodeNonZeroIntegerOnFourthBit(index);
                return;
            } else if (canAddCharacterContentToTable) {
                this._b = 16 | this._nonIdentifyingStringOnThirdBitCES;
                encodeNonEmptyCharacterStringOnSeventhBit(ch, offset, length);
                return;
            } else {
                this._b = this._nonIdentifyingStringOnThirdBitCES;
                encodeNonEmptyCharacterStringOnSeventhBit(ch, offset, length);
                return;
            }
        }
        this._b = this._nonIdentifyingStringOnThirdBitCES;
        encodeNonEmptyCharacterStringOnSeventhBit(ch, offset, length);
    }

    protected final void encodeNonIdentifyingStringOnThirdBit(String URI, int id, Object data) throws FastInfosetException, IOException {
        int length;
        if (URI != null) {
            int id2 = this._v.encodingAlgorithm.get(URI);
            if (id2 == -1) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.EncodingAlgorithmURI", new Object[]{URI}));
            }
            int id3 = id2 + 32;
            EncodingAlgorithm ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
            if (ea != null) {
                encodeCIIObjectAlgorithmData(id3, data, ea);
                return;
            } else {
                if (data instanceof byte[]) {
                    byte[] d2 = (byte[]) data;
                    encodeCIIOctetAlgorithmData(id3, d2, 0, d2.length);
                    return;
                }
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
            }
        }
        if (id > 9) {
            if (id >= 32) {
                if (data instanceof byte[]) {
                    byte[] d3 = (byte[]) data;
                    encodeCIIOctetAlgorithmData(id, d3, 0, d3.length);
                    return;
                }
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
            }
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
        }
        switch (id) {
            case 0:
            case 1:
                length = ((byte[]) data).length;
                break;
            case 2:
                length = ((short[]) data).length;
                break;
            case 3:
                length = ((int[]) data).length;
                break;
            case 4:
            case 8:
                length = ((long[]) data).length;
                break;
            case 5:
                length = ((boolean[]) data).length;
                break;
            case 6:
                length = ((float[]) data).length;
                break;
            case 7:
                length = ((double[]) data).length;
                break;
            case 9:
                throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.CDATA"));
            default:
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.UnsupportedBuiltInAlgorithm", new Object[]{Integer.valueOf(id)}));
        }
        encodeCIIBuiltInAlgorithmData(id, data, 0, length);
    }

    protected final void encodeNonIdentifyingStringOnThirdBit(String URI, int id, byte[] d2, int offset, int length) throws FastInfosetException, IOException {
        if (URI != null) {
            int id2 = this._v.encodingAlgorithm.get(URI);
            if (id2 == -1) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.EncodingAlgorithmURI", new Object[]{URI}));
            }
            id = id2 + 32;
        }
        encodeCIIOctetAlgorithmData(id, d2, offset, length);
    }

    protected final void encodeCIIOctetAlgorithmData(int id, byte[] d2, int offset, int length) throws IOException {
        write(140 | ((id & 192) >> 6));
        this._b = (id & 63) << 2;
        encodeNonZeroOctetStringLengthOnSenventhBit(length);
        write(d2, offset, length);
    }

    protected final void encodeCIIObjectAlgorithmData(int id, Object data, EncodingAlgorithm ea) throws FastInfosetException, IOException {
        write(140 | ((id & 192) >> 6));
        this._b = (id & 63) << 2;
        this._encodingBufferOutputStream.reset();
        ea.encodeToOutputStream(data, this._encodingBufferOutputStream);
        encodeNonZeroOctetStringLengthOnSenventhBit(this._encodingBufferIndex);
        write(this._encodingBuffer, this._encodingBufferIndex);
    }

    protected final void encodeCIIBuiltInAlgorithmData(int id, Object data, int offset, int length) throws FastInfosetException, IOException {
        write(140 | ((id & 192) >> 6));
        this._b = (id & 63) << 2;
        int octetLength = BuiltInEncodingAlgorithmFactory.getAlgorithm(id).getOctetLengthFromPrimitiveLength(length);
        encodeNonZeroOctetStringLengthOnSenventhBit(octetLength);
        ensureSize(octetLength);
        BuiltInEncodingAlgorithmFactory.getAlgorithm(id).encodeToBytes(data, offset, length, this._octetBuffer, this._octetBufferIndex);
        this._octetBufferIndex += octetLength;
    }

    protected final void encodeCIIBuiltInAlgorithmDataAsCDATA(char[] ch, int offset, int length) throws FastInfosetException, IOException {
        write(140);
        this._b = 36;
        int length2 = encodeUTF8String(ch, offset, length);
        encodeNonZeroOctetStringLengthOnSenventhBit(length2);
        write(this._encodingBuffer, length2);
    }

    protected final void encodeIdentifyingNonEmptyStringOnFirstBit(String s2, StringIntMap map) throws IOException {
        int index = map.obtainIndex(s2);
        if (index == -1) {
            encodeNonEmptyOctetStringOnSecondBit(s2);
        } else {
            encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
        }
    }

    protected final void encodeNonEmptyOctetStringOnSecondBit(String s2) throws IOException {
        int length = encodeUTF8String(s2);
        encodeNonZeroOctetStringLengthOnSecondBit(length);
        write(this._encodingBuffer, length);
    }

    protected final void encodeNonZeroOctetStringLengthOnSecondBit(int length) throws IOException {
        if (length < 65) {
            write(length - 1);
            return;
        }
        if (length < 321) {
            write(64);
            write(length - 65);
            return;
        }
        write(96);
        int length2 = length - 321;
        write(length2 >>> 24);
        write((length2 >> 16) & 255);
        write((length2 >> 8) & 255);
        write(length2 & 255);
    }

    protected final void encodeNonEmptyCharacterStringOnFifthBit(String s2) throws IOException {
        int length = this._encodingStringsAsUtf8 ? encodeUTF8String(s2) : encodeUtf16String(s2);
        encodeNonZeroOctetStringLengthOnFifthBit(length);
        write(this._encodingBuffer, length);
    }

    protected final void encodeNonEmptyCharacterStringOnFifthBit(char[] ch, int offset, int length) throws IOException {
        int length2 = this._encodingStringsAsUtf8 ? encodeUTF8String(ch, offset, length) : encodeUtf16String(ch, offset, length);
        encodeNonZeroOctetStringLengthOnFifthBit(length2);
        write(this._encodingBuffer, length2);
    }

    protected final void encodeNonZeroOctetStringLengthOnFifthBit(int length) throws IOException {
        if (length < 9) {
            write(this._b | (length - 1));
            return;
        }
        if (length < 265) {
            write(this._b | 8);
            write(length - 9);
            return;
        }
        write(this._b | 12);
        int length2 = length - 265;
        write(length2 >>> 24);
        write((length2 >> 16) & 255);
        write((length2 >> 8) & 255);
        write(length2 & 255);
    }

    protected final void encodeNonEmptyCharacterStringOnSeventhBit(char[] ch, int offset, int length) throws IOException {
        int length2 = this._encodingStringsAsUtf8 ? encodeUTF8String(ch, offset, length) : encodeUtf16String(ch, offset, length);
        encodeNonZeroOctetStringLengthOnSenventhBit(length2);
        write(this._encodingBuffer, length2);
    }

    protected final void encodeNonEmptyFourBitCharacterStringOnSeventhBit(int[] table, char[] ch, int offset, int length) throws FastInfosetException, IOException {
        int octetPairLength = length / 2;
        int octetSingleLength = length % 2;
        encodeNonZeroOctetStringLengthOnSenventhBit(octetPairLength + octetSingleLength);
        encodeNonEmptyFourBitCharacterString(table, ch, offset, octetPairLength, octetSingleLength);
    }

    protected final void encodeNonEmptyFourBitCharacterString(int[] table, char[] ch, int offset, int octetPairLength, int octetSingleLength) throws FastInfosetException, IOException {
        ensureSize(octetPairLength + octetSingleLength);
        for (int i2 = 0; i2 < octetPairLength; i2++) {
            int i3 = offset;
            int offset2 = offset + 1;
            offset = offset2 + 1;
            int v2 = (table[ch[i3]] << 4) | table[ch[offset2]];
            if (v2 < 0) {
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.characterOutofAlphabetRange"));
            }
            byte[] bArr = this._octetBuffer;
            int i4 = this._octetBufferIndex;
            this._octetBufferIndex = i4 + 1;
            bArr[i4] = (byte) v2;
        }
        if (octetSingleLength == 1) {
            int v3 = (table[ch[offset]] << 4) | 15;
            if (v3 < 0) {
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.characterOutofAlphabetRange"));
            }
            byte[] bArr2 = this._octetBuffer;
            int i5 = this._octetBufferIndex;
            this._octetBufferIndex = i5 + 1;
            bArr2[i5] = (byte) v3;
        }
    }

    protected final void encodeNonEmptyNBitCharacterStringOnSeventhBit(String alphabet, char[] ch, int offset, int length) throws FastInfosetException, IOException {
        int bitsPerCharacter = 1;
        while ((1 << bitsPerCharacter) <= alphabet.length()) {
            bitsPerCharacter++;
        }
        int bits = length * bitsPerCharacter;
        int octets = bits / 8;
        int bitsOfLastOctet = bits % 8;
        int totalOctets = octets + (bitsOfLastOctet > 0 ? 1 : 0);
        encodeNonZeroOctetStringLengthOnSenventhBit(totalOctets);
        resetBits();
        ensureSize(totalOctets);
        for (int i2 = 0; i2 < length; i2++) {
            char c2 = ch[offset + i2];
            int v2 = 0;
            while (v2 < alphabet.length() && c2 != alphabet.charAt(v2)) {
                v2++;
            }
            if (v2 == alphabet.length()) {
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.characterOutofAlphabetRange"));
            }
            writeBits(bitsPerCharacter, v2);
        }
        if (bitsOfLastOctet > 0) {
            this._b |= (1 << (8 - bitsOfLastOctet)) - 1;
            write(this._b);
        }
    }

    private final void resetBits() {
        this._bitsLeftInOctet = 8;
        this._b = 0;
    }

    private final void writeBits(int bits, int v2) throws IOException {
        while (bits > 0) {
            bits--;
            int bit = (v2 & (1 << bits)) > 0 ? 1 : 0;
            int i2 = this._b;
            int i3 = this._bitsLeftInOctet - 1;
            this._bitsLeftInOctet = i3;
            this._b = i2 | (bit << i3);
            if (this._bitsLeftInOctet == 0) {
                write(this._b);
                this._bitsLeftInOctet = 8;
                this._b = 0;
            }
        }
    }

    protected final void encodeNonZeroOctetStringLengthOnSenventhBit(int length) throws IOException {
        if (length < 3) {
            write(this._b | (length - 1));
            return;
        }
        if (length < 259) {
            write(this._b | 2);
            write(length - 3);
            return;
        }
        write(this._b | 3);
        int length2 = length - 259;
        write(length2 >>> 24);
        write((length2 >> 16) & 255);
        write((length2 >> 8) & 255);
        write(length2 & 255);
    }

    protected final void encodeNonZeroIntegerOnSecondBitFirstBitOne(int i2) throws IOException {
        if (i2 < 64) {
            write(128 | i2);
            return;
        }
        if (i2 < 8256) {
            int i3 = i2 - 64;
            this._b = 192 | (i3 >> 8);
            write(this._b);
            write(i3 & 255);
            return;
        }
        if (i2 < 1048576) {
            int i4 = i2 - 8256;
            this._b = 224 | (i4 >> 16);
            write(this._b);
            write((i4 >> 8) & 255);
            write(i4 & 255);
            return;
        }
        throw new IOException(CommonResourceBundle.getInstance().getString("message.integerMaxSize", new Object[]{1048576}));
    }

    protected final void encodeNonZeroIntegerOnSecondBitFirstBitZero(int i2) throws IOException {
        if (i2 < 64) {
            write(i2);
            return;
        }
        if (i2 < 8256) {
            int i3 = i2 - 64;
            this._b = 64 | (i3 >> 8);
            write(this._b);
            write(i3 & 255);
            return;
        }
        int i4 = i2 - 8256;
        this._b = 96 | (i4 >> 16);
        write(this._b);
        write((i4 >> 8) & 255);
        write(i4 & 255);
    }

    protected final void encodeNonZeroIntegerOnThirdBit(int i2) throws IOException {
        if (i2 < 32) {
            write(this._b | i2);
            return;
        }
        if (i2 < 2080) {
            int i3 = i2 - 32;
            this._b |= 32 | (i3 >> 8);
            write(this._b);
            write(i3 & 255);
            return;
        }
        if (i2 < 526368) {
            int i4 = i2 - 2080;
            this._b |= 40 | (i4 >> 16);
            write(this._b);
            write((i4 >> 8) & 255);
            write(i4 & 255);
            return;
        }
        int i5 = i2 - EncodingConstants.INTEGER_3RD_BIT_LARGE_LIMIT;
        this._b |= 48;
        write(this._b);
        write(i5 >> 16);
        write((i5 >> 8) & 255);
        write(i5 & 255);
    }

    protected final void encodeNonZeroIntegerOnFourthBit(int i2) throws IOException {
        if (i2 < 16) {
            write(this._b | i2);
            return;
        }
        if (i2 < 1040) {
            int i3 = i2 - 16;
            this._b |= 16 | (i3 >> 8);
            write(this._b);
            write(i3 & 255);
            return;
        }
        if (i2 < 263184) {
            int i4 = i2 - 1040;
            this._b |= 20 | (i4 >> 16);
            write(this._b);
            write((i4 >> 8) & 255);
            write(i4 & 255);
            return;
        }
        int i5 = i2 - EncodingConstants.INTEGER_4TH_BIT_LARGE_LIMIT;
        this._b |= 24;
        write(this._b);
        write(i5 >> 16);
        write((i5 >> 8) & 255);
        write(i5 & 255);
    }

    protected final void encodeNonEmptyUTF8StringAsOctetString(int b2, String s2, int[] constants) throws IOException {
        char[] ch = s2.toCharArray();
        encodeNonEmptyUTF8StringAsOctetString(b2, ch, 0, ch.length, constants);
    }

    protected final void encodeNonEmptyUTF8StringAsOctetString(int b2, char[] ch, int offset, int length, int[] constants) throws IOException {
        int length2 = encodeUTF8String(ch, offset, length);
        encodeNonZeroOctetStringLength(b2, length2, constants);
        write(this._encodingBuffer, length2);
    }

    protected final void encodeNonZeroOctetStringLength(int b2, int length, int[] constants) throws IOException {
        if (length < constants[0]) {
            write(b2 | (length - 1));
            return;
        }
        if (length < constants[1]) {
            write(b2 | constants[2]);
            write(length - constants[0]);
            return;
        }
        write(b2 | constants[3]);
        int length2 = length - constants[1];
        write(length2 >>> 24);
        write((length2 >> 16) & 255);
        write((length2 >> 8) & 255);
        write(length2 & 255);
    }

    protected final void encodeNonZeroInteger(int b2, int i2, int[] constants) throws IOException {
        if (i2 < constants[0]) {
            write(b2 | i2);
            return;
        }
        if (i2 < constants[1]) {
            int i3 = i2 - constants[0];
            write(b2 | constants[3] | (i3 >> 8));
            write(i3 & 255);
        } else {
            if (i2 < constants[2]) {
                int i4 = i2 - constants[1];
                write(b2 | constants[4] | (i4 >> 16));
                write((i4 >> 8) & 255);
                write(i4 & 255);
                return;
            }
            if (i2 < 1048576) {
                int i5 = i2 - constants[2];
                write(b2 | constants[5]);
                write(i5 >> 16);
                write((i5 >> 8) & 255);
                write(i5 & 255);
                return;
            }
            throw new IOException(CommonResourceBundle.getInstance().getString("message.integerMaxSize", new Object[]{1048576}));
        }
    }

    protected final void mark() {
        this._markIndex = this._octetBufferIndex;
    }

    protected final void resetMark() {
        this._markIndex = -1;
    }

    protected final boolean hasMark() {
        return this._markIndex != -1;
    }

    protected final void write(int i2) throws IOException {
        if (this._octetBufferIndex < this._octetBuffer.length) {
            byte[] bArr = this._octetBuffer;
            int i3 = this._octetBufferIndex;
            this._octetBufferIndex = i3 + 1;
            bArr[i3] = (byte) i2;
            return;
        }
        if (this._markIndex == -1) {
            this._s.write(this._octetBuffer);
            this._octetBufferIndex = 1;
            this._octetBuffer[0] = (byte) i2;
        } else {
            resize((this._octetBuffer.length * 3) / 2);
            byte[] bArr2 = this._octetBuffer;
            int i4 = this._octetBufferIndex;
            this._octetBufferIndex = i4 + 1;
            bArr2[i4] = (byte) i2;
        }
    }

    protected final void write(byte[] b2, int length) throws IOException {
        write(b2, 0, length);
    }

    protected final void write(byte[] b2, int offset, int length) throws IOException {
        if (this._octetBufferIndex + length < this._octetBuffer.length) {
            System.arraycopy(b2, offset, this._octetBuffer, this._octetBufferIndex, length);
            this._octetBufferIndex += length;
        } else if (this._markIndex == -1) {
            this._s.write(this._octetBuffer, 0, this._octetBufferIndex);
            this._s.write(b2, offset, length);
            this._octetBufferIndex = 0;
        } else {
            resize((((this._octetBuffer.length + length) * 3) / 2) + 1);
            System.arraycopy(b2, offset, this._octetBuffer, this._octetBufferIndex, length);
            this._octetBufferIndex += length;
        }
    }

    private void ensureSize(int length) {
        if (this._octetBufferIndex + length > this._octetBuffer.length) {
            resize((((this._octetBufferIndex + length) * 3) / 2) + 1);
        }
    }

    private void resize(int length) {
        byte[] b2 = new byte[length];
        System.arraycopy(this._octetBuffer, 0, b2, 0, this._octetBufferIndex);
        this._octetBuffer = b2;
    }

    private void _flush() throws IOException {
        if (this._octetBufferIndex > 0) {
            this._s.write(this._octetBuffer, 0, this._octetBufferIndex);
            this._octetBufferIndex = 0;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/Encoder$EncodingBufferOutputStream.class */
    private class EncodingBufferOutputStream extends OutputStream {
        private EncodingBufferOutputStream() {
        }

        @Override // java.io.OutputStream
        public void write(int b2) throws IOException {
            if (Encoder.this._encodingBufferIndex < Encoder.this._encodingBuffer.length) {
                Encoder.this._encodingBuffer[Encoder.access$108(Encoder.this)] = (byte) b2;
                return;
            }
            byte[] newbuf = new byte[Math.max(Encoder.this._encodingBuffer.length << 1, Encoder.this._encodingBufferIndex)];
            System.arraycopy(Encoder.this._encodingBuffer, 0, newbuf, 0, Encoder.this._encodingBufferIndex);
            Encoder.this._encodingBuffer = newbuf;
            Encoder.this._encodingBuffer[Encoder.access$108(Encoder.this)] = (byte) b2;
        }

        @Override // java.io.OutputStream
        public void write(byte[] b2, int off, int len) throws IOException {
            if (off < 0 || off > b2.length || len < 0 || off + len > b2.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (len != 0) {
                int newoffset = Encoder.this._encodingBufferIndex + len;
                if (newoffset > Encoder.this._encodingBuffer.length) {
                    byte[] newbuf = new byte[Math.max(Encoder.this._encodingBuffer.length << 1, newoffset)];
                    System.arraycopy(Encoder.this._encodingBuffer, 0, newbuf, 0, Encoder.this._encodingBufferIndex);
                    Encoder.this._encodingBuffer = newbuf;
                }
                System.arraycopy(b2, off, Encoder.this._encodingBuffer, Encoder.this._encodingBufferIndex, len);
                Encoder.this._encodingBufferIndex = newoffset;
            }
        }

        public int getLength() {
            return Encoder.this._encodingBufferIndex;
        }

        public void reset() {
            Encoder.this._encodingBufferIndex = 0;
        }
    }

    protected final int encodeUTF8String(String s2) throws IOException {
        int length = s2.length();
        if (length < this._charBuffer.length) {
            s2.getChars(0, length, this._charBuffer, 0);
            return encodeUTF8String(this._charBuffer, 0, length);
        }
        char[] ch = s2.toCharArray();
        return encodeUTF8String(ch, 0, length);
    }

    private void ensureEncodingBufferSizeForUtf8String(int length) {
        int newLength = 4 * length;
        if (this._encodingBuffer.length < newLength) {
            this._encodingBuffer = new byte[newLength];
        }
    }

    protected final int encodeUTF8String(char[] ch, int offset, int length) throws IOException {
        int bpos = 0;
        ensureEncodingBufferSizeForUtf8String(length);
        int end = offset + length;
        while (end != offset) {
            int i2 = offset;
            offset++;
            char c2 = ch[i2];
            if (c2 < 128) {
                int i3 = bpos;
                bpos++;
                this._encodingBuffer[i3] = (byte) c2;
            } else if (c2 < 2048) {
                int i4 = bpos;
                int bpos2 = bpos + 1;
                this._encodingBuffer[i4] = (byte) (192 | (c2 >> 6));
                bpos = bpos2 + 1;
                this._encodingBuffer[bpos2] = (byte) (128 | (c2 & '?'));
            } else if (c2 <= 65535) {
                if (!XMLChar.isHighSurrogate(c2) && !XMLChar.isLowSurrogate(c2)) {
                    int i5 = bpos;
                    int bpos3 = bpos + 1;
                    this._encodingBuffer[i5] = (byte) (224 | (c2 >> '\f'));
                    int bpos4 = bpos3 + 1;
                    this._encodingBuffer[bpos3] = (byte) (128 | ((c2 >> 6) & 63));
                    bpos = bpos4 + 1;
                    this._encodingBuffer[bpos4] = (byte) (128 | (c2 & '?'));
                } else {
                    encodeCharacterAsUtf8FourByte(c2, ch, offset, end, bpos);
                    bpos += 4;
                    offset++;
                }
            }
        }
        return bpos;
    }

    private void encodeCharacterAsUtf8FourByte(int c2, char[] ch, int chpos, int chend, int bpos) throws IOException {
        if (chpos == chend) {
            throw new IOException("");
        }
        char d2 = ch[chpos];
        if (!XMLChar.isLowSurrogate(d2)) {
            throw new IOException("");
        }
        int uc = (((c2 & 1023) << 10) | (d2 & 1023)) + 65536;
        if (uc < 0 || uc >= 2097152) {
            throw new IOException("");
        }
        int bpos2 = bpos + 1;
        this._encodingBuffer[bpos] = (byte) (240 | (uc >> 18));
        int bpos3 = bpos2 + 1;
        this._encodingBuffer[bpos2] = (byte) (128 | ((uc >> 12) & 63));
        int bpos4 = bpos3 + 1;
        this._encodingBuffer[bpos3] = (byte) (128 | ((uc >> 6) & 63));
        int i2 = bpos4 + 1;
        this._encodingBuffer[bpos4] = (byte) (128 | (uc & 63));
    }

    protected final int encodeUtf16String(String s2) throws IOException {
        int length = s2.length();
        if (length < this._charBuffer.length) {
            s2.getChars(0, length, this._charBuffer, 0);
            return encodeUtf16String(this._charBuffer, 0, length);
        }
        char[] ch = s2.toCharArray();
        return encodeUtf16String(ch, 0, length);
    }

    private void ensureEncodingBufferSizeForUtf16String(int length) {
        int newLength = 2 * length;
        if (this._encodingBuffer.length < newLength) {
            this._encodingBuffer = new byte[newLength];
        }
    }

    protected final int encodeUtf16String(char[] ch, int offset, int length) throws IOException {
        int byteLength = 0;
        ensureEncodingBufferSizeForUtf16String(length);
        int n2 = offset + length;
        for (int i2 = offset; i2 < n2; i2++) {
            char c2 = ch[i2];
            int i3 = byteLength;
            int byteLength2 = byteLength + 1;
            this._encodingBuffer[i3] = (byte) (c2 >> '\b');
            byteLength = byteLength2 + 1;
            this._encodingBuffer[byteLength2] = (byte) (c2 & 255);
        }
        return byteLength;
    }

    public static String getPrefixFromQualifiedName(String qName) {
        int i2 = qName.indexOf(58);
        String prefix = "";
        if (i2 != -1) {
            prefix = qName.substring(0, i2);
        }
        return prefix;
    }

    public static boolean isWhiteSpace(char[] ch, int start, int length) {
        if (!XMLChar.isSpace(ch[start])) {
            return false;
        }
        int end = start + length;
        do {
            start++;
            if (start >= end) {
                break;
            }
        } while (XMLChar.isSpace(ch[start]));
        return start == end;
    }

    public static boolean isWhiteSpace(String s2) {
        if (!XMLChar.isSpace(s2.charAt(0))) {
            return false;
        }
        int end = s2.length();
        int start = 1;
        while (start < end) {
            int i2 = start;
            start++;
            if (!XMLChar.isSpace(s2.charAt(i2))) {
                break;
            }
        }
        return start == end;
    }
}
