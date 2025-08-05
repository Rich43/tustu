package jdk.internal.org.objectweb.asm.commons;

import com.sun.org.apache.bcel.internal.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/AnalyzerAdapter.class */
public class AnalyzerAdapter extends MethodVisitor {
    public List<Object> locals;
    public List<Object> stack;
    private List<Label> labels;
    public Map<Object, Object> uninitializedTypes;
    private int maxStack;
    private int maxLocals;
    private String owner;

    public AnalyzerAdapter(String str, int i2, String str2, String str3, MethodVisitor methodVisitor) {
        this(Opcodes.ASM5, str, i2, str2, str3, methodVisitor);
        if (getClass() != AnalyzerAdapter.class) {
            throw new IllegalStateException();
        }
    }

    protected AnalyzerAdapter(int i2, String str, int i3, String str2, String str3, MethodVisitor methodVisitor) {
        super(i2, methodVisitor);
        this.owner = str;
        this.locals = new ArrayList();
        this.stack = new ArrayList();
        this.uninitializedTypes = new HashMap();
        if ((i3 & 8) == 0) {
            if (Constants.CONSTRUCTOR_NAME.equals(str2)) {
                this.locals.add(Opcodes.UNINITIALIZED_THIS);
            } else {
                this.locals.add(str);
            }
        }
        Type[] argumentTypes = Type.getArgumentTypes(str3);
        for (int i4 = 0; i4 < argumentTypes.length; i4++) {
            switch (argumentTypes[i4].getSort()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    this.locals.add(Opcodes.INTEGER);
                    break;
                case 6:
                    this.locals.add(Opcodes.FLOAT);
                    break;
                case 7:
                    this.locals.add(Opcodes.LONG);
                    this.locals.add(Opcodes.TOP);
                    break;
                case 8:
                    this.locals.add(Opcodes.DOUBLE);
                    this.locals.add(Opcodes.TOP);
                    break;
                case 9:
                    this.locals.add(argumentTypes[i4].getDescriptor());
                    break;
                default:
                    this.locals.add(argumentTypes[i4].getInternalName());
                    break;
            }
        }
        this.maxLocals = this.locals.size();
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        if (i2 != -1) {
            throw new IllegalStateException("ClassReader.accept() should be called with EXPAND_FRAMES flag");
        }
        if (this.mv != null) {
            this.mv.visitFrame(i2, i3, objArr, i4, objArr2);
        }
        if (this.locals != null) {
            this.locals.clear();
            this.stack.clear();
        } else {
            this.locals = new ArrayList();
            this.stack = new ArrayList();
        }
        visitFrameTypes(i3, objArr, this.locals);
        visitFrameTypes(i4, objArr2, this.stack);
        this.maxStack = Math.max(this.maxStack, this.stack.size());
    }

