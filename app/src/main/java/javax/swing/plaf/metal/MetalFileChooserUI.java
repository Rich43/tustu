package javax.swing.plaf.metal;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;
import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDirectoryModel;
import javax.swing.plaf.basic.BasicFileChooserUI;
import org.icepdf.core.util.PdfOps;
import sun.awt.shell.ShellFolder;
import sun.swing.FilePane;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI.class */
public class MetalFileChooserUI extends BasicFileChooserUI {
    private JLabel lookInLabel;
    private JComboBox directoryComboBox;
    private DirectoryComboBoxModel directoryComboBoxModel;
    private Action directoryComboBoxAction;
    private FilterComboBoxModel filterComboBoxModel;
    private JTextField fileNameTextField;
    private FilePane filePane;
    private JToggleButton listViewButton;
    private JToggleButton detailsViewButton;
    private JButton approveButton;
    private JButton cancelButton;
    private JPanel buttonPanel;
    private JPanel bottomPanel;
    private JComboBox filterComboBox;
    private static final Dimension hstrut5 = new Dimension(5, 1);
    private static final Dimension hstrut11 = new Dimension(11, 1);
    private static final Dimension vstrut5 = new Dimension(1, 5);
    private static final Insets shrinkwrap = new Insets(0, 0, 0, 0);
    private static int PREF_WIDTH = 500;
    private static int PREF_HEIGHT = 326;
    private static Dimension PREF_SIZE = new Dimension(PREF_WIDTH, PREF_HEIGHT);
    private static int MIN_WIDTH = 500;
    private static int MIN_HEIGHT = 326;
    private static int LIST_PREF_WIDTH = 405;
    private static int LIST_PREF_HEIGHT = 135;
    private static Dimension LIST_PREF_SIZE = new Dimension(LIST_PREF_WIDTH, LIST_PREF_HEIGHT);
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
    static final int space = 10;

