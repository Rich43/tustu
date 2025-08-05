package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ValueMemberSeqHelper.class */
public final class ValueMemberSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/ValueMemberSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, ValueMember[] valueMemberArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, valueMemberArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ValueMember[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ValueMemberHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "ValueMemberSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ValueMember[] read(InputStream inputStream) {
        ValueMember[] valueMemberArr = new ValueMember[inputStream.read_long()];
        for (int i2 = 0; i2 < valueMemberArr.length; i2++) {
            valueMemberArr[i2] = ValueMemberHelper.read(inputStream);
        }
        return valueMemberArr;
    }

    public static void write(OutputStream outputStream, ValueMember[] valueMemberArr) {
        outputStream.write_long(valueMemberArr.length);
        for (ValueMember valueMember : valueMemberArr) {
            ValueMemberHelper.write(outputStream, valueMember);
        }
    }
}
