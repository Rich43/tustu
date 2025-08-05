package org.icepdf.ri.viewer;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.SwingUtilities;
import org.icepdf.ri.images.Images;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/viewer/Main.class */
public class Main {
    private static SplashWindow splashWindow = null;

    public static void main(final String[] args) throws HeadlessException {
        Image splashImage;
        URL imageURL = Images.get("icepdf-splash-2013.png");
        if (imageURL != null && (splashImage = Toolkit.getDefaultToolkit().getImage(imageURL)) != null) {
            splashWindow = new SplashWindow(splashImage);
            splashWindow.splash();
        }
        SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.viewer.Main.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Class.forName("org.icepdf.ri.viewer.Launcher").getMethod("main", String[].class).invoke(null, args);
                } catch (Throwable e2) {
                    e2.printStackTrace();
                    System.err.flush();
                    System.exit(10);
                }
            }
        });
        if (splashWindow != null) {
            splashWindow.dispose();
        }
    }
}
