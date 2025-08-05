package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JComponent;

/* renamed from: com.efiAnalytics.ui.cp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cp.class */
public class C1605cp extends JComponent {

    /* renamed from: a, reason: collision with root package name */
    String f11270a;

    /* renamed from: b, reason: collision with root package name */
    String f11271b;

    /* renamed from: c, reason: collision with root package name */
    String f11272c;

    /* renamed from: d, reason: collision with root package name */
    Point f11273d;

    /* renamed from: e, reason: collision with root package name */
    Color f11274e;

    /* renamed from: f, reason: collision with root package name */
    Color f11275f;

    /* renamed from: g, reason: collision with root package name */
    Color f11276g;

    /* renamed from: h, reason: collision with root package name */
    Color f11277h;

    /* renamed from: i, reason: collision with root package name */
    boolean f11278i;

    /* renamed from: j, reason: collision with root package name */
    boolean f11279j;

    /* renamed from: k, reason: collision with root package name */
    boolean f11280k;

    /* renamed from: l, reason: collision with root package name */
    boolean f11281l;

    /* renamed from: m, reason: collision with root package name */
    boolean f11282m;

    /* renamed from: n, reason: collision with root package name */
    boolean f11283n;

    /* renamed from: o, reason: collision with root package name */
    Vector f11284o;

    /* renamed from: p, reason: collision with root package name */
    Image f11285p;

    /* renamed from: q, reason: collision with root package name */
    Image f11286q;

    /* renamed from: r, reason: collision with root package name */
    Dimension f11287r;

    /* renamed from: s, reason: collision with root package name */
    Dimension f11288s;

    /* renamed from: t, reason: collision with root package name */
    Dimension f11289t;

    /* renamed from: u, reason: collision with root package name */
    int f11290u;

    /* renamed from: v, reason: collision with root package name */
    int f11291v;

    /* renamed from: w, reason: collision with root package name */
    int f11292w;

    /* renamed from: x, reason: collision with root package name */
    int f11293x;

    /* renamed from: y, reason: collision with root package name */
    boolean f11294y;

    public C1605cp(String str) {
        this.f11270a = "";
        this.f11271b = null;
        this.f11272c = null;
        this.f11273d = null;
        this.f11275f = Color.yellow;
        this.f11277h = null;
        this.f11278i = false;
        this.f11279j = false;
        this.f11280k = false;
        this.f11281l = true;
        this.f11282m = false;
        this.f11283n = false;
        this.f11284o = null;
        this.f11287r = null;
        this.f11288s = null;
        this.f11289t = null;
        this.f11290u = 4;
        this.f11291v = 0;
        this.f11292w = 6;
        this.f11293x = 1;
        this.f11294y = true;
        this.f11270a = str;
        enableEvents(16L);
        this.f11284o = new Vector();
        repaint();
    }

    public C1605cp(String str, Image image) {
        this(str);
        this.f11271b = this.f11271b;
        this.f11285p = image;
    }

    public void a(int i2) {
        this.f11293x = i2;
        if (i2 == 4) {
            b(false);
        }
    }

    public C1605cp(String str, Image image, Dimension dimension) {
        this(str, image);
        if (dimension != null) {
            this.f11287r = dimension;
        }
    }

    public C1605cp(String str, Image image, Dimension dimension, Dimension dimension2) {
        this(str, image, dimension);
        this.f11289t = dimension2;
    }

    public void a(boolean z2) {
        this.f11283n = z2;
    }

    public void b(boolean z2) {
        this.f11294y = z2;
    }

    public void a(String str) {
        this.f11270a = str;
        this.f11291v = e();
        repaint();
    }

    public void a(Image image) {
        this.f11285p = image;
    }

    public String a() {
        return this.f11270a;
    }

    public void b(int i2) {
        this.f11292w = i2;
    }

    @Override // javax.swing.JComponent
    public void setToolTipText(String str) {
        this.f11271b = str;
    }

    @Override // javax.swing.JComponent
    public String getToolTipText() {
        return this.f11271b;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        this.f11276g = color;
        this.f11274e = color.darker();
        super.setBackground(color);
        repaint();
    }

    public void a(Color color) {
        this.f11277h = color;
    }

