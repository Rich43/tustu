package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/Frame.class */
final class Frame {
    static final int DIM = -268435456;
    static final int ARRAY_OF = 268435456;
    static final int ELEMENT_OF = -268435456;
    static final int KIND = 251658240;
    static final int VALUE = 16777215;
    static final int BASE_KIND = 267386880;
    static final int BASE_VALUE = 1048575;
    static final int BASE = 16777216;
    static final int OBJECT = 24117248;
    static final int UNINITIALIZED = 25165824;
    private static final int LOCAL = 33554432;
    private static final int STACK = 50331648;
    static final int TOP = 16777216;
    static final int BOOLEAN = 16777225;
    static final int BYTE = 16777226;
    static final int CHAR = 16777227;
    static final int SHORT = 16777228;
    static final int INTEGER = 16777217;
    static final int FLOAT = 16777218;
    static final int DOUBLE = 16777219;
    static final int LONG = 16777220;
    static final int NULL = 16777221;
    static final int UNINITIALIZED_THIS = 16777222;
    static final int[] SIZE;
    Label owner;
    int[] inputLocals;
    int[] inputStack;
    private int[] outputLocals;
    private int[] outputStack;
    private int outputStackTop;
    private int initializationCount;
    private int[] initializations;

    Frame() {
    }

    static {
        int[] b2 = new int[202];
        for (int i2 = 0; i2 < b2.length; i2++) {
            b2[i2] = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE".charAt(i2) - 'E';
        }
        SIZE = b2;
    }

    private int get(int local) {
        if (this.outputLocals == null || local >= this.outputLocals.length) {
            return 33554432 | local;
        }
        int type = this.outputLocals[local];
        if (type == 0) {
            int i2 = 33554432 | local;
            this.outputLocals[local] = i2;
            type = i2;
        }
        return type;
    }

    private void set(int local, int type) {
        if (this.outputLocals == null) {
            this.outputLocals = new int[10];
        }
        int n2 = this.outputLocals.length;
        if (local >= n2) {
            int[] t2 = new int[Math.max(local + 1, 2 * n2)];
            System.arraycopy(this.outputLocals, 0, t2, 0, n2);
            this.outputLocals = t2;
        }
        this.outputLocals[local] = type;
    }

    private void push(int type) {
        if (this.outputStack == null) {
            this.outputStack = new int[10];
        }
        int n2 = this.outputStack.length;
        if (this.outputStackTop >= n2) {
            int[] t2 = new int[Math.max(this.outputStackTop + 1, 2 * n2)];
            System.arraycopy(this.outputStack, 0, t2, 0, n2);
            this.outputStack = t2;
        }
        int[] iArr = this.outputStack;
        int i2 = this.outputStackTop;
        this.outputStackTop = i2 + 1;
        iArr[i2] = type;
        int top = this.owner.inputStackTop + this.outputStackTop;
        if (top > this.owner.outputStackMax) {
            this.owner.outputStackMax = top;
        }
    }

    private void push(ClassWriter cw, String desc) {
        int type = type(cw, desc);
        if (type != 0) {
            push(type);
            if (type == LONG || type == DOUBLE) {
                push(16777216);
            }
        }
    }

