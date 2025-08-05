package jdk.internal.org.objectweb.asm.tree.analysis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InvokeDynamicInsnNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/SourceInterpreter.class */
public class SourceInterpreter extends Interpreter<SourceValue> implements Opcodes {
    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public /* bridge */ /* synthetic */ Value naryOperation(AbstractInsnNode abstractInsnNode, List list) throws AnalyzerException {
        return naryOperation(abstractInsnNode, (List<? extends SourceValue>) list);
    }

    public SourceInterpreter() {
        super(Opcodes.ASM5);
    }

    protected SourceInterpreter(int i2) {
        super(i2);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public SourceValue newValue(Type type) {
        if (type == Type.VOID_TYPE) {
            return null;
        }
        return new SourceValue(type == null ? 1 : type.getSize());
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public SourceValue newOperation(AbstractInsnNode abstractInsnNode) {
        int size;
        switch (abstractInsnNode.getOpcode()) {
            case 9:
            case 10:
            case 14:
            case 15:
                size = 2;
                break;
            case 18:
                Object obj = ((LdcInsnNode) abstractInsnNode).cst;
                size = ((obj instanceof Long) || (obj instanceof Double)) ? 2 : 1;
                break;
            case 178:
                size = Type.getType(((FieldInsnNode) abstractInsnNode).desc).getSize();
                break;
            default:
                size = 1;
                break;
        }
        return new SourceValue(size, abstractInsnNode);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public SourceValue copyOperation(AbstractInsnNode abstractInsnNode, SourceValue sourceValue) {
        return new SourceValue(sourceValue.getSize(), abstractInsnNode);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public SourceValue unaryOperation(AbstractInsnNode abstractInsnNode, SourceValue sourceValue) {
        int size;
        switch (abstractInsnNode.getOpcode()) {
            case 117:
            case 119:
            case 133:
            case 135:
            case 138:
            case 140:
            case 141:
            case 143:
                size = 2;
                break;
            case 180:
                size = Type.getType(((FieldInsnNode) abstractInsnNode).desc).getSize();
                break;
            default:
                size = 1;
                break;
        }
        return new SourceValue(size, abstractInsnNode);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public SourceValue binaryOperation(AbstractInsnNode abstractInsnNode, SourceValue sourceValue, SourceValue sourceValue2) {
        int i2;
        switch (abstractInsnNode.getOpcode()) {
            case 47:
            case 49:
            case 97:
            case 99:
            case 101:
            case 103:
            case 105:
            case 107:
            case 109:
            case 111:
            case 113:
            case 115:
            case 121:
            case 123:
            case 125:
            case 127:
            case 129:
            case 131:
                i2 = 2;
                break;
            default:
                i2 = 1;
                break;
        }
        return new SourceValue(i2, abstractInsnNode);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public SourceValue ternaryOperation(AbstractInsnNode abstractInsnNode, SourceValue sourceValue, SourceValue sourceValue2, SourceValue sourceValue3) {
        return new SourceValue(1, abstractInsnNode);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public SourceValue naryOperation(AbstractInsnNode abstractInsnNode, List<? extends SourceValue> list) {
        int size;
        int opcode = abstractInsnNode.getOpcode();
        if (opcode == 197) {
            size = 1;
        } else {
            size = Type.getReturnType(opcode == 186 ? ((InvokeDynamicInsnNode) abstractInsnNode).desc : ((MethodInsnNode) abstractInsnNode).desc).getSize();
        }
        return new SourceValue(size, abstractInsnNode);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public void returnOperation(AbstractInsnNode abstractInsnNode, SourceValue sourceValue, SourceValue sourceValue2) {
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public SourceValue merge(SourceValue sourceValue, SourceValue sourceValue2) {
        if ((sourceValue.insns instanceof SmallSet) && (sourceValue2.insns instanceof SmallSet)) {
            Set<AbstractInsnNode> setUnion = ((SmallSet) sourceValue.insns).union((SmallSet) sourceValue2.insns);
            if (setUnion == sourceValue.insns && sourceValue.size == sourceValue2.size) {
                return sourceValue;
            }
            return new SourceValue(Math.min(sourceValue.size, sourceValue2.size), setUnion);
        }
        if (sourceValue.size != sourceValue2.size || !sourceValue.insns.containsAll(sourceValue2.insns)) {
            HashSet hashSet = new HashSet();
            hashSet.addAll(sourceValue.insns);
            hashSet.addAll(sourceValue2.insns);
            return new SourceValue(Math.min(sourceValue.size, sourceValue2.size), hashSet);
        }
        return sourceValue;
    }
}
