package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/StructMemberHelper.class */
public final class StructMemberHelper {
    private static String _id = "IDL:omg.org/CORBA/StructMember:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, StructMember structMember) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, structMember);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static StructMember extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "StructMember", new StructMember[]{new StructMember("name", ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", ORB.init().create_string_tc(0)), null), new StructMember("type", ORB.init().get_primitive_tc(TCKind.tk_TypeCode), null), new StructMember("type_def", IDLTypeHelper.type(), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static StructMember read(InputStream inputStream) {
        StructMember structMember = new StructMember();
        structMember.name = inputStream.read_string();
        structMember.type = inputStream.read_TypeCode();
        structMember.type_def = IDLTypeHelper.read(inputStream);
        return structMember;
    }

    public static void write(OutputStream outputStream, StructMember structMember) {
        outputStream.write_string(structMember.name);
        outputStream.write_TypeCode(structMember.type);
        IDLTypeHelper.write(outputStream, structMember.type_def);
    }
}
