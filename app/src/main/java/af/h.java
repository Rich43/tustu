package af;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:af/h.class */
public class h {

    /* renamed from: a, reason: collision with root package name */
    Map f4478a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    List f4479b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    String f4480c = "";

    public static h a(List list, String str) {
        return new h().b(list, str);
    }

    private h b(List list, String str) {
        this.f4480c = str;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (str2.startsWith("Q")) {
                if (str2.contains(".")) {
                    this.f4479b.add(str2.substring(1, str2.lastIndexOf(".")));
                } else {
                    this.f4479b.add(str2);
                }
            } else if (str2.startsWith(Constants._TAG_P)) {
                try {
                    int i2 = Integer.parseInt(str2.substring(1), 16);
                    this.f4478a.put(Integer.valueOf(i2), new i(this, i2, false));
                } catch (NumberFormatException e2) {
                    e2.printStackTrace();
                }
            } else if (str2.startsWith(PdfOps.M_TOKEN)) {
                try {
                    int i3 = Integer.parseInt(str2.substring(1), 16);
                    this.f4478a.put(Integer.valueOf(i3), new i(this, i3, true));
                } catch (NumberFormatException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return this;
    }

    public boolean a(int i2) {
        i iVar = (i) this.f4478a.get(Integer.valueOf(i2));
        if (iVar == null) {
            return false;
        }
        return iVar.a();
    }

    public int a() {
        int i2 = 0;
        Iterator it = this.f4478a.values().iterator();
        while (it.hasNext()) {
            if (((i) it.next()).a()) {
                i2++;
            }
        }
        return i2;
    }

    public boolean b() {
        Iterator it = this.f4479b.iterator();
        while (it.hasNext()) {
            if (this.f4480c.startsWith((String) it.next())) {
                return true;
            }
        }
        return false;
    }
}
