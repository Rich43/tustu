package al;

import W.C0184j;
import W.C0188n;
import am.C0575c;
import am.C0576d;
import am.C0577e;
import am.h;
import am.k;
import bH.C;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;

/* renamed from: al.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:al/d.class */
public abstract class AbstractC0570d {
    public static byte a(ByteBuffer byteBuffer) {
        return byteBuffer.get();
    }

    public static int b(ByteBuffer byteBuffer) {
        return byteBuffer.getShort() & 65535;
    }

    public static short c(ByteBuffer byteBuffer) {
        return byteBuffer.getShort();
    }

    public static long d(ByteBuffer byteBuffer) {
        return byteBuffer.getInt() & 4294967295L;
    }

    public static long e(ByteBuffer byteBuffer) {
        byteBuffer.get(new byte[8]);
        return ((((((r0[0] & 255) << 0) | ((r0[1] & 255) << 8)) | ((r0[2] & 255) << 16)) | ((r0[3] & 255) << 24)) << 0) | ((((((r0[4] & 255) << 0) | ((r0[5] & 255) << 8)) | ((r0[6] & 255) << 16)) | ((r0[7] & 255) << 24)) << 32);
    }

    public static double f(ByteBuffer byteBuffer) {
        return byteBuffer.getDouble();
    }

    public static long g(ByteBuffer byteBuffer) {
        byteBuffer.get(new byte[8]);
        return ((((((r0[0] & 255) << 0) | ((r0[1] & 255) << 8)) | ((r0[2] & 255) << 16)) | ((r0[3] & 255) << 24)) << 0) | ((((((r0[4] & 255) << 0) | ((r0[5] & 255) << 8)) | ((r0[6] & 255) << 16)) | ((r0[7] & 255) << 24)) << 32);
    }

    public static String a(ByteBuffer byteBuffer, int i2) {
        byte[] bArr = new byte[i2];
        byteBuffer.get(bArr);
        int i3 = 0;
        for (int i4 = 0; i4 < bArr.length && bArr[i4] != 0; i4++) {
            i3++;
        }
        return new String(bArr, 0, i3, FTP.DEFAULT_CONTROL_ENCODING);
    }

    public static String b(ByteBuffer byteBuffer, int i2) {
        byte[] bArr = new byte[i2];
        byteBuffer.get(bArr);
        int i3 = 0;
        for (int i4 = 0; i4 < bArr.length && bArr[i4] != 0; i4++) {
            i3++;
        }
        return new String(bArr, 0, i3, "UTF-8");
    }

    public static List a(C0575c c0575c) {
        ArrayList arrayList = new ArrayList();
        C0576d c0576dG = c0575c.g();
        if (c0576dG != null) {
            arrayList.add(c0576dG);
            while (true) {
                C0576d c0576dN = c0576dG.n();
                c0576dG = c0576dN;
                if (c0576dN == null) {
                    break;
                }
                arrayList.add(c0576dG);
            }
        }
        return arrayList;
    }

    public static List a(h hVar) {
        ArrayList arrayList = new ArrayList();
        C0577e c0577eG = hVar.g();
        if (c0577eG != null) {
            int i2 = 0 + 1;
            c0577eG.a(i2);
            arrayList.add(c0577eG);
            while (true) {
                C0577e c0577eE = c0577eG.e();
                c0577eG = c0577eE;
                if (c0577eE == null) {
                    break;
                }
                i2++;
                c0577eG.a(i2);
                arrayList.add(c0577eG);
            }
        }
        return arrayList;
    }

    public static C0188n a(List list) {
        String strE;
        C0188n c0188n = new C0188n();
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            C0576d c0576d = (C0576d) it.next();
            C0184j c0184j = new C0184j(c0576d.o().e());
            if ((c0576d.q() instanceof k) && (strE = ((k) c0576d.q()).e()) != null && !strE.isEmpty()) {
                c0184j.e(strE);
            }
            c0188n.add(c0184j);
            i2++;
        }
        C.c(GoToActionDialog.EMPTY_DESTINATION + i2 + " Channel Names: " + sb.toString());
        return c0188n;
    }
}
