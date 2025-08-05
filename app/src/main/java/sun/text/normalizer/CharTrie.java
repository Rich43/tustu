package sun.text.normalizer;

import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.text.normalizer.Trie;

/* loaded from: rt.jar:sun/text/normalizer/CharTrie.class */
public class CharTrie extends Trie {
    private char m_initialValue_;
    private char[] m_data_;
    private FriendAgent m_friendAgent_;

    public CharTrie(InputStream inputStream, Trie.DataManipulate dataManipulate) throws IOException {
        super(inputStream, dataManipulate);
        if (!isCharTrie()) {
            throw new IllegalArgumentException("Data given does not belong to a char trie.");
        }
        this.m_friendAgent_ = new FriendAgent();
    }

    public CharTrie(int i2, int i3, Trie.DataManipulate dataManipulate) {
        super(new char[EncodingConstants.INTEGER_3RD_BIT_MEDIUM_LIMIT], 512, dataManipulate);
        int i4 = i3 != i2 ? 256 + 32 : 256;
        this.m_data_ = new char[i4];
        this.m_dataLength_ = i4;
        this.m_initialValue_ = (char) i2;
        for (int i5 = 0; i5 < 256; i5++) {
            this.m_data_[i5] = (char) i2;
        }
        if (i3 != i2) {
            char c2 = (char) (256 >> 2);
            for (int i6 = 1728; i6 < 1760; i6++) {
                this.m_index_[i6] = c2;
            }
            int i7 = 256 + 32;
            for (int i8 = 256; i8 < i7; i8++) {
                this.m_data_[i8] = (char) i3;
            }
        }
        this.m_friendAgent_ = new FriendAgent();
    }

    /* loaded from: rt.jar:sun/text/normalizer/CharTrie$FriendAgent.class */
    public class FriendAgent {
        public FriendAgent() {
        }

        public char[] getPrivateIndex() {
            return CharTrie.this.m_index_;
        }

        public char[] getPrivateData() {
            return CharTrie.this.m_data_;
        }

        public int getPrivateInitialValue() {
            return CharTrie.this.m_initialValue_;
        }
    }

    public void putIndexData(UCharacterProperty uCharacterProperty) {
        uCharacterProperty.setIndexData(this.m_friendAgent_);
    }

    public final char getCodePointValue(int i2) {
        if (0 <= i2 && i2 < 55296) {
            return this.m_data_[(this.m_index_[i2 >> 5] << 2) + (i2 & 31)];
        }
        int codePointOffset = getCodePointOffset(i2);
        return codePointOffset >= 0 ? this.m_data_[codePointOffset] : this.m_initialValue_;
    }

    public final char getLeadValue(char c2) {
        return this.m_data_[getLeadOffset(c2)];
    }

    public final char getSurrogateValue(char c2, char c3) {
        int surrogateOffset = getSurrogateOffset(c2, c3);
        if (surrogateOffset > 0) {
            return this.m_data_[surrogateOffset];
        }
        return this.m_initialValue_;
    }

    public final char getTrailValue(int i2, char c2) {
        if (this.m_dataManipulate_ == null) {
            throw new NullPointerException("The field DataManipulate in this Trie is null");
        }
        int foldingOffset = this.m_dataManipulate_.getFoldingOffset(i2);
        if (foldingOffset > 0) {
            return this.m_data_[getRawOffset(foldingOffset, (char) (c2 & 1023))];
        }
        return this.m_initialValue_;
    }

    @Override // sun.text.normalizer.Trie
    protected final void unserialize(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int i2 = this.m_dataOffset_ + this.m_dataLength_;
        this.m_index_ = new char[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            this.m_index_[i3] = dataInputStream.readChar();
        }
        this.m_data_ = this.m_index_;
        this.m_initialValue_ = this.m_data_[this.m_dataOffset_];
    }

    @Override // sun.text.normalizer.Trie
    protected final int getSurrogateOffset(char c2, char c3) {
        if (this.m_dataManipulate_ == null) {
            throw new NullPointerException("The field DataManipulate in this Trie is null");
        }
        int foldingOffset = this.m_dataManipulate_.getFoldingOffset(getLeadValue(c2));
        if (foldingOffset > 0) {
            return getRawOffset(foldingOffset, (char) (c3 & 1023));
        }
        return -1;
    }

    @Override // sun.text.normalizer.Trie
    protected final int getValue(int i2) {
        return this.m_data_[i2];
    }

    @Override // sun.text.normalizer.Trie
    protected final int getInitialValue() {
        return this.m_initialValue_;
    }
}
