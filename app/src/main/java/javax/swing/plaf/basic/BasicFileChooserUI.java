package javax.swing.plaf.basic;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FileChooserUI;
import javax.swing.plaf.UIResource;
import sun.awt.shell.ShellFolder;
import sun.swing.DefaultLookup;
import sun.swing.FilePane;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI.class */
public class BasicFileChooserUI extends FileChooserUI {
    private Action newFolderAction;
    private boolean usesSingleFilePane;
    private boolean readOnly;
    private Handler handler;
    private static final TransferHandler defaultTransferHandler = new FileTransferHandler();
    protected Icon directoryIcon = null;
    protected Icon fileIcon = null;
    protected Icon computerIcon = null;
    protected Icon hardDriveIcon = null;
    protected Icon floppyDriveIcon = null;
    protected Icon newFolderIcon = null;
    protected Icon upFolderIcon = null;
    protected Icon homeFolderIcon = null;
    protected Icon listViewIcon = null;
    protected Icon detailsViewIcon = null;
    protected Icon viewMenuIcon = null;
    protected int saveButtonMnemonic = 0;
    protected int openButtonMnemonic = 0;
    protected int cancelButtonMnemonic = 0;
    protected int updateButtonMnemonic = 0;
    protected int helpButtonMnemonic = 0;
    protected int directoryOpenButtonMnemonic = 0;
    protected String saveButtonText = null;
    protected String openButtonText = null;
    protected String cancelButtonText = null;
    protected String updateButtonText = null;
    protected String helpButtonText = null;
    protected String directoryOpenButtonText = null;
    private String openDialogTitleText = null;
    private String saveDialogTitleText = null;
    protected String saveButtonToolTipText = null;
    protected String openButtonToolTipText = null;
    protected String cancelButtonToolTipText = null;
    protected String updateButtonToolTipText = null;
    protected String helpButtonToolTipText = null;
    protected String directoryOpenButtonToolTipText = null;
    private Action approveSelectionAction = new ApproveSelectionAction();
    private Action cancelSelectionAction = new CancelSelectionAction();
    private Action updateAction = new UpdateAction();
    private Action goHomeAction = new GoHomeAction();
    private Action changeToParentDirectoryAction = new ChangeToParentDirectoryAction();
    private String newFolderErrorSeparator = null;
    private String newFolderErrorText = null;
    private String newFolderParentDoesntExistTitleText = null;
    private String newFolderParentDoesntExistText = null;
    private String fileDescriptionText = null;
    private String directoryDescriptionText = null;
    private JFileChooser filechooser = null;
    private boolean directorySelected = false;
    private File directory = null;
    private PropertyChangeListener propertyChangeListener = null;
    private AcceptAllFileFilter acceptAllFileFilter = new AcceptAllFileFilter();
    private FileFilter actualFileFilter = null;
    private GlobFilter globFilter = null;
    private BasicDirectoryModel model = null;
    private BasicFileView fileView = new BasicFileView();
    private JPanel accessoryPanel = null;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicFileChooserUI((JFileChooser) jComponent);
    }

    public BasicFileChooserUI(JFileChooser jFileChooser) {
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.accessoryPanel = new JPanel(new BorderLayout());
        this.filechooser = (JFileChooser) jComponent;
        createModel();
        clearIconCache();
        installDefaults(this.filechooser);
        installComponents(this.filechooser);
        installListeners(this.filechooser);
        this.filechooser.applyComponentOrientation(this.filechooser.getComponentOrientation());
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallListeners(this.filechooser);
        uninstallComponents(this.filechooser);
        uninstallDefaults(this.filechooser);
        if (this.accessoryPanel != null) {
            this.accessoryPanel.removeAll();
        }
        this.accessoryPanel = null;
        getFileChooser().removeAll();
        this.handler = null;
    }

    public void installComponents(JFileChooser jFileChooser) {
    }

    public void uninstallComponents(JFileChooser jFileChooser) {
    }

    protected void installListeners(JFileChooser jFileChooser) {
        this.propertyChangeListener = createPropertyChangeListener(jFileChooser);
        if (this.propertyChangeListener != null) {
            jFileChooser.addPropertyChangeListener(this.propertyChangeListener);
        }
        jFileChooser.addPropertyChangeListener(getModel());
        SwingUtilities.replaceUIInputMap(jFileChooser, 1, getInputMap(1));
        SwingUtilities.replaceUIActionMap(jFileChooser, getActionMap());
    }

    InputMap getInputMap(int i2) {
        if (i2 == 1) {
            return (InputMap) DefaultLookup.get(getFileChooser(), this, "FileChooser.ancestorInputMap");
        }
        return null;
    }

    ActionMap getActionMap() {
        return createActionMap();
    }

    ActionMap createActionMap() {
        ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
        UIAction uIAction = new UIAction(FilePane.ACTION_REFRESH) { // from class: javax.swing.plaf.basic.BasicFileChooserUI.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                BasicFileChooserUI.this.getFileChooser().rescanCurrentDirectory();
            }
        };
        actionMapUIResource.put(FilePane.ACTION_APPROVE_SELECTION, getApproveSelectionAction());
        actionMapUIResource.put(FilePane.ACTION_CANCEL, getCancelSelectionAction());
        actionMapUIResource.put(FilePane.ACTION_REFRESH, uIAction);
        actionMapUIResource.put(FilePane.ACTION_CHANGE_TO_PARENT_DIRECTORY, getChangeToParentDirectoryAction());
        return actionMapUIResource;
    }

    protected void uninstallListeners(JFileChooser jFileChooser) {
        if (this.propertyChangeListener != null) {
            jFileChooser.removePropertyChangeListener(this.propertyChangeListener);
        }
        jFileChooser.removePropertyChangeListener(getModel());
        SwingUtilities.replaceUIInputMap(jFileChooser, 1, null);
        SwingUtilities.replaceUIActionMap(jFileChooser, null);
    }

    protected void installDefaults(JFileChooser jFileChooser) {
        installIcons(jFileChooser);
        installStrings(jFileChooser);
        this.usesSingleFilePane = UIManager.getBoolean("FileChooser.usesSingleFilePane");
        this.readOnly = UIManager.getBoolean("FileChooser.readOnly");
        TransferHandler transferHandler = jFileChooser.getTransferHandler();
        if (transferHandler == null || (transferHandler instanceof UIResource)) {
            jFileChooser.setTransferHandler(defaultTransferHandler);
        }
        LookAndFeel.installProperty(jFileChooser, "opaque", Boolean.FALSE);
    }

    protected void installIcons(JFileChooser jFileChooser) {
        this.directoryIcon = UIManager.getIcon("FileView.directoryIcon");
        this.fileIcon = UIManager.getIcon("FileView.fileIcon");
        this.computerIcon = UIManager.getIcon("FileView.computerIcon");
        this.hardDriveIcon = UIManager.getIcon("FileView.hardDriveIcon");
        this.floppyDriveIcon = UIManager.getIcon("FileView.floppyDriveIcon");
        this.newFolderIcon = UIManager.getIcon("FileChooser.newFolderIcon");
        this.upFolderIcon = UIManager.getIcon("FileChooser.upFolderIcon");
        this.homeFolderIcon = UIManager.getIcon("FileChooser.homeFolderIcon");
        this.detailsViewIcon = UIManager.getIcon("FileChooser.detailsViewIcon");
        this.listViewIcon = UIManager.getIcon("FileChooser.listViewIcon");
        this.viewMenuIcon = UIManager.getIcon("FileChooser.viewMenuIcon");
    }

    protected void installStrings(JFileChooser jFileChooser) {
        Locale locale = jFileChooser.getLocale();
        this.newFolderErrorText = UIManager.getString("FileChooser.newFolderErrorText", locale);
        this.newFolderErrorSeparator = UIManager.getString("FileChooser.newFolderErrorSeparator", locale);
        this.newFolderParentDoesntExistTitleText = UIManager.getString("FileChooser.newFolderParentDoesntExistTitleText", locale);
        this.newFolderParentDoesntExistText = UIManager.getString("FileChooser.newFolderParentDoesntExistText", locale);
        this.fileDescriptionText = UIManager.getString("FileChooser.fileDescriptionText", locale);
        this.directoryDescriptionText = UIManager.getString("FileChooser.directoryDescriptionText", locale);
        this.saveButtonText = UIManager.getString("FileChooser.saveButtonText", locale);
        this.openButtonText = UIManager.getString("FileChooser.openButtonText", locale);
        this.saveDialogTitleText = UIManager.getString("FileChooser.saveDialogTitleText", locale);
        this.openDialogTitleText = UIManager.getString("FileChooser.openDialogTitleText", locale);
        this.cancelButtonText = UIManager.getString("FileChooser.cancelButtonText", locale);
        this.updateButtonText = UIManager.getString("FileChooser.updateButtonText", locale);
        this.helpButtonText = UIManager.getString("FileChooser.helpButtonText", locale);
        this.directoryOpenButtonText = UIManager.getString("FileChooser.directoryOpenButtonText", locale);
        this.saveButtonMnemonic = getMnemonic("FileChooser.saveButtonMnemonic", locale);
        this.openButtonMnemonic = getMnemonic("FileChooser.openButtonMnemonic", locale);
        this.cancelButtonMnemonic = getMnemonic("FileChooser.cancelButtonMnemonic", locale);
        this.updateButtonMnemonic = getMnemonic("FileChooser.updateButtonMnemonic", locale);
        this.helpButtonMnemonic = getMnemonic("FileChooser.helpButtonMnemonic", locale);
        this.directoryOpenButtonMnemonic = getMnemonic("FileChooser.directoryOpenButtonMnemonic", locale);
        this.saveButtonToolTipText = UIManager.getString("FileChooser.saveButtonToolTipText", locale);
        this.openButtonToolTipText = UIManager.getString("FileChooser.openButtonToolTipText", locale);
        this.cancelButtonToolTipText = UIManager.getString("FileChooser.cancelButtonToolTipText", locale);
        this.updateButtonToolTipText = UIManager.getString("FileChooser.updateButtonToolTipText", locale);
        this.helpButtonToolTipText = UIManager.getString("FileChooser.helpButtonToolTipText", locale);
        this.directoryOpenButtonToolTipText = UIManager.getString("FileChooser.directoryOpenButtonToolTipText", locale);
    }

    protected void uninstallDefaults(JFileChooser jFileChooser) {
        uninstallIcons(jFileChooser);
        uninstallStrings(jFileChooser);
        if (jFileChooser.getTransferHandler() instanceof UIResource) {
            jFileChooser.setTransferHandler(null);
        }
    }

    protected void uninstallIcons(JFileChooser jFileChooser) {
        this.directoryIcon = null;
        this.fileIcon = null;
        this.computerIcon = null;
        this.hardDriveIcon = null;
        this.floppyDriveIcon = null;
        this.newFolderIcon = null;
        this.upFolderIcon = null;
        this.homeFolderIcon = null;
        this.detailsViewIcon = null;
        this.listViewIcon = null;
        this.viewMenuIcon = null;
    }

    protected void uninstallStrings(JFileChooser jFileChooser) {
        this.saveButtonText = null;
        this.openButtonText = null;
        this.cancelButtonText = null;
        this.updateButtonText = null;
        this.helpButtonText = null;
        this.directoryOpenButtonText = null;
        this.saveButtonToolTipText = null;
        this.openButtonToolTipText = null;
        this.cancelButtonToolTipText = null;
        this.updateButtonToolTipText = null;
        this.helpButtonToolTipText = null;
        this.directoryOpenButtonToolTipText = null;
    }

    protected void createModel() {
        if (this.model != null) {
            this.model.invalidateFileCache();
        }
        this.model = new BasicDirectoryModel(getFileChooser());
    }

    public BasicDirectoryModel getModel() {
        return this.model;
    }

    public PropertyChangeListener createPropertyChangeListener(JFileChooser jFileChooser) {
        return null;
    }

    public String getFileName() {
        return null;
    }

    public String getDirectoryName() {
        return null;
    }

    public void setFileName(String str) {
    }

    public void setDirectoryName(String str) {
    }

    @Override // javax.swing.plaf.FileChooserUI
    public void rescanCurrentDirectory(JFileChooser jFileChooser) {
    }

    @Override // javax.swing.plaf.FileChooserUI
    public void ensureFileIsVisible(JFileChooser jFileChooser, File file) {
    }

    public JFileChooser getFileChooser() {
        return this.filechooser;
    }

    public JPanel getAccessoryPanel() {
        return this.accessoryPanel;
    }

    protected JButton getApproveButton(JFileChooser jFileChooser) {
        return null;
    }

    @Override // javax.swing.plaf.FileChooserUI
    public JButton getDefaultButton(JFileChooser jFileChooser) {
        return getApproveButton(jFileChooser);
    }

    public String getApproveButtonToolTipText(JFileChooser jFileChooser) {
        String approveButtonToolTipText = jFileChooser.getApproveButtonToolTipText();
        if (approveButtonToolTipText != null) {
            return approveButtonToolTipText;
        }
        if (jFileChooser.getDialogType() == 0) {
            return this.openButtonToolTipText;
        }
        if (jFileChooser.getDialogType() == 1) {
            return this.saveButtonToolTipText;
        }
        return null;
    }

    public void clearIconCache() {
        this.fileView.clearIconCache();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected MouseListener createDoubleClickListener(JFileChooser jFileChooser, JList jList) {
        return new Handler(jList);
    }

    public ListSelectionListener createListSelectionListener(JFileChooser jFileChooser) {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$Handler.class */
    private class Handler implements MouseListener, ListSelectionListener {
        JList list;

        Handler() {
        }

        Handler(JList jList) {
            this.list = jList;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            int iLoc2IndexFileList;
            if (this.list != null && SwingUtilities.isLeftMouseButton(mouseEvent) && mouseEvent.getClickCount() % 2 == 0 && (iLoc2IndexFileList = SwingUtilities2.loc2IndexFileList(this.list, mouseEvent.getPoint())) >= 0) {
                File normalizedFile = (File) this.list.getModel().getElementAt(iLoc2IndexFileList);
                try {
                    normalizedFile = ShellFolder.getNormalizedFile(normalizedFile);
                } catch (IOException e2) {
                }
                if (BasicFileChooserUI.this.getFileChooser().isTraversable(normalizedFile)) {
                    this.list.clearSelection();
                    BasicFileChooserUI.this.changeDirectory(normalizedFile);
                } else {
                    BasicFileChooserUI.this.getFileChooser().approveSelection();
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            if (this.list != null) {
                TransferHandler transferHandler = BasicFileChooserUI.this.getFileChooser().getTransferHandler();
                if (transferHandler != this.list.getTransferHandler()) {
                    this.list.setTransferHandler(transferHandler);
                }
                if (BasicFileChooserUI.this.getFileChooser().getDragEnabled() != this.list.getDragEnabled()) {
                    this.list.setDragEnabled(BasicFileChooserUI.this.getFileChooser().getDragEnabled());
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                JFileChooser fileChooser = BasicFileChooserUI.this.getFileChooser();
                FileSystemView fileSystemView = fileChooser.getFileSystemView();
                JList jList = (JList) listSelectionEvent.getSource();
                boolean z2 = BasicFileChooserUI.this.usesSingleFilePane && fileChooser.getFileSelectionMode() == 0;
                if (fileChooser.isMultiSelectionEnabled()) {
                    File[] fileArr = null;
                    Object[] selectedValues = jList.getSelectedValues();
                    if (selectedValues != null) {
                        if (selectedValues.length == 1 && ((File) selectedValues[0]).isDirectory() && fileChooser.isTraversable((File) selectedValues[0]) && (z2 || !fileSystemView.isFileSystem((File) selectedValues[0]))) {
                            BasicFileChooserUI.this.setDirectorySelected(true);
                            BasicFileChooserUI.this.setDirectory((File) selectedValues[0]);
                        } else {
                            ArrayList arrayList = new ArrayList(selectedValues.length);
                            for (Object obj : selectedValues) {
                                File file = (File) obj;
                                boolean zIsDirectory = file.isDirectory();
                                if ((fileChooser.isFileSelectionEnabled() && !zIsDirectory) || (fileChooser.isDirectorySelectionEnabled() && fileSystemView.isFileSystem(file) && zIsDirectory)) {
                                    arrayList.add(file);
                                }
                            }
                            if (arrayList.size() > 0) {
                                fileArr = (File[]) arrayList.toArray(new File[arrayList.size()]);
                            }
                            BasicFileChooserUI.this.setDirectorySelected(false);
                        }
                    }
                    fileChooser.setSelectedFiles(fileArr);
                    return;
                }
                File file2 = (File) jList.getSelectedValue();
                if (file2 != null && file2.isDirectory() && fileChooser.isTraversable(file2) && (z2 || !fileSystemView.isFileSystem(file2))) {
                    BasicFileChooserUI.this.setDirectorySelected(true);
                    BasicFileChooserUI.this.setDirectory(file2);
                    if (BasicFileChooserUI.this.usesSingleFilePane) {
                        fileChooser.setSelectedFile(null);
                        return;
                    }
                    return;
                }
                BasicFileChooserUI.this.setDirectorySelected(false);
                if (file2 != null) {
                    fileChooser.setSelectedFile(file2);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$DoubleClickListener.class */
    protected class DoubleClickListener extends MouseAdapter {
        Handler handler;

        public DoubleClickListener(JList jList) {
            this.handler = BasicFileChooserUI.this.new Handler(jList);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            this.handler.mouseEntered(mouseEvent);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            this.handler.mouseClicked(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$SelectionListener.class */
    protected class SelectionListener implements ListSelectionListener {
        protected SelectionListener() {
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            BasicFileChooserUI.this.getHandler().valueChanged(listSelectionEvent);
        }
    }

    protected boolean isDirectorySelected() {
        return this.directorySelected;
    }

    protected void setDirectorySelected(boolean z2) {
        this.directorySelected = z2;
    }

    protected File getDirectory() {
        return this.directory;
    }

    protected void setDirectory(File file) {
        this.directory = file;
    }

    private int getMnemonic(String str, Locale locale) {
        return SwingUtilities2.getUIDefaultsInt(str, locale);
    }

    @Override // javax.swing.plaf.FileChooserUI
    public FileFilter getAcceptAllFileFilter(JFileChooser jFileChooser) {
        return this.acceptAllFileFilter;
    }

    @Override // javax.swing.plaf.FileChooserUI
    public FileView getFileView(JFileChooser jFileChooser) {
        return this.fileView;
    }

    @Override // javax.swing.plaf.FileChooserUI
    public String getDialogTitle(JFileChooser jFileChooser) {
        String dialogTitle = jFileChooser.getDialogTitle();
        if (dialogTitle != null) {
            return dialogTitle;
        }
        if (jFileChooser.getDialogType() == 0) {
            return this.openDialogTitleText;
        }
        if (jFileChooser.getDialogType() == 1) {
            return this.saveDialogTitleText;
        }
        return getApproveButtonText(jFileChooser);
    }

    public int getApproveButtonMnemonic(JFileChooser jFileChooser) {
        int approveButtonMnemonic = jFileChooser.getApproveButtonMnemonic();
        if (approveButtonMnemonic > 0) {
            return approveButtonMnemonic;
        }
        if (jFileChooser.getDialogType() == 0) {
            return this.openButtonMnemonic;
        }
        if (jFileChooser.getDialogType() == 1) {
            return this.saveButtonMnemonic;
        }
        return approveButtonMnemonic;
    }

    @Override // javax.swing.plaf.FileChooserUI
    public String getApproveButtonText(JFileChooser jFileChooser) {
        String approveButtonText = jFileChooser.getApproveButtonText();
        if (approveButtonText != null) {
            return approveButtonText;
        }
        if (jFileChooser.getDialogType() == 0) {
            return this.openButtonText;
        }
        if (jFileChooser.getDialogType() == 1) {
            return this.saveButtonText;
        }
        return null;
    }

    public Action getNewFolderAction() {
        if (this.newFolderAction == null) {
            this.newFolderAction = new NewFolderAction();
            if (this.readOnly) {
                this.newFolderAction.setEnabled(false);
            }
        }
        return this.newFolderAction;
    }

    public Action getGoHomeAction() {
        return this.goHomeAction;
    }

    public Action getChangeToParentDirectoryAction() {
        return this.changeToParentDirectoryAction;
    }

    public Action getApproveSelectionAction() {
        return this.approveSelectionAction;
    }

    public Action getCancelSelectionAction() {
        return this.cancelSelectionAction;
    }

    public Action getUpdateAction() {
        return this.updateAction;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$NewFolderAction.class */
    public class NewFolderAction extends AbstractAction {
        protected NewFolderAction() {
            super(FilePane.ACTION_NEW_FOLDER);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
            if (BasicFileChooserUI.this.readOnly) {
                return;
            }
            JFileChooser fileChooser = BasicFileChooserUI.this.getFileChooser();
            File currentDirectory = fileChooser.getCurrentDirectory();
            if (!currentDirectory.exists()) {
                JOptionPane.showMessageDialog(fileChooser, BasicFileChooserUI.this.newFolderParentDoesntExistText, BasicFileChooserUI.this.newFolderParentDoesntExistTitleText, 2);
                return;
            }
            try {
                File fileCreateNewFolder = fileChooser.getFileSystemView().createNewFolder(currentDirectory);
                if (fileChooser.isMultiSelectionEnabled()) {
                    fileChooser.setSelectedFiles(new File[]{fileCreateNewFolder});
                } else {
                    fileChooser.setSelectedFile(fileCreateNewFolder);
                }
                fileChooser.rescanCurrentDirectory();
            } catch (IOException e2) {
                JOptionPane.showMessageDialog(fileChooser, BasicFileChooserUI.this.newFolderErrorText + BasicFileChooserUI.this.newFolderErrorSeparator + ((Object) e2), BasicFileChooserUI.this.newFolderErrorText, 0);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$GoHomeAction.class */
    protected class GoHomeAction extends AbstractAction {
        protected GoHomeAction() {
            super("Go Home");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicFileChooserUI.this.changeDirectory(BasicFileChooserUI.this.getFileChooser().getFileSystemView().getHomeDirectory());
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$ChangeToParentDirectoryAction.class */
    protected class ChangeToParentDirectoryAction extends AbstractAction {
        protected ChangeToParentDirectoryAction() {
            super(FilePane.ACTION_CHANGE_TO_PARENT_DIRECTORY);
            putValue(Action.ACTION_COMMAND_KEY, FilePane.ACTION_CHANGE_TO_PARENT_DIRECTORY);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicFileChooserUI.this.getFileChooser().changeToParentDirectory();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$ApproveSelectionAction.class */
    protected class ApproveSelectionAction extends AbstractAction {
        protected ApproveSelectionAction() {
            super(FilePane.ACTION_APPROVE_SELECTION);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicFileChooserUI.this.isDirectorySelected()) {
                File directory = BasicFileChooserUI.this.getDirectory();
                if (directory != null) {
                    try {
                        directory = ShellFolder.getNormalizedFile(directory);
                    } catch (IOException e2) {
                    }
                    BasicFileChooserUI.this.changeDirectory(directory);
                    return;
                }
            }
            JFileChooser fileChooser = BasicFileChooserUI.this.getFileChooser();
            String fileName = BasicFileChooserUI.this.getFileName();
            FileSystemView fileSystemView = fileChooser.getFileSystemView();
            File currentDirectory = fileChooser.getCurrentDirectory();
            if (fileName != null) {
                int length = fileName.length() - 1;
                while (length >= 0 && fileName.charAt(length) <= ' ') {
                    length--;
                }
                fileName = fileName.substring(0, length + 1);
            }
            if (fileName == null || fileName.length() == 0) {
                BasicFileChooserUI.this.resetGlobFilter();
                return;
            }
            File fileCreateFileObject = null;
            File[] fileArr = null;
            if (File.separatorChar == '/') {
                if (fileName.startsWith("~/")) {
                    fileName = System.getProperty("user.home") + fileName.substring(1);
                } else if (fileName.equals("~")) {
                    fileName = System.getProperty("user.home");
                }
            }
            if (fileChooser.isMultiSelectionEnabled() && fileName.length() > 1 && fileName.charAt(0) == '\"' && fileName.charAt(fileName.length() - 1) == '\"') {
                ArrayList arrayList = new ArrayList();
                String[] strArrSplit = fileName.substring(1, fileName.length() - 1).split("\" \"");
                Arrays.sort(strArrSplit);
                File[] files = null;
                int i2 = 0;
                for (String str : strArrSplit) {
                    File fileCreateFileObject2 = fileSystemView.createFileObject(str);
                    if (!fileCreateFileObject2.isAbsolute()) {
                        if (files == null) {
                            files = fileSystemView.getFiles(currentDirectory, false);
                            Arrays.sort(files);
                        }
                        int i3 = 0;
                        while (true) {
                            if (i3 < files.length) {
                                int length2 = (i2 + i3) % files.length;
                                if (!files[length2].getName().equals(str)) {
                                    i3++;
                                } else {
                                    fileCreateFileObject2 = files[length2];
                                    i2 = length2 + 1;
                                    break;
                                }
                            }
                        }
                    }
                    arrayList.add(fileCreateFileObject2);
                }
                if (!arrayList.isEmpty()) {
                    fileArr = (File[]) arrayList.toArray(new File[arrayList.size()]);
                }
                BasicFileChooserUI.this.resetGlobFilter();
            } else {
                fileCreateFileObject = fileSystemView.createFileObject(fileName);
                if (!fileCreateFileObject.isAbsolute()) {
                    fileCreateFileObject = fileSystemView.getChild(currentDirectory, fileName);
                }
                FileFilter fileFilter = fileChooser.getFileFilter();
                if (!fileCreateFileObject.exists() && BasicFileChooserUI.isGlobPattern(fileName)) {
                    BasicFileChooserUI.this.changeDirectory(fileCreateFileObject.getParentFile());
                    if (BasicFileChooserUI.this.globFilter == null) {
                        BasicFileChooserUI.this.globFilter = BasicFileChooserUI.this.new GlobFilter();
                    }
                    try {
                        BasicFileChooserUI.this.globFilter.setPattern(fileCreateFileObject.getName());
                        if (!(fileFilter instanceof GlobFilter)) {
                            BasicFileChooserUI.this.actualFileFilter = fileFilter;
                        }
                        fileChooser.setFileFilter(null);
                        fileChooser.setFileFilter(BasicFileChooserUI.this.globFilter);
                        return;
                    } catch (PatternSyntaxException e3) {
                    }
                }
                BasicFileChooserUI.this.resetGlobFilter();
                boolean z2 = fileCreateFileObject != null && fileCreateFileObject.isDirectory();
                boolean z3 = fileCreateFileObject != null && fileChooser.isTraversable(fileCreateFileObject);
                boolean zIsDirectorySelectionEnabled = fileChooser.isDirectorySelectionEnabled();
                boolean zIsFileSelectionEnabled = fileChooser.isFileSelectionEnabled();
                boolean z4 = (actionEvent == null || (actionEvent.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) == 0) ? false : true;
                if (z2 && z3 && (z4 || !zIsDirectorySelectionEnabled)) {
                    BasicFileChooserUI.this.changeDirectory(fileCreateFileObject);
                    return;
                } else if ((z2 || !zIsFileSelectionEnabled) && ((!z2 || !zIsDirectorySelectionEnabled) && (!zIsDirectorySelectionEnabled || fileCreateFileObject.exists()))) {
                    fileCreateFileObject = null;
                }
            }
            if (fileArr != null || fileCreateFileObject != null) {
                if (fileArr != null || fileChooser.isMultiSelectionEnabled()) {
                    if (fileArr == null) {
                        fileArr = new File[]{fileCreateFileObject};
                    }
                    fileChooser.setSelectedFiles(fileArr);
                    fileChooser.setSelectedFiles(fileArr);
                } else {
                    fileChooser.setSelectedFile(fileCreateFileObject);
                }
                fileChooser.approveSelection();
                return;
            }
            if (fileChooser.isMultiSelectionEnabled()) {
                fileChooser.setSelectedFiles(null);
            } else {
                fileChooser.setSelectedFile(null);
            }
            fileChooser.cancelSelection();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetGlobFilter() {
        if (this.actualFileFilter != null) {
            JFileChooser fileChooser = getFileChooser();
            FileFilter fileFilter = fileChooser.getFileFilter();
            if (fileFilter != null && fileFilter.equals(this.globFilter)) {
                fileChooser.setFileFilter(this.actualFileFilter);
                fileChooser.removeChoosableFileFilter(this.globFilter);
            }
            this.actualFileFilter = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isGlobPattern(String str) {
        return (File.separatorChar == '\\' && (str.indexOf(42) >= 0 || str.indexOf(63) >= 0)) || (File.separatorChar == '/' && (str.indexOf(42) >= 0 || str.indexOf(63) >= 0 || str.indexOf(91) >= 0));
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$GlobFilter.class */
    class GlobFilter extends FileFilter {
        Pattern pattern;
        String globPattern;

        GlobFilter() {
        }

        public void setPattern(String str) {
            char[] charArray = str.toCharArray();
            char[] cArr = new char[charArray.length * 2];
            boolean z2 = File.separatorChar == '\\';
            boolean z3 = false;
            int i2 = 0;
            this.globPattern = str;
            if (z2) {
                int length = charArray.length;
                if (str.endsWith("*.*")) {
                    length -= 2;
                }
                for (int i3 = 0; i3 < length; i3++) {
                    switch (charArray[i3]) {
                        case '*':
                            int i4 = i2;
                            int i5 = i2 + 1;
                            cArr[i4] = '.';
                            i2 = i5 + 1;
                            cArr[i5] = '*';
                            break;
                        case '?':
                            int i6 = i2;
                            i2++;
                            cArr[i6] = '.';
                            break;
                        case '\\':
                            int i7 = i2;
                            int i8 = i2 + 1;
                            cArr[i7] = '\\';
                            i2 = i8 + 1;
                            cArr[i8] = '\\';
                            break;
                        default:
                            if ("+()^$.{}[]".indexOf(charArray[i3]) >= 0) {
                                int i9 = i2;
                                i2++;
                                cArr[i9] = '\\';
                            }
                            int i10 = i2;
                            i2++;
                            cArr[i10] = charArray[i3];
                            break;
                    }
                }
            } else {
                int i11 = 0;
                while (i11 < charArray.length) {
                    switch (charArray[i11]) {
                        case '*':
                            if (!z3) {
                                int i12 = i2;
                                i2++;
                                cArr[i12] = '.';
                            }
                            int i13 = i2;
                            i2++;
                            cArr[i13] = '*';
                            break;
                        case '?':
                            int i14 = i2;
                            i2++;
                            cArr[i14] = z3 ? '?' : '.';
                            break;
                        case '[':
                            z3 = true;
                            int i15 = i2;
                            i2++;
                            cArr[i15] = charArray[i11];
                            if (i11 < charArray.length - 1) {
                                switch (charArray[i11 + 1]) {
                                    case '!':
                                    case '^':
                                        i2++;
                                        cArr[i2] = '^';
                                        i11++;
                                        break;
                                    case ']':
                                        i2++;
                                        i11++;
                                        cArr[i2] = charArray[i11];
                                        break;
                                }
                            } else {
                                break;
                            }
                            break;
                        case '\\':
                            if (i11 == 0 && charArray.length > 1 && charArray[1] == '~') {
                                int i16 = i2;
                                i2++;
                                i11++;
                                cArr[i16] = charArray[i11];
                                break;
                            } else {
                                int i17 = i2;
                                int i18 = i2 + 1;
                                cArr[i17] = '\\';
                                if (i11 < charArray.length - 1 && "*?[]".indexOf(charArray[i11 + 1]) >= 0) {
                                    i2 = i18 + 1;
                                    i11++;
                                    cArr[i18] = charArray[i11];
                                    break;
                                } else {
                                    i2 = i18 + 1;
                                    cArr[i18] = '\\';
                                    break;
                                }
                            }
                            break;
                        case ']':
                            int i19 = i2;
                            i2++;
                            cArr[i19] = charArray[i11];
                            z3 = false;
                            break;
                        default:
                            if (!Character.isLetterOrDigit(charArray[i11])) {
                                int i20 = i2;
                                i2++;
                                cArr[i20] = '\\';
                            }
                            int i21 = i2;
                            i2++;
                            cArr[i21] = charArray[i11];
                            break;
                    }
                    i11++;
                }
            }
            this.pattern = Pattern.compile(new String(cArr, 0, i2), 2);
        }

        @Override // javax.swing.filechooser.FileFilter
        public boolean accept(File file) {
            if (file == null) {
                return false;
            }
            if (file.isDirectory()) {
                return true;
            }
            return this.pattern.matcher(file.getName()).matches();
        }

        @Override // javax.swing.filechooser.FileFilter
        public String getDescription() {
            return this.globPattern;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$CancelSelectionAction.class */
    protected class CancelSelectionAction extends AbstractAction {
        protected CancelSelectionAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicFileChooserUI.this.getFileChooser().cancelSelection();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$UpdateAction.class */
    protected class UpdateAction extends AbstractAction {
        protected UpdateAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser fileChooser = BasicFileChooserUI.this.getFileChooser();
            fileChooser.setCurrentDirectory(fileChooser.getFileSystemView().createFileObject(BasicFileChooserUI.this.getDirectoryName()));
            fileChooser.rescanCurrentDirectory();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeDirectory(File file) {
        JFileChooser fileChooser = getFileChooser();
        if (file != null && FilePane.usesShellFolder(fileChooser)) {
            try {
                ShellFolder shellFolder = ShellFolder.getShellFolder(file);
                if (shellFolder.isLink()) {
                    ShellFolder linkLocation = shellFolder.getLinkLocation();
                    if (linkLocation != null) {
                        if (fileChooser.isTraversable(linkLocation)) {
                            file = linkLocation;
                        } else {
                            return;
                        }
                    } else {
                        file = shellFolder;
                    }
                }
            } catch (FileNotFoundException e2) {
                return;
            }
        }
        fileChooser.setCurrentDirectory(file);
        if (fileChooser.getFileSelectionMode() == 2 && fileChooser.getFileSystemView().isFileSystem(file)) {
            setFileName(file.getAbsolutePath());
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$AcceptAllFileFilter.class */
    protected class AcceptAllFileFilter extends FileFilter {
        public AcceptAllFileFilter() {
        }

        @Override // javax.swing.filechooser.FileFilter
        public boolean accept(File file) {
            return true;
        }

        @Override // javax.swing.filechooser.FileFilter
        public String getDescription() {
            return UIManager.getString("FileChooser.acceptAllFileFilterText");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$BasicFileView.class */
    public class BasicFileView extends FileView {
        protected Hashtable<File, Icon> iconCache = new Hashtable<>();

        public BasicFileView() {
        }

        public void clearIconCache() {
            this.iconCache = new Hashtable<>();
        }

        @Override // javax.swing.filechooser.FileView
        public String getName(File file) {
            String systemDisplayName = null;
            if (file != null) {
                systemDisplayName = BasicFileChooserUI.this.getFileChooser().getFileSystemView().getSystemDisplayName(file);
            }
            return systemDisplayName;
        }

        @Override // javax.swing.filechooser.FileView
        public String getDescription(File file) {
            return file.getName();
        }

        @Override // javax.swing.filechooser.FileView
        public String getTypeDescription(File file) {
            String systemTypeDescription = BasicFileChooserUI.this.getFileChooser().getFileSystemView().getSystemTypeDescription(file);
            if (systemTypeDescription == null) {
                systemTypeDescription = file.isDirectory() ? BasicFileChooserUI.this.directoryDescriptionText : BasicFileChooserUI.this.fileDescriptionText;
            }
            return systemTypeDescription;
        }

        public Icon getCachedIcon(File file) {
            return this.iconCache.get(file);
        }

        public void cacheIcon(File file, Icon icon) {
            if (file == null || icon == null) {
                return;
            }
            this.iconCache.put(file, icon);
        }

        @Override // javax.swing.filechooser.FileView
        public Icon getIcon(File file) {
            Icon cachedIcon = getCachedIcon(file);
            if (cachedIcon != null) {
                return cachedIcon;
            }
            Icon icon = BasicFileChooserUI.this.fileIcon;
            if (file != null) {
                FileSystemView fileSystemView = BasicFileChooserUI.this.getFileChooser().getFileSystemView();
                if (fileSystemView.isFloppyDrive(file)) {
                    icon = BasicFileChooserUI.this.floppyDriveIcon;
                } else if (fileSystemView.isDrive(file)) {
                    icon = BasicFileChooserUI.this.hardDriveIcon;
                } else if (fileSystemView.isComputerNode(file)) {
                    icon = BasicFileChooserUI.this.computerIcon;
                } else if (file.isDirectory()) {
                    icon = BasicFileChooserUI.this.directoryIcon;
                }
            }
            cacheIcon(file, icon);
            return icon;
        }

        public Boolean isHidden(File file) {
            String name = file.getName();
            if (name != null && name.charAt(0) == '.') {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$FileTransferHandler.class */
    static class FileTransferHandler extends TransferHandler implements UIResource {
        FileTransferHandler() {
        }

        @Override // javax.swing.TransferHandler
        protected Transferable createTransferable(JComponent jComponent) {
            JTable jTable;
            int[] selectedRows;
            Object[] selectedValues = null;
            if (jComponent instanceof JList) {
                selectedValues = ((JList) jComponent).getSelectedValues();
            } else if ((jComponent instanceof JTable) && (selectedRows = (jTable = (JTable) jComponent).getSelectedRows()) != null) {
                selectedValues = new Object[selectedRows.length];
                for (int i2 = 0; i2 < selectedRows.length; i2++) {
                    selectedValues[i2] = jTable.getValueAt(selectedRows[i2], 0);
                }
            }
            if (selectedValues == null || selectedValues.length == 0) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("<html>\n<body>\n<ul>\n");
            Object[] objArr = selectedValues;
            int length = objArr.length;
            for (int i3 = 0; i3 < length; i3++) {
                Object obj = objArr[i3];
                String string = obj == null ? "" : obj.toString();
                stringBuffer.append(string + "\n");
                stringBuffer2.append("  <li>" + string + "\n");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer2.append("</ul>\n</body>\n</html>");
            return new FileTransferable(stringBuffer.toString(), stringBuffer2.toString(), selectedValues);
        }

        @Override // javax.swing.TransferHandler
        public int getSourceActions(JComponent jComponent) {
            return 1;
        }

        /* loaded from: rt.jar:javax/swing/plaf/basic/BasicFileChooserUI$FileTransferHandler$FileTransferable.class */
        static class FileTransferable extends BasicTransferable {
            Object[] fileData;

            FileTransferable(String str, String str2, Object[] objArr) {
                super(str, str2);
                this.fileData = objArr;
            }

            @Override // javax.swing.plaf.basic.BasicTransferable
            protected DataFlavor[] getRicherFlavors() {
                return new DataFlavor[]{DataFlavor.javaFileListFlavor};
            }

            @Override // javax.swing.plaf.basic.BasicTransferable
            protected Object getRicherData(DataFlavor dataFlavor) {
                if (DataFlavor.javaFileListFlavor.equals(dataFlavor)) {
                    ArrayList arrayList = new ArrayList();
                    for (Object obj : this.fileData) {
                        arrayList.add(obj);
                    }
                    return arrayList;
                }
                return null;
            }
        }
    }
}
