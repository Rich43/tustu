package com.sun.xml.internal.ws.org.objectweb.asm;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/ClassReader.class */
public class ClassReader {
    static final boolean SIGNATURES = true;
    static final boolean ANNOTATIONS = true;
    static final boolean FRAMES = true;
    static final boolean WRITER = true;
    static final boolean RESIZE = true;
    public static final int SKIP_CODE = 1;
    public static final int SKIP_DEBUG = 2;
    public static final int SKIP_FRAMES = 4;
    public static final int EXPAND_FRAMES = 8;

    /* renamed from: b, reason: collision with root package name */
    public final byte[] f12090b;
    private final int[] items;
    private final String[] strings;
    private final int maxStringLength;
    public final int header;

    public ClassReader(byte[] b2) {
        this(b2, 0, b2.length);
    }

    public ClassReader(byte[] b2, int off, int len) {
        int size;
        this.f12090b = b2;
        this.items = new int[readUnsignedShort(off + 8)];
        int n2 = this.items.length;
        this.strings = new String[n2];
        int max = 0;
        int index = off + 10;
        int i2 = 1;
        while (i2 < n2) {
            this.items[i2] = index + 1;
            switch (b2[index]) {
                case 1:
                    size = 3 + readUnsignedShort(index + 1);
                    if (size <= max) {
                        break;
                    } else {
                        max = size;
                        break;
                    }
                case 2:
                case 7:
                case 8:
                default:
                    size = 3;
                    break;
                case 3:
                case 4:
                case 9:
                case 10:
                case 11:
                case 12:
                    size = 5;
                    break;
                case 5:
                case 6:
                    size = 9;
                    i2++;
                    break;
            }
            index += size;
            i2++;
        }
        this.maxStringLength = max;
        this.header = index;
    }

    public int getAccess() {
        return readUnsignedShort(this.header);
    }

    public String getClassName() {
        return readClass(this.header + 2, new char[this.maxStringLength]);
    }

    public String getSuperName() {
        int n2 = this.items[readUnsignedShort(this.header + 4)];
        if (n2 == 0) {
            return null;
        }
        return readUTF8(n2, new char[this.maxStringLength]);
    }

    public String[] getInterfaces() {
        int index = this.header + 6;
        int n2 = readUnsignedShort(index);
        String[] interfaces = new String[n2];
        if (n2 > 0) {
            char[] buf = new char[this.maxStringLength];
            for (int i2 = 0; i2 < n2; i2++) {
                index += 2;
                interfaces[i2] = readClass(index, buf);
            }
        }
        return interfaces;
    }

    void copyPool(ClassWriter classWriter) {
        char[] buf = new char[this.maxStringLength];
        int ll = this.items.length;
        Item[] items2 = new Item[ll];
        int i2 = 1;
        while (i2 < ll) {
            int index = this.items[i2];
            byte b2 = this.f12090b[index - 1];
            Item item = new Item(i2);
            switch (b2) {
                case 1:
                    String s2 = this.strings[i2];
                    if (s2 == null) {
                        int index2 = this.items[i2];
                        String utf = readUTF(index2 + 2, readUnsignedShort(index2), buf);
                        this.strings[i2] = utf;
                        s2 = utf;
                    }
                    item.set(b2, s2, null, null);
                    break;
                case 2:
                case 7:
                case 8:
                default:
                    item.set(b2, readUTF8(index, buf), null, null);
                    break;
                case 3:
                    item.set(readInt(index));
                    break;
                case 4:
                    item.set(Float.intBitsToFloat(readInt(index)));
                    break;
                case 5:
                    item.set(readLong(index));
                    i2++;
                    break;
                case 6:
                    item.set(Double.longBitsToDouble(readLong(index)));
                    i2++;
                    break;
                case 9:
                case 10:
                case 11:
                    int nameType = this.items[readUnsignedShort(index + 2)];
                    item.set(b2, readClass(index, buf), readUTF8(nameType, buf), readUTF8(nameType + 2, buf));
                    break;
                case 12:
                    item.set(b2, readUTF8(index, buf), readUTF8(index + 2, buf), null);
                    break;
            }
            int index22 = item.hashCode % items2.length;
            item.next = items2[index22];
            items2[index22] = item;
            i2++;
        }
        int off = this.items[1] - 1;
        classWriter.pool.putByteArray(this.f12090b, off, this.header - off);
        classWriter.items = items2;
        classWriter.threshold = (int) (0.75d * ll);
        classWriter.index = ll;
    }

