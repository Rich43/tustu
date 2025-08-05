package com.sun.org.apache.bcel.internal.classfile;

import java.io.Serializable;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/AccessFlags.class */
public abstract class AccessFlags implements Serializable {
    protected int access_flags;

    public AccessFlags() {
    }

    public AccessFlags(int a2) {
        this.access_flags = a2;
    }

    public final int getAccessFlags() {
        return this.access_flags;
    }

    public final int getModifiers() {
        return this.access_flags;
    }

    public final void setAccessFlags(int access_flags) {
        this.access_flags = access_flags;
    }

    public final void setModifiers(int access_flags) {
        setAccessFlags(access_flags);
    }

    private final void setFlag(int flag, boolean set) {
        if ((this.access_flags & flag) != 0) {
            if (!set) {
                this.access_flags ^= flag;
            }
        } else if (set) {
            this.access_flags |= flag;
        }
    }

    public final void isPublic(boolean flag) {
        setFlag(1, flag);
    }

    public final boolean isPublic() {
        return (this.access_flags & 1) != 0;
    }

    public final void isPrivate(boolean flag) {
        setFlag(2, flag);
    }

    public final boolean isPrivate() {
        return (this.access_flags & 2) != 0;
    }

    public final void isProtected(boolean flag) {
        setFlag(4, flag);
    }

    public final boolean isProtected() {
        return (this.access_flags & 4) != 0;
    }

    public final void isStatic(boolean flag) {
        setFlag(8, flag);
    }

    public final boolean isStatic() {
        return (this.access_flags & 8) != 0;
    }

    public final void isFinal(boolean flag) {
        setFlag(16, flag);
    }

    public final boolean isFinal() {
        return (this.access_flags & 16) != 0;
    }

    public final void isSynchronized(boolean flag) {
        setFlag(32, flag);
    }

    public final boolean isSynchronized() {
        return (this.access_flags & 32) != 0;
    }

    public final void isVolatile(boolean flag) {
        setFlag(64, flag);
    }

    public final boolean isVolatile() {
        return (this.access_flags & 64) != 0;
    }

    public final void isTransient(boolean flag) {
        setFlag(128, flag);
    }

    public final boolean isTransient() {
        return (this.access_flags & 128) != 0;
    }

    public final void isNative(boolean flag) {
        setFlag(256, flag);
    }

    public final boolean isNative() {
        return (this.access_flags & 256) != 0;
    }

    public final void isInterface(boolean flag) {
        setFlag(512, flag);
    }

    public final boolean isInterface() {
        return (this.access_flags & 512) != 0;
    }

    public final void isAbstract(boolean flag) {
        setFlag(1024, flag);
    }

    public final boolean isAbstract() {
        return (this.access_flags & 1024) != 0;
    }

    public final void isStrictfp(boolean flag) {
        setFlag(2048, flag);
    }

    public final boolean isStrictfp() {
        return (this.access_flags & 2048) != 0;
    }
}
