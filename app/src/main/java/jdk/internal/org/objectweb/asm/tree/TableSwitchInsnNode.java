package jdk.internal.org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/TableSwitchInsnNode.class */
public class TableSwitchInsnNode extends AbstractInsnNode {
    public int min;
    public int max;
    public LabelNode dflt;
    public List<LabelNode> labels;

    public TableSwitchInsnNode(int i2, int i3, LabelNode labelNode, LabelNode... labelNodeArr) {
        super(170);
        this.min = i2;
        this.max = i3;
        this.dflt = labelNode;
        this.labels = new ArrayList();
        if (labelNodeArr != null) {
            this.labels.addAll(Arrays.asList(labelNodeArr));
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 11;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        Label[] labelArr = new Label[this.labels.size()];
        for (int i2 = 0; i2 < labelArr.length; i2++) {
            labelArr[i2] = this.labels.get(i2).getLabel();
        }
        methodVisitor.visitTableSwitchInsn(this.min, this.max, this.dflt.getLabel(), labelArr);
        acceptAnnotations(methodVisitor);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> map) {
        return new TableSwitchInsnNode(this.min, this.max, clone(this.dflt, map), clone(this.labels, map)).cloneAnnotations(this);
    }
}
