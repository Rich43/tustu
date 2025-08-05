package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/INV_POLICY.class */
public final class INV_POLICY extends SystemException {
    public INV_POLICY() {
        this("");
    }

    public INV_POLICY(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public INV_POLICY(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public INV_POLICY(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
