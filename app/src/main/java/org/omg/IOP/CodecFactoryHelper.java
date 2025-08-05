package org.omg.IOP;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/IOP/CodecFactoryHelper.class */
public abstract class CodecFactoryHelper {
    private static String _id = "IDL:omg.org/IOP/CodecFactory:1.0";
    private static TypeCode __typeCode = null;

    public static void insert(Any any, CodecFactory codecFactory) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, codecFactory);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static CodecFactory extract(Any any) {
        return read(any.create_input_stream());
    }

    public static synchronized TypeCode type() {
        if (__typeCode == null) {
            __typeCode = ORB.init().create_interface_tc(id(), ORBConstants.CODEC_FACTORY_NAME);
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static CodecFactory read(InputStream inputStream) {
        throw new MARSHAL();
    }

    public static void write(OutputStream outputStream, CodecFactory codecFactory) {
        throw new MARSHAL();
    }

    public static CodecFactory narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof CodecFactory) {
            return (CodecFactory) object;
        }
        throw new BAD_PARAM();
    }

    public static CodecFactory unchecked_narrow(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof CodecFactory) {
            return (CodecFactory) object;
        }
        throw new BAD_PARAM();
    }
}
