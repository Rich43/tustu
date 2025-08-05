package jdk.internal.org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/LookupSwitchInsnNode.class */
public class LookupSwitchInsnNode extends AbstractInsnNode {
    public LabelNode dflt;
    public List<Integer> keys;
    public List<LabelNode> labels;

    public LookupSwitchInsnNode(LabelNode labelNode, int[] iArr, LabelNode[] labelNodeArr) {
        super(171);
        this.dflt = labelNode;
        this.keys = new ArrayList(iArr == null ? 0 : iArr.length);
        this.labels = new ArrayList(labelNodeArr == null ? 0 : labelNodeArr.length);
        if (iArr != null) {
            for (int i2 : iArr) {
                this.keys.add(Integer.valueOf(i2));
            }
        }
        if (labelNodeArr != null) {
            this.labels.addAll(Arrays.asList(labelNodeArr));
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 12;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        int[] iArr = new int[this.keys.size()];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = this.keys.get(i2).intValue();
        }
        Label[] labelArr = new Label[this.labels.size()];
        for (int i3 = 0; i3 < labelArr.length; i3++) {
            labelArr[i3] = this.labels.get(i3).getLabel();
        }
        methodVisitor.visitLookupSwitchInsn(this.dflt.getLabel(), iArr, labelArr);
        acceptAnnotations(methodVisitor);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> map) {
        LookupSwitchInsnNode lookupSwitchInsnNode = new LookupSwitchInsnNode(clone(this.dflt, map), null, clone(this.labels, map));
        lookupSwitchInsnNode.keys.addAll(this.keys);
        return lookupSwitchInsnNode.cloneAnnotations(this);
    }
}
