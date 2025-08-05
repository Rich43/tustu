package javax.security.sasl;

import javax.security.auth.callback.TextInputCallback;

/* loaded from: rt.jar:javax/security/sasl/RealmCallback.class */
public class RealmCallback extends TextInputCallback {
    private static final long serialVersionUID = -4342673378785456908L;

    public RealmCallback(String str) {
        super(str);
    }

    public RealmCallback(String str, String str2) {
        super(str, str2);
    }
}
