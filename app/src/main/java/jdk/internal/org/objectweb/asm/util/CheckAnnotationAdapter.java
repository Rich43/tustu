package jdk.internal.org.objectweb.asm.util;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/CheckAnnotationAdapter.class */
public class CheckAnnotationAdapter extends AnnotationVisitor {
    private final boolean named;
    private boolean end;

    public CheckAnnotationAdapter(AnnotationVisitor annotationVisitor) {
        this(annotationVisitor, true);
    }

    CheckAnnotationAdapter(AnnotationVisitor annotationVisitor, boolean z2) {
        super(Opcodes.ASM5, annotationVisitor);
        this.named = z2;
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visit(String str, Object obj) {
        checkEnd();
        checkName(str);
        if (!(obj instanceof Byte) && !(obj instanceof Boolean) && !(obj instanceof Character) && !(obj instanceof Short) && !(obj instanceof Integer) && !(obj instanceof Long) && !(obj instanceof Float) && !(obj instanceof Double) && !(obj instanceof String) && !(obj instanceof Type) && !(obj instanceof byte[]) && !(obj instanceof boolean[]) && !(obj instanceof char[]) && !(obj instanceof short[]) && !(obj instanceof int[]) && !(obj instanceof long[]) && !(obj instanceof float[]) && !(obj instanceof double[])) {
            throw new IllegalArgumentException("Invalid annotation value");
        }
        if ((obj instanceof Type) && ((Type) obj).getSort() == 11) {
            throw new IllegalArgumentException("Invalid annotation value");
        }
        if (this.f12857av != null) {
            this.f12857av.visit(str, obj);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String str, String str2, String str3) {
        checkEnd();
        checkName(str);
        CheckMethodAdapter.checkDesc(str2, false);
        if (str3 == null) {
            throw new IllegalArgumentException("Invalid enum value");
        }
        if (this.f12857av != null) {
            this.f12857av.visitEnum(str, str2, str3);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String str, String str2) {
        checkEnd();
        checkName(str);
        CheckMethodAdapter.checkDesc(str2, false);
        return new CheckAnnotationAdapter(this.f12857av == null ? null : this.f12857av.visitAnnotation(str, str2));
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String str) {
        checkEnd();
        checkName(str);
        return new CheckAnnotationAdapter(this.f12857av == null ? null : this.f12857av.visitArray(str), false);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
        checkEnd();
        this.end = true;
        if (this.f12857av != null) {
            this.f12857av.visitEnd();
        }
    }

    private void checkEnd() {
        if (this.end) {
            throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }

    private void checkName(String str) {
        if (this.named && str == null) {
            throw new IllegalArgumentException("Annotation value name must not be null");
        }
    }
}
