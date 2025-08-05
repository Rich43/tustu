package com.sun.security.sasl.gsskerb;

import com.sun.security.sasl.util.PolicyUtils;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslClientFactory;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import javax.security.sasl.SaslServerFactory;

/* loaded from: rt.jar:com/sun/security/sasl/gsskerb/FactoryImpl.class */
public final class FactoryImpl implements SaslClientFactory, SaslServerFactory {
    private static final String[] myMechs = {"GSSAPI"};
    private static final int[] mechPolicies = {19};
    private static final int GSS_KERB_V5 = 0;

    @Override // javax.security.sasl.SaslClientFactory
    public SaslClient createSaslClient(String[] strArr, String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        for (String str4 : strArr) {
            if (str4.equals(myMechs[0]) && PolicyUtils.checkPolicy(mechPolicies[0], map)) {
                return new GssKrb5Client(str, str2, str3, map, callbackHandler);
            }
        }
        return null;
    }

    @Override // javax.security.sasl.SaslServerFactory
    public SaslServer createSaslServer(String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        if (str.equals(myMechs[0]) && PolicyUtils.checkPolicy(mechPolicies[0], map)) {
            if (callbackHandler == null) {
                throw new SaslException("Callback handler with support for AuthorizeCallback required");
            }
            return new GssKrb5Server(str2, str3, map, callbackHandler);
        }
        return null;
    }

    @Override // javax.security.sasl.SaslClientFactory
    public String[] getMechanismNames(Map<String, ?> map) {
        return PolicyUtils.filterMechs(myMechs, mechPolicies, map);
    }
}
