package sun.security.pkcs11.wrapper;

import java.security.ProviderException;
import sun.util.locale.LanguageTag;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_RSA_PKCS_PSS_PARAMS.class */
public class CK_RSA_PKCS_PSS_PARAMS {
    private final long hashAlg;
    private final long mgf;
    private final long sLen;

    public CK_RSA_PKCS_PSS_PARAMS(String str, String str2, String str3, int i2) {
        this.hashAlg = Functions.getHashMechId(str);
        if (!str2.equals("MGF1")) {
            throw new ProviderException("Only MGF1 is supported");
        }
        this.mgf = Functions.getMGFId("CKG_MGF1_" + str.replaceFirst(LanguageTag.SEP, ""));
        this.sLen = i2;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CK_RSA_PKCS_PSS_PARAMS)) {
            return false;
        }
        CK_RSA_PKCS_PSS_PARAMS ck_rsa_pkcs_pss_params = (CK_RSA_PKCS_PSS_PARAMS) obj;
        return ck_rsa_pkcs_pss_params.hashAlg == this.hashAlg && ck_rsa_pkcs_pss_params.mgf == this.mgf && ck_rsa_pkcs_pss_params.sLen == this.sLen;
    }

    public int hashCode() {
        return (int) ((this.hashAlg << ((int) (2 + this.mgf))) << ((int) (1 + this.sLen)));
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("hashAlg: ");
        stringBuffer.append(Functions.toFullHexString(this.hashAlg));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("mgf: ");
        stringBuffer.append(Functions.toFullHexString(this.mgf));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("sLen(in bytes): ");
        stringBuffer.append(this.sLen);
        return stringBuffer.toString();
    }
}
