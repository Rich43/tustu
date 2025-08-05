package jdk.internal.org.objectweb.asm.tree;

import java.util.Map;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/InvokeDynamicInsnNode.class */
public class InvokeDynamicInsnNode extends AbstractInsnNode {
    public String name;
    public String desc;
    public Handle bsm;
    public Object[] bsmArgs;

    public InvokeDynamicInsnNode(String str, String str2, Handle handle, Object... objArr) {
        super(186);
        this.name = str;
        this.desc = str2;
        this.bsm = handle;
        this.bsmArgs = objArr;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 6;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitInvokeDynamicInsn(this.name, this.desc, this.bsm, this.bsmArgs);
        acceptAnnotations(methodVisitor);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> map) {
        return new InvokeDynamicInsnNode(this.name, this.desc, this.bsm, this.bsmArgs).cloneAnnotations(this);
    }
}
