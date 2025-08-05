package bl;

import G.R;
import G.T;
import com.efiAnalytics.plugin.ecu.ControllerException;
import com.efiAnalytics.plugin.ecu.servers.UiSettingServer;
import com.efiAnalytics.plugin.ecu.servers.UiSettingServerProvider;
import java.util.HashMap;
import java.util.Map;

/* renamed from: bl.q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/q.class */
public class C1195q implements UiSettingServerProvider {

    /* renamed from: b, reason: collision with root package name */
    private static C1195q f8267b = null;

    /* renamed from: a, reason: collision with root package name */
    Map f8268a = new HashMap();

    private C1195q() {
    }

    public static C1195q a() {
        if (f8267b == null) {
            f8267b = new C1195q();
        }
        return f8267b;
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.UiSettingServerProvider
    public UiSettingServer getUiComponentServer(String str) throws ControllerException {
        R rC = T.a().c(str);
        if (rC == null) {
            throw new ControllerException("Configuration not found for name: " + str);
        }
        C1183e c1183e = (C1183e) this.f8268a.get(str);
        if (c1183e == null) {
            c1183e = new C1183e(rC);
            this.f8268a.put(str, c1183e);
        }
        return c1183e;
    }

    public void b() {
        this.f8268a.clear();
    }
}
