package sun.security.tools.policytool;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.AccessController;
import java.text.MessageFormat;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import org.icepdf.core.util.PdfOps;
import sun.security.action.GetPropertyAction;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/ToolWindow.class */
class ToolWindow extends JFrame {
    private static final long serialVersionUID = 5682568601210376777L;
    public static final String NEW_POLICY_FILE = "New";
    public static final String OPEN_POLICY_FILE = "Open";
    public static final String SAVE_POLICY_FILE = "Save";
    public static final String SAVE_AS_POLICY_FILE = "Save.As";
    public static final String VIEW_WARNINGS = "View.Warning.Log";
    public static final String QUIT = "Exit";
    public static final String ADD_POLICY_ENTRY = "Add.Policy.Entry";
    public static final String EDIT_POLICY_ENTRY = "Edit.Policy.Entry";
    public static final String REMOVE_POLICY_ENTRY = "Remove.Policy.Entry";
    public static final String EDIT_KEYSTORE = "Edit";
    public static final String ADD_PUBKEY_ALIAS = "Add.Public.Key.Alias";
    public static final String REMOVE_PUBKEY_ALIAS = "Remove.Public.Key.Alias";
    public static final int MW_FILENAME_LABEL = 0;
    public static final int MW_FILENAME_TEXTFIELD = 1;
    public static final int MW_PANEL = 2;
    public static final int MW_ADD_BUTTON = 0;
    public static final int MW_EDIT_BUTTON = 1;
    public static final int MW_REMOVE_BUTTON = 2;
    public static final int MW_POLICY_LIST = 3;
    private PolicyTool tool;
    private int shortCutModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    static final KeyStroke escKey = KeyStroke.getKeyStroke(27, 0);
    public static final Insets TOP_PADDING = new Insets(25, 0, 0, 0);
    public static final Insets BOTTOM_PADDING = new Insets(0, 0, 25, 0);
    public static final Insets LITE_BOTTOM_PADDING = new Insets(0, 0, 10, 0);
    public static final Insets LR_PADDING = new Insets(0, 10, 0, 10);
    public static final Insets TOP_BOTTOM_PADDING = new Insets(15, 0, 15, 0);
    public static final Insets L_TOP_BOTTOM_PADDING = new Insets(5, 10, 15, 0);
    public static final Insets LR_TOP_BOTTOM_PADDING = new Insets(15, 4, 15, 4);
    public static final Insets LR_BOTTOM_PADDING = new Insets(0, 10, 5, 10);
    public static final Insets L_BOTTOM_PADDING = new Insets(0, 10, 5, 0);
    public static final Insets R_BOTTOM_PADDING = new Insets(0, 0, 25, 5);
    public static final Insets R_PADDING = new Insets(0, 0, 0, 5);
    static final int TEXTFIELD_HEIGHT = new JComboBox().getPreferredSize().height;

    ToolWindow(PolicyTool policyTool) {
        this.tool = policyTool;
    }

    @Override // java.awt.Container
    public Component getComponent(int i2) {
        Component component = getContentPane().getComponent(i2);
        if (component instanceof JScrollPane) {
            component = ((JScrollPane) component).getViewport().getView();
        }
        return component;
    }

