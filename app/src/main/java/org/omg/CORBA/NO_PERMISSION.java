package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/NO_PERMISSION.class */
public final class NO_PERMISSION extends SystemException {
    public NO_PERMISSION() {
        this("");
    }

    public NO_PERMISSION(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public NO_PERMISSION(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public NO_PERMISSION(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
