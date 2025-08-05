package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/SWITCH.class */
public final class SWITCH implements CompoundInstruction {
    private int[] match;
    private InstructionHandle[] targets;
    private Select instruction;
    private int match_length;

    public SWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target, int max_gap) {
        this.match = (int[]) match.clone();
        this.targets = (InstructionHandle[]) targets.clone();
        int length = match.length;
        this.match_length = length;
        if (length < 2) {
            this.instruction = new TABLESWITCH(match, targets, target);
            return;
        }
        sort(0, this.match_length - 1);
        if (matchIsOrdered(max_gap)) {
            fillup(max_gap, target);
            this.instruction = new TABLESWITCH(this.match, this.targets, target);
        } else {
            this.instruction = new LOOKUPSWITCH(this.match, this.targets, target);
        }
    }

    public SWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target) {
        this(match, targets, target, 1);
    }

    private final void fillup(int max_gap, InstructionHandle target) {
        int max_size = this.match_length + (this.match_length * max_gap);
        int[] m_vec = new int[max_size];
        InstructionHandle[] t_vec = new InstructionHandle[max_size];
        int count = 1;
        m_vec[0] = this.match[0];
        t_vec[0] = this.targets[0];
        for (int i2 = 1; i2 < this.match_length; i2++) {
            int prev = this.match[i2 - 1];
            int gap = this.match[i2] - prev;
            for (int j2 = 1; j2 < gap; j2++) {
                m_vec[count] = prev + j2;
                t_vec[count] = target;
                count++;
            }
            m_vec[count] = this.match[i2];
            t_vec[count] = this.targets[i2];
            count++;
        }
        this.match = new int[count];
        this.targets = new InstructionHandle[count];
        System.arraycopy(m_vec, 0, this.match, 0, count);
        System.arraycopy(t_vec, 0, this.targets, 0, count);
    }

    private final void sort(int l2, int r2) {
        int i2 = l2;
        int j2 = r2;
        int m2 = this.match[(l2 + r2) / 2];
        while (true) {
            if (this.match[i2] < m2) {
                i2++;
            } else {
                while (m2 < this.match[j2]) {
                    j2--;
                }
                if (i2 <= j2) {
                    int h2 = this.match[i2];
                    this.match[i2] = this.match[j2];
                    this.match[j2] = h2;
                    InstructionHandle h22 = this.targets[i2];
                    this.targets[i2] = this.targets[j2];
                    this.targets[j2] = h22;
                    i2++;
                    j2--;
                }
                if (i2 > j2) {
                    break;
                }
            }
        }
        if (l2 < j2) {
            sort(l2, j2);
        }
        if (i2 < r2) {
            sort(i2, r2);
        }
    }

    private final boolean matchIsOrdered(int max_gap) {
        for (int i2 = 1; i2 < this.match_length; i2++) {
            if (this.match[i2] - this.match[i2 - 1] > max_gap) {
                return false;
            }
        }
        return true;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CompoundInstruction
    public final InstructionList getInstructionList() {
        return new InstructionList((BranchInstruction) this.instruction);
    }

    public final Instruction getInstruction() {
        return this.instruction;
    }
}
