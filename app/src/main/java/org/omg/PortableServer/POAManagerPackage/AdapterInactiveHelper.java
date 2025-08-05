package org.omg.PortableServer.POAManagerPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableServer/POAManagerPackage/AdapterInactiveHelper.class */
public abstract class AdapterInactiveHelper {
    private static String _id = "IDL:omg.org/PortableServer/POAManager/AdapterInactive:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, AdapterInactive adapterInactive) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, adapterInactive);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static AdapterInactive extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "AdapterInactive", new StructMember[0]);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static AdapterInactive read(InputStream inputStream) {
        AdapterInactive adapterInactive = new AdapterInactive();
        inputStream.read_string();
        return adapterInactive;
    }

    public static void write(OutputStream outputStream, AdapterInactive adapterInactive) {
        outputStream.write_string(id());
    }
}
