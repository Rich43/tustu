package jdk.internal.org.objectweb.asm.tree.analysis;

import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/AnalyzerException.class */
public class AnalyzerException extends Exception {
    public final AbstractInsnNode node;

    public AnalyzerException(AbstractInsnNode abstractInsnNode, String str) {
        super(str);
        this.node = abstractInsnNode;
    }

    public AnalyzerException(AbstractInsnNode abstractInsnNode, String str, Throwable th) {
        super(str, th);
        this.node = abstractInsnNode;
    }

    public AnalyzerException(AbstractInsnNode abstractInsnNode, String str, Object obj, Value value) {
        super((str == null ? "Expected " : str + ": expected ") + obj + ", but found " + ((Object) value));
        this.node = abstractInsnNode;
    }
}
