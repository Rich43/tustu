package com.sun.org.apache.bcel.internal.generic;

import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/JSR.class */
public class JSR extends JsrInstruction implements VariableLengthInstruction {
    JSR() {
    }

    public JSR(InstructionHandle target) {
        super((short) 168, target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        this.index = getTargetOffset();
        if (this.opcode == 168) {
            super.dump(out);
            return;
        }
        this.index = getTargetOffset();
        out.writeByte(this.opcode);
        out.writeInt(this.index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction
    protected int updatePosition(int offset, int max_offset) {
        int i2 = getTargetOffset();
        this.position += offset;
        if (Math.abs(i2) >= Short.MAX_VALUE - max_offset) {
            this.opcode = (short) 201;
            this.length = (short) 5;
            return 2;
        }
        return 0;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackProducer(this);
        v2.visitVariableLengthInstruction(this);
        v2.visitBranchInstruction(this);
        v2.visitJsrInstruction(this);
        v2.visitJSR(this);
    }
}
