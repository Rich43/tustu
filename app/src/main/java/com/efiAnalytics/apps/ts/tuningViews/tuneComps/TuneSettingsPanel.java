package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import G.C0088bu;
import G.R;
import G.T;
import G.bC;
import G.bL;
import bt.C1287O;
import bt.C1324bf;
import bt.C1346e;
import com.efiAnalytics.apps.ts.tuningViews.C1435h;
import com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javafx.fxml.FXMLLoader;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/TuneSettingsPanel.class */
public class TuneSettingsPanel extends TuneViewComponent implements InterfaceC1565bc {

    /* renamed from: c, reason: collision with root package name */
    JPanel f9842c = new JPanel();

    /* renamed from: d, reason: collision with root package name */
    JLabel f9843d = new JLabel();

    /* renamed from: e, reason: collision with root package name */
    C1324bf f9844e = null;

    /* renamed from: f, reason: collision with root package name */
    JScrollPane f9845f = new JScrollPane();

    /* renamed from: g, reason: collision with root package name */
    private String f9846g = null;

    /* renamed from: h, reason: collision with root package name */
    private boolean f9847h = false;

    public TuneSettingsPanel() {
        setLayout(new C1435h(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        add(jPanel);
        this.f9842c.setLayout(new BorderLayout());
        this.f9842c.add(BorderLayout.CENTER, this.f9843d);
        JButton jButton = new JButton(FXMLLoader.CONTROLLER_METHOD_PREFIX);
        jButton.setPreferredSize(new Dimension(eJ.a(15), eJ.a(15)));
        super.addEditComponent(jButton);
        jButton.setToolTipText(C1818g.b("Click to select Settings"));
        jButton.addActionListener(new j(this));
        jButton.setFocusable(false);
        this.f9842c.add("East", jButton);
        jPanel.add("North", this.f9842c);
        jPanel.add(BorderLayout.CENTER, this.f9845f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSelectPopup(int i2, int i3) {
        C1287O c1287o = new C1287O(getEcuConfiguration(), -1);
        c1287o.a(new k(this));
        this.f9842c.add(c1287o);
        c1287o.show(this, i2, i3);
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void setEcuConfigurationName(String str) {
        String ecuConfigurationName = getEcuConfigurationName();
        super.setEcuConfigurationName(str);
        if (ecuConfigurationName.equals(str)) {
            return;
        }
        updateSelectedPanel();
    }

    private R getEcuConfiguration() {
        String ecuConfigurationName = getEcuConfigurationName();
        if (ecuConfigurationName == null || ecuConfigurationName.isEmpty()) {
            ecuConfigurationName = T.a().c().c();
        }
        return T.a().c(ecuConfigurationName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelectedPanel() {
        if (getSettingPanelName() == null) {
            closeSettingsPanel();
            return;
        }
        R ecuConfiguration = getEcuConfiguration();
        C0088bu c0088buC = ecuConfiguration.e().c(getSettingPanelName());
        closeSettingsPanel();
        if (c0088buC == null && getSettingPanelName().startsWith("std_")) {
            c0088buC = new bC();
            c0088buC.v(getSettingPanelName());
            ((bC) c0088buC).a(bL.k(ecuConfiguration, getSettingPanelName()));
            ecuConfiguration.e().a(c0088buC);
        }
        if (c0088buC != null) {
            this.f9844e = new C1324bf(ecuConfiguration, c0088buC);
            this.f9845f.setViewportView(this.f9844e);
            C1346e.a().a(ecuConfiguration.c(), this.f9844e);
        }
    }

    private void closeSettingsPanel() {
        if (this.f9844e != null) {
            C1346e.a().b(getEcuConfiguration().c(), this.f9844e);
            this.f9844e.close();
            this.f9845f.setViewportView(new JPanel());
        }
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        closeSettingsPanel();
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void initializeComponents() {
        updateSelectedPanel();
    }

    public String getSettingPanelName() {
        return this.f9846g;
    }

    public void setSettingPanelName(String str) {
        this.f9846g = str;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public boolean isDirty() {
        return this.f9847h;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void setClean(boolean z2) {
        this.f9847h = !z2;
    }
}
