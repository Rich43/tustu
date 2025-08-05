package com.sun.java.swing.plaf.motif;

import com.intel.bluetooth.BluetoothConsts;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import org.apache.commons.net.ftp.FTPReply;
import org.icepdf.core.util.PdfOps;
import sun.awt.shell.ShellFolder;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifFileChooserUI.class */
public class MotifFileChooserUI extends BasicFileChooserUI {
    private FilterComboBoxModel filterComboBoxModel;
    protected JList<File> directoryList;
    protected JList<File> fileList;
    protected JTextField pathField;
    protected JComboBox<FileFilter> filterComboBox;
    protected JTextField filenameTextField;
    private static final int MIN_WIDTH = 200;
    private static final int MIN_HEIGHT = 300;
    private JPanel bottomPanel;
    protected JButton approveButton;
    private String enterFolderNameLabelText;
    private int enterFolderNameLabelMnemonic;
    private String enterFileNameLabelText;
    private int enterFileNameLabelMnemonic;
    private String filesLabelText;
    private int filesLabelMnemonic;
    private String foldersLabelText;
    private int foldersLabelMnemonic;
    private String pathLabelText;
    private int pathLabelMnemonic;
    private String filterLabelText;
    private int filterLabelMnemonic;
    private JLabel fileNameLabel;
    private static final Dimension hstrut10 = new Dimension(10, 1);
    private static final Dimension vstrut10 = new Dimension(1, 10);
    private static final Insets insets = new Insets(10, 10, 10, 10);
    private static Dimension prefListSize = new Dimension(75, 150);
    private static Dimension WITH_ACCELERATOR_PREF_SIZE = new Dimension(BluetoothConsts.TCP_OBEX_DEFAULT_PORT, 450);
    private static Dimension PREF_SIZE = new Dimension(FTPReply.FILE_ACTION_PENDING, 450);
    private static Dimension PREF_ACC_SIZE = new Dimension(10, 10);
    private static Dimension ZERO_ACC_SIZE = new Dimension(1, 1);
    private static Dimension MAX_SIZE = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    private static final Insets buttonMargin = new Insets(3, 3, 3, 3);

