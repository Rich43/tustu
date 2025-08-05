package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/FloatSeqHelper.class */
public abstract class FloatSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/FloatSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, float[] fArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, fArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static float[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_float);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "FloatSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static float[] read(InputStream inputStream) {
        int i2 = inputStream.read_long();
        float[] fArr = new float[i2];
        inputStream.read_float_array(fArr, 0, i2);
        return fArr;
    }

    public static void write(OutputStream outputStream, float[] fArr) {
        outputStream.write_long(fArr.length);
        outputStream.write_float_array(fArr, 0, fArr.length);
    }
}
