package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/CurrentHelper.class */
public abstract class CurrentHelper {
    private static String _id = "IDL:omg.org/CORBA/Current:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Current current) {
        throw new MARSHAL();
    }

    public static Current extract(Any any) {
        throw new MARSHAL();
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "Current");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Current read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, Current current) {
        throw new MARSHAL();
    }

    public static Current narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof Current) {
            return (Current) object;
        }
        throw new BAD_PARAM();
    }
}
