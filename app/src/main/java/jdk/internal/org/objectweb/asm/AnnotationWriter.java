package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/AnnotationWriter.class */
final class AnnotationWriter extends AnnotationVisitor {
    private final ClassWriter cw;
    private int size;
    private final boolean named;

    /* renamed from: bv, reason: collision with root package name */
    private final ByteVector f12858bv;
    private final ByteVector parent;
    private final int offset;
    AnnotationWriter next;
    AnnotationWriter prev;

    AnnotationWriter(ClassWriter classWriter, boolean z2, ByteVector byteVector, ByteVector byteVector2, int i2) {
        super(Opcodes.ASM5);
        this.cw = classWriter;
        this.named = z2;
        this.f12858bv = byteVector;
        this.parent = byteVector2;
        this.offset = i2;
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visit(String str, Object obj) {
        this.size++;
        if (this.named) {
            this.f12858bv.putShort(this.cw.newUTF8(str));
        }
        if (obj instanceof String) {
            this.f12858bv.put12(115, this.cw.newUTF8((String) obj));
            return;
        }
        if (obj instanceof Byte) {
            this.f12858bv.put12(66, this.cw.newInteger(((Byte) obj).byteValue()).index);
            return;
        }
        if (obj instanceof Boolean) {
            this.f12858bv.put12(90, this.cw.newInteger(((Boolean) obj).booleanValue() ? 1 : 0).index);
            return;
        }
        if (obj instanceof Character) {
            this.f12858bv.put12(67, this.cw.newInteger(((Character) obj).charValue()).index);
            return;
        }
        if (obj instanceof Short) {
            this.f12858bv.put12(83, this.cw.newInteger(((Short) obj).shortValue()).index);
            return;
        }
        if (obj instanceof Type) {
            this.f12858bv.put12(99, this.cw.newUTF8(((Type) obj).getDescriptor()));
            return;
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            this.f12858bv.put12(91, bArr.length);
            for (byte b2 : bArr) {
                this.f12858bv.put12(66, this.cw.newInteger(b2).index);
            }
            return;
        }
        if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            this.f12858bv.put12(91, zArr.length);
            for (boolean z2 : zArr) {
                this.f12858bv.put12(90, this.cw.newInteger(z2 ? 1 : 0).index);
            }
            return;
        }
        if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            this.f12858bv.put12(91, sArr.length);
            for (short s2 : sArr) {
                this.f12858bv.put12(83, this.cw.newInteger(s2).index);
            }
            return;
        }
        if (obj instanceof char[]) {
            char[] cArr = (char[]) obj;
            this.f12858bv.put12(91, cArr.length);
            for (char c2 : cArr) {
                this.f12858bv.put12(67, this.cw.newInteger(c2).index);
            }
            return;
        }
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            this.f12858bv.put12(91, iArr.length);
            for (int i2 : iArr) {
                this.f12858bv.put12(73, this.cw.newInteger(i2).index);
            }
            return;
        }
        if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            this.f12858bv.put12(91, jArr.length);
            for (long j2 : jArr) {
                this.f12858bv.put12(74, this.cw.newLong(j2).index);
            }
            return;
        }
        if (obj instanceof float[]) {
            float[] fArr = (float[]) obj;
            this.f12858bv.put12(91, fArr.length);
            for (float f2 : fArr) {
                this.f12858bv.put12(70, this.cw.newFloat(f2).index);
            }
            return;
        }
        if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            this.f12858bv.put12(91, dArr.length);
            for (double d2 : dArr) {
                this.f12858bv.put12(68, this.cw.newDouble(d2).index);
            }
            return;
        }
        Item itemNewConstItem = this.cw.newConstItem(obj);
        this.f12858bv.put12(".s.IFJDCS".charAt(itemNewConstItem.type), itemNewConstItem.index);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String str, String str2, String str3) {
        this.size++;
        if (this.named) {
            this.f12858bv.putShort(this.cw.newUTF8(str));
        }
        this.f12858bv.put12(101, this.cw.newUTF8(str2)).putShort(this.cw.newUTF8(str3));
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String str, String str2) {
        this.size++;
        if (this.named) {
            this.f12858bv.putShort(this.cw.newUTF8(str));
        }
        this.f12858bv.put12(64, this.cw.newUTF8(str2)).putShort(0);
        return new AnnotationWriter(this.cw, true, this.f12858bv, this.f12858bv, this.f12858bv.length - 2);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String str) {
        this.size++;
        if (this.named) {
            this.f12858bv.putShort(this.cw.newUTF8(str));
        }
        this.f12858bv.put12(91, 0);
        return new AnnotationWriter(this.cw, false, this.f12858bv, this.f12858bv, this.f12858bv.length - 2);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
        if (this.parent != null) {
            byte[] bArr = this.parent.data;
            bArr[this.offset] = (byte) (this.size >>> 8);
            bArr[this.offset + 1] = (byte) this.size;
        }
    }

    int getSize() {
        int i2 = 0;
        AnnotationWriter annotationWriter = this;
        while (true) {
            AnnotationWriter annotationWriter2 = annotationWriter;
            if (annotationWriter2 != null) {
                i2 += annotationWriter2.f12858bv.length;
                annotationWriter = annotationWriter2.next;
            } else {
                return i2;
            }
        }
    }

    void put(ByteVector byteVector) {
        int i2 = 0;
        int i3 = 2;
        AnnotationWriter annotationWriter = null;
        for (AnnotationWriter annotationWriter2 = this; annotationWriter2 != null; annotationWriter2 = annotationWriter2.next) {
            i2++;
            i3 += annotationWriter2.f12858bv.length;
            annotationWriter2.visitEnd();
            annotationWriter2.prev = annotationWriter;
            annotationWriter = annotationWriter2;
        }
        byteVector.putInt(i3);
        byteVector.putShort(i2);
        AnnotationWriter annotationWriter3 = annotationWriter;
        while (true) {
            AnnotationWriter annotationWriter4 = annotationWriter3;
            if (annotationWriter4 != null) {
                byteVector.putByteArray(annotationWriter4.f12858bv.data, 0, annotationWriter4.f12858bv.length);
                annotationWriter3 = annotationWriter4.prev;
            } else {
                return;
            }
        }
    }

    static void put(AnnotationWriter[] annotationWriterArr, int i2, ByteVector byteVector) {
        int length = 1 + (2 * (annotationWriterArr.length - i2));
        for (int i3 = i2; i3 < annotationWriterArr.length; i3++) {
            length += annotationWriterArr[i3] == null ? 0 : annotationWriterArr[i3].getSize();
        }
        byteVector.putInt(length).putByte(annotationWriterArr.length - i2);
        for (int i4 = i2; i4 < annotationWriterArr.length; i4++) {
            AnnotationWriter annotationWriter = null;
            int i5 = 0;
            for (AnnotationWriter annotationWriter2 = annotationWriterArr[i4]; annotationWriter2 != null; annotationWriter2 = annotationWriter2.next) {
                i5++;
                annotationWriter2.visitEnd();
                annotationWriter2.prev = annotationWriter;
                annotationWriter = annotationWriter2;
            }
            byteVector.putShort(i5);
            AnnotationWriter annotationWriter3 = annotationWriter;
            while (true) {
                AnnotationWriter annotationWriter4 = annotationWriter3;
                if (annotationWriter4 != null) {
                    byteVector.putByteArray(annotationWriter4.f12858bv.data, 0, annotationWriter4.f12858bv.length);
                    annotationWriter3 = annotationWriter4.prev;
                }
            }
        }
    }

    static void putTarget(int i2, TypePath typePath, ByteVector byteVector) {
        switch (i2 >>> 24) {
            case 0:
            case 1:
            case 22:
                byteVector.putShort(i2 >>> 16);
                break;
            case 19:
            case 20:
            case 21:
                byteVector.putByte(i2 >>> 24);
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                byteVector.putInt(i2);
                break;
            default:
                byteVector.put12(i2 >>> 24, (i2 & 16776960) >> 8);
                break;
        }
        if (typePath == null) {
            byteVector.putByte(0);
        } else {
            byteVector.putByteArray(typePath.f12860b, typePath.offset, (typePath.f12860b[typePath.offset] * 2) + 1);
        }
    }
}
