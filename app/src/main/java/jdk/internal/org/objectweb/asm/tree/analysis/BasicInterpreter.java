package jdk.internal.org.objectweb.asm.tree.analysis;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.List;
import javafx.fxml.FXMLLoader;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.IntInsnNode;
import jdk.internal.org.objectweb.asm.tree.InvokeDynamicInsnNode;
import jdk.internal.org.objectweb.asm.tree.LdcInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MultiANewArrayInsnNode;
import jdk.internal.org.objectweb.asm.tree.TypeInsnNode;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/BasicInterpreter.class */
public class BasicInterpreter extends Interpreter<BasicValue> implements Opcodes {
    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public /* bridge */ /* synthetic */ Value naryOperation(AbstractInsnNode abstractInsnNode, List list) throws AnalyzerException {
        return naryOperation(abstractInsnNode, (List<? extends BasicValue>) list);
    }

    public BasicInterpreter() {
        super(Opcodes.ASM5);
    }

    protected BasicInterpreter(int i2) {
        super(i2);
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue newValue(Type type) {
        if (type == null) {
            return BasicValue.UNINITIALIZED_VALUE;
        }
        switch (type.getSort()) {
            case 0:
                return null;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return BasicValue.INT_VALUE;
            case 6:
                return BasicValue.FLOAT_VALUE;
            case 7:
                return BasicValue.LONG_VALUE;
            case 8:
                return BasicValue.DOUBLE_VALUE;
            case 9:
            case 10:
                return BasicValue.REFERENCE_VALUE;
            default:
                throw new Error("Internal error");
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue newOperation(AbstractInsnNode abstractInsnNode) throws AnalyzerException {
        switch (abstractInsnNode.getOpcode()) {
            case 1:
                return newValue(Type.getObjectType(FXMLLoader.NULL_KEYWORD));
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                return BasicValue.INT_VALUE;
            case 9:
            case 10:
                return BasicValue.LONG_VALUE;
            case 11:
            case 12:
            case 13:
                return BasicValue.FLOAT_VALUE;
            case 14:
            case 15:
                return BasicValue.DOUBLE_VALUE;
            case 16:
            case 17:
                return BasicValue.INT_VALUE;
            case 18:
                Object obj = ((LdcInsnNode) abstractInsnNode).cst;
                if (obj instanceof Integer) {
                    return BasicValue.INT_VALUE;
                }
                if (obj instanceof Float) {
                    return BasicValue.FLOAT_VALUE;
                }
                if (obj instanceof Long) {
                    return BasicValue.LONG_VALUE;
                }
                if (obj instanceof Double) {
                    return BasicValue.DOUBLE_VALUE;
                }
                if (obj instanceof String) {
                    return newValue(Type.getObjectType("java/lang/String"));
                }
                if (obj instanceof Type) {
                    int sort = ((Type) obj).getSort();
                    if (sort == 10 || sort == 9) {
                        return newValue(Type.getObjectType("java/lang/Class"));
                    }
                    if (sort == 11) {
                        return newValue(Type.getObjectType("java/lang/invoke/MethodType"));
                    }
                    throw new IllegalArgumentException("Illegal LDC constant " + obj);
                }
                if (obj instanceof Handle) {
                    return newValue(Type.getObjectType("java/lang/invoke/MethodHandle"));
                }
                throw new IllegalArgumentException("Illegal LDC constant " + obj);
            case 168:
                return BasicValue.RETURNADDRESS_VALUE;
            case 178:
                return newValue(Type.getType(((FieldInsnNode) abstractInsnNode).desc));
            case 187:
                return newValue(Type.getObjectType(((TypeInsnNode) abstractInsnNode).desc));
            default:
                throw new Error("Internal error.");
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue copyOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue) throws AnalyzerException {
        return basicValue;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue unaryOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue) throws AnalyzerException {
        switch (abstractInsnNode.getOpcode()) {
            case 116:
            case 132:
            case 136:
            case 139:
            case 142:
            case 145:
            case 146:
            case 147:
                return BasicValue.INT_VALUE;
            case 117:
            case 133:
            case 140:
            case 143:
                return BasicValue.LONG_VALUE;
            case 118:
            case 134:
            case 137:
            case 144:
                return BasicValue.FLOAT_VALUE;
            case 119:
            case 135:
            case 138:
            case 141:
                return BasicValue.DOUBLE_VALUE;
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
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 179:
                return null;
            case 180:
                return newValue(Type.getType(((FieldInsnNode) abstractInsnNode).desc));
            case 188:
                switch (((IntInsnNode) abstractInsnNode).operand) {
                    case 4:
                        return newValue(Type.getType("[Z"));
                    case 5:
                        return newValue(Type.getType(Constants.STATIC_CHAR_DATA_FIELD_SIG));
                    case 6:
                        return newValue(Type.getType("[F"));
                    case 7:
                        return newValue(Type.getType("[D"));
                    case 8:
                        return newValue(Type.getType("[B"));
                    case 9:
                        return newValue(Type.getType("[S"));
                    case 10:
                        return newValue(Type.getType(Constants.TYPES_INDEX_SIG));
                    case 11:
                        return newValue(Type.getType("[J"));
                    default:
                        throw new AnalyzerException(abstractInsnNode, "Invalid array type");
                }
            case 189:
                return newValue(Type.getType("[" + ((Object) Type.getObjectType(((TypeInsnNode) abstractInsnNode).desc))));
            case 190:
                return BasicValue.INT_VALUE;
            case 191:
                return null;
            case 192:
                return newValue(Type.getObjectType(((TypeInsnNode) abstractInsnNode).desc));
            case 193:
                return BasicValue.INT_VALUE;
            case 194:
            case 195:
            case 198:
            case 199:
                return null;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue binaryOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue, BasicValue basicValue2) throws AnalyzerException {
        switch (abstractInsnNode.getOpcode()) {
            case 46:
            case 51:
            case 52:
            case 53:
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
                return BasicValue.INT_VALUE;
            case 47:
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 121:
            case 123:
            case 125:
            case 127:
            case 129:
            case 131:
                return BasicValue.LONG_VALUE;
            case 48:
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
                return BasicValue.FLOAT_VALUE;
            case 49:
            case 99:
            case 103:
            case 107:
            case 111:
            case 115:
                return BasicValue.DOUBLE_VALUE;
            case 50:
                return BasicValue.REFERENCE_VALUE;
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
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
                return BasicValue.INT_VALUE;
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 181:
                return null;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue ternaryOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue, BasicValue basicValue2, BasicValue basicValue3) throws AnalyzerException {
        return null;
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue naryOperation(AbstractInsnNode abstractInsnNode, List<? extends BasicValue> list) throws AnalyzerException {
        int opcode = abstractInsnNode.getOpcode();
        if (opcode == 197) {
            return newValue(Type.getType(((MultiANewArrayInsnNode) abstractInsnNode).desc));
        }
        if (opcode == 186) {
            return newValue(Type.getReturnType(((InvokeDynamicInsnNode) abstractInsnNode).desc));
        }
        return newValue(Type.getReturnType(((MethodInsnNode) abstractInsnNode).desc));
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public void returnOperation(AbstractInsnNode abstractInsnNode, BasicValue basicValue, BasicValue basicValue2) throws AnalyzerException {
    }

    @Override // jdk.internal.org.objectweb.asm.tree.analysis.Interpreter
    public BasicValue merge(BasicValue basicValue, BasicValue basicValue2) {
        if (!basicValue.equals(basicValue2)) {
            return BasicValue.UNINITIALIZED_VALUE;
        }
        return basicValue;
    }
}
