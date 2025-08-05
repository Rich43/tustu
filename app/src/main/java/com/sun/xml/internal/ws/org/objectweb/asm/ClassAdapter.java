package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/ClassAdapter.class */
public class ClassAdapter implements ClassVisitor {
    protected ClassVisitor cv;

    public ClassAdapter(ClassVisitor cv) {
        this.cv = cv;
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.cv.visit(version, access, name, signature, superName, interfaces);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor
    public void visitSource(String source, String debug) {
        this.cv.visitSource(source, debug);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String owner, String name, String desc) {
        this.cv.visitOuterClass(owner, name, desc);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return this.cv.visitAnnotation(desc, visible);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attr) {
        this.cv.visitAttribute(attr);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.cv.visitInnerClass(name, outerName, innerName, access);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return this.cv.visitField(access, name, desc, signature, value);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return this.cv.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override // com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        this.cv.visitEnd();
    }
}
