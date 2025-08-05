package sun.awt.shell;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;

/* loaded from: rt.jar:sun/awt/shell/ShellFolder.class */
public abstract class ShellFolder extends File {
    private static final String COLUMN_NAME = "FileChooser.fileNameHeaderText";
    private static final String COLUMN_SIZE = "FileChooser.fileSizeHeaderText";
    private static final String COLUMN_DATE = "FileChooser.fileDateHeaderText";
    protected ShellFolder parent;
    private static final ShellFolderManager shellFolderManager;
    private static final Invoker invoker;
    private static final Comparator DEFAULT_COMPARATOR;
    private static final Comparator<File> FILE_COMPARATOR;

    /* loaded from: rt.jar:sun/awt/shell/ShellFolder$Invoker.class */
    public interface Invoker {
        <T> T invoke(Callable<T> callable) throws Exception;
    }

    protected abstract Object writeReplace() throws ObjectStreamException;

    public abstract boolean isLink();

    public abstract ShellFolder getLinkLocation() throws FileNotFoundException;

    public abstract String getDisplayName();

    public abstract String getFolderType();

    public abstract String getExecutableType();

    ShellFolder(ShellFolder shellFolder, String str) {
        super(str != null ? str : "ShellFolder");
        this.parent = shellFolder;
    }

    public boolean isFileSystem() {
        return !getPath().startsWith("ShellFolder");
    }

    @Override // java.io.File
    public String getParent() {
        if (this.parent == null && isFileSystem()) {
            return super.getParent();
        }
        if (this.parent != null) {
            return this.parent.getPath();
        }
        return null;
    }

    @Override // java.io.File
    public File getParentFile() {
        if (this.parent != null) {
            return this.parent;
        }
        if (isFileSystem()) {
            return super.getParentFile();
        }
        return null;
    }

    @Override // java.io.File
    public File[] listFiles() {
        return listFiles(true);
    }

