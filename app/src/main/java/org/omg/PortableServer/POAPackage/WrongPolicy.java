package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/WrongPolicy.class */
public final class WrongPolicy extends UserException {
    public WrongPolicy() {
        super(WrongPolicyHelper.id());
    }

    public WrongPolicy(String str) {
        super(WrongPolicyHelper.id() + Constants.INDENT + str);
    }
}
