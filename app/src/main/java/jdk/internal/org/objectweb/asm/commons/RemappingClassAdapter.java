package jdk.internal.org.objectweb.asm.commons;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/RemappingClassAdapter.class */
public class RemappingClassAdapter extends ClassVisitor {
    protected final Remapper remapper;
    protected String className;

    public RemappingClassAdapter(ClassVisitor classVisitor, Remapper remapper) {
        this(Opcodes.ASM5, classVisitor, remapper);
    }

    protected RemappingClassAdapter(int i2, ClassVisitor classVisitor, Remapper remapper) {
        super(i2, classVisitor);
        this.remapper = remapper;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        this.className = str;
        super.visit(i2, i3, this.remapper.mapType(str), this.remapper.mapSignature(str2, false), this.remapper.mapType(str3), strArr == null ? null : this.remapper.mapTypes(strArr));
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitAnnotation = super.visitAnnotation(this.remapper.mapDesc(str), z2);
        if (annotationVisitorVisitAnnotation == null) {
            return null;
        }
        return createRemappingAnnotationAdapter(annotationVisitorVisitAnnotation);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitTypeAnnotation = super.visitTypeAnnotation(i2, typePath, this.remapper.mapDesc(str), z2);
        if (annotationVisitorVisitTypeAnnotation == null) {
            return null;
        }
        return createRemappingAnnotationAdapter(annotationVisitorVisitTypeAnnotation);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int i2, String str, String str2, String str3, Object obj) {
        FieldVisitor fieldVisitorVisitField = super.visitField(i2, this.remapper.mapFieldName(this.className, str, str2), this.remapper.mapDesc(str2), this.remapper.mapSignature(str3, true), this.remapper.mapValue(obj));
        if (fieldVisitorVisitField == null) {
            return null;
        }
        return createRemappingFieldAdapter(fieldVisitorVisitField);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        String strMapMethodDesc = this.remapper.mapMethodDesc(str2);
        MethodVisitor methodVisitorVisitMethod = super.visitMethod(i2, this.remapper.mapMethodName(this.className, str, str2), strMapMethodDesc, this.remapper.mapSignature(str3, false), strArr == null ? null : this.remapper.mapTypes(strArr));
        if (methodVisitorVisitMethod == null) {
            return null;
        }
        return createRemappingMethodAdapter(i2, strMapMethodDesc, methodVisitorVisitMethod);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String str, String str2, String str3, int i2) {
        super.visitInnerClass(this.remapper.mapType(str), str2 == null ? null : this.remapper.mapType(str2), str3, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String str, String str2, String str3) {
        super.visitOuterClass(this.remapper.mapType(str), str2 == null ? null : this.remapper.mapMethodName(str, str2, str3), str3 == null ? null : this.remapper.mapMethodDesc(str3));
    }

    protected FieldVisitor createRemappingFieldAdapter(FieldVisitor fieldVisitor) {
        return new RemappingFieldAdapter(fieldVisitor, this.remapper);
    }

    protected MethodVisitor createRemappingMethodAdapter(int i2, String str, MethodVisitor methodVisitor) {
        return new RemappingMethodAdapter(i2, str, methodVisitor, this.remapper);
    }

    protected AnnotationVisitor createRemappingAnnotationAdapter(AnnotationVisitor annotationVisitor) {
        return new RemappingAnnotationAdapter(annotationVisitor, this.remapper);
    }
}
