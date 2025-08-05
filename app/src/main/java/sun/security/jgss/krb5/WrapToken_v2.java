package sun.security.jgss.krb5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import sun.security.jgss.GSSHeader;
import sun.security.krb5.Confounder;

/* loaded from: rt.jar:sun/security/jgss/krb5/WrapToken_v2.class */
class WrapToken_v2 extends MessageToken_v2 {
    byte[] confounder;
    private final boolean privacy;

    public WrapToken_v2(Krb5Context krb5Context, byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        super(1284, krb5Context, bArr, i2, i3, messageProp);
        this.confounder = null;
        this.privacy = messageProp.getPrivacy();
    }

    public WrapToken_v2(Krb5Context krb5Context, InputStream inputStream, MessageProp messageProp) throws GSSException {
        super(1284, krb5Context, inputStream, messageProp);
        this.confounder = null;
        this.privacy = messageProp.getPrivacy();
    }

    public byte[] getData() throws GSSException {
        byte[] bArr = new byte[this.tokenDataLen];
        return Arrays.copyOf(bArr, getData(bArr, 0));
    }

    public int getData(byte[] bArr, int i2) throws GSSException {
        if (this.privacy) {
            this.cipherHelper.decryptData(this, this.tokenData, 0, this.tokenDataLen, bArr, i2, getKeyUsage());
            return ((this.tokenDataLen - 16) - 16) - this.cipherHelper.getChecksumLength();
        }
        int checksumLength = this.tokenDataLen - this.cipherHelper.getChecksumLength();
        System.arraycopy(this.tokenData, 0, bArr, i2, checksumLength);
        if (!verifySign(bArr, i2, checksumLength)) {
            throw new GSSException(6, -1, "Corrupt checksum in Wrap token");
        }
        return checksumLength;
    }

    public WrapToken_v2(Krb5Context krb5Context, MessageProp messageProp, byte[] bArr, int i2, int i3) throws GSSException {
        super(1284, krb5Context);
        this.confounder = null;
        this.confounder = Confounder.bytes(16);
        genSignAndSeqNumber(messageProp, bArr, i2, i3);
        if (!krb5Context.getConfState()) {
            messageProp.setPrivacy(false);
        }
        this.privacy = messageProp.getPrivacy();
        if (!this.privacy) {
            this.tokenData = new byte[i3 + this.checksum.length];
            System.arraycopy(bArr, i2, this.tokenData, 0, i3);
            System.arraycopy(this.checksum, 0, this.tokenData, i3, this.checksum.length);
            return;
        }
        this.tokenData = this.cipherHelper.encryptData(this, this.confounder, getTokenHeader(), bArr, i2, i3, getKeyUsage());
    }

    @Override // sun.security.jgss.krb5.MessageToken_v2
    public void encode(OutputStream outputStream) throws IOException {
        encodeHeader(outputStream);
        outputStream.write(this.tokenData);
    }

    public byte[] encode() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(16 + this.tokenData.length);
        encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public int encode(byte[] bArr, int i2) throws IOException {
        byte[] bArrEncode = encode();
        System.arraycopy(bArrEncode, 0, bArr, i2, bArrEncode.length);
        return bArrEncode.length;
    }

    static int getSizeLimit(int i2, boolean z2, int i3, CipherHelper cipherHelper) throws GSSException {
        return (GSSHeader.getMaxMechTokenSize(OID, i3) - ((16 + cipherHelper.getChecksumLength()) + 16)) - 8;
    }
}
