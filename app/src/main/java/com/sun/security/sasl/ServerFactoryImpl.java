package com.sun.security.sasl;

import com.sun.security.sasl.util.PolicyUtils;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import javax.security.sasl.SaslServerFactory;

/* loaded from: rt.jar:com/sun/security/sasl/ServerFactoryImpl.class */
public final class ServerFactoryImpl implements SaslServerFactory {
    private static final String[] myMechs = {"CRAM-MD5"};
    private static final int[] mechPolicies = {17};
    private static final int CRAMMD5 = 0;

    @Override // javax.security.sasl.SaslServerFactory
    public SaslServer createSaslServer(String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        if (str.equals(myMechs[0]) && PolicyUtils.checkPolicy(mechPolicies[0], map)) {
            if (callbackHandler == null) {
                throw new SaslException("Callback handler with support for AuthorizeCallback required");
            }
            return new CramMD5Server(str2, str3, map, callbackHandler);
        }
        return null;
    }

    @Override // javax.security.sasl.SaslServerFactory
    public String[] getMechanismNames(Map<String, ?> map) {
        return PolicyUtils.filterMechs(myMechs, mechPolicies, map);
    }
}
