package jdk.internal.org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/FrameNode.class */
public class FrameNode extends AbstractInsnNode {
    public int type;
    public List<Object> local;
    public List<Object> stack;

    private FrameNode() {
        super(-1);
    }

    public FrameNode(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        super(-1);
        this.type = i2;
        switch (i2) {
            case -1:
            case 0:
                this.local = asList(i3, objArr);
                this.stack = asList(i4, objArr2);
                break;
            case 1:
                this.local = asList(i3, objArr);
                break;
            case 2:
                this.local = Arrays.asList(new Object[i3]);
                break;
            case 4:
                this.stack = asList(1, objArr2);
                break;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 14;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        switch (this.type) {
            case -1:
            case 0:
                methodVisitor.visitFrame(this.type, this.local.size(), asArray(this.local), this.stack.size(), asArray(this.stack));
                break;
            case 1:
                methodVisitor.visitFrame(this.type, this.local.size(), asArray(this.local), 0, null);
                break;
            case 2:
                methodVisitor.visitFrame(this.type, this.local.size(), null, 0, null);
                break;
            case 3:
                methodVisitor.visitFrame(this.type, 0, null, 0, null);
                break;
            case 4:
                methodVisitor.visitFrame(this.type, 0, null, 1, asArray(this.stack));
                break;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> map) {
        FrameNode frameNode = new FrameNode();
        frameNode.type = this.type;
        if (this.local != null) {
            frameNode.local = new ArrayList();
            for (int i2 = 0; i2 < this.local.size(); i2++) {
                Object obj = this.local.get(i2);
                if (obj instanceof LabelNode) {
                    obj = map.get(obj);
                }
                frameNode.local.add(obj);
            }
        }
        if (this.stack != null) {
            frameNode.stack = new ArrayList();
            for (int i3 = 0; i3 < this.stack.size(); i3++) {
                Object obj2 = this.stack.get(i3);
                if (obj2 instanceof LabelNode) {
                    obj2 = map.get(obj2);
                }
                frameNode.stack.add(obj2);
            }
        }
        return frameNode;
    }

    private static List<Object> asList(int i2, Object[] objArr) {
        return Arrays.asList(objArr).subList(0, i2);
    }

    private static Object[] asArray(List<Object> list) {
        Object[] objArr = new Object[list.size()];
        for (int i2 = 0; i2 < objArr.length; i2++) {
            Object label = list.get(i2);
            if (label instanceof LabelNode) {
                label = ((LabelNode) label).getLabel();
            }
            objArr[i2] = label;
        }
        return objArr;
    }
}
