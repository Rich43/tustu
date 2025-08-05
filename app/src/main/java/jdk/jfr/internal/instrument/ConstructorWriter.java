package jdk.jfr.internal.instrument;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/* loaded from: jfr.jar:jdk/jfr/internal/instrument/ConstructorWriter.class */
final class ConstructorWriter extends MethodVisitor {
    private boolean useInputParameter;
    private String shortClassName;
    private String fullClassName;

    ConstructorWriter(Class<?> cls, boolean z2) {
        super(Opcodes.ASM5);
        this.useInputParameter = z2;
        this.shortClassName = cls.getSimpleName();
        this.fullClassName = cls.getName().replace('.', '/');
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i2) {
        if (i2 == 177) {
            if (this.useInputParameter) {
                useInput();
            } else {
                noInput();
            }
        }
        this.mv.visitInsn(i2);
    }

    private void useInput() {
        this.mv.visitVarInsn(25, 0);
        this.mv.visitVarInsn(25, 1);
        this.mv.visitMethodInsn(184, "jdk/jfr/internal/instrument/ThrowableTracer", "trace" + this.shortClassName, "(L" + this.fullClassName + ";Ljava/lang/String;)V");
    }

    private void noInput() {
        this.mv.visitVarInsn(25, 0);
        this.mv.visitInsn(1);
        this.mv.visitMethodInsn(184, "jdk/jfr/internal/instrument/ThrowableTracer", "trace" + this.shortClassName, "(L" + this.fullClassName + ";Ljava/lang/String;)V");
    }

    public void setMethodVisitor(MethodVisitor methodVisitor) {
        this.mv = methodVisitor;
    }
}
