package jdk.internal.org.objectweb.asm.tree.analysis;

import java.util.ArrayList;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;
import jdk.internal.org.objectweb.asm.tree.InvokeDynamicInsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.MultiANewArrayInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/Frame.class */
public class Frame<V extends Value> {
    private V returnValue;
    private V[] values;
    private int locals;
    private int top;

    public Frame(int i2, int i3) {
        this.values = (V[]) new Value[i2 + i3];
        this.locals = i2;
    }

    public Frame(Frame<? extends V> frame) {
        this(frame.locals, frame.values.length - frame.locals);
        init(frame);
    }

    public Frame<V> init(Frame<? extends V> frame) {
        this.returnValue = frame.returnValue;
        System.arraycopy(frame.values, 0, this.values, 0, this.values.length);
        this.top = frame.top;
        return this;
    }

    public void setReturn(V v2) {
        this.returnValue = v2;
    }

    public int getLocals() {
        return this.locals;
    }

    public int getMaxStackSize() {
        return this.values.length - this.locals;
    }

    public V getLocal(int i2) throws IndexOutOfBoundsException {
        if (i2 >= this.locals) {
            throw new IndexOutOfBoundsException("Trying to access an inexistant local variable");
        }
        return this.values[i2];
    }

    public void setLocal(int i2, V v2) throws IndexOutOfBoundsException {
        if (i2 >= this.locals) {
            throw new IndexOutOfBoundsException("Trying to access an inexistant local variable " + i2);
        }
        this.values[i2] = v2;
    }

    public int getStackSize() {
        return this.top;
    }

    public V getStack(int i2) throws IndexOutOfBoundsException {
        return this.values[i2 + this.locals];
    }

    public void clearStack() {
        this.top = 0;
    }

    public V pop() throws IndexOutOfBoundsException {
        if (this.top == 0) {
            throw new IndexOutOfBoundsException("Cannot pop operand off an empty stack.");
        }
        V[] vArr = this.values;
        int i2 = this.top - 1;
        this.top = i2;
        return vArr[i2 + this.locals];
    }

    public void push(V v2) throws IndexOutOfBoundsException {
        if (this.top + this.locals >= this.values.length) {
            throw new IndexOutOfBoundsException("Insufficient maximum stack size.");
        }
        V[] vArr = this.values;
        int i2 = this.top;
        this.top = i2 + 1;
        vArr[i2 + this.locals] = v2;
    }

