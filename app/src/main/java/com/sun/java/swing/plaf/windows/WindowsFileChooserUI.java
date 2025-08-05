package com.sun.java.swing.plaf.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;
import javax.accessibility.AccessibleContext;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultButtonModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicDirectoryModel;
import javax.swing.plaf.basic.BasicFileChooserUI;
import org.apache.commons.net.ftp.FTPReply;
import org.icepdf.core.util.PdfOps;
import sun.awt.shell.ShellFolder;
import sun.swing.FilePane;
import sun.swing.SwingUtilities2;
import sun.swing.WindowsPlacesBar;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI.class */
public class WindowsFileChooserUI extends BasicFileChooserUI {
    private JPanel centerPanel;
    private JLabel lookInLabel;
    private JComboBox<File> directoryComboBox;
    private DirectoryComboBoxModel directoryComboBoxModel;
    private ActionListener directoryComboBoxAction;
    private FilterComboBoxModel filterComboBoxModel;
    private JTextField filenameTextField;
    private FilePane filePane;
    private WindowsPlacesBar placesBar;
    private JButton approveButton;
    private JButton cancelButton;
    private JPanel buttonPanel;
    private JPanel bottomPanel;
    private JComboBox<FileFilter> filterComboBox;
    private static final Dimension hstrut10 = new Dimension(10, 1);
    private static final Dimension vstrut4 = new Dimension(1, 4);
    private static final Dimension vstrut6 = new Dimension(1, 6);
    private static final Dimension vstrut8 = new Dimension(1, 8);
    private static final Insets shrinkwrap = new Insets(0, 0, 0, 0);
    private static int PREF_WIDTH = FTPReply.CANNOT_OPEN_DATA_CONNECTION;
    private static int PREF_HEIGHT = 245;
    private static Dimension PREF_SIZE = new Dimension(PREF_WIDTH, PREF_HEIGHT);
    private static int MIN_WIDTH = FTPReply.CANNOT_OPEN_DATA_CONNECTION;
    private static int MIN_HEIGHT = 245;
    private static int LIST_PREF_WIDTH = 444;
    private static int LIST_PREF_HEIGHT = 138;
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
    private String newFolderToolTipText;
    private String newFolderAccessibleName;
    private String viewMenuButtonToolTipText;
    private String viewMenuButtonAccessibleName;
    private BasicFileChooserUI.BasicFileView fileView;
    private JLabel fileNameLabel;
    static final int space = 10;

