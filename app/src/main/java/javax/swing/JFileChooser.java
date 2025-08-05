package javax.swing;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.FileChooserUI;

/* loaded from: rt.jar:javax/swing/JFileChooser.class */
public class JFileChooser extends JComponent implements Accessible {
    private static final String uiClassID = "FileChooserUI";
    public static final int OPEN_DIALOG = 0;
    public static final int SAVE_DIALOG = 1;
    public static final int CUSTOM_DIALOG = 2;
    public static final int CANCEL_OPTION = 1;
    public static final int APPROVE_OPTION = 0;
    public static final int ERROR_OPTION = -1;
    public static final int FILES_ONLY = 0;
    public static final int DIRECTORIES_ONLY = 1;
    public static final int FILES_AND_DIRECTORIES = 2;
    public static final String CANCEL_SELECTION = "CancelSelection";
    public static final String APPROVE_SELECTION = "ApproveSelection";
    public static final String APPROVE_BUTTON_TEXT_CHANGED_PROPERTY = "ApproveButtonTextChangedProperty";
    public static final String APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY = "ApproveButtonToolTipTextChangedProperty";
    public static final String APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY = "ApproveButtonMnemonicChangedProperty";
    public static final String CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY = "ControlButtonsAreShownChangedProperty";
    public static final String DIRECTORY_CHANGED_PROPERTY = "directoryChanged";
    public static final String SELECTED_FILE_CHANGED_PROPERTY = "SelectedFileChangedProperty";
    public static final String SELECTED_FILES_CHANGED_PROPERTY = "SelectedFilesChangedProperty";
    public static final String MULTI_SELECTION_ENABLED_CHANGED_PROPERTY = "MultiSelectionEnabledChangedProperty";
    public static final String FILE_SYSTEM_VIEW_CHANGED_PROPERTY = "FileSystemViewChanged";
    public static final String FILE_VIEW_CHANGED_PROPERTY = "fileViewChanged";
    public static final String FILE_HIDING_CHANGED_PROPERTY = "FileHidingChanged";
    public static final String FILE_FILTER_CHANGED_PROPERTY = "fileFilterChanged";
    public static final String FILE_SELECTION_MODE_CHANGED_PROPERTY = "fileSelectionChanged";
    public static final String ACCESSORY_CHANGED_PROPERTY = "AccessoryChangedProperty";
    public static final String ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY = "acceptAllFileFilterUsedChanged";
    public static final String DIALOG_TITLE_CHANGED_PROPERTY = "DialogTitleChangedProperty";
    public static final String DIALOG_TYPE_CHANGED_PROPERTY = "DialogTypeChangedProperty";
    public static final String CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY = "ChoosableFileFilterChangedProperty";
    private String dialogTitle;
    private String approveButtonText;
    private String approveButtonToolTipText;
    private int approveButtonMnemonic;
    private Vector<FileFilter> filters;
    private JDialog dialog;
    private int dialogType;
    private int returnValue;
    private JComponent accessory;
    private FileView fileView;
    private boolean controlsShown;
    private boolean useFileHiding;
    private static final String SHOW_HIDDEN_PROP = "awt.file.showHiddenFiles";
    private transient PropertyChangeListener showFilesListener;
    private int fileSelectionMode;
    private boolean multiSelectionEnabled;
    private boolean useAcceptAllFileFilter;
    private boolean dragEnabled;
    private FileFilter fileFilter;
    private FileSystemView fileSystemView;
    private File currentDirectory;
    private File selectedFile;
    private File[] selectedFiles;
    protected AccessibleContext accessibleContext;

    public JFileChooser() {
        this((File) null, (FileSystemView) null);
    }

    public JFileChooser(String str) {
        this(str, (FileSystemView) null);
    }

    public JFileChooser(File file) {
        this(file, (FileSystemView) null);
    }

    public JFileChooser(FileSystemView fileSystemView) {
        this((File) null, fileSystemView);
    }

