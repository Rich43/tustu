package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import G.R;
import G.T;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1421t;
import com.efiAnalytics.apps.ts.tuningViews.C1435h;
import com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent;
import com.efiAnalytics.tuningwidgets.panels.SelectableTablePanel;
import com.efiAnalytics.tuningwidgets.panels.ay;
import java.awt.BorderLayout;
import java.util.Properties;
import javax.swing.BorderFactory;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/SelectableTable.class */
public class SelectableTable extends TuneViewComponent implements InterfaceC1421t, ay {

    /* renamed from: c, reason: collision with root package name */
    SelectableTablePanel f9820c = null;

    /* renamed from: d, reason: collision with root package name */
    R f9821d = null;

    /* renamed from: f, reason: collision with root package name */
    private String f9822f = null;

    /* renamed from: e, reason: collision with root package name */
    f f9823e = new f(this);

    /* renamed from: g, reason: collision with root package name */
    private boolean f9824g = false;

    public SelectableTable() {
        setLayout(new C1435h(this));
        super.setDoubleBuffered(true);
    }

    private void addTableSelector() {
        if (this.f9820c == null) {
            if (this.f9821d == null) {
                setEcuConfigurationName(getEcuConfigurationName());
            }
            this.f9820c = new SelectableTablePanel(this.f9821d);
            this.f9820c.a(this.f9823e);
            this.f9820c.a(false);
            this.f9820c.a(3);
            this.f9820c.a((ay) this);
            if (this.f9822f != null) {
                this.f9820c.a(this.f9822f);
            }
            if (this.f9822f != null) {
                setSelectedTableName(this.f9822f);
            }
            this.f9820c.setBorder(BorderFactory.createEtchedBorder());
            add(BorderLayout.CENTER, this.f9820c);
            super.addEditComponent(this.f9820c.a());
            return;
        }
        super.removeEditComponent(this.f9820c.a());
        super.remove(this.f9820c);
        this.f9820c = new SelectableTablePanel(this.f9821d);
        this.f9820c.a(this.f9823e);
        this.f9820c.a(false);
        this.f9820c.a(3);
        this.f9820c.a((ay) this);
        if (this.f9822f != null) {
            this.f9820c.a(this.f9822f);
        }
        if (this.f9822f != null) {
            setSelectedTableName(this.f9822f);
        }
        this.f9820c.setBorder(BorderFactory.createEtchedBorder());
        add(BorderLayout.CENTER, this.f9820c);
        super.addEditComponent(this.f9820c.a());
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void setEcuConfigurationName(String str) {
        super.setEcuConfigurationName(str);
        if (str == null || str.isEmpty()) {
            this.f9821d = T.a().c();
        } else {
            this.f9821d = T.a().c(str);
        }
        if (this.f9820c != null) {
            addTableSelector();
        }
    }

    public String getSelectedTableName() {
        return this.f9822f;
    }

    public void setSelectedTableName(String str) {
        this.f9822f = str;
    }

    public void setSavedProperties(Properties properties) {
        this.f9823e.a(properties);
    }

    public Properties getSavedProperties() {
        return this.f9823e.a();
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.ay
    public void panelSelectionChanged(String str, String str2) {
        this.f9822f = str2;
        this.f9824g = true;
        super.setEcuConfigurationName(this.f9757b);
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void initializeComponents() {
        addTableSelector();
        setClean(true);
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        if (this.f9820c != null) {
            this.f9820c.close();
        }
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public boolean isDirty() {
        return this.f9824g;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void setClean(boolean z2) {
        this.f9824g = !z2;
    }

    @Override // com.efiAnalytics.apps.ts.tuningViews.TuneViewComponent
    public void enableEditMode(boolean z2) {
        this.f9820c.b(z2);
        super.enableEditMode(z2);
    }
}
