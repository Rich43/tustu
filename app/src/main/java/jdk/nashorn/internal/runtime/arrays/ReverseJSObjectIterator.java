package jdk.nashorn.internal.runtime.arrays;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.internal.runtime.JSType;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/arrays/ReverseJSObjectIterator.class */
final class ReverseJSObjectIterator extends JSObjectIterator {
    /*  JADX ERROR: Failed to decode insn: 0x0005: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[8]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    @Override // jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator
    protected long bumpIndex() {
        /*
            r8 = this;
            r0 = r8
            r1 = r0
            long r1 = r1.index
            // decode failed: arraycopy: source index -1 out of bounds for object array[8]
            r2 = 1
            long r1 = r1 - r2
            r0.index = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.nashorn.internal.runtime.arrays.ReverseJSObjectIterator.bumpIndex():long");
    }

    ReverseJSObjectIterator(JSObject obj, boolean includeUndefined) {
        super(obj, includeUndefined);
        this.index = JSType.toUint32(obj.hasMember("length") ? obj.getMember("length") : 0) - 1;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator
    public boolean isReverse() {
        return true;
    }

    @Override // jdk.nashorn.internal.runtime.arrays.JSObjectIterator
    protected boolean indexInArray() {
        return this.index >= 0;
    }
}
