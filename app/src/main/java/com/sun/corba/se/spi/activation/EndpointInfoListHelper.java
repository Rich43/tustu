package com.sun.corba.se.spi.activation;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/EndpointInfoListHelper.class */
public abstract class EndpointInfoListHelper {
    private static String _id = "IDL:activation/EndpointInfoList:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, EndPointInfo[] endPointInfoArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, endPointInfoArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static EndPointInfo[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = EndPointInfoHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "EndpointInfoList", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static EndPointInfo[] read(InputStream inputStream) {
        EndPointInfo[] endPointInfoArr = new EndPointInfo[inputStream.read_long()];
        for (int i2 = 0; i2 < endPointInfoArr.length; i2++) {
            endPointInfoArr[i2] = EndPointInfoHelper.read(inputStream);
        }
        return endPointInfoArr;
    }

    public static void write(OutputStream outputStream, EndPointInfo[] endPointInfoArr) {
        outputStream.write_long(endPointInfoArr.length);
        for (EndPointInfo endPointInfo : endPointInfoArr) {
            EndPointInfoHelper.write(outputStream, endPointInfo);
        }
    }
}
