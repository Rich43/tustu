package javax.security.sasl;

import java.util.Map;
import javax.security.auth.callback.CallbackHandler;

/* loaded from: rt.jar:javax/security/sasl/SaslClientFactory.class */
public interface SaslClientFactory {
    SaslClient createSaslClient(String[] strArr, String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException;

    String[] getMechanismNames(Map<String, ?> map);
}
