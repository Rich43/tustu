package ak;

import W.C0187m;
import bH.C0995c;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:ak/an.class */
public class an extends W.V {

    /* renamed from: w, reason: collision with root package name */
    private BufferedInputStream f4756w = null;

    /* renamed from: z, reason: collision with root package name */
    private int f4759z = 55;

    /* renamed from: A, reason: collision with root package name */
    private byte[] f4760A = null;

    /* renamed from: B, reason: collision with root package name */
    private byte[] f4761B = null;

    /* renamed from: a, reason: collision with root package name */
    int f4762a = 0;

    /* renamed from: b, reason: collision with root package name */
    long f4763b = 0;

    /* renamed from: e, reason: collision with root package name */
    int f4764e = 0;

    /* renamed from: f, reason: collision with root package name */
    long f4765f = 0;

    /* renamed from: g, reason: collision with root package name */
    int f4766g = 0;

    /* renamed from: h, reason: collision with root package name */
    int f4767h = 0;

    /* renamed from: i, reason: collision with root package name */
    int f4768i = 0;

    /* renamed from: C, reason: collision with root package name */
    private final byte[] f4769C = new byte[2];

    /* renamed from: D, reason: collision with root package name */
    private final byte[] f4770D = new byte[2];

    /* renamed from: E, reason: collision with root package name */
    private byte[] f4771E = null;

    /* renamed from: F, reason: collision with root package name */
    private byte[] f4772F = new byte[50];

    /* renamed from: G, reason: collision with root package name */
    private byte[] f4773G = new byte[1];

    /* renamed from: H, reason: collision with root package name */
    private final byte[] f4774H = new byte[1];

    /* renamed from: j, reason: collision with root package name */
    long f4775j = 0;

    /* renamed from: k, reason: collision with root package name */
    int f4776k = -1;

    /* renamed from: l, reason: collision with root package name */
    int f4777l = 0;

    /* renamed from: m, reason: collision with root package name */
    byte f4778m = 0;

    /* renamed from: n, reason: collision with root package name */
    int f4779n = 0;

    /* renamed from: o, reason: collision with root package name */
    long f4780o = 0;

    /* renamed from: p, reason: collision with root package name */
    long f4781p = 0;

    /* renamed from: q, reason: collision with root package name */
    int f4782q = -1;

    /* renamed from: I, reason: collision with root package name */
    private int f4783I = 1000;

    /* renamed from: r, reason: collision with root package name */
    List f4784r = new ArrayList();

    /* renamed from: s, reason: collision with root package name */
    List f4785s = new ArrayList();

    /* renamed from: t, reason: collision with root package name */
    List f4786t = new ArrayList();

    /* renamed from: u, reason: collision with root package name */
    HashMap f4787u = new HashMap();

    /* renamed from: v, reason: collision with root package name */
    String f4788v = "";

    /* renamed from: x, reason: collision with root package name */
    private static int f4757x = 22;

    /* renamed from: y, reason: collision with root package name */
    private static final int f4758y = f4757x;

    /* renamed from: J, reason: collision with root package name */
    private static K f4789J = null;

    public static void a(K k2) {
        f4789J = k2;
    }

    @Override // W.V
    public String i() {
        return W.X.f1993I;
    }

