package sun.security.util;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.AlgorithmParametersSpi;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;

/* loaded from: rt.jar:sun/security/util/ECParameters.class */
public final class ECParameters extends AlgorithmParametersSpi {
    private NamedCurve namedCurve;

    public static AlgorithmParameters getAlgorithmParameters(ECParameterSpec eCParameterSpec) throws InvalidKeyException {
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("EC", "SunEC");
            algorithmParameters.init(eCParameterSpec);
            return algorithmParameters;
        } catch (GeneralSecurityException e2) {
            throw new InvalidKeyException("EC parameters error", e2);
        }
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (algorithmParameterSpec == null) {
            throw new InvalidParameterSpecException("paramSpec must not be null");
        }
        if (algorithmParameterSpec instanceof NamedCurve) {
            this.namedCurve = (NamedCurve) algorithmParameterSpec;
            return;
        }
        if (algorithmParameterSpec instanceof ECParameterSpec) {
            this.namedCurve = CurveDB.lookup((ECParameterSpec) algorithmParameterSpec);
        } else if (algorithmParameterSpec instanceof ECGenParameterSpec) {
            this.namedCurve = CurveDB.lookup(((ECGenParameterSpec) algorithmParameterSpec).getName());
        } else if (algorithmParameterSpec instanceof ECKeySizeParameterSpec) {
            this.namedCurve = CurveDB.lookup(((ECKeySizeParameterSpec) algorithmParameterSpec).getKeySize());
        } else {
            throw new InvalidParameterSpecException("Only ECParameterSpec and ECGenParameterSpec supported");
        }
        if (this.namedCurve == null) {
            throw new InvalidParameterSpecException("Not a supported curve: " + ((Object) algorithmParameterSpec));
        }
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr) throws IOException {
        DerValue derValue = new DerValue(bArr);
        if (derValue.tag == 6) {
            ObjectIdentifier oid = derValue.getOID();
            NamedCurve namedCurveLookup = CurveDB.lookup(oid.toString());
            if (namedCurveLookup == null) {
                throw new IOException("Unknown named curve: " + ((Object) oid));
            }
            this.namedCurve = namedCurveLookup;
            return;
        }
        throw new IOException("Only named ECParameters supported");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr, String str) throws IOException {
        engineInit(bArr);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> cls) throws InvalidParameterSpecException {
        if (cls.isAssignableFrom(ECParameterSpec.class)) {
            return cls.cast(this.namedCurve);
        }
        if (cls.isAssignableFrom(ECGenParameterSpec.class)) {
            return cls.cast(new ECGenParameterSpec(this.namedCurve.getObjectId()));
        }
        if (cls.isAssignableFrom(ECKeySizeParameterSpec.class)) {
            return cls.cast(new ECKeySizeParameterSpec(this.namedCurve.getCurve().getField().getFieldSize()));
        }
        throw new InvalidParameterSpecException("Only ECParameterSpec and ECGenParameterSpec supported");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded() throws IOException {
        return this.namedCurve.getEncoded();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded(String str) throws IOException {
        return engineGetEncoded();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected String engineToString() {
        if (this.namedCurve == null) {
            return "Not initialized";
        }
        return this.namedCurve.toString();
    }
}
