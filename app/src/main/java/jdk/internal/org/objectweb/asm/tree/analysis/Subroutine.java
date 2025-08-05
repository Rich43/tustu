package jdk.internal.org.objectweb.asm.tree.analysis;

import java.util.ArrayList;
import java.util.List;
import jdk.internal.org.objectweb.asm.tree.JumpInsnNode;
import jdk.internal.org.objectweb.asm.tree.LabelNode;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/analysis/Subroutine.class */
class Subroutine {
    LabelNode start;
    boolean[] access;
    List<JumpInsnNode> callers;

    private Subroutine() {
    }

    Subroutine(LabelNode labelNode, int i2, JumpInsnNode jumpInsnNode) {
        this.start = labelNode;
        this.access = new boolean[i2];
        this.callers = new ArrayList();
        this.callers.add(jumpInsnNode);
    }

    public Subroutine copy() {
        Subroutine subroutine = new Subroutine();
        subroutine.start = this.start;
        subroutine.access = new boolean[this.access.length];
        System.arraycopy(this.access, 0, subroutine.access, 0, this.access.length);
        subroutine.callers = new ArrayList(this.callers);
        return subroutine;
    }

    public boolean merge(Subroutine subroutine) throws AnalyzerException {
        boolean z2 = false;
        for (int i2 = 0; i2 < this.access.length; i2++) {
            if (subroutine.access[i2] && !this.access[i2]) {
                this.access[i2] = true;
                z2 = true;
            }
        }
        if (subroutine.start == this.start) {
            for (int i3 = 0; i3 < subroutine.callers.size(); i3++) {
                JumpInsnNode jumpInsnNode = subroutine.callers.get(i3);
                if (!this.callers.contains(jumpInsnNode)) {
                    this.callers.add(jumpInsnNode);
                    z2 = true;
                }
            }
        }
        return z2;
    }
}
