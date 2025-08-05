package org.omg.PortableServer;

import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableServer/POAHelper.class */
public abstract class POAHelper {
    private static String _id = "IDL:omg.org/PortableServer/POA:2.3";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, POA poa) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, poa);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static POA extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), "POA");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static POA read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, POA poa) {
        throw new MARSHAL();
    }

    public static POA narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof POA) {
            return (POA) object;
        }
        if (!object._is_a(id())) {
            throw new BAD_PARAM();
        }
        return null;
    }
}
