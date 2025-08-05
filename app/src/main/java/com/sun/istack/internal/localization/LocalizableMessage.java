package com.sun.istack.internal.localization;

import java.util.Arrays;

/* loaded from: rt.jar:com/sun/istack/internal/localization/LocalizableMessage.class */
public final class LocalizableMessage implements Localizable {
    private final String _bundlename;
    private final String _key;
    private final Object[] _args;

    public LocalizableMessage(String bundlename, String key, Object... args) {
        this._bundlename = bundlename;
        this._key = key;
        this._args = args == null ? new Object[0] : args;
    }

    @Override // com.sun.istack.internal.localization.Localizable
    public String getKey() {
        return this._key;
    }

    @Override // com.sun.istack.internal.localization.Localizable
    public Object[] getArguments() {
        return Arrays.copyOf(this._args, this._args.length);
    }

    @Override // com.sun.istack.internal.localization.Localizable
    public String getResourceBundleName() {
        return this._bundlename;
    }
}
