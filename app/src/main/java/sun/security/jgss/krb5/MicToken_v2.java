package sun.security.jgss.krb5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

/* loaded from: rt.jar:sun/security/jgss/krb5/MicToken_v2.class */
class MicToken_v2 extends MessageToken_v2 {
    public MicToken_v2(Krb5Context krb5Context, byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        super(Krb5Token.MIC_ID_v2, krb5Context, bArr, i2, i3, messageProp);
    }

    public MicToken_v2(Krb5Context krb5Context, InputStream inputStream, MessageProp messageProp) throws GSSException {
        super(Krb5Token.MIC_ID_v2, krb5Context, inputStream, messageProp);
    }

    public void verify(byte[] bArr, int i2, int i3) throws GSSException {
        if (!verifySign(bArr, i2, i3)) {
            throw new GSSException(6, -1, "Corrupt checksum or sequence number in MIC token");
        }
    }

    public void verify(InputStream inputStream) throws GSSException {
        try {
            byte[] bArr = new byte[inputStream.available()];
            inputStream.read(bArr);
            verify(bArr, 0, bArr.length);
        } catch (IOException e2) {
            throw new GSSException(6, -1, "Corrupt checksum or sequence number in MIC token");
        }
    }

    public MicToken_v2(Krb5Context krb5Context, MessageProp messageProp, byte[] bArr, int i2, int i3) throws GSSException {
        super(Krb5Token.MIC_ID_v2, krb5Context);
        genSignAndSeqNumber(messageProp == null ? new MessageProp(0, false) : messageProp, bArr, i2, i3);
    }

    public MicToken_v2(Krb5Context krb5Context, MessageProp messageProp, InputStream inputStream) throws IOException, GSSException {
        super(Krb5Token.MIC_ID_v2, krb5Context);
        byte[] bArr = new byte[inputStream.available()];
        inputStream.read(bArr);
        genSignAndSeqNumber(messageProp == null ? new MessageProp(0, false) : messageProp, bArr, 0, bArr.length);
    }

    public byte[] encode() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(50);
        encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public int encode(byte[] bArr, int i2) throws IOException {
        byte[] bArrEncode = encode();
        System.arraycopy(bArrEncode, 0, bArr, i2, bArrEncode.length);
        return bArrEncode.length;
    }

    @Override // sun.security.jgss.krb5.MessageToken_v2
    public void encode(OutputStream outputStream) throws IOException {
        encodeHeader(outputStream);
        outputStream.write(this.checksum);
    }
}
