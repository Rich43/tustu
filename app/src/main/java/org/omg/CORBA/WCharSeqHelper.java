package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/WCharSeqHelper.class */
public abstract class WCharSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/WCharSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, char[] cArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, cArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static char[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_wchar);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "WCharSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static char[] read(InputStream inputStream) {
        int i2 = inputStream.read_long();
        char[] cArr = new char[i2];
        inputStream.read_wchar_array(cArr, 0, i2);
        return cArr;
    }

    public static void write(OutputStream outputStream, char[] cArr) {
        outputStream.write_long(cArr.length);
        outputStream.write_wchar_array(cArr, 0, cArr.length);
    }
}
