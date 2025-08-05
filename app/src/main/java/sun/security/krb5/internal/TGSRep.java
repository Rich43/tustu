package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.EncryptedData;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.RealmException;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/TGSRep.class */
public class TGSRep extends KDCRep {
    public TGSRep(PAData[] pADataArr, PrincipalName principalName, Ticket ticket, EncryptedData encryptedData) throws IOException {
        super(pADataArr, principalName, ticket, encryptedData, 13);
    }

    public TGSRep(byte[] bArr) throws Asn1Exception, KrbApErrException, RealmException, IOException {
        init(new DerValue(bArr));
    }

    public TGSRep(DerValue derValue) throws Asn1Exception, KrbApErrException, RealmException, IOException {
        init(derValue);
    }

    private void init(DerValue derValue) throws Asn1Exception, KrbApErrException, RealmException, IOException {
        init(derValue, 13);
    }
}
