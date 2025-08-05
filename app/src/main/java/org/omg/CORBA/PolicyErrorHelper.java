package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import sun.security.x509.CRLReasonCodeExtension;

/* loaded from: rt.jar:org/omg/CORBA/PolicyErrorHelper.class */
public abstract class PolicyErrorHelper {
    private static String _id = "IDL:omg.org/CORBA/PolicyError:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, PolicyError policyError) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, policyError);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static PolicyError extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "PolicyError", new StructMember[]{new StructMember(CRLReasonCodeExtension.REASON, ORB.init().create_alias_tc(PolicyErrorCodeHelper.id(), "PolicyErrorCode", ORB.init().get_primitive_tc(TCKind.tk_short)), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static PolicyError read(InputStream inputStream) {
        PolicyError policyError = new PolicyError();
        inputStream.read_string();
        policyError.reason = inputStream.read_short();
        return policyError;
    }

    public static void write(OutputStream outputStream, PolicyError policyError) {
        outputStream.write_string(id());
        outputStream.write_short(policyError.reason);
    }
}