    private void populateFileNameLabel() {
        if (getFileChooser().getFileSelectionMode() == 1) {
            this.fileNameLabel.setText(this.folderNameLabelText);
            this.fileNameLabel.setDisplayedMnemonic(this.folderNameLabelMnemonic);
        } else {
            this.fileNameLabel.setText(this.fileNameLabelText);
            this.fileNameLabel.setDisplayedMnemonic(this.fileNameLabelMnemonic);
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalFileChooserUI((JFileChooser) jComponent);
    }

    public MetalFileChooserUI(JFileChooser jFileChooser) {
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
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void uninstallComponents(JFileChooser jFileChooser) {
        jFileChooser.removeAll();
        this.bottomPanel = null;
        this.buttonPanel = null;
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$MetalFileChooserUIAccessor.class */
    private class MetalFileChooserUIAccessor implements FilePane.FileChooserUIAccessor {
        private MetalFileChooserUIAccessor() {
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public JFileChooser getFileChooser() {
            return MetalFileChooserUI.this.getFileChooser();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public BasicDirectoryModel getModel() {
            return MetalFileChooserUI.this.getModel();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public JPanel createList() {
            return MetalFileChooserUI.this.createList(getFileChooser());
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public JPanel createDetailsView() {
            return MetalFileChooserUI.this.createDetailsView(getFileChooser());
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public boolean isDirectorySelected() {
            return MetalFileChooserUI.this.isDirectorySelected();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public File getDirectory() {
            return MetalFileChooserUI.this.getDirectory();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public Action getChangeToParentDirectoryAction() {
            return MetalFileChooserUI.this.getChangeToParentDirectoryAction();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public Action getApproveSelectionAction() {
            return MetalFileChooserUI.this.getApproveSelectionAction();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public Action getNewFolderAction() {
            return MetalFileChooserUI.this.getNewFolderAction();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public MouseListener createDoubleClickListener(JList jList) {
            return MetalFileChooserUI.this.createDoubleClickListener(getFileChooser(), jList);
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public ListSelectionListener createListSelectionListener() {
            return MetalFileChooserUI.this.createListSelectionListener(getFileChooser());
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void installComponents(JFileChooser jFileChooser) throws IllegalArgumentException {
        FileSystemView fileSystemView = jFileChooser.getFileSystemView();
        jFileChooser.setBorder(new EmptyBorder(12, 12, 11, 11));
        jFileChooser.setLayout(new BorderLayout(0, 11));
        this.filePane = new FilePane(new MetalFileChooserUIAccessor());
        jFileChooser.addPropertyChangeListener(this.filePane);
        JPanel jPanel = new JPanel(new BorderLayout(11, 0));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BoxLayout(jPanel2, 2));
        jPanel.add(jPanel2, "After");
        jFileChooser.add(jPanel, "North");
        this.lookInLabel = new JLabel(this.lookInLabelText);
        this.lookInLabel.setDisplayedMnemonic(this.lookInLabelMnemonic);
        jPanel.add(this.lookInLabel, "Before");
        this.directoryComboBox = new JComboBox() { // from class: javax.swing.plaf.metal.MetalFileChooserUI.1
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getPreferredSize() {
                Dimension preferredSize = super.getPreferredSize();
                preferredSize.width = 150;
                return preferredSize;
            }
        };
        this.directoryComboBox.putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, this.lookInLabelText);
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
        JButton jButton = new JButton(getChangeToParentDirectoryAction());
        jButton.setText(null);
        jButton.setIcon(this.upFolderIcon);
        jButton.setToolTipText(this.upFolderToolTipText);
        jButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, this.upFolderAccessibleName);
        jButton.setAlignmentX(0.0f);
        jButton.setAlignmentY(0.5f);
        jButton.setMargin(shrinkwrap);
        jPanel2.add(jButton);
        jPanel2.add(Box.createRigidArea(hstrut5));
        fileSystemView.getHomeDirectory();
        String str = this.homeFolderToolTipText;
        JButton jButton2 = new JButton(this.homeFolderIcon);
        jButton2.setToolTipText(str);
        jButton2.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, this.homeFolderAccessibleName);
        jButton2.setAlignmentX(0.0f);
        jButton2.setAlignmentY(0.5f);
        jButton2.setMargin(shrinkwrap);
        jButton2.addActionListener(getGoHomeAction());
        jPanel2.add(jButton2);
        jPanel2.add(Box.createRigidArea(hstrut5));
        if (!UIManager.getBoolean("FileChooser.readOnly")) {
            jButton2 = new JButton(this.filePane.getNewFolderAction());
            jButton2.setText(null);
            jButton2.setIcon(this.newFolderIcon);
            jButton2.setToolTipText(this.newFolderToolTipText);
            jButton2.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, this.newFolderAccessibleName);
            jButton2.setAlignmentX(0.0f);
            jButton2.setAlignmentY(0.5f);
            jButton2.setMargin(shrinkwrap);
        }
        jPanel2.add(jButton2);
        jPanel2.add(Box.createRigidArea(hstrut5));
        ButtonGroup buttonGroup = new ButtonGroup();
        this.listViewButton = new JToggleButton(this.listViewIcon);
        this.listViewButton.setToolTipText(this.listViewButtonToolTipText);
        this.listViewButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, this.listViewButtonAccessibleName);
        this.listViewButton.setSelected(true);
        this.listViewButton.setAlignmentX(0.0f);
        this.listViewButton.setAlignmentY(0.5f);
        this.listViewButton.setMargin(shrinkwrap);
        this.listViewButton.addActionListener(this.filePane.getViewTypeAction(0));
        jPanel2.add(this.listViewButton);
        buttonGroup.add(this.listViewButton);
        this.detailsViewButton = new JToggleButton(this.detailsViewIcon);
        this.detailsViewButton.setToolTipText(this.detailsViewButtonToolTipText);
        this.detailsViewButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, this.detailsViewButtonAccessibleName);
        this.detailsViewButton.setAlignmentX(0.0f);
        this.detailsViewButton.setAlignmentY(0.5f);
        this.detailsViewButton.setMargin(shrinkwrap);
        this.detailsViewButton.addActionListener(this.filePane.getViewTypeAction(1));
        jPanel2.add(this.detailsViewButton);
        buttonGroup.add(this.detailsViewButton);
        this.filePane.addPropertyChangeListener(new PropertyChangeListener() { // from class: javax.swing.plaf.metal.MetalFileChooserUI.2
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if ("viewType".equals(propertyChangeEvent.getPropertyName())) {
                    switch (MetalFileChooserUI.this.filePane.getViewType()) {
                        case 0:
                            MetalFileChooserUI.this.listViewButton.setSelected(true);
                            break;
                        case 1:
                            MetalFileChooserUI.this.detailsViewButton.setSelected(true);
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
        JPanel bottomPanel = getBottomPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, 1));
        jFileChooser.add(bottomPanel, "South");
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BoxLayout(jPanel3, 2));
        bottomPanel.add(jPanel3);
        bottomPanel.add(Box.createRigidArea(vstrut5));
        this.fileNameLabel = new AlignedLabel();
        populateFileNameLabel();
        jPanel3.add(this.fileNameLabel);
        this.fileNameTextField = new JTextField(35) { // from class: javax.swing.plaf.metal.MetalFileChooserUI.3
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                return new Dimension(Short.MAX_VALUE, super.getPreferredSize().height);
            }
        };
        jPanel3.add(this.fileNameTextField);
        this.fileNameLabel.setLabelFor(this.fileNameTextField);
        this.fileNameTextField.addFocusListener(new FocusAdapter() { // from class: javax.swing.plaf.metal.MetalFileChooserUI.4
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusGained(FocusEvent focusEvent) {
                if (!MetalFileChooserUI.this.getFileChooser().isMultiSelectionEnabled()) {
                    MetalFileChooserUI.this.filePane.clearSelection();
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
        bottomPanel.add(jPanel4);
        AlignedLabel alignedLabel = new AlignedLabel(this.filesOfTypeLabelText);
        alignedLabel.setDisplayedMnemonic(this.filesOfTypeLabelMnemonic);
        jPanel4.add(alignedLabel);
        this.filterComboBoxModel = createFilterComboBoxModel();
        jFileChooser.addPropertyChangeListener(this.filterComboBoxModel);
        this.filterComboBox = new JComboBox(this.filterComboBoxModel);
        this.filterComboBox.putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, this.filesOfTypeLabelText);
        alignedLabel.setLabelFor(this.filterComboBox);
        this.filterComboBox.setRenderer(createFilterComboBoxRenderer());
        jPanel4.add(this.filterComboBox);
        getButtonPanel().setLayout(new ButtonAreaLayout());
        this.approveButton = new JButton(getApproveButtonText(jFileChooser));
        this.approveButton.addActionListener(getApproveSelectionAction());
        this.approveButton.setToolTipText(getApproveButtonToolTipText(jFileChooser));
        getButtonPanel().add(this.approveButton);
        this.cancelButton = new JButton(this.cancelButtonText);
        this.cancelButton.setToolTipText(this.cancelButtonToolTipText);
        this.cancelButton.addActionListener(getCancelSelectionAction());
        getButtonPanel().add(this.cancelButton);
        if (jFileChooser.getControlButtonsAreShown()) {
            addControlButtons();
        }
        groupLabels(new AlignedLabel[]{this.fileNameLabel, alignedLabel});
    }

    protected JPanel getButtonPanel() {
        if (this.buttonPanel == null) {
            this.buttonPanel = new JPanel();
        }
        return this.buttonPanel;
    }

    protected JPanel getBottomPanel() {
        if (this.bottomPanel == null) {
            this.bottomPanel = new JPanel();
        }
        return this.bottomPanel;
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void installStrings(JFileChooser jFileChooser) {
        super.installStrings(jFileChooser);
        Locale locale = jFileChooser.getLocale();
        this.lookInLabelMnemonic = getMnemonic("FileChooser.lookInLabelMnemonic", locale).intValue();
        this.lookInLabelText = UIManager.getString("FileChooser.lookInLabelText", locale);
        this.saveInLabelText = UIManager.getString("FileChooser.saveInLabelText", locale);
        this.fileNameLabelMnemonic = getMnemonic("FileChooser.fileNameLabelMnemonic", locale).intValue();
        this.fileNameLabelText = UIManager.getString("FileChooser.fileNameLabelText", locale);
        this.folderNameLabelMnemonic = getMnemonic("FileChooser.folderNameLabelMnemonic", locale).intValue();
        this.folderNameLabelText = UIManager.getString("FileChooser.folderNameLabelText", locale);
        this.filesOfTypeLabelMnemonic = getMnemonic("FileChooser.filesOfTypeLabelMnemonic", locale).intValue();
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

    private Integer getMnemonic(String str, Locale locale) {
        return Integer.valueOf(SwingUtilities2.getUIDefaultsInt(str, locale));
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void installListeners(JFileChooser jFileChooser) {
        super.installListeners(jFileChooser);
        SwingUtilities.replaceUIActionMap(jFileChooser, getActionMap());
    }

    protected ActionMap getActionMap() {
        return createActionMap();
    }

    protected ActionMap createActionMap() {
        ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
        FilePane.addActionsToMap(actionMapUIResource, this.filePane.getActions());
        return actionMapUIResource;
    }

    protected JPanel createList(JFileChooser jFileChooser) {
        return this.filePane.createList();
    }

    protected JPanel createDetailsView(JFileChooser jFileChooser) {
        return this.filePane.createDetailsView();
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public ListSelectionListener createListSelectionListener(JFileChooser jFileChooser) {
        return super.createListSelectionListener(jFileChooser);
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$SingleClickListener.class */
    protected class SingleClickListener extends MouseAdapter {
        public SingleClickListener(JList jList) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$FileRenderer.class */
    protected class FileRenderer extends DefaultListCellRenderer {
        protected FileRenderer() {
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        jComponent.removePropertyChangeListener(this.filterComboBoxModel);
        jComponent.removePropertyChangeListener(this.filePane);
        this.cancelButton.removeActionListener(getCancelSelectionAction());
        this.approveButton.removeActionListener(getApproveSelectionAction());
        this.fileNameTextField.removeActionListener(getApproveSelectionAction());
        if (this.filePane != null) {
            this.filePane.uninstallUI();
            this.filePane = null;
        }
        super.uninstallUI(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        int i2 = PREF_SIZE.width;
        Dimension dimensionPreferredLayoutSize = jComponent.getLayout().preferredLayoutSize(jComponent);
        if (dimensionPreferredLayoutSize != null) {
            return new Dimension(dimensionPreferredLayoutSize.width < i2 ? i2 : dimensionPreferredLayoutSize.width, dimensionPreferredLayoutSize.height < PREF_SIZE.height ? PREF_SIZE.height : dimensionPreferredLayoutSize.height);
        }
        return new Dimension(i2, PREF_SIZE.height);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return new Dimension(MIN_WIDTH, MIN_HEIGHT);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    private String fileNameString(File file) {
        if (file == null) {
            return null;
        }
        JFileChooser fileChooser = getFileChooser();
        if ((fileChooser.isDirectorySelectionEnabled() && !fileChooser.isFileSelectionEnabled()) || (fileChooser.isDirectorySelectionEnabled() && fileChooser.isFileSelectionEnabled() && fileChooser.getFileSystemView().isFileSystemRoot(file))) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void doSelectedFileChanged(PropertyChangeEvent propertyChangeEvent) {
        File file = (File) propertyChangeEvent.getNewValue();
        JFileChooser fileChooser = getFileChooser();
        if (file != null) {
            if ((fileChooser.isFileSelectionEnabled() && !file.isDirectory()) || (file.isDirectory() && fileChooser.isDirectorySelectionEnabled())) {
                setFileName(fileNameString(file));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSelectedFilesChanged(PropertyChangeEvent propertyChangeEvent) {
        File[] fileArr = (File[]) propertyChangeEvent.getNewValue();
        JFileChooser fileChooser = getFileChooser();
        if (fileArr != null && fileArr.length > 0) {
            if (fileArr.length > 1 || fileChooser.isDirectorySelectionEnabled() || !fileArr[0].isDirectory()) {
                setFileName(fileNameString(fileArr));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doDirectoryChanged(PropertyChangeEvent propertyChangeEvent) {
        JFileChooser fileChooser = getFileChooser();
        FileSystemView fileSystemView = fileChooser.getFileSystemView();
        clearIconCache();
        File currentDirectory = fileChooser.getCurrentDirectory();
        if (currentDirectory == null) {
            return;
        }
        this.directoryComboBoxModel.addItem(currentDirectory);
        if (fileChooser.isDirectorySelectionEnabled() && !fileChooser.isFileSelectionEnabled()) {
            if (fileSystemView.isFileSystem(currentDirectory)) {
                setFileName(currentDirectory.getPath());
            } else {
                setFileName(null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doFilterChanged(PropertyChangeEvent propertyChangeEvent) {
        clearIconCache();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doFileSelectionModeChanged(PropertyChangeEvent propertyChangeEvent) {
        if (this.fileNameLabel != null) {
            populateFileNameLabel();
        }
        clearIconCache();
        JFileChooser fileChooser = getFileChooser();
        File currentDirectory = fileChooser.getCurrentDirectory();
        if (currentDirectory != null && fileChooser.isDirectorySelectionEnabled() && !fileChooser.isFileSelectionEnabled() && fileChooser.getFileSystemView().isFileSystem(currentDirectory)) {
            setFileName(currentDirectory.getPath());
        } else {
            setFileName(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doAccessoryChanged(PropertyChangeEvent propertyChangeEvent) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void doApproveButtonTextChanged(PropertyChangeEvent propertyChangeEvent) {
        JFileChooser fileChooser = getFileChooser();
        this.approveButton.setText(getApproveButtonText(fileChooser));
        this.approveButton.setToolTipText(getApproveButtonToolTipText(fileChooser));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doDialogTypeChanged(PropertyChangeEvent propertyChangeEvent) throws IllegalArgumentException {
        JFileChooser fileChooser = getFileChooser();
        this.approveButton.setText(getApproveButtonText(fileChooser));
        this.approveButton.setToolTipText(getApproveButtonToolTipText(fileChooser));
        if (fileChooser.getDialogType() == 1) {
            this.lookInLabel.setText(this.saveInLabelText);
        } else {
            this.lookInLabel.setText(this.lookInLabelText);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doApproveButtonMnemonicChanged(PropertyChangeEvent propertyChangeEvent) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doControlButtonsChanged(PropertyChangeEvent propertyChangeEvent) {
        if (getFileChooser().getControlButtonsAreShown()) {
            addControlButtons();
        } else {
            removeControlButtons();
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public PropertyChangeListener createPropertyChangeListener(JFileChooser jFileChooser) {
        return new PropertyChangeListener() { // from class: javax.swing.plaf.metal.MetalFileChooserUI.5
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws IllegalArgumentException {
                String propertyName = propertyChangeEvent.getPropertyName();
                if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doSelectedFileChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doSelectedFilesChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doDirectoryChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doFilterChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doFileSelectionModeChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.ACCESSORY_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doAccessoryChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY) || propertyName.equals(JFileChooser.APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doApproveButtonTextChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doDialogTypeChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doApproveButtonMnemonicChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY)) {
                    MetalFileChooserUI.this.doControlButtonsChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals("componentOrientation")) {
                    ComponentOrientation componentOrientation = (ComponentOrientation) propertyChangeEvent.getNewValue();
                    JFileChooser jFileChooser2 = (JFileChooser) propertyChangeEvent.getSource();
                    if (componentOrientation != propertyChangeEvent.getOldValue()) {
                        jFileChooser2.applyComponentOrientation(componentOrientation);
                        return;
                    }
                    return;
                }
                if (propertyName == "FileChooser.useShellFolder") {
                    MetalFileChooserUI.this.doDirectoryChanged(propertyChangeEvent);
                } else if (propertyName.equals("ancestor") && propertyChangeEvent.getOldValue() == null && propertyChangeEvent.getNewValue() != null) {
                    MetalFileChooserUI.this.fileNameTextField.selectAll();
                    MetalFileChooserUI.this.fileNameTextField.requestFocus();
                }
            }
        };
    }

    protected void removeControlButtons() {
        getBottomPanel().remove(getButtonPanel());
    }

    protected void addControlButtons() {
        getBottomPanel().add(getButtonPanel());
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.FileChooserUI
    public void ensureFileIsVisible(JFileChooser jFileChooser, File file) {
        this.filePane.ensureFileIsVisible(jFileChooser, file);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.FileChooserUI
    public void rescanCurrentDirectory(JFileChooser jFileChooser) {
        this.filePane.rescanCurrentDirectory();
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public String getFileName() {
        if (this.fileNameTextField != null) {
            return this.fileNameTextField.getText();
        }
        return null;
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void setFileName(String str) {
        if (this.fileNameTextField != null) {
            this.fileNameTextField.setText(str);
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void setDirectorySelected(boolean z2) {
        super.setDirectorySelected(z2);
        JFileChooser fileChooser = getFileChooser();
        if (z2) {
            if (this.approveButton != null) {
                this.approveButton.setText(this.directoryOpenButtonText);
                this.approveButton.setToolTipText(this.directoryOpenButtonToolTipText);
                return;
            }
            return;
        }
        if (this.approveButton != null) {
            this.approveButton.setText(getApproveButtonText(fileChooser));
            this.approveButton.setToolTipText(getApproveButtonToolTipText(fileChooser));
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public String getDirectoryName() {
        return null;
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void setDirectoryName(String str) {
    }

    protected DirectoryComboBoxRenderer createDirectoryComboBoxRenderer(JFileChooser jFileChooser) {
        return new DirectoryComboBoxRenderer();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$DirectoryComboBoxRenderer.class */
    class DirectoryComboBoxRenderer extends DefaultListCellRenderer {
        IndentIcon ii;

        DirectoryComboBoxRenderer() {
            this.ii = MetalFileChooserUI.this.new IndentIcon();
        }

        @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
        public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            super.getListCellRendererComponent(jList, obj, i2, z2, z3);
            if (obj == null) {
                setText("");
                return this;
            }
            File file = (File) obj;
            setText(MetalFileChooserUI.this.getFileChooser().getName(file));
            this.ii.icon = MetalFileChooserUI.this.getFileChooser().getIcon(file);
            this.ii.depth = MetalFileChooserUI.this.directoryComboBoxModel.getDepth(i2);
            setIcon(this.ii);
            return this;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$IndentIcon.class */
    class IndentIcon implements Icon {
        Icon icon = null;
        int depth = 0;

        IndentIcon() {
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            if (component.getComponentOrientation().isLeftToRight()) {
                this.icon.paintIcon(component, graphics, i2 + (this.depth * 10), i3);
            } else {
                this.icon.paintIcon(component, graphics, i2, i3);
            }
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return this.icon.getIconWidth() + (this.depth * 10);
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return this.icon.getIconHeight();
        }
    }

    protected DirectoryComboBoxModel createDirectoryComboBoxModel(JFileChooser jFileChooser) {
        return new DirectoryComboBoxModel();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$DirectoryComboBoxModel.class */
    protected class DirectoryComboBoxModel extends AbstractListModel<Object> implements ComboBoxModel<Object> {
        Vector<File> directories = new Vector<>();
        int[] depths = null;
        File selectedDirectory = null;
        JFileChooser chooser;
        FileSystemView fsv;

        public DirectoryComboBoxModel() {
            this.chooser = MetalFileChooserUI.this.getFileChooser();
            this.fsv = this.chooser.getFileSystemView();
            File currentDirectory = MetalFileChooserUI.this.getFileChooser().getCurrentDirectory();
            if (currentDirectory != null) {
                addItem(currentDirectory);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
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
            this.directories.clear();
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
            int size = vector.size();
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
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
        public Object getElementAt(int i2) {
            return this.directories.elementAt(i2);
        }
    }

    protected FilterComboBoxRenderer createFilterComboBoxRenderer() {
        return new FilterComboBoxRenderer();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$FilterComboBoxRenderer.class */
    public class FilterComboBoxRenderer extends DefaultListCellRenderer {
        public FilterComboBoxRenderer() {
        }

        @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
        public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            super.getListCellRendererComponent(jList, obj, i2, z2, z3);
            if (obj != null && (obj instanceof FileFilter)) {
                setText(((FileFilter) obj).getDescription());
            }
            return this;
        }
    }

    protected FilterComboBoxModel createFilterComboBoxModel() {
        return new FilterComboBoxModel();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$FilterComboBoxModel.class */
    protected class FilterComboBoxModel extends AbstractListModel<Object> implements ComboBoxModel<Object>, PropertyChangeListener {
        protected FileFilter[] filters;

        protected FilterComboBoxModel() {
            this.filters = MetalFileChooserUI.this.getFileChooser().getChoosableFileFilters();
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
                MetalFileChooserUI.this.getFileChooser().setFileFilter((FileFilter) obj);
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override // javax.swing.ComboBoxModel
        public Object getSelectedItem() {
            FileFilter fileFilter = MetalFileChooserUI.this.getFileChooser().getFileFilter();
            boolean z2 = false;
            if (fileFilter != null) {
                for (FileFilter fileFilter2 : this.filters) {
                    if (fileFilter2 == fileFilter) {
                        z2 = true;
                    }
                }
                if (!z2) {
                    MetalFileChooserUI.this.getFileChooser().addChoosableFileFilter(fileFilter);
                }
            }
            return MetalFileChooserUI.this.getFileChooser().getFileFilter();
        }

        @Override // javax.swing.ListModel
        public int getSize() {
            if (this.filters != null) {
                return this.filters.length;
            }
            return 0;
        }

        @Override // javax.swing.ListModel
        public Object getElementAt(int i2) {
            if (i2 > getSize() - 1) {
                return MetalFileChooserUI.this.getFileChooser().getFileFilter();
            }
            if (this.filters != null) {
                return this.filters[i2];
            }
            return null;
        }
    }

    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        File selectedFile = getFileChooser().getSelectedFile();
        if (!listSelectionEvent.getValueIsAdjusting() && selectedFile != null && !getFileChooser().isTraversable(selectedFile)) {
            setFileName(fileNameString(selectedFile));
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$DirectoryComboBoxAction.class */
    protected class DirectoryComboBoxAction extends AbstractAction {
        protected DirectoryComboBoxAction() {
            super("DirectoryComboBoxAction");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            MetalFileChooserUI.this.directoryComboBox.hidePopup();
            File file = (File) MetalFileChooserUI.this.directoryComboBox.getSelectedItem();
            if (!MetalFileChooserUI.this.getFileChooser().getCurrentDirectory().equals(file)) {
                MetalFileChooserUI.this.getFileChooser().setCurrentDirectory(file);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected JButton getApproveButton(JFileChooser jFileChooser) {
        return this.approveButton;
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$ButtonAreaLayout.class */
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

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalFileChooserUI$AlignedLabel.class */
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
