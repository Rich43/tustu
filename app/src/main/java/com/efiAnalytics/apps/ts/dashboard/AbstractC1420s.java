package com.efiAnalytics.apps.ts.dashboard;

import G.C0113cs;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import javafx.fxml.FXMLLoader;
import javax.swing.JComponent;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/s.class */
public abstract class AbstractC1420s extends JComponent implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private double f9542a = 0.0d;

    /* renamed from: b, reason: collision with root package name */
    private double f9543b = 0.0d;

    /* renamed from: c, reason: collision with root package name */
    private double f9544c = 0.2d;

    /* renamed from: d, reason: collision with root package name */
    private double f9545d = 0.2d;

    /* renamed from: f, reason: collision with root package name */
    private InterfaceC1390ac f9546f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f9547g = "";

    /* renamed from: h, reason: collision with root package name */
    private boolean f9548h = false;

    /* renamed from: i, reason: collision with root package name */
    private String f9549i = "";

    /* renamed from: e, reason: collision with root package name */
    protected String f9550e = "";

    /* renamed from: j, reason: collision with root package name */
    private int f9551j = 0;

    /* renamed from: k, reason: collision with root package name */
    private int f9552k = 0;

    /* renamed from: l, reason: collision with root package name */
    private boolean f9553l = true;

    /* renamed from: m, reason: collision with root package name */
    private boolean f9554m = false;

    /* renamed from: n, reason: collision with root package name */
    private boolean f9555n = false;

    public double getRelativeX() {
        return this.f9542a;
    }

    public void setRelativeX(double d2) {
        this.f9542a = d2;
    }

    public double getRelativeY() {
        return this.f9543b;
    }

    public void setRelativeY(double d2) {
        this.f9543b = d2;
    }

    public double getRelativeWidth() {
        return this.f9544c;
    }

    public void setRelativeWidth(double d2) {
        this.f9544c = d2;
    }

    public double getRelativeHeight() {
        return this.f9545d;
    }

    public void setRelativeHeight(double d2) {
        this.f9545d = d2;
    }

    public boolean isAntialiasingOn() {
        return this.f9553l;
    }

    public void setAntialiasingOn(boolean z2) {
        this.f9553l = z2;
    }

    public void myGaugeContainer(InterfaceC1390ac interfaceC1390ac) {
        this.f9546f = interfaceC1390ac;
    }

    public InterfaceC1390ac myGaugeContainer() {
        if (this.f9546f == null && getParent() != null && (getParent() instanceof InterfaceC1390ac)) {
            this.f9546f = (InterfaceC1390ac) getParent();
        }
        return this.f9546f;
    }

    public void setGaugeContainer(InterfaceC1390ac interfaceC1390ac) {
        this.f9546f = interfaceC1390ac;
    }

    public abstract void setRunDemo(boolean z2);

    public abstract boolean isRunDemo();

    public abstract void goDead();

    public abstract void invalidatePainter();

    public abstract boolean isMustPaint();

    public boolean isDirty() {
        if (!this.f9555n && getWidth() == this.f9551j && getHeight() == this.f9552k) {
            return false;
        }
        updateLastsVals();
        return true;
    }

    protected void updateLastsVals() {
        this.f9551j = getWidth();
        this.f9552k = getHeight();
    }

    public String getFontFamily() {
        return this.f9547g;
    }

    public void setFontFamily(String str) {
        this.f9547g = str;
        invalidatePainter();
    }

    @Override // java.awt.Component
    public String toString() throws SecurityException {
        Field[] declaredFields = getClass().getDeclaredFields();
        AccessibleObject.setAccessible(declaredFields, true);
        String name = getClass().getName();
        for (Field field : declaredFields) {
            try {
                name = name + "\n\t" + field.getName() + "=" + field.get(this) + ", Generic String:" + field.toGenericString() + ", ";
            } catch (Exception e2) {
            }
        }
        return name + "\n";
    }

    public boolean isInvalidState() {
        return this.f9554m;
    }

    protected void setInvalidState(boolean z2) {
        this.f9554m = z2;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
    }

    public void updateRelativeBoundsToCurrent() {
        if (getParent() == null || getParent().getWidth() <= 0 || getParent().getHeight() <= 0) {
            return;
        }
        this.f9542a = super.getX() / getParent().getWidth();
        this.f9543b = super.getY() / getParent().getHeight();
        this.f9544c = super.getWidth() / getParent().getWidth();
        this.f9545d = super.getHeight() / getParent().getHeight();
    }

    public void setDirty(boolean z2) {
        this.f9555n = z2;
    }

    public String getEcuConfigurationName() {
        return (this.f9550e == null || !this.f9550e.equals(C0113cs.f1154a)) ? (G.T.a().c() == null || !G.T.a().c().c().equals(this.f9550e)) ? (this.f9550e == null || this.f9550e.isEmpty()) ? this.f9550e : this.f9550e : "" : this.f9550e;
    }

    public void setEcuConfigurationName(String str) {
        if (str == null || str.equals(FXMLLoader.NULL_KEYWORD)) {
            this.f9550e = "";
        } else {
            this.f9550e = str;
        }
    }

    public abstract boolean isComponentPaintedAt(int i2, int i3);

    public abstract void subscribeToOutput();

    public abstract void unsubscribeToOutput();

    public String getId() {
        return this.f9549i;
    }

    public void setId(String str) {
        this.f9549i = str;
    }

    public boolean isItalicFont() {
        return this.f9548h;
    }

    public void setItalicFont(boolean z2) {
        this.f9548h = z2;
    }

    public abstract void paintBackground(Graphics graphics);

    public abstract boolean requiresBackgroundRepaint();

    public abstract void updateGauge(Graphics graphics);

    public abstract Area areaPainted();
}
