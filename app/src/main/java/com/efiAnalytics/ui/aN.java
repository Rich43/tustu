package com.efiAnalytics.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.icepdf.ri.common.views.DocumentViewComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aN.class */
public class aN {

    /* renamed from: a, reason: collision with root package name */
    static dK f10753a = null;

    public static void a(String str) {
        if (str.startsWith("file:") && str.toLowerCase().contains(DocumentViewComponent.PDF_EXTENSION)) {
            try {
                a(new URL(bH.W.b(str, " ", "%20")));
                return;
            } catch (MalformedURLException e2) {
            }
        }
        String property = System.getProperty("os.name");
        boolean z2 = false;
        if (0 != 0) {
            URI uri = null;
            try {
                uri = new URL(bH.W.b(str, " ", "%20")).toURI();
            } catch (MalformedURLException e3) {
                Logger.getLogger(aN.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                bH.C.d("Invalid URL, will attempt direct opening: " + str);
                z2 = false;
            } catch (URISyntaxException e4) {
                Logger.getLogger(aN.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                bH.C.d("Invalid URL, will attempt direct opening: " + str);
                z2 = false;
            }
            Desktop desktop = Desktop.getDesktop();
            if (str.startsWith("http:")) {
                try {
                    desktop.browse(uri);
                } catch (IOException e5) {
                    Logger.getLogger(aN.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                    JOptionPane.showMessageDialog(null, "Failed to launch App for URL: " + str + "\nError: " + e5.getLocalizedMessage());
                }
            } else {
                try {
                    desktop.open(new File(str.substring(7)));
                } catch (IOException e6) {
                    Logger.getLogger(aN.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                    JOptionPane.showMessageDialog(null, "Failed to launch App for URL: " + str + "\nError: " + e6.getLocalizedMessage());
                }
            }
        }
        if (z2) {
            return;
        }
        try {
            bH.C.d("Opening URL: " + str);
            if (property.startsWith("Mac OS")) {
                Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", String.class).invoke(null, str);
            } else if (property.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + str);
            } else {
                String[] strArr = {"chrome", "firefox", "chromium-browser", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
                String str2 = null;
                for (int i2 = 0; i2 < strArr.length && str2 == null; i2++) {
                    if (Runtime.getRuntime().exec(new String[]{"which", strArr[i2]}).waitFor() == 0) {
                        str2 = strArr[i2];
                    }
                }
                if (str2 == null) {
                    throw new Exception("Could not find web browser");
                }
                Runtime.getRuntime().exec(new String[]{str2, str});
            }
        } catch (Exception e7) {
            JOptionPane.showMessageDialog(null, "Failed to launch App for URL: :\n" + e7.getLocalizedMessage());
        }
    }

    public static void a(URL url) {
        if (f10753a == null) {
            f10753a = new dK(bV.c());
        }
        f10753a.a(url);
        f10753a.a(true);
    }
}
