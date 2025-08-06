package az;

import bH.W;
import bH.aa;
import com.efiAnalytics.ui.aN;
import com.efiAnalytics.ui.bV;
import az.ActivationData;
import az.ActivationMessages;
import az.ActivationParseException;
import az.ActivationRequest;
import az.ActivationResult;
import az.ActivationValidator;
import az.AppInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.icepdf.ri.common.FileExtensionUtils;

/**
 * Dialog that guides the user through the five step offline activation process.
 */
public class OfflineActivationDialog extends JDialog implements ClipboardOwner {

    private final ActivationRequest activationRequest;
    private final JTextPane requestText;
    private final JTextPane activationCodeText;
    private ActivationData activationData;
    private final aa i18n;
    private final AppInfo appInfo;
    private JButton acceptButton;
    private JButton cancelButton;

    public OfflineActivationDialog(Window owner, AppInfo appInfo, aa i18n, ActivationRequest request) throws IllegalArgumentException {
        super(owner, i18n.a("Offline Activate") + " " + W.b(appInfo.getEdition(), "Lite!", ""), Dialog.ModalityType.APPLICATION_MODAL);
        this.requestText = new JTextPane();
        this.activationCodeText = new JTextPane();
        this.i18n = i18n;
        this.activationRequest = request;
        this.appInfo = appInfo;
        ActivationValidator.getInstance().setLocalRequest(request);
        buildUi();
    }

    private void buildUi() throws IllegalArgumentException {
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createTitledBorder(appInfo.getProductName() + " " + i18n.a("Offline Registration Activation")));

        acceptButton = new JButton(i18n.a("Accept"));
        cancelButton = new JButton(i18n.a("Cancel"));

