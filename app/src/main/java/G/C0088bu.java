package G;

import java.awt.BorderLayout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;

/* renamed from: G.bu, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bu.class */
public class C0088bu extends Q implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private cZ f1007a = new C0094c("");

    /* renamed from: f, reason: collision with root package name */
    private ArrayList f1008f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private String f1009g = "yAxis";

    /* renamed from: h, reason: collision with root package name */
    private int f1010h = 1;

    /* renamed from: i, reason: collision with root package name */
    private boolean f1011i = false;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f1012b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f1013c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    ArrayList f1014d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    ArrayList f1015e = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    private String f1016j = null;

    /* renamed from: k, reason: collision with root package name */
    private boolean f1017k = false;

    /* renamed from: l, reason: collision with root package name */
    private String f1018l = null;

    /* renamed from: m, reason: collision with root package name */
    private String f1019m = null;

    public void a(bH bHVar) {
        this.f1015e.add(bHVar);
    }

    public void b(bH bHVar) {
        this.f1015e.remove(bHVar);
    }

    public void a(AbstractC0093bz abstractC0093bz) {
        this.f1013c.add(abstractC0093bz);
    }

    public Iterator F() {
        return this.f1013c.iterator();
    }

    public List G() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f1013c);
        Iterator it = this.f1014d.iterator();
        while (it.hasNext()) {
            arrayList.addAll(((C0088bu) it.next()).G());
        }
        return arrayList;
    }

    public int H() {
        return this.f1013c.size();
    }

    public void a(aA aAVar) {
        this.f1012b.add(aAVar);
    }

    public Iterator I() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f1014d.iterator();
        while (it.hasNext()) {
            Iterator itI = ((C0088bu) it.next()).I();
            while (itI.hasNext()) {
                arrayList.add(itI.next());
            }
        }
        Iterator it2 = this.f1012b.iterator();
        while (it2.hasNext()) {
            arrayList.add(it2.next());
        }
        return arrayList.iterator();
    }

    public int J() {
        int iJ = 0;
        Iterator it = this.f1014d.iterator();
        while (it.hasNext()) {
            iJ += ((C0088bu) it.next()).J();
        }
        return iJ + this.f1012b.size();
    }

    public void a(C0088bu c0088bu) {
        this.f1014d.add(c0088bu);
    }

    public Iterator K() {
        return this.f1014d.iterator();
    }

    public boolean L() {
        return !this.f1014d.isEmpty();
    }

    public String M() {
        try {
            return this.f1007a.a();
        } catch (V.g e2) {
            Logger.getLogger(C0088bu.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return "";
        }
    }

    public cZ N() {
        return this.f1007a;
    }

    public void s(String str) {
        this.f1007a = new C0094c(str);
    }

    public void d(cZ cZVar) {
        this.f1007a = cZVar;
    }

    public ArrayList O() {
        ArrayList arrayListP = P();
        ArrayList arrayList = new ArrayList();
        Iterator it = arrayListP.iterator();
        while (it.hasNext()) {
            cZ cZVar = (cZ) it.next();
            try {
                if (!arrayList.contains(cZVar.a())) {
                    arrayList.add(cZVar.a());
                }
            } catch (V.g e2) {
                Logger.getLogger(C0088bu.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return arrayList;
    }

    public ArrayList P() {
        if (this.f1014d.size() <= 0) {
            return this.f1008f;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f1008f);
        Iterator it = this.f1014d.iterator();
        while (it.hasNext()) {
            arrayList.addAll(((C0088bu) it.next()).P());
        }
        return arrayList;
    }

    public void e(cZ cZVar) {
        this.f1008f.add(cZVar);
    }

    public String Q() {
        return this.f1009g;
    }

    public void t(String str) throws V.g {
        if (str == null || str.equals("")) {
            str = "yAxis";
        }
        if (!str.equals(BorderLayout.CENTER) && !str.equals("East") && !str.equals("North") && !str.equals("South") && !str.equals("West") && !str.equals("xAxis") && !str.equals("yAxis") && !str.equals(Action.DEFAULT)) {
            throw new V.g("Unknown placement constraint " + str + ", Valid values: " + BorderLayout.CENTER + ", East, North, South, West, " + Action.DEFAULT);
        }
        this.f1009g = str;
    }

    public int R() {
        return this.f1010h;
    }

    public void i(int i2) {
        this.f1010h = i2;
    }

    public boolean n() {
        if (this.f1011i) {
            return this.f1011i;
        }
        Iterator it = this.f1014d.iterator();
        while (it.hasNext()) {
            if (((C0088bu) it.next()).n()) {
                return true;
            }
        }
        return false;
    }

    public void g(boolean z2) {
        this.f1011i = z2;
    }

    public boolean S() {
        if (this instanceof C0072be) {
            return true;
        }
        Iterator itK = K();
        while (itK.hasNext()) {
            C0088bu c0088bu = (C0088bu) itK.next();
            if ((c0088bu instanceof C0072be) || c0088bu.S()) {
                return true;
            }
        }
        return false;
    }

    public boolean T() {
        if (this instanceof C0079bl) {
            return true;
        }
        Iterator itK = K();
        while (itK.hasNext()) {
            C0088bu c0088bu = (C0088bu) itK.next();
            if ((c0088bu instanceof C0079bl) || c0088bu.T()) {
                return true;
            }
        }
        return false;
    }

    public List e() {
        ArrayList arrayList = new ArrayList();
        if (aJ() == null || !(aJ().equals("std_constants") || aJ().equals("std_injection"))) {
            Iterator it = this.f1013c.iterator();
            while (it.hasNext()) {
                AbstractC0093bz abstractC0093bz = (AbstractC0093bz) it.next();
                if (abstractC0093bz instanceof C0083bp) {
                    C0083bp c0083bp = (C0083bp) abstractC0093bz;
                    if (c0083bp.b() != null && c0083bp.b().length() > 0) {
                        arrayList.add(c0083bp.b());
                    }
                }
            }
            if (this.f1014d.size() > 0) {
                Iterator it2 = this.f1014d.iterator();
                while (it2.hasNext()) {
                    arrayList.addAll(((C0088bu) it2.next()).e());
                }
            }
        } else {
            cW.a(aJ(), arrayList);
        }
        return arrayList;
    }

    public List U() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f1015e);
        Iterator it = this.f1014d.iterator();
        while (it.hasNext()) {
            arrayList.addAll(((C0088bu) it.next()).U());
        }
        return arrayList;
    }

    public String i(String str) {
        String strI = null;
        Iterator it = this.f1013c.iterator();
        while (it.hasNext()) {
            AbstractC0093bz abstractC0093bz = (AbstractC0093bz) it.next();
            if (abstractC0093bz.aJ() != null && abstractC0093bz.aJ().equals(str)) {
                return abstractC0093bz.aH() != null ? abstractC0093bz.aH() : "";
            }
        }
        Iterator it2 = this.f1014d.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            strI = ((C0088bu) it2.next()).i(str);
            if (strI != null) {
                if (aH() != null && aH().length() > 0) {
                    strI = strI + " && (" + aH() + ") ";
                }
            }
        }
        return strI;
    }

    public String V() {
        return this.f1016j;
    }

    public void x(String str) {
        this.f1016j = str;
    }

    public boolean W() {
        return this.f1017k;
    }

    public void h(boolean z2) {
        this.f1017k = z2;
    }

    public String X() {
        if (this.f1018l != null && !this.f1018l.isEmpty()) {
            return this.f1018l;
        }
        if (!L()) {
            return null;
        }
        Iterator it = this.f1014d.iterator();
        while (it.hasNext()) {
            C0088bu c0088bu = (C0088bu) it.next();
            if (c0088bu.X() != null && !c0088bu.X().isEmpty()) {
                return c0088bu.X();
            }
        }
        return null;
    }

    public void y(String str) {
        this.f1018l = str;
    }

    public boolean Y() {
        if (this.f1018l != null && !this.f1018l.isEmpty()) {
            return true;
        }
        if (!L()) {
            return false;
        }
        Iterator it = this.f1014d.iterator();
        while (it.hasNext()) {
            if (((C0088bu) it.next()).Y()) {
                return true;
            }
        }
        return false;
    }

    public int Z() {
        return this.f1014d.size();
    }

    public C0088bu j(int i2) {
        return (C0088bu) this.f1014d.get(i2);
    }

    public String aa() {
        return this.f1019m;
    }

    public void z(String str) {
        this.f1019m = str;
    }

    protected List a(ArrayList arrayList) {
        if (this.f1019m != null && !this.f1019m.isEmpty()) {
            arrayList.add(this.f1019m);
        }
        if (L()) {
            Iterator it = this.f1014d.iterator();
            while (it.hasNext()) {
                ((C0088bu) it.next()).a(arrayList);
            }
        }
        return arrayList;
    }

    public List ab() {
        return a(new ArrayList());
    }
}
