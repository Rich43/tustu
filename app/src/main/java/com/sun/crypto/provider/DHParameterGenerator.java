package com.sun.crypto.provider;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameterGeneratorSpi;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import javax.crypto.spec.DHGenParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import sun.security.util.SecurityProviderConstants;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DHParameterGenerator.class */
public final class DHParameterGenerator extends AlgorithmParameterGeneratorSpi {
    private int primeSize = SecurityProviderConstants.DEF_DH_KEY_SIZE;
    private int exponentSize = 0;
    private SecureRandom random = null;

    private static void checkSupport(int i2, int i3) throws InvalidParameterException {
        if (!(i2 == 2048 || i2 == 3072 || (i2 >= 512 && i2 <= 1024 && (i2 & 63) == 0))) {
            throw new InvalidParameterException("Supported DH key size must be multiple of 64 and range from 512 to 1024 (inclusive), or 2048, 3072. The specified key size " + i2 + " is not supported");
        }
        if (i3 != 0) {
            DHKeyPairGenerator.checkKeySize(i2, i3);
        }
    }

    @Override // java.security.AlgorithmParameterGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) throws InvalidParameterException {
        checkSupport(i2, 0);
        this.primeSize = i2;
        this.random = secureRandom;
    }

    @Override // java.security.AlgorithmParameterGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof DHGenParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Inappropriate parameter type");
        }
        DHGenParameterSpec dHGenParameterSpec = (DHGenParameterSpec) algorithmParameterSpec;
        int primeSize = dHGenParameterSpec.getPrimeSize();
        int exponentSize = dHGenParameterSpec.getExponentSize();
        try {
            checkSupport(primeSize, exponentSize);
            this.primeSize = primeSize;
            this.exponentSize = exponentSize;
            this.random = secureRandom;
        } catch (InvalidParameterException e2) {
            throw new InvalidAlgorithmParameterException(e2.getMessage());
        }
    }

    @Override // java.security.AlgorithmParameterGeneratorSpi
    protected AlgorithmParameters engineGenerateParameters() {
        DHParameterSpec dHParameterSpec;
        if (this.exponentSize == 0) {
            this.exponentSize = this.primeSize - 1;
        }
        if (this.random == null) {
            this.random = SunJCE.getRandom();
        }
        try {
            AlgorithmParameterGenerator algorithmParameterGenerator = AlgorithmParameterGenerator.getInstance("DSA");
            algorithmParameterGenerator.init(this.primeSize, this.random);
            DSAParameterSpec dSAParameterSpec = (DSAParameterSpec) algorithmParameterGenerator.generateParameters().getParameterSpec(DSAParameterSpec.class);
            if (this.exponentSize > 0) {
                dHParameterSpec = new DHParameterSpec(dSAParameterSpec.getP(), dSAParameterSpec.getG(), this.exponentSize);
            } else {
                dHParameterSpec = new DHParameterSpec(dSAParameterSpec.getP(), dSAParameterSpec.getG());
            }
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("DH", SunJCE.getInstance());
            algorithmParameters.init(dHParameterSpec);
            return algorithmParameters;
        } catch (Exception e2) {
            throw new ProviderException("Unexpected exception", e2);
        }
    }
}
