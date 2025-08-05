package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.ConstantPool;
import java.io.IOException;
import java.util.Arrays;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.apache.commons.net.telnet.TelnetCommand;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/Instruction.class */
class Instruction {
    protected byte[] bytes;
    protected int pc;

    /* renamed from: bc, reason: collision with root package name */
    protected int f11848bc;

    /* renamed from: w, reason: collision with root package name */
    protected int f11849w;
    protected int length;
    protected boolean special;
    private static final byte[][] BC_LENGTH;
    private static final byte[][] BC_INDEX;
    private static final byte[][] BC_TAG;
    private static final byte[][] BC_BRANCH;
    private static final byte[][] BC_SLOT;
    private static final byte[][] BC_CON;
    private static final String[] BC_NAME;
    private static final String[][] BC_FORMAT;
    private static int BW;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Instruction.class.desiredAssertionStatus();
        BC_LENGTH = new byte[2][256];
        BC_INDEX = new byte[2][256];
        BC_TAG = new byte[2][256];
        BC_BRANCH = new byte[2][256];
        BC_SLOT = new byte[2][256];
        BC_CON = new byte[2][256];
        BC_NAME = new String[256];
        BC_FORMAT = new String[2][202];
        for (int i2 = 0; i2 < 202; i2++) {
            BC_LENGTH[0][i2] = -1;
            BC_LENGTH[1][i2] = -1;
        }
        def(PdfOps.b_TOKEN, 0, 15);
        def("bx", 16);
        def("bxx", 17);
        def("bk", 18);
        def("bkk", 19, 20);
        def("blwbll", 21, 25);
        def(PdfOps.b_TOKEN, 26, 53);
        def("blwbll", 54, 58);
        def(PdfOps.b_TOKEN, 59, 131);
        def("blxwbllxx", 132);
        def(PdfOps.b_TOKEN, 133, 152);
        def("boo", 153, 168);
        def("blwbll", 169);
        def("", 170, 171);
        def(PdfOps.b_TOKEN, 172, 177);
        def("bkf", 178, 181);
        def("bkm", 182, 184);
        def("bkixx", 185);
        def("bkyxx", 186);
        def("bkc", 187);
        def("bx", 188);
        def("bkc", 189);
        def(PdfOps.b_TOKEN, 190, 191);
        def("bkc", 192, 193);
        def(PdfOps.b_TOKEN, 194, 195);
        def("", 196);
        def("bkcx", 197);
        def("boo", 198, 199);
        def("boooo", 200, 201);
        for (int i3 = 0; i3 < 202; i3++) {
            if (BC_LENGTH[0][i3] != -1 && BC_LENGTH[1][i3] == -1) {
                BC_LENGTH[1][i3] = (byte) (1 + BC_LENGTH[0][i3]);
            }
        }
        String strSubstring = "nop aconst_null iconst_m1 iconst_0 iconst_1 iconst_2 iconst_3 iconst_4 iconst_5 lconst_0 lconst_1 fconst_0 fconst_1 fconst_2 dconst_0 dconst_1 bipush sipush ldc ldc_w ldc2_w iload lload fload dload aload iload_0 iload_1 iload_2 iload_3 lload_0 lload_1 lload_2 lload_3 fload_0 fload_1 fload_2 fload_3 dload_0 dload_1 dload_2 dload_3 aload_0 aload_1 aload_2 aload_3 iaload laload faload daload aaload baload caload saload istore lstore fstore dstore astore istore_0 istore_1 istore_2 istore_3 lstore_0 lstore_1 lstore_2 lstore_3 fstore_0 fstore_1 fstore_2 fstore_3 dstore_0 dstore_1 dstore_2 dstore_3 astore_0 astore_1 astore_2 astore_3 iastore lastore fastore dastore aastore bastore castore sastore pop pop2 dup dup_x1 dup_x2 dup2 dup2_x1 dup2_x2 swap iadd ladd fadd dadd isub lsub fsub dsub imul lmul fmul dmul idiv ldiv fdiv ddiv irem lrem frem drem ineg lneg fneg dneg ishl lshl ishr lshr iushr lushr iand land ior lor ixor lxor iinc i2l i2f i2d l2i l2f l2d f2i f2l f2d d2i d2l d2f i2b i2c i2s lcmp fcmpl fcmpg dcmpl dcmpg ifeq ifne iflt ifge ifgt ifle if_icmpeq if_icmpne if_icmplt if_icmpge if_icmpgt if_icmple if_acmpeq if_acmpne goto jsr ret tableswitch lookupswitch ireturn lreturn freturn dreturn areturn return getstatic putstatic getfield putfield invokevirtual invokespecial invokestatic invokeinterface invokedynamic new newarray anewarray arraylength athrow checkcast instanceof monitorenter monitorexit wide multianewarray ifnull ifnonnull goto_w jsr_w ";
        int i4 = 0;
        while (strSubstring.length() > 0) {
            int iIndexOf = strSubstring.indexOf(32);
            BC_NAME[i4] = strSubstring.substring(0, iIndexOf);
            strSubstring = strSubstring.substring(iIndexOf + 1);
            i4++;
        }
        BW = 4;
    }

    protected Instruction(byte[] bArr, int i2, int i3, int i4, int i5) {
        reset(bArr, i2, i3, i4, i5);
    }

    private void reset(byte[] bArr, int i2, int i3, int i4, int i5) {
        this.bytes = bArr;
        this.pc = i2;
        this.f11848bc = i3;
        this.f11849w = i4;
        this.length = i5;
    }

    public int getBC() {
        return this.f11848bc;
    }

    public boolean isWide() {
        return this.f11849w != 0;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public int getPC() {
        return this.pc;
    }

    public int getLength() {
        return this.length;
    }

    public int getNextPC() {
        return this.pc + this.length;
    }

    public Instruction next() {
        int i2 = this.pc + this.length;
        if (i2 == this.bytes.length) {
            return null;
        }
        return at(this.bytes, i2, this);
    }

    public boolean isNonstandard() {
        return isNonstandard(this.f11848bc);
    }

    public void setNonstandardLength(int i2) {
        if (!$assertionsDisabled && !isNonstandard()) {
            throw new AssertionError();
        }
        this.length = i2;
    }

    public Instruction forceNextPC(int i2) {
        return new Instruction(this.bytes, this.pc, -1, -1, i2 - this.pc);
    }

    public static Instruction at(byte[] bArr, int i2) {
        return at(bArr, i2, null);
    }

    public static Instruction at(byte[] bArr, int i2, Instruction instruction) {
        int i3 = getByte(bArr, i2);
        int i4 = 0;
        byte b2 = BC_LENGTH[0][i3];
        if (b2 == 0) {
            switch (i3) {
                case 170:
                    return new TableSwitch(bArr, i2);
                case 171:
                    return new LookupSwitch(bArr, i2);
                case 196:
                    i3 = getByte(bArr, i2 + 1);
                    i4 = 1;
                    b2 = BC_LENGTH[1][i3];
                    if (b2 == 0) {
                        b2 = 1;
                        break;
                    }
                    break;
                default:
                    b2 = 1;
                    break;
            }
        }
        if (!$assertionsDisabled && b2 <= 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && i2 + b2 > bArr.length) {
            throw new AssertionError();
        }
        if (instruction != null && !instruction.special) {
            instruction.reset(bArr, i2, i3, i4, b2);
            return instruction;
        }
        return new Instruction(bArr, i2, i3, i4, b2);
    }

    public byte getCPTag() {
        return BC_TAG[this.f11849w][this.f11848bc];
    }

    public int getCPIndex() {
        byte b2 = BC_INDEX[this.f11849w][this.f11848bc];
        if (b2 == 0) {
            return -1;
        }
        if (!$assertionsDisabled && this.f11849w != 0) {
            throw new AssertionError();
        }
        if (this.length == 2) {
            return getByte(this.bytes, this.pc + b2);
        }
        return getShort(this.bytes, this.pc + b2);
    }

    public void setCPIndex(int i2) {
        byte b2 = BC_INDEX[this.f11849w][this.f11848bc];
        if (!$assertionsDisabled && b2 == 0) {
            throw new AssertionError();
        }
        if (this.length == 2) {
            setByte(this.bytes, this.pc + b2, i2);
        } else {
            setShort(this.bytes, this.pc + b2, i2);
        }
        if (!$assertionsDisabled && getCPIndex() != i2) {
            throw new AssertionError();
        }
    }

    public ConstantPool.Entry getCPRef(ConstantPool.Entry[] entryArr) {
        int cPIndex = getCPIndex();
        if (cPIndex < 0) {
            return null;
        }
        return entryArr[cPIndex];
    }

    public int getLocalSlot() {
        byte b2 = BC_SLOT[this.f11849w][this.f11848bc];
        if (b2 == 0) {
            return -1;
        }
        if (this.f11849w == 0) {
            return getByte(this.bytes, this.pc + b2);
        }
        return getShort(this.bytes, this.pc + b2);
    }

    public int getBranchLabel() {
        int i2;
        byte b2 = BC_BRANCH[this.f11849w][this.f11848bc];
        if (b2 == 0) {
            return -1;
        }
        if (!$assertionsDisabled && this.f11849w != 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.length != 3 && this.length != 5) {
            throw new AssertionError();
        }
        if (this.length == 3) {
            i2 = (short) getShort(this.bytes, this.pc + b2);
        } else {
            i2 = getInt(this.bytes, this.pc + b2);
        }
        if (!$assertionsDisabled && i2 + this.pc < 0) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || i2 + this.pc <= this.bytes.length) {
            return i2 + this.pc;
        }
        throw new AssertionError();
    }

    public void setBranchLabel(int i2) {
        byte b2 = BC_BRANCH[this.f11849w][this.f11848bc];
        if (!$assertionsDisabled && b2 == 0) {
            throw new AssertionError();
        }
        if (this.length == 3) {
            setShort(this.bytes, this.pc + b2, i2 - this.pc);
        } else {
            setInt(this.bytes, this.pc + b2, i2 - this.pc);
        }
        if (!$assertionsDisabled && i2 != getBranchLabel()) {
            throw new AssertionError();
        }
    }

    public int getConstant() {
        byte b2 = BC_CON[this.f11849w][this.f11848bc];
        if (b2 == 0) {
            return 0;
        }
        switch (this.length - b2) {
            case 1:
                return (byte) getByte(this.bytes, this.pc + b2);
            case 2:
                return (short) getShort(this.bytes, this.pc + b2);
            default:
                if ($assertionsDisabled) {
                    return 0;
                }
                throw new AssertionError();
        }
    }

    public void setConstant(int i2) {
        byte b2 = BC_CON[this.f11849w][this.f11848bc];
        if (!$assertionsDisabled && b2 == 0) {
            throw new AssertionError();
        }
        switch (this.length - b2) {
            case 1:
                setByte(this.bytes, this.pc + b2, i2);
                break;
            case 2:
                setShort(this.bytes, this.pc + b2, i2);
                break;
        }
        if (!$assertionsDisabled && i2 != getConstant()) {
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Instruction$Switch.class */
    public static abstract class Switch extends Instruction {
        protected int apc;

        public abstract int getCaseCount();

        public abstract int getCaseValue(int i2);

        public abstract int getCaseLabel(int i2);

        public abstract void setCaseCount(int i2);

        public abstract void setCaseValue(int i2, int i3);

        public abstract void setCaseLabel(int i2, int i3);

        protected abstract int getLength(int i2);

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getByteAt(int i2) {
            return super.getByteAt(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getShortAt(int i2) {
            return super.getShortAt(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getIntAt(int i2) {
            return super.getIntAt(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ String toString(ConstantPool.Entry[] entryArr) {
            return super.toString(entryArr);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ boolean equals(Instruction instruction) {
            return super.equals(instruction);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int hashCode() {
            return super.hashCode();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ void setConstant(int i2) {
            super.setConstant(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getConstant() {
            return super.getConstant();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ void setBranchLabel(int i2) {
            super.setBranchLabel(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getBranchLabel() {
            return super.getBranchLabel();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getLocalSlot() {
            return super.getLocalSlot();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ ConstantPool.Entry getCPRef(ConstantPool.Entry[] entryArr) {
            return super.getCPRef(entryArr);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ void setCPIndex(int i2) {
            super.setCPIndex(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getCPIndex() {
            return super.getCPIndex();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ byte getCPTag() {
            return super.getCPTag();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ Instruction forceNextPC(int i2) {
            return super.forceNextPC(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ void setNonstandardLength(int i2) {
            super.setNonstandardLength(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ boolean isNonstandard() {
            return super.isNonstandard();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ Instruction next() {
            return super.next();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getNextPC() {
            return super.getNextPC();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getLength() {
            return super.getLength();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getPC() {
            return super.getPC();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ byte[] getBytes() {
            return super.getBytes();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ boolean isWide() {
            return super.isWide();
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public /* bridge */ /* synthetic */ int getBC() {
            return super.getBC();
        }

        public int getDefaultLabel() {
            return intAt(0) + this.pc;
        }

        public void setDefaultLabel(int i2) {
            setIntAt(0, i2 - this.pc);
        }

        protected int intAt(int i2) {
            return getInt(this.bytes, this.apc + (i2 * 4));
        }

        protected void setIntAt(int i2, int i3) {
            setInt(this.bytes, this.apc + (i2 * 4), i3);
        }

        protected Switch(byte[] bArr, int i2, int i3) {
            super(bArr, i2, i3, 0, 0);
            this.apc = alignPC(i2 + 1);
            this.special = true;
            this.length = getLength(getCaseCount());
        }

        public int getAlignedPC() {
            return this.apc;
        }

        @Override // com.sun.java.util.jar.pack.Instruction
        public String toString() {
            String str = super.toString() + " Default:" + labstr(getDefaultLabel());
            int caseCount = getCaseCount();
            for (int i2 = 0; i2 < caseCount; i2++) {
                str = str + "\n\tCase " + getCaseValue(i2) + CallSiteDescriptor.TOKEN_DELIMITER + labstr(getCaseLabel(i2));
            }
            return str;
        }

        public static int alignPC(int i2) {
            while (i2 % 4 != 0) {
                i2++;
            }
            return i2;
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Instruction$TableSwitch.class */
    public static class TableSwitch extends Switch {
        public int getLowCase() {
            return intAt(1);
        }

        public int getHighCase() {
            return intAt(2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public int getCaseCount() {
            return (intAt(2) - intAt(1)) + 1;
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public int getCaseValue(int i2) {
            return getLowCase() + i2;
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public int getCaseLabel(int i2) {
            return intAt(3 + i2) + this.pc;
        }

        public void setLowCase(int i2) {
            setIntAt(1, i2);
        }

        public void setHighCase(int i2) {
            setIntAt(2, i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public void setCaseLabel(int i2, int i3) {
            setIntAt(3 + i2, i3 - this.pc);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public void setCaseCount(int i2) {
            setHighCase((getLowCase() + i2) - 1);
            this.length = getLength(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public void setCaseValue(int i2, int i3) {
            if (i2 != 0) {
                throw new UnsupportedOperationException();
            }
            int caseCount = getCaseCount();
            setLowCase(i3);
            setCaseCount(caseCount);
        }

        TableSwitch(byte[] bArr, int i2) {
            super(bArr, i2, 170);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        protected int getLength(int i2) {
            return (this.apc - this.pc) + ((3 + i2) * 4);
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Instruction$LookupSwitch.class */
    public static class LookupSwitch extends Switch {
        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public int getCaseCount() {
            return intAt(1);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public int getCaseValue(int i2) {
            return intAt(2 + (i2 * 2) + 0);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public int getCaseLabel(int i2) {
            return intAt(2 + (i2 * 2) + 1) + this.pc;
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public void setCaseCount(int i2) {
            setIntAt(1, i2);
            this.length = getLength(i2);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public void setCaseValue(int i2, int i3) {
            setIntAt(2 + (i2 * 2) + 0, i3);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        public void setCaseLabel(int i2, int i3) {
            setIntAt(2 + (i2 * 2) + 1, i3 - this.pc);
        }

        LookupSwitch(byte[] bArr, int i2) {
            super(bArr, i2, 171);
        }

        @Override // com.sun.java.util.jar.pack.Instruction.Switch
        protected int getLength(int i2) {
            return (this.apc - this.pc) + ((2 + (i2 * 2)) * 4);
        }
    }

    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == Instruction.class && equals((Instruction) obj);
    }

    public int hashCode() {
        return (11 * ((11 * ((11 * ((11 * ((11 * 3) + Arrays.hashCode(this.bytes))) + this.pc)) + this.f11848bc)) + this.f11849w)) + this.length;
    }

    public boolean equals(Instruction instruction) {
        if (this.pc != instruction.pc || this.f11848bc != instruction.f11848bc || this.f11849w != instruction.f11849w || this.length != instruction.length) {
            return false;
        }
        for (int i2 = 1; i2 < this.length; i2++) {
            if (this.bytes[this.pc + i2] != instruction.bytes[instruction.pc + i2]) {
                return false;
            }
        }
        return true;
    }

    static String labstr(int i2) {
        if (i2 >= 0 && i2 < 100000) {
            return ((Config.MAX_REPEAT_NUM + i2) + "").substring(1);
        }
        return i2 + "";
    }

    public String toString() {
        return toString(null);
    }

    public String toString(ConstantPool.Entry[] entryArr) {
        String str = labstr(this.pc) + ": ";
        if (this.f11848bc >= 202) {
            return str + Integer.toHexString(this.f11848bc);
        }
        if (this.f11849w == 1) {
            str = str + "wide ";
        }
        String str2 = this.f11848bc < BC_NAME.length ? BC_NAME[this.f11848bc] : null;
        if (str2 == null) {
            return str + "opcode#" + this.f11848bc;
        }
        String str3 = str + str2;
        byte cPTag = getCPTag();
        if (cPTag != 0) {
            str3 = str3 + " " + ConstantPool.tagName(cPTag) + CallSiteDescriptor.TOKEN_DELIMITER;
        }
        int cPIndex = getCPIndex();
        if (cPIndex >= 0) {
            str3 = str3 + (entryArr == null ? "" + cPIndex : "=" + entryArr[cPIndex].stringValue());
        }
        int localSlot = getLocalSlot();
        if (localSlot >= 0) {
            str3 = str3 + " Local:" + localSlot;
        }
        int branchLabel = getBranchLabel();
        if (branchLabel >= 0) {
            str3 = str3 + " To:" + labstr(branchLabel);
        }
        int constant = getConstant();
        if (constant != 0) {
            str3 = str3 + " Con:" + constant;
        }
        return str3;
    }

    public int getIntAt(int i2) {
        return getInt(this.bytes, this.pc + i2);
    }

    public int getShortAt(int i2) {
        return getShort(this.bytes, this.pc + i2);
    }

    public int getByteAt(int i2) {
        return getByte(this.bytes, this.pc + i2);
    }

    public static int getInt(byte[] bArr, int i2) {
        return (getShort(bArr, i2 + 0) << 16) + (getShort(bArr, i2 + 2) << 0);
    }

    public static int getShort(byte[] bArr, int i2) {
        return (getByte(bArr, i2 + 0) << 8) + (getByte(bArr, i2 + 1) << 0);
    }

    public static int getByte(byte[] bArr, int i2) {
        return bArr[i2] & 255;
    }

    public static void setInt(byte[] bArr, int i2, int i3) {
        setShort(bArr, i2 + 0, i3 >> 16);
        setShort(bArr, i2 + 2, i3 >> 0);
    }

    public static void setShort(byte[] bArr, int i2, int i3) {
        setByte(bArr, i2 + 0, i3 >> 8);
        setByte(bArr, i2 + 1, i3 >> 0);
    }

    public static void setByte(byte[] bArr, int i2, int i3) {
        bArr[i2] = (byte) i3;
    }

    public static boolean isNonstandard(int i2) {
        return BC_LENGTH[0][i2] < 0;
    }

    public static int opLength(int i2) {
        byte b2 = BC_LENGTH[0][i2];
        if ($assertionsDisabled || b2 > 0) {
            return b2;
        }
        throw new AssertionError();
    }

    public static int opWideLength(int i2) {
        byte b2 = BC_LENGTH[1][i2];
        if ($assertionsDisabled || b2 > 0) {
            return b2;
        }
        throw new AssertionError();
    }

    public static boolean isLocalSlotOp(int i2) {
        return i2 < BC_SLOT[0].length && BC_SLOT[0][i2] > 0;
    }

    public static boolean isBranchOp(int i2) {
        return i2 < BC_BRANCH[0].length && BC_BRANCH[0][i2] > 0;
    }

    public static boolean isCPRefOp(int i2) {
        if (i2 >= BC_INDEX[0].length || BC_INDEX[0][i2] <= 0) {
            return (i2 >= 233 && i2 < 242) || i2 == 242 || i2 == 243;
        }
        return true;
    }

    public static byte getCPRefOpTag(int i2) {
        if (i2 < BC_INDEX[0].length && BC_INDEX[0][i2] > 0) {
            return BC_TAG[0][i2];
        }
        if (i2 < 233 || i2 >= 242) {
            return (i2 == 243 || i2 == 242) ? (byte) 11 : (byte) 0;
        }
        return (byte) 51;
    }

    public static boolean isFieldOp(int i2) {
        return i2 >= 178 && i2 <= 181;
    }

    public static boolean isInvokeInitOp(int i2) {
        return i2 >= 230 && i2 < 233;
    }

    public static boolean isSelfLinkerOp(int i2) {
        return i2 >= 202 && i2 < 230;
    }

    public static String byteName(int i2) {
        String str;
        if (i2 < BC_NAME.length && BC_NAME[i2] != null) {
            str = BC_NAME[i2];
        } else if (!isSelfLinkerOp(i2)) {
            if (isInvokeInitOp(i2)) {
                int i3 = i2 - 230;
                switch (i3) {
                    case 0:
                        str = "*invokespecial_init_this";
                        break;
                    case 1:
                        str = "*invokespecial_init_super";
                        break;
                    default:
                        if (!$assertionsDisabled && i3 != 2) {
                            throw new AssertionError();
                        }
                        str = "*invokespecial_init_new";
                        break;
                }
            } else {
                switch (i2) {
                    case 233:
                        str = "*cldc";
                        break;
                    case 234:
                        str = "*ildc";
                        break;
                    case 235:
                        str = "*fldc";
                        break;
                    case 236:
                        str = "*cldc_w";
                        break;
                    case 237:
                        str = "*ildc_w";
                        break;
                    case 238:
                        str = "*fldc_w";
                        break;
                    case 239:
                        str = "*dldc2_w";
                        break;
                    case 240:
                        str = "*qldc";
                        break;
                    case 241:
                        str = "*qldc_w";
                        break;
                    case 242:
                    case 243:
                    case 244:
                    case 245:
                    case 246:
                    case 247:
                    case 248:
                    case TelnetCommand.GA /* 249 */:
                    case 250:
                    case 251:
                    case 252:
                    default:
                        str = "*bc#" + i2;
                        break;
                    case 253:
                        str = "*ref_escape";
                        break;
                    case 254:
                        str = "*byte_escape";
                        break;
                    case 255:
                        str = "*end";
                        break;
                }
            }
        } else {
            int i4 = i2 - 202;
            boolean z2 = i4 >= 14;
            if (z2) {
                i4 -= 14;
            }
            boolean z3 = i4 >= 7;
            if (z3) {
                i4 -= 7;
            }
            int i5 = 178 + i4;
            if (!$assertionsDisabled && (i5 < 178 || i5 > 184)) {
                throw new AssertionError();
            }
            String str2 = BC_NAME[i5] + (z2 ? "_super" : "_this");
            if (z3) {
                str2 = "aload_0&" + str2;
            }
            str = "*" + str2;
        }
        return str;
    }

    private static void def(String str, int i2) {
        def(str, i2, i2);
    }

    private static void def(String str, int i2, int i3) {
        String[] strArr = {str, null};
        if (str.indexOf(119) > 0) {
            strArr[1] = str.substring(str.indexOf(119));
            strArr[0] = str.substring(0, str.indexOf(119));
        }
        for (int i4 = 0; i4 <= 1; i4++) {
            String str2 = strArr[i4];
            if (str2 != null) {
                int length = str2.length();
                int iMax = Math.max(0, str2.indexOf(107));
                int i5 = 0;
                int iMax2 = Math.max(0, str2.indexOf(111));
                int iMax3 = Math.max(0, str2.indexOf(108));
                int iMax4 = Math.max(0, str2.indexOf(120));
                if (iMax > 0 && iMax + 1 < length) {
                    switch (str2.charAt(iMax + 1)) {
                        case 'c':
                            i5 = 7;
                            break;
                        case 'f':
                            i5 = 9;
                            break;
                        case 'i':
                            i5 = 11;
                            break;
                        case 'k':
                            i5 = 51;
                            break;
                        case 'm':
                            i5 = 10;
                            break;
                        case 'y':
                            i5 = 18;
                            break;
                    }
                    if (!$assertionsDisabled && i5 == 0) {
                        throw new AssertionError();
                    }
                } else if (iMax > 0 && length == 2) {
                    if (!$assertionsDisabled && i2 != 18) {
                        throw new AssertionError();
                    }
                    i5 = 51;
                }
                for (int i6 = i2; i6 <= i3; i6++) {
                    BC_FORMAT[i4][i6] = str2;
                    if (!$assertionsDisabled && BC_LENGTH[i4][i6] != -1) {
                        throw new AssertionError();
                    }
                    BC_LENGTH[i4][i6] = (byte) length;
                    BC_INDEX[i4][i6] = (byte) iMax;
                    BC_TAG[i4][i6] = (byte) i5;
                    if (!$assertionsDisabled && iMax == 0 && i5 != 0) {
                        throw new AssertionError();
                    }
                    BC_BRANCH[i4][i6] = (byte) iMax2;
                    BC_SLOT[i4][i6] = (byte) iMax3;
                    if (!$assertionsDisabled && iMax2 != 0 && iMax3 != 0) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && iMax2 != 0 && iMax != 0) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && iMax3 != 0 && iMax != 0) {
                        throw new AssertionError();
                    }
                    BC_CON[i4][i6] = (byte) iMax4;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0045, code lost:
    
        throw new com.sun.java.util.jar.pack.Instruction.FormatException("illegal opcode: " + r0 + " " + ((java.lang.Object) r7));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void opcodeChecker(byte[] r4, com.sun.java.util.jar.pack.ConstantPool.Entry[] r5, com.sun.java.util.jar.pack.Package.Version r6) throws com.sun.java.util.jar.pack.Instruction.FormatException {
        /*
            r0 = r4
            r1 = 0
            com.sun.java.util.jar.pack.Instruction r0 = at(r0, r1)
            r7 = r0
        L6:
            r0 = r7
            if (r0 == 0) goto Lce
            r0 = r7
            int r0 = r0.getBC()
            r8 = r0
            r0 = r8
            if (r0 < 0) goto L1d
            r0 = r8
            r1 = 201(0xc9, float:2.82E-43)
            if (r0 <= r1) goto L46
        L1d:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "illegal opcode: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r8
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = " "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r7
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r9 = r0
            com.sun.java.util.jar.pack.Instruction$FormatException r0 = new com.sun.java.util.jar.pack.Instruction$FormatException
            r1 = r0
            r2 = r9
            r1.<init>(r2)
            throw r0
        L46:
            r0 = r7
            r1 = r5
            com.sun.java.util.jar.pack.ConstantPool$Entry r0 = r0.getCPRef(r1)
            r9 = r0
            r0 = r9
            if (r0 == 0) goto Lc6
            r0 = r7
            byte r0 = r0.getCPTag()
            r10 = r0
            r0 = r9
            r1 = r10
            boolean r0 = r0.tagMatches(r1)
            r11 = r0
            r0 = r11
            if (r0 != 0) goto L91
            r0 = r7
            int r0 = r0.f11848bc
            r1 = 183(0xb7, float:2.56E-43)
            if (r0 == r1) goto L7a
            r0 = r7
            int r0 = r0.f11848bc
            r1 = 184(0xb8, float:2.58E-43)
            if (r0 != r1) goto L91
        L7a:
            r0 = r9
            r1 = 11
            boolean r0 = r0.tagMatches(r1)
            if (r0 == 0) goto L91
            r0 = r6
            com.sun.java.util.jar.pack.Package$Version r1 = com.sun.java.util.jar.pack.Constants.JAVA7_MAX_CLASS_VERSION
            boolean r0 = r0.greaterThan(r1)
            if (r0 == 0) goto L91
            r0 = 1
            r11 = r0
        L91:
            r0 = r11
            if (r0 != 0) goto Lc6
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "illegal reference, expected type="
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r10
            java.lang.String r1 = com.sun.java.util.jar.pack.ConstantPool.tagName(r1)
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = ": "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r7
            r2 = r5
            java.lang.String r1 = r1.toString(r2)
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r12 = r0
            com.sun.java.util.jar.pack.Instruction$FormatException r0 = new com.sun.java.util.jar.pack.Instruction$FormatException
            r1 = r0
            r2 = r12
            r1.<init>(r2)
            throw r0
        Lc6:
            r0 = r7
            com.sun.java.util.jar.pack.Instruction r0 = r0.next()
            r7 = r0
            goto L6
        Lce:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.java.util.jar.pack.Instruction.opcodeChecker(byte[], com.sun.java.util.jar.pack.ConstantPool$Entry[], com.sun.java.util.jar.pack.Package$Version):void");
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Instruction$FormatException.class */
    static class FormatException extends IOException {
        private static final long serialVersionUID = 3175572275651367015L;

        FormatException(String str) {
            super(str);
        }
    }
}