    private static void visitFrameTypes(int i2, Object[] objArr, List<Object> list) {
        for (int i3 = 0; i3 < i2; i3++) {
            Object obj = objArr[i3];
            list.add(obj);
            if (obj == Opcodes.LONG || obj == Opcodes.DOUBLE) {
                list.add(Opcodes.TOP);
            }
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i2) {
        if (this.mv != null) {
            this.mv.visitInsn(i2);
        }
        execute(i2, 0, null);
        if ((i2 >= 172 && i2 <= 177) || i2 == 191) {
            this.locals = null;
            this.stack = null;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int i2, int i3) {
        if (this.mv != null) {
            this.mv.visitIntInsn(i2, i3);
        }
        execute(i2, i3, null);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i2, int i3) {
        if (this.mv != null) {
            this.mv.visitVarInsn(i2, i3);
        }
        execute(i2, i3, null);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i2, String str) {
        if (i2 == 187) {
            if (this.labels == null) {
                Label label = new Label();
                this.labels = new ArrayList(3);
                this.labels.add(label);
                if (this.mv != null) {
                    this.mv.visitLabel(label);
                }
            }
            for (int i3 = 0; i3 < this.labels.size(); i3++) {
                this.uninitializedTypes.put(this.labels.get(i3), str);
            }
        }
        if (this.mv != null) {
            this.mv.visitTypeInsn(i2, str);
        }
        execute(i2, 0, str);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        if (this.mv != null) {
            this.mv.visitFieldInsn(i2, str, str2, str3);
        }
        execute(i2, 0, str3);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    @Deprecated
    public void visitMethodInsn(int i2, String str, String str2, String str3) {
        if (this.api >= 327680) {
            super.visitMethodInsn(i2, str, str2, str3);
        } else {
            doVisitMethodInsn(i2, str, str2, str3, i2 == 185);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            super.visitMethodInsn(i2, str, str2, str3, z2);
        } else {
            doVisitMethodInsn(i2, str, str2, str3, z2);
        }
    }

    private void doVisitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        Object obj;
        if (this.mv != null) {
            this.mv.visitMethodInsn(i2, str, str2, str3, z2);
        }
        if (this.locals == null) {
            this.labels = null;
            return;
        }
        pop(str3);
        if (i2 != 184) {
            Object objPop = pop();
            if (i2 == 183 && str2.charAt(0) == '<') {
                if (objPop == Opcodes.UNINITIALIZED_THIS) {
                    obj = this.owner;
                } else {
                    obj = this.uninitializedTypes.get(objPop);
                }
                for (int i3 = 0; i3 < this.locals.size(); i3++) {
                    if (this.locals.get(i3) == objPop) {
                        this.locals.set(i3, obj);
                    }
                }
                for (int i4 = 0; i4 < this.stack.size(); i4++) {
                    if (this.stack.get(i4) == objPop) {
                        this.stack.set(i4, obj);
                    }
                }
            }
        }
        pushDesc(str3);
        this.labels = null;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        if (this.mv != null) {
            this.mv.visitInvokeDynamicInsn(str, str2, handle, objArr);
        }
        if (this.locals == null) {
            this.labels = null;
            return;
        }
        pop(str2);
        pushDesc(str2);
        this.labels = null;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i2, Label label) {
        if (this.mv != null) {
            this.mv.visitJumpInsn(i2, label);
        }
        execute(i2, 0, null);
        if (i2 == 167) {
            this.locals = null;
            this.stack = null;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        if (this.mv != null) {
            this.mv.visitLabel(label);
        }
        if (this.labels == null) {
            this.labels = new ArrayList(3);
        }
        this.labels.add(label);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        if (this.mv != null) {
            this.mv.visitLdcInsn(obj);
        }
        if (this.locals == null) {
            this.labels = null;
            return;
        }
        if (obj instanceof Integer) {
            push(Opcodes.INTEGER);
        } else if (obj instanceof Long) {
            push(Opcodes.LONG);
            push(Opcodes.TOP);
        } else if (obj instanceof Float) {
            push(Opcodes.FLOAT);
        } else if (obj instanceof Double) {
            push(Opcodes.DOUBLE);
            push(Opcodes.TOP);
        } else if (obj instanceof String) {
            push("java/lang/String");
        } else if (obj instanceof Type) {
            int sort = ((Type) obj).getSort();
            if (sort == 10 || sort == 9) {
                push("java/lang/Class");
            } else if (sort == 11) {
                push("java/lang/invoke/MethodType");
            } else {
                throw new IllegalArgumentException();
            }
        } else if (obj instanceof Handle) {
            push("java/lang/invoke/MethodHandle");
        } else {
            throw new IllegalArgumentException();
        }
        this.labels = null;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i2, int i3) {
        if (this.mv != null) {
            this.mv.visitIincInsn(i2, i3);
        }
        execute(132, i2, null);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr) {
        if (this.mv != null) {
            this.mv.visitTableSwitchInsn(i2, i3, label, labelArr);
        }
        execute(170, 0, null);
        this.locals = null;
        this.stack = null;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        if (this.mv != null) {
            this.mv.visitLookupSwitchInsn(label, iArr, labelArr);
        }
        execute(171, 0, null);
        this.locals = null;
        this.stack = null;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i2) {
        if (this.mv != null) {
            this.mv.visitMultiANewArrayInsn(str, i2);
        }
        execute(197, i2, str);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i2, int i3) {
        if (this.mv != null) {
            this.maxStack = Math.max(this.maxStack, i2);
            this.maxLocals = Math.max(this.maxLocals, i3);
            this.mv.visitMaxs(this.maxStack, this.maxLocals);
        }
    }

    private Object get(int i2) {
        this.maxLocals = Math.max(this.maxLocals, i2 + 1);
        return i2 < this.locals.size() ? this.locals.get(i2) : Opcodes.TOP;
    }

    private void set(int i2, Object obj) {
        this.maxLocals = Math.max(this.maxLocals, i2 + 1);
        while (i2 >= this.locals.size()) {
            this.locals.add(Opcodes.TOP);
        }
        this.locals.set(i2, obj);
    }

    private void push(Object obj) {
        this.stack.add(obj);
        this.maxStack = Math.max(this.maxStack, this.stack.size());
    }

    private void pushDesc(String str) {
        int iIndexOf = str.charAt(0) == '(' ? str.indexOf(41) + 1 : 0;
        switch (str.charAt(iIndexOf)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                push(Opcodes.INTEGER);
                break;
            case 'D':
                push(Opcodes.DOUBLE);
                push(Opcodes.TOP);
                break;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                if (iIndexOf == 0) {
                    push(str.substring(1, str.length() - 1));
                    break;
                } else {
                    push(str.substring(iIndexOf + 1, str.length() - 1));
                    break;
                }
            case 'F':
                push(Opcodes.FLOAT);
                break;
            case 'J':
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case 'V':
                break;
            case '[':
                if (iIndexOf == 0) {
                    push(str);
                    break;
                } else {
                    push(str.substring(iIndexOf, str.length()));
                    break;
                }
        }
    }

    private Object pop() {
        return this.stack.remove(this.stack.size() - 1);
    }

    private void pop(int i2) {
        int size = this.stack.size();
        int i3 = size - i2;
        for (int i4 = size - 1; i4 >= i3; i4--) {
            this.stack.remove(i4);
        }
    }

    private void pop(String str) {
        char cCharAt = str.charAt(0);
        if (cCharAt != '(') {
            if (cCharAt == 'J' || cCharAt == 'D') {
                pop(2);
                return;
            } else {
                pop(1);
                return;
            }
        }
        int size = 0;
        for (Type type : Type.getArgumentTypes(str)) {
            size += type.getSize();
        }
        pop(size);
    }

    private void execute(int i2, int i3, String str) {
        Object obj;
        Object obj2;
        if (this.locals == null) {
            this.labels = null;
            return;
        }
        switch (i2) {
            case 0:
            case 116:
            case 117:
            case 118:
            case 119:
            case 145:
            case 146:
            case 147:
            case 167:
            case 177:
                break;
            case 1:
                push(Opcodes.NULL);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 16:
            case 17:
                push(Opcodes.INTEGER);
                break;
            case 9:
            case 10:
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case 11:
            case 12:
            case 13:
                push(Opcodes.FLOAT);
                break;
            case 14:
            case 15:
                push(Opcodes.DOUBLE);
                push(Opcodes.TOP);
                break;
            case 18:
            case 19:
            case 20:
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
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 196:
            case 197:
            default:
                pop(i3);
                pushDesc(str);
                break;
            case 21:
            case 23:
            case 25:
                push(get(i3));
                break;
            case 22:
            case 24:
                push(get(i3));
                push(Opcodes.TOP);
                break;
            case 46:
            case 51:
            case 52:
            case 53:
                pop(2);
                push(Opcodes.INTEGER);
                break;
            case 47:
            case 143:
                pop(2);
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case 48:
                pop(2);
                push(Opcodes.FLOAT);
                break;
            case 49:
            case 138:
                pop(2);
                push(Opcodes.DOUBLE);
                push(Opcodes.TOP);
                break;
            case 50:
                pop(1);
                Object objPop = pop();
                if (objPop instanceof String) {
                    pushDesc(((String) objPop).substring(1));
                    break;
                } else {
                    push("java/lang/Object");
                    break;
                }
            case 54:
            case 56:
            case 58:
                set(i3, pop());
                if (i3 > 0 && ((obj2 = get(i3 - 1)) == Opcodes.LONG || obj2 == Opcodes.DOUBLE)) {
                    set(i3 - 1, Opcodes.TOP);
                    break;
                }
                break;
            case 55:
            case 57:
                pop(1);
                set(i3, pop());
                set(i3 + 1, Opcodes.TOP);
                if (i3 > 0 && ((obj = get(i3 - 1)) == Opcodes.LONG || obj == Opcodes.DOUBLE)) {
                    set(i3 - 1, Opcodes.TOP);
                    break;
                }
                break;
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
                pop(3);
                break;
            case 80:
            case 82:
                pop(4);
                break;
            case 87:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 170:
            case 171:
            case 172:
            case 174:
            case 176:
            case 191:
            case 194:
            case 195:
            case 198:
            case 199:
                pop(1);
                break;
            case 88:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 173:
            case 175:
                pop(2);
                break;
            case 89:
                Object objPop2 = pop();
                push(objPop2);
                push(objPop2);
                break;
            case 90:
                Object objPop3 = pop();
                Object objPop4 = pop();
                push(objPop3);
                push(objPop4);
                push(objPop3);
                break;
            case 91:
                Object objPop5 = pop();
                Object objPop6 = pop();
                Object objPop7 = pop();
                push(objPop5);
                push(objPop7);
                push(objPop6);
                push(objPop5);
                break;
            case 92:
                Object objPop8 = pop();
                Object objPop9 = pop();
                push(objPop9);
                push(objPop8);
                push(objPop9);
                push(objPop8);
                break;
            case 93:
                Object objPop10 = pop();
                Object objPop11 = pop();
                Object objPop12 = pop();
                push(objPop11);
                push(objPop10);
                push(objPop12);
                push(objPop11);
                push(objPop10);
                break;
            case 94:
                Object objPop13 = pop();
                Object objPop14 = pop();
                Object objPop15 = pop();
                Object objPop16 = pop();
                push(objPop14);
                push(objPop13);
                push(objPop16);
                push(objPop15);
                push(objPop14);
                push(objPop13);
                break;
            case 95:
                Object objPop17 = pop();
                Object objPop18 = pop();
                push(objPop17);
                push(objPop18);
                break;
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
            case 136:
            case 142:
            case 149:
            case 150:
                pop(2);
                push(Opcodes.INTEGER);
                break;
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 127:
            case 129:
            case 131:
                pop(4);
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
            case 137:
            case 144:
                pop(2);
                push(Opcodes.FLOAT);
                break;
            case 99:
            case 103:
            case 107:
            case 111:
            case 115:
                pop(4);
                push(Opcodes.DOUBLE);
                push(Opcodes.TOP);
                break;
            case 121:
            case 123:
            case 125:
                pop(3);
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case 132:
                set(i3, Opcodes.INTEGER);
                break;
            case 133:
            case 140:
                pop(1);
                push(Opcodes.LONG);
                push(Opcodes.TOP);
                break;
            case 134:
                pop(1);
                push(Opcodes.FLOAT);
                break;
            case 135:
            case 141:
                pop(1);
                push(Opcodes.DOUBLE);
                push(Opcodes.TOP);
                break;
            case 139:
            case 190:
            case 193:
                pop(1);
                push(Opcodes.INTEGER);
                break;
            case 148:
            case 151:
            case 152:
                pop(4);
                push(Opcodes.INTEGER);
                break;
            case 168:
            case 169:
                throw new RuntimeException("JSR/RET are not supported");
            case 178:
                pushDesc(str);
                break;
            case 179:
                pop(str);
                break;
            case 180:
                pop(1);
                pushDesc(str);
                break;
            case 181:
                pop(str);
                pop();
                break;
            case 187:
                push(this.labels.get(0));
                break;
            case 188:
                pop();
                switch (i3) {
                    case 4:
                        pushDesc("[Z");
                        break;
                    case 5:
                        pushDesc(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.STATIC_CHAR_DATA_FIELD_SIG);
                        break;
                    case 6:
                        pushDesc("[F");
                        break;
                    case 7:
                        pushDesc("[D");
                        break;
                    case 8:
                        pushDesc("[B");
                        break;
                    case 9:
                        pushDesc("[S");
                        break;
                    case 10:
                        pushDesc(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.TYPES_INDEX_SIG);
                        break;
                    default:
                        pushDesc("[J");
                        break;
                }
            case 189:
                pop();
                pushDesc("[" + ((Object) Type.getObjectType(str)));
                break;
            case 192:
                pop();
                pushDesc(Type.getObjectType(str).getDescriptor());
                break;
        }
        this.labels = null;
    }
}
