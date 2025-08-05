package v;

import G.dh;
import com.efiAnalytics.apps.ts.dashboard.DashLabel;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.BurnButtonTv;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.SelectableTable;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.TableCellCrossHair;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.TuneSettingsPanel;
import com.efiAnalytics.apps.ts.tuningViews.tuneComps.TuneViewGaugeCluster;

/* renamed from: v.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:v/a.class */
public class C1881a {

    /* renamed from: a, reason: collision with root package name */
    public static String f14003a = "Gauge";

    /* renamed from: b, reason: collision with root package name */
    public static String f14004b = "Indicator";

    /* renamed from: c, reason: collision with root package name */
    public static String f14005c = "Label";

    /* renamed from: d, reason: collision with root package name */
    public static String f14006d = "com.efiAnalytics.tunerStudio.Gauge";

    /* renamed from: e, reason: collision with root package name */
    public static String f14007e = "com.efiAnalytics.tunerStudio.Indicator";

    /* renamed from: f, reason: collision with root package name */
    public static String f14008f = "com.efiAnalytics.tunerStudio.dashboard.DashLabel";

    /* renamed from: g, reason: collision with root package name */
    public static String f14009g = "ValueProvider";

    /* renamed from: h, reason: collision with root package name */
    public static String f14010h = "TuneGaugeCluster";

    /* renamed from: i, reason: collision with root package name */
    public static String f14011i = "TuneSlectableTable";

    /* renamed from: j, reason: collision with root package name */
    public static String f14012j = "TuneSettingsPanel";

    /* renamed from: k, reason: collision with root package name */
    public static String f14013k = "BurnButtonTv";

    /* renamed from: l, reason: collision with root package name */
    public static String f14014l = "TableCellCrossHair";

    public static Class a(String str) {
        return (str.equals("com.efiAnalytics.tunerStudio.Gauge") || str.equals(f14003a)) ? Gauge.class : (str.equals("com.efiAnalytics.tunerStudio.Indicator") || str.equals(f14004b)) ? Indicator.class : (str.equals(f14006d) || str.equals(f14005c) || str.equals("com.efiAnalytics.tunerStudio.dashboard.DashLabel")) ? DashLabel.class : str.equals(f14010h) ? TuneViewGaugeCluster.class : str.equals(f14011i) ? SelectableTable.class : str.equals(f14012j) ? TuneSettingsPanel.class : str.equals(f14013k) ? BurnButtonTv.class : str.equals(f14014l) ? TableCellCrossHair.class : Class.forName(str);
    }

    public static String a(Object obj) {
        return obj instanceof Gauge ? f14003a : obj instanceof Indicator ? f14004b : obj instanceof DashLabel ? f14005c : obj instanceof dh ? f14009g : obj instanceof TuneViewGaugeCluster ? f14010h : obj instanceof SelectableTable ? f14011i : obj instanceof TuneSettingsPanel ? f14012j : obj instanceof BurnButtonTv ? f14013k : obj instanceof TableCellCrossHair ? f14014l : obj.getClass().getName();
    }
}
