package com.efiAnalytics.apps.ts.dashboard;

import G.C0094c;
import G.C0126i;
import G.aR;
import G.bQ;
import G.cX;
import G.cY;
import G.cZ;
import G.dh;
import G.di;
import com.efiAnalytics.apps.ts.dashboard.renderers.GaugePainter;
import com.efiAnalytics.apps.ts.dashboard.renderers.RoundAnalogGaugePainter;
import com.efiAnalytics.ui.eJ;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Area;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/Gauge.class */
public class Gauge extends SingleChannelDashComponent implements aO, Serializable {

    /* renamed from: I, reason: collision with root package name */
    cX f9334I;

    /* renamed from: s, reason: collision with root package name */
    public static Color f9305s = new Color(245, 245, 245);

    /* renamed from: t, reason: collision with root package name */
    public static Color f9306t = new Color(105, 105, 122);

    /* renamed from: u, reason: collision with root package name */
    public static Color f9307u = new Color(29, 31, 82);

    /* renamed from: v, reason: collision with root package name */
    public static Color f9308v = new Color(0, 0, 102);

    /* renamed from: F, reason: collision with root package name */
    public static int f9323F = 0;

    /* renamed from: G, reason: collision with root package name */
    public static int f9324G = 1;

    /* renamed from: Q, reason: collision with root package name */
    public static int f9343Q = 0;

    /* renamed from: a, reason: collision with root package name */
    protected dh f9282a = new G.B(0.0d);

    /* renamed from: b, reason: collision with root package name */
    protected dh f9283b = new G.B(100.0d);

    /* renamed from: T, reason: collision with root package name */
    private dh f9284T = new G.B(Double.NEGATIVE_INFINITY);

    /* renamed from: c, reason: collision with root package name */
    protected dh f9285c = new G.B(Double.NEGATIVE_INFINITY);

    /* renamed from: d, reason: collision with root package name */
    protected dh f9286d = new G.B(85.0d);

    /* renamed from: f, reason: collision with root package name */
    protected dh f9287f = new G.B(93.0d);

    /* renamed from: U, reason: collision with root package name */
    private double f9288U = 0.0d;

    /* renamed from: V, reason: collision with root package name */
    private double f9289V = 100.0d;

    /* renamed from: g, reason: collision with root package name */
    protected int f9290g = 0;

    /* renamed from: h, reason: collision with root package name */
    protected int f9291h = 360;

    /* renamed from: i, reason: collision with root package name */
    protected int f9292i = 300;

    /* renamed from: j, reason: collision with root package name */
    protected int f9293j = 300;

    /* renamed from: W, reason: collision with root package name */
    private boolean f9294W = false;

    /* renamed from: k, reason: collision with root package name */
    protected cZ f9295k = new C0094c("Units");

    /* renamed from: l, reason: collision with root package name */
    protected cZ f9296l = new C0094c("Demo");

    /* renamed from: m, reason: collision with root package name */
    protected dh f9297m = new G.B(1.0d);

    /* renamed from: X, reason: collision with root package name */
    private int f9298X = -1;

    /* renamed from: n, reason: collision with root package name */
    protected int f9299n = 0;

    /* renamed from: o, reason: collision with root package name */
    protected int f9300o = 8;

    /* renamed from: Y, reason: collision with root package name */
    private double f9301Y = Double.NaN;

    /* renamed from: p, reason: collision with root package name */
    protected int f9302p = 0;

    /* renamed from: q, reason: collision with root package name */
    protected double f9303q = -1.0d;

    /* renamed from: r, reason: collision with root package name */
    protected double f9304r = -1.0d;

    /* renamed from: w, reason: collision with root package name */
    protected Color f9309w = f9305s;

    /* renamed from: x, reason: collision with root package name */
    protected Color f9310x = f9307u;

    /* renamed from: y, reason: collision with root package name */
    protected Color f9311y = f9306t;

    /* renamed from: z, reason: collision with root package name */
    protected Color f9312z = Color.yellow;

    /* renamed from: A, reason: collision with root package name */
    protected Color f9313A = Color.red;

    /* renamed from: B, reason: collision with root package name */
    protected Color f9314B = f9308v;

