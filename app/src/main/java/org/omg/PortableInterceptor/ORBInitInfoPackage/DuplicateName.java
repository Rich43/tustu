package org.omg.PortableInterceptor.ORBInitInfoPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ORBInitInfoPackage/DuplicateName.class */
public final class DuplicateName extends UserException {
    public String name;

    public DuplicateName() {
        super(DuplicateNameHelper.id());
        this.name = null;
    }

    public DuplicateName(String str) {
        super(DuplicateNameHelper.id());
        this.name = null;
        this.name = str;
    }

    public DuplicateName(String str, String str2) {
        super(DuplicateNameHelper.id() + Constants.INDENT + str);
        this.name = null;
        this.name = str2;
    }
}
