package av;

import G.C0072be;
import G.C0094c;
import G.aM;
import java.awt.AWTEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: av.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/h.class */
public class C0869h extends AbstractC0868g {

    /* renamed from: e, reason: collision with root package name */
    private List f6287e;

    /* renamed from: a, reason: collision with root package name */
    protected static C0869h f6288a = null;

    public static C0869h f() {
        if (f6288a == null) {
            f6288a = new C0869h();
        }
        return f6288a;
    }

    private C0869h() {
        a("SingleDtaMlvConfigInstance", "./inc/dtaTables.ecu");
    }

    public void a(String str) throws V.h {
        try {
            c(str);
            a("rpmBins", 3);
            a("mapBins", 1905);
            a("tpsBins", AWTEvent.RESERVED_ID_MAX);
            b("fuelData", 303);
            b("sparkData", 23);
            b("lambdaData", 2333);
            String str2 = "mapBins";
            String str3 = "MAP";
            if (((String) this.f6287e.get(1903)).trim().equals("0")) {
                str2 = "tpsBins";
                str3 = "TPS";
            }
            h.i.c("yAxisField", str3);
            C0094c c0094c = new C0094c(str3);
            Iterator itN = this.f6284b.n();
            while (itN.hasNext()) {
                C0072be c0072be = (C0072be) itN.next();
                c0072be.b(str2);
                c0072be.e(str3);
                c0072be.b(c0094c);
            }
            this.f6284b.h().g();
            g();
        } catch (Exception e2) {
            Logger.getLogger(C0869h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.h("Failed to load tune file:\n" + str + "\nReported Error:\n" + e2.getMessage());
        }
    }

    @Override // ao.hF
    public void j(String str) throws V.h {
        if (this.f6287e == null || this.f6287e.isEmpty() || str == null || str.isEmpty()) {
            return;
        }
        try {
            c("fuelData", 303);
            c("sparkData", 23);
            c("lambdaData", 2333);
            d(str);
            this.f6284b.h().g();
        } catch (Exception e2) {
            Logger.getLogger(C0869h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.h("Failed to save tune file:\n" + str + "\nReported Error:\n" + e2.getMessage());
        }
    }

    private int a(String str, int i2) {
        aM aMVarC = this.f6284b.c(str);
        double dA = aMVarC.E().a();
        int iB = aMVarC.b();
        double[][] dArr = new double[iB][1];
        int i3 = i2 - 1;
        for (int i4 = 0; i4 < iB; i4++) {
            dArr[i4][0] = Double.parseDouble((String) this.f6287e.get(i3 + i4)) * dA;
        }
        this.f6284b.a(str, dArr);
        return iB;
    }

    private void b(String str, int i2) {
        aM aMVarC = this.f6284b.c(str);
        double dA = aMVarC.E().a();
        int iA = (int) aMVarC.c().f293b.a();
        int iA2 = (int) aMVarC.c().f292a.a();
        double[][] dArr = new double[iA][iA2];
        int i3 = i2 - 1;
        for (int i4 = 0; i4 < iA; i4++) {
            for (int i5 = 0; i5 < iA2; i5++) {
                dArr[i4][i5] = Integer.parseInt(((String) this.f6287e.get(i3 + (i4 * iA2) + i5)).trim()) * dA;
            }
        }
        this.f6284b.a(str, dArr);
    }

    private void c(String str, int i2) throws V.g {
        aM aMVarC = this.f6284b.c(str);
        double dA = aMVarC.E().a();
        int iA = (int) aMVarC.c().f293b.a();
        int iA2 = (int) aMVarC.c().f292a.a();
        double[][] dArrI = aMVarC.i(this.f6284b.p());
        int i3 = i2 - 1;
        for (int i4 = 0; i4 < iA; i4++) {
            for (int i5 = 0; i5 < iA2; i5++) {
                this.f6287e.set(i3 + (i4 * iA2) + i5, String.format(" %d ", Long.valueOf(Math.round(dArrI[i4][i5] / dA))));
            }
        }
    }

    private void c(String str) {
        this.f6287e = new ArrayList(2000);
        BufferedReader bufferedReader = null;
        try {
            try {
                bufferedReader = new BufferedReader(new FileReader(str));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        this.f6287e.add(line);
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (Exception e3) {
                Logger.getLogger(C0869h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e4) {
                    }
                }
            }
        } catch (Throwable th) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
    }

    private void d(String str) {
        if (this.f6287e == null || this.f6287e.isEmpty()) {
            return;
        }
        File file = new File(str);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        BufferedWriter bufferedWriter = null;
        try {
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(str));
                Iterator it = this.f6287e.iterator();
                while (it.hasNext()) {
                    bufferedWriter.write((String) it.next());
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e2) {
                    }
                }
            } catch (Exception e3) {
                Logger.getLogger(C0869h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e4) {
                    }
                }
            }
        } catch (Throwable th) {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
    }
}
