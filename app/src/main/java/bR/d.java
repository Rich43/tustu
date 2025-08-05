package bR;

import G.F;
import bH.C0995c;
import bN.t;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bR/d.class */
public class d implements bQ.i {

    /* renamed from: a, reason: collision with root package name */
    List f7510a = new ArrayList();

    public int a() {
        return 253;
    }

    @Override // bQ.i
    public void a(F f2, bN.k kVar, t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:EVENT_PACKET Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:EVENT_PACKET valid packet data not found.");
        }
        if (bArrC.length < 1) {
            throw new bT.h("PID:EVENT_PACKET invalid packet data length, expected at least 2 byte, found: " + (bArrC.length + 1));
        }
        a(C0995c.a(bArrC[0]), new byte[bArrC.length - 1]);
    }

    private void a(int i2, byte[] bArr) {
        Iterator it = this.f7510a.iterator();
        while (it.hasNext()) {
            ((e) it.next()).a(i2, bArr);
        }
    }

    public void a(e eVar) {
        this.f7510a.add(eVar);
    }

    public void b(e eVar) {
        this.f7510a.remove(eVar);
    }
}
