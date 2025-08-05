package com.sun.corba.se.impl.protocol.giopmsgheaders;

import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UnionMember;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.IOP.TaggedProfileHelper;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/TargetAddressHelper.class */
public abstract class TargetAddressHelper {
    private static String _id = "IDL:messages/TargetAddress:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, TargetAddress targetAddress) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, targetAddress);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static TargetAddress extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            TypeCode typeCodeCreate_alias_tc = ORB.init().create_alias_tc(AddressingDispositionHelper.id(), "AddressingDisposition", ORB.init().get_primitive_tc(TCKind.tk_short));
            Any anyCreate_any = ORB.init().create_any();
            anyCreate_any.insert_short((short) 0);
            TypeCode typeCode = ORB.init().get_primitive_tc(TCKind.tk_octet);
            Any anyCreate_any2 = ORB.init().create_any();
            anyCreate_any2.insert_short((short) 1);
            Any anyCreate_any3 = ORB.init().create_any();
            anyCreate_any3.insert_short((short) 2);
            __typeCode = ORB.init().create_union_tc(id(), "TargetAddress", typeCodeCreate_alias_tc, new UnionMember[]{new UnionMember("object_key", anyCreate_any, ORB.init().create_sequence_tc(0, typeCode), null), new UnionMember("profile", anyCreate_any2, TaggedProfileHelper.type(), null), new UnionMember("ior", anyCreate_any3, IORAddressingInfoHelper.type(), null)});
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static TargetAddress read(InputStream inputStream) {
        TargetAddress targetAddress = new TargetAddress();
        switch (inputStream.read_short()) {
            case 0:
                int i2 = inputStream.read_long();
                byte[] bArr = new byte[i2];
                inputStream.read_octet_array(bArr, 0, i2);
                targetAddress.object_key(bArr);
                break;
            case 1:
                targetAddress.profile(TaggedProfileHelper.read(inputStream));
                break;
            case 2:
                targetAddress.ior(IORAddressingInfoHelper.read(inputStream));
                break;
            default:
                throw new BAD_OPERATION();
        }
        return targetAddress;
    }

    public static void write(OutputStream outputStream, TargetAddress targetAddress) {
        outputStream.write_short(targetAddress.discriminator());
        switch (targetAddress.discriminator()) {
            case 0:
                outputStream.write_long(targetAddress.object_key().length);
                outputStream.write_octet_array(targetAddress.object_key(), 0, targetAddress.object_key().length);
                return;
            case 1:
                TaggedProfileHelper.write(outputStream, targetAddress.profile());
                return;
            case 2:
                IORAddressingInfoHelper.write(outputStream, targetAddress.ior());
                return;
            default:
                throw new BAD_OPERATION();
        }
    }
}
