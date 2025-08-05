package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ExcDescriptionSeqHelper.class */
public final class ExcDescriptionSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/ExcDescriptionSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, ExceptionDescription[] exceptionDescriptionArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, exceptionDescriptionArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ExceptionDescription[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ExceptionDescriptionHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "ExcDescriptionSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ExceptionDescription[] read(InputStream inputStream) {
        ExceptionDescription[] exceptionDescriptionArr = new ExceptionDescription[inputStream.read_long()];
        for (int i2 = 0; i2 < exceptionDescriptionArr.length; i2++) {
            exceptionDescriptionArr[i2] = ExceptionDescriptionHelper.read(inputStream);
        }
        return exceptionDescriptionArr;
    }

    public static void write(OutputStream outputStream, ExceptionDescription[] exceptionDescriptionArr) {
        outputStream.write_long(exceptionDescriptionArr.length);
        for (ExceptionDescription exceptionDescription : exceptionDescriptionArr) {
            ExceptionDescriptionHelper.write(outputStream, exceptionDescription);
        }
    }
}
