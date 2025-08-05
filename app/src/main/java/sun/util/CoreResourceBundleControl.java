package sun.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/* loaded from: rt.jar:sun/util/CoreResourceBundleControl.class */
public class CoreResourceBundleControl extends ResourceBundle.Control {
    private final Collection<Locale> excludedJDKLocales = Arrays.asList(Locale.GERMANY, Locale.ENGLISH, Locale.US, new Locale("es", "ES"), Locale.FRANCE, Locale.ITALY, Locale.JAPAN, Locale.KOREA, new Locale("sv", "SE"), Locale.CHINESE);
    private static CoreResourceBundleControl resourceBundleControlInstance = new CoreResourceBundleControl();

    protected CoreResourceBundleControl() {
    }

    public static CoreResourceBundleControl getRBControlInstance() {
        return resourceBundleControlInstance;
    }

    public static CoreResourceBundleControl getRBControlInstance(String str) {
        if (str.startsWith("com.sun.") || str.startsWith("java.") || str.startsWith("javax.") || str.startsWith("sun.")) {
            return resourceBundleControlInstance;
        }
        return null;
    }

    @Override // java.util.ResourceBundle.Control
    public List<Locale> getCandidateLocales(String str, Locale locale) {
        List<Locale> candidateLocales = super.getCandidateLocales(str, locale);
        candidateLocales.removeAll(this.excludedJDKLocales);
        return candidateLocales;
    }

    @Override // java.util.ResourceBundle.Control
    public long getTimeToLive(String str, Locale locale) {
        return -1L;
    }
}
