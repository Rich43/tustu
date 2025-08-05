package L;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:L/V.class */
public class V {

    /* renamed from: d, reason: collision with root package name */
    private Map f1597d = Collections.synchronizedMap(new HashMap());

    /* renamed from: a, reason: collision with root package name */
    Long f1598a = 0L;

    /* renamed from: c, reason: collision with root package name */
    long f1600c = System.currentTimeMillis();

    /* renamed from: f, reason: collision with root package name */
    private long f1602f = 86400000;

    /* renamed from: b, reason: collision with root package name */
    static int f1599b = 300;

    /* renamed from: e, reason: collision with root package name */
    private static V f1601e = null;

    public static V a() {
        if (f1601e == null) {
            f1601e = new V();
        }
        return f1601e;
    }

    public Long a(String str, String str2) throws FileNotFoundException {
        if (System.currentTimeMillis() - this.f1600c > this.f1602f) {
            this.f1600c = System.currentTimeMillis();
            bH.E.c();
        }
        try {
            if (!bH.E.b(str, str2).a()) {
                throw new FileNotFoundException("File Not Found: " + str2 + " in working Dir:" + str);
            }
            Long l2 = this.f1598a;
            this.f1598a = Long.valueOf(this.f1598a.longValue() + 1);
            this.f1597d.put(l2, new W(this, str, str2));
            if (this.f1597d.size() > f1599b) {
                b();
            }
            return l2;
        } catch (IOException e2) {
            Logger.getLogger(V.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new FileNotFoundException("File Not Found: " + str2 + " in working Dir:" + str);
        }
    }

    private void b() {
        Long lValueOf = Long.valueOf(Long.MAX_VALUE);
        Long[] lArr = (Long[]) this.f1597d.keySet().toArray(new Long[this.f1597d.size()]);
        for (int i2 = 0; i2 < lArr.length; i2++) {
            if (lArr[i2].longValue() < lValueOf.longValue()) {
                lValueOf = lArr[i2];
            }
        }
        this.f1597d.remove(lValueOf);
    }

    public bH.E a(Long l2) throws FileNotFoundException {
        W w2 = (W) this.f1597d.get(l2);
        if (w2 == null) {
            return null;
        }
        try {
            return bH.E.b(w2.f1603a, w2.f1604b);
        } catch (IOException e2) {
            Logger.getLogger(V.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new FileNotFoundException("File Not Found: " + w2.f1603a + " in working Dir:" + w2.f1604b);
        }
    }
}
