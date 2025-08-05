package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/INVALID_ACTIVITY.class */
public final class INVALID_ACTIVITY extends SystemException {
    public INVALID_ACTIVITY() {
        this("");
    }

    public INVALID_ACTIVITY(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public INVALID_ACTIVITY(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public INVALID_ACTIVITY(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