    @Override // W.V
    public boolean a(String str) throws V.a {
        try {
            File file = new File(str);
            this.f4780o = file.length();
            this.f4756w = new BufferedInputStream(new FileInputStream(file));
            this.f4760A = new byte[8];
            if (this.f4756w.read(this.f4760A) != this.f4760A.length) {
                throw new V.a("Read incomplete header, file not valid?");
            }
            if (this.f4760A[0] != 77 || this.f4760A[1] != 76 || this.f4760A[2] != 86 || this.f4760A[3] != 76 || this.f4760A[4] != 71) {
                throw new V.a("Not a valid .mlg file");
            }
            this.f4762a = C0995c.a(this.f4760A, 6, 2, true, false);
            if (this.f4762a > 2) {
                throw new V.a("File Format Version: " + this.f4762a + "\nMaximum supported Format Version: 2\nYou likely need a newer version of this application to load this file.");
            }
            if (this.f4762a > 1) {
                this.f4759z = 89;
                f4757x = 24;
            } else {
                this.f4759z = 55;
                f4757x = 22;
            }
            byte[] bArr = new byte[f4757x];
            System.arraycopy(this.f4760A, 0, bArr, 0, this.f4760A.length);
            byte[] bArr2 = new byte[bArr.length - 8];
            if (this.f4756w.read(bArr2) != bArr2.length) {
                throw new V.a("Read incomplete header, file not valid?");
            }
            System.arraycopy(bArr2, 0, bArr, this.f4760A.length, bArr2.length);
            this.f4760A = bArr;
            this.f4763b = C0995c.b(this.f4760A, 8, 4, true, false) * 1000;
            if (this.f4762a == 1) {
                this.f4764e = C0995c.a(this.f4760A, 12, 2, true, false);
                this.f4765f = C0995c.b(this.f4760A, 14, 4, true, false);
                this.f4766g = C0995c.a(this.f4760A, 18, 2, true, false);
                this.f4771E = new byte[this.f4766g];
                this.f4767h = C0995c.a(this.f4760A, 20, 2, true, false);
                this.f4775j = 22L;
            } else {
                this.f4764e = C0995c.a(this.f4760A, 12, 4, true, false);
                this.f4765f = C0995c.b(this.f4760A, 16, 4, true, false);
                this.f4766g = C0995c.a(this.f4760A, 20, 2, true, false);
                this.f4771E = new byte[this.f4766g];
                this.f4767h = C0995c.a(this.f4760A, 22, 2, true, false);
                this.f4775j = 24L;
            }
            this.f4783I = this.f4771E.length + 5;
            return true;
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            throw new V.a("File not found:\n" + str);
        } catch (IOException e3) {
            e3.printStackTrace();
            throw new V.a("Unable to open file:\n" + str);
        }
    }

