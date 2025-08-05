package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/InstructionComparator.class */
public interface InstructionComparator {
    public static final InstructionComparator DEFAULT = new InstructionComparator() { // from class: com.sun.org.apache.bcel.internal.generic.InstructionComparator.1
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.org.apache.bcel.internal.generic.InstructionComparator
        public boolean equals(Instruction instruction, Instruction instruction2) {
            if (instruction.opcode == instruction2.opcode) {
                if (!(instruction instanceof Select)) {
                    if (instruction instanceof BranchInstruction) {
                        return ((BranchInstruction) instruction).target == ((BranchInstruction) instruction2).target;
                    }
                    if (instruction instanceof ConstantPushInstruction) {
                        return ((ConstantPushInstruction) instruction).getValue().equals(((ConstantPushInstruction) instruction2).getValue());
                    }
                    return instruction instanceof IndexedInstruction ? ((IndexedInstruction) instruction).getIndex() == ((IndexedInstruction) instruction2).getIndex() : !(instruction instanceof NEWARRAY) || ((NEWARRAY) instruction).getTypecode() == ((NEWARRAY) instruction2).getTypecode();
                }
                InstructionHandle[] t1 = ((Select) instruction).getTargets();
                InstructionHandle[] t2 = ((Select) instruction2).getTargets();
                if (t1.length == t2.length) {
                    for (int i2 = 0; i2 < t1.length; i2++) {
                        if (t1[i2] != t2[i2]) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
    };

    boolean equals(Instruction instruction, Instruction instruction2);
}
