package jdk.internal.org.objectweb.asm.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/Printer.class */
public abstract class Printer {
    public static final String[] OPCODES = new String[200];
    public static final String[] TYPES;
    public static final String[] HANDLE_TAG;
    protected final int api;
    protected final StringBuffer buf = new StringBuffer();
    public final List<Object> text = new ArrayList();

    public abstract void visit(int i2, int i3, String str, String str2, String str3, String[] strArr);

    public abstract void visitSource(String str, String str2);

    public abstract void visitOuterClass(String str, String str2, String str3);

    public abstract Printer visitClassAnnotation(String str, boolean z2);

    public abstract void visitClassAttribute(Attribute attribute);

    public abstract void visitInnerClass(String str, String str2, String str3, int i2);

    public abstract Printer visitField(int i2, String str, String str2, String str3, Object obj);

    public abstract Printer visitMethod(int i2, String str, String str2, String str3, String[] strArr);

    public abstract void visitClassEnd();

    public abstract void visit(String str, Object obj);

    public abstract void visitEnum(String str, String str2, String str3);

    public abstract Printer visitAnnotation(String str, String str2);

    public abstract Printer visitArray(String str);

    public abstract void visitAnnotationEnd();

    public abstract Printer visitFieldAnnotation(String str, boolean z2);

    public abstract void visitFieldAttribute(Attribute attribute);

    public abstract void visitFieldEnd();

    public abstract Printer visitAnnotationDefault();

    public abstract Printer visitMethodAnnotation(String str, boolean z2);

    public abstract Printer visitParameterAnnotation(int i2, String str, boolean z2);

    public abstract void visitMethodAttribute(Attribute attribute);

    public abstract void visitCode();

