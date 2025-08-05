package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/LongLongSeqHelper.class */
public abstract class LongLongSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/LongLongSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, long[] jArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, jArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static long[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_longlong);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "LongLongSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static long[] read(InputStream inputStream) {
        int i2 = inputStream.read_long();
        long[] jArr = new long[i2];
        inputStream.read_longlong_array(jArr, 0, i2);
        return jArr;
    }

    public static void write(OutputStream outputStream, long[] jArr) {
        outputStream.write_long(jArr.length);
        outputStream.write_longlong_array(jArr, 0, jArr.length);
    }
}
