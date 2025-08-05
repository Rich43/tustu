package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/INTERNAL.class */
public final class INTERNAL extends SystemException {
    public INTERNAL() {
        this("");
    }

    public INTERNAL(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public INTERNAL(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public INTERNAL(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
