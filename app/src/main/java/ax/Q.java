package ax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: TunerStudioMS.jar:ax/Q.class */
public class Q {

    /* renamed from: c, reason: collision with root package name */
    private String f6343c;

    /* renamed from: f, reason: collision with root package name */
    private String f6344f;

    /* renamed from: i, reason: collision with root package name */
    private S f6348i;

    /* renamed from: d, reason: collision with root package name */
    static Logger f6340d = Logger.getLogger("com.efiAnalytics.mathparser");

    /* renamed from: a, reason: collision with root package name */
    private static List f6341a = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    private static S f6347h = new S();

    /* renamed from: j, reason: collision with root package name */
    private static final Pattern f6349j = Pattern.compile("0[xX][0-9a-fA-F]*");

    /* renamed from: l, reason: collision with root package name */
    private static final Pattern f6351l = Pattern.compile("0[0-9]*");

    /* renamed from: n, reason: collision with root package name */
    private static final Pattern f6353n = Pattern.compile("0[bB][0-1]*");

    /* renamed from: p, reason: collision with root package name */
    private static final Pattern f6355p = Pattern.compile("[0-9]*[.]{0,1}[0-9]*");

    /* renamed from: s, reason: collision with root package name */
    private static InterfaceC0914q f6358s = new C0915r("PI", 3.141592653589793d);

    /* renamed from: t, reason: collision with root package name */
    private static InterfaceC0914q f6359t = new C0915r("E", 2.718281828459045d);

    /* renamed from: u, reason: collision with root package name */
    private static C0889E f6360u = new C0889E(f6358s);

    /* renamed from: v, reason: collision with root package name */
    private static C0889E f6361v = new C0889E(f6359t);

    /* renamed from: b, reason: collision with root package name */
    private Map f6342b = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    private ArrayList f6345g = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    C0921x f6346e = null;

    /* renamed from: k, reason: collision with root package name */
    private Matcher f6350k = f6349j.matcher("");

    /* renamed from: m, reason: collision with root package name */
    private Matcher f6352m = f6351l.matcher("");

    /* renamed from: o, reason: collision with root package name */
    private Matcher f6354o = f6353n.matcher("");

    /* renamed from: q, reason: collision with root package name */
    private Matcher f6356q = f6355p.matcher("");

    /* renamed from: r, reason: collision with root package name */
    private String[] f6357r = null;

    public static am b(String str) {
        return new C0916s(str);
    }

