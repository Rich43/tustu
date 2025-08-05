package aI;

import G.C0130m;
import G.C0132o;
import G.R;
import W.af;
import bH.C;
import bH.C0995c;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:aI/j.class */
public class j {

    /* renamed from: a, reason: collision with root package name */
    R f2461a;

    /* renamed from: b, reason: collision with root package name */
    int[] f2462b = null;

    public j(R r2) {
        this.f2461a = null;
        this.f2461a = r2;
    }

    public void a() {
        this.f2462b = null;
    }

    public List a(int i2) {
        ArrayList arrayList = new ArrayList();
        int i3 = 0;
        while (true) {
            int i4 = i3;
            i3++;
            if (!a(arrayList, i4) || i3 >= 250 || (i2 >= 0 && i2 < arrayList.size())) {
                break;
            }
        }
        return arrayList;
    }

    private boolean a(List list, int i2) throws RemoteAccessException {
        ArrayList arrayList = new ArrayList();
        C0130m c0130mB = d.b(this.f2461a.O(), i2);
        c0130mB.i(30);
        arrayList.add(c0130mB);
        arrayList.add(d.c(this.f2461a.O()));
        C0130m c0130mA = C0130m.a(this.f2461a.O(), arrayList);
        c0130mA.v("Dir List Chunk: " + i2);
        C0132o c0132oA = o.d(this.f2461a).a(c0130mA, false, 1500);
        if (c0132oA == null) {
            throw new RemoteAccessException("Unable to read MS3 SD Directory Listing. No Response");
        }
        if (c0132oA.a() == 3) {
            throw new RemoteAccessException("Unable to read MS3 SD Directory Listing. \nMessage:\n" + c0132oA.c());
        }
        if (this.f2462b == null || !C0995c.b(this.f2462b, c0132oA.e())) {
            this.f2462b = c0132oA.e();
            return a(list, c0132oA.e());
        }
        C.b("SD Dir read, Firmware has began returning redundant data. Aborting further reads.");
        return false;
    }

    private boolean a(List list, int[] iArr) {
        if (iArr == null || b(iArr)) {
            return false;
        }
        int[] iArr2 = new int[32];
        int length = 0;
        while (true) {
            int i2 = length;
            if (i2 + iArr2.length >= iArr.length) {
                return true;
            }
            try {
                System.arraycopy(iArr, i2, iArr2, 0, iArr2.length);
                RemoteFileDescriptor remoteFileDescriptorA = a(iArr2);
                if (remoteFileDescriptorA != null && remoteFileDescriptorA.getName() != null && !remoteFileDescriptorA.getName().equals(".")) {
                    list.add(remoteFileDescriptorA);
                }
            } catch (k e2) {
            }
            length = i2 + iArr2.length;
        }
    }

    private RemoteFileDescriptor a(int[] iArr) throws k {
        RemoteFileDescriptor remoteFileDescriptor = null;
        if (iArr != null && iArr.length == 32 && (iArr[11] & 8) == 0 && (iArr[11] & 16) == 0 && (iArr[11] & 64) == 0 && (iArr[11] & 4) == 0 && iArr[11] > 0 && iArr[0] != 229 && iArr[0] != 0 && iArr[0] != 46 && iArr[0] != 255 && iArr[0] >= 32 && iArr[0] < 128) {
            remoteFileDescriptor = new RemoteFileDescriptor();
            int[] iArr2 = new int[8];
            int[] iArr3 = new int[3];
            System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
            System.arraycopy(iArr, iArr2.length, iArr3, 0, iArr3.length);
            remoteFileDescriptor.setName(new String(C0995c.a(iArr2)).trim() + '.' + new String(C0995c.a(iArr3)));
            int[] iArr4 = new int[4];
            System.arraycopy(iArr, 14, iArr4, 0, 4);
            remoteFileDescriptor.setLastModified(af.a(C0995c.b(iArr4, 0, 4, false, true)).getTime());
            long jC = C0995c.c(iArr, 18, 4, true, true);
            C.c("Sector Number: " + jC);
            v vVar = new v();
            vVar.a(jC);
            remoteFileDescriptor.setDirectory(vVar);
            remoteFileDescriptor.setSize(C0995c.c(iArr, 28, 4, false, true));
            if (remoteFileDescriptor.getSize() < 0 || remoteFileDescriptor.getDirectory().getNumericId() < 0) {
                C.c("File: " + C0995c.a(remoteFileDescriptor.getName()) + " read from SD, but the size or sector can not be valid. Size:=" + remoteFileDescriptor.getSize() + ", sector=" + remoteFileDescriptor.getDirectory().getNumericId());
                remoteFileDescriptor = null;
            }
        } else if (iArr == null || iArr.length < 32 || iArr[0] == 0 || iArr[0] == 255) {
            throw new k(this);
        }
        return remoteFileDescriptor;
    }

    private boolean b(int[] iArr) {
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= iArr.length) {
                return true;
            }
            if (iArr[i3] != 0) {
                return false;
            }
            i2 = i3 + 32;
        }
    }
}
