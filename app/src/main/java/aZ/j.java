package aZ;

import G.C0043ac;
import G.C0052al;
import G.C0126i;
import G.R;
import G.T;
import G.aH;
import L.X;
import W.aq;
import ac.q;
import ax.U;
import bH.C;
import bH.C0995c;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: TunerStudioMS.jar:aZ/j.class */
public class j extends a {

    /* renamed from: F, reason: collision with root package name */
    public static int f4149F = 0;

    /* renamed from: G, reason: collision with root package name */
    public static int f4150G = 1;

    /* renamed from: H, reason: collision with root package name */
    public static int f4151H = 2;

    /* renamed from: I, reason: collision with root package name */
    public static int f4152I = 3;

    /* renamed from: J, reason: collision with root package name */
    public static int f4153J = 32;

    /* renamed from: K, reason: collision with root package name */
    public static int f4154K = 0;

    /* renamed from: L, reason: collision with root package name */
    public static int f4155L = 1;

    /* renamed from: p, reason: collision with root package name */
    boolean f4132p = true;

    /* renamed from: q, reason: collision with root package name */
    int f4133q = 128;

    /* renamed from: r, reason: collision with root package name */
    k f4134r = new k(this, 25, 1);

    /* renamed from: s, reason: collision with root package name */
    k f4135s = new k(this, 26, 4);

    /* renamed from: t, reason: collision with root package name */
    k f4136t = new k(this, 4, 20);

    /* renamed from: u, reason: collision with root package name */
    k f4137u = new k(this, 24, 1);

    /* renamed from: v, reason: collision with root package name */
    int[] f4138v = null;

    /* renamed from: w, reason: collision with root package name */
    int f4139w = 64;

    /* renamed from: x, reason: collision with root package name */
    int f4140x = 0;

    /* renamed from: y, reason: collision with root package name */
    int f4141y = -1;

    /* renamed from: z, reason: collision with root package name */
    double f4142z = 0.0d;

    /* renamed from: N, reason: collision with root package name */
    private ArrayList f4143N = new ArrayList();

    /* renamed from: A, reason: collision with root package name */
    ArrayList f4144A = null;

    /* renamed from: B, reason: collision with root package name */
    ArrayList f4145B = null;

    /* renamed from: C, reason: collision with root package name */
    ArrayList f4146C = null;

    /* renamed from: D, reason: collision with root package name */
    R[] f4147D = null;

    /* renamed from: E, reason: collision with root package name */
    ArrayList f4148E = new ArrayList();

    /* renamed from: O, reason: collision with root package name */
    private int f4156O = 2;

    /* renamed from: P, reason: collision with root package name */
    private int f4157P = -1;

    /* renamed from: M, reason: collision with root package name */
    long f4158M = System.currentTimeMillis();

    public j() {
        a.a(new l(this));
    }

