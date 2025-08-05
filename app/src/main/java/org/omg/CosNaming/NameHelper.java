package org.omg.CosNaming;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CosNaming/NameHelper.class */
public abstract class NameHelper {
    private static String _id = "IDL:omg.org/CosNaming/Name:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, NameComponent[] nameComponentArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, nameComponentArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static NameComponent[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = NameComponentHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "Name", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static NameComponent[] read(InputStream inputStream) {
        NameComponent[] nameComponentArr = new NameComponent[inputStream.read_long()];
        for (int i2 = 0; i2 < nameComponentArr.length; i2++) {
            nameComponentArr[i2] = NameComponentHelper.read(inputStream);
        }
        return nameComponentArr;
    }

    public static void write(OutputStream outputStream, NameComponent[] nameComponentArr) {
        outputStream.write_long(nameComponentArr.length);
        for (NameComponent nameComponent : nameComponentArr) {
            NameComponentHelper.write(outputStream, nameComponent);
        }
    }
}
