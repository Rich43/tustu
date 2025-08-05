package com.sun.corba.se.impl.protocol.giopmsgheaders;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.IOP.IORHelper;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/IORAddressingInfoHelper.class */
public abstract class IORAddressingInfoHelper {
    private static String _id = "IDL:messages/IORAddressingInfo:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, IORAddressingInfo iORAddressingInfo) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, iORAddressingInfo);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static IORAddressingInfo extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "IORAddressingInfo", new StructMember[]{new StructMember("selected_profile_index", ORB.init().get_primitive_tc(TCKind.tk_ulong), null), new StructMember("ior", IORHelper.type(), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static IORAddressingInfo read(InputStream inputStream) {
        IORAddressingInfo iORAddressingInfo = new IORAddressingInfo();
        iORAddressingInfo.selected_profile_index = inputStream.read_ulong();
        iORAddressingInfo.ior = IORHelper.read(inputStream);
        return iORAddressingInfo;
    }

    public static void write(OutputStream outputStream, IORAddressingInfo iORAddressingInfo) {
        outputStream.write_ulong(iORAddressingInfo.selected_profile_index);
        IORHelper.write(outputStream, iORAddressingInfo.ior);
    }
}