    public abstract void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2);

    public abstract void visitInsn(int i2);

    public abstract void visitIntInsn(int i2, int i3);

    public abstract void visitVarInsn(int i2, int i3);

    public abstract void visitTypeInsn(int i2, String str);

    public abstract void visitFieldInsn(int i2, String str, String str2, String str3);

    public abstract void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr);

    public abstract void visitJumpInsn(int i2, Label label);

    public abstract void visitLabel(Label label);

    public abstract void visitLdcInsn(Object obj);

    public abstract void visitIincInsn(int i2, int i3);

    public abstract void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr);

    public abstract void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr);

    public abstract void visitMultiANewArrayInsn(String str, int i2);

    public abstract void visitTryCatchBlock(Label label, Label label2, Label label3, String str);

    public abstract void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2);

    public abstract void visitLineNumber(int i2, Label label);

    public abstract void visitMaxs(int i2, int i3);

    public abstract void visitMethodEnd();

    static {
        int i2 = 0;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            int iIndexOf = "NOP,ACONST_NULL,ICONST_M1,ICONST_0,ICONST_1,ICONST_2,ICONST_3,ICONST_4,ICONST_5,LCONST_0,LCONST_1,FCONST_0,FCONST_1,FCONST_2,DCONST_0,DCONST_1,BIPUSH,SIPUSH,LDC,,,ILOAD,LLOAD,FLOAD,DLOAD,ALOAD,,,,,,,,,,,,,,,,,,,,,IALOAD,LALOAD,FALOAD,DALOAD,AALOAD,BALOAD,CALOAD,SALOAD,ISTORE,LSTORE,FSTORE,DSTORE,ASTORE,,,,,,,,,,,,,,,,,,,,,IASTORE,LASTORE,FASTORE,DASTORE,AASTORE,BASTORE,CASTORE,SASTORE,POP,POP2,DUP,DUP_X1,DUP_X2,DUP2,DUP2_X1,DUP2_X2,SWAP,IADD,LADD,FADD,DADD,ISUB,LSUB,FSUB,DSUB,IMUL,LMUL,FMUL,DMUL,IDIV,LDIV,FDIV,DDIV,IREM,LREM,FREM,DREM,INEG,LNEG,FNEG,DNEG,ISHL,LSHL,ISHR,LSHR,IUSHR,LUSHR,IAND,LAND,IOR,LOR,IXOR,LXOR,IINC,I2L,I2F,I2D,L2I,L2F,L2D,F2I,F2L,F2D,D2I,D2L,D2F,I2B,I2C,I2S,LCMP,FCMPL,FCMPG,DCMPL,DCMPG,IFEQ,IFNE,IFLT,IFGE,IFGT,IFLE,IF_ICMPEQ,IF_ICMPNE,IF_ICMPLT,IF_ICMPGE,IF_ICMPGT,IF_ICMPLE,IF_ACMPEQ,IF_ACMPNE,GOTO,JSR,RET,TABLESWITCH,LOOKUPSWITCH,IRETURN,LRETURN,FRETURN,DRETURN,ARETURN,RETURN,GETSTATIC,PUTSTATIC,GETFIELD,PUTFIELD,INVOKEVIRTUAL,INVOKESPECIAL,INVOKESTATIC,INVOKEINTERFACE,INVOKEDYNAMIC,NEW,NEWARRAY,ANEWARRAY,ARRAYLENGTH,ATHROW,CHECKCAST,INSTANCEOF,MONITORENTER,MONITOREXIT,,MULTIANEWARRAY,IFNULL,IFNONNULL,".indexOf(44, i4);
            if (iIndexOf <= 0) {
                break;
            }
            int i5 = i2;
            i2++;
            OPCODES[i5] = i4 + 1 == iIndexOf ? null : "NOP,ACONST_NULL,ICONST_M1,ICONST_0,ICONST_1,ICONST_2,ICONST_3,ICONST_4,ICONST_5,LCONST_0,LCONST_1,FCONST_0,FCONST_1,FCONST_2,DCONST_0,DCONST_1,BIPUSH,SIPUSH,LDC,,,ILOAD,LLOAD,FLOAD,DLOAD,ALOAD,,,,,,,,,,,,,,,,,,,,,IALOAD,LALOAD,FALOAD,DALOAD,AALOAD,BALOAD,CALOAD,SALOAD,ISTORE,LSTORE,FSTORE,DSTORE,ASTORE,,,,,,,,,,,,,,,,,,,,,IASTORE,LASTORE,FASTORE,DASTORE,AASTORE,BASTORE,CASTORE,SASTORE,POP,POP2,DUP,DUP_X1,DUP_X2,DUP2,DUP2_X1,DUP2_X2,SWAP,IADD,LADD,FADD,DADD,ISUB,LSUB,FSUB,DSUB,IMUL,LMUL,FMUL,DMUL,IDIV,LDIV,FDIV,DDIV,IREM,LREM,FREM,DREM,INEG,LNEG,FNEG,DNEG,ISHL,LSHL,ISHR,LSHR,IUSHR,LUSHR,IAND,LAND,IOR,LOR,IXOR,LXOR,IINC,I2L,I2F,I2D,L2I,L2F,L2D,F2I,F2L,F2D,D2I,D2L,D2F,I2B,I2C,I2S,LCMP,FCMPL,FCMPG,DCMPL,DCMPG,IFEQ,IFNE,IFLT,IFGE,IFGT,IFLE,IF_ICMPEQ,IF_ICMPNE,IF_ICMPLT,IF_ICMPGE,IF_ICMPGT,IF_ICMPLE,IF_ACMPEQ,IF_ACMPNE,GOTO,JSR,RET,TABLESWITCH,LOOKUPSWITCH,IRETURN,LRETURN,FRETURN,DRETURN,ARETURN,RETURN,GETSTATIC,PUTSTATIC,GETFIELD,PUTFIELD,INVOKEVIRTUAL,INVOKESPECIAL,INVOKESTATIC,INVOKEINTERFACE,INVOKEDYNAMIC,NEW,NEWARRAY,ANEWARRAY,ARRAYLENGTH,ATHROW,CHECKCAST,INSTANCEOF,MONITORENTER,MONITOREXIT,,MULTIANEWARRAY,IFNULL,IFNONNULL,".substring(i4, iIndexOf);
            i3 = iIndexOf + 1;
        }
        TYPES = new String[12];
        int i6 = 0;
        int i7 = 4;
        while (true) {
            int iIndexOf2 = "T_BOOLEAN,T_CHAR,T_FLOAT,T_DOUBLE,T_BYTE,T_SHORT,T_INT,T_LONG,".indexOf(44, i6);
            if (iIndexOf2 <= 0) {
                break;
            }
            int i8 = i7;
            i7++;
            TYPES[i8] = "T_BOOLEAN,T_CHAR,T_FLOAT,T_DOUBLE,T_BYTE,T_SHORT,T_INT,T_LONG,".substring(i6, iIndexOf2);
            i6 = iIndexOf2 + 1;
        }
        HANDLE_TAG = new String[10];
        int i9 = 0;
        int i10 = 1;
        while (true) {
            int iIndexOf3 = "H_GETFIELD,H_GETSTATIC,H_PUTFIELD,H_PUTSTATIC,H_INVOKEVIRTUAL,H_INVOKESTATIC,H_INVOKESPECIAL,H_NEWINVOKESPECIAL,H_INVOKEINTERFACE,".indexOf(44, i9);
            if (iIndexOf3 > 0) {
                int i11 = i10;
                i10++;
                HANDLE_TAG[i11] = "H_GETFIELD,H_GETSTATIC,H_PUTFIELD,H_PUTSTATIC,H_INVOKEVIRTUAL,H_INVOKESTATIC,H_INVOKESPECIAL,H_NEWINVOKESPECIAL,H_INVOKEINTERFACE,".substring(i9, iIndexOf3);
                i9 = iIndexOf3 + 1;
            } else {
                return;
            }
        }
    }

    protected Printer(int i2) {
        this.api = i2;
    }

    public Printer visitClassTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        throw new RuntimeException("Must be overriden");
    }

    public Printer visitFieldTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        throw new RuntimeException("Must be overriden");
    }

    public void visitParameter(String str, int i2) {
        throw new RuntimeException("Must be overriden");
    }

    public Printer visitMethodTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        throw new RuntimeException("Must be overriden");
    }

    @Deprecated
    public void visitMethodInsn(int i2, String str, String str2, String str3) {
        if (this.api >= 327680) {
            visitMethodInsn(i2, str, str2, str3, i2 == 185);
            return;
        }
        throw new RuntimeException("Must be overriden");
    }

    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            if (z2 != (i2 == 185)) {
                throw new IllegalArgumentException("INVOKESPECIAL/STATIC on interfaces require ASM 5");
            }
            visitMethodInsn(i2, str, str2, str3);
            return;
        }
        throw new RuntimeException("Must be overriden");
    }

    public Printer visitInsnAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        throw new RuntimeException("Must be overriden");
    }

    public Printer visitTryCatchAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        throw new RuntimeException("Must be overriden");
    }

    public Printer visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        throw new RuntimeException("Must be overriden");
    }

    public List<Object> getText() {
        return this.text;
    }

    public void print(PrintWriter printWriter) {
        printList(printWriter, this.text);
    }

    public static void appendString(StringBuffer stringBuffer, String str) {
        stringBuffer.append('\"');
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt == '\n') {
                stringBuffer.append("\\n");
            } else if (cCharAt == '\r') {
                stringBuffer.append("\\r");
            } else if (cCharAt == '\\') {
                stringBuffer.append("\\\\");
            } else if (cCharAt == '\"') {
                stringBuffer.append("\\\"");
            } else if (cCharAt < ' ' || cCharAt > 127) {
                stringBuffer.append("\\u");
                if (cCharAt < 16) {
                    stringBuffer.append("000");
                } else if (cCharAt < 256) {
                    stringBuffer.append("00");
                } else if (cCharAt < 4096) {
                    stringBuffer.append('0');
                }
                stringBuffer.append(Integer.toString(cCharAt, 16));
            } else {
                stringBuffer.append(cCharAt);
            }
        }
        stringBuffer.append('\"');
    }

    static void printList(PrintWriter printWriter, List<?> list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            Object obj = list.get(i2);
            if (obj instanceof List) {
                printList(printWriter, (List) obj);
            } else {
                printWriter.print(obj.toString());
            }
        }
    }
}
