package ao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.rtf.RTFGenerator;

/* renamed from: ao.ai, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ai.class */
public class C0618ai extends JComponent {

    /* renamed from: a, reason: collision with root package name */
    String f5176a;

    /* renamed from: b, reason: collision with root package name */
    String f5177b;

    /* renamed from: c, reason: collision with root package name */
    String f5178c;

    /* renamed from: d, reason: collision with root package name */
    Point f5179d;

    /* renamed from: e, reason: collision with root package name */
    Color f5180e;

    /* renamed from: f, reason: collision with root package name */
    Color f5181f;

    /* renamed from: g, reason: collision with root package name */
    Color f5182g;

    /* renamed from: h, reason: collision with root package name */
    Color f5183h;

    /* renamed from: i, reason: collision with root package name */
    boolean f5184i;

    /* renamed from: j, reason: collision with root package name */
    boolean f5185j;

    /* renamed from: k, reason: collision with root package name */
    boolean f5186k;

    /* renamed from: l, reason: collision with root package name */
    boolean f5187l;

    /* renamed from: m, reason: collision with root package name */
    boolean f5188m;

    /* renamed from: n, reason: collision with root package name */
    Vector f5189n;

    /* renamed from: o, reason: collision with root package name */
    Image f5190o;

    /* renamed from: p, reason: collision with root package name */
    Image f5191p;

    /* renamed from: q, reason: collision with root package name */
    Dimension f5192q;

    /* renamed from: r, reason: collision with root package name */
    Dimension f5193r;

    /* renamed from: s, reason: collision with root package name */
    Dimension f5194s;

    /* renamed from: t, reason: collision with root package name */
    int f5195t;

    /* renamed from: u, reason: collision with root package name */
    int f5196u;

    /* renamed from: v, reason: collision with root package name */
    int f5197v;

    /* renamed from: w, reason: collision with root package name */
    boolean f5198w;

    public C0618ai(String str) {
        this.f5176a = "";
        this.f5177b = null;
        this.f5178c = null;
        this.f5179d = null;
        this.f5181f = Color.yellow;
        this.f5183h = null;
        this.f5184i = false;
        this.f5185j = false;
        this.f5186k = false;
        this.f5187l = true;
        this.f5188m = false;
        this.f5189n = null;
        this.f5192q = null;
        this.f5193r = null;
        this.f5194s = null;
        this.f5195t = 4;
        this.f5196u = 0;
        this.f5197v = 6;
        this.f5198w = true;
        this.f5176a = str;
        setFont(new Font(RTFGenerator.defaultFontFamily, 0, 10));
        enableEvents(16L);
        this.f5189n = new Vector();
        super.setOpaque(true);
        repaint();
    }

    public C0618ai(String str, Image image) {
        this(str);
        this.f5190o = image;
    }

    public C0618ai(String str, Image image, Dimension dimension) {
        this(str, image);
        if (dimension != null) {
            this.f5192q = dimension;
        }
    }

    public C0618ai(String str, Image image, Dimension dimension, Dimension dimension2) {
        this(str, image, dimension);
        this.f5194s = dimension2;
    }

    public void a(String str) {
        this.f5176a = str;
        this.f5196u = c();
        repaint();
    }

    public String a() {
        return this.f5176a;
    }

    @Override // javax.swing.JComponent
    public void setToolTipText(String str) {
        super.setToolTipText(str);
        this.f5177b = str;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        this.f5182g = color;
        this.f5180e = color.darker();
        super.setBackground(color);
        repaint();
    }

    private Image b() {
        if (getParent().getSize().width <= 0 || getParent().getSize().height <= 0 || getSize().height <= 0) {
            return null;
        }
        Image imageCreateImage = createImage(getSize().width, getSize().height);
        imageCreateImage.getGraphics();
        return imageCreateImage;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.f5196u = c();
        this.f5182g = getBackground();
        graphics.setFont(getFont());
        int height = 0;
        if (this.f5190o != null) {
            if (this.f5176a != null) {
                height = this.f5194s == null ? ((getSize().height - this.f5190o.getHeight(null)) - getFontMetrics(getFont()).getHeight()) / 2 : ((getSize().height - this.f5194s.height) - getFontMetrics(getFont()).getHeight()) / 2;
            } else {
                height = this.f5194s == null ? (getSize().height - this.f5190o.getHeight(null)) / 2 : (getSize().height - this.f5194s.height) / 2;
            }
        }
        int height2 = this.f5190o != null ? getSize().height - (getFontMetrics(getFont()).getHeight() / 3) : ((getSize().height + (getFontMetrics(getFont()).getHeight() / 2)) / 2) + 1;
        graphics.setColor(this.f5182g);
        graphics.fillRect(0, 0, getSize().width, getSize().height);
        if (this.f5184i) {
            graphics.setColor(Color.lightGray);
            graphics.draw3DRect(0, 0, getSize().width, getSize().height, false);
            graphics.draw3DRect(1, 1, getSize().width - 2, getSize().height - 2, false);
            graphics.setColor(this.f5182g.darker());
            if (this.f5190o != null) {
                if (this.f5194s == null) {
                    graphics.drawImage(this.f5190o, ((getSize().width - this.f5190o.getWidth(null)) / 2) + 1, height + 1, this);
                } else {
                    graphics.drawImage(this.f5190o, ((getSize().width - this.f5194s.width) / 2) + 1, height + 1, this.f5194s.width, this.f5194s.height, this);
                }
            }
            graphics.setColor(UIManager.getColor("Button.foreground"));
            if (this.f5176a != null) {
                graphics.drawString(this.f5176a, this.f5196u, height2);
            }
            if (this.f5183h != null) {
                a(graphics, 0);
                return;
            }
            return;
        }
        if (!this.f5185j) {
            graphics.setColor(this.f5182g);
            if (this.f5190o != null) {
                if (this.f5194s == null) {
                    graphics.drawImage(this.f5190o, ((getSize().width - this.f5190o.getWidth(null)) / 2) + 1, height + 1, this);
                } else {
                    graphics.drawImage(this.f5190o, ((getSize().width - this.f5194s.width) / 2) + 1, height + 1, this.f5194s.width, this.f5194s.height, this);
                }
            }
            graphics.setColor(UIManager.getColor("Button.foreground"));
            if (this.f5176a != null) {
                graphics.drawString(this.f5176a, this.f5196u, height2);
            }
            if (this.f5183h != null) {
                a(graphics, 1);
                return;
            }
            return;
        }
        graphics.setColor(Color.lightGray);
        graphics.draw3DRect(0, 0, getSize().width, getSize().height, true);
        graphics.setColor(this.f5182g);
        graphics.draw3DRect(0, 0, getSize().width - 1, getSize().height - 1, true);
        graphics.setColor(getForeground());
        graphics.setColor(UIManager.getColor("Button.foreground"));
        if (this.f5176a != null) {
            graphics.drawString(this.f5176a, this.f5196u - 1, height2 - 1);
        }
        if (this.f5190o != null) {
            if (this.f5194s == null) {
                graphics.drawImage(this.f5190o, (getSize().width - this.f5190o.getWidth(null)) / 2, height, this);
            } else {
                graphics.drawImage(this.f5190o, (getSize().width - this.f5194s.width) / 2, height, this.f5194s.width, this.f5194s.height, this);
            }
        }
        if (this.f5183h != null) {
            a(graphics, -1);
        }
    }