    private void initWindow() throws IllegalArgumentException {
        setDefaultCloseOperation(0);
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu();
        configureButton(jMenu, "File");
        ActionListener fileMenuListener = new FileMenuListener(this.tool, this);
        addMenuItem(jMenu, NEW_POLICY_FILE, fileMenuListener, "N");
        addMenuItem(jMenu, OPEN_POLICY_FILE, fileMenuListener, "O");
        addMenuItem(jMenu, SAVE_POLICY_FILE, fileMenuListener, PdfOps.S_TOKEN);
        addMenuItem(jMenu, SAVE_AS_POLICY_FILE, fileMenuListener, null);
        addMenuItem(jMenu, VIEW_WARNINGS, fileMenuListener, null);
        addMenuItem(jMenu, QUIT, fileMenuListener, null);
        jMenuBar.add(jMenu);
        JMenu jMenu2 = new JMenu();
        configureButton(jMenu2, "KeyStore");
        addMenuItem(jMenu2, EDIT_KEYSTORE, new MainWindowListener(this.tool, this), null);
        jMenuBar.add(jMenu2);
        setJMenuBar(jMenuBar);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(6, 6, 6, 6));
        addNewComponent(this, new JLabel(PolicyTool.getMessage("Policy.File.")), 0, 0, 0, 1, 1, 0.0d, 0.0d, 1, LR_TOP_BOTTOM_PADDING);
        JTextField jTextField = new JTextField(50);
        jTextField.setPreferredSize(new Dimension(jTextField.getPreferredSize().width, TEXTFIELD_HEIGHT));
        jTextField.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("Policy.File."));
        jTextField.setEditable(false);
        addNewComponent(this, jTextField, 1, 1, 0, 1, 1, 0.0d, 0.0d, 1, LR_TOP_BOTTOM_PADDING);
        JComponent jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        JButton jButton = new JButton();
        configureButton(jButton, ADD_POLICY_ENTRY);
        jButton.addActionListener(new MainWindowListener(this.tool, this));
        addNewComponent(jPanel, jButton, 0, 0, 0, 1, 1, 0.0d, 0.0d, 1, LR_PADDING);
        JButton jButton2 = new JButton();
        configureButton(jButton2, EDIT_POLICY_ENTRY);
        jButton2.addActionListener(new MainWindowListener(this.tool, this));
        addNewComponent(jPanel, jButton2, 1, 1, 0, 1, 1, 0.0d, 0.0d, 1, LR_PADDING);
        JButton jButton3 = new JButton();
        configureButton(jButton3, REMOVE_POLICY_ENTRY);
        jButton3.addActionListener(new MainWindowListener(this.tool, this));
        addNewComponent(jPanel, jButton3, 2, 2, 0, 1, 1, 0.0d, 0.0d, 1, LR_PADDING);
        addNewComponent(this, jPanel, 2, 0, 2, 2, 1, 0.0d, 0.0d, 1, BOTTOM_PADDING);
        String policyFileName = this.tool.getPolicyFileName();
        if (policyFileName == null) {
            policyFileName = ((String) AccessController.doPrivileged(new GetPropertyAction("user.home"))) + File.separatorChar + ".java.policy";
        }
        try {
            this.tool.openPolicy(policyFileName);
            DefaultListModel defaultListModel = new DefaultListModel();
            JList jList = new JList(defaultListModel);
            jList.setVisibleRowCount(15);
            jList.setSelectionMode(0);
            jList.addMouseListener(new PolicyListListener(this.tool, this));
            PolicyEntry[] entry = this.tool.getEntry();
            if (entry != null) {
                for (PolicyEntry policyEntry : entry) {
                    defaultListModel.addElement(policyEntry.headerToString());
                }
            }
            ((JTextField) getComponent(1)).setText(policyFileName);
            initPolicyList(jList);
        } catch (FileNotFoundException e2) {
            JList jList2 = new JList(new DefaultListModel());
            jList2.setVisibleRowCount(15);
            jList2.setSelectionMode(0);
            jList2.addMouseListener(new PolicyListListener(this.tool, this));
            initPolicyList(jList2);
            this.tool.setPolicyFileName(null);
            this.tool.modified = false;
            this.tool.warnings.addElement(e2.toString());
        } catch (Exception e3) {
            JList jList3 = new JList(new DefaultListModel());
            jList3.setVisibleRowCount(15);
            jList3.setSelectionMode(0);
            jList3.addMouseListener(new PolicyListListener(this.tool, this));
            initPolicyList(jList3);
            this.tool.setPolicyFileName(null);
            this.tool.modified = false;
            displayErrorDialog((Window) null, new MessageFormat(PolicyTool.getMessage("Could.not.open.policy.file.policyFile.e.toString.")).format(new Object[]{policyFileName, e3.toString()}));
        }
    }

    private void addMenuItem(JMenu jMenu, String str, ActionListener actionListener, String str2) throws IllegalArgumentException {
        KeyStroke keyStroke;
        JMenuItem jMenuItem = new JMenuItem();
        configureButton(jMenuItem, str);
        if (PolicyTool.rb.containsKey(str + ".accelerator")) {
            str2 = PolicyTool.getMessage(str + ".accelerator");
        }
        if (str2 != null && !str2.isEmpty()) {
            if (str2.length() == 1) {
                keyStroke = KeyStroke.getKeyStroke(KeyEvent.getExtendedKeyCodeForChar(str2.charAt(0)), this.shortCutModifier);
            } else {
                keyStroke = KeyStroke.getKeyStroke(str2);
            }
            jMenuItem.setAccelerator(keyStroke);
        }
        jMenuItem.addActionListener(actionListener);
        jMenu.add(jMenuItem);
    }

    static void configureButton(AbstractButton abstractButton, String str) throws IllegalArgumentException {
        abstractButton.setText(PolicyTool.getMessage(str));
        abstractButton.setActionCommand(str);
        int mnemonicInt = PolicyTool.getMnemonicInt(str);
        if (mnemonicInt > 0) {
            abstractButton.setMnemonic(mnemonicInt);
            abstractButton.setDisplayedMnemonicIndex(PolicyTool.getDisplayedMnemonicIndex(str));
        }
    }

    static void configureLabelFor(JLabel jLabel, JComponent jComponent, String str) throws IllegalArgumentException {
        jLabel.setText(PolicyTool.getMessage(str));
        jLabel.setLabelFor(jComponent);
        int mnemonicInt = PolicyTool.getMnemonicInt(str);
        if (mnemonicInt > 0) {
            jLabel.setDisplayedMnemonic(mnemonicInt);
            jLabel.setDisplayedMnemonicIndex(PolicyTool.getDisplayedMnemonicIndex(str));
        }
    }

    void addNewComponent(Container container, JComponent jComponent, int i2, int i3, int i4, int i5, int i6, double d2, double d3, int i7, Insets insets) {
        if (container instanceof JFrame) {
            container = ((JFrame) container).getContentPane();
        } else if (container instanceof JDialog) {
            container = ((JDialog) container).getContentPane();
        }
        container.add(jComponent, i2);
        GridBagLayout gridBagLayout = (GridBagLayout) container.getLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = i3;
        gridBagConstraints.gridy = i4;
        gridBagConstraints.gridwidth = i5;
        gridBagConstraints.gridheight = i6;
        gridBagConstraints.weightx = d2;
        gridBagConstraints.weighty = d3;
        gridBagConstraints.fill = i7;
        if (insets != null) {
            gridBagConstraints.insets = insets;
        }
        gridBagLayout.setConstraints(jComponent, gridBagConstraints);
    }

    void addNewComponent(Container container, JComponent jComponent, int i2, int i3, int i4, int i5, int i6, double d2, double d3, int i7) {
        addNewComponent(container, jComponent, i2, i3, i4, i5, i6, d2, d3, i7, null);
    }

    void initPolicyList(JList jList) {
        addNewComponent(this, new JScrollPane(jList), 3, 0, 3, 2, 1, 1.0d, 1.0d, 1);
    }

    void replacePolicyList(JList jList) {
        ((JList) getComponent(3)).setModel(jList.getModel());
    }

    void displayToolWindow(String[] strArr) {
        setTitle(PolicyTool.getMessage("Policy.Tool"));
        setResizable(true);
        addWindowListener(new ToolWindowListener(this.tool, this));
        getContentPane().setLayout(new GridBagLayout());
        initWindow();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        if (this.tool.newWarning) {
            displayStatusDialog(this, PolicyTool.getMessage("Errors.have.occurred.while.opening.the.policy.configuration.View.the.Warning.Log.for.more.information."));
        }
    }

    void displayErrorDialog(Window window, String str) {
        ToolDialog toolDialog = new ToolDialog(PolicyTool.getMessage("Error"), this.tool, this, true);
        Point locationOnScreen = window == null ? getLocationOnScreen() : window.getLocationOnScreen();
        toolDialog.setLayout(new GridBagLayout());
        addNewComponent(toolDialog, new JLabel(str), 0, 0, 0, 1, 1, 0.0d, 0.0d, 1);
        JButton jButton = new JButton(PolicyTool.getMessage("OK"));
        ErrorOKButtonListener errorOKButtonListener = new ErrorOKButtonListener(toolDialog);
        jButton.addActionListener(errorOKButtonListener);
        addNewComponent(toolDialog, jButton, 1, 0, 1, 1, 1, 0.0d, 0.0d, 3);
        toolDialog.getRootPane().setDefaultButton(jButton);
        toolDialog.getRootPane().registerKeyboardAction(errorOKButtonListener, escKey, 2);
        toolDialog.pack();
        toolDialog.setLocationRelativeTo(window);
        toolDialog.setVisible(true);
    }

    void displayErrorDialog(Window window, Throwable th) {
        if (th instanceof NoDisplayException) {
            return;
        }
        displayErrorDialog(window, th.toString());
    }

    void displayStatusDialog(Window window, String str) {
        ToolDialog toolDialog = new ToolDialog(PolicyTool.getMessage("Status"), this.tool, this, true);
        Point locationOnScreen = window == null ? getLocationOnScreen() : window.getLocationOnScreen();
        toolDialog.setLayout(new GridBagLayout());
        addNewComponent(toolDialog, new JLabel(str), 0, 0, 0, 1, 1, 0.0d, 0.0d, 1);
        JButton jButton = new JButton(PolicyTool.getMessage("OK"));
        StatusOKButtonListener statusOKButtonListener = new StatusOKButtonListener(toolDialog);
        jButton.addActionListener(statusOKButtonListener);
        addNewComponent(toolDialog, jButton, 1, 0, 1, 1, 1, 0.0d, 0.0d, 3);
        toolDialog.getRootPane().setDefaultButton(jButton);
        toolDialog.getRootPane().registerKeyboardAction(statusOKButtonListener, escKey, 2);
        toolDialog.pack();
        toolDialog.setLocationRelativeTo(window);
        toolDialog.setVisible(true);
    }

    void displayWarningLog(Window window) {
        ToolDialog toolDialog = new ToolDialog(PolicyTool.getMessage("Warning"), this.tool, this, true);
        Point locationOnScreen = window == null ? getLocationOnScreen() : window.getLocationOnScreen();
        toolDialog.setLayout(new GridBagLayout());
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        for (int i2 = 0; i2 < this.tool.warnings.size(); i2++) {
            jTextArea.append(this.tool.warnings.elementAt(i2));
            jTextArea.append(PolicyTool.getMessage("NEWLINE"));
        }
        addNewComponent(toolDialog, jTextArea, 0, 0, 0, 1, 1, 0.0d, 0.0d, 1, BOTTOM_PADDING);
        jTextArea.setFocusable(false);
        JButton jButton = new JButton(PolicyTool.getMessage("OK"));
        CancelButtonListener cancelButtonListener = new CancelButtonListener(toolDialog);
        jButton.addActionListener(cancelButtonListener);
        addNewComponent(toolDialog, jButton, 1, 0, 1, 1, 1, 0.0d, 0.0d, 3, LR_PADDING);
        toolDialog.getRootPane().setDefaultButton(jButton);
        toolDialog.getRootPane().registerKeyboardAction(cancelButtonListener, escKey, 2);
        toolDialog.pack();
        toolDialog.setLocationRelativeTo(window);
        toolDialog.setVisible(true);
    }

    char displayYesNoDialog(Window window, String str, String str2, String str3, String str4) {
        final ToolDialog toolDialog = new ToolDialog(str, this.tool, this, true);
        Point locationOnScreen = window == null ? getLocationOnScreen() : window.getLocationOnScreen();
        toolDialog.setLayout(new GridBagLayout());
        JTextArea jTextArea = new JTextArea(str2, 10, 50);
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        addNewComponent(toolDialog, new JScrollPane(jTextArea, 20, 31), 0, 0, 0, 1, 1, 0.0d, 0.0d, 1);
        jTextArea.setFocusable(false);
        JComponent jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        final StringBuffer stringBuffer = new StringBuffer();
        JButton jButton = new JButton(str3);
        jButton.addActionListener(new ActionListener() { // from class: sun.security.tools.policytool.ToolWindow.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                stringBuffer.append('Y');
                toolDialog.setVisible(false);
                toolDialog.dispose();
            }
        });
        addNewComponent(jPanel, jButton, 0, 0, 0, 1, 1, 0.0d, 0.0d, 3, LR_PADDING);
        JButton jButton2 = new JButton(str4);
        jButton2.addActionListener(new ActionListener() { // from class: sun.security.tools.policytool.ToolWindow.2
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                stringBuffer.append('N');
                toolDialog.setVisible(false);
                toolDialog.dispose();
            }
        });
        addNewComponent(jPanel, jButton2, 1, 1, 0, 1, 1, 0.0d, 0.0d, 3, LR_PADDING);
        addNewComponent(toolDialog, jPanel, 1, 0, 1, 1, 1, 0.0d, 0.0d, 3);
        toolDialog.pack();
        toolDialog.setLocationRelativeTo(window);
        toolDialog.setVisible(true);
        if (stringBuffer.length() > 0) {
            return stringBuffer.charAt(0);
        }
        return 'N';
    }
}
