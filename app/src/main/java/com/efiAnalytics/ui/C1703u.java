package com.efiAnalytics.ui;

/* renamed from: com.efiAnalytics.ui.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/u.class */
public class C1703u {
    public static C1701s a(C1701s c1701s) {
        C1701s c1701s2 = new C1701s();
        c1701s2.a(3, 3);
        String[] strArr = new String[3];
        String[] strArr2 = new String[3];
        String[] strArrA = c1701s.a();
        strArr[2] = strArrA[0];
        strArr[0] = strArrA[strArrA.length - 1];
        strArr[1] = bH.W.a((Double.parseDouble(strArr[0]) + Double.parseDouble(strArr[2])) * 0.35d);
        c1701s2.d(strArr);
        String[] strArrB = c1701s.b();
        strArr2[0] = strArrB[0];
        strArr2[2] = strArrB[strArrB.length - 1];
        strArr2[1] = bH.W.a((Double.parseDouble(strArr2[0]) + Double.parseDouble(strArr2[2])) * 0.3d);
        c1701s2.c(strArr2);
        c1701s2.setValueAt(Double.valueOf(200.0d), 0, 0);
        c1701s2.setValueAt(Double.valueOf(250.0d), 1, 0);
        c1701s2.setValueAt(Double.valueOf(350.0d), 2, 0);
        c1701s2.setValueAt(Double.valueOf(70.0d), 0, 1);
        c1701s2.setValueAt(Double.valueOf(150.0d), 1, 1);
        c1701s2.setValueAt(Double.valueOf(300.0d), 2, 1);
        c1701s2.setValueAt(Double.valueOf(40.0d), 0, 2);
        c1701s2.setValueAt(Double.valueOf(100.0d), 1, 2);
        c1701s2.setValueAt(Double.valueOf(200.0d), 2, 2);
        c1701s2.d(c1701s.v());
        c1701s2.e(c1701s.w());
        return c1701s2;
    }

    public static double[] a(int i2, double d2, double d3, double d4, double d5) {
        int i3;
        int i4;
        double[] dArr = new double[i2];
        if (dArr.length > 12) {
            i3 = 3;
            int i5 = 0 + 1;
            dArr[0] = Math.floor((d2 - (d2 * 0.2d)) / 100.0d) * 100.0d;
            int i6 = i5 + 1;
            dArr[i5] = Math.round(d2 / 100.0d) * 100;
            i4 = i6 + 1;
            dArr[i6] = Math.ceil((d2 + (d2 * 0.25d)) / 100.0d) * 100.0d;
        } else if (dArr.length > 8) {
            i3 = 2;
            int i7 = 0 + 1;
            dArr[0] = Math.floor((d2 - (d2 * 0.2d)) / 100.0d) * 100.0d;
            i4 = i7 + 1;
            dArr[i7] = Math.ceil((d2 + (d2 * 0.2d)) / 100.0d) * 100.0d;
        } else {
            i3 = 2;
            int i8 = 0 + 1;
            dArr[0] = Math.floor((d2 - (d2 * 0.25d)) / 100.0d) * 100.0d;
            i4 = i8 + 1;
            dArr[i8] = Math.ceil((d2 + 100.0d) / 100.0d) * 100.0d;
        }
        int iCeil = (int) Math.ceil(((dArr.length - i3) - 3) / 2.0d);
        double dRound = Math.round(d3 / 100.0d) * 100;
        for (int i9 = 1; i9 <= iCeil; i9++) {
            int i10 = i4;
            i4++;
            dArr[i10] = Math.round((dArr[i3 - 1] + ((((((2 * iCeil) + i9) / (iCeil * 3)) * i9) * (dRound - dArr[i3 - 1])) / (iCeil + 1))) / 100.0d) * 100;
        }
        int i11 = i4;
        int i12 = i4 + 1;
        dArr[i11] = dRound;
        int iCeil2 = (int) Math.ceil((2 * ((dArr.length - i12) - 2)) / 3.0d);
        double dRound2 = Math.round(d4 / 100.0d) * 100;
        for (int i13 = 1; i13 <= iCeil2; i13++) {
            int i14 = i12;
            i12++;
            dArr[i14] = Math.round((dRound + ((i13 * (dRound2 - dRound)) / (iCeil2 + 1))) / 100.0d) * 100;
        }
        int i15 = i12;
        int i16 = i12 + 1;
        dArr[i15] = dRound2;
        int length = (dArr.length - i16) - 1;
        double dCeil = Math.ceil(d5 / 100.0d) * 100.0d;
        for (int i17 = 1; i17 <= length; i17++) {
            int i18 = i16;
            i16++;
            dArr[i18] = dRound2 + ((i17 * (dCeil - dRound2)) / (length + 1));
        }
        int i19 = i16;
        int i20 = i16 + 1;
        dArr[i19] = dCeil;
        return dArr;
    }

