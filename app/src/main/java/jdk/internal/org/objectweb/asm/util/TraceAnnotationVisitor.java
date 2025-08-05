package jdk.internal.org.objectweb.asm.util;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/TraceAnnotationVisitor.class */
public final class TraceAnnotationVisitor extends AnnotationVisitor {

    /* renamed from: p, reason: collision with root package name */
    private final Printer f12863p;

    public TraceAnnotationVisitor(Printer printer) {
        this(null, printer);
    }

    public TraceAnnotationVisitor(AnnotationVisitor annotationVisitor, Printer printer) {
        super(Opcodes.ASM5, annotationVisitor);
        this.f12863p = printer;
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visit(String str, Object obj) {
        this.f12863p.visit(str, obj);
        super.visit(str, obj);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String str, String str2, String str3) {
        this.f12863p.visitEnum(str, str2, str3);
        super.visitEnum(str, str2, str3);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String str, String str2) {
        return new TraceAnnotationVisitor(this.f12857av == null ? null : this.f12857av.visitAnnotation(str, str2), this.f12863p.visitAnnotation(str, str2));
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String str) {
        return new TraceAnnotationVisitor(this.f12857av == null ? null : this.f12857av.visitArray(str), this.f12863p.visitArray(str));
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
        this.f12863p.visitAnnotationEnd();
        super.visitEnd();
    }
}
