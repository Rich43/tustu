package org.omg.CosNaming;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CosNaming/NameComponentHelper.class */
public abstract class NameComponentHelper {
    private static String _id = "IDL:omg.org/CosNaming/NameComponent:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, NameComponent nameComponent) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, nameComponent);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NameComponent extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "NameComponent", new StructMember[]{new StructMember("id", ORB.init().create_alias_tc(IstringHelper.id(), "Istring", ORB.init().create_string_tc(0)), null), new StructMember("kind", ORB.init().create_alias_tc(IstringHelper.id(), "Istring", ORB.init().create_string_tc(0)), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NameComponent read(InputStream inputStream) {
        NameComponent nameComponent = new NameComponent();
        nameComponent.id = inputStream.read_string();
        nameComponent.kind = inputStream.read_string();
        return nameComponent;
    }

    public static void write(OutputStream outputStream, NameComponent nameComponent) {
        outputStream.write_string(nameComponent.id);
        outputStream.write_string(nameComponent.kind);
    }
}
