package org.icepdf.core.pobjects;

import org.icepdf.core.pobjects.fonts.FontFile;
import org.icepdf.core.pobjects.security.SecurityManager;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/StringObject.class */
public interface StringObject {
    String toString();

    StringBuilder getLiteralStringBuffer();

    String getLiteralString();

    StringBuilder getHexStringBuffer();

    String getHexString();

    int getUnsignedInt(int i2, int i3);

    StringBuilder getLiteralStringBuffer(int i2, FontFile fontFile);

    int getLength();

    void setReference(Reference reference);

    Reference getReference();

    String getDecryptedLiteralString(SecurityManager securityManager);
}
