package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/UShortSeqHelper.class */
public abstract class UShortSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/UShortSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, short[] sArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, sArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static short[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_ushort);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "UShortSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static short[] read(InputStream inputStream) {
        int i2 = inputStream.read_long();
        short[] sArr = new short[i2];
        inputStream.read_ushort_array(sArr, 0, i2);
        return sArr;
    }

    public static void write(OutputStream outputStream, short[] sArr) {
        outputStream.write_long(sArr.length);
        outputStream.write_ushort_array(sArr, 0, sArr.length);
    }
}
