package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/INITIALIZE.class */
public final class INITIALIZE extends SystemException {
    public INITIALIZE() {
        this("");
    }

    public INITIALIZE(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public INITIALIZE(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public INITIALIZE(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
