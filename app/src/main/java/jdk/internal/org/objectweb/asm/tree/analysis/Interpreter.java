package jdk.internal.org.objectweb.asm.tree.analysis;

import java.util.List;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/Interpreter.class */
public abstract class Interpreter<V extends Value> {
    protected final int api;

    public abstract V newValue(Type type);

    public abstract V newOperation(AbstractInsnNode abstractInsnNode) throws AnalyzerException;

    public abstract V copyOperation(AbstractInsnNode abstractInsnNode, V v2) throws AnalyzerException;

    public abstract V unaryOperation(AbstractInsnNode abstractInsnNode, V v2) throws AnalyzerException;

    public abstract V binaryOperation(AbstractInsnNode abstractInsnNode, V v2, V v3) throws AnalyzerException;

    public abstract V ternaryOperation(AbstractInsnNode abstractInsnNode, V v2, V v3, V v4) throws AnalyzerException;

    public abstract V naryOperation(AbstractInsnNode abstractInsnNode, List<? extends V> list) throws AnalyzerException;

    public abstract void returnOperation(AbstractInsnNode abstractInsnNode, V v2, V v3) throws AnalyzerException;

    public abstract V merge(V v2, V v3);

    protected Interpreter(int i2) {
        this.api = i2;
    }
}
