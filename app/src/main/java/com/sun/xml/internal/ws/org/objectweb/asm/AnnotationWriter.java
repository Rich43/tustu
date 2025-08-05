package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/AnnotationWriter.class */
final class AnnotationWriter implements AnnotationVisitor {
    private final ClassWriter cw;
    private int size;
    private final boolean named;

    /* renamed from: bv, reason: collision with root package name */
    private final ByteVector f12089bv;
    private final ByteVector parent;
    private final int offset;
    AnnotationWriter next;
    AnnotationWriter prev;

    AnnotationWriter(ClassWriter cw, boolean named, ByteVector bv2, ByteVector parent, int offset) {
        this.cw = cw;
        this.named = named;
        this.f12089bv = bv2;
        this.parent = parent;
        this.offset = offset;
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        this.size++;
        if (this.named) {
            this.f12089bv.putShort(this.cw.newUTF8(name));
        }
        if (value instanceof String) {
            this.f12089bv.put12(115, this.cw.newUTF8((String) value));
            return;
        }
        if (value instanceof Byte) {
            this.f12089bv.put12(66, this.cw.newInteger(((Byte) value).byteValue()).index);
            return;
        }
        if (value instanceof Boolean) {
            this.f12089bv.put12(90, this.cw.newInteger(((Boolean) value).booleanValue() ? 1 : 0).index);
            return;
        }
        if (value instanceof Character) {
            this.f12089bv.put12(67, this.cw.newInteger(((Character) value).charValue()).index);
            return;
        }
        if (value instanceof Short) {
            this.f12089bv.put12(83, this.cw.newInteger(((Short) value).shortValue()).index);
            return;
        }
        if (value instanceof Type) {
            this.f12089bv.put12(99, this.cw.newUTF8(((Type) value).getDescriptor()));
            return;
        }
        if (value instanceof byte[]) {
            byte[] v2 = (byte[]) value;
            this.f12089bv.put12(91, v2.length);
            for (byte b2 : v2) {
                this.f12089bv.put12(66, this.cw.newInteger(b2).index);
            }
            return;
        }
        if (value instanceof boolean[]) {
            boolean[] v3 = (boolean[]) value;
            this.f12089bv.put12(91, v3.length);
            for (boolean z2 : v3) {
                this.f12089bv.put12(90, this.cw.newInteger(z2 ? 1 : 0).index);
            }
            return;
        }
        if (value instanceof short[]) {
            short[] v4 = (short[]) value;
            this.f12089bv.put12(91, v4.length);
            for (short s2 : v4) {
                this.f12089bv.put12(83, this.cw.newInteger(s2).index);
            }
            return;
        }
        if (value instanceof char[]) {
            char[] v5 = (char[]) value;
            this.f12089bv.put12(91, v5.length);
            for (char c2 : v5) {
                this.f12089bv.put12(67, this.cw.newInteger(c2).index);
            }
            return;
        }
        if (value instanceof int[]) {
            int[] v6 = (int[]) value;
            this.f12089bv.put12(91, v6.length);
            for (int i2 : v6) {
                this.f12089bv.put12(73, this.cw.newInteger(i2).index);
            }
            return;
        }
        if (value instanceof long[]) {
            long[] v7 = (long[]) value;
            this.f12089bv.put12(91, v7.length);
            for (long j2 : v7) {
                this.f12089bv.put12(74, this.cw.newLong(j2).index);
            }
            return;
        }
        if (value instanceof float[]) {
            float[] v8 = (float[]) value;
            this.f12089bv.put12(91, v8.length);
            for (float f2 : v8) {
                this.f12089bv.put12(70, this.cw.newFloat(f2).index);
            }
            return;
        }
        if (value instanceof double[]) {
            double[] v9 = (double[]) value;
            this.f12089bv.put12(91, v9.length);
            for (double d2 : v9) {
                this.f12089bv.put12(68, this.cw.newDouble(d2).index);
            }
            return;
        }
        Item i3 = this.cw.newConstItem(value);
        this.f12089bv.put12(".s.IFJDCS".charAt(i3.type), i3.index);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String name, String desc, String value) {
        this.size++;
        if (this.named) {
            this.f12089bv.putShort(this.cw.newUTF8(name));
        }
        this.f12089bv.put12(101, this.cw.newUTF8(desc)).putShort(this.cw.newUTF8(value));
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        this.size++;
        if (this.named) {
            this.f12089bv.putShort(this.cw.newUTF8(name));
        }
        this.f12089bv.put12(64, this.cw.newUTF8(desc)).putShort(0);
        return new AnnotationWriter(this.cw, true, this.f12089bv, this.f12089bv, this.f12089bv.length - 2);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String name) {
        this.size++;
        if (this.named) {
            this.f12089bv.putShort(this.cw.newUTF8(name));
        }
        this.f12089bv.put12(91, 0);
        return new AnnotationWriter(this.cw, false, this.f12089bv, this.f12089bv, this.f12089bv.length - 2);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
        if (this.parent != null) {
            byte[] data = this.parent.data;
            data[this.offset] = (byte) (this.size >>> 8);
            data[this.offset + 1] = (byte) this.size;
        }
    }

    int getSize() {
        int size = 0;
        AnnotationWriter annotationWriter = this;
        while (true) {
            AnnotationWriter aw2 = annotationWriter;
            if (aw2 != null) {
                size += aw2.f12089bv.length;
                annotationWriter = aw2.next;
            } else {
                return size;
            }
        }
    }

    void put(ByteVector out) {
        int n2 = 0;
        int size = 2;
        AnnotationWriter last = null;
        for (AnnotationWriter aw2 = this; aw2 != null; aw2 = aw2.next) {
            n2++;
            size += aw2.f12089bv.length;
            aw2.visitEnd();
            aw2.prev = last;
            last = aw2;
        }
        out.putInt(size);
        out.putShort(n2);
        AnnotationWriter annotationWriter = last;
        while (true) {
            AnnotationWriter aw3 = annotationWriter;
            if (aw3 != null) {
                out.putByteArray(aw3.f12089bv.data, 0, aw3.f12089bv.length);
                annotationWriter = aw3.prev;
            } else {
                return;
            }
        }
    }

    static void put(AnnotationWriter[] panns, int off, ByteVector out) {
        int size = 1 + (2 * (panns.length - off));
        for (int i2 = off; i2 < panns.length; i2++) {
            size += panns[i2] == null ? 0 : panns[i2].getSize();
        }
        out.putInt(size).putByte(panns.length - off);
        for (int i3 = off; i3 < panns.length; i3++) {
            AnnotationWriter last = null;
            int n2 = 0;
            for (AnnotationWriter aw2 = panns[i3]; aw2 != null; aw2 = aw2.next) {
                n2++;
                aw2.visitEnd();
                aw2.prev = last;
                last = aw2;
            }
            out.putShort(n2);
            AnnotationWriter annotationWriter = last;
            while (true) {
                AnnotationWriter aw3 = annotationWriter;
                if (aw3 != null) {
                    out.putByteArray(aw3.f12089bv.data, 0, aw3.f12089bv.length);
                    annotationWriter = aw3.prev;
                }
            }
        }
    }
}
