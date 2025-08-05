package com.sun.corba.se.spi.ior;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/TaggedProfile.class */
public interface TaggedProfile extends Identifiable, MakeImmutable {
    TaggedProfileTemplate getTaggedProfileTemplate();

    ObjectId getObjectId();

    ObjectKeyTemplate getObjectKeyTemplate();

    ObjectKey getObjectKey();

    boolean isEquivalent(TaggedProfile taggedProfile);

    org.omg.IOP.TaggedProfile getIOPProfile();

    boolean isLocal();
}
