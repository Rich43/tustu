package org.omg.IOP.CodecPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/IOP/CodecPackage/InvalidTypeForEncoding.class */
public final class InvalidTypeForEncoding extends UserException {
    public InvalidTypeForEncoding() {
        super(InvalidTypeForEncodingHelper.id());
    }

    public InvalidTypeForEncoding(String str) {
        super(InvalidTypeForEncodingHelper.id() + Constants.INDENT + str);
    }
}
