package bl;

import G.C0126i;
import G.R;
import G.T;
import G.aI;
import com.efiAnalytics.plugin.ecu.MathException;
import com.efiAnalytics.plugin.ecu.servers.MathExpressionEvaluator;

/* renamed from: bl.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bl/n.class */
final class C1192n implements MathExpressionEvaluator {
    C1192n() {
    }

    @Override // com.efiAnalytics.plugin.ecu.servers.MathExpressionEvaluator
    public double evaluateExpression(String str, String str2) throws MathException {
        try {
            R rC = T.a().c(str);
            if (rC == null) {
                throw new MathException("Controller " + str + " is not know. This does not appear to be a loaded controller");
            }
            return C0126i.a(str2, (aI) rC);
        } catch (Exception e2) {
            throw new MathException(e2.getMessage());
        }
    }
}
