package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/TypePath.class */
public class TypePath {
    public static final int ARRAY_ELEMENT = 0;
    public static final int INNER_TYPE = 1;
    public static final int WILDCARD_BOUND = 2;
    public static final int TYPE_ARGUMENT = 3;

    /* renamed from: b, reason: collision with root package name */
    byte[] f12860b;
    int offset;

    TypePath(byte[] bArr, int i2) {
        this.f12860b = bArr;
        this.offset = i2;
    }

    public int getLength() {
        return this.f12860b[this.offset];
    }

    public int getStep(int i2) {
        return this.f12860b[this.offset + (2 * i2) + 1];
    }

    public int getStepArgument(int i2) {
        return this.f12860b[this.offset + (2 * i2) + 2];
    }

    public static TypePath fromString(String str) {
        char cCharAt;
        if (str == null || str.length() == 0) {
            return null;
        }
        int length = str.length();
        ByteVector byteVector = new ByteVector(length);
        byteVector.putByte(0);
        int i2 = 0;
        while (i2 < length) {
            int i3 = i2;
            i2++;
            char cCharAt2 = str.charAt(i3);
            if (cCharAt2 == '[') {
                byteVector.put11(0, 0);
            } else if (cCharAt2 == '.') {
                byteVector.put11(1, 0);
            } else if (cCharAt2 == '*') {
                byteVector.put11(2, 0);
            } else if (cCharAt2 >= '0' && cCharAt2 <= '9') {
                int i4 = cCharAt2 - '0';
                while (i2 < length && (cCharAt = str.charAt(i2)) >= '0' && cCharAt <= '9') {
                    i4 = ((i4 * 10) + cCharAt) - 48;
                    i2++;
                }
                if (i2 < length && str.charAt(i2) == ';') {
                    i2++;
                }
                byteVector.put11(3, i4);
            }
        }
        byteVector.data[0] = (byte) (byteVector.length / 2);
        return new TypePath(byteVector.data, 0);
    }

    public String toString() {
        int length = getLength();
        StringBuilder sb = new StringBuilder(length * 2);
        for (int i2 = 0; i2 < length; i2++) {
            switch (getStep(i2)) {
                case 0:
                    sb.append('[');
                    break;
                case 1:
                    sb.append('.');
                    break;
                case 2:
                    sb.append('*');
                    break;
                case 3:
                    sb.append(getStepArgument(i2)).append(';');
                    break;
                default:
                    sb.append('_');
                    break;
            }
        }
        return sb.toString();
    }
}
