package org.omg.IOP;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/IOP/IOR.class */
public final class IOR implements IDLEntity {
    public String type_id;
    public TaggedProfile[] profiles;

    public IOR() {
        this.type_id = null;
        this.profiles = null;
    }

    public IOR(String str, TaggedProfile[] taggedProfileArr) {
        this.type_id = null;
        this.profiles = null;
        this.type_id = str;
        this.profiles = taggedProfileArr;
    }
}
