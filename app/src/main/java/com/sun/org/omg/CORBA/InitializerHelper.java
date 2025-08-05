package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/InitializerHelper.class */
public final class InitializerHelper {
    private static String _id = "IDL:omg.org/CORBA/Initializer:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, Initializer initializer) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, initializer);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Initializer extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "Initializer", new StructMember[]{new StructMember("members", ORB.init().create_alias_tc(StructMemberSeqHelper.id(), "StructMemberSeq", ORB.init().create_sequence_tc(0, StructMemberHelper.type())), null), new StructMember("name", ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", ORB.init().create_string_tc(0)), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Initializer read(InputStream inputStream) {
        Initializer initializer = new Initializer();
        initializer.members = StructMemberSeqHelper.read(inputStream);
        initializer.name = inputStream.read_string();
        return initializer;
    }

    public static void write(OutputStream outputStream, Initializer initializer) {
        StructMemberSeqHelper.write(outputStream, initializer.members);
        outputStream.write_string(initializer.name);
    }
}
