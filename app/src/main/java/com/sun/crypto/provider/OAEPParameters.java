package com.sun.crypto.provider;

import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParametersSpi;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.xml.datatype.DatatypeConstants;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/OAEPParameters.class */
public final class OAEPParameters extends AlgorithmParametersSpi {
    private String mdName;
    private MGF1ParameterSpec mgfSpec;

    /* renamed from: p, reason: collision with root package name */
    private byte[] f11824p;
    private static ObjectIdentifier OID_MGF1;
    private static ObjectIdentifier OID_PSpecified;

    static {
        try {
            OID_MGF1 = new ObjectIdentifier(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 8});
        } catch (IOException e2) {
            OID_MGF1 = null;
        }
        try {
            OID_PSpecified = new ObjectIdentifier(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 9});
        } catch (IOException e3) {
            OID_PSpecified = null;
        }
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        if (!(algorithmParameterSpec instanceof OAEPParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate parameter specification");
        }
        OAEPParameterSpec oAEPParameterSpec = (OAEPParameterSpec) algorithmParameterSpec;
        this.mdName = oAEPParameterSpec.getDigestAlgorithm();
        String mGFAlgorithm = oAEPParameterSpec.getMGFAlgorithm();
        if (!mGFAlgorithm.equalsIgnoreCase("MGF1")) {
            throw new InvalidParameterSpecException("Unsupported mgf " + mGFAlgorithm + "; MGF1 only");
        }
        AlgorithmParameterSpec mGFParameters = oAEPParameterSpec.getMGFParameters();
        if (!(mGFParameters instanceof MGF1ParameterSpec)) {
            throw new InvalidParameterSpecException("Inappropriate mgf parameters; non-null MGF1ParameterSpec only");
        }
        this.mgfSpec = (MGF1ParameterSpec) mGFParameters;
        PSource pSource = oAEPParameterSpec.getPSource();
        if (pSource.getAlgorithm().equals("PSpecified")) {
            this.f11824p = ((PSource.PSpecified) pSource).getValue();
            return;
        }
        throw new InvalidParameterSpecException("Unsupported pSource " + pSource.getAlgorithm() + "; PSpecified only");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected void engineInit(byte[] bArr) throws IOException {
        DerInputStream derInputStream = new DerInputStream(bArr);
        this.mdName = "SHA-1";
        this.mgfSpec = MGF1ParameterSpec.SHA1;
        this.f11824p = new byte[0];
        for (DerValue derValue : derInputStream.getSequence(3)) {
            if (derValue.isContextSpecific((byte) 0)) {
                this.mdName = AlgorithmId.parse(derValue.data.getDerValue()).getName();
            } else if (derValue.isContextSpecific((byte) 1)) {
                AlgorithmId algorithmId = AlgorithmId.parse(derValue.data.getDerValue());
                if (!algorithmId.getOID().equals((Object) OID_MGF1)) {
                    throw new IOException("Only MGF1 mgf is supported");
                }
                byte[] encodedParams = algorithmId.getEncodedParams();
                if (encodedParams == null) {
                    throw new IOException("Missing MGF1 parameters");
                }
                String name = AlgorithmId.parse(new DerValue(encodedParams)).getName();
                if (name.equals("SHA-1")) {
                    this.mgfSpec = MGF1ParameterSpec.SHA1;
                } else if (name.equals("SHA-224")) {
                    this.mgfSpec = MGF1ParameterSpec.SHA224;
                } else if (name.equals("SHA-256")) {
                    this.mgfSpec = MGF1ParameterSpec.SHA256;
                } else if (name.equals("SHA-384")) {
                    this.mgfSpec = MGF1ParameterSpec.SHA384;
                } else if (name.equals("SHA-512")) {
                    this.mgfSpec = MGF1ParameterSpec.SHA512;
                } else if (name.equals("SHA-512/224")) {
                    this.mgfSpec = MGF1ParameterSpec.SHA512_224;
                } else if (name.equals("SHA-512/256")) {
                    this.mgfSpec = MGF1ParameterSpec.SHA512_256;
                } else {
                    throw new IOException("Unrecognized message digest algorithm");
                }
            } else if (derValue.isContextSpecific((byte) 2)) {
                AlgorithmId algorithmId2 = AlgorithmId.parse(derValue.data.getDerValue());
                if (!algorithmId2.getOID().equals((Object) OID_PSpecified)) {
                    throw new IOException("Wrong OID for pSpecified");
                }
                byte[] encodedParams2 = algorithmId2.getEncodedParams();
                if (encodedParams2 == null) {
                    throw new IOException("Missing pSpecified label");
                }
                DerInputStream derInputStream2 = new DerInputStream(encodedParams2);
                this.f11824p = derInputStream2.getOctetString();
                if (derInputStream2.available() != 0) {
                    throw new IOException("Extra data for pSpecified");
                }
            } else {
                throw new IOException("Invalid encoded OAEPParameters");
            }
        }
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
        if (OAEPParameterSpec.class.isAssignableFrom(cls)) {
            return cls.cast(new OAEPParameterSpec(this.mdName, "MGF1", this.mgfSpec, new PSource.PSpecified(this.f11824p)));
        }
        throw new InvalidParameterSpecException("Inappropriate parameter specification");
    }

    @Override // java.security.AlgorithmParametersSpi
    protected byte[] engineGetEncoded() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        try {
            AlgorithmId algorithmId = AlgorithmId.get(this.mdName);
            DerOutputStream derOutputStream2 = new DerOutputStream();
            algorithmId.derEncode(derOutputStream2);
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream2);
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putOID(OID_MGF1);
            try {
                AlgorithmId.get(this.mgfSpec.getDigestAlgorithm()).encode(derOutputStream3);
                DerOutputStream derOutputStream4 = new DerOutputStream();
                derOutputStream4.write((byte) 48, derOutputStream3);
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream4);
                DerOutputStream derOutputStream5 = new DerOutputStream();
                derOutputStream5.putOID(OID_PSpecified);
                derOutputStream5.putOctetString(this.f11824p);
                DerOutputStream derOutputStream6 = new DerOutputStream();
                derOutputStream6.write((byte) 48, derOutputStream5);
                derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream6);
                DerOutputStream derOutputStream7 = new DerOutputStream();
                derOutputStream7.write((byte) 48, derOutputStream);
                return derOutputStream7.toByteArray();
            } catch (NoSuchAlgorithmException e2) {
                throw new IOException("AlgorithmId " + this.mgfSpec.getDigestAlgorithm() + " impl not found");
            }
        } catch (NoSuchAlgorithmException e3) {
            throw new IOException("AlgorithmId " + this.mdName + " impl not found");
        }
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
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("MD: " + this.mdName + "\n");
        stringBuffer.append("MGF: MGF1" + this.mgfSpec.getDigestAlgorithm() + "\n");
        stringBuffer.append("PSource: PSpecified " + (this.f11824p.length == 0 ? "" : Debug.toHexString(new BigInteger(this.f11824p))) + "\n");
        return stringBuffer.toString();
    }
}
