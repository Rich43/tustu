package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/INV_FLAG.class */
public final class INV_FLAG extends SystemException {
    public INV_FLAG() {
        this("");
    }

    public INV_FLAG(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public INV_FLAG(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public INV_FLAG(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
