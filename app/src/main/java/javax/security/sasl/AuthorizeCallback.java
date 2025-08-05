package javax.security.sasl;

import java.io.Serializable;
import javax.security.auth.callback.Callback;

/* loaded from: rt.jar:javax/security/sasl/AuthorizeCallback.class */
public class AuthorizeCallback implements Callback, Serializable {
    private String authenticationID;
    private String authorizationID;
    private String authorizedID;
    private boolean authorized;
    private static final long serialVersionUID = -2353344186490470805L;

    public AuthorizeCallback(String str, String str2) {
        this.authenticationID = str;
        this.authorizationID = str2;
    }

    public String getAuthenticationID() {
        return this.authenticationID;
    }

    public String getAuthorizationID() {
        return this.authorizationID;
    }

    public boolean isAuthorized() {
        return this.authorized;
    }

    public void setAuthorized(boolean z2) {
        this.authorized = z2;
    }

    public String getAuthorizedID() {
        if (this.authorized) {
            return this.authorizedID == null ? this.authorizationID : this.authorizedID;
        }
        return null;
    }

    public void setAuthorizedID(String str) {
        this.authorizedID = str;
    }
}