    public JFileChooser(File file, FileSystemView fileSystemView) {
        this.dialogTitle = null;
        this.approveButtonText = null;
        this.approveButtonToolTipText = null;
        this.approveButtonMnemonic = 0;
        this.filters = new Vector<>(5);
        this.dialog = null;
        this.dialogType = 0;
        this.returnValue = -1;
        this.accessory = null;
        this.fileView = null;
        this.controlsShown = true;
        this.useFileHiding = true;
        this.showFilesListener = null;
        this.fileSelectionMode = 0;
        this.multiSelectionEnabled = false;
        this.useAcceptAllFileFilter = true;
        this.dragEnabled = false;
        this.fileFilter = null;
        this.fileSystemView = null;
        this.currentDirectory = null;
        this.selectedFile = null;
        this.accessibleContext = null;
        setup(fileSystemView);
        setCurrentDirectory(file);
    }

    public JFileChooser(String str, FileSystemView fileSystemView) {
        this.dialogTitle = null;
        this.approveButtonText = null;
        this.approveButtonToolTipText = null;
        this.approveButtonMnemonic = 0;
        this.filters = new Vector<>(5);
        this.dialog = null;
        this.dialogType = 0;
        this.returnValue = -1;
        this.accessory = null;
        this.fileView = null;
        this.controlsShown = true;
        this.useFileHiding = true;
        this.showFilesListener = null;
        this.fileSelectionMode = 0;
        this.multiSelectionEnabled = false;
        this.useAcceptAllFileFilter = true;
        this.dragEnabled = false;
        this.fileFilter = null;
        this.fileSystemView = null;
        this.currentDirectory = null;
        this.selectedFile = null;
        this.accessibleContext = null;
        setup(fileSystemView);
        if (str == null) {
            setCurrentDirectory(null);
        } else {
            setCurrentDirectory(this.fileSystemView.createFileObject(str));
        }
    }

    protected void setup(FileSystemView fileSystemView) {
        installShowFilesListener();
        installHierarchyListener();
        if (fileSystemView == null) {
            fileSystemView = FileSystemView.getFileSystemView();
        }
        setFileSystemView(fileSystemView);
        updateUI();
        if (isAcceptAllFileFilterUsed()) {
            setFileFilter(getAcceptAllFileFilter());
        }
        enableEvents(16L);
    }

    private void installHierarchyListener() {
        addHierarchyListener(new HierarchyListener() { // from class: javax.swing.JFileChooser.1
            @Override // java.awt.event.HierarchyListener
            public void hierarchyChanged(HierarchyEvent hierarchyEvent) {
                JFileChooser jFileChooser;
                JRootPane rootPane;
                if ((hierarchyEvent.getChangeFlags() & 1) == 1 && (rootPane = SwingUtilities.getRootPane((jFileChooser = JFileChooser.this))) != null) {
                    rootPane.setDefaultButton(jFileChooser.getUI().getDefaultButton(jFileChooser));
                }
            }
        });
    }

