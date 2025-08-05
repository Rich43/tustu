package bk;

import G.R;
import bD.C0963i;
import bt.C1345d;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import r.C1806i;
import s.C1818g;
import y.C1894b;

/* loaded from: TunerStudioMS.jar:bk/d.class */
public class d extends C1345d implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    R f8183a;

    /* renamed from: b, reason: collision with root package name */
    h f8184b;

    /* renamed from: c, reason: collision with root package name */
    aI.l f8185c;

    /* renamed from: d, reason: collision with root package name */
    String f8186d;

    /* renamed from: e, reason: collision with root package name */
    Image f8187e = null;

    /* renamed from: f, reason: collision with root package name */
    JPanel f8188f;

    public d(R r2) {
        this.f8185c = null;
        this.f8186d = null;
        this.f8188f = null;
        this.f8183a = r2;
        boolean zA = C1806i.a().a(",sakfdsimfd09rew");
        setLayout(new BorderLayout());
        this.f8188f = new JPanel();
        this.f8188f.setLayout(new BorderLayout());
        this.f8185c = new aI.l(r2, aE.a.A().L());
        this.f8184b = new h(r2, zA);
        this.f8184b.a(this.f8185c);
        if (!zA) {
            JMenuBar jMenuBar = new JMenuBar();
            JMenu jMenu = new JMenu("Upgrade");
            jMenuBar.add(jMenu);
            JMenuItem jMenuItem = new JMenuItem(C1818g.b("Enable SD File Browsing"));
            jMenu.add(jMenuItem);
            jMenuItem.addActionListener(new e(this));
            add("North", jMenuBar);
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("North", this.f8184b);
        this.f8188f.add("West", jPanel);
        f fVar = new f(this);
        if (zA) {
            bD.r rVar = new bD.r(this.f8185c, fVar);
            rVar.a(new g(this));
            rVar.setEnabled(zA);
            this.f8188f.add(BorderLayout.CENTER, rVar);
            add(BorderLayout.CENTER, this.f8188f);
        } else {
            C0963i c0963i = new C0963i(this.f8185c, fVar);
            c0963i.setEnabled(zA);
            this.f8188f.add(BorderLayout.CENTER, c0963i);
            add(BorderLayout.CENTER, this.f8188f);
            this.f8186d = "Please Register to Enable MS3 SD Browsing and Download";
            Dimension preferredSize = this.f8188f.getPreferredSize();
            Dimension dimension = new Dimension(preferredSize.width, preferredSize.height + 50);
            this.f8188f.setPreferredSize(dimension);
            this.f8188f.setMinimumSize(dimension);
        }
        C1894b.a(true);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        C1894b.a(false);
        this.f8184b.close();
        this.f8185c.b();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return this.f8188f.getPreferredSize();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return this.f8188f.getMinimumSize();
    }
}
