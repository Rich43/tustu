package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/BranchHandle.class */
public final class BranchHandle extends InstructionHandle {

    /* renamed from: bi, reason: collision with root package name */
    private BranchInstruction f11996bi;
    private static BranchHandle bh_list = null;

    private BranchHandle(BranchInstruction i2) {
        super(i2);
        this.f11996bi = i2;
    }

    static final BranchHandle getBranchHandle(BranchInstruction i2) {
        if (bh_list == null) {
            return new BranchHandle(i2);
        }
        BranchHandle bh2 = bh_list;
        bh_list = (BranchHandle) bh2.next;
        bh2.setInstruction(i2);
        return bh2;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionHandle
    protected void addHandle() {
        this.next = bh_list;
        bh_list = this;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionHandle
    public int getPosition() {
        return this.f11996bi.position;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionHandle
    void setPosition(int pos) {
        this.f11996bi.position = pos;
        this.i_position = pos;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionHandle
    protected int updatePosition(int offset, int max_offset) {
        int x2 = this.f11996bi.updatePosition(offset, max_offset);
        this.i_position = this.f11996bi.position;
        return x2;
    }

    public void setTarget(InstructionHandle ih) {
        this.f11996bi.setTarget(ih);
    }

    public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
        this.f11996bi.updateTarget(old_ih, new_ih);
    }

    public InstructionHandle getTarget() {
        return this.f11996bi.getTarget();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionHandle
    public void setInstruction(Instruction i2) {
        super.setInstruction(i2);
        if (!(i2 instanceof BranchInstruction)) {
            throw new ClassGenException("Assigning " + ((Object) i2) + " to branch handle which is not a branch instruction");
        }
        this.f11996bi = (BranchInstruction) i2;
    }
}
