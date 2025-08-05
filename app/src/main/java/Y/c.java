package Y;

import bH.C;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:Y/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    String f2213a = "\\s+";

    /* renamed from: b, reason: collision with root package name */
    int f2214b = 0;

    /* renamed from: c, reason: collision with root package name */
    int f2215c = 4;

    /* renamed from: d, reason: collision with root package name */
    int f2216d = 5;

    public List a(File file) {
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        try {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                String[] strArrSplit = line.split("\\s+");
                if (a(strArrSplit)) {
                    int i2 = Integer.parseInt(strArrSplit[this.f2215c], 16);
                    int i3 = Integer.parseInt(strArrSplit[this.f2214b], 16);
                    d dVar = new d();
                    dVar.a(strArrSplit[this.f2216d]);
                    dVar.a(i3);
                    dVar.b(i2);
                    arrayList.add(dVar);
                }
            }
            return arrayList;
        } finally {
            try {
                bufferedReader.close();
            } catch (Exception e2) {
                C.c("failed to close reader");
            }
        }
    }

    private boolean a(String[] strArr) {
        return strArr.length - 1 >= this.f2216d && a(strArr[this.f2214b]) && a(strArr[this.f2215c]) && !strArr[this.f2216d].startsWith("_") && !strArr[this.f2216d].contains(".");
    }

    private boolean a(String str) {
        try {
            Integer.parseInt(str, 16);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }
}
