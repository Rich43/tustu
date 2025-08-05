package sun.security.krb5;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import sun.security.krb5.internal.ASReq;
import sun.security.krb5.internal.HostAddresses;
import sun.security.krb5.internal.KDCOptions;
import sun.security.krb5.internal.KDCReqBody;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.PAData;
import sun.security.krb5.internal.PAEncTSEnc;
import sun.security.krb5.internal.crypto.Nonce;

/* loaded from: rt.jar:sun/security/krb5/KrbAsReq.class */
public class KrbAsReq {
    private ASReq asReqMessg;
    private boolean DEBUG = Krb5.DEBUG;

    public KrbAsReq(EncryptionKey encryptionKey, KDCOptions kDCOptions, PrincipalName principalName, PrincipalName principalName2, KerberosTime kerberosTime, KerberosTime kerberosTime2, KerberosTime kerberosTime3, int[] iArr, HostAddresses hostAddresses, PAData[] pADataArr) throws IOException, ArrayIndexOutOfBoundsException, KrbException {
        String str;
        kDCOptions = kDCOptions == null ? new KDCOptions() : kDCOptions;
        if (kDCOptions.get(2) || kDCOptions.get(4) || kDCOptions.get(28) || kDCOptions.get(30) || kDCOptions.get(31)) {
            throw new KrbException(101);
        }
        if (!kDCOptions.get(6) && kerberosTime != null) {
            kerberosTime = null;
        }
        PAData[] pADataArr2 = encryptionKey != null ? new PAData[]{new PAData(2, new EncryptedData(encryptionKey, new PAEncTSEnc().asn1Encode(), 1).asn1Encode())} : null;
        if (pADataArr != null && pADataArr.length > 0) {
            if (pADataArr2 == null) {
                pADataArr2 = new PAData[pADataArr.length];
            } else {
                pADataArr2 = (PAData[]) Arrays.copyOf(pADataArr2, pADataArr2.length + pADataArr.length);
            }
            System.arraycopy(pADataArr, 0, pADataArr2, pADataArr2.length - pADataArr.length, pADataArr.length);
        }
        if (principalName.getRealm() == null) {
            throw new RealmException(601, "default realm not specified ");
        }
        if (this.DEBUG) {
            System.out.println(">>> KrbAsReq creating message");
        }
        Config config = Config.getInstance();
        if (hostAddresses == null && config.useAddresses()) {
            hostAddresses = HostAddresses.getLocalAddresses();
        }
        if (principalName2 == null) {
            String realmAsString = principalName.getRealmAsString();
            principalName2 = PrincipalName.tgsService(realmAsString, realmAsString);
        }
        if (kerberosTime2 == null) {
            if (config.get("libdefaults", "ticket_lifetime") != null) {
                kerberosTime2 = new KerberosTime(Instant.now().plusSeconds(Config.duration(r0)));
            } else {
                kerberosTime2 = new KerberosTime(0L);
            }
        }
        if (kerberosTime3 == null && (str = config.get("libdefaults", "renew_lifetime")) != null) {
            kerberosTime3 = new KerberosTime(Instant.now().plusSeconds(Config.duration(str)));
        }
        if (kerberosTime3 != null) {
            kDCOptions.set(8, true);
            if (kerberosTime2.greaterThan(kerberosTime3)) {
                kerberosTime3 = kerberosTime2;
            }
        }
        this.asReqMessg = new ASReq(pADataArr2, new KDCReqBody(kDCOptions, principalName, principalName2, kerberosTime, kerberosTime2, kerberosTime3, Nonce.value(), iArr, hostAddresses, null, null));
    }

    byte[] encoding() throws Asn1Exception, IOException {
        return this.asReqMessg.asn1Encode();
    }

    ASReq getMessage() {
        return this.asReqMessg;
    }
}
