package bR;

import G.F;
import bH.C;
import bH.C0995c;
import bN.t;

/* loaded from: TunerStudioMS.jar:bR/n.class */
public class n implements bQ.i {

    /* renamed from: a, reason: collision with root package name */
    k f7533a = new k();

    /* renamed from: b, reason: collision with root package name */
    f f7534b = new f();

    /* renamed from: c, reason: collision with root package name */
    g f7535c = new g();

    /* renamed from: d, reason: collision with root package name */
    h f7536d = new h();

    @Override // bQ.i
    public void a(F f2, bN.k kVar, t tVar) throws bT.h {
        if (tVar.a() != 252) {
            throw new bT.h("Invalid Service Request Packet: " + tVar.toString());
        }
        byte[] bArrC = tVar.c();
        if (bArrC.length < 1) {
            throw new bT.h("Invalid Service Request Code: " + tVar.toString());
        }
        int iA = C0995c.a(bArrC[0]);
        if (iA == 224) {
            this.f7533a.a(f2, kVar, tVar);
            return;
        }
        if (iA == 227) {
            this.f7536d.a(f2, kVar, tVar);
            return;
        }
        if (iA == 226) {
            this.f7535c.a(f2);
            return;
        }
        if (iA != 225) {
            C.b("No Response Processor for Packet: " + tVar.toString());
            return;
        }
        if (bArrC.length <= 1) {
            C.b("Invalid SERV Online message, should have a state byte: " + C0995c.d(bArrC));
        } else if (bArrC[1] == 0) {
            this.f7534b.b();
        } else {
            this.f7534b.a();
        }
    }
}
