package aZ;

import G.R;
import G.T;
import W.aq;
import bH.C;
import bH.W;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.VectorFormat;
import r.C1806i;

/* loaded from: TunerStudioMS.jar:aZ/f.class */
public class f extends a {

    /* renamed from: p, reason: collision with root package name */
    boolean f4104p = true;

    /* renamed from: q, reason: collision with root package name */
    int f4105q = 81;

    /* renamed from: r, reason: collision with root package name */
    g f4106r = new g(this, 6, 2);

    /* renamed from: s, reason: collision with root package name */
    g f4107s = new g(this, 8, 4);

    /* renamed from: t, reason: collision with root package name */
    g f4108t = new g(this, 75, 4);

    /* renamed from: u, reason: collision with root package name */
    g f4109u = new g(this, 79, 2);

    /* renamed from: v, reason: collision with root package name */
    g f4110v = new g(this, 81, 2);

    /* renamed from: w, reason: collision with root package name */
    int[] f4111w = null;

    /* renamed from: x, reason: collision with root package name */
    int f4112x = 0;

    /* renamed from: y, reason: collision with root package name */
    int f4113y = 31;

    /* renamed from: E, reason: collision with root package name */
    private byte[] f4114E = null;

    /* renamed from: z, reason: collision with root package name */
    List f4115z = new ArrayList();

    /* renamed from: A, reason: collision with root package name */
    int f4116A = 1;

    /* renamed from: B, reason: collision with root package name */
    R[] f4117B = null;

    /* renamed from: C, reason: collision with root package name */
    ArrayList f4118C = new ArrayList();

    /* renamed from: D, reason: collision with root package name */
    int f4119D = 0;

    public f() {
        a(true);
    }

    @Override // aZ.a
    public boolean a(R[] rArr, File file) throws V.a {
        String[] strArrB = b(c(file));
        if (rArr.length < strArrB.length) {
            throw new V.a(a(rArr, strArrB));
        }
        for (int i2 = 0; i2 < strArrB.length; i2++) {
            if (!rArr[i2].i().equals(strArrB[i2])) {
                throw new V.a(a(rArr, strArrB));
            }
        }
        return true;
    }

    @Override // aZ.a
    public void a(R[] rArr, File file, File file2, boolean z2) throws V.a {
        this.f4117B = rArr;
        this.f4111w = c(file);
        this.f4119D = 0;
        String[] strArrB = b(this.f4111w);
        if (rArr.length < strArrB.length) {
            if (z2) {
                throw new V.a(a(rArr, strArrB));
            }
            C.b("Fewer Controllers set up in Project than in log file: " + file.getName());
        }
        this.f4116A = c(this.f4111w);
        if (this.f4116A > 3) {
            throw new V.a("newer FRD version: " + this.f4116A + ", Only up to FRD version 2.1 supported.");
        }
        if (this.f4116A > 1.0d && !C1806i.a().a("098532oiutewlkjg098")) {
            throw new V.a("FRD version: " + this.f4116A + " only supprted in registered version.");
        }
        if (this.f4116A >= 2) {
            this.f4105q = this.f4108t.a(this.f4111w);
            this.f4111w = c(file);
        }
        String[] strArr = new String[rArr.length];
        for (int i2 = 0; i2 < strArrB.length; i2++) {
            if (!rArr[i2].i().equals(strArrB[i2])) {
                if (z2) {
                    throw new V.a(a(rArr, strArrB));
                }
                C.b(a(rArr, strArrB));
            }
            strArr[i2] = rArr[i2].c();
        }
        if (this.f4116A >= 2) {
            int iA = this.f4110v.a(this.f4111w);
            for (int i3 = 0; i3 < iA; i3++) {
                int[] iArr = new int[this.f4113y];
                System.arraycopy(this.f4111w, 83 + (i3 * this.f4113y), iArr, 0, iArr.length);
                i iVar = new i();
                iVar.a(iArr);
                this.f4115z.add(iVar);
            }
        }
        a(strArr, file2.getAbsolutePath());
        a(file, this.f4108t.a(this.f4111w), this.f4109u.a(this.f4111w));
        l();
    }

