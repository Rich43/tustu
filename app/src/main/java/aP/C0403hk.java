package aP;

import com.efiAnalytics.ui.InterfaceC1662et;
import i.C1748h;
import java.awt.Frame;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import r.C1807j;

/* renamed from: aP.hk, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hk.class */
public class C0403hk {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1662et f3583a;

    /* renamed from: c, reason: collision with root package name */
    private static String f3584c = "megaLogViewerLocation";

    /* renamed from: b, reason: collision with root package name */
    public static String[] f3585b = {"C:\\Program Files\\EFIAnalytics\\MegaLogViewerHD\\MegaLogViewer.exe", "C:\\Program Files (x86)\\EFIAnalytics\\MegaLogViewerHD\\MegaLogViewer.exe", "C:\\Program Files(x86)\\EFIAnalytics\\MegaLogViewerHD\\MegaLogViewer.exe", "C:\\Program Files\\EFIAnalytics\\MegaLogViewerHD\\MegaLogViewerHD.exe", "C:\\Program Files (x86)\\EFIAnalytics\\MegaLogViewerHD\\MegaLogViewerHD.exe", "C:\\Program Files(x86)\\EFIAnalytics\\MegaLogViewerHD\\MegaLogViewerHD.exe", "c:\\Program Files(x86)\\EFIAnalytics\\MegaLogViewer\\MegaLogViewer(x64).exe", "c:\\Program Files(x86)\\EFIAnalytics\\MegaLogViewer\\MegaLogViewer.exe", "c:\\Program Files (x86)\\EFIAnalytics\\MegaLogViewer\\MegaLogViewer(x64).exe", "c:\\Program Files\\EFIAnalytics\\MegaLogViewer\\MegaLogViewer(x64).exe", "c:\\Program Files (x86)\\EFIAnalytics\\MegaLogViewer\\MegaLogViewer.exe", "C:\\Program Files (x86)\\EFIAnalytics\\BigStuffLog\\BigStuffLog.exe", "c:\\Program Files\\EFIAnalytics\\MegaLogViewer\\MegaLogViewer.exe", "c:\\Program Files\\MegaSquirt\\MegaLogViewer\\MegaLogViewer.exe", "/Applications/MegaLogViewerHD.app/Contents/Java/MegaLogViewer.sh", "/Applications/MegaLogViewerMS.app/Contents/Java/MegaLogViewer.sh"};

    public C0403hk(InterfaceC1662et interfaceC1662et) {
        this.f3583a = null;
        this.f3583a = interfaceC1662et;
    }

    public void a(String str) {
        boolean z2;
        if (C1798a.a().c(C1798a.f13416bT, C1798a.f13417bU)) {
            z2 = true;
        } else {
            z2 = !C1748h.a().a("openLog", str);
        }
        if (z2) {
            b(str);
        }
    }

    public void b(String str) {
        String strB = b();
        if (strB == null || strB.equals("") || !new File(strB).exists()) {
            strB = a();
        }
        if (strB == null || strB.equals("")) {
            bH.C.d("No Path to MLV, not starting.");
            return;
        }
        File file = new File(strB.substring(0, strB.lastIndexOf(File.separator)));
        File file2 = new File(C1807j.A(), "mlvLaunch.properties");
        boolean z2 = false;
        if (!file2.exists() || file2.delete()) {
            Properties properties = new Properties();
            properties.setProperty("fileName", str);
            if (ac.h.k() != null && ac.h.k().u() && ac.h.k().n().equals(str)) {
                properties.setProperty("trailFile", Boolean.toString(true));
                properties.setProperty("startPlayback", Boolean.toString(true));
            }
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                properties.store(bufferedOutputStream, "Launched by: " + C1798a.f13268b);
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                bH.C.d("launching MegaLogViewer from properties " + file2.getAbsolutePath());
                Runtime.getRuntime().exec(new String[]{strB, file2.getAbsolutePath()}, (String[]) null, file);
                z2 = true;
            } catch (IOException e2) {
                Logger.getLogger(C0403hk.class.getName()).log(Level.SEVERE, "Failed to store MLV launch file: " + file2.getAbsolutePath(), (Throwable) e2);
            }
        } else {
            bH.C.a("Failed to delete " + file2.getAbsolutePath());
        }
        if (z2) {
            return;
        }
        try {
            bH.C.d("launching MegaLogViewer at " + strB);
            Runtime.getRuntime().exec(new String[]{strB, str}, (String[]) null, file);
        } catch (IOException e3) {
            bH.C.a("Failed to launch MegaLogViewer at " + strB, e3, null);
        }
    }

    private String a() {
        if (com.efiAnalytics.ui.bV.d()) {
            for (int i2 = 0; i2 < f3585b.length; i2++) {
                File file = new File(f3585b[i2]);
                if (file.exists()) {
                    c(file.getAbsolutePath());
                    return file.getAbsolutePath();
                }
            }
        }
        JFrame jFrameC = cZ.a().c();
        int iA = aY.o.a(jFrameC);
        if (iA == 1) {
            return a(jFrameC);
        }
        if (iA != 4) {
            return null;
        }
        com.efiAnalytics.ui.aN.a(C1798a.f13296D);
        return null;
    }

    private String a(Frame frame) {
        String[] strArr = {"exe"};
        String str = "MegaLogViewer.exe";
        if (!com.efiAnalytics.ui.bV.d()) {
            strArr[0] = PdfOps.sh_TOKEN;
            str = "MegaLogViewer.sh";
        }
        String strB = com.efiAnalytics.ui.bV.b(frame, "Locate MegaLogViewer", strArr, str, "");
        if (strB != null && !strB.equals("")) {
            c(strB);
        }
        return strB;
    }

    private String b() {
        if (this.f3583a == null) {
            return null;
        }
        return this.f3583a.a(f3584c);
    }

    public void c(String str) {
        if (this.f3583a != null) {
            this.f3583a.a(f3584c, str);
        }
    }
}
