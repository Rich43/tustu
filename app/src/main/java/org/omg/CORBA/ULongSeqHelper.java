package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/ULongSeqHelper.class */
public abstract class ULongSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/ULongSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, int[] iArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, iArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static int[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_ulong);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "ULongSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static int[] read(InputStream inputStream) {
        int i2 = inputStream.read_long();
        int[] iArr = new int[i2];
        inputStream.read_ulong_array(iArr, 0, i2);
        return iArr;
    }

    public static void write(OutputStream outputStream, int[] iArr) {
        outputStream.write_long(iArr.length);
        outputStream.write_ulong_array(iArr, 0, iArr.length);
    }
}
