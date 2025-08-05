package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/NO_RESOURCES.class */
public final class NO_RESOURCES extends SystemException {
    public NO_RESOURCES() {
        this("");
    }

    public NO_RESOURCES(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public NO_RESOURCES(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public NO_RESOURCES(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
