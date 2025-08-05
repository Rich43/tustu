package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/DynamicAny/NameDynAnyPairHelper.class */
public abstract class NameDynAnyPairHelper {
    private static String _id = "IDL:omg.org/DynamicAny/NameDynAnyPair:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, NameDynAnyPair nameDynAnyPair) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, nameDynAnyPair);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NameDynAnyPair extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "NameDynAnyPair", new StructMember[]{new StructMember("id", ORB.init().create_alias_tc(FieldNameHelper.id(), "FieldName", ORB.init().create_string_tc(0)), null), new StructMember("value", DynAnyHelper.type(), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NameDynAnyPair read(InputStream inputStream) {
        NameDynAnyPair nameDynAnyPair = new NameDynAnyPair();
        nameDynAnyPair.id = inputStream.read_string();
        nameDynAnyPair.value = DynAnyHelper.read(inputStream);
        return nameDynAnyPair;
    }

    public static void write(OutputStream outputStream, NameDynAnyPair nameDynAnyPair) {
        outputStream.write_string(nameDynAnyPair.id);
        DynAnyHelper.write(outputStream, nameDynAnyPair.value);
    }
}
