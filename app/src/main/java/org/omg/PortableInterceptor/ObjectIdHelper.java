package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.OctetSeqHelper;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ObjectIdHelper.class */
public abstract class ObjectIdHelper {
    private static String _id = "IDL:omg.org/PortableInterceptor/ObjectId:1.0";
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
            __typeCode = ORB.init().create_alias_tc(OctetSeqHelper.id(), "OctetSeq", __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "ObjectId", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static byte[] read(InputStream inputStream) {
        return OctetSeqHelper.read(inputStream);
    }

    public static void write(OutputStream outputStream, byte[] bArr) {
        OctetSeqHelper.write(outputStream, bArr);
    }
}
