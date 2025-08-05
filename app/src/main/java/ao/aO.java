package ao;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:ao/aO.class */
public class aO implements InterfaceC0813k {

    /* renamed from: f, reason: collision with root package name */
    private static aO f5131f = null;

    /* renamed from: a, reason: collision with root package name */
    int f5128a = h.i.a("numberOfGraphs", h.i.f12273t);

    /* renamed from: b, reason: collision with root package name */
    int f5129b = h.i.a("numberOfOverlays", h.i.f12274u);

    /* renamed from: c, reason: collision with root package name */
    boolean f5130c = h.i.a("fieldSelectionStyle", "standardSelection").equals("selectFromDash");

    /* renamed from: g, reason: collision with root package name */
    private ArrayList f5132g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    private List f5133h = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    String[] f5134d = {"white", "black", "dark_blue", "blue", "yellow", "dark_green", "green", "red", "dark_red", "dark_gray", "gray", "lightGray", "cyan", "magenta", "pink", "orange"};

    /* renamed from: e, reason: collision with root package name */
    Color[] f5135e = {Color.white, Color.black, Color.blue.darker(), Color.blue, Color.yellow, Color.green.darker(), Color.green, Color.red, Color.red.darker(), Color.darkGray, Color.gray, Color.lightGray, Color.cyan, Color.magenta, Color.pink, Color.orange};

    private aO() {
        for (int i2 = 0; i2 < 8; i2++) {
            this.f5132g.add(Color.cyan);
            this.f5132g.add(Color.yellow);
            this.f5132g.add(Color.white);
            this.f5132g.add(Color.magenta);
            this.f5132g.add(Color.green);
            this.f5132g.add(Color.red);
            this.f5132g.add(Color.orange);
            this.f5132g.add(Color.pink);
            this.f5132g.add(Color.blue);
            this.f5132g.add(Color.green.darker());
            this.f5132g.add(Color.red.darker());
            this.f5132g.add(Color.cyan.darker().darker());
            this.f5132g.add(Color.pink.darker());
            this.f5132g.add(Color.orange.darker());
            this.f5132g.add(Color.blue.darker());
            this.f5132g.add(Color.lightGray);
        }
    }

    public void a(aN aNVar) {
        this.f5133h.add(aNVar);
    }

    public boolean b(aN aNVar) {
        return this.f5133h.remove(aNVar);
    }

    private void d() {
        Iterator it = this.f5133h.iterator();
        while (it.hasNext()) {
            ((aN) it.next()).a();
        }
    }

    public static aO a() {
        if (f5131f == null) {
            f5131f = new aO();
        }
        return f5131f;
    }

    public String[] b() {
        return this.f5134d;
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color, int i2) {
        if (i2 == this.f5132g.size()) {
            this.f5132g.add(color);
        } else {
            this.f5132g.ensureCapacity(i2 + 1);
            this.f5132g.set(i2, color);
        }
        h.i.c("graphForeColor" + i2, b(color));
        d();
    }

    private String b(Color color) {
        String str = null;
        int i2 = 0;
        while (true) {
            if (i2 >= this.f5135e.length) {
                break;
            }
            if (this.f5135e[i2].equals(color)) {
                str = this.f5134d[i2];
                break;
            }
            i2++;
        }
        if (str == null) {
            bH.C.b("No Color Match Found for: " + color.getRGB());
            str = "cyan";
        }
        return str;
    }

    public Color a(int i2) {
        return i2 < this.f5132g.size() ? (Color) this.f5132g.get(i2) : (Color) this.f5132g.get(i2 % this.f5132g.size());
    }

    public void a(Color color, int i2, int i3) {
        a(color, (i2 * this.f5129b) + i3);
        d();
    }

    public Color a(int i2, int i3) {
        int i4 = i3;
        if (!h.i.a(h.i.f12306aa, h.i.f12307ab)) {
            i4 += i2 * this.f5129b;
        }
        return a(i4);
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color) {
    }

    public ArrayList c() {
        return this.f5132g;
    }
}
