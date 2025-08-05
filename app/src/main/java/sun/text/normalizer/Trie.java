package sun.text.normalizer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/text/normalizer/Trie.class */
public abstract class Trie {
    protected static final int LEAD_INDEX_OFFSET_ = 320;
    protected static final int INDEX_STAGE_1_SHIFT_ = 5;
    protected static final int INDEX_STAGE_2_SHIFT_ = 2;
    protected static final int DATA_BLOCK_LENGTH = 32;
    protected static final int INDEX_STAGE_3_MASK_ = 31;
    protected static final int SURROGATE_BLOCK_BITS = 5;
    protected static final int SURROGATE_BLOCK_COUNT = 32;
    protected static final int BMP_INDEX_LENGTH = 2048;
    protected static final int SURROGATE_MASK_ = 1023;
    protected char[] m_index_;
    protected DataManipulate m_dataManipulate_;
    protected int m_dataOffset_;
    protected int m_dataLength_;
    protected static final int HEADER_OPTIONS_LATIN1_IS_LINEAR_MASK_ = 512;
    protected static final int HEADER_SIGNATURE_ = 1416784229;
    private static final int HEADER_OPTIONS_SHIFT_MASK_ = 15;
    protected static final int HEADER_OPTIONS_INDEX_SHIFT_ = 4;
    protected static final int HEADER_OPTIONS_DATA_IS_32_BIT_ = 256;
    private boolean m_isLatin1Linear_;
    private int m_options_;

    /* loaded from: rt.jar:sun/text/normalizer/Trie$DataManipulate.class */
    public interface DataManipulate {
        int getFoldingOffset(int i2);
    }

    protected abstract int getSurrogateOffset(char c2, char c3);

    protected abstract int getValue(int i2);

    protected abstract int getInitialValue();

    /* loaded from: rt.jar:sun/text/normalizer/Trie$DefaultGetFoldingOffset.class */
    private static class DefaultGetFoldingOffset implements DataManipulate {
        private DefaultGetFoldingOffset() {
        }

        @Override // sun.text.normalizer.Trie.DataManipulate
        public int getFoldingOffset(int i2) {
            return i2;
        }
    }

    protected Trie(InputStream inputStream, DataManipulate dataManipulate) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int i2 = dataInputStream.readInt();
        this.m_options_ = dataInputStream.readInt();
        if (!checkHeader(i2)) {
            throw new IllegalArgumentException("ICU data file error: Trie header authentication failed, please check if you have the most updated ICU data file");
        }
        if (dataManipulate != null) {
            this.m_dataManipulate_ = dataManipulate;
        } else {
            this.m_dataManipulate_ = new DefaultGetFoldingOffset();
        }
        this.m_isLatin1Linear_ = (this.m_options_ & 512) != 0;
        this.m_dataOffset_ = dataInputStream.readInt();
        this.m_dataLength_ = dataInputStream.readInt();
        unserialize(inputStream);
    }

    protected Trie(char[] cArr, int i2, DataManipulate dataManipulate) {
        this.m_options_ = i2;
        if (dataManipulate != null) {
            this.m_dataManipulate_ = dataManipulate;
        } else {
            this.m_dataManipulate_ = new DefaultGetFoldingOffset();
        }
        this.m_isLatin1Linear_ = (this.m_options_ & 512) != 0;
        this.m_index_ = cArr;
        this.m_dataOffset_ = this.m_index_.length;
    }

    protected final int getRawOffset(int i2, char c2) {
        return (this.m_index_[i2 + (c2 >> 5)] << 2) + (c2 & 31);
    }

    protected final int getBMPOffset(char c2) {
        if (c2 >= 55296 && c2 <= 56319) {
            return getRawOffset(LEAD_INDEX_OFFSET_, c2);
        }
        return getRawOffset(0, c2);
    }

    protected final int getLeadOffset(char c2) {
        return getRawOffset(0, c2);
    }

    protected final int getCodePointOffset(int i2) {
        if (i2 < 0) {
            return -1;
        }
        if (i2 < 55296) {
            return getRawOffset(0, (char) i2);
        }
        if (i2 < 65536) {
            return getBMPOffset((char) i2);
        }
        if (i2 <= 1114111) {
            return getSurrogateOffset(UTF16.getLeadSurrogate(i2), (char) (i2 & 1023));
        }
        return -1;
    }

    protected void unserialize(InputStream inputStream) throws IOException {
        this.m_index_ = new char[this.m_dataOffset_];
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        for (int i2 = 0; i2 < this.m_dataOffset_; i2++) {
            this.m_index_[i2] = dataInputStream.readChar();
        }
    }

    protected final boolean isIntTrie() {
        return (this.m_options_ & 256) != 0;
    }

    protected final boolean isCharTrie() {
        return (this.m_options_ & 256) == 0;
    }

    private final boolean checkHeader(int i2) {
        if (i2 != HEADER_SIGNATURE_ || (this.m_options_ & 15) != 5 || ((this.m_options_ >> 4) & 15) != 2) {
            return false;
        }
        return true;
    }
}
