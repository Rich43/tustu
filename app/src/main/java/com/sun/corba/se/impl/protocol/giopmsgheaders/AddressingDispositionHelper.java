package com.sun.corba.se.impl.protocol.giopmsgheaders;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/AddressingDispositionHelper.class */
public abstract class AddressingDispositionHelper {
    private static String _id = "IDL:messages/AddressingDisposition:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, short s2) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, s2);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static short extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_short);
            __typeCode = ORB.init().create_alias_tc(id(), "AddressingDisposition", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static short read(InputStream inputStream) {
        return inputStream.read_short();
    }

    public static void write(OutputStream outputStream, short s2) {
        outputStream.write_short(s2);
    }
}
