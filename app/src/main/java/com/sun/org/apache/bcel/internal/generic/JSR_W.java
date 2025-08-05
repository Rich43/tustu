package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/JSR_W.class */
public class JSR_W extends JsrInstruction {
    JSR_W() {
    }

    public JSR_W(InstructionHandle target) {
        super((short) 201, target);
        this.length = (short) 5;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        this.index = getTargetOffset();
        out.writeByte(this.opcode);
        out.writeInt(this.index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        this.index = bytes.readInt();
        this.length = (short) 5;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackProducer(this);
        v2.visitBranchInstruction(this);
        v2.visitJsrInstruction(this);
        v2.visitJSR_W(this);
    }
}
