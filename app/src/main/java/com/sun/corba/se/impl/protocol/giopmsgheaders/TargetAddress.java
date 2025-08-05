package com.sun.corba.se.impl.protocol.giopmsgheaders;

import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.IOP.TaggedProfile;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/TargetAddress.class */
public final class TargetAddress implements IDLEntity {
    private byte[] ___object_key;
    private TaggedProfile ___profile;
    private IORAddressingInfo ___ior;
    private short __discriminator;
    private boolean __uninitialized = true;

    public short discriminator() {
        if (this.__uninitialized) {
            throw new BAD_OPERATION();
        }
        return this.__discriminator;
    }

    public byte[] object_key() {
        if (this.__uninitialized) {
            throw new BAD_OPERATION();
        }
        verifyobject_key(this.__discriminator);
        return this.___object_key;
    }

    public void object_key(byte[] bArr) {
        this.__discriminator = (short) 0;
        this.___object_key = bArr;
        this.__uninitialized = false;
    }

    private void verifyobject_key(short s2) {
        if (s2 != 0) {
            throw new BAD_OPERATION();
        }
    }

    public TaggedProfile profile() {
        if (this.__uninitialized) {
            throw new BAD_OPERATION();
        }
        verifyprofile(this.__discriminator);
        return this.___profile;
    }

    public void profile(TaggedProfile taggedProfile) {
        this.__discriminator = (short) 1;
        this.___profile = taggedProfile;
        this.__uninitialized = false;
    }

    private void verifyprofile(short s2) {
        if (s2 != 1) {
            throw new BAD_OPERATION();
        }
    }

    public IORAddressingInfo ior() {
        if (this.__uninitialized) {
            throw new BAD_OPERATION();
        }
        verifyior(this.__discriminator);
        return this.___ior;
    }

    public void ior(IORAddressingInfo iORAddressingInfo) {
        this.__discriminator = (short) 2;
        this.___ior = iORAddressingInfo;
        this.__uninitialized = false;
    }

    private void verifyior(short s2) {
        if (s2 != 2) {
            throw new BAD_OPERATION();
        }
    }

    public void _default() {
        this.__discriminator = Short.MIN_VALUE;
        this.__uninitialized = false;
    }
}
