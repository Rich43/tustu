package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ParDescriptionSeqHelper.class */
public final class ParDescriptionSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/ParDescriptionSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, ParameterDescription[] parameterDescriptionArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, parameterDescriptionArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ParameterDescription[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ParameterDescriptionHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "ParDescriptionSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ParameterDescription[] read(InputStream inputStream) {
        ParameterDescription[] parameterDescriptionArr = new ParameterDescription[inputStream.read_long()];
        for (int i2 = 0; i2 < parameterDescriptionArr.length; i2++) {
            parameterDescriptionArr[i2] = ParameterDescriptionHelper.read(inputStream);
        }
        return parameterDescriptionArr;
    }

    public static void write(OutputStream outputStream, ParameterDescription[] parameterDescriptionArr) {
        outputStream.write_long(parameterDescriptionArr.length);
        for (ParameterDescription parameterDescription : parameterDescriptionArr) {
            ParameterDescriptionHelper.write(outputStream, parameterDescription);
        }
    }
}
