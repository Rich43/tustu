package jdk.internal.org.objectweb.asm.tree;

import java.util.Iterator;
import java.util.List;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/TryCatchBlockNode.class */
public class TryCatchBlockNode {
    public LabelNode start;
    public LabelNode end;
    public LabelNode handler;
    public String type;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;

    public TryCatchBlockNode(LabelNode labelNode, LabelNode labelNode2, LabelNode labelNode3, String str) {
        this.start = labelNode;
        this.end = labelNode2;
        this.handler = labelNode3;
        this.type = str;
    }

    public void updateIndex(int i2) {
        int i3 = 1107296256 | (i2 << 8);
        if (this.visibleTypeAnnotations != null) {
            Iterator<TypeAnnotationNode> it = this.visibleTypeAnnotations.iterator();
            while (it.hasNext()) {
                it.next().typeRef = i3;
            }
        }
        if (this.invisibleTypeAnnotations != null) {
            Iterator<TypeAnnotationNode> it2 = this.invisibleTypeAnnotations.iterator();
            while (it2.hasNext()) {
                it2.next().typeRef = i3;
            }
        }
    }

    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitTryCatchBlock(this.start.getLabel(), this.end.getLabel(), this.handler == null ? null : this.handler.getLabel(), this.type);
        int size = this.visibleTypeAnnotations == null ? 0 : this.visibleTypeAnnotations.size();
        for (int i2 = 0; i2 < size; i2++) {
            TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(i2);
            typeAnnotationNode.accept(methodVisitor.visitTryCatchAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
        }
        int size2 = this.invisibleTypeAnnotations == null ? 0 : this.invisibleTypeAnnotations.size();
        for (int i3 = 0; i3 < size2; i3++) {
            TypeAnnotationNode typeAnnotationNode2 = this.invisibleTypeAnnotations.get(i3);
            typeAnnotationNode2.accept(methodVisitor.visitTryCatchAnnotation(typeAnnotationNode2.typeRef, typeAnnotationNode2.typePath, typeAnnotationNode2.desc, false));
        }
    }
}
