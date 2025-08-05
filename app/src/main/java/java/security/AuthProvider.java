package java.security;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;

/* loaded from: rt.jar:java/security/AuthProvider.class */
public abstract class AuthProvider extends Provider {
    private static final long serialVersionUID = 4197859053084546461L;

    public abstract void login(Subject subject, CallbackHandler callbackHandler) throws LoginException;

    public abstract void logout() throws LoginException;

    public abstract void setCallbackHandler(CallbackHandler callbackHandler);

    protected AuthProvider(String str, double d2, String str2) {
        super(str, d2, str2);
    }
}
