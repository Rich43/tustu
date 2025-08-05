package jdk.jfr.internal.instrument;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter;
import jdk.internal.org.objectweb.asm.commons.Remapper;
import jdk.internal.org.objectweb.asm.commons.RemappingMethodAdapter;

@Deprecated
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/JIMethodInliningAdapter.class */
final class JIMethodInliningAdapter extends RemappingMethodAdapter {
    private final LocalVariablesSorter lvs;
    private final Label end;

    public JIMethodInliningAdapter(LocalVariablesSorter localVariablesSorter, Label label, int i2, String str, Remapper remapper) {
        super(i2, str, localVariablesSorter, remapper);
        this.lvs = localVariablesSorter;
        this.end = label;
        int i3 = isStatic(i2) ? 0 : 1;
        Type[] argumentTypes = Type.getArgumentTypes(str);
        for (int length = argumentTypes.length - 1; length >= 0; length--) {
            super.visitVarInsn(argumentTypes[length].getOpcode(54), length + i3);
        }
        if (i3 > 0) {
            super.visitVarInsn(58, 0);
        }
    }

    private boolean isStatic(int i2) {
        return (i2 & 8) != 0;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i2) {
        if (i2 == 177 || i2 == 172 || i2 == 176 || i2 == 173) {
            super.visitJumpInsn(167, this.end);
        } else {
            super.visitInsn(i2);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter, jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i2, int i3) {
    }

    @Override // jdk.internal.org.objectweb.asm.commons.LocalVariablesSorter
    protected int newLocalMapping(Type type) {
        return this.lvs.newLocal(type);
    }
}
