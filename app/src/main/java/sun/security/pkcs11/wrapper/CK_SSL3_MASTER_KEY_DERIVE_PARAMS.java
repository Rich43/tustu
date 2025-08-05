package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_SSL3_MASTER_KEY_DERIVE_PARAMS.class */
public class CK_SSL3_MASTER_KEY_DERIVE_PARAMS {
    public CK_SSL3_RANDOM_DATA RandomInfo;
    public CK_VERSION pVersion;

    public CK_SSL3_MASTER_KEY_DERIVE_PARAMS(CK_SSL3_RANDOM_DATA ck_ssl3_random_data, CK_VERSION ck_version) {
        this.RandomInfo = ck_ssl3_random_data;
        this.pVersion = ck_version;
    }

    public String toString() {
        return Constants.INDENT + "RandomInfo: " + ((Object) this.RandomInfo) + Constants.NEWLINE + Constants.INDENT + "pVersion: " + ((Object) this.pVersion);
    }
}
