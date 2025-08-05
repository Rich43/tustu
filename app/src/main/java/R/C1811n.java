package r;

import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.dQ;
import java.io.File;
import java.util.ArrayList;

/* renamed from: r.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/n.class */
public class C1811n {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1662et f13502a = new dQ(C1798a.a().d(), "recentlyOpenedProject");

    /* renamed from: b, reason: collision with root package name */
    int f13503b = 7;

    public void a(String str) {
        ArrayList arrayListA = a();
        String strB = b(str);
        while (arrayListA.contains(strB)) {
            arrayListA.remove(strB);
        }
        arrayListA.add(0, strB);
        a(arrayListA);
    }

    public ArrayList a() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.f13503b; i2++) {
            String strA = this.f13502a.a(i2 + "");
            if (strA != null && strA.trim().length() > 0 && new File(strA).exists()) {
                arrayList.add(strA);
            }
        }
        return arrayList;
    }

    private String b(String str) {
        while (str.endsWith(File.separator) && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    private void a(ArrayList arrayList) {
        int i2 = 0;
        while (i2 < this.f13503b) {
            String strB = b(i2 < arrayList.size() ? (String) arrayList.get(i2) : "");
            if (strB == null || strB.trim().length() <= 0 || !new File(strB).exists()) {
                this.f13502a.a(i2 + "", "");
            } else {
                this.f13502a.a(i2 + "", strB);
            }
            i2++;
        }
    }
}
