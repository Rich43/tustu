package sun.net.www.protocol.http.spnego;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Arrays;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import sun.net.www.protocol.http.HttpCallerInfo;
import sun.security.jgss.LoginConfigImpl;

/* loaded from: rt.jar:sun/net/www/protocol/http/spnego/NegotiateCallbackHandler.class */
public class NegotiateCallbackHandler implements CallbackHandler {
    private String username;
    private char[] password;
    private boolean answered;
    private final HttpCallerInfo hci;

    public NegotiateCallbackHandler(HttpCallerInfo httpCallerInfo) {
        this.hci = httpCallerInfo;
    }

    private void getAnswer() {
        PasswordAuthentication passwordAuthenticationRequestPasswordAuthentication;
        if (!this.answered) {
            this.answered = true;
            if (LoginConfigImpl.HTTP_USE_GLOBAL_CREDS && (passwordAuthenticationRequestPasswordAuthentication = Authenticator.requestPasswordAuthentication(this.hci.host, this.hci.addr, this.hci.port, this.hci.protocol, this.hci.prompt, this.hci.scheme, this.hci.url, this.hci.authType)) != null) {
                this.username = passwordAuthenticationRequestPasswordAuthentication.getUserName();
                this.password = passwordAuthenticationRequestPasswordAuthentication.getPassword();
            }
        }
    }

    @Override // javax.security.auth.callback.CallbackHandler
    public void handle(Callback[] callbackArr) throws UnsupportedCallbackException, IOException {
        for (Callback callback : callbackArr) {
            if (callback instanceof NameCallback) {
                getAnswer();
                ((NameCallback) callback).setName(this.username);
            } else if (callback instanceof PasswordCallback) {
                getAnswer();
                ((PasswordCallback) callback).setPassword(this.password);
                if (this.password != null) {
                    Arrays.fill(this.password, ' ');
                }
            } else {
                throw new UnsupportedCallbackException(callback, "Call back not supported");
            }
        }
    }
}
