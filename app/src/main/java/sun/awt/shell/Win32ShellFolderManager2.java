package sun.awt.shell;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javafx.fxml.FXMLLoader;
import sun.awt.OSInfo;
import sun.awt.shell.ShellFolder;
import sun.awt.shell.Win32ShellFolder2;
import sun.awt.windows.WToolkit;
import sun.misc.ThreadGroupUtils;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/shell/Win32ShellFolderManager2.class */
public class Win32ShellFolderManager2 extends ShellFolderManager {
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.shell.Win32ShellFolderManager2");
    private static final int VIEW_LIST = 2;
    private static final int VIEW_DETAILS = 3;
    private static final int VIEW_PARENTFOLDER = 8;
    private static final int VIEW_NEWFOLDER = 11;
    private static final Image[] STANDARD_VIEW_BUTTONS;
    private static Win32ShellFolder2 desktop;
    private static Win32ShellFolder2 drives;
    private static Win32ShellFolder2 recent;
    private static Win32ShellFolder2 network;
    private static Win32ShellFolder2 personal;
    private static File[] roots;
    private static List topFolderList;

    static native void initializeCom();

    static native void uninitializeCom();

    static {
        WToolkit.loadLibraries();
        STANDARD_VIEW_BUTTONS = new Image[12];
        topFolderList = null;
    }

    @Override // sun.awt.shell.ShellFolderManager
    public ShellFolder createShellFolder(File file) throws FileNotFoundException {
        try {
            return createShellFolder(getDesktop(), file);
        } catch (InterruptedException e2) {
            throw new FileNotFoundException("Execution was interrupted");
        }
    }

    static Win32ShellFolder2 createShellFolder(Win32ShellFolder2 win32ShellFolder2, File file) throws InterruptedException, FileNotFoundException {
        long displayName;
        try {
            displayName = win32ShellFolder2.parseDisplayName(file.getCanonicalPath());
        } catch (IOException e2) {
            displayName = 0;
        }
        if (displayName == 0) {
            throw new FileNotFoundException("File " + file.getAbsolutePath() + " not found");
        }
        try {
            Win32ShellFolder2 win32ShellFolder2CreateShellFolderFromRelativePIDL = createShellFolderFromRelativePIDL(win32ShellFolder2, displayName);
            Win32ShellFolder2.releasePIDL(displayName);
            return win32ShellFolder2CreateShellFolderFromRelativePIDL;
        } catch (Throwable th) {
            Win32ShellFolder2.releasePIDL(displayName);
            throw th;
        }
    }

    static Win32ShellFolder2 createShellFolderFromRelativePIDL(Win32ShellFolder2 win32ShellFolder2, long j2) throws InterruptedException {
        while (j2 != 0) {
            long jCopyFirstPIDLEntry = Win32ShellFolder2.copyFirstPIDLEntry(j2);
            if (jCopyFirstPIDLEntry == 0) {
                break;
            }
            win32ShellFolder2 = new Win32ShellFolder2(win32ShellFolder2, jCopyFirstPIDLEntry);
            j2 = Win32ShellFolder2.getNextPIDLEntry(j2);
        }
        return win32ShellFolder2;
    }

    private static Image getStandardViewButton(int i2) {
        Image image = STANDARD_VIEW_BUTTONS[i2];
        if (image != null) {
            return image;
        }
        BufferedImage bufferedImage = new BufferedImage(16, 16, 2);
        bufferedImage.setRGB(0, 0, 16, 16, Win32ShellFolder2.getStandardViewButton0(i2), 0, 16);
        STANDARD_VIEW_BUTTONS[i2] = bufferedImage;
        return bufferedImage;
    }

    static Win32ShellFolder2 getDesktop() {
        if (desktop == null) {
            try {
                desktop = new Win32ShellFolder2(0);
            } catch (IOException | InterruptedException e2) {
                if (log.isLoggable(PlatformLogger.Level.WARNING)) {
                    log.warning("Cannot access 'Desktop'", e2);
                }
            } catch (SecurityException e3) {
            }
        }
        return desktop;
    }

    static Win32ShellFolder2 getDrives() {
        if (drives == null) {
            try {
                drives = new Win32ShellFolder2(17);
            } catch (IOException | InterruptedException e2) {
                if (log.isLoggable(PlatformLogger.Level.WARNING)) {
                    log.warning("Cannot access 'Drives'", e2);
                }
            } catch (SecurityException e3) {
            }
        }
        return drives;
    }

