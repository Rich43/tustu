package sun.security.x509;

@Deprecated
/* loaded from: rt.jar:sun/security/x509/CertException.class */
public class CertException extends SecurityException {
    private static final long serialVersionUID = 6930793039696446142L;
    public static final int verf_INVALID_SIG = 1;
    public static final int verf_INVALID_REVOKED = 2;
    public static final int verf_INVALID_NOTBEFORE = 3;
    public static final int verf_INVALID_EXPIRED = 4;
    public static final int verf_CA_UNTRUSTED = 5;
    public static final int verf_CHAIN_LENGTH = 6;
    public static final int verf_PARSE_ERROR = 7;
    public static final int err_CONSTRUCTION = 8;
    public static final int err_INVALID_PUBLIC_KEY = 9;
    public static final int err_INVALID_VERSION = 10;
    public static final int err_INVALID_FORMAT = 11;
    public static final int err_ENCODING = 12;
    private int verfCode;
    private String moreData;

    public CertException(int i2, String str) {
        this.verfCode = i2;
        this.moreData = str;
    }

    public CertException(int i2) {
        this.verfCode = i2;
    }

    public int getVerfCode() {
        return this.verfCode;
    }

    public String getMoreData() {
        return this.moreData;
    }

    public String getVerfDescription() {
        switch (this.verfCode) {
            case 1:
                return "The signature in the certificate is not valid.";
            case 2:
                return "The certificate has been revoked.";
            case 3:
                return "The certificate is not yet valid.";
            case 4:
                return "The certificate has expired.";
            case 5:
                return "The Authority which issued the certificate is not trusted.";
            case 6:
                return "The certificate path to a trusted authority is too long.";
            case 7:
                return "The certificate could not be parsed.";
            case 8:
                return "There was an error when constructing the certificate.";
            case 9:
                return "The public key was not in the correct format.";
            case 10:
                return "The certificate has an invalid version number.";
            case 11:
                return "The certificate has an invalid format.";
            case 12:
                return "Problem encountered while encoding the data.";
            default:
                return "Unknown code:  " + this.verfCode;
        }
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "[Certificate Exception: " + getMessage() + "]";
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return getVerfDescription() + (this.moreData != null ? "\n  (" + this.moreData + ")" : "");
    }
}
