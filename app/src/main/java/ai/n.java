package aI;

import G.C0130m;
import G.C0132o;
import G.F;
import G.R;
import bH.C;
import bH.C0995c;
import bH.L;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:aI/n.class */
public class n {

    /* renamed from: a, reason: collision with root package name */
    R f2483a;

    public n(R r2) {
        this.f2483a = null;
        this.f2483a = r2;
    }

    public void a(u uVar, L l2) throws RemoteAccessException {
        c cVarA = a(this.f2483a.O());
        C.c("Format Info:\n" + cVarA.toString());
        long jG = cVarA.g() + cVarA.a();
        long jG2 = cVarA.g() + cVarA.b() + ((uVar.getFileCount() * 32) / uVar.a());
        C.c("startSector=" + jG + ", endSector=" + jG2);
        if (l2 != null) {
            l2.a();
        }
        o oVarD = o.d(this.f2483a);
        try {
            oVarD.a(false);
            oVarD.a(3000);
            C.c("Runtime Reads disabled.");
            int[] iArr = new int[cVarA.d()];
            int[] iArrA = a(cVarA);
            float fE = 1.0f / (cVarA.e() + 1);
            int i2 = -1;
            for (int i3 = 0; i3 < cVarA.e(); i3++) {
                int iF = (int) (jG + (i3 * cVarA.f()));
                int iF2 = (iF + cVarA.f()) - 1;
                a(this.f2483a.O(), iArrA, iF);
                boolean z2 = false;
                int i4 = iF + 1;
                while (i4 <= iF2 && !z2) {
                    boolean z3 = false;
                    if ((i4 - iF) % 20 == 0) {
                        int iA = a(i4, 20);
                        C.c("Found blank sectors: " + iA + " from sector: " + i4);
                        if (iA >= 20) {
                            z2 = true;
                            i2 = i4 - iF;
                        } else if (iA > 0) {
                            if (i4 <= iF2 - iA) {
                                i4 += iA;
                            } else {
                                z3 = true;
                            }
                        }
                    }
                    if (!z3) {
                        a(this.f2483a.O(), iArr, i4);
                    }
                    oVarD.a(3000);
                    if (l2 != null) {
                        if (i2 > 0) {
                            l2.a((i3 * fE) + ((fE * (i4 - iF)) / i2));
                        } else {
                            double d2 = (i4 - iF) / (iF2 - iF);
                            if (d2 < 0.03d) {
                                d2 = 0.03d;
                            }
                            l2.a(d2);
                        }
                    }
                    i4++;
                }
            }
            for (long jE = (int) (jG + (cVarA.e() * cVarA.f())); jE <= jG2; jE++) {
                C.c("Clearing Directory sector: " + jE);
                a(this.f2483a.O(), iArr, (int) jE);
                oVarD.a(3000);
                if (l2 != null) {
                    l2.a((cVarA.e() * fE) + ((fE * (jE - r0)) / (jG2 - r0)));
                }
            }
        } finally {
            oVarD.a();
            oVarD.b();
            C.c("Runtime Reads enabled.");
            if (l2 != null) {
                l2.b();
            }
        }
    }

    private int a(int i2, int i3) throws RemoteAccessException {
        int i4 = 0;
        int i5 = i2;
        do {
            int i6 = i5;
            i5++;
            int[] iArrA = a(this.f2483a.O(), i6);
            if (iArrA != null && iArrA.length > 4) {
                int[] iArr = new int[iArrA.length - 4];
                System.arraycopy(iArrA, 0, iArr, 0, iArr.length);
                boolean zC = C0995c.c(iArr);
                if (zC) {
                    i4++;
                }
                if (!zC) {
                    break;
                }
            } else {
                return -1;
            }
        } while (i4 < i3);
        return i4;
    }