    public static double[] a(int i2, double d2, double d3) {
        double[] dArr = new double[i2];
        int iRound = (int) (Math.round(d2 / 11.0d) * 5);
        double dCeil = Math.ceil(d2 / 5.0d) * 5.0d;
        double dCeil2 = Math.ceil(d3 / 5.0d) * 5.0d;
        int iRound2 = (int) (dCeil2 <= 105.0d ? 0L : Math.round((i2 * 0.3d) + (2.0d * Math.log((dCeil2 - 100.0d) / (100.0d - iRound)))));
        int i3 = i2 - iRound2;
        int i4 = 5;
        while (iRound + ((i3 - 1) * i4) > 100) {
            i4--;
        }
        while (iRound + ((i3 - 1) * i4 * 2) < 100) {
            i4++;
        }
        int iCeil = (((int) Math.ceil((100.0d - iRound) / i4)) - i3) + 1;
        for (int i5 = 0; i5 < i3; i5++) {
            if (i5 == 0) {
                dArr[i5] = iRound;
            } else if (iCeil > 0 && (3 * (i3 - iCeil)) / 4 < i5) {
                dArr[i5] = dArr[i5 - 1] + (i4 * 2);
                iCeil--;
            } else if (iCeil > 0 && i5 == 1) {
                dArr[i5] = dArr[i5 - 1] + (i4 * 2);
                iCeil--;
            } else if (dArr[i5 - 1] + i4 > 100.0d) {
                dArr[i5] = 100.0d;
            } else {
                dArr[i5] = dArr[i5 - 1] + i4;
            }
        }
        for (int i6 = 0; i6 < iRound2; i6++) {
            dArr[i3 + i6] = 100 + (((i6 + 1) * (dCeil2 - 100)) / iRound2);
        }
        double[] dArr2 = new double[dArr.length];
        for (int i7 = 0; i7 < dArr2.length; i7++) {
            dArr2[i7] = dArr[(dArr.length - 1) - i7];
        }
        return dArr2;
    }

    public static C1701s a(C1701s c1701s, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, boolean z2) {
        double[] dArrA = a(c1701s.getColumnCount(), d2, d8, d4, d5);
        String[] strArr = new String[dArrA.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = "" + ((int) dArrA[i2]);
        }
        c1701s.c(strArr);
        double[] dArrA2 = a(c1701s.getRowCount(), d3, d7 > d10 ? d7 : d10);
        for (int i3 = 0; i3 < dArrA2.length; i3++) {
            c1701s.b("" + dArrA2[i3], i3);
        }
        return c1701s;
    }

    public static C1701s a(C1701s c1701s, int i2, int i3, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, boolean z2, double d12) {
        C1701s c1701s2 = new C1701s();
        c1701s2.a(i3, i2);
        double[] dArrA = a(i2, d2, d8, d4, d5);
        String[] strArr = new String[dArrA.length];
        for (int i4 = 0; i4 < strArr.length; i4++) {
            strArr[i4] = "" + ((int) dArrA[i4]);
        }
        c1701s2.c(strArr);
        double[] dArrA2 = a(i3, d3, d7 > d10 ? d7 : d10);
        for (int i5 = 0; i5 < dArrA2.length; i5++) {
            c1701s2.b("" + dArrA2[i5], i5);
        }
        return a(c1701s2, c1701s, d2, d3, d4, d6, d7, d8, d9, d10, d11, z2, d12);
    }