    public void execute(AbstractInsnNode abstractInsnNode, Interpreter<V> interpreter) throws AnalyzerException {
        Value local;
        switch (abstractInsnNode.getOpcode()) {
            case 0:
            case 167:
            case 169:
                return;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
                push(interpreter.newOperation(abstractInsnNode));
                return;
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
            default:
                throw new RuntimeException("Illegal opcode " + abstractInsnNode.getOpcode());
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
                push(interpreter.copyOperation(abstractInsnNode, getLocal(((VarInsnNode) abstractInsnNode).var)));
                return;
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
                push(interpreter.binaryOperation(abstractInsnNode, pop(), pop()));
                return;
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
                Value valueCopyOperation = interpreter.copyOperation(abstractInsnNode, pop());
                int i2 = ((VarInsnNode) abstractInsnNode).var;
                setLocal(i2, valueCopyOperation);
                if (valueCopyOperation.getSize() == 2) {
                    setLocal(i2 + 1, interpreter.newValue(null));
                }
                if (i2 > 0 && (local = getLocal(i2 - 1)) != null && local.getSize() == 2) {
                    setLocal(i2 - 1, interpreter.newValue(null));
                    return;
                }
                return;
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
                interpreter.ternaryOperation(abstractInsnNode, pop(), pop(), pop());
                return;
            case 87:
                if (pop().getSize() == 2) {
                    throw new AnalyzerException(abstractInsnNode, "Illegal use of POP");
                }
                return;
            case 88:
                if (pop().getSize() == 1 && pop().getSize() != 1) {
                    throw new AnalyzerException(abstractInsnNode, "Illegal use of POP2");
                }
                return;
            case 89:
                Value valuePop = pop();
                if (valuePop.getSize() != 1) {
                    throw new AnalyzerException(abstractInsnNode, "Illegal use of DUP");
                }
                push(valuePop);
                push(interpreter.copyOperation(abstractInsnNode, valuePop));
                return;
            case 90:
                Value valuePop2 = pop();
                Value valuePop3 = pop();
                if (valuePop2.getSize() != 1 || valuePop3.getSize() != 1) {
                    throw new AnalyzerException(abstractInsnNode, "Illegal use of DUP_X1");
                }
                push(interpreter.copyOperation(abstractInsnNode, valuePop2));
                push(valuePop3);
                push(valuePop2);
                return;
            case 91:
                Value valuePop4 = pop();
                if (valuePop4.getSize() == 1) {
                    Value valuePop5 = pop();
                    if (valuePop5.getSize() == 1) {
                        Value valuePop6 = pop();
                        if (valuePop6.getSize() == 1) {
                            push(interpreter.copyOperation(abstractInsnNode, valuePop4));
                            push(valuePop6);
                            push(valuePop5);
                            push(valuePop4);
                            return;
                        }
                    } else {
                        push(interpreter.copyOperation(abstractInsnNode, valuePop4));
                        push(valuePop5);
                        push(valuePop4);
                        return;
                    }
                }
                throw new AnalyzerException(abstractInsnNode, "Illegal use of DUP_X2");
            case 92:
                Value valuePop7 = pop();
                if (valuePop7.getSize() == 1) {
                    Value valuePop8 = pop();
                    if (valuePop8.getSize() == 1) {
                        push(valuePop8);
                        push(valuePop7);
                        push(interpreter.copyOperation(abstractInsnNode, valuePop8));
                        push(interpreter.copyOperation(abstractInsnNode, valuePop7));
                        return;
                    }
                    throw new AnalyzerException(abstractInsnNode, "Illegal use of DUP2");
                }
                push(valuePop7);
                push(interpreter.copyOperation(abstractInsnNode, valuePop7));
                return;
            case 93:
                Value valuePop9 = pop();
                if (valuePop9.getSize() == 1) {
                    Value valuePop10 = pop();
                    if (valuePop10.getSize() == 1) {
                        Value valuePop11 = pop();
                        if (valuePop11.getSize() == 1) {
                            push(interpreter.copyOperation(abstractInsnNode, valuePop10));
                            push(interpreter.copyOperation(abstractInsnNode, valuePop9));
                            push(valuePop11);
                            push(valuePop10);
                            push(valuePop9);
                            return;
                        }
                    }
                } else {
                    Value valuePop12 = pop();
                    if (valuePop12.getSize() == 1) {
                        push(interpreter.copyOperation(abstractInsnNode, valuePop9));
                        push(valuePop12);
                        push(valuePop9);
                        return;
                    }
                }
                throw new AnalyzerException(abstractInsnNode, "Illegal use of DUP2_X1");
            case 94:
                Value valuePop13 = pop();
                if (valuePop13.getSize() == 1) {
                    Value valuePop14 = pop();
                    if (valuePop14.getSize() == 1) {
                        Value valuePop15 = pop();
                        if (valuePop15.getSize() == 1) {
                            Value valuePop16 = pop();
                            if (valuePop16.getSize() == 1) {
                                push(interpreter.copyOperation(abstractInsnNode, valuePop14));
                                push(interpreter.copyOperation(abstractInsnNode, valuePop13));
                                push(valuePop16);
                                push(valuePop15);
                                push(valuePop14);
                                push(valuePop13);
                                return;
                            }
                        } else {
                            push(interpreter.copyOperation(abstractInsnNode, valuePop14));
                            push(interpreter.copyOperation(abstractInsnNode, valuePop13));
                            push(valuePop15);
                            push(valuePop14);
                            push(valuePop13);
                            return;
                        }
                    }
                } else {
                    Value valuePop17 = pop();
                    if (valuePop17.getSize() == 1) {
                        Value valuePop18 = pop();
                        if (valuePop18.getSize() == 1) {
                            push(interpreter.copyOperation(abstractInsnNode, valuePop13));
                            push(valuePop18);
                            push(valuePop17);
                            push(valuePop13);
                            return;
                        }
                    } else {
                        push(interpreter.copyOperation(abstractInsnNode, valuePop13));
                        push(valuePop17);
                        push(valuePop13);
                        return;
                    }
                }
                throw new AnalyzerException(abstractInsnNode, "Illegal use of DUP2_X2");
            case 95:
                Value valuePop19 = pop();
                Value valuePop20 = pop();
                if (valuePop20.getSize() != 1 || valuePop19.getSize() != 1) {
                    throw new AnalyzerException(abstractInsnNode, "Illegal use of SWAP");
                }
                push(interpreter.copyOperation(abstractInsnNode, valuePop19));
                push(interpreter.copyOperation(abstractInsnNode, valuePop20));
                return;
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
                push(interpreter.binaryOperation(abstractInsnNode, pop(), pop()));
                return;
            case 116:
            case 117:
            case 118:
            case 119:
                push(interpreter.unaryOperation(abstractInsnNode, pop()));
                return;
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
                push(interpreter.binaryOperation(abstractInsnNode, pop(), pop()));
                return;
            case 132:
                int i3 = ((IincInsnNode) abstractInsnNode).var;
                setLocal(i3, interpreter.unaryOperation(abstractInsnNode, getLocal(i3)));
                return;
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
                push(interpreter.unaryOperation(abstractInsnNode, pop()));
                return;
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
                push(interpreter.binaryOperation(abstractInsnNode, pop(), pop()));
                return;
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
                interpreter.unaryOperation(abstractInsnNode, pop());
                return;
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
                interpreter.binaryOperation(abstractInsnNode, pop(), pop());
                return;
            case 168:
                push(interpreter.newOperation(abstractInsnNode));
                return;
            case 170:
            case 171:
                interpreter.unaryOperation(abstractInsnNode, pop());
                return;
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
                Value valuePop21 = pop();
                interpreter.unaryOperation(abstractInsnNode, valuePop21);
                interpreter.returnOperation(abstractInsnNode, valuePop21, this.returnValue);
                return;
            case 177:
                if (this.returnValue != null) {
                    throw new AnalyzerException(abstractInsnNode, "Incompatible return type");
                }
                return;
            case 178:
                push(interpreter.newOperation(abstractInsnNode));
                return;
            case 179:
                interpreter.unaryOperation(abstractInsnNode, pop());
                return;
            case 180:
                push(interpreter.unaryOperation(abstractInsnNode, pop()));
                return;
            case 181:
                interpreter.binaryOperation(abstractInsnNode, pop(), pop());
                return;
            case 182:
            case 183:
            case 184:
            case 185:
                ArrayList arrayList = new ArrayList();
                String str = ((MethodInsnNode) abstractInsnNode).desc;
                for (int length = Type.getArgumentTypes(str).length; length > 0; length--) {
                    arrayList.add(0, pop());
                }
                if (abstractInsnNode.getOpcode() != 184) {
                    arrayList.add(0, pop());
                }
                if (Type.getReturnType(str) == Type.VOID_TYPE) {
                    interpreter.naryOperation(abstractInsnNode, arrayList);
                    return;
                } else {
                    push(interpreter.naryOperation(abstractInsnNode, arrayList));
                    return;
                }
            case 186:
                ArrayList arrayList2 = new ArrayList();
                String str2 = ((InvokeDynamicInsnNode) abstractInsnNode).desc;
                for (int length2 = Type.getArgumentTypes(str2).length; length2 > 0; length2--) {
                    arrayList2.add(0, pop());
                }
                if (Type.getReturnType(str2) == Type.VOID_TYPE) {
                    interpreter.naryOperation(abstractInsnNode, arrayList2);
                    return;
                } else {
                    push(interpreter.naryOperation(abstractInsnNode, arrayList2));
                    return;
                }
            case 187:
                push(interpreter.newOperation(abstractInsnNode));
                return;
            case 188:
            case 189:
            case 190:
                push(interpreter.unaryOperation(abstractInsnNode, pop()));
                return;
            case 191:
                interpreter.unaryOperation(abstractInsnNode, pop());
                return;
            case 192:
            case 193:
                push(interpreter.unaryOperation(abstractInsnNode, pop()));
                return;
            case 194:
            case 195:
                interpreter.unaryOperation(abstractInsnNode, pop());
                return;
            case 197:
                ArrayList arrayList3 = new ArrayList();
                for (int i4 = ((MultiANewArrayInsnNode) abstractInsnNode).dims; i4 > 0; i4--) {
                    arrayList3.add(0, pop());
                }
                push(interpreter.naryOperation(abstractInsnNode, arrayList3));
                return;
            case 198:
            case 199:
                interpreter.unaryOperation(abstractInsnNode, pop());
                return;
        }
    }

    public boolean merge(Frame<? extends V> frame, Interpreter<V> interpreter) throws AnalyzerException {
        if (this.top != frame.top) {
            throw new AnalyzerException(null, "Incompatible stack heights");
        }
        boolean z2 = false;
        for (int i2 = 0; i2 < this.locals + this.top; i2++) {
            Value valueMerge = interpreter.merge(this.values[i2], frame.values[i2]);
            if (!valueMerge.equals(this.values[i2])) {
                ((V[]) this.values)[i2] = valueMerge;
                z2 = true;
            }
        }
        return z2;
    }

    public boolean merge(Frame<? extends V> frame, boolean[] zArr) {
        boolean z2 = false;
        for (int i2 = 0; i2 < this.locals; i2++) {
            if (!zArr[i2] && !this.values[i2].equals(frame.values[i2])) {
                this.values[i2] = frame.values[i2];
                z2 = true;
            }
        }
        return z2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < getLocals(); i2++) {
            sb.append((Object) getLocal(i2));
        }
        sb.append(' ');
        for (int i3 = 0; i3 < getStackSize(); i3++) {
            sb.append(getStack(i3).toString());
        }
        return sb.toString();
    }
}
