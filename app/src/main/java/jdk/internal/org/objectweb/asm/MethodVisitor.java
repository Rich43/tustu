package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/MethodVisitor.class */
public abstract class MethodVisitor {
    protected final int api;
    protected MethodVisitor mv;

    public MethodVisitor(int i2) {
        this(i2, null);
    }

    public MethodVisitor(int i2, MethodVisitor methodVisitor) {
        if (i2 != 262144 && i2 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = i2;
        this.mv = methodVisitor;
    }

    public void visitParameter(String str, int i2) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            this.mv.visitParameter(str, i2);
        }
    }

    public AnnotationVisitor visitAnnotationDefault() {
        if (this.mv != null) {
            return this.mv.visitAnnotationDefault();
        }
        return null;
    }

    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        if (this.mv != null) {
            return this.mv.visitAnnotation(str, z2);
        }
        return null;
    }

    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            return this.mv.visitTypeAnnotation(i2, typePath, str, z2);
        }
        return null;
    }

    public AnnotationVisitor visitParameterAnnotation(int i2, String str, boolean z2) {
        if (this.mv != null) {
            return this.mv.visitParameterAnnotation(i2, str, z2);
        }
        return null;
    }

    public void visitAttribute(Attribute attribute) {
        if (this.mv != null) {
            this.mv.visitAttribute(attribute);
        }
    }

    public void visitCode() {
        if (this.mv != null) {
            this.mv.visitCode();
        }
    }

    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        if (this.mv != null) {
            this.mv.visitFrame(i2, i3, objArr, i4, objArr2);
        }
    }

    public void visitInsn(int i2) {
        if (this.mv != null) {
            this.mv.visitInsn(i2);
        }
    }

    public void visitIntInsn(int i2, int i3) {
        if (this.mv != null) {
            this.mv.visitIntInsn(i2, i3);
        }
    }

    public void visitVarInsn(int i2, int i3) {
        if (this.mv != null) {
            this.mv.visitVarInsn(i2, i3);
        }
    }

    public void visitTypeInsn(int i2, String str) {
        if (this.mv != null) {
            this.mv.visitTypeInsn(i2, str);
        }
    }

    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        if (this.mv != null) {
            this.mv.visitFieldInsn(i2, str, str2, str3);
        }
    }

    @Deprecated
    public void visitMethodInsn(int i2, String str, String str2, String str3) {
        if (this.api >= 327680) {
            visitMethodInsn(i2, str, str2, str3, i2 == 185);
        } else if (this.mv != null) {
            this.mv.visitMethodInsn(i2, str, str2, str3);
        }
    }

    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            if (z2 != (i2 == 185)) {
                throw new IllegalArgumentException("INVOKESPECIAL/STATIC on interfaces require ASM 5");
            }
            visitMethodInsn(i2, str, str2, str3);
        } else if (this.mv != null) {
            this.mv.visitMethodInsn(i2, str, str2, str3, z2);
        }
    }

    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        if (this.mv != null) {
            this.mv.visitInvokeDynamicInsn(str, str2, handle, objArr);
        }
    }

    public void visitJumpInsn(int i2, Label label) {
        if (this.mv != null) {
            this.mv.visitJumpInsn(i2, label);
        }
    }

    public void visitLabel(Label label) {
        if (this.mv != null) {
            this.mv.visitLabel(label);
        }
    }

    public void visitLdcInsn(Object obj) {
        if (this.mv != null) {
            this.mv.visitLdcInsn(obj);
        }
    }

    public void visitIincInsn(int i2, int i3) {
        if (this.mv != null) {
            this.mv.visitIincInsn(i2, i3);
        }
    }

    public void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr) {
        if (this.mv != null) {
            this.mv.visitTableSwitchInsn(i2, i3, label, labelArr);
        }
    }

    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        if (this.mv != null) {
            this.mv.visitLookupSwitchInsn(label, iArr, labelArr);
        }
    }

    public void visitMultiANewArrayInsn(String str, int i2) {
        if (this.mv != null) {
            this.mv.visitMultiANewArrayInsn(str, i2);
        }
    }

    public AnnotationVisitor visitInsnAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            return this.mv.visitInsnAnnotation(i2, typePath, str, z2);
        }
        return null;
    }

    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        if (this.mv != null) {
            this.mv.visitTryCatchBlock(label, label2, label3, str);
        }
    }

    public AnnotationVisitor visitTryCatchAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            return this.mv.visitTryCatchAnnotation(i2, typePath, str, z2);
        }
        return null;
    }

    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2) {
        if (this.mv != null) {
            this.mv.visitLocalVariable(str, str2, str3, label, label2, i2);
        }
    }

    public AnnotationVisitor visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            return this.mv.visitLocalVariableAnnotation(i2, typePath, labelArr, labelArr2, iArr, str, z2);
        }
        return null;
    }

    public void visitLineNumber(int i2, Label label) {
        if (this.mv != null) {
            this.mv.visitLineNumber(i2, label);
        }
    }

    public void visitMaxs(int i2, int i3) {
        if (this.mv != null) {
            this.mv.visitMaxs(i2, i3);
        }
    }

    public void visitEnd() {
        if (this.mv != null) {
            this.mv.visitEnd();
        }
    }
}
