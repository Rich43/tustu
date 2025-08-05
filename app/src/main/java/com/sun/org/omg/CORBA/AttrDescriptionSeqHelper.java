package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/AttrDescriptionSeqHelper.class */
public final class AttrDescriptionSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/AttrDescriptionSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, AttributeDescription[] attributeDescriptionArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, attributeDescriptionArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static AttributeDescription[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = AttributeDescriptionHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "AttrDescriptionSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static AttributeDescription[] read(InputStream inputStream) {
        AttributeDescription[] attributeDescriptionArr = new AttributeDescription[inputStream.read_long()];
        for (int i2 = 0; i2 < attributeDescriptionArr.length; i2++) {
            attributeDescriptionArr[i2] = AttributeDescriptionHelper.read(inputStream);
        }
        return attributeDescriptionArr;
    }

    public static void write(OutputStream outputStream, AttributeDescription[] attributeDescriptionArr) {
        outputStream.write_long(attributeDescriptionArr.length);
        for (AttributeDescription attributeDescription : attributeDescriptionArr) {
            AttributeDescriptionHelper.write(outputStream, attributeDescription);
        }
    }
}
