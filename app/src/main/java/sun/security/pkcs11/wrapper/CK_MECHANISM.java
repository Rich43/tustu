package sun.security.pkcs11.wrapper;

import java.math.BigInteger;
import sun.security.pkcs11.P11Util;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_MECHANISM.class */
public class CK_MECHANISM {
    public long mechanism;
    public Object pParameter = null;
    private long pHandle = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CK_MECHANISM.class.desiredAssertionStatus();
    }

    public CK_MECHANISM(long j2) {
        this.mechanism = j2;
    }

    public CK_MECHANISM(long j2, byte[] bArr) {
        init(j2, bArr);
    }

    public CK_MECHANISM(long j2, BigInteger bigInteger) {
        init(j2, P11Util.getMagnitude(bigInteger));
    }

    public CK_MECHANISM(long j2, CK_VERSION ck_version) {
        init(j2, ck_version);
    }

    public CK_MECHANISM(long j2, CK_SSL3_MASTER_KEY_DERIVE_PARAMS ck_ssl3_master_key_derive_params) {
        init(j2, ck_ssl3_master_key_derive_params);
    }

    public CK_MECHANISM(long j2, CK_TLS12_MASTER_KEY_DERIVE_PARAMS ck_tls12_master_key_derive_params) {
        init(j2, ck_tls12_master_key_derive_params);
    }

    public CK_MECHANISM(long j2, CK_SSL3_KEY_MAT_PARAMS ck_ssl3_key_mat_params) {
        init(j2, ck_ssl3_key_mat_params);
    }

    public CK_MECHANISM(long j2, CK_TLS12_KEY_MAT_PARAMS ck_tls12_key_mat_params) {
        init(j2, ck_tls12_key_mat_params);
    }

    public CK_MECHANISM(long j2, CK_TLS_PRF_PARAMS ck_tls_prf_params) {
        init(j2, ck_tls_prf_params);
    }

    public CK_MECHANISM(long j2, CK_TLS_MAC_PARAMS ck_tls_mac_params) {
        init(j2, ck_tls_mac_params);
    }

    public CK_MECHANISM(long j2, CK_ECDH1_DERIVE_PARAMS ck_ecdh1_derive_params) {
        init(j2, ck_ecdh1_derive_params);
    }

    public CK_MECHANISM(long j2, Long l2) {
        init(j2, l2);
    }

    public CK_MECHANISM(long j2, CK_AES_CTR_PARAMS ck_aes_ctr_params) {
        init(j2, ck_aes_ctr_params);
    }

    public CK_MECHANISM(long j2, CK_GCM_PARAMS ck_gcm_params) {
        init(j2, ck_gcm_params);
    }

    public CK_MECHANISM(long j2, CK_CCM_PARAMS ck_ccm_params) {
        init(j2, ck_ccm_params);
    }

    public void setParameter(CK_RSA_PKCS_PSS_PARAMS ck_rsa_pkcs_pss_params) {
        if (!$assertionsDisabled && this.mechanism != 13) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && ck_rsa_pkcs_pss_params == null) {
            throw new AssertionError();
        }
        if (this.pParameter != null && this.pParameter.equals(ck_rsa_pkcs_pss_params)) {
            return;
        }
        freeHandle();
        this.pParameter = ck_rsa_pkcs_pss_params;
    }

    public void freeHandle() {
        if (this.pHandle != 0) {
            this.pHandle = PKCS11.freeMechanism(this.pHandle);
        }
    }

    private void init(long j2, Object obj) {
        this.mechanism = j2;
        this.pParameter = obj;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("mechanism: ");
        stringBuffer.append(this.mechanism);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pParameter: ");
        stringBuffer.append(this.pParameter.toString());
        stringBuffer.append(Constants.NEWLINE);
        if (this.pHandle != 0) {
            stringBuffer.append(Constants.INDENT);
            stringBuffer.append("pHandle: ");
            stringBuffer.append(this.pHandle);
            stringBuffer.append(Constants.NEWLINE);
        }
        return stringBuffer.toString();
    }
}
