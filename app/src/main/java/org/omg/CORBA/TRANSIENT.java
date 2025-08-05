package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/TRANSIENT.class */
public final class TRANSIENT extends SystemException {
    public TRANSIENT() {
        this("");
    }

    public TRANSIENT(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public TRANSIENT(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public TRANSIENT(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
