package com.sun.istack.internal.localization;

/* loaded from: rt.jar:com/sun/istack/internal/localization/LocalizableMessageFactory.class */
public class LocalizableMessageFactory {
    private final String _bundlename;

    public LocalizableMessageFactory(String bundlename) {
        this._bundlename = bundlename;
    }

    public Localizable getMessage(String key, Object... args) {
        return new LocalizableMessage(this._bundlename, key, args);
    }
}
