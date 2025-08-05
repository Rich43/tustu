package com.sun.org.omg.CORBA;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/InitializerSeqHelper.class */
public final class InitializerSeqHelper {
    private static String _id = "IDL:omg.org/CORBA/InitializerSeq:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, Initializer[] initializerArr) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, initializerArr);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static Initializer[] extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = InitializerHelper.type();
            __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
            __typeCode = ORB.init().create_alias_tc(id(), "InitializerSeq", __typeCode);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static Initializer[] read(InputStream inputStream) {
        Initializer[] initializerArr = new Initializer[inputStream.read_long()];
        for (int i2 = 0; i2 < initializerArr.length; i2++) {
            initializerArr[i2] = InitializerHelper.read(inputStream);
        }
        return initializerArr;
    }

    public static void write(OutputStream outputStream, Initializer[] initializerArr) {
        outputStream.write_long(initializerArr.length);
        for (Initializer initializer : initializerArr) {
            InitializerHelper.write(outputStream, initializer);
        }
    }
}
