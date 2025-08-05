package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/FieldVisitor.class */
public interface FieldVisitor {
    AnnotationVisitor visitAnnotation(String str, boolean z2);

    void visitAttribute(Attribute attribute);

    void visitEnd();
}
