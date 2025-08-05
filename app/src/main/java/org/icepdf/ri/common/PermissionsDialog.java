package org.icepdf.ri.common;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.security.Permissions;
import org.icepdf.core.pobjects.security.SecurityManager;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/PermissionsDialog.class */
public class PermissionsDialog extends JDialog {
    private GridBagConstraints constraints;

    public PermissionsDialog(JFrame frame, Document document, ResourceBundle messageBundle) {
        Permissions permissions;
        super((Frame) frame, true);
        setTitle(messageBundle.getString("viewer.dialog.documentPermissions.title"));
        String none = messageBundle.getString("viewer.dialog.documentPermissions.none");
        String no = messageBundle.getString("viewer.dialog.documentPermissions.no");
        String yes = messageBundle.getString("viewer.dialog.documentPermissions.yes");
        String fullyAllowed = messageBundle.getString("viewer.dialog.documentPermissions.fullyAllowed");
        String notAllowed = messageBundle.getString("viewer.dialog.documentPermissions.notAllowed");
        String allowed = messageBundle.getString("viewer.dialog.documentPermissions.allowed");
        String standardSecurity = messageBundle.getString("viewer.dialog.documentPermissions.standardSecurity");
        String lowQuality = messageBundle.getString("viewer.dialog.documentPermissions.partial");
        String securityLevel = messageBundle.getString("viewer.dialog.documentPermissions.securityLevel");
        String securityMethod = none;
        String userPassword = no;
        String ownerPassword = no;
        String printing = fullyAllowed;
        String changing = allowed;
        String extraction = allowed;
        String authoring = allowed;
        String forms = allowed;
        String accessibility = allowed;
        String assembly = allowed;
        String level = none;
        SecurityManager securityManager = document.getSecurityManager();
        if (securityManager != null && (permissions = securityManager.getPermissions()) != null) {
            securityMethod = standardSecurity;
            userPassword = securityManager.getSecurityHandler().isUserAuthorized("") ? userPassword : yes;
            ownerPassword = securityManager.getSecurityHandler().isOwnerAuthorized("") ? ownerPassword : yes;
            if (!permissions.getPermissions(0)) {
                if (!permissions.getPermissions(1)) {
                    printing = lowQuality;
                } else {
                    printing = notAllowed;
                }
            }
            changing = permissions.getPermissions(2) ? changing : notAllowed;
            extraction = permissions.getPermissions(3) ? extraction : notAllowed;
            authoring = permissions.getPermissions(4) ? authoring : notAllowed;
            forms = permissions.getPermissions(5) ? forms : notAllowed;
            accessibility = permissions.getPermissions(6) ? accessibility : notAllowed;
            assembly = permissions.getPermissions(7) ? assembly : notAllowed;
            int length = securityManager.getEncryptionDictionary().getKeyLength();
            Object[] messageArguments = {String.valueOf(length), String.valueOf(securityManager.getEncryptionDictionary().getVersion()), String.valueOf(securityManager.getEncryptionDictionary().getRevisionNumber())};
            MessageFormat formatter = new MessageFormat(securityLevel);
            level = formatter.format(messageArguments);
        }
        final JButton okButton = new JButton(messageBundle.getString("viewer.button.ok.label"));
        okButton.setMnemonic(messageBundle.getString("viewer.button.ok.mnemonic").charAt(0));
        okButton.addActionListener(new ActionListener() { // from class: org.icepdf.ri.common.PermissionsDialog.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent e2) {
                if (e2.getSource() == okButton) {
                    PermissionsDialog.this.setVisible(false);
                    PermissionsDialog.this.dispose();
                }
            }
        });
        JPanel permissionsPanel = new JPanel();
        permissionsPanel.setAlignmentY(0.0f);
        GridBagLayout layout = new GridBagLayout();
        permissionsPanel.setLayout(layout);
        this.constraints = new GridBagConstraints();
        this.constraints.fill = 0;
        this.constraints.weightx = 1.0d;
        this.constraints.anchor = 11;
        this.constraints.anchor = 13;
        this.constraints.insets = new Insets(5, 5, 5, 5);
        addGB(permissionsPanel, new JLabel("Security Method:"), 0, 0, 1, 1);
        addGB(permissionsPanel, new JLabel("User Password:"), 0, 1, 1, 1);
        addGB(permissionsPanel, new JLabel("Owner Password:"), 0, 2, 1, 1);
        addGB(permissionsPanel, new JLabel("Printing:"), 0, 3, 1, 1);
        addGB(permissionsPanel, new JLabel("Changing the Document:"), 0, 4, 1, 1);
        addGB(permissionsPanel, new JLabel("Content Copying or Extraction:"), 0, 5, 1, 1);
        addGB(permissionsPanel, new JLabel("Authoring Comments and Form Fields:"), 0, 6, 1, 1);
        addGB(permissionsPanel, new JLabel("Form Field Fill-in or Signing:"), 0, 7, 1, 1);
        addGB(permissionsPanel, new JLabel("Content Accessibility Enabled:"), 0, 8, 1, 1);
        addGB(permissionsPanel, new JLabel("Document Assembly:"), 0, 9, 1, 1);
        addGB(permissionsPanel, new JLabel("Encryption Level:"), 0, 10, 1, 1);
        this.constraints.insets = new Insets(15, 5, 5, 5);
        this.constraints.anchor = 10;
        addGB(permissionsPanel, okButton, 0, 11, 2, 1);
        this.constraints.insets = new Insets(5, 5, 5, 5);
        this.constraints.anchor = 17;
        addGB(permissionsPanel, new JLabel(securityMethod), 1, 0, 1, 1);
        addGB(permissionsPanel, new JLabel(userPassword), 1, 1, 1, 1);
        addGB(permissionsPanel, new JLabel(ownerPassword), 1, 2, 1, 1);
        addGB(permissionsPanel, new JLabel(printing), 1, 3, 1, 1);
        addGB(permissionsPanel, new JLabel(changing), 1, 4, 1, 1);
        addGB(permissionsPanel, new JLabel(extraction), 1, 5, 1, 1);
        addGB(permissionsPanel, new JLabel(authoring), 1, 6, 1, 1);
        addGB(permissionsPanel, new JLabel(forms), 1, 7, 1, 1);
        addGB(permissionsPanel, new JLabel(accessibility), 1, 8, 1, 1);
        addGB(permissionsPanel, new JLabel(assembly), 1, 9, 1, 1);
        addGB(permissionsPanel, new JLabel(level), 1, 10, 1, 1);
        getContentPane().add(permissionsPanel);
        pack();
        setLocationRelativeTo(frame);
    }

    @Override // javax.swing.JDialog
    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() { // from class: org.icepdf.ri.common.PermissionsDialog.2
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                PermissionsDialog.this.setVisible(false);
                PermissionsDialog.this.dispose();
            }
        };
        JRootPane rootPane = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        rootPane.registerKeyboardAction(actionListener, stroke, 2);
        return rootPane;
    }

    private void addGB(JPanel layout, Component component, int x2, int y2, int rowSpan, int colSpan) {
        this.constraints.gridx = x2;
        this.constraints.gridy = y2;
        this.constraints.gridwidth = rowSpan;
        this.constraints.gridheight = colSpan;
        layout.add(component, this.constraints);
    }
}
