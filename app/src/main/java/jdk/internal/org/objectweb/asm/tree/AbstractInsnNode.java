package jdk.internal.org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/AbstractInsnNode.class */
public abstract class AbstractInsnNode {
    public static final int INSN = 0;
    public static final int INT_INSN = 1;
    public static final int VAR_INSN = 2;
    public static final int TYPE_INSN = 3;
    public static final int FIELD_INSN = 4;
    public static final int METHOD_INSN = 5;
    public static final int INVOKE_DYNAMIC_INSN = 6;
    public static final int JUMP_INSN = 7;
    public static final int LABEL = 8;
    public static final int LDC_INSN = 9;
    public static final int IINC_INSN = 10;
    public static final int TABLESWITCH_INSN = 11;
    public static final int LOOKUPSWITCH_INSN = 12;
    public static final int MULTIANEWARRAY_INSN = 13;
    public static final int FRAME = 14;
    public static final int LINE = 15;
    protected int opcode;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    AbstractInsnNode prev;
    AbstractInsnNode next;
    int index = -1;

    public abstract int getType();

    public abstract void accept(MethodVisitor methodVisitor);

    public abstract AbstractInsnNode clone(Map<LabelNode, LabelNode> map);

    protected AbstractInsnNode(int i2) {
        this.opcode = i2;
    }

    public int getOpcode() {
        return this.opcode;
    }

    public AbstractInsnNode getPrevious() {
        return this.prev;
    }

    public AbstractInsnNode getNext() {
        return this.next;
    }

    protected final void acceptAnnotations(MethodVisitor methodVisitor) {
        int size = this.visibleTypeAnnotations == null ? 0 : this.visibleTypeAnnotations.size();
        for (int i2 = 0; i2 < size; i2++) {
            TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(i2);
            typeAnnotationNode.accept(methodVisitor.visitInsnAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
        }
        int size2 = this.invisibleTypeAnnotations == null ? 0 : this.invisibleTypeAnnotations.size();
        for (int i3 = 0; i3 < size2; i3++) {
            TypeAnnotationNode typeAnnotationNode2 = this.invisibleTypeAnnotations.get(i3);
            typeAnnotationNode2.accept(methodVisitor.visitInsnAnnotation(typeAnnotationNode2.typeRef, typeAnnotationNode2.typePath, typeAnnotationNode2.desc, false));
        }
    }

    static LabelNode clone(LabelNode labelNode, Map<LabelNode, LabelNode> map) {
        return map.get(labelNode);
    }

    static LabelNode[] clone(List<LabelNode> list, Map<LabelNode, LabelNode> map) {
        LabelNode[] labelNodeArr = new LabelNode[list.size()];
        for (int i2 = 0; i2 < labelNodeArr.length; i2++) {
            labelNodeArr[i2] = map.get(list.get(i2));
        }
        return labelNodeArr;
    }

    protected final AbstractInsnNode cloneAnnotations(AbstractInsnNode abstractInsnNode) {
        if (abstractInsnNode.visibleTypeAnnotations != null) {
            this.visibleTypeAnnotations = new ArrayList();
            for (int i2 = 0; i2 < abstractInsnNode.visibleTypeAnnotations.size(); i2++) {
                TypeAnnotationNode typeAnnotationNode = abstractInsnNode.visibleTypeAnnotations.get(i2);
                TypeAnnotationNode typeAnnotationNode2 = new TypeAnnotationNode(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc);
                typeAnnotationNode.accept(typeAnnotationNode2);
                this.visibleTypeAnnotations.add(typeAnnotationNode2);
            }
        }
        if (abstractInsnNode.invisibleTypeAnnotations != null) {
            this.invisibleTypeAnnotations = new ArrayList();
            for (int i3 = 0; i3 < abstractInsnNode.invisibleTypeAnnotations.size(); i3++) {
                TypeAnnotationNode typeAnnotationNode3 = abstractInsnNode.invisibleTypeAnnotations.get(i3);
                TypeAnnotationNode typeAnnotationNode4 = new TypeAnnotationNode(typeAnnotationNode3.typeRef, typeAnnotationNode3.typePath, typeAnnotationNode3.desc);
                typeAnnotationNode3.accept(typeAnnotationNode4);
                this.invisibleTypeAnnotations.add(typeAnnotationNode4);
            }
        }
        return this;
    }
}
