package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CosNaming.NameComponentHelper;
import org.omg.CosNaming.NameHelper;
import org.omg.CosNaming.NamingContextHelper;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/CannotProceedHelper.class */
public abstract class CannotProceedHelper {
    private static String _id = "IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, CannotProceed cannotProceed) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, cannotProceed);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static CannotProceed extract(Any any) {
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
                    __typeCode = ORB.init().create_exception_tc(id(), "CannotProceed", new StructMember[]{new StructMember("cxt", NamingContextHelper.type(), null), new StructMember("rest_of_name", ORB.init().create_alias_tc(NameHelper.id(), "Name", ORB.init().create_sequence_tc(0, NameComponentHelper.type())), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static CannotProceed read(InputStream inputStream) {
        CannotProceed cannotProceed = new CannotProceed();
        inputStream.read_string();
        cannotProceed.cxt = NamingContextHelper.read(inputStream);
        cannotProceed.rest_of_name = NameHelper.read(inputStream);
        return cannotProceed;
    }

    public static void write(OutputStream outputStream, CannotProceed cannotProceed) {
        outputStream.write_string(id());
        NamingContextHelper.write(outputStream, cannotProceed.cxt);
        NameHelper.write(outputStream, cannotProceed.rest_of_name);
    }
}
