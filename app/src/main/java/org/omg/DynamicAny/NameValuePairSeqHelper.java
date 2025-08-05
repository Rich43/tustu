package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/DynamicAny/NameValuePairSeqHelper.class */
public abstract class NameValuePairSeqHelper {
    private static String _id = "IDL:omg.org/DynamicAny/NameValuePairSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, NameValuePair[] nameValuePairArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, nameValuePairArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NameValuePair[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = NameValuePairHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "NameValuePairSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NameValuePair[] read(InputStream inputStream) {
        NameValuePair[] nameValuePairArr = new NameValuePair[inputStream.read_long()];
        for (int i2 = 0; i2 < nameValuePairArr.length; i2++) {
            nameValuePairArr[i2] = NameValuePairHelper.read(inputStream);
        }
        return nameValuePairArr;
    }

    public static void write(OutputStream outputStream, NameValuePair[] nameValuePairArr) {
        outputStream.write_long(nameValuePairArr.length);
        for (NameValuePair nameValuePair : nameValuePairArr) {
            NameValuePairHelper.write(outputStream, nameValuePair);
        }
    }
}
