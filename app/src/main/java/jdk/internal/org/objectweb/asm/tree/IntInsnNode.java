package jdk.internal.org.objectweb.asm.tree;

import java.util.Map;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/IntInsnNode.class */
public class IntInsnNode extends AbstractInsnNode {
    public int operand;

    public IntInsnNode(int i2, int i3) {
        super(i2);
        this.operand = i3;
    }

    public void setOpcode(int i2) {
        this.opcode = i2;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 1;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitIntInsn(this.opcode, this.operand);
        acceptAnnotations(methodVisitor);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> map) {
        return new IntInsnNode(this.opcode, this.operand).cloneAnnotations(this);
    }
}
