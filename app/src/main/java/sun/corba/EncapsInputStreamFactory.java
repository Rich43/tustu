package sun.corba;

import com.sun.corba.se.impl.encoding.EncapsInputStream;
import com.sun.corba.se.impl.encoding.TypeCodeInputStream;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.org.omg.SendingContext.CodeBase;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:sun/corba/EncapsInputStreamFactory.class */
public class EncapsInputStreamFactory {
    public static EncapsInputStream newEncapsInputStream(final ORB orb, final byte[] bArr, final int i2, final boolean z2, final GIOPVersion gIOPVersion) {
        return (EncapsInputStream) AccessController.doPrivileged(new PrivilegedAction<EncapsInputStream>() { // from class: sun.corba.EncapsInputStreamFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public EncapsInputStream run2() {
                return new EncapsInputStream(orb, bArr, i2, z2, gIOPVersion);
            }
        });
    }

    public static EncapsInputStream newEncapsInputStream(final ORB orb, final ByteBuffer byteBuffer, final int i2, final boolean z2, final GIOPVersion gIOPVersion) {
        return (EncapsInputStream) AccessController.doPrivileged(new PrivilegedAction<EncapsInputStream>() { // from class: sun.corba.EncapsInputStreamFactory.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public EncapsInputStream run2() {
                return new EncapsInputStream(orb, byteBuffer, i2, z2, gIOPVersion);
            }
        });
    }

    public static EncapsInputStream newEncapsInputStream(final ORB orb, final byte[] bArr, final int i2) {
        return (EncapsInputStream) AccessController.doPrivileged(new PrivilegedAction<EncapsInputStream>() { // from class: sun.corba.EncapsInputStreamFactory.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public EncapsInputStream run2() {
                return new EncapsInputStream(orb, bArr, i2);
            }
        });
    }

    public static EncapsInputStream newEncapsInputStream(final EncapsInputStream encapsInputStream) {
        return (EncapsInputStream) AccessController.doPrivileged(new PrivilegedAction<EncapsInputStream>() { // from class: sun.corba.EncapsInputStreamFactory.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public EncapsInputStream run2() {
                return new EncapsInputStream(encapsInputStream);
            }
        });
    }

    public static EncapsInputStream newEncapsInputStream(final ORB orb, final byte[] bArr, final int i2, final GIOPVersion gIOPVersion) {
        return (EncapsInputStream) AccessController.doPrivileged(new PrivilegedAction<EncapsInputStream>() { // from class: sun.corba.EncapsInputStreamFactory.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public EncapsInputStream run2() {
                return new EncapsInputStream(orb, bArr, i2, gIOPVersion);
            }
        });
    }

    public static EncapsInputStream newEncapsInputStream(final ORB orb, final byte[] bArr, final int i2, final GIOPVersion gIOPVersion, final CodeBase codeBase) {
        return (EncapsInputStream) AccessController.doPrivileged(new PrivilegedAction<EncapsInputStream>() { // from class: sun.corba.EncapsInputStreamFactory.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public EncapsInputStream run2() {
                return new EncapsInputStream(orb, bArr, i2, gIOPVersion, codeBase);
            }
        });
    }

    public static TypeCodeInputStream newTypeCodeInputStream(final ORB orb, final byte[] bArr, final int i2, final boolean z2, final GIOPVersion gIOPVersion) {
        return (TypeCodeInputStream) AccessController.doPrivileged(new PrivilegedAction<TypeCodeInputStream>() { // from class: sun.corba.EncapsInputStreamFactory.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public TypeCodeInputStream run2() {
                return new TypeCodeInputStream(orb, bArr, i2, z2, gIOPVersion);
            }
        });
    }

    public static TypeCodeInputStream newTypeCodeInputStream(final ORB orb, final ByteBuffer byteBuffer, final int i2, final boolean z2, final GIOPVersion gIOPVersion) {
        return (TypeCodeInputStream) AccessController.doPrivileged(new PrivilegedAction<TypeCodeInputStream>() { // from class: sun.corba.EncapsInputStreamFactory.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public TypeCodeInputStream run2() {
                return new TypeCodeInputStream(orb, byteBuffer, i2, z2, gIOPVersion);
            }
        });
    }

    public static TypeCodeInputStream newTypeCodeInputStream(final ORB orb, final byte[] bArr, final int i2) {
        return (TypeCodeInputStream) AccessController.doPrivileged(new PrivilegedAction<TypeCodeInputStream>() { // from class: sun.corba.EncapsInputStreamFactory.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public TypeCodeInputStream run2() {
                return new TypeCodeInputStream(orb, bArr, i2);
            }
        });
    }
}
