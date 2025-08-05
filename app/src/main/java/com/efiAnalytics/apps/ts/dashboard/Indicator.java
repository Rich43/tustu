package com.efiAnalytics.apps.ts.dashboard;

import G.C0094c;
import G.cX;
import G.cY;
import G.cZ;
import com.efiAnalytics.apps.ts.dashboard.renderers.IndicatorPainter;
import com.efiAnalytics.apps.ts.dashboard.renderers.RectangleIndicatorPainter;
import com.efiAnalytics.ui.eJ;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/Indicator.class */
public class Indicator extends SingleChannelDashComponent implements ImageObserver, Serializable {

    /* renamed from: a, reason: collision with root package name */
    cX f9370a;

    /* renamed from: g, reason: collision with root package name */
    private cZ f9356g = new C0094c("On");

    /* renamed from: h, reason: collision with root package name */
    private cZ f9357h = new C0094c("Off");

    /* renamed from: i, reason: collision with root package name */
    private Color f9358i = Color.RED;

    /* renamed from: j, reason: collision with root package name */
    private Color f9359j = Color.LIGHT_GRAY;

    /* renamed from: k, reason: collision with root package name */
    private Color f9360k = Color.BLACK;

    /* renamed from: l, reason: collision with root package name */
    private Color f9361l = Color.BLACK;

    /* renamed from: m, reason: collision with root package name */
    private boolean f9362m = false;

    /* renamed from: n, reason: collision with root package name */
    private IndicatorPainter f9363n = null;

    /* renamed from: o, reason: collision with root package name */
    private Insets f9364o = new Insets(2, 2, 2, 2);

    /* renamed from: p, reason: collision with root package name */
    private aL f9365p = null;

    /* renamed from: q, reason: collision with root package name */
    private String f9366q = null;

    /* renamed from: r, reason: collision with root package name */
    private Image f9367r = null;

    /* renamed from: s, reason: collision with root package name */
    private String f9368s = null;

    /* renamed from: t, reason: collision with root package name */
    private Image f9369t = null;

    /* renamed from: b, reason: collision with root package name */
    String f9371b = null;

    /* renamed from: c, reason: collision with root package name */
    String f9372c = null;

    /* renamed from: d, reason: collision with root package name */
    String f9373d = null;

    /* renamed from: f, reason: collision with root package name */
    String f9374f = null;

