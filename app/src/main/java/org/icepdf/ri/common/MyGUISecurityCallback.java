package org.icepdf.ri.common;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.icepdf.core.SecurityCallback;
import org.icepdf.core.pobjects.Document;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/MyGUISecurityCallback.class */
public class MyGUISecurityCallback implements SecurityCallback {
    private JFrame parentFrame;
    private ResourceBundle messageBundle;

    public MyGUISecurityCallback(JFrame frame, ResourceBundle messageBundle) {
        this.parentFrame = frame;
        this.messageBundle = messageBundle;
    }

    @Override // org.icepdf.core.SecurityCallback
    public String requestPassword(Document document) {
        PasswordDialog passwordDialog = new PasswordDialog(this.parentFrame);
        passwordDialog.setVisible(true);
        if (passwordDialog.isCanceled) {
            return null;
        }
        return passwordDialog.getPassword();
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/MyGUISecurityCallback$PasswordDialog.class */
    class PasswordDialog extends JDialog implements WindowListener {
        private GridBagConstraints constraints;
        private JPasswordField passwordField;
        private boolean isCanceled;

        public PasswordDialog(JFrame frame) {
            super((Frame) frame, true);
            this.isCanceled = false;
            setTitle(MyGUISecurityCallback.this.messageBundle.getString("viewer.dialog.security.title"));
            final JButton okButton = new JButton(MyGUISecurityCallback.this.messageBundle.getString("viewer.dialog.security.okButton.label"));
            okButton.setMnemonic(MyGUISecurityCallback.this.messageBundle.getString("viewer.dialog.security.okButton.mnemonic").charAt(0));
            okButton.addActionListener(new ActionListener() { // from class: org.icepdf.ri.common.MyGUISecurityCallback.PasswordDialog.1
                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent e2) {
                    if (e2.getSource() == okButton) {
                        PasswordDialog.this.setVisible(false);
                        PasswordDialog.this.dispose();
                    }
                }
            });
            final JButton cancelButton = new JButton(MyGUISecurityCallback.this.messageBundle.getString("viewer.dialog.security.cancelButton.label"));
            cancelButton.setMnemonic(MyGUISecurityCallback.this.messageBundle.getString("viewer.dialog.security.cancelButton.mnemonic").charAt(0));
            cancelButton.addActionListener(new ActionListener() { // from class: org.icepdf.ri.common.MyGUISecurityCallback.PasswordDialog.2
                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent e2) {
                    if (e2.getSource() == cancelButton) {
                        PasswordDialog.this.setVisible(false);
                        PasswordDialog.this.isCanceled = true;
                        PasswordDialog.this.dispose();
                    }
                }
            });
            this.passwordField = new JPasswordField(30);
            this.passwordField.addActionListener(new ActionListener() { // from class: org.icepdf.ri.common.MyGUISecurityCallback.PasswordDialog.3
                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent e2) {
                    if (e2.getSource() == PasswordDialog.this.passwordField) {
                        PasswordDialog.this.setVisible(false);
                        PasswordDialog.this.dispose();
                    }
                }
            });
            JLabel msg1 = new JLabel(MyGUISecurityCallback.this.messageBundle.getString("viewer.dialog.security.msg"));
            JLabel msg2 = new JLabel(MyGUISecurityCallback.this.messageBundle.getString("viewer.dialog.security.password.label"));
            JPanel passwordPanel = new JPanel();
            passwordPanel.setAlignmentY(0.0f);
            passwordPanel.setAlignmentX(0.5f);
            GridBagLayout layout = new GridBagLayout();
            passwordPanel.setLayout(layout);
            getContentPane().add(passwordPanel);
            this.constraints = new GridBagConstraints();
            this.constraints.fill = 0;
            this.constraints.weightx = 1.0d;
            this.constraints.anchor = 11;
            this.constraints.anchor = 17;
            this.constraints.insets = new Insets(1, 10, 1, 1);
            addGB(passwordPanel, msg1, 0, 0, 3, 1);
            addGB(passwordPanel, msg2, 0, 1, 1, 1);
            this.constraints.fill = 2;
            this.constraints.insets = new Insets(1, 10, 1, 10);
            addGB(passwordPanel, this.passwordField, 1, 1, 2, 1);
            this.constraints.insets = new Insets(10, 1, 1, 1);
            this.constraints.fill = 0;
            addGB(passwordPanel, okButton, 1, 2, 1, 1);
            addGB(passwordPanel, cancelButton, 2, 2, 1, 1);
            pack();
            setLocationRelativeTo(frame);
            setResizable(false);
            setSize(306, 150);
            addWindowListener(this);
        }

        public String getPassword() {
            return new String(this.passwordField.getPassword());
        }

        public boolean isCancelled() {
            return this.isCanceled;
        }

        private void addGB(JPanel layout, Component component, int x2, int y2, int colSpan, int rowSpan) {
            this.constraints.gridx = x2;
            this.constraints.gridy = y2;
            this.constraints.gridwidth = colSpan;
            this.constraints.gridheight = rowSpan;
            layout.add(component, this.constraints);
        }

        @Override // java.awt.event.WindowListener
        public void windowClosing(WindowEvent ev) {
            setVisible(false);
            this.isCanceled = true;
            dispose();
        }

        @Override // javax.swing.JDialog
        protected JRootPane createRootPane() {
            ActionListener actionListener = new ActionListener() { // from class: org.icepdf.ri.common.MyGUISecurityCallback.PasswordDialog.4
                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) {
                    PasswordDialog.this.setVisible(false);
                    PasswordDialog.this.isCanceled = true;
                    PasswordDialog.this.dispose();
                }
            };
            JRootPane rootPane = new JRootPane();
            KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
            rootPane.registerKeyboardAction(actionListener, stroke, 2);
            return rootPane;
        }

        @Override // java.awt.event.WindowListener
        public void windowActivated(WindowEvent ev) {
        }

        @Override // java.awt.event.WindowListener
        public void windowClosed(WindowEvent ev) {
        }

        @Override // java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent ev) {
        }

        @Override // java.awt.event.WindowListener
        public void windowDeiconified(WindowEvent ev) {
        }

        @Override // java.awt.event.WindowListener
        public void windowIconified(WindowEvent ev) {
        }

        @Override // java.awt.event.WindowListener
        public void windowOpened(WindowEvent ev) {
        }
    }
}
