package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ConversionInstruction.class */
public abstract class ConversionInstruction extends Instruction implements TypedInstruction, StackProducer, StackConsumer {
    ConversionInstruction() {
    }

    protected ConversionInstruction(short opcode) {
        super(opcode, (short) 1);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        switch (this.opcode) {
            case 133:
            case 140:
            case 143:
                return Type.LONG;
            case 134:
            case 137:
            case 144:
                return Type.FLOAT;
            case 135:
            case 138:
            case 141:
                return Type.DOUBLE;
            case 136:
            case 139:
            case 142:
                return Type.INT;
            case 145:
                return Type.BYTE;
            case 146:
                return Type.CHAR;
            case 147:
                return Type.SHORT;
            default:
                throw new ClassGenException("Unknown type " + ((int) this.opcode));
        }
    }
}
