package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/NO_RESPONSE.class */
public final class NO_RESPONSE extends SystemException {
    public NO_RESPONSE() {
        this("");
    }

    public NO_RESPONSE(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public NO_RESPONSE(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public NO_RESPONSE(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
