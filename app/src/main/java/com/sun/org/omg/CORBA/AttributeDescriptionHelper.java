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

/* loaded from: rt.jar:com/sun/org/omg/CORBA/AttributeDescriptionHelper.class */
public final class AttributeDescriptionHelper {
    private static String _id = "IDL:omg.org/CORBA/AttributeDescription:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, AttributeDescription attributeDescription) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, attributeDescription);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static AttributeDescription extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "AttributeDescription", new StructMember[]{new StructMember("name", ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", ORB.init().create_string_tc(0)), null), new StructMember("id", ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)), null), new StructMember("defined_in", ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)), null), new StructMember("version", ORB.init().create_alias_tc(VersionSpecHelper.id(), "VersionSpec", ORB.init().create_string_tc(0)), null), new StructMember("type", ORB.init().get_primitive_tc(TCKind.tk_TypeCode), null), new StructMember(Constants.ATTRNAME_MODE, AttributeModeHelper.type(), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static AttributeDescription read(InputStream inputStream) {
        AttributeDescription attributeDescription = new AttributeDescription();
        attributeDescription.name = inputStream.read_string();
        attributeDescription.id = inputStream.read_string();
        attributeDescription.defined_in = inputStream.read_string();
        attributeDescription.version = inputStream.read_string();
        attributeDescription.type = inputStream.read_TypeCode();
        attributeDescription.mode = AttributeModeHelper.read(inputStream);
        return attributeDescription;
    }

    public static void write(OutputStream outputStream, AttributeDescription attributeDescription) {
        outputStream.write_string(attributeDescription.name);
        outputStream.write_string(attributeDescription.id);
        outputStream.write_string(attributeDescription.defined_in);
        outputStream.write_string(attributeDescription.version);
        outputStream.write_TypeCode(attributeDescription.type);
        AttributeModeHelper.write(outputStream, attributeDescription.mode);
    }
}
