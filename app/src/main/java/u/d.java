package u;

import G.C0072be;
import G.C0079bl;
import G.C0083bp;
import G.C0088bu;
import G.C0143z;
import G.R;
import G.aM;
import G.bL;
import bH.ab;
import bt.C1303al;
import bt.C1324bf;
import bt.C1337bs;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.efiAnalytics.tuningwidgets.panels.W;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1640dy;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MenuContainer;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:u/d.class */
public class d implements InterfaceC1565bc, InterfaceC1640dy {

    /* renamed from: a, reason: collision with root package name */
    HashMap f13983a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    HashMap f13984b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f13985c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    int f13986d = 0;

    /* renamed from: e, reason: collision with root package name */
    R f13987e;

    /* renamed from: f, reason: collision with root package name */
    R f13988f;

    public d(R r2, R r3, ArrayList arrayList) {
        this.f13987e = r2;
        this.f13988f = r3;
        C0143z c0143z = new C0143z(ab.a().b());
        ArrayList arrayListA = c0143z.a(r2, arrayList);
        ArrayList arrayListB = c0143z.b(r2, arrayListA);
        int i2 = 0;
        int i3 = 0;
        ArrayList arrayList2 = null;
        int iB = 0;
        for (int i4 = 0; i4 < arrayListB.size(); i4++) {
            C0088bu c0088bu = (C0088bu) arrayListB.get(i4);
            if ((c0088bu instanceof C0072be) || (c0088bu.Z() == 1 && (c0088bu.j(0) instanceof C0072be))) {
                aM aMVarC = r2.c((c0088bu instanceof C0072be ? (C0072be) c0088bu : (C0072be) c0088bu.j(0)).a());
                if (aMVarC != null && aMVarC.b() > iB) {
                    iB = aMVarC.b();
                    if (i4 > 0) {
                        C0088bu c0088bu2 = (C0088bu) arrayListB.get(0);
                        arrayListB.set(0, c0088bu);
                        arrayListB.set(i4, c0088bu2);
                    }
                }
            }
        }
        if (arrayListB.size() > 0) {
            for (int i5 = 0; i5 < arrayListB.size(); i5++) {
                if (i5 % 2 == 0) {
                    arrayList2 = new ArrayList();
                    int i6 = i3;
                    i3++;
                    this.f13984b.put(Integer.valueOf(i6), arrayList2);
                }
                arrayList2.add((C0088bu) arrayListB.get(i5));
                i2++;
            }
        } else if (arrayListA.size() > 0) {
            for (int i7 = 0; i7 < arrayListA.size(); i7++) {
                if (i7 % 2 == 0) {
                    arrayList2 = new ArrayList();
                    int i8 = i3;
                    i3++;
                    this.f13984b.put(Integer.valueOf(i8), arrayList2);
                }
                arrayList2.add((C0088bu) arrayListA.get(i7));
                i2++;
            }
        } else {
            C0088bu c0088bu3 = new C0088bu();
            c0088bu3.i(1);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                aM aMVar = (aM) it.next();
                if (!aMVar.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                    C0083bp c0083bp = new C0083bp();
                    c0083bp.a(aMVar.aJ());
                    c0083bp.e(aMVar.aJ() + " raw value:");
                    c0088bu3.a(c0083bp);
                    i2++;
                } else if (aMVar.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                    C0083bp c0083bp2 = new C0083bp();
                    c0083bp2.e(aMVar.aJ() + " values are different.");
                    c0088bu3.a(c0083bp2);
                    i2++;
                }
            }
            ArrayList arrayList3 = new ArrayList();
            arrayList3.add(c0088bu3);
            this.f13984b.put(0, arrayList3);
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            this.f13985c.add(((aM) it2.next()).aJ());
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1640dy
    public int a() {
        return this.f13984b.size();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1640dy
    public boolean b() {
        return this.f13986d < a() - 1;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1640dy
    public boolean c() {
        return this.f13986d > 0;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1640dy
    public Component a(int i2) {
        e eVar = (e) this.f13983a.get(Integer.valueOf(i2));
        if (eVar == null) {
            eVar = new e(this);
            eVar.setLayout(new GridLayout(0, 2));
            C0088bu c0088bu = new C0088bu();
            c0088bu.i(1);
            C0088bu c0088bu2 = new C0088bu();
            c0088bu2.i(1);
            List<C0088bu> list = (List) this.f13984b.get(Integer.valueOf(i2));
            for (int i3 = 0; i3 < list.size(); i3++) {
                C0088bu c0088bu3 = (C0088bu) list.get(i3);
                String strC = bL.c(this.f13987e, c0088bu3.aJ());
                c0088bu3.s(strC);
                if (!W.a(this.f13987e, c0088bu3, bV.c())) {
                    C0088bu c0088bu4 = new C0088bu();
                    C0083bp c0083bp = new C0083bp();
                    c0083bp.e(" ");
                    c0088bu4.a(c0083bp);
                    C0083bp c0083bp2 = new C0083bp();
                    c0083bp2.e(strC);
                    c0088bu4.a(c0083bp2);
                    C0083bp c0083bp3 = new C0083bp();
                    c0083bp3.e(strC + " is Password Protected.");
                    c0083bp3.b(true);
                    c0088bu4.a(c0083bp3);
                    C0083bp c0083bp4 = new C0083bp();
                    c0083bp4.e("To view, click Exit & Go offline, then when you go back online enter the correct password.");
                    c0088bu4.a(c0083bp4);
                    list.set(i3, c0088bu4);
                }
            }
            for (C0088bu c0088bu5 : list) {
                if ((c0088bu5 instanceof C0072be) || (c0088bu5 instanceof C0079bl)) {
                    C0088bu c0088bu6 = new C0088bu();
                    c0088bu6.i(2);
                    try {
                        c0088bu5.t(BorderLayout.CENTER);
                    } catch (V.g e2) {
                        Logger.getLogger(d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                    c0088bu6.s(bL.c(this.f13987e, c0088bu5.aJ()));
                    c0088bu6.a(c0088bu5);
                    c0088bu5 = c0088bu6;
                }
                c0088bu.a(c0088bu5);
                c0088bu2.a(c0088bu5);
            }
            C1324bf c1324bf = new C1324bf(this.f13987e, c0088bu, false);
            eVar.add(c1324bf);
            C1324bf c1324bf2 = new C1324bf(this.f13988f, c0088bu2, false);
            eVar.add(c1324bf2);
            ArrayList arrayListA = a(c1324bf);
            ArrayList arrayListA2 = a(c1324bf2);
            Iterator it = arrayListA.iterator();
            Iterator it2 = arrayListA2.iterator();
            while (it2.hasNext()) {
                ((C1337bs) it2.next()).a(((C1337bs) it.next()).g());
            }
            ArrayList arrayListB = b(c1324bf);
            ArrayList arrayListB2 = b(c1324bf2);
            Iterator it3 = arrayListB.iterator();
            int i4 = (Toolkit.getDefaultToolkit().getScreenSize().width * 4) / 10;
            Iterator it4 = arrayListB2.iterator();
            while (it4.hasNext()) {
                C1303al c1303al = (C1303al) it3.next();
                C1303al c1303al2 = (C1303al) it4.next();
                Dimension preferredSize = c1303al2.getPreferredSize();
                if (preferredSize.width > i4) {
                    preferredSize.width = i4;
                    c1303al2.setPreferredSize(preferredSize);
                }
                Dimension preferredSize2 = c1303al.getPreferredSize();
                if (preferredSize2.width > i4) {
                    preferredSize2.width = i4;
                    c1303al.setPreferredSize(preferredSize2);
                }
                Dimension minimumSize = c1303al2.getMinimumSize();
                if (minimumSize.width > i4) {
                    minimumSize.width = i4;
                    c1303al2.setMinimumSize(minimumSize);
                }
                Dimension minimumSize2 = c1303al.getMinimumSize();
                if (minimumSize2.width > i4) {
                    minimumSize2.width = i4;
                    c1303al.setMinimumSize(minimumSize2);
                }
                c1303al2.a(c1303al.o());
                if (!c1303al2.k()) {
                    c1303al2.a(true);
                    c1303al.a(true);
                }
            }
            c1324bf.a();
            c1324bf2.a();
            c1324bf.a(this.f13985c);
            c1324bf2.a(this.f13985c);
            this.f13983a.put(Integer.valueOf(i2), eVar);
        }
        this.f13986d = i2;
        return eVar;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1640dy
    public int d() {
        return this.f13986d;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1640dy
    public void a(Component component) {
    }

    @Override // com.efiAnalytics.ui.InterfaceC1640dy
    public void b(Component component) {
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        for (MenuContainer menuContainer : this.f13983a.values()) {
            if (menuContainer instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) menuContainer).close();
            }
        }
    }

    private ArrayList a(C1324bf c1324bf) {
        return a(new ArrayList(), c1324bf);
    }

    private ArrayList b(C1324bf c1324bf) {
        return b(new ArrayList(), c1324bf);
    }

    private ArrayList a(ArrayList arrayList, C1324bf c1324bf) {
        for (int i2 = 0; i2 < c1324bf.getComponentCount(); i2++) {
            if (c1324bf.getComponent(i2) instanceof C1337bs) {
                arrayList.add((C1337bs) c1324bf.getComponent(i2));
            } else if (c1324bf.getComponent(i2) instanceof C1324bf) {
                a(arrayList, (C1324bf) c1324bf.getComponent(i2));
            }
        }
        return arrayList;
    }

    private ArrayList b(ArrayList arrayList, C1324bf c1324bf) {
        for (int i2 = 0; i2 < c1324bf.getComponentCount(); i2++) {
            if (c1324bf.getComponent(i2) instanceof C1303al) {
                arrayList.add((C1303al) c1324bf.getComponent(i2));
            } else if (c1324bf.getComponent(i2) instanceof C1324bf) {
                b(arrayList, (C1324bf) c1324bf.getComponent(i2));
            }
        }
        return arrayList;
    }
}
