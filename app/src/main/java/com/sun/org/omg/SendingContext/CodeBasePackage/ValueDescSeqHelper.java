package com.sun.org.omg.SendingContext.CodeBasePackage;

import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescriptionHelper;
import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/SendingContext/CodeBasePackage/ValueDescSeqHelper.class */
public final class ValueDescSeqHelper {
    private static String _id = "IDL:omg.org/SendingContext/CodeBase/ValueDescSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, FullValueDescription[] fullValueDescriptionArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, fullValueDescriptionArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static FullValueDescription[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = FullValueDescriptionHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "ValueDescSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static FullValueDescription[] read(InputStream inputStream) {
        FullValueDescription[] fullValueDescriptionArr = new FullValueDescription[inputStream.read_long()];
        for (int i2 = 0; i2 < fullValueDescriptionArr.length; i2++) {
            fullValueDescriptionArr[i2] = FullValueDescriptionHelper.read(inputStream);
        }
        return fullValueDescriptionArr;
    }

    public static void write(OutputStream outputStream, FullValueDescription[] fullValueDescriptionArr) {
        outputStream.write_long(fullValueDescriptionArr.length);
        for (FullValueDescription fullValueDescription : fullValueDescriptionArr) {
            FullValueDescriptionHelper.write(outputStream, fullValueDescription);
        }
    }
}
