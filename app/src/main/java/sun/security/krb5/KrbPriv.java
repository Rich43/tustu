package sun.security.krb5;

import java.io.IOException;
import sun.security.krb5.internal.EncKrbPrivPart;
import sun.security.krb5.internal.HostAddress;
import sun.security.krb5.internal.KRBPriv;
import sun.security.krb5.internal.KdcErrException;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.SeqNumber;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/KrbPriv.class */
class KrbPriv extends KrbAppMessage {
    private byte[] obuf;
    private byte[] userData;

    private KrbPriv(byte[] bArr, Credentials credentials, EncryptionKey encryptionKey, KerberosTime kerberosTime, SeqNumber seqNumber, HostAddress hostAddress, HostAddress hostAddress2) throws IOException, KrbException {
        EncryptionKey encryptionKey2;
        if (encryptionKey != null) {
            encryptionKey2 = encryptionKey;
        } else {
            encryptionKey2 = credentials.key;
        }
        this.obuf = mk_priv(bArr, encryptionKey2, kerberosTime, seqNumber, hostAddress, hostAddress2);
    }

    private KrbPriv(byte[] bArr, Credentials credentials, EncryptionKey encryptionKey, SeqNumber seqNumber, HostAddress hostAddress, HostAddress hostAddress2, boolean z2, boolean z3) throws IOException, KrbException {
        EncryptionKey encryptionKey2;
        KRBPriv kRBPriv = new KRBPriv(bArr);
        if (encryptionKey != null) {
            encryptionKey2 = encryptionKey;
        } else {
            encryptionKey2 = credentials.key;
        }
        this.userData = rd_priv(kRBPriv, encryptionKey2, seqNumber, hostAddress, hostAddress2, z2, z3, credentials.client);
    }

    public byte[] getMessage() throws KrbException {
        return this.obuf;
    }

    public byte[] getData() {
        return this.userData;
    }

    private byte[] mk_priv(byte[] bArr, EncryptionKey encryptionKey, KerberosTime kerberosTime, SeqNumber seqNumber, HostAddress hostAddress, HostAddress hostAddress2) throws KrbCryptoException, KdcErrException, Asn1Exception, IOException {
        Integer num = null;
        Integer num2 = null;
        if (kerberosTime != null) {
            num = new Integer(kerberosTime.getMicroSeconds());
        }
        if (seqNumber != null) {
            num2 = new Integer(seqNumber.current());
            seqNumber.step();
        }
        KRBPriv kRBPriv = new KRBPriv(new EncryptedData(encryptionKey, new EncKrbPrivPart(bArr, kerberosTime, num, num2, hostAddress, hostAddress2).asn1Encode(), 13));
        kRBPriv.asn1Encode();
        return kRBPriv.asn1Encode();
    }

    private byte[] rd_priv(KRBPriv kRBPriv, EncryptionKey encryptionKey, SeqNumber seqNumber, HostAddress hostAddress, HostAddress hostAddress2, boolean z2, boolean z3, PrincipalName principalName) throws KrbCryptoException, KdcErrException, KrbApErrException, Asn1Exception, IOException {
        EncKrbPrivPart encKrbPrivPart = new EncKrbPrivPart(new DerValue(kRBPriv.encPart.reset(kRBPriv.encPart.decrypt(encryptionKey, 13))));
        check(encKrbPrivPart.timestamp, encKrbPrivPart.usec, encKrbPrivPart.seqNumber, encKrbPrivPart.sAddress, encKrbPrivPart.rAddress, seqNumber, hostAddress, hostAddress2, z2, z3, principalName);
        return encKrbPrivPart.userData;
    }
}
