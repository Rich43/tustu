package bX;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bX/g.class */
public final class g {

    /* renamed from: a, reason: collision with root package name */
    private static final Logger f7710a = Logger.getLogger(g.class.getName().toLowerCase());

    private g() {
        throw new UnsupportedOperationException();
    }

    public static final f a(f fVar, InetAddress inetAddress, int i2, InetAddress inetAddress2, String str, c[] cVarArr) {
        if (fVar == null) {
            throw new NullPointerException("request is null");
        }
        if (!fVar.n()) {
            throw new a("request is BOOTP");
        }
        Byte bW2 = fVar.w();
        if (bW2 == null) {
            throw new a("request has no message type");
        }
        if (bW2.byteValue() != 1) {
            throw new a("request is not DHCPDISCOVER");
        }
        if (inetAddress == null) {
            throw new IllegalArgumentException("offeredAddress must not be null");
        }
        if (!(inetAddress instanceof Inet4Address)) {
            throw new IllegalArgumentException("offeredAddress must be IPv4");
        }
        f fVar2 = new f();
        fVar2.c((byte) 2);
        fVar2.b(fVar.m());
        fVar2.a(fVar.l());
        fVar2.a(fVar.t());
        fVar2.a(fVar.i());
        fVar2.b(inetAddress);
        fVar2.c(fVar.k());
        fVar2.a(fVar.c());
        fVar2.d((byte) 2);
        fVar2.a((byte) 51, i2);
        fVar2.a((byte) 54, inetAddress2);
        fVar2.a((byte) 56, str);
        if (cVarArr != null) {
            for (c cVar : cVarArr) {
                fVar2.a(cVar.a(fVar));
            }
        }
        fVar2.a(a(fVar, (byte) 2));
        return fVar2;
    }

    public static final f b(f fVar, InetAddress inetAddress, int i2, InetAddress inetAddress2, String str, c[] cVarArr) {
        if (fVar == null) {
            throw new NullPointerException("request is null");
        }
        if (!fVar.n()) {
            throw new a("request is BOOTP");
        }
        Byte bW2 = fVar.w();
        if (bW2 == null) {
            throw new a("request has no message type");
        }
        if (bW2.byteValue() != 3 && bW2.byteValue() != 8) {
            throw new a("request is not DHCPREQUEST/DHCPINFORM");
        }
        if (inetAddress == null) {
            throw new IllegalArgumentException("offeredAddress must not be null");
        }
        if (!(inetAddress instanceof Inet4Address)) {
            throw new IllegalArgumentException("offeredAddress must be IPv4");
        }
        f fVar2 = new f();
        fVar2.c((byte) 2);
        fVar2.b(fVar.m());
        fVar2.a(fVar.l());
        fVar2.a(fVar.t());
        fVar2.a(fVar.i());
        fVar2.b(fVar.f());
        if (bW2.byteValue() != 8) {
            fVar2.b(inetAddress);
        }
        fVar2.c(fVar.k());
        fVar2.a(fVar.c());
        fVar2.d((byte) 5);
        if (bW2.byteValue() == 3) {
            fVar2.a((byte) 51, i2);
        }
        fVar2.a((byte) 54, inetAddress2);
        fVar2.a((byte) 56, str);
        if (cVarArr != null) {
            for (c cVar : cVarArr) {
                fVar2.a(cVar.a(fVar));
            }
        }
        fVar2.a(a(fVar, (byte) 5));
        return fVar2;
    }

    public static final f a(f fVar, InetAddress inetAddress, String str) {
        if (fVar == null) {
            throw new NullPointerException("request is null");
        }
        if (!fVar.n()) {
            throw new a("request is BOOTP");
        }
        Byte bW2 = fVar.w();
        if (bW2 == null) {
            throw new a("request has no message type");
        }
        if (bW2.byteValue() != 3) {
            throw new a("request is not DHCPREQUEST");
        }
        f fVar2 = new f();
        fVar2.c((byte) 2);
        fVar2.b(fVar.m());
        fVar2.a(fVar.l());
        fVar2.a(fVar.t());
        fVar2.a(fVar.i());
        fVar2.c(fVar.k());
        fVar2.a(fVar.c());
        fVar2.d((byte) 6);
        fVar2.a((byte) 54, inetAddress);
        fVar2.a((byte) 56, str);
        fVar2.a(a(fVar, (byte) 6));
        return fVar2;
    }

    public static InetSocketAddress a(f fVar, byte b2) {
        InetSocketAddress inetSocketAddress;
        if (fVar == null) {
            throw new IllegalArgumentException("request is null");
        }
        InetAddress inetAddressJ = fVar.j();
        InetAddress inetAddressE = fVar.e();
        switch (b2) {
            case 2:
            case 5:
                if (!b.f7662a.equals(inetAddressJ)) {
                    inetSocketAddress = new InetSocketAddress(inetAddressJ, 67);
                    break;
                } else if (!b.f7662a.equals(inetAddressE)) {
                    inetSocketAddress = new InetSocketAddress(inetAddressE, 68);
                    break;
                } else {
                    inetSocketAddress = new InetSocketAddress(b.f7663b, 68);
                    break;
                }
            case 3:
            case 4:
            default:
                throw new IllegalArgumentException("responseType not valid");
            case 6:
                if (!b.f7662a.equals(inetAddressJ)) {
                    inetSocketAddress = new InetSocketAddress(inetAddressJ, 67);
                    break;
                } else {
                    inetSocketAddress = new InetSocketAddress(b.f7663b, 68);
                    break;
                }
        }
        return inetSocketAddress;
    }
}
