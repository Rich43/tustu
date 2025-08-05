package com.sun.istack.internal.localization;

/* loaded from: rt.jar:com/sun/istack/internal/localization/Localizable.class */
public interface Localizable {
    public static final String NOT_LOCALIZABLE = "��";

    String getKey();

    Object[] getArguments();

    String getResourceBundleName();
}
