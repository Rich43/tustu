package org.omg.IOP;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/IOP/ComponentIdHelper.class */
public abstract class ComponentIdHelper {
    private static String _id = "IDL:omg.org/IOP/ComponentId:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, int i2) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, i2);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static int extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_ulong);
            __typeCode = ORB.init().create_alias_tc(id(), "ComponentId", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static int read(InputStream inputStream) {
        return inputStream.read_ulong();
    }

    public static void write(OutputStream outputStream, int i2) {
        outputStream.write_ulong(i2);
    }
}
