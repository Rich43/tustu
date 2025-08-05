package jdk.internal.org.objectweb.asm.tree;

import java.util.Map;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/MethodInsnNode.class */
public class MethodInsnNode extends AbstractInsnNode {
    public String owner;
    public String name;
    public String desc;
    public boolean itf;

    @Deprecated
    public MethodInsnNode(int i2, String str, String str2, String str3) {
        this(i2, str, str2, str3, i2 == 185);
    }

    public MethodInsnNode(int i2, String str, String str2, String str3, boolean z2) {
        super(i2);
        this.owner = str;
        this.name = str2;
        this.desc = str3;
        this.itf = z2;
    }

    public void setOpcode(int i2) {
        this.opcode = i2;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 5;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitMethodInsn(this.opcode, this.owner, this.name, this.desc, this.itf);
        acceptAnnotations(methodVisitor);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> map) {
        return new MethodInsnNode(this.opcode, this.owner, this.name, this.desc, this.itf);
    }
}
