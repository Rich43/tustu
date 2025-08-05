package ao;

import W.C0184j;
import W.C0188n;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import g.C1733k;
import h.C1737b;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import org.icepdf.core.util.PdfOps;

/* renamed from: ao.ak, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ak.class */
public class C0620ak extends JComponent implements InterfaceC0640bd {

    /* renamed from: a, reason: collision with root package name */
    C0184j f5200a;

    /* renamed from: b, reason: collision with root package name */
    String f5201b;

    /* renamed from: c, reason: collision with root package name */
    String f5202c;

    /* renamed from: d, reason: collision with root package name */
    int f5203d;

    /* renamed from: e, reason: collision with root package name */
    int f5204e;

    /* renamed from: f, reason: collision with root package name */
    int f5205f;

    /* renamed from: g, reason: collision with root package name */
    double f5206g;

    /* renamed from: h, reason: collision with root package name */
    double f5207h;

    /* renamed from: i, reason: collision with root package name */
    double f5208i;

    /* renamed from: j, reason: collision with root package name */
    FontMetrics f5209j;

    /* renamed from: p, reason: collision with root package name */
    private boolean f5210p;

    /* renamed from: q, reason: collision with root package name */
    private boolean f5211q;

    /* renamed from: r, reason: collision with root package name */
    private boolean f5212r;

    /* renamed from: k, reason: collision with root package name */
    Color f5213k;

    /* renamed from: l, reason: collision with root package name */
    Color f5214l;

    /* renamed from: m, reason: collision with root package name */
    Image f5215m;

    /* renamed from: n, reason: collision with root package name */
    Image f5216n;

    /* renamed from: s, reason: collision with root package name */
    private C0583A f5217s;

    /* renamed from: t, reason: collision with root package name */
    private Color f5218t;

    /* renamed from: u, reason: collision with root package name */
    private int f5219u;

    /* renamed from: v, reason: collision with root package name */
    private boolean f5220v;

    /* renamed from: o, reason: collision with root package name */
    int f5221o;

    /* renamed from: w, reason: collision with root package name */
    private boolean f5222w;

    public C0620ak() {
        this.f5200a = null;
        this.f5201b = "";
        this.f5202c = "";
        this.f5203d = 0;
        this.f5204e = 0;
        this.f5205f = 0;
        this.f5206g = 0.0d;
        this.f5207h = 0.0d;
        this.f5208i = 0.0d;
        this.f5209j = null;
        this.f5210p = false;
        this.f5211q = true;
        this.f5212r = false;
        this.f5213k = Color.lightGray;
        this.f5214l = null;
        this.f5215m = null;
        this.f5216n = null;
        this.f5217s = null;
        this.f5218t = null;
        this.f5219u = 0;
        this.f5220v = false;
        this.f5221o = -1;
        this.f5222w = false;
        addMouseListener(new C0624ao(this));
        a();
    }

    public void a() {
        Font font = UIManager.getFont("Label.font");
        int iA = com.efiAnalytics.ui.eJ.a(11);
        if (font != null) {
            iA = Math.round(font.getSize2D() * (iA / com.efiAnalytics.ui.eJ.a()));
        }
        setFont(new Font("SansSerif", 1, iA));
    }

    public C0620ak(C0184j c0184j) {
        this();
        a(c0184j);
    }

    public void a(int i2) {
        this.f5203d = i2;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        a(graphics);
    }

    private int f() {
        if (this.f5221o < 0) {
            this.f5221o = com.efiAnalytics.ui.eJ.a(8);
        }
        return this.f5221o;
    }

