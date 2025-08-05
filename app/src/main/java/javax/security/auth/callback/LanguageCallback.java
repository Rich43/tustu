package javax.security.auth.callback;

import java.io.Serializable;
import java.util.Locale;

/* loaded from: rt.jar:javax/security/auth/callback/LanguageCallback.class */
public class LanguageCallback implements Callback, Serializable {
    private static final long serialVersionUID = 2019050433478903213L;
    private Locale locale;

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale;
    }
}
