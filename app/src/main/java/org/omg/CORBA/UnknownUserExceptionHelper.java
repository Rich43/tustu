package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/UnknownUserExceptionHelper.class */
public abstract class UnknownUserExceptionHelper {
    private static String _id = "IDL:omg.org/CORBA/UnknownUserException:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, UnknownUserException unknownUserException) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, unknownUserException);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static UnknownUserException extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "UnknownUserException", new StructMember[]{new StructMember("except", ORB.init().get_primitive_tc(TCKind.tk_any), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static UnknownUserException read(InputStream inputStream) {
        UnknownUserException unknownUserException = new UnknownUserException();
        inputStream.read_string();
        unknownUserException.except = inputStream.read_any();
        return unknownUserException;
    }

    public static void write(OutputStream outputStream, UnknownUserException unknownUserException) {
        outputStream.write_string(id());
        outputStream.write_any(unknownUserException.except);
    }
}
