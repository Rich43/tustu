package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/PUSH.class */
public final class PUSH implements CompoundInstruction, VariableLengthInstruction, InstructionConstants {
    private Instruction instruction;

    public PUSH(ConstantPoolGen cp, int value) {
        if (value >= -1 && value <= 5) {
            this.instruction = INSTRUCTIONS[3 + value];
            return;
        }
        if (value >= -128 && value <= 127) {
            this.instruction = new BIPUSH((byte) value);
        } else if (value >= -32768 && value <= 32767) {
            this.instruction = new SIPUSH((short) value);
        } else {
            this.instruction = new LDC(cp.addInteger(value));
        }
    }

    public PUSH(ConstantPoolGen cp, boolean value) {
        this.instruction = INSTRUCTIONS[3 + (value ? 1 : 0)];
    }

    public PUSH(ConstantPoolGen cp, float value) {
        if (value == 0.0d) {
            this.instruction = FCONST_0;
            return;
        }
        if (value == 1.0d) {
            this.instruction = FCONST_1;
        } else if (value == 2.0d) {
            this.instruction = FCONST_2;
        } else {
            this.instruction = new LDC(cp.addFloat(value));
        }
    }

    public PUSH(ConstantPoolGen cp, long value) {
        if (value == 0) {
            this.instruction = LCONST_0;
        } else if (value == 1) {
            this.instruction = LCONST_1;
        } else {
            this.instruction = new LDC2_W(cp.addLong(value));
        }
    }

    public PUSH(ConstantPoolGen cp, double value) {
        if (value == 0.0d) {
            this.instruction = DCONST_0;
        } else if (value == 1.0d) {
            this.instruction = DCONST_1;
        } else {
            this.instruction = new LDC2_W(cp.addDouble(value));
        }
    }

    public PUSH(ConstantPoolGen cp, String value) {
        if (value == null) {
            this.instruction = ACONST_NULL;
        } else {
            this.instruction = new LDC(cp.addString(value));
        }
    }

    public PUSH(ConstantPoolGen cp, Number value) {
        if ((value instanceof Integer) || (value instanceof Short) || (value instanceof Byte)) {
            this.instruction = new PUSH(cp, value.intValue()).instruction;
            return;
        }
        if (value instanceof Double) {
            this.instruction = new PUSH(cp, value.doubleValue()).instruction;
        } else if (value instanceof Float) {
            this.instruction = new PUSH(cp, value.floatValue()).instruction;
        } else {
            if (value instanceof Long) {
                this.instruction = new PUSH(cp, value.longValue()).instruction;
                return;
            }
            throw new ClassGenException("What's this: " + ((Object) value));
        }
    }

    public PUSH(ConstantPoolGen cp, Character value) {
        this(cp, (int) value.charValue());
    }

    public PUSH(ConstantPoolGen cp, Boolean value) {
        this(cp, value.booleanValue());
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CompoundInstruction
    public final InstructionList getInstructionList() {
        return new InstructionList(this.instruction);
    }

    public final Instruction getInstruction() {
        return this.instruction;
    }

    public String toString() {
        return this.instruction.toString() + " (PUSH)";
    }
}
