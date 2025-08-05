package bW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

/* loaded from: TunerStudioMS.jar:bW/d.class */
public class d {

    /* renamed from: a, reason: collision with root package name */
    private int f7650a = 0;

    /* renamed from: b, reason: collision with root package name */
    private String f7651b = "";

    /* renamed from: c, reason: collision with root package name */
    private ArrayList f7652c = new ArrayList();

    public int a() {
        return this.f7650a;
    }

    public String b() {
        return this.f7651b;
    }

    public void a(c cVar) {
        this.f7652c.add(cVar);
    }

    public Iterator c() {
        return this.f7652c.iterator();
    }

    public d a(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, "^");
        try {
            this.f7650a = Integer.parseInt(stringTokenizer.nextToken());
        } catch (Exception e2) {
            this.f7650a = 0;
        }
        if (stringTokenizer.hasMoreTokens()) {
            this.f7651b = stringTokenizer.nextToken().trim();
        }
        while (stringTokenizer.hasMoreTokens()) {
            c cVar = new c();
            cVar.c(stringTokenizer.nextToken());
            a(cVar);
        }
        return this;
    }
}