    public void a(Dimension dimension) {
        this.f11289t = dimension;
    }

    private Image b() {
        if (getParent().getSize().width <= 0 || getParent().getSize().height <= 0 || getSize().height <= 0) {
            return null;
        }
        this.f11288s = getParent().getSize();
        Image imageCreateImage = createImage(getParent().getSize().width, getParent().getSize().height);
        Image imageCreateImage2 = createImage(getSize().width, getSize().height);
        imageCreateImage2.getGraphics().drawImage(imageCreateImage, -getLocation().f12370x, -getLocation().f12371y, null);
        return imageCreateImage2;
    }

    private int c() {
        return this.f11289t == null ? this.f11285p.getHeight(null) : this.f11289t.height;
    }

    private int d() {
        return this.f11289t == null ? this.f11285p.getWidth(null) : this.f11289t.width;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        this.f11291v = e();
        this.f11276g = getBackground();
        int iC = 0;
        int iD = 0;
        if (this.f11285p != null) {
            iC = (this.f11270a == null || this.f11293x != 1) ? (getSize().height - c()) / 2 : ((getSize().height - c()) - getFontMetrics(getFont()).getHeight()) / 2;
            if (this.f11293x == 1) {
                iD = (getSize().width - d()) / 2;
            } else if (this.f11293x == 4) {
                iD = (getSize().width - d()) - 3;
            } else if (this.f11293x == 2) {
                iD = 3;
            }
        }
        int height = (this.f11285p == null || this.f11293x != 1) ? ((getSize().height + (getFontMetrics(getFont()).getHeight() / 2)) / 2) + 1 : getSize().height - (getFontMetrics(getFont()).getHeight() / 3);
        graphics.setColor(this.f11276g);
        graphics.fillRect(0, 0, getSize().width, getSize().height);
        if (this.f11286q == null) {
            this.f11286q = b();
        }
        if (this.f11282m && this.f11286q != null) {
            graphics.drawImage(this.f11286q, 0, 0, null);
        }
        if (isEnabled() && this.f11278i) {
            graphics.setColor(Color.lightGray);
            graphics.draw3DRect(0, 0, getSize().width, getSize().height, false);
            graphics.draw3DRect(1, 1, getSize().width - 2, getSize().height - 2, false);
            graphics.setColor(this.f11276g.darker());
            if (this.f11285p != null) {
                if (this.f11289t == null) {
                    graphics.drawImage(this.f11285p, iD + 1, iC + 1, this);
                } else {
                    graphics.drawImage(this.f11285p, iD + 1, iC + 1, this.f11289t.width, this.f11289t.height, this);
                }
            }
            graphics.setColor(getForeground());
            if (this.f11270a != null) {
                graphics.drawString(this.f11270a, this.f11291v, height);
            }
            if (this.f11277h != null) {
                a(graphics, 0);
                return;
            }
            return;
        }
        if (isEnabled() && this.f11279j) {
            graphics.setColor(Color.lightGray);
            graphics.draw3DRect(0, 0, getSize().width, getSize().height, true);
            graphics.setColor(this.f11276g);
            graphics.draw3DRect(0, 0, getSize().width - 1, getSize().height - 1, true);
            graphics.setColor(getForeground());
            if (this.f11270a != null) {
                graphics.drawString(this.f11270a, this.f11291v - 1, height - 1);
            }
            if (this.f11285p != null) {
                if (this.f11289t == null) {
                    graphics.drawImage(this.f11285p, iD, iC, this);
                } else {
                    graphics.drawImage(this.f11285p, iD, iC, this.f11289t.width, this.f11289t.height, this);
                }
            }
            if (this.f11277h != null) {
                a(graphics, -1);
                return;
            }
            return;
        }
        graphics.setColor(this.f11276g);
        if (this.f11285p != null) {
            if (this.f11289t == null) {
                graphics.drawImage(this.f11285p, iD + 1, iC + 1, this);
            } else {
                graphics.drawImage(this.f11285p, iD + 1, iC + 1, this.f11289t.width, this.f11289t.height, this);
            }
        }
        graphics.setColor(getForeground());
        if (this.f11270a != null) {
            graphics.drawString(this.f11270a, this.f11291v, height);
        }
        if (this.f11277h != null) {
            a(graphics, 1);
        }
        if (this.f11283n && isEnabled()) {
            graphics.setColor(Color.lightGray);
            graphics.draw3DRect(1, 1, getWidth() - 3, getHeight() - 3, false);
        }
        if (isEnabled()) {
            return;
        }
        graphics.setColor(new Color(128, 128, 128, 128));
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    private void a(Graphics graphics, int i2) {
        graphics.setColor(this.f11277h);
        graphics.fillRect(((getWidth() - this.f11292w) - 2) + i2, ((getHeight() - this.f11292w) / 2) + i2, this.f11292w, this.f11292w);
    }

    public void a(ActionListener actionListener) {
        this.f11284o.addElement(actionListener);
    }

    protected void a(ActionEvent actionEvent) {
        Enumeration enumerationElements = this.f11284o.elements();
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
        if (isEnabled()) {
            if (mouseEvent.getID() == 501 && (mouseEvent.getModifiers() | 4) != mouseEvent.getModifiers()) {
                this.f11278i = true;
                repaint(0L);
            }
            if (mouseEvent.getID() == 504) {
                this.f11279j = true;
                repaint(0L);
            }
            if (mouseEvent.getID() == 502) {
                if (this.f11279j && this.f11278i) {
                    a(new ActionEvent(this, 1001, (this.f11270a == null || this.f11270a.length() < 1) ? this.f11271b : this.f11270a, mouseEvent.getModifiers()));
                }
                this.f11278i = false;
                repaint(0L);
            }
            if (mouseEvent.getID() == 505) {
                this.f11279j = false;
                repaint();
            }
        }
        super.processMouseEvent(mouseEvent);
    }

    public void b(Dimension dimension) {
        this.f11287r = dimension;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        this.f11280k = true;
        super.setFont(font);
        if (getParent() != null) {
            getParent().invalidate();
        }
    }

    private int e() {
        if (this.f11270a == null || getParent() == null) {
            return 0;
        }
        if (this.f11294y) {
            return (getSize().width - getFontMetrics(getFont()).stringWidth(this.f11270a)) / 2;
        }
        return 5;
    }

    @Override // java.awt.Container, java.awt.Component
    public void validate() {
        if (this.f11282m && !isValid() && this.f11286q != null) {
            this.f11286q = b();
        }
        super.validate();
        if (!this.f11280k) {
            setFont(getParent().getFont());
        }
        this.f11291v = e();
        repaint();
        if (this.f11272c == null || this.f11285p != null) {
            return;
        }
        this.f11285p = Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/" + this.f11272c));
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
        if (!this.f11282m || this.f11286q == null || this.f11288s == null) {
            return;
        }
        if (this.f11288s.width < getLocation().f12370x + getSize().width || this.f11288s.height < getLocation().f12371y + getSize().height) {
            this.f11286q = b();
        }
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        if (i2 == 32) {
            if (this.f11287r == null) {
                getParent().invalidate();
                getParent().doLayout();
                try {
                    getParent().getParent().doLayout();
                } catch (Exception e2) {
                }
            }
            repaint();
        }
        return super.imageUpdate(image, i2, i3, i4, i5, i6);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        if (this.f11287r != null) {
            return this.f11287r;
        }
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int height = 5;
        int iStringWidth = 7;
        if (this.f11270a != null) {
            height = 5 + fontMetrics.getHeight();
            iStringWidth = 7 + fontMetrics.stringWidth(this.f11270a);
        }
        if (this.f11285p != null) {
            int height2 = this.f11289t == null ? this.f11285p.getHeight(null) : this.f11289t.height;
            int width = this.f11289t == null ? this.f11285p.getWidth(null) : this.f11289t.height;
            if (this.f11293x == 1) {
                height += height2;
                iStringWidth = iStringWidth < width + 7 ? width + 5 : iStringWidth;
            } else {
                height = height > height2 + 5 ? height : height2 + 5;
                iStringWidth = width + iStringWidth + 10;
            }
        }
        if (this.f11277h != null) {
            iStringWidth = this.f11292w;
        }
        return new Dimension(iStringWidth, height);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }
}
