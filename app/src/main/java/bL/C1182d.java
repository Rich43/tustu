package bl;

import G.aN;
import com.efiAnalytics.plugin.ecu.ControllerParameterChangeListener;

/* renamed from: bl.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/d.class */
class C1182d implements aN {

    /* renamed from: b, reason: collision with root package name */
    private ControllerParameterChangeListener f8240b = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1181c f8241a;

    C1182d(C1181c c1181c, ControllerParameterChangeListener controllerParameterChangeListener) {
        this.f8241a = c1181c;
        a(controllerParameterChangeListener);
    }

    @Override // G.aN
    public void a(String str, String str2) {
        a().parameterValueChanged(str2);
    }

    public ControllerParameterChangeListener a() {
        return this.f8240b;
    }

    public void a(ControllerParameterChangeListener controllerParameterChangeListener) {
        this.f8240b = controllerParameterChangeListener;
    }
}
