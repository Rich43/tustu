package com.sun.corba.se.spi.activation.LocatorPackage;

import com.sun.corba.se.spi.activation.ORBPortInfoHelper;
import com.sun.corba.se.spi.activation.ORBPortInfoListHelper;
import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/LocatorPackage/ServerLocationHelper.class */
public abstract class ServerLocationHelper {
    private static String _id = "IDL:activation/Locator/ServerLocation:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ServerLocation serverLocation) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, serverLocation);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ServerLocation extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "ServerLocation", new StructMember[]{new StructMember("hostname", ORB.init().create_string_tc(0), null), new StructMember("ports", ORB.init().create_alias_tc(ORBPortInfoListHelper.id(), "ORBPortInfoList", ORB.init().create_sequence_tc(0, ORBPortInfoHelper.type())), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ServerLocation read(InputStream inputStream) {
        ServerLocation serverLocation = new ServerLocation();
        serverLocation.hostname = inputStream.read_string();
        serverLocation.ports = ORBPortInfoListHelper.read(inputStream);
        return serverLocation;
    }

    public static void write(OutputStream outputStream, ServerLocation serverLocation) {
        outputStream.write_string(serverLocation.hostname);
        ORBPortInfoListHelper.write(outputStream, serverLocation.ports);
    }
}
