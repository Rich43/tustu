package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/ServiceDetailHelper.class */
public abstract class ServiceDetailHelper {
    private static TypeCode _tc;

    public static void write(OutputStream outputStream, ServiceDetail serviceDetail) {
        outputStream.write_ulong(serviceDetail.service_detail_type);
        outputStream.write_long(serviceDetail.service_detail.length);
        outputStream.write_octet_array(serviceDetail.service_detail, 0, serviceDetail.service_detail.length);
    }

    public static ServiceDetail read(InputStream inputStream) {
        ServiceDetail serviceDetail = new ServiceDetail();
        serviceDetail.service_detail_type = inputStream.read_ulong();
        serviceDetail.service_detail = new byte[inputStream.read_long()];
        inputStream.read_octet_array(serviceDetail.service_detail, 0, serviceDetail.service_detail.length);
        return serviceDetail;
    }

    public static ServiceDetail extract(Any any) {
        return read(any.create_input_stream());
    }

    public static void insert(Any any, ServiceDetail serviceDetail) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        write(outputStreamCreate_output_stream, serviceDetail);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static synchronized TypeCode type() {
        if (_tc == null) {
            _tc = ORB.init().create_struct_tc(id(), "ServiceDetail", new StructMember[]{new StructMember("service_detail_type", ORB.init().get_primitive_tc(TCKind.tk_ulong), null), new StructMember("service_detail", ORB.init().create_sequence_tc(0, ORB.init().get_primitive_tc(TCKind.tk_octet)), null)});
        }
        return _tc;
    }

    public static String id() {
        return "IDL:omg.org/CORBA/ServiceDetail:1.0";
    }
}
