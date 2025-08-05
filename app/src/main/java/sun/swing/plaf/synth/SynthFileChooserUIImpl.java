package sun.swing.plaf.synth;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.basic.BasicDirectoryModel;
import org.icepdf.core.util.PdfOps;
import sun.awt.shell.ShellFolder;
import sun.swing.FilePane;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl.class */
public class SynthFileChooserUIImpl extends SynthFileChooserUI {
    private JLabel lookInLabel;
    private JComboBox<File> directoryComboBox;
    private DirectoryComboBoxModel directoryComboBoxModel;
    private Action directoryComboBoxAction;
    private FilterComboBoxModel filterComboBoxModel;
    private JTextField fileNameTextField;
    private FilePane filePane;
    private JToggleButton listViewButton;
    private JToggleButton detailsViewButton;
    private boolean readOnly;
    private JPanel buttonPanel;
    private JPanel bottomPanel;
    private JComboBox<FileFilter> filterComboBox;
    private static final Dimension hstrut5 = new Dimension(5, 1);
    private static final Insets shrinkwrap = new Insets(0, 0, 0, 0);
    private static Dimension LIST_PREF_SIZE = new Dimension(405, 135);
    private int lookInLabelMnemonic;
    private String lookInLabelText;
    private String saveInLabelText;
    private int fileNameLabelMnemonic;
    private String fileNameLabelText;
    private int folderNameLabelMnemonic;
    private String folderNameLabelText;
    private int filesOfTypeLabelMnemonic;
    private String filesOfTypeLabelText;
    private String upFolderToolTipText;
    private String upFolderAccessibleName;
    private String homeFolderToolTipText;
    private String homeFolderAccessibleName;
    private String newFolderToolTipText;
    private String newFolderAccessibleName;
    private String listViewButtonToolTipText;
    private String listViewButtonAccessibleName;
    private String detailsViewButtonToolTipText;
    private String detailsViewButtonAccessibleName;
    private AlignedLabel fileNameLabel;
    private final PropertyChangeListener modeListener;
    static final int space = 10;

    /* JADX INFO: Access modifiers changed from: private */
    public void populateFileNameLabel() {
        if (getFileChooser().getFileSelectionMode() == 1) {
            this.fileNameLabel.setText(this.folderNameLabelText);
            this.fileNameLabel.setDisplayedMnemonic(this.folderNameLabelMnemonic);
        } else {
            this.fileNameLabel.setText(this.fileNameLabelText);
            this.fileNameLabel.setDisplayedMnemonic(this.fileNameLabelMnemonic);
        }
    }

