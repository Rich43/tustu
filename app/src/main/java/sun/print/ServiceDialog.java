package sun.print;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.ServiceUIFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.CopiesSupported;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.JobPriority;
import javax.print.attribute.standard.JobSheets;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterInfo;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterMakeAndModel;
import javax.print.attribute.standard.RequestingUserName;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.NumberFormatter;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/print/ServiceDialog.class */
public class ServiceDialog extends JDialog implements ActionListener {
    public static final int WAITING = 0;
    public static final int APPROVE = 1;
    public static final int CANCEL = 2;
    private static final String strBundle = "sun.print.resources.serviceui";
    private static final Insets panelInsets = new Insets(6, 6, 6, 6);
    private static final Insets compInsets = new Insets(3, 6, 3, 6);
    private static ResourceBundle messageRB;
    private JTabbedPane tpTabs;
    private JButton btnCancel;
    private JButton btnApprove;
    private PrintService[] services;
    private int defaultServiceIndex;
    private PrintRequestAttributeSet asOriginal;
    private HashPrintRequestAttributeSet asCurrent;
    private PrintService psCurrent;
    private DocFlavor docFlavor;
    private int status;
    private ValidatingFileChooser jfc;
    private GeneralPanel pnlGeneral;
    private PageSetupPanel pnlPageSetup;
    private AppearancePanel pnlAppearance;
    private boolean isAWT;
    static Class _keyEventClazz;

    static {
        initResource();
        _keyEventClazz = null;
    }

    public ServiceDialog(GraphicsConfiguration graphicsConfiguration, int i2, int i3, PrintService[] printServiceArr, int i4, DocFlavor docFlavor, PrintRequestAttributeSet printRequestAttributeSet, Dialog dialog) throws IllegalArgumentException {
        super(dialog, getMsg("dialog.printtitle"), true, graphicsConfiguration);
        this.isAWT = false;
        initPrintDialog(i2, i3, printServiceArr, i4, docFlavor, printRequestAttributeSet);
    }

    public ServiceDialog(GraphicsConfiguration graphicsConfiguration, int i2, int i3, PrintService[] printServiceArr, int i4, DocFlavor docFlavor, PrintRequestAttributeSet printRequestAttributeSet, Frame frame) throws IllegalArgumentException {
        super(frame, getMsg("dialog.printtitle"), true, graphicsConfiguration);
        this.isAWT = false;
        initPrintDialog(i2, i3, printServiceArr, i4, docFlavor, printRequestAttributeSet);
    }

    void initPrintDialog(int i2, int i3, PrintService[] printServiceArr, int i4, DocFlavor docFlavor, PrintRequestAttributeSet printRequestAttributeSet) throws IllegalArgumentException {
        this.services = printServiceArr;
        this.defaultServiceIndex = i4;
        this.asOriginal = printRequestAttributeSet;
        this.asCurrent = new HashPrintRequestAttributeSet(printRequestAttributeSet);
        this.psCurrent = printServiceArr[i4];
        this.docFlavor = docFlavor;
        if (((SunPageSelection) printRequestAttributeSet.get(SunPageSelection.class)) != null) {
            this.isAWT = true;
        }
        if (printRequestAttributeSet.get(DialogOnTop.class) != null) {
            setAlwaysOnTop(true);
        }
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        this.tpTabs = new JTabbedPane();
        this.tpTabs.setBorder(new EmptyBorder(5, 5, 5, 5));
        String msg = getMsg("tab.general");
        int vKMnemonic = getVKMnemonic("tab.general");
        this.pnlGeneral = new GeneralPanel();
        this.tpTabs.add(msg, this.pnlGeneral);
        this.tpTabs.setMnemonicAt(0, vKMnemonic);
        String msg2 = getMsg("tab.pagesetup");
        int vKMnemonic2 = getVKMnemonic("tab.pagesetup");
        this.pnlPageSetup = new PageSetupPanel();
        this.tpTabs.add(msg2, this.pnlPageSetup);
        this.tpTabs.setMnemonicAt(1, vKMnemonic2);
        String msg3 = getMsg("tab.appearance");
        int vKMnemonic3 = getVKMnemonic("tab.appearance");
        this.pnlAppearance = new AppearancePanel();
        this.tpTabs.add(msg3, this.pnlAppearance);
        this.tpTabs.setMnemonicAt(2, vKMnemonic3);
        contentPane.add(this.tpTabs, BorderLayout.CENTER);
        updatePanels();
        JPanel jPanel = new JPanel(new FlowLayout(4));
        this.btnApprove = createExitButton("button.print", this);
        jPanel.add(this.btnApprove);
        getRootPane().setDefaultButton(this.btnApprove);
        this.btnCancel = createExitButton("button.cancel", this);
        handleEscKey(this.btnCancel);
        jPanel.add(this.btnCancel);
        contentPane.add(jPanel, "South");
        addWindowListener(new WindowAdapter() { // from class: sun.print.ServiceDialog.1
            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosing(WindowEvent windowEvent) {
                ServiceDialog.this.dispose(2);
            }
        });
        getAccessibleContext().setAccessibleDescription(getMsg("dialog.printtitle"));
        setResizable(false);
        setLocation(i2, i3);
        pack();
    }

    public ServiceDialog(GraphicsConfiguration graphicsConfiguration, int i2, int i3, PrintService printService, DocFlavor docFlavor, PrintRequestAttributeSet printRequestAttributeSet, Dialog dialog) {
        super(dialog, getMsg("dialog.pstitle"), true, graphicsConfiguration);
        this.isAWT = false;
        initPageDialog(i2, i3, printService, docFlavor, printRequestAttributeSet);
    }

    public ServiceDialog(GraphicsConfiguration graphicsConfiguration, int i2, int i3, PrintService printService, DocFlavor docFlavor, PrintRequestAttributeSet printRequestAttributeSet, Frame frame) {
        super(frame, getMsg("dialog.pstitle"), true, graphicsConfiguration);
        this.isAWT = false;
        initPageDialog(i2, i3, printService, docFlavor, printRequestAttributeSet);
    }

    void initPageDialog(int i2, int i3, PrintService printService, DocFlavor docFlavor, PrintRequestAttributeSet printRequestAttributeSet) {
        this.psCurrent = printService;
        this.docFlavor = docFlavor;
        this.asOriginal = printRequestAttributeSet;
        this.asCurrent = new HashPrintRequestAttributeSet(printRequestAttributeSet);
        if (printRequestAttributeSet.get(DialogOnTop.class) != null) {
            setAlwaysOnTop(true);
        }
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        this.pnlPageSetup = new PageSetupPanel();
        contentPane.add(this.pnlPageSetup, BorderLayout.CENTER);
        this.pnlPageSetup.updateInfo();
        JPanel jPanel = new JPanel(new FlowLayout(4));
        this.btnApprove = createExitButton("button.ok", this);
        jPanel.add(this.btnApprove);
        getRootPane().setDefaultButton(this.btnApprove);
        this.btnCancel = createExitButton("button.cancel", this);
        handleEscKey(this.btnCancel);
        jPanel.add(this.btnCancel);
        contentPane.add(jPanel, "South");
        addWindowListener(new WindowAdapter() { // from class: sun.print.ServiceDialog.2
            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
            public void windowClosing(WindowEvent windowEvent) {
                ServiceDialog.this.dispose(2);
            }
        });
        getAccessibleContext().setAccessibleDescription(getMsg("dialog.pstitle"));
        setResizable(false);
        setLocation(i2, i3);
        pack();
    }

