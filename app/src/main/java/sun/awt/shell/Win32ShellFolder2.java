package sun.awt.shell;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javafx.fxml.FXMLLoader;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:sun/awt/shell/Win32ShellFolder2.class */
final class Win32ShellFolder2 extends ShellFolder {
    public static final int DESKTOP = 0;
    public static final int INTERNET = 1;
    public static final int PROGRAMS = 2;
    public static final int CONTROLS = 3;
    public static final int PRINTERS = 4;
    public static final int PERSONAL = 5;
    public static final int FAVORITES = 6;
    public static final int STARTUP = 7;
    public static final int RECENT = 8;
    public static final int SENDTO = 9;
    public static final int BITBUCKET = 10;
    public static final int STARTMENU = 11;
    public static final int DESKTOPDIRECTORY = 16;
    public static final int DRIVES = 17;
    public static final int NETWORK = 18;
    public static final int NETHOOD = 19;
    public static final int FONTS = 20;
    public static final int TEMPLATES = 21;
    public static final int COMMON_STARTMENU = 22;
    public static final int COMMON_PROGRAMS = 23;
    public static final int COMMON_STARTUP = 24;
    public static final int COMMON_DESKTOPDIRECTORY = 25;
    public static final int APPDATA = 26;
    public static final int PRINTHOOD = 27;
    public static final int ALTSTARTUP = 29;
    public static final int COMMON_ALTSTARTUP = 30;
    public static final int COMMON_FAVORITES = 31;
    public static final int INTERNET_CACHE = 32;
    public static final int COOKIES = 33;
    public static final int HISTORY = 34;
    public static final int ATTRIB_CANCOPY = 1;
    public static final int ATTRIB_CANMOVE = 2;
    public static final int ATTRIB_CANLINK = 4;
    public static final int ATTRIB_CANRENAME = 16;
    public static final int ATTRIB_CANDELETE = 32;
    public static final int ATTRIB_HASPROPSHEET = 64;
    public static final int ATTRIB_DROPTARGET = 256;
    public static final int ATTRIB_LINK = 65536;
    public static final int ATTRIB_SHARE = 131072;
    public static final int ATTRIB_READONLY = 262144;
    public static final int ATTRIB_GHOSTED = 524288;
    public static final int ATTRIB_HIDDEN = 524288;
    public static final int ATTRIB_FILESYSANCESTOR = 268435456;
    public static final int ATTRIB_FOLDER = 536870912;
    public static final int ATTRIB_FILESYSTEM = 1073741824;
    public static final int ATTRIB_HASSUBFOLDER = Integer.MIN_VALUE;
    public static final int ATTRIB_VALIDATE = 16777216;
    public static final int ATTRIB_REMOVABLE = 33554432;
    public static final int ATTRIB_COMPRESSED = 67108864;
    public static final int ATTRIB_BROWSABLE = 134217728;
    public static final int ATTRIB_NONENUMERATED = 1048576;
    public static final int ATTRIB_NEWCONTENT = 2097152;
    public static final int SHGDN_NORMAL = 0;
    public static final int SHGDN_INFOLDER = 1;
    public static final int SHGDN_INCLUDE_NONFILESYS = 8192;
    public static final int SHGDN_FORADDRESSBAR = 16384;
    public static final int SHGDN_FORPARSING = 32768;
    FolderDisposer disposer;
    private long pIShellIcon;
    private String folderType;
    private String displayName;
    private Image smallIcon;
    private Image largeIcon;
    private Boolean isDir;
    private boolean isPersonal;
    private volatile Boolean cachedIsFileSystem;
    private volatile Boolean cachedIsLink;
    private static Map smallSystemImages;
    private static Map largeSystemImages;
    private static Map smallLinkedSystemImages;
    private static Map largeLinkedSystemImages;
    private static final int LVCFMT_LEFT = 0;
    private static final int LVCFMT_RIGHT = 1;
    private static final int LVCFMT_CENTER = 2;

    private static native void initIDs();

    /* JADX INFO: Access modifiers changed from: private */
    public native void initDesktop();

    /* JADX INFO: Access modifiers changed from: private */
    public native void initSpecial(long j2, int i2);

    static native long getNextPIDLEntry(long j2);

    static native long copyFirstPIDLEntry(long j2);

    private static native long combinePIDLs(long j2, long j3);

