package W;

import G.aM;
import bH.C0995c;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:W/av.class */
public class av extends aw {
    public void a(G.R r2, String str, List list) throws V.a {
        if (list == null) {
            try {
                list = new ArrayList();
                for (String str2 : r2.k()) {
                    list.add(str2);
                }
            } catch (V.g e2) {
                Logger.getLogger(av.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                throw new V.a("Failed to set the data from:\n" + str);
            } catch (FileNotFoundException e3) {
                Logger.getLogger(av.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                throw new V.a("Can not access the file:\n" + str);
            } catch (IOException e4) {
                Logger.getLogger(av.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                throw new V.a("An error occurred while reading tune file:\n" + str);
            } catch (ArrayIndexOutOfBoundsException e5) {
                Logger.getLogger(av.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                throw new V.a("Tune file size doesn't match current configuration.\nFile contained 0 bytes, expected " + r2.O().f(0));
            }
        }
        int[][] iArrA = a(r2, a(str));
        for (String str3 : list) {
            aM aMVarC = r2.c(str3);
            if (aMVarC == null || aMVarC.d() < 0 || a(r2, aMVarC)) {
                if (aMVarC.d() >= 0) {
                    bH.C.d("Skip load of " + str3);
                }
            } else if (aMVarC.d() >= 0) {
                int[] iArr = new int[aMVarC.y()];
                System.arraycopy(iArrA[aMVarC.d()], aMVarC.g(), iArr, 0, iArr.length);
                r2.h().a(aMVarC.d(), aMVarC.g(), iArr);
            } else if (aMVarC.d() != -1) {
                bH.C.b(str3 + " not found in current firmware.");
            }
        }
    }

    private boolean a(G.R r2, aM aMVar) {
        int iY = r2.O().y(aMVar.d()) + aMVar.g();
        int iY2 = (iY + aMVar.y()) - 1;
        if (aMVar.M() || aMVar.G() || !aMVar.B()) {
            return true;
        }
        for (int i2 = 0; i2 < r2.O().g(); i2++) {
            Iterator itA = r2.a(i2);
            while (itA.hasNext()) {
                aM aMVar2 = (aM) itA.next();
                int iY3 = r2.O().y(aMVar2.d()) + aMVar2.g();
                int iY4 = (iY3 + aMVar2.y()) - 1;
                if (!aMVar2.O() && iY4 >= iY && iY3 <= iY2 && (aMVar2.M() || aMVar2.G() || !aMVar2.B())) {
                    if (aMVar.aJ().equals("nosAxisConfig1")) {
                    }
                    return true;
                }
            }
        }
        return false;
    }

    protected int[] a(G.R r2) {
        ArrayList arrayList = new ArrayList();
        int i2 = 30720 + 2048;
        G.Y yH = r2.h();
        for (int i3 = 0; i3 < yH.e(); i3++) {
            int iY = r2.O().y(i3);
            if (iY >= 30720 && iY + yH.c(i3) <= i2) {
                arrayList.add(Integer.valueOf(i3));
            }
        }
        ArrayList arrayListA = a(r2.O(), arrayList);
        int[] iArr = new int[arrayListA.size()];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr[i4] = ((Integer) arrayListA.get(i4)).intValue();
        }
        return iArr;
    }

    public ArrayList a(G.F f2, ArrayList arrayList) {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            for (int i3 = i2 + 1; i3 < arrayList.size(); i3++) {
                Integer num = (Integer) arrayList.get(i2);
                Integer num2 = (Integer) arrayList.get(i3);
                if (f2.y(num.intValue()) > f2.y(num2.intValue())) {
                    arrayList.set(i2, num2);
                    arrayList.set(i3, num);
                }
            }
        }
        return arrayList;
    }

    @Override // W.aw
    public void a(String str, G.R r2) {
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        int[] iArrA = a(r2);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        for (int i2 : iArrA) {
            bufferedOutputStream.write(C0995c.a(r2.h().b(i2)));
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }
}
