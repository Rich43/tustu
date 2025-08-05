package org.icepdf.ri.viewer;

import java.awt.HeadlessException;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.icepdf.core.util.Defs;
import org.icepdf.ri.common.ViewModel;
import org.icepdf.ri.util.FontPropertiesManager;
import org.icepdf.ri.util.PropertiesManager;
import org.icepdf.ri.util.URLAccess;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/viewer/Launcher.class */
public class Launcher {
    private static final Logger logger = Logger.getLogger(Launcher.class.toString());
    public static WindowManager windowManager;
    private static PropertiesManager propertiesManager;

    public static void main(String[] argv) throws HeadlessException, IllegalArgumentException {
        int i2;
        boolean brokenUsage = false;
        String contentURL = "";
        String contentFile = "";
        String contentProperties = null;
        int i3 = 0;
        while (true) {
            if (i3 >= argv.length) {
                break;
            }
            if (i3 == argv.length - 1) {
                brokenUsage = true;
                break;
            }
            String arg = argv[i3];
            if (arg.equals("-loadfile")) {
                i2 = i3 + 1;
                contentFile = argv[i2].trim();
            } else if (arg.equals("-loadurl")) {
                i2 = i3 + 1;
                contentURL = argv[i2].trim();
            } else if (arg.equals("-loadproperties")) {
                i2 = i3 + 1;
                contentProperties = argv[i2].trim();
            } else {
                brokenUsage = true;
                break;
            }
            i3 = i2 + 1;
        }
        ResourceBundle messageBundle = ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE);
        if (brokenUsage) {
            System.out.println(messageBundle.getString("viewer.commandLin.error"));
            System.exit(1);
        }
        run(contentFile, contentURL, contentProperties, messageBundle);
    }

    private static void run(String contentFile, String contentURL, String contentProperties, ResourceBundle messageBundle) throws HeadlessException, IllegalArgumentException {
        Properties sysProps = System.getProperties();
        propertiesManager = new PropertiesManager(sysProps, contentProperties, messageBundle);
        new FontPropertiesManager(propertiesManager, sysProps, messageBundle);
        System.setProperties(sysProps);
        setupLookAndFeel(messageBundle);
        ViewModel.setDefaultFilePath(propertiesManager.getDefaultFilePath());
        ViewModel.setDefaultURL(propertiesManager.getDefaultURL());
        windowManager = WindowManager.createInstance(propertiesManager, messageBundle);
        if (contentFile != null && contentFile.length() > 0) {
            windowManager.newWindow(contentFile);
            ViewModel.setDefaultFilePath(contentFile);
        }
        if (contentURL != null && contentURL.length() > 0) {
            URLAccess urlAccess = URLAccess.doURLAccess(contentURL);
            urlAccess.closeConnection();
            if (urlAccess.errorMessage != null) {
                Object[] messageArguments = {urlAccess.errorMessage, urlAccess.urlLocation};
                MessageFormat formatter = new MessageFormat(messageBundle.getString("viewer.launcher.URLError.dialog.message"));
                JOptionPane.showMessageDialog(null, formatter.format(messageArguments), messageBundle.getString("viewer.launcher.URLError.dialog.title"), 1);
            } else {
                windowManager.newWindow(urlAccess.url);
            }
            ViewModel.setDefaultURL(urlAccess.urlLocation);
            urlAccess.dispose();
        }
        if (((contentFile == null || contentFile.length() == 0) && (contentURL == null || contentURL.length() == 0)) || windowManager.getNumberOfWindows() == 0) {
            windowManager.newWindow("");
        }
    }

    private static void setupLookAndFeel(ResourceBundle messageBundle) throws HeadlessException {
        if (Defs.sysProperty("mrj.version") != null) {
            Defs.setSystemProperty("apple.laf.useScreenMenuBar", "true");
            String appName = messageBundle.getString("viewer.window.title.default");
            Defs.setSystemProperty("com.apple.mrj.application.apple.menu.about.name", appName);
        }
        String className = propertiesManager.getLookAndFeel("application.lookandfeel", null);
        if (className != null) {
            try {
                UIManager.setLookAndFeel(className);
                return;
            } catch (Exception e2) {
                Object[] messageArguments = {propertiesManager.getString("application.lookandfeel")};
                MessageFormat formatter = new MessageFormat(messageBundle.getString("viewer.launcher.URLError.dialog.message"));
                JOptionPane.showMessageDialog(null, formatter.format(messageArguments), messageBundle.getString("viewer.launcher.lookAndFeel.error.message"), 0);
            }
        }
        try {
            String defaultLF = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(defaultLF);
        } catch (Exception e3) {
            logger.log(Level.FINE, "Error setting Swing Look and Feel.", (Throwable) e3);
        }
    }
}
