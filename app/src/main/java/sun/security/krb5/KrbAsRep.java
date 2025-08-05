package sun.security.krb5;

import java.io.IOException;
import java.util.Objects;
import javax.security.auth.kerberos.KeyTab;
import sun.security.jgss.krb5.Krb5Util;
import sun.security.krb5.internal.ASRep;
import sun.security.krb5.internal.ASReq;
import sun.security.krb5.internal.EncASRepPart;
import sun.security.krb5.internal.KRBError;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.PAData;
import sun.security.krb5.internal.crypto.EType;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/KrbAsRep.class */
class KrbAsRep extends KrbKdcRep {
    private ASRep rep;
    private Credentials creds;
    private boolean DEBUG = Krb5.DEBUG;

    KrbAsRep(byte[] bArr) throws IOException, KrbException {
        KrbException krbException;
        DerValue derValue = new DerValue(bArr);
        try {
            this.rep = new ASRep(derValue);
        } catch (Asn1Exception e2) {
            this.rep = null;
            KRBError kRBError = new KRBError(derValue);
            String errorString = kRBError.getErrorString();
            String strSubstring = null;
            if (errorString != null && errorString.length() > 0) {
                strSubstring = errorString.charAt(errorString.length() - 1) == 0 ? errorString.substring(0, errorString.length() - 1) : errorString;
            }
            if (strSubstring == null) {
                krbException = new KrbException(kRBError);
            } else {
                if (this.DEBUG) {
                    System.out.println("KRBError received: " + strSubstring);
                }
                krbException = new KrbException(kRBError, strSubstring);
            }
            krbException.initCause(e2);
            throw krbException;
        }
    }

    PAData[] getPA() {
        return this.rep.pAData;
    }

    void decryptUsingKeyTab(KeyTab keyTab, KrbAsReq krbAsReq, PrincipalName principalName) throws IOException, KrbException {
        EncryptionKey encryptionKeyFindKey = null;
        int eType = this.rep.encPart.getEType();
        Integer num = this.rep.encPart.kvno;
        try {
            encryptionKeyFindKey = EncryptionKey.findKey(eType, num, Krb5Util.keysFromJavaxKeyTab(keyTab, principalName));
        } catch (KrbException e2) {
            if (e2.returnCode() == 44) {
                encryptionKeyFindKey = EncryptionKey.findKey(eType, Krb5Util.keysFromJavaxKeyTab(keyTab, principalName));
            }
        }
        if (encryptionKeyFindKey == null) {
            throw new KrbException(400, "Cannot find key for type/kvno to decrypt AS REP - " + EType.toString(eType) + "/" + ((Object) num));
        }
        decrypt(encryptionKeyFindKey, krbAsReq, principalName);
    }

    void decryptUsingPassword(char[] cArr, KrbAsReq krbAsReq, PrincipalName principalName) throws IOException, KrbException {
        int eType = this.rep.encPart.getEType();
        decrypt(EncryptionKey.acquireSecretKey(principalName, cArr, eType, PAData.getSaltAndParams(eType, this.rep.pAData)), krbAsReq, principalName);
    }

    private void decrypt(EncryptionKey encryptionKey, KrbAsReq krbAsReq, PrincipalName principalName) throws IOException, KrbException {
        EncASRepPart encASRepPart = new EncASRepPart(new DerValue(this.rep.encPart.reset(this.rep.encPart.decrypt(encryptionKey, 3))));
        this.rep.encKDCRepPart = encASRepPart;
        ASReq message = krbAsReq.getMessage();
        check(true, message, this.rep, encryptionKey);
        PrincipalName principalName2 = principalName;
        if (principalName2.equals(this.rep.cname)) {
            principalName2 = null;
        }
        this.creds = new Credentials(this.rep.ticket, this.rep.cname, principalName2, encASRepPart.sname, null, encASRepPart.key, encASRepPart.flags, encASRepPart.authtime, encASRepPart.starttime, encASRepPart.endtime, encASRepPart.renewTill, encASRepPart.caddr);
        if (this.DEBUG) {
            System.out.println(">>> KrbAsRep cons in KrbAsReq.getReply " + message.reqBody.cname.getNameString());
        }
    }

    Credentials getCreds() {
        return (Credentials) Objects.requireNonNull(this.creds, "Creds not available yet.");
    }

    sun.security.krb5.internal.ccache.Credentials getCCreds() {
        return new sun.security.krb5.internal.ccache.Credentials(this.rep);
    }
}
