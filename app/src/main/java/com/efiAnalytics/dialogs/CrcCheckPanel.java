package com.efiAnalytics.dialogs;

import bH.C0995c;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.zip.CRC32;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/dialogs/CrcCheckPanel.class */
public class CrcCheckPanel extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JTextPane f9884a = new JTextPane();

    /* renamed from: b, reason: collision with root package name */
    JLabel f9885b = new JLabel();

    /* renamed from: c, reason: collision with root package name */
    JButton f9886c = new JButton("Calculate CRC");

    public CrcCheckPanel() {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f9884a);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, this.f9885b);
        jPanel.add("East", this.f9886c);
        this.f9886c.addActionListener(new a(this));
        add("South", jPanel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() throws IllegalArgumentException {
        try {
            int[] iArrB = C0995c.b(this.f9884a.getText());
            CRC32 crc32 = new CRC32();
            crc32.reset();
            for (int i2 : iArrB) {
                crc32.update((byte) i2);
            }
            this.f9885b.setText("CRC32: " + C0995c.d(C0995c.a((int) crc32.getValue(), new byte[4], true)));
        } catch (Exception e2) {
            bV.d("Failed to parse input. Expected format: xFF xFE x0F ...", this);
        }
    }

    public static void main(String[] strArr) {
        bV.a(new CrcCheckPanel(), (Component) null, "CRC Calculator", (InterfaceC1565bc) null).setDefaultCloseOperation(2);
    }
}
