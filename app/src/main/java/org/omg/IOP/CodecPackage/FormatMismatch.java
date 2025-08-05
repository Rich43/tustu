package org.omg.IOP.CodecPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/IOP/CodecPackage/FormatMismatch.class */
public final class FormatMismatch extends UserException {
    public FormatMismatch() {
        super(FormatMismatchHelper.id());
    }

    public FormatMismatch(String str) {
        super(FormatMismatchHelper.id() + Constants.INDENT + str);
    }
}