    private void installShowFilesListener() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Object desktopProperty = defaultToolkit.getDesktopProperty(SHOW_HIDDEN_PROP);
        if (desktopProperty instanceof Boolean) {
            this.useFileHiding = !((Boolean) desktopProperty).booleanValue();
            this.showFilesListener = new WeakPCL(this);
            defaultToolkit.addPropertyChangeListener(SHOW_HIDDEN_PROP, this.showFilesListener);
        }
    }

    public void setDragEnabled(boolean z2) {
        if (z2 && GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        this.dragEnabled = z2;
    }

    public boolean getDragEnabled() {
        return this.dragEnabled;
    }

    public File getSelectedFile() {
        return this.selectedFile;
    }

    public void setSelectedFile(File file) {
        File file2 = this.selectedFile;
        this.selectedFile = file;
        if (this.selectedFile != null) {
            if (file.isAbsolute() && !getFileSystemView().isParent(getCurrentDirectory(), this.selectedFile)) {
                setCurrentDirectory(this.selectedFile.getParentFile());
            }
            if (!isMultiSelectionEnabled() || this.selectedFiles == null || this.selectedFiles.length == 1) {
                ensureFileIsVisible(this.selectedFile);
            }
        }
        firePropertyChange(SELECTED_FILE_CHANGED_PROPERTY, file2, this.selectedFile);
    }

    public File[] getSelectedFiles() {
        if (this.selectedFiles == null) {
            return new File[0];
        }
        return (File[]) this.selectedFiles.clone();
    }

    public void setSelectedFiles(File[] fileArr) {
        File[] fileArr2 = this.selectedFiles;
        if (fileArr == null || fileArr.length == 0) {
            fileArr = null;
            this.selectedFiles = null;
            setSelectedFile(null);
        } else {
            this.selectedFiles = (File[]) fileArr.clone();
            setSelectedFile(this.selectedFiles[0]);
        }
        firePropertyChange(SELECTED_FILES_CHANGED_PROPERTY, fileArr2, fileArr);
    }

    public File getCurrentDirectory() {
        return this.currentDirectory;
    }

    public void setCurrentDirectory(File file) {
        File file2 = this.currentDirectory;
        if (file != null && !file.exists()) {
            file = this.currentDirectory;
        }
        if (file == null) {
            file = getFileSystemView().getDefaultDirectory();
        }
        if (this.currentDirectory != null && this.currentDirectory.equals(file)) {
            return;
        }
        File file3 = null;
        while (!isTraversable(file) && file3 != file) {
            file3 = file;
            file = getFileSystemView().getParentDirectory(file);
        }
        this.currentDirectory = file;
        firePropertyChange(DIRECTORY_CHANGED_PROPERTY, file2, this.currentDirectory);
    }

    public void changeToParentDirectory() {
        this.selectedFile = null;
        setCurrentDirectory(getFileSystemView().getParentDirectory(getCurrentDirectory()));
    }

    public void rescanCurrentDirectory() {
        getUI().rescanCurrentDirectory(this);
    }

    public void ensureFileIsVisible(File file) {
        getUI().ensureFileIsVisible(this, file);
    }

    public int showOpenDialog(Component component) throws HeadlessException {
        setDialogType(0);
        return showDialog(component, null);
    }

    public int showSaveDialog(Component component) throws HeadlessException {
        setDialogType(1);
        return showDialog(component, null);
    }

    public int showDialog(Component component, String str) throws HeadlessException {
        if (this.dialog != null) {
            return -1;
        }
        if (str != null) {
            setApproveButtonText(str);
            setDialogType(2);
        }
        this.dialog = createDialog(component);
        this.dialog.addWindowListener(new WindowAdapter() { // from class: javax.swing.JFileChooser.2
            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosing(WindowEvent windowEvent) {
                JFileChooser.this.returnValue = 1;
            }
        });
        this.returnValue = -1;
        rescanCurrentDirectory();
        this.dialog.show();
        firePropertyChange("JFileChooserDialogIsClosingProperty", this.dialog, (Object) null);
        this.dialog.getContentPane().removeAll();
        this.dialog.dispose();
        this.dialog = null;
        return this.returnValue;
    }

    protected JDialog createDialog(Component component) throws HeadlessException {
        JDialog jDialog;
        String dialogTitle = getUI().getDialogTitle(this);
        putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, dialogTitle);
        Window windowForComponent = JOptionPane.getWindowForComponent(component);
        if (windowForComponent instanceof Frame) {
            jDialog = new JDialog((Frame) windowForComponent, dialogTitle, true);
        } else {
            jDialog = new JDialog((Dialog) windowForComponent, dialogTitle, true);
        }
        jDialog.setComponentOrientation(getComponentOrientation());
        Container contentPane = jDialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
        if (JDialog.isDefaultLookAndFeelDecorated() && UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
            jDialog.getRootPane().setWindowDecorationStyle(6);
        }
        jDialog.pack();
        jDialog.setLocationRelativeTo(component);
        return jDialog;
    }

    public boolean getControlButtonsAreShown() {
        return this.controlsShown;
    }

    public void setControlButtonsAreShown(boolean z2) {
        if (this.controlsShown == z2) {
            return;
        }
        boolean z3 = this.controlsShown;
        this.controlsShown = z2;
        firePropertyChange(CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY, z3, this.controlsShown);
    }

    public int getDialogType() {
        return this.dialogType;
    }

    public void setDialogType(int i2) {
        if (this.dialogType == i2) {
            return;
        }
        if (i2 != 0 && i2 != 1 && i2 != 2) {
            throw new IllegalArgumentException("Incorrect Dialog Type: " + i2);
        }
        int i3 = this.dialogType;
        this.dialogType = i2;
        if (i2 == 0 || i2 == 1) {
            setApproveButtonText(null);
        }
        firePropertyChange(DIALOG_TYPE_CHANGED_PROPERTY, i3, i2);
    }

    public void setDialogTitle(String str) {
        String str2 = this.dialogTitle;
        this.dialogTitle = str;
        if (this.dialog != null) {
            this.dialog.setTitle(str);
        }
        firePropertyChange(DIALOG_TITLE_CHANGED_PROPERTY, str2, str);
    }

    public String getDialogTitle() {
        return this.dialogTitle;
    }

    public void setApproveButtonToolTipText(String str) {
        if (this.approveButtonToolTipText == str) {
            return;
        }
        String str2 = this.approveButtonToolTipText;
        this.approveButtonToolTipText = str;
        firePropertyChange(APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY, str2, this.approveButtonToolTipText);
    }

    public String getApproveButtonToolTipText() {
        return this.approveButtonToolTipText;
    }

    public int getApproveButtonMnemonic() {
        return this.approveButtonMnemonic;
    }

    public void setApproveButtonMnemonic(int i2) {
        if (this.approveButtonMnemonic == i2) {
            return;
        }
        int i3 = this.approveButtonMnemonic;
        this.approveButtonMnemonic = i2;
        firePropertyChange(APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY, i3, this.approveButtonMnemonic);
    }

    public void setApproveButtonMnemonic(char c2) {
        int i2 = c2;
        if (i2 >= 97 && i2 <= 122) {
            i2 -= 32;
        }
        setApproveButtonMnemonic(i2);
    }

    public void setApproveButtonText(String str) {
        if (this.approveButtonText == str) {
            return;
        }
        String str2 = this.approveButtonText;
        this.approveButtonText = str;
        firePropertyChange(APPROVE_BUTTON_TEXT_CHANGED_PROPERTY, str2, str);
    }

    public String getApproveButtonText() {
        return this.approveButtonText;
    }

    public FileFilter[] getChoosableFileFilters() {
        FileFilter[] fileFilterArr = new FileFilter[this.filters.size()];
        this.filters.copyInto(fileFilterArr);
        return fileFilterArr;
    }

    public void addChoosableFileFilter(FileFilter fileFilter) {
        if (fileFilter != null && !this.filters.contains(fileFilter)) {
            FileFilter[] choosableFileFilters = getChoosableFileFilters();
            this.filters.addElement(fileFilter);
            firePropertyChange(CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY, choosableFileFilters, getChoosableFileFilters());
            if (this.fileFilter == null && this.filters.size() == 1) {
                setFileFilter(fileFilter);
            }
        }
    }

    public boolean removeChoosableFileFilter(FileFilter fileFilter) {
        int iIndexOf = this.filters.indexOf(fileFilter);
        if (iIndexOf >= 0) {
            if (getFileFilter() == fileFilter) {
                FileFilter acceptAllFileFilter = getAcceptAllFileFilter();
                if (isAcceptAllFileFilterUsed() && acceptAllFileFilter != fileFilter) {
                    setFileFilter(acceptAllFileFilter);
                } else if (iIndexOf > 0) {
                    setFileFilter(this.filters.get(0));
                } else if (this.filters.size() > 1) {
                    setFileFilter(this.filters.get(1));
                } else {
                    setFileFilter(null);
                }
            }
            FileFilter[] choosableFileFilters = getChoosableFileFilters();
            this.filters.removeElement(fileFilter);
            firePropertyChange(CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY, choosableFileFilters, getChoosableFileFilters());
            return true;
        }
        return false;
    }

    public void resetChoosableFileFilters() {
        FileFilter[] choosableFileFilters = getChoosableFileFilters();
        setFileFilter(null);
        this.filters.removeAllElements();
        if (isAcceptAllFileFilterUsed()) {
            addChoosableFileFilter(getAcceptAllFileFilter());
        }
        firePropertyChange(CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY, choosableFileFilters, getChoosableFileFilters());
    }

    public FileFilter getAcceptAllFileFilter() {
        FileFilter acceptAllFileFilter = null;
        if (getUI() != null) {
            acceptAllFileFilter = getUI().getAcceptAllFileFilter(this);
        }
        return acceptAllFileFilter;
    }

    public boolean isAcceptAllFileFilterUsed() {
        return this.useAcceptAllFileFilter;
    }

    public void setAcceptAllFileFilterUsed(boolean z2) {
        boolean z3 = this.useAcceptAllFileFilter;
        this.useAcceptAllFileFilter = z2;
        if (!z2) {
            removeChoosableFileFilter(getAcceptAllFileFilter());
        } else {
            removeChoosableFileFilter(getAcceptAllFileFilter());
            addChoosableFileFilter(getAcceptAllFileFilter());
        }
        firePropertyChange(ACCEPT_ALL_FILE_FILTER_USED_CHANGED_PROPERTY, z3, this.useAcceptAllFileFilter);
    }

    public JComponent getAccessory() {
        return this.accessory;
    }

    public void setAccessory(JComponent jComponent) {
        JComponent jComponent2 = this.accessory;
        this.accessory = jComponent;
        firePropertyChange(ACCESSORY_CHANGED_PROPERTY, jComponent2, this.accessory);
    }

    public void setFileSelectionMode(int i2) {
        if (this.fileSelectionMode == i2) {
            return;
        }
        if (i2 == 0 || i2 == 1 || i2 == 2) {
            int i3 = this.fileSelectionMode;
            this.fileSelectionMode = i2;
            firePropertyChange(FILE_SELECTION_MODE_CHANGED_PROPERTY, i3, this.fileSelectionMode);
            return;
        }
        throw new IllegalArgumentException("Incorrect Mode for file selection: " + i2);
    }

    public int getFileSelectionMode() {
        return this.fileSelectionMode;
    }

    public boolean isFileSelectionEnabled() {
        return this.fileSelectionMode == 0 || this.fileSelectionMode == 2;
    }

    public boolean isDirectorySelectionEnabled() {
        return this.fileSelectionMode == 1 || this.fileSelectionMode == 2;
    }

    public void setMultiSelectionEnabled(boolean z2) {
        if (this.multiSelectionEnabled == z2) {
            return;
        }
        boolean z3 = this.multiSelectionEnabled;
        this.multiSelectionEnabled = z2;
        firePropertyChange(MULTI_SELECTION_ENABLED_CHANGED_PROPERTY, z3, this.multiSelectionEnabled);
    }

    public boolean isMultiSelectionEnabled() {
        return this.multiSelectionEnabled;
    }

    public boolean isFileHidingEnabled() {
        return this.useFileHiding;
    }

    public void setFileHidingEnabled(boolean z2) {
        if (this.showFilesListener != null) {
            Toolkit.getDefaultToolkit().removePropertyChangeListener(SHOW_HIDDEN_PROP, this.showFilesListener);
            this.showFilesListener = null;
        }
        boolean z3 = this.useFileHiding;
        this.useFileHiding = z2;
        firePropertyChange(FILE_HIDING_CHANGED_PROPERTY, z3, this.useFileHiding);
    }

    public void setFileFilter(FileFilter fileFilter) {
        FileFilter fileFilter2 = this.fileFilter;
        this.fileFilter = fileFilter;
        if (fileFilter != null) {
            if (isMultiSelectionEnabled() && this.selectedFiles != null && this.selectedFiles.length > 0) {
                Vector vector = new Vector();
                boolean z2 = false;
                for (File file : this.selectedFiles) {
                    if (fileFilter.accept(file)) {
                        vector.add(file);
                    } else {
                        z2 = true;
                    }
                }
                if (z2) {
                    setSelectedFiles(vector.size() == 0 ? null : (File[]) vector.toArray(new File[vector.size()]));
                }
            } else if (this.selectedFile != null && !fileFilter.accept(this.selectedFile)) {
                setSelectedFile(null);
            }
        }
        firePropertyChange(FILE_FILTER_CHANGED_PROPERTY, fileFilter2, this.fileFilter);
    }

    public FileFilter getFileFilter() {
        return this.fileFilter;
    }

    public void setFileView(FileView fileView) {
        FileView fileView2 = this.fileView;
        this.fileView = fileView;
        firePropertyChange(FILE_VIEW_CHANGED_PROPERTY, fileView2, fileView);
    }

    public FileView getFileView() {
        return this.fileView;
    }

    public String getName(File file) {
        String name = null;
        if (file != null) {
            if (getFileView() != null) {
                name = getFileView().getName(file);
            }
            FileView fileView = getUI().getFileView(this);
            if (name == null && fileView != null) {
                name = fileView.getName(file);
            }
        }
        return name;
    }

    public String getDescription(File file) {
        String description = null;
        if (file != null) {
            if (getFileView() != null) {
                description = getFileView().getDescription(file);
            }
            FileView fileView = getUI().getFileView(this);
            if (description == null && fileView != null) {
                description = fileView.getDescription(file);
            }
        }
        return description;
    }

    public String getTypeDescription(File file) {
        String typeDescription = null;
        if (file != null) {
            if (getFileView() != null) {
                typeDescription = getFileView().getTypeDescription(file);
            }
            FileView fileView = getUI().getFileView(this);
            if (typeDescription == null && fileView != null) {
                typeDescription = fileView.getTypeDescription(file);
            }
        }
        return typeDescription;
    }

    public Icon getIcon(File file) {
        Icon icon = null;
        if (file != null) {
            if (getFileView() != null) {
                icon = getFileView().getIcon(file);
            }
            FileView fileView = getUI().getFileView(this);
            if (icon == null && fileView != null) {
                icon = fileView.getIcon(file);
            }
        }
        return icon;
    }

    public boolean isTraversable(File file) {
        Boolean boolIsTraversable = null;
        if (file != null) {
            if (getFileView() != null) {
                boolIsTraversable = getFileView().isTraversable(file);
            }
            FileView fileView = getUI().getFileView(this);
            if (boolIsTraversable == null && fileView != null) {
                boolIsTraversable = fileView.isTraversable(file);
            }
            if (boolIsTraversable == null) {
                boolIsTraversable = getFileSystemView().isTraversable(file);
            }
        }
        return boolIsTraversable != null && boolIsTraversable.booleanValue();
    }

    public boolean accept(File file) {
        boolean zAccept = true;
        if (file != null && this.fileFilter != null) {
            zAccept = this.fileFilter.accept(file);
        }
        return zAccept;
    }

    public void setFileSystemView(FileSystemView fileSystemView) {
        FileSystemView fileSystemView2 = this.fileSystemView;
        this.fileSystemView = fileSystemView;
        firePropertyChange(FILE_SYSTEM_VIEW_CHANGED_PROPERTY, fileSystemView2, this.fileSystemView);
    }

    public FileSystemView getFileSystemView() {
        return this.fileSystemView;
    }

    public void approveSelection() {
        this.returnValue = 0;
        if (this.dialog != null) {
            this.dialog.setVisible(false);
        }
        fireActionPerformed(APPROVE_SELECTION);
    }

    public void cancelSelection() {
        this.returnValue = 1;
        if (this.dialog != null) {
            this.dialog.setVisible(false);
        }
        fireActionPerformed(CANCEL_SELECTION);
    }

    public void addActionListener(ActionListener actionListener) {
        this.listenerList.add(ActionListener.class, actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        this.listenerList.remove(ActionListener.class, actionListener);
    }

    public ActionListener[] getActionListeners() {
        return (ActionListener[]) this.listenerList.getListeners(ActionListener.class);
    }

    protected void fireActionPerformed(String str) {
        Object[] listenerList = this.listenerList.getListenerList();
        long mostRecentEventTime = EventQueue.getMostRecentEventTime();
        int modifiers = 0;
        AWTEvent currentEvent = EventQueue.getCurrentEvent();
        if (currentEvent instanceof InputEvent) {
            modifiers = ((InputEvent) currentEvent).getModifiers();
        } else if (currentEvent instanceof ActionEvent) {
            modifiers = ((ActionEvent) currentEvent).getModifiers();
        }
        ActionEvent actionEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ActionListener.class) {
                if (actionEvent == null) {
                    actionEvent = new ActionEvent(this, 1001, str, mostRecentEventTime, modifiers);
                }
                ((ActionListener) listenerList[length + 1]).actionPerformed(actionEvent);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/JFileChooser$WeakPCL.class */
    private static class WeakPCL implements PropertyChangeListener {
        WeakReference<JFileChooser> jfcRef;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !JFileChooser.class.desiredAssertionStatus();
        }

        public WeakPCL(JFileChooser jFileChooser) {
            this.jfcRef = new WeakReference<>(jFileChooser);
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (!$assertionsDisabled && !propertyChangeEvent.getPropertyName().equals(JFileChooser.SHOW_HIDDEN_PROP)) {
                throw new AssertionError();
            }
            JFileChooser jFileChooser = this.jfcRef.get();
            if (jFileChooser != null) {
                boolean z2 = jFileChooser.useFileHiding;
                jFileChooser.useFileHiding = !((Boolean) propertyChangeEvent.getNewValue()).booleanValue();
                jFileChooser.firePropertyChange(JFileChooser.FILE_HIDING_CHANGED_PROPERTY, z2, jFileChooser.useFileHiding);
                return;
            }
            Toolkit.getDefaultToolkit().removePropertyChangeListener(JFileChooser.SHOW_HIDDEN_PROP, this);
        }
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        if (isAcceptAllFileFilterUsed()) {
            removeChoosableFileFilter(getAcceptAllFileFilter());
        }
        FileChooserUI fileChooserUI = (FileChooserUI) UIManager.getUI(this);
        if (this.fileSystemView == null) {
            setFileSystemView(FileSystemView.getFileSystemView());
        }
        setUI(fileChooserUI);
        if (isAcceptAllFileFilterUsed()) {
            addChoosableFileFilter(getAcceptAllFileFilter());
        }
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public FileChooserUI getUI() {
        return (FileChooserUI) this.ui;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        installShowFilesListener();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        FileSystemView fileSystemView = null;
        if (isAcceptAllFileFilterUsed()) {
            removeChoosableFileFilter(getAcceptAllFileFilter());
        }
        if (this.fileSystemView.equals(FileSystemView.getFileSystemView())) {
            fileSystemView = this.fileSystemView;
            this.fileSystemView = null;
        }
        objectOutputStream.defaultWriteObject();
        if (fileSystemView != null) {
            this.fileSystemView = fileSystemView;
        }
        if (isAcceptAllFileFilterUsed()) {
            addChoosableFileFilter(getAcceptAllFileFilter());
        }
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str;
        String str2;
        String str3;
        String str4 = this.approveButtonText != null ? this.approveButtonText : "";
        String str5 = this.dialogTitle != null ? this.dialogTitle : "";
        if (this.dialogType == 0) {
            str = "OPEN_DIALOG";
        } else if (this.dialogType == 1) {
            str = "SAVE_DIALOG";
        } else if (this.dialogType == 2) {
            str = "CUSTOM_DIALOG";
        } else {
            str = "";
        }
        if (this.returnValue == 1) {
            str2 = "CANCEL_OPTION";
        } else if (this.returnValue == 0) {
            str2 = "APPROVE_OPTION";
        } else if (this.returnValue == -1) {
            str2 = "ERROR_OPTION";
        } else {
            str2 = "";
        }
        String str6 = this.useFileHiding ? "true" : "false";
        if (this.fileSelectionMode == 0) {
            str3 = "FILES_ONLY";
        } else if (this.fileSelectionMode == 1) {
            str3 = "DIRECTORIES_ONLY";
        } else if (this.fileSelectionMode == 2) {
            str3 = "FILES_AND_DIRECTORIES";
        } else {
            str3 = "";
        }
        return super.paramString() + ",approveButtonText=" + str4 + ",currentDirectory=" + (this.currentDirectory != null ? this.currentDirectory.toString() : "") + ",dialogTitle=" + str5 + ",dialogType=" + str + ",fileSelectionMode=" + str3 + ",returnValue=" + str2 + ",selectedFile=" + (this.selectedFile != null ? this.selectedFile.toString() : "") + ",useFileHiding=" + str6;
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJFileChooser();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JFileChooser$AccessibleJFileChooser.class */
    protected class AccessibleJFileChooser extends JComponent.AccessibleJComponent {
        protected AccessibleJFileChooser() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.FILE_CHOOSER;
        }
    }
}
