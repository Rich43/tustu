package E;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:E/b.class */
public class b {

    /* renamed from: a, reason: collision with root package name */
    private static final Logger f246a = Logger.getLogger("DHCPProcessor");

    /* renamed from: b, reason: collision with root package name */
    private final h f247b;

    /* renamed from: c, reason: collision with root package name */
    private InetAddress f248c;

    /* renamed from: d, reason: collision with root package name */
    private String f249d = j.a();

    public b(h hVar, InetAddress inetAddress) {
        this.f248c = null;
        this.f247b = hVar;
        this.f248c = inetAddress;
    }

    public bX.f a(bX.f fVar) {
        f246a.info("Receive Discover from " + fVar.d() + " Request IP " + fVar.e().toString());
        g gVarA = this.f247b.a(fVar.d());
        if (gVarA == null) {
            return null;
        }
        if (this.f249d == null || !fVar.i((byte) 12) || !fVar.g((byte) 12).equalsIgnoreCase(this.f249d)) {
            return b(fVar, gVarA);
        }
        try {
            return d(fVar, gVarA);
        } catch (UnknownHostException e2) {
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    public bX.f b(bX.f fVar) {
        System.out.println("Receive Request from " + fVar.d() + " Request IP " + fVar.e().toString() + " Serve host " + ((Object) fVar.p()));
        if (this.f249d == null || !fVar.i((byte) 12) || !fVar.g((byte) 12).equalsIgnoreCase(this.f249d)) {
            boolean zD = true;
            if (!fVar.p().equals(this.f248c) && this.f247b.e(fVar.d())) {
                zD = this.f247b.d(fVar.e().toString());
                this.f247b.c(fVar.d());
            }
            g gVarB = zD ? this.f247b.b(fVar.d()) : this.f247b.a(fVar.d());
            return gVarB == null ? c(fVar) : a(fVar, gVarB);
        }
        if (this.f247b instanceof F.a) {
        }
        g gVarA = a(j.a(fVar.e()));
        if (gVarA == null) {
            return c(fVar);
        }
        try {
            return c(fVar, gVarA);
        } catch (UnknownHostException e2) {
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    public bX.f c(bX.f fVar) {
        return bX.g.a(fVar, this.f248c, "");
    }

    private bX.f a(bX.f fVar, g gVar) {
        a aVar = new a();
        aVar.a((byte) 1, gVar.b());
        aVar.a((byte) 3, gVar.c());
        bX.f fVarB = bX.g.b(fVar, gVar.a(), gVar.d(), this.f248c, "", aVar.a());
        fVarB.a(this.f248c);
        return fVarB;
    }

    private bX.f b(bX.f fVar, g gVar) {
        a aVar = new a();
        aVar.a((byte) 1, gVar.b());
        aVar.a((byte) 51, gVar.d());
        bX.f fVarA = bX.g.a(fVar, gVar.a(), gVar.d(), this.f248c, "", aVar.a());
        fVarA.a(this.f248c);
        return fVarA;
    }

    private bX.f c(bX.f fVar, g gVar) throws UnknownHostException {
        a aVar = new a();
        aVar.a((byte) 1, gVar.b());
        aVar.a((byte) 3, gVar.c());
        InetAddress byAddress = InetAddress.getByAddress(this.f249d, new byte[]{-64, -88, 99, 1});
        if (this.f248c != null) {
            byAddress = this.f248c;
        }
        bX.f fVarB = bX.g.b(fVar, byAddress, gVar.d(), this.f248c, "", aVar.a());
        fVarB.a(byAddress);
        return fVarB;
    }

    private bX.f d(bX.f fVar, g gVar) throws UnknownHostException {
        InetAddress byAddress = InetAddress.getByAddress(this.f249d, new byte[]{-64, -88, 99, 1});
        if (this.f248c != null) {
            byAddress = this.f248c;
        }
        a aVar = new a();
        aVar.a((byte) 1, gVar.b());
        aVar.a((byte) 3, gVar.c());
        aVar.a((byte) 51, gVar.d());
        aVar.a((byte) 54, byAddress);
        aVar.a((byte) 54, InetAddress.getLoopbackAddress());
        bX.f fVarA = bX.g.a(fVar, byAddress, gVar.d(), byAddress, "", aVar.a());
        fVarA.a(byAddress);
        fVarA.a(InetAddress.getLoopbackAddress());
        fVarA.a((byte) 54, byAddress);
        return fVarA;
    }

    private g a(String str) {
        g gVar = new g();
        gVar.d(this.f247b.d());
        gVar.c(this.f247b.b());
        gVar.a(str);
        gVar.b(this.f247b.a());
        gVar.a(this.f247b.c());
        return gVar;
    }

    public bX.f d(bX.f fVar) {
        f246a.info("Receive Inform from " + fVar.d());
        return null;
    }

    public bX.f e(bX.f fVar) {
        f246a.info("Receive Decline from " + fVar.d());
        return null;
    }

    public bX.f f(bX.f fVar) {
        f246a.info("Receive Release from " + fVar.d() + " Request IP " + fVar.e().toString());
        this.f247b.c(fVar.d());
        return null;
    }

    public InetAddress a() {
        return this.f248c;
    }

    public void a(InetAddress inetAddress) {
        this.f248c = inetAddress;
    }

    public h b() {
        return this.f247b;
    }
}
