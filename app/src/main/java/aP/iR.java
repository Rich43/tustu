package aP;

import G.C0132o;
import G.InterfaceC0131n;
import com.efiAnalytics.ui.C1658ep;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.SoftBevelBorder;

/* loaded from: TunerStudioMS.jar:aP/iR.class */
public class iR extends JPanel implements G.S, InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    JPanel f3678a = new JPanel();

    /* renamed from: b, reason: collision with root package name */
    com.efiAnalytics.ui.dM f3679b = new com.efiAnalytics.ui.dM();

    /* renamed from: c, reason: collision with root package name */
    iU f3680c = new iU(this);

    /* renamed from: d, reason: collision with root package name */
    iU f3681d = new iU(this);

    /* renamed from: e, reason: collision with root package name */
    Insets f3682e = new Insets(0, 1, 1, 20);

    public iR(boolean z2) {
        setLayout(new BorderLayout());
        this.f3678a.setLayout(new GridLayout(1, 0, 0, 2));
        SoftBevelBorder softBevelBorder = new SoftBevelBorder(1);
        this.f3678a.setBorder(softBevelBorder);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add("East", this.f3678a);
        jPanel.add(BorderLayout.CENTER, this.f3681d);
        if (z2) {
            C0431im c0431im = new C0431im();
            c0431im.setBorder(softBevelBorder);
            jPanel.add("West", c0431im);
        }
        this.f3681d.setBorder(softBevelBorder);
        add("East", jPanel);
        add(BorderLayout.CENTER, this.f3679b);
        this.f3680c.setBorder(new SoftBevelBorder(1));
        add("West", this.f3680c);
        cZ.a().a(this.f3679b);
        cZ.a().a(this);
        setOpaque(true);
    }

    public void a(C1658ep c1658ep) {
        this.f3678a.add(c1658ep);
        doLayout();
        this.f3678a.doLayout();
    }

    public void a(String str) {
        for (int i2 = 0; i2 < this.f3678a.getComponentCount(); i2++) {
            C1658ep c1658ep = (C1658ep) this.f3678a.getComponent(i2);
            if (c1658ep.b().equals(str)) {
                this.f3678a.remove(c1658ep);
                c1658ep.a();
            }
        }
    }

    public void b(String str) {
        String strSubstring = str;
        if (str != null && str.indexOf(File.separator) >= 0) {
            strSubstring = str.substring(str.lastIndexOf(File.separator) + 1, str.length());
        }
        this.f3681d.setText(strSubstring);
        this.f3681d.setToolTipText(str);
    }

    public String a() {
        return this.f3681d.getText();
    }

    public void b() {
        String text = this.f3681d.getText();
        if (text.isEmpty() || text.startsWith("Modified - ")) {
            return;
        }
        this.f3681d.setText("Modified - " + this.f3681d.getText());
    }

    public void c(String str) {
        if (SwingUtilities.isEventDispatchThread()) {
            this.f3680c.setText(str);
        } else {
            SwingUtilities.invokeLater(new iS(this, str));
        }
    }

    @Override // G.S
    public void a(G.R r2) {
    }

    @Override // G.S
    public void b(G.R r2) {
        a(r2.c());
        r2.b(this);
        r2.d(this);
    }

    @Override // G.S
    public void c(G.R r2) {
        iT iTVar = new iT(this, r2, this);
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                iTVar.run();
            } else {
                SwingUtilities.invokeAndWait(iTVar);
            }
        } catch (InterruptedException e2) {
            Logger.getLogger(iR.class.getName()).log(Level.SEVERE, "Error Initializing Status Bar", (Throwable) e2);
        } catch (InvocationTargetException e3) {
            Logger.getLogger(iR.class.getName()).log(Level.SEVERE, "Error Initializing Status Bar", (Throwable) e3);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(getBackground().darker());
        int width = getWidth() - 4;
        graphics.drawOval(width, 8, 1, 1);
        graphics.drawOval(width - 4, 8 + 4, 1, 1);
        graphics.drawOval(width - 8, 8 + 8, 1, 1);
        graphics.drawOval(width, 8 + 4, 1, 1);
        graphics.drawOval(width - 4, 8 + 8, 1, 1);
        graphics.drawOval(width, 8 + 8, 1, 1);
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public Insets getInsets() {
        return this.f3682e;
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }
}