    /* JADX INFO: Access modifiers changed from: private */
    public void populateFileNameLabel() throws IllegalArgumentException {
        if (getFileChooser().getFileSelectionMode() == 1) {
            this.fileNameLabel.setText(this.enterFolderNameLabelText);
            this.fileNameLabel.setDisplayedMnemonic(this.enterFolderNameLabelMnemonic);
        } else {
            this.fileNameLabel.setText(this.enterFileNameLabelText);
            this.fileNameLabel.setDisplayedMnemonic(this.enterFileNameLabelMnemonic);
        }
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

    /* JADX INFO: Access modifiers changed from: private */
    public String fileNameString(File[] fileArr) {
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

    public MotifFileChooserUI(JFileChooser jFileChooser) {
        super(jFileChooser);
        this.directoryList = null;
        this.fileList = null;
        this.pathField = null;
        this.filterComboBox = null;
        this.filenameTextField = null;
        this.enterFolderNameLabelText = null;
        this.enterFolderNameLabelMnemonic = 0;
        this.enterFileNameLabelText = null;
        this.enterFileNameLabelMnemonic = 0;
        this.filesLabelText = null;
        this.filesLabelMnemonic = 0;
        this.foldersLabelText = null;
        this.foldersLabelMnemonic = 0;
        this.pathLabelText = null;
        this.pathLabelMnemonic = 0;
        this.filterLabelText = null;
        this.filterLabelMnemonic = 0;
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public String getFileName() {
        if (this.filenameTextField != null) {
            return this.filenameTextField.getText();
        }
        return null;
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void setFileName(String str) {
        if (this.filenameTextField != null) {
            this.filenameTextField.setText(str);
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public String getDirectoryName() {
        return this.pathField.getText();
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void setDirectoryName(String str) {
        this.pathField.setText(str);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.FileChooserUI
    public void ensureFileIsVisible(JFileChooser jFileChooser, File file) {
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.FileChooserUI
    public void rescanCurrentDirectory(JFileChooser jFileChooser) {
        getModel().validateFileCache();
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public PropertyChangeListener createPropertyChangeListener(JFileChooser jFileChooser) {
        return new PropertyChangeListener() { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.1
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws IllegalArgumentException {
                String propertyName = propertyChangeEvent.getPropertyName();
                if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                    File file = (File) propertyChangeEvent.getNewValue();
                    if (file != null) {
                        MotifFileChooserUI.this.setFileName(MotifFileChooserUI.this.getFileChooser().getName(file));
                        return;
                    }
                    return;
                }
                if (propertyName.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
                    File[] fileArr = (File[]) propertyChangeEvent.getNewValue();
                    JFileChooser fileChooser = MotifFileChooserUI.this.getFileChooser();
                    if (fileArr != null && fileArr.length > 0) {
                        if (fileArr.length > 1 || fileChooser.isDirectorySelectionEnabled() || !fileArr[0].isDirectory()) {
                            MotifFileChooserUI.this.setFileName(MotifFileChooserUI.this.fileNameString(fileArr));
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (propertyName.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
                    MotifFileChooserUI.this.fileList.clearSelection();
                    return;
                }
                if (propertyName.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                    MotifFileChooserUI.this.directoryList.clearSelection();
                    ListSelectionModel selectionModel = MotifFileChooserUI.this.directoryList.getSelectionModel();
                    if (selectionModel instanceof DefaultListSelectionModel) {
                        ((DefaultListSelectionModel) selectionModel).moveLeadSelectionIndex(0);
                        selectionModel.setAnchorSelectionIndex(0);
                    }
                    MotifFileChooserUI.this.fileList.clearSelection();
                    ListSelectionModel selectionModel2 = MotifFileChooserUI.this.fileList.getSelectionModel();
                    if (selectionModel2 instanceof DefaultListSelectionModel) {
                        ((DefaultListSelectionModel) selectionModel2).moveLeadSelectionIndex(0);
                        selectionModel2.setAnchorSelectionIndex(0);
                    }
                    if (MotifFileChooserUI.this.getFileChooser().getCurrentDirectory() != null) {
                        try {
                            MotifFileChooserUI.this.setDirectoryName(ShellFolder.getNormalizedFile((File) propertyChangeEvent.getNewValue()).getPath());
                        } catch (IOException e2) {
                            MotifFileChooserUI.this.setDirectoryName(((File) propertyChangeEvent.getNewValue()).getAbsolutePath());
                        }
                        if (MotifFileChooserUI.this.getFileChooser().getFileSelectionMode() == 1 && !MotifFileChooserUI.this.getFileChooser().isMultiSelectionEnabled()) {
                            MotifFileChooserUI.this.setFileName(MotifFileChooserUI.this.getDirectoryName());
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (propertyName.equals(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY)) {
                    if (MotifFileChooserUI.this.fileNameLabel != null) {
                        MotifFileChooserUI.this.populateFileNameLabel();
                    }
                    MotifFileChooserUI.this.directoryList.clearSelection();
                    return;
                }
                if (propertyName.equals(JFileChooser.MULTI_SELECTION_ENABLED_CHANGED_PROPERTY)) {
                    if (MotifFileChooserUI.this.getFileChooser().isMultiSelectionEnabled()) {
                        MotifFileChooserUI.this.fileList.setSelectionMode(2);
                        return;
                    }
                    MotifFileChooserUI.this.fileList.setSelectionMode(0);
                    MotifFileChooserUI.this.fileList.clearSelection();
                    MotifFileChooserUI.this.getFileChooser().setSelectedFiles(null);
                    return;
                }
                if (propertyName.equals(JFileChooser.ACCESSORY_CHANGED_PROPERTY)) {
                    if (MotifFileChooserUI.this.getAccessoryPanel() != null) {
                        if (propertyChangeEvent.getOldValue() != null) {
                            MotifFileChooserUI.this.getAccessoryPanel().remove((JComponent) propertyChangeEvent.getOldValue());
                        }
                        JComponent jComponent = (JComponent) propertyChangeEvent.getNewValue();
                        if (jComponent == null) {
                            MotifFileChooserUI.this.getAccessoryPanel().setPreferredSize(MotifFileChooserUI.ZERO_ACC_SIZE);
                            MotifFileChooserUI.this.getAccessoryPanel().setMaximumSize(MotifFileChooserUI.ZERO_ACC_SIZE);
                            return;
                        } else {
                            MotifFileChooserUI.this.getAccessoryPanel().add(jComponent, BorderLayout.CENTER);
                            MotifFileChooserUI.this.getAccessoryPanel().setPreferredSize(MotifFileChooserUI.PREF_ACC_SIZE);
                            MotifFileChooserUI.this.getAccessoryPanel().setMaximumSize(MotifFileChooserUI.MAX_SIZE);
                            return;
                        }
                    }
                    return;
                }
                if (propertyName.equals(JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY) || propertyName.equals(JFileChooser.APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY) || propertyName.equals(JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY)) {
                    MotifFileChooserUI.this.approveButton.setText(MotifFileChooserUI.this.getApproveButtonText(MotifFileChooserUI.this.getFileChooser()));
                    MotifFileChooserUI.this.approveButton.setToolTipText(MotifFileChooserUI.this.getApproveButtonToolTipText(MotifFileChooserUI.this.getFileChooser()));
                } else {
                    if (propertyName.equals(JFileChooser.CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY)) {
                        MotifFileChooserUI.this.doControlButtonsChanged(propertyChangeEvent);
                        return;
                    }
                    if (propertyName.equals("componentOrientation")) {
                        ComponentOrientation componentOrientation = (ComponentOrientation) propertyChangeEvent.getNewValue();
                        JFileChooser jFileChooser2 = (JFileChooser) propertyChangeEvent.getSource();
                        if (componentOrientation != ((ComponentOrientation) propertyChangeEvent.getOldValue())) {
                            jFileChooser2.applyComponentOrientation(componentOrientation);
                        }
                    }
                }
            }
        };
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifFileChooserUI((JFileChooser) jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        jComponent.removePropertyChangeListener(this.filterComboBoxModel);
        this.approveButton.removeActionListener(getApproveSelectionAction());
        this.filenameTextField.removeActionListener(getApproveSelectionAction());
        super.uninstallUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void installComponents(JFileChooser jFileChooser) throws IllegalArgumentException {
        jFileChooser.setLayout(new BorderLayout(10, 10));
        jFileChooser.setAlignmentX(0.5f);
        JPanel jPanel = new JPanel() { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.2
            @Override // javax.swing.JComponent, java.awt.Container
            public Insets getInsets() {
                return MotifFileChooserUI.insets;
            }
        };
        jPanel.setInheritsPopupMenu(true);
        align(jPanel);
        jPanel.setLayout(new BoxLayout(jPanel, 3));
        jFileChooser.add(jPanel, BorderLayout.CENTER);
        JLabel jLabel = new JLabel(this.pathLabelText);
        jLabel.setDisplayedMnemonic(this.pathLabelMnemonic);
        align(jLabel);
        jPanel.add(jLabel);
        File currentDirectory = jFileChooser.getCurrentDirectory();
        String path = null;
        if (currentDirectory != null) {
            path = currentDirectory.getPath();
        }
        this.pathField = new JTextField(path) { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.3
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                Dimension maximumSize = super.getMaximumSize();
                maximumSize.height = getPreferredSize().height;
                return maximumSize;
            }
        };
        this.pathField.setInheritsPopupMenu(true);
        jLabel.setLabelFor(this.pathField);
        align(this.pathField);
        this.pathField.addActionListener(getUpdateAction());
        jPanel.add(this.pathField);
        jPanel.add(Box.createRigidArea(vstrut10));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BoxLayout(jPanel2, 2));
        align(jPanel2);
        JComponent jPanel3 = new JPanel();
        jPanel3.setLayout(new BoxLayout(jPanel3, 3));
        align(jPanel3);
        JLabel jLabel2 = new JLabel(this.filterLabelText);
        jLabel2.setDisplayedMnemonic(this.filterLabelMnemonic);
        align(jLabel2);
        jPanel3.add(jLabel2);
        this.filterComboBox = new JComboBox<FileFilter>() { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.4
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                Dimension maximumSize = super.getMaximumSize();
                maximumSize.height = getPreferredSize().height;
                return maximumSize;
            }
        };
        this.filterComboBox.setInheritsPopupMenu(true);
        jLabel2.setLabelFor(this.filterComboBox);
        this.filterComboBoxModel = createFilterComboBoxModel();
        this.filterComboBox.setModel(this.filterComboBoxModel);
        this.filterComboBox.setRenderer(createFilterComboBoxRenderer());
        jFileChooser.addPropertyChangeListener(this.filterComboBoxModel);
        align(this.filterComboBox);
        jPanel3.add(this.filterComboBox);
        JLabel jLabel3 = new JLabel(this.foldersLabelText);
        jLabel3.setDisplayedMnemonic(this.foldersLabelMnemonic);
        align(jLabel3);
        jPanel3.add(jLabel3);
        JScrollPane jScrollPaneCreateDirectoryList = createDirectoryList();
        jScrollPaneCreateDirectoryList.getVerticalScrollBar().setFocusable(false);
        jScrollPaneCreateDirectoryList.getHorizontalScrollBar().setFocusable(false);
        jScrollPaneCreateDirectoryList.setInheritsPopupMenu(true);
        jLabel3.setLabelFor(jScrollPaneCreateDirectoryList.getViewport().getView());
        jPanel3.add(jScrollPaneCreateDirectoryList);
        jPanel3.setInheritsPopupMenu(true);
        JComponent jPanel4 = new JPanel();
        align(jPanel4);
        jPanel4.setLayout(new BoxLayout(jPanel4, 3));
        jPanel4.setInheritsPopupMenu(true);
        JLabel jLabel4 = new JLabel(this.filesLabelText);
        jLabel4.setDisplayedMnemonic(this.filesLabelMnemonic);
        align(jLabel4);
        jPanel4.add(jLabel4);
        JScrollPane jScrollPaneCreateFilesList = createFilesList();
        jLabel4.setLabelFor(jScrollPaneCreateFilesList.getViewport().getView());
        jPanel4.add(jScrollPaneCreateFilesList);
        jScrollPaneCreateFilesList.setInheritsPopupMenu(true);
        jPanel2.add(jPanel3);
        jPanel2.add(Box.createRigidArea(hstrut10));
        jPanel2.add(jPanel4);
        jPanel2.setInheritsPopupMenu(true);
        JPanel accessoryPanel = getAccessoryPanel();
        JComponent accessory = jFileChooser.getAccessory();
        if (accessoryPanel != null) {
            if (accessory == null) {
                accessoryPanel.setPreferredSize(ZERO_ACC_SIZE);
                accessoryPanel.setMaximumSize(ZERO_ACC_SIZE);
            } else {
                getAccessoryPanel().add(accessory, BorderLayout.CENTER);
                accessoryPanel.setPreferredSize(PREF_ACC_SIZE);
                accessoryPanel.setMaximumSize(MAX_SIZE);
            }
            align(accessoryPanel);
            jPanel2.add(accessoryPanel);
            accessoryPanel.setInheritsPopupMenu(true);
        }
        jPanel.add(jPanel2);
        jPanel.add(Box.createRigidArea(vstrut10));
        this.fileNameLabel = new JLabel();
        populateFileNameLabel();
        align(this.fileNameLabel);
        jPanel.add(this.fileNameLabel);
        this.filenameTextField = new JTextField() { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.5
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                Dimension maximumSize = super.getMaximumSize();
                maximumSize.height = getPreferredSize().height;
                return maximumSize;
            }
        };
        this.filenameTextField.setInheritsPopupMenu(true);
        this.fileNameLabel.setLabelFor(this.filenameTextField);
        this.filenameTextField.addActionListener(getApproveSelectionAction());
        align(this.filenameTextField);
        this.filenameTextField.setAlignmentX(0.0f);
        jPanel.add(this.filenameTextField);
        this.bottomPanel = getBottomPanel();
        this.bottomPanel.add(new JSeparator(), "North");
        JComponent jPanel5 = new JPanel();
        align(jPanel5);
        jPanel5.setLayout(new BoxLayout(jPanel5, 2));
        jPanel5.add(Box.createGlue());
        this.approveButton = new JButton(getApproveButtonText(jFileChooser)) { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.6
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                return new Dimension(MotifFileChooserUI.MAX_SIZE.width, getPreferredSize().height);
            }
        };
        this.approveButton.setMnemonic(getApproveButtonMnemonic(jFileChooser));
        this.approveButton.setToolTipText(getApproveButtonToolTipText(jFileChooser));
        this.approveButton.setInheritsPopupMenu(true);
        align(this.approveButton);
        this.approveButton.setMargin(buttonMargin);
        this.approveButton.addActionListener(getApproveSelectionAction());
        jPanel5.add(this.approveButton);
        jPanel5.add(Box.createGlue());
        JButton jButton = new JButton(this.updateButtonText) { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.7
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                return new Dimension(MotifFileChooserUI.MAX_SIZE.width, getPreferredSize().height);
            }
        };
        jButton.setMnemonic(this.updateButtonMnemonic);
        jButton.setToolTipText(this.updateButtonToolTipText);
        jButton.setInheritsPopupMenu(true);
        align(jButton);
        jButton.setMargin(buttonMargin);
        jButton.addActionListener(getUpdateAction());
        jPanel5.add(jButton);
        jPanel5.add(Box.createGlue());
        JButton jButton2 = new JButton(this.cancelButtonText) { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.8
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                return new Dimension(MotifFileChooserUI.MAX_SIZE.width, getPreferredSize().height);
            }
        };
        jButton2.setMnemonic(this.cancelButtonMnemonic);
        jButton2.setToolTipText(this.cancelButtonToolTipText);
        jButton2.setInheritsPopupMenu(true);
        align(jButton2);
        jButton2.setMargin(buttonMargin);
        jButton2.addActionListener(getCancelSelectionAction());
        jPanel5.add(jButton2);
        jPanel5.add(Box.createGlue());
        JButton jButton3 = new JButton(this.helpButtonText) { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.9
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                return new Dimension(MotifFileChooserUI.MAX_SIZE.width, getPreferredSize().height);
            }
        };
        jButton3.setMnemonic(this.helpButtonMnemonic);
        jButton3.setToolTipText(this.helpButtonToolTipText);
        align(jButton3);
        jButton3.setMargin(buttonMargin);
        jButton3.setEnabled(false);
        jButton3.setInheritsPopupMenu(true);
        jPanel5.add(jButton3);
        jPanel5.add(Box.createGlue());
        jPanel5.setInheritsPopupMenu(true);
        this.bottomPanel.add(jPanel5, "South");
        this.bottomPanel.setInheritsPopupMenu(true);
        if (jFileChooser.getControlButtonsAreShown()) {
            jFileChooser.add(this.bottomPanel, "South");
        }
    }

    protected JPanel getBottomPanel() {
        if (this.bottomPanel == null) {
            this.bottomPanel = new JPanel(new BorderLayout(0, 4));
        }
        return this.bottomPanel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doControlButtonsChanged(PropertyChangeEvent propertyChangeEvent) {
        if (getFileChooser().getControlButtonsAreShown()) {
            getFileChooser().add(this.bottomPanel, "South");
        } else {
            getFileChooser().remove(getBottomPanel());
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void uninstallComponents(JFileChooser jFileChooser) {
        jFileChooser.removeAll();
        this.bottomPanel = null;
        if (this.filterComboBoxModel != null) {
            jFileChooser.removePropertyChangeListener(this.filterComboBoxModel);
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void installStrings(JFileChooser jFileChooser) {
        super.installStrings(jFileChooser);
        Locale locale = jFileChooser.getLocale();
        this.enterFolderNameLabelText = UIManager.getString("FileChooser.enterFolderNameLabelText", locale);
        this.enterFolderNameLabelMnemonic = getMnemonic("FileChooser.enterFolderNameLabelMnemonic", locale).intValue();
        this.enterFileNameLabelText = UIManager.getString("FileChooser.enterFileNameLabelText", locale);
        this.enterFileNameLabelMnemonic = getMnemonic("FileChooser.enterFileNameLabelMnemonic", locale).intValue();
        this.filesLabelText = UIManager.getString("FileChooser.filesLabelText", locale);
        this.filesLabelMnemonic = getMnemonic("FileChooser.filesLabelMnemonic", locale).intValue();
        this.foldersLabelText = UIManager.getString("FileChooser.foldersLabelText", locale);
        this.foldersLabelMnemonic = getMnemonic("FileChooser.foldersLabelMnemonic", locale).intValue();
        this.pathLabelText = UIManager.getString("FileChooser.pathLabelText", locale);
        this.pathLabelMnemonic = getMnemonic("FileChooser.pathLabelMnemonic", locale).intValue();
        this.filterLabelText = UIManager.getString("FileChooser.filterLabelText", locale);
        this.filterLabelMnemonic = getMnemonic("FileChooser.filterLabelMnemonic", locale).intValue();
    }

    private Integer getMnemonic(String str, Locale locale) {
        return Integer.valueOf(SwingUtilities2.getUIDefaultsInt(str, locale));
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void installIcons(JFileChooser jFileChooser) {
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected void uninstallIcons(JFileChooser jFileChooser) {
    }

    protected JScrollPane createFilesList() {
        this.fileList = new JList<>();
        if (getFileChooser().isMultiSelectionEnabled()) {
            this.fileList.setSelectionMode(2);
        } else {
            this.fileList.setSelectionMode(0);
        }
        this.fileList.setModel(new MotifFileListModel());
        this.fileList.getSelectionModel().removeSelectionInterval(0, 0);
        this.fileList.setCellRenderer(new FileCellRenderer());
        this.fileList.addListSelectionListener(createListSelectionListener(getFileChooser()));
        this.fileList.addMouseListener(createDoubleClickListener(getFileChooser(), this.fileList));
        this.fileList.addMouseListener(new MouseAdapter() { // from class: com.sun.java.swing.plaf.motif.MotifFileChooserUI.10
            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mouseClicked(MouseEvent mouseEvent) {
                int iLoc2IndexFileList;
                JFileChooser fileChooser = MotifFileChooserUI.this.getFileChooser();
                if (SwingUtilities.isLeftMouseButton(mouseEvent) && !fileChooser.isMultiSelectionEnabled() && (iLoc2IndexFileList = SwingUtilities2.loc2IndexFileList(MotifFileChooserUI.this.fileList, mouseEvent.getPoint())) >= 0) {
                    MotifFileChooserUI.this.setFileName(fileChooser.getName(MotifFileChooserUI.this.fileList.getModel().getElementAt(iLoc2IndexFileList)));
                }
            }
        });
        align(this.fileList);
        JScrollPane jScrollPane = new JScrollPane(this.fileList);
        jScrollPane.setPreferredSize(prefListSize);
        jScrollPane.setMaximumSize(MAX_SIZE);
        align(jScrollPane);
        this.fileList.setInheritsPopupMenu(true);
        jScrollPane.setInheritsPopupMenu(true);
        return jScrollPane;
    }

    protected JScrollPane createDirectoryList() {
        this.directoryList = new JList<>();
        align(this.directoryList);
        this.directoryList.setCellRenderer(new DirectoryCellRenderer());
        this.directoryList.setModel(new MotifDirectoryListModel());
        this.directoryList.getSelectionModel().removeSelectionInterval(0, 0);
        this.directoryList.addMouseListener(createDoubleClickListener(getFileChooser(), this.directoryList));
        this.directoryList.addListSelectionListener(createListSelectionListener(getFileChooser()));
        this.directoryList.setInheritsPopupMenu(true);
        JScrollPane jScrollPane = new JScrollPane(this.directoryList);
        jScrollPane.setMaximumSize(MAX_SIZE);
        jScrollPane.setPreferredSize(prefListSize);
        jScrollPane.setInheritsPopupMenu(true);
        align(jScrollPane);
        return jScrollPane;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension dimension = getFileChooser().getAccessory() != null ? WITH_ACCELERATOR_PREF_SIZE : PREF_SIZE;
        Dimension dimensionPreferredLayoutSize = jComponent.getLayout().preferredLayoutSize(jComponent);
        if (dimensionPreferredLayoutSize != null) {
            return new Dimension(dimensionPreferredLayoutSize.width < dimension.width ? dimension.width : dimensionPreferredLayoutSize.width, dimensionPreferredLayoutSize.height < dimension.height ? dimension.height : dimensionPreferredLayoutSize.height);
        }
        return dimension;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return new Dimension(200, 300);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    protected void align(JComponent jComponent) {
        jComponent.setAlignmentX(0.0f);
        jComponent.setAlignmentY(0.0f);
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifFileChooserUI$FileCellRenderer.class */
    protected class FileCellRenderer extends DefaultListCellRenderer {
        protected FileCellRenderer() {
        }

        @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
        public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            super.getListCellRendererComponent(jList, obj, i2, z2, z3);
            setText(MotifFileChooserUI.this.getFileChooser().getName((File) obj));
            setInheritsPopupMenu(true);
            return this;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifFileChooserUI$DirectoryCellRenderer.class */
    protected class DirectoryCellRenderer extends DefaultListCellRenderer {
        protected DirectoryCellRenderer() {
        }

        @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
        public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            super.getListCellRendererComponent(jList, obj, i2, z2, z3);
            setText(MotifFileChooserUI.this.getFileChooser().getName((File) obj));
            setInheritsPopupMenu(true);
            return this;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifFileChooserUI$MotifDirectoryListModel.class */
    protected class MotifDirectoryListModel extends AbstractListModel<File> implements ListDataListener {
        public MotifDirectoryListModel() {
            MotifFileChooserUI.this.getModel().addListDataListener(this);
        }

        @Override // javax.swing.ListModel
        public int getSize() {
            return MotifFileChooserUI.this.getModel().getDirectories().size();
        }

        @Override // javax.swing.ListModel
        public File getElementAt(int i2) {
            return MotifFileChooserUI.this.getModel().getDirectories().elementAt(i2);
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
            fireIntervalAdded(this, listDataEvent.getIndex0(), listDataEvent.getIndex1());
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
            fireIntervalRemoved(this, listDataEvent.getIndex0(), listDataEvent.getIndex1());
        }

        public void fireContentsChanged() {
            fireContentsChanged(this, 0, MotifFileChooserUI.this.getModel().getDirectories().size() - 1);
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
            fireContentsChanged();
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifFileChooserUI$MotifFileListModel.class */
    protected class MotifFileListModel extends AbstractListModel<File> implements ListDataListener {
        public MotifFileListModel() {
            MotifFileChooserUI.this.getModel().addListDataListener(this);
        }

        @Override // javax.swing.ListModel
        public int getSize() {
            return MotifFileChooserUI.this.getModel().getFiles().size();
        }

        public boolean contains(Object obj) {
            return MotifFileChooserUI.this.getModel().getFiles().contains(obj);
        }

        public int indexOf(Object obj) {
            return MotifFileChooserUI.this.getModel().getFiles().indexOf(obj);
        }

        @Override // javax.swing.ListModel
        public File getElementAt(int i2) {
            return MotifFileChooserUI.this.getModel().getFiles().elementAt(i2);
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalAdded(ListDataEvent listDataEvent) {
            fireIntervalAdded(this, listDataEvent.getIndex0(), listDataEvent.getIndex1());
        }

        @Override // javax.swing.event.ListDataListener
        public void intervalRemoved(ListDataEvent listDataEvent) {
            fireIntervalRemoved(this, listDataEvent.getIndex0(), listDataEvent.getIndex1());
        }

        public void fireContentsChanged() {
            fireContentsChanged(this, 0, MotifFileChooserUI.this.getModel().getFiles().size() - 1);
        }

        @Override // javax.swing.event.ListDataListener
        public void contentsChanged(ListDataEvent listDataEvent) {
            fireContentsChanged();
        }
    }

    protected FilterComboBoxModel createFilterComboBoxModel() {
        return new FilterComboBoxModel();
    }

    protected FilterComboBoxRenderer createFilterComboBoxRenderer() {
        return new FilterComboBoxRenderer();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifFileChooserUI$FilterComboBoxRenderer.class */
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

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifFileChooserUI$FilterComboBoxModel.class */
    protected class FilterComboBoxModel extends AbstractListModel<FileFilter> implements ComboBoxModel<FileFilter>, PropertyChangeListener {
        protected FileFilter[] filters;

        protected FilterComboBoxModel() {
            this.filters = MotifFileChooserUI.this.getFileChooser().getChoosableFileFilters();
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName.equals(JFileChooser.CHOOSABLE_FILE_FILTER_CHANGED_PROPERTY)) {
                this.filters = (FileFilter[]) propertyChangeEvent.getNewValue();
                fireContentsChanged(this, -1, -1);
            } else if (propertyName.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override // javax.swing.ComboBoxModel
        public void setSelectedItem(Object obj) {
            if (obj != null) {
                MotifFileChooserUI.this.getFileChooser().setFileFilter((FileFilter) obj);
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override // javax.swing.ComboBoxModel
        public Object getSelectedItem() {
            FileFilter fileFilter = MotifFileChooserUI.this.getFileChooser().getFileFilter();
            boolean z2 = false;
            if (fileFilter != null) {
                for (FileFilter fileFilter2 : this.filters) {
                    if (fileFilter2 == fileFilter) {
                        z2 = true;
                    }
                }
                if (!z2) {
                    MotifFileChooserUI.this.getFileChooser().addChoosableFileFilter(fileFilter);
                }
            }
            return MotifFileChooserUI.this.getFileChooser().getFileFilter();
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
                return MotifFileChooserUI.this.getFileChooser().getFileFilter();
            }
            if (this.filters != null) {
                return this.filters[i2];
            }
            return null;
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected JButton getApproveButton(JFileChooser jFileChooser) {
        return this.approveButton;
    }
}
