package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.AbstractC1600ck;
import com.efiAnalytics.ui.C1601cl;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.commons.net.nntp.NNTPReply;
import r.C1807j;
import s.C1818g;
import t.AbstractC1827a;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/g.class */
public class C1408g extends AbstractC1827a {

    /* renamed from: a, reason: collision with root package name */
    List f9498a;

    /* renamed from: b, reason: collision with root package name */
    JPanel f9499b;

    /* renamed from: c, reason: collision with root package name */
    C1415n f9500c;

    /* renamed from: d, reason: collision with root package name */
    HashMap f9501d;

    /* renamed from: e, reason: collision with root package name */
    JPanel f9502e;

    public C1408g(Window window, String str) {
        super(window, C1818g.b(str));
        this.f9498a = new ArrayList();
        this.f9499b = new JPanel();
        this.f9501d = new HashMap();
        this.f9502e = new JPanel();
        setLayout(new BorderLayout());
        this.f9502e.setLayout(new BorderLayout());
        this.f9502e.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(BorderLayout.CENTER, this.f9502e);
        JScrollPane jScrollPane = new JScrollPane(this.f9499b);
        this.f9499b.setLayout(new GridLayout(0, 3, 5, 5));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Current Gauges")));
        this.f9502e.add(BorderLayout.CENTER, jPanel);
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        jScrollPane.setSize(eJ.a(NNTPReply.SEND_ARTICLE_TO_POST), eJ.a(240));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        jPanel2.setBorder(BorderFactory.createTitledBorder(C1818g.b("Selected Gauge Preview")));
        this.f9500c = new C1415n(this, eJ.a(185));
        jPanel2.add(this.f9500c);
        jPanel.add("East", jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        JButton jButton = new JButton(C1818g.b("Other Gauges"));
        jButton.addActionListener(new C1409h(this));
        jPanel3.add(jButton);
        JLabel jLabel = new JLabel();
        jLabel.setPreferredSize(eJ.a(50, 20));
        jPanel3.add(jLabel);
        JButton jButton2 = new JButton(C1818g.b("Add Gauge"));
        jButton2.addActionListener(new C1410i(this));
        jPanel3.add(jButton2);
        JButton jButton3 = new JButton(C1818g.b("Done"));
        jButton3.addActionListener(new C1411j(this));
        jPanel3.add(jButton3);
        this.f9502e.add("South", jPanel3);
        setSize(eJ.a(900), eJ.a(550));
    }

    public C1408g(Window window, List list, String str) {
        this(window, str);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            a((File) it.next());
        }
    }

    public void a(File file) {
        File[] fileArrListFiles = file.listFiles(new C1412k(this));
        for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
            try {
                if (this.f9501d.get(fileArrListFiles[i2].getName()) == null) {
                    C1415n c1415n = new C1415n(this, fileArrListFiles[i2]);
                    c1415n.addMouseListener(new C1413l(this));
                    this.f9499b.add(c1415n);
                    this.f9501d.put(fileArrListFiles[i2].getName(), c1415n);
                }
            } catch (Exception e2) {
                bH.C.c("Exception adding a gauge to the selection for file: " + ((Object) fileArrListFiles[i2]));
            }
        }
    }

    public void a() {
        String strA = bV.a((Component) this, getTitle(), new String[]{"gauge"}, "", C1807j.w(), true, (AbstractC1600ck) new C1601cl());
        if (strA == null || strA.equals("")) {
            return;
        }
        C1415n c1415n = new C1415n(this, new File(strA));
        c1415n.addMouseListener(new C1414m(this));
        this.f9499b.add(c1415n);
        this.f9499b.validate();
        this.f9500c.a(c1415n.f9509a);
    }

    public void a(InterfaceC1387a interfaceC1387a) {
        this.f9498a.add(interfaceC1387a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(File file) {
        Iterator it = this.f9498a.iterator();
        while (it.hasNext()) {
            ((InterfaceC1387a) it.next()).a(file);
        }
    }
}
