package org.omg.CosNaming.NamingContextExtPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextExtPackage/InvalidAddress.class */
public final class InvalidAddress extends UserException {
    public InvalidAddress() {
        super(InvalidAddressHelper.id());
    }

    public InvalidAddress(String str) {
        super(InvalidAddressHelper.id() + Constants.INDENT + str);
    }
}
