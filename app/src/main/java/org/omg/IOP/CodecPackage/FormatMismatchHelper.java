package org.omg.IOP.CodecPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/IOP/CodecPackage/FormatMismatchHelper.class */
public abstract class FormatMismatchHelper {
    private static String _id = "IDL:omg.org/IOP/Codec/FormatMismatch:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, FormatMismatch formatMismatch) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, formatMismatch);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static FormatMismatch extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "FormatMismatch", new StructMember[0]);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static FormatMismatch read(InputStream inputStream) {
        FormatMismatch formatMismatch = new FormatMismatch();
        inputStream.read_string();
        return formatMismatch;
    }

    public static void write(OutputStream outputStream, FormatMismatch formatMismatch) {
        outputStream.write_string(id());
    }
}
