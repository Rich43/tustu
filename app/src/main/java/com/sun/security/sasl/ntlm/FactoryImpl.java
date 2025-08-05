package com.sun.security.sasl.ntlm;

import com.sun.security.sasl.util.PolicyUtils;
import java.util.Map;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslClientFactory;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import javax.security.sasl.SaslServerFactory;

/* loaded from: rt.jar:com/sun/security/sasl/ntlm/FactoryImpl.class */
public final class FactoryImpl implements SaslClientFactory, SaslServerFactory {
    private static final String[] myMechs = {"NTLM"};
    private static final int[] mechPolicies = {17};

    @Override // javax.security.sasl.SaslClientFactory
    public SaslClient createSaslClient(String[] strArr, String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2].equals("NTLM") && PolicyUtils.checkPolicy(mechPolicies[0], map)) {
                if (callbackHandler == null) {
                    throw new SaslException("Callback handler with support for RealmCallback, NameCallback, and PasswordCallback required");
                }
                return new NTLMClient(strArr[i2], str, str2, str3, map, callbackHandler);
            }
        }
        return null;
    }

    @Override // javax.security.sasl.SaslServerFactory
    public SaslServer createSaslServer(String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        String str4;
        if (str.equals("NTLM") && PolicyUtils.checkPolicy(mechPolicies[0], map)) {
            if (map != null && (str4 = (String) map.get(Sasl.QOP)) != null && !str4.equals("auth")) {
                throw new SaslException("NTLM only support auth");
            }
            if (callbackHandler == null) {
                throw new SaslException("Callback handler with support for RealmCallback, NameCallback, and PasswordCallback required");
            }
            return new NTLMServer(str, str2, str3, map, callbackHandler);
        }
        return null;
    }

    @Override // javax.security.sasl.SaslClientFactory
    public String[] getMechanismNames(Map<String, ?> map) {
        return PolicyUtils.filterMechs(myMechs, mechPolicies, map);
    }
}
