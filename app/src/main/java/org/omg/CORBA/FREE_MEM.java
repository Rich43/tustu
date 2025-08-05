package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/FREE_MEM.class */
public final class FREE_MEM extends SystemException {
    public FREE_MEM() {
        this("");
    }

    public FREE_MEM(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public FREE_MEM(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public FREE_MEM(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
