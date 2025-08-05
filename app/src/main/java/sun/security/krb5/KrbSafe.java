package sun.security.krb5;

import java.io.IOException;
import sun.security.krb5.internal.HostAddress;
import sun.security.krb5.internal.KRBSafe;
import sun.security.krb5.internal.KRBSafeBody;
import sun.security.krb5.internal.KdcErrException;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.KrbApErrException;
import sun.security.krb5.internal.SeqNumber;

/* loaded from: rt.jar:sun/security/krb5/KrbSafe.class */
class KrbSafe extends KrbAppMessage {
    private byte[] obuf;
    private byte[] userData;

    public KrbSafe(byte[] bArr, Credentials credentials, EncryptionKey encryptionKey, KerberosTime kerberosTime, SeqNumber seqNumber, HostAddress hostAddress, HostAddress hostAddress2) throws IOException, KrbException {
        EncryptionKey encryptionKey2;
        if (encryptionKey != null) {
            encryptionKey2 = encryptionKey;
        } else {
            encryptionKey2 = credentials.key;
        }
        this.obuf = mk_safe(bArr, encryptionKey2, kerberosTime, seqNumber, hostAddress, hostAddress2);
    }

    public KrbSafe(byte[] bArr, Credentials credentials, EncryptionKey encryptionKey, SeqNumber seqNumber, HostAddress hostAddress, HostAddress hostAddress2, boolean z2, boolean z3) throws IOException, KrbException {
        EncryptionKey encryptionKey2;
        KRBSafe kRBSafe = new KRBSafe(bArr);
        if (encryptionKey != null) {
            encryptionKey2 = encryptionKey;
        } else {
            encryptionKey2 = credentials.key;
        }
        this.userData = rd_safe(kRBSafe, encryptionKey2, seqNumber, hostAddress, hostAddress2, z2, z3, credentials.client);
    }

    public byte[] getMessage() {
        return this.obuf;
    }

    public byte[] getData() {
        return this.userData;
    }

    private byte[] mk_safe(byte[] bArr, EncryptionKey encryptionKey, KerberosTime kerberosTime, SeqNumber seqNumber, HostAddress hostAddress, HostAddress hostAddress2) throws KrbCryptoException, KdcErrException, Asn1Exception, KrbApErrException, IOException {
        Integer num = null;
        Integer num2 = null;
        if (kerberosTime != null) {
            num = new Integer(kerberosTime.getMicroSeconds());
        }
        if (seqNumber != null) {
            num2 = new Integer(seqNumber.current());
            seqNumber.step();
        }
        KRBSafeBody kRBSafeBody = new KRBSafeBody(bArr, kerberosTime, num, num2, hostAddress, hostAddress2);
        KRBSafe kRBSafe = new KRBSafe(kRBSafeBody, new Checksum(Checksum.SAFECKSUMTYPE_DEFAULT, kRBSafeBody.asn1Encode(), encryptionKey, 15));
        kRBSafe.asn1Encode();
        return kRBSafe.asn1Encode();
    }

    private byte[] rd_safe(KRBSafe kRBSafe, EncryptionKey encryptionKey, SeqNumber seqNumber, HostAddress hostAddress, HostAddress hostAddress2, boolean z2, boolean z3, PrincipalName principalName) throws KrbCryptoException, KdcErrException, Asn1Exception, KrbApErrException, IOException {
        if (!kRBSafe.cksum.verifyKeyedChecksum(kRBSafe.safeBody.asn1Encode(), encryptionKey, 15)) {
            throw new KrbApErrException(41);
        }
        check(kRBSafe.safeBody.timestamp, kRBSafe.safeBody.usec, kRBSafe.safeBody.seqNumber, kRBSafe.safeBody.sAddress, kRBSafe.safeBody.rAddress, seqNumber, hostAddress, hostAddress2, z2, z3, principalName);
        return kRBSafe.safeBody.userData;
    }
}
