package sun.security.jgss.krb5;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;

/* loaded from: rt.jar:sun/security/jgss/krb5/MicToken.class */
class MicToken extends MessageToken {
    public MicToken(Krb5Context krb5Context, byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        super(257, krb5Context, bArr, i2, i3, messageProp);
    }

    public MicToken(Krb5Context krb5Context, InputStream inputStream, MessageProp messageProp) throws GSSException {
        super(257, krb5Context, inputStream, messageProp);
    }

    public void verify(byte[] bArr, int i2, int i3) throws GSSException {
        if (!verifySignAndSeqNumber(null, bArr, i2, i3, null)) {
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

    public MicToken(Krb5Context krb5Context, MessageProp messageProp, byte[] bArr, int i2, int i3) throws GSSException {
        super(257, krb5Context);
        genSignAndSeqNumber(messageProp == null ? new MessageProp(0, false) : messageProp, null, bArr, i2, i3, null);
    }

    public MicToken(Krb5Context krb5Context, MessageProp messageProp, InputStream inputStream) throws IOException, GSSException {
        super(257, krb5Context);
        byte[] bArr = new byte[inputStream.available()];
        inputStream.read(bArr);
        genSignAndSeqNumber(messageProp == null ? new MessageProp(0, false) : messageProp, null, bArr, 0, bArr.length, null);
    }

    @Override // sun.security.jgss.krb5.MessageToken
    protected int getSealAlg(boolean z2, int i2) {
        return 65535;
    }

    public int encode(byte[] bArr, int i2) throws IOException, GSSException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        super.encode(byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        System.arraycopy(byteArray, 0, bArr, i2, byteArray.length);
        return byteArray.length;
    }

    public byte[] encode() throws IOException, GSSException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(50);
        encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
