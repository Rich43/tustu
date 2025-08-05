package org.icepdf.ri.util;

import com.intel.bluetooth.BlueCoveImpl;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/BareBonesBrowserLaunch.class */
public class BareBonesBrowserLaunch {
    private static final String errMsg = "Error attempting to launch web browser";
    public static final String FILE_PREFIX = "file://";
    private static final Logger logger = Logger.getLogger(BareBonesBrowserLaunch.class.toString());
    private static String os = System.getProperty("os.name").toLowerCase();

    public static void openURL(String url) throws Exception {
        try {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Opening URL: " + url);
            }
            if (isMac()) {
                Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);
                openURL.invoke(null, url);
            } else if (isWindows()) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (isUnix()) {
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                }
                Runtime.getRuntime().exec(new String[]{browser, url});
            } else {
                JOptionPane.showMessageDialog(null, errMsg);
            }
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, "Error attempting to launch web browser:\n" + e2.getLocalizedMessage());
        }
    }

    public static void openFile(String filePath) throws Exception {
        openURL(FILE_PREFIX + filePath);
    }

    public static boolean isWindows() {
        return os.contains("win");
    }

    public static boolean isMac() {
        return os.contains(BlueCoveImpl.STACK_OSX);
    }

    public static boolean isUnix() {
        return os.contains("nix") || os.contains("nux");
    }
}
