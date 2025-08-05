package sun.reflect;

import com.sun.java.util.jar.pack.Constants;

/* loaded from: rt.jar:sun/reflect/ClassFileAssembler.class */
class ClassFileAssembler implements ClassFileConstants {
    private ByteVector vec;
    private short cpIdx;
    private int stack;
    private int maxStack;
    private int maxLocals;

    public ClassFileAssembler() {
        this(ByteVectorFactory.create());
    }

    public ClassFileAssembler(ByteVector byteVector) {
        this.cpIdx = (short) 0;
        this.stack = 0;
        this.maxStack = 0;
        this.maxLocals = 0;
        this.vec = byteVector;
    }

    public ByteVector getData() {
        return this.vec;
    }

    public short getLength() {
        return (short) this.vec.getLength();
    }

    public void emitMagicAndVersion() {
        emitInt(Constants.JAVA_MAGIC);
        emitShort((short) 0);
        emitShort((short) 49);
    }

    public void emitInt(int i2) {
        emitByte((byte) (i2 >> 24));
        emitByte((byte) ((i2 >> 16) & 255));
        emitByte((byte) ((i2 >> 8) & 255));
        emitByte((byte) (i2 & 255));
    }

    public void emitShort(short s2) {
        emitByte((byte) ((s2 >> 8) & 255));
        emitByte((byte) (s2 & 255));
    }

    void emitShort(short s2, short s3) {
        this.vec.put(s2, (byte) ((s3 >> 8) & 255));
        this.vec.put(s2 + 1, (byte) (s3 & 255));
    }

    public void emitByte(byte b2) {
        this.vec.add(b2);
    }

    public void append(ClassFileAssembler classFileAssembler) {
        append(classFileAssembler.vec);
    }

    public void append(ByteVector byteVector) {
        for (int i2 = 0; i2 < byteVector.getLength(); i2++) {
            emitByte(byteVector.get(i2));
        }
    }

    public short cpi() {
        if (this.cpIdx == 0) {
            throw new RuntimeException("Illegal use of ClassFileAssembler");
        }
        return this.cpIdx;
    }

    public void emitConstantPoolUTF8(String str) {
        byte[] bArrEncode = UTF8.encode(str);
        emitByte((byte) 1);
        emitShort((short) bArrEncode.length);
        for (byte b2 : bArrEncode) {
            emitByte(b2);
        }
        this.cpIdx = (short) (this.cpIdx + 1);
    }

    public void emitConstantPoolClass(short s2) {
        emitByte((byte) 7);
        emitShort(s2);
        this.cpIdx = (short) (this.cpIdx + 1);
    }

    public void emitConstantPoolNameAndType(short s2, short s3) {
        emitByte((byte) 12);
        emitShort(s2);
        emitShort(s3);
        this.cpIdx = (short) (this.cpIdx + 1);
    }

    public void emitConstantPoolFieldref(short s2, short s3) {
        emitByte((byte) 9);
        emitShort(s2);
        emitShort(s3);
        this.cpIdx = (short) (this.cpIdx + 1);
    }

    public void emitConstantPoolMethodref(short s2, short s3) {
        emitByte((byte) 10);
        emitShort(s2);
        emitShort(s3);
        this.cpIdx = (short) (this.cpIdx + 1);
    }

    public void emitConstantPoolInterfaceMethodref(short s2, short s3) {
        emitByte((byte) 11);
        emitShort(s2);
        emitShort(s3);
        this.cpIdx = (short) (this.cpIdx + 1);
    }

    public void emitConstantPoolString(short s2) {
        emitByte((byte) 8);
        emitShort(s2);
        this.cpIdx = (short) (this.cpIdx + 1);
    }

    private void incStack() {
        setStack(this.stack + 1);
    }

    private void decStack() {
        this.stack--;
    }

    public short getMaxStack() {
        return (short) this.maxStack;
    }

    public short getMaxLocals() {
        return (short) this.maxLocals;
    }

    public void setMaxLocals(int i2) {
        this.maxLocals = i2;
    }

    public int getStack() {
        return this.stack;
    }

    public void setStack(int i2) {
        this.stack = i2;
        if (this.stack > this.maxStack) {
            this.maxStack = this.stack;
        }
    }

    public void opc_aconst_null() {
        emitByte((byte) 1);
        incStack();
    }

    public void opc_sipush(short s2) {
        emitByte((byte) 17);
        emitShort(s2);
        incStack();
    }

    public void opc_ldc(byte b2) {
        emitByte((byte) 18);
        emitByte(b2);
        incStack();
    }

