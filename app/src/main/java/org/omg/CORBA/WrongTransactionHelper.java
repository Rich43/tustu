package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/WrongTransactionHelper.class */
public abstract class WrongTransactionHelper {
    private static String _id = "IDL:omg.org/CORBA/WrongTransaction:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, WrongTransaction wrongTransaction) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, wrongTransaction);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static WrongTransaction extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "WrongTransaction", new StructMember[0]);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static WrongTransaction read(InputStream inputStream) {
        WrongTransaction wrongTransaction = new WrongTransaction();
        inputStream.read_string();
        return wrongTransaction;
    }

    public static void write(OutputStream outputStream, WrongTransaction wrongTransaction) {
        outputStream.write_string(id());
    }
}
