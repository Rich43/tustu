package org.omg.IOP.CodecFactoryPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/IOP/CodecFactoryPackage/UnknownEncoding.class */
public final class UnknownEncoding extends UserException {
    public UnknownEncoding() {
        super(UnknownEncodingHelper.id());
    }

    public UnknownEncoding(String str) {
        super(UnknownEncodingHelper.id() + Constants.INDENT + str);
    }
}
