package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/NO_IMPLEMENT.class */
public final class NO_IMPLEMENT extends SystemException {
    public NO_IMPLEMENT() {
        this("");
    }

    public NO_IMPLEMENT(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public NO_IMPLEMENT(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public NO_IMPLEMENT(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
