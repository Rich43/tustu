package com.efiAnalytics.apps.ts.dashboard;

import G.C0048ah;
import G.C0051ak;
import G.C0113cs;
import G.C0128k;
import G.InterfaceC0110cp;
import com.efiAnalytics.apps.ts.dashboard.renderers.BasicReadoutGaugePainter;
import com.efiAnalytics.apps.ts.dashboard.renderers.HistogramPainter;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import r.C1807j;
import sun.security.x509.PolicyMappingsExtension;
import v.C1883c;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.aa, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/aa.class */
public class C1388aa {

    /* renamed from: e, reason: collision with root package name */
    private Gauge f9455e = null;

    /* renamed from: f, reason: collision with root package name */
    private final int f9456f = 300;

    /* renamed from: g, reason: collision with root package name */
    private final int f9457g = 300;

    /* renamed from: h, reason: collision with root package name */
    private final int f9458h = 360;

    /* renamed from: i, reason: collision with root package name */
    private final int f9459i = 0;

    /* renamed from: j, reason: collision with root package name */
    private final int f9460j = 6;

    /* renamed from: a, reason: collision with root package name */
    public static int f9452a = 1;

    /* renamed from: b, reason: collision with root package name */
    public static String f9453b = null;

    /* renamed from: c, reason: collision with root package name */
    public static String f9454c = "/com/efiAnalytics/apps/ts/dashboard/default.gauge";

    /* renamed from: k, reason: collision with root package name */
    private static C1388aa f9461k = new C1388aa();

    /* renamed from: d, reason: collision with root package name */
    public static Color f9462d = Color.GRAY;

