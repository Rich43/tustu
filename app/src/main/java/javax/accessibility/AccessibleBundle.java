package javax.accessibility;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: rt.jar:javax/accessibility/AccessibleBundle.class */
public abstract class AccessibleBundle {
    private static Hashtable table = new Hashtable();
    private final String defaultResourceBundleName = "com.sun.accessibility.internal.resources.accessibility";
    protected String key = null;

    protected String toDisplayString(String str, Locale locale) {
        Object obj;
        loadResourceBundle(str, locale);
        Object obj2 = table.get(locale);
        if (obj2 != null && (obj2 instanceof Hashtable) && (obj = ((Hashtable) obj2).get(this.key)) != null && (obj instanceof String)) {
            return (String) obj;
        }
        return this.key;
    }

    public String toDisplayString(Locale locale) {
        return toDisplayString("com.sun.accessibility.internal.resources.accessibility", locale);
    }

    public String toDisplayString() {
        return toDisplayString(Locale.getDefault());
    }

    public String toString() {
        return toDisplayString();
    }

    private void loadResourceBundle(String str, Locale locale) {
        if (!table.contains(locale)) {
            try {
                Hashtable hashtable = new Hashtable();
                ResourceBundle bundle = ResourceBundle.getBundle(str, locale);
                Enumeration<String> keys = bundle.getKeys();
                while (keys.hasMoreElements()) {
                    String strNextElement = keys.nextElement();
                    hashtable.put(strNextElement, bundle.getObject(strNextElement));
                }
                table.put(locale, hashtable);
            } catch (MissingResourceException e2) {
                System.err.println("loadResourceBundle: " + ((Object) e2));
            }
        }
    }
}