    private static int type(ClassWriter cw, String desc) {
        int data;
        int index = desc.charAt(0) == '(' ? desc.indexOf(41) + 1 : 0;
        switch (desc.charAt(index)) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z':
                return INTEGER;
            case 'D':
                return DOUBLE;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
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
                int dims = index + 1;
                while (desc.charAt(dims) == '[') {
                    dims++;
                }
                switch (desc.charAt(dims)) {
                    case 'B':
                        data = BYTE;
                        break;
                    case 'C':
                        data = CHAR;
                        break;
                    case 'D':
                        data = DOUBLE;
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
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    default:
                        String t2 = desc.substring(dims + 1, desc.length() - 1);
                        data = OBJECT | cw.addType(t2);
                        break;
                    case 'F':
                        data = FLOAT;
                        break;
                    case 'I':
                        data = INTEGER;
                        break;
                    case 'J':
                        data = LONG;
                        break;
                    case 'S':
                        data = SHORT;
                        break;
                    case 'Z':
                        data = BOOLEAN;
                        break;
                }
                return ((dims - index) << 28) | data;
            case 'F':
                return FLOAT;
            case 'J':
                return LONG;
            case 'L':
                String t3 = desc.substring(index + 1, desc.length() - 1);
                return OBJECT | cw.addType(t3);
            case 'V':
                return 0;
        }
    }

    private int pop() {
        if (this.outputStackTop > 0) {
            int[] iArr = this.outputStack;
            int i2 = this.outputStackTop - 1;
            this.outputStackTop = i2;
            return iArr[i2];
        }
        Label label = this.owner;
        int i3 = label.inputStackTop - 1;
        label.inputStackTop = i3;
        return STACK | (-i3);
    }

    private void pop(int elements) {
        if (this.outputStackTop >= elements) {
            this.outputStackTop -= elements;
            return;
        }
        this.owner.inputStackTop -= elements - this.outputStackTop;
        this.outputStackTop = 0;
    }

    private void pop(String desc) {
        char c2 = desc.charAt(0);
        if (c2 == '(') {
            pop((MethodWriter.getArgumentsAndReturnSizes(desc) >> 2) - 1);
        } else if (c2 == 'J' || c2 == 'D') {
            pop(2);
        } else {
            pop(1);
        }
    }

    private void init(int var) {
        if (this.initializations == null) {
            this.initializations = new int[2];
        }
        int n2 = this.initializations.length;
        if (this.initializationCount >= n2) {
            int[] t2 = new int[Math.max(this.initializationCount + 1, 2 * n2)];
            System.arraycopy(this.initializations, 0, t2, 0, n2);
            this.initializations = t2;
        }
        int[] iArr = this.initializations;
        int i2 = this.initializationCount;
        this.initializationCount = i2 + 1;
        iArr[i2] = var;
    }

    private int init(ClassWriter cw, int t2) {
        int s2;
        if (t2 == UNINITIALIZED_THIS) {
            s2 = OBJECT | cw.addType(cw.thisName);
        } else if ((t2 & (-1048576)) == UNINITIALIZED) {
            String type = cw.typeTable[t2 & BASE_VALUE].strVal1;
            s2 = OBJECT | cw.addType(type);
        } else {
            return t2;
        }
        for (int j2 = 0; j2 < this.initializationCount; j2++) {
            int u2 = this.initializations[j2];
            int dim = u2 & (-268435456);
            int kind = u2 & KIND;
            if (kind == 33554432) {
                u2 = dim + this.inputLocals[u2 & 16777215];
            } else if (kind == STACK) {
                u2 = dim + this.inputStack[this.inputStack.length - (u2 & 16777215)];
            }
            if (t2 == u2) {
                return s2;
            }
        }
        return t2;
    }

    void initInputFrame(ClassWriter cw, int access, Type[] args, int maxLocals) {
        this.inputLocals = new int[maxLocals];
        this.inputStack = new int[0];
        int i2 = 0;
        if ((access & 8) == 0) {
            if ((access & 262144) == 0) {
                i2 = 0 + 1;
                this.inputLocals[0] = OBJECT | cw.addType(cw.thisName);
            } else {
                i2 = 0 + 1;
                this.inputLocals[0] = UNINITIALIZED_THIS;
            }
        }
        for (Type type : args) {
            int t2 = type(cw, type.getDescriptor());
            int i3 = i2;
            i2++;
            this.inputLocals[i3] = t2;
            if (t2 == LONG || t2 == DOUBLE) {
                i2++;
                this.inputLocals[i2] = 16777216;
            }
        }
        while (i2 < maxLocals) {
            int i4 = i2;
            i2++;
            this.inputLocals[i4] = 16777216;
        }
    }

    void execute(int opcode, int arg, ClassWriter cw, Item item) {
        switch (opcode) {
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
                return;
            case 1:
                push(NULL);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 16:
            case 17:
            case 21:
                push(INTEGER);
                return;
            case 9:
            case 10:
            case 22:
                push(LONG);
                push(16777216);
                return;
            case 11:
            case 12:
            case 13:
            case 23:
                push(FLOAT);
                return;
            case 14:
            case 15:
            case 24:
                push(DOUBLE);
                push(16777216);
                return;
            case 18:
                switch (item.type) {
                    case 3:
                        push(INTEGER);
                        return;
                    case 4:
                        push(FLOAT);
                        return;
                    case 5:
                        push(LONG);
                        push(16777216);
                        return;
                    case 6:
                        push(DOUBLE);
                        push(16777216);
                        return;
                    case 7:
                        push(OBJECT | cw.addType("java/lang/Class"));
                        return;
                    default:
                        push(OBJECT | cw.addType("java/lang/String"));
                        return;
                }
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
            case 186:
            case 196:
            case 197:
            default:
                pop(arg);
                push(cw, item.strVal1);
                return;
            case 25:
                push(get(arg));
                return;
            case 46:
            case 51:
            case 52:
            case 53:
                pop(2);
                push(INTEGER);
                return;
            case 47:
            case 143:
                pop(2);
                push(LONG);
                push(16777216);
                return;
            case 48:
                pop(2);
                push(FLOAT);
                return;
            case 49:
            case 138:
                pop(2);
                push(DOUBLE);
                push(16777216);
                return;
            case 50:
                pop(1);
                int t1 = pop();
                push((-268435456) + t1);
                return;
            case 54:
            case 56:
            case 58:
                int t12 = pop();
                set(arg, t12);
                if (arg > 0) {
                    int t2 = get(arg - 1);
                    if (t2 == LONG || t2 == DOUBLE) {
                        set(arg - 1, 16777216);
                        return;
                    }
                    return;
                }
                return;
            case 55:
            case 57:
                pop(1);
                int t13 = pop();
                set(arg, t13);
                set(arg + 1, 16777216);
                if (arg > 0) {
                    int t22 = get(arg - 1);
                    if (t22 == LONG || t22 == DOUBLE) {
                        set(arg - 1, 16777216);
                        return;
                    }
                    return;
                }
                return;
            case 79:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
                pop(3);
                return;
            case 80:
            case 82:
                pop(4);
                return;
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
                return;
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
                return;
            case 89:
                int t14 = pop();
                push(t14);
                push(t14);
                return;
            case 90:
                int t15 = pop();
                int t23 = pop();
                push(t15);
                push(t23);
                push(t15);
                return;
            case 91:
                int t16 = pop();
                int t24 = pop();
                int t3 = pop();
                push(t16);
                push(t3);
                push(t24);
                push(t16);
                return;
            case 92:
                int t17 = pop();
                int t25 = pop();
                push(t25);
                push(t17);
                push(t25);
                push(t17);
                return;
            case 93:
                int t18 = pop();
                int t26 = pop();
                int t32 = pop();
                push(t26);
                push(t18);
                push(t32);
                push(t26);
                push(t18);
                return;
            case 94:
                int t19 = pop();
                int t27 = pop();
                int t33 = pop();
                int t4 = pop();
                push(t27);
                push(t19);
                push(t4);
                push(t33);
                push(t27);
                push(t19);
                return;
            case 95:
                int t110 = pop();
                int t28 = pop();
                push(t110);
                push(t28);
                return;
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
                push(INTEGER);
                return;
            case 97:
            case 101:
            case 105:
            case 109:
            case 113:
            case 127:
            case 129:
            case 131:
                pop(4);
                push(LONG);
                push(16777216);
                return;
            case 98:
            case 102:
            case 106:
            case 110:
            case 114:
            case 137:
            case 144:
                pop(2);
                push(FLOAT);
                return;
            case 99:
            case 103:
            case 107:
            case 111:
            case 115:
                pop(4);
                push(DOUBLE);
                push(16777216);
                return;
            case 121:
            case 123:
            case 125:
                pop(3);
                push(LONG);
                push(16777216);
                return;
            case 132:
                set(arg, INTEGER);
                return;
            case 133:
            case 140:
                pop(1);
                push(LONG);
                push(16777216);
                return;
            case 134:
                pop(1);
                push(FLOAT);
                return;
            case 135:
            case 141:
                pop(1);
                push(DOUBLE);
                push(16777216);
                return;
            case 139:
            case 190:
            case 193:
                pop(1);
                push(INTEGER);
                return;
            case 148:
            case 151:
            case 152:
                pop(4);
                push(INTEGER);
                return;
            case 168:
            case 169:
                throw new RuntimeException("JSR/RET are not supported with computeFrames option");
            case 178:
                push(cw, item.strVal3);
                return;
            case 179:
                pop(item.strVal3);
                return;
            case 180:
                pop(1);
                push(cw, item.strVal3);
                return;
            case 181:
                pop(item.strVal3);
                pop();
                return;
            case 182:
            case 183:
            case 184:
            case 185:
                pop(item.strVal3);
                if (opcode != 184) {
                    int t111 = pop();
                    if (opcode == 183 && item.strVal2.charAt(0) == '<') {
                        init(t111);
                    }
                }
                push(cw, item.strVal3);
                return;
            case 187:
                push(UNINITIALIZED | cw.addUninitializedType(item.strVal1, arg));
                return;
            case 188:
                pop();
                switch (arg) {
                    case 4:
                        push(285212681);
                        return;
                    case 5:
                        push(285212683);
                        return;
                    case 6:
                        push(285212674);
                        return;
                    case 7:
                        push(285212675);
                        return;
                    case 8:
                        push(285212682);
                        return;
                    case 9:
                        push(285212684);
                        return;
                    case 10:
                        push(285212673);
                        return;
                    default:
                        push(285212676);
                        return;
                }
            case 189:
                String s2 = item.strVal1;
                pop();
                if (s2.charAt(0) == '[') {
                    push(cw, '[' + s2);
                    return;
                } else {
                    push(292552704 | cw.addType(s2));
                    return;
                }
            case 192:
                String s3 = item.strVal1;
                pop();
                if (s3.charAt(0) == '[') {
                    push(cw, s3);
                    return;
                } else {
                    push(OBJECT | cw.addType(s3));
                    return;
                }
        }
    }

    boolean merge(ClassWriter cw, Frame frame, int edge) {
        int t2;
        int t3;
        int s2;
        boolean changed = false;
        int nLocal = this.inputLocals.length;
        int nStack = this.inputStack.length;
        if (frame.inputLocals == null) {
            frame.inputLocals = new int[nLocal];
            changed = true;
        }
        int i2 = 0;
        while (i2 < nLocal) {
            if (this.outputLocals == null || i2 >= this.outputLocals.length || (s2 = this.outputLocals[i2]) == 0) {
                t3 = this.inputLocals[i2];
            } else {
                int dim = s2 & (-268435456);
                int kind = s2 & KIND;
                if (kind == 33554432) {
                    t3 = dim + this.inputLocals[s2 & 16777215];
                } else if (kind == STACK) {
                    t3 = dim + this.inputStack[nStack - (s2 & 16777215)];
                } else {
                    t3 = s2;
                }
            }
            if (this.initializations != null) {
                t3 = init(cw, t3);
            }
            changed |= merge(cw, t3, frame.inputLocals, i2);
            i2++;
        }
        if (edge > 0) {
            for (int i3 = 0; i3 < nLocal; i3++) {
                int t4 = this.inputLocals[i3];
                changed |= merge(cw, t4, frame.inputLocals, i3);
            }
            if (frame.inputStack == null) {
                frame.inputStack = new int[1];
                changed = true;
            }
            return changed | merge(cw, edge, frame.inputStack, 0);
        }
        int nInputStack = this.inputStack.length + this.owner.inputStackTop;
        if (frame.inputStack == null) {
            frame.inputStack = new int[nInputStack + this.outputStackTop];
            changed = true;
        }
        for (int i4 = 0; i4 < nInputStack; i4++) {
            int t5 = this.inputStack[i4];
            if (this.initializations != null) {
                t5 = init(cw, t5);
            }
            changed |= merge(cw, t5, frame.inputStack, i4);
        }
        for (int i5 = 0; i5 < this.outputStackTop; i5++) {
            int s3 = this.outputStack[i5];
            int dim2 = s3 & (-268435456);
            int kind2 = s3 & KIND;
            if (kind2 == 33554432) {
                t2 = dim2 + this.inputLocals[s3 & 16777215];
            } else if (kind2 == STACK) {
                t2 = dim2 + this.inputStack[nStack - (s3 & 16777215)];
            } else {
                t2 = s3;
            }
            if (this.initializations != null) {
                t2 = init(cw, t2);
            }
            changed |= merge(cw, t2, frame.inputStack, nInputStack + i5);
        }
        return changed;
    }

    private static boolean merge(ClassWriter cw, int t2, int[] types, int index) {
        int v2;
        int u2 = types[index];
        if (u2 == t2) {
            return false;
        }
        if ((t2 & 268435455) == NULL) {
            if (u2 == NULL) {
                return false;
            }
            t2 = NULL;
        }
        if (u2 == 0) {
            types[index] = t2;
            return true;
        }
        if ((u2 & BASE_KIND) == OBJECT || (u2 & (-268435456)) != 0) {
            if (t2 == NULL) {
                return false;
            }
            if ((t2 & (-1048576)) == (u2 & (-1048576))) {
                if ((u2 & BASE_KIND) == OBJECT) {
                    v2 = (t2 & (-268435456)) | OBJECT | cw.getMergedType(t2 & BASE_VALUE, u2 & BASE_VALUE);
                } else {
                    v2 = OBJECT | cw.addType("java/lang/Object");
                }
            } else if ((t2 & BASE_KIND) == OBJECT || (t2 & (-268435456)) != 0) {
                v2 = OBJECT | cw.addType("java/lang/Object");
            } else {
                v2 = 16777216;
            }
        } else if (u2 == NULL) {
            v2 = ((t2 & BASE_KIND) == OBJECT || (t2 & (-268435456)) != 0) ? t2 : 16777216;
        } else {
            v2 = 16777216;
        }
        if (u2 != v2) {
            types[index] = v2;
            return true;
        }
        return false;
    }
}
