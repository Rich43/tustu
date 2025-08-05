package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.DefinitionKind;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/DefinitionKindHelper.class */
public final class DefinitionKindHelper {
    private static String _id = "IDL:omg.org/CORBA/DefinitionKind:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, DefinitionKind definitionKind) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, definitionKind);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static DefinitionKind extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_enum_tc(id(), "DefinitionKind", new String[]{"dk_none", "dk_all", "dk_Attribute", "dk_Constant", "dk_Exception", "dk_Interface", "dk_Module", "dk_Operation", "dk_Typedef", "dk_Alias", "dk_Struct", "dk_Union", "dk_Enum", "dk_Primitive", "dk_String", "dk_Sequence", "dk_Array", "dk_Repository", "dk_Wstring", "dk_Fixed", "dk_Value", "dk_ValueBox", "dk_ValueMember", "dk_Native"});
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static DefinitionKind read(InputStream inputStream) {
        return DefinitionKind.from_int(inputStream.read_long());
    }

    public static void write(OutputStream outputStream, DefinitionKind definitionKind) {
        outputStream.write_long(definitionKind.value());
    }
}
