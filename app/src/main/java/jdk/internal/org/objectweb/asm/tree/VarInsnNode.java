package jdk.internal.org.objectweb.asm.tree;

import java.util.Map;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/VarInsnNode.class */
public class VarInsnNode extends AbstractInsnNode {
    public int var;

    public VarInsnNode(int i2, int i3) {
        super(i2);
        this.var = i3;
    }

    public void setOpcode(int i2) {
        this.opcode = i2;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 2;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitVarInsn(this.opcode, this.var);
        acceptAnnotations(methodVisitor);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> map) {
        return new VarInsnNode(this.opcode, this.var).cloneAnnotations(this);
    }
}
