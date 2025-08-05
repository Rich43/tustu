package v;

import bH.C;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.ui.C1606cq;
import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import r.C1807j;

/* renamed from: v.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:v/b.class */
public class C1882b {
    public static HashMap a(Component[] componentArr) {
        File fileA;
        HashMap map = new HashMap();
        int i2 = 0;
        for (int i3 = 0; i3 < componentArr.length; i3++) {
            if (componentArr[i3] instanceof Gauge) {
                Gauge gauge = (Gauge) componentArr[i3];
                if (gauge.getBackgroundImageFileName() != null && !gauge.getBackgroundImageFileName().equals("")) {
                    String backgroundImageFileName = gauge.getBackgroundImageFileName();
                    if (map.get(backgroundImageFileName) == null) {
                        int i4 = i2;
                        i2++;
                        String str = "IMG_ID_" + new File(backgroundImageFileName).getName() + "_" + i4;
                        map.put(backgroundImageFileName, str);
                        gauge.setBackgroundImageFileName(str);
                    } else {
                        gauge.setBackgroundImageFileName((String) map.get(backgroundImageFileName));
                    }
                }
                if (gauge.getNeedleImageFileName() != null && !gauge.getNeedleImageFileName().equals("")) {
                    String needleImageFileName = gauge.getNeedleImageFileName();
                    if (map.get(needleImageFileName) == null) {
                        int i5 = i2;
                        i2++;
                        String str2 = "IMG_ID_" + new File(needleImageFileName).getName() + "_" + i5;
                        map.put(needleImageFileName, str2);
                        gauge.setNeedleImageFileName(str2);
                    } else {
                        gauge.setNeedleImageFileName((String) map.get(needleImageFileName));
                    }
                }
                if (gauge.getFontFamily() != null && gauge.getFontFamily().length() > 0) {
                    File fileA2 = C1606cq.a().a(gauge.getFontFamily(), C1807j.F());
                    if (fileA2 != null) {
                        String absolutePath = fileA2.getAbsolutePath();
                        if (map.get(absolutePath) == null) {
                            map.put(absolutePath, gauge.getFontFamily());
                        }
                    } else {
                        C.b("Font File not found for: " + gauge.getFontFamily() + ", not saved to .dash file");
                    }
                }
            }
            if (componentArr[i3] instanceof Indicator) {
                Indicator indicator = (Indicator) componentArr[i3];
                if (indicator.getFontFamily() != null && indicator.getFontFamily().length() > 0 && (fileA = C1606cq.a().a(indicator.getFontFamily(), C1807j.F())) != null) {
                    String absolutePath2 = fileA.getAbsolutePath();
                    if (map.get(absolutePath2) == null) {
                        map.put(absolutePath2, indicator.getFontFamily());
                    }
                }
            }
            if (componentArr[i3] instanceof Indicator) {
                Indicator indicator2 = (Indicator) componentArr[i3];
                if (indicator2.getOnImageFileName() != null && !indicator2.getOnImageFileName().equals("")) {
                    String onImageFileName = indicator2.getOnImageFileName();
                    if (map.get(onImageFileName) == null) {
                        int i6 = i2;
                        i2++;
                        String str3 = "IMG_ID_" + new File(onImageFileName).getName() + "_" + i6;
                        map.put(onImageFileName, str3);
                        indicator2.setOnImageFileName(str3);
                    } else {
                        indicator2.setOnImageFileName((String) map.get(onImageFileName));
                    }
                }
            }
            if (componentArr[i3] instanceof Indicator) {
                Indicator indicator3 = (Indicator) componentArr[i3];
                if (indicator3.getOffImageFileName() != null && !indicator3.getOffImageFileName().equals("")) {
                    String offImageFileName = indicator3.getOffImageFileName();
                    if (map.get(offImageFileName) == null) {
                        int i7 = i2;
                        i2++;
                        String str4 = "IMG_ID_" + new File(offImageFileName).getName() + "_" + i7;
                        map.put(offImageFileName, str4);
                        indicator3.setOffImageFileName(str4);
                    } else {
                        indicator3.setOffImageFileName((String) map.get(offImageFileName));
                    }
                }
            }
        }
        return map;
    }

    public static Component[] a(HashMap map, Component[] componentArr) {
        HashMap map2 = new HashMap();
        for (String str : map.keySet()) {
            map2.put((String) map.get(str), str);
        }
        return b(map2, componentArr);
    }

    public static Component[] b(HashMap map, Component[] componentArr) {
        for (int i2 = 0; i2 < componentArr.length; i2++) {
            if (componentArr[i2] instanceof Gauge) {
                Gauge gauge = (Gauge) componentArr[i2];
                if (gauge.getBackgroundImageFileName() != null && !gauge.getBackgroundImageFileName().equals("")) {
                    gauge.setBackgroundImageFileName((String) map.get(gauge.getBackgroundImageFileName()));
                }
                if (gauge.getNeedleImageFileName() != null && !gauge.getNeedleImageFileName().equals("")) {
                    gauge.setNeedleImageFileName((String) map.get(gauge.getNeedleImageFileName()));
                }
            }
            if (componentArr[i2] instanceof Indicator) {
                Indicator indicator = (Indicator) componentArr[i2];
                if (indicator.getOnImageFileName() != null && !indicator.getOnImageFileName().equals("")) {
                    indicator.setOnImageFileName((String) map.get(indicator.getOnImageFileName()));
                }
            }
            if (componentArr[i2] instanceof Indicator) {
                Indicator indicator2 = (Indicator) componentArr[i2];
                if (indicator2.getOffImageFileName() != null && !indicator2.getOffImageFileName().equals("")) {
                    indicator2.setOffImageFileName((String) map.get(indicator2.getOffImageFileName()));
                }
            }
        }
        return componentArr;
    }
}
