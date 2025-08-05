package com.sun.org.omg.CORBA;

import com.sun.org.apache.xalan.internal.templates.Constants;
import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ParameterDescriptionHelper.class */
public final class ParameterDescriptionHelper {
    private static String _id = "IDL:omg.org/CORBA/ParameterDescription:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ParameterDescription parameterDescription) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, parameterDescription);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ParameterDescription extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "ParameterDescription", new StructMember[]{new StructMember("name", ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", ORB.init().create_string_tc(0)), null), new StructMember("type", ORB.init().get_primitive_tc(TCKind.tk_TypeCode), null), new StructMember("type_def", IDLTypeHelper.type(), null), new StructMember(Constants.ATTRNAME_MODE, ParameterModeHelper.type(), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ParameterDescription read(InputStream inputStream) {
        ParameterDescription parameterDescription = new ParameterDescription();
        parameterDescription.name = inputStream.read_string();
        parameterDescription.type = inputStream.read_TypeCode();
        parameterDescription.type_def = IDLTypeHelper.read(inputStream);
        parameterDescription.mode = ParameterModeHelper.read(inputStream);
        return parameterDescription;
    }

    public static void write(OutputStream outputStream, ParameterDescription parameterDescription) {
        outputStream.write_string(parameterDescription.name);
        outputStream.write_TypeCode(parameterDescription.type);
        IDLTypeHelper.write(outputStream, parameterDescription.type_def);
        ParameterModeHelper.write(outputStream, parameterDescription.mode);
    }
}
