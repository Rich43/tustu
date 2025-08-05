package com.sun.corba.se.spi.activation;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/ORBPortInfoHelper.class */
public abstract class ORBPortInfoHelper {
    private static String _id = "IDL:activation/ORBPortInfo:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ORBPortInfo oRBPortInfo) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, oRBPortInfo);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ORBPortInfo extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "ORBPortInfo", new StructMember[]{new StructMember("orbId", ORB.init().create_alias_tc(ORBidHelper.id(), "ORBid", ORB.init().create_string_tc(0)), null), new StructMember(DeploymentDescriptorParser.ATTR_PORT, ORB.init().create_alias_tc(TCPPortHelper.id(), "TCPPort", ORB.init().get_primitive_tc(TCKind.tk_long)), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ORBPortInfo read(InputStream inputStream) {
        ORBPortInfo oRBPortInfo = new ORBPortInfo();
        oRBPortInfo.orbId = inputStream.read_string();
        oRBPortInfo.port = inputStream.read_long();
        return oRBPortInfo;
    }

    public static void write(OutputStream outputStream, ORBPortInfo oRBPortInfo) {
        outputStream.write_string(oRBPortInfo.orbId);
        outputStream.write_long(oRBPortInfo.port);
    }
}
