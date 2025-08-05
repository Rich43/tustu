package org.omg.IOP;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/IOP/IORHelper.class */
public abstract class IORHelper {
    private static String _id = "IDL:omg.org/IOP/IOR:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, IOR ior) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, ior);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static IOR extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "IOR", new StructMember[]{new StructMember("type_id", ORB.init().create_string_tc(0), null), new StructMember("profiles", ORB.init().create_sequence_tc(0, TaggedProfileHelper.type()), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static IOR read(InputStream inputStream) {
        IOR ior = new IOR();
        ior.type_id = inputStream.read_string();
        ior.profiles = new TaggedProfile[inputStream.read_long()];
        for (int i2 = 0; i2 < ior.profiles.length; i2++) {
            ior.profiles[i2] = TaggedProfileHelper.read(inputStream);
        }
        return ior;
    }

    public static void write(OutputStream outputStream, IOR ior) {
        outputStream.write_string(ior.type_id);
        outputStream.write_long(ior.profiles.length);
        for (int i2 = 0; i2 < ior.profiles.length; i2++) {
            TaggedProfileHelper.write(outputStream, ior.profiles[i2]);
        }
    }
}