    private void a(Graphics graphics, int i2) {
        graphics.setColor(this.f5183h);
        graphics.fillRect(((getWidth() - this.f5197v) - 2) + i2, ((getHeight() - this.f5197v) / 2) + i2, this.f5197v, this.f5197v);
    }

    public void a(ActionListener actionListener) {
        this.f5189n.addElement(actionListener);
    }

    protected void a(ActionEvent actionEvent) {
        Enumeration enumerationElements = this.f5189n.elements();
        while (enumerationElements.hasMoreElements()) {
            ActionListener actionListener = (ActionListener) enumerationElements.nextElement2();
            if (actionListener != null) {
                actionListener.actionPerformed(actionEvent);
            } else {
                System.out.println("FlatButton:: Can not call actionPerformed(ActionEvent) on null Listener");
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    protected void processMouseEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getID() == 501 && (mouseEvent.getModifiers() | 4) != mouseEvent.getModifiers()) {
            this.f5184i = true;
            repaint(0L);
        }
        if (mouseEvent.getID() == 504) {
            this.f5185j = true;
            repaint(0L);
        }
        if (mouseEvent.getID() == 502) {
            if (this.f5185j && this.f5184i) {
                a(new ActionEvent(this, 1001, (this.f5176a == null || this.f5176a.length() < 1) ? this.f5177b : this.f5176a, mouseEvent.getModifiers()));
            }
            this.f5184i = false;
            repaint(0L);
        }
        if (mouseEvent.getID() == 505) {
            this.f5185j = false;
            repaint();
        }
        super.processMouseEvent(mouseEvent);
    }

    public void a(Dimension dimension) {
        this.f5192q = dimension;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        this.f5186k = true;
        super.setFont(font);
        if (getParent() != null) {
            getParent().invalidate();
        }
    }

    private int c() {
        if (this.f5176a == null) {
            return 0;
        }
        if (this.f5198w) {
            return (getSize().width - getFontMetrics(getFont()).stringWidth(this.f5176a)) / 2;
        }
        return 4;
    }

    @Override // java.awt.Container, java.awt.Component
    public void validate() {
        if (this.f5188m && !isValid() && this.f5191p != null) {
            this.f5191p = b();
        }
        super.validate();
        if (!this.f5186k) {
            setFont(getParent().getFont());
        }
        this.f5196u = c();
        repaint();
        if (this.f5178c == null || this.f5190o != null) {
            return;
        }
        this.f5190o = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/" + this.f5178c));
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
        if (!this.f5188m || this.f5191p == null || this.f5193r == null) {
            return;
        }
        if (this.f5193r.width < getLocation().f12370x + getSize().width || this.f5193r.height < getLocation().f12371y + getSize().height) {
            this.f5191p = b();
        }
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        if (i2 == 32) {
            if (this.f5192q == null) {
                SwingUtilities.invokeLater(new RunnableC0619aj(this));
            }
            repaint();
        }
        return super.imageUpdate(image, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        if (this.f5192q != null) {
            return this.f5192q;
        }
        int height = 5;
        int width = 7;
        if (this.f5176a != null && !this.f5176a.isEmpty()) {
            FontMetrics fontMetrics = getFontMetrics(getFont());
            if (this.f5176a != null) {
                height = 5 + fontMetrics.getHeight();
                width = 7 + fontMetrics.stringWidth(this.f5176a);
            }
        }
        if (this.f5190o != null) {
            if (this.f5194s == null) {
                height += this.f5190o.getHeight(this);
                width = width < this.f5190o.getWidth(this) + 7 ? this.f5190o.getWidth(this) + 5 : width;
            } else {
                height += this.f5194s.height;
                width = width < this.f5194s.width + 7 ? this.f5194s.width + 5 : width;
            }
        }
        if (this.f5183h != null) {
            width = this.f5197v;
        }
        return new Dimension(width, height);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}