    public ClassReader(InputStream is) throws IOException {
        this(readClass(is));
    }

    public ClassReader(String name) throws IOException {
        this(ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class"));
    }

    private static byte[] readClass(InputStream is) throws IOException {
        if (is == null) {
            throw new IOException("Class not found");
        }
        byte[] b2 = new byte[is.available()];
        int len = 0;
        while (true) {
            int n2 = is.read(b2, len, b2.length - len);
            if (n2 == -1) {
                break;
            }
            len += n2;
            if (len == b2.length) {
                byte[] c2 = new byte[b2.length + 1000];
                System.arraycopy(b2, 0, c2, 0, len);
                b2 = c2;
            }
        }
        if (len < b2.length) {
            byte[] c3 = new byte[len];
            System.arraycopy(b2, 0, c3, 0, len);
            b2 = c3;
        }
        return b2;
    }

    public void accept(ClassVisitor classVisitor, int flags) {
        accept(classVisitor, new Attribute[0], flags);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:389:0x0d4b. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:296:0x0912  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x0a95  */
    /* JADX WARN: Removed duplicated region for block: B:329:0x0b3e  */
    /* JADX WARN: Removed duplicated region for block: B:379:0x0cf0  */
    /* JADX WARN: Removed duplicated region for block: B:414:0x0e82  */
    /* JADX WARN: Removed duplicated region for block: B:522:0x1418  */
    /* JADX WARN: Removed duplicated region for block: B:529:0x1433  */
    /* JADX WARN: Removed duplicated region for block: B:536:0x1493  */
    /* JADX WARN: Removed duplicated region for block: B:550:0x152c A[LOOP:43: B:548:0x1527->B:550:0x152c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:554:0x1559  */
    /* JADX WARN: Removed duplicated region for block: B:606:0x1560 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void accept(com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor r10, com.sun.xml.internal.ws.org.objectweb.asm.Attribute[] r11, int r12) {
        /*
            Method dump skipped, instructions count: 5485
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.org.objectweb.asm.ClassReader.accept(com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor, com.sun.xml.internal.ws.org.objectweb.asm.Attribute[], int):void");
    }

    private void readParameterAnnotations(int v2, String desc, char[] buf, boolean visible, MethodVisitor mv) {
        int v3 = v2 + 1;
        int n2 = this.f12090b[v2] & 255;
        int synthetics = Type.getArgumentTypes(desc).length - n2;
        int i2 = 0;
        while (i2 < synthetics) {
            AnnotationVisitor av2 = mv.visitParameterAnnotation(i2, "Ljava/lang/Synthetic;", false);
            if (av2 != null) {
                av2.visitEnd();
            }
            i2++;
        }
        while (i2 < n2 + synthetics) {
            v3 += 2;
            for (int j2 = readUnsignedShort(v3); j2 > 0; j2--) {
                v3 = readAnnotationValues(v3 + 2, buf, true, mv.visitParameterAnnotation(i2, readUTF8(v3, buf), visible));
            }
            i2++;
        }
    }

    private int readAnnotationValues(int v2, char[] buf, boolean named, AnnotationVisitor av2) {
        int i2 = readUnsignedShort(v2);
        int v3 = v2 + 2;
        if (named) {
            while (i2 > 0) {
                v3 = readAnnotationValue(v3 + 2, buf, readUTF8(v3, buf), av2);
                i2--;
            }
        } else {
            while (i2 > 0) {
                v3 = readAnnotationValue(v3, buf, null, av2);
                i2--;
            }
        }
        if (av2 != null) {
            av2.visitEnd();
        }
        return v3;
    }

    private int readAnnotationValue(int v2, char[] buf, String name, AnnotationVisitor av2) {
        if (av2 == null) {
            switch (this.f12090b[v2] & 255) {
                case 64:
                    return readAnnotationValues(v2 + 3, buf, true, null);
                case 91:
                    return readAnnotationValues(v2 + 1, buf, false, null);
                case 101:
                    return v2 + 5;
                default:
                    return v2 + 3;
            }
        }
        int v3 = v2 + 1;
        switch (this.f12090b[v2] & 255) {
            case 64:
                v3 = readAnnotationValues(v3 + 2, buf, true, av2.visitAnnotation(name, readUTF8(v3, buf)));
                break;
            case 66:
                av2.visit(name, new Byte((byte) readInt(this.items[readUnsignedShort(v3)])));
                v3 += 2;
                break;
            case 67:
                av2.visit(name, new Character((char) readInt(this.items[readUnsignedShort(v3)])));
                v3 += 2;
                break;
            case 68:
            case 70:
            case 73:
            case 74:
                av2.visit(name, readConst(readUnsignedShort(v3), buf));
                v3 += 2;
                break;
            case 83:
                av2.visit(name, new Short((short) readInt(this.items[readUnsignedShort(v3)])));
                v3 += 2;
                break;
            case 90:
                av2.visit(name, readInt(this.items[readUnsignedShort(v3)]) == 0 ? Boolean.FALSE : Boolean.TRUE);
                v3 += 2;
                break;
            case 91:
                int size = readUnsignedShort(v3);
                int v4 = v3 + 2;
                if (size == 0) {
                    return readAnnotationValues(v4 - 2, buf, false, av2.visitArray(name));
                }
                int v5 = v4 + 1;
                switch (this.f12090b[v4] & 255) {
                    case 66:
                        byte[] bv2 = new byte[size];
                        for (int i2 = 0; i2 < size; i2++) {
                            bv2[i2] = (byte) readInt(this.items[readUnsignedShort(v5)]);
                            v5 += 3;
                        }
                        av2.visit(name, bv2);
                        v3 = v5 - 1;
                        break;
                    case 67:
                        char[] cv = new char[size];
                        for (int i3 = 0; i3 < size; i3++) {
                            cv[i3] = (char) readInt(this.items[readUnsignedShort(v5)]);
                            v5 += 3;
                        }
                        av2.visit(name, cv);
                        v3 = v5 - 1;
                        break;
                    case 68:
                        double[] dv = new double[size];
                        for (int i4 = 0; i4 < size; i4++) {
                            dv[i4] = Double.longBitsToDouble(readLong(this.items[readUnsignedShort(v5)]));
                            v5 += 3;
                        }
                        av2.visit(name, dv);
                        v3 = v5 - 1;
                        break;
                    case 69:
                    case 71:
                    case 72:
                    case 75:
                    case 76:
                    case 77:
                    case 78:
                    case 79:
                    case 80:
                    case 81:
                    case 82:
                    case 84:
                    case 85:
                    case 86:
                    case 87:
                    case 88:
                    case 89:
                    default:
                        v3 = readAnnotationValues(v5 - 3, buf, false, av2.visitArray(name));
                        break;
                    case 70:
                        float[] fv = new float[size];
                        for (int i5 = 0; i5 < size; i5++) {
                            fv[i5] = Float.intBitsToFloat(readInt(this.items[readUnsignedShort(v5)]));
                            v5 += 3;
                        }
                        av2.visit(name, fv);
                        v3 = v5 - 1;
                        break;
                    case 73:
                        int[] iv = new int[size];
                        for (int i6 = 0; i6 < size; i6++) {
                            iv[i6] = readInt(this.items[readUnsignedShort(v5)]);
                            v5 += 3;
                        }
                        av2.visit(name, iv);
                        v3 = v5 - 1;
                        break;
                    case 74:
                        long[] lv = new long[size];
                        for (int i7 = 0; i7 < size; i7++) {
                            lv[i7] = readLong(this.items[readUnsignedShort(v5)]);
                            v5 += 3;
                        }
                        av2.visit(name, lv);
                        v3 = v5 - 1;
                        break;
                    case 83:
                        short[] sv = new short[size];
                        for (int i8 = 0; i8 < size; i8++) {
                            sv[i8] = (short) readInt(this.items[readUnsignedShort(v5)]);
                            v5 += 3;
                        }
                        av2.visit(name, sv);
                        v3 = v5 - 1;
                        break;
                    case 90:
                        boolean[] zv = new boolean[size];
                        for (int i9 = 0; i9 < size; i9++) {
                            zv[i9] = readInt(this.items[readUnsignedShort(v5)]) != 0;
                            v5 += 3;
                        }
                        av2.visit(name, zv);
                        v3 = v5 - 1;
                        break;
                }
            case 99:
                av2.visit(name, Type.getType(readUTF8(v3, buf)));
                v3 += 2;
                break;
            case 101:
                av2.visitEnum(name, readUTF8(v3, buf), readUTF8(v3 + 2, buf));
                v3 += 4;
                break;
            case 115:
                av2.visit(name, readUTF8(v3, buf));
                v3 += 2;
                break;
        }
        return v3;
    }

    private int readFrameType(Object[] frame, int index, int v2, char[] buf, Label[] labels) {
        int v3 = v2 + 1;
        int type = this.f12090b[v2] & 255;
        switch (type) {
            case 0:
                frame[index] = Opcodes.TOP;
                break;
            case 1:
                frame[index] = Opcodes.INTEGER;
                break;
            case 2:
                frame[index] = Opcodes.FLOAT;
                break;
            case 3:
                frame[index] = Opcodes.DOUBLE;
                break;
            case 4:
                frame[index] = Opcodes.LONG;
                break;
            case 5:
                frame[index] = Opcodes.NULL;
                break;
            case 6:
                frame[index] = Opcodes.UNINITIALIZED_THIS;
                break;
            case 7:
                frame[index] = readClass(v3, buf);
                v3 += 2;
                break;
            default:
                frame[index] = readLabel(readUnsignedShort(v3), labels);
                v3 += 2;
                break;
        }
        return v3;
    }

    protected Label readLabel(int offset, Label[] labels) {
        if (labels[offset] == null) {
            labels[offset] = new Label();
        }
        return labels[offset];
    }

    private Attribute readAttribute(Attribute[] attrs, String type, int off, int len, char[] buf, int codeOff, Label[] labels) {
        for (int i2 = 0; i2 < attrs.length; i2++) {
            if (attrs[i2].type.equals(type)) {
                return attrs[i2].read(this, off, len, buf, codeOff, labels);
            }
        }
        return new Attribute(type).read(this, off, len, null, -1, null);
    }

    public int getItem(int item) {
        return this.items[item];
    }

    public int readByte(int index) {
        return this.f12090b[index] & 255;
    }

    public int readUnsignedShort(int index) {
        byte[] b2 = this.f12090b;
        return ((b2[index] & 255) << 8) | (b2[index + 1] & 255);
    }

    public short readShort(int index) {
        byte[] b2 = this.f12090b;
        return (short) (((b2[index] & 255) << 8) | (b2[index + 1] & 255));
    }

    public int readInt(int index) {
        byte[] b2 = this.f12090b;
        return ((b2[index] & 255) << 24) | ((b2[index + 1] & 255) << 16) | ((b2[index + 2] & 255) << 8) | (b2[index + 3] & 255);
    }

    public long readLong(int index) {
        long l1 = readInt(index);
        long l0 = readInt(index + 4) & 4294967295L;
        return (l1 << 32) | l0;
    }

    public String readUTF8(int index, char[] buf) {
        int item = readUnsignedShort(index);
        String s2 = this.strings[item];
        if (s2 != null) {
            return s2;
        }
        int index2 = this.items[item];
        String[] strArr = this.strings;
        String utf = readUTF(index2 + 2, readUnsignedShort(index2), buf);
        strArr[item] = utf;
        return utf;
    }

    private String readUTF(int index, int utfLen, char[] buf) {
        int endIndex = index + utfLen;
        byte[] b2 = this.f12090b;
        int strLen = 0;
        while (index < endIndex) {
            int i2 = index;
            index++;
            int c2 = b2[i2] & 255;
            switch (c2 >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    int i3 = strLen;
                    strLen++;
                    buf[i3] = (char) c2;
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    int index2 = index + 1;
                    byte b3 = b2[index];
                    index = index2 + 1;
                    int i4 = strLen;
                    strLen++;
                    buf[i4] = (char) (((c2 & 15) << 12) | ((b3 & 63) << 6) | (b2[index2] & 63));
                    break;
                case 12:
                case 13:
                    index++;
                    int i5 = strLen;
                    strLen++;
                    buf[i5] = (char) (((c2 & 31) << 6) | (b2[index] & 63));
                    break;
            }
        }
        return new String(buf, 0, strLen);
    }

    public String readClass(int index, char[] buf) {
        return readUTF8(this.items[readUnsignedShort(index)], buf);
    }

    public Object readConst(int item, char[] buf) {
        int index = this.items[item];
        switch (this.f12090b[index - 1]) {
            case 3:
                return new Integer(readInt(index));
            case 4:
                return new Float(Float.intBitsToFloat(readInt(index)));
            case 5:
                return new Long(readLong(index));
            case 6:
                return new Double(Double.longBitsToDouble(readLong(index)));
            case 7:
                return Type.getObjectType(readUTF8(index, buf));
            default:
                return readUTF8(index, buf);
        }
    }
}
