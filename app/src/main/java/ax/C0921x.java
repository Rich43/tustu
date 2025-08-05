package ax;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: ax.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/x.class */
class C0921x implements ab {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f6421a;

    public C0921x(ArrayList arrayList) throws C0893I {
        this.f6421a = null;
        this.f6421a = arrayList;
        ArrayList arrayList2 = new ArrayList();
        f();
        a();
        b();
        e();
        arrayList2.clear();
        arrayList2.add('*');
        arrayList2.add('/');
        arrayList2.add('%');
        a(arrayList2);
        arrayList2.clear();
        arrayList2.add('+');
        arrayList2.add('-');
        a(arrayList2);
        arrayList2.clear();
        arrayList2.add((char) 179);
        arrayList2.add((char) 180);
        arrayList2.add((char) 181);
        a(arrayList2);
        arrayList2.clear();
        arrayList2.add('&');
        a(arrayList2);
        arrayList2.clear();
        arrayList2.add('^');
        a(arrayList2);
        arrayList2.clear();
        arrayList2.add('|');
        a(arrayList2);
        c();
        d();
        if (arrayList.size() != 1) {
            throw new C0893I("Unknown error: Possibly caused by adjacent operators/operands");
        }
    }

    private void a() {
        boolean z2 = true;
        while (z2) {
            boolean z3 = false;
            int i2 = 0;
            int i3 = 0;
            while (i3 < this.f6421a.size() && !z3) {
                Object obj = this.f6421a.get(i3);
                if ((obj instanceof ad) && ((ad) obj).a() == '(') {
                    z3 = true;
                    i2 = i3;
                }
                i3++;
            }
            int i4 = 0;
            boolean z4 = false;
            int i5 = 0;
            while (i3 < this.f6421a.size() && !z4) {
                Object obj2 = this.f6421a.get(i3);
                if (obj2 instanceof ad) {
                    ad adVar = (ad) obj2;
                    if (adVar.a() == '(') {
                        i4++;
                    } else if (adVar.a() == ')') {
                        if (i4 == 0) {
                            z4 = true;
                            i5 = i3;
                        } else {
                            i4--;
                        }
                    }
                }
                i3++;
            }
            if (z3) {
                this.f6421a.set(i2, a(i2 + 1, i5));
                this.f6421a.subList(i2 + 1, i5 + 1).clear();
            } else {
                z2 = false;
            }
        }
    }

