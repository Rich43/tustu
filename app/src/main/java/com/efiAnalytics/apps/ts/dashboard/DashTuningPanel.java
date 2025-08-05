package com.efiAnalytics.apps.ts.dashboard;

import G.C0088bu;
import bt.C1287O;
import bt.C1324bf;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import javafx.fxml.FXMLLoader;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/DashTuningPanel.class */
public class DashTuningPanel extends AbstractC1420s implements InterfaceC1421t, Serializable {

    /* renamed from: a, reason: collision with root package name */
    JPanel f9274a = new JPanel();

    /* renamed from: b, reason: collision with root package name */
    JLabel f9275b = new JLabel();

    /* renamed from: c, reason: collision with root package name */
    C1324bf f9276c = null;

    /* renamed from: d, reason: collision with root package name */
    JScrollPane f9277d = new JScrollPane();

    public DashTuningPanel() {
        setLayout(new BorderLayout());
        this.f9274a.setLayout(new BorderLayout());
        this.f9274a.add(BorderLayout.CENTER, this.f9275b);
        JButton jButton = new JButton(FXMLLoader.CONTROLLER_METHOD_PREFIX);
        jButton.setPreferredSize(new Dimension(15, 15));
        jButton.setToolTipText(C1818g.b("Click to select Settings"));
        jButton.addActionListener(new C1417p(this));
        jButton.setFocusable(false);
        this.f9274a.add("East", jButton);
        add("North", this.f9274a);
        add(BorderLayout.CENTER, this.f9277d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSelectPopup(int i2, int i3) {
        C1287O c1287o = new C1287O(getEcuConfiguration(), -1);
        c1287o.a(new C1418q(this));
        this.f9274a.add(c1287o);
        c1287o.show(this, i2, i3);
    }

    private G.R getEcuConfiguration() {
        String ecuConfigurationName = getEcuConfigurationName();
        if (ecuConfigurationName == null || ecuConfigurationName.isEmpty()) {
            ecuConfigurationName = G.T.a().c().c();
        }
        return G.T.a().c(ecuConfigurationName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void menuClicked(String str) {
        G.R ecuConfiguration = getEcuConfiguration();
        C0088bu c0088buC = ecuConfiguration.e().c(str);
        closeSettingsPanel();
        if (c0088buC != null) {
            this.f9276c = new C1324bf(ecuConfiguration, c0088buC);
            this.f9277d.setViewportView(this.f9276c);
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isComponentPaintedAt(int i2, int i3) {
        return i2 >= 0 && i2 < getWidth() && i3 >= 0 && i3 < getHeight();
    }

    private void closeSettingsPanel() {
        if (this.f9276c != null) {
            this.f9276c.close();
        }
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

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void subscribeToOutput() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void unsubscribeToOutput() {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void paintBackground(Graphics graphics) {
        super.paint(graphics);
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean requiresBackgroundRepaint() {
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void updateGauge(Graphics graphics) {
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public Area areaPainted() {
        return new Area(new Rectangle2D.Double(0.0d, 0.0d, getWidth(), getHeight()));
    }
}
