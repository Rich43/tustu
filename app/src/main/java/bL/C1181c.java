package bl;

import G.R;
import G.T;
import G.aM;
import G.aR;
import com.efiAnalytics.plugin.ecu.ControllerException;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.efiAnalytics.plugin.ecu.ControllerParameterChangeListener;
import com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer;
import java.util.HashMap;

/* renamed from: bl.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/c.class */
public class C1181c implements ControllerParameterServer {

    /* renamed from: a, reason: collision with root package name */
    HashMap f8239a = new HashMap();

    @Override // com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer
    public void subscribe(String str, String str2, ControllerParameterChangeListener controllerParameterChangeListener) throws ControllerException {
        try {
            R rC = T.a().c(str);
            C1182d c1182d = new C1182d(this, controllerParameterChangeListener);
            this.f8239a.put(controllerParameterChangeListener, c1182d);
            aR.a().a(rC.c(), str2, c1182d);
        } catch (Exception e2) {
            throw new ControllerException(e2.getMessage());
        }
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer
    public void unsubscribe(ControllerParameterChangeListener controllerParameterChangeListener) {
        aR.a().a((C1182d) this.f8239a.get(controllerParameterChangeListener));
        this.f8239a.remove(controllerParameterChangeListener);
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer
    public String[] getParameterNames(String str) {
        R rC = T.a().c(str);
        if (rC == null) {
            return null;
        }
        return rC.k();
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer
    public ControllerParameter getControllerParameter(String str, String str2) throws ControllerException {
        R rC = T.a().c(str);
        if (rC == null) {
            throw new ControllerException("No Controller Configuration loaded");
        }
        aM aMVarC = rC.c(str2);
        if (aMVarC == null) {
            throw new ControllerException("Controller Parameter not found in working controller configuration.\n" + rC.c());
        }
        ControllerParameter controllerParameter = new ControllerParameter();
        controllerParameter.setUnits(aMVarC.o());
        controllerParameter.setDecimalPlaces(aMVarC.u());
        controllerParameter.setMax(aMVarC.r());
        controllerParameter.setMin(aMVarC.q());
        controllerParameter.setOptionDescriptions(aMVarC.x());
        controllerParameter.setParamClass(aMVarC.i());
        try {
            if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                controllerParameter.setStringValue(aMVarC.f(rC.p()));
            } else if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_SCALAR)) {
                controllerParameter.setScalarValue(aMVarC.j(rC.p()));
            } else if (aMVarC.i().equals(ControllerParameter.PARAM_CLASS_ARRAY)) {
                controllerParameter.setArrayValues(aMVarC.i(rC.p()));
            }
            return controllerParameter;
        } catch (V.g e2) {
            throw new ControllerException(e2.getMessage());
        }
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer
    public void updateParameter(String str, String str2, double d2) throws ControllerException {
        try {
            R rC = T.a().c(str);
            rC.c(str2).a(rC.p(), d2);
        } catch (Exception e2) {
            throw new ControllerException(e2.getMessage());
        }
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer
    public void updateParameter(String str, String str2, double[][] dArr) throws ControllerException {
        try {
            R rC = T.a().c(str);
            rC.c(str2).a(rC.p(), dArr);
        } catch (Exception e2) {
            throw new ControllerException(e2.getMessage());
        }
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer
    public void updateParameter(String str, String str2, String str3) throws ControllerException {
        try {
            R rC = T.a().c(str);
            rC.c(str2).a(rC.p(), str3);
        } catch (Exception e2) {
            throw new ControllerException(e2.getMessage());
        }
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.ControllerParameterServer
    public void burnData(String str) throws ControllerException {
        if (T.a().c(str) == null) {
            throw new ControllerException("No Controller found by name of: " + str);
        }
    }
}
