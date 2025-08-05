package com.sun.corba.se.spi.activation;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import sun.security.x509.CRLReasonCodeExtension;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/BadServerDefinitionHelper.class */
public abstract class BadServerDefinitionHelper {
    private static String _id = "IDL:activation/BadServerDefinition:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, BadServerDefinition badServerDefinition) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, badServerDefinition);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static BadServerDefinition extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "BadServerDefinition", new StructMember[]{new StructMember(CRLReasonCodeExtension.REASON, ORB.init().create_string_tc(0), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static BadServerDefinition read(InputStream inputStream) {
        BadServerDefinition badServerDefinition = new BadServerDefinition();
        inputStream.read_string();
        badServerDefinition.reason = inputStream.read_string();
        return badServerDefinition;
    }

    public static void write(OutputStream outputStream, BadServerDefinition badServerDefinition) {
        outputStream.write_string(id());
        outputStream.write_string(badServerDefinition.reason);
    }
}
