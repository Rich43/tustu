package sun.security.jgss.krb5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import sun.security.jgss.GSSHeader;
import sun.security.krb5.Confounder;

/* loaded from: rt.jar:sun/security/jgss/krb5/WrapToken.class */
class WrapToken extends MessageToken {
    static final int CONFOUNDER_SIZE = 8;
    static final byte[][] pads = {0, new byte[]{1}, new byte[]{2, 2}, new byte[]{3, 3, 3}, new byte[]{4, 4, 4, 4}, new byte[]{5, 5, 5, 5, 5}, new byte[]{6, 6, 6, 6, 6, 6}, new byte[]{7, 7, 7, 7, 7, 7, 7}, new byte[]{8, 8, 8, 8, 8, 8, 8, 8}};
    private boolean readTokenFromInputStream;
    private InputStream is;
    private byte[] tokenBytes;
    private int tokenOffset;
    private int tokenLen;
    private byte[] dataBytes;
    private int dataOffset;
    private int dataLen;
    private int dataSize;
    byte[] confounder;
    byte[] padding;
    private boolean privacy;

    public WrapToken(Krb5Context krb5Context, byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        super(513, krb5Context, bArr, i2, i3, messageProp);
        this.readTokenFromInputStream = true;
        this.is = null;
        this.tokenBytes = null;
        this.tokenOffset = 0;
        this.tokenLen = 0;
        this.dataBytes = null;
        this.dataOffset = 0;
        this.dataLen = 0;
        this.dataSize = 0;
        this.confounder = null;
        this.padding = null;
        this.privacy = false;
        this.readTokenFromInputStream = false;
        this.tokenBytes = bArr;
        this.tokenOffset = i2;
        this.tokenLen = i3;
        this.privacy = messageProp.getPrivacy();
        this.dataSize = getGSSHeader().getMechTokenLength() - getKrb5TokenSize();
    }

    public WrapToken(Krb5Context krb5Context, InputStream inputStream, MessageProp messageProp) throws GSSException {
        super(513, krb5Context, inputStream, messageProp);
        this.readTokenFromInputStream = true;
        this.is = null;
        this.tokenBytes = null;
        this.tokenOffset = 0;
        this.tokenLen = 0;
        this.dataBytes = null;
        this.dataOffset = 0;
        this.dataLen = 0;
        this.dataSize = 0;
        this.confounder = null;
        this.padding = null;
        this.privacy = false;
        this.is = inputStream;
        this.privacy = messageProp.getPrivacy();
        this.dataSize = getGSSHeader().getMechTokenLength() - getTokenSize();
    }

