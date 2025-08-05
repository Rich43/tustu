package G;

import java.io.Serializable;
import java.util.List;

/* loaded from: TunerStudioMS.jar:G/bK.class */
public abstract class bK extends Q implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    protected String f853a = "basicPattern";

    public void a(String str) throws V.g {
        if (str == null || !(str.equals("fixedAngle") || str.equals("basicPattern") || str.equals("bitArrayPattern"))) {
            throw new V.g("Unrecognized TriggerWheel Pattern Class: " + str);
        }
        this.f853a = str;
    }

    @Override // G.Q
    public void u(String str) {
        bH.C.a("setActiveCondition in EcuUiTriggerWheelPattern is not used. Use the active condition when adding to TriggerWheel");
    }

    public abstract List a();
}
