package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ExceptionDescriptionHelper.class */
public final class ExceptionDescriptionHelper {
    private static String _id = "IDL:omg.org/CORBA/ExceptionDescription:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ExceptionDescription exceptionDescription) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, exceptionDescription);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ExceptionDescription extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "ExceptionDescription", new StructMember[]{new StructMember("name", ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", ORB.init().create_string_tc(0)), null), new StructMember("id", ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)), null), new StructMember("defined_in", ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)), null), new StructMember("version", ORB.init().create_alias_tc(VersionSpecHelper.id(), "VersionSpec", ORB.init().create_string_tc(0)), null), new StructMember("type", ORB.init().get_primitive_tc(TCKind.tk_TypeCode), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ExceptionDescription read(InputStream inputStream) {
        ExceptionDescription exceptionDescription = new ExceptionDescription();
        exceptionDescription.name = inputStream.read_string();
        exceptionDescription.id = inputStream.read_string();
        exceptionDescription.defined_in = inputStream.read_string();
        exceptionDescription.version = inputStream.read_string();
        exceptionDescription.type = inputStream.read_TypeCode();
        return exceptionDescription;
    }

    public static void write(OutputStream outputStream, ExceptionDescription exceptionDescription) {
        outputStream.write_string(exceptionDescription.name);
        outputStream.write_string(exceptionDescription.id);
        outputStream.write_string(exceptionDescription.defined_in);
        outputStream.write_string(exceptionDescription.version);
        outputStream.write_TypeCode(exceptionDescription.type);
    }
}
