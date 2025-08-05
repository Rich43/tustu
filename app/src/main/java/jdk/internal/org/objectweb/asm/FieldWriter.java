package jdk.internal.org.objectweb.asm;

import com.sun.org.apache.xml.internal.security.utils.Constants;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/FieldWriter.class */
final class FieldWriter extends FieldVisitor {
    private final ClassWriter cw;
    private final int access;
    private final int name;
    private final int desc;
    private int signature;
    private int value;
    private AnnotationWriter anns;
    private AnnotationWriter ianns;
    private AnnotationWriter tanns;
    private AnnotationWriter itanns;
    private Attribute attrs;

    FieldWriter(ClassWriter classWriter, int i2, String str, String str2, String str3, Object obj) {
        super(Opcodes.ASM5);
        if (classWriter.firstField == null) {
            classWriter.firstField = this;
        } else {
            classWriter.lastField.fv = this;
        }
        classWriter.lastField = this;
        this.cw = classWriter;
        this.access = i2;
        this.name = classWriter.newUTF8(str);
        this.desc = classWriter.newUTF8(str2);
        if (str3 != null) {
            this.signature = classWriter.newUTF8(str3);
        }
        if (obj != null) {
            this.value = classWriter.newConstItem(obj).index;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        byteVector.putShort(this.cw.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, 2);
        if (z2) {
            annotationWriter.next = this.anns;
            this.anns = annotationWriter;
        } else {
            annotationWriter.next = this.ianns;
            this.ianns = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        ByteVector byteVector = new ByteVector();
        AnnotationWriter.putTarget(i2, typePath, byteVector);
        byteVector.putShort(this.cw.newUTF8(str)).putShort(0);
        AnnotationWriter annotationWriter = new AnnotationWriter(this.cw, true, byteVector, byteVector, byteVector.length - 2);
        if (z2) {
            annotationWriter.next = this.tanns;
            this.tanns = annotationWriter;
        } else {
            annotationWriter.next = this.itanns;
            this.itanns = annotationWriter;
        }
        return annotationWriter;
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public void visitAttribute(Attribute attribute) {
        attribute.next = this.attrs;
        this.attrs = attribute;
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public void visitEnd() {
    }

    int getSize() {
        int size = 8;
        if (this.value != 0) {
            this.cw.newUTF8("ConstantValue");
            size = 8 + 8;
        }
        if ((this.access & 4096) != 0 && ((this.cw.version & 65535) < 49 || (this.access & 262144) != 0)) {
            this.cw.newUTF8("Synthetic");
            size += 6;
        }
        if ((this.access & 131072) != 0) {
            this.cw.newUTF8("Deprecated");
            size += 6;
        }
        if (this.signature != 0) {
            this.cw.newUTF8(Constants._TAG_SIGNATURE);
            size += 8;
        }
        if (this.anns != null) {
            this.cw.newUTF8("RuntimeVisibleAnnotations");
            size += 8 + this.anns.getSize();
        }
        if (this.ianns != null) {
            this.cw.newUTF8("RuntimeInvisibleAnnotations");
            size += 8 + this.ianns.getSize();
        }
        if (this.tanns != null) {
            this.cw.newUTF8("RuntimeVisibleTypeAnnotations");
            size += 8 + this.tanns.getSize();
        }
        if (this.itanns != null) {
            this.cw.newUTF8("RuntimeInvisibleTypeAnnotations");
            size += 8 + this.itanns.getSize();
        }
        if (this.attrs != null) {
            size += this.attrs.getSize(this.cw, null, 0, -1, -1);
        }
        return size;
    }

    void put(ByteVector byteVector) {
        byteVector.putShort(this.access & ((393216 | ((this.access & 262144) / 64)) ^ (-1))).putShort(this.name).putShort(this.desc);
        int count = 0;
        if (this.value != 0) {
            count = 0 + 1;
        }
        if ((this.access & 4096) != 0 && ((this.cw.version & 65535) < 49 || (this.access & 262144) != 0)) {
            count++;
        }
        if ((this.access & 131072) != 0) {
            count++;
        }
        if (this.signature != 0) {
            count++;
        }
        if (this.anns != null) {
            count++;
        }
        if (this.ianns != null) {
            count++;
        }
        if (this.tanns != null) {
            count++;
        }
        if (this.itanns != null) {
            count++;
        }
        if (this.attrs != null) {
            count += this.attrs.getCount();
        }
        byteVector.putShort(count);
        if (this.value != 0) {
            byteVector.putShort(this.cw.newUTF8("ConstantValue"));
            byteVector.putInt(2).putShort(this.value);
        }
        if ((this.access & 4096) != 0 && ((this.cw.version & 65535) < 49 || (this.access & 262144) != 0)) {
            byteVector.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
        }
        if ((this.access & 131072) != 0) {
            byteVector.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
        }
        if (this.signature != 0) {
            byteVector.putShort(this.cw.newUTF8(Constants._TAG_SIGNATURE));
            byteVector.putInt(2).putShort(this.signature);
        }
        if (this.anns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
            this.anns.put(byteVector);
        }
        if (this.ianns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
            this.ianns.put(byteVector);
        }
        if (this.tanns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeVisibleTypeAnnotations"));
            this.tanns.put(byteVector);
        }
        if (this.itanns != null) {
            byteVector.putShort(this.cw.newUTF8("RuntimeInvisibleTypeAnnotations"));
            this.itanns.put(byteVector);
        }
        if (this.attrs != null) {
            this.attrs.put(this.cw, null, 0, -1, -1, byteVector);
        }
    }
}
