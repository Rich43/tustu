package jdk.internal.org.objectweb.asm.commons;

import java.util.Collections;
import java.util.Comparator;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/TryCatchBlockSorter.class */
public class TryCatchBlockSorter extends MethodNode {
    public TryCatchBlockSorter(MethodVisitor methodVisitor, int i2, String str, String str2, String str3, String[] strArr) {
        this(Opcodes.ASM5, methodVisitor, i2, str, str2, str3, strArr);
    }

    protected TryCatchBlockSorter(int i2, MethodVisitor methodVisitor, int i3, String str, String str2, String str3, String[] strArr) {
        super(i2, i3, str, str2, str3, strArr);
        this.mv = methodVisitor;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.MethodNode, jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
        Collections.sort(this.tryCatchBlocks, new Comparator<TryCatchBlockNode>() { // from class: jdk.internal.org.objectweb.asm.commons.TryCatchBlockSorter.1
            @Override // java.util.Comparator
            public int compare(TryCatchBlockNode tryCatchBlockNode, TryCatchBlockNode tryCatchBlockNode2) {
                return blockLength(tryCatchBlockNode) - blockLength(tryCatchBlockNode2);
            }

            private int blockLength(TryCatchBlockNode tryCatchBlockNode) {
                return TryCatchBlockSorter.this.instructions.indexOf(tryCatchBlockNode.end) - TryCatchBlockSorter.this.instructions.indexOf(tryCatchBlockNode.start);
            }
        });
        for (int i2 = 0; i2 < this.tryCatchBlocks.size(); i2++) {
            this.tryCatchBlocks.get(i2).updateIndex(i2);
        }
        if (this.mv != null) {
            accept(this.mv);
        }
    }
}
