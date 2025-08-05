package jdk.internal.org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/MethodNode.class */
public class MethodNode extends MethodVisitor {
    public int access;
    public String name;
    public String desc;
    public String signature;
    public List<String> exceptions;
    public List<ParameterNode> parameters;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<Attribute> attrs;
    public Object annotationDefault;
    public List<AnnotationNode>[] visibleParameterAnnotations;
    public List<AnnotationNode>[] invisibleParameterAnnotations;
    public InsnList instructions;
    public List<TryCatchBlockNode> tryCatchBlocks;
    public int maxStack;
    public int maxLocals;
    public List<LocalVariableNode> localVariables;
    public List<LocalVariableAnnotationNode> visibleLocalVariableAnnotations;
    public List<LocalVariableAnnotationNode> invisibleLocalVariableAnnotations;
    private boolean visited;

    public MethodNode() {
        this(Opcodes.ASM5);
        if (getClass() != MethodNode.class) {
            throw new IllegalStateException();
        }
    }

    public MethodNode(int i2) {
        super(i2);
        this.instructions = new InsnList();
    }

    public MethodNode(int i2, String str, String str2, String str3, String[] strArr) {
        this(Opcodes.ASM5, i2, str, str2, str3, strArr);
        if (getClass() != MethodNode.class) {
            throw new IllegalStateException();
        }
    }

