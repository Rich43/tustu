package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/NotFoundReasonHelper.class */
public abstract class NotFoundReasonHelper {
    private static String _id = "IDL:omg.org/CosNaming/NamingContext/NotFoundReason:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, NotFoundReason notFoundReason) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, notFoundReason);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NotFoundReason extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_enum_tc(id(), "NotFoundReason", new String[]{"missing_node", "not_context", "not_object"});
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NotFoundReason read(InputStream inputStream) {
        return NotFoundReason.from_int(inputStream.read_long());
    }

    public static void write(OutputStream outputStream, NotFoundReason notFoundReason) {
        outputStream.write_long(notFoundReason.value());
    }
}
