package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ObjectReferenceFactoryHelper.class */
public abstract class ObjectReferenceFactoryHelper {
    private static String _id = "IDL:omg.org/PortableInterceptor/ObjectReferenceFactory:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, ObjectReferenceFactory objectReferenceFactory) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, objectReferenceFactory);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static ObjectReferenceFactory extract(Any any) {
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
                    __typeCode = ORB.init().create_value_tc(_id, "ObjectReferenceFactory", (short) 2, null, new ValueMember[0]);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static ObjectReferenceFactory read(InputStream inputStream) {
        return (ObjectReferenceFactory) ((org.omg.CORBA_2_3.portable.InputStream) inputStream).read_value(id());
    }

    public static void write(OutputStream outputStream, ObjectReferenceFactory objectReferenceFactory) {
        ((org.omg.CORBA_2_3.portable.OutputStream) outputStream).write_value(objectReferenceFactory, id());
    }
}
