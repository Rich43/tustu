package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/OctetSeqHelper.class */
public abstract class OctetSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/OctetSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, byte[] bArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, bArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static byte[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_octet);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "OctetSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static byte[] read(InputStream inputStream) {
        int i2 = inputStream.read_long();
        byte[] bArr = new byte[i2];
        inputStream.read_octet_array(bArr, 0, i2);
        return bArr;
    }

    public static void write(OutputStream outputStream, byte[] bArr) {
        outputStream.write_long(bArr.length);
        outputStream.write_octet_array(bArr, 0, bArr.length);
    }
}
