package sun.security.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:sun/security/util/ObjectIdentifier.class */
public final class ObjectIdentifier implements Serializable {
    private static final int MAXIMUM_OID_SIZE = 4096;
    private byte[] encoding;
    private volatile transient String stringForm;
    private static final long serialVersionUID = 8697030238860181294L;
    private Object components;
    private int componentLen;
    private transient boolean componentsCalculated;
    private static ConcurrentHashMap<String, ObjectIdentifier> oidTable;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ObjectIdentifier.class.desiredAssertionStatus();
        oidTable = new ConcurrentHashMap<>();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.encoding == null) {
            int[] iArr = (int[]) this.components;
            if (this.componentLen > iArr.length) {
                this.componentLen = iArr.length;
            }
            checkOidSize(this.componentLen);
            init(iArr, this.componentLen);
            return;
        }
        checkOidSize(this.encoding.length);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (!this.componentsCalculated) {
            int[] intArray = toIntArray();
            if (intArray != null) {
                this.components = intArray;
                this.componentLen = intArray.length;
            } else {
                this.components = HugeOidNotSupportedByOldJDK.theOne;
            }
            this.componentsCalculated = true;
        }
        objectOutputStream.defaultWriteObject();
    }

    /* loaded from: rt.jar:sun/security/util/ObjectIdentifier$HugeOidNotSupportedByOldJDK.class */
    static class HugeOidNotSupportedByOldJDK implements Serializable {
        private static final long serialVersionUID = 1;
        static HugeOidNotSupportedByOldJDK theOne = new HugeOidNotSupportedByOldJDK();

        HugeOidNotSupportedByOldJDK() {
        }
    }

    public ObjectIdentifier(String str) throws IOException {
        int iIndexOf;
        String strSubstring;
        int length;
        this.encoding = null;
        this.components = null;
        this.componentLen = -1;
        this.componentsCalculated = false;
        int i2 = 0;
        int iPack7Oid = 0;
        byte[] bArr = new byte[str.length()];
        int iIntValue = 0;
        int i3 = 0;
        do {
            try {
                iIndexOf = str.indexOf(46, i2);
                if (iIndexOf == -1) {
                    strSubstring = str.substring(i2);
                    length = str.length() - i2;
                } else {
                    strSubstring = str.substring(i2, iIndexOf);
                    length = iIndexOf - i2;
                }
                if (length > 9) {
                    BigInteger bigInteger = new BigInteger(strSubstring);
                    if (i3 == 0) {
                        checkFirstComponent(bigInteger);
                        iIntValue = bigInteger.intValue();
                    } else {
                        if (i3 == 1) {
                            checkSecondComponent(iIntValue, bigInteger);
                            bigInteger = bigInteger.add(BigInteger.valueOf(40 * iIntValue));
                        } else {
                            checkOtherComponent(i3, bigInteger);
                        }
                        iPack7Oid += pack7Oid(bigInteger, bArr, iPack7Oid);
                    }
                } else {
                    int i4 = Integer.parseInt(strSubstring);
                    if (i3 == 0) {
                        checkFirstComponent(i4);
                        iIntValue = i4;
                    } else {
                        if (i3 == 1) {
                            checkSecondComponent(iIntValue, i4);
                            i4 += 40 * iIntValue;
                        } else {
                            checkOtherComponent(i3, i4);
                        }
                        iPack7Oid += pack7Oid(i4, bArr, iPack7Oid);
                    }
                }
                i2 = iIndexOf + 1;
                i3++;
                checkOidSize(iPack7Oid);
            } catch (IOException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new IOException("ObjectIdentifier() -- Invalid format: " + e3.toString(), e3);
            }
        } while (iIndexOf != -1);
        checkCount(i3);
        this.encoding = new byte[iPack7Oid];
        System.arraycopy(bArr, 0, this.encoding, 0, iPack7Oid);
        this.stringForm = str;
    }

    public ObjectIdentifier(int[] iArr) throws IOException {
        this.encoding = null;
        this.components = null;
        this.componentLen = -1;
        this.componentsCalculated = false;
        checkCount(iArr.length);
        checkFirstComponent(iArr[0]);
        checkSecondComponent(iArr[0], iArr[1]);
        for (int i2 = 2; i2 < iArr.length; i2++) {
            checkOtherComponent(i2, iArr[i2]);
        }
        init(iArr, iArr.length);
    }

    public ObjectIdentifier(DerInputStream derInputStream) throws IOException {
        this.encoding = null;
        this.components = null;
        this.componentLen = -1;
        this.componentsCalculated = false;
        byte b2 = (byte) derInputStream.getByte();
        if (b2 != 6) {
            throw new IOException("ObjectIdentifier() -- data isn't an object ID (tag = " + ((int) b2) + ")");
        }
        int definiteLength = derInputStream.getDefiniteLength();
        checkOidSize(definiteLength);
        if (definiteLength > derInputStream.available()) {
            throw new IOException("ObjectIdentifier length exceeds data available.  Length: " + definiteLength + ", Available: " + derInputStream.available());
        }
        this.encoding = new byte[definiteLength];
        derInputStream.getBytes(this.encoding);
        check(this.encoding);
    }

    ObjectIdentifier(DerInputBuffer derInputBuffer) throws IOException {
        this.encoding = null;
        this.components = null;
        this.componentLen = -1;
        this.componentsCalculated = false;
        DerInputStream derInputStream = new DerInputStream(derInputBuffer);
        int iAvailable = derInputStream.available();
        checkOidSize(iAvailable);
        this.encoding = new byte[iAvailable];
        derInputStream.getBytes(this.encoding);
        check(this.encoding);
    }

    private void init(int[] iArr, int i2) throws IOException {
        int iPack7Oid;
        byte[] bArr = new byte[(i2 * 5) + 1];
        if (iArr[1] < Integer.MAX_VALUE - (iArr[0] * 40)) {
            iPack7Oid = 0 + pack7Oid((iArr[0] * 40) + iArr[1], bArr, 0);
        } else {
            iPack7Oid = 0 + pack7Oid(BigInteger.valueOf(iArr[1]).add(BigInteger.valueOf(iArr[0] * 40)), bArr, 0);
        }
        for (int i3 = 2; i3 < i2; i3++) {
            iPack7Oid += pack7Oid(iArr[i3], bArr, iPack7Oid);
            checkOidSize(iPack7Oid);
        }
        this.encoding = new byte[iPack7Oid];
        System.arraycopy(bArr, 0, this.encoding, 0, iPack7Oid);
    }

    public static ObjectIdentifier newInternal(int[] iArr) {
        try {
            return new ObjectIdentifier(iArr);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static ObjectIdentifier of(String str) throws IOException {
        ObjectIdentifier objectIdentifier = oidTable.get(str);
        if (objectIdentifier == null) {
            objectIdentifier = new ObjectIdentifier(str);
            oidTable.put(str, objectIdentifier);
        }
        return objectIdentifier;
    }

    public static ObjectIdentifier of(KnownOIDs knownOIDs) {
        String strValue = knownOIDs.value();
        ObjectIdentifier objectIdentifier = oidTable.get(strValue);
        if (objectIdentifier == null) {
            try {
                objectIdentifier = new ObjectIdentifier(strValue);
                oidTable.put(strValue, objectIdentifier);
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        }
        return objectIdentifier;
    }

    void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.write((byte) 6, this.encoding);
    }

    @Deprecated
    public boolean equals(ObjectIdentifier objectIdentifier) {
        return equals((Object) objectIdentifier);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ObjectIdentifier)) {
            return false;
        }
        return Arrays.equals(this.encoding, ((ObjectIdentifier) obj).encoding);
    }

    public int hashCode() {
        return Arrays.hashCode(this.encoding);
    }

    private int[] toIntArray() {
        int length = this.encoding.length;
        int[] iArrCopyOf = new int[20];
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            if ((this.encoding[i4] & 128) == 0) {
                if ((i4 - i3) + 1 > 4) {
                    BigInteger bigInteger = new BigInteger(1, pack(this.encoding, i3, (i4 - i3) + 1, 7, 8));
                    if (i3 == 0) {
                        int i5 = i2;
                        int i6 = i2 + 1;
                        iArrCopyOf[i5] = 2;
                        BigInteger bigIntegerSubtract = bigInteger.subtract(BigInteger.valueOf(80L));
                        if (bigIntegerSubtract.compareTo(BigInteger.valueOf(2147483647L)) == 1) {
                            return null;
                        }
                        i2 = i6 + 1;
                        iArrCopyOf[i6] = bigIntegerSubtract.intValue();
                    } else {
                        if (bigInteger.compareTo(BigInteger.valueOf(2147483647L)) == 1) {
                            return null;
                        }
                        int i7 = i2;
                        i2++;
                        iArrCopyOf[i7] = bigInteger.intValue();
                    }
                } else {
                    int i8 = 0;
                    for (int i9 = i3; i9 <= i4; i9++) {
                        i8 = (i8 << 7) | (this.encoding[i9] & Byte.MAX_VALUE);
                    }
                    if (i3 == 0) {
                        if (i8 < 80) {
                            int i10 = i2;
                            int i11 = i2 + 1;
                            iArrCopyOf[i10] = i8 / 40;
                            i2 = i11 + 1;
                            iArrCopyOf[i11] = i8 % 40;
                        } else {
                            int i12 = i2;
                            int i13 = i2 + 1;
                            iArrCopyOf[i12] = 2;
                            i2 = i13 + 1;
                            iArrCopyOf[i13] = i8 - 80;
                        }
                    } else {
                        int i14 = i2;
                        i2++;
                        iArrCopyOf[i14] = i8;
                    }
                }
                i3 = i4 + 1;
            }
            if (i2 >= iArrCopyOf.length) {
                iArrCopyOf = Arrays.copyOf(iArrCopyOf, i2 + 10);
            }
        }
        return Arrays.copyOf(iArrCopyOf, i2);
    }

    public String toString() {
        String string = this.stringForm;
        if (string == null) {
            int length = this.encoding.length;
            StringBuffer stringBuffer = new StringBuffer(length * 4);
            int i2 = 0;
            for (int i3 = 0; i3 < length; i3++) {
                if ((this.encoding[i3] & 128) == 0) {
                    if (i2 != 0) {
                        stringBuffer.append('.');
                    }
                    if ((i3 - i2) + 1 > 4) {
                        BigInteger bigInteger = new BigInteger(1, pack(this.encoding, i2, (i3 - i2) + 1, 7, 8));
                        if (i2 == 0) {
                            stringBuffer.append("2.");
                            stringBuffer.append((Object) bigInteger.subtract(BigInteger.valueOf(80L)));
                        } else {
                            stringBuffer.append((Object) bigInteger);
                        }
                    } else {
                        int i4 = 0;
                        for (int i5 = i2; i5 <= i3; i5++) {
                            i4 = (i4 << 7) | (this.encoding[i5] & Byte.MAX_VALUE);
                        }
                        if (i2 == 0) {
                            if (i4 < 80) {
                                stringBuffer.append(i4 / 40);
                                stringBuffer.append('.');
                                stringBuffer.append(i4 % 40);
                            } else {
                                stringBuffer.append("2.");
                                stringBuffer.append(i4 - 80);
                            }
                        } else {
                            stringBuffer.append(i4);
                        }
                    }
                    i2 = i3 + 1;
                }
            }
            string = stringBuffer.toString();
            this.stringForm = string;
        }
        return string;
    }

    private static byte[] pack(byte[] bArr, int i2, int i3, int i4, int i5) {
        if (!$assertionsDisabled && (i4 <= 0 || i4 > 8)) {
            throw new AssertionError((Object) "input NUB must be between 1 and 8");
        }
        if (!$assertionsDisabled && (i5 <= 0 || i5 > 8)) {
            throw new AssertionError((Object) "output NUB must be between 1 and 8");
        }
        if (i4 == i5) {
            return (byte[]) bArr.clone();
        }
        int i6 = i3 * i4;
        byte[] bArr2 = new byte[((i6 + i5) - 1) / i5];
        int i7 = 0;
        int i8 = ((((i6 + i5) - 1) / i5) * i5) - i6;
        while (true) {
            int i9 = i8;
            if (i7 < i6) {
                int i10 = i4 - (i7 % i4);
                if (i10 > i5 - (i9 % i5)) {
                    i10 = i5 - (i9 % i5);
                }
                int i11 = i9 / i5;
                bArr2[i11] = (byte) (bArr2[i11] | ((((bArr[i2 + (i7 / i4)] + 256) >> ((i4 - (i7 % i4)) - i10)) & ((1 << i10) - 1)) << ((i5 - (i9 % i5)) - i10)));
                i7 += i10;
                i8 = i9 + i10;
            } else {
                return bArr2;
            }
        }
    }

    private static int pack7Oid(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        byte[] bArrPack = pack(bArr, i2, i3, 8, 7);
        int length = bArrPack.length - 1;
        for (int length2 = bArrPack.length - 2; length2 >= 0; length2--) {
            if (bArrPack[length2] != 0) {
                length = length2;
            }
            int i5 = length2;
            bArrPack[i5] = (byte) (bArrPack[i5] | 128);
        }
        System.arraycopy(bArrPack, length, bArr2, i4, bArrPack.length - length);
        return bArrPack.length - length;
    }

    private static int pack8(byte[] bArr, int i2, int i3, byte[] bArr2, int i4) {
        byte[] bArrPack = pack(bArr, i2, i3, 7, 8);
        int length = bArrPack.length - 1;
        for (int length2 = bArrPack.length - 2; length2 >= 0; length2--) {
            if (bArrPack[length2] != 0) {
                length = length2;
            }
        }
        System.arraycopy(bArrPack, length, bArr2, i4, bArrPack.length - length);
        return bArrPack.length - length;
    }

    private static int pack7Oid(int i2, byte[] bArr, int i3) {
        return pack7Oid(new byte[]{(byte) (i2 >> 24), (byte) (i2 >> 16), (byte) (i2 >> 8), (byte) i2}, 0, 4, bArr, i3);
    }

    private static int pack7Oid(BigInteger bigInteger, byte[] bArr, int i2) {
        byte[] byteArray = bigInteger.toByteArray();
        return pack7Oid(byteArray, 0, byteArray.length, bArr, i2);
    }

    private static void check(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (length < 1 || (bArr[length - 1] & 128) != 0) {
            throw new IOException("ObjectIdentifier() -- Invalid DER encoding, not ended");
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (bArr[i2] == Byte.MIN_VALUE && (i2 == 0 || (bArr[i2 - 1] & 128) == 0)) {
                throw new IOException("ObjectIdentifier() -- Invalid DER encoding, useless extra octet detected");
            }
        }
    }

    private static void checkCount(int i2) throws IOException {
        if (i2 < 2) {
            throw new IOException("ObjectIdentifier() -- Must be at least two oid components ");
        }
    }

    private static void checkFirstComponent(int i2) throws IOException {
        if (i2 < 0 || i2 > 2) {
            throw new IOException("ObjectIdentifier() -- First oid component is invalid ");
        }
    }

    private static void checkFirstComponent(BigInteger bigInteger) throws IOException {
        if (bigInteger.signum() == -1 || bigInteger.compareTo(BigInteger.valueOf(2L)) == 1) {
            throw new IOException("ObjectIdentifier() -- First oid component is invalid ");
        }
    }

    private static void checkSecondComponent(int i2, int i3) throws IOException {
        if (i3 < 0 || (i2 != 2 && i3 > 39)) {
            throw new IOException("ObjectIdentifier() -- Second oid component is invalid ");
        }
    }

    private static void checkSecondComponent(int i2, BigInteger bigInteger) throws IOException {
        if (bigInteger.signum() == -1 || (i2 != 2 && bigInteger.compareTo(BigInteger.valueOf(39L)) == 1)) {
            throw new IOException("ObjectIdentifier() -- Second oid component is invalid ");
        }
    }

    private static void checkOtherComponent(int i2, int i3) throws IOException {
        if (i3 < 0) {
            throw new IOException("ObjectIdentifier() -- oid component #" + (i2 + 1) + " must be non-negative ");
        }
    }

    private static void checkOtherComponent(int i2, BigInteger bigInteger) throws IOException {
        if (bigInteger.signum() == -1) {
            throw new IOException("ObjectIdentifier() -- oid component #" + (i2 + 1) + " must be non-negative ");
        }
    }

    private static void checkOidSize(int i2) throws IOException {
        if (i2 < 0) {
            throw new IOException("ObjectIdentifier encoded length was negative: " + i2);
        }
        if (i2 > 4096) {
            throw new IOException("ObjectIdentifier encoded length exceeds the restriction in JDK (OId length(>=): " + i2 + ", Restriction: 4096)");
        }
    }
}
