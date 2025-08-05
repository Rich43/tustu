package javax.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.Locale;
import sun.swing.SwingUtilities2;

/* compiled from: JColorChooser.java */
/* loaded from: rt.jar:javax/swing/ColorChooserDialog.class */
class ColorChooserDialog extends JDialog {
    private Color initialColor;
    private JColorChooser chooserPane;
    private JButton cancelButton;

    public ColorChooserDialog(Dialog dialog, String str, boolean z2, Component component, JColorChooser jColorChooser, ActionListener actionListener, ActionListener actionListener2) throws HeadlessException {
        super(dialog, str, z2);
        initColorChooserDialog(component, jColorChooser, actionListener, actionListener2);
    }

    public ColorChooserDialog(Frame frame, String str, boolean z2, Component component, JColorChooser jColorChooser, ActionListener actionListener, ActionListener actionListener2) throws HeadlessException {
        super(frame, str, z2);
        initColorChooserDialog(component, jColorChooser, actionListener, actionListener2);
    }

    protected void initColorChooserDialog(Component component, JColorChooser jColorChooser, ActionListener actionListener, ActionListener actionListener2) {
        this.chooserPane = jColorChooser;
        Locale locale = getLocale();
        String string = UIManager.getString("ColorChooser.okText", locale);
        String string2 = UIManager.getString("ColorChooser.cancelText", locale);
        String string3 = UIManager.getString("ColorChooser.resetText", locale);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(jColorChooser, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(1));
        JButton jButton = new JButton(string);
        getRootPane().setDefaultButton(jButton);
        jButton.getAccessibleContext().setAccessibleDescription(string);
        jButton.setActionCommand("OK");
        jButton.addActionListener(new ActionListener() { // from class: javax.swing.ColorChooserDialog.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                ColorChooserDialog.this.hide();
            }
        });
        if (actionListener != null) {
            jButton.addActionListener(actionListener);
        }
        jPanel.add(jButton);
        this.cancelButton = new JButton(string2);
        this.cancelButton.getAccessibleContext().setAccessibleDescription(string2);
        AbstractAction abstractAction = new AbstractAction() { // from class: javax.swing.ColorChooserDialog.2
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                ((AbstractButton) actionEvent.getSource()).fireActionPerformed(actionEvent);
            }
        };
        KeyStroke keyStroke = KeyStroke.getKeyStroke(27, 0);
        InputMap inputMap = this.cancelButton.getInputMap(2);
        ActionMap actionMap = this.cancelButton.getActionMap();
        if (inputMap != null && actionMap != null) {
            inputMap.put(keyStroke, "cancel");
            actionMap.put("cancel", abstractAction);
        }
        this.cancelButton.setActionCommand("cancel");
        this.cancelButton.addActionListener(new ActionListener() { // from class: javax.swing.ColorChooserDialog.3
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                ColorChooserDialog.this.hide();
            }
        });
        if (actionListener2 != null) {
            this.cancelButton.addActionListener(actionListener2);
        }
        jPanel.add(this.cancelButton);
        JButton jButton2 = new JButton(string3);
        jButton2.getAccessibleContext().setAccessibleDescription(string3);
        jButton2.addActionListener(new ActionListener() { // from class: javax.swing.ColorChooserDialog.4
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                ColorChooserDialog.this.reset();
            }
        });
        int uIDefaultsInt = SwingUtilities2.getUIDefaultsInt("ColorChooser.resetMnemonic", locale, -1);
        if (uIDefaultsInt != -1) {
            jButton2.setMnemonic(uIDefaultsInt);
        }
        jPanel.add(jButton2);
        contentPane.add(jPanel, "South");
        if (JDialog.isDefaultLookAndFeelDecorated() && UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
            getRootPane().setWindowDecorationStyle(5);
        }
        applyComponentOrientation((component == null ? getRootPane() : component).getComponentOrientation());
        pack();
        setLocationRelativeTo(component);
        addWindowListener(new Closer());
    }

    @Override // java.awt.Dialog, java.awt.Window, java.awt.Component
    public void show() {
        this.initialColor = this.chooserPane.getColor();
        super.show();
    }

    public void reset() {
        this.chooserPane.setColor(this.initialColor);
    }

    /* compiled from: JColorChooser.java */
    /* loaded from: rt.jar:javax/swing/ColorChooserDialog$Closer.class */
    class Closer extends WindowAdapter implements Serializable {
        Closer() {
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowClosing(WindowEvent windowEvent) {
            ColorChooserDialog.this.cancelButton.doClick(0);
            windowEvent.getWindow().hide();
        }
    }

    /* compiled from: JColorChooser.java */
    /* loaded from: rt.jar:javax/swing/ColorChooserDialog$DisposeOnClose.class */
    static class DisposeOnClose extends ComponentAdapter implements Serializable {
        DisposeOnClose() {
        }

        @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
        public void componentHidden(ComponentEvent componentEvent) {
            ((Window) componentEvent.getComponent()).dispose();
        }
    }
}
