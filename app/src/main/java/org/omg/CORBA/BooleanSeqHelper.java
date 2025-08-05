package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/BooleanSeqHelper.class */
public abstract class BooleanSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/BooleanSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, boolean[] zArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, zArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static boolean[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_boolean);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "BooleanSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static boolean[] read(InputStream inputStream) {
        int i2 = inputStream.read_long();
        boolean[] zArr = new boolean[i2];
        inputStream.read_boolean_array(zArr, 0, i2);
        return zArr;
    }

    public static void write(OutputStream outputStream, boolean[] zArr) {
        outputStream.write_long(zArr.length);
        outputStream.write_boolean_array(zArr, 0, zArr.length);
    }
}