    public c a(F f2) throws RemoteAccessException {
        c cVar = new c();
        int[] iArrA = a(f2, 0);
        aJ.b bVar = new aJ.b();
        bVar.a(C0995c.a(iArrA));
        if (bVar.a().size() <= 0) {
            throw new RemoteAccessException("no valid partition found.");
        }
        aJ.c cVar2 = (aJ.c) bVar.a().get(0);
        int[] iArrA2 = a(f2, (int) cVar2.a());
        cVar.a(cVar2.a());
        C.c("PartitionEntryFound:\n" + cVar2.toString());
        if (iArrA2[54] == 70 && iArrA2[55] == 65 && iArrA2[56] == 84 && iArrA2[57] == 49 && iArrA2[58] == 54) {
            cVar.a(16);
        } else {
            if (iArrA2[82] != 70 || iArrA2[83] != 65 || iArrA2[84] != 84 || iArrA2[85] != 51 || iArrA2[86] != 50) {
                throw new RemoteAccessException("Unsupported File System.");
            }
            cVar.a(32);
        }
        cVar.b(C0995c.b(iArrA2, 11, 2, false, false));
        cVar.c(iArrA2[13]);
        cVar.d(C0995c.b(iArrA2, 14, 2, false, false));
        cVar.e(iArrA2[16]);
        if (cVar.c() == 16) {
            cVar.f(C0995c.b(iArrA2, 22, 2, false, false));
        } else if (cVar.c() == 32) {
            cVar.f(C0995c.b(iArrA2, 36, 4, false, false));
        }
        int iB = C0995c.b(iArrA2, 19, 2, false, false);
        if (iB == 0) {
            iB = C0995c.b(iArrA2, 32, 4, false, false);
        }
        cVar.g(iB);
        return cVar;
    }

    private int[] a(c cVar) throws RemoteAccessException {
        int[] iArr = new int[cVar.d()];
        if (cVar.c() == 16) {
            iArr[0] = 248;
            iArr[1] = 255;
        } else {
            if (cVar.c() != 32) {
                throw new RemoteAccessException("Unsupported File System, reformat on PC.");
            }
            iArr[0] = 248;
            iArr[1] = 255;
            iArr[2] = 255;
            iArr[3] = 15;
            iArr[4] = 255;
            iArr[5] = 255;
            iArr[6] = 255;
            iArr[7] = 15;
            iArr[8] = 255;
            iArr[9] = 255;
            iArr[10] = 255;
            iArr[11] = 15;
        }
        return iArr;
    }

    public int[] a(F f2, int i2) throws RemoteAccessException {
        ArrayList arrayList = new ArrayList();
        C0130m c0130mC = d.c(this.f2483a.O(), i2);
        c0130mC.i(30);
        arrayList.add(c0130mC);
        arrayList.add(d.d(this.f2483a.O()));
        C0130m c0130mA = C0130m.a(this.f2483a.O(), arrayList);
        c0130mA.v("Read Sector: " + i2);
        C0132o c0132oA = o.d(this.f2483a).a(c0130mA, false, 1500);
        if (c0132oA == null) {
            throw new RemoteAccessException("Unable to read MS3 SD Sector. No Response");
        }
        if (c0132oA.a() == 3) {
            throw new RemoteAccessException("Unable to read MS3 SD Sector. \nMessage:\n" + c0132oA.c());
        }
        return c0132oA.e();
    }

    public void a(F f2, int[] iArr, int i2) throws RemoteAccessException {
        C0130m c0130mA = d.a(this.f2483a.O(), iArr, i2);
        c0130mA.v("Write Sector: " + i2);
        C0132o c0132oA = o.d(this.f2483a).a(c0130mA, false, 1500);
        if (c0132oA == null) {
            throw new RemoteAccessException("Unable to read MS3 SD Sector. No Response");
        }
        if (c0132oA.a() == 3) {
            throw new RemoteAccessException("Unable to read MS3 SD Sector. \nMessage:\n" + c0132oA.c());
        }
    }
}
