package bR;

import G.C0130m;
import G.F;
import G.R;
import G.T;
import bH.C0995c;
import bN.t;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bR/h.class */
public class h {

    /* renamed from: a, reason: collision with root package name */
    i f7511a = null;

    /* renamed from: b, reason: collision with root package name */
    final List f7512b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    boolean f7513c = true;

    public void a(F f2, bN.k kVar, t tVar) {
        R rC = T.a().c(f2.u());
        if (rC == null) {
            throw new bT.h("Invalid EcuConfiguration: " + f2.u());
        }
        if (tVar.a() != 252) {
            throw new bT.h("Invalid Service Request Packet: " + tVar.toString());
        }
        byte[] bArrC = tVar.c();
        if (C0995c.a(bArrC[0]) != 227) {
            throw new bT.h("Invalid packet for Refresh PC Var: " + tVar.toString());
        }
        if (bArrC.length < 2) {
            throw new bT.h("COMMAND Required: " + tVar.toString());
        }
        int iA = C0995c.a(bArrC[1]);
        if (iA == bV.g.f7642b) {
            a(rC);
        } else {
            if (iA != bV.g.f7641a) {
                throw new bT.h("Invalid COMMAND: " + tVar.toString());
            }
            String str = new String(new byte[bArrC.length - 2]);
            if (str.trim().isEmpty()) {
                throw new bT.h("PcVariableName Required when not read all: " + tVar.toString());
            }
            a(rC, str);
        }
    }

    private void a(R r2) {
        Iterator it = this.f7512b.iterator();
        while (it.hasNext()) {
            if (((j) it.next()).f7519c == null) {
                a().a();
                return;
            }
        }
        synchronized (this.f7512b) {
            this.f7512b.clear();
        }
        a().a(new j(this, r2));
    }

    private void a(R r2, String str) {
        for (j jVar : this.f7512b) {
            if (jVar.a().equals(r2) && (jVar.b() == null || jVar.b().equals(str))) {
                a().a();
                return;
            }
        }
        a().a(new j(this, r2, str));
    }

    private i a() {
        if (this.f7511a == null) {
            this.f7511a = new i(this);
            this.f7511a.start();
        }
        return this.f7511a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(R r2) throws IOException {
        r2.C().b(C0130m.a(r2.O()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(R r2, String str) throws IOException {
        r2.C().b(C0130m.a(r2.O(), str));
    }
}
