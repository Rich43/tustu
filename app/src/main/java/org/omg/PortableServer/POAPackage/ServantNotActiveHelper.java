package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/ServantNotActiveHelper.class */
public abstract class ServantNotActiveHelper {
    private static String _id = "IDL:omg.org/PortableServer/POA/ServantNotActive:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ServantNotActive servantNotActive) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, servantNotActive);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ServantNotActive extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "ServantNotActive", new StructMember[0]);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ServantNotActive read(InputStream inputStream) {
        ServantNotActive servantNotActive = new ServantNotActive();
        inputStream.read_string();
        return servantNotActive;
    }

    public static void write(OutputStream outputStream, ServantNotActive servantNotActive) {
        outputStream.write_string(id());
    }
}
