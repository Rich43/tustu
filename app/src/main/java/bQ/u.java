package bQ;

import G.C0113cs;
import G.C0130m;
import G.C0140w;
import G.InterfaceC0107cm;
import G.R;
import G.T;
import G.aB;
import G.aG;
import G.aH;
import G.bS;
import bH.C;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:bQ/u.class */
public class u implements aG, InterfaceC0107cm {

    /* renamed from: g, reason: collision with root package name */
    private bN.k f7478g;

    /* renamed from: h, reason: collision with root package name */
    private bO.a f7479h;

    /* renamed from: k, reason: collision with root package name */
    private final String f7483k;

    /* renamed from: a, reason: collision with root package name */
    R f7477a = null;

    /* renamed from: i, reason: collision with root package name */
    private bO.c f7480i = null;

    /* renamed from: b, reason: collision with root package name */
    int f7481b = 0;

    /* renamed from: j, reason: collision with root package name */
    private bO.c f7482j = null;

    /* renamed from: c, reason: collision with root package name */
    Map f7484c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    final List f7485d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    x f7486e = null;

    /* renamed from: f, reason: collision with root package name */
    boolean f7487f = false;

    public u(String str, bN.k kVar, bO.a aVar) {
        this.f7478g = null;
        this.f7479h = null;
        this.f7483k = str;
        this.f7478g = kVar;
        this.f7479h = aVar;
    }

    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        if (this.f7477a == null && str.equals(this.f7483k)) {
            this.f7477a = T.a().c(str);
        }
        if (this.f7485d != null) {
        }
        e();
        return true;
    }

    private void e() {
        if (this.f7486e == null) {
            this.f7486e = new x(this);
            this.f7486e.start();
        }
        if (this.f7477a == null || !this.f7477a.R()) {
            return;
        }
        this.f7486e.a();
    }

    @Override // G.aG
    public void a(String str) {
    }

    @Override // G.InterfaceC0107cm
    public void a(List list) {
        this.f7485d.clear();
        if (list != null) {
            this.f7485d.addAll(list);
        }
        if (list != null && this.f7477a != null && this.f7477a.R()) {
            e();
        } else {
            if (this.f7487f) {
                return;
            }
            this.f7487f = true;
            try {
                C0113cs.a().h(this.f7483k);
            } finally {
                this.f7487f = false;
            }
        }
    }

    private List b(List list) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            int iA = this.f7479h.c().a();
            int iA2 = this.f7480i.a();
            int iC = this.f7480i.c();
            bO.k kVar = new bO.k();
            arrayList.add(kVar);
            bO.l lVar = null;
            int iB = 0;
            ArrayList<aH> arrayList2 = new ArrayList();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                arrayList2.add(a(((C0140w) it.next()).a()));
            }
            Collections.sort(arrayList2, new v(this));
            boolean z2 = false;
            int i2 = 0;
            for (aH aHVar : arrayList2) {
                if (aHVar != null) {
                    if (lVar == null || (aHVar.x() + aHVar.l()) - lVar.a() > iA) {
                        lVar = new bO.l();
                        lVar.a(aHVar.x());
                        lVar.a(aHVar.l());
                        iB += lVar.b();
                        if (iB >= this.f7478g.j() - (arrayList.size() == 1 ? this.f7478g.a() + 1 : 1) || kVar.size() >= iC) {
                            if (arrayList.size() == iA2) {
                                if (!z2) {
                                    C.b("ODT Full! Cannot capture all channels!");
                                }
                                z2 = true;
                            } else {
                                kVar = new bO.k();
                                arrayList.add(kVar);
                                iB = 0;
                            }
                        }
                        if (z2) {
                            C.b("ODT Full, skipping channel: " + aHVar.aJ());
                            i2++;
                        } else {
                            bO.i iVar = new bO.i();
                            iVar.a(aHVar.a());
                            iVar.b(aHVar.l());
                            iVar.c((int) (aHVar.x() - lVar.a()));
                            lVar.a(iVar);
                            kVar.add(lVar);
                        }
                    } else {
                        long jMax = Math.max(lVar.a() + lVar.b(), aHVar.x() + aHVar.l());
                        lVar.a(Math.min(lVar.a(), aHVar.x()));
                        int iB2 = iB - lVar.b();
                        lVar.a((int) (jMax - lVar.a()));
                        iB = iB2 + lVar.b();
                        bO.i iVar2 = new bO.i();
                        iVar2.a(aHVar.a());
                        iVar2.b(aHVar.l());
                        iVar2.c((int) (aHVar.x() - lVar.a()));
                        lVar.a(iVar2);
                    }
                }
            }
            c.a().a(this.f7479h, this.f7485d, arrayList2, arrayList, iA, iA2, iC);
            if (z2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Max ODTs: ").append(iA2).append(", Max ODT Entries: ").append(iC).append(", Max Entry Size: ").append(iA).append("\n");
                sb.append(c.b(arrayList2)).append("\n");
                sb.append(c.c(arrayList)).append("\n");
                C.c(sb.toString());
                aB.a().b(this.f7483k, "Exceeding MAX ODT's \n" + i2 + " channels will not be updated.\nSee Log for channels overflow channels");
            }
        }
        return arrayList;
    }

    private aH a(int i2) {
        aH aHVar = (aH) this.f7484c.get(Integer.valueOf(i2));
        if (aHVar == null) {
            Iterator itQ = this.f7477a.K().q();
            while (true) {
                if (!itQ.hasNext()) {
                    break;
                }
                aH aHVar2 = (aH) itQ.next();
                if (!aHVar2.b().equals("formula") && aHVar2.a() == i2) {
                    aHVar = aHVar2;
                    this.f7484c.put(Integer.valueOf(i2), aHVar2);
                    break;
                }
            }
        }
        if (aHVar == null) {
            C.b("No Channel found for offset: " + i2 + ", cannot create ODT Entry");
        }
        return aHVar;
    }

    public void a(bO.c cVar) {
        this.f7480i = cVar;
    }

    public void b(bO.c cVar) {
        this.f7482j = cVar;
    }

    public String a() {
        return this.f7483k;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() throws IOException {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f7485d);
        List listB = b(arrayList);
        C0130m c0130mC = C0130m.c(this.f7477a.O(), this.f7481b);
        c0130mC.b(new w(this));
        this.f7477a.C().b(c0130mC);
        this.f7480i.a(listB);
        C0130m c0130mB = C0130m.b(this.f7477a.O(), this.f7481b);
        C.c("XcpOdtEntryManager:: updated DAQ List: " + this.f7480i.toString());
        this.f7477a.C().b(c0130mB);
    }

    public void b() {
        this.f7486e.a();
    }

    public void c() {
        try {
            f();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.f7486e.f7490a = -1L;
    }

    public boolean d() {
        return this.f7486e.f7490a > 0;
    }
}