    /* renamed from: C, reason: collision with root package name */
    protected String f9315C = "";

    /* renamed from: D, reason: collision with root package name */
    protected double f9316D = 0.0d;

    /* renamed from: E, reason: collision with root package name */
    protected double f9317E = 0.0d;

    /* renamed from: Z, reason: collision with root package name */
    private long f9318Z = 0;

    /* renamed from: aa, reason: collision with root package name */
    private double f9319aa = 0.0d;

    /* renamed from: ab, reason: collision with root package name */
    private int f9320ab = 15000;

    /* renamed from: ac, reason: collision with root package name */
    private long f9321ac = 0;

    /* renamed from: ad, reason: collision with root package name */
    private boolean f9322ad = true;

    /* renamed from: ae, reason: collision with root package name */
    private int f9325ae = f9324G;

    /* renamed from: af, reason: collision with root package name */
    private boolean f9326af = true;

    /* renamed from: ag, reason: collision with root package name */
    private boolean f9327ag = false;

    /* renamed from: H, reason: collision with root package name */
    protected GaugePainter f9328H = null;

    /* renamed from: ah, reason: collision with root package name */
    private transient aH f9329ah = null;

    /* renamed from: ai, reason: collision with root package name */
    private String f9330ai = null;

    /* renamed from: aj, reason: collision with root package name */
    private transient Image f9331aj = null;

    /* renamed from: ak, reason: collision with root package name */
    private String f9332ak = null;

    /* renamed from: al, reason: collision with root package name */
    private transient Image f9333al = null;

    /* renamed from: J, reason: collision with root package name */
    C1424w f9335J = new C1424w(this);

    /* renamed from: K, reason: collision with root package name */
    C1424w f9336K = new C1424w(this);

    /* renamed from: L, reason: collision with root package name */
    C1424w f9337L = new C1424w(this);

    /* renamed from: M, reason: collision with root package name */
    C1424w f9338M = new C1424w(this);

    /* renamed from: N, reason: collision with root package name */
    C1424w f9339N = new C1424w(this);

    /* renamed from: O, reason: collision with root package name */
    C1424w f9340O = new C1424w(this);

    /* renamed from: P, reason: collision with root package name */
    C1424w f9341P = new C1424w(this);

    /* renamed from: am, reason: collision with root package name */
    private int f9342am = 0;

    /* renamed from: R, reason: collision with root package name */
    int f9344R = -1;

    /* renamed from: an, reason: collision with root package name */
    private String f9345an = null;

    /* renamed from: ao, reason: collision with root package name */
    private String f9346ao = null;

    /* renamed from: ap, reason: collision with root package name */
    private String f9347ap = null;

    /* renamed from: aq, reason: collision with root package name */
    private String f9348aq = null;

