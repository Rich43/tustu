package ac;

import G.R;
import bH.W;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import sun.util.locale.LanguageTag;

/* renamed from: ac.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ac/c.class */
public class C0491c extends h {

    /* renamed from: p, reason: collision with root package name */
    private static C0491c f4179p = null;

    /* renamed from: q, reason: collision with root package name */
    private List f4180q = Collections.synchronizedList(new ArrayList());

    /* renamed from: r, reason: collision with root package name */
    private String f4181r = "\t";

    /* renamed from: a, reason: collision with root package name */
    protected char f4182a = '\n';

    /* renamed from: s, reason: collision with root package name */
    private int f4183s = 0;

    /* renamed from: t, reason: collision with root package name */
    private double f4184t = -1.0d;

    /* renamed from: u, reason: collision with root package name */
    private boolean f4185u = false;

    /* renamed from: v, reason: collision with root package name */
    private int f4186v = 0;

    /* renamed from: w, reason: collision with root package name */
    private int f4187w = 21;

    /* renamed from: x, reason: collision with root package name */
    private double f4188x = this.f4187w;

    /* renamed from: b, reason: collision with root package name */
    int f4189b = -1;

    /* renamed from: c, reason: collision with root package name */
    int f4190c = 0;

    /* renamed from: y, reason: collision with root package name */
    private double f4191y = 0.0d;

    /* renamed from: z, reason: collision with root package name */
    private InterfaceC0492d f4192z = new n();

    /* renamed from: A, reason: collision with root package name */
    private int f4193A = 0;

    protected C0491c() {
    }

    public static C0491c a() {
        if (f4179p == null) {
            f4179p = new C0491c();
        }
        return f4179p;
    }

    @Override // ac.h
    protected void a(R[] rArr, OutputStream outputStream) {
        BufferedWriter bufferedWriter;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, FTP.DEFAULT_CONTROL_ENCODING));
        } catch (UnsupportedEncodingException e2) {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        }
        try {
            String strA = this.f4192z.a(rArr);
            bufferedWriter.append((CharSequence) strA);
            if (!strA.endsWith("\n")) {
                bufferedWriter.append((CharSequence) "\"\n");
            }
            this.f4180q = a(rArr);
            StringBuilder sb = new StringBuilder();
            Iterator it = this.f4180q.iterator();
            while (it.hasNext()) {
                q qVar = (q) it.next();
                W.b(qVar.a(), LanguageTag.SEP, " ");
                W.b(qVar.a(), "(", "_");
                bufferedWriter.append((CharSequence) W.b(qVar.a(), ")", ""));
                sb.append(qVar.f());
                if (it.hasNext()) {
                    bufferedWriter.append((CharSequence) g());
                    sb.append(g());
                } else {
                    bufferedWriter.append((CharSequence) a(""));
                    bufferedWriter.append(this.f4182a);
                    sb.append(b(""));
                    sb.append(this.f4182a);
                }
            }
            bufferedWriter.write(sb.toString());
            bufferedWriter.flush();
            this.f4190c = 0;
            this.f4186v = 0;
            a(this);
        } catch (IOException e3) {
            bH.C.a("Failed to write Log Header.", e3, null);
        }
    }

    private void a(OutputStream outputStream, l lVar) {
        if (this.f4186v > 0) {
            PrintWriter printWriter = new PrintWriter(outputStream);
            this.f4183s++;
            printWriter.print("MARK " + W.a("" + this.f4183s, '0', 3) + " - " + lVar.a() + " - " + new Date().toString() + this.f4182a);
            printWriter.flush();
        }
    }

    @Override // ac.h
    protected void a(OutputStream outputStream, String str) {
        this.f4219o.add(new l(this, str, r()));
    }

    protected void b() {
        this.f4183s++;
    }

    protected int c() {
        return this.f4183s;
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x021d A[Catch: IOException -> 0x02ae, TryCatch #1 {IOException -> 0x02ae, blocks: (B:70:0x0214, B:83:0x0280, B:84:0x02a1, B:72:0x021d, B:74:0x0234, B:75:0x023f, B:77:0x0249, B:79:0x025d, B:80:0x026e), top: B:89:0x0214 }] */
    @Override // ac.h
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void a(java.io.OutputStream r9, byte[][] r10) {
        /*
            Method dump skipped, instructions count: 711
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: ac.C0491c.a(java.io.OutputStream, byte[][]):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:9:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected java.lang.StringBuilder a(java.lang.StringBuilder r4) {
        /*
            r3 = this;
            r0 = r3
            java.util.List r0 = r0.j()
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto Le
            r0 = r4
            return r0
        Le:
            r0 = r3
            java.util.List r0 = r0.j()
            java.util.Iterator r0 = r0.iterator()
            r5 = r0
        L18:
            r0 = r5
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L4b
            r0 = r5
            java.lang.Object r0 = r0.next()
            ac.a r0 = (ac.InterfaceC0489a) r0
            java.lang.String r0 = r0.c()
            r6 = r0
            r0 = r4
            r1 = r3
            java.lang.String r1 = r1.g()
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r4
            r1 = r6
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r5
            boolean r0 = r0.hasNext()
            if (r0 == 0) goto L48
        L48:
            goto L18
        L4b:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ac.C0491c.a(java.lang.StringBuilder):java.lang.StringBuilder");
    }

    protected String a(String str) {
        if (j().isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        Iterator it = j().iterator();
        while (it.hasNext()) {
            String strTrim = ((InterfaceC0489a) it.next()).a().trim();
            sb.append(g());
            sb.append(strTrim);
        }
        return sb.toString();
    }

    protected String b(String str) {
        if (j().isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        Iterator it = j().iterator();
        while (it.hasNext()) {
            String strTrim = ((InterfaceC0489a) it.next()).b().trim();
            sb.append(g());
            sb.append(strTrim);
        }
        return sb.toString();
    }

    @Override // ac.h
    protected void a(OutputStream outputStream) {
        this.f4180q.clear();
        try {
            outputStream.flush();
            outputStream.close();
        } catch (IOException e2) {
            Logger.getLogger(C0491c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public double d() {
        return this.f4188x;
    }

    public int e() {
        return this.f4186v;
    }

    public double f() {
        return this.f4191y;
    }

    public String g() {
        return this.f4181r;
    }

    public void c(String str) {
        this.f4181r = str;
    }

    public boolean h() {
        return this.f4185u;
    }

    public void a(boolean z2) {
        this.f4185u = z2;
    }

    public void a(InterfaceC0492d interfaceC0492d) {
        this.f4192z = interfaceC0492d;
    }
}