    public void opc_iload_0() {
        emitByte((byte) 26);
        if (this.maxLocals < 1) {
            this.maxLocals = 1;
        }
        incStack();
    }

    public void opc_iload_1() {
        emitByte((byte) 27);
        if (this.maxLocals < 2) {
            this.maxLocals = 2;
        }
        incStack();
    }

    public void opc_iload_2() {
        emitByte((byte) 28);
        if (this.maxLocals < 3) {
            this.maxLocals = 3;
        }
        incStack();
    }

    public void opc_iload_3() {
        emitByte((byte) 29);
        if (this.maxLocals < 4) {
            this.maxLocals = 4;
        }
        incStack();
    }

    public void opc_lload_0() {
        emitByte((byte) 30);
        if (this.maxLocals < 2) {
            this.maxLocals = 2;
        }
        incStack();
        incStack();
    }

    public void opc_lload_1() {
        emitByte((byte) 31);
        if (this.maxLocals < 3) {
            this.maxLocals = 3;
        }
        incStack();
        incStack();
    }

    public void opc_lload_2() {
        emitByte((byte) 32);
        if (this.maxLocals < 4) {
            this.maxLocals = 4;
        }
        incStack();
        incStack();
    }

    public void opc_lload_3() {
        emitByte((byte) 33);
        if (this.maxLocals < 5) {
            this.maxLocals = 5;
        }
        incStack();
        incStack();
    }

    public void opc_fload_0() {
        emitByte((byte) 34);
        if (this.maxLocals < 1) {
            this.maxLocals = 1;
        }
        incStack();
    }

    public void opc_fload_1() {
        emitByte((byte) 35);
        if (this.maxLocals < 2) {
            this.maxLocals = 2;
        }
        incStack();
    }

    public void opc_fload_2() {
        emitByte((byte) 36);
        if (this.maxLocals < 3) {
            this.maxLocals = 3;
        }
        incStack();
    }

    public void opc_fload_3() {
        emitByte((byte) 37);
        if (this.maxLocals < 4) {
            this.maxLocals = 4;
        }
        incStack();
    }

    public void opc_dload_0() {
        emitByte((byte) 38);
        if (this.maxLocals < 2) {
            this.maxLocals = 2;
        }
        incStack();
        incStack();
    }

    public void opc_dload_1() {
        emitByte((byte) 39);
        if (this.maxLocals < 3) {
            this.maxLocals = 3;
        }
        incStack();
        incStack();
    }

    public void opc_dload_2() {
        emitByte((byte) 40);
        if (this.maxLocals < 4) {
            this.maxLocals = 4;
        }
        incStack();
        incStack();
    }

    public void opc_dload_3() {
        emitByte((byte) 41);
        if (this.maxLocals < 5) {
            this.maxLocals = 5;
        }
        incStack();
        incStack();
    }

    public void opc_aload_0() {
        emitByte((byte) 42);
        if (this.maxLocals < 1) {
            this.maxLocals = 1;
        }
        incStack();
    }

    public void opc_aload_1() {
        emitByte((byte) 43);
        if (this.maxLocals < 2) {
            this.maxLocals = 2;
        }
        incStack();
    }

    public void opc_aload_2() {
        emitByte((byte) 44);
        if (this.maxLocals < 3) {
            this.maxLocals = 3;
        }
        incStack();
    }

    public void opc_aload_3() {
        emitByte((byte) 45);
        if (this.maxLocals < 4) {
            this.maxLocals = 4;
        }
        incStack();
    }

    public void opc_aaload() {
        emitByte((byte) 50);
        decStack();
    }

    public void opc_astore_0() {
        emitByte((byte) 75);
        if (this.maxLocals < 1) {
            this.maxLocals = 1;
        }
        decStack();
    }

    public void opc_astore_1() {
        emitByte((byte) 76);
        if (this.maxLocals < 2) {
            this.maxLocals = 2;
        }
        decStack();
    }

    public void opc_astore_2() {
        emitByte((byte) 77);
        if (this.maxLocals < 3) {
            this.maxLocals = 3;
        }
        decStack();
    }

    public void opc_astore_3() {
        emitByte((byte) 78);
        if (this.maxLocals < 4) {
            this.maxLocals = 4;
        }
        decStack();
    }

    public void opc_pop() {
        emitByte((byte) 87);
        decStack();
    }

    public void opc_dup() {
        emitByte((byte) 89);
        incStack();
    }

    public void opc_dup_x1() {
        emitByte((byte) 90);
        incStack();
    }

    public void opc_swap() {
        emitByte((byte) 95);
    }

