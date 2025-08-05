package com.sun.corba.se.spi.activation;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ORBPortInfoListHelper.class */
public abstract class ORBPortInfoListHelper {
    private static String _id = "IDL:activation/ORBPortInfoList:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, ORBPortInfo[] oRBPortInfoArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, oRBPortInfoArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ORBPortInfo[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORBPortInfoHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "ORBPortInfoList", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ORBPortInfo[] read(InputStream inputStream) {
        ORBPortInfo[] oRBPortInfoArr = new ORBPortInfo[inputStream.read_long()];
        for (int i2 = 0; i2 < oRBPortInfoArr.length; i2++) {
            oRBPortInfoArr[i2] = ORBPortInfoHelper.read(inputStream);
        }
        return oRBPortInfoArr;
    }

    public static void write(OutputStream outputStream, ORBPortInfo[] oRBPortInfoArr) {
        outputStream.write_long(oRBPortInfoArr.length);
        for (ORBPortInfo oRBPortInfo : oRBPortInfoArr) {
            ORBPortInfoHelper.write(outputStream, oRBPortInfo);
        }
    }
}
