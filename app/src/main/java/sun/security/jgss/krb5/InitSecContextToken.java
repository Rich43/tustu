package sun.security.jgss.krb5;

import com.sun.security.jgss.AuthorizationDataEntry;
import java.io.IOException;
import java.io.InputStream;
import org.ietf.jgss.GSSException;
import sun.security.action.GetPropertyAction;
import sun.security.jgss.krb5.InitialToken;
import sun.security.krb5.Checksum;
import sun.security.krb5.Credentials;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbApReq;
import sun.security.krb5.KrbException;
import sun.security.krb5.internal.AuthorizationData;
import sun.security.krb5.internal.KerberosTime;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/jgss/krb5/InitSecContextToken.class */
class InitSecContextToken extends InitialToken {
    private static final boolean ACCEPTOR_USE_INITIATOR_SEQNUM;
    private KrbApReq apReq;

    static {
        String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty("sun.security.krb5.acceptor.sequence.number.nonmutual", "initiator");
        if (strPrivilegedGetProperty.equals("initiator")) {
            ACCEPTOR_USE_INITIATOR_SEQNUM = true;
        } else {
            if (strPrivilegedGetProperty.equals("zero") || strPrivilegedGetProperty.equals("0")) {
                ACCEPTOR_USE_INITIATOR_SEQNUM = false;
                return;
            }
            throw new AssertionError((Object) ("Unrecognized value for sun.security.krb5.acceptor.sequence.number.nonmutual: " + strPrivilegedGetProperty));
        }
    }

    InitSecContextToken(Krb5Context krb5Context, Credentials credentials, Credentials credentials2) throws IOException, GSSException, KrbException {
        this.apReq = null;
        boolean mutualAuthState = krb5Context.getMutualAuthState();
        Checksum checksum = new InitialToken.OverloadedChecksum(krb5Context, credentials, credentials2).getChecksum();
        krb5Context.setTktFlags(credentials2.getFlags());
        krb5Context.setAuthTime(new KerberosTime(credentials2.getAuthTime()).toString());
        this.apReq = new KrbApReq(credentials2, mutualAuthState, true, true, checksum);
        krb5Context.resetMySequenceNumber(this.apReq.getSeqNumber().intValue());
        EncryptionKey subKey = this.apReq.getSubKey();
        if (subKey != null) {
            krb5Context.setKey(1, subKey);
        } else {
            krb5Context.setKey(0, credentials2.getSessionKey());
        }
        if (!mutualAuthState) {
            krb5Context.resetPeerSequenceNumber(ACCEPTOR_USE_INITIATOR_SEQNUM ? this.apReq.getSeqNumber().intValue() : 0);
        }
    }

    InitSecContextToken(Krb5Context krb5Context, Krb5AcceptCredential krb5AcceptCredential, InputStream inputStream) throws IOException, GSSException, KrbException {
        this.apReq = null;
        if (((inputStream.read() << 8) | inputStream.read()) != 256) {
            throw new GSSException(10, -1, "AP_REQ token id does not match!");
        }
        this.apReq = new KrbApReq(new DerValue(inputStream).toByteArray(), krb5AcceptCredential, krb5Context.getChannelBinding() != null ? krb5Context.getChannelBinding().getInitiatorAddress() : null);
        EncryptionKey sessionKey = this.apReq.getCreds().getSessionKey();
        EncryptionKey subKey = this.apReq.getSubKey();
        if (subKey != null) {
            krb5Context.setKey(1, subKey);
        } else {
            krb5Context.setKey(0, sessionKey);
        }
        InitialToken.OverloadedChecksum overloadedChecksum = new InitialToken.OverloadedChecksum(krb5Context, this.apReq.getChecksum(), sessionKey, subKey);
        overloadedChecksum.setContextFlags(krb5Context);
        Credentials delegatedCreds = overloadedChecksum.getDelegatedCreds();
        if (delegatedCreds != null) {
            krb5Context.setDelegCred(Krb5InitCredential.getInstance((Krb5NameElement) krb5Context.getSrcName(), delegatedCreds));
        }
        Integer seqNumber = this.apReq.getSeqNumber();
        int iIntValue = seqNumber != null ? seqNumber.intValue() : 0;
        krb5Context.resetPeerSequenceNumber(iIntValue);
        if (!krb5Context.getMutualAuthState()) {
            krb5Context.resetMySequenceNumber(ACCEPTOR_USE_INITIATOR_SEQNUM ? iIntValue : 0);
        }
        krb5Context.setAuthTime(new KerberosTime(this.apReq.getCreds().getAuthTime()).toString());
        krb5Context.setTktFlags(this.apReq.getCreds().getFlags());
        AuthorizationData authzData = this.apReq.getCreds().getAuthzData();
        if (authzData == null) {
            krb5Context.setAuthzData(null);
            return;
        }
        AuthorizationDataEntry[] authorizationDataEntryArr = new AuthorizationDataEntry[authzData.count()];
        for (int i2 = 0; i2 < authzData.count(); i2++) {
            authorizationDataEntryArr[i2] = new AuthorizationDataEntry(authzData.item(i2).adType, authzData.item(i2).adData);
        }
        krb5Context.setAuthzData(authorizationDataEntryArr);
    }

    public final KrbApReq getKrbApReq() {
        return this.apReq;
    }

    @Override // sun.security.jgss.krb5.InitialToken
    public final byte[] encode() throws IOException {
        byte[] message = this.apReq.getMessage();
        byte[] bArr = new byte[2 + message.length];
        writeInt(256, bArr, 0);
        System.arraycopy(message, 0, bArr, 2, message.length);
        return bArr;
    }
}