    static native void releasePIDL(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void releaseIShellFolder(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int compareIDs(long j2, long j3, long j4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int getAttributes0(long j2, long j3, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getFileSystemPath0(int i2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native long getEnumObjects(long j2, boolean z2, boolean z3);

    /* JADX INFO: Access modifiers changed from: private */
    public native long getNextChild(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void releaseEnumObjects(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long bindToObject(long j2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getLinkLocation(long j2, long j3, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long parseDisplayName0(long j2, String str) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getDisplayNameOf(long j2, long j3, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native String getFolderType(long j2);

    private native String getExecutableType(String str);

    private static native long getIShellIcon(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int getIconIndex(long j2, long j3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getIcon(String str, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long extractIcon(long j2, long j3, boolean z2);

    private static native long getSystemIcon(int i2);

    private static native long getIconResource(String str, int i2, int i3, int i4, boolean z2);

    private static native int[] getIconBits(long j2, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void disposeIcon(long j2);

    static native int[] getStandardViewButton0(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public native ShellFolderColumnInfo[] doGetColumnInfo(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public native Object doGetColumnValue(long j2, long j3, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int compareIDsByColumn(long j2, long j3, long j4, int i2);

    static {
        initIDs();
        smallSystemImages = new HashMap();
        largeSystemImages = new HashMap();
        smallLinkedSystemImages = new HashMap();
        largeLinkedSystemImages = new HashMap();
    }

    /* loaded from: rt.jar:sun/awt/shell/Win32ShellFolder2$SystemIcon.class */
    public enum SystemIcon {
        IDI_APPLICATION(32512),
        IDI_HAND(32513),
        IDI_ERROR(32513),
        IDI_QUESTION(32514),
        IDI_EXCLAMATION(32515),
        IDI_WARNING(32515),
        IDI_ASTERISK(32516),
        IDI_INFORMATION(32516),
        IDI_WINLOGO(32517);

        private final int iconID;

        SystemIcon(int i2) {
            this.iconID = i2;
        }

        public int getIconID() {
            return this.iconID;
        }
    }

    /* loaded from: rt.jar:sun/awt/shell/Win32ShellFolder2$FolderDisposer.class */
    static class FolderDisposer implements DisposerRecord {
        long absolutePIDL;
        long pIShellFolder;
        long relativePIDL;
        boolean disposed;

        FolderDisposer() {
        }

        @Override // sun.java2d.DisposerRecord
        public void dispose() {
            if (this.disposed) {
                return;
            }
            ShellFolder.invoke(new Callable<Void>() { // from class: sun.awt.shell.Win32ShellFolder2.FolderDisposer.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Void call() {
                    if (FolderDisposer.this.relativePIDL != 0) {
                        Win32ShellFolder2.releasePIDL(FolderDisposer.this.relativePIDL);
                    }
                    if (FolderDisposer.this.absolutePIDL != 0) {
                        Win32ShellFolder2.releasePIDL(FolderDisposer.this.absolutePIDL);
                    }
                    if (FolderDisposer.this.pIShellFolder != 0) {
                        Win32ShellFolder2.releaseIShellFolder(FolderDisposer.this.pIShellFolder);
                        return null;
                    }
                    return null;
                }
            });
            this.disposed = true;
        }
    }

    private void setIShellFolder(long j2) {
        this.disposer.pIShellFolder = j2;
    }

    private void setRelativePIDL(long j2) {
        this.disposer.relativePIDL = j2;
    }

    private static String composePathForCsidl(int i2) throws InterruptedException, IOException {
        String fileSystemPath = getFileSystemPath(i2);
        return fileSystemPath == null ? "ShellFolder: 0x" + Integer.toHexString(i2) : fileSystemPath;
    }

    Win32ShellFolder2(final int i2) throws InterruptedException, IOException {
        super(null, composePathForCsidl(i2));
        this.disposer = new FolderDisposer();
        this.pIShellIcon = -1L;
        this.folderType = null;
        this.displayName = null;
        this.smallIcon = null;
        this.largeIcon = null;
        this.isDir = null;
        invoke(new Callable<Void>() { // from class: sun.awt.shell.Win32ShellFolder2.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Void call() throws InterruptedException {
                if (i2 == 0) {
                    Win32ShellFolder2.this.initDesktop();
                    return null;
                }
                Win32ShellFolder2.this.initSpecial(Win32ShellFolder2.this.getDesktop().getIShellFolder(), i2);
                long nextPIDLEntry = Win32ShellFolder2.this.disposer.relativePIDL;
                Win32ShellFolder2.this.parent = Win32ShellFolder2.this.getDesktop();
                while (nextPIDLEntry != 0) {
                    long jCopyFirstPIDLEntry = Win32ShellFolder2.copyFirstPIDLEntry(nextPIDLEntry);
                    if (jCopyFirstPIDLEntry != 0) {
                        nextPIDLEntry = Win32ShellFolder2.getNextPIDLEntry(nextPIDLEntry);
                        if (nextPIDLEntry != 0) {
                            Win32ShellFolder2.this.parent = new Win32ShellFolder2((Win32ShellFolder2) Win32ShellFolder2.this.parent, jCopyFirstPIDLEntry);
                        } else {
                            Win32ShellFolder2.this.disposer.relativePIDL = jCopyFirstPIDLEntry;
                        }
                    } else {
                        return null;
                    }
                }
                return null;
            }
        }, InterruptedException.class);
        Disposer.addRecord(this, this.disposer);
    }

    Win32ShellFolder2(Win32ShellFolder2 win32ShellFolder2, long j2, long j3, String str) {
        super(win32ShellFolder2, str != null ? str : "ShellFolder: ");
        this.disposer = new FolderDisposer();
        this.pIShellIcon = -1L;
        this.folderType = null;
        this.displayName = null;
        this.smallIcon = null;
        this.largeIcon = null;
        this.isDir = null;
        this.disposer.pIShellFolder = j2;
        this.disposer.relativePIDL = j3;
        Disposer.addRecord(this, this.disposer);
    }

    Win32ShellFolder2(Win32ShellFolder2 win32ShellFolder2, final long j2) throws InterruptedException {
        super(win32ShellFolder2, (String) invoke(new Callable<String>() { // from class: sun.awt.shell.Win32ShellFolder2.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public String call() {
                return Win32ShellFolder2.getFileSystemPath(Win32ShellFolder2.this.getIShellFolder(), j2);
            }
        }, RuntimeException.class));
        this.disposer = new FolderDisposer();
        this.pIShellIcon = -1L;
        this.folderType = null;
        this.displayName = null;
        this.smallIcon = null;
        this.largeIcon = null;
        this.isDir = null;
        this.disposer.relativePIDL = j2;
        Disposer.addRecord(this, this.disposer);
    }

    public void setIsPersonal() {
        this.isPersonal = true;
    }

    @Override // sun.awt.shell.ShellFolder
    protected Object writeReplace() throws ObjectStreamException {
        return invoke(new Callable<File>() { // from class: sun.awt.shell.Win32ShellFolder2.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public File call() {
                File[] fileArrListFiles;
                if (Win32ShellFolder2.this.isFileSystem()) {
                    return new File(Win32ShellFolder2.this.getPath());
                }
                Win32ShellFolder2 drives = Win32ShellFolderManager2.getDrives();
                if (drives != null && (fileArrListFiles = drives.listFiles()) != null) {
                    for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                        if (fileArrListFiles[i2] instanceof Win32ShellFolder2) {
                            Win32ShellFolder2 win32ShellFolder2 = (Win32ShellFolder2) fileArrListFiles[i2];
                            if (win32ShellFolder2.isFileSystem() && !win32ShellFolder2.hasAttribute(33554432)) {
                                return new File(win32ShellFolder2.getPath());
                            }
                        }
                    }
                }
                return new File("C:\\");
            }
        });
    }

    protected void dispose() {
        this.disposer.dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getIShellFolder() {
        if (this.disposer.pIShellFolder == 0) {
            try {
                this.disposer.pIShellFolder = ((Long) invoke(new Callable<Long>() { // from class: sun.awt.shell.Win32ShellFolder2.4
                    static final /* synthetic */ boolean $assertionsDisabled;

                    static {
                        $assertionsDisabled = !Win32ShellFolder2.class.desiredAssertionStatus();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Long call() {
                        if (!$assertionsDisabled && !Win32ShellFolder2.this.isDirectory()) {
                            throw new AssertionError();
                        }
                        if (!$assertionsDisabled && Win32ShellFolder2.this.parent == null) {
                            throw new AssertionError();
                        }
                        long parentIShellFolder = Win32ShellFolder2.this.getParentIShellFolder();
                        if (parentIShellFolder != 0) {
                            long jBindToObject = Win32ShellFolder2.bindToObject(parentIShellFolder, Win32ShellFolder2.this.disposer.relativePIDL);
                            if (jBindToObject == 0) {
                                throw new InternalError("Unable to bind " + Win32ShellFolder2.this.getAbsolutePath() + " to parent");
                            }
                            return Long.valueOf(jBindToObject);
                        }
                        throw new InternalError("Parent IShellFolder was null for " + Win32ShellFolder2.this.getAbsolutePath());
                    }
                }, RuntimeException.class)).longValue();
            } catch (InterruptedException e2) {
            }
        }
        return this.disposer.pIShellFolder;
    }

    public long getParentIShellFolder() {
        Win32ShellFolder2 win32ShellFolder2 = (Win32ShellFolder2) getParentFile();
        if (win32ShellFolder2 == null) {
            return getIShellFolder();
        }
        return win32ShellFolder2.getIShellFolder();
    }

    public long getRelativePIDL() {
        if (this.disposer.relativePIDL == 0) {
            throw new InternalError("Should always have a relative PIDL");
        }
        return this.disposer.relativePIDL;
    }

    private long getAbsolutePIDL() {
        if (this.parent == null) {
            return getRelativePIDL();
        }
        if (this.disposer.absolutePIDL == 0) {
            this.disposer.absolutePIDL = combinePIDLs(((Win32ShellFolder2) this.parent).getAbsolutePIDL(), getRelativePIDL());
        }
        return this.disposer.absolutePIDL;
    }

    public Win32ShellFolder2 getDesktop() {
        return Win32ShellFolderManager2.getDesktop();
    }

    public long getDesktopIShellFolder() {
        return getDesktop().getIShellFolder();
    }

    private static boolean pathsEqual(String str, String str2) {
        return str.equalsIgnoreCase(str2);
    }

    @Override // java.io.File
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Win32ShellFolder2)) {
            if (!(obj instanceof File)) {
                return super.equals(obj);
            }
            return pathsEqual(getPath(), ((File) obj).getPath());
        }
        Win32ShellFolder2 win32ShellFolder2 = (Win32ShellFolder2) obj;
        if (this.parent == null && win32ShellFolder2.parent != null) {
            return false;
        }
        if (this.parent != null && win32ShellFolder2.parent == null) {
            return false;
        }
        if (isFileSystem() && win32ShellFolder2.isFileSystem()) {
            return pathsEqual(getPath(), win32ShellFolder2.getPath()) && (this.parent == win32ShellFolder2.parent || this.parent.equals(win32ShellFolder2.parent));
        }
        if (this.parent == win32ShellFolder2.parent || this.parent.equals(win32ShellFolder2.parent)) {
            try {
                return pidlsEqual(getParentIShellFolder(), this.disposer.relativePIDL, win32ShellFolder2.disposer.relativePIDL);
            } catch (InterruptedException e2) {
                return false;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean pidlsEqual(final long j2, final long j3, final long j4) throws InterruptedException {
        return ((Boolean) invoke(new Callable<Boolean>() { // from class: sun.awt.shell.Win32ShellFolder2.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() {
                return Boolean.valueOf(Win32ShellFolder2.compareIDs(j2, j3, j4) == 0);
            }
        }, RuntimeException.class)).booleanValue();
    }

    @Override // sun.awt.shell.ShellFolder
    public boolean isFileSystem() {
        if (this.cachedIsFileSystem == null) {
            this.cachedIsFileSystem = Boolean.valueOf(hasAttribute(1073741824));
        }
        return this.cachedIsFileSystem.booleanValue();
    }

    public boolean hasAttribute(final int i2) {
        Boolean bool = (Boolean) invoke(new Callable<Boolean>() { // from class: sun.awt.shell.Win32ShellFolder2.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() {
                return Boolean.valueOf((Win32ShellFolder2.getAttributes0(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), i2) & i2) != 0);
            }
        });
        return bool != null && bool.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getFileSystemPath(long j2, long j3) {
        String fileSystemPath;
        if (j2 == Win32ShellFolderManager2.getNetwork().getIShellFolder() && getAttributes0(j2, j3, 536936448) == 536936448 && (fileSystemPath = getFileSystemPath(Win32ShellFolderManager2.getDesktop().getIShellFolder(), getLinkLocation(j2, j3, false))) != null && fileSystemPath.startsWith("\\\\")) {
            return fileSystemPath;
        }
        return getDisplayNameOf(j2, j3, 32768);
    }

    static String getFileSystemPath(final int i2) throws InterruptedException, IOException {
        SecurityManager securityManager;
        String str = (String) invoke(new Callable<String>() { // from class: sun.awt.shell.Win32ShellFolder2.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public String call() throws IOException {
                return Win32ShellFolder2.getFileSystemPath0(i2);
            }
        }, IOException.class);
        if (str != null && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkRead(str);
        }
        return str;
    }

    private static boolean isNetworkRoot(String str) {
        return str.equals("\\\\") || str.equals(FXMLLoader.ESCAPE_PREFIX) || str.equals("//") || str.equals("/");
    }

    @Override // sun.awt.shell.ShellFolder, java.io.File
    public File getParentFile() {
        return this.parent;
    }

    @Override // sun.awt.shell.ShellFolder, java.io.File
    public boolean isDirectory() {
        if (this.isDir == null) {
            if (hasAttribute(536870912) && !hasAttribute(134217728)) {
                this.isDir = Boolean.TRUE;
            } else if (isLink()) {
                ShellFolder linkLocation = getLinkLocation(false);
                this.isDir = Boolean.valueOf(linkLocation != null && linkLocation.isDirectory());
            } else {
                this.isDir = Boolean.FALSE;
            }
        }
        return this.isDir.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getEnumObjects(final boolean z2) throws InterruptedException {
        return ((Long) invoke(new Callable<Long>() { // from class: sun.awt.shell.Win32ShellFolder2.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Long call() {
                return Long.valueOf(Win32ShellFolder2.this.getEnumObjects(Win32ShellFolder2.this.disposer.pIShellFolder, Win32ShellFolder2.this.disposer.pIShellFolder == Win32ShellFolder2.this.getDesktopIShellFolder(), z2));
            }
        }, RuntimeException.class)).longValue();
    }

    @Override // sun.awt.shell.ShellFolder
    public File[] listFiles(final boolean z2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(getPath());
        }
        try {
            return Win32ShellFolderManager2.checkFiles((File[]) invoke(new Callable<File[]>() { // from class: sun.awt.shell.Win32ShellFolder2.9
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public File[] call() throws InterruptedException {
                    Win32ShellFolder2 win32ShellFolder2;
                    if (!Win32ShellFolder2.this.isDirectory()) {
                        return null;
                    }
                    if (Win32ShellFolder2.this.isLink() && !Win32ShellFolder2.this.hasAttribute(536870912)) {
                        return new File[0];
                    }
                    Win32ShellFolder2 desktop = Win32ShellFolderManager2.getDesktop();
                    Win32ShellFolder2 personal = Win32ShellFolderManager2.getPersonal();
                    long iShellFolder = Win32ShellFolder2.this.getIShellFolder();
                    ArrayList arrayList = new ArrayList();
                    long enumObjects = Win32ShellFolder2.this.getEnumObjects(z2);
                    if (enumObjects != 0) {
                        do {
                            try {
                                long nextChild = Win32ShellFolder2.this.getNextChild(enumObjects);
                                boolean z3 = true;
                                if (nextChild != 0 && (Win32ShellFolder2.getAttributes0(iShellFolder, nextChild, 1342177280) & 1342177280) != 0) {
                                    if (Win32ShellFolder2.this.equals(desktop) && personal != null && Win32ShellFolder2.pidlsEqual(iShellFolder, nextChild, personal.disposer.relativePIDL)) {
                                        win32ShellFolder2 = personal;
                                    } else {
                                        win32ShellFolder2 = new Win32ShellFolder2(Win32ShellFolder2.this, nextChild);
                                        z3 = false;
                                    }
                                    arrayList.add(win32ShellFolder2);
                                }
                                if (z3) {
                                    Win32ShellFolder2.releasePIDL(nextChild);
                                }
                                if (nextChild == 0) {
                                    break;
                                }
                            } finally {
                                Win32ShellFolder2.this.releaseEnumObjects(enumObjects);
                            }
                        } while (!Thread.currentThread().isInterrupted());
                    }
                    return Thread.currentThread().isInterrupted() ? new File[0] : (File[]) arrayList.toArray(new ShellFolder[arrayList.size()]);
                }
            }, InterruptedException.class));
        } catch (InterruptedException e2) {
            return new File[0];
        }
    }

    Win32ShellFolder2 getChildByPath(final String str) throws InterruptedException {
        return (Win32ShellFolder2) invoke(new Callable<Win32ShellFolder2>() { // from class: sun.awt.shell.Win32ShellFolder2.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Win32ShellFolder2 call() throws InterruptedException {
                String fileSystemPath;
                long iShellFolder = Win32ShellFolder2.this.getIShellFolder();
                long enumObjects = Win32ShellFolder2.this.getEnumObjects(true);
                Win32ShellFolder2 win32ShellFolder2 = null;
                while (true) {
                    long nextChild = Win32ShellFolder2.this.getNextChild(enumObjects);
                    if (nextChild != 0) {
                        if (Win32ShellFolder2.getAttributes0(iShellFolder, nextChild, 1073741824) != 0 && (fileSystemPath = Win32ShellFolder2.getFileSystemPath(iShellFolder, nextChild)) != null && fileSystemPath.equalsIgnoreCase(str)) {
                            win32ShellFolder2 = new Win32ShellFolder2(Win32ShellFolder2.this, Win32ShellFolder2.bindToObject(iShellFolder, nextChild), nextChild, fileSystemPath);
                            break;
                        }
                        Win32ShellFolder2.releasePIDL(nextChild);
                    } else {
                        break;
                    }
                }
                Win32ShellFolder2.this.releaseEnumObjects(enumObjects);
                return win32ShellFolder2;
            }
        }, InterruptedException.class);
    }

    @Override // sun.awt.shell.ShellFolder
    public boolean isLink() {
        if (this.cachedIsLink == null) {
            this.cachedIsLink = Boolean.valueOf(hasAttribute(65536));
        }
        return this.cachedIsLink.booleanValue();
    }

    @Override // java.io.File
    public boolean isHidden() {
        return hasAttribute(524288);
    }

    @Override // sun.awt.shell.ShellFolder
    public ShellFolder getLinkLocation() {
        return getLinkLocation(true);
    }

    private ShellFolder getLinkLocation(final boolean z2) {
        return (ShellFolder) invoke(new Callable<ShellFolder>() { // from class: sun.awt.shell.Win32ShellFolder2.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public ShellFolder call() {
                if (!Win32ShellFolder2.this.isLink()) {
                    return null;
                }
                Win32ShellFolder2 win32ShellFolder2CreateShellFolderFromRelativePIDL = null;
                long linkLocation = Win32ShellFolder2.getLinkLocation(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), z2);
                if (linkLocation != 0) {
                    try {
                        win32ShellFolder2CreateShellFolderFromRelativePIDL = Win32ShellFolderManager2.createShellFolderFromRelativePIDL(Win32ShellFolder2.this.getDesktop(), linkLocation);
                    } catch (InternalError e2) {
                    } catch (InterruptedException e3) {
                    }
                }
                return win32ShellFolder2CreateShellFolderFromRelativePIDL;
            }
        });
    }

    long parseDisplayName(final String str) throws InterruptedException, IOException {
        return ((Long) invoke(new Callable<Long>() { // from class: sun.awt.shell.Win32ShellFolder2.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Long call() throws IOException {
                return Long.valueOf(Win32ShellFolder2.parseDisplayName0(Win32ShellFolder2.this.getIShellFolder(), str));
            }
        }, IOException.class)).longValue();
    }

    @Override // sun.awt.shell.ShellFolder
    public String getDisplayName() {
        if (this.displayName == null) {
            this.displayName = (String) invoke(new Callable<String>() { // from class: sun.awt.shell.Win32ShellFolder2.13
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() {
                    return Win32ShellFolder2.getDisplayNameOf(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), 0);
                }
            });
        }
        return this.displayName;
    }

    @Override // sun.awt.shell.ShellFolder
    public String getFolderType() {
        if (this.folderType == null) {
            final long absolutePIDL = getAbsolutePIDL();
            this.folderType = (String) invoke(new Callable<String>() { // from class: sun.awt.shell.Win32ShellFolder2.14
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() {
                    return Win32ShellFolder2.getFolderType(absolutePIDL);
                }
            });
        }
        return this.folderType;
    }

    @Override // sun.awt.shell.ShellFolder
    public String getExecutableType() {
        if (!isFileSystem()) {
            return null;
        }
        return getExecutableType(getAbsolutePath());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getIShellIcon() {
        if (this.pIShellIcon == -1) {
            this.pIShellIcon = getIShellIcon(getIShellFolder());
        }
        return this.pIShellIcon;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Image makeIcon(long j2, boolean z2) {
        if (j2 != 0 && j2 != -1) {
            int i2 = z2 ? 32 : 16;
            int[] iconBits = getIconBits(j2, i2);
            if (iconBits != null) {
                BufferedImage bufferedImage = new BufferedImage(i2, i2, 2);
                bufferedImage.setRGB(0, 0, i2, i2, iconBits, 0, i2);
                return bufferedImage;
            }
            return null;
        }
        return null;
    }

    @Override // sun.awt.shell.ShellFolder
    public Image getIcon(final boolean z2) {
        Image image = z2 ? this.largeIcon : this.smallIcon;
        if (image == null) {
            image = (Image) invoke(new Callable<Image>() { // from class: sun.awt.shell.Win32ShellFolder2.15
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Image call() {
                    Map map;
                    Image icon = null;
                    if (Win32ShellFolder2.this.isFileSystem()) {
                        int iconIndex = Win32ShellFolder2.getIconIndex(Win32ShellFolder2.this.parent != null ? ((Win32ShellFolder2) Win32ShellFolder2.this.parent).getIShellIcon() : 0L, Win32ShellFolder2.this.getRelativePIDL());
                        if (iconIndex > 0) {
                            if (Win32ShellFolder2.this.isLink()) {
                                map = z2 ? Win32ShellFolder2.largeLinkedSystemImages : Win32ShellFolder2.smallLinkedSystemImages;
                            } else {
                                map = z2 ? Win32ShellFolder2.largeSystemImages : Win32ShellFolder2.smallSystemImages;
                            }
                            icon = (Image) map.get(Integer.valueOf(iconIndex));
                            if (icon == null) {
                                long icon2 = Win32ShellFolder2.getIcon(Win32ShellFolder2.this.getAbsolutePath(), z2);
                                icon = Win32ShellFolder2.makeIcon(icon2, z2);
                                Win32ShellFolder2.disposeIcon(icon2);
                                if (icon != null) {
                                    map.put(Integer.valueOf(iconIndex), icon);
                                }
                            }
                        }
                    }
                    if (icon == null) {
                        long jExtractIcon = Win32ShellFolder2.extractIcon(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), z2);
                        icon = Win32ShellFolder2.makeIcon(jExtractIcon, z2);
                        Win32ShellFolder2.disposeIcon(jExtractIcon);
                    }
                    if (icon == null) {
                        icon = Win32ShellFolder2.super.getIcon(z2);
                    }
                    return icon;
                }
            });
            if (z2) {
                this.largeIcon = image;
            } else {
                this.smallIcon = image;
            }
        }
        return image;
    }

    static Image getSystemIcon(SystemIcon systemIcon) {
        long systemIcon2 = getSystemIcon(systemIcon.getIconID());
        Image imageMakeIcon = makeIcon(systemIcon2, true);
        disposeIcon(systemIcon2);
        return imageMakeIcon;
    }

    static Image getShell32Icon(int i2, boolean z2) {
        boolean zEquals = true;
        int i3 = z2 ? 32 : 16;
        String str = (String) Toolkit.getDefaultToolkit().getDesktopProperty("win.icon.shellIconBPP");
        if (str != null) {
            zEquals = str.equals("4");
        }
        long iconResource = getIconResource("shell32.dll", i2, i3, i3, zEquals);
        if (iconResource != 0) {
            Image imageMakeIcon = makeIcon(iconResource, z2);
            disposeIcon(iconResource);
            return imageMakeIcon;
        }
        return null;
    }

    @Override // java.io.File
    public File getCanonicalFile() throws IOException {
        return this;
    }

    public boolean isSpecial() {
        return this.isPersonal || !isFileSystem() || this == getDesktop();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // sun.awt.shell.ShellFolder, java.io.File, java.lang.Comparable
    public int compareTo(File file) {
        if (!(file instanceof Win32ShellFolder2)) {
            if (isFileSystem() && !isSpecial()) {
                return super.compareTo(file);
            }
            return -1;
        }
        return Win32ShellFolderManager2.compareShellFolders(this, (Win32ShellFolder2) file);
    }

    @Override // sun.awt.shell.ShellFolder
    public ShellFolderColumnInfo[] getFolderColumns() {
        return (ShellFolderColumnInfo[]) invoke(new Callable<ShellFolderColumnInfo[]>() { // from class: sun.awt.shell.Win32ShellFolder2.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public ShellFolderColumnInfo[] call() {
                int i2;
                ShellFolderColumnInfo[] shellFolderColumnInfoArrDoGetColumnInfo = Win32ShellFolder2.this.doGetColumnInfo(Win32ShellFolder2.this.getIShellFolder());
                if (shellFolderColumnInfoArrDoGetColumnInfo != null) {
                    ArrayList arrayList = new ArrayList();
                    for (int i3 = 0; i3 < shellFolderColumnInfoArrDoGetColumnInfo.length; i3++) {
                        ShellFolderColumnInfo shellFolderColumnInfo = shellFolderColumnInfoArrDoGetColumnInfo[i3];
                        if (shellFolderColumnInfo != null) {
                            if (shellFolderColumnInfo.getAlignment().intValue() == 1) {
                                i2 = 4;
                            } else {
                                i2 = shellFolderColumnInfo.getAlignment().intValue() == 2 ? 0 : 10;
                            }
                            shellFolderColumnInfo.setAlignment(Integer.valueOf(i2));
                            shellFolderColumnInfo.setComparator(new ColumnComparator(Win32ShellFolder2.this, i3));
                            arrayList.add(shellFolderColumnInfo);
                        }
                    }
                    shellFolderColumnInfoArrDoGetColumnInfo = new ShellFolderColumnInfo[arrayList.size()];
                    arrayList.toArray(shellFolderColumnInfoArrDoGetColumnInfo);
                }
                return shellFolderColumnInfoArrDoGetColumnInfo;
            }
        });
    }

    @Override // sun.awt.shell.ShellFolder
    public Object getFolderColumnValue(final int i2) {
        return invoke(new Callable<Object>() { // from class: sun.awt.shell.Win32ShellFolder2.17
            @Override // java.util.concurrent.Callable
            public Object call() {
                return Win32ShellFolder2.this.doGetColumnValue(Win32ShellFolder2.this.getParentIShellFolder(), Win32ShellFolder2.this.getRelativePIDL(), i2);
            }
        });
    }

    @Override // sun.awt.shell.ShellFolder
    public void sortChildren(final List<? extends File> list) {
        invoke(new Callable<Void>() { // from class: sun.awt.shell.Win32ShellFolder2.18
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Void call() {
                Collections.sort(list, new ColumnComparator(Win32ShellFolder2.this, 0));
                return null;
            }
        });
    }

    /* loaded from: rt.jar:sun/awt/shell/Win32ShellFolder2$ColumnComparator.class */
    private static class ColumnComparator implements Comparator<File> {
        private final Win32ShellFolder2 shellFolder;
        private final int columnIdx;

        public ColumnComparator(Win32ShellFolder2 win32ShellFolder2, int i2) {
            this.shellFolder = win32ShellFolder2;
            this.columnIdx = i2;
        }

        @Override // java.util.Comparator
        public int compare(final File file, final File file2) {
            Integer num = (Integer) ShellFolder.invoke(new Callable<Integer>() { // from class: sun.awt.shell.Win32ShellFolder2.ColumnComparator.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() {
                    if ((file instanceof Win32ShellFolder2) && (file2 instanceof Win32ShellFolder2)) {
                        return Integer.valueOf(Win32ShellFolder2.compareIDsByColumn(ColumnComparator.this.shellFolder.getIShellFolder(), ((Win32ShellFolder2) file).getRelativePIDL(), ((Win32ShellFolder2) file2).getRelativePIDL(), ColumnComparator.this.columnIdx));
                    }
                    return 0;
                }
            });
            if (num == null) {
                return 0;
            }
            return num.intValue();
        }
    }
}
