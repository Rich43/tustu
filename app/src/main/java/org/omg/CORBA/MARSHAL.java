package org.omg.CORBA;

/* loaded from: rt.jar:org/omg/CORBA/MARSHAL.class */
public final class MARSHAL extends SystemException {
    public MARSHAL() {
        this("");
    }

    public MARSHAL(String str) {
        this(str, 0, CompletionStatus.COMPLETED_NO);
    }

    public MARSHAL(int i2, CompletionStatus completionStatus) {
        this("", i2, completionStatus);
    }

    public MARSHAL(String str, int i2, CompletionStatus completionStatus) {
        super(str, i2, completionStatus);
    }
}
