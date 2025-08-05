package com.sun.corba.se.spi.activation;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerNotActiveHelper.class */
public abstract class ServerNotActiveHelper {
    private static String _id = "IDL:activation/ServerNotActive:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ServerNotActive serverNotActive) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, serverNotActive);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ServerNotActive extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "ServerNotActive", new StructMember[]{new StructMember("serverId", ORB.init().create_alias_tc(ServerIdHelper.id(), "ServerId", ORB.init().get_primitive_tc(TCKind.tk_long)), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ServerNotActive read(InputStream inputStream) {
        ServerNotActive serverNotActive = new ServerNotActive();
        inputStream.read_string();
        serverNotActive.serverId = inputStream.read_long();
        return serverNotActive;
    }

    public static void write(OutputStream outputStream, ServerNotActive serverNotActive) {
        outputStream.write_string(id());
        outputStream.write_long(serverNotActive.serverId);
    }
}
