package W;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:W/aw.class */
public class aw {
    protected int[] a(String str) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(str)));
        ArrayList arrayList = new ArrayList();
        int i2 = bufferedInputStream.read();
        while (true) {
            int i3 = i2;
            if (i3 == -1) {
                break;
            }
            arrayList.add(Integer.valueOf(i3));
            i2 = bufferedInputStream.read();
        }
        int[] iArr = new int[arrayList.size()];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr[i4] = ((Integer) arrayList.get(i4)).intValue();
        }
        return iArr;
    }

    public void a(String str, G.R r2) throws IOException {
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        byte[] bArrB = b(r2);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        bufferedOutputStream.write(bArrB);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    protected byte[] b(G.R r2) {
        G.Y yH = r2.h();
        byte[] bArr = new byte[yH.k()];
        int i2 = 0;
        for (int i3 = 0; i3 < yH.e(); i3++) {
            for (int i4 : yH.b(i3)) {
                int i5 = i2;
                i2++;
                bArr[i5] = (byte) i4;
            }
        }
        return bArr;
    }

    protected int[][] a(G.R r2, int[] iArr) {
        int[][] iArr2 = new int[r2.h().e()][0];
        for (int i2 = 0; i2 < iArr2.length; i2++) {
            iArr2[i2] = new int[r2.h().c(i2)];
            int iY = r2.O().y(i2) - 30720;
            if (iY + iArr2[i2].length < iArr.length) {
                System.arraycopy(iArr, iY, iArr2[i2], 0, iArr2[i2].length);
            }
        }
        return iArr2;
    }
}
