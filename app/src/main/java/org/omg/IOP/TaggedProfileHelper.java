package org.omg.IOP;

import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:org/omg/IOP/TaggedProfileHelper.class */
public abstract class TaggedProfileHelper {
    private static String _id = "IDL:omg.org/IOP/TaggedProfile:1.0";
    private static TypeCode __typeCode = null;
    private static boolean __active = false;

    public static void insert(Any any, TaggedProfile taggedProfile) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        any.type(type());
        write(outputStreamCreate_output_stream, taggedProfile);
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), type());
    }

    public static TaggedProfile extract(Any any) {
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
                    __typeCode = ORB.init().create_struct_tc(id(), "TaggedProfile", new StructMember[]{new StructMember("tag", ORB.init().create_alias_tc(ProfileIdHelper.id(), "ProfileId", ORB.init().get_primitive_tc(TCKind.tk_ulong)), null), new StructMember("profile_data", ORB.init().create_sequence_tc(0, ORB.init().get_primitive_tc(TCKind.tk_octet)), null)});
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static TaggedProfile read(InputStream inputStream) {
        TaggedProfile taggedProfile = new TaggedProfile();
        taggedProfile.tag = inputStream.read_ulong();
        int i2 = inputStream.read_long();
        taggedProfile.profile_data = new byte[i2];
        inputStream.read_octet_array(taggedProfile.profile_data, 0, i2);
        return taggedProfile;
    }

    public static void write(OutputStream outputStream, TaggedProfile taggedProfile) {
        outputStream.write_ulong(taggedProfile.tag);
        outputStream.write_long(taggedProfile.profile_data.length);
        outputStream.write_octet_array(taggedProfile.profile_data, 0, taggedProfile.profile_data.length);
    }
}