    public MethodNode(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        super(i2);
        this.access = i3;
        this.name = str;
        this.desc = str2;
        this.signature = str3;
        this.exceptions = new ArrayList(strArr == null ? 0 : strArr.length);
        if (!((i3 & 1024) != 0)) {
            this.localVariables = new ArrayList(5);
        }
        this.tryCatchBlocks = new ArrayList();
        if (strArr != null) {
            this.exceptions.addAll(Arrays.asList(strArr));
        }
        this.instructions = new InsnList();
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitParameter(String str, int i2) {
        if (this.parameters == null) {
            this.parameters = new ArrayList(5);
        }
        this.parameters.add(new ParameterNode(str, i2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        return new AnnotationNode(new ArrayList<Object>(0) { // from class: jdk.internal.org.objectweb.asm.tree.MethodNode.1
            @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
            public boolean add(Object obj) {
                MethodNode.this.annotationDefault = obj;
                return super.add(obj);
            }
        });
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        AnnotationNode annotationNode = new AnnotationNode(str);
        if (z2) {
            if (this.visibleAnnotations == null) {
                this.visibleAnnotations = new ArrayList(1);
            }
            this.visibleAnnotations.add(annotationNode);
        } else {
            if (this.invisibleAnnotations == null) {
                this.invisibleAnnotations = new ArrayList(1);
            }
            this.invisibleAnnotations.add(annotationNode);
        }
        return annotationNode;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(i2, typePath, str);
        if (z2) {
            if (this.visibleTypeAnnotations == null) {
                this.visibleTypeAnnotations = new ArrayList(1);
            }
            this.visibleTypeAnnotations.add(typeAnnotationNode);
        } else {
            if (this.invisibleTypeAnnotations == null) {
                this.invisibleTypeAnnotations = new ArrayList(1);
            }
            this.invisibleTypeAnnotations.add(typeAnnotationNode);
        }
        return typeAnnotationNode;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int i2, String str, boolean z2) {
        AnnotationNode annotationNode = new AnnotationNode(str);
        if (z2) {
            if (this.visibleParameterAnnotations == null) {
                this.visibleParameterAnnotations = new List[Type.getArgumentTypes(this.desc).length];
            }
            if (this.visibleParameterAnnotations[i2] == null) {
                this.visibleParameterAnnotations[i2] = new ArrayList(1);
            }
            this.visibleParameterAnnotations[i2].add(annotationNode);
        } else {
            if (this.invisibleParameterAnnotations == null) {
                this.invisibleParameterAnnotations = new List[Type.getArgumentTypes(this.desc).length];
            }
            if (this.invisibleParameterAnnotations[i2] == null) {
                this.invisibleParameterAnnotations[i2] = new ArrayList(1);
            }
            this.invisibleParameterAnnotations[i2].add(annotationNode);
        }
        return annotationNode;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attribute) {
        if (this.attrs == null) {
            this.attrs = new ArrayList(1);
        }
        this.attrs.add(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitCode() {
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        this.instructions.add(new FrameNode(i2, i3, objArr == null ? null : getLabelNodes(objArr), i4, objArr2 == null ? null : getLabelNodes(objArr2)));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i2) {
        this.instructions.add(new InsnNode(i2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int i2, int i3) {
        this.instructions.add(new IntInsnNode(i2, i3));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i2, int i3) {
        this.instructions.add(new VarInsnNode(i2, i3));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i2, String str) {
        this.instructions.add(new TypeInsnNode(i2, str));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        this.instructions.add(new FieldInsnNode(i2, str, str2, str3));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    @Deprecated
    public void visitMethodInsn(int i2, String str, String str2, String str3) {
        if (this.api >= 327680) {
            super.visitMethodInsn(i2, str, str2, str3);
        } else {
            this.instructions.add(new MethodInsnNode(i2, str, str2, str3));
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            super.visitMethodInsn(i2, str, str2, str3, z2);
        } else {
            this.instructions.add(new MethodInsnNode(i2, str, str2, str3, z2));
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        this.instructions.add(new InvokeDynamicInsnNode(str, str2, handle, objArr));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i2, Label label) {
        this.instructions.add(new JumpInsnNode(i2, getLabelNode(label)));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        this.instructions.add(getLabelNode(label));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        this.instructions.add(new LdcInsnNode(obj));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i2, int i3) {
        this.instructions.add(new IincInsnNode(i2, i3));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr) {
        this.instructions.add(new TableSwitchInsnNode(i2, i3, getLabelNode(label), getLabelNodes(labelArr)));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        this.instructions.add(new LookupSwitchInsnNode(getLabelNode(label), iArr, getLabelNodes(labelArr)));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i2) {
        this.instructions.add(new MultiANewArrayInsnNode(str, i2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitInsnAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        AbstractInsnNode abstractInsnNode;
        AbstractInsnNode last = this.instructions.getLast();
        while (true) {
            abstractInsnNode = last;
            if (abstractInsnNode.getOpcode() != -1) {
                break;
            }
            last = abstractInsnNode.getPrevious();
        }
        TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(i2, typePath, str);
        if (z2) {
            if (abstractInsnNode.visibleTypeAnnotations == null) {
                abstractInsnNode.visibleTypeAnnotations = new ArrayList(1);
            }
            abstractInsnNode.visibleTypeAnnotations.add(typeAnnotationNode);
        } else {
            if (abstractInsnNode.invisibleTypeAnnotations == null) {
                abstractInsnNode.invisibleTypeAnnotations = new ArrayList(1);
            }
            abstractInsnNode.invisibleTypeAnnotations.add(typeAnnotationNode);
        }
        return typeAnnotationNode;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.tryCatchBlocks.add(new TryCatchBlockNode(getLabelNode(label), getLabelNode(label2), getLabelNode(label3), str));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTryCatchAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        TryCatchBlockNode tryCatchBlockNode = this.tryCatchBlocks.get((i2 & 16776960) >> 8);
        TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(i2, typePath, str);
        if (z2) {
            if (tryCatchBlockNode.visibleTypeAnnotations == null) {
                tryCatchBlockNode.visibleTypeAnnotations = new ArrayList(1);
            }
            tryCatchBlockNode.visibleTypeAnnotations.add(typeAnnotationNode);
        } else {
            if (tryCatchBlockNode.invisibleTypeAnnotations == null) {
                tryCatchBlockNode.invisibleTypeAnnotations = new ArrayList(1);
            }
            tryCatchBlockNode.invisibleTypeAnnotations.add(typeAnnotationNode);
        }
        return typeAnnotationNode;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2) {
        this.localVariables.add(new LocalVariableNode(str, str2, str3, getLabelNode(label), getLabelNode(label2), i2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        LocalVariableAnnotationNode localVariableAnnotationNode = new LocalVariableAnnotationNode(i2, typePath, getLabelNodes(labelArr), getLabelNodes(labelArr2), iArr, str);
        if (z2) {
            if (this.visibleLocalVariableAnnotations == null) {
                this.visibleLocalVariableAnnotations = new ArrayList(1);
            }
            this.visibleLocalVariableAnnotations.add(localVariableAnnotationNode);
        } else {
            if (this.invisibleLocalVariableAnnotations == null) {
                this.invisibleLocalVariableAnnotations = new ArrayList(1);
            }
            this.invisibleLocalVariableAnnotations.add(localVariableAnnotationNode);
        }
        return localVariableAnnotationNode;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int i2, Label label) {
        this.instructions.add(new LineNumberNode(i2, getLabelNode(label)));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i2, int i3) {
        this.maxStack = i2;
        this.maxLocals = i3;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
    }

    protected LabelNode getLabelNode(Label label) {
        if (!(label.info instanceof LabelNode)) {
            label.info = new LabelNode();
        }
        return (LabelNode) label.info;
    }

    private LabelNode[] getLabelNodes(Label[] labelArr) {
        LabelNode[] labelNodeArr = new LabelNode[labelArr.length];
        for (int i2 = 0; i2 < labelArr.length; i2++) {
            labelNodeArr[i2] = getLabelNode(labelArr[i2]);
        }
        return labelNodeArr;
    }

    private Object[] getLabelNodes(Object[] objArr) {
        Object[] objArr2 = new Object[objArr.length];
        for (int i2 = 0; i2 < objArr.length; i2++) {
            Object labelNode = objArr[i2];
            if (labelNode instanceof Label) {
                labelNode = getLabelNode((Label) labelNode);
            }
            objArr2[i2] = labelNode;
        }
        return objArr2;
    }

    public void check(int i2) {
        if (i2 == 262144) {
            if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations.size() > 0) {
                throw new RuntimeException();
            }
            if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations.size() > 0) {
                throw new RuntimeException();
            }
            int size = this.tryCatchBlocks == null ? 0 : this.tryCatchBlocks.size();
            for (int i3 = 0; i3 < size; i3++) {
                TryCatchBlockNode tryCatchBlockNode = this.tryCatchBlocks.get(i3);
                if (tryCatchBlockNode.visibleTypeAnnotations != null && tryCatchBlockNode.visibleTypeAnnotations.size() > 0) {
                    throw new RuntimeException();
                }
                if (tryCatchBlockNode.invisibleTypeAnnotations != null && tryCatchBlockNode.invisibleTypeAnnotations.size() > 0) {
                    throw new RuntimeException();
                }
            }
            for (int i4 = 0; i4 < this.instructions.size(); i4++) {
                AbstractInsnNode abstractInsnNode = this.instructions.get(i4);
                if (abstractInsnNode.visibleTypeAnnotations != null && abstractInsnNode.visibleTypeAnnotations.size() > 0) {
                    throw new RuntimeException();
                }
                if (abstractInsnNode.invisibleTypeAnnotations != null && abstractInsnNode.invisibleTypeAnnotations.size() > 0) {
                    throw new RuntimeException();
                }
                if (abstractInsnNode instanceof MethodInsnNode) {
                    if (((MethodInsnNode) abstractInsnNode).itf != (abstractInsnNode.opcode == 185)) {
                        throw new RuntimeException();
                    }
                }
            }
            if (this.visibleLocalVariableAnnotations != null && this.visibleLocalVariableAnnotations.size() > 0) {
                throw new RuntimeException();
            }
            if (this.invisibleLocalVariableAnnotations != null && this.invisibleLocalVariableAnnotations.size() > 0) {
                throw new RuntimeException();
            }
        }
    }

    public void accept(ClassVisitor classVisitor) {
        String[] strArr = new String[this.exceptions.size()];
        this.exceptions.toArray(strArr);
        MethodVisitor methodVisitorVisitMethod = classVisitor.visitMethod(this.access, this.name, this.desc, this.signature, strArr);
        if (methodVisitorVisitMethod != null) {
            accept(methodVisitorVisitMethod);
        }
    }

    public void accept(MethodVisitor methodVisitor) {
        int size = this.parameters == null ? 0 : this.parameters.size();
        for (int i2 = 0; i2 < size; i2++) {
            ParameterNode parameterNode = this.parameters.get(i2);
            methodVisitor.visitParameter(parameterNode.name, parameterNode.access);
        }
        if (this.annotationDefault != null) {
            AnnotationVisitor annotationVisitorVisitAnnotationDefault = methodVisitor.visitAnnotationDefault();
            AnnotationNode.accept(annotationVisitorVisitAnnotationDefault, null, this.annotationDefault);
            if (annotationVisitorVisitAnnotationDefault != null) {
                annotationVisitorVisitAnnotationDefault.visitEnd();
            }
        }
        int size2 = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();
        for (int i3 = 0; i3 < size2; i3++) {
            AnnotationNode annotationNode = this.visibleAnnotations.get(i3);
            annotationNode.accept(methodVisitor.visitAnnotation(annotationNode.desc, true));
        }
        int size3 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();
        for (int i4 = 0; i4 < size3; i4++) {
            AnnotationNode annotationNode2 = this.invisibleAnnotations.get(i4);
            annotationNode2.accept(methodVisitor.visitAnnotation(annotationNode2.desc, false));
        }
        int size4 = this.visibleTypeAnnotations == null ? 0 : this.visibleTypeAnnotations.size();
        for (int i5 = 0; i5 < size4; i5++) {
            TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(i5);
            typeAnnotationNode.accept(methodVisitor.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
        }
        int size5 = this.invisibleTypeAnnotations == null ? 0 : this.invisibleTypeAnnotations.size();
        for (int i6 = 0; i6 < size5; i6++) {
            TypeAnnotationNode typeAnnotationNode2 = this.invisibleTypeAnnotations.get(i6);
            typeAnnotationNode2.accept(methodVisitor.visitTypeAnnotation(typeAnnotationNode2.typeRef, typeAnnotationNode2.typePath, typeAnnotationNode2.desc, false));
        }
        int length = this.visibleParameterAnnotations == null ? 0 : this.visibleParameterAnnotations.length;
        for (int i7 = 0; i7 < length; i7++) {
            List<AnnotationNode> list = this.visibleParameterAnnotations[i7];
            if (list != null) {
                for (int i8 = 0; i8 < list.size(); i8++) {
                    AnnotationNode annotationNode3 = list.get(i8);
                    annotationNode3.accept(methodVisitor.visitParameterAnnotation(i7, annotationNode3.desc, true));
                }
            }
        }
        int length2 = this.invisibleParameterAnnotations == null ? 0 : this.invisibleParameterAnnotations.length;
        for (int i9 = 0; i9 < length2; i9++) {
            List<AnnotationNode> list2 = this.invisibleParameterAnnotations[i9];
            if (list2 != null) {
                for (int i10 = 0; i10 < list2.size(); i10++) {
                    AnnotationNode annotationNode4 = list2.get(i10);
                    annotationNode4.accept(methodVisitor.visitParameterAnnotation(i9, annotationNode4.desc, false));
                }
            }
        }
        if (this.visited) {
            this.instructions.resetLabels();
        }
        int size6 = this.attrs == null ? 0 : this.attrs.size();
        for (int i11 = 0; i11 < size6; i11++) {
            methodVisitor.visitAttribute(this.attrs.get(i11));
        }
        if (this.instructions.size() > 0) {
            methodVisitor.visitCode();
            int size7 = this.tryCatchBlocks == null ? 0 : this.tryCatchBlocks.size();
            for (int i12 = 0; i12 < size7; i12++) {
                this.tryCatchBlocks.get(i12).updateIndex(i12);
                this.tryCatchBlocks.get(i12).accept(methodVisitor);
            }
            this.instructions.accept(methodVisitor);
            int size8 = this.localVariables == null ? 0 : this.localVariables.size();
            for (int i13 = 0; i13 < size8; i13++) {
                this.localVariables.get(i13).accept(methodVisitor);
            }
            int size9 = this.visibleLocalVariableAnnotations == null ? 0 : this.visibleLocalVariableAnnotations.size();
            for (int i14 = 0; i14 < size9; i14++) {
                this.visibleLocalVariableAnnotations.get(i14).accept(methodVisitor, true);
            }
            int size10 = this.invisibleLocalVariableAnnotations == null ? 0 : this.invisibleLocalVariableAnnotations.size();
            for (int i15 = 0; i15 < size10; i15++) {
                this.invisibleLocalVariableAnnotations.get(i15).accept(methodVisitor, false);
            }
            methodVisitor.visitMaxs(this.maxStack, this.maxLocals);
            this.visited = true;
        }
        methodVisitor.visitEnd();
    }
}
