package com.sun.crypto.provider;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESedeParameters.class */
public final class DESedeParameters extends AlgorithmParametersSpi {
    private BlockCipherParamsCore core = new BlockCipherParamsCore(8);

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        this.core.init(algorithmParameterSpec);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr) throws IOException {
        this.core.init(bArr);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr, String str) throws IOException {
        this.core.init(bArr, str);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> cls) throws InvalidParameterSpecException {
        if (AlgorithmParameterSpec.class.isAssignableFrom(cls)) {
            return (T) this.core.getParameterSpec(cls);
        }
        throw new InvalidParameterSpecException("Inappropriate parameter Specification");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded() throws IOException {
        return this.core.getEncoded();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded(String str) throws IOException {
        return this.core.getEncoded();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected String engineToString() {
        return this.core.toString();
    }
}
