package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ORBAlreadyRegistered.class */
public final class ORBAlreadyRegistered extends UserException {
    public String orbId;

    public ORBAlreadyRegistered() {
        super(ORBAlreadyRegisteredHelper.id());
        this.orbId = null;
    }

    public ORBAlreadyRegistered(String str) {
        super(ORBAlreadyRegisteredHelper.id());
        this.orbId = null;
        this.orbId = str;
    }

    public ORBAlreadyRegistered(String str, String str2) {
        super(ORBAlreadyRegisteredHelper.id() + Constants.INDENT + str);
        this.orbId = null;
        this.orbId = str2;
    }
}
