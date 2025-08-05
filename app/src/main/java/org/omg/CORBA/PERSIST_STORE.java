package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/PERSIST_STORE.class */
public final class PERSIST_STORE extends SystemException {
    public PERSIST_STORE() {
        this("");
    }

    public PERSIST_STORE(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public PERSIST_STORE(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public PERSIST_STORE(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
