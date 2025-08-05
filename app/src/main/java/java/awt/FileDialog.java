package java.awt;

import java.awt.peer.FileDialogPeer;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:java/awt/FileDialog.class */
public class FileDialog extends Dialog {
    public static final int LOAD = 0;
    public static final int SAVE = 1;
    int mode;
    String dir;
    String file;
    private File[] files;
    private boolean multipleMode;
    FilenameFilter filter;
    private static final String base = "filedlg";
    private static int nameCounter = 0;
    private static final long serialVersionUID = 5035145889651310422L;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setFileDialogAccessor(new AWTAccessor.FileDialogAccessor() { // from class: java.awt.FileDialog.1
            @Override // sun.awt.AWTAccessor.FileDialogAccessor
            public void setFiles(FileDialog fileDialog, File[] fileArr) {
                fileDialog.setFiles(fileArr);
            }

            @Override // sun.awt.AWTAccessor.FileDialogAccessor
            public void setFile(FileDialog fileDialog, String str) {
                fileDialog.file = "".equals(str) ? null : str;
            }

            @Override // sun.awt.AWTAccessor.FileDialogAccessor
            public void setDirectory(FileDialog fileDialog, String str) {
                fileDialog.dir = "".equals(str) ? null : str;
            }

            @Override // sun.awt.AWTAccessor.FileDialogAccessor
            public boolean isMultipleMode(FileDialog fileDialog) {
                boolean z2;
                synchronized (fileDialog.getObjectLock()) {
                    z2 = fileDialog.multipleMode;
                }
                return z2;
            }
        });
    }

    public FileDialog(Frame frame) {
        this(frame, "", 0);
    }

    public FileDialog(Frame frame, String str) {
        this(frame, str, 0);
    }

    public FileDialog(Frame frame, String str, int i2) {
        super(frame, str, true);
        this.multipleMode = false;
        setMode(i2);
        setLayout(null);
    }

    public FileDialog(Dialog dialog) {
        this(dialog, "", 0);
    }

    public FileDialog(Dialog dialog, String str) {
        this(dialog, str, 0);
    }

    public FileDialog(Dialog dialog, String str, int i2) {
        super(dialog, str, true);
        this.multipleMode = false;
        setMode(i2);
        setLayout(null);
    }

    @Override // java.awt.Dialog, java.awt.Window, java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (FileDialog.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    @Override // java.awt.Dialog, java.awt.Window, java.awt.Container, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.parent != null && this.parent.getPeer() == null) {
                this.parent.addNotify();
            }
            if (this.peer == null) {
                this.peer = getToolkit().createFileDialog(this);
            }
            super.addNotify();
        }
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int i2) {
        switch (i2) {
            case 0:
            case 1:
                this.mode = i2;
                return;
            default:
                throw new IllegalArgumentException("illegal file dialog mode");
        }
    }

    public String getDirectory() {
        return this.dir;
    }

    public void setDirectory(String str) {
        this.dir = (str == null || !str.equals("")) ? str : null;
        FileDialogPeer fileDialogPeer = (FileDialogPeer) this.peer;
        if (fileDialogPeer != null) {
            fileDialogPeer.setDirectory(this.dir);
        }
    }

    public String getFile() {
        return this.file;
    }

    public File[] getFiles() {
        synchronized (getObjectLock()) {
            if (this.files != null) {
                return (File[]) this.files.clone();
            }
            return new File[0];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFiles(File[] fileArr) {
        synchronized (getObjectLock()) {
            this.files = fileArr;
        }
    }

    public void setFile(String str) {
        this.file = (str == null || !str.equals("")) ? str : null;
        FileDialogPeer fileDialogPeer = (FileDialogPeer) this.peer;
        if (fileDialogPeer != null) {
            fileDialogPeer.setFile(this.file);
        }
    }

    public void setMultipleMode(boolean z2) {
        synchronized (getObjectLock()) {
            this.multipleMode = z2;
        }
    }

    public boolean isMultipleMode() {
        boolean z2;
        synchronized (getObjectLock()) {
            z2 = this.multipleMode;
        }
        return z2;
    }

    public FilenameFilter getFilenameFilter() {
        return this.filter;
    }

    public synchronized void setFilenameFilter(FilenameFilter filenameFilter) {
        this.filter = filenameFilter;
        FileDialogPeer fileDialogPeer = (FileDialogPeer) this.peer;
        if (fileDialogPeer != null) {
            fileDialogPeer.setFilenameFilter(filenameFilter);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.dir != null && this.dir.equals("")) {
            this.dir = null;
        }
        if (this.file != null && this.file.equals("")) {
            this.file = null;
        }
    }

    @Override // java.awt.Dialog, java.awt.Container, java.awt.Component
    protected String paramString() {
        return ((super.paramString() + ",dir= " + this.dir) + ",file= " + this.file) + (this.mode == 0 ? ",load" : ",save");
    }

    @Override // java.awt.Container, java.awt.Component
    boolean postsOldMouseEvents() {
        return false;
    }
}
