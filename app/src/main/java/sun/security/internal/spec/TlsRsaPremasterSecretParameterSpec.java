package sun.security.internal.spec;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.spec.AlgorithmParameterSpec;

@Deprecated
/* loaded from: jce.jar:sun/security/internal/spec/TlsRsaPremasterSecretParameterSpec.class */
public class TlsRsaPremasterSecretParameterSpec implements AlgorithmParameterSpec {
    private final byte[] encodedSecret;
    private static final String PROP_NAME = "com.sun.net.ssl.rsaPreMasterSecretFix";
    private static final boolean rsaPreMasterSecretFix = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Boolean run() {
            String property = System.getProperty(TlsRsaPremasterSecretParameterSpec.PROP_NAME);
            if (property != null && property.equalsIgnoreCase("true")) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
    })).booleanValue();
    private final int clientVersion;
    private final int serverVersion;

    public TlsRsaPremasterSecretParameterSpec(int i2, int i3) {
        this.clientVersion = checkVersion(i2);
        this.serverVersion = checkVersion(i3);
        this.encodedSecret = null;
    }

    public TlsRsaPremasterSecretParameterSpec(int i2, int i3, byte[] bArr) {
        this.clientVersion = checkVersion(i2);
        this.serverVersion = checkVersion(i3);
        if (bArr == null || bArr.length != 48) {
            throw new IllegalArgumentException("Encoded secret is not exactly 48 bytes");
        }
        this.encodedSecret = (byte[]) bArr.clone();
    }

    public int getClientVersion() {
        return this.clientVersion;
    }

    public int getServerVersion() {
        return this.serverVersion;
    }

    public int getMajorVersion() {
        if (rsaPreMasterSecretFix || this.clientVersion >= 770) {
            return (this.clientVersion >>> 8) & 255;
        }
        return (this.serverVersion >>> 8) & 255;
    }

    public int getMinorVersion() {
        if (rsaPreMasterSecretFix || this.clientVersion >= 770) {
            return this.clientVersion & 255;
        }
        return this.serverVersion & 255;
    }

    private int checkVersion(int i2) {
        if (i2 < 0 || i2 > 65535) {
            throw new IllegalArgumentException("Version must be between 0 and 65,535");
        }
        return i2;
    }

    public byte[] getEncodedSecret() {
        if (this.encodedSecret == null) {
            return null;
        }
        return (byte[]) this.encodedSecret.clone();
    }
}
