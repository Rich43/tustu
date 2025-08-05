package az;

import bH.aa;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: az.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:az/a.class */
public class C0940a extends JDialog {
    public C0940a(Window window, aa aaVar) {
        super(window, aaVar.a("Activating Registration"), Dialog.ModalityType.MODELESS);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("wait_animated.gif")));
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        jPanel2.add(jLabel);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(0, 1, 4, 4));
        jPanel3.add(new JLabel(aaVar.a("Verifying and activating registration on this computer."), 0));
        jPanel3.add(new JLabel(aaVar.a("Please Wait") + "....", 0));
        jPanel3.add(new JLabel(" ", 0));
        jPanel.add("South", jPanel3);
        jPanel.add("North", new JLabel(" "));
        pack();
    }
}
