package org.omg.IOP;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/IOP/TaggedComponent.class */
public final class TaggedComponent implements IDLEntity {
    public int tag;
    public byte[] component_data;

    public TaggedComponent() {
        this.tag = 0;
        this.component_data = null;
    }

    public TaggedComponent(int i2, byte[] bArr) {
        this.tag = 0;
        this.component_data = null;
        this.tag = i2;
        this.component_data = bArr;
    }
}
