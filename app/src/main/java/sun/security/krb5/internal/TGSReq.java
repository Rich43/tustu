package sun.security.krb5.internal;

import java.io.IOException;
import sun.security.krb5.KrbException;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/TGSReq.class */
public class TGSReq extends KDCReq {
    public TGSReq(PAData[] pADataArr, KDCReqBody kDCReqBody) throws IOException {
        super(pADataArr, kDCReqBody, 12);
    }

    public TGSReq(byte[] bArr) throws IOException, KrbException {
        init(new DerValue(bArr));
    }

    public TGSReq(DerValue derValue) throws IOException, KrbException {
        init(derValue);
    }

    private void init(DerValue derValue) throws IOException, KrbException {
        init(derValue, 12);
    }
}
