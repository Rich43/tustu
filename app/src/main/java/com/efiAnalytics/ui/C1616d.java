package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.ui.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/d.class */
public class C1616d extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    JLabel f11310a;

    public C1616d(Window window, String str, String str2) {
        super(window, str, Dialog.ModalityType.MODELESS);
        this.f11310a = new JLabel(" ", 0);
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
        StringTokenizer stringTokenizer = new StringTokenizer(str2, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            jPanel3.add(new JLabel(stringTokenizer.nextToken(), 0));
        }
        jPanel3.add(this.f11310a);
        jPanel.add("South", jPanel3);
        jPanel.add("North", new JLabel(" "));
        pack();
    }

    public void a(String str) {
        this.f11310a.setText(str);
    }
}