    private void handleEscKey(JButton jButton) {
        AbstractAction abstractAction = new AbstractAction() { // from class: sun.print.ServiceDialog.3
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                ServiceDialog.this.dispose(2);
            }
        };
        KeyStroke keyStroke = KeyStroke.getKeyStroke(27, 0);
        InputMap inputMap = jButton.getInputMap(2);
        ActionMap actionMap = jButton.getActionMap();
        if (inputMap != null && actionMap != null) {
            inputMap.put(keyStroke, "cancel");
            actionMap.put("cancel", abstractAction);
        }
    }

    public int getStatus() {
        return this.status;
    }

    public PrintRequestAttributeSet getAttributes() {
        if (this.status == 1) {
            return this.asCurrent;
        }
        return this.asOriginal;
    }

    public PrintService getPrintService() {
        if (this.status == 1) {
            return this.psCurrent;
        }
        return null;
    }

    public void dispose(int i2) {
        this.status = i2;
        super.dispose();
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        boolean zShowFileChooser = false;
        if (actionEvent.getSource() == this.btnApprove) {
            zShowFileChooser = true;
            if (this.pnlGeneral != null) {
                if (this.pnlGeneral.isPrintToFileRequested()) {
                    zShowFileChooser = showFileChooser();
                } else {
                    this.asCurrent.remove(Destination.class);
                }
            }
        }
        dispose(zShowFileChooser ? 1 : 2);
    }

    private boolean showFileChooser() {
        File file;
        Destination destination = (Destination) this.asCurrent.get(Destination.class);
        if (destination == null) {
            destination = (Destination) this.asOriginal.get(Destination.class);
            if (destination == null) {
                destination = (Destination) this.psCurrent.getDefaultAttributeValue(Destination.class);
                if (destination == null) {
                    try {
                        destination = new Destination(new URI("file:out.prn"));
                    } catch (URISyntaxException e2) {
                    }
                }
            }
        }
        if (destination != null) {
            try {
                file = new File(destination.getURI());
            } catch (Exception e3) {
                file = new File("out.prn");
            }
        } else {
            file = new File("out.prn");
        }
        ValidatingFileChooser validatingFileChooser = new ValidatingFileChooser();
        validatingFileChooser.setApproveButtonText(getMsg("button.ok"));
        validatingFileChooser.setDialogTitle(getMsg("dialog.printtofile"));
        validatingFileChooser.setDialogType(1);
        validatingFileChooser.setSelectedFile(file);
        int iShowDialog = validatingFileChooser.showDialog(this, null);
        if (iShowDialog != 0) {
            this.asCurrent.remove(Destination.class);
        } else {
            try {
                this.asCurrent.add(new Destination(validatingFileChooser.getSelectedFile().toURI()));
            } catch (Exception e4) {
                this.asCurrent.remove(Destination.class);
            }
        }
        return iShowDialog == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePanels() throws IllegalArgumentException {
        this.pnlGeneral.updateInfo();
        this.pnlPageSetup.updateInfo();
        this.pnlAppearance.updateInfo();
    }

    public static void initResource() {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.ServiceDialog.4
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                try {
                    ResourceBundle unused = ServiceDialog.messageRB = ResourceBundle.getBundle(ServiceDialog.strBundle);
                    return null;
                } catch (MissingResourceException e2) {
                    throw new Error("Fatal: Resource for ServiceUI is missing");
                }
            }
        });
    }

    public static String getMsg(String str) {
        try {
            return removeMnemonics(messageRB.getString(str));
        } catch (MissingResourceException e2) {
            throw new Error("Fatal: Resource for ServiceUI is broken; there is no " + str + " key in resource");
        }
    }

    private static String removeMnemonics(String str) {
        int iIndexOf = str.indexOf(38);
        int length = str.length();
        if (iIndexOf < 0 || iIndexOf == length - 1) {
            return str;
        }
        int iIndexOf2 = str.indexOf(38, iIndexOf + 1);
        if (iIndexOf2 == iIndexOf + 1) {
            if (iIndexOf2 + 1 == length) {
                return str.substring(0, iIndexOf + 1);
            }
            return str.substring(0, iIndexOf + 1) + removeMnemonics(str.substring(iIndexOf2 + 1));
        }
        if (iIndexOf == 0) {
            return removeMnemonics(str.substring(1));
        }
        return str.substring(0, iIndexOf) + removeMnemonics(str.substring(iIndexOf + 1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static char getMnemonic(String str) {
        String strReplace = messageRB.getString(str).replace("&&", "");
        int iIndexOf = strReplace.indexOf(38);
        if (0 <= iIndexOf && iIndexOf < strReplace.length() - 1) {
            return Character.toUpperCase(strReplace.charAt(iIndexOf + 1));
        }
        return (char) 0;
    }

    private static int getVKMnemonic(String str) {
        String strValueOf = String.valueOf(getMnemonic(str));
        if (strValueOf == null || strValueOf.length() != 1) {
            return 0;
        }
        String str2 = "VK_" + strValueOf.toUpperCase();
        try {
            if (_keyEventClazz == null) {
                _keyEventClazz = Class.forName("java.awt.event.KeyEvent", true, ServiceDialog.class.getClassLoader());
            }
            return _keyEventClazz.getDeclaredField(str2).getInt(null);
        } catch (Exception e2) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static URL getImageResource(final String str) {
        URL url = (URL) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.ServiceDialog.5
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return ServiceDialog.class.getResource("resources/" + str);
            }
        });
        if (url == null) {
            throw new Error("Fatal: Resource for ServiceUI is broken; there is no " + str + " key in resource");
        }
        return url;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JButton createButton(String str, ActionListener actionListener) {
        JButton jButton = new JButton(getMsg(str));
        jButton.setMnemonic(getMnemonic(str));
        jButton.addActionListener(actionListener);
        return jButton;
    }

    private static JButton createExitButton(String str, ActionListener actionListener) {
        String msg = getMsg(str);
        JButton jButton = new JButton(msg);
        jButton.addActionListener(actionListener);
        jButton.getAccessibleContext().setAccessibleDescription(msg);
        return jButton;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JCheckBox createCheckBox(String str, ActionListener actionListener) {
        JCheckBox jCheckBox = new JCheckBox(getMsg(str));
        jCheckBox.setMnemonic(getMnemonic(str));
        jCheckBox.addActionListener(actionListener);
        return jCheckBox;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JRadioButton createRadioButton(String str, ActionListener actionListener) {
        JRadioButton jRadioButton = new JRadioButton(getMsg(str));
        jRadioButton.setMnemonic(getMnemonic(str));
        jRadioButton.addActionListener(actionListener);
        return jRadioButton;
    }

    public static void showNoPrintService(GraphicsConfiguration graphicsConfiguration) throws HeadlessException {
        Frame frame = new Frame(graphicsConfiguration);
        JOptionPane.showMessageDialog(frame, getMsg("dialog.noprintermsg"));
        frame.dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addToGB(Component component, Container container, GridBagLayout gridBagLayout, GridBagConstraints gridBagConstraints) {
        gridBagLayout.setConstraints(component, gridBagConstraints);
        container.add(component);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addToBG(AbstractButton abstractButton, Container container, ButtonGroup buttonGroup) {
        buttonGroup.add(abstractButton);
        container.add(abstractButton);
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$GeneralPanel.class */
    private class GeneralPanel extends JPanel {
        private PrintServicePanel pnlPrintService;
        private PrintRangePanel pnlPrintRange;
        private CopiesPanel pnlCopies;

        public GeneralPanel() {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            gridBagConstraints.fill = 1;
            gridBagConstraints.insets = ServiceDialog.panelInsets;
            gridBagConstraints.weightx = 1.0d;
            gridBagConstraints.weighty = 1.0d;
            gridBagConstraints.gridwidth = 0;
            this.pnlPrintService = ServiceDialog.this.new PrintServicePanel();
            ServiceDialog.addToGB(this.pnlPrintService, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = -1;
            this.pnlPrintRange = ServiceDialog.this.new PrintRangePanel();
            ServiceDialog.addToGB(this.pnlPrintRange, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            this.pnlCopies = ServiceDialog.this.new CopiesPanel();
            ServiceDialog.addToGB(this.pnlCopies, this, gridBagLayout, gridBagConstraints);
        }

        public boolean isPrintToFileRequested() {
            return this.pnlPrintService.isPrintToFileSelected();
        }

        public void updateInfo() throws IllegalArgumentException {
            this.pnlPrintService.updateInfo();
            this.pnlPrintRange.updateInfo();
            this.pnlCopies.updateInfo();
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$PrintServicePanel.class */
    private class PrintServicePanel extends JPanel implements ActionListener, ItemListener, PopupMenuListener {
        private FilePermission printToFilePermission;
        private JButton btnProperties;
        private JCheckBox cbPrintToFile;
        private JComboBox cbName;
        private JLabel lblType;
        private JLabel lblStatus;
        private JLabel lblInfo;
        private ServiceUIFactory uiFactory;
        private boolean filePermission;
        private final String strTitle = ServiceDialog.getMsg("border.printservice");
        private boolean changedService = false;

        public PrintServicePanel() throws IllegalArgumentException {
            this.uiFactory = ServiceDialog.this.psCurrent.getServiceUIFactory();
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            String[] strArr = new String[ServiceDialog.this.services.length];
            for (int i2 = 0; i2 < strArr.length; i2++) {
                strArr[i2] = ServiceDialog.this.services[i2].getName();
            }
            this.cbName = new JComboBox(strArr);
            this.cbName.setSelectedIndex(ServiceDialog.this.defaultServiceIndex);
            this.cbName.addItemListener(this);
            this.cbName.addPopupMenuListener(this);
            gridBagConstraints.fill = 1;
            gridBagConstraints.insets = ServiceDialog.compInsets;
            gridBagConstraints.weightx = 0.0d;
            JLabel jLabel = new JLabel(ServiceDialog.getMsg("label.psname"), 11);
            jLabel.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.psname"));
            jLabel.setLabelFor(this.cbName);
            ServiceDialog.addToGB(jLabel, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.weightx = 1.0d;
            gridBagConstraints.gridwidth = -1;
            ServiceDialog.addToGB(this.cbName, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.weightx = 0.0d;
            gridBagConstraints.gridwidth = 0;
            this.btnProperties = ServiceDialog.createButton("button.properties", this);
            ServiceDialog.addToGB(this.btnProperties, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.weighty = 1.0d;
            this.lblStatus = addLabel(ServiceDialog.getMsg("label.status"), gridBagLayout, gridBagConstraints);
            this.lblStatus.setLabelFor(null);
            this.lblType = addLabel(ServiceDialog.getMsg("label.pstype"), gridBagLayout, gridBagConstraints);
            this.lblType.setLabelFor(null);
            gridBagConstraints.gridwidth = 1;
            ServiceDialog.addToGB(new JLabel(ServiceDialog.getMsg("label.info"), 11), this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = -1;
            this.lblInfo = new JLabel();
            this.lblInfo.setLabelFor(null);
            ServiceDialog.addToGB(this.lblInfo, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            this.cbPrintToFile = ServiceDialog.createCheckBox("checkbox.printtofile", this);
            ServiceDialog.addToGB(this.cbPrintToFile, this, gridBagLayout, gridBagConstraints);
            this.filePermission = allowedToPrintToFile();
        }

        public boolean isPrintToFileSelected() {
            return this.cbPrintToFile.isSelected();
        }

        private JLabel addLabel(String str, GridBagLayout gridBagLayout, GridBagConstraints gridBagConstraints) {
            gridBagConstraints.gridwidth = 1;
            ServiceDialog.addToGB(new JLabel(str, 11), this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            JLabel jLabel = new JLabel();
            ServiceDialog.addToGB(jLabel, this, gridBagLayout, gridBagConstraints);
            return jLabel;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) throws IllegalArgumentException {
            PrinterJobWrapper printerJobWrapper;
            PrinterJob printerJob;
            PrintRequestAttributeSet printRequestAttributeSetShowDocumentProperties;
            if (actionEvent.getSource() == this.btnProperties && this.uiFactory != null) {
                JDialog jDialog = (JDialog) this.uiFactory.getUI(3, ServiceUIFactory.JDIALOG_UI);
                if (jDialog != null) {
                    jDialog.show();
                    return;
                }
                DocumentPropertiesUI documentPropertiesUI = null;
                try {
                    documentPropertiesUI = (DocumentPropertiesUI) this.uiFactory.getUI(199, DocumentPropertiesUI.DOCPROPERTIESCLASSNAME);
                } catch (Exception e2) {
                }
                if (documentPropertiesUI != null && (printerJobWrapper = (PrinterJobWrapper) ServiceDialog.this.asCurrent.get(PrinterJobWrapper.class)) != null && (printerJob = printerJobWrapper.getPrinterJob()) != null && (printRequestAttributeSetShowDocumentProperties = documentPropertiesUI.showDocumentProperties(printerJob, ServiceDialog.this, ServiceDialog.this.psCurrent, ServiceDialog.this.asCurrent)) != null) {
                    ServiceDialog.this.asCurrent.addAll(printRequestAttributeSetShowDocumentProperties);
                    ServiceDialog.this.updatePanels();
                }
            }
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
            int selectedIndex;
            if (itemEvent.getStateChange() == 1 && (selectedIndex = this.cbName.getSelectedIndex()) >= 0 && selectedIndex < ServiceDialog.this.services.length && !ServiceDialog.this.services[selectedIndex].equals(ServiceDialog.this.psCurrent)) {
                ServiceDialog.this.psCurrent = ServiceDialog.this.services[selectedIndex];
                this.uiFactory = ServiceDialog.this.psCurrent.getServiceUIFactory();
                this.changedService = true;
                Destination destination = (Destination) ServiceDialog.this.asOriginal.get(Destination.class);
                if ((destination == null && !isPrintToFileSelected()) || !ServiceDialog.this.psCurrent.isAttributeCategorySupported(Destination.class)) {
                    ServiceDialog.this.asCurrent.remove(Destination.class);
                    return;
                }
                if (destination != null) {
                    ServiceDialog.this.asCurrent.add(destination);
                    return;
                }
                Destination destination2 = (Destination) ServiceDialog.this.psCurrent.getDefaultAttributeValue(Destination.class);
                if (destination2 == null) {
                    try {
                        destination2 = new Destination(new URI("file:out.prn"));
                    } catch (URISyntaxException e2) {
                    }
                }
                if (destination2 != null) {
                    ServiceDialog.this.asCurrent.add(destination2);
                }
            }
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
            this.changedService = false;
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) throws IllegalArgumentException {
            if (this.changedService) {
                this.changedService = false;
                ServiceDialog.this.updatePanels();
            }
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
        }

        private boolean allowedToPrintToFile() {
            try {
                throwPrintToFile();
                return true;
            } catch (SecurityException e2) {
                return false;
            }
        }

        private void throwPrintToFile() {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                if (this.printToFilePermission == null) {
                    this.printToFilePermission = new FilePermission("<<ALL FILES>>", SecurityConstants.PROPERTY_RW_ACTION);
                }
                securityManager.checkPermission(this.printToFilePermission);
            }
        }

        public void updateInfo() throws IllegalArgumentException {
            boolean z2 = false;
            boolean z3 = false;
            boolean zAllowedToPrintToFile = this.filePermission ? allowedToPrintToFile() : false;
            if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(Destination.class)) {
                z2 = true;
            }
            if (((Destination) ServiceDialog.this.asCurrent.get(Destination.class)) != null) {
                z3 = true;
            }
            this.cbPrintToFile.setEnabled(z2 && zAllowedToPrintToFile);
            this.cbPrintToFile.setSelected(z3 && zAllowedToPrintToFile && z2);
            PrintServiceAttribute attribute = ServiceDialog.this.psCurrent.getAttribute(PrinterMakeAndModel.class);
            if (attribute != null) {
                this.lblType.setText(attribute.toString());
            }
            PrintServiceAttribute attribute2 = ServiceDialog.this.psCurrent.getAttribute(PrinterIsAcceptingJobs.class);
            if (attribute2 != null) {
                this.lblStatus.setText(ServiceDialog.getMsg(attribute2.toString()));
            }
            PrintServiceAttribute attribute3 = ServiceDialog.this.psCurrent.getAttribute(PrinterInfo.class);
            if (attribute3 != null) {
                this.lblInfo.setText(attribute3.toString());
            }
            this.btnProperties.setEnabled(this.uiFactory != null);
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$PrintRangePanel.class */
    private class PrintRangePanel extends JPanel implements ActionListener, FocusListener {
        private final String strTitle = ServiceDialog.getMsg("border.printrange");
        private final PageRanges prAll = new PageRanges(1, Integer.MAX_VALUE);
        private JRadioButton rbAll;
        private JRadioButton rbPages;
        private JRadioButton rbSelect;
        private JFormattedTextField tfRangeFrom;
        private JFormattedTextField tfRangeTo;
        private JLabel lblRangeTo;
        private boolean prSupported;

        public PrintRangePanel() {
            NumberFormatter numberFormatter;
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            gridBagConstraints.fill = 1;
            gridBagConstraints.insets = ServiceDialog.compInsets;
            gridBagConstraints.gridwidth = 0;
            ButtonGroup buttonGroup = new ButtonGroup();
            JPanel jPanel = new JPanel(new FlowLayout(3));
            this.rbAll = ServiceDialog.createRadioButton("radiobutton.rangeall", this);
            this.rbAll.setSelected(true);
            buttonGroup.add(this.rbAll);
            jPanel.add(this.rbAll);
            ServiceDialog.addToGB(jPanel, this, gridBagLayout, gridBagConstraints);
            JPanel jPanel2 = new JPanel(new FlowLayout(3));
            this.rbPages = ServiceDialog.createRadioButton("radiobutton.rangepages", this);
            buttonGroup.add(this.rbPages);
            jPanel2.add(this.rbPages);
            DecimalFormat decimalFormat = new DecimalFormat("####0");
            decimalFormat.setMinimumFractionDigits(0);
            decimalFormat.setMaximumFractionDigits(0);
            decimalFormat.setMinimumIntegerDigits(0);
            decimalFormat.setMaximumIntegerDigits(5);
            decimalFormat.setParseIntegerOnly(true);
            decimalFormat.setDecimalSeparatorAlwaysShown(false);
            NumberFormatter numberFormatter2 = new NumberFormatter(decimalFormat);
            numberFormatter2.setMinimum(new Integer(1));
            numberFormatter2.setMaximum(new Integer(Integer.MAX_VALUE));
            numberFormatter2.setAllowsInvalid(true);
            numberFormatter2.setCommitsOnValidEdit(true);
            this.tfRangeFrom = new JFormattedTextField((JFormattedTextField.AbstractFormatter) numberFormatter2);
            this.tfRangeFrom.setColumns(4);
            this.tfRangeFrom.setEnabled(false);
            this.tfRangeFrom.addActionListener(this);
            this.tfRangeFrom.addFocusListener(this);
            this.tfRangeFrom.setFocusLostBehavior(3);
            this.tfRangeFrom.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("radiobutton.rangepages"));
            jPanel2.add(this.tfRangeFrom);
            this.lblRangeTo = new JLabel(ServiceDialog.getMsg("label.rangeto"));
            this.lblRangeTo.setEnabled(false);
            jPanel2.add(this.lblRangeTo);
            try {
                numberFormatter = (NumberFormatter) numberFormatter2.clone();
            } catch (CloneNotSupportedException e2) {
                numberFormatter = new NumberFormatter();
            }
            this.tfRangeTo = new JFormattedTextField((JFormattedTextField.AbstractFormatter) numberFormatter);
            this.tfRangeTo.setColumns(4);
            this.tfRangeTo.setEnabled(false);
            this.tfRangeTo.addFocusListener(this);
            this.tfRangeTo.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.rangeto"));
            jPanel2.add(this.tfRangeTo);
            ServiceDialog.addToGB(jPanel2, this, gridBagLayout, gridBagConstraints);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            SunPageSelection sunPageSelection = SunPageSelection.ALL;
            setupRangeWidgets();
            if (source == this.rbAll) {
                ServiceDialog.this.asCurrent.add(this.prAll);
            } else if (source == this.rbSelect) {
                sunPageSelection = SunPageSelection.SELECTION;
            } else if (source == this.rbPages || source == this.tfRangeFrom || source == this.tfRangeTo) {
                updateRangeAttribute();
                sunPageSelection = SunPageSelection.RANGE;
            }
            if (ServiceDialog.this.isAWT) {
                ServiceDialog.this.asCurrent.add(sunPageSelection);
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            Object source = focusEvent.getSource();
            if (source == this.tfRangeFrom || source == this.tfRangeTo) {
                updateRangeAttribute();
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
        }

        private void setupRangeWidgets() {
            boolean z2 = this.rbPages.isSelected() && this.prSupported;
            this.tfRangeFrom.setEnabled(z2);
            this.tfRangeTo.setEnabled(z2);
            this.lblRangeTo.setEnabled(z2);
        }

        private void updateRangeAttribute() {
            int i2;
            int i3;
            String text = this.tfRangeFrom.getText();
            String text2 = this.tfRangeTo.getText();
            try {
                i2 = Integer.parseInt(text);
            } catch (NumberFormatException e2) {
                i2 = 1;
            }
            try {
                i3 = Integer.parseInt(text2);
            } catch (NumberFormatException e3) {
                i3 = i2;
            }
            if (i2 < 1) {
                i2 = 1;
                this.tfRangeFrom.setValue(new Integer(1));
            }
            if (i3 < i2) {
                i3 = i2;
                this.tfRangeTo.setValue(new Integer(i2));
            }
            ServiceDialog.this.asCurrent.add(new PageRanges(i2, i3));
        }

        public void updateInfo() {
            this.prSupported = false;
            if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(PageRanges.class) || ServiceDialog.this.isAWT) {
                this.prSupported = true;
            }
            SunPageSelection sunPageSelection = SunPageSelection.ALL;
            int i2 = 1;
            int i3 = 1;
            PageRanges pageRanges = (PageRanges) ServiceDialog.this.asCurrent.get(PageRanges.class);
            if (pageRanges != null && !pageRanges.equals(this.prAll)) {
                sunPageSelection = SunPageSelection.RANGE;
                int[][] members = pageRanges.getMembers();
                if (members.length > 0 && members[0].length > 1) {
                    i2 = members[0][0];
                    i3 = members[0][1];
                }
            }
            if (ServiceDialog.this.isAWT) {
                sunPageSelection = (SunPageSelection) ServiceDialog.this.asCurrent.get(SunPageSelection.class);
            }
            if (sunPageSelection == SunPageSelection.ALL) {
                this.rbAll.setSelected(true);
            } else if (sunPageSelection != SunPageSelection.SELECTION) {
                this.rbPages.setSelected(true);
            }
            this.tfRangeFrom.setValue(new Integer(i2));
            this.tfRangeTo.setValue(new Integer(i3));
            this.rbAll.setEnabled(this.prSupported);
            this.rbPages.setEnabled(this.prSupported);
            setupRangeWidgets();
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$CopiesPanel.class */
    private class CopiesPanel extends JPanel implements ActionListener, ChangeListener {
        private final String strTitle = ServiceDialog.getMsg("border.copies");
        private SpinnerNumberModel snModel;
        private JSpinner spinCopies;
        private JLabel lblCopies;
        private JCheckBox cbCollate;
        private boolean scSupported;

        public CopiesPanel() throws IllegalArgumentException {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            gridBagConstraints.fill = 2;
            gridBagConstraints.insets = ServiceDialog.compInsets;
            this.lblCopies = new JLabel(ServiceDialog.getMsg("label.numcopies"), 11);
            this.lblCopies.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.numcopies"));
            this.lblCopies.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.numcopies"));
            ServiceDialog.addToGB(this.lblCopies, this, gridBagLayout, gridBagConstraints);
            this.snModel = new SpinnerNumberModel(1, 1, 999, 1);
            this.spinCopies = new JSpinner(this.snModel);
            this.lblCopies.setLabelFor(this.spinCopies);
            ((JSpinner.NumberEditor) this.spinCopies.getEditor()).getTextField().setColumns(3);
            this.spinCopies.addChangeListener(this);
            gridBagConstraints.gridwidth = 0;
            ServiceDialog.addToGB(this.spinCopies, this, gridBagLayout, gridBagConstraints);
            this.cbCollate = ServiceDialog.createCheckBox("checkbox.collate", this);
            this.cbCollate.setEnabled(false);
            ServiceDialog.addToGB(this.cbCollate, this, gridBagLayout, gridBagConstraints);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.cbCollate.isSelected()) {
                ServiceDialog.this.asCurrent.add(SheetCollate.COLLATED);
            } else {
                ServiceDialog.this.asCurrent.add(SheetCollate.UNCOLLATED);
            }
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            updateCollateCB();
            ServiceDialog.this.asCurrent.add(new Copies(this.snModel.getNumber().intValue()));
        }

        private void updateCollateCB() {
            int iIntValue = this.snModel.getNumber().intValue();
            if (ServiceDialog.this.isAWT) {
                this.cbCollate.setEnabled(true);
            } else {
                this.cbCollate.setEnabled(iIntValue > 1 && this.scSupported);
            }
        }

        public void updateInfo() {
            int i2;
            int i3;
            boolean z2 = false;
            this.scSupported = false;
            if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(Copies.class)) {
                z2 = true;
            }
            CopiesSupported copiesSupported = (CopiesSupported) ServiceDialog.this.psCurrent.getSupportedAttributeValues(Copies.class, null, null);
            if (copiesSupported == null) {
                copiesSupported = new CopiesSupported(1, 999);
            }
            Copies copies = (Copies) ServiceDialog.this.asCurrent.get(Copies.class);
            if (copies == null) {
                copies = (Copies) ServiceDialog.this.psCurrent.getDefaultAttributeValue(Copies.class);
                if (copies == null) {
                    copies = new Copies(1);
                }
            }
            this.spinCopies.setEnabled(z2);
            this.lblCopies.setEnabled(z2);
            int[][] members = copiesSupported.getMembers();
            if (members.length > 0 && members[0].length > 0) {
                i2 = members[0][0];
                i3 = members[0][1];
            } else {
                i2 = 1;
                i3 = Integer.MAX_VALUE;
            }
            this.snModel.setMinimum(new Integer(i2));
            this.snModel.setMaximum(new Integer(i3));
            int value = copies.getValue();
            if (value < i2 || value > i3) {
                value = i2;
            }
            this.snModel.setValue(new Integer(value));
            if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(SheetCollate.class)) {
                this.scSupported = true;
            }
            SheetCollate sheetCollate = (SheetCollate) ServiceDialog.this.asCurrent.get(SheetCollate.class);
            if (sheetCollate == null) {
                sheetCollate = (SheetCollate) ServiceDialog.this.psCurrent.getDefaultAttributeValue(SheetCollate.class);
                if (sheetCollate == null) {
                    sheetCollate = SheetCollate.UNCOLLATED;
                }
            }
            this.cbCollate.setSelected(sheetCollate == SheetCollate.COLLATED);
            updateCollateCB();
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$PageSetupPanel.class */
    private class PageSetupPanel extends JPanel {
        private MediaPanel pnlMedia;
        private OrientationPanel pnlOrientation;
        private MarginsPanel pnlMargins;

        public PageSetupPanel() {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            gridBagConstraints.fill = 1;
            gridBagConstraints.insets = ServiceDialog.panelInsets;
            gridBagConstraints.weightx = 1.0d;
            gridBagConstraints.weighty = 1.0d;
            gridBagConstraints.gridwidth = 0;
            this.pnlMedia = ServiceDialog.this.new MediaPanel();
            ServiceDialog.addToGB(this.pnlMedia, this, gridBagLayout, gridBagConstraints);
            this.pnlOrientation = ServiceDialog.this.new OrientationPanel();
            gridBagConstraints.gridwidth = -1;
            ServiceDialog.addToGB(this.pnlOrientation, this, gridBagLayout, gridBagConstraints);
            this.pnlMargins = ServiceDialog.this.new MarginsPanel();
            this.pnlOrientation.addOrientationListener(this.pnlMargins);
            this.pnlMedia.addMediaListener(this.pnlMargins);
            gridBagConstraints.gridwidth = 0;
            ServiceDialog.addToGB(this.pnlMargins, this, gridBagLayout, gridBagConstraints);
        }

        public void updateInfo() {
            this.pnlMedia.updateInfo();
            this.pnlOrientation.updateInfo();
            this.pnlMargins.updateInfo();
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$MarginsPanel.class */
    private class MarginsPanel extends JPanel implements ActionListener, FocusListener {
        private JFormattedTextField leftMargin;
        private JFormattedTextField rightMargin;
        private JFormattedTextField topMargin;
        private JFormattedTextField bottomMargin;
        private JLabel lblLeft;
        private JLabel lblRight;
        private JLabel lblTop;
        private JLabel lblBottom;
        private int units;
        private Float lmObj;
        private Float rmObj;
        private Float tmObj;
        private Float bmObj;
        private final String strTitle = ServiceDialog.getMsg("border.margins");
        private float lmVal = -1.0f;
        private float rmVal = -1.0f;
        private float tmVal = -1.0f;
        private float bmVal = -1.0f;

        public MarginsPanel() throws IllegalArgumentException {
            DecimalFormat decimalFormat;
            this.units = 1000;
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = 2;
            gridBagConstraints.weightx = 1.0d;
            gridBagConstraints.weighty = 0.0d;
            gridBagConstraints.insets = ServiceDialog.compInsets;
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            String str = "label.millimetres";
            String country = Locale.getDefault().getCountry();
            if (country != null && (country.equals("") || country.equals(Locale.US.getCountry()) || country.equals(Locale.CANADA.getCountry()))) {
                str = "label.inches";
                this.units = 25400;
            }
            String msg = ServiceDialog.getMsg(str);
            if (this.units == 1000) {
                decimalFormat = new DecimalFormat("###.##");
                decimalFormat.setMaximumIntegerDigits(3);
            } else {
                decimalFormat = new DecimalFormat("##.##");
                decimalFormat.setMaximumIntegerDigits(2);
            }
            decimalFormat.setMinimumFractionDigits(1);
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumIntegerDigits(1);
            decimalFormat.setParseIntegerOnly(false);
            decimalFormat.setDecimalSeparatorAlwaysShown(true);
            NumberFormatter numberFormatter = new NumberFormatter(decimalFormat);
            numberFormatter.setMinimum(new Float(0.0f));
            numberFormatter.setMaximum(new Float(999.0f));
            numberFormatter.setAllowsInvalid(true);
            numberFormatter.setCommitsOnValidEdit(true);
            this.leftMargin = new JFormattedTextField((JFormattedTextField.AbstractFormatter) numberFormatter);
            this.leftMargin.addFocusListener(this);
            this.leftMargin.addActionListener(this);
            this.leftMargin.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.leftmargin"));
            this.rightMargin = new JFormattedTextField((JFormattedTextField.AbstractFormatter) numberFormatter);
            this.rightMargin.addFocusListener(this);
            this.rightMargin.addActionListener(this);
            this.rightMargin.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.rightmargin"));
            this.topMargin = new JFormattedTextField((JFormattedTextField.AbstractFormatter) numberFormatter);
            this.topMargin.addFocusListener(this);
            this.topMargin.addActionListener(this);
            this.topMargin.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.topmargin"));
            this.bottomMargin = new JFormattedTextField((JFormattedTextField.AbstractFormatter) numberFormatter);
            this.bottomMargin.addFocusListener(this);
            this.bottomMargin.addActionListener(this);
            this.bottomMargin.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.bottommargin"));
            gridBagConstraints.gridwidth = -1;
            this.lblLeft = new JLabel(ServiceDialog.getMsg("label.leftmargin") + " " + msg, 10);
            this.lblLeft.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.leftmargin"));
            this.lblLeft.setLabelFor(this.leftMargin);
            ServiceDialog.addToGB(this.lblLeft, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            this.lblRight = new JLabel(ServiceDialog.getMsg("label.rightmargin") + " " + msg, 10);
            this.lblRight.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.rightmargin"));
            this.lblRight.setLabelFor(this.rightMargin);
            ServiceDialog.addToGB(this.lblRight, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = -1;
            ServiceDialog.addToGB(this.leftMargin, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            ServiceDialog.addToGB(this.rightMargin, this, gridBagLayout, gridBagConstraints);
            ServiceDialog.addToGB(new JPanel(), this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = -1;
            this.lblTop = new JLabel(ServiceDialog.getMsg("label.topmargin") + " " + msg, 10);
            this.lblTop.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.topmargin"));
            this.lblTop.setLabelFor(this.topMargin);
            ServiceDialog.addToGB(this.lblTop, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            this.lblBottom = new JLabel(ServiceDialog.getMsg("label.bottommargin") + " " + msg, 10);
            this.lblBottom.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.bottommargin"));
            this.lblBottom.setLabelFor(this.bottomMargin);
            ServiceDialog.addToGB(this.lblBottom, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = -1;
            ServiceDialog.addToGB(this.topMargin, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            ServiceDialog.addToGB(this.bottomMargin, this, gridBagLayout, gridBagConstraints);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            updateMargins(actionEvent.getSource());
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            updateMargins(focusEvent.getSource());
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
        }

        public void updateMargins(Object obj) {
            if (!(obj instanceof JFormattedTextField)) {
                return;
            }
            JFormattedTextField jFormattedTextField = (JFormattedTextField) obj;
            Float f2 = (Float) jFormattedTextField.getValue();
            if (f2 == null) {
                return;
            }
            if (jFormattedTextField == this.leftMargin && f2.equals(this.lmObj)) {
                return;
            }
            if (jFormattedTextField == this.rightMargin && f2.equals(this.rmObj)) {
                return;
            }
            if (jFormattedTextField == this.topMargin && f2.equals(this.tmObj)) {
                return;
            }
            if (jFormattedTextField == this.bottomMargin && f2.equals(this.bmObj)) {
                return;
            }
            Float f3 = (Float) this.leftMargin.getValue();
            Float f4 = (Float) this.rightMargin.getValue();
            Float f5 = (Float) this.topMargin.getValue();
            Float f6 = (Float) this.bottomMargin.getValue();
            float fFloatValue = f3.floatValue();
            float fFloatValue2 = f4.floatValue();
            float fFloatValue3 = f5.floatValue();
            float fFloatValue4 = f6.floatValue();
            OrientationRequested orientationRequested = (OrientationRequested) ServiceDialog.this.asCurrent.get(OrientationRequested.class);
            if (orientationRequested == null) {
                orientationRequested = (OrientationRequested) ServiceDialog.this.psCurrent.getDefaultAttributeValue(OrientationRequested.class);
            }
            if (orientationRequested == OrientationRequested.REVERSE_PORTRAIT) {
                fFloatValue = fFloatValue2;
                fFloatValue2 = fFloatValue;
                fFloatValue3 = fFloatValue4;
                fFloatValue4 = fFloatValue3;
            } else if (orientationRequested == OrientationRequested.LANDSCAPE) {
                fFloatValue = fFloatValue3;
                fFloatValue3 = fFloatValue2;
                fFloatValue2 = fFloatValue4;
                fFloatValue4 = fFloatValue;
            } else if (orientationRequested == OrientationRequested.REVERSE_LANDSCAPE) {
                fFloatValue = fFloatValue4;
                fFloatValue4 = fFloatValue2;
                fFloatValue2 = fFloatValue3;
                fFloatValue3 = fFloatValue;
            }
            MediaPrintableArea mediaPrintableAreaValidateMargins = validateMargins(fFloatValue, fFloatValue2, fFloatValue3, fFloatValue4);
            if (mediaPrintableAreaValidateMargins != null) {
                ServiceDialog.this.asCurrent.add(mediaPrintableAreaValidateMargins);
                this.lmVal = fFloatValue;
                this.rmVal = fFloatValue2;
                this.tmVal = fFloatValue3;
                this.bmVal = fFloatValue4;
                this.lmObj = f3;
                this.rmObj = f4;
                this.tmObj = f5;
                this.bmObj = f6;
                return;
            }
            if (this.lmObj == null || this.rmObj == null || this.tmObj == null || this.rmObj == null) {
                return;
            }
            this.leftMargin.setValue(this.lmObj);
            this.rightMargin.setValue(this.rmObj);
            this.topMargin.setValue(this.tmObj);
            this.bottomMargin.setValue(this.bmObj);
        }

        private MediaPrintableArea validateMargins(float f2, float f3, float f4, float f5) {
            MediaPrintableArea mediaPrintableArea = null;
            MediaSize mediaSize = null;
            Media media = (Media) ServiceDialog.this.asCurrent.get(Media.class);
            if (media == null || !(media instanceof MediaSizeName)) {
                media = (Media) ServiceDialog.this.psCurrent.getDefaultAttributeValue(Media.class);
            }
            if (media != null && (media instanceof MediaSizeName)) {
                mediaSize = MediaSize.getMediaSizeForName((MediaSizeName) media);
            }
            if (mediaSize == null) {
                mediaSize = new MediaSize(8.5f, 11.0f, 25400);
            }
            if (media != null) {
                HashPrintRequestAttributeSet hashPrintRequestAttributeSet = new HashPrintRequestAttributeSet(ServiceDialog.this.asCurrent);
                hashPrintRequestAttributeSet.add(media);
                Object supportedAttributeValues = ServiceDialog.this.psCurrent.getSupportedAttributeValues(MediaPrintableArea.class, ServiceDialog.this.docFlavor, hashPrintRequestAttributeSet);
                if ((supportedAttributeValues instanceof MediaPrintableArea[]) && ((MediaPrintableArea[]) supportedAttributeValues).length > 0) {
                    mediaPrintableArea = ((MediaPrintableArea[]) supportedAttributeValues)[0];
                }
            }
            if (mediaPrintableArea == null) {
                mediaPrintableArea = new MediaPrintableArea(0.0f, 0.0f, mediaSize.getX(this.units), mediaSize.getY(this.units), this.units);
            }
            float x2 = (mediaSize.getX(this.units) - f2) - f3;
            float y2 = (mediaSize.getY(this.units) - f4) - f5;
            if (x2 <= 0.0f || y2 <= 0.0f || f2 < 0.0f || f4 < 0.0f || f2 < mediaPrintableArea.getX(this.units) || x2 > mediaPrintableArea.getWidth(this.units) || f4 < mediaPrintableArea.getY(this.units) || y2 > mediaPrintableArea.getHeight(this.units)) {
                return null;
            }
            return new MediaPrintableArea(f2, f4, x2, y2, this.units);
        }

        public void updateInfo() {
            float f2;
            float f3;
            if (ServiceDialog.this.isAWT) {
                this.leftMargin.setEnabled(false);
                this.rightMargin.setEnabled(false);
                this.topMargin.setEnabled(false);
                this.bottomMargin.setEnabled(false);
                this.lblLeft.setEnabled(false);
                this.lblRight.setEnabled(false);
                this.lblTop.setEnabled(false);
                this.lblBottom.setEnabled(false);
                return;
            }
            MediaPrintableArea mediaPrintableArea = (MediaPrintableArea) ServiceDialog.this.asCurrent.get(MediaPrintableArea.class);
            MediaPrintableArea mediaPrintableArea2 = null;
            MediaSize mediaSize = null;
            Media media = (Media) ServiceDialog.this.asCurrent.get(Media.class);
            if (media == null || !(media instanceof MediaSizeName)) {
                media = (Media) ServiceDialog.this.psCurrent.getDefaultAttributeValue(Media.class);
            }
            if (media != null && (media instanceof MediaSizeName)) {
                mediaSize = MediaSize.getMediaSizeForName((MediaSizeName) media);
            }
            if (mediaSize == null) {
                mediaSize = new MediaSize(8.5f, 11.0f, 25400);
            }
            if (media != null) {
                HashPrintRequestAttributeSet hashPrintRequestAttributeSet = new HashPrintRequestAttributeSet(ServiceDialog.this.asCurrent);
                hashPrintRequestAttributeSet.add(media);
                Object supportedAttributeValues = ServiceDialog.this.psCurrent.getSupportedAttributeValues(MediaPrintableArea.class, ServiceDialog.this.docFlavor, hashPrintRequestAttributeSet);
                if ((supportedAttributeValues instanceof MediaPrintableArea[]) && ((MediaPrintableArea[]) supportedAttributeValues).length > 0) {
                    mediaPrintableArea2 = ((MediaPrintableArea[]) supportedAttributeValues)[0];
                } else if (supportedAttributeValues instanceof MediaPrintableArea) {
                    mediaPrintableArea2 = (MediaPrintableArea) supportedAttributeValues;
                }
            }
            if (mediaPrintableArea2 == null) {
                mediaPrintableArea2 = new MediaPrintableArea(0.0f, 0.0f, mediaSize.getX(this.units), mediaSize.getY(this.units), this.units);
            }
            float x2 = mediaSize.getX(25400);
            float y2 = mediaSize.getY(25400);
            if (x2 > 5.0f) {
                f2 = 1.0f;
            } else {
                f2 = x2 / 5.0f;
            }
            if (y2 > 5.0f) {
                f3 = 1.0f;
            } else {
                f3 = y2 / 5.0f;
            }
            if (mediaPrintableArea == null) {
                mediaPrintableArea = new MediaPrintableArea(f2, f3, x2 - (2.0f * f2), y2 - (2.0f * f3), 25400);
                ServiceDialog.this.asCurrent.add(mediaPrintableArea);
            }
            float x3 = mediaPrintableArea.getX(this.units);
            float y3 = mediaPrintableArea.getY(this.units);
            float width = mediaPrintableArea.getWidth(this.units);
            float height = mediaPrintableArea.getHeight(this.units);
            float x4 = mediaPrintableArea2.getX(this.units);
            float y4 = mediaPrintableArea2.getY(this.units);
            float width2 = mediaPrintableArea2.getWidth(this.units);
            float height2 = mediaPrintableArea2.getHeight(this.units);
            boolean z2 = false;
            float x5 = mediaSize.getX(this.units);
            float y5 = mediaSize.getY(this.units);
            if (this.lmVal >= 0.0f) {
                z2 = true;
                if (this.lmVal + this.rmVal > x5) {
                    if (width > width2) {
                        width = width2;
                    }
                    x3 = (x5 - width) / 2.0f;
                } else {
                    x3 = this.lmVal >= x4 ? this.lmVal : x4;
                    width = (x5 - x3) - this.rmVal;
                }
                if (this.tmVal + this.bmVal > y5) {
                    if (height > height2) {
                        height = height2;
                    }
                    y3 = (y5 - height) / 2.0f;
                } else {
                    y3 = this.tmVal >= y4 ? this.tmVal : y4;
                    height = (y5 - y3) - this.bmVal;
                }
            }
            if (x3 < x4) {
                z2 = true;
                x3 = x4;
            }
            if (y3 < y4) {
                z2 = true;
                y3 = y4;
            }
            if (width > width2) {
                z2 = true;
                width = width2;
            }
            if (height > height2) {
                z2 = true;
                height = height2;
            }
            if (x3 + width > x4 + width2 || width <= 0.0f) {
                z2 = true;
                x3 = x4;
                width = width2;
            }
            if (y3 + height > y4 + height2 || height <= 0.0f) {
                z2 = true;
                y3 = y4;
                height = height2;
            }
            if (z2) {
                ServiceDialog.this.asCurrent.add(new MediaPrintableArea(x3, y3, width, height, this.units));
            }
            this.lmVal = x3;
            this.tmVal = y3;
            this.rmVal = (mediaSize.getX(this.units) - x3) - width;
            this.bmVal = (mediaSize.getY(this.units) - y3) - height;
            this.lmObj = new Float(this.lmVal);
            this.rmObj = new Float(this.rmVal);
            this.tmObj = new Float(this.tmVal);
            this.bmObj = new Float(this.bmVal);
            OrientationRequested orientationRequested = (OrientationRequested) ServiceDialog.this.asCurrent.get(OrientationRequested.class);
            if (orientationRequested == null) {
                orientationRequested = (OrientationRequested) ServiceDialog.this.psCurrent.getDefaultAttributeValue(OrientationRequested.class);
            }
            if (orientationRequested == OrientationRequested.REVERSE_PORTRAIT) {
                Float f4 = this.lmObj;
                this.lmObj = this.rmObj;
                this.rmObj = f4;
                Float f5 = this.tmObj;
                this.tmObj = this.bmObj;
                this.bmObj = f5;
            } else if (orientationRequested == OrientationRequested.LANDSCAPE) {
                Float f6 = this.lmObj;
                this.lmObj = this.bmObj;
                this.bmObj = this.rmObj;
                this.rmObj = this.tmObj;
                this.tmObj = f6;
            } else if (orientationRequested == OrientationRequested.REVERSE_LANDSCAPE) {
                Float f7 = this.lmObj;
                this.lmObj = this.tmObj;
                this.tmObj = this.rmObj;
                this.rmObj = this.bmObj;
                this.bmObj = f7;
            }
            this.leftMargin.setValue(this.lmObj);
            this.rightMargin.setValue(this.rmObj);
            this.topMargin.setValue(this.tmObj);
            this.bottomMargin.setValue(this.bmObj);
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$MediaPanel.class */
    private class MediaPanel extends JPanel implements ItemListener {
        private JLabel lblSize;
        private JLabel lblSource;
        private JComboBox cbSize;
        private JComboBox cbSource;
        private final String strTitle = ServiceDialog.getMsg("border.media");
        private Vector sizes = new Vector();
        private Vector sources = new Vector();
        private MarginsPanel pnlMargins = null;

        public MediaPanel() throws IllegalArgumentException {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            this.cbSize = new JComboBox();
            this.cbSource = new JComboBox();
            gridBagConstraints.fill = 1;
            gridBagConstraints.insets = ServiceDialog.compInsets;
            gridBagConstraints.weighty = 1.0d;
            gridBagConstraints.weightx = 0.0d;
            this.lblSize = new JLabel(ServiceDialog.getMsg("label.size"), 11);
            this.lblSize.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.size"));
            this.lblSize.setLabelFor(this.cbSize);
            ServiceDialog.addToGB(this.lblSize, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.weightx = 1.0d;
            gridBagConstraints.gridwidth = 0;
            ServiceDialog.addToGB(this.cbSize, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.weightx = 0.0d;
            gridBagConstraints.gridwidth = 1;
            this.lblSource = new JLabel(ServiceDialog.getMsg("label.source"), 11);
            this.lblSource.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.source"));
            this.lblSource.setLabelFor(this.cbSource);
            ServiceDialog.addToGB(this.lblSource, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            ServiceDialog.addToGB(this.cbSource, this, gridBagLayout, gridBagConstraints);
        }

        private String getMediaName(String str) {
            try {
                return ServiceDialog.messageRB.getString(str.replace(' ', '-').replace('#', 'n'));
            } catch (MissingResourceException e2) {
                return str;
            }
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
            Object source = itemEvent.getSource();
            if (itemEvent.getStateChange() == 1) {
                if (source == this.cbSize) {
                    int selectedIndex = this.cbSize.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < this.sizes.size()) {
                        if (this.cbSource.getItemCount() > 1 && this.cbSource.getSelectedIndex() >= 1) {
                            ServiceDialog.this.asCurrent.add(new SunAlternateMedia((MediaTray) this.sources.get(this.cbSource.getSelectedIndex() - 1)));
                        }
                        ServiceDialog.this.asCurrent.add((MediaSizeName) this.sizes.get(selectedIndex));
                    }
                } else if (source == this.cbSource) {
                    int selectedIndex2 = this.cbSource.getSelectedIndex();
                    if (selectedIndex2 >= 1 && selectedIndex2 < this.sources.size() + 1) {
                        ServiceDialog.this.asCurrent.remove(SunAlternateMedia.class);
                        MediaTray mediaTray = (MediaTray) this.sources.get(selectedIndex2 - 1);
                        Media media = (Media) ServiceDialog.this.asCurrent.get(Media.class);
                        if (media == null || (media instanceof MediaTray)) {
                            ServiceDialog.this.asCurrent.add(mediaTray);
                        } else if (media instanceof MediaSizeName) {
                            MediaSizeName mediaSizeName = (MediaSizeName) media;
                            Media media2 = (Media) ServiceDialog.this.psCurrent.getDefaultAttributeValue(Media.class);
                            if (!(media2 instanceof MediaSizeName) || !media2.equals(mediaSizeName)) {
                                ServiceDialog.this.asCurrent.add(new SunAlternateMedia(mediaTray));
                            } else {
                                ServiceDialog.this.asCurrent.add(mediaTray);
                            }
                        }
                    } else if (selectedIndex2 == 0) {
                        ServiceDialog.this.asCurrent.remove(SunAlternateMedia.class);
                        if (this.cbSize.getItemCount() > 0) {
                            ServiceDialog.this.asCurrent.add((MediaSizeName) this.sizes.get(this.cbSize.getSelectedIndex()));
                        }
                    }
                }
                if (this.pnlMargins != null) {
                    this.pnlMargins.updateInfo();
                }
            }
        }

        public void addMediaListener(MarginsPanel marginsPanel) {
            this.pnlMargins = marginsPanel;
        }

        public void updateInfo() {
            boolean z2 = false;
            this.cbSize.removeItemListener(this);
            this.cbSize.removeAllItems();
            this.cbSource.removeItemListener(this);
            this.cbSource.removeAllItems();
            this.cbSource.addItem(getMediaName("auto-select"));
            this.sizes.clear();
            this.sources.clear();
            if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(Media.class)) {
                z2 = true;
                Object supportedAttributeValues = ServiceDialog.this.psCurrent.getSupportedAttributeValues(Media.class, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
                if (supportedAttributeValues instanceof Media[]) {
                    for (Media media : (Media[]) supportedAttributeValues) {
                        if (media instanceof MediaSizeName) {
                            this.sizes.add(media);
                            this.cbSize.addItem(getMediaName(media.toString()));
                        } else if (media instanceof MediaTray) {
                            this.sources.add(media);
                            this.cbSource.addItem(getMediaName(media.toString()));
                        }
                    }
                }
            }
            boolean z3 = z2 && this.sizes.size() > 0;
            this.lblSize.setEnabled(z3);
            this.cbSize.setEnabled(z3);
            if (ServiceDialog.this.isAWT) {
                this.cbSource.setEnabled(false);
                this.lblSource.setEnabled(false);
            } else {
                this.cbSource.setEnabled(z2);
            }
            if (z2) {
                Media media2 = (Media) ServiceDialog.this.asCurrent.get(Media.class);
                Media media3 = (Media) ServiceDialog.this.psCurrent.getDefaultAttributeValue(Media.class);
                if (media3 instanceof MediaSizeName) {
                    this.cbSize.setSelectedIndex(this.sizes.size() > 0 ? this.sizes.indexOf(media3) : -1);
                }
                if (media2 == null || !ServiceDialog.this.psCurrent.isAttributeValueSupported(media2, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent)) {
                    media2 = media3;
                    if (media2 == null && this.sizes.size() > 0) {
                        media2 = (Media) this.sizes.get(0);
                    }
                    if (media2 != null) {
                        ServiceDialog.this.asCurrent.add(media2);
                    }
                }
                if (media2 != null) {
                    if (media2 instanceof MediaSizeName) {
                        this.cbSize.setSelectedIndex(this.sizes.indexOf((MediaSizeName) media2));
                    } else if (media2 instanceof MediaTray) {
                        this.cbSource.setSelectedIndex(this.sources.indexOf((MediaTray) media2) + 1);
                    }
                } else {
                    this.cbSize.setSelectedIndex(this.sizes.size() > 0 ? 0 : -1);
                    this.cbSource.setSelectedIndex(0);
                }
                SunAlternateMedia sunAlternateMedia = (SunAlternateMedia) ServiceDialog.this.asCurrent.get(SunAlternateMedia.class);
                if (sunAlternateMedia != null) {
                    Media media4 = sunAlternateMedia.getMedia();
                    if (media4 instanceof MediaTray) {
                        this.cbSource.setSelectedIndex(this.sources.indexOf((MediaTray) media4) + 1);
                    }
                }
                int selectedIndex = this.cbSize.getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < this.sizes.size()) {
                    ServiceDialog.this.asCurrent.add((MediaSizeName) this.sizes.get(selectedIndex));
                }
                int selectedIndex2 = this.cbSource.getSelectedIndex();
                if (selectedIndex2 >= 1 && selectedIndex2 < this.sources.size() + 1) {
                    MediaTray mediaTray = (MediaTray) this.sources.get(selectedIndex2 - 1);
                    if (media2 instanceof MediaTray) {
                        ServiceDialog.this.asCurrent.add(mediaTray);
                    } else {
                        ServiceDialog.this.asCurrent.add(new SunAlternateMedia(mediaTray));
                    }
                }
            }
            this.cbSize.addItemListener(this);
            this.cbSource.addItemListener(this);
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$OrientationPanel.class */
    private class OrientationPanel extends JPanel implements ActionListener {
        private IconRadioButton rbPortrait;
        private IconRadioButton rbLandscape;
        private IconRadioButton rbRevPortrait;
        private IconRadioButton rbRevLandscape;
        private final String strTitle = ServiceDialog.getMsg("border.orientation");
        private MarginsPanel pnlMargins = null;

        public OrientationPanel() {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            gridBagConstraints.fill = 1;
            gridBagConstraints.insets = ServiceDialog.compInsets;
            gridBagConstraints.weighty = 1.0d;
            gridBagConstraints.gridwidth = 0;
            ButtonGroup buttonGroup = new ButtonGroup();
            this.rbPortrait = ServiceDialog.this.new IconRadioButton("radiobutton.portrait", "orientPortrait.png", true, buttonGroup, this);
            this.rbPortrait.addActionListener(this);
            ServiceDialog.addToGB(this.rbPortrait, this, gridBagLayout, gridBagConstraints);
            this.rbLandscape = ServiceDialog.this.new IconRadioButton("radiobutton.landscape", "orientLandscape.png", false, buttonGroup, this);
            this.rbLandscape.addActionListener(this);
            ServiceDialog.addToGB(this.rbLandscape, this, gridBagLayout, gridBagConstraints);
            this.rbRevPortrait = ServiceDialog.this.new IconRadioButton("radiobutton.revportrait", "orientRevPortrait.png", false, buttonGroup, this);
            this.rbRevPortrait.addActionListener(this);
            ServiceDialog.addToGB(this.rbRevPortrait, this, gridBagLayout, gridBagConstraints);
            this.rbRevLandscape = ServiceDialog.this.new IconRadioButton("radiobutton.revlandscape", "orientRevLandscape.png", false, buttonGroup, this);
            this.rbRevLandscape.addActionListener(this);
            ServiceDialog.addToGB(this.rbRevLandscape, this, gridBagLayout, gridBagConstraints);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (this.rbPortrait.isSameAs(source)) {
                ServiceDialog.this.asCurrent.add(OrientationRequested.PORTRAIT);
            } else if (this.rbLandscape.isSameAs(source)) {
                ServiceDialog.this.asCurrent.add(OrientationRequested.LANDSCAPE);
            } else if (this.rbRevPortrait.isSameAs(source)) {
                ServiceDialog.this.asCurrent.add(OrientationRequested.REVERSE_PORTRAIT);
            } else if (this.rbRevLandscape.isSameAs(source)) {
                ServiceDialog.this.asCurrent.add(OrientationRequested.REVERSE_LANDSCAPE);
            }
            if (this.pnlMargins != null) {
                this.pnlMargins.updateInfo();
            }
        }

        void addOrientationListener(MarginsPanel marginsPanel) {
            this.pnlMargins = marginsPanel;
        }

        public void updateInfo() {
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            if (!ServiceDialog.this.isAWT) {
                if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(OrientationRequested.class)) {
                    Object supportedAttributeValues = ServiceDialog.this.psCurrent.getSupportedAttributeValues(OrientationRequested.class, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
                    if (supportedAttributeValues instanceof OrientationRequested[]) {
                        for (OrientationRequested orientationRequested : (OrientationRequested[]) supportedAttributeValues) {
                            if (orientationRequested == OrientationRequested.PORTRAIT) {
                                z2 = true;
                            } else if (orientationRequested == OrientationRequested.LANDSCAPE) {
                                z3 = true;
                            } else if (orientationRequested == OrientationRequested.REVERSE_PORTRAIT) {
                                z4 = true;
                            } else if (orientationRequested == OrientationRequested.REVERSE_LANDSCAPE) {
                                z5 = true;
                            }
                        }
                    }
                }
            } else {
                z2 = true;
                z3 = true;
            }
            this.rbPortrait.setEnabled(z2);
            this.rbLandscape.setEnabled(z3);
            this.rbRevPortrait.setEnabled(z4);
            this.rbRevLandscape.setEnabled(z5);
            OrientationRequested orientationRequested2 = (OrientationRequested) ServiceDialog.this.asCurrent.get(OrientationRequested.class);
            if (orientationRequested2 == null || !ServiceDialog.this.psCurrent.isAttributeValueSupported(orientationRequested2, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent)) {
                orientationRequested2 = (OrientationRequested) ServiceDialog.this.psCurrent.getDefaultAttributeValue(OrientationRequested.class);
                if (orientationRequested2 != null && !ServiceDialog.this.psCurrent.isAttributeValueSupported(orientationRequested2, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent)) {
                    orientationRequested2 = null;
                    Object supportedAttributeValues2 = ServiceDialog.this.psCurrent.getSupportedAttributeValues(OrientationRequested.class, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
                    if (supportedAttributeValues2 instanceof OrientationRequested[]) {
                        OrientationRequested[] orientationRequestedArr = (OrientationRequested[]) supportedAttributeValues2;
                        if (orientationRequestedArr.length > 1) {
                            orientationRequested2 = orientationRequestedArr[0];
                        }
                    }
                }
                if (orientationRequested2 == null) {
                    orientationRequested2 = OrientationRequested.PORTRAIT;
                }
                ServiceDialog.this.asCurrent.add(orientationRequested2);
            }
            if (orientationRequested2 == OrientationRequested.PORTRAIT) {
                this.rbPortrait.setSelected(true);
                return;
            }
            if (orientationRequested2 == OrientationRequested.LANDSCAPE) {
                this.rbLandscape.setSelected(true);
            } else if (orientationRequested2 == OrientationRequested.REVERSE_PORTRAIT) {
                this.rbRevPortrait.setSelected(true);
            } else {
                this.rbRevLandscape.setSelected(true);
            }
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$AppearancePanel.class */
    private class AppearancePanel extends JPanel {
        private ChromaticityPanel pnlChromaticity;
        private QualityPanel pnlQuality;
        private JobAttributesPanel pnlJobAttributes;
        private SidesPanel pnlSides;

        public AppearancePanel() {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            gridBagConstraints.fill = 1;
            gridBagConstraints.insets = ServiceDialog.panelInsets;
            gridBagConstraints.weightx = 1.0d;
            gridBagConstraints.weighty = 1.0d;
            gridBagConstraints.gridwidth = -1;
            this.pnlChromaticity = ServiceDialog.this.new ChromaticityPanel();
            ServiceDialog.addToGB(this.pnlChromaticity, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            this.pnlQuality = ServiceDialog.this.new QualityPanel();
            ServiceDialog.addToGB(this.pnlQuality, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 1;
            this.pnlSides = ServiceDialog.this.new SidesPanel();
            ServiceDialog.addToGB(this.pnlSides, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            this.pnlJobAttributes = ServiceDialog.this.new JobAttributesPanel();
            ServiceDialog.addToGB(this.pnlJobAttributes, this, gridBagLayout, gridBagConstraints);
        }

        public void updateInfo() {
            this.pnlChromaticity.updateInfo();
            this.pnlQuality.updateInfo();
            this.pnlSides.updateInfo();
            this.pnlJobAttributes.updateInfo();
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$ChromaticityPanel.class */
    private class ChromaticityPanel extends JPanel implements ActionListener {
        private final String strTitle = ServiceDialog.getMsg("border.chromaticity");
        private JRadioButton rbMonochrome;
        private JRadioButton rbColor;

        public ChromaticityPanel() {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            gridBagConstraints.fill = 1;
            gridBagConstraints.gridwidth = 0;
            gridBagConstraints.weighty = 1.0d;
            ButtonGroup buttonGroup = new ButtonGroup();
            this.rbMonochrome = ServiceDialog.createRadioButton("radiobutton.monochrome", this);
            this.rbMonochrome.setSelected(true);
            buttonGroup.add(this.rbMonochrome);
            ServiceDialog.addToGB(this.rbMonochrome, this, gridBagLayout, gridBagConstraints);
            this.rbColor = ServiceDialog.createRadioButton("radiobutton.color", this);
            buttonGroup.add(this.rbColor);
            ServiceDialog.addToGB(this.rbColor, this, gridBagLayout, gridBagConstraints);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (source == this.rbMonochrome) {
                ServiceDialog.this.asCurrent.add(Chromaticity.MONOCHROME);
            } else if (source == this.rbColor) {
                ServiceDialog.this.asCurrent.add(Chromaticity.COLOR);
            }
        }

        public void updateInfo() {
            boolean z2 = false;
            boolean z3 = false;
            if (!ServiceDialog.this.isAWT) {
                if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(Chromaticity.class)) {
                    Object supportedAttributeValues = ServiceDialog.this.psCurrent.getSupportedAttributeValues(Chromaticity.class, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
                    if (supportedAttributeValues instanceof Chromaticity[]) {
                        for (Chromaticity chromaticity : (Chromaticity[]) supportedAttributeValues) {
                            if (chromaticity == Chromaticity.MONOCHROME) {
                                z2 = true;
                            } else if (chromaticity == Chromaticity.COLOR) {
                                z3 = true;
                            }
                        }
                    }
                }
            } else {
                z2 = true;
                z3 = true;
            }
            this.rbMonochrome.setEnabled(z2);
            this.rbColor.setEnabled(z3);
            Chromaticity chromaticity2 = (Chromaticity) ServiceDialog.this.asCurrent.get(Chromaticity.class);
            if (chromaticity2 == null) {
                chromaticity2 = (Chromaticity) ServiceDialog.this.psCurrent.getDefaultAttributeValue(Chromaticity.class);
                if (chromaticity2 == null) {
                    chromaticity2 = Chromaticity.MONOCHROME;
                }
            }
            if (chromaticity2 == Chromaticity.MONOCHROME) {
                this.rbMonochrome.setSelected(true);
            } else {
                this.rbColor.setSelected(true);
            }
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$QualityPanel.class */
    private class QualityPanel extends JPanel implements ActionListener {
        private final String strTitle = ServiceDialog.getMsg("border.quality");
        private JRadioButton rbDraft;
        private JRadioButton rbNormal;
        private JRadioButton rbHigh;

        public QualityPanel() {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            gridBagConstraints.fill = 1;
            gridBagConstraints.gridwidth = 0;
            gridBagConstraints.weighty = 1.0d;
            ButtonGroup buttonGroup = new ButtonGroup();
            this.rbDraft = ServiceDialog.createRadioButton("radiobutton.draftq", this);
            buttonGroup.add(this.rbDraft);
            ServiceDialog.addToGB(this.rbDraft, this, gridBagLayout, gridBagConstraints);
            this.rbNormal = ServiceDialog.createRadioButton("radiobutton.normalq", this);
            this.rbNormal.setSelected(true);
            buttonGroup.add(this.rbNormal);
            ServiceDialog.addToGB(this.rbNormal, this, gridBagLayout, gridBagConstraints);
            this.rbHigh = ServiceDialog.createRadioButton("radiobutton.highq", this);
            buttonGroup.add(this.rbHigh);
            ServiceDialog.addToGB(this.rbHigh, this, gridBagLayout, gridBagConstraints);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (source == this.rbDraft) {
                ServiceDialog.this.asCurrent.add(PrintQuality.DRAFT);
            } else if (source == this.rbNormal) {
                ServiceDialog.this.asCurrent.add(PrintQuality.NORMAL);
            } else if (source == this.rbHigh) {
                ServiceDialog.this.asCurrent.add(PrintQuality.HIGH);
            }
        }

        public void updateInfo() {
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            if (!ServiceDialog.this.isAWT) {
                if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(PrintQuality.class)) {
                    Object supportedAttributeValues = ServiceDialog.this.psCurrent.getSupportedAttributeValues(PrintQuality.class, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
                    if (supportedAttributeValues instanceof PrintQuality[]) {
                        for (PrintQuality printQuality : (PrintQuality[]) supportedAttributeValues) {
                            if (printQuality == PrintQuality.DRAFT) {
                                z2 = true;
                            } else if (printQuality == PrintQuality.NORMAL) {
                                z3 = true;
                            } else if (printQuality == PrintQuality.HIGH) {
                                z4 = true;
                            }
                        }
                    }
                }
            } else {
                z2 = true;
                z3 = true;
                z4 = true;
            }
            this.rbDraft.setEnabled(z2);
            this.rbNormal.setEnabled(z3);
            this.rbHigh.setEnabled(z4);
            PrintQuality printQuality2 = (PrintQuality) ServiceDialog.this.asCurrent.get(PrintQuality.class);
            if (printQuality2 == null) {
                printQuality2 = (PrintQuality) ServiceDialog.this.psCurrent.getDefaultAttributeValue(PrintQuality.class);
                if (printQuality2 == null) {
                    printQuality2 = PrintQuality.NORMAL;
                }
            }
            if (printQuality2 == PrintQuality.DRAFT) {
                this.rbDraft.setSelected(true);
            } else if (printQuality2 == PrintQuality.NORMAL) {
                this.rbNormal.setSelected(true);
            } else {
                this.rbHigh.setSelected(true);
            }
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$SidesPanel.class */
    private class SidesPanel extends JPanel implements ActionListener {
        private final String strTitle = ServiceDialog.getMsg("border.sides");
        private IconRadioButton rbOneSide;
        private IconRadioButton rbTumble;
        private IconRadioButton rbDuplex;

        public SidesPanel() {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            gridBagConstraints.fill = 1;
            gridBagConstraints.insets = ServiceDialog.compInsets;
            gridBagConstraints.weighty = 1.0d;
            gridBagConstraints.gridwidth = 0;
            ButtonGroup buttonGroup = new ButtonGroup();
            this.rbOneSide = ServiceDialog.this.new IconRadioButton("radiobutton.oneside", "oneside.png", true, buttonGroup, this);
            this.rbOneSide.addActionListener(this);
            ServiceDialog.addToGB(this.rbOneSide, this, gridBagLayout, gridBagConstraints);
            this.rbTumble = ServiceDialog.this.new IconRadioButton("radiobutton.tumble", "tumble.png", false, buttonGroup, this);
            this.rbTumble.addActionListener(this);
            ServiceDialog.addToGB(this.rbTumble, this, gridBagLayout, gridBagConstraints);
            this.rbDuplex = ServiceDialog.this.new IconRadioButton("radiobutton.duplex", "duplex.png", false, buttonGroup, this);
            this.rbDuplex.addActionListener(this);
            gridBagConstraints.gridwidth = 0;
            ServiceDialog.addToGB(this.rbDuplex, this, gridBagLayout, gridBagConstraints);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            if (this.rbOneSide.isSameAs(source)) {
                ServiceDialog.this.asCurrent.add(Sides.ONE_SIDED);
            } else if (this.rbTumble.isSameAs(source)) {
                ServiceDialog.this.asCurrent.add(Sides.TUMBLE);
            } else if (this.rbDuplex.isSameAs(source)) {
                ServiceDialog.this.asCurrent.add(Sides.DUPLEX);
            }
        }

        public void updateInfo() {
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(Sides.class)) {
                Object supportedAttributeValues = ServiceDialog.this.psCurrent.getSupportedAttributeValues(Sides.class, ServiceDialog.this.docFlavor, ServiceDialog.this.asCurrent);
                if (supportedAttributeValues instanceof Sides[]) {
                    for (Sides sides : (Sides[]) supportedAttributeValues) {
                        if (sides == Sides.ONE_SIDED) {
                            z2 = true;
                        } else if (sides == Sides.TUMBLE) {
                            z3 = true;
                        } else if (sides == Sides.DUPLEX) {
                            z4 = true;
                        }
                    }
                }
            }
            this.rbOneSide.setEnabled(z2);
            this.rbTumble.setEnabled(z3);
            this.rbDuplex.setEnabled(z4);
            Sides sides2 = (Sides) ServiceDialog.this.asCurrent.get(Sides.class);
            if (sides2 == null) {
                sides2 = (Sides) ServiceDialog.this.psCurrent.getDefaultAttributeValue(Sides.class);
                if (sides2 == null) {
                    sides2 = Sides.ONE_SIDED;
                }
            }
            if (sides2 == Sides.ONE_SIDED) {
                this.rbOneSide.setSelected(true);
            } else if (sides2 == Sides.TUMBLE) {
                this.rbTumble.setSelected(true);
            } else {
                this.rbDuplex.setSelected(true);
            }
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$JobAttributesPanel.class */
    private class JobAttributesPanel extends JPanel implements ActionListener, ChangeListener, FocusListener {
        private final String strTitle = ServiceDialog.getMsg("border.jobattributes");
        private JLabel lblPriority;
        private JLabel lblJobName;
        private JLabel lblUserName;
        private JSpinner spinPriority;
        private SpinnerNumberModel snModel;
        private JCheckBox cbJobSheets;
        private JTextField tfJobName;
        private JTextField tfUserName;

        public JobAttributesPanel() throws IllegalArgumentException {
            GridBagLayout gridBagLayout = new GridBagLayout();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            setLayout(gridBagLayout);
            setBorder(BorderFactory.createTitledBorder(this.strTitle));
            gridBagConstraints.fill = 0;
            gridBagConstraints.insets = ServiceDialog.compInsets;
            gridBagConstraints.weighty = 1.0d;
            this.cbJobSheets = ServiceDialog.createCheckBox("checkbox.jobsheets", this);
            gridBagConstraints.anchor = 21;
            ServiceDialog.addToGB(this.cbJobSheets, this, gridBagLayout, gridBagConstraints);
            JPanel jPanel = new JPanel();
            this.lblPriority = new JLabel(ServiceDialog.getMsg("label.priority"), 11);
            this.lblPriority.setDisplayedMnemonic(ServiceDialog.getMnemonic("label.priority"));
            jPanel.add(this.lblPriority);
            this.snModel = new SpinnerNumberModel(1, 1, 100, 1);
            this.spinPriority = new JSpinner(this.snModel);
            this.lblPriority.setLabelFor(this.spinPriority);
            ((JSpinner.NumberEditor) this.spinPriority.getEditor()).getTextField().setColumns(3);
            this.spinPriority.addChangeListener(this);
            jPanel.add(this.spinPriority);
            gridBagConstraints.anchor = 22;
            gridBagConstraints.gridwidth = 0;
            jPanel.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.priority"));
            ServiceDialog.addToGB(jPanel, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.fill = 2;
            gridBagConstraints.anchor = 10;
            gridBagConstraints.weightx = 0.0d;
            gridBagConstraints.gridwidth = 1;
            char mnemonic = ServiceDialog.getMnemonic("label.jobname");
            this.lblJobName = new JLabel(ServiceDialog.getMsg("label.jobname"), 11);
            this.lblJobName.setDisplayedMnemonic(mnemonic);
            ServiceDialog.addToGB(this.lblJobName, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.weightx = 1.0d;
            gridBagConstraints.gridwidth = 0;
            this.tfJobName = new JTextField();
            this.lblJobName.setLabelFor(this.tfJobName);
            this.tfJobName.addFocusListener(this);
            this.tfJobName.setFocusAccelerator(mnemonic);
            this.tfJobName.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.jobname"));
            ServiceDialog.addToGB(this.tfJobName, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.weightx = 0.0d;
            gridBagConstraints.gridwidth = 1;
            char mnemonic2 = ServiceDialog.getMnemonic("label.username");
            this.lblUserName = new JLabel(ServiceDialog.getMsg("label.username"), 11);
            this.lblUserName.setDisplayedMnemonic(mnemonic2);
            ServiceDialog.addToGB(this.lblUserName, this, gridBagLayout, gridBagConstraints);
            gridBagConstraints.gridwidth = 0;
            this.tfUserName = new JTextField();
            this.lblUserName.setLabelFor(this.tfUserName);
            this.tfUserName.addFocusListener(this);
            this.tfUserName.setFocusAccelerator(mnemonic2);
            this.tfUserName.getAccessibleContext().setAccessibleName(ServiceDialog.getMsg("label.username"));
            ServiceDialog.addToGB(this.tfUserName, this, gridBagLayout, gridBagConstraints);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.cbJobSheets.isSelected()) {
                ServiceDialog.this.asCurrent.add(JobSheets.STANDARD);
            } else {
                ServiceDialog.this.asCurrent.add(JobSheets.NONE);
            }
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            ServiceDialog.this.asCurrent.add(new JobPriority(this.snModel.getNumber().intValue()));
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            Object source = focusEvent.getSource();
            if (source == this.tfJobName) {
                ServiceDialog.this.asCurrent.add(new JobName(this.tfJobName.getText(), Locale.getDefault()));
            } else if (source == this.tfUserName) {
                ServiceDialog.this.asCurrent.add(new RequestingUserName(this.tfUserName.getText(), Locale.getDefault()));
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
        }

        public void updateInfo() {
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(JobSheets.class)) {
                z2 = true;
            }
            JobSheets jobSheets = (JobSheets) ServiceDialog.this.asCurrent.get(JobSheets.class);
            if (jobSheets == null) {
                jobSheets = (JobSheets) ServiceDialog.this.psCurrent.getDefaultAttributeValue(JobSheets.class);
                if (jobSheets == null) {
                    jobSheets = JobSheets.NONE;
                }
            }
            this.cbJobSheets.setSelected(jobSheets != JobSheets.NONE);
            this.cbJobSheets.setEnabled(z2);
            if (!ServiceDialog.this.isAWT && ServiceDialog.this.psCurrent.isAttributeCategorySupported(JobPriority.class)) {
                z3 = true;
            }
            JobPriority jobPriority = (JobPriority) ServiceDialog.this.asCurrent.get(JobPriority.class);
            if (jobPriority == null) {
                jobPriority = (JobPriority) ServiceDialog.this.psCurrent.getDefaultAttributeValue(JobPriority.class);
                if (jobPriority == null) {
                    jobPriority = new JobPriority(1);
                }
            }
            int value = jobPriority.getValue();
            if (value < 1 || value > 100) {
                value = 1;
            }
            this.snModel.setValue(new Integer(value));
            this.lblPriority.setEnabled(z3);
            this.spinPriority.setEnabled(z3);
            if (ServiceDialog.this.psCurrent.isAttributeCategorySupported(JobName.class)) {
                z4 = true;
            }
            JobName jobName = (JobName) ServiceDialog.this.asCurrent.get(JobName.class);
            if (jobName == null) {
                jobName = (JobName) ServiceDialog.this.psCurrent.getDefaultAttributeValue(JobName.class);
                if (jobName == null) {
                    jobName = new JobName("", Locale.getDefault());
                }
            }
            this.tfJobName.setText(jobName.getValue());
            this.tfJobName.setEnabled(z4);
            this.lblJobName.setEnabled(z4);
            if (!ServiceDialog.this.isAWT && ServiceDialog.this.psCurrent.isAttributeCategorySupported(RequestingUserName.class)) {
                z5 = true;
            }
            RequestingUserName requestingUserName = (RequestingUserName) ServiceDialog.this.asCurrent.get(RequestingUserName.class);
            if (requestingUserName == null) {
                requestingUserName = (RequestingUserName) ServiceDialog.this.psCurrent.getDefaultAttributeValue(RequestingUserName.class);
                if (requestingUserName == null) {
                    requestingUserName = new RequestingUserName("", Locale.getDefault());
                }
            }
            this.tfUserName.setText(requestingUserName.getValue());
            this.tfUserName.setEnabled(z5);
            this.lblUserName.setEnabled(z5);
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$IconRadioButton.class */
    private class IconRadioButton extends JPanel {
        private JRadioButton rb;
        private JLabel lbl;

        public IconRadioButton(String str, String str2, boolean z2, ButtonGroup buttonGroup, ActionListener actionListener) {
            super(new FlowLayout(3));
            final URL imageResource = ServiceDialog.getImageResource(str2);
            this.lbl = new JLabel((Icon) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.print.ServiceDialog.IconRadioButton.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    return new ImageIcon(imageResource);
                }
            }));
            add(this.lbl);
            this.rb = ServiceDialog.createRadioButton(str, actionListener);
            this.rb.setSelected(z2);
            ServiceDialog.addToBG(this.rb, this, buttonGroup);
        }

        public void addActionListener(ActionListener actionListener) {
            this.rb.addActionListener(actionListener);
        }

        public boolean isSameAs(Object obj) {
            return this.rb == obj;
        }

        @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
        public void setEnabled(boolean z2) {
            this.rb.setEnabled(z2);
            this.lbl.setEnabled(z2);
        }

        public boolean isSelected() {
            return this.rb.isSelected();
        }

        public void setSelected(boolean z2) {
            this.rb.setSelected(z2);
        }
    }

    /* loaded from: rt.jar:sun/print/ServiceDialog$ValidatingFileChooser.class */
    private class ValidatingFileChooser extends JFileChooser {
        private ValidatingFileChooser() {
        }

        @Override // javax.swing.JFileChooser
        public void approveSelection() throws HeadlessException {
            boolean zExists;
            File selectedFile = getSelectedFile();
            try {
                zExists = selectedFile.exists();
            } catch (SecurityException e2) {
                zExists = false;
            }
            if (zExists && JOptionPane.showConfirmDialog(this, ServiceDialog.getMsg("dialog.overwrite"), ServiceDialog.getMsg("dialog.owtitle"), 0) != 0) {
                return;
            }
            try {
                if (selectedFile.createNewFile()) {
                    selectedFile.delete();
                }
            } catch (IOException e3) {
                JOptionPane.showMessageDialog(this, ServiceDialog.getMsg("dialog.writeerror") + " " + ((Object) selectedFile), ServiceDialog.getMsg("dialog.owtitle"), 2);
                return;
            } catch (SecurityException e4) {
            }
            File parentFile = selectedFile.getParentFile();
            if ((selectedFile.exists() && (!selectedFile.isFile() || !selectedFile.canWrite())) || (parentFile != null && (!parentFile.exists() || (parentFile.exists() && !parentFile.canWrite())))) {
                JOptionPane.showMessageDialog(this, ServiceDialog.getMsg("dialog.writeerror") + " " + ((Object) selectedFile), ServiceDialog.getMsg("dialog.owtitle"), 2);
            } else {
                super.approveSelection();
            }
        }
    }
}
