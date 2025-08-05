package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/UnionMemberHelper.class */
public abstract class UnionMemberHelper {
    private static String _id = "IDL:omg.org/CORBA/UnionMember:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, UnionMember unionMember) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, unionMember);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static UnionMember extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "UnionMember", new StructMember[]{new StructMember("name", ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", ORB.init().create_string_tc(0)), null), new StructMember("label", ORB.init().get_primitive_tc(TCKind.tk_any), null), new StructMember("type", ORB.init().get_primitive_tc(TCKind.tk_TypeCode), null), new StructMember("type_def", IDLTypeHelper.type(), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static UnionMember read(InputStream inputStream) {
        UnionMember unionMember = new UnionMember();
        unionMember.name = inputStream.read_string();
        unionMember.label = inputStream.read_any();
        unionMember.type = inputStream.read_TypeCode();
        unionMember.type_def = IDLTypeHelper.read(inputStream);
        return unionMember;
    }

    public static void write(OutputStream outputStream, UnionMember unionMember) {
        outputStream.write_string(unionMember.name);
        outputStream.write_any(unionMember.label);
        outputStream.write_TypeCode(unionMember.type);
        IDLTypeHelper.write(outputStream, unionMember.type_def);
    }
}
