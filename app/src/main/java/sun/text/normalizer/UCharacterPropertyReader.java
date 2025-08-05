package sun.text.normalizer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.text.normalizer.ICUBinary;

/* loaded from: rt.jar:sun/text/normalizer/UCharacterPropertyReader.class */
final class UCharacterPropertyReader implements ICUBinary.Authenticate {
    private static final int INDEX_SIZE_ = 16;
    private DataInputStream m_dataInputStream_;
    private int m_propertyOffset_;
    private int m_exceptionOffset_;
    private int m_caseOffset_;
    private int m_additionalOffset_;
    private int m_additionalVectorsOffset_;
    private int m_additionalColumnsCount_;
    private int m_reservedOffset_;
    private byte[] m_unicodeVersion_;
    private static final byte[] DATA_FORMAT_ID_ = {85, 80, 114, 111};
    private static final byte[] DATA_FORMAT_VERSION_ = {5, 0, 5, 2};

    @Override // sun.text.normalizer.ICUBinary.Authenticate
    public boolean isDataVersionAcceptable(byte[] bArr) {
        return bArr[0] == DATA_FORMAT_VERSION_[0] && bArr[2] == DATA_FORMAT_VERSION_[2] && bArr[3] == DATA_FORMAT_VERSION_[3];
    }

    protected UCharacterPropertyReader(InputStream inputStream) throws IOException {
        this.m_unicodeVersion_ = ICUBinary.readHeader(inputStream, DATA_FORMAT_ID_, this);
        this.m_dataInputStream_ = new DataInputStream(inputStream);
    }

    protected void read(UCharacterProperty uCharacterProperty) throws IOException {
        this.m_propertyOffset_ = this.m_dataInputStream_.readInt();
        this.m_exceptionOffset_ = this.m_dataInputStream_.readInt();
        this.m_caseOffset_ = this.m_dataInputStream_.readInt();
        this.m_additionalOffset_ = this.m_dataInputStream_.readInt();
        this.m_additionalVectorsOffset_ = this.m_dataInputStream_.readInt();
        this.m_additionalColumnsCount_ = this.m_dataInputStream_.readInt();
        this.m_reservedOffset_ = this.m_dataInputStream_.readInt();
        this.m_dataInputStream_.skipBytes(12);
        uCharacterProperty.m_maxBlockScriptValue_ = this.m_dataInputStream_.readInt();
        uCharacterProperty.m_maxJTGValue_ = this.m_dataInputStream_.readInt();
        this.m_dataInputStream_.skipBytes(((((((((((16 - 1) - 1) - 1) - 1) - 1) - 1) - 1) - 3) - 1) - 1) << 2);
        uCharacterProperty.m_trie_ = new CharTrie(this.m_dataInputStream_, null);
        this.m_dataInputStream_.skipBytes((this.m_exceptionOffset_ - this.m_propertyOffset_) * 4);
        this.m_dataInputStream_.skipBytes((this.m_caseOffset_ - this.m_exceptionOffset_) * 4);
        this.m_dataInputStream_.skipBytes(((this.m_additionalOffset_ - this.m_caseOffset_) << 1) * 2);
        if (this.m_additionalColumnsCount_ > 0) {
            uCharacterProperty.m_additionalTrie_ = new CharTrie(this.m_dataInputStream_, null);
            int i2 = this.m_reservedOffset_ - this.m_additionalVectorsOffset_;
            uCharacterProperty.m_additionalVectors_ = new int[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                uCharacterProperty.m_additionalVectors_[i3] = this.m_dataInputStream_.readInt();
            }
        }
        this.m_dataInputStream_.close();
        uCharacterProperty.m_additionalColumnsCount_ = this.m_additionalColumnsCount_;
        uCharacterProperty.m_unicodeVersion_ = VersionInfo.getInstance(this.m_unicodeVersion_[0], this.m_unicodeVersion_[1], this.m_unicodeVersion_[2], this.m_unicodeVersion_[3]);
    }
}
