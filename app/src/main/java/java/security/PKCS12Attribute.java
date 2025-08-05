package java.security;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.regex.Pattern;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:java/security/PKCS12Attribute.class */
public final class PKCS12Attribute implements KeyStore.Entry.Attribute {
    private static final Pattern COLON_SEPARATED_HEX_PAIRS = Pattern.compile("^[0-9a-fA-F]{2}(:[0-9a-fA-F]{2})+$");
    private String name;
    private String value;
    private byte[] encoded;
    private int hashValue = -1;

    public PKCS12Attribute(String str, String str2) {
        String[] strArrSplit;
        if (str == null || str2 == null) {
            throw new NullPointerException();
        }
        try {
            ObjectIdentifier objectIdentifier = new ObjectIdentifier(str);
            this.name = str;
            int length = str2.length();
            if (str2.charAt(0) == '[' && str2.charAt(length - 1) == ']') {
                strArrSplit = str2.substring(1, length - 1).split(", ");
            } else {
                strArrSplit = new String[]{str2};
            }
            this.value = str2;
            try {
                this.encoded = encode(objectIdentifier, strArrSplit);
            } catch (IOException e2) {
                throw new IllegalArgumentException("Incorrect format: value", e2);
            }
        } catch (IOException e3) {
            throw new IllegalArgumentException("Incorrect format: name", e3);
        }
    }

    public PKCS12Attribute(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        this.encoded = (byte[]) bArr.clone();
        try {
            parse(bArr);
        } catch (IOException e2) {
            throw new IllegalArgumentException("Incorrect format: encoded", e2);
        }
    }

    @Override // java.security.KeyStore.Entry.Attribute
    public String getName() {
        return this.name;
    }

    @Override // java.security.KeyStore.Entry.Attribute
    public String getValue() {
        return this.value;
    }

    public byte[] getEncoded() {
        return (byte[]) this.encoded.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PKCS12Attribute)) {
            return false;
        }
        return Arrays.equals(this.encoded, ((PKCS12Attribute) obj).getEncoded());
    }

    public int hashCode() {
        if (this.hashValue == -1) {
            Arrays.hashCode(this.encoded);
        }
        return this.hashValue;
    }

    public String toString() {
        return this.name + "=" + this.value;
    }

    private byte[] encode(ObjectIdentifier objectIdentifier, String[] strArr) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putOID(objectIdentifier);
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (String str : strArr) {
            if (COLON_SEPARATED_HEX_PAIRS.matcher(str).matches()) {
                byte[] byteArray = new BigInteger(str.replace(CallSiteDescriptor.TOKEN_DELIMITER, ""), 16).toByteArray();
                if (byteArray[0] == 0) {
                    byteArray = Arrays.copyOfRange(byteArray, 1, byteArray.length);
                }
                derOutputStream2.putOctetString(byteArray);
            } else {
                derOutputStream2.putUTF8String(str);
            }
        }
        derOutputStream.write((byte) 49, derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write((byte) 48, derOutputStream);
        return derOutputStream3.toByteArray();
    }

    private void parse(byte[] bArr) throws IOException {
        DerValue[] sequence = new DerInputStream(bArr).getSequence(2);
        if (sequence.length != 2) {
            throw new IOException("Invalid length for PKCS12Attribute");
        }
        ObjectIdentifier oid = sequence[0].getOID();
        DerValue[] set = new DerInputStream(sequence[1].toByteArray()).getSet(1);
        String[] strArr = new String[set.length];
        for (int i2 = 0; i2 < set.length; i2++) {
            if (set[i2].tag == 4) {
                strArr[i2] = Debug.toString(set[i2].getOctetString());
            } else {
                String asString = set[i2].getAsString();
                if (asString != null) {
                    strArr[i2] = asString;
                } else if (set[i2].tag == 6) {
                    strArr[i2] = set[i2].getOID().toString();
                } else if (set[i2].tag == 24) {
                    strArr[i2] = set[i2].getGeneralizedTime().toString();
                } else if (set[i2].tag == 23) {
                    strArr[i2] = set[i2].getUTCTime().toString();
                } else if (set[i2].tag == 2) {
                    strArr[i2] = set[i2].getBigInteger().toString();
                } else if (set[i2].tag == 1) {
                    strArr[i2] = String.valueOf(set[i2].getBoolean());
                } else {
                    strArr[i2] = Debug.toString(set[i2].getDataBytes());
                }
            }
        }
        this.name = oid.toString();
        this.value = strArr.length == 1 ? strArr[0] : Arrays.toString(strArr);
    }
}
