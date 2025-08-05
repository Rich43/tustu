package com.sun.xml.internal.org.jvnet.staxex;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/Base64Encoder.class */
class Base64Encoder {
    private static final char[] encodeMap;
    static final /* synthetic */ boolean $assertionsDisabled;

    Base64Encoder() {
    }

    static {
        $assertionsDisabled = !Base64Encoder.class.desiredAssertionStatus();
        encodeMap = initEncodeMap();
    }

    private static char[] initEncodeMap() {
        char[] map = new char[64];
        for (int i2 = 0; i2 < 26; i2++) {
            map[i2] = (char) (65 + i2);
        }
        for (int i3 = 26; i3 < 52; i3++) {
            map[i3] = (char) (97 + (i3 - 26));
        }
        for (int i4 = 52; i4 < 62; i4++) {
            map[i4] = (char) (48 + (i4 - 52));
        }
        map[62] = '+';
        map[63] = '/';
        return map;
    }

    public static char encode(int i2) {
        return encodeMap[i2 & 63];
    }

    public static byte encodeByte(int i2) {
        return (byte) encodeMap[i2 & 63];
    }

    public static String print(byte[] input, int offset, int len) {
        char[] buf = new char[((len + 2) / 3) * 4];
        int ptr = print(input, offset, len, buf, 0);
        if ($assertionsDisabled || ptr == buf.length) {
            return new String(buf);
        }
        throw new AssertionError();
    }

    public static int print(byte[] input, int offset, int len, char[] buf, int ptr) {
        for (int i2 = offset; i2 < len; i2 += 3) {
            switch (len - i2) {
                case 1:
                    int i3 = ptr;
                    int ptr2 = ptr + 1;
                    buf[i3] = encode(input[i2] >> 2);
                    int ptr3 = ptr2 + 1;
                    buf[ptr2] = encode((input[i2] & 3) << 4);
                    int ptr4 = ptr3 + 1;
                    buf[ptr3] = '=';
                    ptr = ptr4 + 1;
                    buf[ptr4] = '=';
                    break;
                case 2:
                    int i4 = ptr;
                    int ptr5 = ptr + 1;
                    buf[i4] = encode(input[i2] >> 2);
                    int ptr6 = ptr5 + 1;
                    buf[ptr5] = encode(((input[i2] & 3) << 4) | ((input[i2 + 1] >> 4) & 15));
                    int ptr7 = ptr6 + 1;
                    buf[ptr6] = encode((input[i2 + 1] & 15) << 2);
                    ptr = ptr7 + 1;
                    buf[ptr7] = '=';
                    break;
                default:
                    int i5 = ptr;
                    int ptr8 = ptr + 1;
                    buf[i5] = encode(input[i2] >> 2);
                    int ptr9 = ptr8 + 1;
                    buf[ptr8] = encode(((input[i2] & 3) << 4) | ((input[i2 + 1] >> 4) & 15));
                    int ptr10 = ptr9 + 1;
                    buf[ptr9] = encode(((input[i2 + 1] & 15) << 2) | ((input[i2 + 2] >> 6) & 3));
                    ptr = ptr10 + 1;
                    buf[ptr10] = encode(input[i2 + 2] & 63);
                    break;
            }
        }
        return ptr;
    }
}
