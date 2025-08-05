package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/types/BytecodeNumericOps.class */
public interface BytecodeNumericOps {
    Type neg(MethodVisitor methodVisitor, int i2);

    Type sub(MethodVisitor methodVisitor, int i2);

    Type mul(MethodVisitor methodVisitor, int i2);

    Type div(MethodVisitor methodVisitor, int i2);

    Type rem(MethodVisitor methodVisitor, int i2);

    Type cmp(MethodVisitor methodVisitor, boolean z2);
}
