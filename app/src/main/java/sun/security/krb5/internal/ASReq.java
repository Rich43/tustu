package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.KrbException;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/ASReq.class */
public class ASReq extends KDCReq {
    public ASReq(PAData[] pADataArr, KDCReqBody kDCReqBody) throws IOException {
        super(pADataArr, kDCReqBody, 10);
    }

    public ASReq(byte[] bArr) throws IOException, KrbException {
        init(new DerValue(bArr));
    }

    public ASReq(DerValue derValue) throws IOException, KrbException {
        init(derValue);
    }

    private void init(DerValue derValue) throws IOException, KrbException {
        super.init(derValue, 10);
    }
}
