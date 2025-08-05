package com.sun.security.sasl;

import com.sun.security.sasl.util.PolicyUtils;
import java.io.IOException;
import java.util.Map;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslClientFactory;
import javax.security.sasl.SaslException;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/security/sasl/ClientFactoryImpl.class */
public final class ClientFactoryImpl implements SaslClientFactory {
    private static final String[] myMechs = {"EXTERNAL", "CRAM-MD5", "PLAIN"};
    private static final int[] mechPolicies = {7, 17, 16};
    private static final int EXTERNAL = 0;
    private static final int CRAMMD5 = 1;
    private static final int PLAIN = 2;

    @Override // javax.security.sasl.SaslClientFactory
    public SaslClient createSaslClient(String[] strArr, String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2].equals(myMechs[0]) && PolicyUtils.checkPolicy(mechPolicies[0], map)) {
                return new ExternalClient(str);
            }
            if (strArr[i2].equals(myMechs[1]) && PolicyUtils.checkPolicy(mechPolicies[1], map)) {
                Object[] userInfo = getUserInfo("CRAM-MD5", str, callbackHandler);
                return new CramMD5Client((String) userInfo[0], (byte[]) userInfo[1]);
            }
            if (strArr[i2].equals(myMechs[2]) && PolicyUtils.checkPolicy(mechPolicies[2], map)) {
                Object[] userInfo2 = getUserInfo("PLAIN", str, callbackHandler);
                return new PlainClient(str, (String) userInfo2[0], (byte[]) userInfo2[1]);
            }
        }
        return null;
    }

    @Override // javax.security.sasl.SaslClientFactory
    public String[] getMechanismNames(Map<String, ?> map) {
        return PolicyUtils.filterMechs(myMechs, mechPolicies, map);
    }

    private Object[] getUserInfo(String str, String str2, CallbackHandler callbackHandler) throws SaslException {
        byte[] bytes;
        if (callbackHandler == null) {
            throw new SaslException("Callback handler to get username/password required");
        }
        try {
            String str3 = str + " authentication id: ";
            String str4 = str + " password: ";
            NameCallback nameCallback = str2 == null ? new NameCallback(str3) : new NameCallback(str3, str2);
            PasswordCallback passwordCallback = new PasswordCallback(str4, false);
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            char[] password = passwordCallback.getPassword();
            if (password != null) {
                bytes = new String(password).getBytes(InternalZipConstants.CHARSET_UTF8);
                passwordCallback.clearPassword();
            } else {
                bytes = null;
            }
            return new Object[]{nameCallback.getName(), bytes};
        } catch (IOException e2) {
            throw new SaslException("Cannot get password", e2);
        } catch (UnsupportedCallbackException e3) {
            throw new SaslException("Cannot get userid/password", e3);
        }
    }
}