    public static C1701s a(C1701s c1701s, C1701s c1701s2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, boolean z2, double d11) {
        double dF = c1701s.F();
        double d12 = dF > 101.3d ? 0.65d : 1.0d;
        double d13 = d12 + ((1.0d - d12) * ((dF <= 101.3d || !z2) ? 0.0d : 0.59d));
        double d14 = d8 * (101.3d / (d9 * d13));
        double d15 = d5 * (101.3d / (d6 * d13)) * (5252.0d / d4);
        double d16 = d6 > 101.3d ? z2 ? 0.52d : 0.56d : 0.47d;
        double d17 = d6 > 101.3d ? z2 ? 0.56d : 0.6d : 0.5d;
        double d18 = ((941100.0d * d16) * d8) / (d10 * 5252.0d);
        double d19 = ((941100.0d * d17) * d5) / (d10 * d4);
        double d20 = ((941100.0d * d17) * d15) / (d10 * d4);
        if (d19 > (d18 * d17) / d16) {
            d18 = d19;
            d19 = d18 * 0.98d;
            bH.C.c("input HP and TQ look unreasonable, adjusting.");
        }
        double d21 = d18;
        double d22 = (d19 - d21) / (((d4 * d4) - ((2.0d * d7) * d4)) + (d7 * d7));
        double d23 = (-2.0d) * d22 * d7;
        double d24 = d21 + (d22 * d7 * d7);
        double d25 = 0.31d * ((d14 / d10) + (d15 / d10));
        double d26 = (d22 * d7 * d7) + (d23 * d7) + d24;
        double d27 = ((d26 * 0.25d) * 100.0d) / dF;
        int columnCount = c1701s.getColumnCount();
        int rowCount = c1701s.getRowCount();
        double d28 = 1.0d - ((8.0d / columnCount) * 0.05d);
        double d29 = 0.0d;
        double dE = ((d22 * d2 * d2) + (d23 * d2) + d24) * ((dF - ((dF - c1701s.e(0)) * d25)) / dF);
        double d30 = 14.7d;
        for (int i2 = 0; i2 < rowCount; i2++) {
            double dE2 = c1701s.e(i2);
            double d31 = (dF - ((dF - dE2) * (d25 * ((d13 >= 1.0d || dE2 <= 90.0d) ? d13 : 1.0d - (((dE2 - 90.0d) / (dF - 90.0d)) * d13))))) / dF;
            for (int i3 = 0; i3 < columnCount; i3++) {
                double d32 = c1701s.d(i3);
                double d33 = ((d22 * d32 * d32) + (d23 * d32) + d24) * d31;
                double dPow = (Math.pow(d2 + 100.0d, 3.0d) / Math.pow(d32, 3.0d)) * (d3 + 1.0d > dE2 ? Math.pow(d3 + 5.0d, 4.0d) / Math.pow(dE2, 4.0d) : Math.pow(d3 + 5.0d, 5.0d) / Math.pow(dE2, 5.0d));
                double d34 = ((dE * dPow) + d33) / (1.0d + dPow);
                if (d34 < d27) {
                    d34 = d27;
                    bH.C.c("ve below min. ve=" + d34 + ", min=" + d27);
                } else if (d34 > d26) {
                    d34 = d26;
                }
                if (i3 > 0 && d34 < d29 * d28) {
                    d34 = d29 * d28;
                }
                d29 = d34;
                double dA = C1677fh.a(c1701s2, d32, dE2);
                if (d30 > dA) {
                    d30 = dA;
                }
                c1701s.a((Object) Double.valueOf(d34 * d11 * (dA > 3.0d ? dA / d30 : dA)), i2, i3);
            }
        }
        return c1701s;
    }

    public static C1701s a(C1701s c1701s, double d2, double d3, double d4, double d5) {
        for (int i2 = 0; i2 < c1701s.getColumnCount(); i2++) {
            c1701s.b()[i2] = (d4 + (((d5 - d4) * i2) / (c1701s.getColumnCount() - 1))) + "";
        }
        for (int i3 = 0; i3 < c1701s.getRowCount(); i3++) {
            c1701s.a()[(c1701s.getRowCount() - 1) - i3] = (d2 + (((d3 - d2) * i3) / (c1701s.getRowCount() - 1))) + "";
        }
        return b(c1701s);
    }

    public static C1701s b(C1701s c1701s) {
        return a(c1701s, 1.0d);
    }

    public static C1701s a(C1701s c1701s, double d2) throws NumberFormatException {
        C1701s c1701sA = new eY().a(c1701s.getClass().getResourceAsStream("resources/afrTableReference.table"));
        double d3 = Double.parseDouble(c1701s.b()[0]);
        double d4 = Double.parseDouble(c1701s.b()[c1701s.b().length - 1]);
        for (int i2 = 0; i2 < c1701s.getRowCount(); i2++) {
            for (int i3 = 0; i3 < c1701s.getColumnCount(); i3++) {
                double d5 = (Double.parseDouble(c1701s.b()[i3]) - d3) / (d4 - d3);
                double d6 = d3 + (d5 * (d4 - d3));
                c1701s.setValueAt(Double.valueOf(C1677fh.a(c1701sA, d5, Double.parseDouble(c1701s.a()[i2])) * d2), i2, i3);
            }
        }
        c1701s.q();
        return c1701s;
    }
}
