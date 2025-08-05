package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/EncTGSRepPart.class */
public class EncTGSRepPart extends EncKDCRepPart {
    public EncTGSRepPart(EncryptionKey encryptionKey, LastReq lastReq, int i2, KerberosTime kerberosTime, TicketFlags ticketFlags, KerberosTime kerberosTime2, KerberosTime kerberosTime3, KerberosTime kerberosTime4, KerberosTime kerberosTime5, PrincipalName principalName, HostAddresses hostAddresses, PAData[] pADataArr) {
        super(encryptionKey, lastReq, i2, kerberosTime, ticketFlags, kerberosTime2, kerberosTime3, kerberosTime4, kerberosTime5, principalName, hostAddresses, pADataArr, 26);
    }

    public EncTGSRepPart(byte[] bArr) throws IOException, KrbException {
        init(new DerValue(bArr));
    }

    public EncTGSRepPart(DerValue derValue) throws IOException, KrbException {
        init(derValue);
    }

    private void init(DerValue derValue) throws IOException, KrbException {
        init(derValue, 26);
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        return asn1Encode(26);
    }
}
