package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/AnnotationVisitor.class */
public abstract class AnnotationVisitor {
    protected final int api;

    /* renamed from: av, reason: collision with root package name */
    protected AnnotationVisitor f12857av;

    public AnnotationVisitor(int i2) {
        this(i2, null);
    }

    public AnnotationVisitor(int i2, AnnotationVisitor annotationVisitor) {
        if (i2 != 262144 && i2 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = i2;
        this.f12857av = annotationVisitor;
    }

    public void visit(String str, Object obj) {
        if (this.f12857av != null) {
            this.f12857av.visit(str, obj);
        }
    }

    public void visitEnum(String str, String str2, String str3) {
        if (this.f12857av != null) {
            this.f12857av.visitEnum(str, str2, str3);
        }
    }

    public AnnotationVisitor visitAnnotation(String str, String str2) {
        if (this.f12857av != null) {
            return this.f12857av.visitAnnotation(str, str2);
        }
        return null;
    }

    public AnnotationVisitor visitArray(String str) {
        if (this.f12857av != null) {
            return this.f12857av.visitArray(str);
        }
        return null;
    }

    public void visitEnd() {
        if (this.f12857av != null) {
            this.f12857av.visitEnd();
        }
    }
}