    static Win32ShellFolder2 getRecent() {
        if (recent == null) {
            try {
                String fileSystemPath = Win32ShellFolder2.getFileSystemPath(8);
                if (fileSystemPath != null) {
                    recent = createShellFolder(getDesktop(), new File(fileSystemPath));
                }
            } catch (IOException | InterruptedException e2) {
                if (log.isLoggable(PlatformLogger.Level.WARNING)) {
                    log.warning("Cannot access 'Recent'", e2);
                }
            } catch (SecurityException e3) {
            }
        }
        return recent;
    }

    static Win32ShellFolder2 getNetwork() {
        if (network == null) {
            try {
                network = new Win32ShellFolder2(18);
            } catch (IOException | InterruptedException e2) {
                if (log.isLoggable(PlatformLogger.Level.WARNING)) {
                    log.warning("Cannot access 'Network'", e2);
                }
            } catch (SecurityException e3) {
            }
        }
        return network;
    }

    static Win32ShellFolder2 getPersonal() {
        if (personal == null) {
            try {
                String fileSystemPath = Win32ShellFolder2.getFileSystemPath(5);
                if (fileSystemPath != null) {
                    personal = getDesktop().getChildByPath(fileSystemPath);
                    if (personal == null) {
                        personal = createShellFolder(getDesktop(), new File(fileSystemPath));
                    }
                    if (personal != null) {
                        personal.setIsPersonal();
                    }
                }
            } catch (IOException | InterruptedException e2) {
                if (log.isLoggable(PlatformLogger.Level.WARNING)) {
                    log.warning("Cannot access 'Personal'", e2);
                }
            } catch (SecurityException e3) {
            }
        }
        return personal;
    }

    @Override // sun.awt.shell.ShellFolderManager
    public Object get(String str) {
        Win32ShellFolder2.SystemIcon systemIcon;
        int i2;
        Object desktopProperty;
        File[] fileArrCheckFiles;
        if (str.equals("fileChooserDefaultFolder")) {
            Win32ShellFolder2 personal2 = getPersonal();
            if (personal2 == null) {
                personal2 = getDesktop();
            }
            return checkFile(personal2);
        }
        if (str.equals("roots")) {
            if (roots == null) {
                Win32ShellFolder2 desktop2 = getDesktop();
                if (desktop2 != null) {
                    roots = new File[]{desktop2};
                } else {
                    roots = (File[]) super.get(str);
                }
            }
            return checkFiles(roots);
        }
        if (str.equals("fileChooserComboBoxFolders")) {
            Win32ShellFolder2 desktop3 = getDesktop();
            if (desktop3 != null && checkFile(desktop3) != null) {
                ArrayList arrayList = new ArrayList();
                Win32ShellFolder2 drives2 = getDrives();
                Win32ShellFolder2 recent2 = getRecent();
                if (recent2 != null && OSInfo.getWindowsVersion().compareTo(OSInfo.WINDOWS_2000) >= 0) {
                    arrayList.add(recent2);
                }
                arrayList.add(desktop3);
                File[] fileArrCheckFiles2 = checkFiles(desktop3.listFiles());
                Arrays.sort(fileArrCheckFiles2);
                for (File file : fileArrCheckFiles2) {
                    Win32ShellFolder2 win32ShellFolder2 = (Win32ShellFolder2) file;
                    if (!win32ShellFolder2.isFileSystem() || (win32ShellFolder2.isDirectory() && !win32ShellFolder2.isLink())) {
                        arrayList.add(win32ShellFolder2);
                        if (win32ShellFolder2.equals(drives2) && (fileArrCheckFiles = checkFiles(win32ShellFolder2.listFiles())) != null && fileArrCheckFiles.length > 0) {
                            List<? extends File> listAsList = Arrays.asList(fileArrCheckFiles);
                            win32ShellFolder2.sortChildren(listAsList);
                            arrayList.addAll(listAsList);
                        }
                    }
                }
                return checkFiles(arrayList);
            }
            return super.get(str);
        }
        if (str.equals("fileChooserShortcutPanelFolders")) {
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            ArrayList arrayList2 = new ArrayList();
            int i3 = 0;
            do {
                int i4 = i3;
                i3++;
                desktopProperty = defaultToolkit.getDesktopProperty("win.comdlg.placesBarPlace" + i4);
                try {
                    if (desktopProperty instanceof Integer) {
                        arrayList2.add(new Win32ShellFolder2(((Integer) desktopProperty).intValue()));
                    } else if (desktopProperty instanceof String) {
                        arrayList2.add(createShellFolder(new File((String) desktopProperty)));
                    }
                } catch (IOException e2) {
                    if (log.isLoggable(PlatformLogger.Level.WARNING)) {
                        log.warning("Cannot read value = " + desktopProperty, e2);
                    }
                } catch (InterruptedException e3) {
                    if (log.isLoggable(PlatformLogger.Level.WARNING)) {
                        log.warning("Cannot read value = " + desktopProperty, e3);
                    }
                    return new File[0];
                }
            } while (desktopProperty != null);
            if (arrayList2.size() == 0) {
                for (File file2 : new File[]{getRecent(), getDesktop(), getPersonal(), getDrives(), getNetwork()}) {
                    if (file2 != null) {
                        arrayList2.add(file2);
                    }
                }
            }
            return checkFiles(arrayList2);
        }
        if (str.startsWith("fileChooserIcon ")) {
            String strSubstring = str.substring(str.indexOf(" ") + 1);
            if (strSubstring.equals("ListView") || strSubstring.equals("ViewMenu")) {
                i2 = 2;
            } else if (strSubstring.equals("DetailsView")) {
                i2 = 3;
            } else if (strSubstring.equals("UpFolder")) {
                i2 = 8;
            } else if (strSubstring.equals("NewFolder")) {
                i2 = 11;
            } else {
                return null;
            }
            return getStandardViewButton(i2);
        }
        if (str.startsWith("optionPaneIcon ")) {
            if (str == "optionPaneIcon Error") {
                systemIcon = Win32ShellFolder2.SystemIcon.IDI_ERROR;
            } else if (str == "optionPaneIcon Information") {
                systemIcon = Win32ShellFolder2.SystemIcon.IDI_INFORMATION;
            } else if (str == "optionPaneIcon Question") {
                systemIcon = Win32ShellFolder2.SystemIcon.IDI_QUESTION;
            } else if (str == "optionPaneIcon Warning") {
                systemIcon = Win32ShellFolder2.SystemIcon.IDI_EXCLAMATION;
            } else {
                return null;
            }
            return Win32ShellFolder2.getSystemIcon(systemIcon);
        }
        if (str.startsWith("shell32Icon ") || str.startsWith("shell32LargeIcon ")) {
            try {
                int i5 = Integer.parseInt(str.substring(str.indexOf(" ") + 1));
                if (i5 >= 0) {
                    return Win32ShellFolder2.getShell32Icon(i5, str.startsWith("shell32LargeIcon "));
                }
                return null;
            } catch (NumberFormatException e4) {
                return null;
            }
        }
        return null;
    }

