package org.omg.CosNaming;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CosNaming/BindingListHelper.class */
public abstract class BindingListHelper {
    private static String _id = "IDL:omg.org/CosNaming/BindingList:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Binding[] bindingArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, bindingArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Binding[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = BindingHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "BindingList", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Binding[] read(InputStream inputStream) {
        Binding[] bindingArr = new Binding[inputStream.read_long()];
        for (int i2 = 0; i2 < bindingArr.length; i2++) {
            bindingArr[i2] = BindingHelper.read(inputStream);
        }
        return bindingArr;
    }

    public static void write(OutputStream outputStream, Binding[] bindingArr) {
        outputStream.write_long(bindingArr.length);
        for (Binding binding : bindingArr) {
            BindingHelper.write(outputStream, binding);
        }
    }
}
