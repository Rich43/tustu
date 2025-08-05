package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/REBIND.class */
public final class REBIND extends SystemException {
    public REBIND() {
        this("");
    }

    public REBIND(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public REBIND(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public REBIND(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
