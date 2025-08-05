package sun.corba;

import com.sun.corba.se.impl.encoding.CDROutputObject;
import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.encoding.TypeCodeOutputStream;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.transport.CorbaConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/corba/OutputStreamFactory.class */
public final class OutputStreamFactory {
    private OutputStreamFactory() {
    }

    public static TypeCodeOutputStream newTypeCodeOutputStream(final ORB orb) {
        return (TypeCodeOutputStream) AccessController.doPrivileged(new PrivilegedAction<TypeCodeOutputStream>() { // from class: sun.corba.OutputStreamFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public TypeCodeOutputStream run2() {
                return new TypeCodeOutputStream(orb);
            }
        });
    }

    public static TypeCodeOutputStream newTypeCodeOutputStream(final ORB orb, final boolean z2) {
        return (TypeCodeOutputStream) AccessController.doPrivileged(new PrivilegedAction<TypeCodeOutputStream>() { // from class: sun.corba.OutputStreamFactory.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public TypeCodeOutputStream run2() {
                return new TypeCodeOutputStream(orb, z2);
            }
        });
    }

    public static EncapsOutputStream newEncapsOutputStream(final ORB orb) {
        return (EncapsOutputStream) AccessController.doPrivileged(new PrivilegedAction<EncapsOutputStream>() { // from class: sun.corba.OutputStreamFactory.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public EncapsOutputStream run2() {
                return new EncapsOutputStream(orb);
            }
        });
    }

    public static EncapsOutputStream newEncapsOutputStream(final ORB orb, final GIOPVersion gIOPVersion) {
        return (EncapsOutputStream) AccessController.doPrivileged(new PrivilegedAction<EncapsOutputStream>() { // from class: sun.corba.OutputStreamFactory.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public EncapsOutputStream run2() {
                return new EncapsOutputStream(orb, gIOPVersion);
            }
        });
    }

    public static EncapsOutputStream newEncapsOutputStream(final ORB orb, final boolean z2) {
        return (EncapsOutputStream) AccessController.doPrivileged(new PrivilegedAction<EncapsOutputStream>() { // from class: sun.corba.OutputStreamFactory.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public EncapsOutputStream run2() {
                return new EncapsOutputStream(orb, z2);
            }
        });
    }

    public static CDROutputObject newCDROutputObject(final ORB orb, final MessageMediator messageMediator, final Message message, final byte b2) {
        return (CDROutputObject) AccessController.doPrivileged(new PrivilegedAction<CDROutputObject>() { // from class: sun.corba.OutputStreamFactory.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public CDROutputObject run2() {
                return new CDROutputObject(orb, messageMediator, message, b2);
            }
        });
    }

    public static CDROutputObject newCDROutputObject(final ORB orb, final MessageMediator messageMediator, final Message message, final byte b2, final int i2) {
        return (CDROutputObject) AccessController.doPrivileged(new PrivilegedAction<CDROutputObject>() { // from class: sun.corba.OutputStreamFactory.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public CDROutputObject run2() {
                return new CDROutputObject(orb, messageMediator, message, b2, i2);
            }
        });
    }

    public static CDROutputObject newCDROutputObject(final ORB orb, final CorbaMessageMediator corbaMessageMediator, final GIOPVersion gIOPVersion, final CorbaConnection corbaConnection, final Message message, final byte b2) {
        return (CDROutputObject) AccessController.doPrivileged(new PrivilegedAction<CDROutputObject>() { // from class: sun.corba.OutputStreamFactory.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public CDROutputObject run2() {
                return new CDROutputObject(orb, corbaMessageMediator, gIOPVersion, corbaConnection, message, b2);
            }
        });
    }
}
