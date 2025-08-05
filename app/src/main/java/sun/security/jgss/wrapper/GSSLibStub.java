package sun.security.jgss.wrapper;

import java.util.Hashtable;
import org.ietf.jgss.ChannelBinding;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

/* loaded from: rt.jar:sun/security/jgss/wrapper/GSSLibStub.class */
class GSSLibStub {
    private Oid mech;
    private long pMech;
    private static Hashtable<Oid, GSSLibStub> table = new Hashtable<>(5);

    static native boolean init(String str, boolean z2);

    private static native long getMechPtr(byte[] bArr);

    static native Oid[] indicateMechs();

    native Oid[] inquireNamesForMech() throws GSSException;

    native void releaseName(long j2);

    native long importName(byte[] bArr, Oid oid);

    native boolean compareName(long j2, long j3);

    native long canonicalizeName(long j2);

    native byte[] exportName(long j2) throws GSSException;

    native Object[] displayName(long j2) throws GSSException;

    native long acquireCred(long j2, int i2, int i3) throws GSSException;

    native long releaseCred(long j2);

    native long getCredName(long j2);

    native int getCredTime(long j2);

    native int getCredUsage(long j2);

    native NativeGSSContext importContext(byte[] bArr);

    native byte[] initContext(long j2, long j3, ChannelBinding channelBinding, byte[] bArr, NativeGSSContext nativeGSSContext);

    native byte[] acceptContext(long j2, ChannelBinding channelBinding, byte[] bArr, NativeGSSContext nativeGSSContext);

    native long[] inquireContext(long j2);

    native Oid getContextMech(long j2);

    native long getContextName(long j2, boolean z2);

    native int getContextTime(long j2);

    native long deleteContext(long j2);

    native int wrapSizeLimit(long j2, int i2, int i3, int i4);

    native byte[] exportContext(long j2);

    native byte[] getMic(long j2, int i2, byte[] bArr);

    native void verifyMic(long j2, byte[] bArr, byte[] bArr2, MessageProp messageProp);

    native byte[] wrap(long j2, byte[] bArr, MessageProp messageProp);

    native byte[] unwrap(long j2, byte[] bArr, MessageProp messageProp);

    static GSSLibStub getInstance(Oid oid) throws GSSException {
        GSSLibStub gSSLibStub = table.get(oid);
        if (gSSLibStub == null) {
            gSSLibStub = new GSSLibStub(oid);
            table.put(oid, gSSLibStub);
        }
        return gSSLibStub;
    }

    private GSSLibStub(Oid oid) throws GSSException {
        SunNativeProvider.debug("Created GSSLibStub for mech " + ((Object) oid));
        this.mech = oid;
        this.pMech = getMechPtr(oid.getDER());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GSSLibStub)) {
            return false;
        }
        return this.mech.equals(((GSSLibStub) obj).getMech());
    }

    public int hashCode() {
        return this.mech.hashCode();
    }

    Oid getMech() {
        return this.mech;
    }
}
