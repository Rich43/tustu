package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/NO_MEMORY.class */
public final class NO_MEMORY extends SystemException {
    public NO_MEMORY() {
        this("");
    }

    public NO_MEMORY(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public NO_MEMORY(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public NO_MEMORY(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