    @Override // W.V
    public void a() {
        try {
            this.f4756w.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @Override // W.V
    public Iterator b() throws V.a {
        byte[] bArr = new byte[this.f4759z];
        this.f4785s.clear();
        this.f4784r.clear();
        this.f4786t.clear();
        this.f4777l = 0;
        for (int i2 = 0; i2 < this.f4767h; i2++) {
            try {
                int i3 = 0;
                do {
                    int i4 = this.f4756w.read(bArr, i3, bArr.length - i3);
                    i3 += i4;
                    if (i4 == -1) {
                        break;
                    }
                } while (i3 < bArr.length);
                if (i3 > bArr.length) {
                    throw new V.a("Over-run reading header, expected: " + bArr.length + ", but got: " + i3);
                }
                this.f4775j += this.f4759z;
                this.f4785s.add(a(bArr, this.f4762a));
            } catch (IOException e2) {
                throw new V.a("Error reading file: " + e2.getLocalizedMessage());
            }
        }
        this.f4776k = (int) this.f4775j;
        this.f4761B = new byte[(int) (this.f4765f - this.f4776k)];
        int i5 = 0;
        do {
            int i6 = this.f4756w.read(this.f4761B, i5, this.f4761B.length - i5);
            i5 += i6;
            if (i6 == -1) {
                break;
            }
        } while (i5 < this.f4761B.length);
        if (i5 > this.f4761B.length) {
            throw new V.a("Over-run reading extended header, expected: " + bArr.length + ", but got: " + i5);
        }
        this.f4779n = (int) ((this.f4780o - this.f4765f) / (this.f4766g + 5));
        int i7 = 0;
        for (ac.w wVar : this.f4785s) {
            if (wVar instanceof ac.t) {
                ac.t tVar = (ac.t) wVar;
                List listA = a(this.f4761B, tVar.a() - this.f4776k, tVar.e());
                for (int i8 = 0; i8 < tVar.e(); i8++) {
                    if (listA.size() > i8) {
                        tVar.a((String) listA.get(i8));
                    } else {
                        int i9 = i7;
                        i7++;
                        tVar.a("Unknown" + i9);
                    }
                }
            }
        }
        if (this.f4764e > 0) {
            this.f4788v = bH.W.a(this.f4761B, this.f4764e - this.f4776k, this.f4761B.length - (this.f4764e - this.f4776k));
            l();
        }
        int iK = 0;
        for (ac.w wVar2 : this.f4785s) {
            if (wVar2 instanceof ac.t) {
                ac.t tVar2 = (ac.t) wVar2;
                if (tVar2.h().trim().length() > 0) {
                    C0543c c0543c = new C0543c();
                    c0543c.a(tVar2.h());
                    c0543c.b(tVar2.i());
                    int iA = f4789J != null ? f4789J.a(c0543c.a()) : -1;
                    if (iA >= 0) {
                        c0543c.b(iA);
                    } else {
                        c0543c.b((int) tVar2.b());
                    }
                    c0543c.a((int) tVar2.e());
                    this.f4784r.add(c0543c);
                    this.f4786t.add(new ap(this, iK, tVar2.k(), false, false));
                }
                List listD = tVar2.d();
                for (int i10 = 0; i10 < tVar2.e() && i10 < listD.size(); i10++) {
                    if (!((String) listD.get(i10)).equals("INVALID")) {
                        C0543c c0543c2 = new C0543c();
                        c0543c2.a((String) listD.get(i10));
                        int iA2 = f4789J != null ? f4789J.a(c0543c2.a()) : -1;
                        if (iA2 >= 0) {
                            c0543c2.b(iA2);
                        } else if (tVar2.b() == 2) {
                            c0543c2.b(4);
                        } else {
                            c0543c2.b((int) tVar2.b());
                        }
                        this.f4784r.add(c0543c2);
                        this.f4786t.add(new ao(this, iK, tVar2.k(), i10));
                    }
                }
            } else if (wVar2 instanceof ac.x) {
                ac.x xVar = (ac.x) wVar2;
                C0543c c0543c3 = new C0543c();
                c0543c3.a(xVar.h());
                c0543c3.b(xVar.i());
                c0543c3.b((int) xVar.j());
                c0543c3.a(xVar.a());
                c0543c3.b(xVar.b());
                c0543c3.a((int) xVar.c());
                this.f4784r.add(c0543c3);
                this.f4786t.add(new ap(this, iK, xVar.k(), xVar.g() == ac.w.f4307j, xVar.l()));
            }
            iK += wVar2.k();
        }
        if (!a(this.f4784r, "Time")) {
            C0543c c0543c4 = new C0543c();
            c0543c4.a("Time");
            c0543c4.b(PdfOps.s_TOKEN);
            c0543c4.a(5);
            this.f4784r.add(c0543c4);
            this.f4786t.add(new aq(this));
        }
        this.f4768i = this.f4784r.size();
        this.f4775j = this.f4764e;
        return this.f4784r.iterator();
    }

    private void l() {
        Scanner scanner = new Scanner(this.f4788v);
        boolean z2 = false;
        while (scanner.hasNextLine()) {
            String strNextLine = scanner.nextLine();
            if (strNextLine.contains("NEW_INFO_PROVIDER,[FooterData]")) {
                z2 = true;
            } else if (z2 && strNextLine.contains("=")) {
                this.f4787u.put(strNextLine.substring(0, strNextLine.indexOf("=")), strNextLine.substring(strNextLine.indexOf("=") + 1));
            } else if (z2 && strNextLine.startsWith("NEW_INFO_PROVIDER")) {
                return;
            }
        }
    }

    private boolean a(List list, String str) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((W.T) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private ac.w a(byte[] bArr, int i2) throws V.a {
        if (bArr.length != this.f4759z) {
            throw new V.a("Invalid LoggerField Buffer size: " + bArr.length);
        }
        byte b2 = bArr[0];
        if (b2 != ac.w.f4300c && b2 != ac.w.f4301d && b2 != ac.w.f4302e && b2 != ac.w.f4303f && b2 != ac.w.f4304g && b2 != ac.w.f4305h && b2 != ac.w.f4307j && b2 != ac.w.f4306i) {
            if (b2 != ac.w.f4308k && b2 != ac.w.f4309l && b2 != ac.w.f4310m) {
                throw new V.a("Unknow LoggerField Type: " + ((int) b2));
            }
            ac.t tVar = new ac.t();
            tVar.b(b2);
            tVar.b(bH.W.a(bArr, 1, 34));
            tVar.d(bH.W.a(bArr, 35, 10));
            tVar.e(bArr[45]);
            tVar.c(bArr[46]);
            tVar.b(C0995c.a(bArr, 47, 4, true, true));
            tVar.a(bArr[51]);
            if (i2 > 1) {
                tVar.c(bH.W.a(bArr, 55, 34));
            }
            return tVar;
        }
        ac.x xVar = new ac.x();
        xVar.b(b2);
        String strA = bH.W.a(bArr, 1, 34);
        xVar.b(strA);
        xVar.d(bH.W.a(bArr, 35, 10));
        byte b3 = bArr[45];
        int iA = -1;
        if (f4789J != null) {
            iA = f4789J.a(strA);
        }
        if (iA >= 0) {
            xVar.e(iA);
        } else {
            xVar.e(b3);
        }
        xVar.b(Float.intBitsToFloat(C0995c.a(bArr, 46, 4, true, true)));
        xVar.c(Float.intBitsToFloat(C0995c.a(bArr, 50, 4, true, true)));
        xVar.a(bArr[54]);
        if (i2 > 1) {
            xVar.c(bH.W.a(bArr, 55, 34));
        }
        return xVar;
    }

    private List a(byte[] bArr, int i2, int i3) {
        ArrayList arrayList = new ArrayList();
        for (int i4 = 0; i4 < i3; i4++) {
            String strA = bH.W.a(bArr, i2, bArr.length - i2);
            arrayList.add(strA);
            i2 += strA.length() + 1;
        }
        return arrayList;
    }

    @Override // W.V
    public float[] c() throws V.a {
        if (this.f4777l >= 500 && k()) {
            throw new V.a("This Edition is limited to loading 500 rows of data. \nPlease Register to load large log files.");
        }
        this.f4756w.mark(this.f4783I);
        a(this.f4769C);
        if (this.f4778m != this.f4769C[1]) {
            bH.C.b("Row: " + this.f4777l + " Unexpected record counter! Expected: " + C0995c.b(this.f4778m) + ", found: " + C0995c.b(this.f4769C[1]));
            this.f4778m = this.f4769C[1];
        }
        this.f4778m = (byte) (this.f4778m + 1);
        if (this.f4769C[0] == 1) {
            a(this.f4770D);
            c(C0995c.a(this.f4770D, 0, this.f4770D.length, true, false));
            a(this.f4772F);
            throw new C0187m(bH.W.a(this.f4772F));
        }
        if (this.f4769C[0] != 0) {
            if (!m()) {
                throw new V.a("Unsupported block type: " + ((int) this.f4769C[0]));
            }
            this.f4777l--;
            return c();
        }
        a(this.f4770D);
        c(C0995c.a(this.f4770D, 0, this.f4770D.length, true, false));
        a(this.f4771E);
        a(this.f4773G);
        byte b2 = 0;
        for (int i2 = 0; i2 < this.f4771E.length; i2++) {
            b2 = (byte) (b2 + this.f4771E[i2]);
        }
        if (b2 != this.f4773G[0]) {
            int i3 = this.f4783I;
            bH.C.b("Record " + this.f4777l + " CRC does not match! Expected: " + C0995c.b((byte) (b2 & 255)) + ", found: " + C0995c.b(this.f4773G[0]) + ", Record start index: 0x" + Long.toHexString(this.f4775j - i3).toUpperCase() + ", Record Size: " + i3);
            if (m()) {
                return c();
            }
        }
        float[] fArr = new float[this.f4768i];
        for (int i4 = 0; i4 < fArr.length; i4++) {
            fArr[i4] = ((ar) this.f4786t.get(i4)).a(this.f4771E);
        }
        this.f4777l++;
        return fArr;
    }

    private boolean m() throws V.a {
        int i2 = 0;
        bH.C.c("Corrupt data in log, attempting to resync.NaN");
        boolean z2 = false;
        byte b2 = this.f4778m;
        long j2 = this.f4775j;
        int i3 = 0;
        while (i3 < 3) {
            while (true) {
                if (!z2) {
                    try {
                        this.f4756w.reset();
                    } catch (IOException e2) {
                        bH.C.d("Unable to reset for resync");
                    }
                    a(this.f4774H);
                    i2++;
                    this.f4756w.mark(this.f4783I * 3);
                }
                a(this.f4769C);
                if (this.f4769C[0] == 0) {
                    a(this.f4770D);
                    a(this.f4771E);
                    a(this.f4773G);
                    int i4 = 0;
                    for (int i5 = 0; i5 < this.f4771E.length; i5++) {
                        i4 += this.f4771E[i5];
                    }
                    z2 = i3 > 0 ? ((byte) (i4 & 255)) == this.f4773G[0] && this.f4769C[1] == b2 + 1 : ((byte) (i4 & 255)) == this.f4773G[0];
                    if (z2) {
                        b2 = this.f4769C[1];
                    } else {
                        i3 = 0;
                        b2 = -1;
                    }
                    if (z2) {
                        break;
                    }
                }
            }
            if (i3 > 0) {
                bH.C.c("Resync: Consecutive Records Passed crc and counter: " + i3);
            }
            z2 = true;
            i3++;
        }
        try {
            this.f4756w.reset();
            bH.C.d("Found 3 valid record(s), ReSynced. Bytes Skipped: " + i2);
            this.f4775j = j2 + i2;
            return true;
        } catch (IOException e3) {
            bH.C.d("Resync failed, invalid mark");
            return false;
        }
    }

    private long c(int i2) {
        if (this.f4782q == -1) {
            this.f4781p = 0L;
        } else if (i2 > this.f4782q) {
            this.f4781p += (i2 - this.f4782q) * 10;
        } else {
            this.f4781p = (long) (this.f4781p + (((Math.pow(2.0d, 16.0d) + i2) - this.f4782q) * 10.0d));
        }
        this.f4782q = i2;
        return this.f4781p;
    }

    private byte[] a(byte[] bArr) throws V.a {
        int i2;
        int i3 = 0;
        int i4 = 0;
        do {
            try {
                int i5 = this.f4756w.read(bArr);
                if (i5 != -1) {
                    i4 += i5;
                } else {
                    if (i3 > 8) {
                        throw new V.f("End of file on record " + this.f4777l + ".");
                    }
                    bH.C.c("MLVLG EOF Reported, waiting and trying again.");
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e2) {
                        Logger.getLogger(an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                if (i4 >= bArr.length) {
                    break;
                }
                i2 = i3;
                i3++;
            } catch (IOException e3) {
                Logger.getLogger(an.class.getName()).log(Level.WARNING, "Unable to read from file.", (Throwable) e3);
                throw new V.a("IO Error reading from file on record " + this.f4777l + ".");
            }
        } while (i2 < 10);
        this.f4775j += bArr.length;
        return bArr;
    }

    @Override // W.V
    public long d() {
        return this.f4779n;
    }

    @Override // W.V
    public boolean e() {
        try {
            if (this.f4756w.available() > 0 && this.f4756w.available() < this.f4771E.length) {
                bH.C.b("File read ending prematurely. There is remaining data, but not enough for a full record.");
            }
            return this.f4756w.available() > this.f4771E.length;
        } catch (IOException e2) {
            Logger.getLogger(an.class.getName()).log(Level.WARNING, "File Closed or ended", (Throwable) e2);
            return false;
        }
    }

    @Override // W.V
    public boolean f() {
        return false;
    }

    @Override // W.V
    public HashMap g() {
        return this.f4787u;
    }

    @Override // W.V
    public String h() {
        return this.f4788v;
    }
}
