package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/BAD_QOS.class */
public final class BAD_QOS extends SystemException {
    public BAD_QOS() {
        this("");
    }

    public BAD_QOS(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public BAD_QOS(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public BAD_QOS(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
