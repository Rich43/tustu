package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/ACTIVITY_COMPLETED.class */
public final class ACTIVITY_COMPLETED extends SystemException {
    public ACTIVITY_COMPLETED() {
        this("");
    }

    public ACTIVITY_COMPLETED(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public ACTIVITY_COMPLETED(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public ACTIVITY_COMPLETED(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
