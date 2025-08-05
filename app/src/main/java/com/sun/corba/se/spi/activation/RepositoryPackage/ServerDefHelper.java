package com.sun.corba.se.spi.activation.RepositoryPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/RepositoryPackage/ServerDefHelper.class */
public abstract class ServerDefHelper {
    private static String _id = "IDL:activation/Repository/ServerDef:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ServerDef serverDef) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, serverDef);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ServerDef extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "ServerDef", new StructMember[]{new StructMember("applicationName", ORB.init().create_string_tc(0), null), new StructMember("serverName", ORB.init().create_string_tc(0), null), new StructMember("serverClassPath", ORB.init().create_string_tc(0), null), new StructMember("serverArgs", ORB.init().create_string_tc(0), null), new StructMember("serverVmArgs", ORB.init().create_string_tc(0), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ServerDef read(InputStream inputStream) {
        ServerDef serverDef = new ServerDef();
        serverDef.applicationName = inputStream.read_string();
        serverDef.serverName = inputStream.read_string();
        serverDef.serverClassPath = inputStream.read_string();
        serverDef.serverArgs = inputStream.read_string();
        serverDef.serverVmArgs = inputStream.read_string();
        return serverDef;
    }

    public static void write(OutputStream outputStream, ServerDef serverDef) {
        outputStream.write_string(serverDef.applicationName);
        outputStream.write_string(serverDef.serverName);
        outputStream.write_string(serverDef.serverClassPath);
        outputStream.write_string(serverDef.serverArgs);
        outputStream.write_string(serverDef.serverVmArgs);
    }
}