    public String[] a() {
        if (this.f6357r == null) {
            ArrayList arrayList = new ArrayList();
            for (String str : this.f6342b.keySet()) {
                if (((C0889E) this.f6342b.get(str)).a() instanceof am) {
                    arrayList.add(str);
                }
            }
            this.f6357r = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        return this.f6357r;
    }

    static ab a(String str, ArrayList arrayList) {
        ac acVar = null;
        Iterator it = f6341a.iterator();
        while (it.hasNext() && acVar == null) {
            ac acVarA = ((AbstractC0902e) it.next()).a(str, arrayList);
            if (acVarA != null) {
                acVar = acVarA;
            }
        }
        if (acVar == null) {
            throw new C0923z(str);
        }
        return acVar;
    }

    public static void a(AbstractC0902e abstractC0902e) {
        f6341a.add(0, abstractC0902e);
    }

    public Q() {
        this.f6348i = null;
        this.f6348i = (S) f6347h.clone();
        this.f6348i.a(new R(this));
    }

    public void a(String str) {
        this.f6343c = str;
        this.f6344f = str;
        this.f6346e = null;
        this.f6357r = null;
        h();
        e();
        f();
        g();
    }

    public double d() {
        if (this.f6346e == null) {
            b();
        }
        return this.f6346e.b(this.f6348i);
    }

    private synchronized void b() {
        this.f6346e = new C0921x(this.f6345g);
    }

    private void e() throws C0893I {
        char[] charArray = this.f6343c.toCharArray();
        int length = charArray.length;
        boolean z2 = true;
        boolean z3 = false;
        StringBuffer stringBuffer = null;
        for (int i2 = 0; i2 < length; i2++) {
            if (charArray[i2] == '-' && z2) {
                this.f6345g.add(new ad((char) 196));
            } else if (charArray[i2] != '+' || !z2) {
                if (charArray[i2] == '~') {
                    if (z3) {
                        f6340d.severe("A Bitwise Not was found following an Operand: " + ((Object) stringBuffer));
                        throw new C0893I("A Bitwise Not was found following an Operand: " + ((Object) stringBuffer));
                    }
                    this.f6345g.add(new ad('~'));
                } else if (charArray[i2] == '!') {
                    if (z3) {
                        c(stringBuffer.toString());
                        stringBuffer = null;
                        z3 = false;
                    }
                    this.f6345g.add(new ad(charArray[i2]));
                    z2 = true;
                } else if (charArray[i2] == '(' || charArray[i2] == ')') {
                    if (z3) {
                        c(stringBuffer.toString());
                        stringBuffer = null;
                        z3 = false;
                    }
                    if (charArray[i2] == ')') {
                        z2 = false;
                    } else {
                        z2 = true;
                        if (this.f6345g.size() >= 1) {
                            Object obj = this.f6345g.get(this.f6345g.size() - 1);
                            if (obj instanceof C0891G) {
                                ((C0891G) obj).a();
                            }
                        }
                    }
                    this.f6345g.add(new ad(charArray[i2]));
                } else if (charArray[i2] == '+' || charArray[i2] == '-' || charArray[i2] == '*' || charArray[i2] == '/' || charArray[i2] == '?' || charArray[i2] == ':' || charArray[i2] == '=' || charArray[i2] == 241 || charArray[i2] == 242 || charArray[i2] == 243 || charArray[i2] == '>' || charArray[i2] == '<' || charArray[i2] == 186 || charArray[i2] == 187 || charArray[i2] == ',' || charArray[i2] == '&' || charArray[i2] == '|' || charArray[i2] == '^' || charArray[i2] == 179 || charArray[i2] == 180 || charArray[i2] == 181 || charArray[i2] == '%') {
                    if (z2) {
                        f6340d.severe("Invalid expression - Expected Opperand");
                        throw new C0893I("Invalid expression - Expected Opperand");
                    }
                    if (z3) {
                        c(stringBuffer.toString());
                        z2 = true;
                        stringBuffer = null;
                        z3 = false;
                    }
                    this.f6345g.add(new ad(charArray[i2]));
                } else {
                    if (charArray[i2] == '!') {
                        throw new RuntimeException("Unsupported Operator - '!'");
                    }
                    if (charArray[i2] != ' ') {
                        if (stringBuffer == null) {
                            stringBuffer = new StringBuffer();
                        }
                        stringBuffer.append(charArray[i2]);
                        z2 = false;
                        z3 = true;
                    }
                }
            }
        }
        if (z3) {
            c(stringBuffer.toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [ax.L] */
    /* JADX WARN: Type inference failed for: r0v15, types: [ax.L] */
    /* JADX WARN: Type inference failed for: r0v16, types: [ax.L] */
    /* JADX WARN: Type inference failed for: r0v17, types: [ax.L] */
    /* JADX WARN: Type inference failed for: r0v21, types: [ax.L] */
    private void c(String str) {
        this.f6345g.add(f(str) ? new C0896L(Long.parseLong(str.substring(2), 16)) : h(str) ? new C0896L(Long.parseLong(str, 8)) : g(str) ? new C0896L(Long.parseLong(str.substring(2), 2)) : i(str) ? new C0896L(Double.parseDouble(str)) : d(str) ? new C0896L(e(str)) : new C0891G(str));
    }

    private boolean d(String str) {
        String lowerCase = str.toLowerCase();
        if (!lowerCase.contains("e")) {
            return false;
        }
        String[] strArrSplit = lowerCase.split("e");
        return strArrSplit.length == 2 && bH.H.a(strArrSplit[0]) && bH.H.a(strArrSplit[1]);
    }

    private double e(String str) {
        String[] strArrSplit = str.toLowerCase().split("e");
        if (strArrSplit.length == 2) {
            return Double.parseDouble(strArrSplit[0]) * Math.pow(10.0d, Double.parseDouble(strArrSplit[1]));
        }
        System.out.print("Invalid Scientific Notation: " + str);
        return Double.NaN;
    }

    private boolean f(String str) {
        this.f6350k.reset(str);
        return this.f6350k.matches();
    }

    private boolean g(String str) {
        this.f6354o.reset(str);
        return this.f6354o.matches();
    }

    private boolean h(String str) {
        this.f6352m.reset(str);
        return this.f6352m.matches();
    }

    private boolean i(String str) {
        this.f6356q.reset(str);
        return this.f6356q.matches();
    }

    private void f() {
        this.f6342b.put(f6358s.a(), f6360u);
        this.f6342b.put(f6359t.a(), f6361v);
    }

    private void g() {
        for (int i2 = 0; i2 < this.f6345g.size(); i2++) {
            Object obj = this.f6345g.get(i2);
            if (obj instanceof C0891G) {
                C0891G c0891g = (C0891G) obj;
                if (!c0891g.b()) {
                    if (this.f6342b.containsKey(c0891g.c())) {
                        this.f6345g.set(i2, (C0889E) this.f6342b.get(c0891g.c()));
                    } else {
                        C0889E c0889e = new C0889E(b(c0891g.c()));
                        this.f6342b.put(c0891g.c(), c0889e);
                        this.f6345g.set(i2, c0889e);
                    }
                }
            }
        }
    }

    public void a(String str, double d2) throws C0890F, C0892H {
        C0889E c0889e = (C0889E) this.f6342b.get(str);
        if (c0889e == null) {
            throw new C0890F(str);
        }
        if (!(c0889e.a() instanceof am)) {
            throw new C0892H(str);
        }
        ((am) c0889e.a()).a(d2);
    }

    private void h() {
        this.f6343c = a(this.f6343c, "==", "=");
        this.f6343c = a(this.f6343c, "!=", "ñ");
        this.f6343c = a(this.f6343c, ">=", "ò");
        this.f6343c = a(this.f6343c, "<=", "ó");
        this.f6343c = a(this.f6343c, "||", "º");
        this.f6343c = a(this.f6343c, "&&", "»");
        this.f6343c = a(this.f6343c, "<<", "³");
        this.f6343c = a(this.f6343c, ">>>", "µ");
        this.f6343c = a(this.f6343c, ">>", "´");
    }

    private static String a(String str, String str2, String str3) {
        int iIndexOf = str.indexOf(str2);
        while (true) {
            int i2 = iIndexOf;
            if (i2 < 0) {
                return str;
            }
            str = str.substring(0, i2) + str3 + str.substring(i2 + str2.length());
            iIndexOf = str.indexOf(str2, i2 + str3.length());
        }
    }

    public String c() {
        return this.f6344f;
    }

    static {
        f6341a.add(new C0904g());
    }
}
