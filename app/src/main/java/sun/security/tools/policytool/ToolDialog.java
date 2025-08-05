package sun.security.tools.policytool;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import org.icepdf.core.util.PdfOps;
import sun.security.provider.PolicyParser;

/* compiled from: PolicyTool.java */
/* loaded from: rt.jar:sun/security/tools/policytool/ToolDialog.class */
class ToolDialog extends JDialog {
    private static final long serialVersionUID = -372244357011301190L;
    public static final int NOACTION = 0;
    public static final int QUIT = 1;
    public static final int NEW = 2;
    public static final int OPEN = 3;
    public static final String ALL_PERM_CLASS = "java.security.AllPermission";
    public static final String FILE_PERM_CLASS = "java.io.FilePermission";
    public static final String X500_PRIN_CLASS = "javax.security.auth.x500.X500Principal";
    public static final int PE_CODEBASE_LABEL = 0;
    public static final int PE_CODEBASE_TEXTFIELD = 1;
    public static final int PE_SIGNEDBY_LABEL = 2;
    public static final int PE_SIGNEDBY_TEXTFIELD = 3;
    public static final int PE_PANEL0 = 4;
    public static final int PE_ADD_PRIN_BUTTON = 0;
    public static final int PE_EDIT_PRIN_BUTTON = 1;
    public static final int PE_REMOVE_PRIN_BUTTON = 2;
    public static final int PE_PRIN_LABEL = 5;
    public static final int PE_PRIN_LIST = 6;
    public static final int PE_PANEL1 = 7;
    public static final int PE_ADD_PERM_BUTTON = 0;
    public static final int PE_EDIT_PERM_BUTTON = 1;
    public static final int PE_REMOVE_PERM_BUTTON = 2;
    public static final int PE_PERM_LIST = 8;
    public static final int PE_PANEL2 = 9;
    public static final int PE_CANCEL_BUTTON = 1;
    public static final int PE_DONE_BUTTON = 0;
    public static final int PRD_DESC_LABEL = 0;
    public static final int PRD_PRIN_CHOICE = 1;
    public static final int PRD_PRIN_TEXTFIELD = 2;
    public static final int PRD_NAME_LABEL = 3;
    public static final int PRD_NAME_TEXTFIELD = 4;
    public static final int PRD_CANCEL_BUTTON = 6;
    public static final int PRD_OK_BUTTON = 5;
    public static final int PD_DESC_LABEL = 0;
    public static final int PD_PERM_CHOICE = 1;
    public static final int PD_PERM_TEXTFIELD = 2;
    public static final int PD_NAME_CHOICE = 3;
    public static final int PD_NAME_TEXTFIELD = 4;
    public static final int PD_ACTIONS_CHOICE = 5;
    public static final int PD_ACTIONS_TEXTFIELD = 6;
    public static final int PD_SIGNEDBY_LABEL = 7;
    public static final int PD_SIGNEDBY_TEXTFIELD = 8;
    public static final int PD_CANCEL_BUTTON = 10;
    public static final int PD_OK_BUTTON = 9;
    public static final int EDIT_KEYSTORE = 0;
    public static final int KSD_NAME_LABEL = 0;
    public static final int KSD_NAME_TEXTFIELD = 1;
    public static final int KSD_TYPE_LABEL = 2;
    public static final int KSD_TYPE_TEXTFIELD = 3;
    public static final int KSD_PROVIDER_LABEL = 4;
    public static final int KSD_PROVIDER_TEXTFIELD = 5;
    public static final int KSD_PWD_URL_LABEL = 6;
    public static final int KSD_PWD_URL_TEXTFIELD = 7;
    public static final int KSD_CANCEL_BUTTON = 9;
    public static final int KSD_OK_BUTTON = 8;
    public static final int USC_LABEL = 0;
    public static final int USC_PANEL = 1;
    public static final int USC_YES_BUTTON = 0;
    public static final int USC_NO_BUTTON = 1;
    public static final int USC_CANCEL_BUTTON = 2;
    public static final int CRPE_LABEL1 = 0;
    public static final int CRPE_LABEL2 = 1;
    public static final int CRPE_PANEL = 2;
    public static final int CRPE_PANEL_OK = 0;
    public static final int CRPE_PANEL_CANCEL = 1;
    private static final int PERMISSION = 0;
    private static final int PERMISSION_NAME = 1;
    private static final int PERMISSION_ACTIONS = 2;
    private static final int PERMISSION_SIGNEDBY = 3;
    private static final int PRINCIPAL_TYPE = 4;
    private static final int PRINCIPAL_NAME = 5;
    public static ArrayList<Prin> PRIN_ARRAY;
    PolicyTool tool;
    ToolWindow tw;
    static final KeyStroke escKey = KeyStroke.getKeyStroke(27, 0);
    public static final String PERM = PolicyTool.getMessage("Permission.");
    public static final String PRIN_TYPE = PolicyTool.getMessage("Principal.Type.");
    public static final String PRIN_NAME = PolicyTool.getMessage("Principal.Name.");
    public static final String PERM_NAME = PolicyTool.getMessage("Target.Name.");
    public static final String PERM_ACTIONS = PolicyTool.getMessage("Actions.");
    static final int TEXTFIELD_HEIGHT = new JComboBox().getPreferredSize().height;
    public static ArrayList<Perm> PERM_ARRAY = new ArrayList<>();

