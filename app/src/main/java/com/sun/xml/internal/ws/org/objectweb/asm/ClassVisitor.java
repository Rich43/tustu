package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/ClassVisitor.class */
public interface ClassVisitor {
    void visit(int i2, int i3, String str, String str2, String str3, String[] strArr);

    void visitSource(String str, String str2);

    void visitOuterClass(String str, String str2, String str3);

    AnnotationVisitor visitAnnotation(String str, boolean z2);

    void visitAttribute(Attribute attribute);

    void visitInnerClass(String str, String str2, String str3, int i2);

    FieldVisitor visitField(int i2, String str, String str2, String str3, Object obj);

    MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr);

    void visitEnd();
}
