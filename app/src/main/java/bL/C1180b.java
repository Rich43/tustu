package bl;

import G.InterfaceC0109co;
import com.efiAnalytics.plugin.ecu.OutputChannelClient;

/* renamed from: bl.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/b.class */
class C1180b implements InterfaceC0109co {

    /* renamed from: b, reason: collision with root package name */
    private OutputChannelClient f8237b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1179a f8238a;

    C1180b(C1179a c1179a, OutputChannelClient outputChannelClient) {
        this.f8238a = c1179a;
        this.f8237b = null;
        this.f8237b = outputChannelClient;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        a().setCurrentOutputChannelValue(str, d2);
    }

    public OutputChannelClient a() {
        return this.f8237b;
    }
}
