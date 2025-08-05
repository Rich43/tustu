package sun.security.krb5.internal.util;

import java.io.IOException;
import java.util.Arrays;
import sun.security.util.BitArray;
import sun.security.util.DerOutputStream;

/* loaded from: rt.jar:sun/security/krb5/internal/util/KerberosFlags.class */
public class KerberosFlags {
    BitArray bits;
    protected static final int BITS_PER_UNIT = 8;

    public KerberosFlags(int i2) throws IllegalArgumentException {
        this.bits = new BitArray(i2);
    }

    public KerberosFlags(int i2, byte[] bArr) throws IllegalArgumentException {
        this.bits = new BitArray(i2, bArr);
        if (i2 != 32) {
            this.bits = new BitArray(Arrays.copyOf(this.bits.toBooleanArray(), 32));
        }
    }

    public KerberosFlags(boolean[] zArr) {
        this.bits = new BitArray(zArr.length == 32 ? zArr : Arrays.copyOf(zArr, 32));
    }

    public void set(int i2, boolean z2) throws ArrayIndexOutOfBoundsException {
        this.bits.set(i2, z2);
    }

    public boolean get(int i2) {
        return this.bits.get(i2);
    }

    public boolean[] toBooleanArray() {
        return this.bits.toBooleanArray();
    }

    public byte[] asn1Encode() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putUnalignedBitString(this.bits);
        return derOutputStream.toByteArray();
    }

    public String toString() {
        return this.bits.toString();
    }
}
