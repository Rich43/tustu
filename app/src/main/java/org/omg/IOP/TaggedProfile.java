package org.omg.IOP;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/IOP/TaggedProfile.class */
public final class TaggedProfile implements IDLEntity {
    public int tag;
    public byte[] profile_data;

    public TaggedProfile() {
        this.tag = 0;
        this.profile_data = null;
    }

    public TaggedProfile(int i2, byte[] bArr) {
        this.tag = 0;
        this.profile_data = null;
        this.tag = i2;
        this.profile_data = bArr;
    }
}
