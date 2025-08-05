package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/AnySeqHelper.class */
public abstract class AnySeqHelper {
    private static String _id = "IDL:omg.org/CORBA/AnySeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Any[] anyArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, anyArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Any[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_any);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "AnySeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Any[] read(InputStream inputStream) {
        Any[] anyArr = new Any[inputStream.read_long()];
        for (int i2 = 0; i2 < anyArr.length; i2++) {
            anyArr[i2] = inputStream.read_any();
        }
        return anyArr;
    }

    public static void write(OutputStream outputStream, Any[] anyArr) {
        outputStream.write_long(anyArr.length);
        for (Any any : anyArr) {
            outputStream.write_any(any);
        }
    }
}
