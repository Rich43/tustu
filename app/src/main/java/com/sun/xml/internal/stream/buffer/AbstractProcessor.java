package com.sun.xml.internal.stream.buffer;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/AbstractProcessor.class */
public abstract class AbstractProcessor extends AbstractCreatorProcessor {
    protected static final int STATE_ILLEGAL = 0;
    protected static final int STATE_DOCUMENT = 1;
    protected static final int STATE_DOCUMENT_FRAGMENT = 2;
    protected static final int STATE_ELEMENT_U_LN_QN = 3;
    protected static final int STATE_ELEMENT_P_U_LN = 4;
    protected static final int STATE_ELEMENT_U_LN = 5;
    protected static final int STATE_ELEMENT_LN = 6;
    protected static final int STATE_TEXT_AS_CHAR_ARRAY_SMALL = 7;
    protected static final int STATE_TEXT_AS_CHAR_ARRAY_MEDIUM = 8;
    protected static final int STATE_TEXT_AS_CHAR_ARRAY_COPY = 9;
    protected static final int STATE_TEXT_AS_STRING = 10;
    protected static final int STATE_TEXT_AS_OBJECT = 11;
    protected static final int STATE_COMMENT_AS_CHAR_ARRAY_SMALL = 12;
    protected static final int STATE_COMMENT_AS_CHAR_ARRAY_MEDIUM = 13;
    protected static final int STATE_COMMENT_AS_CHAR_ARRAY_COPY = 14;
    protected static final int STATE_COMMENT_AS_STRING = 15;
    protected static final int STATE_PROCESSING_INSTRUCTION = 16;
    protected static final int STATE_END = 17;
    protected static final int STATE_NAMESPACE_ATTRIBUTE = 1;
    protected static final int STATE_NAMESPACE_ATTRIBUTE_P = 2;
    protected static final int STATE_NAMESPACE_ATTRIBUTE_P_U = 3;
    protected static final int STATE_NAMESPACE_ATTRIBUTE_U = 4;
    protected static final int STATE_ATTRIBUTE_U_LN_QN = 1;
    protected static final int STATE_ATTRIBUTE_P_U_LN = 2;
    protected static final int STATE_ATTRIBUTE_U_LN = 3;
    protected static final int STATE_ATTRIBUTE_LN = 4;
    protected static final int STATE_ATTRIBUTE_U_LN_QN_OBJECT = 5;
    protected static final int STATE_ATTRIBUTE_P_U_LN_OBJECT = 6;
    protected static final int STATE_ATTRIBUTE_U_LN_OBJECT = 7;
    protected static final int STATE_ATTRIBUTE_LN_OBJECT = 8;
    protected XMLStreamBuffer _buffer;
    protected boolean _fragmentMode;
    protected int _treeCount;
    private static final int[] _eiiStateTable = new int[256];
    private static final int[] _niiStateTable = new int[256];
    private static final int[] _aiiStateTable = new int[256];
    protected boolean _stringInterningFeature = false;
    protected final StringBuilder _qNameBuffer = new StringBuilder();

    static {
        _eiiStateTable[16] = 1;
        _eiiStateTable[17] = 2;
        _eiiStateTable[38] = 3;
        _eiiStateTable[35] = 4;
        _eiiStateTable[34] = 5;
        _eiiStateTable[32] = 6;
        _eiiStateTable[80] = 7;
        _eiiStateTable[81] = 8;
        _eiiStateTable[84] = 9;
        _eiiStateTable[88] = 10;
        _eiiStateTable[92] = 11;
        _eiiStateTable[96] = 12;
        _eiiStateTable[97] = 13;
        _eiiStateTable[100] = 14;
        _eiiStateTable[104] = 15;
        _eiiStateTable[112] = 16;
        _eiiStateTable[144] = 17;
        _niiStateTable[64] = 1;
        _niiStateTable[65] = 2;
        _niiStateTable[67] = 3;
        _niiStateTable[66] = 4;
        _aiiStateTable[54] = 1;
        _aiiStateTable[51] = 2;
        _aiiStateTable[50] = 3;
        _aiiStateTable[48] = 4;
        _aiiStateTable[62] = 5;
        _aiiStateTable[59] = 6;
        _aiiStateTable[58] = 7;
        _aiiStateTable[56] = 8;
    }

    protected final void setBuffer(XMLStreamBuffer buffer) {
        setBuffer(buffer, buffer.isFragment());
    }

