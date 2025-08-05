package ak;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ak/am.class */
public class am extends W.V {

    /* renamed from: a, reason: collision with root package name */
    private BufferedReader f4748a = null;

    /* renamed from: b, reason: collision with root package name */
    private StringBuilder f4749b = null;

    /* renamed from: e, reason: collision with root package name */
    private List f4750e = null;

    /* renamed from: f, reason: collision with root package name */
    private List f4751f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f4752g = null;

    /* renamed from: h, reason: collision with root package name */
    private int f4753h = 0;

    /* renamed from: i, reason: collision with root package name */
    private float[] f4754i = null;

    /* renamed from: j, reason: collision with root package name */
    private final HashMap f4755j = new HashMap();

    @Override // W.V
    public boolean a(String str) throws V.a {
        this.f4749b = new StringBuilder();
        this.f4750e = new ArrayList();
        this.f4751f = new ArrayList();
        try {
            this.f4748a = new BufferedReader(new InputStreamReader(new FileInputStream(str)));
            this.f4752g = this.f4748a.readLine();
            while (this.f4752g != null && !this.f4752g.startsWith(" LOG")) {
                if (!this.f4752g.trim().isEmpty()) {
                    this.f4749b.append(this.f4752g).append("\n");
                }
                this.f4752g = this.f4748a.readLine();
            }
            String strTrim = this.f4752g.trim();
            while (true) {
                String line = this.f4748a.readLine();
                this.f4752g = line;
                if (line == null) {
                    break;
                }
                if (!this.f4752g.isEmpty()) {
                    this.f4751f.add(this.f4752g);
                }
            }
            String[] strArrSplit = strTrim.split("\t");
            String[] strArrSplit2 = ((String) this.f4751f.get(0)).replaceAll("[0-9 .'-]", "").split("\t");
            int i2 = 0;
            while (i2 < strArrSplit.length) {
                String strTrim2 = strArrSplit[i2].trim();
                this.f4750e.add(new C0543c(strTrim2, ("Status,LOAD".contains(strTrim2) || i2 >= strArrSplit2.length) ? "" : strArrSplit2[i2]));
                i2++;
            }
            this.f4754i = new float[this.f4750e.size()];
            return true;
        } catch (IOException e2) {
            Logger.getLogger(am.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.a("Failed to parse the log file", e2);
        }
    }

    @Override // W.V
    public void a() {
        if (this.f4748a != null) {
            try {
                this.f4748a.close();
                this.f4748a = null;
            } catch (IOException e2) {
                Logger.getLogger(am.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    @Override // W.V
    public Iterator b() {
        return this.f4750e.iterator();
    }

    @Override // W.V
    public float[] c() {
        List list = this.f4751f;
        int i2 = this.f4753h;
        this.f4753h = i2 + 1;
        String[] strArrSplit = ((String) list.get(i2)).split("\t");
        for (int i3 = 0; i3 < strArrSplit.length; i3++) {
            String strReplaceAll = strArrSplit[i3].replaceAll("[^0-9.-]", "");
            if (!strReplaceAll.isEmpty() && !".".equals(strReplaceAll)) {
                this.f4754i[i3] = Float.parseFloat(strReplaceAll);
            }
        }
        return this.f4754i;
    }

    @Override // W.V
    public long d() {
        return this.f4751f.size();
    }

    @Override // W.V
    public boolean e() {
        return this.f4753h < this.f4751f.size();
    }

    @Override // W.V
    public boolean f() {
        return true;
    }

    @Override // W.V
    public HashMap g() {
        return this.f4755j;
    }

    @Override // W.V
    public String h() {
        return this.f4749b.toString();
    }

    @Override // W.V
    public String i() {
        return W.X.f2003S;
    }
}
