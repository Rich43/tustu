package sun.security.jgss.krb5;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import org.ietf.jgss.GSSException;
import sun.security.action.GetBooleanAction;
import sun.security.krb5.Credentials;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbApRep;
import sun.security.krb5.KrbApReq;
import sun.security.krb5.KrbException;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/jgss/krb5/AcceptSecContextToken.class */
class AcceptSecContextToken extends InitialToken {
    private KrbApRep apRep;

    public AcceptSecContextToken(Krb5Context krb5Context, KrbApReq krbApReq) throws IOException, GSSException, KrbException {
        this.apRep = null;
        EncryptionKey encryptionKey = null;
        if (((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.security.krb5.acceptor.subkey"))).booleanValue()) {
            encryptionKey = new EncryptionKey(krbApReq.getCreds().getSessionKey());
            krb5Context.setKey(2, encryptionKey);
        }
        this.apRep = new KrbApRep(krbApReq, true, encryptionKey);
        krb5Context.resetMySequenceNumber(this.apRep.getSeqNumber().intValue());
    }

    public AcceptSecContextToken(Krb5Context krb5Context, Credentials credentials, KrbApReq krbApReq, InputStream inputStream) throws IOException, GSSException, KrbException {
        this.apRep = null;
        if (((inputStream.read() << 8) | inputStream.read()) != 512) {
            throw new GSSException(10, -1, "AP_REP token id does not match!");
        }
        KrbApRep krbApRep = new KrbApRep(new DerValue(inputStream).toByteArray(), credentials, krbApReq);
        EncryptionKey subKey = krbApRep.getSubKey();
        if (subKey != null) {
            krb5Context.setKey(2, subKey);
        }
        Integer seqNumber = krbApRep.getSeqNumber();
        krb5Context.resetPeerSequenceNumber(seqNumber != null ? seqNumber.intValue() : 0);
    }

    @Override // sun.security.jgss.krb5.InitialToken
    public final byte[] encode() throws IOException {
        byte[] message = this.apRep.getMessage();
        byte[] bArr = new byte[2 + message.length];
        writeInt(512, bArr, 0);
        System.arraycopy(message, 0, bArr, 2, message.length);
        return bArr;
    }
}
