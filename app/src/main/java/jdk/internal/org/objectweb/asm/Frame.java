package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/Frame.class */
final class Frame {
    static final int DIM = -268435456;
    static final int ARRAY_OF = 268435456;
    static final int ELEMENT_OF = -268435456;
    static final int KIND = 251658240;
    static final int TOP_IF_LONG_OR_DOUBLE = 8388608;
    static final int VALUE = 8388607;
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
        int[] iArr = new int[202];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE".charAt(i2) - 'E';
        }
        SIZE = iArr;
    }

    private int get(int i2) {
        if (this.outputLocals == null || i2 >= this.outputLocals.length) {
            return 33554432 | i2;
        }
        int i3 = this.outputLocals[i2];
        if (i3 == 0) {
            int i4 = 33554432 | i2;
            this.outputLocals[i2] = i4;
            i3 = i4;
        }
        return i3;
    }

    private void set(int i2, int i3) {
        if (this.outputLocals == null) {
            this.outputLocals = new int[10];
        }
        int length = this.outputLocals.length;
        if (i2 >= length) {
            int[] iArr = new int[Math.max(i2 + 1, 2 * length)];
            System.arraycopy(this.outputLocals, 0, iArr, 0, length);
            this.outputLocals = iArr;
        }
        this.outputLocals[i2] = i3;
    }

    private void push(int i2) {
        if (this.outputStack == null) {
            this.outputStack = new int[10];
        }
        int length = this.outputStack.length;
        if (this.outputStackTop >= length) {
            int[] iArr = new int[Math.max(this.outputStackTop + 1, 2 * length)];
            System.arraycopy(this.outputStack, 0, iArr, 0, length);
            this.outputStack = iArr;
        }
        int[] iArr2 = this.outputStack;
        int i3 = this.outputStackTop;
        this.outputStackTop = i3 + 1;
        iArr2[i3] = i2;
        int i4 = this.owner.inputStackTop + this.outputStackTop;
        if (i4 > this.owner.outputStackMax) {
            this.owner.outputStackMax = i4;
        }
    }

    private void push(ClassWriter classWriter, String str) {
        int iType = type(classWriter, str);
        if (iType != 0) {
            push(iType);
            if (iType == LONG || iType == DOUBLE) {
                push(16777216);
            }
        }
    }

    private static int type(ClassWriter classWriter, String str) {
        int iAddType;
        int iIndexOf = str.charAt(0) == '(' ? str.indexOf(41) + 1 : 0;
        switch (str.charAt(iIndexOf)) {
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
                int i2 = iIndexOf + 1;
                while (str.charAt(i2) == '[') {
                    i2++;
                }
                switch (str.charAt(i2)) {
                    case 'B':
                        iAddType = BYTE;
                        break;
                    case 'C':
                        iAddType = CHAR;
                        break;
                    case 'D':
                        iAddType = DOUBLE;
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
                        iAddType = OBJECT | classWriter.addType(str.substring(i2 + 1, str.length() - 1));
                        break;
                    case 'F':
                        iAddType = FLOAT;
                        break;
                    case 'I':
                        iAddType = INTEGER;
                        break;
                    case 'J':
                        iAddType = LONG;
                        break;
                    case 'S':
                        iAddType = SHORT;
                        break;
                    case 'Z':
                        iAddType = BOOLEAN;
                        break;
                }
                return ((i2 - iIndexOf) << 28) | iAddType;
            case 'F':
                return FLOAT;
            case 'J':
                return LONG;
            case 'L':
                return OBJECT | classWriter.addType(str.substring(iIndexOf + 1, str.length() - 1));
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

    private void pop(int i2) {
        if (this.outputStackTop >= i2) {
            this.outputStackTop -= i2;
            return;
        }
        this.owner.inputStackTop -= i2 - this.outputStackTop;
        this.outputStackTop = 0;
    }

    private void pop(String str) {
        char cCharAt = str.charAt(0);
        if (cCharAt == '(') {
            pop((Type.getArgumentsAndReturnSizes(str) >> 2) - 1);
        } else if (cCharAt == 'J' || cCharAt == 'D') {
            pop(2);
        } else {
            pop(1);
        }
    }

    private void init(int i2) {
        if (this.initializations == null) {
            this.initializations = new int[2];
        }
        int length = this.initializations.length;
        if (this.initializationCount >= length) {
            int[] iArr = new int[Math.max(this.initializationCount + 1, 2 * length)];
            System.arraycopy(this.initializations, 0, iArr, 0, length);
            this.initializations = iArr;
        }
        int[] iArr2 = this.initializations;
        int i3 = this.initializationCount;
        this.initializationCount = i3 + 1;
        iArr2[i3] = i2;
    }

    private int init(ClassWriter classWriter, int i2) {
        int iAddType;
        if (i2 == UNINITIALIZED_THIS) {
            iAddType = OBJECT | classWriter.addType(classWriter.thisName);
        } else if ((i2 & (-1048576)) == UNINITIALIZED) {
            iAddType = OBJECT | classWriter.addType(classWriter.typeTable[i2 & BASE_VALUE].strVal1);
        } else {
            return i2;
        }
        for (int i3 = 0; i3 < this.initializationCount; i3++) {
            int i4 = this.initializations[i3];
            int i5 = i4 & (-268435456);
            int i6 = i4 & KIND;
            if (i6 == 33554432) {
                i4 = i5 + this.inputLocals[i4 & 8388607];
            } else if (i6 == STACK) {
                i4 = i5 + this.inputStack[this.inputStack.length - (i4 & 8388607)];
            }
            if (i2 == i4) {
                return iAddType;
            }
        }
        return i2;
    }

    void initInputFrame(ClassWriter classWriter, int i2, Type[] typeArr, int i3) {
        this.inputLocals = new int[i3];
        this.inputStack = new int[0];
        int i4 = 0;
        if ((i2 & 8) == 0) {
            if ((i2 & 524288) == 0) {
                i4 = 0 + 1;
                this.inputLocals[0] = OBJECT | classWriter.addType(classWriter.thisName);
            } else {
                i4 = 0 + 1;
                this.inputLocals[0] = UNINITIALIZED_THIS;
            }
        }
        for (Type type : typeArr) {
            int iType = type(classWriter, type.getDescriptor());
            int i5 = i4;
            i4++;
            this.inputLocals[i5] = iType;
            if (iType == LONG || iType == DOUBLE) {
                i4++;
                this.inputLocals[i4] = 16777216;
            }
        }
        while (i4 < i3) {
            int i6 = i4;
            i4++;
            this.inputLocals[i6] = 16777216;
        }
    }

    void execute(int i2, int i3, ClassWriter classWriter, Item item) {
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
                        push(OBJECT | classWriter.addType("java/lang/Class"));
                        return;
                    case 8:
                        push(OBJECT | classWriter.addType("java/lang/String"));
                        return;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    default:
                        push(OBJECT | classWriter.addType("java/lang/invoke/MethodHandle"));
                        return;
                    case 16:
                        push(OBJECT | classWriter.addType("java/lang/invoke/MethodType"));
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
            case 196:
            case 197:
            default:
                pop(i3);
                push(classWriter, item.strVal1);
                return;
            case 25:
                push(get(i3));
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
                push((-268435456) + pop());
                return;
            case 54:
            case 56:
            case 58:
                set(i3, pop());
                if (i3 > 0) {
                    int i4 = get(i3 - 1);
                    if (i4 == LONG || i4 == DOUBLE) {
                        set(i3 - 1, 16777216);
                        return;
                    } else {
                        if ((i4 & KIND) != 16777216) {
                            set(i3 - 1, i4 | 8388608);
                            return;
                        }
                        return;
                    }
                }
                return;
            case 55:
            case 57:
                pop(1);
                set(i3, pop());
                set(i3 + 1, 16777216);
                if (i3 > 0) {
                    int i5 = get(i3 - 1);
                    if (i5 == LONG || i5 == DOUBLE) {
                        set(i3 - 1, 16777216);
                        return;
                    } else {
                        if ((i5 & KIND) != 16777216) {
                            set(i3 - 1, i5 | 8388608);
                            return;
                        }
                        return;
                    }
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
                int iPop = pop();
                push(iPop);
                push(iPop);
                return;
            case 90:
                int iPop2 = pop();
                int iPop3 = pop();
                push(iPop2);
                push(iPop3);
                push(iPop2);
                return;
            case 91:
                int iPop4 = pop();
                int iPop5 = pop();
                int iPop6 = pop();
                push(iPop4);
                push(iPop6);
                push(iPop5);
                push(iPop4);
                return;
            case 92:
                int iPop7 = pop();
                int iPop8 = pop();
                push(iPop8);
                push(iPop7);
                push(iPop8);
                push(iPop7);
                return;
            case 93:
                int iPop9 = pop();
                int iPop10 = pop();
                int iPop11 = pop();
                push(iPop10);
                push(iPop9);
                push(iPop11);
                push(iPop10);
                push(iPop9);
                return;
            case 94:
                int iPop12 = pop();
                int iPop13 = pop();
                int iPop14 = pop();
                int iPop15 = pop();
                push(iPop13);
                push(iPop12);
                push(iPop15);
                push(iPop14);
                push(iPop13);
                push(iPop12);
                return;
            case 95:
                int iPop16 = pop();
                int iPop17 = pop();
                push(iPop16);
                push(iPop17);
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
                set(i3, INTEGER);
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
                push(classWriter, item.strVal3);
                return;
            case 179:
                pop(item.strVal3);
                return;
            case 180:
                pop(1);
                push(classWriter, item.strVal3);
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
                if (i2 != 184) {
                    int iPop18 = pop();
                    if (i2 == 183 && item.strVal2.charAt(0) == '<') {
                        init(iPop18);
                    }
                }
                push(classWriter, item.strVal3);
                return;
            case 186:
                pop(item.strVal2);
                push(classWriter, item.strVal2);
                return;
            case 187:
                push(UNINITIALIZED | classWriter.addUninitializedType(item.strVal1, i3));
                return;
            case 188:
                pop();
                switch (i3) {
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
                String str = item.strVal1;
                pop();
                if (str.charAt(0) == '[') {
                    push(classWriter, '[' + str);
                    return;
                } else {
                    push(292552704 | classWriter.addType(str));
                    return;
                }
            case 192:
                String str2 = item.strVal1;
                pop();
                if (str2.charAt(0) == '[') {
                    push(classWriter, str2);
                    return;
                } else {
                    push(OBJECT | classWriter.addType(str2));
                    return;
                }
        }
    }

    boolean merge(ClassWriter classWriter, Frame frame, int i2) {
        int iInit;
        int iInit2;
        int i3;
        boolean zMerge = false;
        int length = this.inputLocals.length;
        int length2 = this.inputStack.length;
        if (frame.inputLocals == null) {
            frame.inputLocals = new int[length];
            zMerge = true;
        }
        int i4 = 0;
        while (i4 < length) {
            if (this.outputLocals == null || i4 >= this.outputLocals.length || (i3 = this.outputLocals[i4]) == 0) {
                iInit2 = this.inputLocals[i4];
            } else {
                int i5 = i3 & (-268435456);
                int i6 = i3 & KIND;
                if (i6 == 16777216) {
                    iInit2 = i3;
                } else {
                    if (i6 == 33554432) {
                        iInit2 = i5 + this.inputLocals[i3 & 8388607];
                    } else {
                        iInit2 = i5 + this.inputStack[length2 - (i3 & 8388607)];
                    }
                    if ((i3 & 8388608) != 0 && (iInit2 == LONG || iInit2 == DOUBLE)) {
                        iInit2 = 16777216;
                    }
                }
            }
            if (this.initializations != null) {
                iInit2 = init(classWriter, iInit2);
            }
            zMerge |= merge(classWriter, iInit2, frame.inputLocals, i4);
            i4++;
        }
        if (i2 > 0) {
            for (int i7 = 0; i7 < length; i7++) {
                zMerge |= merge(classWriter, this.inputLocals[i7], frame.inputLocals, i7);
            }
            if (frame.inputStack == null) {
                frame.inputStack = new int[1];
                zMerge = true;
            }
            return zMerge | merge(classWriter, i2, frame.inputStack, 0);
        }
        int length3 = this.inputStack.length + this.owner.inputStackTop;
        if (frame.inputStack == null) {
            frame.inputStack = new int[length3 + this.outputStackTop];
            zMerge = true;
        }
        for (int i8 = 0; i8 < length3; i8++) {
            int iInit3 = this.inputStack[i8];
            if (this.initializations != null) {
                iInit3 = init(classWriter, iInit3);
            }
            zMerge |= merge(classWriter, iInit3, frame.inputStack, i8);
        }
        for (int i9 = 0; i9 < this.outputStackTop; i9++) {
            int i10 = this.outputStack[i9];
            int i11 = i10 & (-268435456);
            int i12 = i10 & KIND;
            if (i12 == 16777216) {
                iInit = i10;
            } else {
                if (i12 == 33554432) {
                    iInit = i11 + this.inputLocals[i10 & 8388607];
                } else {
                    iInit = i11 + this.inputStack[length2 - (i10 & 8388607)];
                }
                if ((i10 & 8388608) != 0 && (iInit == LONG || iInit == DOUBLE)) {
                    iInit = 16777216;
                }
            }
            if (this.initializations != null) {
                iInit = init(classWriter, iInit);
            }
            zMerge |= merge(classWriter, iInit, frame.inputStack, length3 + i9);
        }
        return zMerge;
    }

    private static boolean merge(ClassWriter classWriter, int i2, int[] iArr, int i3) {
        int iMin;
        int i4 = iArr[i3];
        if (i4 == i2) {
            return false;
        }
        if ((i2 & 268435455) == NULL) {
            if (i4 == NULL) {
                return false;
            }
            i2 = NULL;
        }
        if (i4 == 0) {
            iArr[i3] = i2;
            return true;
        }
        if ((i4 & BASE_KIND) == OBJECT || (i4 & (-268435456)) != 0) {
            if (i2 == NULL) {
                return false;
            }
            if ((i2 & (-1048576)) == (i4 & (-1048576))) {
                if ((i4 & BASE_KIND) == OBJECT) {
                    iMin = (i2 & (-268435456)) | OBJECT | classWriter.getMergedType(i2 & BASE_VALUE, i4 & BASE_VALUE);
                } else {
                    iMin = ((-268435456) + (i4 & (-268435456))) | OBJECT | classWriter.addType("java/lang/Object");
                }
            } else if ((i2 & BASE_KIND) == OBJECT || (i2 & (-268435456)) != 0) {
                iMin = Math.min((((i2 & (-268435456)) == 0 || (i2 & BASE_KIND) == OBJECT) ? 0 : -268435456) + (i2 & (-268435456)), (((i4 & (-268435456)) == 0 || (i4 & BASE_KIND) == OBJECT) ? 0 : -268435456) + (i4 & (-268435456))) | OBJECT | classWriter.addType("java/lang/Object");
            } else {
                iMin = 16777216;
            }
        } else if (i4 == NULL) {
            iMin = ((i2 & BASE_KIND) == OBJECT || (i2 & (-268435456)) != 0) ? i2 : 16777216;
        } else {
            iMin = 16777216;
        }
        if (i4 != iMin) {
            iArr[i3] = iMin;
            return true;
        }
        return false;
    }
}
