package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import s.C1818g;
import v.C1887g;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/b.class */
public class C1429b extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    File f9766a = null;

    /* renamed from: b, reason: collision with root package name */
    J f9767b = new J();

    /* renamed from: c, reason: collision with root package name */
    JComboBox f9768c = new JComboBox();

    /* renamed from: d, reason: collision with root package name */
    JTextField f9769d = new JTextField("", 25);

    /* renamed from: e, reason: collision with root package name */
    JCheckBox f9770e = new JCheckBox(C1818g.b("Other"));

    /* renamed from: f, reason: collision with root package name */
    JButton f9771f = new JButton("...");

    /* renamed from: g, reason: collision with root package name */
    C1432e f9772g = new C1432e(this);

    /* renamed from: h, reason: collision with root package name */
    private G.R f9773h = null;

    public C1429b() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Select Tuning View")));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f9768c.addItem("");
        this.f9768c.addItemListener(new C1430c(this));
        jPanel.add("North", this.f9768c);
        jPanel.add("West", this.f9770e);
        this.f9770e.addItemListener(new C1431d(this));
        jPanel.add(BorderLayout.CENTER, this.f9769d);
        this.f9769d.setEnabled(this.f9770e.isSelected());
        this.f9769d.setEditable(false);
        jPanel.add("East", this.f9771f);
        this.f9771f.setEnabled(this.f9770e.isSelected());
        this.f9771f.addActionListener(new C1433f(this));
        add("North", jPanel);
        this.f9767b.setEnabled(false);
        add(BorderLayout.CENTER, this.f9767b);
        this.f9767b.y();
    }

    public void a(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            this.f9768c.addItem((C1438k) it.next());
        }
        if (this.f9768c.getItemCount() > 1) {
            this.f9768c.setSelectedIndex(1);
        }
    }

    public File a() {
        return this.f9766a;
    }

    public void a(File file) {
        this.f9766a = file;
        try {
            a(new C1887g().a(file));
        } catch (V.a e2) {
            e2.printStackTrace();
        }
    }

    public void a(F f2) {
        File fileD = null;
        try {
            fileD = f2.d();
        } catch (Exception e2) {
        }
        if (fileD != null) {
            try {
                remove(this.f9767b);
                add(BorderLayout.CENTER, this.f9772g);
                this.f9772g.a(ImageIO.read(fileD));
                validate();
                this.f9772g.repaint();
                return;
            } catch (IOException e3) {
                Logger.getLogger(C1429b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        remove(this.f9772g);
        add(BorderLayout.CENTER, this.f9767b);
        validate();
        this.f9767b.a(f2);
        this.f9767b.doLayout();
        this.f9767b.z();
    }

    public boolean b() {
        return this.f9770e.isSelected();
    }
}