    static {
        PERM_ARRAY.add(new AllPerm());
        PERM_ARRAY.add(new AudioPerm());
        PERM_ARRAY.add(new AuthPerm());
        PERM_ARRAY.add(new AWTPerm());
        PERM_ARRAY.add(new DelegationPerm());
        PERM_ARRAY.add(new FilePerm());
        PERM_ARRAY.add(new URLPerm());
        PERM_ARRAY.add(new InqSecContextPerm());
        PERM_ARRAY.add(new LogPerm());
        PERM_ARRAY.add(new MgmtPerm());
        PERM_ARRAY.add(new MBeanPerm());
        PERM_ARRAY.add(new MBeanSvrPerm());
        PERM_ARRAY.add(new MBeanTrustPerm());
        PERM_ARRAY.add(new NetPerm());
        PERM_ARRAY.add(new PrivCredPerm());
        PERM_ARRAY.add(new PropPerm());
        PERM_ARRAY.add(new ReflectPerm());
        PERM_ARRAY.add(new RuntimePerm());
        PERM_ARRAY.add(new SecurityPerm());
        PERM_ARRAY.add(new SerialPerm());
        PERM_ARRAY.add(new ServicePerm());
        PERM_ARRAY.add(new SocketPerm());
        PERM_ARRAY.add(new SQLPerm());
        PERM_ARRAY.add(new SSLPerm());
        PERM_ARRAY.add(new SubjDelegPerm());
        PRIN_ARRAY = new ArrayList<>();
        PRIN_ARRAY.add(new KrbPrin());
        PRIN_ARRAY.add(new X500Prin());
    }

