package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/ACTIVITY_REQUIRED.class */
public final class ACTIVITY_REQUIRED extends SystemException {
    public ACTIVITY_REQUIRED() {
        this("");
    }

    public ACTIVITY_REQUIRED(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public ACTIVITY_REQUIRED(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public ACTIVITY_REQUIRED(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
