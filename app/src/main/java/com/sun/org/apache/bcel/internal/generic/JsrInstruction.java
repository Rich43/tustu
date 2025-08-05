package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/JsrInstruction.class */
public abstract class JsrInstruction extends BranchInstruction implements UnconditionalBranch, TypedInstruction, StackProducer {
    JsrInstruction(short opcode, InstructionHandle target) {
        super(opcode, target);
    }

    JsrInstruction() {
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        return new ReturnaddressType(physicalSuccessor());
    }

    public InstructionHandle physicalSuccessor() {
        InstructionHandle ih;
        InstructionHandle prev = this.target;
        while (true) {
            ih = prev;
            if (ih.getPrev() == null) {
                break;
            }
            prev = ih.getPrev();
        }
        while (ih.getInstruction() != this) {
            ih = ih.getNext();
        }
        InstructionHandle toThis = ih;
        while (ih != null) {
            ih = ih.getNext();
            if (ih != null && ih.getInstruction() == this) {
                throw new RuntimeException("physicalSuccessor() called on a shared JsrInstruction.");
            }
        }
        return toThis.getNext();
    }
}
