package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.tuningwidgets.panels.SelectableTablePanel;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import javax.accessibility.AccessibleContext;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/SelectableTableComponent.class */
public class SelectableTableComponent extends SingleChannelDashComponent implements InterfaceC1421t, com.efiAnalytics.tuningwidgets.panels.ay, Serializable {

    /* renamed from: a, reason: collision with root package name */
    SelectableTablePanel f9398a = null;

    /* renamed from: b, reason: collision with root package name */
    G.R f9399b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f9400c = null;

    public SelectableTableComponent() {
        setLayout(new BorderLayout());
        super.setDoubleBuffered(true);
    }

    private void addTableSelector() {
        if (this.f9398a == null) {
            if (this.f9399b == null) {
                setEcuConfigurationName(getEcuConfigurationName());
            }
            this.f9398a = new SelectableTablePanel(this.f9399b);
            this.f9398a.a(3);
            this.f9398a.a(this);
            if (this.f9400c != null) {
                this.f9398a.a(this.f9400c);
            }
            if (this.f9400c != null) {
                setSelectedTableName(this.f9400c);
            }
            add(BorderLayout.CENTER, this.f9398a);
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        return null;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void setEcuConfigurationName(String str) {
        super.setEcuConfigurationName(str);
        if (str == null || str.isEmpty()) {
            this.f9399b = G.T.a().c();
        } else {
            this.f9399b = G.T.a().c(str);
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent
    public void setCurrentOutputChannelValue(String str, String str2) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent
    public void setValue(double d2) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent
    public double getValue() {
        return 0.0d;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent, G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void setRunDemo(boolean z2) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isRunDemo() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void goDead() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void invalidatePainter() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isMustPaint() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.SingleChannelDashComponent, com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void subscribeToOutput() {
        addTableSelector();
    }

    public String getSelectedTableName() {
        return this.f9400c;
    }

    public void setSelectedTableName(String str) {
        this.f9400c = str;
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.ay
    public void panelSelectionChanged(String str, String str2) {
        this.f9400c = str2;
        super.setEcuConfigurationName(this.f9550e);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
        System.out.println("SelectablTableComponent::Repaint: x=" + i2 + ", y=" + i3 + ", w=" + i4 + ", h=" + i5);
        super.repaint(j2, i2, i3, i4, i5);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isComponentPaintedAt(int i2, int i3) {
        return i2 >= 0 && i2 < getWidth() && i3 >= 0 && i3 < getHeight();
    }

    @Override // java.awt.Component
    public void paintAll(Graphics graphics) {
        bH.C.c("PaintAll");
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void paintBackground(Graphics graphics) {
        this.f9398a.paint(graphics);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean requiresBackgroundRepaint() {
        return this.f9398a != null && this.f9398a.isValid();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void updateGauge(Graphics graphics) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public Area areaPainted() {
        return new Area(new Rectangle2D.Double(0.0d, 0.0d, getWidth(), getHeight()));
    }
}
