package org.omg.IOP;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/IOP/ServiceContextHelper.class */
public abstract class ServiceContextHelper {
    private static String _id = "IDL:omg.org/IOP/ServiceContext:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ServiceContext serviceContext) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, serviceContext);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ServiceContext extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "ServiceContext", new StructMember[]{new StructMember("context_id", ORB.init().create_alias_tc(ServiceIdHelper.id(), "ServiceId", ORB.init().get_primitive_tc(TCKind.tk_ulong)), null), new StructMember("context_data", ORB.init().create_sequence_tc(0, ORB.init().get_primitive_tc(TCKind.tk_octet)), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ServiceContext read(InputStream inputStream) {
        ServiceContext serviceContext = new ServiceContext();
        serviceContext.context_id = inputStream.read_ulong();
        int i2 = inputStream.read_long();
        serviceContext.context_data = new byte[i2];
        inputStream.read_octet_array(serviceContext.context_data, 0, i2);
        return serviceContext;
    }

    public static void write(OutputStream outputStream, ServiceContext serviceContext) {
        outputStream.write_ulong(serviceContext.context_id);
        outputStream.write_long(serviceContext.context_data.length);
        outputStream.write_octet_array(serviceContext.context_data, 0, serviceContext.context_data.length);
    }
}