    private void populateFileNameLabel() throws IllegalArgumentException {
        if (getFileChooser().getFileSelectionMode() == 1) {
            this.fileNameLabel.setText(this.folderNameLabelText);
            this.fileNameLabel.setDisplayedMnemonic(this.folderNameLabelMnemonic);
        } else {
            this.fileNameLabel.setText(this.fileNameLabelText);
            this.fileNameLabel.setDisplayedMnemonic(this.fileNameLabelMnemonic);
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsFileChooserUI((JFileChooser) jComponent);
    }

    public WindowsFileChooserUI(JFileChooser jFileChooser) {
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
        this.newFolderToolTipText = null;
        this.newFolderAccessibleName = null;
        this.viewMenuButtonToolTipText = null;
        this.viewMenuButtonAccessibleName = null;
        this.fileView = new WindowsFileView();
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void uninstallComponents(JFileChooser jFileChooser) {
        jFileChooser.removeAll();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$WindowsFileChooserUIAccessor.class */
    private class WindowsFileChooserUIAccessor implements FilePane.FileChooserUIAccessor {
        private WindowsFileChooserUIAccessor() {
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public JFileChooser getFileChooser() {
            return WindowsFileChooserUI.this.getFileChooser();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public BasicDirectoryModel getModel() {
            return WindowsFileChooserUI.this.getModel();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public JPanel createList() {
            return WindowsFileChooserUI.this.createList(getFileChooser());
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public JPanel createDetailsView() {
            return WindowsFileChooserUI.this.createDetailsView(getFileChooser());
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public boolean isDirectorySelected() {
            return WindowsFileChooserUI.this.isDirectorySelected();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public File getDirectory() {
            return WindowsFileChooserUI.this.getDirectory();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public Action getChangeToParentDirectoryAction() {
            return WindowsFileChooserUI.this.getChangeToParentDirectoryAction();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public Action getApproveSelectionAction() {
            return WindowsFileChooserUI.this.getApproveSelectionAction();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public Action getNewFolderAction() {
            return WindowsFileChooserUI.this.getNewFolderAction();
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public MouseListener createDoubleClickListener(JList jList) {
            return WindowsFileChooserUI.this.createDoubleClickListener(getFileChooser(), jList);
        }

        @Override // sun.swing.FilePane.FileChooserUIAccessor
        public ListSelectionListener createListSelectionListener() {
            return WindowsFileChooserUI.this.createListSelectionListener(getFileChooser());
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    public void installComponents(JFileChooser jFileChooser) throws IllegalArgumentException {
        this.filePane = new FilePane(new WindowsFileChooserUIAccessor());
        jFileChooser.addPropertyChangeListener(this.filePane);
        jFileChooser.getFileSystemView();
        jFileChooser.setBorder(new EmptyBorder(4, 10, 10, 10));
        jFileChooser.setLayout(new BorderLayout(8, 8));
        updateUseShellFolder();
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
        jToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        jFileChooser.add(jToolBar, "North");
        this.lookInLabel = new JLabel(this.lookInLabelText, 11) { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.1
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getPreferredSize() {
                return getMinimumSize();
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMinimumSize() {
                Dimension preferredSize = super.getPreferredSize();
                if (WindowsFileChooserUI.this.placesBar != null) {
                    preferredSize.width = Math.max(preferredSize.width, WindowsFileChooserUI.this.placesBar.getWidth());
                }
                return preferredSize;
            }
        };
        this.lookInLabel.setDisplayedMnemonic(this.lookInLabelMnemonic);
        this.lookInLabel.setAlignmentX(0.0f);
        this.lookInLabel.setAlignmentY(0.5f);
        jToolBar.add(this.lookInLabel);
        jToolBar.add(Box.createRigidArea(new Dimension(8, 0)));
        this.directoryComboBox = new JComboBox<File>() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.2
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMinimumSize() {
                Dimension minimumSize = super.getMinimumSize();
                minimumSize.width = 60;
                return minimumSize;
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getPreferredSize() {
                Dimension preferredSize = super.getPreferredSize();
                preferredSize.width = 150;
                return preferredSize;
            }
        };
        this.directoryComboBox.putClientProperty("JComboBox.lightweightKeyboardNavigation", "Lightweight");
        this.lookInLabel.setLabelFor(this.directoryComboBox);
        this.directoryComboBoxModel = createDirectoryComboBoxModel(jFileChooser);
        this.directoryComboBox.setModel(this.directoryComboBoxModel);
        this.directoryComboBox.addActionListener(this.directoryComboBoxAction);
        this.directoryComboBox.setRenderer(createDirectoryComboBoxRenderer(jFileChooser));
        this.directoryComboBox.setAlignmentX(0.0f);
        this.directoryComboBox.setAlignmentY(0.5f);
        this.directoryComboBox.setMaximumRowCount(8);
        jToolBar.add(this.directoryComboBox);
        jToolBar.add(Box.createRigidArea(hstrut10));
        jToolBar.add(createToolButton(getChangeToParentDirectoryAction(), this.upFolderIcon, this.upFolderToolTipText, this.upFolderAccessibleName));
        if (!UIManager.getBoolean("FileChooser.readOnly")) {
            jToolBar.add(createToolButton(this.filePane.getNewFolderAction(), this.newFolderIcon, this.newFolderToolTipText, this.newFolderAccessibleName));
        }
        ButtonGroup buttonGroup = new ButtonGroup();
        final JPopupMenu jPopupMenu = new JPopupMenu();
        final JRadioButtonMenuItem jRadioButtonMenuItem = new JRadioButtonMenuItem(this.filePane.getViewTypeAction(0));
        jRadioButtonMenuItem.setSelected(this.filePane.getViewType() == 0);
        jPopupMenu.add((JMenuItem) jRadioButtonMenuItem);
        buttonGroup.add(jRadioButtonMenuItem);
        final JRadioButtonMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem(this.filePane.getViewTypeAction(1));
        jRadioButtonMenuItem2.setSelected(this.filePane.getViewType() == 1);
        jPopupMenu.add((JMenuItem) jRadioButtonMenuItem2);
        buttonGroup.add(jRadioButtonMenuItem2);
        BufferedImage bufferedImage = new BufferedImage(this.viewMenuIcon.getIconWidth() + 7, this.viewMenuIcon.getIconHeight(), 2);
        Graphics graphics = bufferedImage.getGraphics();
        this.viewMenuIcon.paintIcon(this.filePane, graphics, 0, 0);
        int width = bufferedImage.getWidth() - 5;
        int height = (bufferedImage.getHeight() / 2) - 1;
        graphics.setColor(Color.BLACK);
        graphics.fillPolygon(new int[]{width, width + 5, width + 2}, new int[]{height, height, height + 3}, 3);
        final JButton jButtonCreateToolButton = createToolButton(null, new ImageIcon(bufferedImage), this.viewMenuButtonToolTipText, this.viewMenuButtonAccessibleName);
        jButtonCreateToolButton.addMouseListener(new MouseAdapter() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.3
            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mousePressed(MouseEvent mouseEvent) {
                if (SwingUtilities.isLeftMouseButton(mouseEvent) && !jButtonCreateToolButton.isSelected()) {
                    jButtonCreateToolButton.setSelected(true);
                    jPopupMenu.show(jButtonCreateToolButton, 0, jButtonCreateToolButton.getHeight());
                }
            }
        });
        jButtonCreateToolButton.addKeyListener(new KeyAdapter() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.4
            @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == 32 && jButtonCreateToolButton.getModel().isRollover()) {
                    jButtonCreateToolButton.setSelected(true);
                    jPopupMenu.show(jButtonCreateToolButton, 0, jButtonCreateToolButton.getHeight());
                }
            }
        });
        jPopupMenu.addPopupMenuListener(new PopupMenuListener() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.5
            @Override // javax.swing.event.PopupMenuListener
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
            }

            @Override // javax.swing.event.PopupMenuListener
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
                SwingUtilities.invokeLater(new Runnable() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        jButtonCreateToolButton.setSelected(false);
                    }
                });
            }

            @Override // javax.swing.event.PopupMenuListener
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
            }
        });
        jToolBar.add(jButtonCreateToolButton);
        jToolBar.add(Box.createRigidArea(new Dimension(80, 0)));
        this.filePane.addPropertyChangeListener(new PropertyChangeListener() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.6
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if ("viewType".equals(propertyChangeEvent.getPropertyName())) {
                    switch (WindowsFileChooserUI.this.filePane.getViewType()) {
                        case 0:
                            jRadioButtonMenuItem.setSelected(true);
                            break;
                        case 1:
                            jRadioButtonMenuItem2.setSelected(true);
                            break;
                    }
                }
            }
        });
        this.centerPanel = new JPanel(new BorderLayout());
        this.centerPanel.add(getAccessoryPanel(), "After");
        JComponent accessory = jFileChooser.getAccessory();
        if (accessory != null) {
            getAccessoryPanel().add(accessory);
        }
        this.filePane.setPreferredSize(LIST_PREF_SIZE);
        this.centerPanel.add(this.filePane, BorderLayout.CENTER);
        jFileChooser.add(this.centerPanel, BorderLayout.CENTER);
        getBottomPanel().setLayout(new BoxLayout(getBottomPanel(), 2));
        this.centerPanel.add(getBottomPanel(), "South");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 3));
        jPanel.add(Box.createRigidArea(vstrut4));
        this.fileNameLabel = new JLabel();
        populateFileNameLabel();
        this.fileNameLabel.setAlignmentY(0.0f);
        jPanel.add(this.fileNameLabel);
        jPanel.add(Box.createRigidArea(new Dimension(1, 12)));
        JLabel jLabel = new JLabel(this.filesOfTypeLabelText);
        jLabel.setDisplayedMnemonic(this.filesOfTypeLabelMnemonic);
        jPanel.add(jLabel);
        getBottomPanel().add(jPanel);
        getBottomPanel().add(Box.createRigidArea(new Dimension(15, 0)));
        JPanel jPanel2 = new JPanel();
        jPanel2.add(Box.createRigidArea(vstrut8));
        jPanel2.setLayout(new BoxLayout(jPanel2, 1));
        this.filenameTextField = new JTextField(35) { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.7
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                return new Dimension(Short.MAX_VALUE, super.getPreferredSize().height);
            }
        };
        this.fileNameLabel.setLabelFor(this.filenameTextField);
        this.filenameTextField.addFocusListener(new FocusAdapter() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.8
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusGained(FocusEvent focusEvent) {
                if (!WindowsFileChooserUI.this.getFileChooser().isMultiSelectionEnabled()) {
                    WindowsFileChooserUI.this.filePane.clearSelection();
                }
            }
        });
        if (jFileChooser.isMultiSelectionEnabled()) {
            setFileName(fileNameString(jFileChooser.getSelectedFiles()));
        } else {
            setFileName(fileNameString(jFileChooser.getSelectedFile()));
        }
        jPanel2.add(this.filenameTextField);
        jPanel2.add(Box.createRigidArea(vstrut8));
        this.filterComboBoxModel = createFilterComboBoxModel();
        jFileChooser.addPropertyChangeListener(this.filterComboBoxModel);
        this.filterComboBox = new JComboBox<>(this.filterComboBoxModel);
        jLabel.setLabelFor(this.filterComboBox);
        this.filterComboBox.setRenderer(createFilterComboBoxRenderer());
        jPanel2.add(this.filterComboBox);
        getBottomPanel().add(jPanel2);
        getBottomPanel().add(Box.createRigidArea(new Dimension(30, 0)));
        getButtonPanel().setLayout(new BoxLayout(getButtonPanel(), 1));
        this.approveButton = new JButton(getApproveButtonText(jFileChooser)) { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.9
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                return WindowsFileChooserUI.this.approveButton.getPreferredSize().width > WindowsFileChooserUI.this.cancelButton.getPreferredSize().width ? WindowsFileChooserUI.this.approveButton.getPreferredSize() : WindowsFileChooserUI.this.cancelButton.getPreferredSize();
            }
        };
        Insets margin = this.approveButton.getMargin();
        InsetsUIResource insetsUIResource = new InsetsUIResource(margin.top, margin.left + 5, margin.bottom, margin.right + 5);
        this.approveButton.setMargin(insetsUIResource);
        this.approveButton.setMnemonic(getApproveButtonMnemonic(jFileChooser));
        this.approveButton.addActionListener(getApproveSelectionAction());
        this.approveButton.setToolTipText(getApproveButtonToolTipText(jFileChooser));
        getButtonPanel().add(Box.createRigidArea(vstrut6));
        getButtonPanel().add(this.approveButton);
        getButtonPanel().add(Box.createRigidArea(vstrut4));
        this.cancelButton = new JButton(this.cancelButtonText) { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.10
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public Dimension getMaximumSize() {
                return WindowsFileChooserUI.this.approveButton.getPreferredSize().width > WindowsFileChooserUI.this.cancelButton.getPreferredSize().width ? WindowsFileChooserUI.this.approveButton.getPreferredSize() : WindowsFileChooserUI.this.cancelButton.getPreferredSize();
            }
        };
        this.cancelButton.setMargin(insetsUIResource);
        this.cancelButton.setToolTipText(this.cancelButtonToolTipText);
        this.cancelButton.addActionListener(getCancelSelectionAction());
        getButtonPanel().add(this.cancelButton);
        if (jFileChooser.getControlButtonsAreShown()) {
            addControlButtons();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUseShellFolder() {
        JFileChooser fileChooser = getFileChooser();
        if (FilePane.usesShellFolder(fileChooser)) {
            if (this.placesBar == null && !UIManager.getBoolean("FileChooser.noPlacesBar")) {
                this.placesBar = new WindowsPlacesBar(fileChooser, XPStyle.getXP() != null);
                fileChooser.add(this.placesBar, "Before");
                fileChooser.addPropertyChangeListener(this.placesBar);
                return;
            }
            return;
        }
        if (this.placesBar != null) {
            fileChooser.remove(this.placesBar);
            fileChooser.removePropertyChangeListener(this.placesBar);
            this.placesBar = null;
        }
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
        this.newFolderToolTipText = UIManager.getString("FileChooser.newFolderToolTipText", locale);
        this.newFolderAccessibleName = UIManager.getString("FileChooser.newFolderAccessibleName", locale);
        this.viewMenuButtonToolTipText = UIManager.getString("FileChooser.viewMenuButtonToolTipText", locale);
        this.viewMenuButtonAccessibleName = UIManager.getString("FileChooser.viewMenuButtonAccessibleName", locale);
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

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$WindowsNewFolderAction.class */
    protected class WindowsNewFolderAction extends BasicFileChooserUI.NewFolderAction {
        protected WindowsNewFolderAction() {
            super();
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$SingleClickListener.class */
    protected class SingleClickListener extends MouseAdapter {
        protected SingleClickListener() {
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$FileRenderer.class */
    protected class FileRenderer extends DefaultListCellRenderer {
        protected FileRenderer() {
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        jComponent.removePropertyChangeListener(this.filterComboBoxModel);
        jComponent.removePropertyChangeListener(this.filePane);
        if (this.placesBar != null) {
            jComponent.removePropertyChangeListener(this.placesBar);
        }
        this.cancelButton.removeActionListener(getCancelSelectionAction());
        this.approveButton.removeActionListener(getApproveSelectionAction());
        this.filenameTextField.removeActionListener(getApproveSelectionAction());
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
    public void doFileSelectionModeChanged(PropertyChangeEvent propertyChangeEvent) throws IllegalArgumentException {
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
        this.approveButton.setMnemonic(getApproveButtonMnemonic(fileChooser));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doDialogTypeChanged(PropertyChangeEvent propertyChangeEvent) throws IllegalArgumentException {
        JFileChooser fileChooser = getFileChooser();
        this.approveButton.setText(getApproveButtonText(fileChooser));
        this.approveButton.setToolTipText(getApproveButtonToolTipText(fileChooser));
        this.approveButton.setMnemonic(getApproveButtonMnemonic(fileChooser));
        if (fileChooser.getDialogType() == 1) {
            this.lookInLabel.setText(this.saveInLabelText);
        } else {
            this.lookInLabel.setText(this.lookInLabelText);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doApproveButtonMnemonicChanged(PropertyChangeEvent propertyChangeEvent) {
        this.approveButton.setMnemonic(getApproveButtonMnemonic(getFileChooser()));
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
        return new PropertyChangeListener() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.11
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws IllegalArgumentException {
                String propertyName = propertyChangeEvent.getPropertyName();
                if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doSelectedFileChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doSelectedFilesChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doDirectoryChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.FILE_FILTER_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doFilterChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.FILE_SELECTION_MODE_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doFileSelectionModeChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.ACCESSORY_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doAccessoryChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.APPROVE_BUTTON_TEXT_CHANGED_PROPERTY) || propertyName.equals(JFileChooser.APPROVE_BUTTON_TOOL_TIP_TEXT_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doApproveButtonTextChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.DIALOG_TYPE_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doDialogTypeChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.APPROVE_BUTTON_MNEMONIC_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doApproveButtonMnemonicChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName.equals(JFileChooser.CONTROL_BUTTONS_ARE_SHOWN_CHANGED_PROPERTY)) {
                    WindowsFileChooserUI.this.doControlButtonsChanged(propertyChangeEvent);
                    return;
                }
                if (propertyName == "FileChooser.useShellFolder") {
                    WindowsFileChooserUI.this.updateUseShellFolder();
                    WindowsFileChooserUI.this.doDirectoryChanged(propertyChangeEvent);
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
                if (propertyName.equals("ancestor") && propertyChangeEvent.getOldValue() == null && propertyChangeEvent.getNewValue() != null) {
                    WindowsFileChooserUI.this.filenameTextField.selectAll();
                    WindowsFileChooserUI.this.filenameTextField.requestFocus();
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
    protected void setDirectorySelected(boolean z2) {
        super.setDirectorySelected(z2);
        JFileChooser fileChooser = getFileChooser();
        if (z2) {
            this.approveButton.setText(this.directoryOpenButtonText);
            this.approveButton.setToolTipText(this.directoryOpenButtonToolTipText);
            this.approveButton.setMnemonic(this.directoryOpenButtonMnemonic);
        } else {
            this.approveButton.setText(getApproveButtonText(fileChooser));
            this.approveButton.setToolTipText(getApproveButtonToolTipText(fileChooser));
            this.approveButton.setMnemonic(getApproveButtonMnemonic(fileChooser));
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

    private static JButton createToolButton(Action action, Icon icon, String str, String str2) {
        final JButton jButton = new JButton(action);
        jButton.setText(null);
        jButton.setIcon(icon);
        jButton.setToolTipText(str);
        jButton.setRequestFocusEnabled(false);
        jButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, str2);
        jButton.putClientProperty(WindowsLookAndFeel.HI_RES_DISABLED_ICON_CLIENT_KEY, Boolean.TRUE);
        jButton.setAlignmentX(0.0f);
        jButton.setAlignmentY(0.5f);
        jButton.setMargin(shrinkwrap);
        jButton.setFocusPainted(false);
        jButton.setModel(new DefaultButtonModel() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.12
            @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
            public void setPressed(boolean z2) {
                if (!z2 || isRollover()) {
                    super.setPressed(z2);
                }
            }

            @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
            public void setRollover(boolean z2) {
                if (z2 && !isRollover()) {
                    for (Component component : jButton.getParent().getComponents()) {
                        if ((component instanceof JButton) && component != jButton) {
                            ((JButton) component).getModel().setRollover(false);
                        }
                    }
                }
                super.setRollover(z2);
            }

            @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
            public void setSelected(boolean z2) {
                super.setSelected(z2);
                if (z2) {
                    this.stateMask |= 5;
                } else {
                    this.stateMask &= -6;
                }
            }
        });
        jButton.addFocusListener(new FocusAdapter() { // from class: com.sun.java.swing.plaf.windows.WindowsFileChooserUI.13
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusGained(FocusEvent focusEvent) {
                jButton.getModel().setRollover(true);
            }

            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusLost(FocusEvent focusEvent) {
                jButton.getModel().setRollover(false);
            }
        });
        return jButton;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$DirectoryComboBoxRenderer.class */
    class DirectoryComboBoxRenderer extends DefaultListCellRenderer {
        IndentIcon ii;

        DirectoryComboBoxRenderer() {
            this.ii = WindowsFileChooserUI.this.new IndentIcon();
        }

        @Override // javax.swing.DefaultListCellRenderer, javax.swing.ListCellRenderer
        public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            super.getListCellRendererComponent(jList, obj, i2, z2, z3);
            if (obj == null) {
                setText("");
                return this;
            }
            File file = (File) obj;
            setText(WindowsFileChooserUI.this.getFileChooser().getName(file));
            this.ii.icon = WindowsFileChooserUI.this.getFileChooser().getIcon(file);
            this.ii.depth = WindowsFileChooserUI.this.directoryComboBoxModel.getDepth(i2);
            setIcon(this.ii);
            return this;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$IndentIcon.class */
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

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$DirectoryComboBoxModel.class */
    protected class DirectoryComboBoxModel extends AbstractListModel<File> implements ComboBoxModel<File> {
        Vector<File> directories = new Vector<>();
        int[] depths = null;
        File selectedDirectory = null;
        JFileChooser chooser;
        FileSystemView fsv;

        public DirectoryComboBoxModel() {
            this.chooser = WindowsFileChooserUI.this.getFileChooser();
            this.fsv = this.chooser.getFileSystemView();
            File currentDirectory = WindowsFileChooserUI.this.getFileChooser().getCurrentDirectory();
            if (currentDirectory != null) {
                addItem(currentDirectory);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public void addItem(File file) {
            File[] roots;
            File canonicalFile;
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
                canonicalFile = file.getCanonicalFile();
            } catch (IOException e2) {
                canonicalFile = file;
            }
            if (zUsesShellFolder) {
                try {
                    shellFolder = ShellFolder.getShellFolder(canonicalFile);
                } catch (FileNotFoundException e3) {
                    calculateDepths();
                    return;
                }
            } else {
                shellFolder = canonicalFile;
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
        public File getElementAt(int i2) {
            return this.directories.elementAt(i2);
        }
    }

    protected FilterComboBoxRenderer createFilterComboBoxRenderer() {
        return new FilterComboBoxRenderer();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$FilterComboBoxRenderer.class */
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

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$FilterComboBoxModel.class */
    protected class FilterComboBoxModel extends AbstractListModel<FileFilter> implements ComboBoxModel<FileFilter>, PropertyChangeListener {
        protected FileFilter[] filters;

        protected FilterComboBoxModel() {
            this.filters = WindowsFileChooserUI.this.getFileChooser().getChoosableFileFilters();
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
                WindowsFileChooserUI.this.getFileChooser().setFileFilter((FileFilter) obj);
                fireContentsChanged(this, -1, -1);
            }
        }

        @Override // javax.swing.ComboBoxModel
        public Object getSelectedItem() {
            FileFilter fileFilter = WindowsFileChooserUI.this.getFileChooser().getFileFilter();
            boolean z2 = false;
            if (fileFilter != null) {
                for (FileFilter fileFilter2 : this.filters) {
                    if (fileFilter2 == fileFilter) {
                        z2 = true;
                    }
                }
                if (!z2) {
                    WindowsFileChooserUI.this.getFileChooser().addChoosableFileFilter(fileFilter);
                }
            }
            return WindowsFileChooserUI.this.getFileChooser().getFileFilter();
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
                return WindowsFileChooserUI.this.getFileChooser().getFileFilter();
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

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$DirectoryComboBoxAction.class */
    protected class DirectoryComboBoxAction implements ActionListener {
        protected DirectoryComboBoxAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            WindowsFileChooserUI.this.getFileChooser().setCurrentDirectory((File) WindowsFileChooserUI.this.directoryComboBox.getSelectedItem());
        }
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI
    protected JButton getApproveButton(JFileChooser jFileChooser) {
        return this.approveButton;
    }

    @Override // javax.swing.plaf.basic.BasicFileChooserUI, javax.swing.plaf.FileChooserUI
    public FileView getFileView(JFileChooser jFileChooser) {
        return this.fileView;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsFileChooserUI$WindowsFileView.class */
    protected class WindowsFileView extends BasicFileChooserUI.BasicFileView {
        protected WindowsFileView() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicFileChooserUI.BasicFileView, javax.swing.filechooser.FileView
        public Icon getIcon(File file) {
            Icon cachedIcon = getCachedIcon(file);
            if (cachedIcon != null) {
                return cachedIcon;
            }
            if (file != null) {
                cachedIcon = WindowsFileChooserUI.this.getFileChooser().getFileSystemView().getSystemIcon(file);
            }
            if (cachedIcon == null) {
                cachedIcon = super.getIcon(file);
            }
            cacheIcon(file, cachedIcon);
            return cachedIcon;
        }
    }
}
