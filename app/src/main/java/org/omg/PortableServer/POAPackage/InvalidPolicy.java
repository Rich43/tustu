package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/InvalidPolicy.class */
public final class InvalidPolicy extends UserException {
    public short index;

    public InvalidPolicy() {
        super(InvalidPolicyHelper.id());
        this.index = (short) 0;
    }

    public InvalidPolicy(short s2) {
        super(InvalidPolicyHelper.id());
        this.index = (short) 0;
        this.index = s2;
    }

    public InvalidPolicy(String str, short s2) {
        super(InvalidPolicyHelper.id() + Constants.INDENT + str);
        this.index = (short) 0;
        this.index = s2;
    }
}
