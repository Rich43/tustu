package jdk.jfr.internal.instrument;

import java.util.ArrayList;
import java.util.List;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter;
import jdk.internal.org.objectweb.asm.commons.SimpleRemapper;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;

@Deprecated
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/JIMethodCallInliner.class */
final class JIMethodCallInliner extends LocalVariablesSorter {
    private final String oldClass;
    private final String newClass;
    private final MethodNode inlineTarget;
    private final List<CatchBlock> blocks;
    private boolean inlining;

    public JIMethodCallInliner(int i2, String str, MethodVisitor methodVisitor, MethodNode methodNode, String str2, String str3) {
        super(Opcodes.ASM5, i2, str, methodVisitor);
        this.blocks = new ArrayList();
        this.oldClass = str2;
        this.newClass = str3;
        this.inlineTarget = methodNode;
        Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.DEBUG, "MethodCallInliner: targetMethod=" + str3 + "." + methodNode.name + methodNode.desc);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (!shouldBeInlined(str, str2, str3)) {
            this.mv.visitMethodInsn(i2, str, str2, str3, z2);
            return;
        }
        Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.DEBUG, "Inlining call to " + str2 + str3);
        SimpleRemapper simpleRemapper = new SimpleRemapper(this.oldClass, this.newClass);
        Label label = new Label();
        this.inlining = true;
        this.inlineTarget.instructions.resetLabels();
        this.inlineTarget.accept(new JIMethodInliningAdapter(this, label, i2 == 184 ? 8 : 0, str3, simpleRemapper));
        this.inlining = false;
        super.visitLabel(label);
    }

    private boolean shouldBeInlined(String str, String str2, String str3) {
        return this.inlineTarget.desc.equals(str3) && this.inlineTarget.name.equals(str2) && str.equals(this.newClass.replace('.', '/'));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        if (!this.inlining) {
            this.blocks.add(new CatchBlock(label, label2, label3, str));
        } else {
            super.visitTryCatchBlock(label, label2, label3, str);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter, jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i2, int i3) {
        for (CatchBlock catchBlock : this.blocks) {
            super.visitTryCatchBlock(catchBlock.start, catchBlock.end, catchBlock.handler, catchBlock.type);
        }
        super.visitMaxs(i2, i3);
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/instrument/JIMethodCallInliner$CatchBlock.class */
    static final class CatchBlock {
        final Label start;
        final Label end;
        final Label handler;
        final String type;

        CatchBlock(Label label, Label label2, Label label3, String str) {
            this.start = label;
            this.end = label2;
            this.handler = label3;
            this.type = str;
        }
    }
}
