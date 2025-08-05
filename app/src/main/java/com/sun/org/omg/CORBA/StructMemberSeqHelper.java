package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/StructMemberSeqHelper.class */
public final class StructMemberSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/StructMemberSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, StructMember[] structMemberArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, structMemberArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static StructMember[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = StructMemberHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "StructMemberSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static StructMember[] read(InputStream inputStream) {
        StructMember[] structMemberArr = new StructMember[inputStream.read_long()];
        for (int i2 = 0; i2 < structMemberArr.length; i2++) {
            structMemberArr[i2] = StructMemberHelper.read(inputStream);
        }
        return structMemberArr;
    }

    public static void write(OutputStream outputStream, StructMember[] structMemberArr) {
        outputStream.write_long(structMemberArr.length);
        for (StructMember structMember : structMemberArr) {
            StructMemberHelper.write(outputStream, structMember);
        }
    }
}
