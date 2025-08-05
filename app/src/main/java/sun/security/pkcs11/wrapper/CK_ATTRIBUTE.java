package sun.security.pkcs11.wrapper;

import java.math.BigInteger;
import sun.security.pkcs11.P11Util;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_ATTRIBUTE.class */
public class CK_ATTRIBUTE {
    public static final CK_ATTRIBUTE TOKEN_FALSE = new CK_ATTRIBUTE(1L, false);
    public static final CK_ATTRIBUTE SENSITIVE_FALSE = new CK_ATTRIBUTE(259L, false);
    public static final CK_ATTRIBUTE EXTRACTABLE_TRUE = new CK_ATTRIBUTE(354L, true);
    public static final CK_ATTRIBUTE ENCRYPT_TRUE = new CK_ATTRIBUTE(260L, true);
    public static final CK_ATTRIBUTE DECRYPT_TRUE = new CK_ATTRIBUTE(261L, true);
    public static final CK_ATTRIBUTE WRAP_TRUE = new CK_ATTRIBUTE(262L, true);
    public static final CK_ATTRIBUTE UNWRAP_TRUE = new CK_ATTRIBUTE(263L, true);
    public static final CK_ATTRIBUTE SIGN_TRUE = new CK_ATTRIBUTE(264L, true);
    public static final CK_ATTRIBUTE VERIFY_TRUE = new CK_ATTRIBUTE(266L, true);
    public static final CK_ATTRIBUTE SIGN_RECOVER_TRUE = new CK_ATTRIBUTE(265L, true);
    public static final CK_ATTRIBUTE VERIFY_RECOVER_TRUE = new CK_ATTRIBUTE(267L, true);
    public static final CK_ATTRIBUTE DERIVE_TRUE = new CK_ATTRIBUTE(268L, true);
    public static final CK_ATTRIBUTE ENCRYPT_NULL = new CK_ATTRIBUTE(260);
    public static final CK_ATTRIBUTE DECRYPT_NULL = new CK_ATTRIBUTE(261);
    public static final CK_ATTRIBUTE WRAP_NULL = new CK_ATTRIBUTE(262);
    public static final CK_ATTRIBUTE UNWRAP_NULL = new CK_ATTRIBUTE(263);
    public long type;
    public Object pValue;

    public CK_ATTRIBUTE() {
    }

    public CK_ATTRIBUTE(long j2) {
        this.type = j2;
    }

    public CK_ATTRIBUTE(long j2, Object obj) {
        this.type = j2;
        this.pValue = obj;
    }

    public CK_ATTRIBUTE(long j2, boolean z2) {
        this.type = j2;
        this.pValue = Boolean.valueOf(z2);
    }

    public CK_ATTRIBUTE(long j2, long j3) {
        this.type = j2;
        this.pValue = Long.valueOf(j3);
    }

    public CK_ATTRIBUTE(long j2, BigInteger bigInteger) {
        this.type = j2;
        this.pValue = P11Util.getMagnitude(bigInteger);
    }

    public BigInteger getBigInteger() {
        if (!(this.pValue instanceof byte[])) {
            throw new RuntimeException("Not a byte[]");
        }
        return new BigInteger(1, (byte[]) this.pValue);
    }

    public boolean getBoolean() {
        if (!(this.pValue instanceof Boolean)) {
            throw new RuntimeException("Not a Boolean: " + this.pValue.getClass().getName());
        }
        return ((Boolean) this.pValue).booleanValue();
    }

    public char[] getCharArray() {
        if (!(this.pValue instanceof char[])) {
            throw new RuntimeException("Not a char[]");
        }
        return (char[]) this.pValue;
    }

    public byte[] getByteArray() {
        if (!(this.pValue instanceof byte[])) {
            throw new RuntimeException("Not a byte[]");
        }
        return (byte[]) this.pValue;
    }

    public long getLong() {
        if (!(this.pValue instanceof Long)) {
            throw new RuntimeException("Not a Long: " + this.pValue.getClass().getName());
        }
        return ((Long) this.pValue).longValue();
    }

    public String toString() {
        String strValueOf;
        String str = Functions.getAttributeName(this.type) + " = ";
        if (this.type == 0) {
            return str + Functions.getObjectClassName(getLong());
        }
        if (this.type == 256) {
            return str + Functions.getKeyName(getLong());
        }
        if (this.pValue instanceof char[]) {
            strValueOf = new String((char[]) this.pValue);
        } else if (this.pValue instanceof byte[]) {
            strValueOf = Functions.toHexString((byte[]) this.pValue);
        } else {
            strValueOf = String.valueOf(this.pValue);
        }
        return str + strValueOf;
    }
}
