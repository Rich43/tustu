package com.sun.corba.se.spi.activation.InitialNameServicePackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/InitialNameServicePackage/NameAlreadyBound.class */
public final class NameAlreadyBound extends UserException {
    public NameAlreadyBound() {
        super(NameAlreadyBoundHelper.id());
    }

    public NameAlreadyBound(String str) {
        super(NameAlreadyBoundHelper.id() + Constants.INDENT + str);
    }
}
