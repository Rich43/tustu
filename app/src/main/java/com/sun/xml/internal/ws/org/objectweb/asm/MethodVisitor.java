package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/MethodVisitor.class */
public interface MethodVisitor {
    AnnotationVisitor visitAnnotationDefault();

    AnnotationVisitor visitAnnotation(String str, boolean z2);

    AnnotationVisitor visitParameterAnnotation(int i2, String str, boolean z2);

    void visitAttribute(Attribute attribute);

    void visitCode();

    void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2);

    void visitInsn(int i2);

    void visitIntInsn(int i2, int i3);

    void visitVarInsn(int i2, int i3);

    void visitTypeInsn(int i2, String str);

    void visitFieldInsn(int i2, String str, String str2, String str3);

    void visitMethodInsn(int i2, String str, String str2, String str3);

    void visitJumpInsn(int i2, Label label);

    void visitLabel(Label label);

    void visitLdcInsn(Object obj);

    void visitIincInsn(int i2, int i3);

    void visitTableSwitchInsn(int i2, int i3, Label label, Label[] labelArr);

    void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr);

    void visitMultiANewArrayInsn(String str, int i2);

    void visitTryCatchBlock(Label label, Label label2, Label label3, String str);

    void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2);

    void visitLineNumber(int i2, Label label);

    void visitMaxs(int i2, int i3);

    void visitEnd();
}
