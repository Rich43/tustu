package javax.swing.plaf.basic;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import javax.swing.AbstractListModel;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.filechooser.FileSystemView;
import sun.awt.shell.ShellFolder;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicDirectoryModel.class */
public class BasicDirectoryModel extends AbstractListModel<Object> implements PropertyChangeListener {
    private JFileChooser filechooser;
    private PropertyChangeSupport changeSupport;
    private Vector<File> fileCache = new Vector<>(50);
    private LoadFilesThread loadThread = null;
    private Vector<File> files = null;
    private Vector<File> directories = null;
    private int fetchID = 0;
    private boolean busy = false;

    public BasicDirectoryModel(JFileChooser jFileChooser) {
        this.filechooser = null;
        this.filechooser = jFileChooser;
        validateFileCache();
    }

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
            if ((oldValue instanceof BasicFileChooserUI) && (model = ((BasicFileChooserUI) oldValue).getModel()) != null) {
                model.invalidateFileCache();
            }
        }
    }

    public void invalidateFileCache() {
        if (this.loadThread != null) {
            this.loadThread.interrupt();
            this.loadThread.cancelRunnables();
            this.loadThread = null;
        }
    }

    public Vector<File> getDirectories() {
        synchronized (this.fileCache) {
            if (this.directories != null) {
                return this.directories;
            }
            getFiles();
            return this.directories;
        }
    }

    public Vector<File> getFiles() {
        synchronized (this.fileCache) {
            if (this.files != null) {
                return this.files;
            }
            this.files = new Vector<>();
            this.directories = new Vector<>();
            this.directories.addElement(this.filechooser.getFileSystemView().createFileObject(this.filechooser.getCurrentDirectory(), Constants.ATTRVAL_PARENT));
            for (int i2 = 0; i2 < getSize(); i2++) {
                File file = this.fileCache.get(i2);
                if (this.filechooser.isTraversable(file)) {
                    this.directories.add(file);
                } else {
                    this.files.add(file);
                }
            }
            return this.files;
        }
    }

    public void validateFileCache() {
        File currentDirectory = this.filechooser.getCurrentDirectory();
        if (currentDirectory == null) {
            return;
        }
        if (this.loadThread != null) {
            this.loadThread.interrupt();
            this.loadThread.cancelRunnables();
        }
        int i2 = this.fetchID + 1;
        this.fetchID = i2;
        setBusy(true, i2);
        this.loadThread = new LoadFilesThread(currentDirectory, this.fetchID);
        this.loadThread.start();
    }

    public boolean renameFile(File file, File file2) {
        synchronized (this.fileCache) {
            if (file.renameTo(file2)) {
                validateFileCache();
                return true;
            }
            return false;
        }
    }

    public void fireContentsChanged() {
        fireContentsChanged(this, 0, getSize() - 1);
    }

    @Override // javax.swing.ListModel
    public int getSize() {
        return this.fileCache.size();
    }

    public boolean contains(Object obj) {
        return this.fileCache.contains(obj);
    }

    public int indexOf(Object obj) {
        return this.fileCache.indexOf(obj);
    }

    @Override // javax.swing.ListModel
    public Object getElementAt(int i2) {
        return this.fileCache.get(i2);
    }

    public void intervalAdded(ListDataEvent listDataEvent) {
    }

    public void intervalRemoved(ListDataEvent listDataEvent) {
    }

    protected void sort(Vector<? extends File> vector) {
        ShellFolder.sort(vector);
    }

    protected boolean lt(File file, File file2) {
        int iCompareTo = file.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
        return iCompareTo != 0 ? iCompareTo < 0 : file.getName().compareTo(file2.getName()) < 0;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDirectoryModel$LoadFilesThread.class */
    class LoadFilesThread extends Thread {
        File currentDirectory;
        int fid;
        Vector<DoChangeContents> runnables;

        public LoadFilesThread(File file, int i2) {
            super("Basic L&F File Loading Thread");
            this.currentDirectory = null;
            this.runnables = new Vector<>(10);
            this.currentDirectory = file;
            this.fid = i2;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            run0();
            BasicDirectoryModel.this.setBusy(false, this.fid);
        }

        public void run0() {
            FileSystemView fileSystemView = BasicDirectoryModel.this.filechooser.getFileSystemView();
            if (isInterrupted()) {
                return;
            }
            File[] files = fileSystemView.getFiles(this.currentDirectory, BasicDirectoryModel.this.filechooser.isFileHidingEnabled());
            if (isInterrupted()) {
                return;
            }
            final Vector<? extends File> vector = new Vector<>();
            Vector<? extends File> vector2 = new Vector<>();
            for (File file : files) {
                if (BasicDirectoryModel.this.filechooser.accept(file)) {
                    if (!BasicDirectoryModel.this.filechooser.isTraversable(file)) {
                        if (BasicDirectoryModel.this.filechooser.isFileSelectionEnabled()) {
                            vector2.addElement(file);
                        }
                    } else {
                        vector.addElement(file);
                    }
                    if (isInterrupted()) {
                        return;
                    }
                }
            }
            BasicDirectoryModel.this.sort(vector);
            BasicDirectoryModel.this.sort(vector2);
            vector.addAll(vector2);
            DoChangeContents doChangeContents = (DoChangeContents) ShellFolder.invoke(new Callable<DoChangeContents>() { // from class: javax.swing.plaf.basic.BasicDirectoryModel.LoadFilesThread.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public DoChangeContents call() {
                    int size = vector.size();
                    int size2 = BasicDirectoryModel.this.fileCache.size();
                    if (size > size2) {
                        int i2 = size2;
                        int i3 = size;
                        int i4 = 0;
                        while (true) {
                            if (i4 >= size2) {
                                break;
                            }
                            if (((File) vector.get(i4)).equals(BasicDirectoryModel.this.fileCache.get(i4))) {
                                i4++;
                            } else {
                                i2 = i4;
                                int i5 = i4;
                                while (true) {
                                    if (i5 >= size) {
                                        break;
                                    }
                                    if (!((File) vector.get(i5)).equals(BasicDirectoryModel.this.fileCache.get(i4))) {
                                        i5++;
                                    } else {
                                        i3 = i5;
                                        break;
                                    }
                                }
                            }
                        }
                        if (i2 >= 0 && i3 > i2 && vector.subList(i3, size).equals(BasicDirectoryModel.this.fileCache.subList(i2, size2))) {
                            if (LoadFilesThread.this.isInterrupted()) {
                                return null;
                            }
                            return BasicDirectoryModel.this.new DoChangeContents(vector.subList(i2, i3), i2, null, 0, LoadFilesThread.this.fid);
                        }
                    } else if (size < size2) {
                        int i6 = -1;
                        int i7 = -1;
                        int i8 = 0;
                        while (true) {
                            if (i8 >= size) {
                                break;
                            }
                            if (((File) vector.get(i8)).equals(BasicDirectoryModel.this.fileCache.get(i8))) {
                                i8++;
                            } else {
                                i6 = i8;
                                i7 = (i8 + size2) - size;
                                break;
                            }
                        }
                        if (i6 >= 0 && i7 > i6 && BasicDirectoryModel.this.fileCache.subList(i7, size2).equals(vector.subList(i6, size))) {
                            if (LoadFilesThread.this.isInterrupted()) {
                                return null;
                            }
                            return BasicDirectoryModel.this.new DoChangeContents(null, 0, new Vector(BasicDirectoryModel.this.fileCache.subList(i6, i7)), i6, LoadFilesThread.this.fid);
                        }
                    }
                    if (!BasicDirectoryModel.this.fileCache.equals(vector)) {
                        if (LoadFilesThread.this.isInterrupted()) {
                            LoadFilesThread.this.cancelRunnables(LoadFilesThread.this.runnables);
                        }
                        return BasicDirectoryModel.this.new DoChangeContents(vector, 0, BasicDirectoryModel.this.fileCache, 0, LoadFilesThread.this.fid);
                    }
                    return null;
                }
            });
            if (doChangeContents != null) {
                this.runnables.addElement(doChangeContents);
                SwingUtilities.invokeLater(doChangeContents);
            }
        }

        public void cancelRunnables(Vector<DoChangeContents> vector) {
            Iterator<DoChangeContents> it = vector.iterator();
            while (it.hasNext()) {
                it.next().cancel();
            }
        }

        public void cancelRunnables() {
            cancelRunnables(this.runnables);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport == null) {
            this.changeSupport = new PropertyChangeSupport(this);
        }
        this.changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport != null) {
            this.changeSupport.removePropertyChangeListener(propertyChangeListener);
        }
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        if (this.changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return this.changeSupport.getPropertyChangeListeners();
    }

    protected void firePropertyChange(String str, Object obj, Object obj2) {
        if (this.changeSupport != null) {
            this.changeSupport.firePropertyChange(str, obj, obj2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setBusy(final boolean z2, int i2) {
        if (i2 == this.fetchID) {
            boolean z3 = this.busy;
            this.busy = z2;
            if (this.changeSupport != null && z2 != z3) {
                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.plaf.basic.BasicDirectoryModel.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BasicDirectoryModel.this.firePropertyChange("busy", Boolean.valueOf(!z2), Boolean.valueOf(z2));
                    }
                });
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDirectoryModel$DoChangeContents.class */
    class DoChangeContents implements Runnable {
        private List<File> addFiles;
        private List<File> remFiles;
        private boolean doFire = true;
        private int fid;
        private int addStart;
        private int remStart;

        public DoChangeContents(List<File> list, int i2, List<File> list2, int i3, int i4) {
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
                Method dump skipped, instructions count: 240
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.basic.BasicDirectoryModel.DoChangeContents.run():void");
        }
    }
}