    ToolDialog(String str, PolicyTool policyTool, ToolWindow toolWindow, boolean z2) {
        super(toolWindow, z2);
        setTitle(str);
        this.tool = policyTool;
        this.tw = toolWindow;
        addWindowListener(new ChildWindowListener(this));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(6, 6, 6, 6));
    }

    @Override // java.awt.Container
    public Component getComponent(int i2) {
        Component component = getContentPane().getComponent(i2);
        if (component instanceof JScrollPane) {
            component = ((JScrollPane) component).getViewport().getView();
        }
        return component;
    }

    static Perm getPerm(String str, boolean z2) {
        for (int i2 = 0; i2 < PERM_ARRAY.size(); i2++) {
            Perm perm = PERM_ARRAY.get(i2);
            if (z2) {
                if (perm.FULL_CLASS.equals(str)) {
                    return perm;
                }
            } else if (perm.CLASS.equals(str)) {
                return perm;
            }
        }
        return null;
    }

    static Prin getPrin(String str, boolean z2) {
        for (int i2 = 0; i2 < PRIN_ARRAY.size(); i2++) {
            Prin prin = PRIN_ARRAY.get(i2);
            if (z2) {
                if (prin.FULL_CLASS.equals(str)) {
                    return prin;
                }
            } else if (prin.CLASS.equals(str)) {
                return prin;
            }
        }
        return null;
    }

    void displayPolicyEntryDialog(boolean z2) {
        int selectedIndex = 0;
        PolicyEntry[] entry = null;
        TaggedList taggedList = new TaggedList(3, false);
        taggedList.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("Principal.List"));
        taggedList.addMouseListener(new EditPrinButtonListener(this.tool, this.tw, this, z2));
        TaggedList taggedList2 = new TaggedList(10, false);
        taggedList2.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("Permission.List"));
        taggedList2.addMouseListener(new EditPermButtonListener(this.tool, this.tw, this, z2));
        this.tw.getLocationOnScreen();
        setLayout(new GridBagLayout());
        setResizable(true);
        if (z2) {
            entry = this.tool.getEntry();
            selectedIndex = ((JList) this.tw.getComponent(3)).getSelectedIndex();
            LinkedList<PolicyParser.PrincipalEntry> linkedList = entry[selectedIndex].getGrantEntry().principals;
            for (int i2 = 0; i2 < linkedList.size(); i2++) {
                PolicyParser.PrincipalEntry principalEntry = linkedList.get(i2);
                taggedList.addTaggedItem(PrincipalEntryToUserFriendlyString(principalEntry), principalEntry);
            }
            Vector<PolicyParser.PermissionEntry> vector = entry[selectedIndex].getGrantEntry().permissionEntries;
            for (int i3 = 0; i3 < vector.size(); i3++) {
                PolicyParser.PermissionEntry permissionEntryElementAt = vector.elementAt(i3);
                taggedList2.addTaggedItem(PermissionEntryToUserFriendlyString(permissionEntryElementAt), permissionEntryElementAt);
            }
        }
        JLabel jLabel = new JLabel();
        this.tw.addNewComponent(this, jLabel, 0, 0, 0, 1, 1, 0.0d, 0.0d, 1, ToolWindow.R_PADDING);
        JTextField jTextField = z2 ? new JTextField(entry[selectedIndex].getGrantEntry().codeBase) : new JTextField();
        ToolWindow.configureLabelFor(jLabel, jTextField, "CodeBase.");
        jTextField.setPreferredSize(new Dimension(jTextField.getPreferredSize().width, TEXTFIELD_HEIGHT));
        jTextField.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("Code.Base"));
        this.tw.addNewComponent(this, jTextField, 1, 1, 0, 1, 1, 1.0d, 0.0d, 1);
        JLabel jLabel2 = new JLabel();
        this.tw.addNewComponent(this, jLabel2, 2, 0, 1, 1, 1, 0.0d, 0.0d, 1, ToolWindow.R_PADDING);
        JTextField jTextField2 = z2 ? new JTextField(entry[selectedIndex].getGrantEntry().signedBy) : new JTextField();
        ToolWindow.configureLabelFor(jLabel2, jTextField2, "SignedBy.");
        jTextField2.setPreferredSize(new Dimension(jTextField2.getPreferredSize().width, TEXTFIELD_HEIGHT));
        jTextField2.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("Signed.By."));
        this.tw.addNewComponent(this, jTextField2, 3, 1, 1, 1, 1, 1.0d, 0.0d, 1);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        JButton jButton = new JButton();
        ToolWindow.configureButton(jButton, "Add.Principal");
        jButton.addActionListener(new AddPrinButtonListener(this.tool, this.tw, this, z2));
        this.tw.addNewComponent(jPanel, jButton, 0, 0, 0, 1, 1, 100.0d, 0.0d, 2);
        JButton jButton2 = new JButton();
        ToolWindow.configureButton(jButton2, "Edit.Principal");
        jButton2.addActionListener(new EditPrinButtonListener(this.tool, this.tw, this, z2));
        this.tw.addNewComponent(jPanel, jButton2, 1, 1, 0, 1, 1, 100.0d, 0.0d, 2);
        JButton jButton3 = new JButton();
        ToolWindow.configureButton(jButton3, "Remove.Principal");
        jButton3.addActionListener(new RemovePrinButtonListener(this.tool, this.tw, this, z2));
        this.tw.addNewComponent(jPanel, jButton3, 2, 2, 0, 1, 1, 100.0d, 0.0d, 2);
        this.tw.addNewComponent(this, jPanel, 4, 1, 2, 1, 1, 0.0d, 0.0d, 2, ToolWindow.LITE_BOTTOM_PADDING);
        JLabel jLabel3 = new JLabel();
        this.tw.addNewComponent(this, jLabel3, 5, 0, 3, 1, 1, 0.0d, 0.0d, 1, ToolWindow.R_BOTTOM_PADDING);
        JScrollPane jScrollPane = new JScrollPane(taggedList);
        ToolWindow.configureLabelFor(jLabel3, jScrollPane, "Principals.");
        this.tw.addNewComponent(this, jScrollPane, 6, 1, 3, 3, 1, 0.0d, taggedList.getVisibleRowCount(), 1, ToolWindow.BOTTOM_PADDING);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridBagLayout());
        JButton jButton4 = new JButton();
        ToolWindow.configureButton(jButton4, ".Add.Permission");
        jButton4.addActionListener(new AddPermButtonListener(this.tool, this.tw, this, z2));
        this.tw.addNewComponent(jPanel2, jButton4, 0, 0, 0, 1, 1, 100.0d, 0.0d, 2);
        JButton jButton5 = new JButton();
        ToolWindow.configureButton(jButton5, ".Edit.Permission");
        jButton5.addActionListener(new EditPermButtonListener(this.tool, this.tw, this, z2));
        this.tw.addNewComponent(jPanel2, jButton5, 1, 1, 0, 1, 1, 100.0d, 0.0d, 2);
        JButton jButton6 = new JButton();
        ToolWindow.configureButton(jButton6, "Remove.Permission");
        jButton6.addActionListener(new RemovePermButtonListener(this.tool, this.tw, this, z2));
        this.tw.addNewComponent(jPanel2, jButton6, 2, 2, 0, 1, 1, 100.0d, 0.0d, 2);
        this.tw.addNewComponent(this, jPanel2, 7, 0, 4, 2, 1, 0.0d, 0.0d, 2, ToolWindow.LITE_BOTTOM_PADDING);
        this.tw.addNewComponent(this, new JScrollPane(taggedList2), 8, 0, 5, 3, 1, 0.0d, taggedList2.getVisibleRowCount(), 1, ToolWindow.BOTTOM_PADDING);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridBagLayout());
        JButton jButton7 = new JButton(PolicyTool.getMessage("Done"));
        jButton7.addActionListener(new AddEntryDoneButtonListener(this.tool, this.tw, this, z2));
        this.tw.addNewComponent(jPanel3, jButton7, 0, 0, 0, 1, 1, 0.0d, 0.0d, 3, ToolWindow.LR_PADDING);
        JButton jButton8 = new JButton(PolicyTool.getMessage("Cancel"));
        CancelButtonListener cancelButtonListener = new CancelButtonListener(this);
        jButton8.addActionListener(cancelButtonListener);
        this.tw.addNewComponent(jPanel3, jButton8, 1, 1, 0, 1, 1, 0.0d, 0.0d, 3, ToolWindow.LR_PADDING);
        this.tw.addNewComponent(this, jPanel3, 9, 0, 6, 2, 1, 0.0d, 0.0d, 3);
        getRootPane().setDefaultButton(jButton7);
        getRootPane().registerKeyboardAction(cancelButtonListener, escKey, 2);
        pack();
        setLocationRelativeTo(this.tw);
        setVisible(true);
    }

    PolicyEntry getPolicyEntryFromDialog() throws Exception {
        JTextField jTextField = (JTextField) getComponent(1);
        String str = null;
        if (!jTextField.getText().trim().equals("")) {
            str = new String(jTextField.getText().trim());
        }
        JTextField jTextField2 = (JTextField) getComponent(3);
        String str2 = null;
        if (!jTextField2.getText().trim().equals("")) {
            str2 = new String(jTextField2.getText().trim());
        }
        PolicyParser.GrantEntry grantEntry = new PolicyParser.GrantEntry(str2, str);
        LinkedList<PolicyParser.PrincipalEntry> linkedList = new LinkedList<>();
        TaggedList taggedList = (TaggedList) getComponent(6);
        for (int i2 = 0; i2 < taggedList.getModel().getSize(); i2++) {
            linkedList.add((PolicyParser.PrincipalEntry) taggedList.getObject(i2));
        }
        grantEntry.principals = linkedList;
        Vector<PolicyParser.PermissionEntry> vector = new Vector<>();
        TaggedList taggedList2 = (TaggedList) getComponent(8);
        for (int i3 = 0; i3 < taggedList2.getModel().getSize(); i3++) {
            vector.addElement((PolicyParser.PermissionEntry) taggedList2.getObject(i3));
        }
        grantEntry.permissionEntries = vector;
        return new PolicyEntry(this.tool, grantEntry);
    }

    void keyStoreDialog(int i2) {
        this.tw.getLocationOnScreen();
        setLayout(new GridBagLayout());
        if (i2 == 0) {
            JLabel jLabel = new JLabel();
            this.tw.addNewComponent(this, jLabel, 0, 0, 0, 1, 1, 0.0d, 0.0d, 1, ToolWindow.R_BOTTOM_PADDING);
            JTextField jTextField = new JTextField(this.tool.getKeyStoreName(), 30);
            ToolWindow.configureLabelFor(jLabel, jTextField, "KeyStore.URL.");
            jTextField.setPreferredSize(new Dimension(jTextField.getPreferredSize().width, TEXTFIELD_HEIGHT));
            jTextField.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("KeyStore.U.R.L."));
            this.tw.addNewComponent(this, jTextField, 1, 1, 0, 1, 1, 1.0d, 0.0d, 1, ToolWindow.BOTTOM_PADDING);
            JLabel jLabel2 = new JLabel();
            this.tw.addNewComponent(this, jLabel2, 2, 0, 1, 1, 1, 0.0d, 0.0d, 1, ToolWindow.R_BOTTOM_PADDING);
            JTextField jTextField2 = new JTextField(this.tool.getKeyStoreType(), 30);
            ToolWindow.configureLabelFor(jLabel2, jTextField2, "KeyStore.Type.");
            jTextField2.setPreferredSize(new Dimension(jTextField2.getPreferredSize().width, TEXTFIELD_HEIGHT));
            jTextField2.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("KeyStore.Type."));
            this.tw.addNewComponent(this, jTextField2, 3, 1, 1, 1, 1, 1.0d, 0.0d, 1, ToolWindow.BOTTOM_PADDING);
            JLabel jLabel3 = new JLabel();
            this.tw.addNewComponent(this, jLabel3, 4, 0, 2, 1, 1, 0.0d, 0.0d, 1, ToolWindow.R_BOTTOM_PADDING);
            JTextField jTextField3 = new JTextField(this.tool.getKeyStoreProvider(), 30);
            ToolWindow.configureLabelFor(jLabel3, jTextField3, "KeyStore.Provider.");
            jTextField3.setPreferredSize(new Dimension(jTextField3.getPreferredSize().width, TEXTFIELD_HEIGHT));
            jTextField3.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("KeyStore.Provider."));
            this.tw.addNewComponent(this, jTextField3, 5, 1, 2, 1, 1, 1.0d, 0.0d, 1, ToolWindow.BOTTOM_PADDING);
            JLabel jLabel4 = new JLabel();
            this.tw.addNewComponent(this, jLabel4, 6, 0, 3, 1, 1, 0.0d, 0.0d, 1, ToolWindow.R_BOTTOM_PADDING);
            JTextField jTextField4 = new JTextField(this.tool.getKeyStorePwdURL(), 30);
            ToolWindow.configureLabelFor(jLabel4, jTextField4, "KeyStore.Password.URL.");
            jTextField4.setPreferredSize(new Dimension(jTextField4.getPreferredSize().width, TEXTFIELD_HEIGHT));
            jTextField4.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("KeyStore.Password.U.R.L."));
            this.tw.addNewComponent(this, jTextField4, 7, 1, 3, 1, 1, 1.0d, 0.0d, 1, ToolWindow.BOTTOM_PADDING);
            JButton jButton = new JButton(PolicyTool.getMessage("OK"));
            jButton.addActionListener(new ChangeKeyStoreOKButtonListener(this.tool, this.tw, this));
            this.tw.addNewComponent(this, jButton, 8, 0, 4, 1, 1, 0.0d, 0.0d, 3);
            JButton jButton2 = new JButton(PolicyTool.getMessage("Cancel"));
            CancelButtonListener cancelButtonListener = new CancelButtonListener(this);
            jButton2.addActionListener(cancelButtonListener);
            this.tw.addNewComponent(this, jButton2, 9, 1, 4, 1, 1, 0.0d, 0.0d, 3);
            getRootPane().setDefaultButton(jButton);
            getRootPane().registerKeyboardAction(cancelButtonListener, escKey, 2);
        }
        pack();
        setLocationRelativeTo(this.tw);
        setVisible(true);
    }

    void displayPrincipalDialog(boolean z2, boolean z3) {
        JLabel jLabel;
        PolicyParser.PrincipalEntry principalEntry = null;
        TaggedList taggedList = (TaggedList) getComponent(6);
        int selectedIndex = taggedList.getSelectedIndex();
        if (z3) {
            principalEntry = (PolicyParser.PrincipalEntry) taggedList.getObject(selectedIndex);
        }
        ToolDialog toolDialog = new ToolDialog(PolicyTool.getMessage("Principals"), this.tool, this.tw, true);
        toolDialog.addWindowListener(new ChildWindowListener(toolDialog));
        getLocationOnScreen();
        toolDialog.setLayout(new GridBagLayout());
        toolDialog.setResizable(true);
        if (z3) {
            jLabel = new JLabel(PolicyTool.getMessage(".Edit.Principal."));
        } else {
            jLabel = new JLabel(PolicyTool.getMessage(".Add.New.Principal."));
        }
        this.tw.addNewComponent(toolDialog, jLabel, 0, 0, 0, 1, 1, 0.0d, 0.0d, 1, ToolWindow.TOP_BOTTOM_PADDING);
        JComboBox jComboBox = new JComboBox();
        jComboBox.addItem(PRIN_TYPE);
        jComboBox.getAccessibleContext().setAccessibleName(PRIN_TYPE);
        for (int i2 = 0; i2 < PRIN_ARRAY.size(); i2++) {
            jComboBox.addItem(PRIN_ARRAY.get(i2).CLASS);
        }
        if (z3) {
            if (PolicyParser.PrincipalEntry.WILDCARD_CLASS.equals(principalEntry.getPrincipalClass())) {
                jComboBox.setSelectedItem(PRIN_TYPE);
            } else {
                Prin prin = getPrin(principalEntry.getPrincipalClass(), true);
                if (prin != null) {
                    jComboBox.setSelectedItem(prin.CLASS);
                }
            }
        }
        jComboBox.addItemListener(new PrincipalTypeMenuListener(toolDialog));
        this.tw.addNewComponent(toolDialog, jComboBox, 1, 0, 1, 1, 1, 0.0d, 0.0d, 1, ToolWindow.LR_PADDING);
        JTextField jTextField = z3 ? new JTextField(principalEntry.getDisplayClass(), 30) : new JTextField(30);
        jTextField.setPreferredSize(new Dimension(jTextField.getPreferredSize().width, TEXTFIELD_HEIGHT));
        jTextField.getAccessibleContext().setAccessibleName(PRIN_TYPE);
        this.tw.addNewComponent(toolDialog, jTextField, 2, 1, 1, 1, 1, 1.0d, 0.0d, 1, ToolWindow.LR_PADDING);
        JLabel jLabel2 = new JLabel(PRIN_NAME);
        JTextField jTextField2 = z3 ? new JTextField(principalEntry.getDisplayName(), 40) : new JTextField(40);
        jTextField2.setPreferredSize(new Dimension(jTextField2.getPreferredSize().width, TEXTFIELD_HEIGHT));
        jTextField2.getAccessibleContext().setAccessibleName(PRIN_NAME);
        this.tw.addNewComponent(toolDialog, jLabel2, 3, 0, 2, 1, 1, 0.0d, 0.0d, 1, ToolWindow.LR_PADDING);
        this.tw.addNewComponent(toolDialog, jTextField2, 4, 1, 2, 1, 1, 1.0d, 0.0d, 1, ToolWindow.LR_PADDING);
        JButton jButton = new JButton(PolicyTool.getMessage("OK"));
        jButton.addActionListener(new NewPolicyPrinOKButtonListener(this.tool, this.tw, this, toolDialog, z3));
        this.tw.addNewComponent(toolDialog, jButton, 5, 0, 3, 1, 1, 0.0d, 0.0d, 3, ToolWindow.TOP_BOTTOM_PADDING);
        JButton jButton2 = new JButton(PolicyTool.getMessage("Cancel"));
        CancelButtonListener cancelButtonListener = new CancelButtonListener(toolDialog);
        jButton2.addActionListener(cancelButtonListener);
        this.tw.addNewComponent(toolDialog, jButton2, 6, 1, 3, 1, 1, 0.0d, 0.0d, 3, ToolWindow.TOP_BOTTOM_PADDING);
        toolDialog.getRootPane().setDefaultButton(jButton);
        toolDialog.getRootPane().registerKeyboardAction(cancelButtonListener, escKey, 2);
        toolDialog.pack();
        toolDialog.setLocationRelativeTo(this.tw);
        toolDialog.setVisible(true);
    }

    void displayPermissionDialog(boolean z2, boolean z3) {
        JLabel jLabel;
        Perm perm;
        PolicyParser.PermissionEntry permissionEntry = null;
        TaggedList taggedList = (TaggedList) getComponent(8);
        int selectedIndex = taggedList.getSelectedIndex();
        if (z3) {
            permissionEntry = (PolicyParser.PermissionEntry) taggedList.getObject(selectedIndex);
        }
        ToolDialog toolDialog = new ToolDialog(PolicyTool.getMessage("Permissions"), this.tool, this.tw, true);
        toolDialog.addWindowListener(new ChildWindowListener(toolDialog));
        getLocationOnScreen();
        toolDialog.setLayout(new GridBagLayout());
        toolDialog.setResizable(true);
        if (z3) {
            jLabel = new JLabel(PolicyTool.getMessage(".Edit.Permission."));
        } else {
            jLabel = new JLabel(PolicyTool.getMessage(".Add.New.Permission."));
        }
        this.tw.addNewComponent(toolDialog, jLabel, 0, 0, 0, 1, 1, 0.0d, 0.0d, 1, ToolWindow.TOP_BOTTOM_PADDING);
        JComboBox jComboBox = new JComboBox();
        jComboBox.addItem(PERM);
        jComboBox.getAccessibleContext().setAccessibleName(PERM);
        for (int i2 = 0; i2 < PERM_ARRAY.size(); i2++) {
            jComboBox.addItem(PERM_ARRAY.get(i2).CLASS);
        }
        this.tw.addNewComponent(toolDialog, jComboBox, 1, 0, 1, 1, 1, 0.0d, 0.0d, 1, ToolWindow.LR_BOTTOM_PADDING);
        JTextField jTextField = z3 ? new JTextField(permissionEntry.permission, 30) : new JTextField(30);
        jTextField.setPreferredSize(new Dimension(jTextField.getPreferredSize().width, TEXTFIELD_HEIGHT));
        jTextField.getAccessibleContext().setAccessibleName(PERM);
        if (z3 && (perm = getPerm(permissionEntry.permission, true)) != null) {
            jComboBox.setSelectedItem(perm.CLASS);
        }
        this.tw.addNewComponent(toolDialog, jTextField, 2, 1, 1, 1, 1, 1.0d, 0.0d, 1, ToolWindow.LR_BOTTOM_PADDING);
        jComboBox.addItemListener(new PermissionMenuListener(toolDialog));
        JComboBox jComboBox2 = new JComboBox();
        jComboBox2.addItem(PERM_NAME);
        jComboBox2.getAccessibleContext().setAccessibleName(PERM_NAME);
        JTextField jTextField2 = z3 ? new JTextField(permissionEntry.name, 40) : new JTextField(40);
        jTextField2.setPreferredSize(new Dimension(jTextField2.getPreferredSize().width, TEXTFIELD_HEIGHT));
        jTextField2.getAccessibleContext().setAccessibleName(PERM_NAME);
        if (z3) {
            setPermissionNames(getPerm(permissionEntry.permission, true), jComboBox2, jTextField2);
        }
        this.tw.addNewComponent(toolDialog, jComboBox2, 3, 0, 2, 1, 1, 0.0d, 0.0d, 1, ToolWindow.LR_BOTTOM_PADDING);
        this.tw.addNewComponent(toolDialog, jTextField2, 4, 1, 2, 1, 1, 1.0d, 0.0d, 1, ToolWindow.LR_BOTTOM_PADDING);
        jComboBox2.addItemListener(new PermissionNameMenuListener(toolDialog));
        JComboBox jComboBox3 = new JComboBox();
        jComboBox3.addItem(PERM_ACTIONS);
        jComboBox3.getAccessibleContext().setAccessibleName(PERM_ACTIONS);
        JTextField jTextField3 = z3 ? new JTextField(permissionEntry.action, 40) : new JTextField(40);
        jTextField3.setPreferredSize(new Dimension(jTextField3.getPreferredSize().width, TEXTFIELD_HEIGHT));
        jTextField3.getAccessibleContext().setAccessibleName(PERM_ACTIONS);
        if (z3) {
            setPermissionActions(getPerm(permissionEntry.permission, true), jComboBox3, jTextField3);
        }
        this.tw.addNewComponent(toolDialog, jComboBox3, 5, 0, 3, 1, 1, 0.0d, 0.0d, 1, ToolWindow.LR_BOTTOM_PADDING);
        this.tw.addNewComponent(toolDialog, jTextField3, 6, 1, 3, 1, 1, 1.0d, 0.0d, 1, ToolWindow.LR_BOTTOM_PADDING);
        jComboBox3.addItemListener(new PermissionActionsMenuListener(toolDialog));
        this.tw.addNewComponent(toolDialog, new JLabel(PolicyTool.getMessage("Signed.By.")), 7, 0, 4, 1, 1, 0.0d, 0.0d, 1, ToolWindow.LR_BOTTOM_PADDING);
        JTextField jTextField4 = z3 ? new JTextField(permissionEntry.signedBy, 40) : new JTextField(40);
        jTextField4.setPreferredSize(new Dimension(jTextField4.getPreferredSize().width, TEXTFIELD_HEIGHT));
        jTextField4.getAccessibleContext().setAccessibleName(PolicyTool.getMessage("Signed.By."));
        this.tw.addNewComponent(toolDialog, jTextField4, 8, 1, 4, 1, 1, 1.0d, 0.0d, 1, ToolWindow.LR_BOTTOM_PADDING);
        JButton jButton = new JButton(PolicyTool.getMessage("OK"));
        jButton.addActionListener(new NewPolicyPermOKButtonListener(this.tool, this.tw, this, toolDialog, z3));
        this.tw.addNewComponent(toolDialog, jButton, 9, 0, 5, 1, 1, 0.0d, 0.0d, 3, ToolWindow.TOP_BOTTOM_PADDING);
        JButton jButton2 = new JButton(PolicyTool.getMessage("Cancel"));
        CancelButtonListener cancelButtonListener = new CancelButtonListener(toolDialog);
        jButton2.addActionListener(cancelButtonListener);
        this.tw.addNewComponent(toolDialog, jButton2, 10, 1, 5, 1, 1, 0.0d, 0.0d, 3, ToolWindow.TOP_BOTTOM_PADDING);
        toolDialog.getRootPane().setDefaultButton(jButton);
        toolDialog.getRootPane().registerKeyboardAction(cancelButtonListener, escKey, 2);
        toolDialog.pack();
        toolDialog.setLocationRelativeTo(this.tw);
        toolDialog.setVisible(true);
    }

    PolicyParser.PrincipalEntry getPrinFromDialog() throws Exception {
        String str = new String(((JTextField) getComponent(2)).getText().trim());
        String str2 = new String(((JTextField) getComponent(4)).getText().trim());
        if (str.equals("*")) {
            str = PolicyParser.PrincipalEntry.WILDCARD_CLASS;
        }
        if (str2.equals("*")) {
            str2 = PolicyParser.PrincipalEntry.WILDCARD_NAME;
        }
        if (str.equals(PolicyParser.PrincipalEntry.WILDCARD_CLASS) && !str2.equals(PolicyParser.PrincipalEntry.WILDCARD_NAME)) {
            throw new Exception(PolicyTool.getMessage("Cannot.Specify.Principal.with.a.Wildcard.Class.without.a.Wildcard.Name"));
        }
        if (str2.equals("")) {
            throw new Exception(PolicyTool.getMessage("Cannot.Specify.Principal.without.a.Name"));
        }
        if (str.equals("")) {
            str = PolicyParser.PrincipalEntry.REPLACE_NAME;
            this.tool.warnings.addElement("Warning: Principal name '" + str2 + "' specified without a Principal class.\n\t'" + str2 + "' will be interpreted as a key store alias.\n\tThe final principal class will be " + X500_PRIN_CLASS + ".\n\tThe final principal name will be determined by the following:\n\n\tIf the key store entry identified by '" + str2 + "'\n\tis a key entry, then the principal name will be\n\tthe subject distinguished name from the first\n\tcertificate in the entry's certificate chain.\n\n\tIf the key store entry identified by '" + str2 + "'\n\tis a trusted certificate entry, then the\n\tprincipal name will be the subject distinguished\n\tname from the trusted public key certificate.");
            this.tw.displayStatusDialog(this, PdfOps.SINGLE_QUOTE_TOKEN + str2 + "' will be interpreted as a key store alias.  View Warning Log for details.");
        }
        return new PolicyParser.PrincipalEntry(str, str2);
    }

    PolicyParser.PermissionEntry getPermFromDialog() {
        String str = new String(((JTextField) getComponent(2)).getText().trim());
        JTextField jTextField = (JTextField) getComponent(4);
        String str2 = null;
        if (!jTextField.getText().trim().equals("")) {
            str2 = new String(jTextField.getText().trim());
        }
        if (str.equals("") || (!str.equals(ALL_PERM_CLASS) && str2 == null)) {
            throw new InvalidParameterException(PolicyTool.getMessage("Permission.and.Target.Name.must.have.a.value"));
        }
        if (str.equals(FILE_PERM_CLASS) && str2.lastIndexOf("\\\\") > 0 && this.tw.displayYesNoDialog(this, PolicyTool.getMessage("Warning"), PolicyTool.getMessage("Warning.File.name.may.include.escaped.backslash.characters.It.is.not.necessary.to.escape.backslash.characters.the.tool.escapes"), PolicyTool.getMessage("Retain"), PolicyTool.getMessage(ToolWindow.EDIT_KEYSTORE)) != 'Y') {
            throw new NoDisplayException();
        }
        JTextField jTextField2 = (JTextField) getComponent(6);
        String str3 = null;
        if (!jTextField2.getText().trim().equals("")) {
            str3 = new String(jTextField2.getText().trim());
        }
        JTextField jTextField3 = (JTextField) getComponent(8);
        String str4 = null;
        if (!jTextField3.getText().trim().equals("")) {
            str4 = new String(jTextField3.getText().trim());
        }
        PolicyParser.PermissionEntry permissionEntry = new PolicyParser.PermissionEntry(str, str2, str3);
        permissionEntry.signedBy = str4;
        if (str4 != null) {
            String[] signers = this.tool.parseSigners(permissionEntry.signedBy);
            for (int i2 = 0; i2 < signers.length; i2++) {
                try {
                    if (this.tool.getPublicKeyAlias(signers[i2]) == null) {
                        MessageFormat messageFormat = new MessageFormat(PolicyTool.getMessage("Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured."));
                        Object[] objArr = {signers[i2]};
                        this.tool.warnings.addElement(messageFormat.format(objArr));
                        this.tw.displayStatusDialog(this, messageFormat.format(objArr));
                    }
                } catch (Exception e2) {
                    this.tw.displayErrorDialog(this, e2);
                }
            }
        }
        return permissionEntry;
    }

    void displayConfirmRemovePolicyEntry() {
        int selectedIndex = ((JList) this.tw.getComponent(3)).getSelectedIndex();
        PolicyEntry[] entry = this.tool.getEntry();
        this.tw.getLocationOnScreen();
        setLayout(new GridBagLayout());
        this.tw.addNewComponent(this, new JLabel(PolicyTool.getMessage("Remove.this.Policy.Entry.")), 0, 0, 0, 2, 1, 0.0d, 0.0d, 1, ToolWindow.BOTTOM_PADDING);
        this.tw.addNewComponent(this, new JLabel(entry[selectedIndex].codebaseToString()), 1, 0, 1, 2, 1, 0.0d, 0.0d, 1);
        this.tw.addNewComponent(this, new JLabel(entry[selectedIndex].principalsToString().trim()), 2, 0, 2, 2, 1, 0.0d, 0.0d, 1);
        Vector<PolicyParser.PermissionEntry> vector = entry[selectedIndex].getGrantEntry().permissionEntries;
        for (int i2 = 0; i2 < vector.size(); i2++) {
            JLabel jLabel = new JLabel("    " + PermissionEntryToUserFriendlyString(vector.elementAt(i2)));
            if (i2 == vector.size() - 1) {
                this.tw.addNewComponent(this, jLabel, 3 + i2, 1, 3 + i2, 1, 1, 0.0d, 0.0d, 1, ToolWindow.BOTTOM_PADDING);
            } else {
                this.tw.addNewComponent(this, jLabel, 3 + i2, 1, 3 + i2, 1, 1, 0.0d, 0.0d, 1);
            }
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        JButton jButton = new JButton(PolicyTool.getMessage("OK"));
        jButton.addActionListener(new ConfirmRemovePolicyEntryOKButtonListener(this.tool, this.tw, this));
        this.tw.addNewComponent(jPanel, jButton, 0, 0, 0, 1, 1, 0.0d, 0.0d, 3, ToolWindow.LR_PADDING);
        JButton jButton2 = new JButton(PolicyTool.getMessage("Cancel"));
        CancelButtonListener cancelButtonListener = new CancelButtonListener(this);
        jButton2.addActionListener(cancelButtonListener);
        this.tw.addNewComponent(jPanel, jButton2, 1, 1, 0, 1, 1, 0.0d, 0.0d, 3, ToolWindow.LR_PADDING);
        this.tw.addNewComponent(this, jPanel, 3 + vector.size(), 0, 3 + vector.size(), 2, 1, 0.0d, 0.0d, 3, ToolWindow.TOP_BOTTOM_PADDING);
        getRootPane().setDefaultButton(jButton);
        getRootPane().registerKeyboardAction(cancelButtonListener, escKey, 2);
        pack();
        setLocationRelativeTo(this.tw);
        setVisible(true);
    }

    void displaySaveAsDialog(int i2) {
        FileDialog fileDialog = new FileDialog(this.tw, PolicyTool.getMessage(ToolWindow.SAVE_AS_POLICY_FILE), 1);
        fileDialog.addWindowListener(new WindowAdapter() { // from class: sun.security.tools.policytool.ToolDialog.1
            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosing(WindowEvent windowEvent) {
                windowEvent.getWindow().setVisible(false);
            }
        });
        fileDialog.setVisible(true);
        if (fileDialog.getFile() == null || fileDialog.getFile().equals("")) {
            return;
        }
        String path = new File(fileDialog.getDirectory(), fileDialog.getFile()).getPath();
        fileDialog.dispose();
        try {
            this.tool.savePolicy(path);
            this.tw.displayStatusDialog(null, new MessageFormat(PolicyTool.getMessage("Policy.successfully.written.to.filename")).format(new Object[]{path}));
            ((JTextField) this.tw.getComponent(1)).setText(path);
            this.tw.setVisible(true);
            userSaveContinue(this.tool, this.tw, this, i2);
        } catch (FileNotFoundException e2) {
            if (path == null || path.equals("")) {
                this.tw.displayErrorDialog((Window) null, new FileNotFoundException(PolicyTool.getMessage("null.filename")));
            } else {
                this.tw.displayErrorDialog((Window) null, e2);
            }
        } catch (Exception e3) {
            this.tw.displayErrorDialog((Window) null, e3);
        }
    }

    void displayUserSave(int i2) {
        if (this.tool.modified) {
            this.tw.getLocationOnScreen();
            setLayout(new GridBagLayout());
            this.tw.addNewComponent(this, new JLabel(PolicyTool.getMessage("Save.changes.")), 0, 0, 0, 3, 1, 0.0d, 0.0d, 1, ToolWindow.L_TOP_BOTTOM_PADDING);
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            JButton jButton = new JButton();
            ToolWindow.configureButton(jButton, "Yes");
            jButton.addActionListener(new UserSaveYesButtonListener(this, this.tool, this.tw, i2));
            this.tw.addNewComponent(jPanel, jButton, 0, 0, 0, 1, 1, 0.0d, 0.0d, 3, ToolWindow.LR_BOTTOM_PADDING);
            JButton jButton2 = new JButton();
            ToolWindow.configureButton(jButton2, "No");
            jButton2.addActionListener(new UserSaveNoButtonListener(this, this.tool, this.tw, i2));
            this.tw.addNewComponent(jPanel, jButton2, 1, 1, 0, 1, 1, 0.0d, 0.0d, 3, ToolWindow.LR_BOTTOM_PADDING);
            JButton jButton3 = new JButton();
            ToolWindow.configureButton(jButton3, "Cancel");
            CancelButtonListener cancelButtonListener = new CancelButtonListener(this);
            jButton3.addActionListener(cancelButtonListener);
            this.tw.addNewComponent(jPanel, jButton3, 2, 2, 0, 1, 1, 0.0d, 0.0d, 3, ToolWindow.LR_BOTTOM_PADDING);
            this.tw.addNewComponent(this, jPanel, 1, 0, 1, 1, 1, 0.0d, 0.0d, 1);
            getRootPane().registerKeyboardAction(cancelButtonListener, escKey, 2);
            pack();
            setLocationRelativeTo(this.tw);
            setVisible(true);
            return;
        }
        userSaveContinue(this.tool, this.tw, this, i2);
    }

    void userSaveContinue(PolicyTool policyTool, ToolWindow toolWindow, ToolDialog toolDialog, int i2) {
        switch (i2) {
            case 1:
                toolWindow.setVisible(false);
                toolWindow.dispose();
                System.exit(0);
                break;
            case 2:
                break;
            case 3:
                FileDialog fileDialog = new FileDialog(toolWindow, PolicyTool.getMessage(ToolWindow.OPEN_POLICY_FILE), 0);
                fileDialog.addWindowListener(new WindowAdapter() { // from class: sun.security.tools.policytool.ToolDialog.2
                    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                    public void windowClosing(WindowEvent windowEvent) {
                        windowEvent.getWindow().setVisible(false);
                    }
                });
                fileDialog.setVisible(true);
                if (fileDialog.getFile() == null || fileDialog.getFile().equals("")) {
                    return;
                }
                String path = new File(fileDialog.getDirectory(), fileDialog.getFile()).getPath();
                try {
                    policyTool.openPolicy(path);
                    DefaultListModel defaultListModel = new DefaultListModel();
                    JList jList = new JList(defaultListModel);
                    jList.setVisibleRowCount(15);
                    jList.setSelectionMode(0);
                    jList.addMouseListener(new PolicyListListener(policyTool, toolWindow));
                    PolicyEntry[] entry = policyTool.getEntry();
                    if (entry != null) {
                        for (PolicyEntry policyEntry : entry) {
                            defaultListModel.addElement(policyEntry.headerToString());
                        }
                    }
                    toolWindow.replacePolicyList(jList);
                    policyTool.modified = false;
                    ((JTextField) toolWindow.getComponent(1)).setText(path);
                    toolWindow.setVisible(true);
                    if (policyTool.newWarning) {
                        toolWindow.displayStatusDialog(null, PolicyTool.getMessage("Errors.have.occurred.while.opening.the.policy.configuration.View.the.Warning.Log.for.more.information."));
                    }
                    return;
                } catch (Exception e2) {
                    JList jList2 = new JList(new DefaultListModel());
                    jList2.setVisibleRowCount(15);
                    jList2.setSelectionMode(0);
                    jList2.addMouseListener(new PolicyListListener(policyTool, toolWindow));
                    toolWindow.replacePolicyList(jList2);
                    policyTool.setPolicyFileName(null);
                    policyTool.modified = false;
                    ((JTextField) toolWindow.getComponent(1)).setText("");
                    toolWindow.setVisible(true);
                    toolWindow.displayErrorDialog((Window) null, new MessageFormat(PolicyTool.getMessage("Could.not.open.policy.file.policyFile.e.toString.")).format(new Object[]{path, e2.toString()}));
                    return;
                }
            default:
                return;
        }
        try {
            policyTool.openPolicy(null);
        } catch (Exception e3) {
            policyTool.modified = false;
            toolWindow.displayErrorDialog((Window) null, e3);
        }
        JList jList3 = new JList(new DefaultListModel());
        jList3.setVisibleRowCount(15);
        jList3.setSelectionMode(0);
        jList3.addMouseListener(new PolicyListListener(policyTool, toolWindow));
        toolWindow.replacePolicyList(jList3);
        ((JTextField) toolWindow.getComponent(1)).setText("");
        toolWindow.setVisible(true);
    }

    void setPermissionNames(Perm perm, JComboBox jComboBox, JTextField jTextField) {
        jComboBox.removeAllItems();
        jComboBox.addItem(PERM_NAME);
        if (perm == null) {
            jTextField.setEditable(true);
            return;
        }
        if (perm.TARGETS == null) {
            jTextField.setEditable(false);
            return;
        }
        jTextField.setEditable(true);
        for (int i2 = 0; i2 < perm.TARGETS.length; i2++) {
            jComboBox.addItem(perm.TARGETS[i2]);
        }
    }

    void setPermissionActions(Perm perm, JComboBox jComboBox, JTextField jTextField) {
        jComboBox.removeAllItems();
        jComboBox.addItem(PERM_ACTIONS);
        if (perm == null) {
            jTextField.setEditable(true);
            return;
        }
        if (perm.ACTIONS == null) {
            jTextField.setEditable(false);
            return;
        }
        jTextField.setEditable(true);
        for (int i2 = 0; i2 < perm.ACTIONS.length; i2++) {
            jComboBox.addItem(perm.ACTIONS[i2]);
        }
    }

    static String PermissionEntryToUserFriendlyString(PolicyParser.PermissionEntry permissionEntry) {
        String str = permissionEntry.permission;
        if (permissionEntry.name != null) {
            str = str + " " + permissionEntry.name;
        }
        if (permissionEntry.action != null) {
            str = str + ", \"" + permissionEntry.action + PdfOps.DOUBLE_QUOTE__TOKEN;
        }
        if (permissionEntry.signedBy != null) {
            str = str + ", signedBy " + permissionEntry.signedBy;
        }
        return str;
    }

    static String PrincipalEntryToUserFriendlyString(PolicyParser.PrincipalEntry principalEntry) {
        StringWriter stringWriter = new StringWriter();
        principalEntry.write(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
