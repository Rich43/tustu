package org.omg.CORBA.portable;

import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:org/omg/CORBA/portable/IndirectionException.class */
public class IndirectionException extends SystemException {
    public int offset;

    public IndirectionException(int i2) {
        super("", 0, CompletionStatus.COMPLETED_MAYBE);
        this.offset = i2;
    }
}
