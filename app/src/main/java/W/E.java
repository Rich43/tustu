package W;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

/* loaded from: TunerStudioMS.jar:W/E.class */
public class E {
    public File a(String str, int i2, int i3) {
        F f2 = new F(this);
        f2.b("THROTTLEFACTOR:");
        f2.a("EFI Analytics generated Linear Throttle Calibration");
        f2.a("Created on " + new Date().toString());
        f2.a("");
        f2.a("\tLow ADC = " + i2 + "  \tHigh ADC = " + i3);
        f2.a("");
        G[] gArr = new G[257];
        gArr[0] = new G(this);
        gArr[0].a("ADC");
        for (int i4 = 1; i4 < gArr.length; i4++) {
            gArr[i4] = new G(this, "DB", "" + Math.round(100.0f * ((r0 - i2) / (i3 - i2))), "" + (i4 - 1));
        }
        return a(new File(str, "throttlefactor.inc"), f2, gArr);
    }

    public File a(File file, F f2, G[] gArr) throws V.a {
        try {
            if (file.exists() && !file.delete()) {
                throw new V.a("Unable to delete existing inc file! Check permissions.\n" + file.getAbsolutePath());
            }
            file.createNewFile();
            PrintStream printStream = new PrintStream(new FileOutputStream(file));
            printStream.print(f2.a());
            printStream.println(f2.b());
            for (int i2 = 0; i2 < gArr.length; i2++) {
                if (gArr[i2].b() == null || gArr[i2].b().equals("")) {
                    printStream.println("\t" + gArr[i2].a() + "\t" + bH.W.a(gArr[i2].b(), ' ', 3) + "\t; " + bH.W.a(gArr[i2].c(), ' ', 3));
                } else {
                    printStream.println("\t" + gArr[i2].a() + "\t" + bH.W.a(gArr[i2].b(), ' ', 3) + "T\t; " + bH.W.a(gArr[i2].c(), ' ', 3));
                }
            }
            bH.E.c();
            return file;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Error writing " + ((Object) file.getAbsoluteFile()) + "\n" + e2.getMessage());
        }
    }
}
