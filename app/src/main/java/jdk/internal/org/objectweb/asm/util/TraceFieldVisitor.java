package jdk.internal.org.objectweb.asm.util;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/TraceFieldVisitor.class */
public final class TraceFieldVisitor extends FieldVisitor {

    /* renamed from: p, reason: collision with root package name */
    public final Printer f12865p;

    public TraceFieldVisitor(Printer printer) {
        this(null, printer);
    }

    public TraceFieldVisitor(FieldVisitor fieldVisitor, Printer printer) {
        super(Opcodes.ASM5, fieldVisitor);
        this.f12865p = printer;
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        return new TraceAnnotationVisitor(this.fv == null ? null : this.fv.visitAnnotation(str, z2), this.f12865p.visitFieldAnnotation(str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return new TraceAnnotationVisitor(this.fv == null ? null : this.fv.visitTypeAnnotation(i2, typePath, str, z2), this.f12865p.visitFieldTypeAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public void visitAttribute(Attribute attribute) {
        this.f12865p.visitFieldAttribute(attribute);
        super.visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public void visitEnd() {
        this.f12865p.visitFieldEnd();
        super.visitEnd();
    }
}
