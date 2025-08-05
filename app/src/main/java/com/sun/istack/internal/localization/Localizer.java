package com.sun.istack.internal.localization;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/istack/internal/localization/Localizer.class */
public class Localizer {
    private final Locale _locale;
    private final HashMap _resourceBundles;

    public Localizer() {
        this(Locale.getDefault());
    }

    public Localizer(Locale l2) {
        this._locale = l2;
        this._resourceBundles = new HashMap();
    }

    public Locale getLocale() {
        return this._locale;
    }

    public String localize(Localizable l2) {
        String msg;
        String key = l2.getKey();
        if (key == Localizable.NOT_LOCALIZABLE) {
            return (String) l2.getArguments()[0];
        }
        String bundlename = l2.getResourceBundleName();
        try {
            ResourceBundle bundle = (ResourceBundle) this._resourceBundles.get(bundlename);
            if (bundle == null) {
                try {
                    bundle = ResourceBundle.getBundle(bundlename, this._locale);
                } catch (MissingResourceException e2) {
                    int i2 = bundlename.lastIndexOf(46);
                    if (i2 != -1) {
                        String alternateBundleName = bundlename.substring(i2 + 1);
                        try {
                            bundle = ResourceBundle.getBundle(alternateBundleName, this._locale);
                        } catch (MissingResourceException e3) {
                            try {
                                bundle = ResourceBundle.getBundle(bundlename, this._locale, Thread.currentThread().getContextClassLoader());
                            } catch (MissingResourceException e4) {
                                return getDefaultMessage(l2);
                            }
                        }
                    }
                }
                this._resourceBundles.put(bundlename, bundle);
            }
            if (bundle == null) {
                return getDefaultMessage(l2);
            }
            if (key == null) {
                key = "undefined";
            }
            try {
                msg = bundle.getString(key);
            } catch (MissingResourceException e5) {
                msg = bundle.getString("undefined");
            }
            Object[] args = l2.getArguments();
            for (int i3 = 0; i3 < args.length; i3++) {
                if (args[i3] instanceof Localizable) {
                    args[i3] = localize((Localizable) args[i3]);
                }
            }
            String message = MessageFormat.format(msg, args);
            return message;
        } catch (MissingResourceException e6) {
            return getDefaultMessage(l2);
        }
    }

    private String getDefaultMessage(Localizable l2) {
        String key = l2.getKey();
        Object[] args = l2.getArguments();
        StringBuilder sb = new StringBuilder();
        sb.append("[failed to localize] ");
        sb.append(key);
        if (args != null) {
            sb.append('(');
            for (int i2 = 0; i2 < args.length; i2++) {
                if (i2 != 0) {
                    sb.append(", ");
                }
                sb.append(String.valueOf(args[i2]));
            }
            sb.append(')');
        }
        return sb.toString();
    }
}
