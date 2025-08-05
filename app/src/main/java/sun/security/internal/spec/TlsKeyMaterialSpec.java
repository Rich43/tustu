package sun.security.internal.spec;

import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@Deprecated
/* loaded from: jce.jar:sun/security/internal/spec/TlsKeyMaterialSpec.class */
public class TlsKeyMaterialSpec implements KeySpec, SecretKey {
    static final long serialVersionUID = 812912859129525028L;
    private final SecretKey clientMacKey;
    private final SecretKey serverMacKey;
    private final SecretKey clientCipherKey;
    private final SecretKey serverCipherKey;
    private final IvParameterSpec clientIv;
    private final IvParameterSpec serverIv;

    public TlsKeyMaterialSpec(SecretKey secretKey, SecretKey secretKey2) {
        this(secretKey, secretKey2, null, null, null, null);
    }

    public TlsKeyMaterialSpec(SecretKey secretKey, SecretKey secretKey2, SecretKey secretKey3, SecretKey secretKey4) {
        this(secretKey, secretKey2, secretKey3, null, secretKey4, null);
    }

    public TlsKeyMaterialSpec(SecretKey secretKey, SecretKey secretKey2, SecretKey secretKey3, IvParameterSpec ivParameterSpec, SecretKey secretKey4, IvParameterSpec ivParameterSpec2) {
        this.clientMacKey = secretKey;
        this.serverMacKey = secretKey2;
        this.clientCipherKey = secretKey3;
        this.serverCipherKey = secretKey4;
        this.clientIv = ivParameterSpec;
        this.serverIv = ivParameterSpec2;
    }

    @Override // java.security.Key
    public String getAlgorithm() {
        return "TlsKeyMaterial";
    }

    @Override // java.security.Key
    public String getFormat() {
        return null;
    }

    @Override // java.security.Key
    public byte[] getEncoded() {
        return null;
    }

    public SecretKey getClientMacKey() {
        return this.clientMacKey;
    }

    public SecretKey getServerMacKey() {
        return this.serverMacKey;
    }

    public SecretKey getClientCipherKey() {
        return this.clientCipherKey;
    }

    public IvParameterSpec getClientIv() {
        return this.clientIv;
    }

    public SecretKey getServerCipherKey() {
        return this.serverCipherKey;
    }

    public IvParameterSpec getServerIv() {
        return this.serverIv;
    }
}
