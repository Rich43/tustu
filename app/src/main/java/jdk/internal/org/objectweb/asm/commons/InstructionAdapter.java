package jdk.internal.org.objectweb.asm.commons;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/InstructionAdapter.class */
public class InstructionAdapter extends MethodVisitor {
    public static final Type OBJECT_TYPE = Type.getType(Constants.OBJECT_SIG);

    public InstructionAdapter(MethodVisitor methodVisitor) {
        this(Opcodes.ASM5, methodVisitor);
        if (getClass() != InstructionAdapter.class) {
            throw new IllegalStateException();
        }
    }

    protected InstructionAdapter(int i2, MethodVisitor methodVisitor) {
        super(i2, methodVisitor);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i2) {
        switch (i2) {
            case 0:
                nop();
                return;
            case 1:
                aconst(null);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                iconst(i2 - 3);
                return;
            case 9:
            case 10:
                lconst(i2 - 9);
                return;
            case 11:
            case 12:
            case 13:
                fconst(i2 - 11);
                return;
            case 14:
            case 15:
                dconst(i2 - 14);
                return;
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
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
            case 132:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
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
            case 170:
            case 171:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 192:
            case 193:
            default:
                throw new IllegalArgumentException();
            case 46:
                aload(Type.INT_TYPE);
                return;
            case 47:
                aload(Type.LONG_TYPE);
                return;
            case 48:
                aload(Type.FLOAT_TYPE);
                return;
            case 49:
                aload(Type.DOUBLE_TYPE);
                return;
            case 50:
                aload(OBJECT_TYPE);
                return;
            case 51:
                aload(Type.BYTE_TYPE);
                return;
            case 52:
                aload(Type.CHAR_TYPE);
                return;
            case 53:
                aload(Type.SHORT_TYPE);
                return;
            case 79:
                astore(Type.INT_TYPE);
                return;
            case 80:
                astore(Type.LONG_TYPE);
                return;
            case 81:
                astore(Type.FLOAT_TYPE);
                return;
            case 82:
                astore(Type.DOUBLE_TYPE);
                return;
            case 83:
                astore(OBJECT_TYPE);
                return;
            case 84:
                astore(Type.BYTE_TYPE);
                return;
            case 85:
                astore(Type.CHAR_TYPE);
                return;
            case 86:
                astore(Type.SHORT_TYPE);
                return;
            case 87:
                pop();
                return;
            case 88:
                pop2();
                return;
            case 89:
                dup();
                return;
            case 90:
                dupX1();
                return;
            case 91:
                dupX2();
                return;
            case 92:
                dup2();
                return;
            case 93:
                dup2X1();
                return;
            case 94:
                dup2X2();
                return;
            case 95:
                swap();
                return;
            case 96:
                add(Type.INT_TYPE);
                return;
            case 97:
                add(Type.LONG_TYPE);
                return;
            case 98:
                add(Type.FLOAT_TYPE);
                return;
            case 99:
                add(Type.DOUBLE_TYPE);
                return;
            case 100:
                sub(Type.INT_TYPE);
                return;
            case 101:
                sub(Type.LONG_TYPE);
                return;
            case 102:
                sub(Type.FLOAT_TYPE);
                return;
            case 103:
                sub(Type.DOUBLE_TYPE);
                return;
            case 104:
                mul(Type.INT_TYPE);
                return;
            case 105:
                mul(Type.LONG_TYPE);
                return;
            case 106:
                mul(Type.FLOAT_TYPE);
                return;
            case 107:
                mul(Type.DOUBLE_TYPE);
                return;
            case 108:
                div(Type.INT_TYPE);
                return;
            case 109:
                div(Type.LONG_TYPE);
                return;
            case 110:
                div(Type.FLOAT_TYPE);
                return;
            case 111:
                div(Type.DOUBLE_TYPE);
                return;
            case 112:
                rem(Type.INT_TYPE);
                return;
            case 113:
                rem(Type.LONG_TYPE);
                return;
            case 114:
                rem(Type.FLOAT_TYPE);
                return;
            case 115:
                rem(Type.DOUBLE_TYPE);
                return;
            case 116:
                neg(Type.INT_TYPE);
                return;
            case 117:
                neg(Type.LONG_TYPE);
                return;
            case 118:
                neg(Type.FLOAT_TYPE);
                return;
            case 119:
                neg(Type.DOUBLE_TYPE);
                return;
            case 120:
                shl(Type.INT_TYPE);
                return;
            case 121:
                shl(Type.LONG_TYPE);
                return;
            case 122:
                shr(Type.INT_TYPE);
                return;
            case 123:
                shr(Type.LONG_TYPE);
                return;
            case 124:
                ushr(Type.INT_TYPE);
                return;
            case 125:
                ushr(Type.LONG_TYPE);
                return;
            case 126:
                and(Type.INT_TYPE);
                return;
            case 127:
                and(Type.LONG_TYPE);
                return;
            case 128:
                or(Type.INT_TYPE);
                return;
            case 129:
                or(Type.LONG_TYPE);
                return;
            case 130:
                xor(Type.INT_TYPE);
                return;
            case 131:
                xor(Type.LONG_TYPE);
                return;
            case 133:
                cast(Type.INT_TYPE, Type.LONG_TYPE);
                return;
            case 134:
                cast(Type.INT_TYPE, Type.FLOAT_TYPE);
                return;
            case 135:
                cast(Type.INT_TYPE, Type.DOUBLE_TYPE);
                return;
            case 136:
                cast(Type.LONG_TYPE, Type.INT_TYPE);
                return;
            case 137:
                cast(Type.LONG_TYPE, Type.FLOAT_TYPE);
                return;
            case 138:
                cast(Type.LONG_TYPE, Type.DOUBLE_TYPE);
                return;
            case 139:
                cast(Type.FLOAT_TYPE, Type.INT_TYPE);
                return;
            case 140:
                cast(Type.FLOAT_TYPE, Type.LONG_TYPE);
                return;
            case 141:
                cast(Type.FLOAT_TYPE, Type.DOUBLE_TYPE);
                return;
            case 142:
                cast(Type.DOUBLE_TYPE, Type.INT_TYPE);
                return;
            case 143:
                cast(Type.DOUBLE_TYPE, Type.LONG_TYPE);
                return;
            case 144:
                cast(Type.DOUBLE_TYPE, Type.FLOAT_TYPE);
                return;
            case 145:
                cast(Type.INT_TYPE, Type.BYTE_TYPE);
                return;
            case 146:
                cast(Type.INT_TYPE, Type.CHAR_TYPE);
                return;
            case 147:
                cast(Type.INT_TYPE, Type.SHORT_TYPE);
                return;
            case 148:
                lcmp();
                return;
            case 149:
                cmpl(Type.FLOAT_TYPE);
                return;
            case 150:
                cmpg(Type.FLOAT_TYPE);
                return;
            case 151:
                cmpl(Type.DOUBLE_TYPE);
                return;
            case 152:
                cmpg(Type.DOUBLE_TYPE);
                return;
            case 172:
                areturn(Type.INT_TYPE);
                return;
            case 173:
                areturn(Type.LONG_TYPE);
                return;
            case 174:
                areturn(Type.FLOAT_TYPE);
                return;
            case 175:
                areturn(Type.DOUBLE_TYPE);
                return;
            case 176:
                areturn(OBJECT_TYPE);
                return;
            case 177:
                areturn(Type.VOID_TYPE);
                return;
            case 190:
                arraylength();
                return;
            case 191:
                athrow();
                return;
            case 194:
                monitorenter();
                return;
            case 195:
                monitorexit();
                return;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int i2, int i3) {
        switch (i2) {
            case 16:
                iconst(i3);
                return;
            case 17:
                iconst(i3);
                return;
            case 188:
                switch (i3) {
                    case 4:
                        newarray(Type.BOOLEAN_TYPE);
                        return;
                    case 5:
                        newarray(Type.CHAR_TYPE);
                        return;
                    case 6:
                        newarray(Type.FLOAT_TYPE);
                        return;
                    case 7:
                        newarray(Type.DOUBLE_TYPE);
                        return;
                    case 8:
                        newarray(Type.BYTE_TYPE);
                        return;
                    case 9:
                        newarray(Type.SHORT_TYPE);
                        return;
                    case 10:
                        newarray(Type.INT_TYPE);
                        return;
                    case 11:
                        newarray(Type.LONG_TYPE);
                        return;
                    default:
                        throw new IllegalArgumentException();
                }
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i2, int i3) {
        switch (i2) {
            case 21:
                load(i3, Type.INT_TYPE);
                return;
            case 22:
                load(i3, Type.LONG_TYPE);
                return;
            case 23:
                load(i3, Type.FLOAT_TYPE);
                return;
            case 24:
                load(i3, Type.DOUBLE_TYPE);
                return;
            case 25:
                load(i3, OBJECT_TYPE);
                return;
            case 54:
                store(i3, Type.INT_TYPE);
                return;
            case 55:
                store(i3, Type.LONG_TYPE);
                return;
            case 56:
                store(i3, Type.FLOAT_TYPE);
                return;
            case 57:
                store(i3, Type.DOUBLE_TYPE);
                return;
            case 58:
                store(i3, OBJECT_TYPE);
                return;
            case 169:
                ret(i3);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i2, String str) {
        Type objectType = Type.getObjectType(str);
        switch (i2) {
            case 187:
                anew(objectType);
                return;
            case 188:
            case 190:
            case 191:
            default:
                throw new IllegalArgumentException();
            case 189:
                newarray(objectType);
                return;
            case 192:
                checkcast(objectType);
                return;
            case 193:
                instanceOf(objectType);
                return;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        switch (i2) {
            case 178:
                getstatic(str, str2, str3);
                return;
            case 179:
                putstatic(str, str2, str3);
                return;
            case 180:
                getfield(str, str2, str3);
                return;
            case 181:
                putfield(str, str2, str3);
                return;
            default:
                throw new IllegalArgumentException();
        }
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
        switch (i2) {
            case 182:
                invokevirtual(str, str2, str3, z2);
                return;
            case 183:
                invokespecial(str, str2, str3, z2);
                return;
            case 184:
                invokestatic(str, str2, str3, z2);
                return;
            case 185:
                invokeinterface(str, str2, str3);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        invokedynamic(str, str2, handle, objArr);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i2, Label label) {
        switch (i2) {
            case 153:
                ifeq(label);
                return;
            case 154:
                ifne(label);
                return;
            case 155:
                iflt(label);
                return;
            case 156:
                ifge(label);
                return;
            case 157:
                ifgt(label);
                return;
            case 158:
                ifle(label);
                return;
            case 159:
                ificmpeq(label);
                return;
            case 160:
                ificmpne(label);
                return;
            case 161:
                ificmplt(label);
                return;
            case 162:
                ificmpge(label);
                return;
            case 163:
                ificmpgt(label);
                return;
            case 164:
                ificmple(label);
                return;
            case 165:
                ifacmpeq(label);
                return;
            case 166:
                ifacmpne(label);
                return;
            case 167:
                goTo(label);
                return;
            case 168:
                jsr(label);
                return;
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
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            default:
                throw new IllegalArgumentException();
            case 198:
                ifnull(label);
                return;
            case 199:
                ifnonnull(label);
                return;
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        mark(label);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        if (obj instanceof Integer) {
            iconst(((Integer) obj).intValue());
            return;
        }
        if (obj instanceof Byte) {
            iconst(((Byte) obj).intValue());
            return;
        }
        if (obj instanceof Character) {
            iconst(((Character) obj).charValue());
            return;
        }
        if (obj instanceof Short) {
            iconst(((Short) obj).intValue());
            return;
        }
        if (obj instanceof Boolean) {
            iconst(((Boolean) obj).booleanValue() ? 1 : 0);
            return;
        }
        if (obj instanceof Float) {
            fconst(((Float) obj).floatValue());
            return;
        }
        if (obj instanceof Long) {
            lconst(((Long) obj).longValue());
            return;
        }
        if (obj instanceof Double) {
            dconst(((Double) obj).doubleValue());
            return;
        }
        if (obj instanceof String) {
            aconst(obj);
        } else if (obj instanceof Type) {
            tconst((Type) obj);
        } else {
            if (obj instanceof Handle) {
                hconst((Handle) obj);
                return;
            }
            throw new IllegalArgumentException();
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i2, int i3) {
        iinc(i2, i3);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr) {
        tableswitch(i2, i3, label, labelArr);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        lookupswitch(label, iArr, labelArr);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i2) {
        multianewarray(str, i2);
    }

    public void nop() {
        this.mv.visitInsn(0);
    }

    public void aconst(Object obj) {
        if (obj == null) {
            this.mv.visitInsn(1);
        } else {
            this.mv.visitLdcInsn(obj);
        }
    }

    public void iconst(int i2) {
        if (i2 >= -1 && i2 <= 5) {
            this.mv.visitInsn(3 + i2);
            return;
        }
        if (i2 >= -128 && i2 <= 127) {
            this.mv.visitIntInsn(16, i2);
        } else if (i2 >= -32768 && i2 <= 32767) {
            this.mv.visitIntInsn(17, i2);
        } else {
            this.mv.visitLdcInsn(Integer.valueOf(i2));
        }
    }

    public void lconst(long j2) {
        if (j2 == 0 || j2 == 1) {
            this.mv.visitInsn(9 + ((int) j2));
        } else {
            this.mv.visitLdcInsn(Long.valueOf(j2));
        }
    }

    public void fconst(float f2) {
        int iFloatToIntBits = Float.floatToIntBits(f2);
        if (iFloatToIntBits == 0 || iFloatToIntBits == 1065353216 || iFloatToIntBits == 1073741824) {
            this.mv.visitInsn(11 + ((int) f2));
        } else {
            this.mv.visitLdcInsn(Float.valueOf(f2));
        }
    }

    public void dconst(double d2) {
        long jDoubleToLongBits = Double.doubleToLongBits(d2);
        if (jDoubleToLongBits == 0 || jDoubleToLongBits == 4607182418800017408L) {
            this.mv.visitInsn(14 + ((int) d2));
        } else {
            this.mv.visitLdcInsn(Double.valueOf(d2));
        }
    }

    public void tconst(Type type) {
        this.mv.visitLdcInsn(type);
    }

    public void hconst(Handle handle) {
        this.mv.visitLdcInsn(handle);
    }

    public void load(int i2, Type type) {
        this.mv.visitVarInsn(type.getOpcode(21), i2);
    }

    public void aload(Type type) {
        this.mv.visitInsn(type.getOpcode(46));
    }

    public void store(int i2, Type type) {
        this.mv.visitVarInsn(type.getOpcode(54), i2);
    }

    public void astore(Type type) {
        this.mv.visitInsn(type.getOpcode(79));
    }

    public void pop() {
        this.mv.visitInsn(87);
    }

    public void pop2() {
        this.mv.visitInsn(88);
    }

    public void dup() {
        this.mv.visitInsn(89);
    }

    public void dup2() {
        this.mv.visitInsn(92);
    }

    public void dupX1() {
        this.mv.visitInsn(90);
    }

    public void dupX2() {
        this.mv.visitInsn(91);
    }

    public void dup2X1() {
        this.mv.visitInsn(93);
    }

    public void dup2X2() {
        this.mv.visitInsn(94);
    }

    public void swap() {
        this.mv.visitInsn(95);
    }

    public void add(Type type) {
        this.mv.visitInsn(type.getOpcode(96));
    }

    public void sub(Type type) {
        this.mv.visitInsn(type.getOpcode(100));
    }

    public void mul(Type type) {
        this.mv.visitInsn(type.getOpcode(104));
    }

    public void div(Type type) {
        this.mv.visitInsn(type.getOpcode(108));
    }

    public void rem(Type type) {
        this.mv.visitInsn(type.getOpcode(112));
    }

    public void neg(Type type) {
        this.mv.visitInsn(type.getOpcode(116));
    }

    public void shl(Type type) {
        this.mv.visitInsn(type.getOpcode(120));
    }

    public void shr(Type type) {
        this.mv.visitInsn(type.getOpcode(122));
    }

    public void ushr(Type type) {
        this.mv.visitInsn(type.getOpcode(124));
    }

    public void and(Type type) {
        this.mv.visitInsn(type.getOpcode(126));
    }

    public void or(Type type) {
        this.mv.visitInsn(type.getOpcode(128));
    }

    public void xor(Type type) {
        this.mv.visitInsn(type.getOpcode(130));
    }

    public void iinc(int i2, int i3) {
        this.mv.visitIincInsn(i2, i3);
    }

    public void cast(Type type, Type type2) {
        if (type != type2) {
            if (type == Type.DOUBLE_TYPE) {
                if (type2 == Type.FLOAT_TYPE) {
                    this.mv.visitInsn(144);
                    return;
                } else if (type2 == Type.LONG_TYPE) {
                    this.mv.visitInsn(143);
                    return;
                } else {
                    this.mv.visitInsn(142);
                    cast(Type.INT_TYPE, type2);
                    return;
                }
            }
            if (type == Type.FLOAT_TYPE) {
                if (type2 == Type.DOUBLE_TYPE) {
                    this.mv.visitInsn(141);
                    return;
                } else if (type2 == Type.LONG_TYPE) {
                    this.mv.visitInsn(140);
                    return;
                } else {
                    this.mv.visitInsn(139);
                    cast(Type.INT_TYPE, type2);
                    return;
                }
            }
            if (type == Type.LONG_TYPE) {
                if (type2 == Type.DOUBLE_TYPE) {
                    this.mv.visitInsn(138);
                    return;
                } else if (type2 == Type.FLOAT_TYPE) {
                    this.mv.visitInsn(137);
                    return;
                } else {
                    this.mv.visitInsn(136);
                    cast(Type.INT_TYPE, type2);
                    return;
                }
            }
            if (type2 == Type.BYTE_TYPE) {
                this.mv.visitInsn(145);
                return;
            }
            if (type2 == Type.CHAR_TYPE) {
                this.mv.visitInsn(146);
                return;
            }
            if (type2 == Type.DOUBLE_TYPE) {
                this.mv.visitInsn(135);
                return;
            }
            if (type2 == Type.FLOAT_TYPE) {
                this.mv.visitInsn(134);
            } else if (type2 == Type.LONG_TYPE) {
                this.mv.visitInsn(133);
            } else if (type2 == Type.SHORT_TYPE) {
                this.mv.visitInsn(147);
            }
        }
    }

    public void lcmp() {
        this.mv.visitInsn(148);
    }

    public void cmpl(Type type) {
        this.mv.visitInsn(type == Type.FLOAT_TYPE ? 149 : 151);
    }

    public void cmpg(Type type) {
        this.mv.visitInsn(type == Type.FLOAT_TYPE ? 150 : 152);
    }

    public void ifeq(Label label) {
        this.mv.visitJumpInsn(153, label);
    }

    public void ifne(Label label) {
        this.mv.visitJumpInsn(154, label);
    }

    public void iflt(Label label) {
        this.mv.visitJumpInsn(155, label);
    }

    public void ifge(Label label) {
        this.mv.visitJumpInsn(156, label);
    }

    public void ifgt(Label label) {
        this.mv.visitJumpInsn(157, label);
    }

    public void ifle(Label label) {
        this.mv.visitJumpInsn(158, label);
    }

    public void ificmpeq(Label label) {
        this.mv.visitJumpInsn(159, label);
    }

    public void ificmpne(Label label) {
        this.mv.visitJumpInsn(160, label);
    }

    public void ificmplt(Label label) {
        this.mv.visitJumpInsn(161, label);
    }

    public void ificmpge(Label label) {
        this.mv.visitJumpInsn(162, label);
    }

    public void ificmpgt(Label label) {
        this.mv.visitJumpInsn(163, label);
    }

    public void ificmple(Label label) {
        this.mv.visitJumpInsn(164, label);
    }

    public void ifacmpeq(Label label) {
        this.mv.visitJumpInsn(165, label);
    }

    public void ifacmpne(Label label) {
        this.mv.visitJumpInsn(166, label);
    }

    public void goTo(Label label) {
        this.mv.visitJumpInsn(167, label);
    }

    public void jsr(Label label) {
        this.mv.visitJumpInsn(168, label);
    }

    public void ret(int i2) {
        this.mv.visitVarInsn(169, i2);
    }

    public void tableswitch(int i2, int i3, Label label, Label... labelArr) {
        this.mv.visitTableSwitchInsn(i2, i3, label, labelArr);
    }

    public void lookupswitch(Label label, int[] iArr, Label[] labelArr) {
        this.mv.visitLookupSwitchInsn(label, iArr, labelArr);
    }

    public void areturn(Type type) {
        this.mv.visitInsn(type.getOpcode(172));
    }

    public void getstatic(String str, String str2, String str3) {
        this.mv.visitFieldInsn(178, str, str2, str3);
    }

    public void putstatic(String str, String str2, String str3) {
        this.mv.visitFieldInsn(179, str, str2, str3);
    }

    public void getfield(String str, String str2, String str3) {
        this.mv.visitFieldInsn(180, str, str2, str3);
    }

    public void putfield(String str, String str2, String str3) {
        this.mv.visitFieldInsn(181, str, str2, str3);
    }

    @Deprecated
    public void invokevirtual(String str, String str2, String str3) {
        if (this.api >= 327680) {
            invokevirtual(str, str2, str3, false);
        } else {
            this.mv.visitMethodInsn(182, str, str2, str3);
        }
    }

    public void invokevirtual(String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            if (z2) {
                throw new IllegalArgumentException("INVOKEVIRTUAL on interfaces require ASM 5");
            }
            invokevirtual(str, str2, str3);
            return;
        }
        this.mv.visitMethodInsn(182, str, str2, str3, z2);
    }

    @Deprecated
    public void invokespecial(String str, String str2, String str3) {
        if (this.api >= 327680) {
            invokespecial(str, str2, str3, false);
        } else {
            this.mv.visitMethodInsn(183, str, str2, str3, false);
        }
    }

    public void invokespecial(String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            if (z2) {
                throw new IllegalArgumentException("INVOKESPECIAL on interfaces require ASM 5");
            }
            invokespecial(str, str2, str3);
            return;
        }
        this.mv.visitMethodInsn(183, str, str2, str3, z2);
    }

    @Deprecated
    public void invokestatic(String str, String str2, String str3) {
        if (this.api >= 327680) {
            invokestatic(str, str2, str3, false);
        } else {
            this.mv.visitMethodInsn(184, str, str2, str3, false);
        }
    }

    public void invokestatic(String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            if (z2) {
                throw new IllegalArgumentException("INVOKESTATIC on interfaces require ASM 5");
            }
            invokestatic(str, str2, str3);
            return;
        }
        this.mv.visitMethodInsn(184, str, str2, str3, z2);
    }

    public void invokeinterface(String str, String str2, String str3) {
        this.mv.visitMethodInsn(185, str, str2, str3, true);
    }

    public void invokedynamic(String str, String str2, Handle handle, Object[] objArr) {
        this.mv.visitInvokeDynamicInsn(str, str2, handle, objArr);
    }

    public void anew(Type type) {
        this.mv.visitTypeInsn(187, type.getInternalName());
    }

    public void newarray(Type type) {
        int i2;
        switch (type.getSort()) {
            case 1:
                i2 = 4;
                break;
            case 2:
                i2 = 5;
                break;
            case 3:
                i2 = 8;
                break;
            case 4:
                i2 = 9;
                break;
            case 5:
                i2 = 10;
                break;
            case 6:
                i2 = 6;
                break;
            case 7:
                i2 = 11;
                break;
            case 8:
                i2 = 7;
                break;
            default:
                this.mv.visitTypeInsn(189, type.getInternalName());
                return;
        }
        this.mv.visitIntInsn(188, i2);
    }

    public void arraylength() {
        this.mv.visitInsn(190);
    }

    public void athrow() {
        this.mv.visitInsn(191);
    }

    public void checkcast(Type type) {
        this.mv.visitTypeInsn(192, type.getInternalName());
    }

    public void instanceOf(Type type) {
        this.mv.visitTypeInsn(193, type.getInternalName());
    }

    public void monitorenter() {
        this.mv.visitInsn(194);
    }

    public void monitorexit() {
        this.mv.visitInsn(195);
    }

    public void multianewarray(String str, int i2) {
        this.mv.visitMultiANewArrayInsn(str, i2);
    }

    public void ifnull(Label label) {
        this.mv.visitJumpInsn(198, label);
    }

    public void ifnonnull(Label label) {
        this.mv.visitJumpInsn(199, label);
    }

    public void mark(Label label) {
        this.mv.visitLabel(label);
    }
}
