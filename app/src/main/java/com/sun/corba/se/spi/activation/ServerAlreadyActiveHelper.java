package com.sun.corba.se.spi.activation;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ServerAlreadyActiveHelper.class */
public abstract class ServerAlreadyActiveHelper {
    private static String _id = "IDL:activation/ServerAlreadyActive:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ServerAlreadyActive serverAlreadyActive) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, serverAlreadyActive);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ServerAlreadyActive extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "ServerAlreadyActive", new StructMember[]{new StructMember("serverId", ORB.init().create_alias_tc(ServerIdHelper.id(), "ServerId", ORB.init().get_primitive_tc(TCKind.tk_long)), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ServerAlreadyActive read(InputStream inputStream) {
        ServerAlreadyActive serverAlreadyActive = new ServerAlreadyActive();
        inputStream.read_string();
        serverAlreadyActive.serverId = inputStream.read_long();
        return serverAlreadyActive;
    }

    public static void write(OutputStream outputStream, ServerAlreadyActive serverAlreadyActive) {
        outputStream.write_string(id());
        outputStream.write_long(serverAlreadyActive.serverId);
    }
}
