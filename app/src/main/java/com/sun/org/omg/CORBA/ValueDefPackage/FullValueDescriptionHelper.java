package com.sun.org.omg.CORBA.ValueDefPackage;

import com.sun.org.omg.CORBA.AttrDescriptionSeqHelper;
import com.sun.org.omg.CORBA.AttributeDescriptionHelper;
import com.sun.org.omg.CORBA.IdentifierHelper;
import com.sun.org.omg.CORBA.InitializerHelper;
import com.sun.org.omg.CORBA.InitializerSeqHelper;
import com.sun.org.omg.CORBA.OpDescriptionSeqHelper;
import com.sun.org.omg.CORBA.OperationDescriptionHelper;
import com.sun.org.omg.CORBA.RepositoryIdHelper;
import com.sun.org.omg.CORBA.RepositoryIdSeqHelper;
import com.sun.org.omg.CORBA.ValueMemberHelper;
import com.sun.org.omg.CORBA.ValueMemberSeqHelper;
import com.sun.org.omg.CORBA.VersionSpecHelper;
import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ValueDefPackage/FullValueDescriptionHelper.class */
public final class FullValueDescriptionHelper {
    private static String _id = "IDL:omg.org/CORBA/ValueDef/FullValueDescription:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, FullValueDescription fullValueDescription) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, fullValueDescription);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static FullValueDescription extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "FullValueDescription", new StructMember[]{new StructMember("name", ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", ORB.init().create_string_tc(0)), null), new StructMember("id", ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)), null), new StructMember("is_abstract", ORB.init().get_primitive_tc(TCKind.tk_boolean), null), new StructMember("is_custom", ORB.init().get_primitive_tc(TCKind.tk_boolean), null), new StructMember("defined_in", ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)), null), new StructMember("version", ORB.init().create_alias_tc(VersionSpecHelper.id(), "VersionSpec", ORB.init().create_string_tc(0)), null), new StructMember("operations", ORB.init().create_alias_tc(OpDescriptionSeqHelper.id(), "OpDescriptionSeq", ORB.init().create_sequence_tc(0, OperationDescriptionHelper.type())), null), new StructMember("attributes", ORB.init().create_alias_tc(AttrDescriptionSeqHelper.id(), "AttrDescriptionSeq", ORB.init().create_sequence_tc(0, AttributeDescriptionHelper.type())), null), new StructMember("members", ORB.init().create_alias_tc(ValueMemberSeqHelper.id(), "ValueMemberSeq", ORB.init().create_sequence_tc(0, ValueMemberHelper.type())), null), new StructMember("initializers", ORB.init().create_alias_tc(InitializerSeqHelper.id(), "InitializerSeq", ORB.init().create_sequence_tc(0, InitializerHelper.type())), null), new StructMember("supported_interfaces", ORB.init().create_alias_tc(RepositoryIdSeqHelper.id(), "RepositoryIdSeq", ORB.init().create_sequence_tc(0, ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)))), null), new StructMember("abstract_base_values", ORB.init().create_alias_tc(RepositoryIdSeqHelper.id(), "RepositoryIdSeq", ORB.init().create_sequence_tc(0, ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)))), null), new StructMember("is_truncatable", ORB.init().get_primitive_tc(TCKind.tk_boolean), null), new StructMember("base_value", ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)), null), new StructMember("type", ORB.init().get_primitive_tc(TCKind.tk_TypeCode), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static FullValueDescription read(InputStream inputStream) {
        FullValueDescription fullValueDescription = new FullValueDescription();
        fullValueDescription.name = inputStream.read_string();
        fullValueDescription.id = inputStream.read_string();
        fullValueDescription.is_abstract = inputStream.read_boolean();
        fullValueDescription.is_custom = inputStream.read_boolean();
        fullValueDescription.defined_in = inputStream.read_string();
        fullValueDescription.version = inputStream.read_string();
        fullValueDescription.operations = OpDescriptionSeqHelper.read(inputStream);
        fullValueDescription.attributes = AttrDescriptionSeqHelper.read(inputStream);
        fullValueDescription.members = ValueMemberSeqHelper.read(inputStream);
        fullValueDescription.initializers = InitializerSeqHelper.read(inputStream);
        fullValueDescription.supported_interfaces = RepositoryIdSeqHelper.read(inputStream);
        fullValueDescription.abstract_base_values = RepositoryIdSeqHelper.read(inputStream);
        fullValueDescription.is_truncatable = inputStream.read_boolean();
        fullValueDescription.base_value = inputStream.read_string();
        fullValueDescription.type = inputStream.read_TypeCode();
        return fullValueDescription;
    }

    public static void write(OutputStream outputStream, FullValueDescription fullValueDescription) {
        outputStream.write_string(fullValueDescription.name);
        outputStream.write_string(fullValueDescription.id);
        outputStream.write_boolean(fullValueDescription.is_abstract);
        outputStream.write_boolean(fullValueDescription.is_custom);
        outputStream.write_string(fullValueDescription.defined_in);
        outputStream.write_string(fullValueDescription.version);
        OpDescriptionSeqHelper.write(outputStream, fullValueDescription.operations);
        AttrDescriptionSeqHelper.write(outputStream, fullValueDescription.attributes);
        ValueMemberSeqHelper.write(outputStream, fullValueDescription.members);
        InitializerSeqHelper.write(outputStream, fullValueDescription.initializers);
        RepositoryIdSeqHelper.write(outputStream, fullValueDescription.supported_interfaces);
        RepositoryIdSeqHelper.write(outputStream, fullValueDescription.abstract_base_values);
        outputStream.write_boolean(fullValueDescription.is_truncatable);
        outputStream.write_string(fullValueDescription.base_value);
        outputStream.write_TypeCode(fullValueDescription.type);
    }
}