    /* JADX WARN: Code restructure failed: missing block: B:116:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x038b, code lost:
    
        if (r8.f4104p == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x038e, code lost:
    
        y();
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0394, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x039c, code lost:
    
        bH.C.b("FrdLoTransformer: Failed to close file???");
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x03fd, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void a(java.io.File r9, int r10, int r11) {
        /*
            Method dump skipped, instructions count: 1022
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: aZ.f.a(java.io.File, int, int):void");
    }

    @Override // ac.C0491c
    protected StringBuilder a(StringBuilder sb) {
        byte[] bArr;
        if (!this.f4115z.isEmpty() && this.f4114E != null) {
            byte[] bArr2 = new byte[4];
            byte[] bArr3 = new byte[4];
            byte[] bArr4 = new byte[8];
            int i2 = 0;
            for (int i3 = 0; i3 < this.f4115z.size(); i3++) {
                i iVar = (i) this.f4115z.get(i3);
                if (iVar.d() == i.f4130c) {
                    System.arraycopy(this.f4114E, i2, bArr4, 0, bArr4.length);
                    bArr = bArr4;
                    i2 += 8;
                } else {
                    System.arraycopy(this.f4114E, i2, bArr3, 0, bArr3.length);
                    bArr = bArr3;
                    i2 += 4;
                }
                try {
                    sb.append(g()).append(W.b(iVar.a(bArr), iVar.c()));
                } catch (h e2) {
                    Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    sb.append(g()).append("NaN");
                }
            }
        }
        return sb;
    }

    @Override // ac.C0491c
    protected String a(String str) {
        if (!this.f4115z.isEmpty()) {
            Iterator it = this.f4115z.iterator();
            while (it.hasNext()) {
                str = str + g() + ((i) it.next()).a();
            }
        }
        return str;
    }

    @Override // ac.C0491c
    protected String b(String str) {
        if (!this.f4115z.isEmpty()) {
            Iterator it = this.f4115z.iterator();
            while (it.hasNext()) {
                str = str + g() + ((i) it.next()).b();
            }
        }
        return str;
    }

    @Override // ac.C0491c, ac.h
    protected void a(OutputStream outputStream, String str) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        b();
        printWriter.print("MARK " + W.a("" + c(), '0', 3) + " - " + str + " - " + new Date().toString() + this.f4182a);
        printWriter.flush();
    }

    @Override // aZ.a
    public void w() {
        this.f4104p = false;
    }

    private String a(R[] rArr, String[] strArr) {
        String str = "FRD Log file signature does not match that of the project.\nThe converted file may appear corrupt. To avoid this message \nPlease open a project with the ECU Definition(ini) that matches the firmware used\n when you captured the FRD log file.\n";
        int i2 = 0;
        while (i2 < strArr.length) {
            str = ((i2 >= rArr.length || rArr[i2] == null || rArr[i2].i() == null) ? str + "\nProject Signature " + (i2 + 1) + ": none " : str + "\nProject Serial Signature " + (i2 + 1) + ": " + rArr[i2].i()) + "\nFRD File Signature " + (i2 + 1) + ": " + strArr[i2] + "\n";
            i2++;
        }
        return str;
    }

    private void b(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("\"Capture Time:" + a(this.f4111w).toString());
        String[] strArrB = b(this.f4111w);
        printWriter.write("\"Signatures:");
        for (int i2 = 0; i2 < strArrB.length; i2++) {
            printWriter.print(strArrB[i2]);
            if (i2 < strArrB.length - 1) {
                printWriter.write(VectorFormat.DEFAULT_SEPARATOR);
            }
        }
        printWriter.print("\n");
        try {
            outputStream.flush();
        } catch (Exception e2) {
            Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public Date a(int[] iArr) {
        return new Date(this.f4107s.a(iArr) * 1000);
    }

    private int[] c(File file) {
        FileInputStream fileInputStream = null;
        try {
            try {
                try {
                    fileInputStream = new FileInputStream(file);
                    int[] iArr = new int[this.f4105q];
                    for (int i2 = 0; i2 < iArr.length; i2++) {
                        iArr[i2] = fileInputStream.read();
                    }
                    try {
                        fileInputStream.close();
                    } catch (IOException e2) {
                        C.b("FrdLogTransformer: Failed to close file???");
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
                C.b("FrdLogTransformer: Failed to close file???");
            }
            throw th;
        }
    }

    private String[] b(int[] iArr) {
        String[] strArr = new String[1];
        int i2 = -1;
        byte[] bArr = new byte[40];
        int i3 = 0;
        while (true) {
            if (i3 >= 40) {
                break;
            }
            bArr[i3] = (byte) iArr[i3 + 12];
            if (bArr[i3] == 0) {
                i2 = i3;
                break;
            }
            i3++;
        }
        byte[] bArr2 = new byte[i2];
        for (int i4 = 0; i4 < bArr2.length; i4++) {
            bArr2[i4] = bArr[i4];
        }
        strArr[0] = new String(bArr2);
        return strArr;
    }

    @Override // ac.h
    public void l() throws V.a {
        this.f4203h = false;
        for (int i2 = 0; i2 < this.f4197d.size(); i2++) {
            try {
                String strA = ((ac.m) this.f4197d.get(i2)).a();
                if (T.a().c(strA) == null) {
                    throw new V.a("Configuration '" + strA + "' not currently loaded. \nCan't stop data Log.");
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new V.a("Could Not close log file, error\n" + e2.getMessage());
            }
        }
        a(this.f4201g);
        this.f4201g.close();
        if (this.f4206k != null) {
            this.f4206k.a();
        }
    }

    @Override // ac.h
    public void a(String[] strArr, String str) throws V.a {
        this.f4203h = true;
        a(0.0d);
        T tA = T.a();
        R[] rArr = new R[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            rArr[i2] = tA.c(strArr[i2]);
            if (rArr[i2] == null) {
                throw new V.a("Configuration '" + strArr[i2] + "' not currently loaded. \nCan't start data Log.");
            }
        }
        try {
            this.f4201g = e(str);
            b(this.f4201g);
            a(rArr, this.f4201g);
            for (int i3 = 0; i3 < rArr.length; i3++) {
                this.f4197d.add(new ac.m(this, strArr[i3], i3, rArr[i3].O().n()));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Could not create file " + str);
        }
    }

    @Override // aZ.a
    public void a(aq aqVar) {
        this.f4118C.add(aqVar);
    }

    private void a(double d2) {
        Iterator it = this.f4118C.iterator();
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
        Iterator it = this.f4118C.iterator();
        while (it.hasNext()) {
            try {
                ((aq) it.next()).a();
            } catch (Exception e2) {
                C.b("Exeption caught during progress notification (Complete). Continuing...");
                e2.printStackTrace();
            }
        }
    }

    @Override // aZ.a
    public int x() {
        return this.f4112x;
    }

    private int c(int[] iArr) {
        return (iArr[6] * 256) + iArr[7];
    }
}
