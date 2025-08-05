package sun.java2d.cmm;

import java.awt.color.CMMException;

/* loaded from: rt.jar:sun/java2d/cmm/Profile.class */
public class Profile {
    private final long nativePtr;

    protected Profile(long j2) {
        this.nativePtr = j2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long getNativePtr() {
        if (this.nativePtr == 0) {
            throw new CMMException("Invalid profile: ptr is null");
        }
        return this.nativePtr;
    }
}
