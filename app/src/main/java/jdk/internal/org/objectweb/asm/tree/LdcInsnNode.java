package jdk.internal.org.objectweb.asm.tree;

import java.util.Map;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/LdcInsnNode.class */
public class LdcInsnNode extends AbstractInsnNode {
    public Object cst;

    public LdcInsnNode(Object obj) {
        super(18);
        this.cst = obj;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 9;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitLdcInsn(this.cst);
        acceptAnnotations(methodVisitor);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> map) {
        return new LdcInsnNode(this.cst).cloneAnnotations(this);
    }
}
