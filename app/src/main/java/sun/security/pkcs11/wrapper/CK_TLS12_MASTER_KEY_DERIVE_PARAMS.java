package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_TLS12_MASTER_KEY_DERIVE_PARAMS.class */
public class CK_TLS12_MASTER_KEY_DERIVE_PARAMS {
    public CK_SSL3_RANDOM_DATA RandomInfo;
    public CK_VERSION pVersion;
    public long prfHashMechanism;

    public CK_TLS12_MASTER_KEY_DERIVE_PARAMS(CK_SSL3_RANDOM_DATA ck_ssl3_random_data, CK_VERSION ck_version, long j2) {
        this.RandomInfo = ck_ssl3_random_data;
        this.pVersion = ck_version;
        this.prfHashMechanism = j2;
    }
}
