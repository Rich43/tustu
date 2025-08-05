package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableInterceptor/InvalidSlotHelper.class */
public abstract class InvalidSlotHelper {
    private static String _id = "IDL:omg.org/PortableInterceptor/InvalidSlot:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, InvalidSlot invalidSlot) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, invalidSlot);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static InvalidSlot extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "InvalidSlot", new StructMember[0]);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static InvalidSlot read(InputStream inputStream) {
        InvalidSlot invalidSlot = new InvalidSlot();
        inputStream.read_string();
        return invalidSlot;
    }

    public static void write(OutputStream outputStream, InvalidSlot invalidSlot) {
        outputStream.write_string(id());
    }
}