    public Indicator() {
        this.f9370a = null;
        setPainter(new RectangleIndicatorPainter());
        setRelativeX(0.02d);
        setRelativeY(0.02d);
        setRelativeWidth(0.12d);
        setRelativeHeight(0.035d);
        this.f9370a = new aM(this);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void setRunDemo(boolean z2) {
        if (!z2) {
            if (this.f9365p != null) {
                this.f9365p.f9449a = false;
            }
        } else if (this.f9365p != null && this.f9365p.isAlive()) {
            this.f9365p.f9449a = true;
        } else {
            this.f9365p = new aL(this, true);
            this.f9365p.start();
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent
    public void setCurrentOutputChannelValue(String str, String str2) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent, G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (this.f9365p != null) {
            this.f9365p.f9449a = false;
        }
        setValue(d2);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void goDead() {
        if (this.f9365p != null && this.f9365p.isAlive()) {
            this.f9365p.f9449a = false;
        } else {
            this.f9365p = new aL(this, false);
            this.f9365p.start();
        }
    }

    public String getOnText() {
        return this.f9356g.toString();
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        if ((i2 & 32) != 32) {
            invalidate();
            setDirty(true);
        }
        return super.imageUpdate(image, i2, i3, i4, i5, i6);
    }

    public String onText() {
        try {
            String strA = this.f9356g.a();
            if (this.f9371b != null && strA.equals(this.f9371b)) {
                return this.f9373d;
            }
            this.f9371b = strA;
            this.f9373d = C1818g.b(strA);
            return this.f9373d;
        } catch (V.g e2) {
            return "Error";
        }
    }

    public void setOnText(String str) {
        try {
            this.f9356g = cY.a().a(this.f9370a, str);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public String getOffText() {
        return this.f9357h.toString();
    }

    public String offText() {
        try {
            String strA = this.f9357h.a();
            if (this.f9372c != null && strA.equals(this.f9372c)) {
                return this.f9374f;
            }
            this.f9372c = strA;
            this.f9374f = C1818g.b(strA);
            return this.f9374f;
        } catch (V.g e2) {
            return "Error";
        }
    }

    public void setOffText(String str) {
        try {
            this.f9357h = cY.a().a(this.f9370a, str);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public Color getOnBackgroundColor() {
        return this.f9358i;
    }

    public void setOnBackgroundColor(Color color) {
        this.f9358i = color;
        invalidatePainter();
    }

    public Color getOffBackgroundColor() {
        return this.f9359j;
    }

    public void setOffBackgroundColor(Color color) {
        this.f9359j = color;
        invalidatePainter();
    }

    public Color getOnTextColor() {
        return this.f9360k;
    }

    public void setOnTextColor(Color color) {
        this.f9360k = color;
        invalidatePainter();
    }

    public Color getOffTextColor() {
        return this.f9361l;
    }

    public void setOffTextColor(Color color) {
        this.f9361l = color;
        invalidatePainter();
    }

    public IndicatorPainter getPainter() {
        return this.f9363n;
    }

    public void setPainter(IndicatorPainter indicatorPainter) {
        this.f9363n = indicatorPainter;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f9363n != null && getWidth() > 0 && getHeight() > 0) {
            this.f9363n.paintGauge(graphics, this);
        }
        if (isInvalidState()) {
            paintInvalid(graphics);
        }
        super.setDirty(false);
    }

    public void paintInvalid(Graphics graphics) {
        if (getOffBackgroundColor().getRed() <= 220 || getOffBackgroundColor().getBlue() >= 92 || getOffBackgroundColor().getGreen() >= 92) {
            graphics.setColor(Color.red);
        } else {
            graphics.setXORMode(getOffBackgroundColor());
        }
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setStroke(new BasicStroke(eJ.a(3)));
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int height = getHeight() - 1;
        graphics.drawOval((getWidth() - height) / 2, 0, height, height);
        int i2 = (int) (((height / 2) * 1.414213d) / 2.0d);
        int width = getWidth() / 2;
        int height2 = getHeight() / 2;
        graphics.drawLine(width - i2, height2 - i2, width + i2, height2 + i2);
        graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void update(Graphics graphics) {
        if (this.f9363n != null) {
            this.f9363n.updateGauge(graphics, this);
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public void invalidate() {
        if (this.f9363n != null) {
            this.f9363n.invalidate();
        }
        super.invalidate();
    }

    @Override // java.awt.Container
    public Insets insets() {
        return this.f9364o;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent
    public double getValue() {
        return this.f9362m ? 1.0d : 0.0d;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent
    public void setValue(double d2) {
        boolean z2 = false;
        if (this.f9362m && d2 == 0.0d) {
            this.f9362m = false;
            z2 = true;
        } else if (!this.f9362m && d2 != 0.0d) {
            this.f9362m = true;
            z2 = true;
        }
        if (z2) {
            callRepaint();
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void invalidatePainter() {
        if (getPainter() != null) {
            getPainter().invalidate();
            setDirty(true);
        }
        this.f9367r = null;
        this.f9369t = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void callRepaint() {
        if (getParent() instanceof InterfaceC1390ac) {
            ((InterfaceC1390ac) getParent()).a(this);
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isMustPaint() {
        return getPainter().isMustPaint();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isRunDemo() {
        return this.f9365p != null;
    }

    public String getOnImageFileName() {
        return this.f9366q;
    }

    public void setOnImageFileName(String str) {
        this.f9366q = str;
        if (str == null || str.equals("")) {
            this.f9367r = null;
        } else {
            this.f9367r = Toolkit.getDefaultToolkit().getImage(str);
            MediaTracker mediaTracker = new MediaTracker(this);
            try {
                mediaTracker.addImage(this.f9367r, 0);
                mediaTracker.waitForAll(1000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(AbstractC1420s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        invalidatePainter();
    }

    public Image onImage() {
        if (this.f9367r == null && this.f9366q != null && !this.f9366q.equals("")) {
            this.f9367r = Toolkit.getDefaultToolkit().getImage(this.f9366q);
            MediaTracker mediaTracker = new MediaTracker(this);
            try {
                mediaTracker.addImage(this.f9367r, 0);
                mediaTracker.waitForAll(1000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(AbstractC1420s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return this.f9367r;
    }

    public String getOffImageFileName() {
        return this.f9368s;
    }

    public void setOffImageFileName(String str) {
        this.f9368s = str;
        if (str == null || str.equals("")) {
            this.f9369t = null;
        } else {
            this.f9369t = Toolkit.getDefaultToolkit().getImage(str);
            MediaTracker mediaTracker = new MediaTracker(this);
            try {
                mediaTracker.addImage(this.f9369t, 0);
                mediaTracker.waitForAll(1000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(AbstractC1420s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        invalidatePainter();
    }

    public Image offImage() {
        if (this.f9369t == null && this.f9368s != null && !this.f9368s.equals("")) {
            this.f9369t = Toolkit.getDefaultToolkit().getImage(this.f9368s);
            MediaTracker mediaTracker = new MediaTracker(this);
            try {
                mediaTracker.addImage(this.f9369t, 0);
                mediaTracker.waitForAll(1000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(AbstractC1420s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return this.f9369t;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isComponentPaintedAt(int i2, int i3) {
        return i2 >= 0 && i2 < getWidth() && i3 >= 0 && i3 < getHeight();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void paintBackground(Graphics graphics) {
        if (this.f9363n != null) {
            this.f9363n.paintBackground(graphics, this);
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean requiresBackgroundRepaint() {
        if (this.f9363n != null) {
            return this.f9363n.requiresBackgroundRepaint(this);
        }
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void updateGauge(Graphics graphics) {
        if (this.f9363n != null) {
            this.f9363n.updateGauge(graphics, this);
        }
        if (isInvalidState()) {
            paintInvalid(graphics);
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public Area areaPainted() {
        return this.f9363n.areaPainted(this);
    }
}
