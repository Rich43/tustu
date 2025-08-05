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

/* loaded from: rt.jar:com/sun/org/omg/CORBA/OperationDescriptionHelper.class */
public final class OperationDescriptionHelper {
    private static String _id = "IDL:omg.org/CORBA/OperationDescription:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, OperationDescription operationDescription) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, operationDescription);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static OperationDescription extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "OperationDescription", new StructMember[]{new StructMember("name", ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", ORB.init().create_string_tc(0)), null), new StructMember("id", ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)), null), new StructMember("defined_in", ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", ORB.init().create_string_tc(0)), null), new StructMember("version", ORB.init().create_alias_tc(VersionSpecHelper.id(), "VersionSpec", ORB.init().create_string_tc(0)), null), new StructMember("result", ORB.init().get_primitive_tc(TCKind.tk_TypeCode), null), new StructMember(Constants.ATTRNAME_MODE, OperationModeHelper.type(), null), new StructMember("contexts", ORB.init().create_alias_tc(ContextIdSeqHelper.id(), "ContextIdSeq", ORB.init().create_sequence_tc(0, ORB.init().create_alias_tc(ContextIdentifierHelper.id(), "ContextIdentifier", ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", ORB.init().create_string_tc(0))))), null), new StructMember("parameters", ORB.init().create_alias_tc(ParDescriptionSeqHelper.id(), "ParDescriptionSeq", ORB.init().create_sequence_tc(0, ParameterDescriptionHelper.type())), null), new StructMember("exceptions", ORB.init().create_alias_tc(ExcDescriptionSeqHelper.id(), "ExcDescriptionSeq", ORB.init().create_sequence_tc(0, ExceptionDescriptionHelper.type())), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static OperationDescription read(InputStream inputStream) {
        OperationDescription operationDescription = new OperationDescription();
        operationDescription.name = inputStream.read_string();
        operationDescription.id = inputStream.read_string();
        operationDescription.defined_in = inputStream.read_string();
        operationDescription.version = inputStream.read_string();
        operationDescription.result = inputStream.read_TypeCode();
        operationDescription.mode = OperationModeHelper.read(inputStream);
        operationDescription.contexts = ContextIdSeqHelper.read(inputStream);
        operationDescription.parameters = ParDescriptionSeqHelper.read(inputStream);
        operationDescription.exceptions = ExcDescriptionSeqHelper.read(inputStream);
        return operationDescription;
    }

    public static void write(OutputStream outputStream, OperationDescription operationDescription) {
        outputStream.write_string(operationDescription.name);
        outputStream.write_string(operationDescription.id);
        outputStream.write_string(operationDescription.defined_in);
        outputStream.write_string(operationDescription.version);
        outputStream.write_TypeCode(operationDescription.result);
        OperationModeHelper.write(outputStream, operationDescription.mode);
        ContextIdSeqHelper.write(outputStream, operationDescription.contexts);
        ParDescriptionSeqHelper.write(outputStream, operationDescription.parameters);
        ExcDescriptionSeqHelper.write(outputStream, operationDescription.exceptions);
    }
}
