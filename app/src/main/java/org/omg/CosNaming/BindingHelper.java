package org.omg.CosNaming;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CosNaming/BindingHelper.class */
public abstract class BindingHelper {
    private static String _id = "IDL:omg.org/CosNaming/Binding:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, Binding binding) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, binding);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Binding extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "Binding", new StructMember[]{new StructMember("binding_name", ORB.init().create_alias_tc(NameHelper.id(), "Name", ORB.init().create_sequence_tc(0, NameComponentHelper.type())), null), new StructMember("binding_type", BindingTypeHelper.type(), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Binding read(InputStream inputStream) {
        Binding binding = new Binding();
        binding.binding_name = NameHelper.read(inputStream);
        binding.binding_type = BindingTypeHelper.read(inputStream);
        return binding;
    }

    public static void write(OutputStream outputStream, Binding binding) {
        NameHelper.write(outputStream, binding.binding_name);
        BindingTypeHelper.write(outputStream, binding.binding_type);
    }
}
