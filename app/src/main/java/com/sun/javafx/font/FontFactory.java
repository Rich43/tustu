package com.sun.javafx.font;

import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/javafx/font/FontFactory.class */
public interface FontFactory {
    public static final String DEFAULT_FULLNAME = "System Regular";

    PGFont createFont(String str, float f2);

    PGFont createFont(String str, boolean z2, boolean z3, float f2);

    PGFont deriveFont(PGFont pGFont, boolean z2, boolean z3, float f2);

    String[] getFontFamilyNames();

    String[] getFontFullNames();

    String[] getFontFullNames(String str);

    boolean hasPermission();

    PGFont loadEmbeddedFont(String str, InputStream inputStream, float f2, boolean z2);

    PGFont loadEmbeddedFont(String str, String str2, float f2, boolean z2);

    boolean isPlatformFont(String str);
}
