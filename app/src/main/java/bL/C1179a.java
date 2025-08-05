package bl;

import G.C0113cs;
import G.InterfaceC0109co;
import G.R;
import G.S;
import G.T;
import G.aH;
import com.efiAnalytics.plugin.ecu.ControllerException;
import com.efiAnalytics.plugin.ecu.OutputChannel;
import com.efiAnalytics.plugin.ecu.OutputChannelClient;
import com.efiAnalytics.plugin.ecu.servers.OutputChannelServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* renamed from: bl.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/a.class */
public class C1179a implements S, OutputChannelServer {

    /* renamed from: a, reason: collision with root package name */
    Map f8236a = new HashMap();

    @Override // com.efiAnalytics.plugin.ecu.servers.OutputChannelServer
    public void subscribe(String str, String str2, OutputChannelClient outputChannelClient) throws ControllerException {
        try {
            List listA = a(str);
            C1180b c1180b = new C1180b(this, outputChannelClient);
            C0113cs.a().a(str, str2, c1180b);
            listA.add(c1180b);
        } catch (V.a e2) {
            throw new ControllerException(e2.getMessage());
        }
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.OutputChannelServer
    public void unsubscribeConfiguration(String str) {
        Iterator it = a(str).iterator();
        while (it.hasNext()) {
            C0113cs.a().a((InterfaceC0109co) it.next());
            it.remove();
        }
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.OutputChannelServer
    public void unsubscribe(OutputChannelClient outputChannelClient) {
        C1180b c1180bA = a(outputChannelClient);
        if (c1180bA != null) {
            C0113cs.a().a(c1180bA);
            a(c1180bA);
        }
    }

    private C1180b a(OutputChannelClient outputChannelClient) {
        Iterator it = this.f8236a.values().iterator();
        while (it.hasNext()) {
            for (C1180b c1180b : (List) it.next()) {
                if (c1180b.a().equals(outputChannelClient)) {
                    return c1180b;
                }
            }
        }
        return null;
    }

    private void a(C1180b c1180b) {
        Iterator it = this.f8236a.values().iterator();
        while (it.hasNext()) {
            Iterator it2 = ((List) it.next()).iterator();
            while (it2.hasNext()) {
                if (((C1180b) it2.next()).equals(c1180b)) {
                    it2.remove();
                }
            }
        }
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.OutputChannelServer
    public String[] getOutputChannels(String str) throws ControllerException {
        R rC = T.a().c(str);
        if (rC == null) {
            throw new ControllerException("Controller Not Found for configuration: " + str);
        }
        return rC.s();
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.OutputChannelServer
    public OutputChannel getOutputChannel(String str, String str2) throws ControllerException {
        R rC = T.a().c(str);
        if (rC == null) {
            throw new ControllerException("Controller Not Found for configuration: " + str);
        }
        aH aHVarG = rC.g(str2);
        if (aHVarG == null) {
            throw new ControllerException("OutputChannel " + str2 + " Not Found in configuration: " + str);
        }
        OutputChannel outputChannel = new OutputChannel();
        outputChannel.setFormula(aHVarG.k());
        outputChannel.setMaxValue(aHVarG.m());
        outputChannel.setMinValue(aHVarG.n());
        outputChannel.setUnits(aHVarG.e());
        outputChannel.setName(str2);
        return outputChannel;
    }

    private List a(String str) {
        List arrayList = (List) this.f8236a.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f8236a.put(str, arrayList);
        }
        return arrayList;
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        unsubscribeConfiguration(r2.c());
    }

    @Override // G.S
    public void c(R r2) {
    }
}
