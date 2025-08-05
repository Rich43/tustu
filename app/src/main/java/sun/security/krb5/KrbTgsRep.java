package sun.security.krb5;

import java.io.IOException;
import sun.security.krb5.internal.EncTGSRepPart;
import sun.security.krb5.internal.KRBError;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.TGSRep;
import sun.security.krb5.internal.TGSReq;
import sun.security.krb5.internal.Ticket;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/KrbTgsRep.class */
public class KrbTgsRep extends KrbKdcRep {
    private TGSRep rep;
    private Credentials creds;
    private Ticket secondTicket;
    private static final boolean DEBUG = Krb5.DEBUG;

    KrbTgsRep(byte[] bArr, KrbTgsReq krbTgsReq) throws IOException, KrbException {
        KrbException krbException;
        DerValue derValue = new DerValue(bArr);
        TGSReq message = krbTgsReq.getMessage();
        try {
            TGSRep tGSRep = new TGSRep(derValue);
            EncTGSRepPart encTGSRepPart = new EncTGSRepPart(new DerValue(tGSRep.encPart.reset(tGSRep.encPart.decrypt(krbTgsReq.tgsReqKey, krbTgsReq.usedSubkey() ? 9 : 8))));
            tGSRep.encKDCRepPart = encTGSRepPart;
            check(false, message, tGSRep, krbTgsReq.tgsReqKey);
            PrincipalName serverAlias = krbTgsReq.getServerAlias();
            if (serverAlias != null) {
                PrincipalName principalName = encTGSRepPart.sname;
                if (serverAlias.equals(principalName) || isReferralSname(principalName)) {
                    serverAlias = null;
                }
            }
            this.creds = new Credentials(tGSRep.ticket, tGSRep.cname, tGSRep.cname.equals(message.reqBody.cname) ? krbTgsReq.getClientAlias() : null, encTGSRepPart.sname, serverAlias, encTGSRepPart.key, encTGSRepPart.flags, encTGSRepPart.authtime, encTGSRepPart.starttime, encTGSRepPart.endtime, encTGSRepPart.renewTill, encTGSRepPart.caddr);
            this.rep = tGSRep;
            this.secondTicket = krbTgsReq.getSecondTicket();
        } catch (Asn1Exception e2) {
            KRBError kRBError = new KRBError(derValue);
            String errorString = kRBError.getErrorString();
            String strSubstring = null;
            if (errorString != null && errorString.length() > 0) {
                strSubstring = errorString.charAt(errorString.length() - 1) == 0 ? errorString.substring(0, errorString.length() - 1) : errorString;
            }
            if (strSubstring == null) {
                krbException = new KrbException(kRBError.getErrorCode());
            } else {
                krbException = new KrbException(kRBError.getErrorCode(), strSubstring);
            }
            krbException.initCause(e2);
            throw krbException;
        }
    }

    public Credentials getCreds() {
        return this.creds;
    }

    sun.security.krb5.internal.ccache.Credentials setCredentials() {
        return new sun.security.krb5.internal.ccache.Credentials(this.rep, this.secondTicket);
    }

    private static boolean isReferralSname(PrincipalName principalName) {
        if (principalName != null) {
            String[] nameStrings = principalName.getNameStrings();
            if (nameStrings.length == 2 && nameStrings[0].equals(PrincipalName.TGS_DEFAULT_SRV_NAME)) {
                return true;
            }
            return false;
        }
        return false;
    }
}
