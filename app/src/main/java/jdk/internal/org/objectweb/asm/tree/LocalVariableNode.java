package jdk.internal.org.objectweb.asm.tree;

import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/LocalVariableNode.class */
public class LocalVariableNode {
    public String name;
    public String desc;
    public String signature;
    public LabelNode start;
    public LabelNode end;
    public int index;

    public LocalVariableNode(String str, String str2, String str3, LabelNode labelNode, LabelNode labelNode2, int i2) {
        this.name = str;
        this.desc = str2;
        this.signature = str3;
        this.start = labelNode;
        this.end = labelNode2;
        this.index = i2;
    }

    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitLocalVariable(this.name, this.desc, this.signature, this.start.getLabel(), this.end.getLabel(), this.index);
    }
}
