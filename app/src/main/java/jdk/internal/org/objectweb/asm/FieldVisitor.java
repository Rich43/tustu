package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/FieldVisitor.class */
public abstract class FieldVisitor {
    protected final int api;
    protected FieldVisitor fv;

    public FieldVisitor(int i2) {
        this(i2, null);
    }

    public FieldVisitor(int i2, FieldVisitor fieldVisitor) {
        if (i2 != 262144 && i2 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = i2;
        this.fv = fieldVisitor;
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        if (this.fv != null) {
            return this.fv.visitAnnotation(str, z2);
        }
        return null;
    }

    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.fv != null) {
            return this.fv.visitTypeAnnotation(i2, typePath, str, z2);
        }
        return null;
    }

    public void visitAttribute(Attribute attribute) {
        if (this.fv != null) {
            this.fv.visitAttribute(attribute);
        }
    }

    public void visitEnd() {
        if (this.fv != null) {
            this.fv.visitEnd();
        }
    }
}
