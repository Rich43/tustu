package ao;

import W.C0171aa;
import com.efiAnalytics.ui.C1677fh;
import h.C1737b;
import r.C1798a;

/* renamed from: ao.I, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/I.class */
public class C0591I {
    public void a(String[] strArr, String str) {
        new C0592J(this).start();
        if ((strArr.length > 0 && strArr[0].equals("hog")) || str.equals("HogLogViewer.jar")) {
            if (strArr.length > 0 && strArr[0].equals("hog")) {
                String[] strArr2 = new String[0];
            }
            h.i.f12255b = C1737b.f12230j;
            if (h.i.f12256c.contains(C1737b.f12224d)) {
                h.i.f12256c = C1737b.f12229i + C1737b.f12222b + C1737b.f12222b + C1737b.f12224d;
            } else {
                h.i.f12256c = C1737b.f12229i + C1737b.f12222b;
            }
            h.i.f12280A = "./resources/HogLogLogo.png";
            h.i.f12263j = "HogLogViewer.properties";
            h.i.f12260g = ".hlvreg";
            h.i.f12262i = "hlv.properties";
            C1798a.f13335aq = "hlv.properties";
            h.i.f12267n = "https://www.efianalytics.com/MegaLogViewerHD/register.html";
            h.i.f12266m = "https://www.efianalytics.com/MegaLogViewerHD/";
            h.i.f12265l = "HogLogViewer.jar";
            h.i.f12264k = "MegaLogViewerHD.exe";
            return;
        }
        if ((strArr.length > 0 && strArr[0].equals("dyno")) || str.equals("DynoSpectrum.jar")) {
            if (strArr.length > 0 && strArr[0].equals("dyno")) {
                String[] strArr3 = new String[0];
            }
            h.i.f12255b = C1737b.f12232l;
            if (h.i.f12256c.contains(C1737b.f12224d)) {
                h.i.f12256c = C1737b.f12228h + C1737b.f12222b + C1737b.f12224d;
            } else {
                h.i.f12256c = C1737b.f12228h + C1737b.f12222b;
            }
            h.i.f12280A = "./resources/DynoSpectrum.jpg";
            h.i.f12281B = "resources/DynoSpectrumIcon.png";
            h.i.f12263j = "DynoSpectrum.properties";
            h.i.f12260g = "dynoSpec.reg";
            h.i.f12262i = "dsUser.properties";
            C1798a.f13335aq = "dsUser.properties";
            h.i.f12267n = "https://www.efianalytics.com/DynoSpectrum/register.html";
            h.i.f12266m = "https://www.efianalytics.com/DynoSpectrum/";
            h.i.f12279z = true;
            h.i.f12265l = "DynoSpectrum.jar";
            h.i.f12264k = "DynoSpectrum.exe";
            return;
        }
        if ((strArr.length <= 0 || !strArr[0].equals("big")) && !str.equals("BigStuffLog.jar")) {
            return;
        }
        if (strArr.length > 0 && strArr[0].equals("big")) {
            String[] strArr4 = new String[0];
        }
        h.i.f12255b = C1737b.f12231k;
        if (h.i.f12256c.contains(C1737b.f12224d)) {
            h.i.f12256c = C1737b.f12225e + C1737b.f12222b + C1737b.f12224d;
        } else {
            h.i.f12256c = C1737b.f12225e + C1737b.f12222b;
        }
        h.i.f12280A = "./resources/BigLogLogo.png";
        h.i.f12263j = "BigStuff3.properties";
        h.i.f12260g = "mlvbig.reg";
        h.i.f12262i = "mlvBig.properties";
        C1798a.f13335aq = "mlvBig.properties";
        h.i.f12267n = "https://www.efianalytics.com/BigStuffLog/register.html";
        h.i.f12266m = "http://www.efianalytics.com/BigStuffLog/";
        h.i.f12299T = "withLabels";
        h.g.f12240a = "FieldMaps/BigStuffReplay.properties";
        h.i.f12276w = "big";
        C0171aa.f2043c = "bsq";
        h.i.f12264k = "BigStuffLog.exe";
        h.i.f12265l = "BigStuffLog.jar";
        h.i.f12301V = true;
        h.i.f12277x = false;
        hn.f6131t = "Auto Tune";
        aO.k.f2695A = true;
        C1677fh.a(true);
    }
}
