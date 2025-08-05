package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/OBJ_ADAPTER.class */
public final class OBJ_ADAPTER extends SystemException {
    public OBJ_ADAPTER() {
        this("");
    }

    public OBJ_ADAPTER(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public OBJ_ADAPTER(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public OBJ_ADAPTER(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
