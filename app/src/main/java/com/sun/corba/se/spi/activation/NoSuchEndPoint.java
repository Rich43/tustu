package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/NoSuchEndPoint.class */
public final class NoSuchEndPoint extends UserException {
    public NoSuchEndPoint() {
        super(NoSuchEndPointHelper.id());
    }

    public NoSuchEndPoint(String str) {
        super(NoSuchEndPointHelper.id() + Constants.INDENT + str);
    }
}
