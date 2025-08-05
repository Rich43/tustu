package ar;

import ao.C0645bi;
import bH.C;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: ar.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ar/g.class */
public class C0840g extends JTabbedPane implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    int f6232a;

    /* renamed from: b, reason: collision with root package name */
    Icon f6233b;

    /* renamed from: c, reason: collision with root package name */
    Icon f6234c;

    /* renamed from: d, reason: collision with root package name */
    boolean f6235d;

    /* renamed from: e, reason: collision with root package name */
    boolean f6236e;

    public C0840g() {
        super(1, 1);
        this.f6232a = -1;
        this.f6235d = true;
        this.f6236e = false;
        try {
            this.f6233b = new ImageIcon(cO.a().a(cO.f11111A, this, 12));
            this.f6234c = new ImageIcon(cO.a().a(cO.f11116F, this, 16));
        } catch (V.a e2) {
            Logger.getLogger(C0840g.class.getName()).log(Level.WARNING, "Failed to create Dirty Icon", (Throwable) e2);
        }
        a();
        super.setSelectedIndex(0);
        addMouseListener(new C0844k(this));
        super.addChangeListener(this);
        C0839f.a().a(new C0843j(this));
    }

    private void a() {
        for (C0836c c0836c : C0839f.a().b()) {
            super.addTab(c0836c.b(), null, c(), c0836c.a());
        }
        if (this.f6235d) {
            addTab("", this.f6234c, c(), "New Quick View");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        String strTrim;
        boolean z2 = false;
        do {
            String strA = bV.a((Component) this, false, "New Quick View Name", "");
            if (strA == null || strA.trim().isEmpty()) {
                return;
            }
            strTrim = strA.trim();
            if (C0839f.a().c(strTrim) != null) {
                bV.d("A Quick View with that name already exists, the name must be unique.", this);
            } else {
                z2 = true;
            }
        } while (!z2);
        if (C0645bi.a().c().j()) {
            String strG = C0839f.a().g();
            C.c("Current Log View: " + strG);
            if (strG != null && !strG.isEmpty()) {
                C0836c c0836cC = C0839f.a().c(strG);
                C0836c c0836cB = C0645bi.a().c().B();
                if (C0645bi.a().c().j()) {
                    C.c("View Changed: " + C0645bi.a().c().j());
                    c0836cB.a(c0836cC.b());
                    C0839f.a().a(c0836cB);
                }
            }
        }
        C0839f.a().d(strTrim);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JPanel c() {
        JPanel jPanel = new JPanel();
        jPanel.setMinimumSize(new Dimension(10, 1));
        jPanel.setPreferredSize(new Dimension(10, 1));
        jPanel.setMaximumSize(new Dimension(10, 1));
        return jPanel;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        SwingUtilities.invokeLater(new RunnableC0841h(this));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return super.getMinimumSize();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    public int a(String str) {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (str.equals(getTitleAt(i2))) {
                return i2;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) {
        C0836c c0836cC = C0839f.a().c(str);
        if (c0836cC.f() || bV.a("Are you sure you want to delete " + str + "?", (Component) this, true)) {
            C0839f.a().b(str);
            C0839f.a().c(c0836cC);
        }
    }

    protected void a(MouseEvent mouseEvent) {
        String titleAt = null;
        int i2 = 0;
        while (true) {
            if (i2 >= getTabCount()) {
                break;
            }
            if (super.getBoundsAt(i2).contains(mouseEvent.getPoint())) {
                titleAt = getTitleAt(i2);
                if (i2 != getSelectedIndex()) {
                    C0839f.a().a(titleAt);
                }
            } else {
                i2++;
            }
        }
        if (titleAt == null || titleAt.isEmpty()) {
            return;
        }
        JPopupMenu jPopupMenu = new JPopupMenu();
        C0836c c0836cC = C0839f.a().c(titleAt);
        JMenuItem jMenuItem = new JMenuItem((c0836cC == null || !c0836cC.f()) ? "Delete Quick View" : "Restore Default Fields");
        jMenuItem.setActionCommand(titleAt);
        jMenuItem.addActionListener(new C0842i(this));
        jPopupMenu.add(jMenuItem);
        jPopupMenu.show(this, mouseEvent.getX(), mouseEvent.getY());
    }
}