        JLabel instructions = new JLabel();
        instructions.setText("<html><body><H1>" + i18n.a("5 Step Offline Activation") + "</H1><strong>" + i18n.a("Step 1") + "</strong> - " + i18n.a("Save Activation Request to File ActivationRequest.txt on a USB drive or other medium.") + "<br><strong>" + i18n.a("Step 2") + "</strong> - " + i18n.a("On a Computer that is connected to the Internet, open a web browser and go to") + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=\"blue\"><u>http://www.efianalytics.com/activate</u></font><br><strong>" + i18n.a("Step 3") + "</strong> - " + i18n.a("Upload your saved ActivationRequest.txt, the site will provide you with ActivationCode.txt") + "<br><strong>" + i18n.a("Step 4") + "</strong> - " + i18n.a("Return to TunerStudio and click Load Activation From File to load ActivationCode.txt into TunerStudio") + "<br><strong>" + i18n.a("Step 5") + "</strong> - " + i18n.a("Click Accept") + "<br>" + i18n.a("Done!") + "</body></html>");
        instructions.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                aN.a("http://www.efianalytics.com/activate");
            }
        });
        content.add(instructions, BorderLayout.NORTH);

        JPanel requestPanel = new JPanel(new BorderLayout());
        requestPanel.setBorder(BorderFactory.createTitledBorder(i18n.a("Activation Request")));
        int requestHeight = Toolkit.getDefaultToolkit().getScreenSize().height < 640 ? 60 : 120;
        requestText.setMinimumSize(new Dimension(520, requestHeight));
        requestText.setPreferredSize(new Dimension(520, requestHeight));
        requestText.setMaximumSize(new Dimension(520, requestHeight));
        requestText.setBorder(BorderFactory.createBevelBorder(1));
        requestText.setEditable(false);
        requestText.setBackground(Color.LIGHT_GRAY);
        try {
            requestText.setText(activationRequest.toBase64());
            requestText.selectAll();
        } catch (IOException e) {
            bV.d(e.getMessage(), this);
            Logger.getLogger(OfflineActivationDialog.class.getName()).log(Level.SEVERE, (String) null, e);
        }
        requestPanel.add(requestText, BorderLayout.CENTER);

        JPanel requestButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        requestButtons.add(new JLabel("Step 1 -->"));
        JButton saveRequestButton = new JButton(i18n.a("Save Request to File"));
        saveRequestButton.addActionListener(e -> saveRequestToFile());
        requestButtons.add(saveRequestButton);
        JButton copyRequestButton = new JButton(i18n.a("Copy Request to Clipboard"));
        copyRequestButton.addActionListener(e -> copyRequestToClipboard());
        requestButtons.add(copyRequestButton);
        requestPanel.add(requestButtons, BorderLayout.SOUTH);

        content.add(requestPanel, BorderLayout.CENTER);

        JPanel codePanel = new JPanel(new BorderLayout());
        codePanel.setBorder(BorderFactory.createTitledBorder(i18n.a("Server Activation Code")));
        activationCodeText.setMinimumSize(new Dimension(520, 120));
        activationCodeText.setPreferredSize(new Dimension(520, 120));
        activationCodeText.setMaximumSize(new Dimension(520, 120));
        activationCodeText.setBorder(BorderFactory.createBevelBorder(1));
        activationCodeText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateActivationCode();
            }
            @Override
            public void removeUpdate(DocumentEvent e) { }
            @Override
            public void changedUpdate(DocumentEvent e) { }
        });
        codePanel.add(activationCodeText, BorderLayout.CENTER);

        JPanel codeButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        codeButtons.add(new JLabel("Step 4 -->"));
        JButton loadCodeButton = new JButton(i18n.a("Load Activation From File"));
        loadCodeButton.addActionListener(e -> loadActivationFromFile());
        codeButtons.add(loadCodeButton);
        JButton pasteCodeButton = new JButton(i18n.a("Paste Activation Code"));
        pasteCodeButton.addActionListener(e -> pasteActivationCodeFromClipboard());
        codeButtons.add(pasteCodeButton);
        codePanel.add(codeButtons, BorderLayout.SOUTH);

        content.add(codePanel, BorderLayout.SOUTH);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acceptButton.addActionListener(e -> onAccept());
        cancelButton.addActionListener(e -> onCancel());
        footer.add(new JLabel("Step 5 -->"));
        footer.add(acceptButton);
        footer.add(cancelButton);
        acceptButton.setEnabled(false);

        add(footer, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
    }

    private void onAccept() {
        dispose();
    }

    private void onCancel() {
        activationData = null;
        dispose();
    }

    private void copyRequestToClipboard() {
        requestText.selectAll();
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(requestText.getText()), this);
    }

    private void loadActivationFromFile() {
        String path = bV.b(this, i18n.a("Load Activation From File"), new String[]{FileExtensionUtils.txt}, "*.txt", appInfo.getDataDirectory());
        if (path == null || path.equals("")) {
            return;
        }
        File file = new File(path);
        FileReader reader = null;
        try {
            reader = new FileReader(file);
            StringBuilder sb = new StringBuilder();
            for (int ch = reader.read(); ch != -1; ch = reader.read()) {
                sb.append((char) ch);
            }
            activationCodeText.setText(sb.toString());
        } catch (IOException ex) {
            bV.d("unable to read file:\n" + file.getAbsolutePath(), this);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(OfflineActivationDialog.class.getName()).log(Level.SEVERE, (String) null, ex);
                }
            }
        }
    }

    private void saveRequestToFile() {
        requestText.selectAll();
        String requestValue = requestText.getText();
        String path = bV.a(this, "Save Activation Request to File", new String[]{FileExtensionUtils.txt}, appInfo.getProductName() + "ActivationRequest.txt", appInfo.getDataDirectory());
        if (path == null || path.equals("")) {
            return;
        }
        File file = new File(path);
        FileWriter writer = null;
        try {
            if (file.exists()) {
                file.delete();
            }
            if (file.createNewFile()) {
                writer = new FileWriter(file);
                writer.write(requestValue);
            } else {
                bV.d("Can not write file to:\n" + file.getAbsolutePath(), this);
            }
        } catch (IOException ex) {
            bV.d("Can not write to file:\n" + file.getAbsolutePath(), this);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public String getActivationCodeText() {
        return activationCodeText.getText();
    }

    private void pasteActivationCodeFromClipboard() throws HeadlessException {
        try {
            activationCodeText.setText(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor).toString());
        } catch (UnsupportedFlavorException | IOException ex) {
            Logger.getLogger(OfflineActivationDialog.class.getName()).log(Level.SEVERE, (String) null, ex);
        }
    }

    private void validateActivationCode() {
        String code = getActivationCodeText();
        if (code != null && !code.trim().equals("")) {
            try {
                activationData = new ActivationData(code);
                activationData.setRawData(code);
                if (activationData.getRenewalDate().before(new Date())) {
                    bV.d("This Activation code has expired. Please request a new one from:\nhttps://www.efianalytics.com/activate", this);
                    return;
                }
                ActivationResult result = ActivationValidator.getInstance().validate(activationData);
                if (result.getCode() != 0) {
                    bV.d(result.getMessage(), this);
                    activationData = null;
                    acceptButton.setEnabled(false);
                    return;
                }
            } catch (ActivationParseException e) {
                bV.d(i18n.a(ActivationMessages.INVALID_SERVER_CODE), this);
                activationData = null;
                acceptButton.setEnabled(false);
                return;
            }
        }
        acceptButton.setEnabled(true);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }

    public ActivationData getActivationData() {
        return activationData;
    }
}

