package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.ior.TaggedProfile;
import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.IOP.TaggedProfileHelper;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/GenericTaggedProfile.class */
public class GenericTaggedProfile extends GenericIdentifiable implements TaggedProfile {
    private ORB orb;

    public GenericTaggedProfile(int i2, InputStream inputStream) {
        super(i2, inputStream);
        this.orb = (ORB) inputStream.orb();
    }

    public GenericTaggedProfile(ORB orb, int i2, byte[] bArr) {
        super(i2, bArr);
        this.orb = orb;
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public TaggedProfileTemplate getTaggedProfileTemplate() {
        return null;
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public ObjectId getObjectId() {
        return null;
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public ObjectKeyTemplate getObjectKeyTemplate() {
        return null;
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public ObjectKey getObjectKey() {
        return null;
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public boolean isEquivalent(TaggedProfile taggedProfile) {
        return equals(taggedProfile);
    }

    @Override // com.sun.corba.se.spi.ior.MakeImmutable
    public void makeImmutable() {
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public boolean isLocal() {
        return false;
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfile
    public org.omg.IOP.TaggedProfile getIOPProfile() {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.orb);
        write(encapsOutputStreamNewEncapsOutputStream);
        return TaggedProfileHelper.read((InputStream) encapsOutputStreamNewEncapsOutputStream.create_input_stream());
    }
}