    protected final void setBuffer(XMLStreamBuffer buffer, boolean fragmentMode) {
        this._buffer = buffer;
        this._fragmentMode = fragmentMode;
        this._currentStructureFragment = this._buffer.getStructure();
        this._structure = this._currentStructureFragment.getArray();
        this._structurePtr = this._buffer.getStructurePtr();
        this._currentStructureStringFragment = this._buffer.getStructureStrings();
        this._structureStrings = this._currentStructureStringFragment.getArray();
        this._structureStringsPtr = this._buffer.getStructureStringsPtr();
        this._currentContentCharactersBufferFragment = this._buffer.getContentCharactersBuffer();
        this._contentCharactersBuffer = this._currentContentCharactersBufferFragment.getArray();
        this._contentCharactersBufferPtr = this._buffer.getContentCharactersBufferPtr();
        this._currentContentObjectFragment = this._buffer.getContentObjects();
        this._contentObjects = this._currentContentObjectFragment.getArray();
        this._contentObjectsPtr = this._buffer.getContentObjectsPtr();
        this._stringInterningFeature = this._buffer.hasInternedStrings();
        this._treeCount = this._buffer.treeCount;
    }

    protected final int peekStructure() {
        if (this._structurePtr < this._structure.length) {
            return this._structure[this._structurePtr] & 255;
        }
        return readFromNextStructure(0);
    }

    protected final int readStructure() {
        if (this._structurePtr < this._structure.length) {
            byte[] bArr = this._structure;
            int i2 = this._structurePtr;
            this._structurePtr = i2 + 1;
            return bArr[i2] & 255;
        }
        return readFromNextStructure(1);
    }

    protected final int readEiiState() {
        return _eiiStateTable[readStructure()];
    }

    protected static int getEIIState(int item) {
        return _eiiStateTable[item];
    }

    protected static int getNIIState(int item) {
        return _niiStateTable[item];
    }

    protected static int getAIIState(int item) {
        return _aiiStateTable[item];
    }

    protected final int readStructure16() {
        return (readStructure() << 8) | readStructure();
    }

    private int readFromNextStructure(int v2) {
        this._structurePtr = v2;
        this._currentStructureFragment = this._currentStructureFragment.getNext();
        this._structure = this._currentStructureFragment.getArray();
        return this._structure[0] & 255;
    }

    protected final String readStructureString() {
        if (this._structureStringsPtr < this._structureStrings.length) {
            String[] strArr = this._structureStrings;
            int i2 = this._structureStringsPtr;
            this._structureStringsPtr = i2 + 1;
            return strArr[i2];
        }
        this._structureStringsPtr = 1;
        this._currentStructureStringFragment = this._currentStructureStringFragment.getNext();
        this._structureStrings = this._currentStructureStringFragment.getArray();
        return this._structureStrings[0];
    }

    protected final String readContentString() {
        return (String) readContentObject();
    }

    protected final char[] readContentCharactersCopy() {
        return (char[]) readContentObject();
    }

    protected final int readContentCharactersBuffer(int length) {
        if (this._contentCharactersBufferPtr + length < this._contentCharactersBuffer.length) {
            int start = this._contentCharactersBufferPtr;
            this._contentCharactersBufferPtr += length;
            return start;
        }
        this._contentCharactersBufferPtr = length;
        this._currentContentCharactersBufferFragment = this._currentContentCharactersBufferFragment.getNext();
        this._contentCharactersBuffer = this._currentContentCharactersBufferFragment.getArray();
        return 0;
    }

    protected final Object readContentObject() {
        if (this._contentObjectsPtr < this._contentObjects.length) {
            Object[] objArr = this._contentObjects;
            int i2 = this._contentObjectsPtr;
            this._contentObjectsPtr = i2 + 1;
            return objArr[i2];
        }
        this._contentObjectsPtr = 1;
        this._currentContentObjectFragment = this._currentContentObjectFragment.getNext();
        this._contentObjects = this._currentContentObjectFragment.getArray();
        return this._contentObjects[0];
    }

    protected final String getQName(String prefix, String localName) {
        this._qNameBuffer.append(prefix).append(':').append(localName);
        String qName = this._qNameBuffer.toString();
        this._qNameBuffer.setLength(0);
        return this._stringInterningFeature ? qName.intern() : qName;
    }

    protected final String getPrefixFromQName(String qName) {
        int pIndex = qName.indexOf(58);
        return this._stringInterningFeature ? pIndex != -1 ? qName.substring(0, pIndex).intern() : "" : pIndex != -1 ? qName.substring(0, pIndex) : "";
    }
}