    public void opc_i2l() {
        emitByte((byte) -123);
    }

    public void opc_i2f() {
        emitByte((byte) -122);
    }

    public void opc_i2d() {
        emitByte((byte) -121);
    }

    public void opc_l2f() {
        emitByte((byte) -119);
    }

    public void opc_l2d() {
        emitByte((byte) -118);
    }

    public void opc_f2d() {
        emitByte((byte) -115);
    }

    public void opc_ifeq(short s2) {
        emitByte((byte) -103);
        emitShort(s2);
        decStack();
    }

    public void opc_ifeq(Label label) {
        short length = getLength();
        emitByte((byte) -103);
        label.add(this, length, getLength(), getStack() - 1);
        emitShort((short) -1);
    }

    public void opc_if_icmpeq(short s2) {
        emitByte((byte) -97);
        emitShort(s2);
        setStack(getStack() - 2);
    }

    public void opc_if_icmpeq(Label label) {
        short length = getLength();
        emitByte((byte) -97);
        label.add(this, length, getLength(), getStack() - 2);
        emitShort((short) -1);
    }

    public void opc_goto(short s2) {
        emitByte((byte) -89);
        emitShort(s2);
    }

    public void opc_goto(Label label) {
        short length = getLength();
        emitByte((byte) -89);
        label.add(this, length, getLength(), getStack());
        emitShort((short) -1);
    }

    public void opc_ifnull(short s2) {
        emitByte((byte) -58);
        emitShort(s2);
        decStack();
    }

    public void opc_ifnull(Label label) {
        short length = getLength();
        emitByte((byte) -58);
        label.add(this, length, getLength(), getStack() - 1);
        emitShort((short) -1);
        decStack();
    }

    public void opc_ifnonnull(short s2) {
        emitByte((byte) -57);
        emitShort(s2);
        decStack();
    }

    public void opc_ifnonnull(Label label) {
        short length = getLength();
        emitByte((byte) -57);
        label.add(this, length, getLength(), getStack() - 1);
        emitShort((short) -1);
        decStack();
    }

    public void opc_ireturn() {
        emitByte((byte) -84);
        setStack(0);
    }

    public void opc_lreturn() {
        emitByte((byte) -83);
        setStack(0);
    }

    public void opc_freturn() {
        emitByte((byte) -82);
        setStack(0);
    }

    public void opc_dreturn() {
        emitByte((byte) -81);
        setStack(0);
    }

    public void opc_areturn() {
        emitByte((byte) -80);
        setStack(0);
    }

    public void opc_return() {
        emitByte((byte) -79);
        setStack(0);
    }

    public void opc_getstatic(short s2, int i2) {
        emitByte((byte) -78);
        emitShort(s2);
        setStack(getStack() + i2);
    }

    public void opc_putstatic(short s2, int i2) {
        emitByte((byte) -77);
        emitShort(s2);
        setStack(getStack() - i2);
    }

    public void opc_getfield(short s2, int i2) {
        emitByte((byte) -76);
        emitShort(s2);
        setStack((getStack() + i2) - 1);
    }

    public void opc_putfield(short s2, int i2) {
        emitByte((byte) -75);
        emitShort(s2);
        setStack((getStack() - i2) - 1);
    }

    public void opc_invokevirtual(short s2, int i2, int i3) {
        emitByte((byte) -74);
        emitShort(s2);
        setStack(((getStack() - i2) - 1) + i3);
    }

    public void opc_invokespecial(short s2, int i2, int i3) {
        emitByte((byte) -73);
        emitShort(s2);
        setStack(((getStack() - i2) - 1) + i3);
    }

    public void opc_invokestatic(short s2, int i2, int i3) {
        emitByte((byte) -72);
        emitShort(s2);
        setStack((getStack() - i2) + i3);
    }

    public void opc_invokeinterface(short s2, int i2, byte b2, int i3) {
        emitByte((byte) -71);
        emitShort(s2);
        emitByte(b2);
        emitByte((byte) 0);
        setStack(((getStack() - i2) - 1) + i3);
    }

    public void opc_arraylength() {
        emitByte((byte) -66);
    }

    public void opc_new(short s2) {
        emitByte((byte) -69);
        emitShort(s2);
        incStack();
    }

    public void opc_athrow() {
        emitByte((byte) -65);
        setStack(1);
    }

    public void opc_checkcast(short s2) {
        emitByte((byte) -64);
        emitShort(s2);
    }

    public void opc_instanceof(short s2) {
        emitByte((byte) -63);
        emitShort(s2);
    }
}
