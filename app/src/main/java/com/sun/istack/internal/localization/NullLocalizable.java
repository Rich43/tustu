package com.sun.istack.internal.localization;

/* loaded from: rt.jar:com/sun/istack/internal/localization/NullLocalizable.class */
public final class NullLocalizable implements Localizable {
    private final String msg;

    public NullLocalizable(String msg) {
        if (msg == null) {
            throw new IllegalArgumentException();
        }
        this.msg = msg;
    }

    @Override // com.sun.istack.internal.localization.Localizable
    public String getKey() {
        return Localizable.NOT_LOCALIZABLE;
    }

    @Override // com.sun.istack.internal.localization.Localizable
    public Object[] getArguments() {
        return new Object[]{this.msg};
    }

    @Override // com.sun.istack.internal.localization.Localizable
    public String getResourceBundleName() {
        return "";
    }
}
