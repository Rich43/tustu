package java.awt;

import java.awt.peer.DesktopPeer;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import sun.awt.AppContext;
import sun.awt.DesktopBrowse;
import sun.awt.SunToolkit;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/awt/Desktop.class */
public class Desktop {
    private DesktopPeer peer = Toolkit.getDefaultToolkit().createDesktopPeer(this);

    /* loaded from: rt.jar:java/awt/Desktop$Action.class */
    public enum Action {
        OPEN,
        EDIT,
        PRINT,
        MAIL,
        BROWSE
    }

    private Desktop() {
    }

    public static synchronized Desktop getDesktop() {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        if (!isDesktopSupported()) {
            throw new UnsupportedOperationException("Desktop API is not supported on the current platform");
        }
        AppContext appContext = AppContext.getAppContext();
        Desktop desktop = (Desktop) appContext.get(Desktop.class);
        if (desktop == null) {
            desktop = new Desktop();
            appContext.put(Desktop.class, desktop);
        }
        return desktop;
    }

    public static boolean isDesktopSupported() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof SunToolkit) {
            return ((SunToolkit) defaultToolkit).isDesktopSupported();
        }
        return false;
    }

    public boolean isSupported(Action action) {
        return this.peer.isSupported(action);
    }

    private static void checkFileValidation(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("The file: " + file.getPath() + " doesn't exist.");
        }
    }

    private void checkActionSupport(Action action) {
        if (!isSupported(action)) {
            throw new UnsupportedOperationException("The " + action.name() + " action is not supported on the current platform!");
        }
    }

    private void checkAWTPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AWTPermission("showWindowWithoutWarningBanner"));
        }
    }

    public void open(File file) throws IOException {
        File file2 = new File(file.getPath());
        checkAWTPermission();
        checkExec();
        checkActionSupport(Action.OPEN);
        checkFileValidation(file2);
        this.peer.open(file2);
    }

    public void edit(File file) throws SecurityException, IOException {
        File file2 = new File(file.getPath());
        checkAWTPermission();
        checkExec();
        checkActionSupport(Action.EDIT);
        file2.canWrite();
        checkFileValidation(file2);
        this.peer.edit(file2);
    }

    public void print(File file) throws SecurityException, IOException {
        File file2 = new File(file.getPath());
        checkExec();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPrintJobAccess();
        }
        checkActionSupport(Action.PRINT);
        checkFileValidation(file2);
        this.peer.print(file2);
    }

    public void browse(URI uri) throws IOException {
        SecurityException securityException = null;
        try {
            checkAWTPermission();
            checkExec();
        } catch (SecurityException e2) {
            securityException = e2;
        }
        checkActionSupport(Action.BROWSE);
        if (uri == null) {
            throw new NullPointerException();
        }
        if (securityException == null) {
            this.peer.browse(uri);
            return;
        }
        try {
            URL url = uri.toURL();
            DesktopBrowse desktopBrowse = DesktopBrowse.getInstance();
            if (desktopBrowse == null) {
                throw securityException;
            }
            desktopBrowse.browse(url);
        } catch (MalformedURLException e3) {
            throw new IllegalArgumentException("Unable to convert URI to URL", e3);
        }
    }

    public void mail() throws SecurityException, IOException {
        checkAWTPermission();
        checkExec();
        checkActionSupport(Action.MAIL);
        try {
            this.peer.mail(new URI("mailto:?"));
        } catch (URISyntaxException e2) {
        }
    }

    public void mail(URI uri) throws SecurityException, IOException {
        checkAWTPermission();
        checkExec();
        checkActionSupport(Action.MAIL);
        if (uri == null) {
            throw new NullPointerException();
        }
        if (!"mailto".equalsIgnoreCase(uri.getScheme())) {
            throw new IllegalArgumentException("URI scheme is not \"mailto\"");
        }
        this.peer.mail(uri);
    }

    private void checkExec() throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new FilePermission("<<ALL FILES>>", SecurityConstants.FILE_EXECUTE_ACTION));
        }
    }
}
