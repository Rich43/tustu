package bl;

import G.R;
import G.T;
import com.efiAnalytics.plugin.ecu.ControllerException;
import com.efiAnalytics.plugin.ecu.servers.BurnExecutor;

/* renamed from: bl.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/f.class */
public class C1184f implements BurnExecutor {
    @Override // com.efiAnalytics.plugin.ecu.servers.BurnExecutor
    public void burnData(String str) throws ControllerException {
        R rC = T.a().c(str);
        if (rC == null) {
            throw new ControllerException("Burn Failed, Configuration name is not valid. Has it been unloaded?");
        }
        rC.I();
    }
}
