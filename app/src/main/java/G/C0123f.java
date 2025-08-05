package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* renamed from: G.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/f.class */
public class C0123f implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private final HashMap f1240a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    private final HashMap f1241b = new HashMap();

    public byte[] a(int i2) {
        byte[] bArr;
        synchronized (this.f1240a) {
            byte[] bArr2 = (byte[]) this.f1240a.get(Integer.valueOf(i2));
            if (bArr2 == null) {
                bArr2 = new byte[i2];
                this.f1240a.put(Integer.valueOf(i2), bArr2);
            }
            bArr = bArr2;
        }
        return bArr;
    }

    public byte[] b(int i2) {
        synchronized (this.f1241b) {
            List listC = c(i2);
            if (listC.isEmpty()) {
                return new byte[i2];
            }
            return (byte[]) listC.remove(0);
        }
    }

    public synchronized void a(byte[] bArr) {
        if (bArr != null) {
            List listC = c(bArr.length);
            if (listC.contains(bArr)) {
                return;
            }
            listC.add(bArr);
        }
    }

    private List c(int i2) {
        List list;
        synchronized (this.f1241b) {
            List arrayList = (List) this.f1241b.get(Integer.valueOf(i2));
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.f1241b.put(Integer.valueOf(i2), arrayList);
            }
            list = arrayList;
        }
        return list;
    }
}
