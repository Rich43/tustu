package jdk.internal.org.objectweb.asm.tree;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/TypeAnnotationNode.class */
public class TypeAnnotationNode extends AnnotationNode {
    public int typeRef;
    public TypePath typePath;

    public TypeAnnotationNode(int i2, TypePath typePath, String str) {
        this(Opcodes.ASM5, i2, typePath, str);
        if (getClass() != TypeAnnotationNode.class) {
            throw new IllegalStateException();
        }
    }

    public TypeAnnotationNode(int i2, int i3, TypePath typePath, String str) {
        super(i2, str);
        this.typeRef = i3;
        this.typePath = typePath;
    }
}
