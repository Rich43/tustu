package org.icepdf.ri.common;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PInfo;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/DocumentInformationDialog.class */
public class DocumentInformationDialog extends JDialog {
    private GridBagConstraints constraints;

    public DocumentInformationDialog(JFrame frame, Document document, ResourceBundle messageBundle) {
        super((Frame) frame, true);
        setTitle(messageBundle.getString("viewer.dialog.documentInformation.title"));
        String title = "";
        String author = "";
        String subject = "";
        String keyWords = "";
        String creator = "";
        String producer = "";
        String creationDate = "";
        String modDate = "";
        String notAvailable = messageBundle.getString("viewer.dialog.documentInformation.notAvailable");
        PInfo documentInfo = document.getInfo();
        if (documentInfo != null) {
            title = documentInfo.getTitle();
            author = documentInfo.getAuthor();
            subject = documentInfo.getSubject();
            keyWords = documentInfo.getKeywords();
            creator = documentInfo.getCreator() != null ? documentInfo.getCreator() : notAvailable;
            producer = documentInfo.getProducer() != null ? documentInfo.getProducer() : notAvailable;
            creationDate = documentInfo.getCreationDate() != null ? documentInfo.getCreationDate().toString() : notAvailable;
            modDate = documentInfo.getModDate() != null ? documentInfo.getModDate().toString() : notAvailable;
        }
        final JButton okButton = new JButton(messageBundle.getString("viewer.button.ok.label"));
        okButton.setMnemonic(messageBundle.getString("viewer.button.ok.mnemonic").charAt(0));
        okButton.addActionListener(new ActionListener() { // from class: org.icepdf.ri.common.DocumentInformationDialog.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent e2) {
                if (e2.getSource() == okButton) {
                    DocumentInformationDialog.this.setVisible(false);
                    DocumentInformationDialog.this.dispose();
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
        addGB(permissionsPanel, new JLabel(messageBundle.getString("viewer.dialog.documentInformation.title.label")), 0, 0, 1, 1);
        addGB(permissionsPanel, new JLabel(messageBundle.getString("viewer.dialog.documentInformation.subject.label")), 0, 1, 1, 1);
        addGB(permissionsPanel, new JLabel(messageBundle.getString("viewer.dialog.documentInformation.author.label")), 0, 2, 1, 1);
        addGB(permissionsPanel, new JLabel(messageBundle.getString("viewer.dialog.documentInformation.keywords.label")), 0, 3, 1, 1);
        addGB(permissionsPanel, new JLabel(messageBundle.getString("viewer.dialog.documentInformation.creator.label")), 0, 4, 1, 1);
        addGB(permissionsPanel, new JLabel(messageBundle.getString("viewer.dialog.documentInformation.producer.label")), 0, 5, 1, 1);
        addGB(permissionsPanel, new JLabel(messageBundle.getString("viewer.dialog.documentInformation.created.label")), 0, 6, 1, 1);
        addGB(permissionsPanel, new JLabel(messageBundle.getString("viewer.dialog.documentInformation.modified.label")), 0, 7, 1, 1);
        this.constraints.insets = new Insets(15, 5, 5, 5);
        this.constraints.anchor = 10;
        addGB(permissionsPanel, okButton, 0, 8, 2, 1);
        this.constraints.insets = new Insets(5, 5, 5, 5);
        this.constraints.anchor = 17;
        addGB(permissionsPanel, new JLabel(title), 1, 0, 1, 1);
        addGB(permissionsPanel, new JLabel(subject), 1, 1, 1, 1);
        addGB(permissionsPanel, new JLabel(author), 1, 2, 1, 1);
        addGB(permissionsPanel, new JLabel(keyWords), 1, 3, 1, 1);
        addGB(permissionsPanel, new JLabel(creator), 1, 4, 1, 1);
        addGB(permissionsPanel, new JLabel(producer), 1, 5, 1, 1);
        addGB(permissionsPanel, new JLabel(creationDate), 1, 6, 1, 1);
        addGB(permissionsPanel, new JLabel(modDate), 1, 7, 1, 1);
        getContentPane().add(permissionsPanel);
        pack();
        setLocationRelativeTo(frame);
    }

    @Override // javax.swing.JDialog
    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() { // from class: org.icepdf.ri.common.DocumentInformationDialog.2
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                DocumentInformationDialog.this.setVisible(false);
                DocumentInformationDialog.this.dispose();
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