    public SynthFileChooserUIImpl(JFileChooser jFileChooser) {
        super(jFileChooser);
        this.directoryComboBoxAction = new DirectoryComboBoxAction();
        this.lookInLabelMnemonic = 0;
        this.lookInLabelText = null;
        this.saveInLabelText = null;
        this.fileNameLabelMnemonic = 0;
        this.fileNameLabelText = null;
        this.folderNameLabelMnemonic = 0;
        this.folderNameLabelText = null;
        this.filesOfTypeLabelMnemonic = 0;
        this.filesOfTypeLabelText = null;
        this.upFolderToolTipText = null;
        this.upFolderAccessibleName = null;
        this.homeFolderToolTipText = null;
        this.homeFolderAccessibleName = null;
        this.newFolderToolTipText = null;
        this.newFolderAccessibleName = null;
        this.listViewButtonToolTipText = null;
        this.listViewButtonAccessibleName = null;
        this.detailsViewButtonToolTipText = null;
        this.detailsViewButtonAccessibleName = null;
        this.modeListener = new PropertyChangeListener() { // from class: sun.swing.plaf.synth.SynthFileChooserUIImpl.1
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if (SynthFileChooserUIImpl.this.fileNameLabel != null) {
                    SynthFileChooserUIImpl.this.populateFileNameLabel();
                }
            }
        };
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl$SynthFileChooserUIAccessor.class */
    private class SynthFileChooserUIAccessor implements FilePane.FileChooserUIAccessor {
        private SynthFileChooserUIAccessor() {
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public JFileChooser getFileChooser() {
            return SynthFileChooserUIImpl.this.getFileChooser();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public BasicDirectoryModel getModel() {
            return SynthFileChooserUIImpl.this.getModel();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public JPanel createList() {
            return null;
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public JPanel createDetailsView() {
            return null;
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public boolean isDirectorySelected() {
            return SynthFileChooserUIImpl.this.isDirectorySelected();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public File getDirectory() {
            return SynthFileChooserUIImpl.this.getDirectory();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public Action getChangeToParentDirectoryAction() {
            return SynthFileChooserUIImpl.this.getChangeToParentDirectoryAction();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public Action getApproveSelectionAction() {
            return SynthFileChooserUIImpl.this.getApproveSelectionAction();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public Action getNewFolderAction() {
            return SynthFileChooserUIImpl.this.getNewFolderAction();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public MouseListener createDoubleClickListener(JList jList) {
            return SynthFileChooserUIImpl.this.createDoubleClickListener(getFileChooser(), jList);
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public ListSelectionListener createListSelectionListener() {
            return SynthFileChooserUIImpl.this.createListSelectionListener(getFileChooser());
        }
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI, javax.swing.plaf.basic.BasicFileChooserUI
    protected void installDefaults(JFileChooser jFileChooser) {
        super.installDefaults(jFileChooser);
        this.readOnly = UIManager.getBoolean("FileChooser.readOnly");
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI, javax.swing.plaf.basic.BasicFileChooserUI
    public void installComponents(JFileChooser jFileChooser) throws IllegalArgumentException {
        super.installComponents(jFileChooser);
        getContext(jFileChooser, 1);
        jFileChooser.setLayout(new BorderLayout(0, 11));
        JPanel jPanel = new JPanel(new BorderLayout(11, 0));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BoxLayout(jPanel2, 2));
        jPanel.add(jPanel2, "After");
        jFileChooser.add(jPanel, "North");
        this.lookInLabel = new JLabel(this.lookInLabelText);
        this.lookInLabel.setDisplayedMnemonic(this.lookInLabelMnemonic);
        jPanel.add(this.lookInLabel, "Before");
        this.directoryComboBox = new JComboBox<>();
        this.directoryComboBox.getAccessibleContext().setAccessibleDescription(this.lookInLabelText);
        this.directoryComboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        this.lookInLabel.setLabelFor(this.directoryComboBox);
        this.directoryComboBoxModel = createDirectoryComboBoxModel(jFileChooser);
        this.directoryComboBox.setModel(this.directoryComboBoxModel);
        this.directoryComboBox.addActionListener(this.directoryComboBoxAction);
        this.directoryComboBox.setRenderer(createDirectoryComboBoxRenderer(jFileChooser));
        this.directoryComboBox.setAlignmentX(0.0f);
        this.directoryComboBox.setAlignmentY(0.0f);
        this.directoryComboBox.setMaximumRowCount(8);
        jPanel.add(this.directoryComboBox, BorderLayout.CENTER);
        this.filePane = new FilePane(new SynthFileChooserUIAccessor());
        jFileChooser.addPropertyChangeListener(this.filePane);
        JPopupMenu componentPopupMenu = this.filePane.getComponentPopupMenu();
        if (componentPopupMenu != null) {
            componentPopupMenu.insert(getChangeToParentDirectoryAction(), 0);
            if (File.separatorChar == '/') {
                componentPopupMenu.insert(getGoHomeAction(), 1);
            }
        }
        FileSystemView fileSystemView = jFileChooser.getFileSystemView();
        JButton jButton = new JButton(getChangeToParentDirectoryAction());
        jButton.setText(null);
        jButton.setIcon(this.upFolderIcon);
        jButton.setToolTipText(this.upFolderToolTipText);
        jButton.getAccessibleContext().setAccessibleName(this.upFolderAccessibleName);
        jButton.setAlignmentX(0.0f);
        jButton.setAlignmentY(0.5f);
        jButton.setMargin(shrinkwrap);
        jPanel2.add(jButton);
        jPanel2.add(Box.createRigidArea(hstrut5));
        fileSystemView.getHomeDirectory();
        String str = this.homeFolderToolTipText;
        JButton jButton2 = new JButton(this.homeFolderIcon);
        jButton2.setToolTipText(str);
        jButton2.getAccessibleContext().setAccessibleName(this.homeFolderAccessibleName);
        jButton2.setAlignmentX(0.0f);
        jButton2.setAlignmentY(0.5f);
        jButton2.setMargin(shrinkwrap);
        jButton2.addActionListener(getGoHomeAction());
        jPanel2.add(jButton2);
        jPanel2.add(Box.createRigidArea(hstrut5));
        if (!this.readOnly) {
            JButton jButton3 = new JButton(this.filePane.getNewFolderAction());
            jButton3.setText(null);
            jButton3.setIcon(this.newFolderIcon);
            jButton3.setToolTipText(this.newFolderToolTipText);
            jButton3.getAccessibleContext().setAccessibleName(this.newFolderAccessibleName);
            jButton3.setAlignmentX(0.0f);
            jButton3.setAlignmentY(0.5f);
            jButton3.setMargin(shrinkwrap);
            jPanel2.add(jButton3);
            jPanel2.add(Box.createRigidArea(hstrut5));
        }
        ButtonGroup buttonGroup = new ButtonGroup();
        this.listViewButton = new JToggleButton(this.listViewIcon);
        this.listViewButton.setToolTipText(this.listViewButtonToolTipText);
        this.listViewButton.getAccessibleContext().setAccessibleName(this.listViewButtonAccessibleName);
        this.listViewButton.setSelected(true);
        this.listViewButton.setAlignmentX(0.0f);
        this.listViewButton.setAlignmentY(0.5f);
        this.listViewButton.setMargin(shrinkwrap);
        this.listViewButton.addActionListener(this.filePane.getViewTypeAction(0));
        jPanel2.add(this.listViewButton);
        buttonGroup.add(this.listViewButton);
        this.detailsViewButton = new JToggleButton(this.detailsViewIcon);
        this.detailsViewButton.setToolTipText(this.detailsViewButtonToolTipText);
        this.detailsViewButton.getAccessibleContext().setAccessibleName(this.detailsViewButtonAccessibleName);
        this.detailsViewButton.setAlignmentX(0.0f);
        this.detailsViewButton.setAlignmentY(0.5f);
        this.detailsViewButton.setMargin(shrinkwrap);
        this.detailsViewButton.addActionListener(this.filePane.getViewTypeAction(1));
        jPanel2.add(this.detailsViewButton);
        buttonGroup.add(this.detailsViewButton);
        this.filePane.addPropertyChangeListener(new PropertyChangeListener() { // from class: sun.swing.plaf.synth.SynthFileChooserUIImpl.2
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if ("viewType".equals(propertyChangeEvent.getPropertyName())) {
                    switch (SynthFileChooserUIImpl.this.filePane.getViewType()) {
                        case 0:
                            SynthFileChooserUIImpl.this.listViewButton.setSelected(true);
                            break;
                        case 1:
                            SynthFileChooserUIImpl.this.detailsViewButton.setSelected(true);
                            break;
                    }
                }
            }
        });
        jFileChooser.add(getAccessoryPanel(), "After");
        JComponent accessory = jFileChooser.getAccessory();
        if (accessory != null) {
            getAccessoryPanel().add(accessory);
        }
        this.filePane.setPreferredSize(LIST_PREF_SIZE);
        jFileChooser.add(this.filePane, BorderLayout.CENTER);
        this.bottomPanel = new JPanel();
        this.bottomPanel.setLayout(new BoxLayout(this.bottomPanel, 1));
        jFileChooser.add(this.bottomPanel, "South");
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BoxLayout(jPanel3, 2));
        this.bottomPanel.add(jPanel3);
        this.bottomPanel.add(Box.createRigidArea(new Dimension(1, 5)));
        this.fileNameLabel = new AlignedLabel();
        populateFileNameLabel();
        jPanel3.add(this.fileNameLabel);
        this.fileNameTextField = new JTextField(35) { // from class: sun.swing.plaf.synth.SynthFileChooserUIImpl.3
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                return new Dimension(Short.MAX_VALUE, super.getPreferredSize().height);
            }
        };
        jPanel3.add(this.fileNameTextField);
        this.fileNameLabel.setLabelFor(this.fileNameTextField);
        this.fileNameTextField.addFocusListener(new FocusAdapter() { // from class: sun.swing.plaf.synth.SynthFileChooserUIImpl.4
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusGained(FocusEvent focusEvent) {
                if (!SynthFileChooserUIImpl.this.getFileChooser().isMultiSelectionEnabled()) {
                    SynthFileChooserUIImpl.this.filePane.clearSelection();
                }
            }
        });
        if (jFileChooser.isMultiSelectionEnabled()) {
            setFileName(fileNameString(jFileChooser.getSelectedFiles()));
        } else {
            setFileName(fileNameString(jFileChooser.getSelectedFile()));
        }
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BoxLayout(jPanel4, 2));
        this.bottomPanel.add(jPanel4);
        AlignedLabel alignedLabel = new AlignedLabel(this.filesOfTypeLabelText);
        alignedLabel.setDisplayedMnemonic(this.filesOfTypeLabelMnemonic);
        jPanel4.add(alignedLabel);
        this.filterComboBoxModel = createFilterComboBoxModel();
        jFileChooser.addPropertyChangeListener(this.filterComboBoxModel);
        this.filterComboBox = new JComboBox<>(this.filterComboBoxModel);
        this.filterComboBox.getAccessibleContext().setAccessibleDescription(this.filesOfTypeLabelText);
        alignedLabel.setLabelFor(this.filterComboBox);
        this.filterComboBox.setRenderer(createFilterComboBoxRenderer());
        jPanel4.add(this.filterComboBox);
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new ButtonAreaLayout());
        this.buttonPanel.add(getApproveButton(jFileChooser));
        this.buttonPanel.add(getCancelButton(jFileChooser));
        if (jFileChooser.getControlButtonsAreShown()) {
            addControlButtons();
        }
        groupLabels(new AlignedLabel[]{this.fileNameLabel, alignedLabel});
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI, javax.swing.plaf.basic.BasicFileChooserUI
    protected void installListeners(JFileChooser jFileChooser) {
        super.installListeners(jFileChooser);
        jFileChooser.addPropertyChangeListener(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY, this.modeListener);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void uninstallListeners(JFileChooser jFileChooser) {
        jFileChooser.removePropertyChangeListener(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY, this.modeListener);
        super.uninstallListeners(jFileChooser);
    }

    private String fileNameString(File file) {
        if (file == null) {
            return null;
        }
        JFileChooser fileChooser = getFileChooser();
        if (fileChooser.isDirectorySelectionEnabled() && !fileChooser.isFileSelectionEnabled()) {
            return file.getPath();
        }
        return file.getName();
    }

    private String fileNameString(File[] fileArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; fileArr != null && i2 < fileArr.length; i2++) {
            if (i2 > 0) {
                stringBuffer.append(" ");
            }
            if (fileArr.length > 1) {
                stringBuffer.append(PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            stringBuffer.append(fileNameString(fileArr[i2]));
            if (fileArr.length > 1) {
                stringBuffer.append(PdfOps.DOUBLE_QUOTE__TOKEN);
            }
        }
        return stringBuffer.toString();
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        jComponent.removePropertyChangeListener(this.filterComboBoxModel);
        jComponent.removePropertyChangeListener(this.filePane);
        if (this.filePane != null) {
            this.filePane.uninstallUI();
            this.filePane = null;
        }
        super.uninstallUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void installStrings(JFileChooser jFileChooser) {
        super.installStrings(jFileChooser);
        Locale locale = jFileChooser.getLocale();
        this.lookInLabelMnemonic = getMnemonic("FileChooser.lookInLabelMnemonic", locale);
        this.lookInLabelText = UIManager.getString("FileChooser.lookInLabelText", locale);
        this.saveInLabelText = UIManager.getString("FileChooser.saveInLabelText", locale);
        this.fileNameLabelMnemonic = getMnemonic("FileChooser.fileNameLabelMnemonic", locale);
        this.fileNameLabelText = UIManager.getString("FileChooser.fileNameLabelText", locale);
        this.folderNameLabelMnemonic = getMnemonic("FileChooser.folderNameLabelMnemonic", locale);
        this.folderNameLabelText = UIManager.getString("FileChooser.folderNameLabelText", locale);
        this.filesOfTypeLabelMnemonic = getMnemonic("FileChooser.filesOfTypeLabelMnemonic", locale);
        this.filesOfTypeLabelText = UIManager.getString("FileChooser.filesOfTypeLabelText", locale);
        this.upFolderToolTipText = UIManager.getString("FileChooser.upFolderToolTipText", locale);
        this.upFolderAccessibleName = UIManager.getString("FileChooser.upFolderAccessibleName", locale);
        this.homeFolderToolTipText = UIManager.getString("FileChooser.homeFolderToolTipText", locale);
        this.homeFolderAccessibleName = UIManager.getString("FileChooser.homeFolderAccessibleName", locale);
        this.newFolderToolTipText = UIManager.getString("FileChooser.newFolderToolTipText", locale);
        this.newFolderAccessibleName = UIManager.getString("FileChooser.newFolderAccessibleName", locale);
        this.listViewButtonToolTipText = UIManager.getString("FileChooser.listViewButtonToolTipText", locale);
        this.listViewButtonAccessibleName = UIManager.getString("FileChooser.listViewButtonAccessibleName", locale);
        this.detailsViewButtonToolTipText = UIManager.getString("FileChooser.detailsViewButtonToolTipText", locale);
        this.detailsViewButtonAccessibleName = UIManager.getString("FileChooser.detailsViewButtonAccessibleName", locale);
    }

    private int getMnemonic(String str, Locale locale) {
        return SwingUtilities2.getUIDefaultsInt(str, locale);
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI, javax.swing.plaf.basic.BasicFileChooserUI
    public String getFileName() {
        if (this.fileNameTextField != null) {
            return this.fileNameTextField.getText();
        }
        return null;
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI, javax.swing.plaf.basic.BasicFileChooserUI
    public void setFileName(String str) {
        if (this.fileNameTextField != null) {
            this.fileNameTextField.setText(str);
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.FileChooserUI
    public void rescanCurrentDirectory(JFileChooser jFileChooser) {
        this.filePane.rescanCurrentDirectory();
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI
    protected void doSelectedFileChanged(PropertyChangeEvent propertyChangeEvent) {
        super.doSelectedFileChanged(propertyChangeEvent);
        File file = (File) propertyChangeEvent.getNewValue();
        JFileChooser fileChooser = getFileChooser();
        if (file != null) {
            if ((fileChooser.isFileSelectionEnabled() && !file.isDirectory()) || (file.isDirectory() && fileChooser.isDirectorySelectionEnabled())) {
                setFileName(fileNameString(file));
            }
        }
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI
    protected void doSelectedFilesChanged(PropertyChangeEvent propertyChangeEvent) {
        super.doSelectedFilesChanged(propertyChangeEvent);
        File[] fileArr = (File[]) propertyChangeEvent.getNewValue();
        JFileChooser fileChooser = getFileChooser();
        if (fileArr != null && fileArr.length > 0) {
            if (fileArr.length > 1 || fileChooser.isDirectorySelectionEnabled() || !fileArr[0].isDirectory()) {
                setFileName(fileNameString(fileArr));
            }
        }
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI
    protected void doDirectoryChanged(PropertyChangeEvent propertyChangeEvent) {
        super.doDirectoryChanged(propertyChangeEvent);
        JFileChooser fileChooser = getFileChooser();
        FileSystemView fileSystemView = fileChooser.getFileSystemView();
        File currentDirectory = fileChooser.getCurrentDirectory();
        if (!this.readOnly && currentDirectory != null) {
            getNewFolderAction().setEnabled(this.filePane.canWrite(currentDirectory));
        }
        if (currentDirectory != null) {
            JComponent directoryComboBox = getDirectoryComboBox();
            if (directoryComboBox instanceof JComboBox) {
                ComboBoxModel model = ((JComboBox) directoryComboBox).getModel();
                if (model instanceof DirectoryComboBoxModel) {
                    ((DirectoryComboBoxModel) model).addItem(currentDirectory);
                }
            }
            if (fileChooser.isDirectorySelectionEnabled() && !fileChooser.isFileSelectionEnabled()) {
                if (fileSystemView.isFileSystem(currentDirectory)) {
                    setFileName(currentDirectory.getPath());
                } else {
                    setFileName(null);
                }
            }
        }
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI
    protected void doFileSelectionModeChanged(PropertyChangeEvent propertyChangeEvent) {
        super.doFileSelectionModeChanged(propertyChangeEvent);
        JFileChooser fileChooser = getFileChooser();
        File currentDirectory = fileChooser.getCurrentDirectory();
        if (currentDirectory != null && fileChooser.isDirectorySelectionEnabled() && !fileChooser.isFileSelectionEnabled() && fileChooser.getFileSystemView().isFileSystem(currentDirectory)) {
            setFileName(currentDirectory.getPath());
        } else {
            setFileName(null);
        }
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI
    protected void doAccessoryChanged(PropertyChangeEvent propertyChangeEvent) {
        if (getAccessoryPanel() != null) {
            if (propertyChangeEvent.getOldValue() != null) {
                getAccessoryPanel().remove((JComponent) propertyChangeEvent.getOldValue());
            }
            JComponent jComponent = (JComponent) propertyChangeEvent.getNewValue();
            if (jComponent != null) {
                getAccessoryPanel().add(jComponent, BorderLayout.CENTER);
            }
        }
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI
    protected void doControlButtonsChanged(PropertyChangeEvent propertyChangeEvent) {
        super.doControlButtonsChanged(propertyChangeEvent);
        if (getFileChooser().getControlButtonsAreShown()) {
            addControlButtons();
        } else {
            removeControlButtons();
        }
    }

    protected void addControlButtons() {
        if (this.bottomPanel != null) {
            this.bottomPanel.add(this.buttonPanel);
        }
    }

    protected void removeControlButtons() {
        if (this.bottomPanel != null) {
            this.bottomPanel.remove(this.buttonPanel);
        }
    }

    @Override // sun.swing.plaf.synth.SynthFileChooserUI
    protected ActionMap createActionMap() {
        ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
        FilePane.addActionsToMap(actionMapUIResource, this.filePane.getActions());
        actionMapUIResource.put("fileNameCompletion", getFileNameCompletionAction());
        return actionMapUIResource;
    }

    protected JComponent getDirectoryComboBox() {
        return this.directoryComboBox;
    }

    protected Action getDirectoryComboBoxAction() {
        return this.directoryComboBoxAction;
    }

    protected DirectoryComboBoxRenderer createDirectoryComboBoxRenderer(JFileChooser jFileChooser) {
        return new DirectoryComboBoxRenderer(this.directoryComboBox.getRenderer());
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl$DirectoryComboBoxRenderer.class */
    private class DirectoryComboBoxRenderer implements ListCellRenderer<File> {
        private ListCellRenderer<? super File> delegate;
        IndentIcon ii;
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // javax.swing.ListCellRenderer
        public /* bridge */ /* synthetic */ Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            return getListCellRendererComponent((JList<? extends File>) jList, (File) obj, i2, z2, z3);
        }

        static {
            $assertionsDisabled = !SynthFileChooserUIImpl.class.desiredAssertionStatus();
        }

        private DirectoryComboBoxRenderer(ListCellRenderer<? super File> listCellRenderer) {
            this.ii = SynthFileChooserUIImpl.this.new IndentIcon();
            this.delegate = listCellRenderer;
        }

        public Component getListCellRendererComponent(JList<? extends File> jList, File file, int i2, boolean z2, boolean z3) throws IllegalArgumentException {
            Component listCellRendererComponent = this.delegate.getListCellRendererComponent(jList, file, i2, z2, z3);
            if (!$assertionsDisabled && !(listCellRendererComponent instanceof JLabel)) {
                throw new AssertionError();
            }
            JLabel jLabel = (JLabel) listCellRendererComponent;
            if (file == null) {
                jLabel.setText("");
                return jLabel;
            }
            jLabel.setText(SynthFileChooserUIImpl.this.getFileChooser().getName(file));
            this.ii.icon = SynthFileChooserUIImpl.this.getFileChooser().getIcon(file);
            this.ii.depth = SynthFileChooserUIImpl.this.directoryComboBoxModel.getDepth(i2);
            jLabel.setIcon(this.ii);
            return jLabel;
        }
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl$IndentIcon.class */
    class IndentIcon implements Icon {
        Icon icon = null;
        int depth = 0;

        IndentIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (this.icon != null) {
                if (component.getComponentOrientation().isLeftToRight()) {
                    this.icon.paintIcon(component, graphics, i2 + (this.depth * 10), i3);
                } else {
                    this.icon.paintIcon(component, graphics, i2, i3);
                }
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return (this.icon != null ? this.icon.getIconWidth() : 0) + (this.depth * 10);
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            if (this.icon != null) {
                return this.icon.getIconHeight();
            }
            return 0;
        }
    }

    protected DirectoryComboBoxModel createDirectoryComboBoxModel(JFileChooser jFileChooser) {
        return new DirectoryComboBoxModel();
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl$DirectoryComboBoxModel.class */
    protected class DirectoryComboBoxModel extends AbstractListModel<File> implements ComboBoxModel<File> {
        Vector<File> directories = new Vector<>();
        int[] depths = null;
        File selectedDirectory = null;
        JFileChooser chooser;
        FileSystemView fsv;

        public DirectoryComboBoxModel() {
            this.chooser = SynthFileChooserUIImpl.this.getFileChooser();
            this.fsv = this.chooser.getFileSystemView();
            File currentDirectory = SynthFileChooserUIImpl.this.getFileChooser().getCurrentDirectory();
            if (currentDirectory != null) {
                addItem(currentDirectory);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void addItem(File file) {
            File[] roots;
            File normalizedFile;
            File shellFolder;
            File parentFile;
            if (file == null) {
                return;
            }
            boolean zUsesShellFolder = FilePane.usesShellFolder(this.chooser);
            int size = this.directories.size();
            this.directories.clear();
            if (size > 0) {
                fireIntervalRemoved(this, 0, size);
            }
            if (zUsesShellFolder) {
                roots = (File[]) ShellFolder.get("fileChooserComboBoxFolders");
            } else {
                roots = this.fsv.getRoots();
            }
            this.directories.addAll(Arrays.asList(roots));
            try {
                normalizedFile = ShellFolder.getNormalizedFile(file);
            } catch (IOException e2) {
                normalizedFile = file;
            }
            if (zUsesShellFolder) {
                try {
                    shellFolder = ShellFolder.getShellFolder(normalizedFile);
                } catch (FileNotFoundException e3) {
                    calculateDepths();
                    return;
                }
            } else {
                shellFolder = normalizedFile;
            }
            File file2 = shellFolder;
            File file3 = file2;
            Vector vector = new Vector(10);
            do {
                vector.addElement(file3);
                parentFile = file3.getParentFile();
                file3 = parentFile;
            } while (parentFile != null);
            int size2 = vector.size();
            int i2 = 0;
            while (true) {
                if (i2 >= size2) {
                    break;
                }
                File file4 = (File) vector.get(i2);
                if (!this.directories.contains(file4)) {
                    i2++;
                } else {
                    int iIndexOf = this.directories.indexOf(file4);
                    for (int i3 = i2 - 1; i3 >= 0; i3--) {
                        this.directories.insertElementAt(vector.get(i3), (iIndexOf + i2) - i3);
                    }
                }
            }
            calculateDepths();
            setSelectedItem(file2);
        }

        private void calculateDepths() {
            this.depths = new int[this.directories.size()];
            for (int i2 = 0; i2 < this.depths.length; i2++) {
                File parentFile = this.directories.get(i2).getParentFile();
                this.depths[i2] = 0;
                if (parentFile != null) {
                    int i3 = i2 - 1;
                    while (true) {
                        if (i3 < 0) {
                            break;
                        }
                        if (parentFile.equals(this.directories.get(i3))) {
                            this.depths[i2] = this.depths[i3] + 1;
                            break;
                        }
                        i3--;
                    }
                }
            }
        }

        public int getDepth(int i2) {
            if (this.depths == null || i2 < 0 || i2 >= this.depths.length) {
                return 0;
            }
            return this.depths[i2];
        }

        @Override // javax.swing.ComboBoxModel
        public void setSelectedItem(Object obj) {
            this.selectedDirectory = (File) obj;
            fireContentsChanged(this, -1, -1);
        }

        @Override // javax.swing.ComboBoxModel
        public Object getSelectedItem() {
            return this.selectedDirectory;
        }

        @Override // javax.swing.ListModel
        public int getSize() {
            return this.directories.size();
        }

        @Override // javax.swing.ListModel
        public File getElementAt(int i2) {
            return this.directories.elementAt(i2);
        }
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl$DirectoryComboBoxAction.class */
    protected class DirectoryComboBoxAction extends AbstractAction {
        protected DirectoryComboBoxAction() {
            super("DirectoryComboBoxAction");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            SynthFileChooserUIImpl.this.directoryComboBox.hidePopup();
            JComponent directoryComboBox = SynthFileChooserUIImpl.this.getDirectoryComboBox();
            if (directoryComboBox instanceof JComboBox) {
                SynthFileChooserUIImpl.this.getFileChooser().setCurrentDirectory((File) ((JComboBox) directoryComboBox).getSelectedItem());
            }
        }
    }

    protected FilterComboBoxRenderer createFilterComboBoxRenderer() {
        return new FilterComboBoxRenderer(this.filterComboBox.getRenderer());
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl$FilterComboBoxRenderer.class */
    public class FilterComboBoxRenderer implements ListCellRenderer<FileFilter> {
        private ListCellRenderer<? super FileFilter> delegate;
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // javax.swing.ListCellRenderer
        public /* bridge */ /* synthetic */ Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            return getListCellRendererComponent((JList<? extends FileFilter>) jList, (FileFilter) obj, i2, z2, z3);
        }

        static {
            $assertionsDisabled = !SynthFileChooserUIImpl.class.desiredAssertionStatus();
        }

        private FilterComboBoxRenderer(ListCellRenderer<? super FileFilter> listCellRenderer) {
            this.delegate = listCellRenderer;
        }

        public Component getListCellRendererComponent(JList<? extends FileFilter> jList, FileFilter fileFilter, int i2, boolean z2, boolean z3) throws IllegalArgumentException {
            Component listCellRendererComponent = this.delegate.getListCellRendererComponent(jList, fileFilter, i2, z2, z3);
            String description = null;
            if (fileFilter != null) {
                description = fileFilter.getDescription();
            }
            if (!$assertionsDisabled && !(listCellRendererComponent instanceof JLabel)) {
                throw new AssertionError();
            }
            if (description != null) {
                ((JLabel) listCellRendererComponent).setText(description);
            }
            return listCellRendererComponent;
        }
    }

    protected FilterComboBoxModel createFilterComboBoxModel() {
        return new FilterComboBoxModel();
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl$FilterComboBoxModel.class */
    protected class FilterComboBoxModel extends AbstractListModel<FileFilter> implements ComboBoxModel<FileFilter>, PropertyChangeListener {
        protected FileFilter[] filters;

        protected FilterComboBoxModel() {
            this.filters = SynthFileChooserUIImpl.this.getFileChooser().getChoosableFileFilters();
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == JFileChooser.CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY) {
                this.filters = (FileFilter[]) propertyChangeEvent.getNewValue();
                fireContentsChanged(this, -1, -1);
            } else if (propertyName == JFileChooser.FILE_FILTER_CHANGED_PROPERTY) {
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override // javax.swing.ComboBoxModel
        public void setSelectedItem(Object obj) {
            if (obj != null) {
                SynthFileChooserUIImpl.this.getFileChooser().setFileFilter((FileFilter) obj);
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override // javax.swing.ComboBoxModel
        public Object getSelectedItem() {
            FileFilter fileFilter = SynthFileChooserUIImpl.this.getFileChooser().getFileFilter();
            boolean z2 = false;
            if (fileFilter != null) {
                for (FileFilter fileFilter2 : this.filters) {
                    if (fileFilter2 == fileFilter) {
                        z2 = true;
                    }
                }
                if (!z2) {
                    SynthFileChooserUIImpl.this.getFileChooser().addChoosableFileFilter(fileFilter);
                }
            }
            return SynthFileChooserUIImpl.this.getFileChooser().getFileFilter();
        }

        @Override // javax.swing.ListModel
        public int getSize() {
            if (this.filters != null) {
                return this.filters.length;
            }
            return 0;
        }

        @Override // javax.swing.ListModel
        public FileFilter getElementAt(int i2) {
            if (i2 > getSize() - 1) {
                return SynthFileChooserUIImpl.this.getFileChooser().getFileFilter();
            }
            if (this.filters != null) {
                return this.filters[i2];
            }
            return null;
        }
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl$ButtonAreaLayout.class */
    private static class ButtonAreaLayout implements LayoutManager {
        private int hGap;
        private int topMargin;

        private ButtonAreaLayout() {
            this.hGap = 5;
            this.topMargin = 17;
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int i2;
            int i3;
            Component[] components = container.getComponents();
            if (components != null && components.length > 0) {
                int length = components.length;
                Dimension[] dimensionArr = new Dimension[length];
                Insets insets = container.getInsets();
                int i4 = insets.top + this.topMargin;
                int iMax = 0;
                for (int i5 = 0; i5 < length; i5++) {
                    dimensionArr[i5] = components[i5].getPreferredSize();
                    iMax = Math.max(iMax, dimensionArr[i5].width);
                }
                if (container.getComponentOrientation().isLeftToRight()) {
                    i2 = (container.getSize().width - insets.left) - iMax;
                    i3 = this.hGap + iMax;
                } else {
                    i2 = insets.left;
                    i3 = -(this.hGap + iMax);
                }
                for (int i6 = length - 1; i6 >= 0; i6--) {
                    components[i6].setBounds(i2, i4, iMax, dimensionArr[i6].height);
                    i2 -= i3;
                }
            }
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            Component[] components;
            if (container != null && (components = container.getComponents()) != null && components.length > 0) {
                int length = components.length;
                int iMax = 0;
                Insets insets = container.getInsets();
                int i2 = this.topMargin + insets.top + insets.bottom;
                int i3 = insets.left + insets.right;
                int iMax2 = 0;
                for (Component component : components) {
                    Dimension preferredSize = component.getPreferredSize();
                    iMax = Math.max(iMax, preferredSize.height);
                    iMax2 = Math.max(iMax2, preferredSize.width);
                }
                return new Dimension(i3 + (length * iMax2) + ((length - 1) * this.hGap), i2 + iMax);
            }
            return new Dimension(0, 0);
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return minimumLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }
    }

    private static void groupLabels(AlignedLabel[] alignedLabelArr) {
        for (AlignedLabel alignedLabel : alignedLabelArr) {
            alignedLabel.group = alignedLabelArr;
        }
    }

    /* loaded from: rt.jar:sun/swing/plaf/synth/SynthFileChooserUIImpl$AlignedLabel.class */
    private class AlignedLabel extends JLabel {
        private AlignedLabel[] group;
        private int maxWidth;

        AlignedLabel() {
            this.maxWidth = 0;
            setAlignmentX(0.0f);
        }

        AlignedLabel(String str) {
            super(str);
            this.maxWidth = 0;
            setAlignmentX(0.0f);
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            return new Dimension(getMaxWidth() + 11, super.getPreferredSize().height);
        }

        private int getMaxWidth() {
            if (this.maxWidth == 0 && this.group != null) {
                int iMax = 0;
                for (int i2 = 0; i2 < this.group.length; i2++) {
                    iMax = Math.max(this.group[i2].getSuperPreferredWidth(), iMax);
                }
                for (int i3 = 0; i3 < this.group.length; i3++) {
                    this.group[i3].maxWidth = iMax;
                }
            }
            return this.maxWidth;
        }

        private int getSuperPreferredWidth() {
            return super.getPreferredSize().width;
        }
    }
}
