package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LDC_W.class */
public class LDC_W extends LDC {
    LDC_W() {
    }

    public LDC_W(int index) {
        super(index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LDC, com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        setIndex(bytes.readUnsignedShort());
        this.opcode = (short) 19;
    }
}
