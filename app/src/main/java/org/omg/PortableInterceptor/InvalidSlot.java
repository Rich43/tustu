package org.omg.PortableInterceptor;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableInterceptor/InvalidSlot.class */
public final class InvalidSlot extends UserException {
    public InvalidSlot() {
        super(InvalidSlotHelper.id());
    }

    public InvalidSlot(String str) {
        super(InvalidSlotHelper.id() + Constants.INDENT + str);
    }
}
