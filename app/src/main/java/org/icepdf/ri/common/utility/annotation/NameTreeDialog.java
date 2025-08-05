package org.icepdf.ri.common.utility.annotation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.net.ftp.FTPReply;
import org.icepdf.core.pobjects.NameTree;
import org.icepdf.ri.common.SwingController;
import sun.security.ssl.SSLRecord;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/NameTreeDialog.class */
public class NameTreeDialog extends JDialog implements ActionListener, TreeSelectionListener {
    private SwingController controller;
    private ResourceBundle messageBundle;
    private JTree nameJTree;
    private NameTreeNode selectedName;
    private JLabel destinationName;
    private JButton okButton;
    private JButton cancelButton;
    private GridBagConstraints constraints;

    public NameTreeDialog(SwingController controller, boolean modal, NameTree nameTree) throws HeadlessException {
        super(controller.getViewerFrame(), modal);
        this.controller = controller;
        this.messageBundle = this.controller.getMessageBundle();
        setGui(nameTree);
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent e2) throws IllegalArgumentException {
        if (e2.getSource() == this.okButton) {
            if (this.selectedName != null) {
                this.destinationName.setText(this.selectedName.getName().toString());
            }
            setVisible(false);
            dispose();
            return;
        }
        if (e2.getSource() == this.cancelButton) {
            setVisible(false);
            dispose();
        }
    }

    @Override // javax.swing.event.TreeSelectionListener
    public void valueChanged(TreeSelectionEvent e2) {
        if (this.nameJTree.getLastSelectedPathComponent() != null) {
            NameTreeNode selectedNode = (NameTreeNode) this.nameJTree.getLastSelectedPathComponent();
            if (selectedNode.getReference() != null) {
                this.selectedName = selectedNode;
            } else {
                this.nameJTree.setSelectionPath(null);
                this.selectedName = null;
            }
        }
    }

    private void setGui(NameTree nameTree) {
        setTitle(this.messageBundle.getString("viewer.utilityPane.action.dialog.goto.nameTree.title"));
        this.nameJTree = new NameJTree();
        this.nameJTree.setModel(new DefaultTreeModel(new NameTreeNode(nameTree.getRoot(), this.messageBundle)));
        this.nameJTree.setRootVisible(!nameTree.getRoot().isEmpty());
        this.nameJTree.addTreeSelectionListener(this);
        JScrollPane nameTreeScroller = new JScrollPane(this.nameJTree);
        nameTreeScroller.setPreferredSize(new Dimension(SSLRecord.maxPlaintextPlusSize, 225));
        this.okButton = new JButton(this.messageBundle.getString("viewer.button.ok.label"));
        this.okButton.setMnemonic(this.messageBundle.getString("viewer.button.ok.mnemonic").charAt(0));
        this.okButton.addActionListener(this);
        this.cancelButton = new JButton(this.messageBundle.getString("viewer.button.cancel.label"));
        this.cancelButton.setMnemonic(this.messageBundle.getString("viewer.button.cancel.mnemonic").charAt(0));
        this.cancelButton.addActionListener(this);
        JPanel okCancelPanel = new JPanel(new FlowLayout());
        okCancelPanel.add(this.okButton);
        okCancelPanel.add(this.cancelButton);
        JPanel nameTreePanel = new JPanel();
        nameTreePanel.setAlignmentY(0.0f);
        GridBagLayout layout = new GridBagLayout();
        nameTreePanel.setLayout(layout);
        this.constraints = new GridBagConstraints();
        this.constraints.fill = 0;
        this.constraints.weightx = 1.0d;
        this.constraints.anchor = 11;
        this.constraints.anchor = 13;
        this.constraints.insets = new Insets(5, 5, 5, 5);
        this.constraints.anchor = 10;
        addGB(nameTreePanel, nameTreeScroller, 0, 0, 1, 1);
        addGB(nameTreePanel, okCancelPanel, 0, 1, 1, 1);
        getContentPane().add(nameTreePanel);
        setSize(new Dimension(375, FTPReply.FILE_ACTION_PENDING));
        validate();
        setLocationRelativeTo(this.controller.getViewerFrame());
    }

    public void setDestinationName(JLabel destinationName) {
        this.destinationName = destinationName;
    }

    @Override // javax.swing.JDialog
    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() { // from class: org.icepdf.ri.common.utility.annotation.NameTreeDialog.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                NameTreeDialog.this.setVisible(false);
                NameTreeDialog.this.dispose();
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