    private static File checkFile(File file) {
        SecurityManager securityManager = System.getSecurityManager();
        return (securityManager == null || file == null) ? file : checkFile(file, securityManager);
    }

    private static File checkFile(File file, SecurityManager securityManager) {
        Win32ShellFolder2 win32ShellFolder2;
        try {
            securityManager.checkRead(file.getPath());
            if (file instanceof Win32ShellFolder2) {
                Win32ShellFolder2 win32ShellFolder22 = (Win32ShellFolder2) file;
                if (win32ShellFolder22.isLink() && (win32ShellFolder2 = (Win32ShellFolder2) win32ShellFolder22.getLinkLocation()) != null) {
                    securityManager.checkRead(win32ShellFolder2.getPath());
                }
            }
            return file;
        } catch (SecurityException e2) {
            return null;
        }
    }

    static File[] checkFiles(File[] fileArr) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null || fileArr == null || fileArr.length == 0) {
            return fileArr;
        }
        return checkFiles(Arrays.stream(fileArr), securityManager);
    }

    private static File[] checkFiles(List<File> list) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null || list.isEmpty()) {
            return (File[]) list.toArray(new File[list.size()]);
        }
        return checkFiles(list.stream(), securityManager);
    }

    private static File[] checkFiles(Stream<File> stream, SecurityManager securityManager) {
        return (File[]) stream.filter(file -> {
            return checkFile(file, securityManager) != null;
        }).toArray(i2 -> {
            return new File[i2];
        });
    }

    @Override // sun.awt.shell.ShellFolderManager
    public boolean isComputerNode(final File file) {
        if (file != null && file == getDrives()) {
            return true;
        }
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.awt.shell.Win32ShellFolderManager2.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return file.getAbsolutePath();
            }
        });
        return str.startsWith("\\\\") && str.indexOf(FXMLLoader.ESCAPE_PREFIX, 2) < 0;
    }

    @Override // sun.awt.shell.ShellFolderManager
    public boolean isFileSystemRoot(File file) {
        File[] fileArrListFiles;
        if (file != null) {
            Win32ShellFolder2 drives2 = getDrives();
            if (file instanceof Win32ShellFolder2) {
                Win32ShellFolder2 win32ShellFolder2 = (Win32ShellFolder2) file;
                if (win32ShellFolder2.isFileSystem()) {
                    if (win32ShellFolder2.parent != null) {
                        return win32ShellFolder2.parent.equals(drives2);
                    }
                } else {
                    return false;
                }
            }
            String path = file.getPath();
            return path.length() == 3 && path.charAt(1) == ':' && (fileArrListFiles = drives2.listFiles()) != null && Arrays.asList(fileArrListFiles).contains(file);
        }
        return false;
    }

    static int compareShellFolders(Win32ShellFolder2 win32ShellFolder2, Win32ShellFolder2 win32ShellFolder22) {
        boolean zIsSpecial = win32ShellFolder2.isSpecial();
        boolean zIsSpecial2 = win32ShellFolder22.isSpecial();
        if (zIsSpecial || zIsSpecial2) {
            if (topFolderList == null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(getPersonal());
                arrayList.add(getDesktop());
                arrayList.add(getDrives());
                arrayList.add(getNetwork());
                topFolderList = arrayList;
            }
            int iIndexOf = topFolderList.indexOf(win32ShellFolder2);
            int iIndexOf2 = topFolderList.indexOf(win32ShellFolder22);
            if (iIndexOf >= 0 && iIndexOf2 >= 0) {
                return iIndexOf - iIndexOf2;
            }
            if (iIndexOf >= 0) {
                return -1;
            }
            if (iIndexOf2 >= 0) {
                return 1;
            }
        }
        if (zIsSpecial && !zIsSpecial2) {
            return -1;
        }
        if (zIsSpecial2 && !zIsSpecial) {
            return 1;
        }
        return compareNames(win32ShellFolder2.getAbsolutePath(), win32ShellFolder22.getAbsolutePath());
    }

    static int compareNames(String str, String str2) {
        int iCompareToIgnoreCase = str.compareToIgnoreCase(str2);
        if (iCompareToIgnoreCase != 0) {
            return iCompareToIgnoreCase;
        }
        return str.compareTo(str2);
    }

    @Override // sun.awt.shell.ShellFolderManager
    protected ShellFolder.Invoker createInvoker() {
        return new ComInvoker();
    }

    /* loaded from: rt.jar:sun/awt/shell/Win32ShellFolderManager2$ComInvoker.class */
    private static class ComInvoker extends ThreadPoolExecutor implements ThreadFactory, ShellFolder.Invoker {
        private static Thread comThread;

        private ComInvoker() {
            super(1, 1, 0L, TimeUnit.DAYS, new LinkedBlockingQueue());
            allowCoreThreadTimeOut(false);
            setThreadFactory(this);
            final Runnable runnable = new Runnable() { // from class: sun.awt.shell.Win32ShellFolderManager2.ComInvoker.1
                @Override // java.lang.Runnable
                public void run() {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.shell.Win32ShellFolderManager2.ComInvoker.1.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Void run2() {
                            ComInvoker.this.shutdownNow();
                            return null;
                        }
                    });
                }
            };
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.shell.Win32ShellFolderManager2.ComInvoker.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    Runtime.getRuntime().addShutdownHook(new Thread(runnable));
                    return null;
                }
            });
        }

        @Override // java.util.concurrent.ThreadFactory
        public synchronized Thread newThread(final Runnable runnable) {
            Runnable runnable2 = new Runnable() { // from class: sun.awt.shell.Win32ShellFolderManager2.ComInvoker.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        Win32ShellFolderManager2.initializeCom();
                        runnable.run();
                    } finally {
                        Win32ShellFolderManager2.uninitializeCom();
                    }
                }
            };
            comThread = (Thread) AccessController.doPrivileged(() -> {
                Thread thread = new Thread(ThreadGroupUtils.getRootThreadGroup(), runnable2, "Swing-Shell");
                thread.setDaemon(true);
                return thread;
            });
            return comThread;
        }

        @Override // sun.awt.shell.ShellFolder.Invoker
        public <T> T invoke(Callable<T> callable) throws Exception {
            if (Thread.currentThread() == comThread) {
                return callable.call();
            }
            try {
                final Future<T> futureSubmit = submit(callable);
                try {
                    return futureSubmit.get();
                } catch (InterruptedException e2) {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.shell.Win32ShellFolderManager2.ComInvoker.4
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Void run2() {
                            futureSubmit.cancel(true);
                            return null;
                        }
                    });
                    throw e2;
                } catch (ExecutionException e3) {
                    Throwable cause = e3.getCause();
                    if (cause instanceof Exception) {
                        throw ((Exception) cause);
                    }
                    if (cause instanceof Error) {
                        throw ((Error) cause);
                    }
                    throw new RuntimeException("Unexpected error", cause);
                }
            } catch (RejectedExecutionException e4) {
                throw new InterruptedException(e4.getMessage());
            }
        }
    }
}
