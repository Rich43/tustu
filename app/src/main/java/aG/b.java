package aG;

import G.C0069bb;
import G.C0116cv;
import G.C0122e;
import G.C0126i;
import G.C0130m;
import G.C0132o;
import G.InterfaceC0131n;
import G.R;
import G.aH;
import G.aM;
import G.da;
import bH.C;
import bH.C0995c;
import bH.W;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aG/b.class */
public class b {

    /* renamed from: a, reason: collision with root package name */
    R f2383a;

    /* renamed from: c, reason: collision with root package name */
    private boolean f2384c = true;

    /* renamed from: d, reason: collision with root package name */
    private boolean f2385d = false;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f2386b = new ArrayList();

    public b(R r2) {
        this.f2383a = r2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int a(File file, InterfaceC0131n interfaceC0131n) throws RemoteAccessException {
        a("Reading Replay Data");
        int[] iArrA = a(interfaceC0131n);
        byte[] bArrA = C0995c.a(iArrA);
        long jCurrentTimeMillis = System.currentTimeMillis();
        C.c("Read " + iArrA.length + " Replay bytes, begin file write.");
        a("Generating Replay Log file");
        interfaceC0131n.a(0.0d);
        int iAi = this.f2383a.O().ai();
        int length = bArrA.length / iAi;
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            BufferedWriter bufferedWriter = null;
            try {
                try {
                    try {
                        bufferedWriter = new BufferedWriter(new FileWriter(file));
                        bufferedWriter.write("Replay Data Uploaded on: ");
                        bufferedWriter.append((CharSequence) new Date().toString());
                        bufferedWriter.newLine();
                        bufferedWriter.append((CharSequence) (length + ",Replay Records Used"));
                        bufferedWriter.newLine();
                        bufferedWriter.append((CharSequence) "Record #").append((CharSequence) ",");
                        bufferedWriter.append((CharSequence) "Time").append((CharSequence) ",");
                        this.f2383a.t();
                        ArrayList arrayListU = c() ? this.f2383a.u() : this.f2383a.t();
                        ArrayList arrayList = new ArrayList();
                        int size = arrayListU.size();
                        for (int i2 = 0; i2 < size; i2++) {
                            C0069bb c0069bb = (C0069bb) arrayListU.get(i2);
                            if (c0069bb.s()) {
                                arrayList.add(c0069bb);
                                bufferedWriter.append((CharSequence) c0069bb.aJ());
                                if (i2 < size - 1) {
                                    bufferedWriter.append((CharSequence) ",");
                                }
                            }
                        }
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        int size2 = arrayList.size();
                        byte[] bArr = new byte[iAi];
                        float f2 = 0.02f;
                        if (c() && this.f2383a.c("AFRM_Hdw_Cfg") != null) {
                            switch ((int) this.f2383a.c("AFRM_Hdw_Cfg").j(this.f2383a.h())) {
                                case 0:
                                    f2 = 0.02f;
                                    break;
                                case 1:
                                    f2 = 0.04f;
                                    break;
                                case 2:
                                    f2 = 0.06f;
                                    break;
                                case 3:
                                    f2 = 0.08f;
                                    break;
                            }
                        }
                        for (int i3 = 0; i3 < length; i3++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(i3).append(",");
                            sb.append(Math.round((1000.0f * f2) * i3) / 1000.0f).append(",");
                            System.arraycopy(bArrA, iAi * i3, bArr, 0, bArr.length);
                            for (int i4 = 0; i4 < size2; i4++) {
                                try {
                                    sb.append(((C0069bb) arrayList.get(i4)).a(bArr));
                                } catch (V.g e2) {
                                    sb.append(" ");
                                }
                                if (i4 < size2 - 1) {
                                    sb.append(",");
                                } else {
                                    sb.append(",");
                                    bufferedWriter.append((CharSequence) sb.toString());
                                    bufferedWriter.newLine();
                                    bufferedWriter.flush();
                                }
                            }
                            interfaceC0131n.a(0.5d * (i3 / length));
                        }
                        bufferedWriter.append((CharSequence) "Raw Log Data...").append((CharSequence) "\n");
                        for (int i5 = 0; i5 < length; i5++) {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(i5).append(",");
                            sb2.append(W.b(Math.round((1000.0f * f2) * i5) / 1000.0f, 2)).append(",");
                            System.arraycopy(bArrA, iAi * i5, bArr, 0, bArr.length);
                            for (int i6 = 0; i6 < size2; i6++) {
                                C0069bb c0069bb2 = (C0069bb) arrayList.get(i6);
                                try {
                                    if (c0069bb2.b().equals("formula")) {
                                        ArrayList arrayListA = C0126i.a(c0069bb2.v(), c0069bb2);
                                        C0069bb c0069bb3 = null;
                                        int i7 = 0;
                                        while (true) {
                                            if (i7 < arrayListA.size()) {
                                                if (!(arrayListA.get(i7) instanceof C0069bb) || ((aH) arrayListA.get(i7)).b().equals("formula")) {
                                                    i7++;
                                                } else {
                                                    c0069bb3 = (C0069bb) arrayListA.get(i7);
                                                }
                                            }
                                        }
                                        if (c0069bb3 != null) {
                                            sb2.append(c0069bb3.c(bArr));
                                        } else {
                                            sb2.append("0");
                                        }
                                    } else {
                                        sb2.append(c0069bb2.c(bArr));
                                    }
                                } catch (V.g e3) {
                                    sb2.append(" ");
                                }
                                if (i6 < size2 - 1) {
                                    sb2.append(",");
                                } else {
                                    sb2.append(",");
                                    bufferedWriter.append((CharSequence) sb2.toString());
                                    if (i6 < length - 1) {
                                        bufferedWriter.newLine();
                                    }
                                    bufferedWriter.flush();
                                }
                            }
                            interfaceC0131n.a(0.5d + (0.5d * (i5 / length)));
                        }
                        interfaceC0131n.a(1.0d);
                        C0132o c0132o = new C0132o();
                        c0132o.a(1);
                        interfaceC0131n.a(c0132o);
                        C.c("Completed Replay File write. Time: " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
                        a("File Generation Complete");
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e4) {
                                Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                            }
                        }
                        return iArrA.length;
                    } catch (IOException e5) {
                        C.a(e5);
                        throw new RemoteAccessException("Unable to write to file: " + file.getAbsolutePath());
                    } catch (IndexOutOfBoundsException e6) {
                        C.a(e6);
                        throw new RemoteAccessException("Error processing Replay Data, check Replay Entry addressing offsets. " + e6.getMessage());
                    }
                } catch (Exception e7) {
                    C.a(e7);
                    throw new RemoteAccessException("Error processing Replay Data, check Replay Entrys. " + e7.getMessage());
                }
            } catch (Throwable th) {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e8) {
                        Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
                    }
                }
                throw th;
            }
        } catch (IOException e9) {
            throw new RemoteAccessException("Unable to create file: " + file.getAbsolutePath());
        }
    }

    public int[] a(InterfaceC0131n interfaceC0131n) {
        this.f2385d = true;
        try {
            boolean zB = b();
            if (c()) {
                int[] iArrC = c(interfaceC0131n);
                this.f2385d = false;
                return iArrC;
            }
            if (!zB) {
                throw new RemoteAccessException("Replay is not currently available.");
            }
            int[] iArrB = b(interfaceC0131n);
            this.f2385d = false;
            return iArrB;
        } catch (Throwable th) {
            this.f2385d = false;
            throw th;
        }
    }

    private boolean b() {
        return c.b(this.f2383a);
    }

    private boolean c() {
        return c.a(this.f2383a);
    }

    public int[] b(InterfaceC0131n interfaceC0131n) throws RemoteAccessException {
        String strAh = this.f2383a.O().ah();
        if (strAh == null) {
            throw new RemoteAccessException("Configuration Error, replayRecordCountParam not set!");
        }
        aM aMVarC = this.f2383a.c(strAh);
        if (aMVarC == null) {
            throw new RemoteAccessException("Configuration Error, replayRecordCountParam " + ((Object) aMVarC) + " not found!");
        }
        a(3000);
        C0116cv.b(this.f2383a, aMVarC.d());
        aM aMVarC2 = this.f2383a.c("Key_On_Baro");
        if (aMVarC2 != null) {
            C0116cv.b(this.f2383a, aMVarC2.d());
        }
        da daVar = new da();
        try {
            int iJ = (int) aMVarC.j(this.f2383a.p());
            try {
                if (this.f2383a.O().T()) {
                    C0122e.a(this.f2383a);
                }
            } catch (V.g e2) {
                C.a(e2);
            }
            C0130m c0130mA = a.a(this.f2383a.O(), iJ);
            c0130mA.b(interfaceC0131n);
            boolean zH = this.f2383a.O().H();
            int iK = this.f2383a.O().k();
            this.f2383a.O().e(false);
            this.f2383a.O().d(45);
            this.f2383a.O().a(false);
            try {
                C0132o c0132oA = daVar.a(this.f2383a, c0130mA, 3000);
                try {
                    a(2000);
                    C0122e.b(this.f2383a);
                } catch (V.g e3) {
                    Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
                if (c0132oA.a() == 3) {
                    throw new RemoteAccessException("Replay Read Failed! " + c0132oA.c());
                }
                int[] iArrE = c0132oA.e();
                this.f2383a.O().e(zH);
                this.f2383a.O().d(iK);
                return iArrE;
            } catch (Throwable th) {
                this.f2383a.O().e(zH);
                this.f2383a.O().d(iK);
                throw th;
            }
        } catch (V.g e4) {
            C.a(e4);
            throw new RemoteAccessException("Failed to get Replay Record Count");
        }
    }

    public int[] c(InterfaceC0131n interfaceC0131n) throws RemoteAccessException {
        String strAh = this.f2383a.O().ah();
        if (strAh == null) {
            throw new RemoteAccessException("Configuration Error, replayRecordCountParam not set!");
        }
        aM aMVarC = this.f2383a.c(strAh);
        if (aMVarC == null) {
            throw new RemoteAccessException("Configuration Error, replayRecordCountParam " + ((Object) aMVarC) + " not found!");
        }
        a(1000);
        C0116cv.b(this.f2383a, aMVarC.d());
        aM aMVarC2 = this.f2383a.c("Key_On_Baro");
        if (aMVarC2 != null) {
            C0116cv.b(this.f2383a, aMVarC2.d());
        }
        da daVar = new da();
        try {
            int iJ = (int) aMVarC.j(this.f2383a.p());
            try {
                if (this.f2383a.O().T()) {
                    C0122e.a(this.f2383a);
                }
            } catch (V.g e2) {
                C.a(e2);
            }
            this.f2384c = true;
            int iAi = iJ * this.f2383a.O().ai();
            int[] iArr = new int[iAi];
            byte[] bArr = {-14, -8, -10, -12};
            int i2 = 0;
            boolean zH = this.f2383a.O().H();
            int iK = this.f2383a.O().k();
            this.f2383a.O().e(false);
            this.f2383a.O().d(45);
            this.f2383a.O().a(false);
            interfaceC0131n.e();
            try {
                do {
                    try {
                        byte b2 = bArr[(16384 + i2) / 65536];
                        int i3 = iAi - i2 > 2048 ? 2048 : iAi - i2;
                        a(3000);
                        C0132o c0132oA = daVar.a(this.f2383a, a.a(this.f2383a.O(), b2, (16384 + i2) % 65536, i3), 4000);
                        if (c0132oA.a() == 1 && c0132oA.e() != null) {
                            int[] iArrE = c0132oA.e();
                            System.arraycopy(iArrE, 0, iArr, i2, iArrE.length > i3 ? i3 : iArrE.length);
                            i2 += i3;
                            interfaceC0131n.a(i2 / iAi);
                            try {
                                C0122e.c(this.f2383a);
                            } catch (V.g e3) {
                                C.b("Ping high speed baud failed.");
                            }
                            if (i2 < iAi - 2048) {
                            }
                            break;
                        }
                        throw new RemoteAccessException("Replay Read interrupted!\n " + c0132oA.c());
                    } catch (Throwable th) {
                        try {
                            C.c("Replay, totalBytes=" + iAi + ", readBytes=" + i2);
                            a(2000);
                            C0122e.b(this.f2383a);
                            if (!this.f2384c) {
                                C0132o c0132o = new C0132o();
                                c0132o.a(3);
                                c0132o.a("Replay Read Cancelled");
                                interfaceC0131n.a(c0132o);
                            }
                        } catch (V.g e4) {
                            C.a(e4);
                        }
                        this.f2383a.O().e(zH);
                        this.f2383a.O().a(zH);
                        this.f2383a.O().d(iK);
                        throw th;
                    }
                } while (this.f2384c);
                break;
                C.c("Replay, totalBytes=" + iAi + ", readBytes=" + i2);
                a(2000);
                C0122e.b(this.f2383a);
                if (!this.f2384c) {
                    C0132o c0132o2 = new C0132o();
                    c0132o2.a(3);
                    c0132o2.a("Replay Read Cancelled");
                    interfaceC0131n.a(c0132o2);
                }
            } catch (V.g e5) {
                C.a(e5);
            }
            this.f2383a.O().e(zH);
            this.f2383a.O().a(zH);
            this.f2383a.O().d(iK);
            return iArr;
        } catch (V.g e6) {
            C.a(e6);
            throw new RemoteAccessException("Failed to get Replay Record Count");
        }
    }

    public void a() {
        this.f2384c = false;
    }

    protected void a(int i2) {
        this.f2383a.C().d(System.currentTimeMillis() + i2);
    }

    private void a(String str) {
        Iterator it = this.f2386b.iterator();
        while (it.hasNext()) {
            ((h) it.next()).a(str);
        }
    }
}
