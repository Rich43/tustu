package bQ;

import G.C0140w;
import G.aH;
import bH.W;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bQ/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private static c f7391a = null;

    /* renamed from: b, reason: collision with root package name */
    private int f7392b = -1;

    /* renamed from: c, reason: collision with root package name */
    private int f7393c = -1;

    /* renamed from: d, reason: collision with root package name */
    private int f7394d = -1;

    /* renamed from: e, reason: collision with root package name */
    private bO.a f7395e = null;

    /* renamed from: f, reason: collision with root package name */
    private List f7396f = null;

    /* renamed from: g, reason: collision with root package name */
    private List f7397g = null;

    /* renamed from: h, reason: collision with root package name */
    private List f7398h = null;

    /* renamed from: i, reason: collision with root package name */
    private List f7399i = new ArrayList();

    private c() {
    }

    protected void a(bO.a aVar, List list, List list2, List list3, int i2, int i3, int i4) {
        this.f7395e = aVar;
        this.f7396f = list;
        this.f7397g = list2;
        this.f7398h = list3;
        this.f7393c = i3;
        this.f7394d = i4;
        this.f7392b = i2;
        h();
    }

    private void h() {
        Iterator it = this.f7399i.iterator();
        while (it.hasNext()) {
            ((d) it.next()).a();
        }
    }

    public static c a() {
        if (f7391a == null) {
            f7391a = new c();
        }
        return f7391a;
    }

    public static String a(List list) {
        StringBuilder sb = new StringBuilder();
        sb.append("Range ").append(" \t").append("Start\t").append("End\t").append("Length\n");
        int iC = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            sb.append("Range ").append(i2).append(":\t").append(((C0140w) list.get(i2)).a()).append("\t").append(((C0140w) list.get(i2)).b()).append("\t").append(((C0140w) list.get(i2)).c()).append("\n");
            iC += ((C0140w) list.get(i2)).c();
        }
        sb.append("Data Range Count: ").append(list.size()).append("\tTotal Bytes: ").append(iC).append("\n");
        return sb.toString();
    }

    public static String b(List list) {
        StringBuilder sb = new StringBuilder();
        sb.append(W.b("Channel", ' ', 40)).append(" \t").append("address\t\t").append("Length\n");
        int iL = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            sb.append(W.b(((aH) list.get(i2)).aJ(), ' ', 40)).append("\t").append("0x").append(Long.toHexString(((aH) list.get(i2)).x()).toUpperCase()).append("\t\t").append(((aH) list.get(i2)).l()).append("\n");
            iL += ((aH) list.get(i2)).l();
        }
        sb.append("Channel Count: ").append(list.size()).append("\tTotal Bytes: ").append(iL).append("\n");
        return sb.toString();
    }

    public static String c(List list) {
        StringBuilder sb = new StringBuilder();
        sb.append("Channel ").append(" \t").append("address\t").append("Length\n");
        int iB = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            sb.append("ODT #").append(i2).append("\tSize: ").append(((bO.k) list.get(i2)).b()).append("\tEntries:\n");
            iB += ((bO.k) list.get(i2)).b();
            List<bO.l> list2 = (List) list.get(i2);
            int i3 = 0;
            sb.append("\tEntry #\tAdd\tLen\n");
            for (bO.l lVar : list2) {
                int i4 = i3;
                i3++;
                sb.append("\t").append(i4).append("\t").append("0x").append(Long.toHexString(lVar.a()).toUpperCase()).append("\t").append(lVar.b()).append("\n");
            }
        }
        sb.append("ODT Count: ").append(list.size()).append("\tTotal Bytes: ").append(iB).append("\n");
        return sb.toString();
    }

    public List b() {
        return this.f7396f;
    }

    public List c() {
        return this.f7397g;
    }

    public List d() {
        return this.f7398h;
    }

    public void a(d dVar) {
        this.f7399i.add(dVar);
    }

    public void b(d dVar) {
        this.f7399i.remove(dVar);
    }

    public int e() {
        return this.f7392b;
    }

    public int f() {
        return this.f7393c;
    }

    public int g() {
        return this.f7394d;
    }
}