    public Gauge() {
        this.f9334I = null;
        setGaugePainter(new RoundAnalogGaugePainter());
        this.f9334I = new C1423v(this);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f9328H != null && getWidth() > 0 && getHeight() > 0) {
            this.f9328H.paintGauge(graphics, this);
            if (isInvalidState()) {
                paintInvalid(graphics);
            }
        }
        super.setDirty(false);
    }

    private void paintInvalid(Graphics graphics) {
        if (getBackColor().getRed() <= 220 || getBackColor().getBlue() >= 92 || getBackColor().getGreen() >= 92) {
            graphics.setColor(Color.red);
        } else {
            graphics.setXORMode(getBackColor());
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
        graphics.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
    }

    @Override // java.awt.Container, java.awt.Component
    public void invalidate() {
        if (this.f9328H != null) {
            this.f9317E = getValue();
            this.f9328H.invalidate();
        }
        this.f9298X = -1;
        super.invalidate();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s, java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        if (this.f9328H != null && (i4 != getWidth() || i5 != getHeight())) {
            this.f9328H.invalidate();
        }
        this.f9344R = i3;
        super.setBounds(i2, i3, i4, i5);
    }

    public GaugePainter getGaugePainter() {
        return this.f9328H;
    }

    public void setGaugePainter(GaugePainter gaugePainter) {
        this.f9328H = gaugePainter;
        gaugePainter.initialize(this);
        repaint();
    }

    public double min() {
        return getDefaultMin();
    }

    public double getMin() {
        return min();
    }

    public void initializeExpressionMonitors() {
        initExpressionMonitor(this.f9335J, this.f9282a);
        initExpressionMonitor(this.f9336K, this.f9283b);
        initExpressionMonitor(this.f9338M, this.f9284T);
        initExpressionMonitor(this.f9337L, this.f9285c);
        initExpressionMonitor(this.f9340O, this.f9287f);
        initExpressionMonitor(this.f9339N, this.f9286d);
        initExpressionMonitor(this.f9341P, this.f9297m);
    }

    public dh getMinVP() {
        return this.f9282a;
    }

    public void setMin(Object obj) {
        if (this.f9282a instanceof bQ) {
            return;
        }
        try {
            this.f9282a = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9335J, this.f9282a);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.WARNING, "Failed to set min monitor", (Throwable) e2);
        }
        invalidatePainter();
    }

    public void setMinVP(Object obj) {
        try {
            this.f9282a = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9335J, this.f9282a);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.WARNING, "Failed to set up Min Monitor", (Throwable) e2);
        }
        invalidatePainter();
    }

    private void initExpressionMonitor(C1424w c1424w, dh dhVar) {
        if (this.f9334I.a() == null || this.f9334I.a().isEmpty()) {
            return;
        }
        aR.a().a(c1424w);
        try {
            C0126i.a(this.f9334I.a(), dhVar, c1424w);
        } catch (V.a e2) {
            bH.C.a(e2);
        }
    }

    public double max() {
        return getDefaultMax();
    }

    public double getMax() {
        return max();
    }

    public dh getMaxVP() {
        return this.f9283b;
    }

    public void setMax(Object obj) {
        if (this.f9283b instanceof bQ) {
            return;
        }
        try {
            this.f9283b = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9336K, this.f9283b);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public void setMaxVP(Object obj) {
        try {
            this.f9283b = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9336K, this.f9283b);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public double lowWarning() {
        return this.f9285c.a();
    }

    public double getLowWarning() {
        return lowWarning();
    }

    public dh getLowWarningVP() {
        return this.f9285c;
    }

    public void setLowWarning(Object obj) {
        if (this.f9285c instanceof bQ) {
            return;
        }
        try {
            this.f9285c = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9337L, this.f9285c);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public void setLowWarningVP(Object obj) {
        try {
            this.f9285c = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9337L, this.f9285c);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public double highWarning() {
        return this.f9286d.a();
    }

    public double getHighWarning() {
        return highWarning();
    }

    public dh getHighWarningVP() {
        return this.f9286d;
    }

    public void setHighWarningVP(Object obj) {
        try {
            this.f9286d = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9339N, this.f9286d);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public void setHighWarning(Object obj) {
        if (this.f9286d instanceof bQ) {
            return;
        }
        try {
            this.f9286d = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9339N, this.f9286d);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public double highCritical() {
        return this.f9287f.a();
    }

    public double getHighCritical() {
        return highCritical();
    }

    public dh getHighCriticalVP() {
        return this.f9287f;
    }

    public void setHighCritical(Object obj) {
        if (this.f9287f instanceof bQ) {
            return;
        }
        try {
            this.f9287f = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9340O, this.f9287f);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public void setHighCriticalVP(Object obj) {
        try {
            this.f9287f = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9340O, this.f9287f);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public double lowCritical() {
        return this.f9284T.a();
    }

    public double getLowCritical() {
        return lowCritical();
    }

    public dh getLowCriticalVP() {
        return this.f9284T;
    }

    public void setLowCritical(Object obj) {
        if (this.f9284T instanceof bQ) {
            return;
        }
        try {
            this.f9284T = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9338M, this.f9284T);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public void setLowCriticalVP(Object obj) {
        try {
            this.f9284T = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9338M, this.f9284T);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public boolean isPegLimits() {
        return this.f9322ad;
    }

    public void setPegLimits(boolean z2) {
        this.f9322ad = z2;
    }

    public int getGroupId() {
        return this.f9342am;
    }

    public void setGroupId(int i2) {
        this.f9342am = i2;
    }

    public int getStartAngle() {
        return this.f9290g;
    }

    public void setStartAngle(int i2) {
        this.f9290g = i2;
        invalidatePainter();
    }

    public int getFaceAngle() {
        return this.f9291h;
    }

    public void setFaceAngle(int i2) {
        this.f9291h = i2;
        invalidatePainter();
    }

    public String units() {
        try {
            String strA = this.f9295k.a();
            if (this.f9345an != null && strA.equals(this.f9345an)) {
                return this.f9346ao;
            }
            this.f9345an = strA;
            this.f9346ao = C1818g.b(strA);
            return this.f9346ao;
        } catch (V.g e2) {
            return "";
        }
    }

    public void setUnits(String str) {
        try {
            this.f9295k = cY.a().a(this.f9334I, str);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public String getUnits() {
        return this.f9295k.toString();
    }

    public String title() {
        try {
            String strA = this.f9296l.a();
            if (this.f9347ap != null && strA.equals(this.f9347ap)) {
                return this.f9348aq;
            }
            this.f9347ap = strA;
            this.f9348aq = C1818g.b(strA);
            return this.f9348aq;
        } catch (V.g e2) {
            return "Error";
        }
    }

    public String getTitle() {
        return this.f9296l.toString();
    }

    public void setTitle(String str) {
        try {
            this.f9296l = cY.a().a(this.f9334I, str);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public int valueDigits() {
        if (this.f9298X == -1) {
            this.f9298X = (int) Math.round(this.f9297m.a());
        }
        return this.f9298X;
    }

    public int getValueDigits() {
        return valueDigits();
    }

    public dh getValueDigitsVP() {
        return this.f9297m;
    }

    public void setValueDigits(Object obj) {
        if (!(this.f9297m instanceof bQ)) {
            try {
                this.f9297m = di.a(this.f9334I, obj.toString());
                initExpressionMonitor(this.f9341P, this.f9297m);
            } catch (V.g e2) {
                Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            invalidatePainter();
        }
        this.f9298X = -1;
    }

    public void setValueDigitsVP(Object obj) {
        try {
            this.f9297m = di.a(this.f9334I, obj.toString());
            initExpressionMonitor(this.f9341P, this.f9297m);
        } catch (V.g e2) {
            Logger.getLogger(Gauge.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        invalidatePainter();
    }

    public int getLabelDigits() {
        return this.f9299n;
    }

    public void setLabelDigits(int i2) {
        this.f9299n = i2;
        invalidatePainter();
    }

    public double getMajorTicks() {
        return this.f9303q;
    }

    public void setMajorTicks(double d2) {
        this.f9303q = d2;
        invalidatePainter();
    }

    public double getMinorTicks() {
        return this.f9304r;
    }

    public void setMinorTicks(double d2) {
        this.f9304r = d2;
        invalidatePainter();
    }

    public Color getBackColor() {
        return this.f9309w;
    }

    public void setBackColor(Color color) {
        this.f9309w = color;
        invalidatePainter();
    }

    public Color getFontColor() {
        return this.f9310x;
    }

    public void setFontColor(Color color) {
        this.f9310x = color;
        invalidatePainter();
    }

    public Color getTrimColor() {
        return this.f9311y;
    }

    public void setTrimColor(Color color) {
        this.f9311y = color;
        invalidatePainter();
    }

    public Color getWarnColor() {
        return this.f9312z;
    }

    public void setWarnColor(Color color) {
        this.f9312z = color;
        invalidatePainter();
    }

    public Color getCriticalColor() {
        return this.f9313A;
    }

    public void setCriticalColor(Color color) {
        this.f9313A = color;
        invalidatePainter();
    }

    public Color getNeedleColor() {
        return this.f9314B;
    }

    public void setNeedleColor(Color color) {
        this.f9314B = color;
    }

    public int getSweepAngle() {
        return this.f9292i;
    }

    public void setSweepAngle(int i2) {
        this.f9292i = i2;
        invalidatePainter();
    }

    public int getSweepBeginDegree() {
        return this.f9293j;
    }

    public void setSweepBeginDegree(int i2) {
        this.f9293j = i2;
        invalidatePainter();
    }

    public String getGaugeStyle() {
        return this.f9328H.getName();
    }

    public String getDisplayValue() {
        return bH.W.c(getValue(), valueDigits());
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent
    public double getValue() {
        return this.f9316D;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent
    public void setValue(double d2) {
        updateSmoothedValue(d2);
        boolean z2 = getValue() != d2 || Math.abs(this.f9317E - d2) / (this.f9283b.a() - this.f9282a.a()) > 0.004d;
        this.f9316D = d2;
        if (!z2 || myGaugeContainer() == null) {
            return;
        }
        myGaugeContainer().a(this);
        setDirty(true);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isRunDemo() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void setRunDemo(boolean z2) {
    }

    public boolean isShapeLockedToAspect() {
        return this.f9328H.isShapeLockedToAspect();
    }

    public boolean isGoingDead() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent
    public void setCurrentOutputChannelValue(String str, String str2) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent, G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (Double.isNaN(d2)) {
            return;
        }
        setValue(d2);
        if (getHistoricalPeakValue() < d2 || System.currentTimeMillis() - this.f9321ac > getHistoryDelay()) {
            this.f9321ac = System.currentTimeMillis();
            setHistoricalPeakValue(d2);
        }
        if (this.f9329ah != null) {
            this.f9329ah.a(d2);
        }
    }

    private double updateSmoothedValue(double d2) {
        if (this.f9325ae == f9324G) {
            long jNanoTime = System.nanoTime() - this.f9318Z;
            this.f9318Z = System.nanoTime();
            if (jNanoTime >= 250000000 || Double.isInfinite(this.f9317E)) {
                this.f9317E = d2;
            } else {
                this.f9317E = ((this.f9316D * jNanoTime) + (this.f9317E * 6.0E7d)) / (jNanoTime + 60000000);
            }
        } else {
            this.f9317E = d2;
        }
        return this.f9317E;
    }

    public double getSmoothedValue() {
        return this.f9317E;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void goDead() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isMustPaint() {
        return !isValid() || getBackColor().getAlpha() < 200 || getWarnColor().getAlpha() < 200 || getCriticalColor().getAlpha() < 200;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void paintBackground(Graphics graphics) {
        if (this.f9328H != null) {
            this.f9328H.paintBackground(graphics, this);
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean requiresBackgroundRepaint() {
        if (!isValid()) {
            return true;
        }
        if (this.f9328H != null) {
            return this.f9328H.requiresBackgroundRepaint(this);
        }
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void updateGauge(Graphics graphics) {
        if (this.f9328H != null) {
            this.f9328H.updateGauge(graphics, this);
        }
        if (isInvalidState()) {
            paintInvalid(graphics);
        }
    }

    public boolean isDisplayValueAt180() {
        return this.f9327ag;
    }

    public void setDisplayValueAt180(boolean z2) {
        this.f9327ag = z2;
    }

    public int getNeedleSmoothing() {
        return this.f9325ae;
    }

    public void setNeedleSmoothing(int i2) {
        this.f9325ae = i2;
    }

    public double getDefaultMin() {
        return Double.isNaN(getMinVP().a()) ? this.f9288U : getMinVP().a();
    }

    public void setDefaultMin(double d2) {
        this.f9288U = d2;
    }

    public double getDefaultMax() {
        return Double.isNaN(getMaxVP().a()) ? this.f9289V : getMaxVP().a();
    }

    public void setDefaultMax(double d2) {
        this.f9289V = d2;
    }

    public void setRelativeBorderWidth2(double d2) {
        this.f9301Y = d2;
    }

    public String getNeedleImageFileName() {
        return this.f9332ak;
    }

    public void setNeedleImageFileName(String str) {
        if (str != null && !str.startsWith("IMG_ID_")) {
            this.f9333al = null;
        }
        this.f9332ak = str;
        invalidatePainter();
        repaint();
    }

    public int getBorderWidth() {
        return Double.isNaN(this.f9301Y) ? this.f9300o : (int) Math.round(getShortestSize() * getRelativeBorderWidth2());
    }

    public double getRelativeBorderWidth2() {
        return this.f9301Y;
    }

    public int getShortestSize() {
        return getWidth() > getHeight() ? getHeight() : getWidth();
    }

    public void setBorderWidth(int i2) {
        this.f9300o = i2;
        invalidatePainter();
    }

    public int getFontSizeAdjustment() {
        return this.f9302p;
    }

    public void setFontSizeAdjustment(int i2) {
        this.f9302p = i2;
        invalidatePainter();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void invalidatePainter() {
        this.f9298X = -1;
        this.f9331aj = null;
        this.f9333al = null;
        if (this.f9328H != null) {
            this.f9328H.invalidate();
            setDirty(true);
            repaint();
        }
    }

    public double getHistoricalPeakValue() {
        return this.f9319aa;
    }

    public void setHistoricalPeakValue(double d2) {
        this.f9319aa = d2;
    }

    public boolean isShowHistory() {
        return this.f9326af;
    }

    public void setShowHistory(boolean z2) {
        this.f9319aa = this.f9282a.a();
        this.f9326af = z2;
    }

    public int getHistoryDelay() {
        return this.f9320ab;
    }

    public void setHistoryDelay(int i2) {
        this.f9320ab = i2;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void setAntialiasingOn(boolean z2) {
        super.setAntialiasingOn(z2);
        invalidate();
    }

    public aH setCaptureHistoricalData(boolean z2) {
        if (!z2) {
            this.f9329ah = null;
        } else if (this.f9329ah == null) {
            this.f9329ah = new aH();
        }
        return this.f9329ah;
    }

    public String formatDouble(double d2, int i2) {
        return bH.W.b(d2, i2);
    }

    public int historySize() {
        if (this.f9329ah == null) {
            return -1;
        }
        return this.f9329ah.size();
    }

    public boolean isCounterClockwise() {
        return this.f9294W;
    }

    public void setCounterClockwise(boolean z2) {
        this.f9294W = z2;
        invalidate();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isComponentPaintedAt(int i2, int i3) {
        return getGaugePainter() instanceof aO ? ((aO) getGaugePainter()).isComponentPaintedAt(i2, i3) : i2 <= getWidth() && i3 <= getHeight();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent, com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isDirty() {
        if (super.isDirty()) {
            return true;
        }
        if (Math.abs(getSmoothedValue() - getValue()) / (max() - min()) <= Math.pow(10.0d, -valueDigits())) {
            return false;
        }
        myGaugeContainer().a(this);
        return true;
    }

    public String getBackgroundImageFileName() {
        return this.f9330ai;
    }

    public void setBackgroundImageFileName(String str) {
        this.f9330ai = str;
        if (str != null && !str.startsWith("IMG_ID_")) {
            if (str.equals("")) {
                this.f9331aj = null;
            } else {
                this.f9331aj = Toolkit.getDefaultToolkit().getImage(str);
                MediaTracker mediaTracker = new MediaTracker(this);
                mediaTracker.addImage(this.f9331aj, 0);
                try {
                    mediaTracker.waitForAll(250L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(AbstractC1420s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        }
        invalidatePainter();
        repaint();
    }

    public Image backgroundImage() {
        if (this.f9330ai != null && !this.f9330ai.isEmpty()) {
            if (this.f9330ai.equals("")) {
                this.f9331aj = null;
            } else {
                this.f9331aj = Toolkit.getDefaultToolkit().getImage(this.f9330ai);
                MediaTracker mediaTracker = new MediaTracker(this);
                mediaTracker.addImage(this.f9331aj, 0);
                try {
                    mediaTracker.waitForAll(250L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(AbstractC1420s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        }
        return this.f9331aj;
    }

    public Image needleImage(int i2) {
        if (i2 > 0 && this.f9332ak != null && !this.f9332ak.equals("") && !this.f9332ak.startsWith("IMG_ID_") && (this.f9333al == null || this.f9333al.getHeight(null) != i2)) {
            this.f9333al = com.efiAnalytics.apps.ts.dashboard.renderers.f.a().a(this.f9332ak, i2, this);
        }
        return this.f9333al;
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        if (i2 == 32) {
            invalidatePainter();
            repaint();
        }
        return super.imageUpdate(image, i2, i3, i4, i5, i6);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public Area areaPainted() {
        return this.f9328H.areaPainted(this);
    }
}
