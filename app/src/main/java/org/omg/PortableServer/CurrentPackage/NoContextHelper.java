package org.omg.PortableServer.CurrentPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableServer/CurrentPackage/NoContextHelper.class */
public abstract class NoContextHelper {
    private static String _id = "IDL:omg.org/PortableServer/Current/NoContext:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, NoContext noContext) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, noContext);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NoContext extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "NoContext", new StructMember[0]);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NoContext read(InputStream inputStream) {
        NoContext noContext = new NoContext();
        inputStream.read_string();
        return noContext;
    }

    public static void write(OutputStream outputStream, NoContext noContext) {
        outputStream.write_string(id());
    }
}
