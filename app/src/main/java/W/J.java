package W;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:W/J.class */
public class J {

    /* renamed from: a, reason: collision with root package name */
    private ArrayList f1928a = null;

    /* renamed from: b, reason: collision with root package name */
    private ArrayList f1929b = null;

    /* renamed from: c, reason: collision with root package name */
    private HashMap f1930c = null;

    /* renamed from: d, reason: collision with root package name */
    private ArrayList f1931d = null;

    /* renamed from: e, reason: collision with root package name */
    private K f1932e = new L(this);

    /* renamed from: f, reason: collision with root package name */
    private File f1933f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f1934g = null;

    private void e() {
        this.f1928a = new ArrayList();
        this.f1929b = new ArrayList();
        this.f1930c = new HashMap();
        this.f1931d = new ArrayList();
    }

    public void a(File file) {
        a(file, false);
    }

    public void a(File file, boolean z2) {
        e();
        if (!file.exists()) {
            System.out.println("Can not find File:" + file.getAbsolutePath());
        }
        this.f1933f = file;
        BufferedReader bufferedReaderA = this.f1932e.a(file);
        int i2 = 0;
        try {
            this.f1929b.add("FILE_HEADER");
            while (true) {
                String line = bufferedReaderA.readLine();
                if (line == null) {
                    break;
                }
                i2++;
                M m2 = new M(file.getName());
                m2.a(line, i2);
                if (line.startsWith("[") && line.indexOf("]") > 0) {
                    String strTrim = bH.W.b(bH.W.b(line, "[", ""), "]", "").trim();
                    if (this.f1934g != null && strTrim.equals(this.f1934g)) {
                        break;
                    } else {
                        this.f1929b.add(strTrim);
                    }
                } else if (line.startsWith("#include")) {
                    this.f1931d.add(bH.W.b(bH.W.b(line, "#include", ""), PdfOps.DOUBLE_QUOTE__TOKEN, "").trim());
                }
                if (z2 || !m2.d()) {
                    this.f1928a.add(m2);
                }
            }
        } finally {
            try {
                bufferedReaderA.close();
            } catch (Exception e2) {
            }
        }
    }

    public boolean a(String str) {
        return this.f1929b.contains(str);
    }

    public N b(String str) {
        N n2 = (N) this.f1930c.get(str);
        if (n2 != null && n2.size() > 0) {
            return n2;
        }
        if (this.f1930c.isEmpty()) {
            if (this.f1930c.get("FILE_HEADER") == null) {
                n2 = new N();
                this.f1930c.put("FILE_HEADER", n2);
                n2.a("FILE_HEADER");
            }
            Iterator it = a().iterator();
            while (it.hasNext()) {
                M m2 = (M) it.next();
                if (m2.b().startsWith("[") && m2.b().indexOf("]") > 0) {
                    String strB = bH.W.b(bH.W.b(m2.b(), "[", ""), "]", "");
                    if (this.f1930c.get(strB) == null) {
                        n2 = new N();
                        this.f1930c.put(strB, n2);
                        n2.a(strB);
                    }
                } else if (n2 != null) {
                    n2.add(m2);
                }
            }
        }
        N n3 = (N) this.f1930c.get(str);
        if (n3 == null) {
            n3 = new N();
            n3.a(str);
            this.f1930c.put(str, n3);
        }
        return n3;
    }

    public M a(String str, String str2) {
        N nB = b(str);
        if (nB == null) {
            return null;
        }
        return nB.b(str2);
    }

    public ArrayList a() {
        return this.f1928a;
    }

    public void a(ArrayList arrayList) {
        this.f1928a = arrayList;
        this.f1930c.clear();
    }

    public Iterator b() {
        return this.f1929b.iterator();
    }

    public Iterator c() {
        return this.f1931d.iterator();
    }

    protected void a(String str, M m2) {
        boolean z2 = false;
        for (int i2 = 0; i2 < this.f1928a.size(); i2++) {
            M m3 = (M) this.f1928a.get(i2);
            if (m3.b().equals("[" + str + "]")) {
                z2 = true;
            } else if (z2 && (m3.b().startsWith("[") || i2 == this.f1928a.size() - 1)) {
                this.f1928a.add(i2, m2);
                return;
            }
        }
    }

    public void a(K k2) {
        this.f1932e = k2;
    }

    public void c(String str) {
        this.f1934g = str;
    }

    public File d() {
        return this.f1933f;
    }
}
