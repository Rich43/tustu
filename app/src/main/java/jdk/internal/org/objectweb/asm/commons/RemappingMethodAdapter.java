package jdk.internal.org.objectweb.asm.commons;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/RemappingMethodAdapter.class */
public class RemappingMethodAdapter extends LocalVariablesSorter {
    protected final Remapper remapper;

    public RemappingMethodAdapter(int i2, String str, MethodVisitor methodVisitor, Remapper remapper) {
        this(Opcodes.ASM5, i2, str, methodVisitor, remapper);
    }

    protected RemappingMethodAdapter(int i2, int i3, String str, MethodVisitor methodVisitor, Remapper remapper) {
        super(i2, i3, str, methodVisitor);
        this.remapper = remapper;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        AnnotationVisitor annotationVisitorVisitAnnotationDefault = super.visitAnnotationDefault();
        return annotationVisitorVisitAnnotationDefault == null ? annotationVisitorVisitAnnotationDefault : new RemappingAnnotationAdapter(annotationVisitorVisitAnnotationDefault, this.remapper);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitAnnotation = super.visitAnnotation(this.remapper.mapDesc(str), z2);
        return annotationVisitorVisitAnnotation == null ? annotationVisitorVisitAnnotation : new RemappingAnnotationAdapter(annotationVisitorVisitAnnotation, this.remapper);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitTypeAnnotation = super.visitTypeAnnotation(i2, typePath, this.remapper.mapDesc(str), z2);
        return annotationVisitorVisitTypeAnnotation == null ? annotationVisitorVisitTypeAnnotation : new RemappingAnnotationAdapter(annotationVisitorVisitTypeAnnotation, this.remapper);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int i2, String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitParameterAnnotation = super.visitParameterAnnotation(i2, this.remapper.mapDesc(str), z2);
        return annotationVisitorVisitParameterAnnotation == null ? annotationVisitorVisitParameterAnnotation : new RemappingAnnotationAdapter(annotationVisitorVisitParameterAnnotation, this.remapper);
    }

    @Override // jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter, jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        super.visitFrame(i2, i3, remapEntries(i3, objArr), i4, remapEntries(i4, objArr2));
    }

    private Object[] remapEntries(int i2, Object[] objArr) {
        int i3 = 0;
        while (i3 < i2) {
            if (!(objArr[i3] instanceof String)) {
                i3++;
            } else {
                Object[] objArr2 = new Object[i2];
                if (i3 > 0) {
                    System.arraycopy(objArr, 0, objArr2, 0, i3);
                }
                do {
                    Object obj = objArr[i3];
                    int i4 = i3;
                    i3++;
                    objArr2[i4] = obj instanceof String ? this.remapper.mapType((String) obj) : obj;
                } while (i3 < i2);
                return objArr2;
            }
        }
        return objArr;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        super.visitFieldInsn(i2, this.remapper.mapType(str), this.remapper.mapFieldName(str, str2, str3), this.remapper.mapDesc(str3));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    @Deprecated
    public void visitMethodInsn(int i2, String str, String str2, String str3) {
        if (this.api >= 327680) {
            super.visitMethodInsn(i2, str, str2, str3);
        } else {
            doVisitMethodInsn(i2, str, str2, str3, i2 == 185);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            super.visitMethodInsn(i2, str, str2, str3, z2);
        } else {
            doVisitMethodInsn(i2, str, str2, str3, z2);
        }
    }

    private void doVisitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (this.mv != null) {
            this.mv.visitMethodInsn(i2, this.remapper.mapType(str), this.remapper.mapMethodName(str, str2, str3), this.remapper.mapMethodDesc(str3), z2);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            objArr[i2] = this.remapper.mapValue(objArr[i2]);
        }
        super.visitInvokeDynamicInsn(this.remapper.mapInvokeDynamicMethodName(str, str2), this.remapper.mapMethodDesc(str2), (Handle) this.remapper.mapValue(handle), objArr);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i2, String str) {
        super.visitTypeInsn(i2, this.remapper.mapType(str));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        super.visitLdcInsn(this.remapper.mapValue(obj));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i2) {
        super.visitMultiANewArrayInsn(this.remapper.mapDesc(str), i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitInsnAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitInsnAnnotation = super.visitInsnAnnotation(i2, typePath, this.remapper.mapDesc(str), z2);
        return annotationVisitorVisitInsnAnnotation == null ? annotationVisitorVisitInsnAnnotation : new RemappingAnnotationAdapter(annotationVisitorVisitInsnAnnotation, this.remapper);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        super.visitTryCatchBlock(label, label2, label3, str == null ? null : this.remapper.mapType(str));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTryCatchAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitTryCatchAnnotation = super.visitTryCatchAnnotation(i2, typePath, this.remapper.mapDesc(str), z2);
        return annotationVisitorVisitTryCatchAnnotation == null ? annotationVisitorVisitTryCatchAnnotation : new RemappingAnnotationAdapter(annotationVisitorVisitTryCatchAnnotation, this.remapper);
    }

    @Override // jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter, jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2) {
        super.visitLocalVariable(str, this.remapper.mapDesc(str2), this.remapper.mapSignature(str3, true), label, label2, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter, jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitLocalVariableAnnotation = super.visitLocalVariableAnnotation(i2, typePath, labelArr, labelArr2, iArr, this.remapper.mapDesc(str), z2);
        return annotationVisitorVisitLocalVariableAnnotation == null ? annotationVisitorVisitLocalVariableAnnotation : new RemappingAnnotationAdapter(annotationVisitorVisitLocalVariableAnnotation, this.remapper);
    }
}
