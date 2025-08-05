package aI;

import G.C0130m;
import G.F;
import G.G;
import G.H;
import G.Q;
import bH.C0995c;
import java.util.Calendar;
import java.util.Date;

/* loaded from: TunerStudioMS.jar:aI/d.class */
public class d extends Q implements Cloneable {

    /* renamed from: a, reason: collision with root package name */
    public static String f2436a = "sd_status";

    /* renamed from: b, reason: collision with root package name */
    public static byte f2437b = 1;

    /* renamed from: c, reason: collision with root package name */
    public static byte f2438c = 4;

    /* renamed from: d, reason: collision with root package name */
    public static byte f2439d = 8;

    /* renamed from: e, reason: collision with root package name */
    public static byte f2440e = 16;

    public static C0130m a(F f2, int i2) throws x {
        if (i2 < 0 || i2 > 5) {
            throw new x("Unknown Command:" + i2);
        }
        return C0130m.a(f2, new int[]{119, f2.x(), 17, 0, 0, 0, 1, i2});
    }

    public static C0130m a(F f2) {
        C0130m c0130mA = C0130m.a(f2, new int[]{119, f2.x(), 17, 0, 0, 0, 1, 4});
        c0130mA.v("SD Status Init");
        return c0130mA;
    }

    public static C0130m b(F f2) {
        C0130m c0130mA = C0130m.a(f2, new int[]{114, f2.x(), 17, 0, 0, 0, 16});
        c0130mA.v("SD Status Read");
        return c0130mA;
    }

    public static C0130m b(F f2, int i2) {
        int[] iArrA = C0995c.a(i2, new int[2], true);
        C0130m c0130mA = C0130m.a(f2, new int[]{119, f2.x(), 17, 0, 1, 0, 2, iArrA[0], iArrA[1]});
        c0130mA.v("SD Read Dir Init, dirChunk=" + i2);
        return c0130mA;
    }

    public static C0130m c(F f2) {
        C0130m c0130mA = C0130m.a(f2, new int[]{114, f2.x(), 17, 0, 0, 2, 2});
        c0130mA.v("SD Read Dir");
        return c0130mA;
    }

    public static C0130m c(F f2, int i2) {
        int[] iArrA = C0995c.a(i2, new int[4], true);
        C0130m c0130mA = C0130m.a(f2, new int[]{119, f2.x(), 17, 0, 2, 0, 4, iArrA[0], iArrA[1], iArrA[2], iArrA[3]});
        c0130mA.v("SD Read Sector Init");
        return c0130mA;
    }

    public static C0130m d(F f2) {
        C0130m c0130mA = C0130m.a(f2, new int[]{114, f2.x(), 17, 0, 0, 2, 4});
        c0130mA.v("SD Read Sector");
        return c0130mA;
    }

    public static C0130m a(F f2, int[] iArr, int i2) {
        int[] iArr2 = {119, f2.x(), 17, 0, 3, 2, 4};
        int[] iArrA = C0995c.a(i2, new int[4], true);
        int[] iArr3 = new int[iArr2.length + iArr.length + iArrA.length];
        System.arraycopy(iArr2, 0, iArr3, 0, iArr2.length);
        System.arraycopy(iArr, 0, iArr3, iArr2.length, iArr.length);
        System.arraycopy(iArrA, 0, iArr3, iArr2.length + iArr.length, iArrA.length);
        C0130m c0130mA = C0130m.a(f2, iArr3);
        c0130mA.v("SD Write Sector");
        return c0130mA;
    }

    public static C0130m a(F f2, int i2, int i3) {
        int[] iArrA = C0995c.a(i2, new int[4], true);
        int[] iArrA2 = C0995c.a(i3, new int[4], true);
        C0130m c0130mA = C0130m.a(f2, new int[]{119, f2.x(), 17, 0, 5, 0, 8, iArrA[0], iArrA[1], iArrA[2], iArrA[3], iArrA2[0], iArrA2[1], iArrA2[2], iArrA2[3]});
        c0130mA.a(800);
        c0130mA.v("SD Read Compressed File Init");
        return c0130mA;
    }

    public static C0130m d(F f2, int i2) {
        int[] iArrA = C0995c.a(i2, new int[4], true);
        C0130m c0130mA = C0130m.a(f2, new int[]{114, f2.x(), 20, iArrA[2], iArrA[3], 8, 0});
        c0130mA.i(10);
        c0130mA.a(2000);
        c0130mA.b(true);
        c0130mA.v("SD Read Compressed File");
        return c0130mA;
    }

    public static C0130m a(F f2, char[] cArr, int i2) {
        int[] iArrA = C0995c.a(i2, new int[4], true);
        C0130m c0130mA = C0130m.a(f2, new int[]{119, f2.x(), 17, 0, 6, 0, 6, cArr[0], cArr[1], cArr[2], cArr[3], iArrA[2], iArrA[3], 8, 0});
        c0130mA.v("SD Delete File");
        return c0130mA;
    }

    public static C0130m e(F f2) {
        int[] iArrB = {114, f2.x(), 7, 2, 77, 0, 8};
        G gB = f2.b("cmdReadMs3Rtc");
        if (gB != null && gB.size() > 0) {
            iArrB = C0995c.b(((H) gB.get(0)).d());
        }
        C0130m c0130mA = C0130m.a(f2, iArrB);
        c0130mA.a(600);
        c0130mA.v("SD RTC Read");
        return c0130mA;
    }

    public static C0130m f(F f2) {
        C0130m c0130mA = C0130m.a(f2, new int[]{114, f2.x(), 7, 2, 126, 0, 8});
        c0130mA.v("SD RTC Set Init");
        return c0130mA;
    }

    public static C0130m a(F f2, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i2 = calendar.get(1);
        byte[] bArr = new byte[2];
        C0995c.a(i2, bArr, true);
        int[] iArr = {calendar.get(13), calendar.get(12), calendar.get(11), calendar.get(7), calendar.get(5), calendar.get(2) + 1, bArr[0], bArr[1], 90};
        C0130m c0130mA = C0130m.a(f2, new int[]{119, f2.x(), 7, 2, 126, 0, 9, iArr[0], iArr[1], iArr[2], iArr[3], iArr[4], iArr[5], iArr[6], iArr[7], iArr[8]});
        c0130mA.v("SD RTC Set ");
        return c0130mA;
    }
}
