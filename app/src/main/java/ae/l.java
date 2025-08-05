package ae;

import W.D;
import bH.ad;
import java.io.File;
import java.io.IOException;
import javax.swing.plaf.basic.BasicRootPaneUI;

/* loaded from: TunerStudioMS.jar:ae/l.class */
public class l {
    public static k a(File file) throws x {
        String lowerCase = file.getName().toLowerCase();
        if (lowerCase.endsWith("zip")) {
            return b(file);
        }
        if (!lowerCase.endsWith("s19") && !lowerCase.endsWith("mot") && !lowerCase.endsWith("bsf")) {
            throw new x("Unknown file type.");
        }
        k kVar = new k();
        a(file.getParentFile(), kVar);
        return kVar;
    }

    private static k a(File file, k kVar) {
        for (File file2 : file.listFiles()) {
            if (file2.isFile() && (file2.getName().toLowerCase().endsWith("ini") || file2.getName().toLowerCase().endsWith("ecu"))) {
                kVar.a(file2);
            } else if (file2.isFile() && (file2.getName().toLowerCase().endsWith("s19") || file2.getName().toLowerCase().endsWith("mot") || file2.getName().toLowerCase().endsWith("bsf"))) {
                kVar.b(file2);
            } else if (file2.isFile() && file2.getName().toLowerCase().startsWith("license")) {
                kVar.e(file2);
            } else if (file2.isFile() && file2.getName().toLowerCase().startsWith("readme")) {
                kVar.d(file2);
            } else if (file2.isFile() && file2.getName().toLowerCase().startsWith(BasicRootPaneUI.Actions.RELEASE) && file2.getName().toLowerCase().contains("notes")) {
                kVar.g(file2);
            }
        }
        return kVar;
    }

    private static k b(File file) throws IOException {
        File fileCreateTempFile = File.createTempFile("tmp", Long.toString(System.nanoTime()));
        File file2 = new File(fileCreateTempFile.getParentFile(), "firmware" + Long.toString(System.currentTimeMillis()));
        file2.mkdir();
        fileCreateTempFile.delete();
        ad.a(file, file2, (String) null);
        D.a().a(file2);
        while (file2.list().length == 1 && file2.listFiles()[0].isDirectory()) {
            file2 = file2.listFiles()[0];
        }
        k kVar = new k();
        a(file2, kVar);
        return kVar;
    }
}
