package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/RepositoryHelper.class */
public final class RepositoryHelper {
    private static String _id = "IDL:com.sun.omg.org/CORBA/Repository:3.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Repository repository) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, repository);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Repository extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_string_tc(0);
            __typeCode = ORB.init().create_alias_tc(id(), "Repository", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Repository read(InputStream inputStream) {
        inputStream.read_string();
        return null;
    }

    public static void write(OutputStream outputStream, Repository repository) {
        outputStream.write_string(null);
    }
}
