package sun.security.rsa;

import java.io.IOException;
import java.security.AlgorithmParametersSpi;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/rsa/PSSParameters.class */
public final class PSSParameters extends AlgorithmParametersSpi {
    private PSSParameterSpec spec;

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (!(algorithmParameterSpec instanceof PSSParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate parameter specification");
        }
        PSSParameterSpec pSSParameterSpec = (PSSParameterSpec) algorithmParameterSpec;
        String mGFAlgorithm = pSSParameterSpec.getMGFAlgorithm();
        if (!pSSParameterSpec.getMGFAlgorithm().equalsIgnoreCase("MGF1")) {
            throw new InvalidParameterSpecException("Unsupported mgf " + mGFAlgorithm + "; MGF1 only");
        }
        if (!(pSSParameterSpec.getMGFParameters() instanceof MGF1ParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate mgf parameters; non-null MGF1ParameterSpec only");
        }
        this.spec = pSSParameterSpec;
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr) throws IOException {
        String name;
        String digestAlgorithm = PSSParameterSpec.DEFAULT.getDigestAlgorithm();
        MGF1ParameterSpec mGF1ParameterSpec = (MGF1ParameterSpec) PSSParameterSpec.DEFAULT.getMGFParameters();
        int saltLength = PSSParameterSpec.DEFAULT.getSaltLength();
        int trailerField = PSSParameterSpec.DEFAULT.getTrailerField();
        for (DerValue derValue : new DerInputStream(bArr).getSequence(4)) {
            if (derValue.isContextSpecific((byte) 0)) {
                digestAlgorithm = AlgorithmId.parse(derValue.data.getDerValue()).getName();
            } else if (derValue.isContextSpecific((byte) 1)) {
                AlgorithmId algorithmId = AlgorithmId.parse(derValue.data.getDerValue());
                if (!algorithmId.getOID().equals(AlgorithmId.mgf1_oid)) {
                    throw new IOException("Only MGF1 mgf is supported");
                }
                byte[] encodedParams = algorithmId.getEncodedParams();
                if (encodedParams == null) {
                    throw new IOException("Missing MGF1 parameters");
                }
                name = AlgorithmId.parse(new DerValue(encodedParams)).getName();
                switch (name) {
                    case "SHA-1":
                        mGF1ParameterSpec = MGF1ParameterSpec.SHA1;
                        break;
                    case "SHA-224":
                        mGF1ParameterSpec = MGF1ParameterSpec.SHA224;
                        break;
                    case "SHA-256":
                        mGF1ParameterSpec = MGF1ParameterSpec.SHA256;
                        break;
                    case "SHA-384":
                        mGF1ParameterSpec = MGF1ParameterSpec.SHA384;
                        break;
                    case "SHA-512":
                        mGF1ParameterSpec = MGF1ParameterSpec.SHA512;
                        break;
                    case "SHA-512/224":
                        mGF1ParameterSpec = MGF1ParameterSpec.SHA512_224;
                        break;
                    case "SHA-512/256":
                        mGF1ParameterSpec = MGF1ParameterSpec.SHA512_256;
                        break;
                    default:
                        throw new IOException("Unrecognized message digest algorithm " + name);
                }
            } else if (derValue.isContextSpecific((byte) 2)) {
                saltLength = derValue.data.getDerValue().getInteger();
                if (saltLength < 0) {
                    throw new IOException("Negative value for saltLength");
                }
            } else if (derValue.isContextSpecific((byte) 3)) {
                trailerField = derValue.data.getDerValue().getInteger();
                if (trailerField != 1) {
                    throw new IOException("Unsupported trailerField value " + trailerField);
                }
            } else {
                throw new IOException("Invalid encoded PSSParameters");
            }
        }
        this.spec = new PSSParameterSpec(digestAlgorithm, "MGF1", mGF1ParameterSpec, saltLength, trailerField);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr, String str) throws IOException {
        if (str != null && !str.equalsIgnoreCase("ASN.1")) {
            throw new IllegalArgumentException("Only support ASN.1 format");
        }
        engineInit(bArr);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected <T extends AlgorithmParameterSpec> T engineGetParameterSpec(Class<T> cls) throws InvalidParameterSpecException {
        if (PSSParameterSpec.class.isAssignableFrom(cls)) {
            return cls.cast(this.spec);
        }
        throw new InvalidParameterSpecException("Inappropriate parameter specification");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded() throws IOException {
        return getEncoded(this.spec);
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded(String str) throws IOException {
        if (str != null && !str.equalsIgnoreCase("ASN.1")) {
            throw new IllegalArgumentException("Only support ASN.1 format");
        }
        return engineGetEncoded();
    }

    @Override // java.security.AlgorithmParametersSpi
    protected String engineToString() {
        return this.spec.toString();
    }

    public static byte[] getEncoded(PSSParameterSpec pSSParameterSpec) throws IOException {
        AlgorithmParameterSpec mGFParameters = pSSParameterSpec.getMGFParameters();
        if (!(mGFParameters instanceof MGF1ParameterSpec)) {
            throw new IOException("Cannot encode " + ((Object) mGFParameters));
        }
        MGF1ParameterSpec mGF1ParameterSpec = (MGF1ParameterSpec) mGFParameters;
        DerOutputStream derOutputStream = new DerOutputStream();
        try {
            AlgorithmId algorithmId = AlgorithmId.get(pSSParameterSpec.getDigestAlgorithm());
            if (!algorithmId.getOID().equals(AlgorithmId.SHA_oid)) {
                DerOutputStream derOutputStream2 = new DerOutputStream();
                algorithmId.derEncode(derOutputStream2);
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
            }
            try {
                AlgorithmId algorithmId2 = AlgorithmId.get(mGF1ParameterSpec.getDigestAlgorithm());
                if (!algorithmId2.getOID().equals(AlgorithmId.SHA_oid)) {
                    DerOutputStream derOutputStream3 = new DerOutputStream();
                    derOutputStream3.putOID(AlgorithmId.mgf1_oid);
                    algorithmId2.encode(derOutputStream3);
                    DerOutputStream derOutputStream4 = new DerOutputStream();
                    derOutputStream4.write((byte) 48, derOutputStream3);
                    derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream4);
                }
                if (pSSParameterSpec.getSaltLength() != 20) {
                    DerOutputStream derOutputStream5 = new DerOutputStream();
                    derOutputStream5.putInteger(pSSParameterSpec.getSaltLength());
                    derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream5);
                }
                if (pSSParameterSpec.getTrailerField() != 1) {
                    DerOutputStream derOutputStream6 = new DerOutputStream();
                    derOutputStream6.putInteger(pSSParameterSpec.getTrailerField());
                    derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 3), derOutputStream6);
                }
                DerOutputStream derOutputStream7 = new DerOutputStream();
                derOutputStream7.write((byte) 48, derOutputStream);
                return derOutputStream7.toByteArray();
            } catch (NoSuchAlgorithmException e2) {
                throw new IOException("AlgorithmId " + mGF1ParameterSpec.getDigestAlgorithm() + " impl not found");
            }
        } catch (NoSuchAlgorithmException e3) {
            throw new IOException("AlgorithmId " + pSSParameterSpec.getDigestAlgorithm() + " impl not found");
        }
    }
}
