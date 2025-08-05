package org.omg.CORBA;

import com.sun.corba.se.impl.util.RepositoryId;
import java.io.Serializable;
import org.omg.CORBA.portable.BoxedValueHelper;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/CORBA/WStringValueHelper.class */
public class WStringValueHelper implements BoxedValueHelper {
    private static String _id = RepositoryId.kWStringValueRepID;
    private static WStringValueHelper _instance = new WStringValueHelper();
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, String str) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, str);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static String extract(Any any) {
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
                    __typeCode = ORB.init().create_wstring_tc(0);
                    __typeCode = ORB.init().create_value_box_tc(_id, RepositoryId.kWStringStubValue, __typeCode);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static String read(InputStream inputStream) {
        if (!(inputStream instanceof org.omg.CORBA_2_3.portable.InputStream)) {
            throw new BAD_PARAM();
        }
        return (String) ((org.omg.CORBA_2_3.portable.InputStream) inputStream).read_value(_instance);
    }

    @Override // org.omg.CORBA.portable.BoxedValueHelper
    public Serializable read_value(InputStream inputStream) {
        return inputStream.read_wstring();
    }

    public static void write(OutputStream outputStream, String str) {
        if (!(outputStream instanceof org.omg.CORBA_2_3.portable.OutputStream)) {
            throw new BAD_PARAM();
        }
        ((org.omg.CORBA_2_3.portable.OutputStream) outputStream).write_value(str, _instance);
    }

    @Override // org.omg.CORBA.portable.BoxedValueHelper
    public void write_value(OutputStream outputStream, Serializable serializable) {
        if (!(serializable instanceof String)) {
            throw new MARSHAL();
        }
        outputStream.write_wstring((String) serializable);
    }

    @Override // org.omg.CORBA.portable.BoxedValueHelper
    public String get_id() {
        return _id;
    }
}
