package jdk.nashorn.internal.codegen.types;

import jdk.internal.org.objectweb.asm.MethodVisitor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/types/BytecodeBitwiseOps.class */
public interface BytecodeBitwiseOps {
    Type shr(MethodVisitor methodVisitor);

    Type sar(MethodVisitor methodVisitor);

    Type shl(MethodVisitor methodVisitor);

    Type and(MethodVisitor methodVisitor);

    Type or(MethodVisitor methodVisitor);

    Type xor(MethodVisitor methodVisitor);

    Type cmp(MethodVisitor methodVisitor);
}
