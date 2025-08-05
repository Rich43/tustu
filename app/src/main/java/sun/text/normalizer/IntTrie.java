package sun.text.normalizer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.text.normalizer.Trie;

/* loaded from: rt.jar:sun/text/normalizer/IntTrie.class */
public class IntTrie extends Trie {
    private int m_initialValue_;
    private int[] m_data_;

    public IntTrie(InputStream inputStream, Trie.DataManipulate dataManipulate) throws IOException {
        super(inputStream, dataManipulate);
        if (!isIntTrie()) {
            throw new IllegalArgumentException("Data given does not belong to a int trie.");
        }
    }

    public final int getCodePointValue(int i2) {
        int codePointOffset = getCodePointOffset(i2);
        return codePointOffset >= 0 ? this.m_data_[codePointOffset] : this.m_initialValue_;
    }

    public final int getLeadValue(char c2) {
        return this.m_data_[getLeadOffset(c2)];
    }

    public final int getTrailValue(int i2, char c2) {
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
        super.unserialize(inputStream);
        this.m_data_ = new int[this.m_dataLength_];
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        for (int i2 = 0; i2 < this.m_dataLength_; i2++) {
            this.m_data_[i2] = dataInputStream.readInt();
        }
        this.m_initialValue_ = this.m_data_[0];
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

    IntTrie(char[] cArr, int[] iArr, int i2, int i3, Trie.DataManipulate dataManipulate) {
        super(cArr, i3, dataManipulate);
        this.m_data_ = iArr;
        this.m_dataLength_ = this.m_data_.length;
        this.m_initialValue_ = i2;
    }
}
