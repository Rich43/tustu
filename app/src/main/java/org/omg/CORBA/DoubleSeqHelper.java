package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/DoubleSeqHelper.class */
public abstract class DoubleSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/DoubleSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, double[] dArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, dArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static double[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_double);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "DoubleSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static double[] read(InputStream inputStream) {
        int i2 = inputStream.read_long();
        double[] dArr = new double[i2];
        inputStream.read_double_array(dArr, 0, i2);
        return dArr;
    }

    public static void write(OutputStream outputStream, double[] dArr) {
        outputStream.write_long(dArr.length);
        outputStream.write_double_array(dArr, 0, dArr.length);
    }
}
