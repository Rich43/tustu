package com.sun.corba.se.spi.activation;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerIdsHelper.class */
public abstract class ServerIdsHelper {
    private static String _id = "IDL:activation/ServerIds:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, int[] iArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, iArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static int[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().get_primitive_tc(TCKind.tk_long);
            __typeCode = ORB.init().create_alias_tc(ServerIdHelper.id(), "ServerId", __typeCode);
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "ServerIds", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static int[] read(InputStream inputStream) {
        int[] iArr = new int[inputStream.read_long()];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = ServerIdHelper.read(inputStream);
        }
        return iArr;
    }

    public static void write(OutputStream outputStream, int[] iArr) {
        outputStream.write_long(iArr.length);
        for (int i2 : iArr) {
            ServerIdHelper.write(outputStream, i2);
        }
    }
}
