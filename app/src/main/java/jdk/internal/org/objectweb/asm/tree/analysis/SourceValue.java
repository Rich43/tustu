package jdk.internal.org.objectweb.asm.tree.analysis;

import java.util.Set;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/SourceValue.class */
public class SourceValue implements Value {
    public final int size;
    public final Set<AbstractInsnNode> insns;

    public SourceValue(int i2) {
        this(i2, (Set<AbstractInsnNode>) SmallSet.emptySet());
    }

    public SourceValue(int i2, AbstractInsnNode abstractInsnNode) {
        this.size = i2;
        this.insns = new SmallSet(abstractInsnNode, null);
    }

    public SourceValue(int i2, Set<AbstractInsnNode> set) {
        this.size = i2;
        this.insns = set;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Value
    public int getSize() {
        return this.size;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SourceValue)) {
            return false;
        }
        SourceValue sourceValue = (SourceValue) obj;
        return this.size == sourceValue.size && this.insns.equals(sourceValue.insns);
    }

    public int hashCode() {
        return this.insns.hashCode();
    }
}