    public void a(Graphics graphics) {
        if (this.f5209j == null) {
            this.f5209j = getFontMetrics(getFont());
        }
        if (this.f5214l == null) {
            this.f5214l = getBackground();
        }
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String strH = h();
        graphics.setColor(this.f5214l);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(getForeground());
        graphics.setFont(getFont());
        int width = (getWidth() - this.f5209j.stringWidth(strH)) - f();
        int width2 = ((getWidth() - this.f5209j.stringWidth(this.f5202c)) - this.f5209j.stringWidth(": 00.000")) - f();
        this.f5204e = width < width2 ? width : width2;
        this.f5205f = (getHeight() - ((getHeight() - getFont().getSize()) / 2)) - 2;
        if (this.f5204e < g()) {
            this.f5204e = g();
        }
        graphics.drawString(strH, this.f5204e, this.f5205f);
        if (this.f5217s != null) {
            b(graphics);
        } else if (e()) {
            a(graphics, this.f5208i, this.f5206g, this.f5207h);
        }
        graphics.setColor(this.f5213k);
        graphics.draw3DRect(0, 0, getWidth(), getHeight(), false);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void update(Graphics graphics) {
        if (!isValid()) {
            paint(graphics);
            validate();
            return;
        }
        graphics.setColor(this.f5214l);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(getForeground());
        graphics.setFont(getFont());
        graphics.drawString(h(), this.f5204e, this.f5205f);
        if (e()) {
            a(graphics, this.f5208i, this.f5206g, this.f5207h);
        }
        graphics.setColor(this.f5213k);
        graphics.draw3DRect(0, 0, getWidth(), getHeight(), false);
    }

    private int g() {
        if (e() && this.f5217s == null) {
            return i() + 2 + 5;
        }
        if (this.f5217s != null) {
            return getHeight() + 2;
        }
        return 2;
    }

    private String h() {
        String str = this.f5202c;
        if (this.f5200a != null && this.f5200a.l()) {
            str = str + h.i.d();
        }
        if (this.f5200a != null && this.f5200a.n() != null && !this.f5200a.n().isEmpty() && !this.f5200a.n().equals("bit")) {
            str = str + "(" + this.f5200a.n() + ")";
        }
        return str + ": " + this.f5201b;
    }

    public void a(C0184j c0184j) {
        this.f5200a = c0184j;
        this.f5202c = c0184j.a();
    }

    public C0184j b() {
        return this.f5200a;
    }

    public void a(String str) {
        this.f5202c = str;
    }

    public void b(String str) {
        this.f5201b = str;
        repaint(1000L);
    }

    public String c() {
        return this.f5201b;
    }

    @Override // javax.swing.JComponent
    public String getToolTipText(MouseEvent mouseEvent) {
        return super.getToolTipText();
    }

    public void b(int i2) {
        try {
            this.f5208i = this.f5200a.c(i2);
            String strA = this.f5200a.a(i2);
            if (strA == null || c() == null || !strA.equals(c())) {
                this.f5206g = this.f5203d == 0 ? this.f5200a.h() : 0.0d;
                this.f5207h = this.f5203d == 0 ? this.f5200a.g() : 1.0d;
                if (this.f5203d != 0) {
                    int i3 = (int) this.f5208i;
                    strA = (i3 & this.f5203d) == this.f5203d ? Constants._TAG_Y : "N";
                    this.f5208i = (i3 & this.f5203d) == this.f5203d ? 1.0d : 0.0d;
                }
                if (d()) {
                    this.f5214l = k();
                }
                C0188n c0188nS = C0804hg.a().s();
                if (c0188nS != null && c0188nS.a(this.f5200a.a()) != null && i2 - this.f5219u > 0 && i2 - this.f5219u < c0188nS.d()) {
                    strA = strA + " : " + c0188nS.a(this.f5200a.a()).a(i2 - this.f5219u);
                }
                b(strA);
                String strN = this.f5200a.n();
                if (strN != null && !strN.isEmpty()) {
                    setToolTipText(strA + " (" + strN + ")");
                }
            }
        } catch (Exception e2) {
            b("##");
            System.out.println("Gauge Exception Caught:\n" + ((Object) e2));
        }
    }

    private void a(Graphics graphics, double d2, double d3, double d4) {
        int i2 = i();
        int iA = com.efiAnalytics.ui.eJ.a(2);
        graphics.drawImage(j(), iA + 1, iA + 1, null);
        graphics.setColor(Color.black);
        graphics.fillArc(iA + 1, iA + 1, i2, i2, 180 - ((int) (180.0d * ((d2 - d3) / (d4 - d3)))), 10);
    }

    private int i() {
        int height = (getHeight() - com.efiAnalytics.ui.eJ.a(4)) * 2;
        int width = height < getWidth() / 4 ? height : getWidth() / 4;
        if (width > 0) {
            return width;
        }
        return 1;
    }

    public void c(int i2) {
        this.f5219u = i2;
        repaint();
    }

    public void a(boolean z2) {
        this.f5220v = z2;
    }

    private Image j() {
        if (this.f5216n != null && i() == this.f5216n.getHeight(null)) {
            return this.f5216n;
        }
        int i2 = i();
        this.f5216n = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(i2 + 2, i2, 3);
        Graphics graphics = this.f5216n.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.red);
        graphics.fillArc(0, 0, i2, i2, 0, 35);
        graphics.setColor(Color.orange);
        graphics.fillArc(0, 0, i2, i2, 35, 20);
        graphics.setColor(Color.yellow);
        graphics.fillArc(0, 0, i2, i2, 55, 70);
        graphics.setColor(new Color(164, 255, 0));
        graphics.fillArc(0, 0, i2, i2, 125, 20);
        graphics.setColor(Color.green);
        graphics.fillArc(0, 0, i2, i2, 145, 35);
        graphics.setColor(Color.black);
        graphics.drawArc(0, 0, i2, i2, 0, 180);
        graphics.drawLine(0, i2 / 2, i2, i2 / 2);
        return this.f5216n;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        int size = getFont().getSize() + com.efiAnalytics.ui.eJ.a(6);
        return new Dimension(e() ? getFontMetrics(getFont()).stringWidth(h()) + (2 * size) : getFontMetrics(getFont()).stringWidth(h()), size);
    }

