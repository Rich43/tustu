package javax.security.auth.callback;

import java.io.IOException;

/* loaded from: rt.jar:javax/security/auth/callback/CallbackHandler.class */
public interface CallbackHandler {
    void handle(Callback[] callbackArr) throws UnsupportedCallbackException, IOException;
}
