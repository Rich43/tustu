package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/Instruction.class */
public abstract class Instruction implements Cloneable, Serializable {
    protected short length;
    protected short opcode;
    private static InstructionComparator cmp = InstructionComparator.DEFAULT;

    public abstract void accept(Visitor visitor);

    Instruction() {
        this.length = (short) 1;
        this.opcode = (short) -1;
    }

    public Instruction(short opcode, short length) {
        this.length = (short) 1;
        this.opcode = (short) -1;
        this.length = length;
        this.opcode = opcode;
    }

    public void dump(DataOutputStream out) throws IOException {
        out.writeByte(this.opcode);
    }

    public String getName() {
        return Constants.OPCODE_NAMES[this.opcode];
    }

    public String toString(boolean verbose) {
        if (verbose) {
            return getName() + "[" + ((int) this.opcode) + "](" + ((int) this.length) + ")";
        }
        return getName();
    }

    public String toString() {
        return toString(true);
    }

    public String toString(ConstantPool cp) {
        return toString(false);
    }

    public Instruction copy() {
        Instruction i2 = null;
        if (InstructionConstants.INSTRUCTIONS[getOpcode()] != null) {
            i2 = this;
        } else {
            try {
                i2 = (Instruction) clone();
            } catch (CloneNotSupportedException e2) {
                System.err.println(e2);
            }
        }
        return i2;
    }

    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
    }

    public static final Instruction readInstruction(ByteSequence bytes) throws Exception {
        boolean wide = false;
        short opcode = (short) bytes.readUnsignedByte();
        if (opcode == 196) {
            wide = true;
            opcode = (short) bytes.readUnsignedByte();
        }
        if (InstructionConstants.INSTRUCTIONS[opcode] != null) {
            return InstructionConstants.INSTRUCTIONS[opcode];
        }
        try {
            Class clazz = Class.forName(className(opcode));
            try {
                Instruction obj = (Instruction) clazz.newInstance();
                if (wide && !(obj instanceof LocalVariableInstruction) && !(obj instanceof IINC) && !(obj instanceof RET)) {
                    throw new Exception("Illegal opcode after wide: " + ((int) opcode));
                }
                obj.setOpcode(opcode);
                obj.initFromFile(bytes, wide);
                return obj;
            } catch (Exception e2) {
                throw new ClassGenException(e2.toString());
            }
        } catch (ClassNotFoundException e3) {
            throw new ClassGenException("Illegal opcode detected.");
        }
    }

    private static final String className(short opcode) {
        String name = Constants.OPCODE_NAMES[opcode].toUpperCase();
        try {
            int len = name.length();
            char ch1 = name.charAt(len - 2);
            char ch2 = name.charAt(len - 1);
            if (ch1 == '_' && ch2 >= '0' && ch2 <= '5') {
                name = name.substring(0, len - 2);
            }
            if (name.equals("ICONST_M1")) {
                name = "ICONST";
            }
        } catch (StringIndexOutOfBoundsException e2) {
            System.err.println(e2);
        }
        return "com.sun.org.apache.bcel.internal.generic." + name;
    }

    public int consumeStack(ConstantPoolGen cpg) {
        return Constants.CONSUME_STACK[this.opcode];
    }

    public int produceStack(ConstantPoolGen cpg) {
        return Constants.PRODUCE_STACK[this.opcode];
    }

    public short getOpcode() {
        return this.opcode;
    }

    public int getLength() {
        return this.length;
    }

    private void setOpcode(short opcode) {
        this.opcode = opcode;
    }

    void dispose() {
    }

    public static InstructionComparator getComparator() {
        return cmp;
    }

    public static void setComparator(InstructionComparator c2) {
        cmp = c2;
    }

    public boolean equals(Object that) {
        if (that instanceof Instruction) {
            return cmp.equals(this, (Instruction) that);
        }
        return false;
    }
}
