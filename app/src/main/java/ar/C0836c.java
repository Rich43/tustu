package ar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* renamed from: ar.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ar/c.class */
public class C0836c {

    /* renamed from: b, reason: collision with root package name */
    private String f6217b;

    /* renamed from: c, reason: collision with root package name */
    private List f6218c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private Map f6219d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    private int f6220e = -1;

    /* renamed from: f, reason: collision with root package name */
    private boolean f6221f = false;

    /* renamed from: a, reason: collision with root package name */
    public static String f6222a = " ";

    public C0836c(String str) {
        this.f6217b = str;
    }

    public String a() {
        String strSubstring = "<html>";
        Object obj = "";
        int i2 = 1;
        for (int i3 = 0; i3 < c().size(); i3++) {
            Object objA = ((C0837d) c().get(i3)).a();
            if (objA != null && objA.contains(".")) {
                objA = objA.substring(0, objA.lastIndexOf("."));
            }
            if (!objA.equals(obj)) {
                int i4 = i2;
                i2++;
                strSubstring = strSubstring + "Graph " + i4 + " --------<BR>";
                obj = objA;
            }
            String strB = ((C0837d) c().get(i3)).b();
            if (strB.indexOf("Field.") != -1) {
                strB = h.g.a().a(strB);
            }
            if (!strB.trim().isEmpty()) {
                strSubstring = strSubstring + "&nbsp;&nbsp;&nbsp;&nbsp;" + Z.b.a().b(strB) + "<br>";
            }
        }
        if (strSubstring.trim().endsWith("<br>")) {
            strSubstring = strSubstring.trim().substring(0, strSubstring.lastIndexOf("<br>"));
        }
        return strSubstring;
    }

    public String b() {
        return this.f6217b;
    }

    public void a(String str) {
        this.f6217b = str;
    }

    public List c() {
        return this.f6218c;
    }

    public void a(C0837d c0837d) {
        C0837d c0837d2 = (C0837d) this.f6219d.put(c0837d.a(), c0837d);
        if (c0837d2 != null) {
            this.f6218c.remove(c0837d2);
        }
        this.f6218c.add(c0837d);
    }

    public C0837d b(String str) {
        C0837d c0837d = (C0837d) this.f6219d.remove(str);
        if (c0837d != null) {
            this.f6218c.remove(c0837d);
        }
        return c0837d;
    }

    public void d() {
        this.f6218c.clear();
        this.f6219d.clear();
    }

    public boolean a(C0836c c0836c) {
        return c0836c.a().equals(a());
    }

    public String c(String str) {
        C0837d c0837d = (C0837d) this.f6219d.get(str);
        return c0837d == null ? f6222a : c0837d.b();
    }

    public int e() {
        return this.f6220e;
    }

    public void a(int i2) {
        this.f6220e = i2;
    }

    public boolean f() {
        return this.f6221f;
    }

    public void a(boolean z2) {
        this.f6221f = z2;
    }
}
