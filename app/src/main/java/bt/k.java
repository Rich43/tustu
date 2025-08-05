package bT;

import bU.A;
import bU.B;
import bU.C;
import bU.C1019a;
import bU.C1020b;
import bU.C1021c;
import bU.C1022d;
import bU.D;
import bU.s;
import bU.t;
import bU.u;
import bU.v;
import bU.w;
import bU.x;
import bU.y;
import bU.z;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.net.telnet.TelnetCommand;

/* loaded from: TunerStudioMS.jar:bT/k.class */
public class k {

    /* renamed from: a, reason: collision with root package name */
    private final Map f7592a = new HashMap();

    protected k() {
        a();
    }

    private void a() {
        this.f7592a.put(255, new C1021c());
        this.f7592a.put(254, new C1022d());
        this.f7592a.put(251, new bU.j());
        this.f7592a.put(240, new bU.e());
        this.f7592a.put(239, new bU.g());
        this.f7592a.put(237, new bU.h());
        this.f7592a.put(238, new bU.f());
        this.f7592a.put(250, new bU.p());
        this.f7592a.put(253, new bU.q());
        this.f7592a.put(246, new u());
        this.f7592a.put(252, new z());
        this.f7592a.put(245, new A());
        this.f7592a.put(244, new B());
        this.f7592a.put(243, new C1019a());
        this.f7592a.put(235, new bU.r());
        this.f7592a.put(234, new bU.i());
        this.f7592a.put(230, new w());
        this.f7592a.put(Integer.valueOf(TelnetCommand.GA), new v());
        this.f7592a.put(218, new bU.n());
        this.f7592a.put(217, new bU.o());
        this.f7592a.put(226, new t());
        this.f7592a.put(215, new bU.k());
        this.f7592a.put(216, new bU.l());
        this.f7592a.put(225, new D());
        this.f7592a.put(224, new s());
        this.f7592a.put(223, new bU.m());
        this.f7592a.put(221, new y());
        this.f7592a.put(222, new x());
        this.f7592a.put(227, new C1020b());
        this.f7592a.put(241, new C());
    }

    public a a(int i2) {
        return (a) this.f7592a.get(Integer.valueOf(i2));
    }
}