    public byte[] getData() throws GSSException {
        byte[] bArr = new byte[this.dataSize];
        getData(bArr, 0);
        byte[] bArr2 = new byte[(this.dataSize - this.confounder.length) - this.padding.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        return bArr2;
    }

    public int getData(byte[] bArr, int i2) throws GSSException {
        if (this.readTokenFromInputStream) {
            getDataFromStream(bArr, i2);
        } else {
            getDataFromBuffer(bArr, i2);
        }
        return (this.dataSize - this.confounder.length) - this.padding.length;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v22, types: [int] */
    private void getDataFromBuffer(byte[] bArr, int i2) throws GSSException {
        int length = this.tokenOffset + getGSSHeader().getLength() + getTokenSize();
        if (length + this.dataSize > this.tokenOffset + this.tokenLen) {
            throw new GSSException(10, -1, "Insufficient data in " + getTokenName(getTokenId()));
        }
        this.confounder = new byte[8];
        if (this.privacy) {
            this.cipherHelper.decryptData(this, this.tokenBytes, length, this.dataSize, bArr, i2);
        } else {
            System.arraycopy(this.tokenBytes, length, this.confounder, 0, 8);
            byte b2 = this.tokenBytes[(length + this.dataSize) - 1];
            if (b2 < 0) {
                b2 = 0;
            }
            if (b2 > 8) {
                b2 %= 8;
            }
            this.padding = pads[b2];
            System.arraycopy(this.tokenBytes, length + 8, bArr, i2, (this.dataSize - 8) - b2);
        }
        if (!verifySignAndSeqNumber(this.confounder, bArr, i2, (this.dataSize - 8) - this.padding.length, this.padding)) {
            throw new GSSException(6, -1, "Corrupt checksum or sequence number in Wrap token");
        }
    }

    private void getDataFromStream(byte[] bArr, int i2) throws GSSException {
        getGSSHeader();
        this.confounder = new byte[8];
        try {
            if (this.privacy) {
                this.cipherHelper.decryptData(this, this.is, this.dataSize, bArr, i2);
            } else {
                readFully(this.is, this.confounder);
                if (this.cipherHelper.isArcFour()) {
                    this.padding = pads[1];
                    readFully(this.is, bArr, i2, (this.dataSize - 8) - 1);
                } else {
                    int i3 = ((this.dataSize - 8) / 8) - 1;
                    int i4 = i2;
                    for (int i5 = 0; i5 < i3; i5++) {
                        readFully(this.is, bArr, i4, 8);
                        i4 += 8;
                    }
                    byte[] bArr2 = new byte[8];
                    readFully(this.is, bArr2);
                    byte b2 = bArr2[7];
                    this.padding = pads[b2];
                    System.arraycopy(bArr2, 0, bArr, i4, bArr2.length - b2);
                }
            }
            if (!verifySignAndSeqNumber(this.confounder, bArr, i2, (this.dataSize - 8) - this.padding.length, this.padding)) {
                throw new GSSException(6, -1, "Corrupt checksum or sequence number in Wrap token");
            }
        } catch (IOException e2) {
            throw new GSSException(10, -1, getTokenName(getTokenId()) + ": " + e2.getMessage());
        }
    }

    private byte[] getPadding(int i2) {
        int i3;
        if (this.cipherHelper.isArcFour()) {
            i3 = 1;
        } else {
            i3 = 8 - (i2 % 8);
        }
        return pads[i3];
    }

    public WrapToken(Krb5Context krb5Context, MessageProp messageProp, byte[] bArr, int i2, int i3) throws GSSException {
        super(513, krb5Context);
        this.readTokenFromInputStream = true;
        this.is = null;
        this.tokenBytes = null;
        this.tokenOffset = 0;
        this.tokenLen = 0;
        this.dataBytes = null;
        this.dataOffset = 0;
        this.dataLen = 0;
        this.dataSize = 0;
        this.confounder = null;
        this.padding = null;
        this.privacy = false;
        this.confounder = Confounder.bytes(8);
        this.padding = getPadding(i3);
        this.dataSize = this.confounder.length + i3 + this.padding.length;
        this.dataBytes = bArr;
        this.dataOffset = i2;
        this.dataLen = i3;
        genSignAndSeqNumber(messageProp, this.confounder, bArr, i2, i3, this.padding);
        if (!krb5Context.getConfState()) {
            messageProp.setPrivacy(false);
        }
        this.privacy = messageProp.getPrivacy();
    }

    @Override // sun.security.jgss.krb5.MessageToken
    public void encode(OutputStream outputStream) throws IOException, GSSException {
        super.encode(outputStream);
        if (!this.privacy) {
            outputStream.write(this.confounder);
            outputStream.write(this.dataBytes, this.dataOffset, this.dataLen);
            outputStream.write(this.padding);
            return;
        }
        this.cipherHelper.encryptData(this, this.confounder, this.dataBytes, this.dataOffset, this.dataLen, this.padding, outputStream);
    }

    public byte[] encode() throws IOException, GSSException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(this.dataSize + 50);
        encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public int encode(byte[] bArr, int i2) throws IOException, GSSException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        super.encode(byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        System.arraycopy(byteArray, 0, bArr, i2, byteArray.length);
        int length = i2 + byteArray.length;
        if (!this.privacy) {
            System.arraycopy(this.confounder, 0, bArr, length, this.confounder.length);
            int length2 = length + this.confounder.length;
            System.arraycopy(this.dataBytes, this.dataOffset, bArr, length2, this.dataLen);
            System.arraycopy(this.padding, 0, bArr, length2 + this.dataLen, this.padding.length);
        } else {
            this.cipherHelper.encryptData(this, this.confounder, this.dataBytes, this.dataOffset, this.dataLen, this.padding, bArr, length);
        }
        return byteArray.length + this.confounder.length + this.dataLen + this.padding.length;
    }

    @Override // sun.security.jgss.krb5.MessageToken
    protected int getKrb5TokenSize() throws GSSException {
        return getTokenSize() + this.dataSize;
    }

    @Override // sun.security.jgss.krb5.MessageToken
    protected int getSealAlg(boolean z2, int i2) throws GSSException {
        if (!z2) {
            return 65535;
        }
        return this.cipherHelper.getSealAlg();
    }

    static int getSizeLimit(int i2, boolean z2, int i3, CipherHelper cipherHelper) throws GSSException {
        return (GSSHeader.getMaxMechTokenSize(OID, i3) - (getTokenSize(cipherHelper) + 8)) - 8;
    }
}
