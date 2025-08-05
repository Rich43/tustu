package jdk.internal.org.objectweb.asm.tree;

import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/ParameterNode.class */
public class ParameterNode {
    public String name;
    public int access;

    public ParameterNode(String str, int i2) {
        this.name = str;
        this.access = i2;
    }

    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitParameter(this.name, this.access);
    }
}
