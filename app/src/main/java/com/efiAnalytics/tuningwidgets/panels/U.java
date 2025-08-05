package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.eJ;
import com.sun.glass.ui.Clipboard;
import h.C1737b;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import jdk.internal.dynalink.CallSiteDescriptor;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/U.class */
public class U extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JLabel f10308a = new JLabel();

    /* renamed from: b, reason: collision with root package name */
    JLabel f10309b = new JLabel();

    /* renamed from: c, reason: collision with root package name */
    JLabel f10310c = new JLabel();

    /* renamed from: d, reason: collision with root package name */
    JLabel f10311d = new JLabel();

    /* renamed from: e, reason: collision with root package name */
    JTextPane f10312e = new JTextPane();

    public U() {
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        jPanel.add(a(C1818g.b("Restore Point Date") + CallSiteDescriptor.TOKEN_DELIMITER, this.f10310c));
        if (C1737b.a().a(" ,SD;LKODGPOIGD9")) {
            jPanel.add(a(C1818g.b("Firmware Rev Level") + CallSiteDescriptor.TOKEN_DELIMITER, this.f10309b));
            jPanel.add(a(C1818g.b("Serial Number") + CallSiteDescriptor.TOKEN_DELIMITER, this.f10311d));
        } else {
            jPanel.add(a(C1818g.b("Serial Signature") + CallSiteDescriptor.TOKEN_DELIMITER, this.f10309b));
            jPanel.add(a(C1818g.b("Firmware Version") + CallSiteDescriptor.TOKEN_DELIMITER, this.f10311d));
        }
        jPanel.add(new JLabel(C1818g.b("Tune Log Notes") + CallSiteDescriptor.TOKEN_DELIMITER));
        add("North", jPanel);
        this.f10312e.setEditable(false);
        add(BorderLayout.CENTER, new JScrollPane(this.f10312e));
    }

    public void a(W.ag agVar) throws IllegalArgumentException {
        if (agVar == null) {
            this.f10308a.setText("");
            this.f10309b.setText("");
            this.f10310c.setText("");
            this.f10311d.setText("");
            this.f10312e.setText("");
            return;
        }
        this.f10308a.setText(agVar.a().getName());
        this.f10309b.setText(agVar.d());
        this.f10310c.setText(agVar.c());
        this.f10311d.setText(agVar.e());
        if (agVar.b().startsWith("<html")) {
            this.f10312e.setContentType(Clipboard.HTML_TYPE);
        } else {
            this.f10312e.setContentType(Clipboard.TEXT_TYPE);
        }
        this.f10312e.setText(agVar.b());
        this.f10312e.setCaretPosition(0);
    }

    private JPanel a(String str, JLabel jLabel) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JLabel jLabel2 = new JLabel(str);
        jPanel.add("West", jLabel2);
        jLabel2.setPreferredSize(new Dimension(eJ.a(120), eJ.a(21)));
        jPanel.add(BorderLayout.CENTER, jLabel);
        jLabel.setPreferredSize(new Dimension(eJ.a(320), eJ.a(21)));
        return jPanel;
    }
}