    public File[] listFiles(boolean z2) {
        File[] fileArrListFiles = super.listFiles();
        if (!z2) {
            Vector vector = new Vector();
            int length = fileArrListFiles == null ? 0 : fileArrListFiles.length;
            for (int i2 = 0; i2 < length; i2++) {
                if (!fileArrListFiles[i2].isHidden()) {
                    vector.addElement(fileArrListFiles[i2]);
                }
            }
            fileArrListFiles = (File[]) vector.toArray(new File[vector.size()]);
        }
        return fileArrListFiles;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.io.File, java.lang.Comparable
    public int compareTo(File file) {
        if (file == null || !(file instanceof ShellFolder) || ((file instanceof ShellFolder) && ((ShellFolder) file).isFileSystem())) {
            if (isFileSystem()) {
                return super.compareTo(file);
            }
            return -1;
        }
        if (isFileSystem()) {
            return 1;
        }
        return getName().compareTo(file.getName());
    }

    public Image getIcon(boolean z2) {
        return null;
    }

    static {
        Class<?> cls = null;
        try {
            cls = Class.forName((String) Toolkit.getDefaultToolkit().getDesktopProperty("Shell.shellFolderManager"), false, null);
            if (!ShellFolderManager.class.isAssignableFrom(cls)) {
                cls = null;
            }
        } catch (ClassNotFoundException e2) {
        } catch (NullPointerException e3) {
        } catch (SecurityException e4) {
        }
        if (cls == null) {
            cls = ShellFolderManager.class;
        }
        try {
            shellFolderManager = (ShellFolderManager) cls.newInstance();
            invoker = shellFolderManager.createInvoker();
            DEFAULT_COMPARATOR = new Comparator() { // from class: sun.awt.shell.ShellFolder.3
                @Override // java.util.Comparator
                public int compare(Object obj, Object obj2) {
                    int iCompareTo;
                    if (obj == null && obj2 == null) {
                        iCompareTo = 0;
                    } else if (obj != null && obj2 == null) {
                        iCompareTo = 1;
                    } else if (obj == null && obj2 != null) {
                        iCompareTo = -1;
                    } else if (obj instanceof Comparable) {
                        iCompareTo = ((Comparable) obj).compareTo(obj2);
                    } else {
                        iCompareTo = 0;
                    }
                    return iCompareTo;
                }
            };
            FILE_COMPARATOR = new Comparator<File>() { // from class: sun.awt.shell.ShellFolder.4
                @Override // java.util.Comparator
                public int compare(File file, File file2) {
                    ShellFolder shellFolder = null;
                    ShellFolder shellFolder2 = null;
                    if (file instanceof ShellFolder) {
                        shellFolder = (ShellFolder) file;
                        if (shellFolder.isFileSystem()) {
                            shellFolder = null;
                        }
                    }
                    if (file2 instanceof ShellFolder) {
                        shellFolder2 = (ShellFolder) file2;
                        if (shellFolder2.isFileSystem()) {
                            shellFolder2 = null;
                        }
                    }
                    if (shellFolder != null && shellFolder2 != null) {
                        return shellFolder.compareTo((File) shellFolder2);
                    }
                    if (shellFolder != null) {
                        return -1;
                    }
                    if (shellFolder2 != null) {
                        return 1;
                    }
                    String name = file.getName();
                    String name2 = file2.getName();
                    int iCompareToIgnoreCase = name.compareToIgnoreCase(name2);
                    if (iCompareToIgnoreCase != 0) {
                        return iCompareToIgnoreCase;
                    }
                    return name.compareTo(name2);
                }
            };
        } catch (IllegalAccessException e5) {
            throw new Error("Could not access Shell Folder Manager: " + cls.getName());
        } catch (InstantiationException e6) {
            throw new Error("Could not instantiate Shell Folder Manager: " + cls.getName());
        }
    }

    public static ShellFolder getShellFolder(File file) throws FileNotFoundException {
        if (file instanceof ShellFolder) {
            return (ShellFolder) file;
        }
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return shellFolderManager.createShellFolder(file);
    }

    public static Object get(String str) {
        return shellFolderManager.get(str);
    }

    public static boolean isComputerNode(File file) {
        return shellFolderManager.isComputerNode(file);
    }

    public static boolean isFileSystemRoot(File file) {
        return shellFolderManager.isFileSystemRoot(file);
    }

    public static File getNormalizedFile(File file) throws IOException {
        File canonicalFile = file.getCanonicalFile();
        if (file.equals(canonicalFile)) {
            return canonicalFile;
        }
        return new File(file.toURI().normalize());
    }

    public static void sort(final List<? extends File> list) {
        if (list == null || list.size() <= 1) {
            return;
        }
        invoke(new Callable<Void>() { // from class: sun.awt.shell.ShellFolder.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Code restructure failed: missing block: B:9:0x0031, code lost:
            
                r4 = null;
             */
            @Override // java.util.concurrent.Callable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.lang.Void call() {
                /*
                    r3 = this;
                    r0 = 0
                    r4 = r0
                    r0 = r3
                    java.util.List r0 = r4
                    java.util.Iterator r0 = r0.iterator()
                    r5 = r0
                Lc:
                    r0 = r5
                    boolean r0 = r0.hasNext()
                    if (r0 == 0) goto L57
                    r0 = r5
                    java.lang.Object r0 = r0.next()
                    java.io.File r0 = (java.io.File) r0
                    r6 = r0
                    r0 = r6
                    java.io.File r0 = r0.getParentFile()
                    r7 = r0
                    r0 = r7
                    if (r0 == 0) goto L31
                    r0 = r6
                    boolean r0 = r0 instanceof sun.awt.shell.ShellFolder
                    if (r0 != 0) goto L36
                L31:
                    r0 = 0
                    r4 = r0
                    goto L57
                L36:
                    r0 = r4
                    if (r0 != 0) goto L40
                    r0 = r7
                    r4 = r0
                    goto L54
                L40:
                    r0 = r4
                    r1 = r7
                    if (r0 == r1) goto L54
                    r0 = r4
                    r1 = r7
                    boolean r0 = r0.equals(r1)
                    if (r0 != 0) goto L54
                    r0 = 0
                    r4 = r0
                    goto L57
                L54:
                    goto Lc
                L57:
                    r0 = r4
                    boolean r0 = r0 instanceof sun.awt.shell.ShellFolder
                    if (r0 == 0) goto L6c
                    r0 = r4
                    sun.awt.shell.ShellFolder r0 = (sun.awt.shell.ShellFolder) r0
                    r1 = r3
                    java.util.List r1 = r4
                    r0.sortChildren(r1)
                    goto L76
                L6c:
                    r0 = r3
                    java.util.List r0 = r4
                    java.util.Comparator r1 = sun.awt.shell.ShellFolder.access$000()
                    java.util.Collections.sort(r0, r1)
                L76:
                    r0 = 0
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.awt.shell.ShellFolder.AnonymousClass1.call():java.lang.Void");
            }
        });
    }

    public void sortChildren(final List<? extends File> list) {
        invoke(new Callable<Void>() { // from class: sun.awt.shell.ShellFolder.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Void call() {
                Collections.sort(list, ShellFolder.FILE_COMPARATOR);
                return null;
            }
        });
    }

    @Override // java.io.File
    public boolean isAbsolute() {
        return !isFileSystem() || super.isAbsolute();
    }

    @Override // java.io.File
    public File getAbsoluteFile() {
        return isFileSystem() ? super.getAbsoluteFile() : this;
    }

    @Override // java.io.File
    public boolean canRead() {
        if (isFileSystem()) {
            return super.canRead();
        }
        return true;
    }

    @Override // java.io.File
    public boolean canWrite() {
        if (isFileSystem()) {
            return super.canWrite();
        }
        return false;
    }

    @Override // java.io.File
    public boolean exists() {
        return !isFileSystem() || isFileSystemRoot(this) || super.exists();
    }

    @Override // java.io.File
    public boolean isDirectory() {
        if (isFileSystem()) {
            return super.isDirectory();
        }
        return true;
    }

    @Override // java.io.File
    public boolean isFile() {
        return isFileSystem() ? super.isFile() : !isDirectory();
    }

    @Override // java.io.File
    public long lastModified() {
        if (isFileSystem()) {
            return super.lastModified();
        }
        return 0L;
    }

    @Override // java.io.File
    public long length() {
        if (isFileSystem()) {
            return super.length();
        }
        return 0L;
    }

    @Override // java.io.File
    public boolean createNewFile() throws IOException {
        if (isFileSystem()) {
            return super.createNewFile();
        }
        return false;
    }

    @Override // java.io.File
    public boolean delete() {
        if (isFileSystem()) {
            return super.delete();
        }
        return false;
    }

    @Override // java.io.File
    public void deleteOnExit() {
        if (isFileSystem()) {
            super.deleteOnExit();
        }
    }

    @Override // java.io.File
    public boolean mkdir() {
        if (isFileSystem()) {
            return super.mkdir();
        }
        return false;
    }

    @Override // java.io.File
    public boolean mkdirs() {
        if (isFileSystem()) {
            return super.mkdirs();
        }
        return false;
    }

    @Override // java.io.File
    public boolean renameTo(File file) {
        if (isFileSystem()) {
            return super.renameTo(file);
        }
        return false;
    }

    @Override // java.io.File
    public boolean setLastModified(long j2) {
        if (isFileSystem()) {
            return super.setLastModified(j2);
        }
        return false;
    }

    @Override // java.io.File
    public boolean setReadOnly() {
        if (isFileSystem()) {
            return super.setReadOnly();
        }
        return false;
    }

    @Override // java.io.File
    public String toString() {
        return isFileSystem() ? super.toString() : getDisplayName();
    }

    public static ShellFolderColumnInfo[] getFolderColumns(File file) {
        ShellFolderColumnInfo[] folderColumns = null;
        if (file instanceof ShellFolder) {
            folderColumns = ((ShellFolder) file).getFolderColumns();
        }
        if (folderColumns == null) {
            folderColumns = new ShellFolderColumnInfo[]{new ShellFolderColumnInfo(COLUMN_NAME, 150, 10, true, null, FILE_COMPARATOR), new ShellFolderColumnInfo(COLUMN_SIZE, 75, 4, true, null, DEFAULT_COMPARATOR, true), new ShellFolderColumnInfo(COLUMN_DATE, 130, 10, true, null, DEFAULT_COMPARATOR, true)};
        }
        return folderColumns;
    }

    public ShellFolderColumnInfo[] getFolderColumns() {
        return null;
    }

    public static Object getFolderColumnValue(File file, int i2) {
        Object folderColumnValue;
        if ((file instanceof ShellFolder) && (folderColumnValue = ((ShellFolder) file).getFolderColumnValue(i2)) != null) {
            return folderColumnValue;
        }
        if (file == null || !file.exists()) {
            return null;
        }
        switch (i2) {
            case 0:
                return file;
            case 1:
                if (file.isDirectory()) {
                    return null;
                }
                return Long.valueOf(file.length());
            case 2:
                if (isFileSystemRoot(file)) {
                    return null;
                }
                long jLastModified = file.lastModified();
                if (jLastModified == 0) {
                    return null;
                }
                return new Date(jLastModified);
            default:
                return null;
        }
    }

    public Object getFolderColumnValue(int i2) {
        return null;
    }

    public static <T> T invoke(Callable<T> callable) {
        try {
            return (T) invoke(callable, RuntimeException.class);
        } catch (InterruptedException e2) {
            return null;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: E extends java.lang.Throwable */
    public static <T, E extends Throwable> T invoke(Callable<T> callable, Class<E> cls) throws Throwable {
        try {
            return (T) invoker.invoke(callable);
        } catch (Exception e2) {
            if (e2 instanceof RuntimeException) {
                throw ((RuntimeException) e2);
            }
            if (e2 instanceof InterruptedException) {
                Thread.currentThread().interrupt();
                throw ((InterruptedException) e2);
            }
            if (cls.isInstance(e2)) {
                throw cls.cast(e2);
            }
            throw new RuntimeException("Unexpected error", e2);
        }
    }
}
