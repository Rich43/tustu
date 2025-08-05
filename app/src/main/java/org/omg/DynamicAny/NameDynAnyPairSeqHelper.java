package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/DynamicAny/NameDynAnyPairSeqHelper.class */
public abstract class NameDynAnyPairSeqHelper {
    private static String _id = "IDL:omg.org/DynamicAny/NameDynAnyPairSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, NameDynAnyPair[] nameDynAnyPairArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, nameDynAnyPairArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NameDynAnyPair[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = NameDynAnyPairHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "NameDynAnyPairSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NameDynAnyPair[] read(InputStream inputStream) {
        NameDynAnyPair[] nameDynAnyPairArr = new NameDynAnyPair[inputStream.read_long()];
        for (int i2 = 0; i2 < nameDynAnyPairArr.length; i2++) {
            nameDynAnyPairArr[i2] = NameDynAnyPairHelper.read(inputStream);
        }
        return nameDynAnyPairArr;
    }

    public static void write(OutputStream outputStream, NameDynAnyPair[] nameDynAnyPairArr) {
        outputStream.write_long(nameDynAnyPairArr.length);
        for (NameDynAnyPair nameDynAnyPair : nameDynAnyPairArr) {
            NameDynAnyPairHelper.write(outputStream, nameDynAnyPair);
        }
    }
}
