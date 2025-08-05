package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/InvalidORBid.class */
public final class InvalidORBid extends UserException {
    public InvalidORBid() {
        super(InvalidORBidHelper.id());
    }

    public InvalidORBid(String str) {
        super(InvalidORBidHelper.id() + Constants.INDENT + str);
    }
}
