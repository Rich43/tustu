package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/ServiceInformationHelper.class */
public abstract class ServiceInformationHelper {
    private static TypeCode _tc;

    public static void write(OutputStream outputStream, ServiceInformation serviceInformation) {
        outputStream.write_long(serviceInformation.service_options.length);
        outputStream.write_ulong_array(serviceInformation.service_options, 0, serviceInformation.service_options.length);
        outputStream.write_long(serviceInformation.service_details.length);
        for (int i2 = 0; i2 < serviceInformation.service_details.length; i2++) {
            ServiceDetailHelper.write(outputStream, serviceInformation.service_details[i2]);
        }
    }

    public static ServiceInformation read(InputStream inputStream) {
        ServiceInformation serviceInformation = new ServiceInformation();
        serviceInformation.service_options = new int[inputStream.read_long()];
        inputStream.read_ulong_array(serviceInformation.service_options, 0, serviceInformation.service_options.length);
        serviceInformation.service_details = new ServiceDetail[inputStream.read_long()];
        for (int i2 = 0; i2 < serviceInformation.service_details.length; i2++) {
            serviceInformation.service_details[i2] = ServiceDetailHelper.read(inputStream);
        }
        return serviceInformation;
    }

    public static ServiceInformation extract(Any any) {
        return read(any.create_input_stream());
    }

    public static void insert(Any any, ServiceInformation serviceInformation) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        write(outputStreamCreate_output_stream, serviceInformation);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static synchronized TypeCode type() {
        if (_tc == null) {
            _tc = ORB.init().create_struct_tc(id(), "ServiceInformation", new StructMember[]{new StructMember("service_options", ORB.init().create_sequence_tc(0, ORB.init().get_primitive_tc(TCKind.tk_ulong)), null), new StructMember("service_details", ORB.init().create_sequence_tc(0, ServiceDetailHelper.type()), null)});
        }
        return _tc;
    }

    public static String id() {
        return "IDL:omg.org/CORBA/ServiceInformation:1.0";
    }
}
