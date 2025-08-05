package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/ClassVisitor.class */
public abstract class ClassVisitor {
    protected final int api;
    protected ClassVisitor cv;

    public ClassVisitor(int i2) {
        this(i2, null);
    }

    public ClassVisitor(int i2, ClassVisitor classVisitor) {
        if (i2 != 262144 && i2 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = i2;
        this.cv = classVisitor;
    }

    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        if (this.cv != null) {
            this.cv.visit(i2, i3, str, str2, str3, strArr);
        }
    }

    public void visitSource(String str, String str2) {
        if (this.cv != null) {
            this.cv.visitSource(str, str2);
        }
    }

    public void visitOuterClass(String str, String str2, String str3) {
        if (this.cv != null) {
            this.cv.visitOuterClass(str, str2, str3);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        if (this.cv != null) {
            return this.cv.visitAnnotation(str, z2);
        }
        return null;
    }

    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.cv != null) {
            return this.cv.visitTypeAnnotation(i2, typePath, str, z2);
        }
        return null;
    }

    public void visitAttribute(Attribute attribute) {
        if (this.cv != null) {
            this.cv.visitAttribute(attribute);
        }
    }

    public void visitInnerClass(String str, String str2, String str3, int i2) {
        if (this.cv != null) {
            this.cv.visitInnerClass(str, str2, str3, i2);
        }
    }

    public FieldVisitor visitField(int i2, String str, String str2, String str3, Object obj) {
        if (this.cv != null) {
            return this.cv.visitField(i2, str, str2, str3, obj);
        }
        return null;
    }

    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        if (this.cv != null) {
            return this.cv.visitMethod(i2, str, str2, str3, strArr);
        }
        return null;
    }

    public void visitEnd() {
        if (this.cv != null) {
            this.cv.visitEnd();
        }
    }
}
