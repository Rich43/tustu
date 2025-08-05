package bl;

import G.T;
import com.efiAnalytics.plugin.ecu.servers.EcuConfigurationNameServer;

/* renamed from: bl.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/m.class */
final class C1191m implements EcuConfigurationNameServer {
    C1191m() {
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.EcuConfigurationNameServer
    public String[] getAllConfigurationNames() {
        return T.a().d();
    }
}
