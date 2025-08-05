package org.icepdf.core.pobjects.fonts;

import org.icepdf.core.pobjects.Name;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/fonts/CMap.class */
public interface CMap {
    public static final Name TYPE = new Name("CMap");

    char toSelector(char c2);

    char toSelector(char c2, boolean z2);

    String toUnicode(char c2);

    boolean isOneByte(int i2);
}