    @Override // aZ.a
    public boolean a(R[] rArr, File file) throws V.a {
        String[] strArrD = d(e(file));
        if (rArr.length < strArrD.length) {
            throw new V.a(a(rArr, strArrD));
        }
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            if (!rArr[i2].i().equals(strArrD[i2])) {
                throw new V.a(a(rArr, strArrD));
            }
        }
        return true;
    }

    /* JADX WARN: Finally extract failed */
    @Override // aZ.a
    public void a(R[] rArr, File file, File file2, boolean z2) throws V.a {
        this.f4147D = rArr;
        this.f4138v = e(file);
        int iC = c(this.f4138v);
        if (iC > this.f4156O) {
            throw new V.a("Unsupported file specification. Supported specification " + this.f4156O + "\nFile level:" + iC);
        }
        this.f4157P = b(this.f4138v);
        if ((this.f4157P & (f4153J ^ (-1))) == f4149F) {
            this.f4139w = 64;
            this.f4133q = 128;
        } else {
            if ((this.f4157P & (f4153J ^ (-1))) != f4151H) {
                throw new V.a("Unsupported log type.");
            }
            this.f4139w = 128;
            this.f4133q = 256;
        }
        if (this.f4138v.length != this.f4133q) {
            this.f4138v = e(file);
        }
        String[] strArrD = d(this.f4138v);
        e((this.f4157P & f4153J) != 0 || d(file));
        this.f4143N = a(rArr, this.f4138v);
        if (rArr.length < strArrD.length) {
            if (z2) {
                throw new V.a(a(rArr, strArrD));
            }
            C.b("Fewer Controllers set up in Project than in log file: " + file.getName());
        }
        String[] strArr = new String[rArr.length];
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            if (!rArr[i2].i().equals(strArrD[i2])) {
                if (z2) {
                    throw new V.a(a(rArr, strArrD));
                }
                C.b(a(rArr, strArrD));
            }
            strArr[i2] = rArr[i2].c();
        }
        b(false);
        a(strArr, file2.getAbsolutePath());
        d(false);
        long jC = X.c();
        try {
            T.a().c().C().d(System.currentTimeMillis() + 20000);
            c(true);
            for (R r2 : rArr) {
                r2.X();
            }
            X.a(true);
            this.f4158M = System.currentTimeMillis();
            X.b(this.f4158M);
            C0126i.a();
            c(file);
            m();
            X.a(false);
            X.b(jC);
            T.a().c().C().d(0L);
            c(false);
            C0126i.a();
            for (R r3 : rArr) {
                r3.Y();
            }
        } catch (Throwable th) {
            X.a(false);
            X.b(jC);
            T.a().c().C().d(0L);
            c(false);
            C0126i.a();
            for (R r4 : rArr) {
                r4.Y();
            }
            throw th;
        }
    }

    private ArrayList a(R[] rArr, int[] iArr) {
        this.f4145B = new ArrayList();
        int[] iArr2 = new int[2];
        int i2 = 30;
        while (i2 < this.f4133q) {
            if ((this.f4157P & (f4153J ^ (-1))) == f4149F && i2 == 64) {
                i2 = 68;
            } else if ((this.f4157P & f4151H) == f4151H && i2 == 128) {
                i2 = 132;
            }
            int i3 = i2;
            int i4 = i2 + 1;
            iArr2[0] = iArr[i3];
            iArr2[1] = iArr[i4];
            int iB = C0995c.b(iArr2, 0, 2, true, false);
            if (iB == 65535) {
                break;
            }
            m mVar = new m(this);
            mVar.e(0);
            aH aHVarA = C0126i.a(rArr[0], iB);
            if (aHVarA != null) {
                mVar.c(aHVarA.l());
                mVar.a(aHVarA.aJ());
            } else {
                C.b("No OuputChannel found for offset " + iB + ", it seems likely the SD card tables have invalid data.");
            }
            mVar.b(iB);
            this.f4145B.add(mVar);
            i2 = i4 + 1;
        }
        this.f4144A = new ArrayList();
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f4145B);
        arrayList.addAll(this.f4146C);
        ArrayList arrayList2 = new ArrayList();
        for (int i5 = 0; i5 < 1; i5++) {
            R r2 = rArr[i5];
            for (int i6 = 0; i6 < arrayList.size(); i6++) {
                Iterator itQ = r2.q();
                while (itQ.hasNext()) {
                    aH aHVar = (aH) itQ.next();
                    if (!aHVar.b().equals("formula") && aHVar.a() >= ((m) arrayList.get(i6)).a() && aHVar.a() <= ((m) arrayList.get(i6)).a() + ((m) arrayList.get(i6)).b()) {
                        this.f4144A.add(aHVar);
                    }
                }
                Iterator it = r2.f().iterator();
                while (it.hasNext()) {
                    aH aHVar2 = (aH) it.next();
                    if (aHVar2.a() == ((m) arrayList.get(i6)).a()) {
                        this.f4144A.add(aHVar2);
                        ((m) arrayList.get(i6)).c(aHVar2.l());
                    }
                }
            }
            Iterator itQ2 = r2.q();
            while (itQ2.hasNext()) {
                aH aHVar3 = (aH) itQ2.next();
                if (aHVar3.b().equals("formula") && a(r2, this.f4144A, aHVar3.aJ())) {
                    this.f4144A.add(aHVar3);
                }
            }
            Iterator it2 = r2.g().iterator();
            while (it2.hasNext()) {
                C0043ac c0043ac = (C0043ac) it2.next();
                if (a(r2, c0043ac) && a(c0043ac.a(), this.f4144A)) {
                    q qVar = new q();
                    if (i5 == 0) {
                        qVar.a(c0043ac.b());
                    } else {
                        qVar.a(rArr[i5].c() + "." + c0043ac.b());
                    }
                    qVar.a(c0043ac);
                    qVar.a(i5);
                    qVar.a(rArr[i5].g(c0043ac.a()));
                    arrayList2.add(qVar);
                }
            }
            Iterator it3 = r2.f().iterator();
            while (it3.hasNext()) {
                C0052al c0052al = (C0052al) it3.next();
                if (this.f4144A.contains(c0052al)) {
                    q qVar2 = new q();
                    C0043ac c0043ac2 = new C0043ac();
                    c0043ac2.c(c0052al.aJ());
                    c0043ac2.v(c0052al.aJ());
                    c0043ac2.b(c0052al.aJ());
                    c0043ac2.a(c0052al.d());
                    if (i5 == 0) {
                        qVar2.a(c0043ac2.b());
                    } else {
                        qVar2.a(rArr[i5].c() + "." + c0043ac2.b());
                    }
                    qVar2.a(c0043ac2);
                    qVar2.a(i5);
                    qVar2.a(c0052al);
                    arrayList2.add(qVar2);
                    int iA = c0052al.a() + c0052al.l();
                    this.f4141y = iA > this.f4141y ? iA : this.f4141y;
                }
            }
        }
        return arrayList2;
    }

    @Override // ac.C0491c
    public double f() {
        return this.f4142z;
    }

    private boolean a(String str, ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            if (((aH) it.next()).aJ().equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Override // ac.h
    protected ArrayList a(R[] rArr) {
        return this.f4143N;
    }

    private int a(int i2) {
        int iN = this.f4147D[i2].O().n();
        if (iN < this.f4141y) {
            iN = this.f4141y;
        }
        return iN;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x00f5, code lost:
    
        bH.C.d("SD Transformer, Time jump, log assumed to end. Last Time:" + r6.f4142z + ", current Time:" + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x011c, code lost:
    
        r6.f4142z = 0.0d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0120, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x012a, code lost:
    
        bH.C.b("Ms3SdLogTransformer: Failed to close file???");
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0263, code lost:
    
        bH.C.d("No GPS Data found in 1st 300, assuming not GPS interleaving");
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0283, code lost:
    
        r6.f4142z = 0.0d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0287, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0291, code lost:
    
        bH.C.b("Ms3SdLogTransformer: Failed to close file???");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean d(java.io.File r7) {
        /*
            Method dump skipped, instructions count: 796
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: aZ.j.d(java.io.File):boolean");
    }

    protected void c(File file) {
        int i2 = this.f4133q;
        long length = file.length();
        double d2 = 0.0d;
        BufferedInputStream bufferedInputStream = null;
        byte b2 = -2147483648;
        try {
            try {
                try {
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    bufferedInputStream.skip(i2);
                    byte[] bArr = new byte[4];
                    byte[] bArr2 = new byte[this.f4139w - (bArr.length * 2)];
                    byte[] bArr3 = new byte[a(0)];
                    this.f4140x = 0;
                    double d3 = 0.0d;
                    int i3 = 0;
                    int i4 = 0;
                    int i5 = 0;
                    while (true) {
                        if (!this.f4132p) {
                            break;
                        }
                        i5++;
                        if (bufferedInputStream.read(bArr) != bArr.length) {
                            C.c("Block is short in length, done processing.");
                            break;
                        }
                        byte b3 = bArr[0];
                        int iA = C0995c.a(bArr, 1, 3, true, false);
                        if (bufferedInputStream.read(bArr) != bArr.length) {
                            C.c("Block is short in length, done processing.");
                            break;
                        }
                        double dA = C0995c.a(bArr, 0, 4, true, false) * 1.28E-4d;
                        if (this.f4142z == 0.0d) {
                            d3 = dA + 0.001d;
                        } else if ((dA - d3) - this.f4142z > 30.0d || (dA - d3) - this.f4142z < 0.0d) {
                            C.d("SD Transformer, Time jump. Last Time:" + this.f4142z + ", current Time:" + dA);
                            i3++;
                            bufferedInputStream.read(bArr2);
                            if (i3 > 2) {
                                C.d("SD Transformer, 3 Time jumps, log assumed to end. Last Time:" + this.f4142z + ", current Time:" + dA);
                                break;
                            }
                        }
                        this.f4142z = dA - d3;
                        X.a(X.c() + Math.round(this.f4142z * 1000.0d));
                        i3 = 0;
                        if (bufferedInputStream.read(bArr2) != bArr2.length) {
                            C.c("Block is short in length, done processing.");
                            break;
                        }
                        if (b2 == -2147483648) {
                            b2 = bArr2[bArr2.length - 1];
                        } else if (b2 != bArr2[bArr2.length - 1]) {
                            C.c("block " + this.f4140x + ", blockNumber " + iA + " wrong magic number.");
                            i4++;
                            if (i4 > 2) {
                                C.d("SD Transformer, 3 Bad magic numbers, log assumed to end.");
                                break;
                            }
                        }
                        i4 = 0;
                        if (!a(bArr2)) {
                            C.c("block " + this.f4140x + " unwritten, done processing.");
                            break;
                        }
                        this.f4140x++;
                        int iB = 0;
                        if (b3 == f4155L) {
                            Iterator it = this.f4146C.iterator();
                            while (it.hasNext()) {
                                m mVar = (m) it.next();
                                System.arraycopy(bArr2, iB, bArr3, mVar.a(), mVar.b());
                                iB += mVar.b();
                            }
                        } else if (b3 == f4154K) {
                            Iterator it2 = this.f4145B.iterator();
                            while (it2.hasNext()) {
                                m mVar2 = (m) it2.next();
                                if (mVar2.c() == 0) {
                                    if (mVar2.b() >= 0 && (mVar2.a() + mVar2.b()) - 1 < bArr3.length) {
                                        System.arraycopy(bArr2, iB + mVar2.d(), bArr3, mVar2.a(), mVar2.b());
                                        if (i5 == 1) {
                                            C.c("blockOffset = " + iB + ", adding " + mVar2.b() + ", channel offset = 0x" + Integer.toHexString(mVar2.a()).toUpperCase() + "/" + mVar2.a() + ", channel = " + mVar2.e());
                                        }
                                        iB += mVar2.b();
                                    } else if ((mVar2.a() + mVar2.b()) - 1 >= bArr3.length) {
                                        C.b("Offset for SD log transformation: " + mVar2.a() + ", You may need to activate InternalFields to transform these fields.");
                                        it2.remove();
                                    } else if (mVar2.b() < 0) {
                                        C.b("Offset for SD log transformation: " + mVar2.a() + " is invalid for the current configuration, removing.");
                                        it2.remove();
                                    } else {
                                        C.c("Code error, shouldn't get here...");
                                    }
                                }
                            }
                            a(this.f4147D[0].c(), bArr3);
                            double d4 = (this.f4140x * this.f4139w) / (length - i2);
                            if (d4 - d2 > 0.01d) {
                                a(d4);
                                d2 = d4;
                            }
                        } else {
                            C.b("Unknown blockType! " + ((int) b3));
                        }
                    }
                    if (this.f4132p) {
                        y();
                    }
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e2) {
                        C.b("Ms3SdLogTransformer: Failed to close file???");
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                    throw new V.a("Problem reading from file:\n" + file.getAbsolutePath());
                } catch (Exception e4) {
                    e4.printStackTrace();
                    throw new V.a("Invalid MS3 SD file format:\n" + file.getAbsolutePath());
                }
            } catch (FileNotFoundException e5) {
                throw new V.a("File not found:\n" + file.getAbsolutePath());
            }
        } catch (Throwable th) {
            try {
                bufferedInputStream.close();
            } catch (IOException e6) {
                C.b("Ms3SdLogTransformer: Failed to close file???");
            }
            throw th;
        }
    }

    @Override // aZ.a
    public void w() {
        this.f4132p = false;
    }

    private String a(R[] rArr, String[] strArr) {
        String str = "SD Log file serial signature does not match that of the project.\nConvert a file may produce corrupt data. To avoid this message \nPlease open a project with the ECU Definition(ini) that matches the firmware used\n when you captured the SD log file.\n";
        int i2 = 0;
        while (i2 < strArr.length) {
            str = ((i2 >= rArr.length || rArr[i2] == null || rArr[i2].i() == null) ? str + "\nProject Signature " + (i2 + 1) + ": none " : str + "\nProject Serial Signature " + (i2 + 1) + ": " + rArr[i2].i()) + "\nMS3 SD File Signature " + (i2 + 1) + ": " + strArr[i2] + "\n";
            i2++;
        }
        return str;
    }

    private boolean a(R r2, ArrayList arrayList, String str) {
        aH aHVarG = r2.g(str);
        if (C0126i.a(str) || r2.c(str) != null) {
            return true;
        }
        if (aHVarG == null || !aHVarG.b().equals("formula")) {
            return arrayList.contains(aHVarG);
        }
        String[] strArrF = null;
        try {
            strArrF = C0126i.f(aHVarG.k(), r2);
        } catch (U e2) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (strArrF == null) {
            return false;
        }
        for (String str2 : strArrF) {
            if (!a(r2, arrayList, str2)) {
                return false;
            }
        }
        return true;
    }

    private boolean a(byte[] bArr) {
        for (byte b2 : bArr) {
            if (b2 != 255) {
                return true;
            }
        }
        return false;
    }

    private void b(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("\"Capture Time:" + a(this.f4138v).toString());
        String[] strArrD = null;
        try {
            strArrD = d(this.f4138v);
        } catch (V.a e2) {
            Logger.getLogger(j.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        printWriter.write("\"Signatures:");
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            printWriter.print(strArrD[i2]);
            if (i2 < strArrD.length - 1) {
                printWriter.write(VectorFormat.DEFAULT_SEPARATOR);
            }
        }
        printWriter.print("\n");
    }

    @Override // aZ.a
    public int x() {
        return this.f4140x;
    }

    public Date a(int[] iArr) {
        return new Date(this.f4135s.a(iArr) * 1000);
    }

    private int[] e(File file) {
        FileInputStream fileInputStream = null;
        try {
            try {
                try {
                    fileInputStream = new FileInputStream(file);
                    int[] iArr = new int[this.f4133q];
                    for (int i2 = 0; i2 < iArr.length; i2++) {
                        iArr[i2] = fileInputStream.read();
                    }
                    try {
                        fileInputStream.close();
                    } catch (IOException e2) {
                        C.b("MS3 SD Log Transformer: Failed to close file???");
                    }
                    return iArr;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    throw new V.a("Failed to read header from file:\n" + file.getAbsolutePath());
                }
            } catch (FileNotFoundException e4) {
                throw new V.a("File not found:\n" + file.getAbsolutePath());
            }
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (IOException e5) {
                C.b("MS3 SD Log Transformer: Failed to close file???");
            }
            throw th;
        }
    }

    private int b(int[] iArr) {
        return iArr[24];
    }

    private int c(int[] iArr) {
        return iArr[25];
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0053, code lost:
    
        r0 = new byte[r11];
        r14 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0061, code lost:
    
        if (r14 >= r0.length) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0064, code lost:
    
        r0[r14] = r0[r14];
        r14 = r14 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0074, code lost:
    
        r0[0] = new java.lang.String(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0081, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String[] d(int[] r7) throws V.a {
        /*
            r6 = this;
            r0 = 1
            java.lang.String[] r0 = new java.lang.String[r0]
            r8 = r0
            r0 = 4
            r9 = r0
            r0 = 20
            r10 = r0
            r0 = 20
            r11 = r0
            r0 = r10
            byte[] r0 = new byte[r0]
            r12 = r0
            r0 = 0
            r13 = r0
        L18:
            r0 = r13
            r1 = r10
            if (r0 >= r1) goto L53
            r0 = r12
            r1 = r13
            r2 = r7
            r3 = r13
            r4 = r9
            int r3 = r3 + r4
            r2 = r2[r3]
            byte r2 = (byte) r2
            r0[r1] = r2
            r0 = r12
            r1 = r13
            r0 = r0[r1]
            if (r0 != 0) goto L3a
            r0 = r13
            r11 = r0
            goto L53
        L3a:
            r0 = r13
            r1 = r10
            r2 = 1
            int r1 = r1 - r2
            if (r0 != r1) goto L4d
            V.a r0 = new V.a
            r1 = r0
            java.lang.String r2 = "Unexpected Signature length.\nExpected null terminator at position 20."
            r1.<init>(r2)
            throw r0
        L4d:
            int r13 = r13 + 1
            goto L18
        L53:
            r0 = r11
            byte[] r0 = new byte[r0]
            r13 = r0
            r0 = 0
            r14 = r0
        L5c:
            r0 = r14
            r1 = r13
            int r1 = r1.length
            if (r0 >= r1) goto L74
            r0 = r13
            r1 = r14
            r2 = r12
            r3 = r14
            r2 = r2[r3]
            r0[r1] = r2
            int r14 = r14 + 1
            goto L5c
        L74:
            r0 = r8
            r1 = 0
            java.lang.String r2 = new java.lang.String
            r3 = r2
            r4 = r13
            r3.<init>(r4)
            r0[r1] = r2
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: aZ.j.d(int[]):java.lang.String[]");
    }

    @Override // ac.h
    public void a(String[] strArr, String str) throws V.a {
        this.f4203h = true;
        a(0.0d);
        String[] strArr2 = {strArr[0]};
        T tA = T.a();
        R[] rArr = new R[strArr2.length];
        for (int i2 = 0; i2 < strArr2.length; i2++) {
            rArr[i2] = tA.c(strArr2[i2]);
            if (rArr[i2] == null) {
                throw new V.a("Configuration '" + strArr2[i2] + "' not currently loaded. \nCan't start data Log.");
            }
        }
        try {
            this.f4201g = e(str);
            b(this.f4201g);
            a(rArr, this.f4201g);
            for (int i3 = 0; i3 < rArr.length; i3++) {
                this.f4197d.add(new ac.m(this, strArr2[i3], i3, rArr[i3].O().n()));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Could not create file " + str);
        }
    }

    @Override // aZ.a
    public void a(aq aqVar) {
        this.f4148E.add(aqVar);
    }

    private void a(double d2) {
        Iterator it = this.f4148E.iterator();
        while (it.hasNext()) {
            try {
                ((aq) it.next()).a(d2);
            } catch (Exception e2) {
                C.b("Exeption caught during progress notification. Continuing...");
                e2.printStackTrace();
            }
        }
    }

    private void y() {
        Iterator it = this.f4148E.iterator();
        while (it.hasNext()) {
            try {
                ((aq) it.next()).a();
            } catch (Exception e2) {
                C.b("Exeption caught during progress notification (Complete). Continuing...");
                e2.printStackTrace();
            }
        }
    }

    private ArrayList e(boolean z2) {
        this.f4146C = new ArrayList();
        if (z2) {
            int iL = 0;
            for (String str : new String[]{"gps_latdeg", "gps_latmin", "gps_latmmin", "gps_londeg", "gps_lonmin", "gps_lonmmin", "gps_outstatus", "gps_altk", "gps_altm", "gps_speed", "gps_course"}) {
                aH aHVarG = this.f4147D[0].g(str);
                if (aHVarG != null) {
                    m mVar = new m(this);
                    mVar.b(aHVarG.a());
                    mVar.c(aHVarG.l());
                    mVar.a(iL);
                    this.f4146C.add(mVar);
                    iL += aHVarG.l();
                }
            }
        }
        return this.f4146C;
    }
}