    private void b() throws C0893I {
        int i2 = 0;
        int i3 = 0;
        boolean z2 = false;
        int i4 = 0;
        while (i4 < this.f6421a.size() && !z2) {
            Object obj = this.f6421a.get(i4);
            if ((obj instanceof ad) && ((ad) obj).a() == '?') {
                i2 = i4;
                z2 = true;
            }
            i4++;
        }
        boolean z3 = true;
        int i5 = 0;
        while (i4 < this.f6421a.size() && z3) {
            Object obj2 = this.f6421a.get(i4);
            if (obj2 instanceof ad) {
                ad adVar = (ad) obj2;
                if (adVar.a() == ':') {
                    if (i5 == 0) {
                        i3 = i4;
                        z3 = false;
                    } else {
                        i5--;
                    }
                } else if (adVar.a() == '?') {
                    i5++;
                }
            }
            i4++;
        }
        if (z2) {
            if (i2 == 0 || i3 == this.f6421a.size() - 1) {
                throw new C0893I("Invalid Ternary Expression");
            }
            this.f6421a.set(0, new al(a(0, i2), a(i2 + 1, i3), a(i3 + 1, this.f6421a.size())));
            this.f6421a.subList(1, this.f6421a.size()).clear();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v39, types: [ax.C] */
    /* JADX WARN: Type inference failed for: r0v40, types: [ax.J] */
    /* JADX WARN: Type inference failed for: r0v41, types: [ax.B] */
    /* JADX WARN: Type inference failed for: r0v42, types: [ax.aa] */
    /* JADX WARN: Type inference failed for: r0v49, types: [ax.w] */
    private void c() throws C0893I {
        for (int size = this.f6421a.size() - 1; size >= 0; size--) {
            Object obj = this.f6421a.get(size);
            if (obj instanceof ad) {
                ad adVar = (ad) obj;
                if (adVar.a() == '=' || adVar.a() == 241 || adVar.a() == 242 || adVar.a() == 243 || adVar.a() == '>' || adVar.a() == '<') {
                    if (size == 0 || size == this.f6421a.size() - 1 || !(this.f6421a.get(size - 1) instanceof ab) || !(this.f6421a.get(size + 1) instanceof ab)) {
                        throw new C0893I("Evaluation Expression missing left or right Operand");
                    }
                    C0895K c0895k = null;
                    if (adVar.a() == '=') {
                        c0895k = new C0920w((ab) this.f6421a.get(size - 1), (ab) this.f6421a.get(size + 1));
                    } else if (adVar.a() == 241) {
                        c0895k = new aa((ab) this.f6421a.get(size - 1), (ab) this.f6421a.get(size + 1));
                    } else if (adVar.a() == 242) {
                        c0895k = new C0886B((ab) this.f6421a.get(size - 1), (ab) this.f6421a.get(size + 1));
                    } else if (adVar.a() == 243) {
                        c0895k = new C0894J((ab) this.f6421a.get(size - 1), (ab) this.f6421a.get(size + 1));
                    } else if (adVar.a() == '>') {
                        c0895k = new C0887C((ab) this.f6421a.get(size - 1), (ab) this.f6421a.get(size + 1));
                    } else if (adVar.a() == '<') {
                        c0895k = new C0895K((ab) this.f6421a.get(size - 1), (ab) this.f6421a.get(size + 1));
                    }
                    this.f6421a.set(size - 1, c0895k);
                    this.f6421a.subList(size, size + 2).clear();
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v46, types: [ax.l] */
    /* JADX WARN: Type inference failed for: r0v47, types: [ax.i] */
    /* JADX WARN: Type inference failed for: r0v48, types: [ax.k] */
    /* JADX WARN: Type inference failed for: r0v49, types: [ax.n] */
    /* JADX WARN: Type inference failed for: r0v50, types: [ax.h] */
    /* JADX WARN: Type inference failed for: r0v51, types: [ax.aj] */
    /* JADX WARN: Type inference failed for: r0v52, types: [ax.f] */
    /* JADX WARN: Type inference failed for: r0v53, types: [ax.V] */
    /* JADX WARN: Type inference failed for: r0v54, types: [ax.t] */
    /* JADX WARN: Type inference failed for: r0v61, types: [ax.W] */
    private void a(List list) throws C0893I {
        int i2 = 0;
        while (i2 < this.f6421a.size()) {
            Object obj = this.f6421a.get(i2);
            if (obj instanceof ad) {
                ad adVar = (ad) obj;
                if (!list.contains(Character.valueOf(adVar.a()))) {
                    continue;
                } else {
                    if (i2 == 0 || i2 == this.f6421a.size() - 1 || !(this.f6421a.get(i2 - 1) instanceof ab) || !(this.f6421a.get(i2 + 1) instanceof ab)) {
                        throw new C0893I("Operation missing left or right Operand");
                    }
                    C0910m c0910m = null;
                    if (adVar.a() == '*') {
                        c0910m = new W((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == '/') {
                        c0910m = new C0917t((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == '%') {
                        c0910m = new V((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == '+') {
                        c0910m = new C0903f((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == '-') {
                        c0910m = new aj((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == '&') {
                        c0910m = new C0905h((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == '^') {
                        c0910m = new C0911n((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == '|') {
                        c0910m = new C0908k((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == 179) {
                        c0910m = new C0906i((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == 180) {
                        c0910m = new C0909l((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    } else if (adVar.a() == 181) {
                        c0910m = new C0910m((ab) this.f6421a.get(i2 - 1), (ab) this.f6421a.get(i2 + 1));
                    }
                    this.f6421a.set(i2 - 1, c0910m);
                    this.f6421a.subList(i2, i2 + 2).clear();
                    i2--;
                }
            }
            i2++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v37, types: [ax.P] */
    private void d() throws C0893I {
        for (int size = this.f6421a.size() - 1; size >= 0; size--) {
            Object obj = this.f6421a.get(size);
            if (obj instanceof ad) {
                ad adVar = (ad) obj;
                if (adVar.a() == 186 || adVar.a() == 187) {
                    if (size == 0 || size == this.f6421a.size() - 1 || !(this.f6421a.get(size - 1) instanceof ab) || !(this.f6421a.get(size + 1) instanceof ab)) {
                        throw new C0893I("Logical Expression missing left or right Operand");
                    }
                    O o2 = null;
                    if (adVar.a() == 186) {
                        o2 = new P((ab) this.f6421a.get(size - 1), (ab) this.f6421a.get(size + 1));
                    } else if (adVar.a() == 187) {
                        o2 = new O((ab) this.f6421a.get(size - 1), (ab) this.f6421a.get(size + 1));
                    }
                    this.f6421a.set(size - 1, o2);
                    this.f6421a.subList(size, size + 2).clear();
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v30, types: [ax.Y] */
    /* JADX WARN: Type inference failed for: r0v37, types: [ax.X] */
    private void e() throws C0893I {
        for (int size = this.f6421a.size() - 1; size >= 0; size--) {
            Object obj = this.f6421a.get(size);
            if (obj instanceof ad) {
                ad adVar = (ad) obj;
                if (adVar.a() == 196 || adVar.a() == '!' || adVar.a() == 197 || adVar.a() == '~') {
                    if (size == this.f6421a.size() - 1 || !(this.f6421a.get(size + 1) instanceof ab)) {
                        throw new C0893I("Unary operator is missing an operand.");
                    }
                    C0907j c0907j = null;
                    if (adVar.a() == 196) {
                        c0907j = new X((ab) this.f6421a.get(size + 1));
                    } else if (adVar.a() == '!') {
                        c0907j = new Y((ab) this.f6421a.get(size + 1));
                    } else if (adVar.a() == '~') {
                        c0907j = new C0907j((ab) this.f6421a.get(size + 1));
                    } else if (adVar.a() == 197) {
                    }
                    this.f6421a.set(size, c0907j);
                    this.f6421a.subList(size + 1, size + 2).clear();
                }
            }
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator it = this.f6421a.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof C0921x) {
                stringBuffer.append("( ").append(next.toString()).append(" )");
            } else {
                stringBuffer.append(next.toString());
            }
        }
        return stringBuffer.toString();
    }

    @Override // ax.ab
    public double b(S s2) throws C0893I {
        Object obj = this.f6421a.get(0);
        if (obj instanceof ab) {
            return ((ab) obj).b(s2);
        }
        throw new C0893I("Reason Unknown.");
    }

    private void f() throws C0893I {
        int i2 = 0;
        while (i2 < this.f6421a.size()) {
            Object obj = this.f6421a.get(i2);
            if ((obj instanceof C0891G) && ((C0891G) obj).b()) {
                C0891G c0891g = (C0891G) obj;
                int i3 = i2;
                int i4 = i2 + 2;
                ArrayList arrayList = new ArrayList();
                int i5 = 0;
                boolean z2 = true;
                int i6 = i4;
                int i7 = 0;
                boolean z3 = false;
                while (z2) {
                    Object obj2 = this.f6421a.get(i4);
                    if ((obj2 instanceof ad) && ((ad) obj2).a() == ')') {
                        if (i5 == 0) {
                            if (z3) {
                                arrayList.add(a(i6, i4));
                            }
                            i7 = i4;
                            z2 = false;
                        } else {
                            i5--;
                        }
                    } else if ((obj2 instanceof ad) && ((ad) obj2).a() == '(') {
                        i5++;
                    } else if (!(obj2 instanceof ad) || ((ad) obj2).a() != ',') {
                        z3 = true;
                    } else if (i5 != 0) {
                        continue;
                    } else {
                        if (!z3) {
                            throw new C0893I("Invalid Expression. Comma found without a preceding expression.");
                        }
                        arrayList.add(a(i6, i4));
                        z3 = false;
                        i6 = i4 + 1;
                    }
                    i4++;
                }
                this.f6421a.set(i3, Q.a(c0891g.c(), arrayList));
                this.f6421a.subList(i3 + 1, i7 + 1).clear();
                i2 = i3 + 1;
            } else {
                i2++;
            }
        }
    }

    private C0921x a(int i2, int i3) {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f6421a.subList(i2, i3).iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return new C0921x(arrayList);
    }
}
