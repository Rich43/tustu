package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ObjectHelper;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ForwardRequestHelper.class */
public abstract class ForwardRequestHelper {
    private static String _id = "IDL:omg.org/PortableInterceptor/ForwardRequest:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ForwardRequest forwardRequest) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, forwardRequest);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ForwardRequest extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "ForwardRequest", new StructMember[]{new StructMember("forward", ObjectHelper.type(), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ForwardRequest read(InputStream inputStream) {
        ForwardRequest forwardRequest = new ForwardRequest();
        inputStream.read_string();
        forwardRequest.forward = ObjectHelper.read(inputStream);
        return forwardRequest;
    }

    public static void write(OutputStream outputStream, ForwardRequest forwardRequest) {
        outputStream.write_string(id());
        ObjectHelper.write(outputStream, forwardRequest.forward);
    }
}
