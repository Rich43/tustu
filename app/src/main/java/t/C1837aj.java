package t;

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

/* renamed from: t.aj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/aj.class */
public class C1837aj extends AbstractC1827a {

    /* renamed from: a, reason: collision with root package name */
    List f13787a;

    /* renamed from: b, reason: collision with root package name */
    JPanel f13788b;

    /* renamed from: c, reason: collision with root package name */
    C1845ar f13789c;

    /* renamed from: d, reason: collision with root package name */
    HashMap f13790d;

    /* renamed from: e, reason: collision with root package name */
    JPanel f13791e;

    public C1837aj(Window window, String str) {
        super(window, C1818g.b(str));
        this.f13787a = new ArrayList();
        this.f13788b = new JPanel();
        this.f13790d = new HashMap();
        this.f13791e = new JPanel();
        setLayout(new BorderLayout());
        this.f13791e.setLayout(new BorderLayout());
        this.f13791e.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(BorderLayout.CENTER, this.f13791e);
        JScrollPane jScrollPane = new JScrollPane(this.f13788b);
        this.f13788b.setLayout(new GridLayout(0, 3, 5, 5));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Current Images")));
        this.f13791e.add(BorderLayout.CENTER, jPanel);
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        jScrollPane.setSize(eJ.a(NNTPReply.SEND_ARTICLE_TO_POST), eJ.a(240));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        jPanel2.setBorder(BorderFactory.createTitledBorder(C1818g.b("Selected Image Preview")));
        this.f13789c = new C1845ar(this, 185);
        jPanel2.add(this.f13789c);
        jPanel.add("East", jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        JButton jButton = new JButton(C1818g.b("Other Images"));
        jButton.addActionListener(new C1838ak(this));
        jPanel3.add(jButton);
        JLabel jLabel = new JLabel();
        jLabel.setPreferredSize(eJ.a(50, 20));
        jPanel3.add(jLabel);
        JButton jButton2 = new JButton(C1818g.b("Apply"));
        jButton2.addActionListener(new C1839al(this));
        jPanel3.add(jButton2);
        JButton jButton3 = new JButton(C1818g.b("Clear"));
        jButton3.addActionListener(new C1840am(this));
        jPanel3.add(jButton3);
        JButton jButton4 = new JButton(C1818g.b("Done"));
        jButton4.addActionListener(new C1841an(this));
        jPanel3.add(jButton4);
        this.f13791e.add("South", jPanel3);
        setSize(eJ.a(800), eJ.a(450));
        setResizable(false);
    }

    public C1837aj(Window window, File file, String str) {
        this(window, str);
        a(file);
        a(C1807j.i());
    }

    public C1837aj(Window window, List list, String str) {
        this(window, str);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            a((File) it.next());
        }
        a(C1807j.i());
    }

    public void a(File file) {
        File[] fileArrListFiles = file.listFiles(new C1842ao(this));
        for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
            if (this.f13790d.get(fileArrListFiles[i2].getName()) == null) {
                C1845ar c1845ar = new C1845ar(this, fileArrListFiles[i2]);
                c1845ar.addMouseListener(new C1843ap(this));
                this.f13788b.add(c1845ar);
                this.f13790d.put(fileArrListFiles[i2].getName(), c1845ar);
            }
        }
    }

    public void a() {
        String strA = bV.a((Component) this, getTitle(), new String[]{"gif", "jpg", "jpeg", "png"}, "", C1807j.w(), true, (AbstractC1600ck) new C1601cl());
        if (strA == null || strA.equals("")) {
            return;
        }
        C1845ar c1845ar = new C1845ar(this, new File(strA));
        c1845ar.addMouseListener(new C1844aq(this));
        this.f13788b.add(c1845ar);
        this.f13788b.validate();
        this.f13789c.a(c1845ar.f13799a);
    }

    public void a(com.efiAnalytics.apps.ts.dashboard.aK aKVar) {
        this.f13787a.add(aKVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(File file) {
        Iterator it = this.f13787a.iterator();
        while (it.hasNext()) {
            ((com.efiAnalytics.apps.ts.dashboard.aK) it.next()).a(file);
        }
    }
}
