package de.muntjak.tinylookandfeel;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.List;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.plaf.basic.BasicDirectoryModel;
import javax.swing.plaf.basic.BasicFileChooserUI;
import sun.awt.shell.ShellFolder;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyDirectoryModel.class */
public class TinyDirectoryModel extends BasicDirectoryModel {
    private JFileChooser filechooser;
    private Vector fileCache;
    private LoadFilesThread loadThread;
    private Vector files;
    private Vector directories;
    private int fetchID;
    private PropertyChangeSupport changeSupport;
    private boolean busy;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyDirectoryModel$DoChangeContents.class */
    class DoChangeContents implements Runnable {
        private List addFiles;
        private List remFiles;
        private boolean doFire = true;
        private int fid;
        private int addStart;
        private int remStart;
        private int change;
        private final TinyDirectoryModel this$0;

        public DoChangeContents(TinyDirectoryModel tinyDirectoryModel, List list, int i2, List list2, int i3, int i4) {
            this.this$0 = tinyDirectoryModel;
            this.addStart = 0;
            this.remStart = 0;
            this.addFiles = list;
            this.addStart = i2;
            this.remFiles = list2;
            this.remStart = i3;
            this.fid = i4;
        }

        synchronized void cancel() {
            this.doFire = false;
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x0060 A[Catch: all -> 0x008a, TryCatch #0 {, blocks: (B:18:0x004d, B:21:0x0060, B:22:0x0073, B:23:0x0086), top: B:44:0x004d }] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public synchronized void run() {
            /*
                Method dump skipped, instructions count: 235
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: de.muntjak.tinylookandfeel.TinyDirectoryModel.DoChangeContents.run():void");
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyDirectoryModel$LoadFilesThread.class */
    class LoadFilesThread extends Thread {
        File currentDirectory;
        int fid;
        Vector runnables;
        private final TinyDirectoryModel this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public LoadFilesThread(TinyDirectoryModel tinyDirectoryModel, File file, int i2) {
            super("Basic L&F File Loading Thread");
            this.this$0 = tinyDirectoryModel;
            this.currentDirectory = null;
            this.runnables = new Vector(10);
            this.currentDirectory = file;
            this.fid = i2;
        }

        private void invokeLater(Runnable runnable) {
            this.runnables.addElement(runnable);
            SwingUtilities.invokeLater(runnable);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            run0();
            this.this$0.setBusy(false, this.fid);
        }

        public void run0() {
            File[] files = this.this$0.filechooser.getFileSystemView().getFiles(this.currentDirectory, this.this$0.filechooser.isFileHidingEnabled());
            Vector vector = new Vector();
            if (isInterrupted()) {
                return;
            }
            for (int i2 = 0; i2 < files.length; i2++) {
                if (this.this$0.filechooser.accept(files[i2])) {
                    vector.addElement(files[i2]);
                }
            }
            if (isInterrupted()) {
                return;
            }
            this.this$0.sort(vector);
            Vector vector2 = new Vector(50);
            Vector vector3 = new Vector();
            for (int i3 = 0; i3 < vector.size(); i3++) {
                File file = (File) vector.elementAt(i3);
                boolean zIsTraversable = this.this$0.filechooser.isTraversable(file);
                if (zIsTraversable) {
                    vector2.addElement(file);
                } else if (!zIsTraversable && this.this$0.filechooser.isFileSelectionEnabled()) {
                    vector3.addElement(file);
                }
                if (isInterrupted()) {
                    return;
                }
            }
            Vector vector4 = new Vector(vector2);
            vector4.addAll(vector3);
            int size = vector4.size();
            int size2 = this.this$0.fileCache.size();
            if (size > size2) {
                int i4 = size2;
                int i5 = size;
                int i6 = 0;
                while (true) {
                    if (i6 >= size2) {
                        break;
                    }
                    if (vector4.get(i6).equals(this.this$0.fileCache.get(i6))) {
                        i6++;
                    } else {
                        i4 = i6;
                        int i7 = i6;
                        while (true) {
                            if (i7 >= size) {
                                break;
                            }
                            if (vector4.get(i7).equals(this.this$0.fileCache.get(i6))) {
                                i5 = i7;
                                break;
                            }
                            i7++;
                        }
                    }
                }
                if (i4 >= 0 && i5 > i4 && vector4.subList(i5, size).equals(this.this$0.fileCache.subList(i4, size2))) {
                    if (isInterrupted()) {
                        return;
                    }
                    invokeLater(new DoChangeContents(this.this$0, vector4.subList(i4, i5), i4, null, 0, this.fid));
                    vector4 = null;
                }
            } else if (size < size2) {
                int i8 = -1;
                int i9 = -1;
                int i10 = 0;
                while (true) {
                    if (i10 >= size) {
                        break;
                    }
                    if (!vector4.get(i10).equals(this.this$0.fileCache.get(i10))) {
                        i8 = i10;
                        i9 = (i10 + size2) - size;
                        break;
                    }
                    i10++;
                }
                if (i8 >= 0 && i9 > i8 && this.this$0.fileCache.subList(i9, size2).equals(vector4.subList(i8, size))) {
                    if (isInterrupted()) {
                        return;
                    }
                    invokeLater(new DoChangeContents(this.this$0, null, 0, new Vector(this.this$0.fileCache.subList(i8, i9)), i8, this.fid));
                    vector4 = null;
                }
            }
            if (vector4 == null || this.this$0.fileCache.equals(vector4)) {
                return;
            }
            if (isInterrupted()) {
                cancelRunnables(this.runnables);
            }
            invokeLater(new DoChangeContents(this.this$0, vector4, 0, this.this$0.fileCache, 0, this.fid));
        }

        public void cancelRunnables(Vector vector) {
            for (int i2 = 0; i2 < vector.size(); i2++) {
                ((DoChangeContents) vector.elementAt(i2)).cancel();
            }
        }

        public void cancelRunnables() {
            cancelRunnables(this.runnables);
        }
    }

    public TinyDirectoryModel(JFileChooser jFileChooser) {
        super(jFileChooser);
        this.filechooser = null;
        this.fileCache = new Vector(50);
        this.loadThread = null;
        this.files = null;
        this.directories = null;
        this.fetchID = 0;
        this.busy = false;
        this.filechooser = jFileChooser;
        validateFileCache();
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel, java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        BasicDirectoryModel model;
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName == JFileChooser.DIRECTORY_CHANGED_PROPERTY || propertyName == JFileChooser.FILE_VIEW_CHANGED_PROPERTY || propertyName == JFileChooser.FILE_FILTER_CHANGED_PROPERTY || propertyName == JFileChooser.FILE_HIDING_CHANGED_PROPERTY || propertyName == JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY) {
            validateFileCache();
            return;
        }
        if (!"UI".equals(propertyName)) {
            if ("JFileChooserDialogIsClosingProperty".equals(propertyName)) {
                invalidateFileCache();
            }
        } else {
            Object oldValue = propertyChangeEvent.getOldValue();
            if (!(oldValue instanceof BasicFileChooserUI) || (model = ((BasicFileChooserUI) oldValue).getModel()) == null) {
                return;
            }
            model.invalidateFileCache();
        }
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public void invalidateFileCache() {
        if (this.loadThread != null) {
            this.loadThread.interrupt();
            this.loadThread.cancelRunnables();
            this.loadThread = null;
        }
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public Vector getDirectories() {
        synchronized (this.fileCache) {
            if (this.directories != null) {
                return this.directories;
            }
            getFiles();
            return this.directories;
        }
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public Vector getFiles() {
        synchronized (this.fileCache) {
            if (this.files != null) {
                return this.files;
            }
            this.files = new Vector();
            this.directories = new Vector();
            this.directories.addElement(this.filechooser.getFileSystemView().createFileObject(this.filechooser.getCurrentDirectory(), Constants.ATTRVAL_PARENT));
            for (int i2 = 0; i2 < getSize(); i2++) {
                File file = (File) this.fileCache.get(i2);
                if (this.filechooser.isTraversable(file)) {
                    this.directories.add(file);
                } else {
                    this.files.add(file);
                }
            }
            return this.files;
        }
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public void validateFileCache() {
        File currentDirectory;
        if (this.filechooser == null || (currentDirectory = this.filechooser.getCurrentDirectory()) == null) {
            return;
        }
        if (this.loadThread != null) {
            this.loadThread.interrupt();
            this.loadThread.cancelRunnables();
        }
        int i2 = this.fetchID + 1;
        this.fetchID = i2;
        setBusy(true, i2);
        this.loadThread = new LoadFilesThread(this, currentDirectory, this.fetchID);
        this.loadThread.start();
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public boolean renameFile(File file, File file2) {
        synchronized (this.fileCache) {
            if (!file.renameTo(file2)) {
                return false;
            }
            validateFileCache();
            return true;
        }
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public void fireContentsChanged() {
        fireContentsChanged(this, 0, getSize() - 1);
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel, javax.swing.ListModel
    public int getSize() {
        return this.fileCache.size();
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public boolean contains(Object obj) {
        return this.fileCache.contains(obj);
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public int indexOf(Object obj) {
        return this.fileCache.indexOf(obj);
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel, javax.swing.ListModel
    public Object getElementAt(int i2) {
        return this.fileCache.get(i2);
    }

    public Vector getFileCache() {
        return this.fileCache;
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public void intervalAdded(ListDataEvent listDataEvent) {
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public void intervalRemoved(ListDataEvent listDataEvent) {
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    protected void sort(Vector vector) {
        ShellFolder.sortFiles(vector);
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    protected boolean lt(File file, File file2) {
        int iCompareTo = file.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
        return iCompareTo != 0 ? iCompareTo < 0 : file.getName().compareTo(file2.getName()) < 0;
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport == null) {
            this.changeSupport = new PropertyChangeSupport(this);
        }
        this.changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport != null) {
            this.changeSupport.removePropertyChangeListener(propertyChangeListener);
        }
    }

    @Override // javax.swing.plaf.basic.BasicDirectoryModel
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.changeSupport == null ? new PropertyChangeListener[0] : this.changeSupport.getPropertyChangeListeners();
    }

    protected void firePropertyChange(String str, boolean z2, boolean z3) {
        if (this.changeSupport != null) {
            this.changeSupport.firePropertyChange(str, z2, z3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setBusy(boolean z2, int i2) {
        if (i2 == this.fetchID) {
            boolean z3 = this.busy;
            this.busy = z2;
            if (this.changeSupport == null || z2 == z3) {
                return;
            }
            SwingUtilities.invokeLater(new Runnable(this, z2) { // from class: de.muntjak.tinylookandfeel.TinyDirectoryModel.1
                private final boolean val$busy;
                private final TinyDirectoryModel this$0;

                {
                    this.this$0 = this;
                    this.val$busy = z2;
                }

                @Override // java.lang.Runnable
                public void run() {
                    this.this$0.firePropertyChange("busy", !this.val$busy, this.val$busy);
                }
            });
        }
    }
}