    public int d(int i2) {
        int iStringWidth;
        if (this.f5217s != null) {
            int height = (3 * getHeight()) / 4;
            iStringWidth = getFontMetrics(getFont()).stringWidth(h()) + height + com.efiAnalytics.ui.eJ.a(1) + (getHeight() - height);
        } else {
            iStringWidth = e() ? getFontMetrics(getFont()).stringWidth(h()) + (2 * i2) : getFontMetrics(getFont()).stringWidth(h());
        }
        return iStringWidth;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public boolean d() {
        return this.f5210p;
    }

    private Color k() {
        return this.f5222w ? this.f5208i == ((double) this.f5200a.h()) ? UIManager.getColor("Label.background") : this.f5208i > this.f5207h ? C1733k.a(this.f5207h, this.f5207h, this.f5206g) : C1733k.a(this.f5208i, this.f5207h, this.f5206g) : C1733k.a(this.f5208i, this.f5206g, this.f5207h);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        if (d()) {
            super.setBackground(k());
        } else {
            this.f5214l = color;
            super.setBackground(color);
        }
    }

    @Override // java.awt.Component
    public Color getForeground() {
        return (!d() || this.f5208i == ((double) this.f5200a.h()) || this.f5200a.h() == this.f5200a.g()) ? UIManager.getColor("Label.foreground") : Color.black;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setForeground(Color color) {
        super.setForeground(color);
    }

    public void b(boolean z2) {
        this.f5210p = z2;
        setBackground(getBackground());
        repaint();
    }

    public boolean e() {
        return this.f5211q;
    }

    public void c(boolean z2) {
        this.f5211q = z2;
        invalidate();
        repaint();
    }

    @Override // java.awt.Component
    public void setSize(int i2, int i3) {
        super.setSize(i2, i3);
        this.f5216n = null;
    }

    public void a(C0583A c0583a) {
        this.f5217s = c0583a;
    }

    @Override // ao.InterfaceC0640bd
    public void a(String str, String str2, int i2) {
        if (str2.equals(this.f5202c)) {
            this.f5218t = null;
            repaint();
        }
    }

    @Override // ao.InterfaceC0640bd
    public void a(String str, C0184j c0184j, int i2) {
        Color color = this.f5218t;
        if (c0184j == null) {
            this.f5218t = null;
            repaint();
        } else if (c0184j != null && c0184j.a().equals(this.f5202c)) {
            this.f5218t = aO.a().a(Integer.parseInt(str.substring(str.indexOf(PdfOps.h_TOKEN) + 1)), i2);
            repaint();
        }
        if (((color == null) ^ (this.f5218t == null)) || color == null || !color.equals(this.f5218t)) {
            repaint();
        }
    }

    @Override // ao.InterfaceC0640bd
    public void d(boolean z2) {
    }

    private void b(Graphics graphics) {
        if (this.f5218t == null) {
            Color background = getBackground();
            if (background.getRed() + background.getBlue() + background.getGreen() < 38) {
                graphics.setColor(Color.DARK_GRAY.darker());
            } else {
                graphics.setColor(Color.GRAY);
            }
        } else {
            graphics.setColor(this.f5218t);
        }
        int height = (3 * getHeight()) / 4;
        int iA = com.efiAnalytics.ui.eJ.a(1) + ((getHeight() - height) / 2);
        graphics.fill3DRect(iA, iA, height, height, this.f5218t != null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
        if (this.f5217s != null) {
            if (this.f5218t == null) {
                this.f5218t = this.f5217s.a(this.f5202c);
                if (this.f5218t == null) {
                    com.efiAnalytics.ui.bV.d("All graph traces are in use, to add a new trace you must:\n - Turn off 1 or more current traces.\n - Increase the Maximum number of graphs.\n - Increase the Traces Per Graph.", this);
                }
            } else {
                this.f5217s.b(this.f5202c);
                this.f5218t = null;
            }
            repaint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2, int i3) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        a(jPopupMenu);
        add(jPopupMenu);
        jPopupMenu.show(this, i2, i3);
    }

    private void a(JPopupMenu jPopupMenu) {
        if (this.f5217s != null) {
            int iA = h.i.a("numberOfGraphs", h.i.f12273t);
            int iA2 = h.i.a("numberOfOverlays", h.i.f12274u);
            ArrayList arrayListC = aO.a().c();
            C0621al c0621al = new C0621al(this);
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < iA; i2++) {
                JMenu jMenu = new JMenu("Send to Graph " + (i2 + 1));
                arrayList.clear();
                for (int i3 = 0; i3 < arrayListC.size() && i3 < iA2; i3++) {
                    Color color = (Color) arrayListC.get(i3);
                    if (!arrayList.contains(color)) {
                        C0623an c0623an = new C0623an(this, color, i2);
                        c0623an.setActionCommand("graph" + i2 + "." + i3);
                        c0623an.addActionListener(c0621al);
                        jMenu.add((JMenuItem) c0623an);
                        arrayList.add(color);
                    }
                }
                jPopupMenu.add((JMenuItem) jMenu);
            }
            jPopupMenu.addSeparator();
        }
        JMenuItem jMenuItem = new JMenuItem("Select Displayed Fields");
        jMenuItem.addActionListener(new C0622am(this));
        jMenuItem.setEnabled(C1737b.a().a("selectableFields"));
        jPopupMenu.add(jMenuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(C0623an c0623an) {
        if (this.f5217s != null) {
            if (this.f5218t != null) {
                this.f5217s.b(this.f5202c);
            }
            String actionCommand = c0623an.getActionCommand();
            try {
                this.f5218t = this.f5217s.a(this.f5202c, null, c0623an.b(), Integer.parseInt(actionCommand.substring(actionCommand.lastIndexOf(".") + 1)));
            } catch (Exception e2) {
                bH.C.a("Failed to set Graph with color");
                this.f5218t = this.f5217s.a(this.f5202c, c0623an.a(), c0623an.b());
            }
        }
    }

    public void e(boolean z2) {
        this.f5222w = z2;
    }
}
