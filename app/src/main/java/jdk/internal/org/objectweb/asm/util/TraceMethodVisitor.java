package jdk.internal.org.objectweb.asm.util;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/TraceMethodVisitor.class */
public final class TraceMethodVisitor extends MethodVisitor {

    /* renamed from: p, reason: collision with root package name */
    public final Printer f12866p;

    public TraceMethodVisitor(Printer printer) {
        this(null, printer);
    }

    public TraceMethodVisitor(MethodVisitor methodVisitor, Printer printer) {
        super(Opcodes.ASM5, methodVisitor);
        this.f12866p = printer;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitParameter(String str, int i2) {
        this.f12866p.visitParameter(str, i2);
        super.visitParameter(str, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        return new TraceAnnotationVisitor(this.mv == null ? null : this.mv.visitAnnotation(str, z2), this.f12866p.visitMethodAnnotation(str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return new TraceAnnotationVisitor(this.mv == null ? null : this.mv.visitTypeAnnotation(i2, typePath, str, z2), this.f12866p.visitMethodTypeAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attribute) {
        this.f12866p.visitMethodAttribute(attribute);
        super.visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        return new TraceAnnotationVisitor(this.mv == null ? null : this.mv.visitAnnotationDefault(), this.f12866p.visitAnnotationDefault());
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int i2, String str, boolean z2) {
        return new TraceAnnotationVisitor(this.mv == null ? null : this.mv.visitParameterAnnotation(i2, str, z2), this.f12866p.visitParameterAnnotation(i2, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitCode() {
        this.f12866p.visitCode();
        super.visitCode();
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        this.f12866p.visitFrame(i2, i3, objArr, i4, objArr2);
        super.visitFrame(i2, i3, objArr, i4, objArr2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i2) {
        this.f12866p.visitInsn(i2);
        super.visitInsn(i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int i2, int i3) {
        this.f12866p.visitIntInsn(i2, i3);
        super.visitIntInsn(i2, i3);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i2, int i3) {
        this.f12866p.visitVarInsn(i2, i3);
        super.visitVarInsn(i2, i3);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i2, String str) {
        this.f12866p.visitTypeInsn(i2, str);
        super.visitTypeInsn(i2, str);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        this.f12866p.visitFieldInsn(i2, str, str2, str3);
        super.visitFieldInsn(i2, str, str2, str3);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    @Deprecated
    public void visitMethodInsn(int i2, String str, String str2, String str3) {
        if (this.api >= 327680) {
            super.visitMethodInsn(i2, str, str2, str3);
            return;
        }
        this.f12866p.visitMethodInsn(i2, str, str2, str3);
        if (this.mv != null) {
            this.mv.visitMethodInsn(i2, str, str2, str3);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            super.visitMethodInsn(i2, str, str2, str3, z2);
            return;
        }
        this.f12866p.visitMethodInsn(i2, str, str2, str3, z2);
        if (this.mv != null) {
            this.mv.visitMethodInsn(i2, str, str2, str3, z2);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        this.f12866p.visitInvokeDynamicInsn(str, str2, handle, objArr);
        super.visitInvokeDynamicInsn(str, str2, handle, objArr);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i2, Label label) {
        this.f12866p.visitJumpInsn(i2, label);
        super.visitJumpInsn(i2, label);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        this.f12866p.visitLabel(label);
        super.visitLabel(label);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        this.f12866p.visitLdcInsn(obj);
        super.visitLdcInsn(obj);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i2, int i3) {
        this.f12866p.visitIincInsn(i2, i3);
        super.visitIincInsn(i2, i3);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr) {
        this.f12866p.visitTableSwitchInsn(i2, i3, label, labelArr);
        super.visitTableSwitchInsn(i2, i3, label, labelArr);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        this.f12866p.visitLookupSwitchInsn(label, iArr, labelArr);
        super.visitLookupSwitchInsn(label, iArr, labelArr);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i2) {
        this.f12866p.visitMultiANewArrayInsn(str, i2);
        super.visitMultiANewArrayInsn(str, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitInsnAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return new TraceAnnotationVisitor(this.mv == null ? null : this.mv.visitInsnAnnotation(i2, typePath, str, z2), this.f12866p.visitInsnAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.f12866p.visitTryCatchBlock(label, label2, label3, str);
        super.visitTryCatchBlock(label, label2, label3, str);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTryCatchAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return new TraceAnnotationVisitor(this.mv == null ? null : this.mv.visitTryCatchAnnotation(i2, typePath, str, z2), this.f12866p.visitTryCatchAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2) {
        this.f12866p.visitLocalVariable(str, str2, str3, label, label2, i2);
        super.visitLocalVariable(str, str2, str3, label, label2, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        return new TraceAnnotationVisitor(this.mv == null ? null : this.mv.visitLocalVariableAnnotation(i2, typePath, labelArr, labelArr2, iArr, str, z2), this.f12866p.visitLocalVariableAnnotation(i2, typePath, labelArr, labelArr2, iArr, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int i2, Label label) {
        this.f12866p.visitLineNumber(i2, label);
        super.visitLineNumber(i2, label);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i2, int i3) {
        this.f12866p.visitMaxs(i2, i3);
        super.visitMaxs(i2, i3);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
        this.f12866p.visitMethodEnd();
        super.visitEnd();
    }
}