    public static boolean a(G.R r2, Z z2) {
        for (Component component : z2.c()) {
            if (component instanceof SingleChannelDashComponent) {
                SingleChannelDashComponent singleChannelDashComponent = (SingleChannelDashComponent) component;
                if (singleChannelDashComponent.getOutputChannel() != null && !singleChannelDashComponent.getOutputChannel().isEmpty() && !singleChannelDashComponent.getEcuConfigurationName().equals(C0113cs.f1154a) && r2.g(singleChannelDashComponent.getOutputChannel()) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void a(C1425x c1425x, aE.a aVar, String str) {
        c1425x.d(a(aVar, str));
    }

    private String a(aE.a aVar, String str) {
        return aVar.m() + str + "." + C1798a.co;
    }

    public Z a(G.R r2, aE.a aVar, String str, int i2) {
        String strA = a(aVar, str);
        return new File(strA).exists() ? new C1883c(C1807j.G()).a(strA) : a(r2, str, i2);
    }

    private Z b(G.R r2, String str) throws V.a {
        String[] strArrC = c(r2, "FrontPage");
        File file = new File(C1807j.h(), str);
        if (!file.exists()) {
            throw new V.a("Invalid Dashboard Template: " + str);
        }
        Z zA = new C1883c(C1807j.G()).a(file.getAbsolutePath());
        ArrayList arrayListA = a(r2, strArrC, true);
        zA.b(r2.i());
        int size = 1 + ((arrayListA.size() - 1) / 7);
        Gauge gauge = null;
        Component[] componentArrC = zA.c();
        ArrayList arrayList = new ArrayList();
        for (Component component : componentArrC) {
            if (gauge == null && (component instanceof Gauge)) {
                gauge = (Gauge) component;
            }
            if (component instanceof AbstractC1420s) {
                arrayList.add((AbstractC1420s) component);
            }
        }
        if (gauge == null) {
            gauge = new Gauge();
            gauge.setGaugePainter(new BasicReadoutGaugePainter());
        }
        int iW = r2.w();
        int i2 = iW > 0 ? (iW / 14) + 1 : 0;
        if (i2 == 1 && r2.w() > 14) {
            i2 = 2;
        }
        int i3 = 0;
        int iCeil = (int) Math.ceil(iW / i2);
        double d2 = iW > 0 ? iW % i2 == 0 ? i2 / iW : 1.0f / iCeil : 0.0d;
        double d3 = 1.0d / 7;
        double d4 = (0.5d - (i2 * 0.031d)) / size;
        if (d4 > 0.09d) {
            d4 = 0.09d;
        }
        Component[] componentArr = new AbstractC1420s[arrayList.size() + arrayListA.size() + iW];
        int i4 = 0;
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            componentArr[i4] = (AbstractC1420s) it.next();
            i4++;
        }
        double d5 = (1.0d - (size * d4)) - (i2 * 0.031d);
        for (int i5 = 0; i5 < size; i5++) {
            double d6 = d5 + (d4 * i5);
            for (int i6 = 0; i6 < 7 && (i5 * 7) + i6 < arrayListA.size(); i6++) {
                C0048ah c0048ah = (C0048ah) arrayListA.get((i5 * 7) + i6);
                Gauge gauge2 = new Gauge();
                a(gauge, gauge2);
                a(gauge2, c0048ah);
                gauge2.setRelativeX((i6 * d3) + 0.002d);
                gauge2.setRelativeY(d6);
                gauge2.setRelativeWidth(d3 - (0.002d * 2.0d));
                gauge2.setRelativeHeight(d4 - (0.004d * 2.0d));
                componentArr[i4] = gauge2;
                i4++;
            }
        }
        double d7 = d5 + (d4 * size) + 0.004d;
        Iterator itA = r2.A();
        while (itA.hasNext()) {
            C0051ak c0051ak = (C0051ak) itA.next();
            if (c0051ak.k()) {
                Indicator indicator = new Indicator();
                indicator.setRelativeX((0.0015d + (i3 * d2)) % 1.0d);
                indicator.setRelativeY(d7 + (d2 * ((int) (i3 * d2))));
                indicator.setRelativeWidth(d2 - (0.0015d * 2.0d));
                indicator.setRelativeHeight(0.031d - (0.0015d * 2.0d));
                indicator.setOnText(c0051ak.a().toString());
                indicator.setOnBackgroundColor(new Color(c0051ak.g().a()));
                indicator.setOnTextColor(new Color(c0051ak.i().a()));
                indicator.setOffText(c0051ak.d().toString());
                indicator.setOffBackgroundColor(new Color(c0051ak.h().a()));
                indicator.setOffTextColor(new Color(c0051ak.j().a()));
                indicator.setOutputChannel(c0051ak.f());
                i3++;
                componentArr[i4] = indicator;
                i4++;
            }
        }
        zA.a(componentArr);
        return zA;
    }

    public Z a(G.R r2, String str, int i2) throws V.a {
        int i3;
        int i4;
        String[] strArrC = c(r2, str);
        if (strArrC == null) {
            throw new V.a("Cluster not found for " + str + ", and no default gauge set defined in configuration.");
        }
        int iA = a(r2, strArrC);
        if (a(str)) {
            try {
                return b(r2, f9453b);
            } catch (V.a e2) {
                e2.printStackTrace();
            }
        }
        if (i2 == 1) {
            i4 = iA / 2;
            i3 = 2;
        } else if (i2 == 2) {
            i3 = iA / 2;
            i4 = 2;
        } else if (i2 == 6) {
            i3 = iA;
            i4 = 1;
        } else {
            if (i2 == 4) {
                return a(r2, str, true, 1);
            }
            if (i2 == 5) {
                Z zA = a(r2, str, false, 1);
                zA.b(c());
                return zA;
            }
            double dSqrt = Math.sqrt(strArrC.length);
            i3 = dSqrt > ((double) ((int) dSqrt)) ? ((int) dSqrt) + 1 : (int) dSqrt;
            i4 = i3;
        }
        return a(r2, str, i3, i4);
    }

    private String[] c(G.R r2, String str) {
        String[] strArrJ = r2.j(str);
        if (strArrJ == null) {
            strArrJ = r2.j(Action.DEFAULT);
            if (strArrJ == null || strArrJ.length == 0) {
                strArrJ = r2.j("FrontPage");
            }
            if ((str.startsWith("veAnalyze_") || str.startsWith("wueAnalyze_")) && !str.endsWith("_histogram") && strArrJ.length > 3) {
                return new String[]{strArrJ[0], strArrJ[1], strArrJ[2]};
            }
        }
        return strArrJ;
    }

    public Z a(G.R r2, String str, boolean z2, int i2) {
        Z zA = a(a(r2, c(r2, str), z2), i2);
        zA.b(r2.i());
        return zA;
    }

    public Z a(ArrayList arrayList, int i2) {
        Z z2 = new Z();
        z2.a(Color.BLACK);
        double d2 = 1.0d / i2;
        double d3 = 0.33d / i2;
        int iCeil = (int) Math.ceil(arrayList.size() / i2);
        double d4 = 1.0d / iCeil;
        ArrayList arrayList2 = new ArrayList();
        for (int i3 = 0; i3 < iCeil; i3++) {
            for (int i4 = 0; i4 < i2; i4++) {
                int i5 = i4 + (i2 * i3);
                double d5 = i4 * d2;
                if (i5 < arrayList.size()) {
                    C0048ah c0048ah = (C0048ah) arrayList.get(i5);
                    Gauge gauge = new Gauge();
                    gauge.setRelativeX(d5);
                    gauge.setRelativeY(i3 * d4);
                    gauge.setRelativeWidth(d2);
                    gauge.setRelativeHeight(d4);
                    gauge.setBackColor(new Color(0, 0, 0, 0));
                    gauge.setFontColor(Color.WHITE);
                    Gauge gaugeB = b(gauge, c0048ah);
                    int iRandom = (int) (Math.random() * 2.147483647E9d);
                    HistogramPainter histogramPainter = new HistogramPainter();
                    gaugeB.setNeedleColor(a(i5));
                    gaugeB.setGaugePainter(histogramPainter);
                    gaugeB.setGroupId(iRandom);
                    arrayList2.add(gaugeB);
                    Gauge gauge2 = new Gauge();
                    gauge2.setRelativeX((d5 + d2) - d3);
                    gauge2.setRelativeY((i3 * d4) + (d4 / 3.3d));
                    gauge2.setRelativeWidth(d3);
                    gauge2.setRelativeHeight(d4 / 5.0d);
                    gauge2.setBackColor(new Color(0, 0, 0, 0));
                    gauge2.setWarnColor(Color.BLACK);
                    gauge2.setCriticalColor(Color.BLACK);
                    gauge2.setFontColor(Color.WHITE);
                    Gauge gaugeB2 = b(gauge2, c0048ah);
                    gaugeB2.setBorderWidth(0);
                    gaugeB2.setTitle("");
                    gaugeB2.setUnits("");
                    gaugeB2.setFontSizeAdjustment(2);
                    gaugeB2.setGaugePainter(new BasicReadoutGaugePainter());
                    gaugeB2.setWarnColor(gaugeB2.getBackColor());
                    gaugeB2.setCriticalColor(gaugeB2.getBackColor());
                    gaugeB2.setGroupId(iRandom);
                    arrayList2.add(gaugeB2);
                }
            }
        }
        z2.a((Component[]) arrayList2.toArray(new Gauge[arrayList2.size()]));
        return z2;
    }

    private Color a(int i2) {
        switch (i2 % 5) {
            case 0:
                return Color.CYAN;
            case 1:
                return Color.RED;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.MAGENTA;
            default:
                return new Color(0, 255, 0);
        }
    }

    private Gauge a(Gauge gauge, C0048ah c0048ah) {
        gauge.setTitle(c0048ah.k().toString());
        gauge.setUnits(c0048ah.j().toString());
        gauge.setMinVP(c0048ah.b());
        gauge.setMaxVP(c0048ah.e());
        gauge.setLowWarningVP(c0048ah.f());
        gauge.setLowCriticalVP(c0048ah.o());
        gauge.setHighWarningVP(c0048ah.g());
        gauge.setHighCriticalVP(c0048ah.h());
        gauge.setOutputChannel(c0048ah.i());
        gauge.setValueDigits(c0048ah.m());
        gauge.setLabelDigits(c0048ah.n());
        gauge.setValue(c0048ah.d());
        return gauge;
    }

    private Gauge b(Gauge gauge, C0048ah c0048ah) {
        gauge.setTitle(c0048ah.k().toString());
        gauge.setUnits(c0048ah.j().toString());
        gauge.setMin(c0048ah.b());
        gauge.setMax(c0048ah.e());
        gauge.setLowWarning(c0048ah.f());
        gauge.setLowCritical(c0048ah.o());
        gauge.setHighWarning(c0048ah.g());
        gauge.setHighCritical(c0048ah.h());
        gauge.setOutputChannel(c0048ah.i());
        gauge.setValueDigits(c0048ah.m());
        gauge.setLabelDigits(c0048ah.n());
        gauge.setBorderWidth(1);
        gauge.setValue(c0048ah.d());
        return gauge;
    }

    private boolean a(String str) {
        return f9453b != null && f9452a == 7 && str != null && str.equals("FrontPage");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r33v0, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r33v1 */
    /* JADX WARN: Type inference failed for: r33v2 */
    public Z a(G.R r2, String str, int i2, int i3) {
        if (a(str)) {
            try {
                return b(r2, f9453b);
            } catch (V.a e2) {
                e2.printStackTrace();
            }
        }
        String[] strArrC = c(r2, str);
        Gauge gaugeD = d();
        ArrayList arrayListA = a(r2, strArrC, true);
        Z z2 = new Z();
        z2.a(Color.BLACK);
        z2.b(r2.i());
        double d2 = 0.0d;
        double d3 = 0.0015d;
        ArrayList arrayListA2 = a();
        if (str.equals("FrontPage")) {
            d3 = 0.005d;
            d2 = 0.031d;
            z2.a(new Color(51, 51, 51));
            z2.b(new Color(20, 20, 20));
        }
        double d4 = 1.0d / i3;
        int iW = r2.w() + arrayListA2.size();
        int i4 = (iW / 14) + 1;
        if (i4 == 1 && r2.w() > 14) {
            i4 = 2;
        }
        double d5 = (1.0d - (i4 * d2)) / i2;
        Component[] componentArr = new AbstractC1420s[arrayListA.size()];
        for (int i5 = 0; i5 < i2; i5++) {
            for (int i6 = 0; i6 < i3; i6++) {
                int i7 = i6 + (i3 * i5);
                if (i7 < arrayListA.size()) {
                    C0048ah c0048ah = (C0048ah) arrayListA.get(i7);
                    Gauge gauge = new Gauge();
                    gauge.setRelativeX((i6 * d4) + 0.0015d);
                    gauge.setRelativeY((i5 * d5) + d3);
                    gauge.setRelativeWidth(d4 - (0.0015d * 2.0d));
                    gauge.setRelativeHeight(d5 - (d3 * 2.0d));
                    gauge.setEcuConfigurationName(r2.c());
                    if (gaugeD != null) {
                        gauge = a(gaugeD, gauge);
                    } else {
                        gauge.setSweepAngle(300);
                        gauge.setSweepBeginDegree(300);
                        gauge.setFaceAngle(360);
                        gauge.setStartAngle(0);
                        gauge.setRelativeBorderWidth2(0.02d);
                        gauge.setDisplayValueAt180(true);
                    }
                    gauge.setCounterClockwise(c0048ah.q());
                    gauge.setTitle(c0048ah.k().toString());
                    gauge.setUnits(c0048ah.j().toString());
                    gauge.setMin(c0048ah.b());
                    gauge.setMax(c0048ah.e());
                    gauge.setLowWarning(c0048ah.f());
                    gauge.setLowCritical(c0048ah.o());
                    gauge.setHighWarning(c0048ah.g());
                    gauge.setHighCritical(c0048ah.h());
                    gauge.setOutputChannel(c0048ah.i());
                    gauge.setValueDigits(c0048ah.m());
                    gauge.setLabelDigits(c0048ah.n());
                    gauge.setValue(c0048ah.d());
                    componentArr[i7] = gauge;
                }
            }
        }
        if (str.equals("FrontPage")) {
            z2.a(C1389ab.a());
            int i8 = 0;
            double dCeil = iW % i4 == 0 ? i4 / iW : 1.0f / ((int) Math.ceil(iW / i4));
            AbstractC1420s[] abstractC1420sArr = new AbstractC1420s[componentArr.length + iW];
            System.arraycopy(componentArr, 0, abstractC1420sArr, 0, componentArr.length);
            componentArr = abstractC1420sArr;
            Iterator itA = r2.A();
            while (itA.hasNext()) {
                C0051ak c0051ak = (C0051ak) itA.next();
                if (c0051ak.k()) {
                    Indicator indicator = new Indicator();
                    indicator.setRelativeX((0.0015d + (i8 * dCeil)) % 1.0d);
                    indicator.setRelativeY((1.0d - 0.0015d) - (d2 * (i4 - ((int) (i8 * dCeil)))));
                    indicator.setRelativeWidth(dCeil - (0.0015d * 2.0d));
                    indicator.setRelativeHeight(d2 - (0.0015d * 2.0d));
                    indicator.setOnText(c0051ak.a().toString());
                    indicator.setOnBackgroundColor(new Color(c0051ak.g().a()));
                    indicator.setOnTextColor(new Color(c0051ak.i().a()));
                    indicator.setOffText(c0051ak.d().toString());
                    indicator.setOffBackgroundColor(new Color(c0051ak.h().a()));
                    indicator.setOffTextColor(new Color(c0051ak.j().a()));
                    indicator.setOutputChannel(c0051ak.f());
                    componentArr[(componentArr.length - i8) - 1] = indicator;
                    i8++;
                }
            }
            Iterator it = arrayListA2.iterator();
            while (it.hasNext()) {
                C0051ak c0051ak2 = (C0051ak) it.next();
                Indicator indicator2 = new Indicator();
                indicator2.setEcuConfigurationName(C0113cs.f1154a);
                indicator2.setRelativeX((0.0015d + (i8 * dCeil)) % 1.0d);
                indicator2.setRelativeY((1.0d - 0.0015d) - (d2 * (i4 - ((int) (i8 * dCeil)))));
                indicator2.setRelativeWidth(dCeil - (0.0015d * 2.0d));
                indicator2.setRelativeHeight(d2 - (0.0015d * 2.0d));
                indicator2.setOnText(c0051ak2.a().toString());
                indicator2.setOnBackgroundColor(a(c0051ak2.g()));
                indicator2.setOnTextColor(a(c0051ak2.i()));
                indicator2.setOffText(c0051ak2.d().toString());
                indicator2.setOffBackgroundColor(a(c0051ak2.h()));
                indicator2.setOffTextColor(a(c0051ak2.j()));
                indicator2.setOutputChannel(c0051ak2.f());
                componentArr[(componentArr.length - i8) - 1] = indicator2;
                i8++;
            }
        } else {
            z2.a(C1389ab.b());
        }
        z2.a(componentArr);
        z2.b(c());
        b(r2, z2);
        return z2;
    }

    private Color a(C0128k c0128k) {
        return new Color(c0128k.b(), c0128k.c(), c0128k.d(), c0128k.e());
    }

    private Color c() {
        return f9462d;
    }

    public Z a(G.R r2) {
        return a((InterfaceC0110cp) r2);
    }

    public Z b(G.R r2) {
        C0048ah c0048ahA;
        ArrayList arrayList = new ArrayList();
        arrayList.add("rpm");
        arrayList.add(PolicyMappingsExtension.MAP);
        arrayList.add("mat");
        arrayList.add("clt");
        arrayList.add("O2");
        arrayList.add("tps");
        arrayList.add("warmupEnrich");
        arrayList.add("batteryVoltage");
        if (r2.d("NARROW_BAND_EGO") != null) {
            arrayList.add("egoVoltage");
        } else if (r2.g("afr") != null) {
            arrayList.add("afr");
        } else if (r2.g("afr1") != null) {
            arrayList.add("afr1");
        } else if (r2.g("lambda") != null) {
            arrayList.add("lambda");
        }
        arrayList.add("coolant");
        arrayList.add("throttle");
        arrayList.add("advance");
        arrayList.add("pulseWidth1");
        arrayList.add("gammaEnrich");
        arrayList.add("veCurr1");
        arrayList.add("advSpark");
        arrayList.add("iacstep");
        ArrayList arrayList2 = new ArrayList();
        Object[] objArrA = bH.R.a((Object[]) r2.s());
        for (int i2 = 0; i2 < objArrA.length; i2++) {
            if (arrayList.contains(objArrA[i2]) && (c0048ahA = a(r2, (String) objArrA[i2])) != null) {
                arrayList2.add(c0048ahA);
            }
        }
        List listA = bH.R.a(arrayList2);
        ArrayList arrayList3 = new ArrayList();
        Iterator it = listA.iterator();
        while (it.hasNext()) {
            arrayList3.add((C0048ah) it.next());
        }
        return a(a(arrayList3, 2), r2.c());
    }

    public Z a(Z z2, String str) {
        for (Component component : z2.c()) {
            if (component instanceof AbstractC1420s) {
                ((AbstractC1420s) component).setEcuConfigurationName(str);
            }
        }
        return z2;
    }

    public int a(G.R r2, String[] strArr) {
        int i2 = 0;
        for (String str : strArr) {
            if (r2.k(str) != null) {
                i2++;
            }
        }
        return i2;
    }

    public Z a(InterfaceC0110cp interfaceC0110cp) {
        int i2;
        Object[] objArrA = bH.R.a((Object[]) interfaceC0110cp.s());
        Z z2 = new Z();
        z2.a(Color.LIGHT_GRAY);
        Component[] componentArr = new AbstractC1420s[objArrA.length];
        int iRound = (int) Math.round(Math.sqrt(2.0d * objArrA.length));
        int iCeil = (int) Math.ceil(objArrA.length / iRound);
        while ((iRound - 1) * iCeil >= objArrA.length) {
            iRound--;
        }
        double d2 = 1.0d / iCeil;
        double d3 = 1.0d / iRound;
        for (int i3 = 0; i3 < iRound; i3++) {
            for (int i4 = 0; i4 < iCeil && (i2 = i4 + (iCeil * i3)) < objArrA.length; i4++) {
                String str = (String) objArrA[i2];
                Gauge gauge = new Gauge();
                gauge.setGaugePainter(new BasicReadoutGaugePainter());
                gauge.setOutputChannel(str);
                gauge.setEcuConfigurationName(interfaceC0110cp.c());
                gauge.setRelativeX((i4 * d2) + 0.001d);
                gauge.setRelativeY((i3 * d3) + 0.001d);
                gauge.setRelativeWidth(d2 - (0.001d * 2.0d));
                gauge.setRelativeHeight(d3 - (0.001d * 2.0d));
                gauge.setBorderWidth(0);
                gauge.setBackColor(Color.BLACK);
                gauge.setWarnColor(Color.BLACK);
                gauge.setCriticalColor(Color.BLACK);
                gauge.setFontColor(Color.WHITE);
                gauge.setTitle(str);
                gauge.setUnits("");
                componentArr[i2] = gauge;
            }
        }
        z2.a(componentArr);
        return z2;
    }

    private ArrayList a(G.R r2, String[] strArr, boolean z2) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            if (r2.k(str) != null && (!str.equals("veBucketGauge") || z2)) {
                arrayList.add(r2.k(str));
            }
        }
        return arrayList;
    }

    public Z b(G.R r2, Z z2) {
        C0048ah c0048ahA;
        boolean z3 = r2.d("CELSIUS") != null;
        boolean z4 = r2.d("NARROW_BAND_EGO") != null;
        boolean z5 = r2.d("LAMBDA") != null;
        Component[] componentArrC = z2.c();
        for (int i2 = 0; i2 < componentArrC.length; i2++) {
            if (componentArrC[i2] instanceof Gauge) {
                Gauge gauge = (Gauge) componentArrC[i2];
                if (r2.O().ae() && ((z3 && gauge.getUnits() != null && (gauge.getUnits().contains(bH.S.a() + PdfOps.F_TOKEN) || gauge.getUnits().endsWith(PdfOps.F_TOKEN))) || (!(z3 || gauge.getUnits() == null || (!gauge.getUnits().contains(bH.S.a() + "C") && !gauge.getUnits().endsWith("C"))) || (gauge.units() != null && gauge.units().contains("%TEMP"))))) {
                    C0048ah c0048ahA2 = a(r2, gauge.getOutputChannel());
                    if (c0048ahA2 != null) {
                        gauge.setMin(c0048ahA2.b());
                        gauge.setMax(c0048ahA2.e());
                        gauge.setLowWarning(c0048ahA2.f());
                        gauge.setLowCritical(c0048ahA2.o());
                        gauge.setHighWarning(c0048ahA2.g());
                        gauge.setHighCritical(c0048ahA2.h());
                        if (z3) {
                            gauge.setUnits(bH.S.a() + "C");
                        } else {
                            gauge.setUnits(bH.S.a() + PdfOps.F_TOKEN);
                        }
                    }
                } else if (z4 && gauge.getOutputChannel() != null && gauge.getOutputChannel().contains("afr") && (c0048ahA = a(r2, "egoVoltage")) != null) {
                    gauge.setMin(c0048ahA.b());
                    gauge.setMax(c0048ahA.e());
                    gauge.setUnits(c0048ahA.j().toString());
                    gauge.setTitle(c0048ahA.k().toString());
                    gauge.setOutputChannel(c0048ahA.i());
                    gauge.setLowWarning(c0048ahA.f());
                    gauge.setLowCritical(c0048ahA.o());
                    gauge.setHighWarning(c0048ahA.g());
                    gauge.setHighCritical(c0048ahA.h());
                }
            }
        }
        return z2;
    }

    public C0048ah a(G.R r2, String str) {
        Iterator itB = r2.B();
        while (itB.hasNext()) {
            C0048ah c0048ah = (C0048ah) itB.next();
            if (c0048ah.i().equals(str)) {
                return c0048ah;
            }
        }
        return null;
    }

    public ArrayList a() {
        ArrayList arrayList = new ArrayList();
        Iterator itB = I.d.a().b();
        while (itB.hasNext()) {
            arrayList.add(itB.next());
        }
        return arrayList;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x005c, code lost:
    
        r4.f9455e = (com.efiAnalytics.apps.ts.dashboard.Gauge) r0.get(0);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.efiAnalytics.apps.ts.dashboard.Gauge d() {
        /*
            r4 = this;
            r0 = r4
            com.efiAnalytics.apps.ts.dashboard.Gauge r0 = r0.f9455e
            if (r0 != 0) goto L8d
            java.lang.String r0 = com.efiAnalytics.apps.ts.dashboard.C1388aa.f9454c
            if (r0 == 0) goto L8d
            java.lang.String r0 = com.efiAnalytics.apps.ts.dashboard.C1388aa.f9454c
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L8d
            r0 = r4
            java.lang.Class r0 = r0.getClass()     // Catch: java.lang.Exception -> L73
            java.lang.String r1 = com.efiAnalytics.apps.ts.dashboard.C1388aa.f9454c     // Catch: java.lang.Exception -> L73
            java.io.InputStream r0 = r0.getResourceAsStream(r1)     // Catch: java.lang.Exception -> L73
            r5 = r0
            v.c r0 = new v.c     // Catch: java.lang.Exception -> L73
            r1 = r0
            java.io.File r2 = r.C1807j.G()     // Catch: java.lang.Exception -> L73
            r1.<init>(r2)     // Catch: java.lang.Exception -> L73
            r1 = r5
            java.util.ArrayList r0 = r0.a(r1)     // Catch: java.lang.Exception -> L73
            r6 = r0
            r0 = r6
            int r0 = r0.size()     // Catch: java.lang.Exception -> L73
            if (r0 <= 0) goto L70
            r0 = r6
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Exception -> L73
            r7 = r0
        L40:
            r0 = r7
            boolean r0 = r0.hasNext()     // Catch: java.lang.Exception -> L73
            if (r0 == 0) goto L70
            r0 = r7
            java.lang.Object r0 = r0.next()     // Catch: java.lang.Exception -> L73
            com.efiAnalytics.apps.ts.dashboard.s r0 = (com.efiAnalytics.apps.ts.dashboard.AbstractC1420s) r0     // Catch: java.lang.Exception -> L73
            r8 = r0
            r0 = r8
            boolean r0 = r0 instanceof com.efiAnalytics.apps.ts.dashboard.Gauge     // Catch: java.lang.Exception -> L73
            if (r0 == 0) goto L6d
            r0 = r4
            r1 = r6
            r2 = 0
            java.lang.Object r1 = r1.get(r2)     // Catch: java.lang.Exception -> L73
            com.efiAnalytics.apps.ts.dashboard.Gauge r1 = (com.efiAnalytics.apps.ts.dashboard.Gauge) r1     // Catch: java.lang.Exception -> L73
            r0.f9455e = r1     // Catch: java.lang.Exception -> L73
            goto L70
        L6d:
            goto L40
        L70:
            goto L8d
        L73:
            r5 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "Failed to load Gauge Template, will use generated gauges: "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r5
            java.lang.String r1 = r1.getLocalizedMessage()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            bH.C.b(r0)
        L8d:
            r0 = r4
            com.efiAnalytics.apps.ts.dashboard.Gauge r0 = r0.f9455e
            if (r0 != 0) goto Lcf
            r0 = r4
            com.efiAnalytics.apps.ts.dashboard.Gauge r1 = new com.efiAnalytics.apps.ts.dashboard.Gauge
            r2 = r1
            r2.<init>()
            r0.f9455e = r1
            r0 = r4
            com.efiAnalytics.apps.ts.dashboard.Gauge r0 = r0.f9455e
            r1 = 300(0x12c, float:4.2E-43)
            r0.setSweepAngle(r1)
            r0 = r4
            com.efiAnalytics.apps.ts.dashboard.Gauge r0 = r0.f9455e
            r1 = 300(0x12c, float:4.2E-43)
            r0.setSweepBeginDegree(r1)
            r0 = r4
            com.efiAnalytics.apps.ts.dashboard.Gauge r0 = r0.f9455e
            r1 = 360(0x168, float:5.04E-43)
            r0.setFaceAngle(r1)
            r0 = r4
            com.efiAnalytics.apps.ts.dashboard.Gauge r0 = r0.f9455e
            r1 = 0
            r0.setStartAngle(r1)
            r0 = r4
            com.efiAnalytics.apps.ts.dashboard.Gauge r0 = r0.f9455e
            r1 = 4581421828931458171(0x3f947ae147ae147b, double:0.02)
            r0.setRelativeBorderWidth2(r1)
        Lcf:
            r0 = r4
            com.efiAnalytics.apps.ts.dashboard.Gauge r0 = r0.f9455e
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.efiAnalytics.apps.ts.dashboard.C1388aa.d():com.efiAnalytics.apps.ts.dashboard.Gauge");
    }

    public static Gauge b() {
        return a(f9461k.d(), new Gauge());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Gauge a(File file) {
        if (file == null) {
            return b();
        }
        Gauge gauge = null;
        ArrayList arrayListB = new C1883c(C1807j.G()).b(file.getAbsolutePath());
        if (arrayListB.size() > 0) {
            Iterator<E> it = arrayListB.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (((AbstractC1420s) it.next()) instanceof Gauge) {
                    gauge = (Gauge) arrayListB.get(0);
                    break;
                }
            }
        }
        if (gauge == null) {
            return new Gauge();
        }
        gauge.setGroupId(0);
        gauge.setTitle("Title");
        gauge.setUnits("Units");
        gauge.setEcuConfigurationName("");
        gauge.setOutputChannel(null);
        gauge.setMinVP(0);
        gauge.setMaxVP(100);
        gauge.setValue(35.0d);
        gauge.setLowCriticalVP(5);
        gauge.setLowWarningVP(10);
        gauge.setHighWarningVP(80);
        gauge.setHighCriticalVP(90);
        return gauge;
    }

    public static Gauge a(Gauge gauge, Gauge gauge2) {
        gauge2.setSweepAngle(gauge.getSweepAngle());
        gauge2.setSweepBeginDegree(gauge.getSweepBeginDegree());
        gauge2.setFaceAngle(gauge.getFaceAngle());
        gauge2.setStartAngle(gauge.getStartAngle());
        gauge2.setRelativeBorderWidth2(gauge.getRelativeBorderWidth2());
        gauge2.setBackColor(gauge.getBackColor());
        gauge2.setFontColor(gauge.getFontColor());
        gauge2.setCriticalColor(gauge.getCriticalColor());
        gauge2.setWarnColor(gauge.getWarnColor());
        gauge2.setNeedleColor(gauge.getNeedleColor());
        gauge2.setTrimColor(gauge.getTrimColor());
        gauge2.setFontFamily(gauge.getFontFamily());
        gauge2.setFontSizeAdjustment(gauge.getFontSizeAdjustment());
        gauge2.setBackgroundImageFileName(gauge.getBackgroundImageFileName());
        gauge2.setNeedleImageFileName(gauge.getNeedleImageFileName());
        gauge2.setNeedleSmoothing(gauge.getNeedleSmoothing());
        gauge2.setDisplayValueAt180(gauge.isDisplayValueAt180());
        gauge2.setBorderWidth(gauge.getBorderWidth());
        gauge2.setGaugePainter(com.efiAnalytics.apps.ts.dashboard.renderers.e.a(com.efiAnalytics.apps.ts.dashboard.renderers.e.a(gauge.getGaugePainter())));
        return gauge2;
    }
}
