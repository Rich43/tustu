package com.sun.corba.se.spi.activation;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ORBAlreadyRegisteredHelper.class */
public abstract class ORBAlreadyRegisteredHelper {
    private static String _id = "IDL:activation/ORBAlreadyRegistered:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ORBAlreadyRegistered oRBAlreadyRegistered) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, oRBAlreadyRegistered);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ORBAlreadyRegistered extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            synchronized (TypeCode.class) {
                if (__typeCode == null) {
                    if (__active) {
                        return ORB.init().create_recursive_tc(_id);
                    }
                    __active = true;
                    __typeCode = ORB.init().create_exception_tc(id(), "ORBAlreadyRegistered", new StructMember[]{new StructMember("orbId", ORB.init().create_alias_tc(ORBidHelper.id(), "ORBid", ORB.init().create_string_tc(0)), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ORBAlreadyRegistered read(InputStream inputStream) {
        ORBAlreadyRegistered oRBAlreadyRegistered = new ORBAlreadyRegistered();
        inputStream.read_string();
        oRBAlreadyRegistered.orbId = inputStream.read_string();
        return oRBAlreadyRegistered;
    }

    public static void write(OutputStream outputStream, ORBAlreadyRegistered oRBAlreadyRegistered) {
        outputStream.write_string(id());
        outputStream.write_string(oRBAlreadyRegistered.orbId);
    }
}
