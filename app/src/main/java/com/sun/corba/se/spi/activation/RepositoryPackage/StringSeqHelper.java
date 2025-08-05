package com.sun.corba.se.spi.activation.RepositoryPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/RepositoryPackage/StringSeqHelper.class */
public abstract class StringSeqHelper {
    private static String _id = "IDL:activation/Repository/StringSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, String[] strArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, strArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static String[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_string_tc(0);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "StringSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static String[] read(InputStream inputStream) {
        String[] strArr = new String[inputStream.read_long()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = inputStream.read_string();
        }
        return strArr;
    }

    public static void write(OutputStream outputStream, String[] strArr) {
        outputStream.write_long(strArr.length);
        for (String str : strArr) {
            outputStream.write_string(str);
        }
    }
}
