package com.efiAnalytics.apps.ts.dashboard;

import G.C0113cs;
import G.InterfaceC0109co;
import d.InterfaceC1712d;
import java.io.Serializable;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/SingleChannelDashComponent.class */
public abstract class SingleChannelDashComponent extends AbstractC1420s implements InterfaceC0109co, InterfaceC1712d, d.j, Serializable {

    /* renamed from: S, reason: collision with root package name */
    protected String f9401S = null;

    /* renamed from: a, reason: collision with root package name */
    private String f9402a = null;

    /* renamed from: b, reason: collision with root package name */
    private String f9403b = null;

    /* renamed from: c, reason: collision with root package name */
    private double f9404c = 0.0d;

    /* renamed from: d, reason: collision with root package name */
    private String f9405d = null;

    /* renamed from: f, reason: collision with root package name */
    private static String[] f9406f = {"veTuneValue", "deadValue"};

    public String getOutputChannel() {
        return this.f9401S;
    }

    public abstract void setCurrentOutputChannelValue(String str, String str2);

    public abstract void setValue(double d2);

    public abstract double getValue();

    public abstract void setCurrentOutputChannelValue(String str, double d2);

    public void setOutputChannel(String str) {
        this.f9401S = str;
        invalidatePainter();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void subscribeToOutput() {
        String strU = this.f9550e;
        if (aE.a.A() != null && (strU == null || strU.equals("") || (!strU.equals(C0113cs.f1154a) && G.T.a().c(strU) == null))) {
            strU = aE.a.A().u();
        }
        try {
            if (getOutputChannel() != null && !isOnDontSubscribeList(getOutputChannel())) {
                C0113cs c0113csA = C0113cs.a();
                c0113csA.a(this);
                c0113csA.a(strU, getOutputChannel(), this);
            }
            setInvalidState(false);
        } catch (Exception e2) {
            if (this.f9405d == null || (getOutputChannel() != null && !this.f9405d.equals(getOutputChannel()))) {
                bH.C.b("Failed to subscribe DashComp to OutputChannel " + getOutputChannel() + "\nNot found in current configuration.");
                this.f9405d = getOutputChannel();
            }
            setInvalidState(true);
            throw new V.a("Failed to subscribe DashComp to OutputChannel " + getOutputChannel() + "\nPlease set to a valid OutputChannel.");
        }
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public void unsubscribeToOutput() {
        C0113cs.a().a(this);
    }

    private boolean isOnDontSubscribeList(String str) {
        for (int i2 = 0; i2 < f9406f.length; i2++) {
            if (f9406f[i2].equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    public boolean isDirty() {
        return super.isDirty();
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.AbstractC1420s
    protected void updateLastsVals() {
        super.updateLastsVals();
        this.f9404c = getValue();
    }

    @Override // d.InterfaceC1712d
    public String getShortClickAction() {
        return this.f9402a;
    }

    @Override // d.InterfaceC1712d
    public void setShortClickAction(String str) {
        this.f9402a = str;
    }

    @Override // d.InterfaceC1712d
    public String getLongClickAction() {
        return this.f9403b;
    }

    @Override // d.InterfaceC1712d
    public void setLongClickAction(String str) {
        this.f9403b = str;
    }

    @Override // d.j
    public String getParameterValue(String str) {
        if (str.equals("outputChannelName")) {
            return getOutputChannel();
        }
        if (str.equals("ecuConfigName")) {
            return getEcuConfigurationName();
        }
        return null;
    }
}
