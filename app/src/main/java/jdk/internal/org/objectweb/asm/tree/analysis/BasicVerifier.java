package jdk.internal.org.objectweb.asm.tree.analysis;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.List;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.InvokeDynamicInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/BasicVerifier.class */
public class BasicVerifier extends BasicInterpreter {
    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter, jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public /* bridge */ /* synthetic */ Value naryOperation(AbstractInsnNode abstractInsnNode, List list) throws AnalyzerException {
        return naryOperation(abstractInsnNode, (List<? extends BasicValue>) list);
    }

    public BasicVerifier() {
        super(Opcodes.ASM5);
    }

    protected BasicVerifier(int i2) {
        super(i2);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter, jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue copyOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue) throws AnalyzerException {
        BasicValue basicValue2;
        switch (abstractInsnNode.getOpcode()) {
            case 21:
            case 54:
                basicValue2 = BasicValue.INT_VALUE;
                break;
            case 22:
            case 55:
                basicValue2 = BasicValue.LONG_VALUE;
                break;
            case 23:
            case 56:
                basicValue2 = BasicValue.FLOAT_VALUE;
                break;
            case 24:
            case 57:
                basicValue2 = BasicValue.DOUBLE_VALUE;
                break;
            case 25:
                if (!basicValue.isReference()) {
                    throw new AnalyzerException(abstractInsnNode, null, "an object reference", basicValue);
                }
                return basicValue;
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            default:
                return basicValue;
            case 58:
                if (!basicValue.isReference() && !BasicValue.RETURNADDRESS_VALUE.equals(basicValue)) {
                    throw new AnalyzerException(abstractInsnNode, null, "an object reference or a return address", basicValue);
                }
                return basicValue;
        }
        if (!basicValue2.equals(basicValue)) {
            throw new AnalyzerException(abstractInsnNode, null, basicValue2, basicValue);
        }
        return basicValue;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter, jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue unaryOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue) throws AnalyzerException {
        BasicValue basicValueNewValue;
        switch (abstractInsnNode.getOpcode()) {
            case 116:
            case 132:
            case 133:
            case 134:
            case 135:
            case 145:
            case 146:
            case 147:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 188:
            case 189:
                basicValueNewValue = BasicValue.INT_VALUE;
                break;
            case 117:
            case 136:
            case 137:
            case 138:
            case 173:
                basicValueNewValue = BasicValue.LONG_VALUE;
                break;
            case 118:
            case 139:
            case 140:
            case 141:
            case 174:
                basicValueNewValue = BasicValue.FLOAT_VALUE;
                break;
            case 119:
            case 142:
            case 143:
            case 144:
            case 175:
                basicValueNewValue = BasicValue.DOUBLE_VALUE;
                break;
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 177:
            case 178:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 196:
            case 197:
            default:
                throw new Error("Internal error.");
            case 176:
            case 191:
            case 193:
            case 194:
            case 195:
            case 198:
            case 199:
                if (!basicValue.isReference()) {
                    throw new AnalyzerException(abstractInsnNode, null, "an object reference", basicValue);
                }
                return super.unaryOperation(abstractInsnNode, basicValue);
            case 179:
                basicValueNewValue = newValue(Type.getType(((FieldInsnNode) abstractInsnNode).desc));
                break;
            case 180:
                basicValueNewValue = newValue(Type.getObjectType(((FieldInsnNode) abstractInsnNode).owner));
                break;
            case 190:
                if (!isArrayValue(basicValue)) {
                    throw new AnalyzerException(abstractInsnNode, null, "an array reference", basicValue);
                }
                return super.unaryOperation(abstractInsnNode, basicValue);
            case 192:
                if (!basicValue.isReference()) {
                    throw new AnalyzerException(abstractInsnNode, null, "an object reference", basicValue);
                }
                return super.unaryOperation(abstractInsnNode, basicValue);
        }
        if (!isSubTypeOf(basicValue, basicValueNewValue)) {
            throw new AnalyzerException(abstractInsnNode, null, basicValueNewValue, basicValue);
        }
        return super.unaryOperation(abstractInsnNode, basicValue);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter, jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue binaryOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue, BasicValue basicValue2) throws AnalyzerException {
        BasicValue basicValueNewValue;
        BasicValue basicValueNewValue2;
        switch (abstractInsnNode.getOpcode()) {
            case 46:
                basicValueNewValue = newValue(Type.getType(Constants.TYPES_INDEX_SIG));
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 47:
                basicValueNewValue = newValue(Type.getType("[J"));
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 48:
                basicValueNewValue = newValue(Type.getType("[F"));
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 49:
                basicValueNewValue = newValue(Type.getType("[D"));
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 50:
                basicValueNewValue = newValue(Type.getType("[Ljava/lang/Object;"));
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 51:
                if (isSubTypeOf(basicValue, newValue(Type.getType("[Z")))) {
                    basicValueNewValue = newValue(Type.getType("[Z"));
                } else {
                    basicValueNewValue = newValue(Type.getType("[B"));
                }
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 52:
                basicValueNewValue = newValue(Type.getType(Constants.STATIC_CHAR_DATA_FIELD_SIG));
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 53:
                basicValueNewValue = newValue(Type.getType("[S"));
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 116:
            case 117:
            case 118:
            case 119:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 167:
            case 168:
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            default:
                throw new Error("Internal error.");
            case 96:
            case 100:
            case 104:
            case 108:
            case 112:
            case 120:
            case 122:
            case 124:
            case 126:
            case 128:
            case 130:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
                basicValueNewValue = BasicValue.INT_VALUE;
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 127:
            case 129:
            case 131:
            case 148:
                basicValueNewValue = BasicValue.LONG_VALUE;
                basicValueNewValue2 = BasicValue.LONG_VALUE;
                break;
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
            case 149:
            case 150:
                basicValueNewValue = BasicValue.FLOAT_VALUE;
                basicValueNewValue2 = BasicValue.FLOAT_VALUE;
                break;
            case 99:
            case 103:
            case 107:
            case 111:
            case 115:
            case 151:
            case 152:
                basicValueNewValue = BasicValue.DOUBLE_VALUE;
                basicValueNewValue2 = BasicValue.DOUBLE_VALUE;
                break;
            case 121:
            case 123:
            case 125:
                basicValueNewValue = BasicValue.LONG_VALUE;
                basicValueNewValue2 = BasicValue.INT_VALUE;
                break;
            case 165:
            case 166:
                basicValueNewValue = BasicValue.REFERENCE_VALUE;
                basicValueNewValue2 = BasicValue.REFERENCE_VALUE;
                break;
            case 181:
                FieldInsnNode fieldInsnNode = (FieldInsnNode) abstractInsnNode;
                basicValueNewValue = newValue(Type.getObjectType(fieldInsnNode.owner));
                basicValueNewValue2 = newValue(Type.getType(fieldInsnNode.desc));
                break;
        }
        if (!isSubTypeOf(basicValue, basicValueNewValue)) {
            throw new AnalyzerException(abstractInsnNode, "First argument", basicValueNewValue, basicValue);
        }
        if (!isSubTypeOf(basicValue2, basicValueNewValue2)) {
            throw new AnalyzerException(abstractInsnNode, "Second argument", basicValueNewValue2, basicValue2);
        }
        if (abstractInsnNode.getOpcode() == 50) {
            return getElementValue(basicValue);
        }
        return super.binaryOperation(abstractInsnNode, basicValue, basicValue2);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter, jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue ternaryOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue, BasicValue basicValue2, BasicValue basicValue3) throws AnalyzerException {
        BasicValue basicValueNewValue;
        BasicValue basicValue4;
        switch (abstractInsnNode.getOpcode()) {
            case 79:
                basicValueNewValue = newValue(Type.getType(Constants.TYPES_INDEX_SIG));
                basicValue4 = BasicValue.INT_VALUE;
                break;
            case 80:
                basicValueNewValue = newValue(Type.getType("[J"));
                basicValue4 = BasicValue.LONG_VALUE;
                break;
            case 81:
                basicValueNewValue = newValue(Type.getType("[F"));
                basicValue4 = BasicValue.FLOAT_VALUE;
                break;
            case 82:
                basicValueNewValue = newValue(Type.getType("[D"));
                basicValue4 = BasicValue.DOUBLE_VALUE;
                break;
            case 83:
                basicValueNewValue = basicValue;
                basicValue4 = BasicValue.REFERENCE_VALUE;
                break;
            case 84:
                if (isSubTypeOf(basicValue, newValue(Type.getType("[Z")))) {
                    basicValueNewValue = newValue(Type.getType("[Z"));
                } else {
                    basicValueNewValue = newValue(Type.getType("[B"));
                }
                basicValue4 = BasicValue.INT_VALUE;
                break;
            case 85:
                basicValueNewValue = newValue(Type.getType(Constants.STATIC_CHAR_DATA_FIELD_SIG));
                basicValue4 = BasicValue.INT_VALUE;
                break;
            case 86:
                basicValueNewValue = newValue(Type.getType("[S"));
                basicValue4 = BasicValue.INT_VALUE;
                break;
            default:
                throw new Error("Internal error.");
        }
        if (!isSubTypeOf(basicValue, basicValueNewValue)) {
            throw new AnalyzerException(abstractInsnNode, "First argument", "a " + ((Object) basicValueNewValue) + " array reference", basicValue);
        }
        if (!BasicValue.INT_VALUE.equals(basicValue2)) {
            throw new AnalyzerException(abstractInsnNode, "Second argument", BasicValue.INT_VALUE, basicValue2);
        }
        if (!isSubTypeOf(basicValue3, basicValue4)) {
            throw new AnalyzerException(abstractInsnNode, "Third argument", basicValue4, basicValue3);
        }
        return null;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter, jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue naryOperation(AbstractInsnNode abstractInsnNode, List<? extends BasicValue> list) throws AnalyzerException {
        int opcode = abstractInsnNode.getOpcode();
        if (opcode == 197) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                if (!BasicValue.INT_VALUE.equals(list.get(i2))) {
                    throw new AnalyzerException(abstractInsnNode, null, BasicValue.INT_VALUE, list.get(i2));
                }
            }
        } else {
            int i3 = 0;
            int i4 = 0;
            if (opcode != 184 && opcode != 186) {
                Type objectType = Type.getObjectType(((MethodInsnNode) abstractInsnNode).owner);
                i3 = 0 + 1;
                if (!isSubTypeOf(list.get(0), newValue(objectType))) {
                    throw new AnalyzerException(abstractInsnNode, "Method owner", newValue(objectType), list.get(0));
                }
            }
            Type[] argumentTypes = Type.getArgumentTypes(opcode == 186 ? ((InvokeDynamicInsnNode) abstractInsnNode).desc : ((MethodInsnNode) abstractInsnNode).desc);
            while (i3 < list.size()) {
                int i5 = i4;
                i4++;
                BasicValue basicValueNewValue = newValue(argumentTypes[i5]);
                int i6 = i3;
                i3++;
                BasicValue basicValue = list.get(i6);
                if (!isSubTypeOf(basicValue, basicValueNewValue)) {
                    throw new AnalyzerException(abstractInsnNode, "Argument " + i4, basicValueNewValue, basicValue);
                }
            }
        }
        return super.naryOperation(abstractInsnNode, list);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.BasicInterpreter, jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public void returnOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue, BasicValue basicValue2) throws AnalyzerException {
        if (!isSubTypeOf(basicValue, basicValue2)) {
            throw new AnalyzerException(abstractInsnNode, "Incompatible return type", basicValue2, basicValue);
        }
    }

    protected boolean isArrayValue(BasicValue basicValue) {
        return basicValue.isReference();
    }

    protected BasicValue getElementValue(BasicValue basicValue) throws AnalyzerException {
        return BasicValue.REFERENCE_VALUE;
    }

    protected boolean isSubTypeOf(BasicValue basicValue, BasicValue basicValue2) {
        return basicValue.equals(basicValue2);
    }
}
