package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/MethodAdapter.class */
public class MethodAdapter implements MethodVisitor {
    protected MethodVisitor mv;

    public MethodAdapter(MethodVisitor mv) {
        this.mv = mv;
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        return this.mv.visitAnnotationDefault();
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return this.mv.visitAnnotation(desc, visible);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        return this.mv.visitParameterAnnotation(parameter, desc, visible);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attr) {
        this.mv.visitAttribute(attr);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitCode() {
        this.mv.visitCode();
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        this.mv.visitFrame(type, nLocal, local, nStack, stack);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitInsn(int opcode) {
        this.mv.visitInsn(opcode);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int opcode, int operand) {
        this.mv.visitIntInsn(opcode, operand);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int opcode, int var) {
        this.mv.visitVarInsn(opcode, var);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int opcode, String type) {
        this.mv.visitTypeInsn(opcode, type);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.mv.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.mv.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int opcode, Label label) {
        this.mv.visitJumpInsn(opcode, label);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        this.mv.visitLabel(label);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object cst) {
        this.mv.visitLdcInsn(cst);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int var, int increment) {
        this.mv.visitIincInsn(var, increment);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
        this.mv.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.mv.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String desc, int dims) {
        this.mv.visitMultiANewArrayInsn(desc, dims);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.mv.visitTryCatchBlock(start, end, handler, type);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        this.mv.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int line, Label start) {
        this.mv.visitLineNumber(line, start);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int maxStack, int maxLocals) {
        this.mv.visitMaxs(maxStack, maxLocals);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
        this.mv.visitEnd();
    }
}
