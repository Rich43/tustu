package jdk.internal.org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/LocalVariableAnnotationNode.class */
public class LocalVariableAnnotationNode extends TypeAnnotationNode {
    public List<LabelNode> start;
    public List<LabelNode> end;
    public List<Integer> index;

    public LocalVariableAnnotationNode(int i2, TypePath typePath, LabelNode[] labelNodeArr, LabelNode[] labelNodeArr2, int[] iArr, String str) {
        this(Opcodes.ASM5, i2, typePath, labelNodeArr, labelNodeArr2, iArr, str);
    }

    public LocalVariableAnnotationNode(int i2, int i3, TypePath typePath, LabelNode[] labelNodeArr, LabelNode[] labelNodeArr2, int[] iArr, String str) {
        super(i2, i3, typePath, str);
        this.start = new ArrayList(labelNodeArr.length);
        this.start.addAll(Arrays.asList(labelNodeArr));
        this.end = new ArrayList(labelNodeArr2.length);
        this.end.addAll(Arrays.asList(labelNodeArr2));
        this.index = new ArrayList(iArr.length);
        for (int i4 : iArr) {
            this.index.add(Integer.valueOf(i4));
        }
    }

    public void accept(MethodVisitor methodVisitor, boolean z2) {
        Label[] labelArr = new Label[this.start.size()];
        Label[] labelArr2 = new Label[this.end.size()];
        int[] iArr = new int[this.index.size()];
        for (int i2 = 0; i2 < labelArr.length; i2++) {
            labelArr[i2] = this.start.get(i2).getLabel();
            labelArr2[i2] = this.end.get(i2).getLabel();
            iArr[i2] = this.index.get(i2).intValue();
        }
        accept(methodVisitor.visitLocalVariableAnnotation(this.typeRef, this.typePath, labelArr, labelArr2, iArr, this.desc, true));
    }
}
