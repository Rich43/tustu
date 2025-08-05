package sun.reflect;

import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:sun/reflect/Label.class */
class Label {
    private List<PatchInfo> patches = new ArrayList();

    /* loaded from: rt.jar:sun/reflect/Label$PatchInfo.class */
    static class PatchInfo {
        final ClassFileAssembler asm;
        final short instrBCI;
        final short patchBCI;
        final int stackDepth;

        PatchInfo(ClassFileAssembler classFileAssembler, short s2, short s3, int i2) {
            this.asm = classFileAssembler;
            this.instrBCI = s2;
            this.patchBCI = s3;
            this.stackDepth = i2;
        }
    }

    void add(ClassFileAssembler classFileAssembler, short s2, short s3, int i2) {
        this.patches.add(new PatchInfo(classFileAssembler, s2, s3, i2));
    }

    public void bind() {
        for (PatchInfo patchInfo : this.patches) {
            patchInfo.asm.emitShort(patchInfo.patchBCI, (short) (patchInfo.asm.getLength() - patchInfo.instrBCI));
            patchInfo.asm.setStack(patchInfo.stackDepth);
        }
    }
}
